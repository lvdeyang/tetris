package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 合屏（混音）内容类型，用于自动合屏时标记使用
 * @author zsy
 * @date 2020年8月20日 上午8:23:28 
 */
public enum CombineContentType {

	FOR_CHAIRMAN("主席观看"),
	FOR_MEMBER("成员观看");
	
	private String name;
	
	private CombineContentType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取合屏分屏的画面类型 <br/>
	 * @param name 
	 * @return PictureType 合屏分屏的画面类型 
	 */
	public static CombineContentType fromName(String name) throws Exception{
		CombineContentType[] values = CombineContentType.values();
		for(CombineContentType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
