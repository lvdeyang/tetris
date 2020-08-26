package com.sumavision.tetris.omms.hardware.server.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServerHardDiskDataPO.class, idClass = Long.class)
public interface ServerHardDiskDataDAO extends BaseDAO<ServerHardDiskDataPO>{

	/**
	 * 根据服务器查询最新数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午11:17:58
	 * @param Long serverId 服务器id
	 * @return List<ServerHardDiskDataPO> 服务器硬盘信息
	 */
	@Query(value = "SELECT * FROM TETRIS_OMMS_SERVER_HARD_DISK_DATA WHERE SERVER_ID=?1 and UPDATE_TIME = (SELECT MAX(UPDATE_TIME) FROM TETRIS_OMMS_SERVER_HARD_DISK_DATA)", nativeQuery = true)
	public List<ServerHardDiskDataPO> findLastDataByServerId(Long serverId);
	
}
