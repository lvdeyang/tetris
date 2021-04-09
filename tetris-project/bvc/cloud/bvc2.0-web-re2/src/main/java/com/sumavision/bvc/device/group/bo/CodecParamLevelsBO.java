package com.sumavision.bvc.device.group.bo;

import java.util.Map;

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

public class CodecParamLevelsBO {
	
	private Map<GearsLevel, CodecParamBO> codecParamMap;

	public Map<GearsLevel, CodecParamBO> getCodecParamMap() {
		return codecParamMap;
	}

	public CodecParamLevelsBO setCodecParamMap(Map<GearsLevel, CodecParamBO> codecParamMap) {
		this.codecParamMap = codecParamMap;
		return this;
	}
	
//	/**
//	 * @Title: 协议参数数据转换 <br/>
//	 * @param avtpl 会议参数模板
//	 * @param gear 当前会议参数档位
//	 * @return CodecParamBO 协议参数
//	 */
//	public CodecParamLevelsBO set(DeviceGroupAvtplPO avtpl, DeviceGroupAvtplGearsPO gear){
//		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName())
//												.setBitrate(Integer.parseInt(gear.getAudioBitRate())))
//		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
//				   						      .setResolution(gear.getVideoResolution().getName())
//				   						      .setBitrate(gear.getVideoBitRate())
//				   						      .setFps(gear.getFps()==null?"25.0":gear.getFps()));
//		return this;
//	}
//	public CodecParamLevelsBO set(AvtplPO avtpl, AvtplGearsPO gear){
//		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName())
//												.setBitrate(Integer.parseInt(gear.getAudioBitRate())))
//		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
//				   						      .setResolution(gear.getVideoResolution().getName())
//				   						      .setBitrate(gear.getVideoBitRate())
//				   						      .setFps(gear.getFps()==null?"25.0":gear.getFps()));
//		return this;
//	}
//	public CodecParamLevelsBO set(CommandGroupAvtplPO avtpl, CommandGroupAvtplGearsPO gear){
//		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName()))
//		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
//				   						      .setResolution(gear.getVideoResolution().getName())
//				   						      .setBitrate(gear.getVideoBitRate()));
//		return this;
//	}
//	
//	/**
//	 * @Title: 协议参数数据转换 <br/>
//	 * @param avtpl 通用会议参数模板
//	 * @param gear 当前会议参数档位
//	 * @return CodecParamBO 协议参数
//	 */
//	public CodecParamLevelsBO set(CommonAvtplPO avtpl, CommonAvtplGearsPO gear){
//		this.setAudio_param(new AudioParamBO().setCodec(avtpl.getAudioFormat().getName()))
//		    .setVideo_param(new VideoParamBO().setCodec(avtpl.getVideoFormat().getName())
//				   						      .setResolution(gear.getVideoResolution().getName())
//				   						      .setBitrate(gear.getVideoBitRate()));
//		return this;
//	}
//	
//	public CodecParamLevelsBO set(DispatchVideoParamPO videoParam, DispatchAudioParamPO auidoParam){
//		this.setAudio_param(new AudioParamBO().set(auidoParam))
//			.setVideo_param(new VideoParamBO().set(videoParam));
//		return this;
//	}
//	
//	public CodecParamLevelsBO copy(CodecParamLevelsBO codecParam){
//		this.setAudio_param(new AudioParamBO().copy(codecParam.getAudio_param()))
//			.setVideo_param(new VideoParamBO().copy(codecParam.getVideo_param()));
//		return this;
//	}
	
}
