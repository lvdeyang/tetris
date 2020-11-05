package com.sumavision.tetris.cs.area;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AreaPO.class, idClass = Long.class)
public interface AreaDAO extends BaseDAO<AreaPO>{
	/**
	 * 获取频道id获取id下的地区列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<AreaPO> 地区列表
	 */
	@Query(value = "SELECT * FROM TETRIS_CS_AREA WHERE CHANNEL_ID LIKE ?1", nativeQuery = true)
	public List<AreaPO> findByChannelId(Long reg1);
	
	/**
	 * 根据地区id和频道id获取该地区信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return AreaPO 地区信息
	 */
	public AreaPO findByChannelIdAndAreaId(Long channelId,String areaId);
	
	/**
	 * 校验地区占用情况<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午5:51:41
	 * @param List<String> areaIds 地区id数组
	 * @param Long channelId 频道id
	 * @return
	 */
	@Query(value = "SELECT area.* FROM TETRIS_CS_AREA area "
			+ "WHERE area.AREA_ID IN ?1 "
			+ "AND area.CHANNEL_ID <> ?2", nativeQuery = true)
	public List<AreaPO> findByAreaIdInWithExceptChannelId(List<String> areaIds, Long channelId);
	
	/**
	 * 校验地区占用情况<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午5:51:41
	 * @param List<String> areaIds 地区id数组
	 * @param Long channelId 频道id
	 * @return
	 */
	@Query(value = "SELECT area.* FROM TETRIS_CS_AREA area "
			+ "LEFT JOIN TETRIS_CS_TERMINAL_BROAD_INFO info ON area.CHANNEL_ID = info.CHANNEL_ID "
			+ "WHERE area.AREA_ID IN ?1 "
			+ "AND area.CHANNEL_ID <> ?2 "
			+ "AND info.LEVEL = ?3", nativeQuery = true)
	public List<AreaPO> findByAreaIdInWithExceptChannelId(List<String> areaIds, Long channelId, String level);
}
