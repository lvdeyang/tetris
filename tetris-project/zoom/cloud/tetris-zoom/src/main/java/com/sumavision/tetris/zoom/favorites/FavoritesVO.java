package com.sumavision.tetris.zoom.favorites;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class FavoritesVO extends AbstractBaseVO<FavoritesVO, FavoritesPO>{

	/** 收藏对象id */
	private String favoritesId;
	
	/** 收藏对象类型 */
	private String type;
	
	/** 用户id */
	private String userId;
	
	/** 分组id */
	private Long sourceGroupId;
	
	public String getFavoritesId() {
		return favoritesId;
	}

	public FavoritesVO setFavoritesId(String favoritesId) {
		this.favoritesId = favoritesId;
		return this;
	}

	public String getType() {
		return type;
	}

	public FavoritesVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public FavoritesVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public Long getSourceGroupId() {
		return sourceGroupId;
	}

	public FavoritesVO setSourceGroupId(Long sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
		return this;
	}

	@Override
	public FavoritesVO set(FavoritesPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFavoritesId(entity.getFavoritesId())
			.setType(entity.getType().toString())
			.setUserId(entity.getUserId())
			.setSourceGroupId(entity.getSourceGroupId());
		return this;
	}
	
}
