package com.sumavision.bvc.device.group.bo;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;

public class DisconnectBundleBO {
	
	public static final String BUSINESS_TYPE_MEETING = "meeting";
	
	public static final String BUSINESS_TYPE_VOD = "vod";
	
	public static final String OPERATE_TYPE = "stop";

	private String taskId = "";
	
	private String layerId = "";
	
	private String bundleId = "";
	
	/** 留着备用 */
	private String channelId = "";
	
	private String bundle_type = "";
	
	private String device_model;
	
	private String businessType = "meeting";
	
	private String operateType = "";
	
	/** 参数模板 */
	private CodecParamBO codec_param = new CodecParamBO();
	
	private PassByBO pass_by_str;

	public String getTaskId() {
		return taskId;
	}

	public DisconnectBundleBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DisconnectBundleBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DisconnectBundleBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DisconnectBundleBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public DisconnectBundleBO setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
		return this;
	}

	public String getDevice_model() {
		return device_model;
	}

	public DisconnectBundleBO setDevice_model(String device_model) {
		this.device_model = device_model;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public DisconnectBundleBO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getOperateType() {
		return operateType;
	}

	public DisconnectBundleBO setOperateType(String operateType) {
		this.operateType = operateType;
		return this;
	}

	public CodecParamBO getCodec_param() {
		return codec_param;
	}

	public DisconnectBundleBO setCodec_param(CodecParamBO codec_param) {
		this.codec_param = codec_param;
		return this;
	}

	public PassByBO getPass_by_str() {
		return pass_by_str;
	}

	public DisconnectBundleBO setPass_by_str(PassByBO pass_by_str) {
		this.pass_by_str = pass_by_str;
		return this;
	}
	
	/**
	 * @Title: 生成设备挂断协议
	 * @param member 成员设备
	 * @param codec 参数模板
	 * @return DisconnectBO  
	 */
	public DisconnectBundleBO setDisconnectBundle(DeviceGroupPO group, DeviceGroupMemberPO member, CodecParamBO codec){
		this.setLayerId(member.getLayerId())
			.setBundleId(member.getBundleId())
			.setBundle_type(member.getVenusBundleType())
			.setCodec_param(codec);
		
		PassByBO passBy = new PassByBO().setHangUp(group, member);
		this.setPass_by_str(passBy);
		
		return this;
	}
	
	/**
	 * 设备挂断协议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 下午3:44:48
	 * @param bundle
	 * @param codec
	 * @return
	 */
	public DisconnectBundleBO setDisconnectBundle(BundlePO bundle, CodecParamBO codec){
		this.setLayerId(bundle.getAccessNodeUid())
			.setBundleId(bundle.getBundleId())
			.setBundle_type(bundle.getBundleType())
			.setCodec_param(codec);
		
		return this;
	}
}
