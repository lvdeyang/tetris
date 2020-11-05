package com.sumavision.bvc.device.group.bo;

/**
 * @Title: 录制源协议 
 * @author lvdeyang
 * @date 2018年8月13日 下午3:06:36 
 */
public class RecordSourceBO {

	private String type = "";
	
	private String layer_id = "";
	
	private String bundle_id = "";
	
	private String channel_id = "";
	
	private String uuid = "";

	public String getType() {
		return type;
	}

	public RecordSourceBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getLayer_id() {
		return layer_id;
	}

	public RecordSourceBO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public RecordSourceBO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public RecordSourceBO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public RecordSourceBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
	
}
