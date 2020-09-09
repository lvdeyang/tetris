package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: GearsSerial 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author A18ccms a18ccms_gmail_com 
 * @date 2018年7月24日 下午3:06:56 
 */
public enum GearsLevel {

	LEVEL_1("一档", 1),
	LEVEL_2("二挡", 2),
	LEVEL_3("三挡", 3);
	
	private int level;
	
	private String name;
	
	private GearsLevel(String name, int level){
		this.name = name;
		this.level = level;
	}
	
	public int getLevel(){
		return this.level;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据级别获取档位 <br/>
	 * @param level 级别
	 * @return GearsLevel 档位
	 */
	public static GearsLevel fromLevel(int level) throws Exception{
		GearsLevel[] gears = GearsLevel.values();
		for(GearsLevel gear:gears){
			if(gear.getLevel() == level){
				return gear;
			}
		}
		throw new ErrorTypeException("level", level);
	}
	
	/**
	 * @Title: 降一档 <br/>
	 * @return 档位
	 */
	public GearsLevel decrease() throws Exception{
		return GearsLevel.fromLevel(this.getLevel()-1);
	}
	
	/**
	 * @Title: 根据名称获取档位 <br/>
	 * @param name 档位名称<br/>
	 * @return GearsLevel 档位 
	 */
	public static GearsLevel fromName(String name) throws Exception{
		GearsLevel[] gears = GearsLevel.values();
		for(GearsLevel gear:gears){
			if(gear.getName().equals(name)){
				return gear;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
