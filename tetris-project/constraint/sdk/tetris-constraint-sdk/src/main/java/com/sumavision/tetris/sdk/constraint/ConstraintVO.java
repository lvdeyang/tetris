package com.sumavision.tetris.sdk.constraint;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ConstraintVO extends AbstractBaseVO<ConstraintVO, ConstraintBO<InternalConstraintBean>>{

	private String name;
	
	private String primaryKey;
	
	private String remarks;
	
	private List<ParamVO> params;
	
	public String getName() {
		return name;
	}

	public ConstraintVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public ConstraintVO setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public ConstraintVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
	
	public List<ParamVO> getParams() {
		return params;
	}

	public ConstraintVO setParams(List<ParamVO> params) {
		this.params = params;
		return this;
	}

	@Override
	public ConstraintVO set(ConstraintBO<InternalConstraintBean> entity) throws Exception {
		this.setPrimaryKey(entity.getId())
			.setName(entity.getName())
			.setRemarks(entity.getRemarks());
		if(entity.getParams()!=null && entity.getParams().size()>0){
			this.setParams(new ArrayList<ParamVO>());
			for(ParamBO scope:entity.getParams()){
				this.getParams().add(new ParamVO().set(scope));
			}
		}
		return this;
	}

}
