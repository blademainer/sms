/**
 * SMS
 */
package com.xiongyingqi.sms.event;

import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.OutboundMessage;

/**
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-16 下午6:30:41
 */
public interface MessageListener {
	/**
	 * 当有消息发生时（接收或发送）通知该方法<br>
	 * <br>2013-9-16 下午6:31:08
	 * @param message 发生该事件的消息
	 */
	public void messageEvent(Message message);
	
	/**
	 * 当发送消息失败时调用该方法
	 * <br>2013-9-16 下午6:31:46
	 * @param message 消息对象
	 * @param throwable 抛出的错误
	 */
	public void messageException(Message message, Throwable throwable);
	
}
