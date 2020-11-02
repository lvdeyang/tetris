/**
 * 
 */
package com.sumavision.tetris.omms.software.service.installation;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;


/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月10日 下午4:38:34
 */
public class ProcessVO extends AbstractBaseVO<ProcessVO, ProcessPO>{

	/** 进程名称 */
	private String processId;
	
	/* 进程名称 */
	private String processName;
	
	/* 安装包id */
	private Long installationPackageId;
	
	public String getProcessId() {
		return processId;
	}

	public ProcessVO setProcessId(String processId) {
		this.processId = processId;
		return this;
	}

	public String getProcessName() {
		return processName;
	}

	public ProcessVO setProcessName(String processName) {
		this.processName = processName;
		return this;
	}

	public Long getInstallationPackageId() {
		return installationPackageId;
	}

	public ProcessVO setInstallationPackageId(Long installationPackageId) {
		this.installationPackageId = installationPackageId;
		return this;
	}

	@Override
	public ProcessVO set(ProcessPO entity) throws Exception {
		this.setId(entity.getId())
			.setProcessId(entity.getProcessId())
			.setProcessName(entity.getProcessName())
			.setInstallationPackageId(entity.getInstallationPackageId());
		return this;
	}

}
