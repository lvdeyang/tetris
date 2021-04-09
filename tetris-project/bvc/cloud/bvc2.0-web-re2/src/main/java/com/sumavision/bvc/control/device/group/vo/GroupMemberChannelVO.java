package com.sumavision.bvc.control.device.group.vo;

import java.util.Comparator;

import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class GroupMemberChannelVO extends AbstractBaseVO<GroupMemberChannelVO, DeviceGroupMemberChannelPO>{

	private Long id;
	
	/** 通道别名 */
	private String name;
	
	/** 业务层的通道类型，这个类型与资源层的通道类型一一对应 */
	private ChannelType type;
	
	/** 来自于资源层通道id */
	private String channelId;
	
	/** 来自于资源层通道名称 */
	private String channelName;
	
	/** 来自于资源层通道类型 */
	private String channelType;
	
	/** 来自于资源层设备id */
	private String bundleId;
	
	/** 来自于资源层设备名称 */
	private String bundleName;
	
	/** 来自于资源层的设备类型 */
	private String bundleType;
	
	/** 来自于资源层的资源设备类型*/
	private String venusBundleType;
	
	/** 关联设备组成员 */
	private Long memberId;
	
	/**执行层id*/
	private String layerId;
	
	public Long getId() {
		return id;
	}

	public GroupMemberChannelVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public GroupMemberChannelVO setName(String name) {
		this.name = name;
		return this;
	}

	public ChannelType getType() {
		return type;
	}

	public GroupMemberChannelVO setType(ChannelType type) {
		this.type = type;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public GroupMemberChannelVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public GroupMemberChannelVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public GroupMemberChannelVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getChannelType() {
		return channelType;
	}

	public GroupMemberChannelVO setChannelType(String channelType) {
		this.channelType = channelType;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public GroupMemberChannelVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public GroupMemberChannelVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public GroupMemberChannelVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getVenusBundleType() {
		return venusBundleType;
	}

	public GroupMemberChannelVO setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public GroupMemberChannelVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	@Override
	public GroupMemberChannelVO set(DeviceGroupMemberChannelPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setLayerId(entity.getMember().getLayerId())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setChannelType(entity.getChannelType())
			.setBundleType(entity.getBundleType())
			.setType(entity.getType())
			.setMemberId(entity.getMember().getId())
			.setLayerId(entity.getMember().getLayerId());
		
		return this;
	}
	
	/**
	 * @ClassName: channel排序 
	 * @author wjw
	 * @date 2018年9月25日 下午5:51:40
	 */
	public static final class ChannelComparetor implements Comparator<GroupMemberChannelVO>{

		@Override
		public int compare(GroupMemberChannelVO o1, GroupMemberChannelVO o2) {

			String str1 = o1.getBundleName();
			String str2 = o2.getBundleName();
			
			String str3 = o1.getChannelName();
			String str4 = o2.getChannelName();
			
			if(str1.compareTo(str2) < 0){
				return -1;
			}
			
			if(str1.compareTo(str2) > 0){
				return 1;
			}
			
			if(str1.compareTo(str2) == 0){
				
				if(str3.compareTo(str4) < 0){
					return -1;
				}
				
				if(str3.compareTo(str4) > 0){
					return 1;
				}
				
			}
			
			return 0;
		}
		
	}
}
