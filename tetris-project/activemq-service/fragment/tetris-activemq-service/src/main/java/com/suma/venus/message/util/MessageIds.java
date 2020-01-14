package com.suma.venus.message.util;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 共享数据
 * 由于全部采用异步通信，用这个类记录MessageId，消息发送方可以探测返回信息结果
 * 
 * @author admin
 *
 */
public class MessageIds {

	/**
	 * 全局Map，记录发送的消息Id，供发送和接收响应使用
	 */
	public static volatile Map<String,Object> messageInfoMap = new ConcurrentHashMap<String, Object>();
}
