package com.sumavision.tetris.transcoding;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RequestUrlType {
	ADD_TASK_RUL("/mpp/rest/transcode/transcode"),
	GET_TEMPLETE_NAME_LIST_URL("/mpp/rest/transcode/transcode"),
	GET_STATUS("/mpp/rest/transcode/transcode");
	
	private String url;
	private String ip = "192.165.58.167";
	private String port = "8180";

	public String getUrl() {
		return "http://" + ip + ":" + port + url;
	}

	private RequestUrlType(String url){
		this.url = url;
	}
	
	/**
	 * 根据请求类型名称获取类型<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午5:17:19
	 * @param typeName 名称
	 * @return RequestCmdType 类型
	 */
	public static RequestUrlType fromName(String typeName) throws Exception{
		RequestUrlType[] values = RequestUrlType.values();
		for(RequestUrlType value:values){
			if(value.getUrl().equals(typeName)){
				return value;
			}
		}
		throw new ErrorTypeException("typeName", typeName);
	}
}
