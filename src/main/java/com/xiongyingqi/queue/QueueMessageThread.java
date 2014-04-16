package com.xiongyingqi.queue;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.smslib.GatewayException;
import org.smslib.Message;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;

import com.xiongyingqi.message.MessageStore;
import com.xiongyingqi.sms.event.MessageListenerManager;
import com.xiongyingqi.utils.ThreadPool;
import com.xiongyingqi.utils.TimerHelper;

/**
 * 
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-16 上午9:46:29
 */
public class QueueMessageThread implements Runnable {
	public static final Logger log = Logger.getLogger(QueueMessageThread.class);
	
	private boolean shouldRun = true;
	private static final Object LOCK = new Object();
	private static Thread currentThread;
	
	@Override
	public void run() {
		QueueManager<OutboundMessage> messageQueue = QueueManager
				.getInstance(OutboundMessage.class);
		while (shouldRun) {
			OutboundMessage message = messageQueue.poll();
			try {
				sendMessage(message);
			} catch (TimeoutException e) {
				e.printStackTrace();
				log.error(e);
			} catch (GatewayException e) {
				e.printStackTrace();
				log.error(e);
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(e);
			}
		}
	}

	public void stop() {
		this.shouldRun = false;
	}

	/**
	 * 在线程池中使用代理线程执行发送消息
	 * <br>2013-9-17 上午10:05:08
	 * @param message
	 * @throws TimeoutException
	 * @throws GatewayException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendMessage(OutboundMessage message) throws TimeoutException, GatewayException, IOException, InterruptedException {
		if(message != null){
			try {// 使用代理线程来发送消息
				ThreadPool.invoke(this, getClass().getDeclaredMethod("sendMessageCallBack", Object.class, Throwable.class, Object[].class), Service.getInstance(), Service.class.getMethod("sendMessage", OutboundMessage.class), message);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
//			Service.getInstance().sendMessage(message);
		}
	}
	
	/**
	 * 代理线程的回调方法
	 * <br>2013-9-17 上午10:04:51
	 * @param result
	 * @param throwable
	 * @param parameters
	 */
	public void sendMessageCallBack(Object result, Throwable throwable, Object... parameters){
		for (int i = 0; i < parameters.length; i++) {
			Object object = parameters[i];
			Message message = (Message) object;
			log.info(message);
		}
		
		boolean sentStatus = (Boolean) result;
		MessageStore messageStore = MessageStore.getInstance();
		if (throwable != null) {
			throwable.printStackTrace();
			log.error(throwable);
			for (int i = 0; i < parameters.length; i++) {
				Object object = parameters[i];
				Message message = (Message) object;
				MessageListenerManager.getInstance().messageException(message, throwable);
			}
		} 
		if(sentStatus) {// 发送成功
			for (int i = 0; i < parameters.length; i++) {
				Object object = parameters[i];
				OutboundMessage message = (OutboundMessage) object;
				messageStore.deleteOutMessage(message);
				messageStore.storeSentMessage(message);
			}
		} else {// 发送失败，重新发送
			for (int i = 0; i < parameters.length; i++) {
				Object object = parameters[i];
				OutboundMessage message = (OutboundMessage) object;
				try {
					sendMessage(message);
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (GatewayException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 启动发送消息线程
	 * <br>2013-9-17 上午10:16:53
	 */
	public static void startThread(){
		synchronized (LOCK) {
			if(currentThread == null){
				currentThread = new Thread(new QueueMessageThread());
			}
		}
		if(!currentThread.isAlive()){
			currentThread.start();
		}
	}
	
	public static void stopThread(){
		synchronized (LOCK) {
			if(currentThread != null){
				currentThread.interrupt();
			}
		}
	}
	
}
