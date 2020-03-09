package com.sumavision.tetris.zoom.favorites;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = FavoritesPO.class, idClass = Long.class)
public interface FavoritesDAO extends BaseDAO<FavoritesPO>{

	/**
	 * 查询收藏夹组下的所有收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午10:58:13
	 * @param Long sourceGroupId 收藏夹组id
	 * @return List<FavoritesPO> 收藏夹列表
	 */
	public List<FavoritesPO> findBySourceGroupId(Long sourceGroupId);
	
}
