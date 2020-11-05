package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.command.group.enumeration.MediaType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.common.group.po.CommonChannelForwardPO;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;

/**
 * @ClassName: 删除转发协议<br/> 
 * @author lvdeyang
 * @date 2018年8月7日 上午10:46:42 
 */
public class ForwardDelBO {
	
	private String taskId = "";
	
	private ForwardDelDstBO dst = new ForwardDelDstBO();
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public ForwardDelDstBO getDst() {
		return dst;
	}

	public void setDst(ForwardDelDstBO dst) {
		this.dst = dst;
	}
	
	/**
	 * @Title: 生成协议层删除转发数据 <br/>
	 * @param forward 业务转发数据
	 * @param codec 参数模板
	 * @return ForwardDelBO 
	 */
	public ForwardDelBO set(ChannelForwardPO forward, CodecParamBO codec){
		ForwardDelDstBO dst = new ForwardDelDstBO();
		if(ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType()) ||
				ForwardSourceType.FORWARDAUDIO.equals(forward.getForwardSourceType())){
			dst.setBase_type("VenusAudioOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType());
		}else if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType()) ||
				ForwardSourceType.FORWARVIDEO.equals(forward.getForwardSourceType())){
			dst.setBase_type("VenusVideoOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType());
		}
		this.setDst(dst);
		return this;
	}
	public ForwardDelBO set(CommandGroupForwardPO forward, CodecParamBO codec, MediaType mediaType){
		ForwardDelDstBO dst = new ForwardDelDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//删除视频转发
			dst.setBase_type("VenusVideoOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getDstVideoLayerId())
			   .setBundleId(forward.getDstVideoBundleId())
			   .setChannelId(forward.getDstVideoChannelId())
			   .setBundle_type(forward.getDstVideoBundleType());
		}else if(MediaType.AUDIO.equals(mediaType)){
			//删除音频转发
			dst.setBase_type("VenusAudioOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getDstAudioLayerId())
			   .setBundleId(forward.getDstAudioBundleId())
			   .setChannelId(forward.getDstAudioChannelId())
			   .setBundle_type(forward.getDstAudioBundleType());
		}
		this.setDst(dst);
		return this;
	}
	public ForwardDelBO setByDstCastDevice(CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardDelDstBO dst = new ForwardDelDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//删除视频转发
			dst.setBase_type("VenusVideoOut")
			   .setCodec_param(codec)
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstBundleType());
		}else if(MediaType.AUDIO.equals(mediaType)){
			//删除音频转发
			dst.setBase_type("VenusAudioOut")
			   .setCodec_param(codec)
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstBundleType());
		}
		this.setDst(dst);
		return this;
	}
	public ForwardDelBO set(CommandGroupForwardDemandPO forward, CodecParamBO codec, MediaType mediaType){
		ForwardDelDstBO dst = new ForwardDelDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//删除视频转发
			dst.setBase_type("VenusVideoOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getDstVideoLayerId())
			   .setBundleId(forward.getDstVideoBundleId())
			   .setChannelId(forward.getDstVideoChannelId())
			   .setBundle_type(forward.getDstVideoBundleType());
		}else if(MediaType.AUDIO.equals(mediaType)){
			//删除音频转发
			dst.setBase_type("VenusAudioOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getDstAudioLayerId())
			   .setBundleId(forward.getDstAudioBundleId())
			   .setChannelId(forward.getDstAudioChannelId())
			   .setBundle_type(forward.getDstAudioBundleType());
		}
		this.setDst(dst);
		return this;
	}
	public ForwardDelBO set(CommonChannelForwardPO forward, CodecParamBO codec){
		ForwardDelDstBO dst = new ForwardDelDstBO();
		if(ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType()) ||
				ForwardSourceType.FORWARDAUDIO.equals(forward.getForwardSourceType())){
			dst.setBase_type("VenusAudioOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType());
		}else if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType()) ||
				ForwardSourceType.FORWARVIDEO.equals(forward.getForwardSourceType())){
			dst.setBase_type("VenusVideoOut")
			   .setCodec_param(codec)
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType());
		}
		this.setDst(dst);
		return this;
	}
	
}
