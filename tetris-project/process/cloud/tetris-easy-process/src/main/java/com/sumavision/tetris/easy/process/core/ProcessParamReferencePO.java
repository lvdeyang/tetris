package com.sumavision.tetris.easy.process.core;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 流程参数映射<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月25日 上午10:15:19
 */
@Entity
@Table(name = "TETRIS_PROCESS_PARAM_REFERENCE")
public class ProcessParamReferencePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 参数主键分隔符 */
	public static final String KEY_SEPARATOR = ",";

	/** 流程参数映射 primaryKeyPath,primaryKeyPath,primaryKeyPath */
	private String reference;
	
	/** 流程id */
	private Long processId;

	@Column(name = "REFERENCE")
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public void setReference(List<String> reference){
		StringBuffer referenceBuffer = new StringBuffer();
		for(int i=0; i<reference.size(); i++){
			referenceBuffer.append(reference.get(i));
			if(i != (reference.size()-1)){
				referenceBuffer.append(ProcessParamReferencePO.KEY_SEPARATOR);
			}
		}
		this.reference = referenceBuffer.toString();
	}

	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
}
