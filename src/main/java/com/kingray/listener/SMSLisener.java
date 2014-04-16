/**
 * SMS
 */
package com.kingray.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xiongyingqi.sms.SMSService;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:18:08
 */
public class SMSLisener implements ServletContextListener {

	/**
	 * <br>
	 * 2013-8-1 下午5:19:23
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		SMSService.stopService();
	}

	/**
	 * <br>
	 * 2013-8-1 下午5:19:23
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		SMSService.startService();
	}

}
