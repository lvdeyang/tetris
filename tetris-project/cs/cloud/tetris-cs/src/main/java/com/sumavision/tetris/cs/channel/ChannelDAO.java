package com.sumavision.tetris.cs.channel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ChannelPO.class, idClass = Long.class)
public interface ChannelDAO extends BaseDAO<ChannelPO>{
	/**
	 * 根据组织id分页查询频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午5:09:08
	 * @param groupId 组织id
	 * @param pageable 分页
	 * @return Page<ChannelPO> 频道列表
	 */
	@Query(value = "SELECT channel.* FROM TETRIS_CS_CHANNEL channel WHERE channel.group_id = ?1 AND channel.type = ?2 \n#pageable\n",
			countQuery = "SELECT count(channel.id) FROM TETRIS_CS_CHANNEL channel WHERE channel.group_id = ?1 AND channel.type = ?2",
			nativeQuery = true)
	public Page<ChannelPO> PagefindAllByGroupIdAndType(String groupId, String type, Pageable pageable);
	
	@Query(value = "SELECT channel.* FROM tetris_cs_channel channel where channel.group_id = ?1 AND channel.type = ?2", nativeQuery = true)
	public List<ChannelPO> findAllByGroupIAndType(String groupId, String type);
	
	/**
	 * 根据组织id分页查询频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午5:09:08
	 * @param groupId 组织id
	 * @param pageable 分页
	 * @return Page<ChannelPO> 频道列表
	 */
	@Query(value = "SELECT channel.* FROM TETRIS_CS_CHANNEL channel WHERE channel.group_id = ?1 AND channel.type = ?2 ", nativeQuery = true)
	public List<ChannelPO> findAllByGroupIdAndType(String groupId, ChannelType type);
	
	/**
	 * 判断播发后输出的ip和port是否被使用(仅针对能力播发，修改频道时使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午5:09:08
	 * @return ChannelPO 频道信息
	 */
	@Query(value = "SELECT * FROM TETRIS_CS_CHANNEL WHERE id <> ?1 AND preview_url_ip = ?2 AND preview_url_port = ?3", nativeQuery = true)
	public ChannelPO checkIpAndPortExists(Long channelId, String previewUrlIp, String previewUrlPort);
	
	/**
	 * 判断播发后输出的ip和port是否被使用(仅针对能力播发，新建频道时使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午5:09:08
	 * @return ChannelPO 频道信息
	 */
	@Query(value = "SELECT * FROM TETRIS_CS_CHANNEL WHERE preview_url_ip = ?1 AND preview_url_port = ?2", nativeQuery = true)
	public ChannelPO checkIpAndPortExists(String previewUrlIp, String previewUrlPort);
	
	@Query(value = "SELECT ABILITY_BROAD_ID FROM TETRIS_CS_CHANNEL WHERE ABILITY_BROAD_ID is not null", nativeQuery = true)
	public List<Integer> getAllAbilityId();
	
	public List<ChannelPO> findByBroadWayAndBroadcastStatus(String broadWay, String broadStatus);
}
