package com.sumavision.signal.bvc.director.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.director.enumeration.NodeType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 导播源<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月210日 下午2:53:22
 */
@Entity
@Table(name = "TETRIS_SCL_DIRECTOR_SOURCE")
public class DirectorSourcePO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 任务id */
	private String taskId;
	
	/** 设备所占用能力id */
	private String capacityId;
	
	/** 设备所占用能力ip */
	private String capacityIp;
	
	/** 设备所占用能力port */
	private Long capacityPort;

	/** 设备bundleId */
	private String bundleId;
	
	/** 通道channelId */
	private String channelId;
	
	/** 设备类型 */
	private String deviceModel;
	
	/** 设备ip */
	private String deviceIp;
	
	/** 源(编码)参数 */
	private String codeParam;
	
	/** 源附加参数 */
	private String passBy;
	
	/** 源索引 */
	private String source_index;

	@Column
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column
	public String getCapacityId() {
		return capacityId;
	}

	public void setCapacityId(String capacityId) {
		this.capacityId = capacityId;
	}

	@Column
	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	@Column
	public Long getCapacityPort() {
		return capacityPort;
	}

	public void setCapacityPort(Long capacityPort) {
		this.capacityPort = capacityPort;
	}

	@Column
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Column
	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	@Column(name = "CODE_PARAM", columnDefinition = "longtext")
	public String getCodeParam() {
		return codeParam;
	}

	public void setCodeParam(String codeParam) {
		this.codeParam = codeParam;
	}

	@Column(name = "PASS_BY", columnDefinition = "longtext")
	public String getPassBy() {
		return passBy;
	}

	public void setPassBy(String passBy) {
		this.passBy = passBy;
	}

	@Column
	public String getSource_index() {
		return source_index;
	}

	public void setSource_index(String source_index) {
		this.source_index = source_index;
	}
	
}
