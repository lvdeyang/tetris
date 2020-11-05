package com.sumavision.bvc.api.controller;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastAlarmBundlePO;
import com.sumavision.bvc.command.emergent.broadcast.CommandBroadcastAlarmPO;
import com.sumavision.tetris.commons.util.date.DateUtil;

/**
 * 存储的告警信息VO<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月22日 下午4:39:04
 */
public class EmergentAlarmVO {

	private String id;
	
	private String unifiedId;
	
	private String creatTime;
	
	private List<EmergentBundleVO> bundles;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnifiedId() {
		return unifiedId;
	}

	public void setUnifiedId(String unifiedId) {
		this.unifiedId = unifiedId;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public List<EmergentBundleVO> getBundles() {
		return bundles;
	}

	public void setBundles(List<EmergentBundleVO> bundles) {
		this.bundles = bundles;
	}

	public EmergentAlarmVO set(CommandBroadcastAlarmPO alarm){
		this.id = alarm.getId().toString();
		this.unifiedId = alarm.getUnifiedId();
		this.creatTime = DateUtil.format(alarm.getCreatetime(), DateUtil.dateTimePattern);
		this.bundles = new ArrayList<EmergentBundleVO>();
		List<CommandBroadcastAlarmBundlePO> bundles = alarm.getBundles();
		if(bundles != null){
			for(CommandBroadcastAlarmBundlePO bundle : bundles){
				EmergentBundleVO bundleVO = new EmergentBundleVO().set(bundle);
				this.getBundles().add(bundleVO);
			}
		}
		return this;
	}
	
}
