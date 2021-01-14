package com.sumavision.tetris.omms.hardware.database.databases;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DatabasesVO extends AbstractBaseVO<DatabasesVO,DatabasesPO>{
	/** 服务数据库名称 */
	private String name;
	
	/** 服务数据库真实名称 */
	private String realName;
	
	/** 数据库id */
	private Long databaseId;

	public String getName() {
		return name;
	}

	public DatabasesVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRealName() {
		return realName;
	}

	public DatabasesVO setRealName(String realName) {
		this.realName = realName;
		return this;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public DatabasesVO setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
		return this;
	}

	@Override
	public DatabasesVO set(DatabasesPO entity) throws Exception {
		this.setId(entity.getId())
		.setDatabaseId(entity.getDatabaseId())
		.setName(entity.getName())
		.setRealName(entity.getRealName());
		
		return this;
	}
	
}
