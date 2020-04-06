package com.sumavision.tetris.websocket.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * websocket session 开启事件<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月23日 上午9:29:39
 */
public class WebsocketSessionOpenEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private Long userId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public WebsocketSessionOpenEvent(Object source, Long userId) {
		super(source);
		this.userId = userId;
	}

}
