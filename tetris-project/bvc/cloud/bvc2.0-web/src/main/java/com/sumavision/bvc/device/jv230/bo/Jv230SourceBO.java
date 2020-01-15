package com.sumavision.bvc.device.jv230.bo;

import com.sumavision.bvc.device.group.enumeration.CombineVideoSrcType;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;

public class Jv230SourceBO {

	/** 源执行层layerId */
	private String layer_id;
	
	/** 源设备bundleId */
	private String bundle_id;
	
	/** 源通道channelId */
	private String channel_id;
	
	/**
	 * 音频需要
	 */
	/** channel,combineAudio,combineVideo */
	private String type;
	
	/** 混音/合屏--uuid */
	private String uuid;

	public String getLayer_id() {
		return layer_id;
	}

	public Jv230SourceBO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public Jv230SourceBO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public Jv230SourceBO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}

	public String getType() {
		return type;
	}

	public Jv230SourceBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public Jv230SourceBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
	
	public Jv230SourceBO setVideo(CombineVideoSrcPO src){
		if(src.getType().equals(CombineVideoSrcType.CHANNEL)){
			this.setType("channel")
				.setLayer_id(src.getLayerId())
				.setBundle_id(src.getBundleId())
				.setChannel_id(src.getChannelId());
		}else if(src.getType().equals(CombineVideoSrcType.VIRTUAL)){
			this.setType("combineVideo")
				.setUuid(src.getVirtualUuid());
		}
		
		return this;
	}
}
