package com.sumavision.tetris.bvc.business.bo;

import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.command.group.vod.CommandVodPO;

/**
 * 用户及其对应的终端类型<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月9日 上午10:43:58
 */
public class UserTerminalBO {
	
	/** 用户id */
	private String userId;
	
	/** 终端类型 */
	private Long terminalId;

	public String getUserId() {
		return userId;
	}

	public UserTerminalBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public UserTerminalBO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}
	
}
