package com.sumavision.bvc.device.group.bo;

/**
* @ClassName: forwardSetDSTBO 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author zy
* @date 2018年8月7日 上午10:37:43 
*
 */
public class ForwardSetDstBO {
	
	/**
	 * 接入层Id
	 */
	private String layerId = "";
	
	/**
	 * 设备标识		
	 */
	private String bundleId = "";
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId = "";
	
	/** VenusVideoIn VenusVideoOut VenusAudioIn VenusAudioOut */
	private String base_type = "";
	
	/** 所属终端类型 */
	private String bundle_type;
	
	/** 参数模板 */
	private CodecParamBO codec_param = new CodecParamBO();
	
	/** position用于设置这个通道的显示位置 */
	private positionDstBO position = new positionDstBO();

	public String getLayerId() {
		return layerId;
	}

	public ForwardSetDstBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ForwardSetDstBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public ForwardSetDstBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getBase_type() {
		return base_type;
	}

	public ForwardSetDstBO setBase_type(String base_type) {
		this.base_type = base_type;
		return this;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public ForwardSetDstBO setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public ForwardSetDstBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

	public positionDstBO getPosition() {
		return position;
	}

	public ForwardSetDstBO setPosition(positionDstBO position) {
		this.position = position;
		return this;
	}

}
