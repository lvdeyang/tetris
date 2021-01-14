package com.sumavision.tetris.cs.channel;

import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;
import com.sumavision.tetris.user.UserVO;

public class ChannelVO extends AbstractBaseVO<ChannelVO, ChannelPO> {

	private String name;

	private String date;

	private String remark;
	
	private String level;
	
	private Boolean hasFile;
	
	private String endDate;
	
	private String broadWay;
	
	private String broadcastStatus;
	
	private String groupId;
	
	private String type;
	
	private Boolean encryption;
	
	private Boolean autoBroad;
	
	private Boolean autoBroadShuffle;
	
	private Integer autoBroadDuration;
	
	private String autoBroadStart;
	
	private long autoBroadTemplateId;
	
	private String outputUserPort;
	
	private String outputUserEndPort;
	
	private List<BroadAbilityBroadInfoVO> output;
	
	private List<UserVO> outputUsers; 
	
	private String transcodeTemplateId;
	
	private String protoType;
	
	private String gasketPath;
	
	private String channelUuid;
	
	private String freq;
	
	private String videoPid;
	
	private String videoType;
	
	private String audioPid;
	
	private String audioType;
	
	/**转码模板*/
	private String taskTemple;
	
	/**码率控制*/
	private String rateCtrl;
	
	/**输出码率*/
	private String rate;
	
	/**编码码率*/
	private String codeRate;
	
	/** 分辨率*/
	private String resolution;
	
	/**是否轮播*/
	private boolean rotation;
	
	/**垫播媒资地址*/
	private String backfileUrl;
	
	/**垫播媒资时长*/
	private String backfileDuration;
	
	/**垫播媒资名称*/
	private String backfileName;
	
	/**视频编码类型*/
	private String audioCodec;
	
	/**音频编码类型*/
	private String videoCodec;
	
	
	@Override
	public ChannelVO set(ChannelPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setTranscodeTemplateId("1")
		.setProtoType("udp")
		.setGasketPath("http://192.165.56.85:1234")
		.setDate(entity.getDate())
		.setRemark(entity.getRemark())
		.setBroadWay(entity.getBroadWay())
		.setBroadcastStatus(entity.getBroadcastStatus())
		.setGroupId(entity.getGroupId())
		.setType(entity.getType())
		.setEncryption(entity.getEncryption())
		.setAutoBroad(entity.getAutoBroad())
		.setChannelUuid(entity.getChannelUuid())
		.setTaskTemple(entity.getTaskTemple())
		.setBackfileDuration(entity.getBackfileDuration())
		.setBackfileName(entity.getBackfileName())
		.setBackfileUrl(entity.getBackfileUrl())
		.setRate(entity.getRate())
		.setRateCtrl(entity.getRateCtrl())
		.setCodeRate(entity.getCodeRate())
		.setResolution(entity.getResolution())
		.setAudioCodec(entity.getAudioCodec())
		.setVideoCodec(entity.getVideoCodec());
		return this;
	}
	
	
	
	public String getBackfileUrl() {
		return backfileUrl;
	}



	public ChannelVO setBackfileUrl(String backfileUrl) {
		this.backfileUrl = backfileUrl;
		return this;
	}



	public String getBackfileDuration() {
		return backfileDuration;
	}



	public ChannelVO setBackfileDuration(String backfileDuration) {
		this.backfileDuration = backfileDuration;
		return this;
	}



	public String getBackfileName() {
		return backfileName;
	}



	public ChannelVO setBackfileName(String backfileName) {
		this.backfileName = backfileName;
		return this;
	}



	public String getTaskTemple() {
		return taskTemple;
	}

	public ChannelVO setTaskTemple(String taskTemple) {
		this.taskTemple = taskTemple;
		return this;
	}

	
	public String getRateCtrl() {
		return rateCtrl;
	}

	public ChannelVO setRateCtrl(String rateCtrl) {
		this.rateCtrl = rateCtrl;
		return this;
	}

	public String getRate() {
		return rate;
	}

	public ChannelVO setRate(String rate) {
		this.rate = rate;
		return this;
	}

	public boolean isRotation() {
		return rotation;
	}

	public ChannelVO setRotation(boolean rotation) {
		this.rotation = rotation;
		return this;
	}

	public String getTranscodeTemplateId() {
		return transcodeTemplateId;
	}

	public ChannelVO setTranscodeTemplateId(String transcodeTemplateId) {
		this.transcodeTemplateId = transcodeTemplateId;
		return this;
	}

	public String getProtoType() {
		return protoType;
	}

	public ChannelVO setProtoType(String protoType) {
		this.protoType = protoType;
		return this;
	}

	public String getGasketPath() {
		return gasketPath;
	}

	public ChannelVO setGasketPath(String gasketPath) {
		this.gasketPath = gasketPath;
		return this;
	}

