package com.sumavision.bvc.command.group.enumeration;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 指挥角色特殊描述<br/>
 * @Description: 指挥角色的特殊处理 <br/>
 * @author zsy
 * @date 2019年9月23日 上午10:11:22  
 */
public enum CommandRoleSpecial {
	
	COMMAND_MEMBER("指挥成员", false),//普通成员，相当于观众
	CHAIRMAN("主席", false),
	SPOKESMAN("发言人", true),
	CUSTOM("自定义", true);
	
	private String name;
	
	private boolean visiable;
	
	private CommandRoleSpecial(String name, boolean visiable){
		this.name = name;
		this.visiable = visiable;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isVisiable(){
		return this.visiable;
	}
	
	/**
	 * @Title: 根据名称获取业务角色特殊描述<br/>
	 * @param name 描述
	 * @param 
	 * @return BusinessRoleSpecial 特殊字段
	 * @throws
	 */
	public static CommandRoleSpecial fromName(String name) throws Exception{
		CommandRoleSpecial[] values = CommandRoleSpecial.values();
		for(CommandRoleSpecial value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	/**
	 * 获取可见的业务角色描述<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月11日 下午3:33:13
	 * @return List<String> 角色描述名称列表
	 */
	public static List<String> getVisiableList(){
		List<String> visiableList = new ArrayList<String>();
		CommandRoleSpecial[] values = CommandRoleSpecial.values();
		for(CommandRoleSpecial value:values){
			if(value.isVisiable()){
				visiableList.add(value.getName());
			}
		}
		return visiableList;
	}
	
	/**
	 * 获取所有的业务角色描述<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月11日 下午3:36:36
	 * @return List<String> 角色描述名称列表
	 */
	public static List<String> getTotalList(){
		List<String> totalList = new ArrayList<String>();
		CommandRoleSpecial[] values = CommandRoleSpecial.values();
		for(CommandRoleSpecial value:values){
			totalList.add(value.getName());
		}
		return totalList;
	}
	
}
