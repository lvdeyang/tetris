package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.group.enumeration.MediaType;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardDemandPO;
import com.sumavision.bvc.command.group.forward.CommandGroupForwardPO;
import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.common.group.po.CommonChannelForwardPO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ForwardSourceType;
import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

/**
 * @Title: 协议层转发数据 
 * @author lvdeyang
 * @date 2018年8月7日 上午10:37:25 
 */
public class ForwardSetBO {
	
	/** 任务id，这里暂时空着 */
	private String taskId = "";

	/** 转发源 */
	private ForwardSetSrcBO src = new ForwardSetSrcBO();
	
	/** 转发目的 */
	private ForwardSetDstBO dst = new ForwardSetDstBO();
	
	/** 描述屏幕布局 */
	private List<ScreenBO> screens;

	public String getTaskId() {
		return taskId;
	}

	public ForwardSetBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public ForwardSetSrcBO getSrc() {
		return src;
	}

	public ForwardSetBO setSrc(ForwardSetSrcBO src) {
		this.src = src;
		return this;
	}

	public ForwardSetDstBO getDst() {
		return dst;
	}

	public ForwardSetBO setDst(ForwardSetDstBO dst) {
		this.dst = dst;
		return this;
	}
	
	public List<ScreenBO> getScreens() {
		return screens;
	}

	public ForwardSetBO setScreens(List<ScreenBO> screens) {
		this.screens = screens;
		return this;
	}

