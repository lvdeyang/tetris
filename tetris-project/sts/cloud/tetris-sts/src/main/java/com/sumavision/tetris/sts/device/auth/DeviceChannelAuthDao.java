package com.sumavision.tetris.sts.device.auth;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonDao;


@RepositoryDefinition(domainClass = DeviceChannelAuthPO.class, idClass = Long.class)
public interface DeviceChannelAuthDao extends CommonDao<DeviceChannelAuthPO>{

	public List<DeviceChannelAuthPO> findByInputTypeOrOutputTypeLike(Long inputType,String outputType);

	@Query(value = "select * from device_channel_auth d where d.deviceId in ?1 and d.inputType=?2 and d.outputType=?3 and d.authNumRest!=?4", nativeQuery = true)
	public List<DeviceChannelAuthPO> findByDeviceIdListAndInputTypeAndOutputTypeAndAuthNumRestNot(List<Long> deviceIdList, Long inputType, String outputType, Integer authNumRest);

	@Query(value = "select * from device_channel_auth d where d.deviceId = ?1 and d.inputType=?2 and d.outputType=?3 and d.authNumRest!=?4", nativeQuery = true)
	public List<DeviceChannelAuthPO> findByDeviceIdAndInputTypeAndOutputTypeAndAuthNumRestNot(Long deviceId, Long inputType, String outputType, Integer authNumRest);
	
	@Query(value = "select * from device_channel_auth d where d.deviceId = ?1 and d.cardNumber = ?2 and d.inputType=?3 and d.outputType=?4 and d.authNumRest!=?5", nativeQuery = true)
	public List<DeviceChannelAuthPO> findByDeviceIdAndCardNumberAndInputTypeAndOutputTypeAndAuthNumRestNot(Long deviceId, Integer cardNumber,Long inputType, String outputType, Integer authNumRest);

	//查询对应设备id_卡号上的最优通道，用于优先使用已经解码过的卡
	@Query(value = "select * from device_channel_auth d where d.deviceId in ?1 and d.inputType=?2 and d.outputType=?3 and d.authNumRest!=?4 and concat_ws('_',d.deviceId,d.cardNumber) in ?5", nativeQuery = true)
	public List<DeviceChannelAuthPO> findBestChannel(List<Long> deviceIdList, Long inputType, String outputType, Integer authNumRest, List<String> deviceId_cardNumber);
	//查询不在某一批设备id_卡号上的最优通道，配合上面的方法使用
	@Query(value = "select * from device_channel_auth d where d.deviceId in ?1 and d.inputType=?2 and d.outputType=?3 and d.authNumRest!=?4 and concat_ws('_',d.deviceId,d.cardNumber) not in ?5", nativeQuery = true)
	public List<DeviceChannelAuthPO> findRestBestChannel(List<Long> deviceIdList, Long inputType, String outputType, Integer authNumRest, List<String> deviceId_cardNumber);

	@Query(value = "select * from device_channel_auth d where d.deviceId in ?1 and d.authNumRest!='0'", nativeQuery = true)
	public List<DeviceChannelAuthPO> findAllAvailableChannel(List<Long> deviceIdList);

	//获取设备上已占用通道的类型
	@Query(value="select distinct d.inputType,d.outputType from device_channel_auth d where d.deviceId = ?1 and d.authNumRest < d.authNum", nativeQuery = true)
	public List<Object> getUsedChannelTypes(Long deviceNodeId);
	
	//查询设备上的最优通道，用于通道非混加占用时,优先选择这种类型通道剩余数量最多的卡
	@Query(value = "select * from device_channel_auth d where d.deviceId=?1 and d.inputType=?2 and d.outputType=?3 and d.authNumRest>0 order by d.authNumRest desc limit 1", nativeQuery = true)
	DeviceChannelAuthPO findRestBestChannel(Long deviceId, Long inputType, String outputType);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update DeviceChannelAuthPO set authNumRest = authNum where id in ?1")
	public Integer recoverAuthNum(List<Long> id);


	@Query(value = "select a.* from task_link_node_info i join trans_task t on i.nodeId = t.nodeId join device_channel_auth a on t.deviceChannelId = a.id  where i.tasklinkId in ?1 and i.nodeType = 2", nativeQuery = true)
	public Set<DeviceChannelAuthPO> findTransAuthByTasklinkId(Set<Long> tasklinkId);
}
