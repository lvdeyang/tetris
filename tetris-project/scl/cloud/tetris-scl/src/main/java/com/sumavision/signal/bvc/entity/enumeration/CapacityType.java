package com.sumavision.signal.bvc.entity.enumeration;

/**
 * 能力类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月16日 上午10:09:17
 */
public enum CapacityType {

	SRT("srt服务"),
	TRANSCODE("转码服务");
	
	private String name;
	
	private CapacityType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
