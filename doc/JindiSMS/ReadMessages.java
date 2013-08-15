// JindiSMS v3.1 
// 该程序主要用于读取GSM Modem接收到的短信。
// 金笛短信网 www.sendsms.cn
// 为节省您的开发时间，请配合金笛短信设备以达到最佳效果。 

import java.util.*;
import cn.sendsms.*;

public class ReadMessages
{
	private Service srv;

	public void doIt() throws Exception
	{
		List msgList;
		InboundNotification inboundNotification = new InboundNotification();
		CallNotification callNotification = new CallNotification();
		try
		{
			System.out.println("示例: 从串口短信设备读取短信.");
			System.out.println(Library.getLibraryDescription());
			System.out.println("版本: " + Library.getLibraryVersion());
			srv = new Service();
			// 创建串口GSM modem连接通道.
			SerialModemGateway gateway = new SerialModemGateway("jindi", "COM1", 115200, "wavecom", "M1306B", srv.getLogger());
			//设置通道是否接收短信
			gateway.setInbound(true);
			//设置通道是否可以发送短信
			gateway.setOutbound(true);
			gateway.setSimPin("0000");
			//设置短信到达后调用方法
			gateway.setInboundNotification(inboundNotification);
			gateway.setCallNotification(callNotification);
			// 增加短信通道到服务对象
			srv.addGateway(gateway);
			// 如果有多个设备，可以依次添加到服务对象。

			// 启动服务! (连接到所有已定义的设备通道)
			srv.startService();
			System.out.println();
			System.out.println("GSM Modem 信息:");
			System.out.println("  厂家: " + gateway.getManufacturer());
			System.out.println("  型号: " + gateway.getModel());
			System.out.println("  序列号: " + gateway.getSerialNo());
			System.out.println("  SIM IMSI: " + gateway.getImsi());
			System.out.println("  信号强度: " + gateway.getSignalLevel() + "%");
			System.out.println("  电池容量: " + gateway.getBatteryLevel() + "%");
			System.out.println();
			// 读取短信.
			msgList = new ArrayList();
			srv.readMessages(msgList, MessageClasses.ALL);
			for (int i = 0; i < msgList.size(); i++)
				System.out.println(msgList.get(i));
			// 进入休眠状态. 如果有新短信进来，就会重新激活。
			System.out.println("进入等待状态... - 按 <enter>回车键退出.");
			System.in.read();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			srv.stopService();
		}
	}

	public class InboundNotification implements IInboundMessageNotification
	{
		public void process(String gatewayId, MessageTypes msgType, String memLoc, int memIndex)
		{
			List msgList;
			if (msgType == MessageTypes.INBOUND)
			{
				System.out.println(">>> 监测到设备收到新的短信: " + gatewayId + " : " + memLoc + " @ " + memIndex);
				try
				{
					// Read...
					msgList = new ArrayList();
					srv.readMessages(msgList, MessageClasses.UNREAD, gatewayId);
					for (int i = 0; i < msgList.size(); i++)
						System.out.println(msgList.get(i));
					// ...and reply.
					// for (int i = 0; i < msgList.size(); i ++)
					// 	srv.sendMessage(new OutboundMessage(msgList.get(i).getOriginator(), "Got it!"), gatewayId);
				}
				catch (Exception e)
				{
					System.out.println("有异常...");
					e.printStackTrace();
				}
			}
			else if (msgType == MessageTypes.STATUSREPORT)
			{
				System.out.println(">>> 监测到设备收到短信状态报告: " + gatewayId + " : " + memLoc + " @ " + memIndex);
			}
		}
	}

	public class CallNotification implements ICallNotification
	{
		public void process(String gatewayId, String callerId)
		{
			System.out.println(">>> 监测到有电话打入: " + gatewayId + " : " + callerId);
		}
	}

	public static void main(String args[])
	{
		ReadMessages app = new ReadMessages();
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
