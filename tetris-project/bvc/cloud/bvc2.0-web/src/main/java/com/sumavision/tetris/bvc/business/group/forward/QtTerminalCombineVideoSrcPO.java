package com.sumavision.tetris.bvc.business.group.forward;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_GROUP_QT_TERMINAL_COMBINE_VIDEO_SRC")
public class QtTerminalCombineVideoSrcPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

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
	
	/** 业务名称 */
	private String businessName;
	
	/** 源所在接入层 */
	private String sourceLayerId;
	
	/** 源设备id */
	private String sourceBundleId;
	
	/** 源通道id */
	private String sourceChannelId;
	
	/** 隶属合屏id */
	private Long qtTerminalCombineVideoId;

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

	@Column(name = "BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Column(name = "SOURCE_LAYER_ID")
	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
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

	@Column(name = "QT_TERMINAL_COMBINE_VIDEO_ID")
	public Long getQtTerminalCombineVideoId() {
		return qtTerminalCombineVideoId;
	}

	public void setQtTerminalCombineVideoId(Long qtTerminalCombineVideoId) {
		this.qtTerminalCombineVideoId = qtTerminalCombineVideoId;
	}
	
}
