/**
 * SMS
 */
package com.xiongyingqi.sms;

import org.smslib.AGateway;
import org.smslib.ICallNotification;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:24:08
 */
public class CallNotification implements ICallNotification {

	/**
	 * <br>
	 * 2013-9-13 下午12:19:30
	 * 
	 * @see org.smslib.ICallNotification#process(org.smslib.AGateway,
	 *      java.lang.String)
	 */
	@Override
	public void process(AGateway gateway, String callerId) {
		System.out.println(">>> 监测到有电话打入: " + gateway.getGatewayId() + " : "
				+ callerId);
	}

}
