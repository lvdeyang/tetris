package com.sumavision.tetris.bvc.business.terminal.user.layout;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 记录当前用户为终端设置的布局<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:35:08
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_TERMINAL_LAYOUT_USER_PERMISSION")
public class LayoutUserPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 终端类型id */
	private Long terminalId;
	
	/** 终端布局id */
	private Long layoutId;
	
	/** 用户id */
	private String userId;

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
