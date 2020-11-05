package com.sumavision.tetris.streamTranscoding;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ToolRequestType {
	ADD_TASK("添加任务", "/mpp/rest/transcode/transcode"),
	DELETE_TASK("删除任务", "/mpp/rest/transcode/transcode"),
	ADD_OUTPUT("添加输出", "/mpp/rest/transcode/transcode"),
	DELETE_OUTPUT("删除输出", "");
	
	private String action;
	private String url;
	
	private ToolRequestType(String action, String url){
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
				.put("ip", jsonObject.getString("toolIp"))
				.put("port", jsonObject.getString("toolPort"))
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
	public static ToolRequestType fromName(String typeName) throws Exception{
		ToolRequestType[] values = ToolRequestType.values();
		for(ToolRequestType value:values){
			if(value.getUrl().equals(typeName)){
				return value;
			}
		}
		throw new ErrorTypeException("typeName", typeName);
	}
}
