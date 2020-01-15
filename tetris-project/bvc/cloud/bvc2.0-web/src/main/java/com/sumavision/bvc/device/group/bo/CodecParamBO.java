package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplPO;
import com.sumavision.bvc.common.group.po.CommonAvtplGearsPO;
import com.sumavision.bvc.common.group.po.CommonAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;

public class CodecParamBO {

	private AudioParamBO audio_param = new AudioParamBO();
	
	private VideoParamBO video_param = new VideoParamBO();

	public AudioParamBO getAudio_param() {
		return audio_param;
	}

	public CodecParamBO setAudio_param(AudioParamBO audio_param) {
		this.audio_param = audio_param;
		return this;
	}

	public VideoParamBO getVideo_param() {
		return video_param;
	}

	public CodecParamBO setVideo_param(VideoParamBO video_param) {
		this.video_param = video_param;
		return this;
	}
	
	/**
	 * @Title: 协议参数数据转换 <br/>
	 * @param avtpl 会议参数模板
	 * @param gear 当前会议参数档位
	 * @return CodecParamBO 协议参数
	 */
	public CodecParamBO set(DeviceGroupAvtplPO avtpl, DeviceGroupAvtplGearsPO gear){
		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName())
												.setBitrate(gear.getAudioBitRate()))
		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
				   						      .setResolution(gear.getVideoResolution().getName())
				   						      .setBitrate(gear.getVideoBitRate())
				   						      .setFps(gear.getFps()==null?"25.0":gear.getFps()));
		return this;
	}
	public CodecParamBO set(CommandGroupAvtplPO avtpl, CommandGroupAvtplGearsPO gear){
		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName()))
		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
				   						      .setResolution(gear.getVideoResolution().getName())
				   						      .setBitrate(gear.getVideoBitRate()));
		return this;
	}
	
	/**
	 * @Title: 协议参数数据转换 <br/>
	 * @param avtpl 通用会议参数模板
	 * @param gear 当前会议参数档位
	 * @return CodecParamBO 协议参数
	 */
	public CodecParamBO set(CommonAvtplPO avtpl, CommonAvtplGearsPO gear){
		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName()))
		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
				   						      .setResolution(gear.getVideoResolution().getName())
				   						      .setBitrate(gear.getVideoBitRate()));
		return this;
	}
	
}
