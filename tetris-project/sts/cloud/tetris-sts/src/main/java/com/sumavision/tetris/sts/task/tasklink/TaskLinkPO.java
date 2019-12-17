package com.sumavision.tetris.sts.task.tasklink;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.task.source.AudioElement;
import com.sumavision.tetris.sts.task.source.VideoElement;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="TaskLinkPO")
public class TaskLinkPO extends CommonPO<TaskLinkPO> implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6646641139613859872L;
	/**
	 * 链路名称
	 */
	private String linkName;
	/**
	 * 链路状态，枚举
	 */
	private TaskLinkStatus linkStatus;
	
	/**
	 * 是否有告警标识
	 */
	private Boolean alarmFlag = false;
	
	/**
	 * 任务类型，是否转码
	 * 0：不转码
	 * 1：转码
	 */
	private Integer taskType;
	
	/**
	 * 任务链状态
	 * @author gaofeng
	 */
	public static enum TaskLinkStatus{
		RUN,//正常运行
		CREATE_ERROR,//创建错误，授权不满足等错误，实际无任务
		STOP,//正常停止
	}
	
	/**
	 * 所属任务分组TaskGroupPO的id
	 */
	private Long taskGroupId;
	/**
	 * 任务链创建时间
	 */
	private Date addTime;
	/**
	 * 所用节目源的id
	 */
	private Long sourceId;
	/**
	 * 节目号
	 */
	private Integer programNum;
	private String sourceName;
	private String programName;
	
	/**
	 * 当前工作源的信息
	 */
	private Long workSourceId;
	private Integer workProgramNum;
	private String workSourceName;
	private String workProgramName;

	/**
	 * json格式存储其余所有从页面带来的任务信息
	 */
	private String taskParamDetail;

	/**
	 * 设备分组ID
	 */
	private Long deviceGroupId;

	/**
	 * 任务开始所选设备，表示用户主动选择的设备，用于后续支持任务备份回迁，0表示自动选择
	 */
	private Long chooseDeviceNodeId = 0l;

	/**
	 * 任务当前所属设备，发生备份切换后，任务所属设备可能与开始所选设备不一致，0表示自动选择
	 */
	private Long curDeviceNodeId = 0l;
	
	/**
	 * 任务创建失败错误信息
	 */
	private String errInfo;
	
	/**
	 * 
	 */
	private String taskParamView;
	
	/**
	 * 节目预览
	 * 0：正常任务
	 * 1：任务预览任务
	 */
	private Integer preview;
	
	/**
	 * json格式存储从页面带来的盖播信息
	 */
	private String coverPalyerDetail;
	
	private Long streamAccConfigId;

	//是否使用入网关
	private Boolean useInpub = true;
	
	//N卡序号
	private Integer nCardIndex;
	
	//该任务视频解码方式
	private String programDecodeType;
	
	@Column
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	@Column
	@Enumerated(EnumType.STRING)
	public TaskLinkStatus getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(TaskLinkStatus linkStatus) {
		this.linkStatus = linkStatus;
	}

	//@Column(columnDefinition="DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createtime")
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	@Column
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	@Column
	public Integer getProgramNum() {
		return programNum;
	}
	
	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	@Column(columnDefinition="TEXT")
	public String getTaskParamDetail() {
		return taskParamDetail;
	}
	@Deprecated
	public void setTaskParamDetail(String taskParamDetail) {
		this.taskParamDetail = taskParamDetail;
	}
	@Column(name="taskGroupId")
	public Long getTaskGroupId() {
		return taskGroupId;
	}
	public void setTaskGroupId(Long taskGroupId) {
		this.taskGroupId = taskGroupId;
	}
	@Column
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	@Column
	public String getErrInfo() {
		return errInfo;
	}
	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}

	@Column
	public Boolean getAlarmFlag() {
		return alarmFlag;
	}
	public void setAlarmFlag(Boolean alarmFlag) {
		this.alarmFlag = alarmFlag;
	}
	@Column(columnDefinition="TEXT")
	public String getTaskParamView() {
		return taskParamView;
	}
	public void setTaskParamView(String taskParamView) {
		this.taskParamView = taskParamView;
	}
	@Column
	public Long getWorkSourceId() {
		return workSourceId;
	}
	public void setWorkSourceId(Long workSourceId) {
		this.workSourceId = workSourceId;
	}
	@Column
	public Integer getWorkProgramNum() {
		return workProgramNum;
	}
	public void setWorkProgramNum(Integer workProgramNum) {
		this.workProgramNum = workProgramNum;
	}
	@Column
	public String getWorkSourceName() {
		return workSourceName;
	}
	public void setWorkSourceName(String workSourceName) {
		this.workSourceName = workSourceName;
	}
	@Column
	public String getWorkProgramName() {
		return workProgramName;
	}
	public void setWorkProgramName(String workProgramName) {
		this.workProgramName = workProgramName;
	}
	@Column
	public Integer getPreview() {
		return preview;
	}
	public void setPreview(Integer preview) {
		this.preview = preview;
	}
	
	@Column(columnDefinition="TEXT")
	public String getCoverPalyerDetail() {
		return coverPalyerDetail;
	}
	public void setCoverPalyerDetail(String coverPalyerDetail) {
		this.coverPalyerDetail = coverPalyerDetail;
	}
	
	@Column
	public Long getStreamAccConfigId() {
		return streamAccConfigId;
	}
	public void setStreamAccConfigId(Long streamAccConfigId) {
		this.streamAccConfigId = streamAccConfigId;
	}

	@Column
	public Long getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(Long deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	@Column
	public Long getChooseDeviceNodeId() {
		return chooseDeviceNodeId;
	}

	public void setChooseDeviceNodeId(Long chooseDeviceNodeId) {
		this.chooseDeviceNodeId = chooseDeviceNodeId;
	}

	@Column
	public Long getCurDeviceNodeId() {
		return curDeviceNodeId;
	}

	public void setCurDeviceNodeId(Long curDeviceNodeId) {
		this.curDeviceNodeId = curDeviceNodeId;
	}

    @Column
    @Type(type = "yes_no")
	public Boolean getUseInpub() {
		return useInpub;
	}
	public void setUseInpub(Boolean useInpub) {
		this.useInpub = useInpub;
	}
	
	@Column
	public Integer getnCardIndex() {
		return nCardIndex;
	}
	public void setnCardIndex(Integer nCardIndex) {
		this.nCardIndex = nCardIndex;
	}
	
	@Column
	public String getProgramDecodeType() {
		return programDecodeType;
	}
	public void setProgramDecodeType(String programDecodeType) {
		this.programDecodeType = programDecodeType;
	}
	//从json中获取相关信息
	public void generateFromJson(JSONObject jsonObject){
		setLinkName(jsonObject.getString("taskLinkName"));
		setTaskGroupId(jsonObject.getLong("taskGroupId"));
		setDeviceGroupId(jsonObject.getLong("sdmGroupId"));
		setSourceId(jsonObject.getLong("sourceId"));
		setProgramNum(jsonObject.getInteger("programNum"));
		setTaskType(jsonObject.getInteger("taskType"));
		setCurDeviceNodeId(jsonObject.getLong("deviceNode"));
		setChooseDeviceNodeId(jsonObject.getLong("deviceNode"));
		Boolean userInpub = (null==jsonObject.getBoolean("useInpub")) ? true : jsonObject.getBoolean("useInpub");
		setUseInpub(userInpub);
		setnCardIndex(jsonObject.getInteger("nCardIndex"));
		setProgramDecodeType(jsonObject.getString("programDecodeType"));
		setTaskParamDetail(jsonObject.getJSONObject("taskParam").toJSONString());
	}
	
	public void updateVideoPid(List<VideoElement> videoList) {
		JSONObject jsonObject = obainTaskParam();
		int videoPid = 0;
		JSONArray array = jsonObject.getJSONArray("videoParams");
		for (int i = 0; i < array.size(); i++) {
			JSONObject video = array.getJSONObject(i);
			videoPid = videoList.get(video.getInteger("index")).getPid();
			video.put("pid" , videoPid);
		}
		jsonObject.put("videoPid", videoPid);
		this.taskParamDetail = jsonObject.toJSONString();
	}

	public void updateAudioPid(List<AudioElement> audioPids) {
		JSONObject jsonObject = obainTaskParam();
		JSONArray array = jsonObject.getJSONArray("audioParams");
		for (int i = 0; i < array.size(); i++) {
			JSONObject audio = array.getJSONObject(i);
			audio.put("pid" , audioPids.get(audio.getInteger("index")).getPid());
		}
		this.taskParamDetail = jsonObject.toJSONString();
	}

	
	private JSONObject obainTaskParam() {
		return JSON.parseObject(taskParamDetail);
	}
	
}
