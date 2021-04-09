package com.sumavision.bvc.control.device.monitor.osd;

import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorOsdVO extends AbstractBaseVO<MonitorOsdVO, MonitorOsdPO>{

	private String name;
	
	private String username;

	public String getName() {
		return name;
	}

	public MonitorOsdVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public MonitorOsdVO setUsername(String username) {
		this.username = username;
		return this;
	}

	@Override
	public MonitorOsdVO set(MonitorOsdPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?null:DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setUsername(entity.getUsername());
		return this;
	}
	
	
	
}
