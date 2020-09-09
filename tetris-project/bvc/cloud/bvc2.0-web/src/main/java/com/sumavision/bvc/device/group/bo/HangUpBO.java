package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.po.DeviceGroupPO;

/**
 * @ClassName: 挂断通知<br/> 
 * @author lvdeyang
 * @date 2018年8月15日 上午9:08:41 
 */
public class HangUpBO {

	/** 1会议，3多人童话 */
	private String type = "";
	
	/** xxx会议 */
	private String caller_name = "";
	
	private String callee_bundle_id = "";
	
	private String callee_layer_id = "";
	
	/** 业务唯一标识 */
	private String groupUuid = "";

	public String getType() {
		return type;
	}

	public HangUpBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getCaller_name() {
		return caller_name;
	}

	public HangUpBO setCaller_name(String caller_name) {
		this.caller_name = caller_name;
		return this;
	}

	public String getCallee_bundle_id() {
		return callee_bundle_id;
	}

	public HangUpBO setCallee_bundle_id(String callee_bundle_id) {
		this.callee_bundle_id = callee_bundle_id;
		return this;
	}

	public String getCallee_layer_id() {
		return callee_layer_id;
	}

	public HangUpBO setCallee_layer_id(String callee_layer_id) {
		this.callee_layer_id = callee_layer_id;
		return this;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public HangUpBO setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
		return this;
	}
	
	/**
	 * @Title: 多人通话hang_up
	 * @param group
	 * @return CallBO
	 */
	public HangUpBO setHangUp(DeviceGroupPO group){
		this.setType(group.getType().getProtocalId())
			.setCaller_name(group.getName())
			.setGroupUuid(group.getUuid());
		
		return this;
	}
	
}
