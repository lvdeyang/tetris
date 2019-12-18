package com.sumavision.tetris.sts.device;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;

import java.util.List;

@RepositoryDefinition(domainClass = DeviceGroupPO.class, idClass = Long.class)
public interface DeviceGroupDao extends CommonDao<DeviceGroupPO>{

//    List<DeviceGroupPO> findByType(GroupType type);

//    List<DeviceGroupPO> findByTypeNot(GroupType type);
    
    List<DeviceGroupPO> findByDataNetIdsNotNull();

    DeviceGroupPO findTopByName(String name);

    DeviceGroupPO findByNameAndIdNot(String name , Long id);
    
//    DeviceGroupPO findByNameAndTypeAndIdNot(String name , GroupType type , Long id);

//    List<DeviceGroupPO> findByGroupId(Long groupId);
    
    @Query(value = "select id from DeviceGroupPO where 1=1")
    List<Long> findAllIds();
//    @Query(value = "select id from DeviceGroupPO where type = ?1")
//    List<Long> getIdByType(GroupType groupType);
}
