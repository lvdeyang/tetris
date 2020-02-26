package com.sumavision.signal.bvc.director.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.signal.bvc.director.enumeration.DeviceType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 导播目的<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月21日 下午2:53:47
 */
@Entity
@Table(name = "TETRIS_SCL_DIRECTOR_DST")
public class DirectorDstPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 任务id */
	private String taskId;

	/** 设备bundleId */
	private String bundleId;
	
	/** 通道channelId */
	private String channelId;
	
	/** 设备类型 */
	private String deviceModel;
	
	/** 设备ip */
	private String deviceIp;
	
	/** 设备解码端口 */
	private Long devicePort;
	
	/** 设备类型 */
	private DeviceType deviceType;
	
	/** 目的(解码)参数 */
	private String codeParam;
	
	/** 目的附加参数 */
	private String passBy;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Long getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Long devicePort) {
		this.devicePort = devicePort;
	}

	@Enumerated(value = EnumType.STRING)
	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
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
	
}
