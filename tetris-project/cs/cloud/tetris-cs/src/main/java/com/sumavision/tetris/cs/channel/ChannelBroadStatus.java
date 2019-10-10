package com.sumavision.tetris.cs.channel;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSONObject;

public abstract class ChannelBroadStatus {
	public static String terminalBroadIp = "";
	public static String terminalBroadPort = "";
	public static String abilityBroadIp = "";
	public static String abilityBroadPort = "";
	
	public final static String CHANNEL_BROAD_STATUS_BROADING = "发送中";
	public final static String CHANNEL_BROAD_STATUS_BROADED = "发送完成";
	public final static String CHANNEL_BROAD_STATUS_STOPPED = "发送停止";
	
	/**
	 * 从配置文件获取ip和端口生成url(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 */
	public static String getBroadcastIPAndPort(BroadWay way) throws Exception{
		if (way == BroadWay.TERMINAL_BROAD) {
			if(terminalBroadIp.isEmpty() || terminalBroadPort.isEmpty()){
				File jsonFile = ResourceUtils.getFile("classpath:profile.json");
				String json = FileUtils.readFileToString(jsonFile);
				JSONObject jsonObject = JSONObject.parseObject(json);
				
				if (jsonObject.containsKey("terminalBroadIp") && jsonObject.containsKey("terminalBroadPort")) {
					terminalBroadIp = jsonObject.getString("terminalBroadIp");
					terminalBroadPort = jsonObject.getString("terminalBroadPort");
				}else {
					return "";
				}
			}
			
			return terminalBroadIp + ":" + terminalBroadPort;
		} else {
			if(abilityBroadIp.isEmpty() || abilityBroadPort.isEmpty()){
				File jsonFile = ResourceUtils.getFile("classpath:profile.json");
				String json = FileUtils.readFileToString(jsonFile);
				JSONObject jsonObject = JSONObject.parseObject(json);
				
				if (jsonObject.containsKey("abilityBroadIp") && jsonObject.containsKey("abilityBroadPort")) {
					abilityBroadIp = jsonObject.getString("abilityBroadIp");
					abilityBroadPort = jsonObject.getString("abilityBroadPort");
				}else {
					return "";
				}
			}
			
			return abilityBroadIp + ":" + abilityBroadPort;
		}
	}
	
	/**
	 * 根据配置文件判断是否为本地播发(仅终端播发生效)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 */
	public static Boolean getBroadcastIfLocal() throws Exception{
		File jsonFile = ResourceUtils.getFile("classpath:profile.json");
		String json = FileUtils.readFileToString(jsonFile);
		JSONObject jsonObject = JSONObject.parseObject(json);
		
		if (jsonObject.containsKey("ifLocal")) {
			return jsonObject.getBoolean("ifLocal");
		}else {
			return false;
		}
	}
}
