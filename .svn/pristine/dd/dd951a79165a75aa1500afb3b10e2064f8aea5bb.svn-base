// JindiSMS v3.1 
// 该程序主要用于GSM Modem短信发送示例。
// 金笛短信网 www.sendsms.cn
// 为节省您的开发时间，请配合金笛短信设备以达到最佳效果。 

import cn.sendsms.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class SendMessage
{
	public void doIt() throws Exception
	{
		Service srv;
		OutboundMessage msg;

		OutboundNotification outboundNotification = new OutboundNotification();

		System.out.println("示例: 通过金笛串口短信猫发送短信.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("版本: " + Library.getLibraryVersion());

		srv = new Service();
		SerialModemGateway gateway = new SerialModemGateway("jindi", "COM1", 115200, "wavecom", "M1306B", srv.getLogger());
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");
		gateway.setOutboundNotification(outboundNotification);
		srv.addGateway(gateway);

		srv.startService();
		System.out.println();
		System.out.println("GSM Modem信息:");
		System.out.println("  厂家: " + gateway.getManufacturer());
		System.out.println("  型号: " + gateway.getModel());
		System.out.println("  序列号: " + gateway.getSerialNo());
		System.out.println("  SIM IMSI: " + gateway.getImsi());
		System.out.println("  信号强度: " + gateway.getSignalLevel() + "%");
		System.out.println("  电池容量: " + gateway.getBatteryLevel() + "%");
		System.out.println();

		// 发送短信（同步发送，发送完成才执行下一步）.
		msg = new OutboundMessage("13601019694", "您好！欢迎使用金笛JAVA短信开发包。www.sendsms.cn");
		msg.setEncoding(MessageEncodings.ENCUCS2);
		srv.sendMessage(msg);
		System.out.println(msg);

		// 发送WAP短信.
		//OutboundWapSIMessage wapMsg = new OutboundWapSIMessage("+8613601019694",  new URL("https://www.sendsms.cn/"), "欢迎访问金笛短信网!");
		//srv.sendMessage(wapMsg);
		//System.out.println(wapMsg);

		//发送短信（异步发送，待发送短信进入队列，发送时不做任何等待）
		//msg = new OutboundMessage("+869999999999", "Wrong number!");
		//msg.setPriority(OutboundMessage.Priorities.LOW);
		//srv.queueMessage(msg, gateway.getGatewayId());
		//msg = new OutboundMessage("+868888888888", "Wrong number!");
		//msg.setPriority(OutboundMessage.Priorities.HIGH);
		//srv.queueMessage(msg, gateway.getGatewayId());

		System.out.println("进入等待状态... - 按回车键退出.");
		System.in.read();

		srv.stopService();
	}

	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(String gatewayId, OutboundMessage msg)
		{
			System.out.println("发送状态: " + gatewayId);
			System.out.println(msg);
		}
	}

	public static void main(String args[])
	{
		SendMessage app = new SendMessage();
		try
		{
			app.doIt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
