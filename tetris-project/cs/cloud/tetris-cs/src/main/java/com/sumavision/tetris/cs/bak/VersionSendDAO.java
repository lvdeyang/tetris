package com.sumavision.tetris.cs.bak;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = VersionSendPO.class, idClass = Long.class)
public interface VersionSendDAO extends BaseDAO<VersionSendPO>{
	/**
	 * 根据频道id获取频道上次播发的版本列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return List<ResourceSendPO> 地区列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CS_SEND_VERSION WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<VersionSendPO> findByChannelId(Long reg1);
	
	/**
	 * 根据播发id获取频道播发的版本信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param broadId 播发id
	 * @return VersionSendPO 版本信息
	 */
	public VersionSendPO findByBroadId(String broadId);
	
	/**
	 * 根据频道id和版本号获取<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月24日 上午11:54:40
	 * @param Long channelId 频道id
	 * @param String version 版本号
	 * @return VersionSendPO 版本信息
	 */
	public VersionSendPO findByChannelIdAndVersion(Long channelId,String version);
	
	/**
	 * 根据频道id和播发id获取频道播发的版本信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadId 播发id
	 * @return VersionSendPO 版本信息
	 */
	public VersionSendPO findByChannelIdAndBroadId(Long channelId,String broadId);
	
	/**
	 * 根据频道id查询（根据播发id降序）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月24日 下午12:07:12
	 * @param Long channelId 频道id 
	 * @return List<VersionSendPO> 版本信息数组
	 */
	public List<VersionSendPO> findByChannelIdOrderByBroadIdDesc(Long channelId);
	
	/**
	 * 根据频道id和播发类型查询（根据播发id降序）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月24日 下午12:04:44
	 * @param Long channelId 频道id
	 * @param String fileType 播发类型
	 * @return List<VersionSendPO> 版本信息数组
	 */
	public List<VersionSendPO> findByChannelIdAndFileTypeOrderByBroadIdDesc(Long channelId, VersionSendType fileType);
	
	/**
	 * 根据频道id和播发类型查询（根据播发id降序）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月24日 下午12:04:44
	 * @param Long channelId 频道id
	 * @param String fileType 播发类型
	 * @return List<VersionSendPO> 版本信息数组
	 */
	public List<VersionSendPO> findByChannelIdAndFileTypeNotOrderByBroadIdDesc(Long channelId, VersionSendType fileType);
	
	/**
	 * 根据频道id删除频道上次播发的版本列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @return List<ResourceSendPO> 地区列表
	 */
	@Modifying
	@Query(value = "delete from TETRIS_CS_SEND_VERSION where channel_id=?1 ", nativeQuery = true)
	public void deleteByChannelId(Long channelId);
}
