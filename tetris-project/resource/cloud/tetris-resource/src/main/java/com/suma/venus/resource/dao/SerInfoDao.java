package com.suma.venus.resource.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;

@RepositoryDefinition(domainClass = SerInfoPO.class, idClass = Long.class)
public interface SerInfoDao extends CommonDao<SerInfoPO> {

	public SerInfoPO findTopBySerUuid(String serUuid);

	@Query("select s from SerInfoPO s where (s.syncStatus='ASYNC' or s.syncStatus is null) and (s.sourceType='SYSTEM' or s.sourceType is null)")
	public List<SerInfoPO> findSerInfoSyncToLdap();

	public List<SerInfoPO> findBySerType(Integer serType);

	public List<SerInfoPO> findBySourceType(SOURCE_TYPE sourceType);


}



