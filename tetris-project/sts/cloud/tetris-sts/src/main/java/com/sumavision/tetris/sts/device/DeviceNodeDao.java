package com.sumavision.tetris.sts.device;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonConstants.BackType;
import com.sumavision.tetris.sts.common.CommonConstants.FunUnitStatus;
import com.sumavision.tetris.sts.common.CommonDao;

import java.util.List;

@RepositoryDefinition(domainClass = DeviceNodePO.class, idClass = Long.class)
public interface DeviceNodeDao extends CommonDao<DeviceNodePO>{

    DeviceNodePO findByName(String name);

    DeviceNodePO findByDeviceIp(String deviceIp);

    @Query(value = "select distinct d from DeviceNodePO d where d.encapsulateSocketAddress=?1 or d.transSocketAddress=?1")
    DeviceNodePO findBySocketAddress(String socketAddress);

    List<DeviceNodePO> findByIdIn(List<Long> ids);

    List<DeviceNodePO> findByDeviceGroupId(Long deviceGroupId);

    List<DeviceNodePO> findByDeviceId(Long deviceId);

    List<DeviceNodePO> findByDeviceIdAndTransBackType(Long deviceId , BackType backType);
    
    List<DeviceNodePO> findByDeviceIdAndTransStatusAndTransBackType(Long deviceId , FunUnitStatus funUnitStatus, BackType backType);

    List<DeviceNodePO> findByDeviceGroupIdAndDeviceId(Long deviceGroupId , Long deviceId);

    List<DeviceNodePO> findByDeviceGroupIdAndTransBackType(Long deviceGroupId , BackType transBackType);

    List<DeviceNodePO> findByTransStatusAndTransBackType(FunUnitStatus funUnitStatus, BackType backType);

//    @Query(value = "select devno from DeviceNodePO devno join DevicePO dev on devno.deviceId = dev.id where " +
//            "dev.backType = 'MAIN' and devno.transStatus = ?1 and devno.transBackType = ?2")
//    List<DeviceNodePO> findByTransStatusAndTransBackTypeInMainDevice(FunUnitStatus funUnitStatus, BackType backType);

    List<DeviceNodePO> findByEncapsulateStatusAndEncapsulateBackType(FunUnitStatus funUnitStatus, BackType backType);

//    @Query(value = "select devno from DeviceNodePO devno join DevicePO dev on devno.deviceId = dev.id where " +
//            "dev.backType = 'MAIN' and devno.encapsulateStatus = ?1 and devno.encapsulateBackType = ?2")
//    List<DeviceNodePO> findByEncapsulateStatusAndEncapsulateBackTypeInMainDevice(FunUnitStatus funUnitStatus, BackType backType);

    List<DeviceNodePO> findByDeviceIdAndEncapsulateStatusAndTransStatus(Long deviceId, FunUnitStatus encapsulateStatus, FunUnitStatus transStatus);

    List<DeviceNodePO> findByDeviceIdAndTransStatus(Long deviceId, FunUnitStatus transStatus);

    List<DeviceNodePO> findByDeviceIdAndEncapsulateStatus(Long deviceId, FunUnitStatus encapsulateStatus);

    List<DeviceNodePO> findByDeviceGroupIdAndTransStatusAndTransBackType(Long deviceGroupId , FunUnitStatus funUnitStatus, BackType backType);

    List<DeviceNodePO> findByDeviceGroupIdAndEncapsulateStatusAndEncapsulateBackType(Long deviceGroupId , FunUnitStatus funUnitStatus, BackType backType);
    
    @Query(value = "select id from DeviceNodePO where 1=1")
    List<Long> findAllIds();

    @Query(value = "select d from DeviceNodePO d where d.deviceId=?1 and d.encapsulateBackType=?2 " +
            "and d.backIndex=?3 and d.encapsulateStatus='NORMAL'")
    List<DeviceNodePO> findBackEncapsulateDevice(Long deviceId , BackType encapsulateBackType , Integer backIndex);

