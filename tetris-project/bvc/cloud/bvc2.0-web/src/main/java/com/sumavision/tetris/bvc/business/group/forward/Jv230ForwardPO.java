package com.sumavision.tetris.bvc.business.group.forward;

import javax.jdo.annotations.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * jv230转发<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月14日 下午3:02:00
 */
public class Jv230ForwardPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** jv230 bundleId */
	private String bundleId;
	
	/** jv230通道id */
	private String channelId;
	
	/** 序号 */
	private int serialNum;
	
	/** 横坐标 */
	private int x;
	
	/** 纵坐标 */
	private int y;
	
	/** 宽 */
	private int w;
	
	/** 高 */
	private int h;
	
	/** 转发源类型 */
	private Jv230ForwardSourceType sourceType;
	
	/** 源设备id */
	private String sourceBundleId;
	
	/** 源通道id */
	private String sourceChannelId;
	
	private String userId;
	
	private Jv230ForwardBusinessType businessType;

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

	@Column(name = "SERIAL_NUM")
	public int getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

	@Column(name = "X")
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Column(name = "Y")
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Column(name = "W")
	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	@Column(name = "H")
	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SOURCE_TYPE")
	public Jv230ForwardSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(Jv230ForwardSourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "SOURCE_BUNDLE_ID")
	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public void setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
	}

	@Column(name = "SOURCE_CHANNEL_ID")
	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public void setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "BUSINESS_TYPE")
	public Jv230ForwardBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Jv230ForwardBusinessType businessType) {
		this.businessType = businessType;
	}
	
}
