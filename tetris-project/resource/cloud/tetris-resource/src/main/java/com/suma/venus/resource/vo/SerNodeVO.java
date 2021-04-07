package com.suma.venus.resource.vo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.SerNodePO.ConnectionStatus;

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
	
	// 订阅节点
	private String nodePublisher;

	private SYNC_STATUS syncStatus;

	private SOURCE_TYPE sourceType;
	
	private String password;
	
	private ConnectionStatus operate;
	
	/**连接状态*/
	private ConnectionStatus status;
	
	private String BusinessRoles;
	
	/**外域ip*/
	private String ip;
	
	/**外域端口*/
	private String port;
	
	/**扩展字段**/
	private JSONObject param;
	
	/**显示名称*/
	private String fakeName;
	
	public String getFakeName() {
		return fakeName;
	}

	public void setFakeName(String fakeName) {
		this.fakeName = fakeName;
	}

	public JSONObject getParam() {
		return param;
	}

	public void setParam(JSONObject param) {
		this.param = param;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public static SerNodeVO transFromPO(SerNodePO po) {

		if (po == null) {
			return null;
		}

		SerNodeVO vo = new SerNodeVO();
		BeanUtils.copyProperties(po, vo);
		return vo;

	}

	public static List<SerNodeVO> transFromPOs(Collection<SerNodePO> pos) {

		List<SerNodeVO> vos = new LinkedList<>();
		if (CollectionUtils.isEmpty(pos)) {
			return vos;
		}

		for (SerNodePO po : pos) {
			vos.add(transFromPO(po));
		}

		return vos;
	}

	public ConnectionStatus getStatus() {
		return status;
	}

	public void setStatus(ConnectionStatus status) {
		this.status = status;
	}

	public String getBusinessRoles() {
		return BusinessRoles;
	}

	public void setBusinessRoles(String businessRoles) {
		BusinessRoles = businessRoles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public ConnectionStatus getOperate() {
		return operate;
	}

	public void setOperate(ConnectionStatus operate) {
		this.operate = operate;
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

	public String getNodePublisher() {
		return nodePublisher;
	}

	public void setNodePublisher(String nodePublisher) {
		this.nodePublisher = nodePublisher;
	}

}