	public String getName() {
		return name;
	}

	public ChannelVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public ChannelVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getLevel() {
		return level;
	}

	public ChannelVO setLevel(String level) {
		this.level = level;
		return this;
	}

	public Boolean getHasFile() {
		return hasFile;
	}

	public ChannelVO setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
		return this;
	}

	public String getEndDate() {
		return endDate;
	}

	public ChannelVO setEndDate(String endDate) {
		this.endDate = endDate;
		return this;
	}

	public String getDate() {
		return date;
	}

	public ChannelVO setDate(String date) {
		this.date = date;
		return this;
	}

	public String getBroadWay() {
		return broadWay;
	}

	public ChannelVO setBroadWay(String broadWay) {
		this.broadWay = broadWay;
		return this;
	}

	public String getBroadcastStatus() {
		return broadcastStatus;
	}

	public ChannelVO setBroadcastStatus(String broadcastStatus) {
		this.broadcastStatus = broadcastStatus;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public ChannelVO setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getType() {
		return type;
	}

	public ChannelVO setType(String type) {
		this.type = type;
		return this;
	}

	public Boolean getEncryption() {
		return encryption;
	}

	public ChannelVO setEncryption(Boolean encryption) {
		this.encryption = encryption;
		return this;
	}

	public Boolean getAutoBroad() {
		return autoBroad;
	}

	public ChannelVO setAutoBroad(Boolean autoBroad) {
		this.autoBroad = autoBroad;
		return this;
	}

	public Boolean getAutoBroadShuffle() {
		return autoBroadShuffle;
	}

	public ChannelVO setAutoBroadShuffle(Boolean autoBroadShuffle) {
		this.autoBroadShuffle = autoBroadShuffle;
		return this;
	}

	public Integer getAutoBroadDuration() {
		return autoBroadDuration;
	}

	public ChannelVO setAutoBroadDuration(Integer autoBroadDuration) {
		this.autoBroadDuration = autoBroadDuration;
		return this;
	}

	public String getAutoBroadStart() {
		return autoBroadStart;
	}

	public ChannelVO setAutoBroadStart(String autoBroadStart) {
		this.autoBroadStart = autoBroadStart;
		return this;
	}

	public String getOutputUserPort() {
		return outputUserPort;
	}

	public ChannelVO setOutputUserPort(String outputUserPort) {
		this.outputUserPort = outputUserPort;
		return this;
	}

	public String getOutputUserEndPort() {
		return outputUserEndPort;
	}

	public ChannelVO setOutputUserEndPort(String outputUserEndPort) {
		this.outputUserEndPort = outputUserEndPort;
		return this;
	}

	public List<BroadAbilityBroadInfoVO> getOutput() {
		return output;
	}

	public ChannelVO setOutput(List<BroadAbilityBroadInfoVO> output) {
		this.output = output;
		return this;
	}

	public List<UserVO> getOutputUsers() {
		return outputUsers;
	}

	public ChannelVO setOutputUsers(List<UserVO> outputUsers) {
		this.outputUsers = outputUsers;
		return this;
	}

	public String getChannelUuid() {
		return channelUuid;
	}

	public ChannelVO setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
		return this;
	}

	public String getFreq() {
		return freq;
	}

	public ChannelVO setFreq(String freq) {
		this.freq = freq;
		return this;
	}

	public String getVideoPid() {
		return videoPid;
	}

	public ChannelVO setVideoPid(String videoPid) {
		this.videoPid = videoPid;
		return this;
	}

	public String getVideoType() {
		return videoType;
	}

	public ChannelVO setVideoType(String videoType) {
		this.videoType = videoType;
		return this;
	}

	public String getAudioPid() {
		return audioPid;
	}

	public ChannelVO setAudioPid(String audioPid) {
		this.audioPid = audioPid;
		return this;
	}

	public String getAudioType() {
		return audioType;
	}

	public ChannelVO setAudioType(String audioType) {
		this.audioType = audioType;
		return this;
	}

	public long getAutoBroadTemplateId() {
		return autoBroadTemplateId;
	}

	public ChannelVO setAutoBroadTemplateId(long autoBroadTemplateId) {
		this.autoBroadTemplateId = autoBroadTemplateId;
		return this;
	}



	public String getCodeRate() {
		return codeRate;
	}

	public ChannelVO setCodeRate(String codeRate) {
		this.codeRate = codeRate;
		return this;
	}



	public String getResolution() {
		return resolution;
	}


	public ChannelVO setResolution(String resolution) {
		this.resolution = resolution;
		return this;
	}



	public String getAudioCodec() {
		return audioCodec;
	}



	public ChannelVO setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
		return this;
	}



	public String getVideoCodec() {
		return videoCodec;
	}



	public ChannelVO setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
		return this;
	}
	
}
