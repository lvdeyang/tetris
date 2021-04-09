package com.sumavision.bvc.control.device.command.group.vo.record;

import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.device.group.po.GroupRecordInfoPO;
import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.tetris.commons.util.date.DateUtil;

public class RecordVO{
	
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

	public RecordVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getInfo() {
		return info;
	}

	public RecordVO setInfo(String info) {
		this.info = info;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public RecordVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public RecordVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public RecordVO set(GroupRecordPO record){
		this.setId(record.getId().toString())
		   .setStartTime(DateUtil.format(record.getStartTime(), DateUtil.dateTimePattern))
		   .setEndTime(DateUtil.format(record.getEndTime(), DateUtil.dateTimePattern))
		   .setInfo(record.getInfo());
		
		return this;
	}
	
}
