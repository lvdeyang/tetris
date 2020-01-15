package com.sumavision.bvc.BO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomingCallRespParam {

	/**
	 * 1会议，2点对点来电，后续可扩展
	 */
	private String type;
	
	/**
	 * 被呼叫者的bundle_id
	 */
	private String callee_bundle_id;
	
	/**
	 * 通知的唯一标识，用于反馈
	 */
	private String uuid;
	
	/**
	 * 会议/点对点通话标识
	 */
	private String groupUuid;
	

	/**
	 * 是否接受，true接受，false拒绝
	 */
	private String accept;


	@Override
	public String toString() {
		return "IncomingCallRespParam [type=" + type + ", callee_bundle_id=" + callee_bundle_id + ", uuid=" + uuid
				+ ", groupUuid=" + groupUuid + ", accept=" + accept + "]";
	}

	
	
}
