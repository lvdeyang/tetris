package com.sumavision.tetris.bvc.business.dispatch.bo;

/**
 * @ClassName: 透传协议 
 * @author wjw 
 * @date 2018年10月11日 上午8:55:15
 */
public class PassByBO {
	
	private String layer_id = "";
	
	private String bundle_id = "";
	
	/** 透传类型 */
	private String type = ""; 
	
	/** 保留，以后可能用 */
	private String channel_id = "";
	
	private Object pass_by_content;

	public String getLayer_id() {
		return layer_id;
	}

	public PassByBO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public PassByBO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getType() {
		return type;
	}

	public PassByBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public PassByBO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}

	public Object getPass_by_content() {
		return pass_by_content;
	}

	public PassByBO setPass_by_content(Object pass_by_content) {
		this.pass_by_content = pass_by_content;
		return this;
	}
	
}
