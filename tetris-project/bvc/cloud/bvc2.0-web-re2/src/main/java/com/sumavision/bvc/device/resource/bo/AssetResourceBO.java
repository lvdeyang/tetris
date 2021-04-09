package com.sumavision.bvc.device.resource.bo;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetResourceBO {
	
	private String name;
	
	private String pid;//BO的点播唯一标识，videoID
	
	private String type = "asset";
	
	private String assetPlayUrl;
	
	private String locationID;//地区
	
	private String categoryID;//点播分类	
	
}
