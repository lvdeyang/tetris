package com.sumavision.bvc.control.device.command.group.vo.decoder;

/**
 * 字幕信息<br/>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月13日 上午10:37:55
 */
public class OsdVO{

	private String id;
	
	private String name;
	
	public String getId() {
		return id;
	}

	public OsdVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public OsdVO setName(String name) {
		this.name = name;
		return this;
	}
	
}
