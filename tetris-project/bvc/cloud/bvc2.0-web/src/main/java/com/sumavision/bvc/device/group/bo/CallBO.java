package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;

/**
 * @ClassName: 协议层呼叫通知 <br/>
 * @author lvdeyang
 * @date 2018年8月15日 上午9:01:34 
 */
public class CallBO {

	/** 1会议，3多人通话 */
	private String type = "";
	
	/** type为多人通话时，表示通话类型：video/audio */
	private String mode;
	
	/** xxx会议 */
	private String caller_name = "";
	
	private String callee_bundle_id = "";
	
	private String callee_layer_id = "";
	
	/** 通知的唯一标识 */
	private String uuid = "";
	
	/** 业务唯一标识 */
	private String groupUuid = "";
	
	/** 是否可以拒绝 */
	private boolean deniable = false;

	public String getType() {
		return type;
	}

	public CallBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public CallBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getCaller_name() {
		return caller_name;
	}

	public CallBO setCaller_name(String caller_name) {
		this.caller_name = caller_name;
		return this;
	}

	public String getCallee_bundle_id() {
		return callee_bundle_id;
	}

	public CallBO setCallee_bundle_id(String callee_bundle_id) {
		this.callee_bundle_id = callee_bundle_id;
		return this;
	}

	public String getCallee_layer_id() {
		return callee_layer_id;
	}

	public CallBO setCallee_layer_id(String callee_layer_id) {
		this.callee_layer_id = callee_layer_id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public CallBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public CallBO setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
		return this;
	}

	public boolean isDeniable() {
		return deniable;
	}

	public CallBO setDeniable(boolean deniable) {
		this.deniable = deniable;
		return this;
	}
	
	/**
	 * @Title: 多人通话incoming_call 
	 * @param group
	 * @paramcallType
	 * @return CallBO
	 */
	public CallBO setIncomingCall(DeviceGroupPO group, String callType){
		this.setType(group.getType().getProtocalId())
			.setMode(callType)
			.setCaller_name(group.getName())
			.setDeniable(true)
			.setGroupUuid(group.getUuid());
		
		return this;
	}
}
