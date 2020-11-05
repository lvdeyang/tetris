package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;

/**
 * @ClassName: 协议层挂断<br/> 
 * @author lvdeyang
 * @date 2018年8月7日 上午11:18:31 
 */
public class DisconnectBO {
	
	private String taskId = "";
	
	private String layerId = "";
	
	private String bundleId = "";
	
	private String channelId = "";
	
	//VenusVideoIn VenusVideoOut VenusAudioIn VenusAudioOut
	private String base_type = "";
	
	/** 参数模板 */
	private CodecParamBO codec_param = new CodecParamBO();

	public String getTaskId() {
		return taskId;
	}

	public DisconnectBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DisconnectBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DisconnectBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DisconnectBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getBase_type() {
		return base_type;
	}

	public DisconnectBO setBase_type(String base_type) {
		this.base_type = base_type;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public DisconnectBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}
	
	/**
	 * @Title: 生成通道挂断协议<br/> 
	 * @param channel 通道
	 * @param codec 参数模板
	 * @return DisconnectBO 
	 */
	public DisconnectBO set(DeviceGroupMemberChannelPO channel, CodecParamBO codec){
		this.setLayerId(channel.getMember().getLayerId())
		  	.setBase_type(channel.getChannelType())
		  	.setBundleId(channel.getBundleId())
		  	.setChannelId(channel.getChannelId())
		  	.setBase_type(channel.getVenusBundleType())
		  	.setCodec_param(codec);
		return this;
	}
	
}
