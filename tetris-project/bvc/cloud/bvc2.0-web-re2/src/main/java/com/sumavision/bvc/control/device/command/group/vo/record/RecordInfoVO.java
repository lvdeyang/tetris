package com.sumavision.bvc.control.device.command.group.vo.record;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.device.group.po.GroupRecordInfoPO;
import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.tetris.commons.util.date.DateUtil;


public class RecordInfoVO{
	
	/** 设备id */
	private String id;
	
	/** 业务类别 */
	private String startTime;

	/** 业务所属id */
	private String endTime;
	
	/** 播放状态 */
	private List<RecordVO> fragments;

	public String getId() {
		return id;
	}

	public RecordInfoVO setId(String id) {
		this.id = id;
		return this;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public RecordInfoVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public RecordInfoVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public List<RecordVO> getFragments() {
		return fragments;
	}

	public RecordInfoVO setFragments(List<RecordVO> fragments) {
		this.fragments = fragments;
		return this;
	}

	public RecordInfoVO set(GroupRecordInfoPO recordInfo){
		this.setId(recordInfo.getId().toString())
			.setStartTime(DateUtil.format(recordInfo.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(DateUtil.format(recordInfo.getEndTime(), DateUtil.dateTimePattern))
			.setFragments(new ArrayList<RecordVO>());
		List<GroupRecordPO> records = recordInfo.getRecords();
		if(records!=null && records.size() > 0){
			for(GroupRecordPO record : records){
				RecordVO fragmentVO = new RecordVO().set(record);
				this.getFragments().add(fragmentVO);
			}
		}
		
		return this;
	}
	
}
