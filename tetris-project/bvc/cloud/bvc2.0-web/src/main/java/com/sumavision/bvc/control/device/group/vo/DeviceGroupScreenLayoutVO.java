package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupScreenLayoutVO extends AbstractBaseVO<DeviceGroupScreenLayoutVO, DeviceGroupScreenLayoutPO>{

	private String name;
	
	private String websiteDraw;
	
	public String getName() {
		return name;
	}

	public DeviceGroupScreenLayoutVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public DeviceGroupScreenLayoutVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	@Override
	public DeviceGroupScreenLayoutVO set(DeviceGroupScreenLayoutPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setWebsiteDraw(entity.getWebsiteDraw());
		return this;
	}
	
}
