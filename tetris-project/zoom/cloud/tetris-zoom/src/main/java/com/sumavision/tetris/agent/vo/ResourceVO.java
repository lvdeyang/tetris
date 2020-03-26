package com.sumavision.tetris.agent.vo;

/**
 * response资源内容<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月9日 下午2:14:16
 */
public class ResourceVO {

	private String name;
	
	private String number;
	
	private String type;

	public String getName() {
		return name;
	}

	public ResourceVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getNumber() {
		return number;
	}

	public ResourceVO setNumber(String number) {
		this.number = number;
		return this;
	}

	public String getType() {
		return type;
	}

	public ResourceVO setType(String type) {
		this.type = type;
		return this;
	}
	
}
