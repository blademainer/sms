package com.kingray.sms;
// JindiSMS v3.1 
// 该程序主要用于GSM Modem短信发送示例。
// 金笛短信网 www.sendsms.cn
// 为节省您的开发时间，请配合金笛短信设备以达到最佳效果。 

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;


import cn.sendsms.*;

public class SMSService {
	
	private static Service service;
	
	public static Set<SerialModemGateway> loadDeviceResources(Logger logger){
		Set<SerialModemGateway> gateways = new HashSet<SerialModemGateway>();
		InputStream inputStream = SMSService.class.getClassLoader().getResourceAsStream("device.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			String gateWays = properties.getProperty("GateWays");
			StringTokenizer stringTokenizer = new StringTokenizer(gateWays, ",");
			while(stringTokenizer.hasMoreElements()){
				String gateWayId = (String) stringTokenizer.nextElement();
				gateWayId = gateWayId.trim();
				String comPort = properties.getProperty(gateWayId + ".comPort");
				int baudRate = Integer.parseInt(properties.getProperty(gateWayId + ".baudRate"));
				String manufacturer = properties.getProperty(gateWayId + ".manufacturer");
				String model = properties.getProperty(gateWayId + ".model");
				System.out.println(gateWayId);
				System.out.println(comPort);
				System.out.println(baudRate);
				System.out.println(model);
				SerialModemGateway gateway = new SerialModemGateway(gateWayId, comPort, baudRate, manufacturer, model, logger);
				gateways.add(gateway);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gateways;
	}
	/**
	 * 启动服务
	 * <br>2013-7-29 下午2:41:22
	 * @return
	 * @throws Exception
	 */
	public static Service startService(){
		Service service;
		OutboundNotification outboundNotification = new OutboundNotification();
		InboundNotification inboundNotification = new InboundNotification();
		CallNotification callNotification = new CallNotification();
//		System.out.println("DeviceSearcher.getPortId().getName() ========= " + DeviceSearcher.getPortId().getName());// 900E 1800
//		System.out.println("DeviceSearcher.getBaudPort() ========= " + DeviceSearcher.getBaudPort());
		service = new Service();
		
		Set<SerialModemGateway> gateways = loadDeviceResources(service.getLogger());
		for (Iterator iterator = gateways.iterator(); iterator.hasNext();) {
			SerialModemGateway gateway = (SerialModemGateway) iterator
					.next();
//			SerialModemGateway gateway = new SerialModemGateway("kingray", "COM4",
//					9600, "WAVECOM MODEM", "MULTIBAND  900E  1800", service.getLogger()); // AT+CGMM
			gateway.setInbound(true);
			gateway.setOutbound(true);
			gateway.setSimPin("0000");
			
			gateway.setOutboundNotification(outboundNotification);
			gateway.setInboundNotification(inboundNotification);
			gateway.setCallNotification(callNotification);
			
			System.out.println();
			System.out.println("service ======== " + service);
//			System.out.println("GSM Modem信息:");
//			try {
//				System.out.println("  厂家: " + gateway.getManufacturer());
//				System.out.println("  型号: " + gateway.getModel());
//				System.out.println("  序列号: " + gateway.getSerialNo());
//				System.out.println("  SIM IMSI: " + gateway.getImsi());
//				System.out.println("  信号强度: " + gateway.getSignalLevel() + "%");
//				System.out.println("  电池容量: " + gateway.getBatteryLevel() + "%");
//				System.out.println();
//			} catch (TimeoutException e) {
//				e.printStackTrace();
//			} catch (GatewayException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			service.addGateway(gateway);
			
		}
		try {
			service.startService();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SMSLibException e) {
			e.printStackTrace();
		}
		


		// 发送WAP短信.
		// OutboundWapSIMessage wapMsg = new
		// OutboundWapSIMessage("+8613601019694", new
		// URL("https://www.sendsms.cn/"), "欢迎访问金笛短信网!");
		// srv.sendMessage(wapMsg);
		// System.out.println(wapMsg);

		// 发送短信（异步发送，待发送短信进入队列，发送时不做任何等待）
		// msg = new OutboundMessage("+869999999999", "Wrong number!");
		// msg.setPriority(OutboundMessage.Priorities.LOW);
		// srv.queueMessage(msg, gateway.getGatewayId());
		// msg = new OutboundMessage("+868888888888", "Wrong number!");
		// msg.setPriority(OutboundMessage.Priorities.HIGH);
		// srv.queueMessage(msg, gateway.getGatewayId());

//		System.out.println("进入等待状态... - 按回车键退出.");
//		System.in.read();
		SMSService.service = service;
		return service;
	}
	
	public static void sendSms(String phoneNumber, String content) throws TimeoutException, GatewayException, IOException, InterruptedException{
		if(service == null){
			startService();
		}
		// 发送短信（同步发送，发送完成才执行下一步）.
		OutboundMessage msg = new OutboundMessage(phoneNumber, content);
		msg.setEncoding(MessageEncodings.ENCUCS2);
		
//		service.queueMessage(msg);
		service.sendMessage(msg);
		System.out.println(msg);
	}
	
	public static void sendSmses(List<OutboundMessage> messages) throws TimeoutException, GatewayException, IOException, InterruptedException{
		if(service == null){
			startService();
		}
		for (Iterator iterator = messages.iterator(); iterator.hasNext();) { // 设置字符集
			OutboundMessage outboundMessage = (OutboundMessage) iterator.next();
			outboundMessage.setEncoding(MessageEncodings.ENCUCS2);
		}
		System.out.println("messages ========== " + messages);
		service.sendMessages(messages);
	}
	
	public static void sendSmses(String content, List<String> phoneNumbers) throws TimeoutException, GatewayException, IOException, InterruptedException{
		List<OutboundMessage> messages = new LinkedList<OutboundMessage>();
		for (Iterator iterator = phoneNumbers.iterator(); iterator.hasNext();) {
			String phoneNumber = (String) iterator.next();
			OutboundMessage message = new OutboundMessage(phoneNumber, content);
			messages.add(message);
		}
		sendSmses(messages);
	}
	
//	public static void addSmsToQueue(String phoneNumber, String content) throws TimeoutException, GatewayException, IOException, InterruptedException{
//		if(service == null){
//			startService();
//		}
//		// 发送短信（同步发送，发送完成才执行下一步）.
//		OutboundMessage msg = new OutboundMessage(phoneNumber, content);
//		msg.setEncoding(MessageEncodings.ENCUCS2);
//		
//		service.queueMessage(msg);
////		service.sendMessage(msg);
//		System.out.println(msg);
//	}

	public static void main(String args[]) {
//		loadDeviceResources(null);
		
		SMSService app = new SMSService();
		try {
			app.startService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			app.sendSms("15084818017", "linux 短信猫测试");
			app.sendSms("15084818017", "linux 短信猫测试2");
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
////		app.sendSms("18520802082", "linux 短信猫测试");
//		try {
//			app.service.stopService();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		} catch (GatewayException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Scanner scanner = new Scanner(System.in); // , "utf-8"
//		String nextLine = null;
//		while (true) {
//			String phoneNumber = null;
//			String content = null;
////			System.out.println("请输入电话号码！"); // 13480657523
////			if((nextLine = scanner.nextLine()) != null && !"".equals(nextLine.trim())){
////				phoneNumber = nextLine;
////			} else {
////				break;
////			}
//			System.out.println("请输入内容！");
//			if((nextLine = scanner.nextLine()) != null && !"".equals(nextLine.trim())){
//				content = nextLine;
//				System.out.println("content ============ " + content);
//			} else {
//				break;
//			}
////			app.sendSms(phoneNumber, content);
//		}
		
	}
	/**
	 * Service
	 * @return the service
	 */
	public static Service getService() {
		return service;
	}
	
}
