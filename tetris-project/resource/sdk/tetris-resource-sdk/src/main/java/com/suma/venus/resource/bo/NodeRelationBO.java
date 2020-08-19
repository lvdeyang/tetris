package com.suma.venus.resource.bo;

public class NodeRelationBO {

	private String nodeUuid1;
	
	private String nodeUuid2;
	
	private boolean isConnected;

	public String getNodeUuid1() {
		return nodeUuid1;
	}

	public void setNodeUuid1(String nodeUuid1) {
		this.nodeUuid1 = nodeUuid1;
	}

	public String getNodeUuid2() {
		return nodeUuid2;
	}

	public void setNodeUuid2(String nodeUuid2) {
		this.nodeUuid2 = nodeUuid2;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
}
