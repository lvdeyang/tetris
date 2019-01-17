package com.sumavision.tetris.constraint;

import java.util.List;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ParamVO extends AbstractBaseVO<ParamVO, ParamBO>{

	private String key;
	
	private String value;
	
	private String name;
	
	private int serial;
	
	private String remarks;
	
	private String paramType;
	
	private List<String> enums;
	
	public String getKey() {
		return key;
	}

	public ParamVO setKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ParamVO setValue(String value) {
		this.value = value;
		return this;
	}

	public String getName() {
		return name;
	}

	public ParamVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getSerial() {
		return serial;
	}

	public ParamVO setSerial(int serial) {
		this.serial = serial;
		return this;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public ParamVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
	
	public String getParamType() {
		return paramType;
	}

	public ParamVO setParamType(String paramType) {
		this.paramType = paramType;
		return this;
	}

	public List<String> getEnums() {
		return enums;
	}

	public ParamVO setEnums(List<String> enums) {
		this.enums = enums;
		return this;
	}

	@Override
	public ParamVO set(ParamBO entity) throws Exception {
		this.setKey(entity.getKey())
			.setName(entity.getName())
			.setSerial(entity.getSerial())
			.setRemarks(entity.getRemarks())
			.setParamType(entity.getParamType().toString())
			.setEnums(entity.getEnums());
		return this;
	}

}
