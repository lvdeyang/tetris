package com.sumavision.bvc.control.device.command.group.vo;

import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;

/**
 * 携带业务信息的播放器信息<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月17日 上午10:37:55
 */
public class BusinessPlayerVO{

	/** 屏幕序号 */
	private Integer serial;
	
	/** 设备id */
	private String bundleId;
	
	/** 设备号码 */
	private String bundleNo;
	
	/** 业务类别 */
	private String businessType;
	
	/** 业务所属id */
	private String businessId;
	
	/** 业务信息 */
	private String businessInfo;
	
	/** 文件地址 */
	private String url;
	
	/** 播放状态 */
	private String status;

	public Integer getSerial() {
		return serial;
	}

	public BusinessPlayerVO setSerial(Integer serial) {
		this.serial = serial;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public BusinessPlayerVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleNo() {
		return bundleNo;
	}

	public BusinessPlayerVO setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public BusinessPlayerVO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getBusinessId() {
		return businessId;
	}

	public BusinessPlayerVO setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}

	public String getBusinessInfo() {
		return businessInfo;
	}

	public BusinessPlayerVO setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public BusinessPlayerVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BusinessPlayerVO set(CommandGroupUserPlayerPO player){
		this.setBundleId(player.getBundleId())
		   .setBundleNo(player.getCode())
		   .setBusinessId(player.getBusinessId().toString())
		   .setBusinessType(player.getPlayerBusinessType().getCode())
		   .setBusinessInfo(player.getBusinessName())
		   .setSerial(player.getLocationIndex())
		   .setUrl(player.getPlayUrl());;
		
		return this;
	}
	
}
