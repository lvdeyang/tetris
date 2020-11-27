package com.sumavision.tetris.bvc.business.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name="TETRIS_BVC_LOCATION_OF_SCREEN_WALL")
public class LocationOfScreenWallPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	//屏幕墙：以左上角的第一个屏幕为坐标[0.0]。[numberX, numberY]
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
	
	/** 视频的执行状态(转发暂停也当作RUN)*/
	private LocationExecuteStatus status = LocationExecuteStatus.STOP;
	
	/** 布局模板templateLayout的id*/
	private Long locationTemplateLayoutId;
	
	/** 点播任务MonitorLiveDevicePO的id*/
	private Long monitorLiveDeviceId;
	
	@Column(name="LOCATION_X")
	public Integer getLocationX() {
		return locationX;
	}

	public LocationOfScreenWallPO setLocationX(Integer locationX) {
		this.locationX = locationX;
		return this;
	}

	@Column(name="LOCATION_Y")
	public Integer getLocationY() {
		return locationY;
	}

	public LocationOfScreenWallPO setLocationY(Integer locationY) {
		this.locationY = locationY;
		return this;
	}

	@Column(name="DECODER_BUNDLE_ID")
	public String getDecoderBundleId() {
		return decoderBundleId;
	}

	public LocationOfScreenWallPO setDecoderBundleId(String decoderBundleId) {
		this.decoderBundleId = decoderBundleId;
		return this;
	}

	@Column(name="DECODER_BUNDLE_NAME")
	public String getDecoderBundleName() {
		return decoderBundleName;
	}

	public LocationOfScreenWallPO setDecoderBundleName(String decoderBundleName) {
		this.decoderBundleName = decoderBundleName;
		return this;
	}

	@Column(name="ENCODER_BUNDLE_ID")
	public String getEncoderBundleId() {
		return encoderBundleId;
	}

	public LocationOfScreenWallPO setEncoderBundleId(String encoderBundleId) {
		this.encoderBundleId = encoderBundleId;
		return this;
	}

	@Column(name="ENCODER_BUNDLE_NAME")
	public String getEncoderBundleName() {
		return encoderBundleName;
	}

	public LocationOfScreenWallPO setEncoderBundleName(String encoderBundleName) {
		this.encoderBundleName = encoderBundleName;
		return this;
	}

	@Enumerated(value=EnumType.STRING)
	@Column(name="STATUS")
	public LocationExecuteStatus getStatus() {
		return status;
	}

	public LocationOfScreenWallPO setStatus(LocationExecuteStatus status) {
		this.status = status;
		return this;
	}

	@Column(name="LOCATION_TEMPLATE_LAYOUT_ID")
	public Long getLocationTemplateLayoutId() {
		return locationTemplateLayoutId;
	}

	public LocationOfScreenWallPO setLocationTemplateLayoutId(Long locationTemplateLayoutId) {
		this.locationTemplateLayoutId = locationTemplateLayoutId;
		return this;
	}

	@Column(name="MONITOR_LIVE_DEVICE_ID")
	public Long getMonitorLiveDeviceId() {
		return monitorLiveDeviceId;
	}

	public LocationOfScreenWallPO setMonitorLiveDeviceId(Long monitorLiveDeviceId) {
		this.monitorLiveDeviceId = monitorLiveDeviceId;
		return this;
	}
	
}
