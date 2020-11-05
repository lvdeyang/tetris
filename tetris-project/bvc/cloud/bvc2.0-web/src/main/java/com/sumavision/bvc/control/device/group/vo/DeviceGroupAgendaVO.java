package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAgendaVO extends AbstractBaseVO<DeviceGroupAgendaVO, DeviceGroupConfigDTO>{

	private String name;
	
	private Boolean run;
	
	private String remark;
	
	private String audioOperation;
	
	public String getName() {
		return name;
	}

	public DeviceGroupAgendaVO setName(String name) {
		this.name = name;
		return this;
	}

	public Boolean getRun() {
		return run;
	}

	public DeviceGroupAgendaVO setRun(Boolean run) {
		this.run = run;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public DeviceGroupAgendaVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}
	
	public String getAudioOperation() {
		return audioOperation;
	}

	public DeviceGroupAgendaVO setAudioOperation(String audioOperation) {
		this.audioOperation = audioOperation;
		return this;
	}

	@Override
	public DeviceGroupAgendaVO set(DeviceGroupConfigDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRun(entity.getRun())
			.setRemark(entity.getRemark())
			.setAudioOperation(entity.getAudioOperation().getName());
		return this;
	}
	
	public DeviceGroupAgendaVO set(DeviceGroupConfigPO entity) throws Exception{
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRun(entity.getRun())
			.setRemark(entity.getRemark())
			.setAudioOperation(entity.getAudioOperation().getName());
		return this;
	}
	
}
