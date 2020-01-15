package com.sumavision.bvc.control.device.command.group.vo.record;

import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.tetris.commons.util.date.DateUtil;

public class FragmentVO{
	
	/** 设备id */
	private String id;
	
	/** 设备号码 */
	private String info;
	
	/** 业务类别 */
	private String startTime;
	
	/** 业务所属id */
	private String endTime;

	public String getId() {
		return id;
	}

	public FragmentVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getInfo() {
		return info;
	}

	public FragmentVO setInfo(String info) {
		this.info = info;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public FragmentVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public FragmentVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public FragmentVO set(CommandGroupRecordFragmentPO fragment){
		this.setId(fragment.getId().toString())
		   .setStartTime(DateUtil.format(fragment.getStartTime(), DateUtil.dateTimePattern))
		   .setEndTime(DateUtil.format(fragment.getEndTime(), DateUtil.dateTimePattern))
		   .setInfo(fragment.getInfo());
		
		return this;
	}
	
}
