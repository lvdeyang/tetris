package com.sumavision.tetris.oldCMS.passthrough;

import java.util.Map;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.oldCMS.StreamAdapter;

public enum PassthroughRequestType {
	ADD_TASK("添加透传任务","/mediaProcess-ui-publication/outInterface/pubcms/stream2StreamForEdAction.action"),
	ADD_OUTPUT("流透传添加输出","/mediaProcess-ui-publication/outInterface/pubcms/addStreamOutputEdAction.action"),
	DELETE_OUTPUT("流透传删除输出","/mediaProcess-ui-publication/outInterface/pubcms/delStreamOutputEdAction.action");
	
	private String action;
	private String url;
	
	public String getAction() {
		return action;
	}

	private PassthroughRequestType(String action, String url) {
		this.action = action;
		this.url = url;
	}
	
	public String getIp() throws Exception{
		Map<String, String> map = getRecordInfo();
		return map.get("ip");
	}

	public String getUrl() throws Exception{
		Map<String, String> map = getRecordInfo();
		return "http://" + map.get("ip") + ":" + map.get("port") + url;
	}
	
	private Map<String, String> getRecordInfo() throws Exception{
		StreamAdapter streamAdapter = SpringContext.getBean(StreamAdapter.class);
		
		return streamAdapter.getOldCMSInfo();
	}
}
