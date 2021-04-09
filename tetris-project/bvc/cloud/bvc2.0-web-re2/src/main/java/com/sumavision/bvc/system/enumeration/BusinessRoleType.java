package com.sumavision.bvc.system.enumeration;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 业务角色业务类型描述<br/>
 * @Description: 业务类型：默认的/可录制的 <br/>
 * @author wjw
 * @date 2018年10月29日 下午3:30:03 
 */
public enum BusinessRoleType {

	DEFAULT("默认的"),
	RECORDABLE("可录制的");
	
	private String name;
	
	private BusinessRoleType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取业务角色特殊描述<br/>
	 * @param name 描述
	 * @return BusinessRoleType 特殊字段
	 * @throws
	 */
	public static BusinessRoleType fromName(String name) throws Exception{
		BusinessRoleType[] values = BusinessRoleType.values();
		for(BusinessRoleType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	/**
	 * 获取所有的业务角色类型描述<br/>
	 * @return List<String> 业务角色类型描述名称列表
	 */
	public static List<String> getTotalList(){
		List<String> totalList = new ArrayList<String>();
		BusinessRoleType[] values = BusinessRoleType.values();
		for(BusinessRoleType value:values){
			totalList.add(value.getName());
		}
		return totalList;
	}
}
