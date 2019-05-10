package com.sumavision.tetris.cs.channel;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONObject;

public abstract class ChannelBroadStatus {
//	public final static String BROADCAST_IP = "192.165.58.123:8180";
//	final static String BROADCAST_IP = "10.10.40.189";
	
	public static String broadcastIp = "";
	public static String broadcastPort = "";
	
	public final static String CHANNEL_BROAD_STATUS_BROADING = "发送中";
	public final static String CHANNEL_BROAD_STATUS_BROADED = "发送完成";
	public final static String CHANNEL_BROAD_STATUS_STOPPED = "发送停止";
	
	public static String getBroadcastIPAndPort() throws Exception{
		if(broadcastIp.isEmpty() || broadcastPort.isEmpty()){
			File jsonFile = ResourceUtils.getFile("classpath:broadConfig.json");
			String json = FileUtils.readFileToString(jsonFile);
			JSONObject jsonObject = JSONObject.parseObject(json);
			
			if (jsonObject.containsKey("broadIp")) {
				broadcastIp = jsonObject.getString("broadIp");
			}else {
				broadcastIp = "192.165.58.123";
			}
			if (jsonObject.containsKey("broadPort")) {
				broadcastPort = jsonObject.getString("broadPort");
			}else {
				broadcastPort = "8081";
			}
		}
		
		return broadcastIp + ":" + broadcastPort;
	}
	
	public static Boolean getBroadcastIfLocal() throws Exception{
		File jsonFile = ResourceUtils.getFile("classpath:broadConfig.json");
		String json = FileUtils.readFileToString(jsonFile);
		JSONObject jsonObject = JSONObject.parseObject(json);
		
		if (jsonObject.containsKey("ifLocal")) {
			return jsonObject.getBoolean("ifLocal");
		}else {
			return false;
		}
	}
}
