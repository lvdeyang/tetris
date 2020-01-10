package com.suma.venus.resource.base.bo;


public class ChannelBody {
	
	private String bundle_id;

	private String channel_id;
	
	private String channel_name;
	
	private String channel_state;
	
	private Object channel_param;
	
	private String base_type;
	
	private String extern_type;
	
	private Integer audio_decode_cnt;
	
	private Integer video_decode_cnt;
		
	public Integer getAudio_decode_cnt() {
		return audio_decode_cnt;
	}

	public void setAudio_decode_cnt(Integer audio_decode_cnt) {
		this.audio_decode_cnt = audio_decode_cnt;
	}

	public Integer getVideo_decode_cnt() {
		return video_decode_cnt;
	}

	public void setVideo_decode_cnt(Integer video_decode_cnt) {
		this.video_decode_cnt = video_decode_cnt;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

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

	public String getChannel_state() {
		return channel_state;
	}

	public void setChannel_state(String channel_state) {
		this.channel_state = channel_state;
	}

	public Object getChannel_param() {
		return channel_param;
	}

	public void setChannel_param(Object channel_param) {
		this.channel_param = channel_param;
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
