package com.sumavision.bvc.BO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HangupParam {

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
	 * 会议/点对点通话标识
	 */
	private String groupUuid;

	@Override
	public String toString() {
		return "HangupParam [type=" + type + ", caller_name=" + caller_name + ", callee_bundle_id=" + callee_bundle_id
				+ ", callee_layer_id=" + callee_layer_id + ", groupUuid=" + groupUuid + "]";
	}

	
	
	
}
