package com.sumavision.tetris.sts.device.netcard;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonDao;

import java.util.List;

/**
 * Created by Lost on 2017/2/7.
 */
@RepositoryDefinition(domainClass = NetCardInfoPO.class, idClass = Long.class)
public interface NetCardInfoDao extends CommonDao<NetCardInfoPO> {

    NetCardInfoPO findByIpv4OrVirtualIpv4(String ipv4 , String virtualIpv4);
    
    List<NetCardInfoPO> findByIpv4(String ipv4);

    List<NetCardInfoPO> findByDeviceId(Long id);

    List<NetCardInfoPO> findByDeviceIdAndBeCtrl(Long id,Boolean beCtrl);

    NetCardInfoPO findByDeviceIdAndIpv4(Long deviceId,String ipv4);

//    @Query(value = "select n from NetGroupPO m inner join NetCardInfoPO n on m.id = n.netGroupId where n.deviceId = ?1 and m.netGroupType in ('OUTPUT','DATA')")
//    List<NetCardInfoPO> findAllOutputNetCardByDeviceId(Long deviceId);
//
//    @Query(value = "select n from NetGroupPO m inner join NetCardInfoPO n on m.id = n.netGroupId where n.deviceId = ?1 and m.netGroupType in ('INPUT','DATA')")
//    List<NetCardInfoPO> findAllInputNetCardByDeviceId(Long deviceId);

    List<NetCardInfoPO> findByDeviceIdOrderByIndexNum(Long id);

    List<NetCardInfoPO> findByDeviceIdAndBeCtrlOrderByIndexNum(Long id,Boolean bectrl);

//    @Query(value = "select n from DeviceNodePO d , NetCardInfoPO n where " +
//            "d.deviceGroupId=?1 and d.id=n.deviceId")
//    List<NetCardInfoPO> findByDeviceGroupId(Long id);

    NetCardInfoPO findTopByDeviceIdAndBeCtrlAndStatus(Long deviceId,Boolean beCtrl,Integer linkStatus);

    NetCardInfoPO findTopByDeviceIdAndNetGroupIdAndStatus(Long deviceId, Long netGroupId, Integer linkStatus);

    List<NetCardInfoPO> findByNetGroupId(Long netGroupId);
    
    List<NetCardInfoPO> findByNetGroupIdAndDeviceIdAndStatus(Long netGroupId,Long deviceId, Integer linkStatus);

    List<NetCardInfoPO> findByNetGroupIdAndDeviceId(Long netGroupId,Long deviceId);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update NetCardInfoPO n set n.status=?2 where n.id = ?1")
	public Integer updateNetCardStatusById(Long id,Integer status);
	
}