	/**
	 * @Title: 生成协议层转发数据 <br/>
	 * @param forward 业务层转发数据
	 * @return ForwardSetBO 协议层转发数据
	 */
	public ForwardSetBO set(ChannelForwardPO forward, CodecParamBO codec){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(ForwardSourceType.FORWARVIDEO.equals(forward.getForwardSourceType())){
			//视频转发
			src.setType("channel")
			   .setLayerId(forward.getSourceLayerId())
			   .setBundleId(forward.getSourceBundleId())
			   .setChannelId(forward.getSourceChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}else if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType())){
			//合屏转发
			src.setType("combineVideo")
			   .setUuid(forward.getCombineUuid());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}else if(ForwardSourceType.FORWARDAUDIO.equals(forward.getForwardSourceType())){
			//音频转发
			src.setType("channel")
			   .setLayerId(forward.getSourceLayerId())
			   .setBundleId(forward.getSourceBundleId())
			   .setChannelId(forward.getSourceChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}else if(ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType())){
			//混音转发
			src.setType("combineAudio")
			   .setUuid(forward.getCombineUuid());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(forward.getSourceChannelId()!=null || forward.getCombineUuid()!=null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		if(this.getScreens() == null) this.setScreens(new ArrayList<ScreenBO>());
		
		//设置屏幕布局
		if(!ForwardSourceType.FORWARDAUDIO.equals(forward.getForwardSourceType()) &&
				!ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType())){
			
			if(forward.getLayout()==null || ScreenLayout.SINGLE.equals(forward.getLayout())){
				//单屏远端1
				if(ChannelType.VIDEODECODE1.equals(forward.getChannelType())){
					//生成单画面
					ScreenBO screen = ScreenBO.SINGLE(forward.getRectId());
					screen.setId(forward.getScreenId());
					//处理屏幕覆盖
					if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType()) && 
							forward.getOverlapX()!=null && forward.getOverlapRectId() != null){
						screen.setOverlaps(new ArrayListWrapper<RectBO>().add(new RectBO().setChannel_id(forward.getOverlapChannelId())
																						  .setRect_id(forward.getOverlapRectId())
																						  .setZ_index(2)
																						  .setX(transTenThousandProportion(forward.getOverlapX()))
																						  .setY(transTenThousandProportion(forward.getOverlapY()))
																						  .setWidth(transTenThousandProportion(forward.getOverlapW()))
																						  .setHeight(transTenThousandProportion(forward.getOverlapH()))
																						  .setType("overlap"))
																		 .getList());
					}
					this.getScreens().add(screen);
				}
			}else if(ScreenLayout.REMOTE_LARGE.equals(forward.getLayout())){
				//大屏远端1，小屏本地
				if(ChannelType.VIDEODECODE1.equals(forward.getChannelType())){
					ScreenBO screen = ScreenBO.REMOTE_LARGE(forward.getOverlapChannelId(), forward.getRectId(), forward.getOverlapRectId());
					screen.setId(forward.getScreenId());
					this.getScreens().add(screen);
				}
			}else if(ScreenLayout.REMOTE_SMARLL.equals(forward.getLayout())){
				//大屏本地，小屏远端1
				if(ChannelType.VIDEODECODE1.equals(forward.getChannelType())){
					ScreenBO screen = ScreenBO.REMOTE_SMALL(forward.getOverlapChannelId(), forward.getRectId(), forward.getOverlapRectId());
					screen.setId(forward.getScreenId());
					this.getScreens().add(screen);
				}
			}else if(ScreenLayout.SMALL.equals(forward.getLayout())){
				//ppt模式，大屏远端2，小屏远端1
				ScreenBO screen = ScreenBO.PPT_REMOTE1(forward.getRectId());
				screen.setId(forward.getScreenId());
				this.getScreens().add(screen);
			}else if(ScreenLayout.PPT_MODE.equals(forward.getLayout())){
				//ppt模式，大屏远端2，小屏远端1
				ScreenBO screen = ScreenBO.PPT_REMOTE2(forward.getRectId());
				screen.setId(forward.getScreenId());
				this.getScreens().add(screen);
			}
		}
		
		return this;
	}
	public ForwardSetBO set(CommandGroupForwardPO forward, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发
			src.setType("channel")
			   .setLayerId(forward.getVideoLayerId())
			   .setBundleId(forward.getVideoBundleId())
			   .setChannelId(forward.getVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(forward.getDstVideoLayerId())
			   .setBundleId(forward.getDstVideoBundleId())
			   .setChannelId(forward.getDstVideoChannelId())
			   .setBundle_type(forward.getDstVideoBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发
			src.setType("channel")
			   .setLayerId(forward.getAudioLayerId())
			   .setBundleId(forward.getAudioBundleId())
			   .setChannelId(forward.getAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(forward.getDstAudioLayerId())
			   .setBundleId(forward.getDstAudioBundleId())
			   .setChannelId(forward.getDstAudioChannelId())
			   .setBundle_type(forward.getDstVideoBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	public ForwardSetBO setBySrcForwardAndDstCastDevice(CommandGroupForwardPO forward, CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发
			src.setType("channel")
			   .setLayerId(forward.getVideoLayerId())
			   .setBundleId(forward.getVideoBundleId())
			   .setChannelId(forward.getVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发
			src.setType("channel")
			   .setLayerId(forward.getAudioLayerId())
			   .setBundleId(forward.getAudioBundleId())
			   .setChannelId(forward.getAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	public ForwardSetBO set(CommandGroupForwardDemandPO demand, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发
			src.setType("channel")
			   .setLayerId(demand.getVideoLayerId())
			   .setBundleId(demand.getVideoBundleId())
			   .setChannelId(demand.getVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(demand.getDstVideoLayerId())
			   .setBundleId(demand.getDstVideoBundleId())
			   .setChannelId(demand.getDstVideoChannelId())
			   .setBundle_type(demand.getDstVideoBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发
			src.setType("channel")
			   .setLayerId(demand.getAudioLayerId())
			   .setBundleId(demand.getAudioBundleId())
			   .setChannelId(demand.getAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(demand.getDstAudioLayerId())
			   .setBundleId(demand.getDstAudioBundleId())
			   .setChannelId(demand.getDstAudioChannelId())
			   .setBundle_type(demand.getDstVideoBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	public ForwardSetBO setBySrcDemandAndDstCastDevice(CommandGroupForwardDemandPO demand, CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发
			src.setType("channel")
			   .setLayerId(demand.getVideoLayerId())
			   .setBundleId(demand.getVideoBundleId())
			   .setChannelId(demand.getVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发
			src.setType("channel")
			   .setLayerId(demand.getAudioLayerId())
			   .setBundleId(demand.getAudioBundleId())
			   .setChannelId(demand.getAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	public ForwardSetBO setByMediapushAndDstCastDevice(String mediaPushUuid, CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发
			src.setType("mediaPush")
			   .setUuid(mediaPushUuid);
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发
			src.setType("mediaPush")
			   .setUuid(mediaPushUuid);
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		this.setSrc(src);
		this.setDst(dst);
		
		return this;
	}

	public ForwardSetBO setBySrcVodAndDstCastDevice(CommandVodPO vod, CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发
			src.setType("channel")
			   .setLayerId(vod.getSourceLayerId())
			   .setBundleId(vod.getSourceBundleId())
			   .setChannelId(vod.getSourceVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发
			src.setType("channel")
			   .setLayerId(vod.getSourceLayerId())
			   .setBundleId(vod.getSourceBundleId())
			   .setChannelId(vod.getSourceAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	
	/** 用于生成发起呼叫人的上屏转发，看被叫方的源 */
	public ForwardSetBO setBySrcCallAndDstCastDevice(UserLiveCallPO call, CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发，以被呼叫人做源
			src.setType("channel")
			   .setLayerId(call.getCalledEncoderLayerId())
			   .setBundleId(call.getCalledEncoderBundleId())
			   .setChannelId(call.getCalledEncoderVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发，以被呼叫人做源
			src.setType("channel")
			   .setLayerId(call.getCalledEncoderLayerId())
			   .setBundleId(call.getCalledEncoderBundleId())
			   .setChannelId(call.getCalledEncoderAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	
	/** 用于生成被呼叫人的上屏转发，看呼叫方的源 */
	public ForwardSetBO setBySrcCalledAndDstCastDevice(UserLiveCallPO call, CommandGroupUserPlayerCastDevicePO castDevice, CodecParamBO codec, MediaType mediaType){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(MediaType.VIDEO.equals(mediaType)){
			//视频转发，以呼叫人做源
			src.setType("channel")
			   .setLayerId(call.getCallEncoderLayerId())
			   .setBundleId(call.getCallEncoderBundleId())
			   .setChannelId(call.getCallEncoderVideoChannelId());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstVideoChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}else if(MediaType.AUDIO.equals(mediaType)){
			//音频转发，以呼叫人做源
			src.setType("channel")
			   .setLayerId(call.getCallEncoderLayerId())
			   .setBundleId(call.getCallEncoderBundleId())
			   .setChannelId(call.getCallEncoderAudioChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(castDevice.getDstLayerId())
			   .setBundleId(castDevice.getDstBundleId())
			   .setChannelId(castDevice.getDstAudioChannelId())
			   .setBundle_type(castDevice.getDstVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(src.getChannelId() != null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		return this;
	}
	
	public ForwardSetBO set(CommonChannelForwardPO forward, CodecParamBO codec){
		ForwardSetSrcBO src = new ForwardSetSrcBO();
		ForwardSetDstBO dst = new ForwardSetDstBO();
		if(ForwardSourceType.FORWARVIDEO.equals(forward.getForwardSourceType())){
			//视频转发
			src.setType("channel")
			   .setLayerId(forward.getSourceLayerId())
			   .setBundleId(forward.getSourceBundleId())
			   .setChannelId(forward.getSourceChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}else if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType())){
			//合屏转发
			src.setType("combineVideo")
			   .setUuid(forward.getCombineUuid());
			dst.setBase_type("VenusVideoOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}else if(ForwardSourceType.FORWARDAUDIO.equals(forward.getForwardSourceType())){
			//音频转发
			src.setType("channel")
			   .setLayerId(forward.getSourceLayerId())
			   .setBundleId(forward.getSourceBundleId())
			   .setChannelId(forward.getSourceChannelId());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}else if(ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType())){
			//混音转发
			src.setType("combineAudio")
			   .setUuid(forward.getCombineUuid());
			dst.setBase_type("VenusAudioOut")
			   .setLayerId(forward.getLayerId())
			   .setBundleId(forward.getBundleId())
			   .setChannelId(forward.getChannelId())
			   .setBundle_type(forward.getVenusBundleType())
			   .setCodec_param(codec);
		}
		
		//空源src要发null
		if(forward.getSourceChannelId()!=null || forward.getCombineUuid()!=null){
			this.setSrc(src);
		}else{
			this.setSrc(null);
		} 
		this.setDst(dst);
		
		if(this.getScreens() == null) this.setScreens(new ArrayList<ScreenBO>());
		
		//设置屏幕布局
		if(!ForwardSourceType.FORWARDAUDIO.equals(forward.getForwardSourceType()) &&
				!ForwardSourceType.COMBINEAUDIO.equals(forward.getForwardSourceType())){
			
			if(forward.getLayout()==null || ScreenLayout.SINGLE.equals(forward.getLayout())){
				//单屏远端1
				if(ChannelType.VIDEODECODE1.equals(forward.getChannelType())){
					//生成单画面
					ScreenBO screen = ScreenBO.SINGLE(forward.getRectId());
					screen.setId(forward.getScreenId());
					//处理屏幕覆盖
					if(ForwardSourceType.COMBINEVIDEO.equals(forward.getForwardSourceType()) && 
							forward.getOverlapX()!=null && forward.getOverlapRectId() != null){
						screen.setOverlaps(new ArrayListWrapper<RectBO>().add(new RectBO().setChannel_id(forward.getOverlapChannelId())
																						  .setRect_id(forward.getOverlapRectId())
																						  .setZ_index(2)
																						  .setX(transTenThousandProportion(forward.getOverlapX()))
																						  .setY(transTenThousandProportion(forward.getOverlapY()))
																						  .setWidth(transTenThousandProportion(forward.getOverlapW()))
																						  .setHeight(transTenThousandProportion(forward.getOverlapH()))
																						  .setType("overlap"))
																		 .getList());
					}
					this.getScreens().add(screen);
				}
			}else if(ScreenLayout.REMOTE_LARGE.equals(forward.getLayout())){
				//大屏远端1，小屏本地
				if(ChannelType.VIDEODECODE1.equals(forward.getChannelType())){
					ScreenBO screen = ScreenBO.REMOTE_LARGE(forward.getOverlapChannelId(), forward.getRectId(), forward.getOverlapRectId());
					screen.setId(forward.getScreenId());
					this.getScreens().add(screen);
				}
			}else if(ScreenLayout.REMOTE_SMARLL.equals(forward.getLayout())){
				//大屏本地，小屏远端1
				if(ChannelType.VIDEODECODE1.equals(forward.getChannelType())){
					ScreenBO screen = ScreenBO.REMOTE_SMALL(forward.getOverlapChannelId(), forward.getRectId(), forward.getOverlapRectId());
					screen.setId(forward.getScreenId());
					this.getScreens().add(screen);
				}
			}else if(ScreenLayout.SMALL.equals(forward.getLayout())){
				//ppt模式，大屏远端2，小屏远端1
				ScreenBO screen = ScreenBO.PPT_REMOTE1(forward.getRectId());
				screen.setId(forward.getScreenId());
				this.getScreens().add(screen);
			}else if(ScreenLayout.PPT_MODE.equals(forward.getLayout())){
				//ppt模式，大屏远端2，小屏远端1
				ScreenBO screen = ScreenBO.PPT_REMOTE2(forward.getRectId());
				screen.setId(forward.getScreenId());
				this.getScreens().add(screen);
			}
		}
		
		return this;
	}
	
	/**
	 * @Title: 将分数转换成万分比<br/> 
	 * @param score 分数
	 * @return int 万分比分子
	 */
	private int transTenThousandProportion(String score){
		String[] structure = score.split("/");
		return Integer.parseInt(structure[0]) * 10000 / Integer.parseInt(structure[1]);
	}
	
}
