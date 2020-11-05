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
 * <b>日期：</b>2020年9月16日 下午2:08:22
 */
public enum ChannelLayout {
	MONO("mono"),
	STEREO("stereo");
	
	private String name;
	
	private ChannelLayout(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static ChannelLayout fromName(String name) throws Exception{
		ChannelLayout[] values = ChannelLayout.values();
		for (ChannelLayout channelLayout : values) {
			if(channelLayout.getName().equals(name)){
				return channelLayout;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
