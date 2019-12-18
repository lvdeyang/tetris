package com.sumavision.tetris.sts.device;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;

import java.util.List;

@RepositoryDefinition(domainClass = DeviceNodePO.class, idClass = Long.class)
public interface DeviceNodeDao extends CommonDao<DeviceNodePO>{

    DeviceNodePO findByName(String name);

    DeviceNodePO findByDeviceIp(String deviceIp);


    List<DeviceNodePO> findByIdIn(List<Long> ids);

    List<DeviceNodePO> findByDeviceGroupId(Long deviceGroupId);

    List<DeviceNodePO> findByDeviceId(Long deviceId);

    List<DeviceNodePO> findByDeviceGroupIdAndDeviceId(Long deviceGroupId , Long deviceId);


    @Query(value = "select id from DeviceNodePO where 1=1")
    List<Long> findAllIds();


    @Query(value = "select count(d) from DeviceNodePO d where d.deviceGroupId=?1")
    Integer findCountByDeviceGroupId(Long id);
    
	
	 /**
     * 根据通道授权po的id查询出所属deviceInfoPO
     * @param deviceChannelAuthId
     * @return
     */
    @Query(value = "select distinct d from DeviceNodePO d left join d.deviceChannelAuthPOs s where s.id = ?1")
    public DeviceNodePO findByDeviceChannelAuthId(Long deviceChannelAuthId);
	 
    
}
