package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.po.RecordPO;

public class RecordVO {

	private String recordUuid;
	
	private Long id;
	
	/** 记录group类型 */
	private GroupType groupType;
	
	/** 视频名称 */
	private String videoName;
	
	/** 视频描述 */
	private String description;
	
	/** 录制类型 */
	private RecordType type;

	public GroupType getGroupType() {
		return groupType;
	}

	public RecordVO setGroupType(GroupType groupType) {
		this.groupType = groupType;
		return this;
	}

	public String getVideoName() {
		return videoName;
	}

	public RecordVO setVideoName(String videoName) {
		this.videoName = videoName;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public RecordVO setDescription(String description) {
		this.description = description;
		return this;
	}

	public RecordType getType() {
		return type;
	}

	public RecordVO setType(RecordType type) {
		this.type = type;
		return this;
	}
	
	public Long getId() {
		return id;
	}

	public RecordVO setId(Long id) {
		this.id = id;
		return this;
	}	
	
	public String getRecordUuid() {
		return recordUuid;
	}

	public RecordVO setRecordUuid(String recordUuid) {
		this.recordUuid = recordUuid;
		return this;
	}

	public RecordVO set(RecordPO recordPO){
		this.setId(recordPO.getId())
			.setGroupType(recordPO.getGroupType())
			.setVideoName(recordPO.getVideoName())
			.setDescription(recordPO.getDescription())
			.setType(recordPO.getType())
			.setRecordUuid(recordPO.getUuid());
		
		return this;
	}
}
