package com.sumavision.tetris.zoom;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.zoom.exception.SourceGroupNotFoundException;
import com.sumavision.tetris.zoom.favorites.FavoritesDAO;
import com.sumavision.tetris.zoom.favorites.FavoritesPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class SourceGroupService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SourceGroupDAO sourceGroupDao;
	
	@Autowired
	private FavoritesDAO favoritesDao;
	
	/**
	 * 修改资源分组名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:47:38
	 * @param Long id 收藏夹分组id
	 * @param String name 收藏夹分组名称
	 * @return SourceGroupVO 收藏夹分组数据
	 */
	public SourceGroupVO editName(Long id, String name) throws Exception{
		SourceGroupPO sourceGroup = sourceGroupDao.findOne(id);
		if(sourceGroup == null){
			throw new SourceGroupNotFoundException(id);
		}
		sourceGroup.setName(name);
		sourceGroupDao.save(sourceGroup);
		return new SourceGroupVO().set(sourceGroup);
	}
	
	/**
	 * 创建收藏夹分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午10:49:39
	 * @param String name 分组名称
	 * @return SourceGroupVO 分组
	 */
	public SourceGroupVO createFavorites(String name) throws Exception{
		
		UserVO user = userQuery.current();
		
		SourceGroupPO sourceGroup = new SourceGroupPO();
		sourceGroup.setName(name);
		sourceGroup.setType(SourceGroupType.FAVORITES);
		sourceGroup.setUserId(user.getId().toString());
		sourceGroup.setUpdateTime(new Date());
		sourceGroupDao.save(sourceGroup);
		
		return new SourceGroupVO().set(sourceGroup);
	}
	
	/**
	 * 删除收藏夹组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:01:41
	 * @param Long id 收藏夹组id
	 */
	public void removeFavorites(Long id) throws Exception{
		SourceGroupPO sourceGroup = sourceGroupDao.findOne(id);
		if(sourceGroup == null) return;
		List<FavoritesPO> favorites = favoritesDao.findBySourceGroupId(sourceGroup.getId());
		if(favorites!=null && favorites.size()>0){
			for(FavoritesPO favorite:favorites){
				favorite.setSourceGroupId(null);
			}
			favoritesDao.save(favorites);
		}
		sourceGroupDao.delete(sourceGroup);
	}
	
	/**
	 * 向收藏夹分组下添加收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:20:40
	 * @param Long id 收藏夹分组id
	 * @param Collection<Long> favoritesIds 收藏夹id列表
	 */
	public void appendFavorites(
			Long id, 
			Collection<Long> favoritesIds) throws Exception{
		
		SourceGroupPO sourceGroup = sourceGroupDao.findOne(id);
		if(sourceGroup == null){
			throw new SourceGroupNotFoundException(id);
		}
		List<FavoritesPO> favorites = favoritesDao.findAll(favoritesIds);
		if(favorites!=null && favorites.size()>0){
			for(FavoritesPO favorite:favorites){
				favorite.setSourceGroupId(sourceGroup.getId());
			}
			favoritesDao.save(favorites);
		}
	}
	
	/**
	 * 从收藏夹分组中移除收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:41:06
	 * @param Collection<Long> favoritesIds 收藏夹id列表
	 */
	public void removeFavorites(Collection<Long> favoritesIds) throws Exception{
		if(favoritesIds==null || favoritesIds.size()<=0) return;
		List<FavoritesPO> favorites = favoritesDao.findAll(favoritesIds);
		if(favorites!=null && favorites.size()>0){
			for(FavoritesPO favorite:favorites){
				favorite.setSourceGroupId(null);
			}
			favoritesDao.save(favorites);
		}
	}
	
	/**
	 * 收藏夹更换分组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 上午11:59:21
	 * @param Long id 目标收藏夹分组id
	 * @param Collection<Long> favoritesIds 收藏夹id列表
	 */
	public void changeFavorites(
			Long id, 
			Collection<Long> favoritesIds) throws Exception{
		
		SourceGroupPO sourceGroup = sourceGroupDao.findOne(id);
		if(sourceGroup == null){
			throw new SourceGroupNotFoundException(id);
		}
		
		List<FavoritesPO> favorites = favoritesDao.findAll(favoritesIds);
		if(favorites!=null && favorites.size()>0){
			for(FavoritesPO favorite:favorites){
				favorite.setSourceGroupId(sourceGroup.getId());
			}
			favoritesDao.save(favorites);
		}
		
	}
	
}
