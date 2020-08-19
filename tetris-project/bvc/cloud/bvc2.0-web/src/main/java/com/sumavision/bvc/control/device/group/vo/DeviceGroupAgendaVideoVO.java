package com.sumavision.bvc.control.device.group.vo;

import java.util.List;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDTO;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAgendaVideoVO extends AbstractBaseVO<DeviceGroupAgendaVideoVO, DeviceGroupConfigVideoDTO>{

	private String name;
	
	private String videoOperation;
	
	private String websiteDraw;
	
	private String layout;
	
	private List<DeviceGroupAgendaVideoSrcVO> srcs;
	
	private List<DeviceGroupAgendaVideoDstVO> dsts;
	
	private DeviceGroupAgendaVideoSmallScreenSrcVO small;
	
	public String getName() {
		return name;
	}

	public DeviceGroupAgendaVideoVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoOperation() {
		return videoOperation;
	}

	public DeviceGroupAgendaVideoVO setVideoOperation(String videoOperation) {
		this.videoOperation = videoOperation;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public DeviceGroupAgendaVideoVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}
	
	public String getLayout() {
		return layout;
	}

	public DeviceGroupAgendaVideoVO setLayout(String layout) {
		this.layout = layout;
		return this;
	}

	public List<DeviceGroupAgendaVideoSrcVO> getSrcs() {
		return srcs;
	}

	public DeviceGroupAgendaVideoVO setSrcs(List<DeviceGroupAgendaVideoSrcVO> srcs) {
		this.srcs = srcs;
		return this;
	}

	public List<DeviceGroupAgendaVideoDstVO> getDsts() {
		return dsts;
	}

	public DeviceGroupAgendaVideoVO setDsts(List<DeviceGroupAgendaVideoDstVO> dsts) {
		this.dsts = dsts;
		return this;
	}

	public DeviceGroupAgendaVideoSmallScreenSrcVO getSmall() {
		return small;
	}

	public void setSmall(DeviceGroupAgendaVideoSmallScreenSrcVO small) {
		this.small = small;
	}

	@Override
	public DeviceGroupAgendaVideoVO set(DeviceGroupConfigVideoDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setVideoOperation(entity.getVideoOperation().getName())
			.setWebsiteDraw(entity.getWebsiteDraw())
			.setLayout(entity.getLayout()==null?ScreenLayout.SINGLE.getName():entity.getLayout().getName());
		return this;
	}
	
	public DeviceGroupAgendaVideoVO set(DeviceGroupConfigVideoPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setVideoOperation(entity.getVideoOperation().getName())
			.setWebsiteDraw(entity.getWebsiteDraw())
			.setLayout(entity.getLayout()==null?ScreenLayout.SINGLE.getName():entity.getLayout().getName());
		return this;
	}
	
}
