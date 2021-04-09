package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.po.GroupPublishStreamPO;
import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordPO;

public class PublishStreamSetBO {

	private String taskId = "";
	
	private String uuid = "";
	
	private String recordUuid = "";
	
//	private String videoType = "";
	
	private String videoName = "";
	
	private String format = "";
	
	private String url = "";
	
	private CodecParamBO codec_param = new CodecParamBO();
	
	private RecordSourceBO video_source = new RecordSourceBO();
	
	private RecordSourceBO audio_source = new RecordSourceBO();

	public String getTaskId() {
		return taskId;
	}

	public PublishStreamSetBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public PublishStreamSetBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getVideoName() {
		return videoName;
	}

	public PublishStreamSetBO setVideoName(String videoName) {
		this.videoName = videoName;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public PublishStreamSetBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

	public RecordSourceBO getVideo_source() {
		return video_source;
	}

	public PublishStreamSetBO setVideo_source(RecordSourceBO video_source) {
		this.video_source = video_source;
		return this;
	}

	public RecordSourceBO getAudio_source() {
		return audio_source;
	}

	public PublishStreamSetBO setAudio_source(RecordSourceBO audio_source) {
		this.audio_source = audio_source;
		return this;
	}
	
	/**
	 * @Title: 从业务数据中复制数据<br/> 
	 * @param record 业务录制数据
	 * @param codec 参数模板
	 * @return RecordSetBO 协议数据 
	 */
	public PublishStreamSetBO set(PublishStreamPO publishStream, CodecParamBO codec){
		RecordPO record = publishStream.getRecord();
		this.setUuid(publishStream.getUuid())
			.setRecordUuid(record.getUuid())
			.setFormat(publishStream.getFormat())
			.setUrl(publishStream.getUrl())
			.setVideoName(record.getVideoName())
			.setCodec_param(codec);
		
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
	
	/**
	 * @Title: 从业务数据中复制数据<br/> 
	 * @param record 业务录制数据
	 * @param codec 参数模板
	 * @return RecordSetBO 协议数据 
	 */
	public PublishStreamSetBO set(GroupPublishStreamPO publishStream, CodecParamBO codec){
		this.setUuid(publishStream.getUuid())
			.setRecordUuid(publishStream.getUuid())
			.setFormat(publishStream.getFormat())
			.setUrl(publishStream.getUrl())
			.setVideoName("")
			.setCodec_param(codec);
		
		if(publishStream.getVideoType() != null){
			this.setVideo_source(new RecordSourceBO());
			if(publishStream.getVideoType().equals(SourceType.CHANNEL)){
				this.getVideo_source().setType(SourceType.CHANNEL.getProtocalName())
									  .setLayer_id(publishStream.getVideoLayerId())
								      .setBundle_id(publishStream.getVideoBundleId())
								      .setChannel_id(publishStream.getVideoChannelId());
			}else{
				this.getVideo_source().setType(SourceType.COMBINEVIDEO.getProtocalName())
									  .setUuid(publishStream.getCombineVideoUuid());
			}
		}
		
		if(publishStream.getAudioType() != null){
			this.setAudio_source(new RecordSourceBO());
			if(publishStream.getAudioType().equals(SourceType.CHANNEL)){
				this.getAudio_source().setType(SourceType.CHANNEL.getProtocalName())
								      .setLayer_id(publishStream.getAudioLayerId())
								      .setBundle_id(publishStream.getAudioBundleId())
								      .setChannel_id(publishStream.getAudioChannelId());
			}else{
				this.getAudio_source().setType(SourceType.COMBINEAUDIO.getProtocalName())
									  .setUuid(publishStream.getCombineAudioUuid());
			}  
		}
		
		return this;
	}

	public String getRecordUuid() {
		return recordUuid;
	}

	public PublishStreamSetBO setRecordUuid(String recordUuid) {
		this.recordUuid = recordUuid;
		return this;
	}

	public String getFormat() {
		return format;
	}

	public PublishStreamSetBO setFormat(String format) {
		this.format = format;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public PublishStreamSetBO setUrl(String url) {
		this.url = url;
		return this;
	}
	
}
