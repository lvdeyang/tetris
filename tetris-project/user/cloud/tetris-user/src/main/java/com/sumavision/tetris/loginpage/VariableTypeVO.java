package com.sumavision.tetris.loginpage;

import java.util.List;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class VariableTypeVO extends AbstractBaseVO<VariableTypeVO,VariableTypePO>{

	private String name;
	
	/** 变量key*/
	private String variableKey;
	
	/**	类型*/
	private String type;
	
	private String typeName;
	
	
	private List<VariableVO> variable;
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public List<VariableVO> getVariable() {
		return variable;
	}

	public void setVariable(List<VariableVO> variable) {
		this.variable = variable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVariableKey() {
		return variableKey;
	}

	public void setVariableKey(String variableKey) {
		this.variableKey = variableKey;
	}
	
	@Override
	public VariableTypeVO set(VariableTypePO entity) throws Exception {
		VariableTypeVO vo = new VariableTypeVO();
		vo.setId(entity.getId());
		vo.setName(entity.getName());
		vo.setVariableKey(entity.getVariableKey());
		vo.setType(entity.getType() == null? null:entity.getType().toString());
		vo.setTypeName(entity.getType() == null? null:entity.getType().getName());
		return vo;
	}
}
