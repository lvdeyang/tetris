package com.sumavision.bvc.control.device.group.vo;

import java.util.Comparator;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSrcDTO;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupSchemeVideoSrcVO extends AbstractBaseVO<DeviceGroupSchemeVideoSrcVO, DeviceGroupConfigVideoSrcDTO>{

	private int serialnum;
	
	private String type;
	
	private Long roleId;
	
	private String roleName;
	
	private String roleChannelType;
	
	private String pictureType;
	
	private String pollingTime;
	
	private String pollingStatus;
	
	private Long memberId;
	
	private Long memberChannelId;
	
	private String memberChannelName;
	
	private String layerId;
	
	private String channelId;
	
	private String channelName;
	
	private String bundleId;
	
	private String bundleName;
	
	private String virtualUuid;
	
	private String virtualName;
	
	public int getSerialnum() {
		return serialnum;
	}

	public DeviceGroupSchemeVideoSrcVO setSerialnum(int serialnum) {
		this.serialnum = serialnum;
		return this;
	}

	public String getType() {
		return type;
	}

	public DeviceGroupSchemeVideoSrcVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupSchemeVideoSrcVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupSchemeVideoSrcVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public String getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupSchemeVideoSrcVO setRoleChannelType(String roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public String getPictureType() {
		return pictureType;
	}

	public DeviceGroupSchemeVideoSrcVO setPictureType(String pictureType) {
		this.pictureType = pictureType;
		return this;
	}

	public String getPollingTime() {
		return pollingTime;
	}

	public DeviceGroupSchemeVideoSrcVO setPollingTime(String pollingTime) {
		this.pollingTime = pollingTime;
		return this;
	}

	public String getPollingStatus() {
		return pollingStatus;
	}

	public DeviceGroupSchemeVideoSrcVO setPollingStatus(String pollingStatus) {
		this.pollingStatus = pollingStatus;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupSchemeVideoSrcVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupSchemeVideoSrcVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public DeviceGroupSchemeVideoSrcVO setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupSchemeVideoSrcVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupSchemeVideoSrcVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupSchemeVideoSrcVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupSchemeVideoSrcVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupSchemeVideoSrcVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getVirtualUuid() {
		return virtualUuid;
	}

	public DeviceGroupSchemeVideoSrcVO setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
		return this;
	}

	public String getVirtualName() {
		return virtualName;
	}

	public DeviceGroupSchemeVideoSrcVO setVirtualName(String virtualName) {
		this.virtualName = virtualName;
		return this;
	}

	@Override
	public DeviceGroupSchemeVideoSrcVO set(DeviceGroupConfigVideoSrcDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSerialnum(entity.getSerialnum())
			.setType(entity.getType()==null?ForwardSrcType.CHANNEL.toString():entity.getType().toString())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setRoleChannelType(entity.getRoleChannelType()==null?"":entity.getRoleChannelType().toString())
			.setPictureType(entity.getPictureType().toString())
			.setPollingTime(entity.getPollingTime())
			.setPollingStatus(entity.getPollingStatus()==null?null:entity.getPollingStatus().toString())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setMemberChannelName(entity.getMemberChannelName())
			.setLayerId(entity.getLayerId())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setVirtualUuid(entity.getVirtualUuid())
			.setVirtualName(entity.getVirtualName());
		return this;
	}
	
	public DeviceGroupSchemeVideoSrcVO set(
			DeviceGroupConfigVideoSrcPO entity, 
			int serialnum,
			PictureType pictureType,
			String pollingTime,
			PollingStatus pollingStatus) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setSerialnum(serialnum)
			.setType(entity.getType()==null?ForwardSrcType.CHANNEL.toString():entity.getType().toString())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setRoleChannelType(entity.getRoleChannelType()==null?"":entity.getRoleChannelType().toString())
			.setPictureType(pictureType.toString())
			.setPollingTime(pollingTime)
			.setPollingStatus(pollingStatus==null?null:pollingStatus.toString())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setMemberChannelName(entity.getMemberChannelName())
			.setLayerId(entity.getLayerId())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setVirtualUuid(entity.getVirtualUuid())
			.setVirtualName(entity.getVirtualName());
		return this;
	}
	
	/**
	 * @ClassName: 视频源排序器<br/>
	 * @Description: 按照序号从小到大排序<br/>
	 * @author lvdeyang
	 * @date 2018年8月5日 下午2:13:24 
	 */
	public static final class SerialNumAscSorter implements Comparator<DeviceGroupSchemeVideoSrcVO>{
		@Override
		public int compare(DeviceGroupSchemeVideoSrcVO o1, DeviceGroupSchemeVideoSrcVO o2) {
			if(o1.getSerialnum() < o2.getSerialnum()){
				return 1;
			}
			if(o1.getSerialnum() == o2.getSerialnum()){
				return 0;
			}
			return -1;
		}
	}
}
