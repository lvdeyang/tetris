package com.sumavision.bvc.device.group.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.alibaba.druid.sql.visitor.functions.If;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.common.group.po.CommonChannelForwardPO;
import com.sumavision.bvc.common.group.po.CommonCombineAudioPO;
import com.sumavision.bvc.common.group.po.CommonCombineVideoPO;
import com.sumavision.bvc.common.group.po.CommonGroupPO;
import com.sumavision.bvc.common.group.po.CommonMemberPO;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.jv230.bo.Jv230BaseParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ChannelParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ForwardBO;
import com.sumavision.bvc.device.jv230.dto.Jv230ChannelDTO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @Title: 协议数据 
 * @author lvdeyang
 * @date 2018年8月7日 上午11:15:02 
 */
public class LogicBO {
	
	private String userId;
	
	/** 必须锁定所有bundle，默认true，点播系统使用true，开会时使用使用false */
	private boolean mustLockAllBundle = true;
	
	private List<ConnectBO> connect;
	
	private List<ConnectBundleBO> connectBundle;
	
	private List<DisconnectBO> disconnect;
	
	private List<DisconnectBundleBO> disconnectBundle;
	
	private List<ForwardSetBO> forwardSet;
	
	private List<ForwardDelBO> forwardDel;
	
	private List<CombineVideoBO> combineVideoSet;
	
	private List<CombineVideoBO> combineVideoUpdate;
	
	private List<CombineVideoBO> combineVideoDel;
	
	private List<CombineVideoBO> combineVideoOperation;
	
	private List<CombineAudioBO> combineAudioSet;
	
	private List<CombineAudioBO> combineAudioUpdate;
	
	private List<CombineAudioBO> combineAudioDel;
	
	private List<SourceBO> lock;
	
	private List<SourceBO> lockBundle;
	
	private List<SourceBO> unlock;
	
	private List<SourceBO> unlockBundle;
	
	private List<RecordSetBO> recordSet;
	
	private List<RecordSetBO> recordUpdate;
	
	private List<RecordSetBO> recordDel;
	
	private List<MediaPushSetBO> mediaPushSet;
	
	private List<MediaPushSetBO> mediaPushDel;
	
	private List<PublishStreamSetBO> publishStreamSet;
	
	private List<CallBO> incoming_call_request;
	
	private List<HangUpBO> hang_up_request;
	
	private List<Jv230ForwardBO> jv230ForwardSet;
	
	private List<Jv230ForwardBO> jv230AudioSet;
	
	private List<SourceBO> jv230ForwardDel;
	
	private List<PassByBO> pass_by;

	public String getUserId() {
		return userId;
	}

	public LogicBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public boolean isMustLockAllBundle() {
		return mustLockAllBundle;
	}

	public LogicBO setMustLockAllBundle(boolean mustLockAllBundle) {
		this.mustLockAllBundle = mustLockAllBundle;
		return this;
	}

	public List<ConnectBO> getConnect() {
		return connect;
	}

	public LogicBO setConnect(List<ConnectBO> connect) {
		this.connect = connect;
		return this;
	}

	public List<DisconnectBO> getDisconnect() {
		return disconnect;
	}

	public LogicBO setDisconnect(List<DisconnectBO> disconnect) {
		this.disconnect = disconnect;
		return this;
	}

	public List<ConnectBundleBO> getConnectBundle() {
		return connectBundle;
	}

	public LogicBO setConnectBundle(List<ConnectBundleBO> connectBundle) {
		this.connectBundle = connectBundle;
		return this;
	}

	public List<DisconnectBundleBO> getDisconnectBundle() {
		return disconnectBundle;
	}

	public LogicBO setDisconnectBundle(List<DisconnectBundleBO> disconnectBundle) {
		this.disconnectBundle = disconnectBundle;
		return this;
	}

	public List<ForwardSetBO> getForwardSet() {
		return forwardSet;
	}

	public LogicBO setForwardSet(List<ForwardSetBO> forwardSet) {
		this.forwardSet = forwardSet;
		return this;
	}

	public List<ForwardDelBO> getForwardDel() {
		return forwardDel;
	}

	public LogicBO setForwardDel(List<ForwardDelBO> forwardDel) {
		this.forwardDel = forwardDel;
		return this;
	}

	public List<CombineVideoBO> getCombineVideoSet() {
		return combineVideoSet;
	}

	public LogicBO setCombineVideoSet(List<CombineVideoBO> combineVideoSet) {
		this.combineVideoSet = combineVideoSet;
		return this;
	}

	public List<CombineVideoBO> getCombineVideoUpdate() {
		return combineVideoUpdate;
	}

	public LogicBO setCombineVideoUpdate(List<CombineVideoBO> combineVideoUpdate) {
		this.combineVideoUpdate = combineVideoUpdate;
		return this;
	}

	public List<CombineVideoBO> getCombineVideoDel() {
		return combineVideoDel;
	}

	public List<CombineVideoBO> getCombineVideoOperation() {
		return combineVideoOperation;
	}

	public LogicBO setCombineVideoOperation(List<CombineVideoBO> combineVideoOperation) {
		this.combineVideoOperation = combineVideoOperation;
		return this;
	}

	public LogicBO setCombineVideoDel(List<CombineVideoBO> combineVideoDel) {
		this.combineVideoDel = combineVideoDel;
		return this;
	}

	public List<CombineAudioBO> getCombineAudioSet() {
		return combineAudioSet;
	}

	public LogicBO setCombineAudioSet(List<CombineAudioBO> combineAudioSet) {
		this.combineAudioSet = combineAudioSet;
		return this;
	}

	public List<CombineAudioBO> getCombineAudioUpdate() {
		return combineAudioUpdate;
	}

	public LogicBO setCombineAudioUpdate(List<CombineAudioBO> combineAudioUpdate) {
		this.combineAudioUpdate = combineAudioUpdate;
		return this;
	}

	public List<CombineAudioBO> getCombineAudioDel() {
		return combineAudioDel;
	}

	public LogicBO setCombineAudioDel(List<CombineAudioBO> combineAudioDel) {
		this.combineAudioDel = combineAudioDel;
		return this;
	}

	public List<SourceBO> getLock() {
		return lock;
	}

	public LogicBO setLock(List<SourceBO> lock) {
		this.lock = lock;
		return this;
	}

	public List<SourceBO> getUnlock() {
		return unlock;
	}

	public LogicBO setUnlock(List<SourceBO> unlock) {
		this.unlock = unlock;
		return this;
	}

	public List<SourceBO> getLockBundle() {
		return lockBundle;
	}

	public LogicBO setLockBundle(List<SourceBO> lockBundle) {
		this.lockBundle = lockBundle;
		return this;
	}

