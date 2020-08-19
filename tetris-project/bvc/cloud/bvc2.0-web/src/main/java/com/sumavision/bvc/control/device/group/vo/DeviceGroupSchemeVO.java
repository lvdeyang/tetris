package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupSchemeVO extends AbstractBaseVO<DeviceGroupSchemeVO, DeviceGroupConfigDTO>{

	private String name;
	
	private String remark;
	
	public String getName() {
		return name;
	}

	public DeviceGroupSchemeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public DeviceGroupSchemeVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	@Override
	public DeviceGroupSchemeVO set(DeviceGroupConfigDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark());
		return this;
	}
	
	public DeviceGroupSchemeVO set(DeviceGroupConfigPO entity) throws Exception{
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRemark(entity.getRemark());
		return this;
	}
	
}
