/**
 * SMS
 */
package com.kingray.sms;

import cn.sendsms.IOutboundMessageNotification;
import cn.sendsms.OutboundMessage;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:21:33
 */
public class OutboundNotification implements IOutboundMessageNotification {
	public void process(String gatewayId, OutboundMessage msg) {
		System.out.println("发送状态: " + gatewayId);
		System.out.println(msg);
	}
}
