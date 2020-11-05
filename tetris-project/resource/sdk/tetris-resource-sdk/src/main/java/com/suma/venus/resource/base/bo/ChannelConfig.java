package com.suma.venus.resource.base.bo;

/**
 * 通道配置
 * @author lxw
 *
 */
public class ChannelConfig {

	private String channel_id;
	
	private String channel_name;
	
	private String base_type;
	
	private String extern_type;

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getBase_type() {
		return base_type;
	}

	public void setBase_type(String base_type) {
		this.base_type = base_type;
	}

	public String getExtern_type() {
		return extern_type;
	}

	public void setExtern_type(String extern_type) {
		this.extern_type = extern_type;
	}
	
}
