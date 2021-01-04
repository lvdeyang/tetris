package com.sumavision.tetris.omms.hardware.database.databases;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_OMMS_DATABASES")
public class DatabasesPO extends AbstractBasePO{

	/** 服务数据库名称 */
	private String name;
	
	/** 服务数据库真实名称 */
	private String realName;
	
	/** 数据库id */
	private Long databaseId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}
	
}
