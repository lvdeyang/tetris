package com.sumavision.bvc.control.device.jv230.vo;

import com.sumavision.bvc.device.jv230.po.ConfigLocationPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ConfigLocationVo extends AbstractBaseVO<ConfigLocationVo, ConfigLocationPO>{

	private Long id;
	
	//uuid，站点间设备唯一识别码
	private String uuid;
	
	//位置信息
	private String location;

	public Long getId() {
		return id;
	}

	public ConfigLocationVo setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public ConfigLocationVo setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public ConfigLocationVo setLocation(String location) {
		this.location = location;
		return this;
	}

	@Override
	public ConfigLocationVo set(ConfigLocationPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setLocation(new String(entity.getLocation()));
		return this;
	}
}
