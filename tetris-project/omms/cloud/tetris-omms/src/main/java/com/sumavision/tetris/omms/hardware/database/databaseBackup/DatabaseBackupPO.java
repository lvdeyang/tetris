package com.sumavision.tetris.omms.hardware.database.databaseBackup;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_OMMS_DATABASEBACKUP")
public class DatabaseBackupPO extends AbstractBasePO{
	
	private String name;
	
	/** 保存的文件名称*/
	private String filename;
	
	/**数据库id*/
	private Long databaseId;
	
	/**备份数据库路径*/
	private String path;
	
	/**创建时间*/
	private String date;
	
	/**说明*/
	private String remark;
	
	private String downuri;
	
	/** 备份的数据库名*/
	private String backupname;
	
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBackupname() {
		return backupname;
	}

	public void setBackupname(String backupname) {
		this.backupname = backupname;
	}

	public String getDownuri() {
		return downuri;
	}

	public void setDownuri(String downuri) {
		this.downuri = downuri;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
