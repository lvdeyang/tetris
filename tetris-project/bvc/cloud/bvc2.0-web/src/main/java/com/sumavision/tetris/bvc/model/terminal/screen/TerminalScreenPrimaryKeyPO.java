package com.sumavision.tetris.bvc.model.terminal.screen;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 屏幕主键，用作屏幕相关业务之间的关联<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:12:29
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL_SCREEN_ID")
public class TerminalScreenPrimaryKeyPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;
	
	/** 屏幕id定义 ,内置默认screen_1到screen_16*/
	private String screenPrimaryKey;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SCREEN_PRIMARY_KEY")
	public String getScreenPrimaryKey() {
		return screenPrimaryKey;
	}

	public void setScreenPrimaryKey(String screenPrimaryKey) {
		this.screenPrimaryKey = screenPrimaryKey;
	}
	
}
