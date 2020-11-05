package com.sumavision.tetris.bvc.business.location;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class LocationOfScreenWallVO extends AbstractBaseVO<LocationOfScreenWallVO, LocationOfScreenWallPO>{

	/** 屏幕墙X方向的坐标*/
	private Integer locationX;
	
	/** 屏幕墙Y方向的坐标*/ 
	private Integer locationY;
	
	/** 所绑定的解码设备id；对应BundlePO的bundleId*/
	private String decoderBundleId;
	
	/** 所绑定的解码设备name；对应BundlePO的bundleName*/
	private String decoderBundleName;
	
	/** 所绑定的编码设备id；对应BundlePO的bundleId*/
	private String encoderBundleId;
	
	/** 所绑定的编码设备name；对应BundlePO的bundleName*/
	private String encoderBundleName;
	
	/** 视频的执行状态*/
	private String status;
	
	/** 布局模板templateLayout的id*/
	private Long locationTemplateLayoutId;
	
	/** 点播任务MonitorLiveDevicePO的id*/
	private Long monitorLiveDeviceId;

	public Integer getLocationX() {
		return locationX;
	}

	public LocationOfScreenWallVO setLocationX(Integer locationX) {
		this.locationX = locationX;
		return this;
	}

	public Integer getLocationY() {
		return locationY;
	}

	public LocationOfScreenWallVO setLocationY(Integer locationY) {
		this.locationY = locationY;
		return this;
	}

	public String getDecoderBundleId() {
		return decoderBundleId;
	}

	public LocationOfScreenWallVO setDecoderBundleId(String decoderBundleId) {
		this.decoderBundleId = decoderBundleId;
		return this;
	}

	public String getDecoderBundleName() {
		return decoderBundleName;
	}

	public LocationOfScreenWallVO setDecoderBundleName(String decoderBundleName) {
		this.decoderBundleName = decoderBundleName;
		return this;
	}

	public String getEncoderBundleId() {
		return encoderBundleId;
	}

	public LocationOfScreenWallVO setEncoderBundleId(String encoderBundleId) {
		this.encoderBundleId = encoderBundleId;
		return this;
	}

	public String getEncoderBundleName() {
		return encoderBundleName;
	}

	public LocationOfScreenWallVO setEncoderBundleName(String encoderBundleName) {
		this.encoderBundleName = encoderBundleName;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public LocationOfScreenWallVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public Long getLocationTemplateLayoutId() {
		return locationTemplateLayoutId;
	}

	public LocationOfScreenWallVO setLocationTemplateLayoutId(Long locationTemplateLayoutId) {
		this.locationTemplateLayoutId = locationTemplateLayoutId;
		return this;
	}

	public Long getMonitorLiveDeviceId() {
		return monitorLiveDeviceId;
	}

	public LocationOfScreenWallVO setMonitorLiveDeviceId(Long monitorLiveDeviceId) {
		this.monitorLiveDeviceId = monitorLiveDeviceId;
		return this;
	}

	@Override
	public LocationOfScreenWallVO set(LocationOfScreenWallPO entity) throws Exception {
		
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setLocationX(entity.getLocationX())
			.setLocationY(entity.getLocationY())
			.setDecoderBundleId(entity.getDecoderBundleId()==null?"":entity.getDecoderBundleId())
			.setDecoderBundleName(entity.getDecoderBundleName()==null?"":entity.getDecoderBundleName())
			.setEncoderBundleId(entity.getEncoderBundleId()==null?"":entity.getEncoderBundleId())
			.setEncoderBundleName(entity.getEncoderBundleName()==null?"":entity.getEncoderBundleName())
			.setStatus(entity.getStatus()==null?"":entity.getStatus().toString())
			.setLocationTemplateLayoutId(locationTemplateLayoutId)
			.setMonitorLiveDeviceId(monitorLiveDeviceId);
		
		return this;
	}

}
