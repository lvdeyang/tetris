package com.sumavision.tetris.p2p.room;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = RoomPO.class, idClass = Long.class)
public interface RoomDAO extends BaseDAO<RoomPO>{

	/**
	 * 根据房间标识查询房间信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 上午11:14:30
	 * @param String roomUuid 房间号标识
	 * @return RoomPO 房间信息
	 */
	public RoomPO findByRoomUuid(String roomUuid);
	
	/**
	 * 根据用户id查询房间信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 上午11:16:17
	 * @param Long userId 用户id
	 * @return List<RoomPO>  房间信息
	 */
	public List<RoomPO> findByInitiatorIdOrCalleeId(Long initialorId, Long calleeId);
	
}
