package com.sumavision.bvc.control.device.group.vo;

import java.util.List;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDTO;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupSchemeVideoVO extends AbstractBaseVO<DeviceGroupSchemeVideoVO, DeviceGroupConfigVideoDTO>{

	private String name;
	
	private String videoOperation;
	
	private String websiteDraw;
	
	private String layout;
	
	private Boolean record;
	
	private List<DeviceGroupSchemeVideoSrcVO> srcs;
	
	private List<DeviceGroupSchemeVideoDstVO> dsts;
	
	public String getName() {
		return name;
	}

	public DeviceGroupSchemeVideoVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getVideoOperation() {
		return videoOperation;
	}

	public DeviceGroupSchemeVideoVO setVideoOperation(String videoOperation) {
		this.videoOperation = videoOperation;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public DeviceGroupSchemeVideoVO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}
	
	public String getLayout() {
		return layout;
	}

	public DeviceGroupSchemeVideoVO setLayout(String layout) {
		this.layout = layout;
		return this;
	}

	public Boolean getRecord() {
		return record;
	}

	public DeviceGroupSchemeVideoVO setRecord(Boolean record) {
		this.record = record;
		return this;
	}

	public List<DeviceGroupSchemeVideoSrcVO> getSrcs() {
		return srcs;
	}

	public DeviceGroupSchemeVideoVO setSrcs(List<DeviceGroupSchemeVideoSrcVO> srcs) {
		this.srcs = srcs;
		return this;
	}

	public List<DeviceGroupSchemeVideoDstVO> getDsts() {
		return dsts;
	}

	public DeviceGroupSchemeVideoVO setDsts(List<DeviceGroupSchemeVideoDstVO> dsts) {
		this.dsts = dsts;
		return this;
	}

	@Override
	public DeviceGroupSchemeVideoVO set(DeviceGroupConfigVideoDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setVideoOperation(entity.getVideoOperation().getName())
			.setWebsiteDraw(entity.getWebsiteDraw())
			.setLayout(entity.getLayout()==null?ScreenLayout.SINGLE.getName():entity.getLayout().getName())
			.setRecord(entity.getRecord());
		return this;
	}
	
	public DeviceGroupSchemeVideoVO set(DeviceGroupConfigVideoPO entity) throws Exception {
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
