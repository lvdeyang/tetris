package com.sumavision.bvc.device.group.bo;

import java.util.List;

import com.sumavision.bvc.command.group.basic.CommandGroupAvtplGearsPO;
import com.sumavision.bvc.command.group.basic.CommandGroupAvtplPO;
import com.sumavision.bvc.common.group.po.CommonAvtplGearsPO;
import com.sumavision.bvc.common.group.po.CommonAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.bvc.business.dispatch.po.DispatchAudioParamPO;
import com.sumavision.tetris.bvc.business.dispatch.po.DispatchVideoParamPO;

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
												.setBitrate(Integer.parseInt(gear.getAudioBitRate())))
		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
				   						      .setResolution(gear.getVideoResolution().getName())
				   						      .setBitrate(gear.getVideoBitRate())
				   						      .setFps(gear.getFps()==null?"25.0":gear.getFps()));
		return this;
	}
	public CodecParamBO set(AvtplPO avtpl, AvtplGearsPO gear){
		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName())
												.setBitrate(Integer.parseInt(gear.getAudioBitRate())))
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
	 * 协议参数数据转换，取LEVEL_3档位<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月21日 下午3:58:41
	 * @param avtpl
	 * @return
	 */
	public CodecParamBO set(DeviceGroupAvtplPO avtpl){
		List<DeviceGroupAvtplGearsPO> gears = avtpl.getGears();
		DeviceGroupAvtplGearsPO gear = null;
		for(DeviceGroupAvtplGearsPO _gear:gears){
			if(_gear.getLevel().equals(GearsLevel.LEVEL_3)){
				gear = _gear;
			}
		}
		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName())
												.setBitrate(Integer.parseInt(gear.getAudioBitRate())))
		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
				   						      .setResolution(gear.getVideoResolution().getName())
				   						      .setBitrate(gear.getVideoBitRate())
				   						      .setFps(gear.getFps()==null?"25.0":gear.getFps()));
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
	
	public CodecParamBO set(DispatchVideoParamPO videoParam, DispatchAudioParamPO auidoParam){
		this.setAudio_param(new AudioParamBO().set(auidoParam))
			.setVideo_param(new VideoParamBO().set(videoParam));
		return this;
	}
	
	public CodecParamBO copy(CodecParamBO codecParam){
		this.setAudio_param(new AudioParamBO().copy(codecParam.getAudio_param()))
			.setVideo_param(new VideoParamBO().copy(codecParam.getVideo_param()));
		return this;
	}

	/**
	 * 根据gears参数生成对象
	 * @param gearsPO
	 * @return
	 */
	public CodecParamBO set(DeviceGroupAvtplGearsPO gearsPO){
		this.setVideo_param(new VideoParamBO().set(gearsPO));
		this.setAudio_param(new AudioParamBO().set(gearsPO));
		return this;
	}
	
}
