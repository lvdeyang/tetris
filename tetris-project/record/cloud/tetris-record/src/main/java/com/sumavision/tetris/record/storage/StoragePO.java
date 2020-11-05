package com.sumavision.tetris.record.storage;

import javax.persistence.Entity;
import javax.persistence.Table;

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

	private Integer size;

	private String description;

	/** 磁盘清理空间阈值(G) **/
	private Integer cleanSpaceThreshold;

	/** 磁盘清理时间阈值(天) **/
	private Integer cleanTimeThreshold;

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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCleanSpaceThreshold() {
		return cleanSpaceThreshold;
	}

	public void setCleanSpaceThreshold(Integer cleanSpaceThreshold) {
		this.cleanSpaceThreshold = cleanSpaceThreshold;
	}

	public Integer getCleanTimeThreshold() {
		return cleanTimeThreshold;
	}

	public void setCleanTimeThreshold(Integer cleanTimeThreshold) {
		this.cleanTimeThreshold = cleanTimeThreshold;
	}

}
