package com.sumavision.tetris.device.netcard;

import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Lost on 2017/2/7.
 */
@RepositoryDefinition(domainClass = NetCardInfoPO.class, idClass = Long.class)
public interface NetCardInfoDao extends BaseDAO<NetCardInfoPO> {

    NetCardInfoPO findByIpv4OrVirtualIpv4(String ipv4, String virtualIpv4);

    List<NetCardInfoPO> findByIpv4(String ipv4);

    List<NetCardInfoPO> findByDeviceId(Long id);

    NetCardInfoPO findTopByDeviceIdAndOutputNetGroupId(Long deviceId, Long netGroupId);

    List<NetCardInfoPO> findByDeviceIdAndBeCtrl(Long id, Boolean beCtrl);

    NetCardInfoPO findByDeviceIdAndIpv4(Long deviceId, String ipv4);

    List<NetCardInfoPO> findByOutputNetGroupIdIsNotNullAndDeviceId(Long deviceId);

    @Query(value="select distinct net from NetCardInfoPO net, NetGroupPO grp where net.inputNetGroupId = grp.id and net.deviceId=?1")
    List<NetCardInfoPO> findInputNetByDeviceId(Long deviceId);

    @Query(value="select distinct net from NetCardInfoPO net, NetGroupPO grp where net.outputNetGroupId = grp.id and net.deviceId=?1")
    List<NetCardInfoPO> findOutputNetByDeviceId(Long deviceId);

    List<NetCardInfoPO> findByOutputNetGroupIdIsNotNullAndDeviceIdAndStatus(Long deviceId, Integer linkStatus);

    @Query(value = "select n from NetCardInfoPO n where n.deviceId = ?1 and n.inputNetGroupId is not null")
    List<NetCardInfoPO> findAllInputNetCardByDeviceId(Long deviceId);

    @Query(value = "select n from NetCardInfoPO n where n.deviceId = ?1 and n.status = ?2 and n.inputNetGroupId is not null")
    Integer countInputNetCardsByDeviceIdAndStatus(Long deviceId,Integer status);

    NetCardInfoPO findTopByDeviceIdAndBeCtrlAndStatus(Long deviceId, Boolean beCtrl, Integer linkStatus);

    NetCardInfoPO findTopByDeviceIdAndOutputNetGroupIdAndStatus(Long deviceId, Long netGroupId, Integer linkStatus);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update NetCardInfoPO n set n.status=?2 where n.id = ?1")
	public Integer updateNetCardStatusById(Long id, Integer status);

    List<NetCardInfoPO> findByOutputNetGroupIdAndDeviceIdAndStatus(Long netGroupId, Long deviceId, Integer status);

    List<NetCardInfoPO> findByInputNetGroupIdAndDeviceIdAndStatus(Long netGroupId, Long deviceId, Integer status);

    List<NetCardInfoPO> findByInputNetGroupId(Long netGroupId);

    @Query(value = "select count(*) from TETRIS_CAPACITY_DEVICE_NETCARD nc inner join TETRIS_CAPACITY_DEVICE d on d.id = nc.device_id where nc.input_net_group_id = ?1",nativeQuery = true)
    Integer countDistinctByInputNetGroupId(Long netGroupId);

    @Query(value = "select count(*) from TETRIS_CAPACITY_DEVICE_NETCARD nc inner join TETRIS_CAPACITY_DEVICE d on d.id = nc.device_id where nc.output_net_group_id = ?1",nativeQuery = true)
    Integer countDistinctByOutputNetGroupId(Long netGroupId);

    List<NetCardInfoPO> findByOutputNetGroupId(Long netGroupId);

    NetCardInfoPO findTopByInputNetGroupIdAndDeviceIdAndStatus(Long netGroupId, Long deviceId, Integer status);


    NetCardInfoPO findTopByOutputNetGroupIdAndDeviceIdAndStatus(Long netGroupId, Long deviceId, Integer status);

    @Query(value = "select * from NetCardInfoPO n where n.outputNetGroupId is not null and n.deviceId = ?1",nativeQuery = true)
    List<NetCardInfoPO> findByOutputNetGroupIdNotNullByDeviceId(Long deivceId);

    List<NetCardInfoPO> findByDeviceIdOrderByCardNum(Long id);
}
