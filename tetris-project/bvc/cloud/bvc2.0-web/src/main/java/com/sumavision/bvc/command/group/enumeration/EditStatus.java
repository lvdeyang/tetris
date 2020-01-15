package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 指挥组编辑状态 
 * @author zsy
 * @date 2019年9月20日 下午1:22:00
 */
public enum EditStatus {

	NORMAL("正常"),//预设
	TEMP("临时");//保存后变为“正常”，如果不保存则停止后直接删除
//	DELETE("已删除");//待定，如果做了录制，那么删除需要通过指挥组来呈现录制文件
	
	private String name;
	
	private EditStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static EditStatus fromName(String name) throws Exception{
		EditStatus[] values = EditStatus.values();
		for(EditStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
