package com.sumavision.tetris.sts.task.tasklink;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sumavision.tetris.sts.common.CommonConstants.NodeStatus;
import com.sumavision.tetris.sts.common.CommonPO;
import com.sumavision.tetris.sts.task.source.AudioParamPO;
import com.sumavision.tetris.sts.task.source.VideoParamPO;




@Entity
@Table(name="trans_task")
public class TransTaskPO extends CommonPO<TransTaskPO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1234676034915530711L;
	
	/**
	 * 所属taskLinkPO的id
	 */
	private Long linkId;
	
	/**
	 * 节点id
	 */
	private Long nodeId;

	/**
	 * 所在deviceNode的id，是device内部的一个节点，注意不是devicePO的id，代码涉及面广，后续重构完将名称改成deviceNodeId
	 */
	private Long deviceId;
	
	/**
	 * 所属input的id
	 */
	private Long inputId;
	
	/**
	 * 所属节目源的id
	 */
	private Long sourceId;
	
	/**
	 * 节目号
	 */
	private Integer programNum;
	
	
	/**
	 * 节目节点ID
	 */
	private Long programNodeId;
	
	/**
	 * 任务类型（是否需要？）
	 * 0：普通任务
	 * 1：多码率自适应任务  
	 */
	private Integer taskType;
	/**
	 * 任务节点状态
	 * 0：正常
	 * 1：异常
	 * 2：删除中
	 */
	private NodeStatus nodeStatus;
	
	/**
	 * json格式存储给能力下发盖播信息
	 */
	private String coverPalyerDetail;
	
	/**
	 * 任务视频参数
	 */
	private Set<VideoParamPO> videoParams;
	/**
	 * 任务音频参数
	 */
	private Set<AudioParamPO> audioParams;
	
	/*
	 * key是轨道号trackid，不存数据库的两个字段，方便根据轨道号取数据
	 */
	private Map<Integer, VideoParamPO> videoParamsMap;
	private Map<Integer, AudioParamPO> audioParamsMap;
	
	private Integer cardNumber;
	
	private Long deviceChannelId;
	
	public TransTaskPO(){
		videoParams = new HashSet<VideoParamPO>();
		audioParams = new HashSet<AudioParamPO>();
	}
	
	@Column
	public Long getLinkId() {
		return linkId;
	}
	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}
	
	
	@Column
	public Integer getTaskType() {
		return taskType;
	}
	
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="transTaskId")
	public Set<VideoParamPO> getVideoParams() {
		return videoParams;
	}

	public void setVideoParams(Set<VideoParamPO> videoParams) {
		this.videoParams = videoParams;
	}

	@Column
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public NodeStatus getNodeStatus() {
		return nodeStatus;
	}
	public void setNodeStatus(NodeStatus nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="transTaskId")
	public Set<AudioParamPO> getAudioParams() {
		return audioParams;
	}

	public void setAudioParams(Set<AudioParamPO> audioParams) {
		this.audioParams = audioParams;
	}

	@Column
	public Long getInputId() {
		return inputId;
	}

	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}

	@Column
	public Integer getProgramNum() {
		return programNum;
	}

	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}
	@Column
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	
	
	@Column(columnDefinition="TEXT")
	public String getCoverPalyerDetail() {
		return coverPalyerDetail;
	}
	public void setCoverPalyerDetail(String coverPalyerDetail) {
		this.coverPalyerDetail = coverPalyerDetail;
	}
	
	@Transient
	public Map<Integer, VideoParamPO> getVideoParamsMap() {
		if(videoParamsMap == null || videoParamsMap.size()  == 0){
			videoParamsMap = new HashMap<Integer, VideoParamPO>();
			for(VideoParamPO videoParamPO : videoParams){
				videoParamsMap.put(videoParamPO.getTrackId(), videoParamPO);
			}
		}
		return videoParamsMap;
	}

	@Transient
	public Map<Integer, AudioParamPO> getAudioParamsMap() {
		if(audioParamsMap == null || audioParamsMap.size() == 0){
			audioParamsMap = new HashMap<Integer, AudioParamPO>();
			for(AudioParamPO audioParamPO : audioParams){
				audioParamsMap.put(audioParamPO.getTrackId(), audioParamPO);
			}
		}
		return audioParamsMap;
	}
	
	public void addVideo(VideoParamPO videoParamPO) {
		this.videoParams.add(videoParamPO);
	}

	public void addAudio(AudioParamPO audioParamPO) {
		this.audioParams.add(audioParamPO);
	}

	public Integer getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Integer cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public Long getDeviceChannelId() {
		return deviceChannelId;
	}

	public void setDeviceChannelId(Long deviceChannelId) {
		this.deviceChannelId = deviceChannelId;
	}

	@Column
	public Long getProgramNodeId() {
		return programNodeId;
	}

	public void setProgramNodeId(Long programNodeId) {
		this.programNodeId = programNodeId;
	}
	
	public Boolean isSameWithCfg(TransTaskPO transTaskPO, String transType){
		if(transTaskPO == null){
			return false;
		}
		if (this.nodeId == null) {
			if (transTaskPO.nodeId != null)
				return false;
		} else if (!this.nodeId.equals(transTaskPO.nodeId))
			return false;
		if (this.inputId == null) {
			if (transTaskPO.inputId != null)
				return false;
		} else if (!this.inputId.equals(transTaskPO.inputId))
			return false;
		if (this.programNodeId == null) {
			if (transTaskPO.programNodeId != null)
				return false;
		} else if (!this.programNodeId.equals(transTaskPO.programNodeId))
			return false;
		
		if (this.videoParams == null) {
			if (transTaskPO.videoParams != null)
				return false;
		} else if (this.videoParams.size() > 0 && this.videoParams.size() == transTaskPO.videoParams.size()){
			for (VideoParamPO videoParamPO : this.videoParams) {
				for (VideoParamPO videoParamCfgPO : transTaskPO.videoParams) {
					if(videoParamPO.getTrackId().equals(videoParamCfgPO.getTrackId())){
						if(!videoParamPO.isSameWithCfg(videoParamCfgPO, transType)){
							return false;
		}}}}}
		
		if (this.audioParams == null) {
			if (transTaskPO.audioParams != null)
				return false;
		} else if (this.audioParams.size() > 0 && this.audioParams.size() == transTaskPO.audioParams.size()){
			for (AudioParamPO audioParamPO : this.audioParams) {
				for (AudioParamPO audioParamCfgPO : transTaskPO.audioParams) {
					if(audioParamPO.getTrackId().equals(audioParamCfgPO.getTrackId())){
						if(!audioParamPO.isSameWithCfg(audioParamCfgPO)){
							return false;
		}}}}}
		
		return true;
	}

}
