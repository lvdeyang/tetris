package com.sumavision.tetris.bvc.business.group.demand;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * GroupDemandPO 指挥中转发点播的业务类型 <br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月8日 下午2:20:13
 */
public enum DemandBusinessType {
	
	FORWARD_DEVICE("转发设备", "设备"),
	FORWARD_USER("转发用户", "用户"),
	FORWARD_FILE("转发文件", "文件");
	
	private String name;
	
	private String code;
	
	private DemandBusinessType(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName(){
		return this.name;
	}

	public String getCode(){
		return this.code;
	}
	
	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static DemandBusinessType fromName(String name) throws Exception{
		DemandBusinessType[] values = DemandBusinessType.values();
		for(DemandBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
