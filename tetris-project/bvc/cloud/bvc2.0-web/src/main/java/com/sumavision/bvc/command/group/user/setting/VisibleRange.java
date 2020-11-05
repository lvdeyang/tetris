package com.sumavision.bvc.command.group.user.setting;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 
* @ClassName: VisibleRange
* @Description: 可见范围
* @author zsy
* @date 2019年11月29日 上午10:15:06 
*
 */
public enum VisibleRange {
	
	ALL("全网用户", "all"),
	GROUP("组内用户", "group");
	
	private String name;
	
	private String code;
	
	private VisibleRange(String name, String code){
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
	public static VisibleRange fromName(String name) throws Exception{
		VisibleRange[] values = VisibleRange.values();
		for(VisibleRange value:values){
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
	public static VisibleRange fromCode(String code) throws Exception{
		VisibleRange[] values = VisibleRange.values();
		for(VisibleRange value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
