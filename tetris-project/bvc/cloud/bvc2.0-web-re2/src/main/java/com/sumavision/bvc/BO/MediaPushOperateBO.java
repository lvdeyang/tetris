package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaPushOperateBO {
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * uuid
	 */
	private String uuid;
	
	/**
	 * 文件名
	 */
	private String file_source;
	
	/**
	 * 编解码参数
	 */
	private JSONObject codec_param;
	
	/**
	 * 接入层Id
	 */
	private String layerId;
	
	/**
	 * 编码通道的bundleId和channelId,mediaPushUpdate使用
	 */
	private String bundleId;
	private String channelId;
	
//	@Override
//	public String toString() {
//		return "MediaMuxOutBO [taskId=" + taskId + ", lock_type=" + lock_type + ", uuid=" + uuid + ", layerId="
//				+ layerId + ", bundleId=" + bundleId + ", channelId=" + channelId + ", channel_param=" + channel_param
//				+ "]";
//	}

}
