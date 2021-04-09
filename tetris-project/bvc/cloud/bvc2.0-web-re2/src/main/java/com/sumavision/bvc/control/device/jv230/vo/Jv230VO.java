package com.sumavision.bvc.control.device.jv230.vo;

import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class Jv230VO extends AbstractBaseVO<Jv230VO, Jv230PO>{

	private int serialnum;
	
	private String bundleId;
	
	private String bundleName;
	
	private String layerId;

	public int getSerialnum() {
		return serialnum;
	}

	public Jv230VO setSerialnum(int serialnum) {
		this.serialnum = serialnum;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public Jv230VO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public Jv230VO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public Jv230VO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	@Override
	public Jv230VO set(Jv230PO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSerialnum(entity.getSerialnum())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setLayerId(entity.getLayerId());
		return this;
	}
	
}
