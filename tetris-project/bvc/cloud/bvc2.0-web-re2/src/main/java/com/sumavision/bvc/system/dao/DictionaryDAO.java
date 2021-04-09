package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.enumeration.ServLevel;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = DictionaryPO.class, idClass = long.class)
public interface DictionaryDAO extends MetBaseDAO<DictionaryPO>{

	@Query("from DictionaryPO d order by d.dicType asc")
	public Page<DictionaryPO> findDics(Pageable pageable);
	
	public List<DictionaryPO> findByDicType(DicType type);
	
	public List<DictionaryPO> findByDicTypeAndServLevel(DicType type, ServLevel level);
	
	public DictionaryPO findByContentIs(String content);

	public DictionaryPO findByContentAndDicType(String content, DicType dicType);
	
	public DictionaryPO findByContentAndDicTypeAndServLevel(String content, DicType dicType, ServLevel servLevel);
	
	public List<DictionaryPO> findByServLevelAndDicType(ServLevel servLevel, DicType dicType);
	
	@Query("select dic from DictionaryPO dic where dic.dicType = ?1 or dic.dicType = ?2 ")
	public List<DictionaryPO> findPrograms(DicType live, DicType demand);
	
	public DictionaryPO findByBoId(String boId);
	
	public DictionaryPO findByLiveBoId(String boId);
	
	public DictionaryPO findByDicTypeAndCode(DicType dicType, String code);
	
	/*
	 * 通过名称前缀和类型查找
	 */
	@Query("select dic from DictionaryPO dic where dic.content like ?1% and dic.dicType = ?2")
	public List<DictionaryPO> findByContentPrefixAndDicType(String contentPrefix, DicType dicType);
	
	/*
	 * 通过名称前缀、类型、栏目等级查找
	 */
	@Query("select dic from DictionaryPO dic where dic.content like ?1% and dic.dicType = ?2 and dic.servLevel = ?3 ")
	public List<DictionaryPO> findByContentPrefixAndDicTypeAndServLevel(String contentPrefix, DicType dicType, ServLevel servLevel);
}
