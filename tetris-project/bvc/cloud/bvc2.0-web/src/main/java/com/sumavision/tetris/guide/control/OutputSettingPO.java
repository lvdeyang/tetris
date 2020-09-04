/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	private String outputProtocol;
	
	/** 输出地址 */
	private String outputAddress;
	
	/** 导播任务id */
	private Long guideId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "OUTPUT_PROTOCOL")
	public String getOutputProtocol() {
		return outputProtocol;
	}

	public void setOutputProtocol(String outputProtocol) {
		this.outputProtocol = outputProtocol;
	}
	
	@Column(name = "OUTPUT_ADDRESS")
	public String getOutputAddress() {
		return outputAddress;
	}

	public void setOutputAddress(String outputAddress) {
		this.outputAddress = outputAddress;
	}
	
	@Column(name = "GUIDE_ID")
	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

}
