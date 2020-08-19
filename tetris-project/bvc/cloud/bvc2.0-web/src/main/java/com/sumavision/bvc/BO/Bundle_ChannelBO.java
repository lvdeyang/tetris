package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;

/**
 * 
 * @ClassName:  Bundle_ChannelBO   
 * @Description:TODO  
 * @author: 
 * @date:   2018年11月12日 上午11:27:49   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class Bundle_ChannelBO {
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId;
	
	/**
	 * 通道状态Open/Close
	 */
	private String channel_status;
	
	/**
	 * channelParam的基本能力参数类型
	 */
	private String base_type;
	
	/**
	 * 通道的源参数
	 */
	private SrcBO source_param;
	
	/**
	 * 编解码参数
	 */
	private JSONObject codec_param;
	
	/**
	 * 字幕
	 */
	private JSONObject osds;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannel_status() {
		return channel_status;
	}

	public void setChannel_status(String channel_status) {
		this.channel_status = channel_status;
	}

	public String getBase_type() {
		return base_type;
	}

	public void setBase_type(String base_type) {
		this.base_type = base_type;
	}

	public SrcBO getSource_param() {
		return source_param;
	}

	public void setSource_param(SrcBO source_param) {
		this.source_param = source_param;
	}

	public JSONObject getCodec_param() {
		return codec_param;
	}

	public void setCodec_param(JSONObject codec_param) {
		this.codec_param = codec_param;
	}

	public JSONObject getOsds() {
		return osds;
	}

	public void setOsds(JSONObject osds) {
		this.osds = osds;
	}

	@Override
	public String toString() {
		return "Bundle_ChannelBO [channelId=" + channelId + ", channel_status=" + channel_status + ", base_type="
				+ base_type + ", source_param=" + source_param + ", codec_param="
				+ codec_param + "]";
	}
	
}
