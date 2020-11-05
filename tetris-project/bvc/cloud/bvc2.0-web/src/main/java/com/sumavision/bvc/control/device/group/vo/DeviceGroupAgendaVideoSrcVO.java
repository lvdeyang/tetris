package com.sumavision.bvc.control.device.group.vo;

import java.util.Comparator;

import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSrcDTO;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.PollingSourceVisible;
import com.sumavision.bvc.device.group.enumeration.PollingStatus;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupAgendaVideoSrcVO extends AbstractBaseVO<DeviceGroupAgendaVideoSrcVO, DeviceGroupConfigVideoSrcDTO>{

	private int serialnum;
	
	private String type;
	
	private Long roleId;
	
	private String roleName;
	
	private String roleChannelType;
	
	private String pictureType;
	
	private String pollingTime;
	
	private String pollingStatus;
	
	private String visible;
	
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

	public DeviceGroupAgendaVideoSrcVO setSerialnum(int serialnum) {
		this.serialnum = serialnum;
		return this;
	}

	public String getType() {
		return type;
	}

	public DeviceGroupAgendaVideoSrcVO setType(String type) {
		this.type = type;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupAgendaVideoSrcVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupAgendaVideoSrcVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public String getRoleChannelType() {
		return roleChannelType;
	}

	public DeviceGroupAgendaVideoSrcVO setRoleChannelType(String roleChannelType) {
		this.roleChannelType = roleChannelType;
		return this;
	}

	public String getPictureType() {
		return pictureType;
	}

	public DeviceGroupAgendaVideoSrcVO setPictureType(String pictureType) {
		this.pictureType = pictureType;
		return this;
	}

	public String getPollingTime() {
		return pollingTime;
	}

	public DeviceGroupAgendaVideoSrcVO setPollingTime(String pollingTime) {
		this.pollingTime = pollingTime;
		return this;
	}

	public String getPollingStatus() {
		return pollingStatus;
	}

	public DeviceGroupAgendaVideoSrcVO setPollingStatus(String pollingStatus) {
		this.pollingStatus = pollingStatus;
		return this;
	}
	
	public String getVisible() {
		return visible;
	}

	public DeviceGroupAgendaVideoSrcVO setVisible(String visible) {
		this.visible = visible;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public DeviceGroupAgendaVideoSrcVO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public Long getMemberChannelId() {
		return memberChannelId;
	}

	public DeviceGroupAgendaVideoSrcVO setMemberChannelId(Long memberChannelId) {
		this.memberChannelId = memberChannelId;
		return this;
	}
	
	public String getMemberChannelName() {
		return memberChannelName;
	}

	public DeviceGroupAgendaVideoSrcVO setMemberChannelName(String memberChannelName) {
		this.memberChannelName = memberChannelName;
		return this;
	}
	
	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupAgendaVideoSrcVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public DeviceGroupAgendaVideoSrcVO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public DeviceGroupAgendaVideoSrcVO setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupAgendaVideoSrcVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupAgendaVideoSrcVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getVirtualUuid() {
		return virtualUuid;
	}

	public DeviceGroupAgendaVideoSrcVO setVirtualUuid(String virtualUuid) {
		this.virtualUuid = virtualUuid;
		return this;
	}

	public String getVirtualName() {
		return virtualName;
	}

	public DeviceGroupAgendaVideoSrcVO setVirtualName(String virtualName) {
		this.virtualName = virtualName;
		return this;
	}

	@Override
	public DeviceGroupAgendaVideoSrcVO set(DeviceGroupConfigVideoSrcDTO entity) throws Exception {
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
			.setVisible(entity.getVisible()==null?PollingSourceVisible.VISIBLE.toString():entity.getVisible().toString())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setLayerId(entity.getLayerId())
			.setMemberChannelName(entity.getMemberChannelName())
			.setChannelId(entity.getChannelId())
			.setChannelName(entity.getChannelName())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setVirtualUuid(entity.getVirtualUuid())
			.setVirtualName(entity.getVirtualName());
		return this;
	}
	
	public DeviceGroupAgendaVideoSrcVO set(
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
			.setVisible(entity.getVisible()==null?PollingSourceVisible.VISIBLE.toString():entity.getVisible().toString())
			.setLayerId(entity.getLayerId())
			.setMemberId(entity.getMemberId())
			.setMemberChannelId(entity.getMemberChannelId())
			.setMemberChannelName(entity.getMemberChannelName())
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
	public static final class SerialNumAscSorter implements Comparator<DeviceGroupAgendaVideoSrcVO>{
		@Override
		public int compare(DeviceGroupAgendaVideoSrcVO o1, DeviceGroupAgendaVideoSrcVO o2) {
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
