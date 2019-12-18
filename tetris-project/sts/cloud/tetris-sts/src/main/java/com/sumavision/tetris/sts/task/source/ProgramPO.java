package com.sumavision.tetris.sts.task.source;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.sts.common.CommonPO;

@Entity
@Table
public class ProgramPO extends CommonPO<ProgramPO> implements Serializable, Comparable<ProgramPO>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6269574265121852816L;

	private String programName;

	private String programProvider;

	private Integer programNum;

	private Integer pcrPid;

	private Integer pmtPid;

	private Integer encryption;
	
	private String deinterlaceMode = "middle";

	private String audioJson;
	private String videoJson;
	private String subtitleJson;

	/*
	 * 音视频详细信息是有顺序的，
	 */
	private List<AudioElement> audioElements;

	private List<VideoElement> videoElements;

	private List<SubtitleElement> subtitleElements;
	
	private Long backupSourceId;
	
	private Integer backupProgramNum;
	
	//所属任务输入（InputPO）ID
	private Long inputId;
	
	//节点ID
	private Long nodeId;
	
	//所属编码卡卡号
	private Integer cardNum;
	
	//解码方式:cpu/gpu/auto
	private String decodeMode;
	
	public enum BackupMode{
		AUTO, //舍弃
		AUTO_HIGH_FRST, //舍弃  近期避免数据库变化，暂留
		AUTO_MAIN,
		AUTO_BACKUP,
		AUTO_MAT
	}
	
	private BackupMode backupMode = BackupMode.AUTO_MAIN;
	
	private Boolean highFirstFlag = true;
	
	public enum CurWorkSource{
		MAIN,
		BACKUP,
		MAT
	}
	
	private CurWorkSource curWorkSource;

	/**
	 * 422标致
	 */
	private Boolean fourTwoTwoFlag = false;
	
	@Column
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	@Column
	public String getProgramProvider() {
		return programProvider;
	}
	public void setProgramProvider(String programProvider) {
		this.programProvider = programProvider;
	}

	@Column
	public Integer getEncryption() {
		return encryption;
	}
	public void setEncryption(Integer encryption) {
		this.encryption = encryption;
	}
	@Column(columnDefinition="TEXT")
	public String getAudioJson() {
		return audioJson;
	}

	public void setAudioJson(String audioJson) {
		this.audioJson = audioJson;
	}
	@Column(columnDefinition="TEXT")
	public String getVideoJson() {
		return videoJson;
	}

	public void setVideoJson(String videoJson) {
		this.videoJson = videoJson;
	}
	@Column(columnDefinition="TEXT")
	public String getSubtitleJson() {
		return subtitleJson;
	}

	public void setSubtitleJson(String subtitleJson) {
		this.subtitleJson = subtitleJson;
	}

	@Column
	public Integer getProgramNum() {
		return programNum;
	}

	public void setProgramNum(Integer programNum) {
		this.programNum = programNum;
	}
	@Column
	public Integer getPcrPid() {
		return pcrPid;
	}

	public void setPcrPid(Integer pcrPid) {
		this.pcrPid = pcrPid;
	}
	@Column
	public Integer getPmtPid() {
		return pmtPid;
	}

	public void setPmtPid(Integer pmtPid) {
		this.pmtPid = pmtPid;
	}

	@Transient
	public List<AudioElement> getAudioElements() {
		if(audioElements == null || audioElements.isEmpty()){
			audioElements = JSON.parseArray(this.audioJson , AudioElement.class);
			if(audioElements == null){
				audioElements = new ArrayList<AudioElement>();
			}
		}
		return audioElements;
	}

	public void setAudioElements(List<AudioElement> audioElements) {
		this.audioElements = audioElements;
		this.audioJson = JSONObject.toJSONString(audioElements);
	}

	@Transient
	public List<VideoElement> getVideoElements() {
		if(videoElements == null || videoElements.isEmpty()){
			videoElements = JSON.parseArray(this.videoJson , VideoElement.class);
			if(videoElements == null){
				videoElements = new ArrayList<VideoElement>();
			}
		}
		return videoElements;
	}

	public void setVideoElements(List<VideoElement> videoElements) {
		this.videoElements = videoElements;
		this.videoJson = JSONObject.toJSONString(videoElements);
	}

	@Transient
	public List<SubtitleElement> getSubtitleElements() {
		return JSON.parseArray(this.subtitleJson , SubtitleElement.class);
	}

	public void setSubtitleElements(List<SubtitleElement> subtitleElements) {
		this.subtitleElements = subtitleElements;
		this.subtitleJson = JSONObject.toJSONString(subtitleElements);
	}

	@Column
	public Long getBackupSourceId() {
		return backupSourceId;
	}
	public void setBackupSourceId(Long backupSourceId) {
		this.backupSourceId = backupSourceId;
	}
	
	@Column
	public Integer getBackupProgramNum() {
		return backupProgramNum;
	}
	public void setBackupProgramNum(Integer backupProgramNum) {
		this.backupProgramNum = backupProgramNum;
	}
	public void toJson() {
		this.audioJson = audioElements == null? null : JSON.toJSONString(audioElements);
		this.videoJson = videoElements == null ? null : JSON.toJSONString(videoElements);
		//目前刷出的流里面都没有字幕，错误的字幕信息还导致程序报错，暂时注掉
		//this.subtitleJson = null;
		this.subtitleJson = subtitleElements == null ? null : JSON.toJSONString(subtitleElements);
	}
	
	
	@Column
	public String getDeinterlaceMode() {
		return deinterlaceMode;
	}
	public void setDeinterlaceMode(String deinterlaceMode) {
		this.deinterlaceMode = deinterlaceMode;
	}

	@Column
	public BackupMode getBackupMode() {
		return backupMode;
	}
	public void setBackupMode(BackupMode backupMode) {
		this.backupMode = backupMode;
	}
	@Column
	public CurWorkSource getCurWorkSource() {
		return curWorkSource;
	}
	public void setCurWorkSource(CurWorkSource curWorkSource) {
		this.curWorkSource = curWorkSource;
	}
	
	@Column
	public Boolean getHighFirstFlag() {
		return highFirstFlag;
	}
	public void setHighFirstFlag(Boolean highFirstFlag) {
		this.highFirstFlag = highFirstFlag;
	}
	
	@Column
	public Long getInputId() {
		return inputId;
	}
	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}
	
	@Column
	public Integer getCardNum() {
		return cardNum;
	}
	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}
	
	@Column
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	
	@Column
	public String getDecodeMode() {
		return decodeMode;
	}
	public void setDecodeMode(String decodeMode) {
		this.decodeMode = decodeMode;
	}

	@Column
	public Boolean getFourTwoTwoFlag() {
		return fourTwoTwoFlag;
	}

	public void setFourTwoTwoFlag(Boolean fourTwoTwoFlag) {
		this.fourTwoTwoFlag = fourTwoTwoFlag;
	}

	/**
	 * 需要clone的地方基本都是因为programpo同时被input表和source表关联，
	 * 拿出来不能直接用，否则数据库会错误，所以需要clone，保留id外的其它信息
	 * @return
	 */
	public ProgramPO deepClone(){
		ProgramPO programPO = new ProgramPO();
		programPO.setAudioJson(audioJson);
		programPO.setEncryption(encryption);
		programPO.setPcrPid(pcrPid);
		programPO.setPmtPid(pmtPid);
		programPO.setProgramName(programName);
		programPO.setProgramProvider(programProvider);
		programPO.setSubtitleJson(subtitleJson);
		programPO.setVideoJson(videoJson);
		programPO.setProgramNum(programNum);
		programPO.setDeinterlaceMode(deinterlaceMode);
		programPO.setSubtitleJson(subtitleJson);
		programPO.setDecodeMode(decodeMode);
		return programPO;
	}
	
	
	/**
	 * 严格对比，判断节目信息是否一样
	 * @param other
	 * @return
	 */
	public boolean equals(ProgramPO other){
		if(!this.getProgramNum().equals(other.getProgramNum())){
			return false;
		}
		if(!this.getPcrPid().equals(other.getPcrPid())){
			return false;
		}
		//视频对比
		if(this.getVideoElements().size() != other.getVideoElements().size()){
			return false;
		}
		Collections.sort(this.getVideoElements());
		Collections.sort(other.getVideoElements());
		for(int index = 0; index < this.getVideoElements().size(); index++){
			if(!this.getVideoElements().get(index).equals(other.getVideoElements().get(index))){
				return false;
			}
		}
		//音频对比
		if(this.getAudioElements().size() != other.getAudioElements().size()){
			return false;
		}
		Collections.sort(this.getAudioElements());
		Collections.sort(other.getAudioElements());
		for(int index = 0; index < this.getAudioElements().size(); index++){
			if(!this.getAudioElements().get(index).equals(other.getAudioElements().get(index))){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 替换条件对比，目标节目是否可作为本节目的替换（换源或者源更新功能）或者备份
	 * 只要各视音轨编码类型一样
	 * @param other
	 * @return
	 */
	public boolean isAbleToExchange(ProgramPO other){
		//视频对比
		if(this.getVideoElements().size() != other.getVideoElements().size()){
			return false;
		}
		Collections.sort(this.getVideoElements());
		Collections.sort(other.getVideoElements());
		for(int index = 0; index < this.getVideoElements().size(); index++){
			//只对比编码方式
			if(!this.getVideoElements().get(index).getType().equals(other.getVideoElements().get(index).getType())){
				return false;
			}
		}
		//音频对比
		if(this.getAudioElements().size() != other.getAudioElements().size()){
			return false;
		}
		Collections.sort(this.getAudioElements());
		Collections.sort(other.getAudioElements());
		for(int index = 0; index < this.getAudioElements().size(); index++){
			//只对比编码方式
			if(!this.getAudioElements().get(index).getType().equals(other.getAudioElements().get(index).getType())){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int compareTo(ProgramPO o) {
		// TODO Auto-generated method stub
		if(this.getProgramNum().intValue() < o.getProgramNum().intValue()){
			return -1;
		}
		return 1;
	}
	
}
