package com.sumavision.tetris.sts.task.tasklink;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumavision.tetris.sts.common.CommonPO;


@Entity
public class NodeIdPO extends CommonPO<NodeIdPO>{

	private Long nodeId;
	
	public NodeIdPO(){
		//只会初始化一次，以后递增，暂时初始化为1000
		setNodeId(1000l);
	}
	@Column
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	
}
