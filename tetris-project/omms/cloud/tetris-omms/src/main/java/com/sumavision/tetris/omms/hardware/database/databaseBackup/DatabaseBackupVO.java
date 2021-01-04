package com.sumavision.tetris.omms.hardware.database.databaseBackup;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DatabaseBackupVO extends AbstractBaseVO<DatabaseBackupVO,DatabaseBackupPO>{

	private String name;
	
	/**数据库id*/
	private Long databaseId;
	
	/**备份数据库路径*/
	private String path;
	
	/**创建时间*/
	private String date;
	
	/**说明*/
	private String remark;
	
	/** 备份的数据库名*/
	private String backupname;
	
	public String getBackupname() {
		return backupname;
	}

	public DatabaseBackupVO setBackupname(String backupname) {
		this.backupname = backupname;
		return this;
	}

	public String getDate() {
		return date;
	}

	public DatabaseBackupVO setDate(String date) {
		this.date = date;
		return this;
	}

	public String getName() {
		return name;
	}

	public DatabaseBackupVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public DatabaseBackupVO setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
		return this;
	}

	public String getPath() {
		return path;
	}

	public DatabaseBackupVO setPath(String path) {
		this.path = path;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public DatabaseBackupVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	@Override
	public DatabaseBackupVO set(DatabaseBackupPO entity) throws Exception {
		this.setName(entity.getName())
			.setDate(entity.getDate())
			.setDatabaseId(entity.getDatabaseId())
			.setRemark(entity.getRemark())
			.setId(entity.getId())
			.setBackupname(entity.getBackupname());
		return this;
	}

}
