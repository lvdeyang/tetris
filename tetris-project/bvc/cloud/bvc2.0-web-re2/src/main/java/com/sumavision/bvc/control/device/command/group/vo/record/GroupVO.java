package com.sumavision.bvc.control.device.command.group.vo.record;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.device.group.po.GroupRecordInfoPO;


public class GroupVO{
	
	/** 设备id */
	private String id;
	
	/** 设备号码 */
	private String groupName;
	
	/** 播放状态 */
	private List<RecordInfoVO> records;

	public String getId() {
		return id;
	}

	public GroupVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getGroupName() {
		return groupName;
	}

	public GroupVO setGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}

	public List<RecordInfoVO> getRecords() {
		return records;
	}

	public GroupVO setRecords(List<RecordInfoVO> records) {
		this.records = records;
		return this;
	}
	
	public GroupVO set(List<GroupRecordInfoPO> records){
		if(records.size() > 0){
			this.setId(records.get(0).getGroupId().toString())
				.setGroupName(records.get(0).getGroupName())
				.setRecords(new ArrayList<RecordInfoVO>());
			for(GroupRecordInfoPO record : records){
				if(record.getRecords()!=null && record.getRecords().size()>0){
					//过滤掉没有片段的
					RecordInfoVO recordVO = new RecordInfoVO().set(record);
					this.getRecords().add(recordVO);
				}
			}
			
		}
		
		return this;
	}
	
}
