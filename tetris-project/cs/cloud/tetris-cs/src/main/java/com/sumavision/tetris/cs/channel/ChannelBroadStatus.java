package com.sumavision.tetris.cs.channel;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONObject;

public abstract class ChannelBroadStatus {
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
			
			if (jsonObject.containsKey("broadIp") && jsonObject.containsKey("broadPort")) {
				broadcastIp = jsonObject.getString("broadIp");
				broadcastPort = jsonObject.getString("broadPort");
			}else {
				return "";
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
