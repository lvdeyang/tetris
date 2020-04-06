package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;

@RepositoryDefinition(domainClass = SerNodePO.class, idClass = Long.class)
public interface SerNodeDao extends CommonDao<SerNodePO> {

	public SerNodePO findTopByNodeUuid(String nodeUuid);

	@Query("select s from SerNodePO s where (s.syncStatus='ASYNC' or s.syncStatus is null) and (s.sourceType='SYSTEM' or s.sourceType is null)")
	public List<SerNodePO> findSerNodeSyncToLdap();

	public SerNodePO findTopBySourceType(SOURCE_TYPE sourceType);

	public List<SerNodePO> findBySourceType(SOURCE_TYPE sourceType);
	
	public List<SerNodePO> findByNodeFather(String nodeFather);
	
	public List<SerNodePO> findByNodeUuidIn(Collection<String> nodeUuids);
	
	@Query(value = "SELECT n.* FROM ser_nodepo n LEFT JOIN ser_infopo f ON f.ser_node = n.node_uuid where f.ser_no = ?1", nativeQuery = true)
	public SerNodePO findByApplicationId(String applicationId);
}
