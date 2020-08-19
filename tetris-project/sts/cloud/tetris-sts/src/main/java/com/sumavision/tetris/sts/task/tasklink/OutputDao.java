package com.sumavision.tetris.sts.task.tasklink;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonDao;


@RepositoryDefinition(domainClass = OutputPO.class, idClass = Long.class)
public interface OutputDao extends CommonDao<OutputPO>{

    @Override
    @Deprecated
	public OutputPO findOne(Long id);
	
	public List<OutputPO> findByInputId(Long inputId);
	
	public List<OutputPO> findByInputIdAndProgramId(Long inputId,Integer programNum);
	
	public List<OutputPO> findByTaskId(Long taskId);
	
	public void delete(Iterable<OutputPO> OutputPOs);
	
	public OutputPO findTopByNodeId(Long nodeId);
	
	public OutputPO findByPort(Integer port);
	
	@Query(value = "select max(port) from OutputPO")
	public Integer findMaxPort();
	
	public List<OutputPO> findByIpAndPort(String ip,Integer port);
	
	public OutputPO findTopByIpAndPortAndNetGroupId(String ip,Integer port,Long netGroupId);
	
	public OutputPO findTopByIpAndPortAndTypeAndPubName(String ip,Integer port,ProtoType protoType,String pubName);
	
	public List<OutputPO> findByStreamMediaId(Long streamMediaId);
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteByDeviceId(Long deviceId);
	
	@Query(value = "select distinct nodeId from OutputPO o where o.nodeId is not null")
	Set<Long> findNodeIds();
	
	@Query(value = "select o from OutputPO o where o.nodeId in ?1")
	public  List<OutputPO> findByOutputNodeIds(List<Long> outputNodeIds);

	public List<OutputPO> findByDeviceId(Long deivceId);
	
}
