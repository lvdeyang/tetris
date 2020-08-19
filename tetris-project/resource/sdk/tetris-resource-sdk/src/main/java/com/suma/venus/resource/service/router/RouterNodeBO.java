package com.suma.venus.resource.service.router;

import java.util.List;

/**
 * 路由节点信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月27日 下午1:13:29
 */
public class RouterNodeBO {

	/** 节点id */
	private String nodeId;
	
	/** 节点相邻节点（包含上下级）的所有节点id */
	private List<String> relations;
	
	private String router;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public List<String> getRelations() {
		return relations;
	}

	public void setRelations(List<String> relations) {
		this.relations = relations;
	}

	public String getRouter() {
		return router;
	}

	public RouterNodeBO setRouter(String router) {
		this.router = router;
		return this;
	}
	
}