	public List<SourceBO> getUnlockBundle() {
		return unlockBundle;
	}

	public LogicBO setUnlockBundle(List<SourceBO> unlockBundle) {
		this.unlockBundle = unlockBundle;
		return this;
	}

	public List<RecordSetBO> getRecordSet() {
		return recordSet;
	}

	public LogicBO setRecordSet(List<RecordSetBO> recordSet) {
		this.recordSet = recordSet;
		return this;
	}
	
	public List<RecordSetBO> getRecordUpdate() {
		return recordUpdate;
	}

	public LogicBO setRecordUpdate(List<RecordSetBO> recordUpdate) {
		this.recordUpdate = recordUpdate;
		return this;
	}

	public List<RecordSetBO> getRecordDel() {
		return recordDel;
	}

	public LogicBO setRecordDel(List<RecordSetBO> recordDel) {
		this.recordDel = recordDel;
		return this;
	}
	
	public List<MediaPushSetBO> getMediaPushSet() {
		return mediaPushSet;
	}

	public LogicBO setMediaPushSet(List<MediaPushSetBO> mediaPushSet) {
		this.mediaPushSet = mediaPushSet;
		return this;
	}

	public List<MediaPushSetBO> getMediaPushDel() {
		return mediaPushDel;
	}

	public LogicBO setMediaPushDel(List<MediaPushSetBO> mediaPushDel) {
		this.mediaPushDel = mediaPushDel;
		return this;
	}

	public List<CallBO> getIncoming_call_request() {
		return incoming_call_request;
	}

	public LogicBO setIncoming_call_request(List<CallBO> incoming_call_request) {
		this.incoming_call_request = incoming_call_request;
		return this;
	}

	public List<HangUpBO> getHang_up_request() {
		return hang_up_request;
	}

	public LogicBO setHang_up_request(List<HangUpBO> hang_up_request) {
		this.hang_up_request = hang_up_request;
		return this;
	}

	public List<Jv230ForwardBO> getJv230ForwardSet() {
		return jv230ForwardSet;
	}

	public LogicBO setJv230ForwardSet(List<Jv230ForwardBO> jv230ForwardSet) {
		this.jv230ForwardSet = jv230ForwardSet;
		return this;
	}

	public List<SourceBO> getJv230ForwardDel() {
		return jv230ForwardDel;
	}

	public LogicBO setJv230ForwardDel(List<SourceBO> jv230ForwardDel) {
		this.jv230ForwardDel = jv230ForwardDel;
		return this;
	}

	public List<Jv230ForwardBO> getJv230AudioSet() {
		return jv230AudioSet;
	}

	public LogicBO setJv230AudioSet(List<Jv230ForwardBO> jv230AudioSet) {
		this.jv230AudioSet = jv230AudioSet;
		return this;
	}

	public List<PassByBO> getPass_by() {
		return pass_by;
	}

	public LogicBO setPass_by(List<PassByBO> pass_by) {
		this.pass_by = pass_by;
		return this;
	}

	public List<PublishStreamSetBO> getPublishStreamSet() {
		return publishStreamSet;
	}

	public void setPublishStreamSet(List<PublishStreamSetBO> publishStreamSet) {
		this.publishStreamSet = publishStreamSet;
	}
	
