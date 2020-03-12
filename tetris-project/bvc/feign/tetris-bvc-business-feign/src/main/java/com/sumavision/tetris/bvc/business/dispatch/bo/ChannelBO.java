package com.sumavision.tetris.bvc.business.dispatch.bo;

/**
 * @ClassName: ChannelBO 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zsy
 * @date 2020年3月11日 上午11:14:03 
 */
public class ChannelBO {
	
	/** 设备能力通道ID。只在设备调度中有效，用户调度中无效 */
	private String channelId = "";

	private SourceParamBO source_param;
	
	private String codecParamType = "AUTO";
	
	/** 编解码参数 */
	private CodecParamBO codec_param = new CodecParamBO();

	public String getChannelId() {
		return channelId;
	}

	public ChannelBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public ChannelBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}
	
	public SourceParamBO getSource_param() {
		return source_param;
	}

	public ChannelBO setSource_param(SourceParamBO source_param) {
		this.source_param = source_param;
		return this;
	}

	public String getCodecParamType() {
		return codecParamType;
	}

	public ChannelBO setCodecParamType(String codecParamType) {
		this.codecParamType = codecParamType;
		return this;
	}
	
}
