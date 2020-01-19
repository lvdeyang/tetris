package com.sumavision.signal.bvc.entity.po;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 能力绑定端口信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月16日 上午10:09:40
 */
@Entity
@Table(name = "BVC_CAPACITY_PERMISSION_PORT")
public class CapacityPermissionPortPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 设备bundleId */
	private String bundleId;
	
	/** 设备channelId */
	private String channelId;
	
	/** 设备ip */
	private String bundleIp;
	
	/** srt服务ip */
	private String srtIp;
	
	/** 设备给srt服务发流端口 */
	private Long srtPort;
	
	/** 转码能力ip */
	private String capacityIp;
	
	/** 转码能力端口 */
	private Long capacityPort;

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

	public String getBundleIp() {
		return bundleIp;
	}

	public void setBundleIp(String bundleIp) {
		this.bundleIp = bundleIp;
	}

	public String getSrtIp() {
		return srtIp;
	}

	public void setSrtIp(String srtIp) {
		this.srtIp = srtIp;
	}

	public Long getSrtPort() {
		return srtPort;
	}

	public void setSrtPort(Long srtPort) {
		this.srtPort = srtPort;
	}

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	public Long getCapacityPort() {
		return capacityPort;
	}

	public void setCapacityPort(Long capacityPort) {
		this.capacityPort = capacityPort;
	}

}
