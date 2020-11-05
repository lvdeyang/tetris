package com.sumavision.bvc.command.group.user.setting;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 
* @ClassName: OpenCloseMode
* @Description: 各种设置中的开启/关闭模式
* @author zsy
* @date 2019年11月29日 上午10:15:06 
*
 */
public enum OpenCloseMode {
	
	OPEN("开启", "open"),
	CLOSE("关闭", "close");
	
	private String name;
	
	private String code;
	
	private OpenCloseMode(String name, String code){
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
	public static OpenCloseMode fromName(String name) throws Exception{
		OpenCloseMode[] values = OpenCloseMode.values();
		for(OpenCloseMode value:values){
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
	public static OpenCloseMode fromCode(String code) throws Exception{
		OpenCloseMode[] values = OpenCloseMode.values();
		for(OpenCloseMode value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
