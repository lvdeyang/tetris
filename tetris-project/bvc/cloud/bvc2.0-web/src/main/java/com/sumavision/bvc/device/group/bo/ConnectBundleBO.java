package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.common.group.po.CommonMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @ClassName: openBundle协议数据
 * @author wjw
 * @date 2018年11月7日 上午10:33:33
 */
public class ConnectBundleBO {
	
	public static final String BUSINESS_TYPE_MEETING = "meeting";
	
	public static final String BUSINESS_TYPE_VOD = "vod";
	
	public static final String OPERATE_TYPE = "start";

	private String taskId = "";
	
	private String lock_type = "";
	
	private String layerId = "";
	
	private String bundleId = "";
	
	private String bundle_type = "";
	
	private String device_model;//device类型，jv210/player 等
	
	private String businessType = "";
	
	private String operateType = "";
	
	private List<ConnectBO> channels = new ArrayList<ConnectBO>();
	
	private PassByBO pass_by_str;

	public String getTaskId() {
		return taskId;
	}

	public ConnectBundleBO setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}

	public String getLock_type() {
		return lock_type;
	}

	public ConnectBundleBO setLock_type(String lock_type) {
		this.lock_type = lock_type;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public ConnectBundleBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public ConnectBundleBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundle_type() {
		return bundle_type;
	}

	public ConnectBundleBO setBundle_type(String bundle_type) {
		this.bundle_type = bundle_type;
		return this;
	}

	public String getDevice_model() {
		return device_model;
	}

	public ConnectBundleBO setDevice_model(String device_model) {
		this.device_model = device_model;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public ConnectBundleBO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getOperateType() {
		return operateType;
	}

	public ConnectBundleBO setOperateType(String operateType) {
		this.operateType = operateType;
		return this;
	}

	public List<ConnectBO> getChannels() {
		return channels;
	}

	public ConnectBundleBO setChannels(List<ConnectBO> channels) {
		this.channels = channels;
		return this;
	}
	
	public PassByBO getPass_by_str() {
		return pass_by_str;
	}

	public ConnectBundleBO setPass_by_str(PassByBO pass_by_str) {
		this.pass_by_str = pass_by_str;
		return this;
	}

	/**
	 * @Title: 生成openBundle协议(带passby)
	 * @param member
	 * @param codec
	 * @return ConnectBundleBO
	 */
	public ConnectBundleBO set(DeviceGroupPO group, DeviceGroupMemberPO member, CodecParamBO codec){
		this.setLock_type("write")
			.setLayerId(member.getLayerId())
			.setBundleId(member.getBundleId())
			.setBundle_type(member.getVenusBundleType());
		
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		for(DeviceGroupMemberChannelPO channel: channels){
			ConnectBO connect = new ConnectBO().set(channel, codec);
			this.getChannels().add(connect);
		}
		
		PassByBO passBy = new PassByBO().setIncomingCall(group, member);
		this.setPass_by_str(passBy);
			
		return this;
	}

	/**
	 * 生成openBundle协议(role)<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 下午1:57:23
	 * @param role
	 * @param members
	 * @return
	 */
	public ConnectBundleBO setRole(DeviceGroupBusinessRolePO role, List<DeviceGroupMemberPO> members, CodecParamBO codec){
		this.setLock_type("write")
			.setLayerId(role.getLayerId())
			.setBundleId(role.getBundleId())
			.setBundle_type(role.getBaseType());
		
		String channel = role.getChannel();
		String[] channelIds = channel.split("%");
		for(String channelId: channelIds){
			ConnectBO connect = new ConnectBO().setLock_type("write")
				    						   .setLayerId(role.getLayerId())
				    						   .setBase_type(role.getBaseType())
				    						   .setBundleId(role.getBundleId())
				    						   .setChannelId(channelId)
				    						   .setChannel_status("Open")
				    						   .setCodec_param(codec);
			this.getChannels().add(connect);
		}
		
		PassByBO passBy = new PassByBO();
		SwitchRepeaterNodeBO switchContent = new SwitchRepeaterNodeBO();
		StringBufferWrapper buffer = new StringBufferWrapper();
		for(int i=0; i<members.size(); i++){
			if(i == members.size()-1){
				buffer.append(members.get(i).getBundleId());
			}else{
				buffer.append(members.get(i).getBundleId()).append("%");
			}
		}	
		switchContent.setBundles(buffer.toString());
		passBy.setType("bind_repeater_node");
		passBy.setBundle_id(role.getBundleId());
		passBy.setLayer_id(role.getLayerId());
		passBy.setPass_by_content(switchContent);
		
		this.setPass_by_str(passBy);
			
		return this;
	}
	
	/**
	 * 生成openBundle协议(role)--只传passby--切换<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 下午1:57:23
	 * @param role
	 * @param members
	 * @return
	 */
	public ConnectBundleBO setRolePassby(DeviceGroupBusinessRolePO role, List<DeviceGroupMemberPO> members){
		this.setLock_type("write")
			.setLayerId(role.getLayerId())
			.setBundleId(role.getBundleId())
			.setBundle_type(role.getBaseType());
		
		PassByBO passBy = new PassByBO();
		SwitchRepeaterNodeBO switchContent = new SwitchRepeaterNodeBO();
		StringBufferWrapper buffer = new StringBufferWrapper();
		for(int i=0; i<members.size(); i++){
			if(i == members.size()-1){
				buffer.append(members.get(i).getBundleId());
			}else{
				buffer.append(members.get(i).getBundleId()).append("%");
			}
		}	
		switchContent.setBundles(buffer.toString());
		passBy.setType("switch_repeater_node");
		passBy.setBundle_id(role.getBundleId());
		passBy.setLayer_id(role.getLayerId());
		passBy.setPass_by_content(switchContent);
		
		this.setPass_by_str(passBy);
			
		return this;
	}
	public ConnectBundleBO setRolePassby(CommonBusinessRolePO role, List<CommonMemberPO> members){
		this.setLock_type("write")
			.setLayerId(role.getLayerId())
			.setBundleId(role.getBundleId())
			.setBundle_type(role.getBaseType());
		
		PassByBO passBy = new PassByBO();
		SwitchRepeaterNodeBO switchContent = new SwitchRepeaterNodeBO();
		StringBufferWrapper buffer = new StringBufferWrapper();
		for(int i=0; i<members.size(); i++){
			if(i == members.size()-1){
				buffer.append(members.get(i).getBundleId());
			}else{
				buffer.append(members.get(i).getBundleId()).append("%");
			}
		}	
		switchContent.setBundles(buffer.toString());
		passBy.setType("switch_repeater_node");
		passBy.setBundle_id(role.getBundleId());
		passBy.setLayer_id(role.getLayerId());
		passBy.setPass_by_content(switchContent);
		
		this.setPass_by_str(passBy);
			
		return this;
	}
	
	/**
	 * 生成openBundle协议(role)--只传passby--解绑<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 下午1:57:23
	 * @param role
	 * @param members
	 * @return
	 */
	public ConnectBundleBO unbindRolePassby(DeviceGroupBusinessRolePO role, List<DeviceGroupMemberPO> members, CodecParamBO codec){
		this.setLock_type("write")
			.setLayerId(role.getLayerId())
			.setBundleId(role.getBundleId())
			.setBundle_type(role.getBaseType());
		
		String channel = role.getChannel();
		String[] channelIds = channel.split("%");
		for(String channelId: channelIds){
			ConnectBO connect = new ConnectBO().setLock_type("write")
				    						   .setLayerId(role.getLayerId())
				    						   .setBase_type(role.getBaseType())
				    						   .setBundleId(role.getBundleId())
				    						   .setChannelId(channelId)
				    						   .setChannel_status("Open")
				    						   .setCodec_param(codec);
			this.getChannels().add(connect);
		}
		
		PassByBO passBy = new PassByBO();
		SwitchRepeaterNodeBO switchContent = new SwitchRepeaterNodeBO();
		StringBufferWrapper buffer = new StringBufferWrapper();
		for(int i=0; i<members.size(); i++){
			if(i == members.size()-1){
				buffer.append(members.get(i).getBundleId());
			}else{
				buffer.append(members.get(i).getBundleId()).append("%");
			}
		}	
		switchContent.setBundles(buffer.toString());
		passBy.setType("unbind_repeater_node");
		passBy.setBundle_id(role.getBundleId());
		passBy.setLayer_id(role.getLayerId());
		passBy.setPass_by_content(switchContent);
		
		this.setPass_by_str(passBy);
			
		return this;
	}
	
	/**
	 * 生成openBundle协议(role)--只传passby<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 下午1:57:23
	 * @param DeviceGroupMemberPO member
	 * @return
	 */
	public ConnectBundleBO setRolePassby(DeviceGroupMemberPO member){
		this.setLock_type("write")
			.setLayerId(member.getLayerId())
			.setBundleId(member.getBundleId())
			.setBundle_type(member.getVenusBundleType());
		
		PassByBO passBy = new PassByBO();

		passBy.setType("bind_repeater_node");
		
		this.setPass_by_str(passBy);
			
		return this;
	}
	
	/**
	 * @Title: 生成openBundle协议(不带passby)
	 * @param member
	 * @param codec
	 * @return ConnectBundleBO
	 */
	public ConnectBundleBO setWithoutPassBy(DeviceGroupPO group, DeviceGroupMemberPO member, CodecParamBO codec){
		this.setLock_type("write")
			.setLayerId(member.getLayerId())
			.setBundleId(member.getBundleId())
			.setBundle_type(member.getVenusBundleType());
		
		Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
		for(DeviceGroupMemberChannelPO channel: channels){
			ConnectBO connect = new ConnectBO().set(channel, codec);
			this.getChannels().add(connect);
		}
			
		return this;
	}

}
