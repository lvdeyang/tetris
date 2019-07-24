package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ResourceSendPO.class, idClass = Long.class)
public interface ResourceSendDAO extends BaseDAO<ResourceSendPO>{
	/**
	 * 根据频道id获取频道上次播发的媒资列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return List<ResourceSendPO> 地区列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CS_SEND_RESOURCE WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<ResourceSendPO> findByChannelId(Long reg1);
	
	/**
	 * 根据频道id删除频道上次播发的媒资列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return List<ResourceSendPO> 地区列表
	 */
	@Modifying
	@Query(value = "delete from TETRIS_CS_SEND_RESOURCE where channel_id=?1 ", nativeQuery = true)
	public void removeByChannelId(String channelId);
}
