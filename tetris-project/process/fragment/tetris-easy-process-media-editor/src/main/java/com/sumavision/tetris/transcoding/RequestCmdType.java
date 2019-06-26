package com.sumavision.tetris.transcoding;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RequestCmdType {
	ADD_TASK("AddTask"),
	GET_TEMPLATE_NAME_LIST("GetTemplateNameList"),
	GET_STATUS("QueryTask");
	
	private String typeName;
	
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 根据请求类型名称获取类型<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 下午5:17:19
	 * @param typeName 名称
	 * @return RequestCmdType 类型
	 */
	public static RequestCmdType fromName(String typeName) throws Exception{
		RequestCmdType[] values = RequestCmdType.values();
		for(RequestCmdType value:values){
			if(value.getTypeName().equals(typeName)){
				return value;
			}
		}
		throw new ErrorTypeException("typeName", typeName);
	}
	
	private RequestCmdType(String typeName){
		this.typeName = typeName;
	}
}
