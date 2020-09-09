package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 是否将录制转换成点播 <br/>
 * @Description: 停止录制时，是否将录制转换成点播<br/>
 * @author zsy
 * @date 2018年12月11日 下午1:51:24 
 */
public enum RecordToVodType {

	NOTTOVOD("保存成点播", "0"),
	TOVOD("不保存成点播", "1");
	
	private String name;
	
	private String protocalId;
		
	private RecordToVodType(String name, String protocalId){
		this.name = name;
		this.protocalId = protocalId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocalId(){
		return this.protocalId;
	}
	
	/**
	 * @Title: 根据名称获取类型 
	 * @param name
	 * @throws Exception 
	 * @return RecordToVodType 设备组类型 
	 */
	public static RecordToVodType fromName(String name) throws Exception{
		RecordToVodType[] values = RecordToVodType.values();
		for(RecordToVodType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
