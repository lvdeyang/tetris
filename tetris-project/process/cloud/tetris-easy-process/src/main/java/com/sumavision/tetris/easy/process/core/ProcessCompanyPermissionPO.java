package com.sumavision.tetris.easy.process.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 公司流程权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年7月11日 上午10:10:16
 */
@Entity
@Table(name = "TETRIS_PROCESS_COMPANY_PERMISSION")
public class ProcessCompanyPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 流程id */
	private Long processId;
	
	/** 公司id */
	private String companyId;

	@Column(name = "PROCESS_ID")
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	@Column(name = "COMPANY_ID")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
