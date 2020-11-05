package com.sumavision.tetris.bvc.business.group.forward;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * LQ项目：qt终端全部上屏转发非jv230设备<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月27日 下午4:49:51
 */
@Entity
@Table(name = "TETRIS_BVC_GROUP_QT_TERMINAL_FORWARD")
public class QtTerminalForwardPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;
	
	/** 源id */
	private String sourceId;
	
	/** 源类型 */
	private ForwardSourceType sourceType;
	
	/** jv230接入层id */
	private String layerId;
	
	/** jv230 bundleId */
	private String bundleId;
	
	/** jv230通道id */
	private String channelId;
	
	/** 用户id */
	private String userId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 业务类型 */
	private ForwardBusinessType businessType;

	@Column(name = "SOURCE_ID")
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SOURCE_TYPE")
	public ForwardSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(ForwardSourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "LAYER_ID")
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "BUSINESS_TYPE")
	public ForwardBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(ForwardBusinessType businessType) {
		this.businessType = businessType;
	}
	
}
