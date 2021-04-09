package com.sumavision.bvc.device.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DeviceGroupAuthorizationPO.class, idClass = long.class)
public interface DeviceGroupAuthorizationDAO extends MetBaseDAO<DeviceGroupAuthorizationPO>{
			
	public DeviceGroupAuthorizationPO findByGroupUuid(String groupUuid);
	
	@Query(value="SELECT live.cid FROM bvc_device_group_authorization_member member INNER JOIN bvc_device_group_authorization auth ON member.authorization_id=auth.id INNER JOIN bvc_record_live_channel live ON live.authorization_id=auth.id WHERE member.bundle_id=?1", nativeQuery=true)
	public List<String> findAuthorizationCidsByBundleId(String bundleId);
	
	@Query(value="SELECT asset.pid FROM bvc_device_group_authorization_member member INNER JOIN bvc_device_group_authorization auth ON member.authorization_id=auth.id INNER JOIN bvc_record_asset asset ON asset.authorization_id=auth.id WHERE member.bundle_id=?1", nativeQuery=true)
	public List<String> findAuthorizationPidsByBundleId(String bundleId);
}
