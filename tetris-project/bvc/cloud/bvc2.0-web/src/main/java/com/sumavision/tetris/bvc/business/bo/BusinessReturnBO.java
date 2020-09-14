package com.sumavision.tetris.bvc.business.bo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.group.bo.LogicBO;

public class BusinessReturnBO {
	
	/** 逻辑层协议 */
	private LogicBO logic;
	
	private List<MessageSendCacheBO> websocketCaches = new ArrayList<MessageSendCacheBO>();

	public LogicBO getLogic() {
		return logic;
	}
	
	public BusinessReturnBO setLogic(LogicBO logic) {
		this.logic = logic;
		return this;
	}

	public List<MessageSendCacheBO> getWebsocketCaches() {
		return websocketCaches;
	}

	public BusinessReturnBO setWebsocketCaches(List<MessageSendCacheBO> websocketCaches) {
		this.websocketCaches = websocketCaches;
		return this;
	}
	
}
