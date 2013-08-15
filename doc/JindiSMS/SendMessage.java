// JindiSMS v3.1 
// �ó�����Ҫ����GSM Modem���ŷ���ʾ����
// ��Ѷ����� www.sendsms.cn
// Ϊ��ʡ���Ŀ���ʱ�䣬����Ͻ�Ѷ����豸�Դﵽ���Ч���� 

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

		System.out.println("ʾ��: ͨ����Ѵ��ڶ���è���Ͷ���.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("�汾: " + Library.getLibraryVersion());

		srv = new Service();
		SerialModemGateway gateway = new SerialModemGateway("jindi", "COM1", 115200, "wavecom", "M1306B", srv.getLogger());
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");
		gateway.setOutboundNotification(outboundNotification);
		srv.addGateway(gateway);

		srv.startService();
		System.out.println();
		System.out.println("GSM Modem��Ϣ:");
		System.out.println("  ����: " + gateway.getManufacturer());
		System.out.println("  �ͺ�: " + gateway.getModel());
		System.out.println("  ���к�: " + gateway.getSerialNo());
		System.out.println("  SIM IMSI: " + gateway.getImsi());
		System.out.println("  �ź�ǿ��: " + gateway.getSignalLevel() + "%");
		System.out.println("  �������: " + gateway.getBatteryLevel() + "%");
		System.out.println();

		// ���Ͷ��ţ�ͬ�����ͣ�������ɲ�ִ����һ����.
		msg = new OutboundMessage("13601019694", "���ã���ӭʹ�ý��JAVA���ſ�������www.sendsms.cn");
		msg.setEncoding(MessageEncodings.ENCUCS2);
		srv.sendMessage(msg);
		System.out.println(msg);

		// ����WAP����.
		//OutboundWapSIMessage wapMsg = new OutboundWapSIMessage("+8613601019694",  new URL("https://www.sendsms.cn/"), "��ӭ���ʽ�Ѷ�����!");
		//srv.sendMessage(wapMsg);
		//System.out.println(wapMsg);

		//���Ͷ��ţ��첽���ͣ������Ͷ��Ž�����У�����ʱ�����κεȴ���
		//msg = new OutboundMessage("+869999999999", "Wrong number!");
		//msg.setPriority(OutboundMessage.Priorities.LOW);
		//srv.queueMessage(msg, gateway.getGatewayId());
		//msg = new OutboundMessage("+868888888888", "Wrong number!");
		//msg.setPriority(OutboundMessage.Priorities.HIGH);
		//srv.queueMessage(msg, gateway.getGatewayId());

		System.out.println("����ȴ�״̬... - ���س����˳�.");
		System.in.read();

		srv.stopService();
	}

	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(String gatewayId, OutboundMessage msg)
		{
			System.out.println("����״̬: " + gatewayId);
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
