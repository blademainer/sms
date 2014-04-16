/**
 * SMS
 */
package com.xiongyingqi.sms;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;

import com.xiongyingqi.sms.event.MessageListenerManager;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:21:33
 */
public class OutboundNotification implements IOutboundMessageNotification {
	public static final Logger log = Logger.getLogger(OutboundNotification.class);
	/**
	 * <br>
	 * 2013-9-13 下午12:22:30
	 * 
	 * @see org.smslib.IOutboundMessageNotification#process(org.smslib.AGateway,
	 *      org.smslib.OutboundMessage)
	 */
	@Override
	public void process(AGateway gateway, OutboundMessage msg) {
		System.out.println("发送状态: " + gateway);
		System.out.println(msg);
		log.info(msg);
		MessageListenerManager.getInstance().messageEvent(msg);
	}
}
