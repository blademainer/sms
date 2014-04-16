package com.xiongyingqi.queue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.xiongyingqi.utils.EntityHelper;
import com.xiongyingqi.utils.FileHelper;

/**
 * 
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-16 下午2:52:24
 * @param <E>
 */
public class QueueManager<E> {
	private static final Object LOCK = new Object();
	private static Map<Class, QueueManager> instancesMap;
	private BlockingQueue<E> queue;

	private QueueManager() {
		queue = new LinkedBlockingQueue<E>();
	}

	public static QueueManager getInstance(Class clazz) {
		synchronized (LOCK) {
			if (instancesMap == null) {
				instancesMap = new HashMap<Class, QueueManager>();
			}
			QueueManager singleton = instancesMap.get(clazz);
			if (singleton == null) {
				singleton = new QueueManager();
				instancesMap.put(clazz, singleton);
			}
			return singleton;
		}
	}
	/**
	 * 
	 * <br>2013-9-16 下午2:51:47
	 * @param e
	 */
	public void add(E e) {
		try {
			queue.add(e);
		} catch (Exception ex) {
		}
	}

	public E poll() {
		try {
			return queue.poll(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
