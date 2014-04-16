/**
 * SMS
 */
package com.xiongyingqi.sms.event;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.OutboundMessage;

/**
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-16 下午6:33:22
 */
public class MessageListenerManager implements MessageListener {
	private static final Object LOCK = new Object();
	private static MessageListenerManager singleton;
	private Set<MessageListener> listeners = new LinkedHashSet<MessageListener>();

	public static MessageListenerManager getInstance() {
		synchronized (LOCK) {
			if (singleton == null) {
				singleton = new MessageListenerManager();
			}
		}
		return singleton;
	}

	private MessageListenerManager() {

	}

	public void addListener(MessageListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * 当有消息发生时（接收或发送）通知该方法<br>
	 * 
	 * <br>
	 * 2013-9-16 下午6:33:45
	 * 
	 * @see com.xiongyingqi.sms.event.MessageListener#messageEvent(org.smslib.Message)
	 */
	@Override
	public void messageEvent(Message message) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			MessageListener listener = (MessageListener) iterator.next();
			try {
				listener.messageEvent(message);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 当发送消息发生异常时通知本接口
	 * <br>
	 * 2013-9-16 下午6:33:45
	 * 
	 * @see com.xiongyingqi.sms.event.MessageListener#messageException(org.smslib.Message,
	 *      java.lang.Throwable)
	 */
	@Override
	public void messageException(Message message, Throwable throwable) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			MessageListener listener = (MessageListener) iterator.next();
			try {
				listener.messageException(message, throwable);
			} catch (Exception e) {
			}
		}
	}

}
