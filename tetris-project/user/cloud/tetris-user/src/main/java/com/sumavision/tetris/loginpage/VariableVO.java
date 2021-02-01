package com.sumavision.tetris.loginpage;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class VariableVO extends AbstractBaseVO<VariableVO,VariablePO>{

	/** 变量类型ID*/
	private String variableTypeId;
	
	/** 取值，text或img（base64编码）*/
	private String value;
	
	private String name;
	
	/** 变量key*/
	private String variableKey;
	
	/**	类型*/
	private Type type;
	
	private String typeName;
	
	public Type getType() {
		return type;
	}

	public VariableVO setType(Type type) {
		this.type = type;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public VariableVO setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	public String getVariableTypeId() {
		return variableTypeId;
	}

	public VariableVO setVariableTypeId(String variableTypeId) {
		this.variableTypeId = variableTypeId;
		return this;
	}

	public String getValue() {
		return value;
	}

	public VariableVO setValue(String value) {
		this.value = value;
		return this;
	}

	public String getName() {
		return name;
	}

	public VariableVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVariableKey() {
		return variableKey;
	}

	public VariableVO setVariableKey(String variableKey) {
		this.variableKey = variableKey;
		return this;
	}

	@Override
	public VariableVO set(VariablePO entity) throws Exception {
		this.setId(entity.getId())
			.setVariableTypeId(entity.getVariableTypeId())
			.setValue(entity.getValue());
		return this;
	}


}
