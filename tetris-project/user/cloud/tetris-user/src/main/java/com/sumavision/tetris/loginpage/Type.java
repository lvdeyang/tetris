/**
 * 
 */
package com.sumavision.tetris.loginpage;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * value类型<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月13日 下午3:40:50
 */
public enum Type {
	IMG("img"),
	TEXT("text");
	
	/** value值类型名称 */
	private String name;
	
	private Type(String name){
		this.name =name;
	}
	
	private String getType(){
		return name;
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Type fromName(String name) throws Exception{
		Type[] values = Type.values();
		for(Type value:values){
			if(value.getType().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("Type", name);
	}

}
