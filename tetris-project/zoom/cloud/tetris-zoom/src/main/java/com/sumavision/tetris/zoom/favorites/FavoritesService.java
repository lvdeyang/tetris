package com.sumavision.tetris.zoom.favorites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.zoom.SourceGroupDAO;
import com.sumavision.tetris.zoom.SourceGroupPO;
import com.sumavision.tetris.zoom.SourceGroupType;
import com.sumavision.tetris.zoom.ZoomDAO;
import com.sumavision.tetris.zoom.ZoomPO;
import com.sumavision.tetris.zoom.exception.SourceGroupNotFoundException;
import com.sumavision.tetris.zoom.exception.ZoomNotFoundException;
import com.sumavision.tetris.zoom.favorites.exception.FavoritesNotFoundException;
import com.sumavision.tetris.zoom.history.HistoryDAO;
import com.sumavision.tetris.zoom.history.HistoryPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class FavoritesService {

	@Autowired
	private ZoomDAO zoomDao;
	
	@Autowired
	private FavoritesDAO favoritesDao;
	
	@Autowired
	private SourceGroupDAO sourceGroupDao;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 创建会议收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午2:12:25
	 * @param String code 会议号码
	 * @param Long sourceGroupId 收藏夹分组
	 * @return FavoritesVO 收藏夹数据
	 */
	public FavoritesVO creatZoom(
			String code,
			Long sourceGroupId) throws Exception{
		
		UserVO user = userQuery.current();
		
		ZoomPO zoom = zoomDao.findByCode(code);
		if(zoom == null){
			throw new ZoomNotFoundException(code);
		}
		
		SourceGroupPO sourceGroup = null;
		if(sourceGroupId != null){
			sourceGroup = sourceGroupDao.findOne(sourceGroupId);
			if(sourceGroup == null){
				throw new SourceGroupNotFoundException(sourceGroupId);
			}
		}
		
		FavoritesPO favorites = new FavoritesPO();
		favorites.setName(zoom.getName());
		favorites.setFavoritesId(code);
		favorites.setType(FavoritesType.ZOOM_CODE);
		favorites.setUserId(user.getId().toString());
		favorites.setSourceGroupId(sourceGroup==null?null:sourceGroup.getId());
		favoritesDao.save(favorites);
		
		return new FavoritesVO().set(favorites);
	}
	
	/**
	 * 修改收藏夹名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午2:58:27
	 * @param Long id 收藏夹id
	 * @param String name 收藏夹名称
	 * @return FavoritesVO 收藏夹数据
	 */
	public FavoritesVO editName(
			Long id,
			String name) throws Exception{
		
		FavoritesPO favorites = favoritesDao.findOne(id);
		if(favorites == null){
			throw new FavoritesNotFoundException(id);
		}
		
		favorites.setName(name);
		favoritesDao.save(favorites);
		
		return new FavoritesVO().set(favorites);
	}
	
	/**
	 * 删除收藏夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午3:08:57
	 * @param Long id 收藏夹id
	 */
	public void remove(Long id) throws Exception{
		FavoritesPO favorites = favoritesDao.findOne(id);
		if(favorites != null){
			favoritesDao.delete(favorites);
		}
	}
	
}
