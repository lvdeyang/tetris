package com.sumavision.tetris.omms.software.service.installation;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class InstallationPackageVO extends AbstractBaseVO<InstallationPackageVO, InstallationPackagePO>{
	
	/** 服务类型id */
	private Long serviceTypeId;
	
	/** 文件路径 */
	private String filePath;
	
	/** 文件名称 */
	private String fileName;
	
	/** 版本号 */
	private String version;
	
	/** 备注 */
	private String remark;
	
	/** 创建人 */
	private String creator;
	
	/** 创建时间 */
	private String createTime;
	
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public InstallationPackageVO setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
		return this;
	}

	public String getFilePath() {
		return filePath;
	}

	public InstallationPackageVO setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public InstallationPackageVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public InstallationPackageVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public InstallationPackageVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getCreator() {
		return creator;
	}

	public InstallationPackageVO setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public InstallationPackageVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	@Override
	public InstallationPackageVO set(InstallationPackagePO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setServiceTypeId(entity.getServiceTypeId())
			.setFilePath(entity.getFilePath())
			.setFileName(entity.getFileName())
			.setVersion(entity.getVersion())
			.setRemark(entity.getRemark())
			.setCreator(entity.getCreator())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern));
		return this;
	}

}