	/**
	 * @Title: 生成锁协议<br/> 
	 * @param channels
	 * @return LogicBO
	 */
	public LogicBO setLock(Collection<DeviceGroupMemberChannelPO> channels){
		if(channels != null && channels.size()>0){
			if(this.getLock() == null) this.setLock(new ArrayList<SourceBO>());
			for(DeviceGroupMemberChannelPO channel: channels){
				SourceBO source = new SourceBO().setBundleId(channel.getBundleId())
												.setChannelId(channel.getChannelId())
												.setLayerId(channel.getMember().getLayerId());
				this.getLock().add(source);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成锁设备协议<br/> 
	 * @param members
	 * @return LogicBO
	 */
	public LogicBO setLockBundle(Collection<DeviceGroupMemberPO> members){
		if(members != null && members.size()>0){
			if(this.getLockBundle() == null) this.setLockBundle(new ArrayList<SourceBO>());
			for(DeviceGroupMemberPO member: members){
				SourceBO source = new SourceBO().setLayerId(member.getLayerId())
												.setBundleId(member.getBundleId());
				this.getLockBundle().add(source);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成呼叫协议 <br/>
	 * @param channels 通道
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setConnect(Collection<DeviceGroupMemberChannelPO> channels, CodecParamBO codec){
		if(channels != null){
			if(this.getConnect() == null) this.setConnect(new ArrayList<ConnectBO>());
			for(DeviceGroupMemberChannelPO channel:channels){
				ConnectBO protocalConnect = new ConnectBO().set(channel, codec);
				this.getConnect().add(protocalConnect);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成呼叫设备协议 <br/>
	 * @param members 成员
	 * @param codec 参数模板
	 * @return LogicBO    返回类型 
	 */
	public LogicBO setConnectBundle(DeviceGroupPO group, Collection<DeviceGroupMemberPO> members, CodecParamBO codec){
		if(members != null){
			if(this.getConnectBundle() == null) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
			for(DeviceGroupMemberPO member: members){
				ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().set(group, member, codec);
				this.getConnectBundle().add(protocalConnectBundleBO);
			}
		}
		return this;
	}
	
	/**
	 * 生成呼叫角色透传协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:12:58
	 * @param role
	 * @return
	 */
	public LogicBO setRole(DeviceGroupBusinessRolePO role, List<DeviceGroupMemberPO> members, CodecParamBO codec){
		if(role != null && members != null && members.size()>0){
			if(this.getConnectBundle() == null || this.getConnectBundle().size()<=0) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
			ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().setRole(role, members, codec);
			this.getConnectBundle().add(protocalConnectBundleBO);
		}
		return this;
	}
	
	/**
	 * 方法概述<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月12日 上午9:23:25
	 * @param TerminalBundlePO bundleInfo 虚拟设备信息
	 * @param List<ChannelSchemeDTO> channelInfos 虚拟设备通道信息
	 * @return LogicBO
	 */
	public LogicBO createRole(DeviceGroupBusinessRolePO role){
		
		//TODO:信令passby下发
		PassByBO passBy = new PassByBO();
		CreateRepeaterNodeBO passByContent = new CreateRepeaterNodeBO();
		passByContent.setChannels(role.getChannel());
		
		passBy.setLayer_id(role.getLayerId());
		passBy.setBundle_id(role.getBundleId());
		passBy.setType("create_repeater_node");
		passBy.setPass_by_content(passByContent);
		
		if(this.getPass_by() == null || this.getPass_by().size() <= 0) this.setPass_by(new ArrayList<PassByBO>());
		this.getPass_by().add(passBy);
		
		return this;
	}
	
	/**
	 * 生成转发角色透传协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:12:58
	 * @param role
	 * @return
	 */
	public LogicBO setRolePassby(DeviceGroupBusinessRolePO role, List<DeviceGroupMemberPO> members){
		if(role != null && members != null && members.size()>0){
			if(this.getConnectBundle() == null || this.getConnectBundle().size()<=0) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
			ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().setRolePassby(role, members);
			this.getConnectBundle().add(protocalConnectBundleBO);
		}
		return this;
	}
	public LogicBO setRolePassby(CommonBusinessRolePO role, List<CommonMemberPO> members){
		if(role != null && members != null && members.size()>0){
			if(this.getConnectBundle() == null || this.getConnectBundle().size()<=0) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
			ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().setRolePassby(role, members);
			this.getConnectBundle().add(protocalConnectBundleBO);
		}
		return this;
	}
	
	/**
	 * 生成解绑角色透传协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:12:58
	 * @param role
	 * @return
	 */
	public LogicBO unbindRolePassby(DeviceGroupBusinessRolePO role, List<DeviceGroupMemberPO> members, CodecParamBO codec){
		if(role != null && members != null && members.size()>0){
			if(this.getConnectBundle() == null || this.getConnectBundle().size()<=0) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
			ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().unbindRolePassby(role, members, codec);
			this.getConnectBundle().add(protocalConnectBundleBO);
		}
		return this;
	}
	
	/**
	 * 生成转发角色透传协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午11:12:58
	 * @param member
	 * @return
	 */
	public LogicBO setRolePassby(DeviceGroupMemberPO member){
		if(member != null){
			if(this.getConnectBundle() == null || this.getConnectBundle().size()<=0) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
			ConnectBundleBO protocalConnectBundleBO = new ConnectBundleBO().setRolePassby(member);
			this.getConnectBundle().add(protocalConnectBundleBO);
		}
		return this;
	}
	
	/**
	 * @Title: 生成Jv230通道置空协议 <br/>
	 * @param channels 通道
	 * @return LogicBO 
	 */
	public LogicBO setJv230Connect(Collection<Jv230ChannelPO> channels){
		if(channels != null && channels.size() > 0){
			if(this.getJv230ForwardSet() == null)this.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
			if(this.getJv230AudioSet() == null)this.setJv230AudioSet(new ArrayList<Jv230ForwardBO>());
			
			for(Jv230ChannelPO channel: channels){	
				if(channel.getChannelType().equals("VenusVideoOut")){
					Jv230BaseParamBO baseParamBO = new Jv230BaseParamBO().setNull();
					Jv230ChannelParamBO channelParamBO = new Jv230ChannelParamBO().set(channel,baseParamBO);
					Jv230ForwardBO forwardBO = new Jv230ForwardBO().set(channel, channelParamBO);
					this.getJv230ForwardSet().add(forwardBO);	
				}else if(channel.getChannelType().equals("VenusAudioOut")){					
					Jv230BaseParamBO baseParamBO = new Jv230BaseParamBO().setAudioNull();
					Jv230ChannelParamBO channelParamBO = new Jv230ChannelParamBO().set(channel,baseParamBO);
					Jv230ForwardBO forwardBO = new Jv230ForwardBO().set(channel, channelParamBO);
					this.getJv230AudioSet().add(forwardBO);
				}
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成通道挂断协议<br/>
	 * @param channels 通道
	 * @param codec 参数模板
	 * @return LogicBO
	 */
	public LogicBO setDisconnect(Collection<DeviceGroupMemberChannelPO> channels, CodecParamBO codec){
		if(channels != null){
			if(this.getDisconnect() == null) this.setDisconnect(new ArrayList<DisconnectBO>());
			for(DeviceGroupMemberChannelPO channel:channels){
				DisconnectBO protocalDisconnect = new DisconnectBO().set(channel, codec);
				this.getDisconnect().add(protocalDisconnect);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成设备挂断协议 <br/> 
	 * @param members 成员设备
	 * @param codec 参数模板
	 * @return LogicBO 
	 */	
	public LogicBO setDisconnectBundle(DeviceGroupPO group, Collection<DeviceGroupMemberPO> members, CodecParamBO codec){
		if(members != null){
			if(this.getDisconnectBundle() == null) this.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
			for(DeviceGroupMemberPO member: members){
				DisconnectBundleBO protocalDisconnectBundle = new DisconnectBundleBO().setDisconnectBundle(group, member, codec);
				this.getDisconnectBundle().add(protocalDisconnectBundle);
			}
		}
		return this;
	}
	
	/**
	 * 生成角色挂断协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 上午10:58:19
	 * @param roles
	 */
	public LogicBO setDisconnectRole(Collection<DeviceGroupBusinessRolePO> roles){
		if(roles != null){
			if(this.getDisconnectBundle() == null) this.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
			for(DeviceGroupBusinessRolePO role: roles){
				DisconnectBundleBO protocalDisconnectBundle = new DisconnectBundleBO().setBundle_type(role.getBaseType())
																					  .setBundleId(role.getBundleId())
																					  .setLayerId(role.getLayerId());
				this.getDisconnectBundle().add(protocalDisconnectBundle);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 关闭jv230通道<br/> 
	 * @param channels jv230通道
	 * @return LogicBO 协议 
	 */
	public LogicBO setJv230Disconnect(Collection<Jv230ChannelDTO> channels){
		if(channels!=null && channels.size()>0){
			if(this.getJv230ForwardDel() == null) this.setJv230ForwardDel(new ArrayList<SourceBO>());
			for(Jv230ChannelDTO channel:channels){
				this.getJv230ForwardDel().add(new SourceBO().setBundleId(channel.getBundleId())
															.setChannelId(channel.getChannelId())
															.setLayerId(channel.getLayerId()));	
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成呼叫通知协议 <br/>
	 * @param group 设备组
	 * @return LogicBO 
	 */
	public LogicBO setCall(DeviceGroupPO group){
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			if(this.getIncoming_call_request() == null) this.setIncoming_call_request(new ArrayList<CallBO>());
			for(DeviceGroupMemberPO member:members){
				CallBO call = new CallBO().setType(group.getType().getProtocalId())
										  .setCaller_name(group.getName())
										  .setCallee_layer_id(member.getLayerId())
										  .setCallee_bundle_id(member.getBundleId().toString())
										  .setUuid(member.getUuid())
										  .setGroupUuid(group.getUuid());
				this.getIncoming_call_request().add(call);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成单个设备呼叫通知协议 <br/>
	 * @param group 设备组
	 * @param member 设备成员
	 * @return LogicBO 
	 */
	public LogicBO setCall(DeviceGroupPO group, DeviceGroupMemberPO member){
		if(this.getIncoming_call_request() == null) this.setIncoming_call_request(new ArrayList<CallBO>());
		CallBO call = new CallBO().setType(group.getType().getProtocalId())
								  .setCaller_name(group.getName())
								  .setCallee_layer_id(member.getLayerId())
								  .setCallee_bundle_id(member.getBundleId().toString())
								  .setUuid(member.getUuid())
								  .setGroupUuid(group.getUuid());
		this.getIncoming_call_request().add(call);
		return this;
	}
	
	/**
	 * @Title: 生成挂断通知协议<br/> 
	 * @param group 设备组
	 * @return LogicBO  
	 */
	public LogicBO setHangup(DeviceGroupPO group){
		Set<DeviceGroupMemberPO> members = group.getMembers();
		if(members!=null && members.size()>0){
			if(this.getHang_up_request() == null)this.setHang_up_request(new ArrayList<HangUpBO>());
			for(DeviceGroupMemberPO member:members){
				HangUpBO hangUp = new HangUpBO().setType(group.getType().getProtocalId())
												.setCaller_name(group.getName())
												.setCallee_layer_id(member.getLayerId())
												.setCallee_bundle_id(member.getBundleId().toString())
												.setGroupUuid(group.getUuid());
				this.getHang_up_request().add(hangUp);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成单个设备挂断通知协议<br/> 
	 * @param group 设备组
	 * @param member 设备成员
	 * @return LogicBO  
	 */
	public LogicBO setHangup(DeviceGroupPO group, DeviceGroupMemberPO member){
		if(this.getHang_up_request() == null)this.setHang_up_request(new ArrayList<HangUpBO>());
		HangUpBO hangUp = new HangUpBO().setType(group.getType().getProtocalId())
										.setCaller_name(group.getName())
										.setCallee_layer_id(member.getLayerId())
										.setCallee_bundle_id(member.getBundleId().toString())
										.setGroupUuid(group.getUuid());
		this.getHang_up_request().add(hangUp);
		return this;
	}
	
	/**
	 * @Title: 生成设置转发协议 <br/>
	 * @param forwards 业务转发数据
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setForward(Collection<ChannelForwardPO> forwards, CodecParamBO codec){
		if(forwards!=null && forwards.size()>0){
			if(this.getForwardSet() == null) this.setForwardSet(new ArrayList<ForwardSetBO>());
			for(ChannelForwardPO forward:forwards){
				ForwardSetBO protocolForward = new ForwardSetBO().set(forward, codec);
				this.getForwardSet().add(protocolForward);
			}
		}
		return this;
	}
	public LogicBO setForward_Common(Collection<CommonChannelForwardPO> forwards, CodecParamBO codec){
		if(forwards!=null && forwards.size()>0){
			if(this.getForwardSet() == null) this.setForwardSet(new ArrayList<ForwardSetBO>());
			for(CommonChannelForwardPO forward:forwards){
				ForwardSetBO protocolForward = new ForwardSetBO().set(forward, codec);
				this.getForwardSet().add(protocolForward);
			}
		}
		return this;
	}
	
	/**
	 * 设置设备观看角色协议<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月26日 下午1:31:39
	 * @param DeviceGroupMemberPO member
	 * @param DeviceGroupBusinessRolePO role
	 * @param CodecParamBO codec
	 * @return
	 */
	public LogicBO setRoleForward(DeviceGroupMemberPO member, DeviceGroupBusinessRolePO role, CodecParamBO codec){
		if(member!=null && role!=null && (role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM))){
			if(this.getForwardSet() == null) this.setForwardSet(new ArrayList<ForwardSetBO>());
			Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
			for(DeviceGroupMemberChannelPO channel: channels){
				if(channel.getType().isVideoDecode()){
					ForwardSetSrcBO src = new ForwardSetSrcBO().setType("channel")
															   .setLayerId(role.getLayerId())
															   .setBundleId(role.getBundleId())
															   .setChannelId(channel.getChannelId());
					ForwardSetDstBO dst = new ForwardSetDstBO().setCodec_param(codec)
															   .setBase_type(channel.getChannelType())
															   .setBundle_type(channel.getVenusBundleType())
															   .setBundleId(member.getBundleId())
															   .setChannelId(channel.getChannelId())
															   .setLayerId(member.getLayerId());
					ForwardSetBO protocolForward = new ForwardSetBO().setDst(dst)
																	 .setSrc(src);
					this.getForwardSet().add(protocolForward);
				}
			}
		}
		return this;
	}
	
	/**
	 * 设置设备观看空源协议<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月26日 下午1:34:35
	 * @param DeviceGroupMemberPO member
	 * @param CodecParamBO codec
	 * @return
	 */
	public LogicBO setRoleForwardNull(DeviceGroupMemberPO member, CodecParamBO codec){
		if(member!=null){
			if(this.getForwardSet() == null) this.setForwardSet(new ArrayList<ForwardSetBO>());
			Set<DeviceGroupMemberChannelPO> channels = member.getChannels();
			for(DeviceGroupMemberChannelPO channel: channels){
				if(channel.getType().isVideoDecode()){
					ForwardSetDstBO dst = new ForwardSetDstBO().setCodec_param(codec)
															   .setBase_type(channel.getChannelType())
															   .setBundle_type(channel.getVenusBundleType())
															   .setBundleId(member.getBundleId())
															   .setChannelId(channel.getChannelId())
															   .setLayerId(member.getLayerId());
					ForwardSetBO protocolForward = new ForwardSetBO().setDst(dst)
																	 .setSrc(null);
					this.getForwardSet().add(protocolForward);
				}
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成删除转发协议<br/> 
	 * @param forwards 需要删除的转发
	 * @param codec 参数模板
	 * @return LogicBO
	 */
	public LogicBO deleteForward(Collection<ChannelForwardPO> forwards, CodecParamBO codec){
		if(forwards!=null && forwards.size()>0){
			if(this.getForwardDel() == null) this.setForwardDel(new ArrayList<ForwardDelBO>());
			for(ChannelForwardPO forward:forwards){
				ForwardDelBO protocalForward = new ForwardDelBO().set(forward, codec);
				this.getForwardDel().add(protocalForward);
			}
		}
		return this;
	}
	public LogicBO deleteForward_Common(Collection<CommonChannelForwardPO> forwards, CodecParamBO codec){
		if(forwards!=null && forwards.size()>0){
			if(this.getForwardDel() == null) this.setForwardDel(new ArrayList<ForwardDelBO>());
			for(CommonChannelForwardPO forward:forwards){
				ForwardDelBO protocalForward = new ForwardDelBO().set(forward, codec);
				this.getForwardDel().add(protocalForward);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成创建混音协议<br/> 
	 * @param combineAudios 业务混音数据
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setCombineAudioSet(Collection<CombineAudioPO> combineAudios, CodecParamBO codec){
		if(combineAudios!=null && combineAudios.size()>0){
			if(this.getCombineAudioSet() == null) this.setCombineAudioSet(new ArrayList<CombineAudioBO>());
			for(CombineAudioPO combineAudio:combineAudios){
				if(combineAudio.getSrcs().size() > 1){
					CombineAudioBO protocalCombineAudio = new CombineAudioBO().set(combineAudio, codec);
					this.getCombineAudioSet().add(protocalCombineAudio);
				}
			}
		}
		return this;
	}
	public LogicBO setCombineAudioSet_Common(Collection<CommonCombineAudioPO> combineAudios, CodecParamBO codec){
		if(combineAudios!=null && combineAudios.size()>0){
			if(this.getCombineAudioSet() == null) this.setCombineAudioSet(new ArrayList<CombineAudioBO>());
			for(CommonCombineAudioPO combineAudio:combineAudios){
				if(combineAudio.getSrcs().size() > 1){
					CombineAudioBO protocalCombineAudio = new CombineAudioBO().set(combineAudio, codec);
					this.getCombineAudioSet().add(protocalCombineAudio);
				}
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成修改混音协议 <br/>
	 * @param combineAudios 业务混音数据
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setCombineAudioUpdate(Collection<CombineAudioPO> combineAudios, CodecParamBO codec){
		if(combineAudios!=null && combineAudios.size()>0){
			if(this.getCombineAudioUpdate() == null) this.setCombineAudioUpdate(new ArrayList<CombineAudioBO>());
			for(CombineAudioPO combineAudio:combineAudios){

					CombineAudioBO protocalCombineAudio = new CombineAudioBO().set(combineAudio, codec);
					this.getCombineAudioUpdate().add(protocalCombineAudio);

			}
		}
		return this;
	}
	public LogicBO setCombineAudioUpdate_Common(Collection<CommonCombineAudioPO> combineAudios, CodecParamBO codec){
		if(combineAudios!=null && combineAudios.size()>0){
			if(this.getCombineAudioUpdate() == null) this.setCombineAudioUpdate(new ArrayList<CombineAudioBO>());
			for(CommonCombineAudioPO combineAudio:combineAudios){

					CombineAudioBO protocalCombineAudio = new CombineAudioBO().set(combineAudio, codec);
					this.getCombineAudioUpdate().add(protocalCombineAudio);

			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成删除混音协议 <br/>
	 * @param combineAudios 业务混音数据
	 * @param codec 参数模板
	 * @return LogicBO
	 */
	public LogicBO setCombineAudioDel(Collection<CombineAudioPO> combineAudios){
		if(combineAudios!=null && combineAudios.size()>0){
			if(this.getCombineAudioDel() == null) this.setCombineAudioDel(new ArrayList<CombineAudioBO>());
			for(CombineAudioPO combineAudio:combineAudios){
				CombineAudioBO protocalCombineAudio = new CombineAudioBO().setUuid(combineAudio.getUuid());
				this.getCombineAudioDel().add(protocalCombineAudio);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成创建合屏协议<br/> 
	 * @param combineVideos 业务合屏数据
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setCombineVideoSet(Collection<CombineVideoPO> combineVideos, CodecParamBO codec){
		if(combineVideos!=null && combineVideos.size()>0){
			if(this.getCombineVideoSet() == null) this.setCombineVideoSet(new ArrayList<CombineVideoBO>());
			for(CombineVideoPO combineVideo:combineVideos){
				if(combineVideo.isEffective()){
					CombineVideoBO protocalCombineVideo = new CombineVideoBO().set(combineVideo, codec);
					this.getCombineVideoSet().add(protocalCombineVideo);
				}
			}
		}
		return this;
	}
	public LogicBO setCombineVideoSet_Common(Collection<CommonCombineVideoPO> combineVideos, CodecParamBO codec){
		if(combineVideos!=null && combineVideos.size()>0){
			if(this.getCombineVideoSet() == null) this.setCombineVideoSet(new ArrayList<CombineVideoBO>());
			for(CommonCombineVideoPO combineVideo:combineVideos){
				if(combineVideo.isEffective()){
					CombineVideoBO protocalCombineVideo = new CombineVideoBO().set(combineVideo, codec);
					this.getCombineVideoSet().add(protocalCombineVideo);
				}
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成修改合屏协议<br/> 
	 * @param combineVideos 业务合屏数据
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setCombineVideoUpdate(Collection<CombineVideoPO> combineVideos, CodecParamBO codec){
		if(combineVideos!=null && combineVideos.size()>0){
			if(this.getCombineVideoUpdate() == null) this.setCombineVideoUpdate(new ArrayList<CombineVideoBO>());
			for(CombineVideoPO combineVideo:combineVideos){
				if(combineVideo.isEffective()){
					CombineVideoBO protocalCombineVideo = new CombineVideoBO().set(combineVideo, codec);
					this.getCombineVideoUpdate().add(protocalCombineVideo);
				}
			}
		}
		return this;
	}
	public LogicBO setCombineVideoUpdate_Common(Collection<CommonCombineVideoPO> combineVideos, CodecParamBO codec){
		if(combineVideos!=null && combineVideos.size()>0){
			if(this.getCombineVideoUpdate() == null) this.setCombineVideoUpdate(new ArrayList<CombineVideoBO>());
			for(CommonCombineVideoPO combineVideo:combineVideos){
				if(combineVideo.isEffective()){
					CombineVideoBO protocalCombineVideo = new CombineVideoBO().set(combineVideo, codec);
					this.getCombineVideoUpdate().add(protocalCombineVideo);
				}
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成删除合屏协议<br/> 
	 * @param combineVideos 业务合屏数据
	 * @param codec 参数模板
	 * @return LogicBO 
	 */
	public LogicBO setCombineVideoDel(Collection<CombineVideoPO> combineVideos){
		if(combineVideos!=null && combineVideos.size()>0){
			if(this.getCombineVideoDel() == null) this.setCombineVideoDel(new ArrayList<CombineVideoBO>());
			for(CombineVideoPO combineVideo:combineVideos){
				CombineVideoBO protocalCombineVideo = new CombineVideoBO().setUuid(combineVideo.getUuid());
				this.getCombineVideoDel().add(protocalCombineVideo);
			}
		}
		return this;
	}
	public LogicBO setCombineVideoDel_Common(Collection<CommonCombineVideoPO> combineVideos){
		if(combineVideos!=null && combineVideos.size()>0){
			if(this.getCombineVideoDel() == null) this.setCombineVideoDel(new ArrayList<CombineVideoBO>());
			for(CommonCombineVideoPO combineVideo:combineVideos){
				CombineVideoBO protocalCombineVideo = new CombineVideoBO().setUuid(combineVideo.getUuid());
				this.getCombineVideoDel().add(protocalCombineVideo);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成开始录制协议<br/> 
	 * @param records 业务层录制数据
	 * @param codec 参数模板
	 * @return LogicBO 协议数据
	 */
	public LogicBO setRecordSet(Collection<RecordPO> records, CodecParamBO codec, String regionId, String programId, String liveBoId){
		if(records!=null && records.size()>0){
			if(this.getRecordSet() == null) this.setRecordSet(new ArrayList<RecordSetBO>());
			for(RecordPO record:records){
				if(record.getVideoType()!=null && record.getVideoType().getName()!=null && record.getVideoType().getName()!=""){
					RecordSetBO protocalRecord = new RecordSetBO().set(record, codec);
					protocalRecord.setLocationID(regionId);
					protocalRecord.setCategoryID(programId);
					protocalRecord.setCategoryLiveID(liveBoId);
					this.getRecordSet().add(protocalRecord);
				}
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成修改录制协议<br/> 
	 * @param records 业务层录制数据
	 * @param codec 参数模板
	 * @return LogicBO 协议数据
	 */
	public LogicBO setRecordUpdate(Collection<RecordPO> records, CodecParamBO codec, String regionId, String programId, String liveBoId){
		if(records!=null && records.size()>0){
			if(this.getRecordUpdate() == null) this.setRecordUpdate(new ArrayList<RecordSetBO>());
			for(RecordPO record:records){
				//过滤没有视频的录制
				if(record.getVideoType()!=null && record.getVideoType().getName()!=null && record.getVideoType().getName()!=""){
					RecordSetBO protocalRecord = new RecordSetBO().set(record, codec);
					protocalRecord.setLocationID(regionId);
					protocalRecord.setCategoryID(programId);
					protocalRecord.setCategoryLiveID(liveBoId);
					this.getRecordUpdate().add(protocalRecord);
				}			
			}
		}
		return this;
	}
	
	/**
	 * @Title: 生成停止录制协议<br/> 
	 * @param records 业务层录制数据
	 * @return LogicBO 协议数据
	 */
	public LogicBO setRecordDel(Collection<RecordPO> records, String transferToVod){
		if(records!=null && records.size()>0){
			if(this.getRecordDel() == null) this.setRecordDel(new ArrayList<RecordSetBO>());
			for(RecordPO record:records){
				RecordSetBO protocalRecord = new RecordSetBO().setUuid(record.getUuid());
				protocalRecord.setTransferToVod(transferToVod);
				protocalRecord.setGroupUuid(record.getGroup().getUuid());
				this.getRecordDel().add(protocalRecord);
			}
		}
		return this;
	}

	/**
	 * @Title: 生成发布流的协议<br/> 
	 * @param records 业务层录制数据
	 * @param codec 参数模板
	 * @return LogicBO 协议数据
	 */
	public LogicBO setPublishStreamSet(Collection<PublishStreamPO> publishStreams, CodecParamBO codec){
		if(publishStreams!=null && publishStreams.size()>0){
			if(this.getPublishStreamSet() == null) this.setPublishStreamSet(new ArrayList<PublishStreamSetBO>());
			for(PublishStreamPO publishStream : publishStreams){
				PublishStreamSetBO protocalPublishStream = new PublishStreamSetBO().set(publishStream, codec);
				this.getPublishStreamSet().add(protocalPublishStream);
			}
		}
		return this;
	}
	
	/**
	 * @Title: 合并协议数据 <br/>
	 * @param from
	 * @return LogicBO 
	 */
	public LogicBO merge(LogicBO from){
		if(this.getUserId()==null && from.getUserId()!=null) this.setUserId(from.getUserId());
		
		if(this.getConnect() == null) this.setConnect(new ArrayList<ConnectBO>());
		if(from.getConnect()!=null && from.getConnect().size()>0) this.getConnect().addAll(from.getConnect());
		
		if(this.getConnectBundle() == null) this.setConnectBundle(new ArrayList<ConnectBundleBO>());
		if(from.getConnectBundle()!=null && from.getConnectBundle().size()>0) this.getConnectBundle().addAll(from.getConnectBundle());
		
		if(this.getDisconnect() == null) this.setDisconnect(new ArrayList<DisconnectBO>());
		if(from.getDisconnect()!=null && from.getDisconnect().size()>0) this.getDisconnect().addAll(from.getDisconnect());
		
		if(this.getDisconnectBundle() == null) this.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
		if(from.getDisconnectBundle()!=null && from.getDisconnectBundle().size()>0) this.getDisconnectBundle().addAll(from.getDisconnectBundle());
		
		if(this.getForwardSet() == null) this.setForwardSet(new ArrayList<ForwardSetBO>());
		if(from.getForwardSet()!=null && from.getForwardSet().size()>0) this.getForwardSet().addAll(from.getForwardSet());
		
		if(this.getForwardDel() == null) this.setForwardDel(new ArrayList<ForwardDelBO>());
		if(from.getForwardDel()!=null && from.getForwardDel().size()>0) this.getForwardDel().addAll(from.getForwardDel());
		
		if(this.getCombineVideoSet() == null) this.setCombineVideoSet(new ArrayList<CombineVideoBO>());
		if(from.getCombineVideoSet()!=null && from.getCombineVideoSet().size()>0) this.getCombineVideoSet().addAll(from.getCombineVideoSet());
		
		if(this.getCombineVideoUpdate() == null) this.setCombineVideoUpdate(new ArrayList<CombineVideoBO>());
		if(from.getCombineVideoUpdate()!=null && from.getCombineVideoUpdate().size()>0) this.getCombineVideoUpdate().addAll(from.getCombineVideoUpdate());
		
		if(this.getCombineVideoDel() == null) this.setCombineVideoDel(new ArrayList<CombineVideoBO>());
		if(from.getCombineVideoDel()!=null && from.getCombineVideoDel().size()>0) this.getCombineVideoDel().addAll(from.getCombineVideoDel());

		if(this.getCombineVideoOperation() == null) this.setCombineVideoOperation(new ArrayList<CombineVideoBO>());
		if(from.getCombineVideoOperation()!=null && from.getCombineVideoOperation().size()>0) this.getCombineVideoOperation().addAll(from.getCombineVideoOperation());
		
		if(this.getCombineAudioSet() == null) this.setCombineAudioSet(new ArrayList<CombineAudioBO>());
		if(from.getCombineAudioSet()!=null && from.getCombineAudioSet().size()>0) this.getCombineAudioSet().addAll(from.getCombineAudioSet());
		
		if(this.getCombineAudioUpdate() == null) this.setCombineAudioUpdate(new ArrayList<CombineAudioBO>());
		if(from.getCombineAudioUpdate()!=null && from.getCombineAudioUpdate().size()>0) this.getCombineAudioUpdate().addAll(from.getCombineAudioUpdate());
		
		if(this.getCombineAudioDel() == null) this.setCombineAudioDel(new ArrayList<CombineAudioBO>());
		if(from.getCombineAudioDel()!=null && from.getCombineAudioDel().size()>0) this.getCombineAudioDel().addAll(from.getCombineAudioDel());
		
		if(this.getLock() == null) this.setLock(new ArrayList<SourceBO>());
		if(from.getLock()!=null && from.getLock().size()>0) this.getLock().addAll(from.getLock());
		
		if(this.getUnlock() == null) this.setUnlock(new ArrayList<SourceBO>());
		if(from.getUnlock()!=null && from.getUnlock().size()>0) this.getUnlock().addAll(from.getUnlock());
		
		if(this.getLockBundle() == null) this.setLockBundle(new ArrayList<SourceBO>());
		if(from.getLockBundle()!=null && from.getLockBundle().size()>0) this.getLockBundle().addAll(from.getLockBundle());
		
		if(this.getUnlockBundle() == null) this.setUnlockBundle(new ArrayList<SourceBO>());
		if(from.getUnlockBundle()!=null && from.getUnlockBundle().size()>0) this.getUnlockBundle().addAll(from.getUnlockBundle());
		
		if(this.getRecordSet() == null) this.setRecordSet(new ArrayList<RecordSetBO>());
		if(from.getRecordSet()!=null && from.getRecordSet().size()>0) this.getRecordSet().addAll(from.getRecordSet());
		
		if(this.getRecordUpdate() == null) this.setRecordUpdate(new ArrayList<RecordSetBO>());
		if(from.getRecordUpdate()!=null && from.getRecordUpdate().size()>0) this.getRecordUpdate().addAll(from.getRecordUpdate());
		
		if(this.getRecordDel() == null) this.setRecordDel(new ArrayList<RecordSetBO>());
		if(from.getRecordDel()!=null && from.getRecordDel().size()>0) this.getRecordDel().addAll(from.getRecordDel());
		
		if(this.getMediaPushSet() == null) this.setMediaPushSet(new ArrayList<MediaPushSetBO>());
		if(from.getMediaPushSet()!=null && from.getMediaPushSet().size()>0) this.getMediaPushSet().addAll(from.getMediaPushSet());
		
		if(this.getMediaPushDel() == null) this.setMediaPushDel(new ArrayList<MediaPushSetBO>());
		if(from.getMediaPushDel()!=null && from.getMediaPushDel().size()>0) this.getMediaPushDel().addAll(from.getMediaPushDel());
		
		if(this.getPublishStreamSet() == null) this.setPublishStreamSet(new ArrayList<PublishStreamSetBO>());
		if(from.getPublishStreamSet()!=null && from.getPublishStreamSet().size()>0) this.getPublishStreamSet().addAll(from.getPublishStreamSet());
		
		if(this.getIncoming_call_request() == null) this.setIncoming_call_request(new ArrayList<CallBO>());
		if(from.getIncoming_call_request()!=null && from.getIncoming_call_request().size()>0) this.getIncoming_call_request().addAll(from.getIncoming_call_request());
		
		if(this.getHang_up_request() == null) this.setHang_up_request(new ArrayList<HangUpBO>());
		if(from.getHang_up_request()!=null && from.getHang_up_request().size()>0) this.getHang_up_request().addAll(from.getHang_up_request());
		
		if(this.getJv230ForwardSet() == null) this.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		if(from.getJv230ForwardSet()!=null && from.getJv230ForwardSet().size()>0) this.getJv230ForwardSet().addAll(from.getJv230ForwardSet());
		
		if(this.getJv230AudioSet() == null) this.setJv230AudioSet(new ArrayList<Jv230ForwardBO>());
		if(from.getJv230AudioSet()!=null && from.getJv230AudioSet().size()>0) this.getJv230AudioSet().addAll(from.getJv230AudioSet());
		
		if(this.getJv230ForwardDel() == null) this.setJv230ForwardDel(new ArrayList<SourceBO>());
		if(from.getJv230ForwardDel()!=null && from.getJv230ForwardDel().size()>0) this.getJv230ForwardDel().addAll(from.getJv230ForwardDel());
		
		if(this.getPass_by() == null) this.setPass_by(new ArrayList<PassByBO>());
		if(from.getPass_by()!= null && from.getPass_by().size()>0) this.getPass_by().addAll(from.getPass_by());
		
		return this;
	}
	
	/**
	 * @Title: 过滤无效的协议<br/> 
	 * @Description: 1.处理合屏协议<br/>
	 * 					a.如果group中最终的数据中不存在的合屏，去除掉合屏的set和update协议<br/>
	 * 					//b.如果group中最终的数据中存在的合屏，去除掉合屏的delete协议--布局修改的合屏会出现这种情况<br/>
	 * 					c.无用的set协议中，去除掉合屏的delete协议<br/>
	 * @param group 设备组
	 * @return LogicBO 协议数据
	 */
	public LogicBO doFilter(DeviceGroupPO group){
		//处理合屏协议
		Set<CombineVideoPO> combineVideos = group.getCombineVideos();
		
		//group中存在且combineVideoDelete中存在的合屏
		List<CombineVideoBO> combineVideoDelete = this.getCombineVideoDel();
		/*if(combineVideoDelete!=null && combineVideoDelete.size()>0){
			List<CombineVideoBO> needDeleteVideoDelete = new ArrayList<CombineVideoBO>();
			for(CombineVideoBO scopeCombineVideoDelete:combineVideoDelete){
				boolean finded = false;
				for(CombineVideoPO scopeCombineVideo:combineVideos){
					if(scopeCombineVideoDelete.getUuid().equals(scopeCombineVideo.getUuid())){
						finded = true;
						break;
					}
				}
				if(finded){
					needDeleteVideoDelete.add(scopeCombineVideoDelete);
				}
			}
			if(needDeleteVideoDelete.size() > 0){
				this.getCombineVideoDel().removeAll(needDeleteVideoDelete);
			}
		}*/
		
		//group中不存在而combineVideoSet存在的合屏
		List<CombineVideoBO> combineVideoSet = this.getCombineVideoSet();
		if(combineVideoSet!=null && combineVideoSet.size()>0){
			List<CombineVideoBO> needDeleteVideoSet = new ArrayList<CombineVideoBO>();
			for(CombineVideoBO scopeCombineVideoSet:combineVideoSet){
				boolean finded = false;
				for(CombineVideoPO scopeCombineVideo:combineVideos){
					if(scopeCombineVideoSet.getUuid().equals(scopeCombineVideo.getUuid())){
						finded = true;
						break;
					}
				}
				if(!finded){
					needDeleteVideoSet.add(scopeCombineVideoSet);
				}
			}
			if(needDeleteVideoSet.size() > 0){
				//无用的set协议中，去除掉delete协议
				this.getCombineVideoSet().removeAll(needDeleteVideoSet);
				combineVideoDelete = this.getCombineVideoDel();
				if(combineVideoDelete.size() > 0){
					List<CombineVideoBO> needDeleteVideoDelete = new ArrayList<CombineVideoBO>();
					
					for(CombineVideoBO scopeCombineVideoDelete:combineVideoDelete){
						boolean finded = false;
						for(CombineVideoBO scopeCombineVideoSet:needDeleteVideoSet){
							if(scopeCombineVideoSet.getUuid().equals(scopeCombineVideoDelete.getUuid())){
								finded = true;
								break;
							}
						}
						if(finded){
							needDeleteVideoDelete.add(scopeCombineVideoDelete);
						}
					}
					if(needDeleteVideoDelete.size() > 0){
						this.getCombineVideoDel().removeAll(needDeleteVideoDelete);
					}
				}
			}
		}
		
		//group中不存在而combineVideoUpdate存在的合屏
		List<CombineVideoBO> combineVideoUpdate = this.getCombineVideoUpdate();
		if(combineVideoUpdate!=null && combineVideoUpdate.size()>0){
			List<CombineVideoBO> needDeleteVideoUpdate = new ArrayList<CombineVideoBO>();
			for(CombineVideoBO scopeCombineVideoUpdate:combineVideoUpdate){
				boolean finded = false;
				for(CombineVideoPO scopeCombineVideo:combineVideos){
					if(scopeCombineVideoUpdate.getUuid().equals(scopeCombineVideo.getUuid())){
						finded = true;
						break;
					}
				}
				if(!finded){
					needDeleteVideoUpdate.add(scopeCombineVideoUpdate);
				}
			}
			if(needDeleteVideoUpdate.size() > 0){
				this.getCombineVideoUpdate().removeAll(needDeleteVideoUpdate);
			}
		}
		
		return this;
	}
	public LogicBO doFilter(CommonGroupPO group){
		//处理合屏协议
		Set<CommonCombineVideoPO> combineVideos = group.getCombineVideos();
		
		//group中存在且combineVideoDelete中存在的合屏
		List<CombineVideoBO> combineVideoDelete = this.getCombineVideoDel();
		/*if(combineVideoDelete!=null && combineVideoDelete.size()>0){
			List<CombineVideoBO> needDeleteVideoDelete = new ArrayList<CombineVideoBO>();
			for(CombineVideoBO scopeCombineVideoDelete:combineVideoDelete){
				boolean finded = false;
				for(CombineVideoPO scopeCombineVideo:combineVideos){
					if(scopeCombineVideoDelete.getUuid().equals(scopeCombineVideo.getUuid())){
						finded = true;
						break;
					}
				}
				if(finded){
					needDeleteVideoDelete.add(scopeCombineVideoDelete);
				}
			}
			if(needDeleteVideoDelete.size() > 0){
				this.getCombineVideoDel().removeAll(needDeleteVideoDelete);
			}
		}*/
		
		//group中不存在而combineVideoSet存在的合屏
		List<CombineVideoBO> combineVideoSet = this.getCombineVideoSet();
		if(combineVideoSet!=null && combineVideoSet.size()>0){
			List<CombineVideoBO> needDeleteVideoSet = new ArrayList<CombineVideoBO>();
			for(CombineVideoBO scopeCombineVideoSet:combineVideoSet){
				boolean finded = false;
				for(CommonCombineVideoPO scopeCombineVideo:combineVideos){
					if(scopeCombineVideoSet.getUuid().equals(scopeCombineVideo.getUuid())){
						finded = true;
						break;
					}
				}
				if(!finded){
					needDeleteVideoSet.add(scopeCombineVideoSet);
				}
			}
			if(needDeleteVideoSet.size() > 0){
				//无用的set协议中，去除掉delete协议
				this.getCombineVideoSet().removeAll(needDeleteVideoSet);
				combineVideoDelete = this.getCombineVideoDel();
				if(combineVideoDelete.size() > 0){
					List<CombineVideoBO> needDeleteVideoDelete = new ArrayList<CombineVideoBO>();
					
					for(CombineVideoBO scopeCombineVideoDelete:combineVideoDelete){
						boolean finded = false;
						for(CombineVideoBO scopeCombineVideoSet:needDeleteVideoSet){
							if(scopeCombineVideoSet.getUuid().equals(scopeCombineVideoDelete.getUuid())){
								finded = true;
								break;
							}
						}
						if(finded){
							needDeleteVideoDelete.add(scopeCombineVideoDelete);
						}
					}
					if(needDeleteVideoDelete.size() > 0){
						this.getCombineVideoDel().removeAll(needDeleteVideoDelete);
					}
				}
			}
		}
		
		//group中不存在而combineVideoUpdate存在的合屏
		List<CombineVideoBO> combineVideoUpdate = this.getCombineVideoUpdate();
		if(combineVideoUpdate!=null && combineVideoUpdate.size()>0){
			List<CombineVideoBO> needDeleteVideoUpdate = new ArrayList<CombineVideoBO>();
			for(CombineVideoBO scopeCombineVideoUpdate:combineVideoUpdate){
				boolean finded = false;
				for(CommonCombineVideoPO scopeCombineVideo:combineVideos){
					if(scopeCombineVideoUpdate.getUuid().equals(scopeCombineVideo.getUuid())){
						finded = true;
						break;
					}
				}
				if(!finded){
					needDeleteVideoUpdate.add(scopeCombineVideoUpdate);
				}
			}
			if(needDeleteVideoUpdate.size() > 0){
				this.getCombineVideoUpdate().removeAll(needDeleteVideoUpdate);
			}
		}
		
		return this;
	}
	
}
