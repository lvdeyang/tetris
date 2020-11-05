package com.sumavision.tetris.omms.software.service.installation.history;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class InstallationPackageHistoryVO extends AbstractBaseVO<InstallationPackageHistoryVO, InstallationPackageHistoryPO>{

	/** 服务类型id */
	private Long serviceTypeId;
	
	/** 文件路径 */
	private String filePath;
	
	/** 版本号 */
	private String version;
	
	/** 备注 */
	private String remark;
	
	/** 创建人 */
	private String creator;
	
	/** 创建时间 */
	@JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8" )
	private Date createTime;
	
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public InstallationPackageHistoryVO setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
		return this;
	}

	public String getFilePath() {
		return filePath;
	}

	public InstallationPackageHistoryVO setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public InstallationPackageHistoryVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public InstallationPackageHistoryVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getCreator() {
		return creator;
	}

	public InstallationPackageHistoryVO setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public InstallationPackageHistoryVO setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	@Override
	public InstallationPackageHistoryVO set(InstallationPackageHistoryPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setServiceTypeId(entity.getServiceTypeId())
			.setFilePath(entity.getFilePath())
			.setVersion(entity.getVersion())
			.setCreator(entity.getCreator())
			.setCreateTime(entity.getCreateTime());
		return this;
	}
}