    List<DeviceNodePO> findByDeviceIdAndEncapsulateStatusAndEncapsulateBackType(Long deviceId , FunUnitStatus funUnitStatus , BackType backType);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and n.status=1")
    List<DeviceNodePO> findEncapsulate(String sourceIp);

    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and d.deviceId=?2 and n.status=1")
    List<DeviceNodePO> findEncapsulate(String sourceIp , Long deviceId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n join d.encapsulateAuthPO a where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and d.deviceId=?2 and n.status=1 order by a.outputRestNum")
    List<DeviceNodePO> findEncapsulateInDeviceBySourceIp(String sourceIp , Long deviceId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n join d.encapsulateAuthPO a where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and d.deviceId=?2 and n.status=1 order by a.outputRestNum")
    List<DeviceNodePO> findEncapsulateInDeviceByNetGroup(Long netGroupId , Long deviceId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n join d.encapsulateAuthPO a where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id " +
            "and d.deviceId=?1 and n.status=1 order by a.outputRestNum")
    List<DeviceNodePO> findEncapsulateInDevice(Long deviceId);

    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n join d.encapsulateAuthPO a where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and d.deviceGroupId=?2 and n.status=1 order by a.outputRestNum")
    List<DeviceNodePO> findEncapsulateInGroupBySourceIp(String sourceIp , Long deviceGroupId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n join d.encapsulateAuthPO a where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and d.deviceGroupId=?2 and n.status=1 order by a.outputRestNum")
    List<DeviceNodePO> findEncapsulateInGroupByNetGroup(Long netGroupId , Long deviceGroupId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n join d.encapsulateAuthPO a where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id " +
            "and d.deviceGroupId=?1 and n.status=1 order by a.outputRestNum")
    List<DeviceNodePO> findEncapsulateInGroup(Long deviceGroupId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and d.deviceId=?2 and n.status=1")
    List<DeviceNodePO> findEncapsulate(Long netGroupId , Long deviceId);

    @Query(value = "select d , n from DeviceNodePO d , NetCardInfoPO n where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and n.status=1")
    Object[] findEncapsulateAndNetCardByNetGroupId(Long netGroupId);
    
    @Query(value = "select d , n from DeviceNodePO d , NetCardInfoPO n where " +
            "d.transStatus='NORMAL' and d.transBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and n.status=1")
    Object[] findTransAndNetCardByNetGroupId(Long netGroupId);

    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.transStatus='NORMAL' and d.transBackType='MAIN' and n.deviceId=d.id and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and n.status=1")
    List<DeviceNodePO> findTrans(String sourceIp);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.transStatus='NORMAL' and d.transBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and n.status=1")
    List<DeviceNodePO> findTransByNetGroup(Long groupId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.transStatus='NORMAL' and d.transBackType='MAIN' and n.deviceId=d.id and d.deviceGroupId=?2 and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and n.status=1")
    List<DeviceNodePO> findTransAndNetCard(String sourceIp, Long deviceGroupId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and d.deviceGroupId=?2 and (n.ipv4=?1 or n.virtualIpv4=?1) " +
            "and n.status=1")
    List<DeviceNodePO> findEncapsulateStatusAndsdmGroupId(String sourceIp, Long sdmGroupId);
    
    @Query(value = "select d from DeviceNodePO d , NetCardInfoPO n where " +
            "d.encapsulateStatus='NORMAL' and d.encapsulateBackType='MAIN' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and n.status=1")
    List<DeviceNodePO> findEncapsulateByNetGroupId(Long netGroupId);

    @Query(value = "select d , n from DeviceNodePO d , NetCardInfoPO n where " +
            "d.transStatus='NORMAL' and n.deviceId=d.id and n.netGroupId=?1 " +
            "and n.status=1")
    Object[] findTransAndNetCard(Long netGroupId);

    @Query(value = "select count(d) from DeviceNodePO d where d.deviceGroupId=?1")
    Integer findCountByDeviceGroupId(Long id);

    @Query(value = "select count(d) from DeviceNodePO d where d.transBackType='MAIN' and d.deviceGroupId=?1")
    Integer findCountMainByDeviceGroupId(Long id);

    @Query(value = "select count(d) from DeviceNodePO d where d.transBackType='BACK' and d.deviceGroupId=?1")
    Integer findCountBackUpByDeviceGroupId(Long id);
    
//    @Query(value = "select d from DeviceNodePO d join d.deviceAuthPO a where d.deviceGroupId=?1 order by a.cpuUsedNum")
//    List<DeviceNodePO> findByDeviceGroupIdOrderByDeviceAuth(Long deviceGroupId);
    
    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update DeviceNodePO d set d.encapsulateStatus = ?1 where d.encapsulateSocketAddress = ?2")
	public Integer updateEncapsulateStatusBySocketAddress(FunUnitStatus status,String socketAddress);
    
    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update DeviceNodePO d set d.transStatus = ?1 where d.transSocketAddress = ?2")
	public Integer updateTransStatusBySocketAddress(FunUnitStatus status,String socketAddress);
    
    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update DeviceNodePO d set d.transBackType = ?2 where d.id = ?1")
	public Integer updateTransBackTypeById(Long id,BackType backType);
    
    @Query(value = "select d from DeviceNodePO d, NetCardInfoPO n where d.deviceGroupId=?1 and d.transBackType='MAIN' " +
           "and d.transStatus='NORMAL' and n.deviceId=d.id and n.status=1")
    List<DeviceNodePO> findBackTransDevice(Long deviceGroupId);
	
	@Query(value = "select d from DeviceNodePO d where d.transStatus=?1 and d.deviceGroupId=?2")
    List<DeviceNodePO> findDeviceByTransStatus(FunUnitStatus transStatus, Long deviceGroupId);
	
	@Query(value = "select d from DeviceNodePO d where d.encapsulateStatus=?1 and d.deviceGroupId=?2")
    List<DeviceNodePO> findDeviceByEncapsulateStatus(FunUnitStatus transStatus, Long deviceGroupId);
	
	@Query(value = "select d from DeviceNodePO d join d.encapsulateAuthPO a where d.encapsulateStatus=?1 and d.deviceGroupId=?2 order by a.outputRestNum")
    List<DeviceNodePO> findByEncapsulateStatusOrderByOutputRestNum(FunUnitStatus encapsulateStatus, Long deviceGroupId);
	
	 /**
     * 根据通道授权po的id查询出所属deviceInfoPO
     * @param deviceChannelAuthId
     * @return
     */
    @Query(value = "select distinct d from DeviceNodePO d left join d.deviceChannelAuthPOs s where s.id = ?1")
    public DeviceNodePO findByDeviceChannelAuthId(Long deviceChannelAuthId);
	 
    
    /**
     * 设备统计：总数量、NONE(不统计) 、DEFAULT、NORMAL 、NEED_SYNC、OFF_LINE(转码与网关分开统计)
     * @return count
     */
	@Query(value = "select count(*) from DeviceNodePO d where d.transStatus != 'NONE' ")
	public int findTransTotalCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.transStatus = 'DEFAULT' ")
	public int findTransDefaultCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.transStatus = 'NEED_SYNC' ")
	public int findTransNeedSyncCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.transStatus = 'NORMAL' ")
	public int findTransNormalCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.transStatus = 'OFF_LINE' ")
	public int findTransOfflineCount();
	
	@Query(value = "select count(*) from DeviceNodePO d where d.encapsulateStatus != 'NONE'")
	public int findEncapsulateTotalCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.encapsulateStatus = 'DEFAULT' ")
	public int findEncapsulateDefaultCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.encapsulateStatus = 'NEED_SYNC' ")
	public int findEncapsulateNeedSyncCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.encapsulateStatus = 'NORMAL' ")
	public int findEncapsulateNormalCount();
	@Query(value = "select count(*) from DeviceNodePO d where d.encapsulateStatus = 'OFF_LINE' ")
	public int findEncapsulateOfflineCount();
}
