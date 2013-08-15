/**
 * SMS
 */
package com.kingray.servlet;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.junit.Test;

/**
 * @author 瑛琪
 * @version 2013-8-2 上午9:46:53
 */
public class TestRequest {
	@Test
	public void testSms() {
		InputStream is = null;
		try {
			Socket socket = new Socket("127.0.0.1", 27080);//
			StringBuilder builder = new StringBuilder();
			builder.append("POST ");
			String str = "/SMS/sms.do?actionType=sendSMS&phoneNumber=15084818017&content=" + URLEncoder.encode("熊瑛琪", "UTF-8");
			builder.append(str);
			builder.append("\r\n");
			builder.append("Host: 10.188.199.3:9090 \r\n");
			builder.append("Connection: keep-alive  \r\n");
			builder.append("Cache-Control: max-age=0  \r\n");
			builder.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8  \r\n");
			builder.append("User-Agent: Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1464.0 Safari/537.36  \r\n");
			builder.append("Accept-Encoding: gzip,deflate,sdch  \r\n");
			builder.append("Accept-Language: zh-CN,zh;q=0.8  \r\n\r\n");
			OutputStream os = socket.getOutputStream();
			// System.out.println(builder.toString());
			os.write(builder.toString().getBytes());
			os.flush();
			is = socket.getInputStream();

			String rs = null;
			StringBuilder stringBuilder = new StringBuilder();
			byte[] data = new byte[1024]; // 1k缓存
			int length = -1;
			while ((length = is.read(data)) > 0) {
				if (length != data.length) { // 读取的数据长度不是data的长度，那么就要读取有效的字节
					byte[] dataDes = new byte[length];
					System.arraycopy(data, 0, dataDes, 0, length); // 拷贝不是标准长度的字节
					data = dataDes;
				}
				stringBuilder.append(new String(data));
			}
			rs = stringBuilder.toString();
			System.out.println(rs);

			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(is));
			// StringBuilder sb = new StringBuilder();
			// while (br.read() != -1) {
			// sb.append(br.readLine());
			// }
			// String content = new String(sb);
			// content = new String(content.getBytes("UTF-8"), "ISO-8859-1");
			// System.out.println(content);
			// br.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
