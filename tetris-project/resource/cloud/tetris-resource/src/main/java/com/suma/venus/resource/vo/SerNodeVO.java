package com.suma.venus.resource.vo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

public class SerNodeVO {

	private Long id;

	private String nodeUuid;

	private String nodeName;

	// 上级服务节点ID
	private String nodeFather;

	// 关联服务节点 ID
	private String nodeRelations;

	// 厂商信息
	private String nodeFactInfo;

	private SYNC_STATUS syncStatus;

	private SOURCE_TYPE sourceType;

	public static SerNodeVO transFromPO(SerNodePO po) {

		if (po == null) {
			return null;
		}

		SerNodeVO vo = new SerNodeVO();
		BeanUtils.copyProperties(po, vo);
		return vo;

	}

	public static List<SerNodeVO> transFromPOs(Collection<SerNodePO> pos) {

		if (CollectionUtils.isEmpty(pos)) {
			return null;
		}

		List<SerNodeVO> vos = new LinkedList<>();
		for (SerNodePO po : pos) {
			vos.add(transFromPO(po));
		}

		return vos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNodeUuid() {
		return nodeUuid;
	}

	public void setNodeUuid(String nodeUuid) {
		this.nodeUuid = nodeUuid;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeFather() {
		return nodeFather;
	}

	public void setNodeFather(String nodeFather) {
		this.nodeFather = nodeFather;
	}

	public String getNodeRelations() {
		return nodeRelations;
	}

	public void setNodeRelations(String nodeRelations) {
		this.nodeRelations = nodeRelations;
	}

	public String getNodeFactInfo() {
		return nodeFactInfo;
	}

	public void setNodeFactInfo(String nodeFactInfo) {
		this.nodeFactInfo = nodeFactInfo;
	}

	public SYNC_STATUS getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(SYNC_STATUS syncStatus) {
		this.syncStatus = syncStatus;
	}

	public SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}

}
