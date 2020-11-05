package com.sumavision.bvc.device.group.bo;

import java.util.Collection;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.tetris.bvc.business.dispatch.po.TetrisDispatchPO;
import com.sumavision.tetris.bvc.business.group.GroupPO;

/**
 * @ClassName: 透传协议 
 * @author wjw 
 * @date 2018年10月11日 上午8:55:15
 */
public class PassByBO {
	
	private String layer_id = "";
	
	private String bundle_id = "";
	
	/** 透传类型 */
	private String type = ""; 
	
	/** 保留，以后可能用 */
	private String channel_id = "";
	
	private Object pass_by_content;
	
	/** 设备号码 */
	private String username;
	
	/** 对方设备号码 */
	private String targetUsername;

	public String getLayer_id() {
		return layer_id;
	}

	public PassByBO setLayer_id(String layer_id) {
		this.layer_id = layer_id;
		return this;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public PassByBO setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
		return this;
	}

	public String getType() {
		return type;
	}

	public PassByBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public PassByBO setChannel_id(String channel_id) {
		this.channel_id = channel_id;
		return this;
	}

	public Object getPass_by_content() {
		return pass_by_content;
	}

	public PassByBO setPass_by_content(Object pass_by_content) {
		this.pass_by_content = pass_by_content;
		return this;
	}
	
	public String getUsername() {
		return username;
	}

	public PassByBO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getTargetUsername() {
		return targetUsername;
	}

	public PassByBO setTargetUsername(String targetUsername) {
		this.targetUsername = targetUsername;
		return this;
	}

	/**
	 * @Title: 透传IncomingCall
	 * @param group
	 * @param member
	 * @return PassByBO
	 */
	public PassByBO setIncomingCall(DeviceGroupPO group, DeviceGroupMemberPO member){
		
		PassByContentBO passByContent = new PassByContentBO().setIncomingCall(group, member);		
		this.setBundle_id(member.getBundleId())
			.setLayer_id(member.getLayerId())
			.setType("incoming_call_request")
			.setPass_by_content(passByContent);
		
		return this;
	}
	
	
	//614
	public PassByBO setIncomingCall(GroupPO group, String bundle_id, String layer_id){
		
		PassByContentBO passByContent = new PassByContentBO().setIncomingCall(group);		
		this.setBundle_id(bundle_id)
			.setLayer_id(layer_id)
			.setType("incoming_call_request")
			.setPass_by_content(passByContent);
		
		return this;
	}
	/**
	 * @Title: 透传HangUp
	 * @param group
	 * @param member
	 * @return PassByBO
	 */
	public PassByBO setHangUp(DeviceGroupPO group, DeviceGroupMemberPO member){
		
		PassByContentBO passByContent = new PassByContentBO().setHangUp(group, member);		
		this.setBundle_id(member.getBundleId())
			.setLayer_id(member.getLayerId())
			.setType("hang_up_request")
			.setPass_by_content(passByContent);
		
		return this;
	}

//614
	public PassByBO setHangUp(GroupPO group,String bundle_id,String layer_id){
			
			PassByContentBO passByContent = new PassByContentBO().setHangUp(group);		
			this.setBundle_id(bundle_id)
				.setLayer_id(layer_id)
				.setType("hang_up_request")
				.setPass_by_content(passByContent);
			
			return this;
		}
	
	/**
	 * @Title: 成员变更MemberUpdate 
	 * @param group
	 * @param member
	 * @param members
	 * @return PassByBO
	 */
	public PassByBO setMemberUpdate(DeviceGroupPO group, DeviceGroupMemberPO member, Collection<DeviceGroupMemberPO> members){
		
		PassByContentBO passByContent = new PassByContentBO().setMemberUpdate(group, members);		
		this.setBundle_id(member.getBundleId())
			.setLayer_id(member.getLayerId())
			.setType("member_update_request")
			.setPass_by_content(passByContent);
		
		return this;
	}
	
	/**
	 * @Title: 管理员变更AdministratorUpdate 
	 * @param group
	 * @param member
	 * @return PassByBO
	 */
	public PassByBO setAdministratorUpdate(DeviceGroupPO group, DeviceGroupMemberPO member){
		
		PassByContentBO passByContent = new PassByContentBO().setAdministratorUpdate(group);		
		this.setBundle_id(member.getBundleId())
			.setLayer_id(member.getLayerId())
			.setType("administrator_update_request")
			.setPass_by_content(passByContent);
		
		return this;
	}
	
	public PassByBO setMeetingInfo(TetrisDispatchPO dispatch){
		
		JSONObject passByContent = new JSONObject();
		passByContent.put("meetingCode", dispatch.getMeetingCode());
		this.setType("meeting_info")
			.setPass_by_content(passByContent);
		
		return this;
	}
	
}
