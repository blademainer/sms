/**
 * SMS
 */
package com.xiongyingqi.sms;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.OutboundMessage;
import org.smslib.balancing.LoadBalancer;

import com.xiongyingqi.utils.ComparatorHelper;

/**
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-13 下午3:50:50
 */
public class CheckStatusLoadBalance extends LoadBalancer{
	public static final Logger log = Logger.getLogger(CheckStatusLoadBalance.class);
	
	private ComparatorHelper<Number> comparatorHelper = new ComparatorHelper<Number>();
	/**
	 * 优先级加载映射表
	 */
	private Map<Integer, Set<AGateway>> gateWayPriorityMap = new LinkedHashMap<Integer, Set<AGateway>>();
	
	/**
	 * 设置网关优先级
	 * <br>2013-9-13 下午4:49:35
	 * @param gateway
	 * @param priority
	 */
	public void setGatewayPriority(AGateway gateway, Integer priority){
		Set<AGateway> gateways = gateWayPriorityMap.get(priority);
		if(gateways == null){
			gateways = new LinkedHashSet<AGateway>();
		}
		gateways.add(gateway);
		gateWayPriorityMap.put(priority, gateways);
	}
	
	/**
	 * <br>2013-9-13 下午3:51:18
	 * @see org.smslib.balancing.LoadBalancer#balance(org.smslib.OutboundMessage, java.util.Collection)
	 */
	@Override
	public AGateway balance(OutboundMessage msg, Collection<AGateway> candidates) {
		Set<Integer> priorities = gateWayPriorityMap.keySet();
		List<Integer> integers = comparatorHelper.sortByNumber(priorities, true);
		for (Iterator<Integer> iterator = integers.iterator(); iterator.hasNext();) {
			Integer number = (Integer) iterator.next();
			Set<AGateway> gateways = gateWayPriorityMap.get(number);
			for (Iterator iterator2 = gateways.iterator(); iterator2.hasNext();) {
				AGateway aGateway = (AGateway) iterator2.next();
				if(aGateway.getStatus() == GatewayStatuses.STARTED){
					log.info("CheckStatusLoadBalance balance ---------- " + aGateway.getGatewayId());
					log.info(aGateway);
					return aGateway;
				}
			}
		}
		return super.balance(msg, candidates);
	}
}
