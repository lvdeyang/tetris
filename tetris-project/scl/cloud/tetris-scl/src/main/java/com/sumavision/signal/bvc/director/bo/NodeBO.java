package com.sumavision.signal.bvc.director.bo;

import java.util.List;

/**
 * 节点(基站)<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月21日 下午2:47:00
 */
public class NodeBO {

	private String nodeId;
	
	private List<BundleBO> bundles;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public List<BundleBO> getBundles() {
		return bundles;
	}

	public void setBundles(List<BundleBO> bundles) {
		this.bundles = bundles;
	}
	
}
