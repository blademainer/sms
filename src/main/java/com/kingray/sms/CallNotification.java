/**
 * SMS
 */
package com.kingray.sms;

import cn.sendsms.ICallNotification;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:24:08
 */
public class CallNotification implements ICallNotification {
	public void process(String gatewayId, String callerId) {
		System.out.println(">>> 监测到有电话打入: " + gatewayId + " : " + callerId);
	}

}
