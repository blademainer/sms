/**
 * SMS
 */
package com.kingray.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sendsms.GatewayException;
import cn.sendsms.TimeoutException;

import com.kingray.sms.SMSService;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:04:34
 */
public class SmsServlet extends HttpServlet{
	/**
	 * <br>2013-8-1 下午5:04:46
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	/**
	 * <br>2013-8-1 下午5:04:51
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String actionType = request.getParameter("actionType");
		if("sendSMS".equals(actionType)){
			sendMessage(request, response);
		}
		
	}
	
	protected void sendMessage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("servlet request.getCharacterEncoding() ======= " + request.getCharacterEncoding());
		String[] phoneNumbers = request.getParameterValues("phoneNumber");
//		String phoneNumber = request.getParameter("phoneNumber");
		String content = request.getParameter("content");
		System.out.println("content =========== " + content);
		try {
			List phoneNumberList = new LinkedList();
//			phoneNumberList.addAll(phoneNumbers);
			for (int i = 0; i < phoneNumbers.length; i++) {
				String phoneNumber = phoneNumbers[i];
				StringTokenizer tokenizer = new StringTokenizer(phoneNumber, ",");
				while(tokenizer.hasMoreElements()){
					String nextPhoneNumber = (String) tokenizer.nextElement();
					phoneNumberList.add(nextPhoneNumber);
				}
			}
			SMSService.sendSmses(content, phoneNumberList);
//			SMSService.sendSms(phoneNumber, content);
			replyMessage("ok", response);
		} catch (TimeoutException e) {
			e.printStackTrace();
			replyMessage(e.toString(), response);
		} catch (GatewayException e) {
			e.printStackTrace();
			replyMessage(e.toString(), response);
		} catch (InterruptedException e) {
			e.printStackTrace();
			replyMessage(e.toString(), response);
		}
		
	}
	
	protected void replyMessage(String message, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
//		response.setContentType("application/x-javascript");
		// response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");// 没有缓存，立即返回响应
		PrintWriter out = response.getWriter();
		out.print(message);// 不能用println
		// System.out.println("jsonArray ====== " + jsonArray);
		out.flush();
		out.close();
	}
	
	
	
}
