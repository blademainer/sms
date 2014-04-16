/**
 * SMS
 */
package com.xiongyingqi.message;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.OutboundMessage;

import com.xiongyingqi.utils.ComparatorHelper;
import com.xiongyingqi.utils.EntityHelper;
import com.xiongyingqi.utils.FileHelper;

/**
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-16 下午3:08:03
 */
public class MessageStore {
	public static final String MESSAGE_STORE_OUT_MESSAGE_FILE_SUFFIX = ".outmessage";
	public static final String MESSAGE_STORE_SENT_MESSAGE_FILE_SUFFIX = ".sentmessage";
	public static final String MESSAGE_STORE_IN_MESSAGE_FILE_SUFFIX = ".inmessage";
	public static final String STORE_QUEUE_UNIT_FOLDER_PATH = "/messages/";
	public File STORE_QUEUE_UNIT_FOLDER = new File("/messages/");
	private static final Object LOCK = new Object();
	private static MessageStore singleton;
	private ComparatorHelper<Message> comparatorHelper = new ComparatorHelper<Message>();
	
	private MessageStore(){
		URL url =  getClass().getResource("/");
		STORE_QUEUE_UNIT_FOLDER = new File(url.getFile(), STORE_QUEUE_UNIT_FOLDER_PATH);
		STORE_QUEUE_UNIT_FOLDER.mkdirs();
		EntityHelper.printDetail(STORE_QUEUE_UNIT_FOLDER);
	}
	
	public static MessageStore getInstance(){
		synchronized (LOCK) {
			if(singleton == null){
				singleton = new MessageStore();
			}
		}
		return singleton;
	}
	
	
	public OutboundMessage[] readOutMessages(){
		Collection<OutboundMessage> messages = new ArrayList<OutboundMessage>();
		File[] files = FileHelper.listFilesBySuffix(STORE_QUEUE_UNIT_FOLDER, MESSAGE_STORE_OUT_MESSAGE_FILE_SUFFIX);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			Object instance = FileHelper.unSerializeObjectFromFile(file);
			if(instance != null && instance instanceof OutboundMessage){
				OutboundMessage message = (OutboundMessage) instance;
				messages.add(message);
			}
		}
		return messages.toArray(new OutboundMessage[]{});
	}
	
	/**
	 * 删除已经存储的发送消息文件
	 * <br>2013-9-16 下午3:18:26
	 * @param message
	 */
	public void deleteOutMessage(OutboundMessage message){
		FileHelper.deleteFile(new File(STORE_QUEUE_UNIT_FOLDER, message.getUuid() + MESSAGE_STORE_OUT_MESSAGE_FILE_SUFFIX));
	}
	
	/**
	 * 存储发送消息到文件
	 * <br>2013-9-16 下午3:18:46
	 * @param message
	 */
	public void storeOutMessage(OutboundMessage message){
		try {
			FileHelper.serializeObjectToFile(new File(STORE_QUEUE_UNIT_FOLDER, message.getUuid() + MESSAGE_STORE_OUT_MESSAGE_FILE_SUFFIX), message);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 保存接收到的消息
	 * <br>2013-9-16 下午3:19:01
	 * @param message
	 */
	public void storeInMessage(InboundMessage message){
		try {
			FileHelper.serializeObjectToFile(new File(STORE_QUEUE_UNIT_FOLDER, message.getUuid() + MESSAGE_STORE_IN_MESSAGE_FILE_SUFFIX), message);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 保存已经发送的消息
	 * <br>2013-9-16 下午5:50:35
	 * @param message
	 */
	public void storeSentMessage(OutboundMessage message){
		try {
			FileHelper.serializeObjectToFile(new File(STORE_QUEUE_UNIT_FOLDER, message.getUuid() + MESSAGE_STORE_SENT_MESSAGE_FILE_SUFFIX), message);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 获取保存的已经发送的消息
	 * <br>2013-9-16 下午6:08:37
	 * @param asc 是否按升序排列
	 * @return
	 */
	public OutboundMessage[] readSentMessages(boolean asc){
		Collection<OutboundMessage> messages = new ArrayList<OutboundMessage>();
		File[] files = FileHelper.listFilesBySuffix(STORE_QUEUE_UNIT_FOLDER, MESSAGE_STORE_SENT_MESSAGE_FILE_SUFFIX);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			Object object = FileHelper.unSerializeObjectFromFile(file);
			OutboundMessage message = (OutboundMessage) object;
			messages.add(message);
		}
		messages = (Collection<OutboundMessage>) sortMessages(messages, asc);
		
		return messages.toArray(new OutboundMessage[]{});
	}
	
	/**
	 * 读取收取的消息
	 * <br>2013-9-16 下午5:51:05
	 * @return
	 */
	public InboundMessage[] readInMessages(boolean asc){
		Collection<InboundMessage> messages = new ArrayList<InboundMessage>();
		File[] files = FileHelper.listFilesBySuffix(STORE_QUEUE_UNIT_FOLDER, MESSAGE_STORE_IN_MESSAGE_FILE_SUFFIX);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			Object object = FileHelper.unSerializeObjectFromFile(file);
			messages.add((InboundMessage) object);
		}
		messages = (Collection<InboundMessage>) sortMessages(messages, asc);
		
		
		return messages.toArray(new InboundMessage[]{});
	}
	
	/**
	 * 读取所有消息
	 * <br>2013-9-16 下午6:20:38
	 * @param asc
	 * @return
	 */
	public Message[] readMessages(boolean asc){
		Collection<Message> messages = new ArrayList<Message>();
		File[] sentMessageFiles = FileHelper.listFilesBySuffix(STORE_QUEUE_UNIT_FOLDER, MESSAGE_STORE_SENT_MESSAGE_FILE_SUFFIX);
		File[] inMessageFiles = FileHelper.listFilesBySuffix(STORE_QUEUE_UNIT_FOLDER, MESSAGE_STORE_IN_MESSAGE_FILE_SUFFIX);
		for (int i = 0; i < inMessageFiles.length; i++) {// 接收的消息
			File file = inMessageFiles[i];
			Object object = FileHelper.unSerializeObjectFromFile(file);
			messages.add((Message) object);
		}
		for (int i = 0; i < sentMessageFiles.length; i++) {// 发送的消息
			File file = sentMessageFiles[i];
			Object object = FileHelper.unSerializeObjectFromFile(file);
			messages.add((Message) object);
		}
		messages = (Collection<Message>) sortMessages(messages, asc);
		return messages.toArray(new Message[]{});
	}
	
	private Collection<? extends Message> sortMessages(Collection<? extends Message> messages, boolean asc){
		try {
			List messagesSorted = comparatorHelper.sortByDateTime(messages, Message.class.getDeclaredField("date"), asc);
			return messagesSorted;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public String getPath(){
		URL url =  getClass().getResource("/");
		return url.getFile();
	}
	
	public static void main(String[] args) {
		
		System.out.println(getInstance().getPath());
	}
}
