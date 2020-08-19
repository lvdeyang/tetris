package com.sumavision.tetris.easy.process.core;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProcessTaskMyReviewVariableVO extends AbstractBaseVO<ProcessTaskMyReviewVariableVO, JSONObject>{

	private String key;
	
	private String name;
	
	private String value;
	
	private String type;
	
	private List<JSONObject> radio;
	
	public String getKey() {
		return key;
	}

	public ProcessTaskMyReviewVariableVO setKey(String key) {
		this.key = key;
		return this;
	}

	public String getName() {
		return name;
	}

	public ProcessTaskMyReviewVariableVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ProcessTaskMyReviewVariableVO setValue(String value) {
		this.value = value;
		return this;
	}

	public String getType() {
		return type;
	}

	public ProcessTaskMyReviewVariableVO setType(String type) {
		this.type = type;
		return this;
	}

	public List<JSONObject> getRadio() {
		return radio;
	}

	public ProcessTaskMyReviewVariableVO setRadio(List<JSONObject> radio) {
		this.radio = radio;
		return this;
	}

	@Override
	public ProcessTaskMyReviewVariableVO set(JSONObject entity) throws Exception {
		this.setKey(entity.getString("key"))
		    .setName(entity.getString("name"))
		    .setType(entity.getString("type"))
		    .setRadio(JSON.parseArray(entity.getString("radio"), JSONObject.class));
		return this;
	}
	
}
