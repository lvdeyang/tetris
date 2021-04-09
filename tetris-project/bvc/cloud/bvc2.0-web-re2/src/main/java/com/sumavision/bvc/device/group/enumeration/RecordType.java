package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 录制的类型：录制角色(动态)和录制视频(非动态) 
 * @author lvdeyang
 * @date 2018年8月8日 下午4:34:10 
 */
public enum RecordType {

	SCHEME("录制方案"),
	VIDEO("视频配置"),
	PUBLISH("发布直播"),//发布直播rtmp
	BUNDLE("设备录制");
	
	private String name;
	
	private RecordType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static RecordType fromName(String name) throws Exception{
		RecordType[] values = RecordType.values();
		for(RecordType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
