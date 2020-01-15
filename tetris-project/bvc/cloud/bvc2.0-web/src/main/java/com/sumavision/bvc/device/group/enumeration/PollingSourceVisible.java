package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 描述合屏的源是否可见/隐藏
 * @author zsy
 * @date 2019年4月20日 13:15:28 
 */
public enum PollingSourceVisible {

	VISIBLE("显示", 1),
	INVISIBLE("隐藏", 0);
	
	private String name;
	
	private int protocal;
	
	private PollingSourceVisible(String name, int protocal){
		this.name = name;
		this.protocal = protocal;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getProtocal(){
		return this.protocal;
	}
	
	/**
	 * @Title: 根据名称获取 <br/>
	 * @param name 
	 * @return PollingSourceVisible 合屏的源是否可见/隐藏
	 */
	public static PollingSourceVisible fromName(String name) throws Exception{
		PollingSourceVisible[] values = PollingSourceVisible.values();
		for(PollingSourceVisible value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
