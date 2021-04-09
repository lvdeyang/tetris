package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sumavision.bvc.common.group.po.CommonCombineAudioPO;
import com.sumavision.bvc.common.group.po.CommonCombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;

/**
 * @Title: 协议层混音数据 <br/>
 * @author lvdeyang
 * @date 2018年8月7日 上午10:56:04 
 */
public class CombineAudioBO {

	private String taskId = "";
	
	private String lock_type = "write";
	
	private CodecParamBO codec_param = new CodecParamBO();
	
	private String uuid = "";
	
	private List<SourceBO> src = new ArrayList<SourceBO>();
	
	public String getTaskId() {
		return taskId;
	}
	
	public CombineAudioBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}
	
	public String getLock_type() {
		return lock_type;
	}
	
	public CombineAudioBO setLock_type(String lock_type) {
		this.lock_type = lock_type;
		return this;
	}
	
	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public CombineAudioBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

	public String getUuid() {
		return uuid;
	}
	
	public CombineAudioBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
	
	public List<SourceBO> getSrc() {
		return src;
	}

	public CombineAudioBO setSrc(List<SourceBO> src) {
		this.src = src;
		return this;
	}

	/**
	 * @Title: 设置协议层混音数据<br/> 
	 * @param combineAudio 业务混音数据
	 * @param codec 参数模板
	 * @return CombineAudioBO
	 */
	public CombineAudioBO set(CombineAudioPO combineAudio, CodecParamBO codec){
		this.setCodec_param(codec)
		    .setUuid(combineAudio.getUuid())
		    .setSrc(new ArrayList<SourceBO>());
		Set<CombineAudioSrcPO> audioSrcs = combineAudio.getSrcs();
		if(audioSrcs != null){
			for(CombineAudioSrcPO audioSrc:audioSrcs){
				SourceBO protocalSource = new SourceBO().setLayerId(audioSrc.getLayerId())
														.setBundleId(audioSrc.getBundleId())
														.setChannelId(audioSrc.getChannelId());
				this.getSrc().add(protocalSource);
			} 

		}
		return this;
	}
	public CombineAudioBO set(BusinessCombineAudioPO combineAudio, CodecParamBO codec){
		this.setCodec_param(codec)
		    .setUuid(combineAudio.getUuid())
		    .setSrc(new ArrayList<SourceBO>());
		List<BusinessCombineAudioSrcPO> audioSrcs = combineAudio.getSrcs();
		if(audioSrcs != null){
			for(BusinessCombineAudioSrcPO audioSrc:audioSrcs){
				if(!audioSrc.isHasSource()) continue;
				SourceBO protocalSource = new SourceBO().setLayerId(audioSrc.getLayerId())
														.setBundleId(audioSrc.getBundleId())
														.setChannelId(audioSrc.getChannelId());
				this.getSrc().add(protocalSource);
			} 

		}
		return this;
	}
	public CombineAudioBO set(CommonCombineAudioPO combineAudio, CodecParamBO codec){
		this.setCodec_param(codec)
		    .setUuid(combineAudio.getUuid())
		    .setSrc(new ArrayList<SourceBO>());
		Set<CommonCombineAudioSrcPO> audioSrcs = combineAudio.getSrcs();
		if(audioSrcs != null){
			for(CommonCombineAudioSrcPO audioSrc:audioSrcs){
				SourceBO protocalSource = new SourceBO().setLayerId(audioSrc.getLayerId())
														.setBundleId(audioSrc.getBundleId())
														.setChannelId(audioSrc.getChannelId());
				this.getSrc().add(protocalSource);
			} 

		}
		return this;
	}
	
}
