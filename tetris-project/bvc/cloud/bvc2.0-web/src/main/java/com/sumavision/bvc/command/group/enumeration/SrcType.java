package com.sumavision.bvc.command.group.enumeration;

/**
 * 
* @ClassName: SrcType 
* @Description: 源类型，主要用于标识上屏任务
* @author zsy
* @date 2020年5月12日 上午10:56:48 
*
 */
public enum SrcType {

	NONE("无", "none"),
	USER("用户", "user"),
	DEVICE("设备", "device"),
	FILE("文件", "file");
	
	private String name;
	
	private String code;
	
	private SrcType(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}

}
