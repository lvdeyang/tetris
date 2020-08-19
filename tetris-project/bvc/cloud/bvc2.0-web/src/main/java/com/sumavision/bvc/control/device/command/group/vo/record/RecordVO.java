package com.sumavision.bvc.control.device.command.group.vo.record;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.group.record.CommandGroupRecordFragmentPO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.tetris.commons.util.date.DateUtil;


public class RecordVO{
	
	/** 设备id */
	private String id;
	
	/** 业务类别 */
	private String startTime;

	/** 业务所属id */
	private String endTime;
	
	/** 播放状态 */
	private List<FragmentVO> fragments;

	public String getId() {
		return id;
	}

	public RecordVO setId(String id) {
		this.id = id;
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

	public List<FragmentVO> getFragments() {
		return fragments;
	}

	public RecordVO setFragments(List<FragmentVO> fragments) {
		this.fragments = fragments;
		return this;
	}

	public RecordVO set(CommandGroupRecordPO record){
		this.setId(record.getId().toString())
			.setStartTime(DateUtil.format(record.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(DateUtil.format(record.getEndTime(), DateUtil.dateTimePattern))
			.setFragments(new ArrayList<FragmentVO>());
		List<CommandGroupRecordFragmentPO> fragments = record.getFragments();
		if(fragments!=null && fragments.size() > 0){
			for(CommandGroupRecordFragmentPO fragment : fragments){
				FragmentVO fragmentVO = new FragmentVO().set(fragment);
				this.getFragments().add(fragmentVO);
			}
		}
		
		return this;
	}
	
}
