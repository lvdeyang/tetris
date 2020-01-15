package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.ExtraInfoPO;

/**
 * 附加信息表
 * @author lxw
 *
 */
@RepositoryDefinition(domainClass = ExtraInfoPO.class, idClass = Long.class)
public interface ExtraInfoDao extends CommonDao<ExtraInfoPO>{

	public List<ExtraInfoPO> findByBundleId(String bundleId);
	
	public List<ExtraInfoPO> findByBundleIdIn(Collection<String> bundleIds);
	
	public ExtraInfoPO findByBundleIdAndName(String bundleId,String name);
	
	public List<ExtraInfoPO> findByNameAndValue(String name,String value);
	
	@Query("select extraInfo.bundleId from ExtraInfoPO extraInfo where extraInfo.name=?1 and extraInfo.value=?2")
	public Set<String> queryBundleIdByNameAndValue(String name,String value);
	
	@Query("select extraInfo.bundleId from ExtraInfoPO extraInfo where extraInfo.value like %?1%")
	public Set<String> queryBundleIdByValueLike(String value);
	
	@Query("select extraInfo.name from ExtraInfoPO extraInfo where 1=1")
	public Set<String> queryNames();
	
	@Modifying
	@Transactional
	@Query("delete from ExtraInfoPO po where po.bundleId = ?1")
	public int deleteByBundleId(String bundleId);
}
