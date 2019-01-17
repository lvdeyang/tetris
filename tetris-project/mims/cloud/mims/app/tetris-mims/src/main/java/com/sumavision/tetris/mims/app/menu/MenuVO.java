package com.sumavision.tetris.mims.app.menu;

import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 菜单项<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月19日 下午4:59:19
 */
public class MenuVO extends AbstractBaseVO<MenuVO, MenuPO>{

	/** 菜单名称 */
	private String title;
	
	/** 菜单链接 */
	private String link;
	
	/** 菜单图标 */
	private String icon;
	
	/** 菜单图标微调 */
	private String style;
	
	/** 是否是当前菜单 */
	private boolean active;
	
	/** 子菜单 */
	private List<MenuVO> sub;
	
	public String getTitle() {
		return title;
	}

	public MenuVO setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getLink() {
		return link;
	}

	public MenuVO setLink(String link) {
		this.link = link;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MenuVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MenuVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public boolean isActive() {
		return active;
	}

	public MenuVO setActive(boolean active) {
		this.active = active;
		return this;
	}

	public List<MenuVO> getSub() {
		return sub;
	}

	public MenuVO setSub(List<MenuVO> sub) {
		this.sub = sub;
		return this;
	}

	@Override
	public MenuVO set(MenuPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setTitle(entity.getTitle())
			.setLink(entity.getLink())
			.setIcon(entity.getIcon())
			.setStyle(entity.getStyle());
		return this;
	}
	
}
