package com.sumavision.bvc.command.group.enumeration;

/**
 * 用户播放器布局方案类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月15日 下午5:34:39
 */
public enum SchemeType {

	DEFAULT("默认方案"),
	CREATE("创建方案");
	
	private String name;
	
	private SchemeType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
