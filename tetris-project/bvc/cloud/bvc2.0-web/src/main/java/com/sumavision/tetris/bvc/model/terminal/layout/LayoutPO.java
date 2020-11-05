package com.sumavision.tetris.bvc.model.terminal.layout;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 布局定义<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:15:01
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_LAYOUT")
public class LayoutPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 名称 ,内置屏幕：单屏、1左2右、1大2小、1小2大、四屏、六屏、九屏、十六屏*/
	private String name;
	
	/** web端布局 */
	private String websiteDraw;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "WEBSITEDRAW", length = 1024)
	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public void setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
	}
	
}
