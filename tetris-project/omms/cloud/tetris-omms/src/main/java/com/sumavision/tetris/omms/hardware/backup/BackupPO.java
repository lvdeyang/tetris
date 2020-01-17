package com.sumavision.tetris.omms.hardware.backup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 服务器备份表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 上午11:53:17
 */
@Entity
@Table(name = "TETRIS_OMMS_BACKUP")
public class BackupPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 服务器id */
	private Long serverId;

	/** 一级分类 */
	private BackupType firstType;
	
	/** 二级分类 */
	private BackupType secondType;
	
	/** 三级分类 */
	private BackupType thirdType;
	
	/** 版本说明 TODO 需要格式 */
	private String versions;
	
	/** 备份文件路径 */
	private String filePath;
	
	/** 备注 */
	private String remark;
	
	/** 备份创建者 */
	private String creator;
	
	/** 备份时间 */
	private Date createTime;

	@Column(name = "SERVER_ID")
	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FIRST_TYPE")
	public BackupType getFirstType() {
		return firstType;
	}

	public void setFirstType(BackupType firstType) {
		this.firstType = firstType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SECOND_TYPE")
	public BackupType getSecondType() {
		return secondType;
	}

	public void setSecondType(BackupType secondType) {
		this.secondType = secondType;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "THIRD_TYPE")
	public BackupType getThirdType() {
		return thirdType;
	}

	public void setThirdType(BackupType thirdType) {
		this.thirdType = thirdType;
	}

	@Lob
	@Column(name = "versions", columnDefinition = "LONGTEXT")
	public String getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = versions;
	}

	@Lob
	@Column(name = "FILE_PATH", columnDefinition = "LONGTEXT")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
