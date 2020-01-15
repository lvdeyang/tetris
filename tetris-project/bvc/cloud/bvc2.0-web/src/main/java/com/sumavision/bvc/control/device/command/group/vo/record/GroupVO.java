package com.sumavision.bvc.control.device.command.group.vo.record;

import java.util.ArrayList;
import java.util.List;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;


public class GroupVO{
	
	/** 设备id */
	private String id;
	
	/** 设备号码 */
	private String groupName;
	
	/** 播放状态 */
	private List<RecordVO> records;

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

	public List<RecordVO> getRecords() {
		return records;
	}

	public GroupVO setRecords(List<RecordVO> records) {
		this.records = records;
		return this;
	}
	
	public GroupVO set(List<CommandGroupRecordPO> records){
		if(records.size() > 0){
			this.setId(records.get(0).getGroupId().toString())
				.setGroupName(records.get(0).getGroupName())
				.setRecords(new ArrayList<RecordVO>());
			for(CommandGroupRecordPO record : records){
				if(record.getFragments()!=null && record.getFragments().size()>0){
					//过滤掉没有片段的
					RecordVO recordVO = new RecordVO().set(record);
					this.getRecords().add(recordVO);
				}
			}
			
		}
		
		return this;
	}
	
}
