package com.sumavision.tetris.stream.transcoding.task;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class StreamTranscodingTaskVO extends AbstractBaseVO<StreamTranscodingTaskVO, StreamTranscodingTaskPO>{

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
	/** 小工具请求inputId */
	private Long inputId;
	/** 请求类型 */
	private String action;
	/** 用户id */
	private Long userId;
	/** 节点id */
	private Long accessPointId;
	/** 流程id */
	private Long processInstanceId;
	/** 旧媒资返回的任务id */
	private Long uniqId;
	
	public boolean isRecord() {
		return record;
	}
	public StreamTranscodingTaskVO setRecord(boolean record) {
		this.record = record;
		return this;
	}
	public String getRecordCallback() {
		return recordCallback;
	}
	public StreamTranscodingTaskVO setRecordCallback(String recordCallback) {
		this.recordCallback = recordCallback;
		return this;
	}
	public Integer getProgNum() {
		return progNum;
	}
	public StreamTranscodingTaskVO setProgNum(Integer progNum) {
		this.progNum = progNum;
		return this;
	}
	public Integer getEsType() {
		return esType;
	}
	public String getvCodec() {
		return vCodec;
	}
	public StreamTranscodingTaskVO setvCodec(String vCodec) {
		this.vCodec = vCodec;
		return this;
	}
	public String getaCodec() {
		return aCodec;
	}
	public StreamTranscodingTaskVO setaCodec(String aCodec) {
		this.aCodec = aCodec;
		return this;
	}
	public StreamTranscodingTaskVO setEsType(Integer esType) {
		this.esType = esType;
		return this;
	}
	public Long getInputId() {
		return inputId;
	}
	public String getAction() {
		return action;
	}
	public StreamTranscodingTaskVO setAction(String action) {
		this.action = action;
		return this;
	}
	public StreamTranscodingTaskVO setInputId(Long inputId) {
		this.inputId = inputId;
		return this;
	}
	public Long getUserId() {
		return userId;
	}
	public StreamTranscodingTaskVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	public Long getAccessPointId() {
		return accessPointId;
	}
	public StreamTranscodingTaskVO setAccessPointId(Long accessPointId) {
		this.accessPointId = accessPointId;
		return this;
	}
	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	public StreamTranscodingTaskVO setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}
	
	public Long getUniqId() {
		return uniqId;
	}
	public StreamTranscodingTaskVO setUniqId(Long uniqId) {
		this.uniqId = uniqId;
		return this;
	}
	@Override
	public StreamTranscodingTaskVO set(StreamTranscodingTaskPO entity)
			throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUuid(entity.getUuid())
			.setRecord(entity.isRecord())
			.setRecordCallback(entity.getRecordCallback())
			.setProgNum(entity.getProgNum())
			.setEsType(entity.getEsType())
			.setvCodec(entity.getvCodec())
			.setaCodec(entity.getaCodec())
			.setInputId(entity.getInputId())
			.setAction(entity.getAction())
			.setUserId(entity.getUserId())
			.setAccessPointId(entity.getAccessPointId())
			.setProcessInstanceId(entity.getProcessInstanceId())
			.setUniqId(entity.getUniqId() == null ? entity.getId() : entity.getUniqId());
		return this;
	}
}
