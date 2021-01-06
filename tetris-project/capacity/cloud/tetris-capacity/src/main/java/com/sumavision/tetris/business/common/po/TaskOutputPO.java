package com.sumavision.tetris.business.common.po;

import javax.persistence.*;

import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CAPACITY_TASK_OUTPUT",uniqueConstraints = {@UniqueConstraint(columnNames={"taskUuid"})})
public class TaskOutputPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 任务唯一标识 */
	private String taskUuid;
	
	/** 单源 */
	private Long inputId;
	
	/** 多源的时候用 */
	private String inputList;
	
	private String task;
	
	private String output;
	
	private String capacityIp;
	
	private BusinessType type;
	
	/** 盖播id */
	private Long coverId;
	
	/** **************
	 *    yjgb使用          *
	 ** ************ */
	/** 是否录制 */
	private boolean isRecord = false;
	
	/** 录制地址 */
	private String recordAddress;
	
	/** 媒体类型 */
	private String mediaType;
	
	/** 录制完成回调地址 */
	private String recordCallbackUrl;
	
	/** **************
	 *    编单使用          *
	 ** ************ */
	/** 编单中起始源的id */
	private Long prevId;
	
	/** 编单中下一个源的id */
	private Long nextId;
	
	/** 排期源id */
	private Long scheduleId;

	private Integer syncStatus = 0; //0表示同步
	
	public String getTaskUuid() {
		return taskUuid;
	}

	public void setTaskUuid(String taskUuid) {
		this.taskUuid = taskUuid;
	}

	public Long getInputId() {
		return inputId;
	}

	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}

	public String getInputList() {
		return inputList;
	}

	public void setInputList(String inputList) {
		this.inputList = inputList;
	}

	@Column(name = "TASK", columnDefinition = "longtext")
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Column(name = "OUTPUT", columnDefinition = "longtext")
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public boolean isRecord() {
		return isRecord;
	}

	public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

	public String getRecordAddress() {
		return recordAddress;
	}

	public void setRecordAddress(String recordAddress) {
		this.recordAddress = recordAddress;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getRecordCallbackUrl() {
		return recordCallbackUrl;
	}

	public void setRecordCallbackUrl(String recordCallbackUrl) {
		this.recordCallbackUrl = recordCallbackUrl;
	}

	@Enumerated(EnumType.STRING)
	public BusinessType getType() {
		return type;
	}

	public void setType(BusinessType type) {
		this.type = type;
	}

	public String getCapacityIp() {
		return capacityIp;
	}

	public void setCapacityIp(String capacityIp) {
		this.capacityIp = capacityIp;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	public Long getNextId() {
		return nextId;
	}

	public void setNextId(Long nextId) {
		this.nextId = nextId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}
}
