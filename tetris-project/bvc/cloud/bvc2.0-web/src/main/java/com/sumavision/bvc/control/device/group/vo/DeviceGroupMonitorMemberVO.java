package com.sumavision.bvc.control.device.group.vo;

import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.RecordStatus;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupMonitorMemberVO extends AbstractBaseVO<DeviceGroupMonitorMemberVO, DeviceGroupMemberDTO> {
	
	/** 设备id */
	private String bundleId;

	/** 设备名称 */
	private String bundleName;
	
	/** 执行层id, 对应资源层nodeUid*/
	private String layerId;
	
	/** 所属文件夹id*/
	private Long folderId;
	
	/** 设备分类这里面还没有定 */
	
	/** 所属业务角色id（这里是会议关联的业务角色，并不是系统级的业务角色） */
	private Long roleId;
	
	/** 所属业务角色名称（这里是会议关联的业务角色，并不是系统级的业务角色） */
	private String roleName;
	
	/** 是否开启音频 */
	private boolean openAudio;
	
	/** 设备录制状态  run|stop*/
	private String recordStatus;

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupMonitorMemberVO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupMonitorMemberVO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupMonitorMemberVO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public DeviceGroupMonitorMemberVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupMonitorMemberVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupMonitorMemberVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public boolean isOpenAudio() {
		return openAudio;
	}

	public DeviceGroupMonitorMemberVO setOpenAudio(boolean openAudio) {
		this.openAudio = openAudio;
		return this;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public DeviceGroupMonitorMemberVO setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
		return this;
	}

	@Override
	public DeviceGroupMonitorMemberVO set(DeviceGroupMemberDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setBundleId(entity.getBundleId())
			.setBundleName(entity.getBundleName())
			.setLayerId(entity.getLayerId())
			.setFolderId(entity.getFolderId())
			.setRoleId(entity.getRoleId())
			.setRoleName(entity.getRoleName())
			.setOpenAudio(entity.isOpenAudio())
			.setRecordStatus(RecordStatus.STOP.getName());
		
		return this;
	}
}