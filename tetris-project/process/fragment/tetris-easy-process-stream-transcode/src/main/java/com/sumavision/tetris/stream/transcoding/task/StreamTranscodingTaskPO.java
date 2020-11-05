package com.sumavision.tetris.stream.transcoding.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_STREAM_TRANSCODING_TASK")
public class StreamTranscodingTaskPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 是否录制 */
	private boolean record;
	/** 回调地址 */
	private String recordCallback;
	/** 节目号 */
	private Integer progNum;
	/** 输出封装类型 */
	private Integer esType;
	/** 视频编码格式 */
	private String vCodec;
	/** 音频编码格式 */
	private String aCodec;
	/** 小工具请求任务inputid */
	private Long inputId;
	/** 任务类型 */
	private String action;
	/** 用户id */
	private Long userId;
	/** 节点id */
	private Long accessPointId;
	/** 流程id */
	private Long processInstanceId;
	/** 旧媒资平台返回的任务id */
	private Long uniqId;
	
	@Column(name = "RECORD")
	public boolean isRecord() {
		return record;
	}
	
	public void setRecord(boolean record) {
		this.record = record;
	}
	
	@Column(name = "RECORD_CALLBACK")
	public String getRecordCallback() {
		return recordCallback;
	}
	
	public void setRecordCallback(String recordCallback) {
		this.recordCallback = recordCallback;
	}
	
	@Column(name = "PROG_NUM")
	public Integer getProgNum() {
		return progNum;
	}
	
	public void setProgNum(Integer progNum) {
		this.progNum = progNum;
	}
	
	@Column(name = "ES_TYPE")
	public Integer getEsType() {
		return esType;
	}
	
	public void setEsType(Integer esType) {
		this.esType = esType;
	}
	
	@Column(name = "V_CODEC")
	public String getvCodec() {
		return vCodec;
	}

	public void setvCodec(String vCodec) {
		this.vCodec = vCodec;
	}

	@Column(name = "A_CODEC")
	public String getaCodec() {
		return aCodec;
	}

	public void setaCodec(String aCodec) {
		this.aCodec = aCodec;
	}

	@Column(name = "INPUT_ID")
	public Long getInputId() {
		return inputId;
	}
	
	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}
	
	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Column(name = "ACTION")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "ACCESS_POINT_ID")
	public Long getAccessPointId() {
		return accessPointId;
	}
	
	public void setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
	}
	
	@Column(name = "PROCESS_INSTANCE_ID")
	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	
	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "UNIQ_ID" )
	public Long getUniqId() {
		return uniqId;
	}

	public void setUniqId(Long uniqId) {
		this.uniqId = uniqId;
	}
}
