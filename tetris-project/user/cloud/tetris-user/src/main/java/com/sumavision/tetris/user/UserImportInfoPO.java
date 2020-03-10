package com.sumavision.tetris.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_USER_IMPORT_INFO")
public class UserImportInfoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 企业id */
	private String companyId;
	
	/** 用户导入次数 */
	private Long times;

	@Column(name = "COMPANY_ID")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "TIMES")
	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}
	
}
