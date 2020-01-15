package com.sumavision.bvc.device.resource.bo;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;

import lombok.Getter;
import lombok.Setter;

/**
 * CreateResource的格式参见《资源层接口文档_HTTP部分.docx》1.1.2
 * 		{    
			"create_resource_request" : {
				"userId" : 10,
				"resource" : {
					"name" : "直播流1",
					"type" : "recordStream",
					"cid" : "10"
				}
			}
		}
	resource是对象
 * 
 */

@Getter
@Setter
public class CreateResourceBO {
	private create_resource_request create_resource_request = new create_resource_request();
		
	@Getter
	@Setter
	public class create_resource_request{
		private Long userId;
		
		//直播为LiveChannelResourceBO，点播为AssetResourceBO
		private Object resource;
	};
}
