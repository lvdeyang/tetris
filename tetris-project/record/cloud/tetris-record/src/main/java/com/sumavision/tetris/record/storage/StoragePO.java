package com.sumavision.tetris.record.storage;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "record_storage")
public class StoragePO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	private String name;

	private String localRecordPath;

	private String ftpBasePath;

	private String ftpUserName;

	private String ftpPassword;

	private String httpBasePath;

	// private Integer size;

	private String description;

	private String localFFMpegOutputPath;

	/** 磁盘清理-录制占用空间阈值(G) **/
	private Integer clean_recordMaxSpaceThreshold;

	/** 磁盘清理-磁盘整体占用比率阈值(G) **/
	private Integer clean_diskUsedSpaceThreshold;

	/** 磁盘清理-任务保存时间阈值(天) **/
	private Integer clean_timeThreshold;

	@Type(type = "yes_no")
	private Boolean isMounted = true;
	
	@Type(type = "yes_no")
	private Boolean isCheckTimeThreshold;
	
	@Type(type = "yes_no")
	private Boolean isCheckRecordMaxSpaceThreshold;

	@Type(type = "yes_no")
	private Boolean isCheckDiskUsedSpacePctThreshold;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalRecordPath() {
		return localRecordPath;
	}

	public void setLocalRecordPath(String localRecordPath) {
		this.localRecordPath = localRecordPath;
	}

	public String getFtpBasePath() {
		return ftpBasePath;
	}

	public void setFtpBasePath(String ftpBasePath) {
		this.ftpBasePath = ftpBasePath;
	}

	public String getFtpUserName() {
		return ftpUserName;
	}

	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getHttpBasePath() {
		return httpBasePath;
	}

	public void setHttpBasePath(String httpBasePath) {
		this.httpBasePath = httpBasePath;
	}
	
	/*
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getClean_recordMaxSpaceThreshold() {
		return clean_recordMaxSpaceThreshold;
	}

	public void setClean_recordMaxSpaceThreshold(Integer clean_recordMaxSpaceThreshold) {
		this.clean_recordMaxSpaceThreshold = clean_recordMaxSpaceThreshold;
	}

	public Integer getClean_diskUsedSpaceThreshold() {
		return clean_diskUsedSpaceThreshold;
	}

	public void setClean_diskUsedSpaceThreshold(Integer clean_diskUsedSpaceThreshold) {
		this.clean_diskUsedSpaceThreshold = clean_diskUsedSpaceThreshold;
	}

	public Integer getClean_timeThreshold() {
		return clean_timeThreshold;
	}

	public void setClean_timeThreshold(Integer clean_timeThreshold) {
		this.clean_timeThreshold = clean_timeThreshold;
	}

	public Boolean getIsCheckTimeThreshold() {
		return isCheckTimeThreshold;
	}

	public void setIsCheckTimeThreshold(Boolean isCheckTimeThreshold) {
		this.isCheckTimeThreshold = isCheckTimeThreshold;
	}

	public Boolean getIsCheckRecordMaxSpaceThreshold() {
		return isCheckRecordMaxSpaceThreshold;
	}

	public void setIsCheckRecordMaxSpaceThreshold(Boolean isCheckRecordMaxSpaceThreshold) {
		this.isCheckRecordMaxSpaceThreshold = isCheckRecordMaxSpaceThreshold;
	}

	public Boolean getIsCheckDiskUsedSpacePctThreshold() {
		return isCheckDiskUsedSpacePctThreshold;
	}

	public void setIsCheckDiskUsedSpacePctThreshold(Boolean isCheckDiskUsedSpacePctThreshold) {
		this.isCheckDiskUsedSpacePctThreshold = isCheckDiskUsedSpacePctThreshold;
	}

	public String getLocalFFMpegOutputPath() {
		return localFFMpegOutputPath;
	}

	public void setLocalFFMpegOutputPath(String localFFMpegOutputPath) {
		this.localFFMpegOutputPath = localFFMpegOutputPath;
	}

	public Boolean getIsMounted() {
		return isMounted;
	}

	public void setIsMounted(Boolean isMounted) {
		this.isMounted = isMounted;
	}

}
