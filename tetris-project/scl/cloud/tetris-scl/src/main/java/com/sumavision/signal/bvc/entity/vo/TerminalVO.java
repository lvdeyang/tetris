package com.sumavision.signal.bvc.entity.vo;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TerminalVO extends AbstractBaseVO<TerminalVO, BundlePO>{

	/** 终端名称 */
	private String bundleName;
	
	/** 终端bundleId */
	private String bundleId;
	
	/** 终端ip */
	private String bundleIp;
	
	/** 终端venus类型 */
	private String bundleType;
	
	/** 终端设备类型 */
	private String deviceModel;
	
	/** 终端接入id */
	private String layerId;
	
	public String getBundleName() {
		return bundleName;
	}

	public TerminalVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public TerminalVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleIp() {
		return bundleIp;
	}

	public TerminalVO setBundleIp(String bundleIp) {
		this.bundleIp = bundleIp;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public TerminalVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public TerminalVO setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public TerminalVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	@Override
	public TerminalVO set(BundlePO entity) throws Exception {

		this.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setBundleIp(entity.getDeviceIp())
			.setBundleType(entity.getBundleType())
			.setDeviceModel(entity.getDeviceModel())
			.setLayerId(entity.getAccessNodeUid());
		
		return this;
	}

}
