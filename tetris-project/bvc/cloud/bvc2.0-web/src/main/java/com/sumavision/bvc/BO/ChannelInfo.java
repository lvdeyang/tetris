package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelInfo {

	private String channel_id;
	
	private String channel_status;
	
	private String channel_name;
	
	private Integer operate_index;
	
	private JSONObject channel_param;	
	
}
