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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;

import com.xiongyingqi.message.MessageStore;
import com.xiongyingqi.sms.SMSService;

/**
 * @author 瑛琪
 * @version 2013-8-1 下午5:04:34
 */
public class SmsServlet extends HttpServlet {
	public static final Logger logger = Logger.getLogger(SmsServlet.class);

	/**
	 * <br>
	 * 2013-8-1 下午5:04:46
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * <br>
	 * 2013-8-1 下午5:04:51
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String actionType = request.getParameter("actionType");
		if ("sendSMS".equals(actionType)) {
			sendMessage(request, response);
		} else if("readMessages".equals(actionType)){
			readMessages(request, response);
		} else if("readSentMessages".equals(actionType)){
			readSentMessages(request, response);
		} else if("readReceivedMessages".equals(actionType)){
			readReceivedMessages(request, response);
		}

	}

	protected void sendMessage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] phoneNumbers = request.getParameterValues("phoneNumber");
		// String phoneNumber = request.getParameter("phoneNumber");
		String content = request.getParameter("content");
		System.out.println("content =========== " + content);
		List phoneNumberList = new LinkedList();
		// phoneNumberList.addAll(phoneNumbers);
		for (int i = 0; i < phoneNumbers.length; i++) {
			String phoneNumber = phoneNumbers[i];
			StringTokenizer tokenizer = new StringTokenizer(phoneNumber, ",");
			while (tokenizer.hasMoreElements()) {
				String nextPhoneNumber = (String) tokenizer.nextElement();
				phoneNumberList.add(nextPhoneNumber);
			}
		}
		logger.info("phoneNumberList: " + phoneNumberList + "\r\n"
				+ "phoneNumberList: " + phoneNumberList);
		SMSService.sendSmses(content, phoneNumberList);
		// SMSService.sendSms(phoneNumber, content);
		replyMessage("1", response);// 成功

	}

	protected void readMessages(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ascString = request.getParameter("asc");
		Boolean asc = Boolean.parseBoolean(ascString);
		Message[] messages = MessageStore.getInstance().readMessages(asc);
		JSONArray jsonArray = JSONArray.fromObject(messages);
		replyMessage(jsonArray.toString(), response);
	}
	
	protected void readSentMessages(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ascString = request.getParameter("asc");
		Boolean asc = Boolean.parseBoolean(ascString);
		OutboundMessage[] outboundMessages = MessageStore.getInstance().readSentMessages(asc);
		JSONArray jsonArray = JSONArray.fromObject(outboundMessages);
		replyMessage(jsonArray.toString(), response);
	}
	
	protected void readReceivedMessages(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ascString = request.getParameter("asc");
		Boolean asc = Boolean.parseBoolean(ascString);
		InboundMessage[] inboundMessages = MessageStore.getInstance().readInMessages(asc);
		JSONArray jsonArray = JSONArray.fromObject(inboundMessages);
		replyMessage(jsonArray.toString(), response);
	}

	protected void replyMessage(String message, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		// response.setContentType("application/x-javascript");
		// response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");// 没有缓存，立即返回响应
		PrintWriter out = response.getWriter();
		out.print(message);// 不能用println
		// System.out.println("jsonArray ====== " + jsonArray);
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) {
		InboundMessage inboundMessage = new InboundMessage(MessageTypes.INBOUND, 1, "asfasf");
		
		JSONArray jsonArray = JSONArray.fromObject(new InboundMessage[]{inboundMessage, inboundMessage});
		System.out.println(jsonArray);
	}

}
