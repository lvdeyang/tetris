package com.sumavision.tetris.oldCMS;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;

public enum OldCMSRequestType {
	ADD_TASK("添加任务", "/mediaProcess-ui-publication/outInterface/pubcms/addStreamTask.action"),
	DELETE_TASK("删除任务", "/mediaProcess-ui-publication/outInterface/pubcms/delTask.action"),
	ADD_OUTPUT("添加输出", "/mediaProcess-ui-publication/outInterface/pubcms/addOutput.action"),
	DELETE_OUTPUT("删除输出", "/mediaProcess-ui-publication/outInterface/pubcms/delOutput.action");
	
	private String action;
	private String url;
	
	private OldCMSRequestType(String action, String url){
		this.action = action;
		this.url = url;
	}

	public String getAction() {
		return action;
	}

	public String getUrl() throws Exception{
		Map<String, String> map = getToolInfo();
		return "http://" + map.get("ip") + ":" + map.get("port") + url;
	}
	
	private Map<String, String> getToolInfo() throws Exception{
		StreamTranscodingAdapter streamTranscodingAdapter = SpringContext.getBean(StreamTranscodingAdapter.class);
		
		String jsonString = streamTranscodingAdapter.readProfile();
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		return new HashMapWrapper<String, String>()
				.put("ip", jsonObject.getString("cmsIp"))
				.put("port", jsonObject.getString("cmsPort"))
				.getMap();
	}
	
	/**
	 * 根据请求类型名称获取类型<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午5:17:19
	 * @param typeName 名称
	 * @return RequestCmdType 类型
	 */
	public static OldCMSRequestType fromName(String typeName) throws Exception{
		OldCMSRequestType[] values = OldCMSRequestType.values();
		for(OldCMSRequestType value:values){
			if(value.getUrl().equals(typeName)){
				return value;
			}
		}
		throw new ErrorTypeException("typeName", typeName);
	}
}
