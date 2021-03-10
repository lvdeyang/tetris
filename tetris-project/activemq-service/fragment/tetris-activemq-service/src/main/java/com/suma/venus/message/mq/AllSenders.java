package com.suma.venus.message.mq;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

// import org.apache.commons.collections.map.HashedMap;


/**
 * 系统内所有发送者，持有系统内所有队列的sender
 * @author admin
 *
 */
public class AllSenders {

	/**
	 *系统内所有发送者， key是queueName，value是发送者
	 */
	public static Map<String,QueueMsgSender> sendersMap = new HashedMap();
}
