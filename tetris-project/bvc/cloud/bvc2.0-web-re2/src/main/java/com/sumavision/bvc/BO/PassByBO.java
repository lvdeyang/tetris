package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassByBO {

	private String layer_id;
	
	private String bundle_id;
	
	private String type;
	
	private JSONObject pass_by_content;

	@Override
	public String toString() {
		return "PassByBO [layer_id=" + layer_id + ", bundle_id=" + bundle_id + ", type=" + type + ", pass_by_content="
				+ pass_by_content + "]";
	}
	
	
}
