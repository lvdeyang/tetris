package com.sumavision.signal.bvc.common.enumeration;

/**
 * scl服务节点类型节点类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月27日 下午1:16:37
 */
public enum NodeType {

	S100("s100转发服务节点", "signal-control-s100"),
	DIRECTOR("云导播服务节点", "signal-control-director"),
	NETWORK("网络调度服务节点", "signal-control-network");
	
	private String name;
	
	private String id;
	
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	private NodeType(String name, String id){
		this.name = name;
		this.id = id;
	}
	
}
