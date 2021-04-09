package com.sumavision.tetris.bvc.business.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sumavision.bvc.device.command.bo.MessageSendCacheBO;
import com.sumavision.bvc.device.group.bo.LogicBO;

public class BusinessReturnBO {
	
	/** true命令合并执行下发，false命令分段执行下发*/
	private Boolean segmentedExecute = Boolean.FALSE;
	
	public Boolean getSegmentedExecute() {
		return segmentedExecute;
	}

	public BusinessReturnBO setSegmentedExecute(Boolean segmentedExecute) {
		this.segmentedExecute = segmentedExecute;
		return this;
	}

	/** 逻辑层协议 */
	private LogicBO logic;
	
	private Map<MessageSendCacheBO,String> websocketCaches = new HashMap<MessageSendCacheBO,String>();

	public LogicBO getLogic() {
		return logic;
	}
	
	public BusinessReturnBO setLogic(LogicBO logic) {
		this.logic = logic;
		return this;
	}

	public Map<MessageSendCacheBO,String> getWebsocketCaches() {
		return websocketCaches;
	}

	public BusinessReturnBO setWebsocketCaches(Map<MessageSendCacheBO,String> websocketCaches) {
		this.websocketCaches = websocketCaches;
		return this;
	}
	
}
