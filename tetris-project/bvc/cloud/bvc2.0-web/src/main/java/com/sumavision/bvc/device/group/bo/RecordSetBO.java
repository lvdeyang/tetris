package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.po.RecordPO;

public class RecordSetBO {

	private String taskId = "";
	
	private String uuid = "";
	
	/** 会议uuid，供逻辑层查询，提取地区等信息 **/
	private String groupUuid = "";
	
	/** 1 会议， 2 监控， 5 发布rtmp(不建立录制，只发布rtmp) **/
	private String videoType = "";
	
	private String videoName = "";
	
	private String description = "";
	
	/** 地区分类信息，每次生成去group和dictionary查  */
	private String locationID = "";
	
	private String categoryLiveID = "";
	
	private String categoryID = "";
	
	/** 是否将录制转换成点播，默认0不准换；1转换，见RecordToVodType  */
	private String transferToVod = "0";
	
	private String url;
	
	private String playUrl;
	
	private CodecParamBO codec_param = new CodecParamBO();
	
	private RecordSourceBO video_source = new RecordSourceBO();
	
	private RecordSourceBO audio_source = new RecordSourceBO();

	public String getTaskId() {
		return taskId;
	}

	public RecordSetBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public RecordSetBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getVideoType() {
		return videoType;
	}

	public RecordSetBO setVideoType(String videoType) {
		this.videoType = videoType;
		return this;
	}

	public String getVideoName() {
		return videoName;
	}

	public RecordSetBO setVideoName(String videoName) {
		this.videoName = videoName;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public RecordSetBO setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getLocationID() {
		return locationID;
	}

	public RecordSetBO setLocationID(String locationID) {
		this.locationID = locationID;
		return this;
	}

	public String getCategoryLiveID() {
		return categoryLiveID;
	}

	public RecordSetBO setCategoryLiveID(String categoryLiveID) {
		this.categoryLiveID = categoryLiveID;
		return this;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public RecordSetBO setCategoryID(String categoryID) {
		this.categoryID = categoryID;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public RecordSetBO setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public RecordSetBO setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public RecordSetBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

	public RecordSourceBO getVideo_source() {
		return video_source;
	}

	public RecordSetBO setVideo_source(RecordSourceBO video_source) {
		this.video_source = video_source;
		return this;
	}

	public RecordSourceBO getAudio_source() {
		return audio_source;
	}

	public RecordSetBO setAudio_source(RecordSourceBO audio_source) {
		this.audio_source = audio_source;
		return this;
	}

	public String getTransferToVod() {
		return transferToVod;
	}

	public RecordSetBO setTransferToVod(String transferToVod) {
		this.transferToVod = transferToVod;
		return this;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public RecordSetBO setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
		return this;
	}
	
	/**
	 * @Title: 从业务数据中复制数据<br/> 
	 * @param record 业务录制数据
	 * @param codec 参数模板
	 * @return RecordSetBO 协议数据 
	 */
	public RecordSetBO set(RecordPO record, CodecParamBO codec){
		this.setUuid(record.getUuid())
			.setGroupUuid(record.getGroup().getUuid())
			.setVideoType(record.getGroupType().getProtocalId())
			.setVideoName(record.getVideoName())
			.setDescription(record.getDescription())
			.setCodec_param(codec);
		
		//发布直播(rtmp)的时候，videoType=5，逻辑层不建立录制，只发布rtmp
		if(RecordType.PUBLISH.equals(record.getType())){
			this.setVideoType("5");
		}
		
		if(record.getVideoType() != null){
			this.setVideo_source(new RecordSourceBO());
			if(record.getVideoType().equals(SourceType.CHANNEL)){
				this.getVideo_source().setType(SourceType.CHANNEL.getProtocalName())
									  .setLayer_id(record.getVideoLayerId())
								      .setBundle_id(record.getVideoBundleId())
								      .setChannel_id(record.getVideoChannelId());
			}else{
				this.getVideo_source().setType(SourceType.COMBINEVIDEO.getProtocalName())
									  .setUuid(record.getCombineVideoUuid());
			}
		}
		
		if(record.getAudioType() != null){
			this.setAudio_source(new RecordSourceBO());
			if(record.getAudioType().equals(SourceType.CHANNEL)){
				this.getAudio_source().setType(SourceType.CHANNEL.getProtocalName())
								      .setLayer_id(record.getAudioLayerId())
								      .setBundle_id(record.getAudioBundleId())
								      .setChannel_id(record.getAudioChannelId());
			}else{
				this.getAudio_source().setType(SourceType.COMBINEAUDIO.getProtocalName())
									  .setUuid(record.getCombineAudioUuid());
			}  
		}
		
		return this;
	}
	
}
