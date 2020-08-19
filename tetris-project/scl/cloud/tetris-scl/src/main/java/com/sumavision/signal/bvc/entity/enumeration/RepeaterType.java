package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 流转发器类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午5:36:35
 */
public enum RepeaterType {

	MAIN("主"),
	BACKUP("备份");
	
	private String name;
	
	private RepeaterType(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * 根据名称查询<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午5:36:00
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static final RepeaterType fromName(String name) throws Exception{
		RepeaterType values[] = RepeaterType.values();
		for(RepeaterType value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
