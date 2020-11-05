/**
 * 
 */
package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月30日 上午9:03:32
 */
public enum RateCtrl {
	VBR("vbr"),
	CBR("cbr");
	
	private String name;
	
	private RateCtrl(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static RateCtrl fromName(String name) throws Exception{
		RateCtrl[] values = RateCtrl.values();
		for (RateCtrl rateCtrl : values) {
			if(rateCtrl.getName().equals(name)){
				return rateCtrl;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
