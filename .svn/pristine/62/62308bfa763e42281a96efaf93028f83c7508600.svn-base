/**
 * SMS
 */
package com.kingray.sms;

import java.util.ArrayList;
import java.util.List;

import cn.sendsms.IInboundMessageNotification;
import cn.sendsms.MessageClasses;
import cn.sendsms.MessageTypes;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:22:05
 */
public class InboundNotification implements IInboundMessageNotification {
	public void process(String gatewayId, MessageTypes msgType,
			String memLoc, int memIndex) {
		List msgList;
		if (msgType == MessageTypes.INBOUND) {
			System.out.println(">>> 监测到设备收到新的短信: " + gatewayId + " : "
					+ memLoc + " @ " + memIndex);
			try {
				// Read...
				msgList = new ArrayList();
				SMSService.getService().readMessages(msgList, MessageClasses.UNREAD, gatewayId);
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

}
