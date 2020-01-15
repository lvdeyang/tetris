package com.sumavision.bvc.command.group.user.setting;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 
* @ClassName: CastMode
* @Description: 单播/组播模式
* @author zsy
* @date 2019年11月29日 上午10:15:06 
*
 */
public enum CastMode {
	
	UNICAST("单播", "unicast"),
	MULTICAST("组播", "multicast");
	
	private String name;
	
	private String code;
	
	private CastMode(String name, String code){
		this.name = name;
		this.code = code;
	}

	public String getName(){
		return this.name;
	}
	
	public String getCode() {
		return code;
	}

	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static CastMode fromName(String name) throws Exception{
		CastMode[] values = CastMode.values();
		for(CastMode value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

	/**
	 * @Title: 根据code获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static CastMode fromCode(String code) throws Exception{
		CastMode[] values = CastMode.values();
		for(CastMode value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
