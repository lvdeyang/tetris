package com.sumavision.tetris.system.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 存储<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午5:27:11
 */
@Entity
@Table(name = "TETRIS_SYSTEM_STORAGE")
public class SystemStoragePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 存储名称 */
	private String name;
	
	/** 存储根路径 */
	private String rootPath;
	
	/** ftp根路径 */
	private String baseFtpPath;
	
	/** 预览http根路径 */
	private String basePreviewPath;
	
	/** 小工具访问http根路径 */
	private String gadgetBasePath;
	
	/** 服务小工具类型 */
	private ServerGadgetType serverGadgetType;
	
	/** 存储空间总大小，单位：B */
	private Long totalSize;
	
	/** 已使用存储空间大小 ，单位：B*/
	private Long usedSize;
	
	/** 剩余存储空间大小 ，单位：B*/
	private Long freeSize;
	
	/** 服务状态 */
	private Integer status;
	
	/** 备注 */
	private String remark;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ROOT_PATH")
	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Column(name = "BASE_FTP_PATH")
	public String getBaseFtpPath() {
		return baseFtpPath;
	}

	public void setBaseFtpPath(String baseFtpPath) {
		this.baseFtpPath = baseFtpPath;
	}

	@Column(name = "BASE_PREVIEW_PATH")
	public String getBasePreviewPath() {
		return basePreviewPath;
	}

	public void setBasePreviewPath(String basePreviewPath) {
		this.basePreviewPath = basePreviewPath;
	}

	@Column(name = "GADGET_BASE_PATH")
	public String getGadgetBasePath() {
		return gadgetBasePath;
	}

	public void setGadgetBasePath(String gadgetBasePath) {
		this.gadgetBasePath = gadgetBasePath;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SERVER_GADGET_TYPE")
	public ServerGadgetType getServerGadgetType() {
		return serverGadgetType;
	}

	public void setServerGadgetType(ServerGadgetType serverGadgetType) {
		this.serverGadgetType = serverGadgetType;
	}

	@Column(name = "TOTAL_SIZE")
	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	@Column(name = "USED_SIZE")
	public Long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(Long usedSize) {
		this.usedSize = usedSize;
	}

	@Column(name = "FREE_SIZE")
	public Long getFreeSize() {
		return freeSize;
	}

	public void setFreeSize(Long freeSize) {
		this.freeSize = freeSize;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
