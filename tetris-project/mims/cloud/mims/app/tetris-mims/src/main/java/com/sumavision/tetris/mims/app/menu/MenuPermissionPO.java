package com.sumavision.tetris.mims.app.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.sumavision.tetris.mims.app.user.UserClassify;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 菜单权限<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月20日 上午11:55:29
 */
@Entity
@Table(name = "MIMS_MENU_PERMISSION")
public class MenuPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 菜单id */
	private Long menuId;
	
	/** 用户分类 */
	private UserClassify userClassify;

	@Column(name = "MENU_ID")
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "USER_CLASSIFY")
	public UserClassify getUserClassify() {
		return userClassify;
	}

	public void setUserClassify(UserClassify userClassify) {
		this.userClassify = userClassify;
	}
	
}
