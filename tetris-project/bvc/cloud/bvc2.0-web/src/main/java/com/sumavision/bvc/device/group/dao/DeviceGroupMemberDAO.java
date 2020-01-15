package com.sumavision.bvc.device.group.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupMemberPO.class, idClass = long.class)
public interface DeviceGroupMemberDAO extends MetBaseDAO<DeviceGroupMemberPO>{
	
	/**
	* @Title: 查询设备组成员
	* @param @param groupId 设备组id
	* @param @return    设定文件 
	* @return Set<DeviceGroupMemberDTO>  成员列表
	 */
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO(m.id, m.uuid, m.updateTime, m.bundleId, m.bundleName, m.bundleType, m.venusBundleType, m.layerId, m.folderId, m.roleId, m.roleName, m.openAudio) from DeviceGroupMemberPO m where m.group.id=?1 order by m.bundleName")
	public List<DeviceGroupMemberDTO> findGroupMembersByGroupId(Long groupId);
	
	@Query("select new com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO(m.id, m.uuid, m.updateTime, m.bundleId, m.bundleName, m.bundleType, m.venusBundleType, m.layerId, m.folderId, m.roleId, m.roleName, m.openAudio) from DeviceGroupMemberPO m where m.group.id=?1 and (m.bundleType='jv210' or m.bundleType='ipc' or m.bundleType='proxy') order by m.bundleName")
	public Page<DeviceGroupMemberDTO> findGroupRecordMembersByGroupId(Long groupId, Pageable page);
	
	public List<DeviceGroupMemberPO> findByRoleIdIn(Set<Long> roleIds);
	
	public List<DeviceGroupMemberPO> findByGroupIdAndBundleIdIn(Long groupId, Set<String> bundleIds);

	@Query("select m.group.id from DeviceGroupMemberPO m where m.bundleId=?1")
	public List<Long> findGroupIdByBundleId(String bundleId);
	
	public List<DeviceGroupMemberPO> findByBundleId(String bundleId);

	public DeviceGroupMemberPO findByGroupIdAndBundleId(Long groupId, String bundleId);	

	//进行中的会议（不含监控室）的成员总数
	@Query(value="SELECT COUNT(*) FROM bvc_device_group_member member INNER JOIN bvc_device_group _group ON member.group_id=_group.id WHERE (_group.status='START' AND _group.type!='MONITOR')", nativeQuery=true)
	public int getNumberOfMembersOfRunningMeeting();
}
