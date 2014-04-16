/**
 * SMS
 */
package com.xiongyingqi.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.TimeoutException;

import com.xiongyingqi.message.MessageStore;
import com.xiongyingqi.sms.event.MessageListenerManager;
import com.xiongyingqi.utils.EntityHelper;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:22:05
 */
public class InboundNotification implements IInboundMessageNotification {
	public static final Logger log = Logger.getLogger(InboundNotification.class);
	
	public void process(String gatewayId, MessageTypes msgType, String memLoc,
			int memIndex) {
		List msgList;
		if (msgType == MessageTypes.INBOUND) {
			System.out.println(">>> 监测到设备收到新的短信: " + gatewayId + " : " + memLoc
					+ " @ " + memIndex);
			try {
				// Read...
				msgList = new ArrayList();
				SMSService.getService().readMessages(msgList,
						MessageClasses.UNREAD, gatewayId);
				for (int i = 0; i < msgList.size(); i++)
					System.out.println(msgList.get(i));
				// ...and reply.
				// for (int i = 0; i < msgList.size(); i ++)
				// srv.sendMessage(new
				// OutboundMessage(msgList.get(i).getOriginator(),
				// "Got it!"), gatewayId);
			} catch (Exception e) {
				System.out.println("有异常...");
				e.printStackTrace();
			}
		} else if (msgType == MessageTypes.STATUSREPORT) {
			System.out.println(">>> 监测到设备收到短信状态报告: " + gatewayId + " : "
					+ memLoc + " @ " + memIndex);
		}
	}

	/**
	 * <br>
	 * 2013-9-13 下午1:24:38
	 * 
	 * @see org.smslib.IInboundMessageNotification#process(org.smslib.AGateway,
	 *      org.smslib.Message.MessageTypes, org.smslib.InboundMessage)
	 */
	@Override
	public void process(AGateway gateway, MessageTypes msgType,
			InboundMessage msg) {
		log.info(msg);
		EntityHelper.printDetail(msg);
		if (msgType == MessageTypes.INBOUND) {
			System.out.println(">>> 监测到设备收到新的短信: " + gateway.getGatewayId());
			MessageStore.getInstance().storeInMessage(msg);// 存储消息
			MessageListenerManager.getInstance().messageEvent(msg);
			try {
				gateway.deleteMessage(msg);// 从网关删除该消息
			} catch (TimeoutException e1) {
				e1.printStackTrace();
			} catch (GatewayException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} else if (msgType == MessageTypes.STATUSREPORT) {
			System.out.println(">>> 监测到设备收到短信状态报告: " + gateway.getGatewayId());
		}
	}

}
