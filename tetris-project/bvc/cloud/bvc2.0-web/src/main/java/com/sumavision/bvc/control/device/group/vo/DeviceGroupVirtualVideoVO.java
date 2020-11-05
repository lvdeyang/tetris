package com.sumavision.bvc.control.device.group.vo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupVirtualVideoVO extends AbstractBaseVO<DeviceGroupVirtualVideoVO, DeviceGroupConfigVideoPO>{

	private String name;
	
	private String websiteDraw;
	
	private List<DeviceGroupVirtualVideoSrcVO> srcs;
	
	public String getName() {
		return name;
	}

	public DeviceGroupVirtualVideoVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public DeviceGroupVirtualVideoVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	public List<DeviceGroupVirtualVideoSrcVO> getSrcs() {
		return srcs;
	}

	public DeviceGroupVirtualVideoVO setSrcs(List<DeviceGroupVirtualVideoSrcVO> srcs) {
		this.srcs = srcs;
		return this;
	}

	@Override
	public DeviceGroupVirtualVideoVO set(DeviceGroupConfigVideoPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setWebsiteDraw(entity.getWebsiteDraw())
			.setSrcs(new ArrayList<DeviceGroupVirtualVideoSrcVO>());
		
		return this;
	}

}
