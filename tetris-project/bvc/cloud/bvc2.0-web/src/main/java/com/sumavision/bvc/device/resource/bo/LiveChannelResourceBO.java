package com.sumavision.bvc.device.resource.bo;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveChannelResourceBO {

	private String name;
	
	private String cid;//BO的直播唯一标识，videoLiveID
	
	private String type = "liveChannel";
	
	private String playUrl;
	
	private String locationID;//地区
	
	private String categoryLiveID;//直播分类
}
