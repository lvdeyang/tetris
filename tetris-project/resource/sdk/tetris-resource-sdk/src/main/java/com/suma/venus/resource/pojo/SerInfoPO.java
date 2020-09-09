package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

//服务设备PO
@Entity
public class SerInfoPO extends CommonPO<SerInfoPO>{
	
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
	
	/**
	 * 节点设备类型<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:53:19
	 */
	public enum SerInfoType{
		BUSINESS("业务控制单元", 1),
		ENCODER("编码设备", 2),
		DECODER("解码设备", 3),
		APPLICATION("应用服务单元", 4),
		SIGNAL("信令控制单元", 5),
		MEDIA("媒体处理服务单元", 6),
		STORAGE("媒体存储服务单元", 7),
		SIGNAL_ZUUL("信令网关服务单元", 8),
		MEDIA_ZUUL("媒体网关服务单元", 9),
		DIRECTOR("导播控制服务单元", 10),
		ENCODER_DECODER("编解码一体设备", 11);
		
		private String name;
		
		private Integer num;
		
		private SerInfoType(String name, Integer num){
			this.name = name;
			this.num = num;
		}

		public String getName() {
			return name;
		}

		public Integer getNum() {
			return num;
		}
		
	}

	@Column
	public String getSerUuid() {
		return serUuid;
	}

	public void setSerUuid(String serUuid) {
		this.serUuid = serUuid;
	}

	@Column
	public String getSerNo() {
		return serNo;
	}

	public void setSerNo(String serNo) {
		this.serNo = serNo;
	}

	@Column
	public String getSerName() {
		return serName;
	}

	public void setSerName(String serName) {
		this.serName = serName;
	}

	@Column
	public String getSerAddr() {
		return serAddr;
	}

	public void setSerAddr(String serAddr) {
		this.serAddr = serAddr;
	}

	@Column
	public Integer getSerPort() {
		return serPort;
	}

	public void setSerPort(Integer serPort) {
		this.serPort = serPort;
	}

	@Column
	public String getSerPwd() {
		return serPwd;
	}

	public void setSerPwd(String serPwd) {
		this.serPwd = serPwd;
	}

	@Column
	public Integer getSerType() {
		return serType;
	}

	public void setSerType(Integer serType) {
		this.serType = serType;
	}

	@Column
	public Integer getSerPro() {
		return serPro;
	}

	public void setSerPro(Integer serPro) {
		this.serPro = serPro;
	}

	@Column
	public String getSerNode() {
		return serNode;
	}

	public void setSerNode(String serNode) {
		this.serNode = serNode;
	}

	@Column
	public String getSerFactInfo() {
		return serFactInfo;
	}

	public void setSerFactInfo(String serFactInfo) {
		this.serFactInfo = serFactInfo;
	}

	@Column(name="sync_status")
	@Enumerated(EnumType.STRING)
	public SYNC_STATUS getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(SYNC_STATUS syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Column(name="source_type")
	@Enumerated(EnumType.STRING)
	public SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}
	
}
