package com.sumavision.tetris.record;

import java.util.Map;

import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.streamTranscoding.StreamTranscodingAdapter;

public enum RecordRequestType {
	ADD_RECORD("添加录制","/cps-record-web/wolive/record/add"),
	DELETE_RECORD("删除录制","/cps-record-web/wolive/record/delete");
	
	private String action;
	private String url;
	
	private RecordRequestType(String action, String url){
		this.action = action;
		this.url = url;
	}

	public String getAction() {
		return action;
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
		StreamTranscodingAdapter streamTranscodingAdapter = SpringContext.getBean(StreamTranscodingAdapter.class);
		
		return streamTranscodingAdapter.getRecordInfo();
	}
	
	/**
	 * 根据请求类型名称获取类型<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午5:17:19
	 * @param typeName 名称
	 * @return RequestCmdType 类型
	 */
	public static RecordRequestType fromName(String typeName) throws Exception{
		RecordRequestType[] values = RecordRequestType.values();
		for(RecordRequestType value:values){
			if(value.getUrl().equals(typeName)){
				return value;
			}
		}
		throw new ErrorTypeException("typeName", typeName);
	}
}
