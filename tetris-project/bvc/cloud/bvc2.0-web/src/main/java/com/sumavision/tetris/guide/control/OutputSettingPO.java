/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>
 * 详细描述
 * </p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月2日 下午7:47:36
 */
@Entity
@Table(name = "TETRIS_OUTPUT_SETTING_PO")
public class OutputSettingPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	/** 输出编号 */
	private String name;
	
	/** 输出协议 */
	private OutputProtocol outputProtocol;
	
	/** 输出地址 */
	private String outputAddress;
	
	/** 码率控制方式 */
	private RateCtrl rateCtrl;
	
	/** 系统码率 */
	private Long bitrate;

	/** 输出组id */
	private Long groupId;
	
	/** 任务类型：预监任务，切换任务*/
	private OutType outType;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setOutputProtocol(OutputProtocol outputProtocol) {
		this.outputProtocol = outputProtocol;
	}

	public void setOutputAddress(String outputAddress) {
		this.outputAddress = outputAddress;
	}
	
	@Column(name = "OUTPUT_ADDRESS")
	public String getOutputAddress() {
		return outputAddress;
	}

	@Column(name = "OUTPUT_PROTOCOL")
	@Enumerated(value = EnumType.STRING)
	public OutputProtocol getOutputProtocol() {
		return outputProtocol;
	}
	
	@Column(name = "RATE_CTRL")
	@Enumerated(value = EnumType.STRING)
	public RateCtrl getRateCtrl() {
		return rateCtrl;
	}

	public void setRateCtrl(RateCtrl rateCtrl) {
		this.rateCtrl = rateCtrl;
	}

	@Column(name = "BITRATE")
	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(Long bitrate) {
		this.bitrate = bitrate;
	}

	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	@Column(name = "OUT_TYPE")
	@Enumerated(value = EnumType.STRING)
	public OutType getOutType() {
		return outType;
	}

	public void setOutType(OutType outType) {
		this.outType = outType;
	}
	
	

}
