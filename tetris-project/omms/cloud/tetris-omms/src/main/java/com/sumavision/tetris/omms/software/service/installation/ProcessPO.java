/**
 * 
 */
package com.sumavision.tetris.omms.software.service.installation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月10日 下午4:07:05
 */
@Entity
@Table(name = "TETRIS_OMMS_PROCESS")
public class ProcessPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 进程名称 */
	private String processId;
	
	/* 进程别名 */
	private String processName;
	
	/* 安装包id */
	private Long installationPackageId;

	@Column(name = "PROCESS_ID")
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Column(name = "PROCESS_NAME")
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Column(name = "INSTALLATION_PACKAGE_ID")
	public Long getInstallationPackageId() {
		return installationPackageId;
	}

	public void setInstallationPackageId(Long installationPackageId) {
		this.installationPackageId = installationPackageId;
	}
	
	
}
