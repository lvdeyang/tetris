package com.sumavision.tetris.system.storage;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SystemStoragePartitionPO.class, idClass = Long.class)
public interface SystemStoragePartitionDAO extends BaseDAO<SystemStoragePartitionPO>{

	/**
	 * 查询存储分区<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 上午11:42:55
	 * @param Long storageId 存储id
	 * @return List<SystemStoragePartitionPO> 分区列表
	 */
	public List<SystemStoragePartitionPO> findByStorageId(Long storageId);
	
}
