package com.sumavision.tetris.device.group;


import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryDefinition(domainClass = DeviceGroupPO.class, idClass = Long.class)
public interface DeviceGroupDao extends BaseDAO<DeviceGroupPO> {

    DeviceGroupPO findTopByName(String name);

    DeviceGroupPO findByNameAndIdNot(String name, Long id);

    @Query(value = "select id from DeviceGroupPO where 1=1")
    List<Long> findAllIds();

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
    @Query(value = "update DeviceGroupPO d set d.dataNetIds = ?2 where d.id = ?1")
    public Integer updateDataNetIdsById(Long id,String dataNetIds);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
    @Query(value = "update DeviceGroupPO d set d.autoBackupFlag = ?2 where d.id = ?1")
    public Integer updateAutoBackupFlagById(Long id,Boolean checked);

}
