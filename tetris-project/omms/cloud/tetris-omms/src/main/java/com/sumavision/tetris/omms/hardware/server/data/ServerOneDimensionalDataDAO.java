package com.sumavision.tetris.omms.hardware.server.data;

import java.util.List;

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
	@Query(value = "SELECT * FROM TETRIS_OMMS_SERVER_ONE_DIMENSIONAL_DATA WHERE SERVER_ID=?1 and UPDATE_TIME = (SELECT MAX(UPDATE_TIME) FROM TETRIS_OMMS_SERVER_ONE_DIMENSIONAL_DATA WHERE SERVER_ID=?1)", nativeQuery = true)
	public List<ServerOneDimensionalDataPO> findLastDataByServerId(Long serverId);
	
	/**
	 * 根据服务器查询存在告警信息的数据<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午9:47:27
	 * @param serverId 服务器id
	 * @return ServerOneDimensionalDataPO 服务器一维数据信息
	 */
	@Query(value = "SELECT * FROM TETRIS_OMMS_SERVER_ONE_DIMENSIONAL_DATA WHERE SERVER_ID=?1 and ALARM_MESSAGE IS NOT NULL order by ID DESC", nativeQuery = true)
	public List<ServerOneDimensionalDataPO> findByServerIdAndAlarmMessageNotNULL(Long serverId);
	
}
