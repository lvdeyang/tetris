package com.sumavision.tetris.sts.device.group;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.sts.common.CommonDao;

import java.util.List;

@RepositoryDefinition(domainClass = DeviceGroupPO.class, idClass = Long.class)
public interface DeviceGroupDao extends CommonDao<DeviceGroupPO>{

    
    List<DeviceGroupPO> findByDataNetIdsNotNull();

    DeviceGroupPO findTopByName(String name);

    DeviceGroupPO findByNameAndIdNot(String name , Long id);
    
    @Query(value = "select id from DeviceGroupPO where 1=1")
    List<Long> findAllIds();
}
