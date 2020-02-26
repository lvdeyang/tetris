package com.sumavision.bvc.system.enumeration;


import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 参数模板用途<br/> 
 * @author czg
 * @date 2018年8月22日 下午5:21:09 
 */
public enum AvtplUsageType {

	VOD("点播系统"),
	WEB_PLAYER("播放器预览"),
	COMMAND("会议系统."),
	DEVICE_GROUP("设备组"),
	STB("机顶盒"),
	MOBILE("手机终端");
	
	private String name;
	
	private AvtplUsageType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取模板用途<br/> 
	 * @param name 名称
	 * @throws Exception 
	 * @return AvtplUsageType
	 */
	public static AvtplUsageType fromName(String name) throws Exception{
		AvtplUsageType[] values = AvtplUsageType.values();
		for(AvtplUsageType value:values){
			if (value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
