package com.sumavision.bvc.BO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomingCallParam {

	/**
	 * 1会议，2点对点来电，后续可扩展
	 */
	private String type;
	
	/**
	 * 终端用来显示来电名称
	 */
	private String caller_name;
	
	/**
	 * 被呼叫者的bundle_id
	 */
	private String callee_bundle_id;
	
	/**
	 * 被呼叫者的接入层ID
	 */
	private String callee_layer_id;
	
	/**
	 * 通知的唯一标识，用于反馈
	 */
	private String uuid;
	
	/**
	 * 会议/点对点通话标识
	 */
	private String groupUuid;
	
	/**
	 * 是否可以拒绝，true可以，false不能
	 */
	private String deniable;

	@Override
	public String toString() {
		return "IncomingCallParam [type=" + type + ", caller_name=" + caller_name + ", callee_bundle_id="
				+ callee_bundle_id + ", callee_layer_id=" + callee_layer_id + ", uuid=" + uuid + ", groupUuid="
				+ groupUuid + ", deniable=" + deniable + "]";
	}
	
	
}
