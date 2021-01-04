/**
 * 
 */
package com.sumavision.tetris.omms.hardware.database;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

/**
 * 类型概述<br/>
 * <b>作者:</b>jiajun<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月2日 下午5:29:39
 */
@RepositoryDefinition(domainClass = DatabasePO.class, idClass = Long.class)
public interface DatabaseDAO extends BaseDAO<DatabasePO>{
	
	public List<DatabasePO> findByServerId(Long serverId);

	public DatabasePO findByDatabaseIP(String databaseIP);
	
	public DatabasePO findByDatabaseIPAndDatabasePort(String databaseIP, String databasePort);
}
