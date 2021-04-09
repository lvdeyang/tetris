/**
 * 
 */
package com.sumavision.tetris.guide.control;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月25日 下午4:26:46
 */
public class OutputGroupVO extends AbstractBaseVO<OutputGroupVO, OutputGroupPO>{
	
	private Long id;
	
	/** 导播任务id */
	private Long guideId;
	
	/** 输出组名称 */
	private String name;
	
	/** 切换方式 */
	private String switchingMode;

	private String switchingModeName;
	
	/** 转码模板 */
	private String transcodingTemplate;
	
	private String monitorUuid;
	
	public Long getId() {
		return id;
	}

	public OutputGroupVO setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getGuideId() {
		return guideId;
	}

	public OutputGroupVO setGuideId(Long guideId) {
		this.guideId = guideId;
		return this;
	}

	public String getName() {
		return name;
	}

	public OutputGroupVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getSwitchingMode() {
		return switchingMode;
	}

	public OutputGroupVO setSwitchingMode(String switchingMode) {
		this.switchingMode = switchingMode;
		return this;
	}

	public String getSwitchingModeName() {
		return switchingModeName;
	}

	public OutputGroupVO setSwitchingModeName(String switchingModeName) {
		this.switchingModeName = switchingModeName;
		return this;
	}

	public String getTranscodingTemplate() {
		return transcodingTemplate;
	}

	public OutputGroupVO setTranscodingTemplate(String transcodingTemplate) {
		this.transcodingTemplate = transcodingTemplate;
		return this;
	}
	

	public String getMonitorUuid() {
		return monitorUuid;
	}

	public void setMonitorUuid(String monitorUuid) {
		this.monitorUuid = monitorUuid;
	}

	@Override
	public OutputGroupVO set(OutputGroupPO entity) throws Exception {
		this.setId(entity.getId());
		this.setGuideId(entity.getId());
		this.setName(entity.getName());
		this.setSwitchingMode(entity.getSwitchingMode()!= null ? entity.getSwitchingMode().toString(): null);
		this.setSwitchingModeName(entity.getSwitchingMode()!= null ? entity.getSwitchingMode().getName(): null);
		this.setTranscodingTemplate(entity.getTranscodingTemplate());
		this.setMonitorUuid(entity.getMonitorUuid());
		return this;
	}
	
	
	
}
