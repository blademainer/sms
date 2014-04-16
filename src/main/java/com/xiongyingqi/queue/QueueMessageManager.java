/**
 * SMS
 */
package com.xiongyingqi.queue;

import java.util.Iterator;
import java.util.List;

import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.Service.ServiceStatus;

import com.xiongyingqi.message.MessageStore;
import com.xiongyingqi.sms.event.MessageListener;
import com.xiongyingqi.sms.event.MessageListenerManager;

/**
 * 用于对队列消息的管理
 * 
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-16 下午2:04:10
 */
public class QueueMessageManager implements MessageListener {
	private QueueManager<OutboundMessage> queueManager = QueueManager
			.getInstance(OutboundMessage.class);

	private static final Object LOCK = new Object();
	private static QueueMessageManager singleton;

	private QueueMessageManager() {
		init();
	}

	public static QueueMessageManager getInstance() {
		synchronized (LOCK) {
			if (singleton == null) {
				singleton = new QueueMessageManager();
			}
		}
		return singleton;
	}

	private void init() {
		OutboundMessage[] messages = MessageStore.getInstance()
				.readOutMessages();
		for (int i = 0; i < messages.length; i++) {
			OutboundMessage outboundMessage = messages[i];
			queueManager.add(outboundMessage);
		}
		Thread waitForServiceStarted = new Thread(new Runnable() {// 等待设备启动完成
					public void run() {
						while (Service.getInstance().getServiceStatus() != ServiceStatus.STARTED) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						QueueMessageThread.startThread();
					}
				});
		waitForServiceStarted.start();
	}

	/**
	 * 将消息压入队列 <br>
	 * 2013-9-16 下午2:43:46
	 * 
	 * @param message
	 */
	public void queueMessage(OutboundMessage message) {
		MessageStore.getInstance().storeOutMessage(message);
		queueManager.add(message);
	}

	/**
	 * 将多个消息压入队列 <br>
	 * 2013-9-16 下午3:00:06
	 * 
	 * @param messages
	 */
	public void queueMessages(List<OutboundMessage> messages) {
		for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
			OutboundMessage outboundMessage = (OutboundMessage) iterator.next();
			queueMessage(outboundMessage);
		}
	}

	/**
	 * <br>
	 * 2013-9-16 下午6:32:23
	 * 
	 * @see com.xiongyingqi.sms.MessageListener#messageEvent(org.smslib.Message)
	 */
	@Override
	public void messageEvent(Message message) {
	}

	/**
	 * <br>
	 * 2013-9-16 下午6:32:23
	 * 
	 * @see com.xiongyingqi.sms.MessageListener#messageException(org.smslib.Message,
	 *      java.lang.Throwable)
	 */
	@Override
	public void messageException(Message message, Throwable throwable) {
		MessageListenerManager.getInstance().messageException(message,
				throwable);
	}

}
