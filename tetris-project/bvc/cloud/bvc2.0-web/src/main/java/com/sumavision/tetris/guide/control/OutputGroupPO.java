/**
 * 
 */
package com.sumavision.tetris.guide.control;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月25日 下午3:43:23
 */
@Entity
@Table(name = "TETRIS_OUTPUT_GROUP_PO")
public class OutputGroupPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 导播任务id */
	private Long guideId;
	
	/** 输出组名称 */
	private String name = "输出组1";
	
	/** 切换方式 */
	private SwitchingMode switchingMode;
	
	/** 转码模板 */
	private String transcodingTemplate;

	@Column(name = "GUIDE_ID")
	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "SWITCHING_MODE")
	public SwitchingMode getSwitchingMode() {
		return switchingMode;
	}

	public void setSwitchingMode(SwitchingMode switchingMode) {
		this.switchingMode = switchingMode;
	}
	
	@Column(name = "TRANSCODING_TEMPLATE")
	public String getTranscodingTemplate() {
		return transcodingTemplate;
	}

	public void setTranscodingTemplate(String transcodingTemplate) {
		this.transcodingTemplate = transcodingTemplate;
	}
	
	
}
