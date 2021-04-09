package com.sumavision.bvc.BO;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BundleInfo {

	private String bundle_id;
	
	private String bundle_type;
	
	private String device_model;
	
	private Integer operate_index;
	
	private ChannelInfo channel;
	
	private List<ChannelInfo> channels;
	
	private JSONArray screens;
	
	private String pass_by_str;
}
