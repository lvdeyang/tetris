package com.sumavision.bvc.control.device.monitor.point;

import com.sumavision.bvc.device.monitor.point.MonitorPointPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorPointVO extends AbstractBaseVO<MonitorPointVO, MonitorPointPO>{

	private String bundleId;
	
	private String bundleName;
	
	private String layerId;
	
	private String name;
	
	private String index;
	
	private String username;
	
	private boolean removeable;
	
	public String getBundleId() {
		return bundleId;
	}

	public MonitorPointVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}
	
	public String getBundleName() {
		return bundleName;
	}

	public MonitorPointVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public MonitorPointVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getName() {
		return name;
	}

	public MonitorPointVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getIndex() {
		return index;
	}

	public MonitorPointVO setIndex(String index) {
		this.index = index;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public MonitorPointVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public boolean isRemoveable() {
		return removeable;
	}

	public MonitorPointVO setRemoveable(boolean removeable) {
		this.removeable = removeable;
		return this;
	}

	@Override
	public MonitorPointVO set(MonitorPointPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setLayerId(entity.getLayerId())
			.setName(entity.getName())
			.setIndex(entity.getIndex())
			.setUsername(entity.getUsername());
		return this;
	}
	
	public MonitorPointVO set(MonitorPointPO entity, Long userId) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setLayerId(entity.getLayerId())
			.setName(entity.getName())
			.setIndex(entity.getIndex())
			.setUsername(entity.getUsername())
			.setRemoveable(entity.getUserId().equals(userId)?true:false);
		return this;
	}

}
