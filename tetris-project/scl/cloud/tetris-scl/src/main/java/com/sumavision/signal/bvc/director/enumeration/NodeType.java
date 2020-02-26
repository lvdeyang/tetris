package com.sumavision.signal.bvc.director.enumeration;

/**
 * 节点类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月12日 下午2:16:13
 */
public enum NodeType {

	CENTER("中心节点"),
	EDGE("边缘节点");
	
	private String name;
	
	private NodeType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
