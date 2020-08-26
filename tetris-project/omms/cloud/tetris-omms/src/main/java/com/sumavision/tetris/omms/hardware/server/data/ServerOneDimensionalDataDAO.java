package com.sumavision.tetris.omms.hardware.server.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ServerOneDimensionalDataPO.class, idClass = Long.class)
public interface ServerOneDimensionalDataDAO extends BaseDAO<ServerOneDimensionalDataPO>{

	/**
	 * 根据服务器查询最新数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午11:17:58
	 * @param Long serverId 服务器id
	 * @return ServerOneDimensionalDataPO 服务器一维数据信息
	 */
	@Query(value = "SELECT * FROM TETRIS_OMMS_SERVER_ONE_DIMENSIONAL_DATA WHERE SERVER_ID=?1 and UPDATE_TIME = (SELECT MAX(UPDATE_TIME) FROM TETRIS_OMMS_SERVER_ONE_DIMENSIONAL_DATA)", nativeQuery = true)
	public ServerOneDimensionalDataPO findLastDataByServerId(Long serverId);
	
}
