package com.xiongyingqi.sms;

import java.util.*;
import java.io.*;

import org.smslib.helper.CommPortIdentifier;
import org.smslib.helper.SerialPort;

public class DeviceSearcher {
	private static CommPortIdentifier portId;
	private static Enumeration portList;
	private static int baudPort;
	private static int bauds[] = { 9600, 19200, 57600, 115200 };

	static {
		scanDevice();
	}

	public static String scanDevice() {
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println("找到串口: " + portId.getName());
				for (int i = 0; i < bauds.length; i++) {
					System.out.print("	Trying at " + bauds[i] + "...");
					try {
						SerialPort serialPort;
						InputStream inStream;
						OutputStream outStream;
						int c;
						String response;
						serialPort = (SerialPort) portId.open(
								"SMSLibCommTester", 1971);
						serialPort
								.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
						serialPort.setSerialPortParams(bauds[i],
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
						inStream = serialPort.getInputStream();
						outStream = serialPort.getOutputStream();
						serialPort.enableReceiveTimeout(1000);
						c = inStream.read();
						while (c != -1)
							c = inStream.read();
						outStream.write('A');
						outStream.write('T');
						outStream.write('\r');
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
						response = "";
						c = inStream.read();
						while (c != -1) {
							response += (char) c;
							c = inStream.read();
						}
						if (response.indexOf("OK") >= 0) {
							try {
								System.out.print("  获取设备信息...");
								outStream.write('A');
								outStream.write('T');
								outStream.write('+');
								outStream.write('C');
								outStream.write('G');
								outStream.write('M');
								outStream.write('M');
								outStream.write('\r');
								response = "";
								c = inStream.read();
								while (c != -1) {
									response += (char) c;
									c = inStream.read();
								}
								System.out.println("  发现设备: "
										+ response.replaceAll("\\s+OK\\s+", "")
												.replaceAll("\n", "")
												.replaceAll("\r", ""));
								baudPort = bauds[i];
								return response;
							} catch (Exception e) {
								System.out.println("  没有发现设备!");
							}
						} else
							System.out.println("  没有发现设备!");
						serialPort.close();
					} catch (Exception e) {
						System.out.println("  没有发现设备!");
					}
				}
			}
		}
		return null;

	}

	/**
	 * CommPortIdentifier
	 * 
	 * @return the portId
	 */
	public static CommPortIdentifier getPortId() {
		return portId;
	}

	/**
	 * Enumeration
	 * 
	 * @return the portList
	 */
	public static Enumeration getPortList() {
		return portList;
	}

	/**
	 * int
	 * 
	 * @return the baudPort
	 */
	public static int getBaudPort() {
		return baudPort;
	}

	public static void main(String[] args) {
		String deviceInfo = scanDevice();
	}
}
