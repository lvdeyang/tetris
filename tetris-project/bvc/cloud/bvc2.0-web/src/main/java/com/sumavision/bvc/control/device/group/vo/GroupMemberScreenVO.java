package com.sumavision.bvc.control.device.group.vo;

import java.util.Comparator;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class GroupMemberScreenVO extends AbstractBaseVO<GroupMemberScreenVO, DeviceGroupMemberScreenPO>{

	private Long id;
	
	/** 屏幕别名 */
	private String name;
	
	/** 来自于资源层屏幕id */
	private String screenId;
	
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

	public GroupMemberScreenVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public GroupMemberScreenVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getScreenId() {
		return screenId;
	}

	public GroupMemberScreenVO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public GroupMemberScreenVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public GroupMemberScreenVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public GroupMemberScreenVO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getVenusBundleType() {
		return venusBundleType;
	}

	public GroupMemberScreenVO setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public GroupMemberScreenVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public GroupMemberScreenVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	@Override
	public GroupMemberScreenVO set(DeviceGroupMemberScreenPO entity) throws Exception {
		this.setId(entity.getId())
			.setName(entity.getName())
			.setScreenId(entity.getScreenId())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getMember().getBundleName())
			.setBundleType(entity.getMember().getBundleType())
			.setVenusBundleType(entity.getMember().getVenusBundleType())
			.setMemberId(entity.getMember().getId())
			.setLayerId(entity.getMember().getLayerId());

		return this;
	}
	
	/**
	 * @ClassName: screen排序 
	 * @author wjw
	 * @date 2018年11月12日 上午11:25:40
	 */
	public static final class ScreenComparetor implements Comparator<GroupMemberScreenVO>{

		@Override
		public int compare(GroupMemberScreenVO o1, GroupMemberScreenVO o2) {

			String str1 = o1.getBundleName();
			String str2 = o2.getBundleName();
			
			String str3 = o1.getName();
			String str4 = o2.getName();
			
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
