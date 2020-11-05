package com.sumavision.tetris.zoom.favorites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_FAVORITES")
public class FavoritesPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 收藏夹名称 */
	private String name;
	
	/** 收藏对象id */
	private String favoritesId;
	
	/** 收藏对象类型 */
	private FavoritesType type;
	
	/** 用户id */
	private String userId;
	
	/** 分组id */
	private Long sourceGroupId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "FAVORITES_ID")
	public String getFavoritesId() {
		return favoritesId;
	}

	public void setFavoritesId(String favoritesId) {
		this.favoritesId = favoritesId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public FavoritesType getType() {
		return type;
	}

	public void setType(FavoritesType type) {
		this.type = type;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "SOURCE_GROUP_ID")
	public Long getSourceGroupId() {
		return sourceGroupId;
	}

	public void setSourceGroupId(Long sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
	}
	
}
