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
}
