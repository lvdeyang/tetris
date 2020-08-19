package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.suma.venus.resource.pojo.SerInfoPO;

public class SerInfoVO {

	private Long id;
	private String serUuid;
	private String serNo;
	private String serName;
	private String serAddr;
	private Integer serPort;
	private String serPwd;
	private Integer serType;
	private Integer serPro;
	private String serNode;
	private String serFactInfo;

	private SYNC_STATUS syncStatus = SYNC_STATUS.ASYNC;
	private SOURCE_TYPE sourceType = SOURCE_TYPE.SYSTEM;

	public static SerInfoVO transFromPO(SerInfoPO po) {

		if (null == po) {
			return null;
		}

		SerInfoVO serInfoVO = new SerInfoVO();

		BeanUtils.copyProperties(po, serInfoVO);

		return serInfoVO;

	}

	public static List<SerInfoVO> transFromPOs(Collection<SerInfoPO> pos) {
		if (ObjectUtils.isEmpty(pos)) {
			return null;
		}

		List<SerInfoVO> vos = new LinkedList<>();
		for (SerInfoPO po : pos) {
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

	public String getSerUuid() {
		return serUuid;
	}

	public void setSerUuid(String serUuid) {
		this.serUuid = serUuid;
	}

	public String getSerNo() {
		return serNo;
	}

	public void setSerNo(String serNo) {
		this.serNo = serNo;
	}

	public String getSerName() {
		return serName;
	}

	public void setSerName(String serName) {
		this.serName = serName;
	}

	public String getSerAddr() {
		return serAddr;
	}

	public void setSerAddr(String serAddr) {
		this.serAddr = serAddr;
	}

	public Integer getSerPort() {
		return serPort;
	}

	public void setSerPort(Integer serPort) {
		this.serPort = serPort;
	}

	public String getSerPwd() {
		return serPwd;
	}

	public void setSerPwd(String serPwd) {
		this.serPwd = serPwd;
	}

	public Integer getSerType() {
		return serType;
	}

	public void setSerType(Integer serType) {
		this.serType = serType;
	}

	public Integer getSerPro() {
		return serPro;
	}

	public void setSerPro(Integer serPro) {
		this.serPro = serPro;
	}

	public String getSerNode() {
		return serNode;
	}

	public void setSerNode(String serNode) {
		this.serNode = serNode;
	}

	public String getSerFactInfo() {
		return serFactInfo;
	}

	public void setSerFactInfo(String serFactInfo) {
		this.serFactInfo = serFactInfo;
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
