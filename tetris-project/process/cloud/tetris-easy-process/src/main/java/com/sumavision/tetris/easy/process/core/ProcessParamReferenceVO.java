package com.sumavision.tetris.easy.process.core;

import java.util.HashSet;
import java.util.Set;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ProcessParamReferenceVO extends AbstractBaseVO<ProcessParamReferenceVO, ProcessParamReferencePO>{

	private Set<String> reference;
	
	private Long processId;
	
	private boolean removeable;
	
	public Set<String> getReference() {
		return reference;
	}

	public ProcessParamReferenceVO setReference(Set<String> reference) {
		this.reference = reference;
		return this;
	}

	public Long getProcessId() {
		return processId;
	}

	public ProcessParamReferenceVO setProcessId(Long processId) {
		this.processId = processId;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public ProcessParamReferenceVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	@Override
	public ProcessParamReferenceVO set(ProcessParamReferencePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setProcessId(entity.getProcessId())
			.setReference(new HashSet<String>())
			.setRemoveable(entity.getAutoGeneration()==null?true:!entity.getAutoGeneration().booleanValue());
		if(entity.getReference()!=null && !"".equals(entity.getReference())){
			String[] primaryKeys = entity.getReference().split(ProcessParamReferencePO.KEY_SEPARATOR);
			for(String primaryKey:primaryKeys){
				this.getReference().add(primaryKey);
			}
		}
		return this;
	}

}
