package com.sumavision.bvc.control.device.monitor.record;

import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordType;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorRecordTaskVO extends AbstractBaseVO<MonitorRecordTaskVO, MonitorRecordPO>{

	/** 文件名称 */
	private String fileName;
	
	/** 视频源 */
	private String videoSource;
	
	/** 音频源 */
	private String audioSource;
	
	/** 任务开始时间 */
	private String startTime;
	
	/** 任务结束时间 */
	private String endTime;
	
	/** 任务模式 */
	private String mode;
	
	/** 录制模式，标识录制用户还是录制设备 */
	private String type;
	
	/** 录制用户id */
	private String recordUserId;

	/** 录制用户名 */
	private String recordUsername;
	
	/** 录制用户号码 */
	private String recordUserno;
	
	/** 录制状态*/
	private String status;
	
	/** 是否有权限下载*/
	private Boolean privilegeOfDownload;
	
	/** 所属任务id*/
	private Long taskId;

	/** 所属任务名字*/
	private String taskName;
	
	/** 预览地址*/
	private String previewUrl;
	
	/** 做业务的用户id */
	private Long userId;
	
	/** 做业务的用户号码 */
	private String userno;
	
	/** 做业务的用户昵称 */
	private String nickname;
	
	/** 录制如果是设备，记录bundleId*/
	private String bundleId;
	
	/** 录制最大时长*/
	private Long time_duration;
	 
	/** 录制最大存储容量*/
	private Long total_size_mb; 
	
	private Long alarm_size_mb;
	
	public Long getTime_duration() {
		return time_duration;
	}

	public MonitorRecordTaskVO setTime_duration(Long time_duration) {
		this.time_duration = time_duration;
		return this;
	}

	public Long getTotal_size_mb() {
		return total_size_mb;
	}

	public MonitorRecordTaskVO setTotal_size_mb(Long total_size_mb) {
		this.total_size_mb = total_size_mb;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public MonitorRecordTaskVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public MonitorRecordTaskVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserno() {
		return userno;
	}

	public MonitorRecordTaskVO setUserno(String userno) {
		this.userno = userno;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public MonitorRecordTaskVO setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public Boolean getPrivilegeOfDownload() {
		return privilegeOfDownload;
	}

	public MonitorRecordTaskVO setPrivilegeOfDownload(Boolean privilegeOfDownload) {
		this.privilegeOfDownload = privilegeOfDownload;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public MonitorRecordTaskVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MonitorRecordTaskVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getVideoSource() {
		return videoSource;
	}

	public MonitorRecordTaskVO setVideoSource(String videoSource) {
		this.videoSource = videoSource;
		return this;
	}

	public String getAudioSource() {
		return audioSource;
	}

	public MonitorRecordTaskVO setAudioSource(String audioSource) {
		this.audioSource = audioSource;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public MonitorRecordTaskVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public MonitorRecordTaskVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public MonitorRecordTaskVO setMode(String mode) {
		this.mode = mode;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MonitorRecordTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getRecordUserId() {
		return recordUserId;
	}

	public MonitorRecordTaskVO setRecordUserId(String recordUserId) {
		this.recordUserId = recordUserId;
		return this;
	}

	public String getRecordUsername() {
		return recordUsername;
	}

	public MonitorRecordTaskVO setRecordUsername(String recordUsername) {
		this.recordUsername = recordUsername;
		return this;
	}

	public String getRecordUserno() {
		return recordUserno;
	}

	public MonitorRecordTaskVO setRecordUserno(String recordUserno) {
		this.recordUserno = recordUserno;
		return this;
	}
	
	public Long getTaskId() {
		return taskId;
	}

	public MonitorRecordTaskVO setTaskId(Long taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getTaskName() {
		return taskName;
	}

	public MonitorRecordTaskVO setTaskName(String taskName) {
		this.taskName = taskName;
		return this;
	}
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public MonitorRecordTaskVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}
	
	public Long getAlarm_size_mb() {
		return alarm_size_mb;
	}

	public MonitorRecordTaskVO setAlarm_size_mb(Long alarm_size_mb) {
		this.alarm_size_mb = alarm_size_mb;
		return this;
	}

	@Override
	public MonitorRecordTaskVO set(MonitorRecordPO entity) throws Exception {
		this.setId(entity.getId())
			.setStatus(entity.getStatus()==null?"":entity.getStatus().getName())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFileName(entity.getFileName())
			.setVideoSource(new StringBufferWrapper().append(entity.getVideoBundleName()).append("-").append(entity.getVideoChannelId()).toString())
			.setAudioSource(new StringBufferWrapper().append(entity.getAudioBundleName()==null?"":entity.getAudioBundleName()).append("-").append(entity.getAudioChannelId()==null?"":entity.getAudioChannelId()).toString())
			.setStartTime(DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(entity.getEndTime()==null?"-":DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern))
			.setMode(entity.getMode().getName())
			.setType(entity.getType()==null?MonitorRecordType.LOCAL_DEVICE.getName():entity.getType().getName())
			.setRecordUserId(entity.getRecordUserId()==null?"-":entity.getRecordUserId().toString())
			.setRecordUsername(entity.getRecordUsername()==null?"-":entity.getRecordUsername())
			.setRecordUserno(entity.getRecordUserno()==null?"-":entity.getRecordUserno())
			.setPreviewUrl(entity.getPreviewUrl()==null?"":entity.getPreviewUrl())
			.setUserId(entity.getUserId()==null?null:entity.getUserId())
			.setUserno(entity.getUserno()==null?"":entity.getUserno())
			.setNickname(entity.getNickname()==null?"":entity.getNickname())
			.setTaskId(entity.getTaskId()==null?null:entity.getTaskId())
			.setTaskName(entity.getTaskName()==null?null:entity.getTaskName())
			.setBundleId(entity.getAudioBundleId()==null?"":entity.getAudioBundleId())
			.setTotal_size_mb(entity.getTotal_size_mb())
			.setTime_duration(entity.getTime_duration())
			.setAlarm_size_mb(entity.getAlarm_size_mb());
		return this;
	}
	
	/**
	 * 扩展下载权限<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月27日 下午5:37:58
	 * @param entity
	 * @param bo
	 * @return
	 * @throws Exception
	 */
	public MonitorRecordTaskVO set(MonitorRecordPO entity,ResourceIdListBO bo) throws Exception {
		set(entity);
		
		if(bo!=null&&bo.getResourceCodes()!=null&&bo.getResourceCodes().size()>0){
			if(entity.getType().equals(MonitorRecordType.LOCAL_DEVICE)&&bo.getResourceCodes().contains(entity.getAudioBundleId()+BUSINESS_OPR_TYPE.DOWNLOAD.getCode())){
				this.setPrivilegeOfDownload(Boolean.TRUE);
			}else if(entity.getType().equals(MonitorRecordType.LOCAL_USER)&&bo.getResourceCodes().contains(entity.getUserno()+BUSINESS_OPR_TYPE.DOWNLOAD.getCode())){
				this.setPrivilegeOfDownload(Boolean.TRUE);
			}else{
				this.setPrivilegeOfDownload(Boolean.FALSE);
			}
		}
		return this;
	}

}
