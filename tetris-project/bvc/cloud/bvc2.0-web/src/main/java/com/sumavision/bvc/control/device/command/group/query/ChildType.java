/**
 * 
 */
package com.sumavision.bvc.control.device.command.group.query;

import com.sumavision.tetris.bvc.model.agenda.AgendaForwardType;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @author Administrator
 *
 */
public enum ChildType {
	ORIGIN("外域"),
	FOLDER("目录"),
	BUNDLE("设备");
	
	private ChildType(String name){
		this.name = name;
	}
	
	private String name;
	
	public String getName() {
		return name;
	}

	public static ChildType fromName(String name) throws Exception{
		ChildType[] values = ChildType.values();
		for(ChildType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
