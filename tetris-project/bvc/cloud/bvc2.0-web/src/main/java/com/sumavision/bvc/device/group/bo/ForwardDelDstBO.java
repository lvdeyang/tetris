package com.sumavision.bvc.device.group.bo;

/**
 * 
 * @ClassName: 协议层删除转发目的 
 * @author lvdeyang
 * @date 2018年8月7日 上午11:14:38 
 */
public class ForwardDelDstBO {
	
	private String layerId = "";
	
	private String bundleId = "";
	
	private String channelId = "";
	
	/** VenusVideoIn VenusVideoOut VenusAudioIn VenusAudioOut */
	private String base_type = "";
	
    /** 终端类型 */
	private String bundle_type;
	
	/** 参数模板 */
	private CodecParamBO codec_param = new CodecParamBO();

	public String getLayerId() {
		return layerId;
	}

	public ForwardDelDstBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ForwardDelDstBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ForwardDelDstBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getBase_type() {
		return base_type;
	}

	public ForwardDelDstBO setBase_type(String base_type) {
		this.base_type = base_type;
		return this;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public ForwardDelDstBO setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public ForwardDelDstBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

}
