package com.sumavision.tetris.record.source;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = SourcePO.class, idClass = Long.class)
public interface SourceDAO extends BaseDAO<SourcePO> {

	// List<SourcePO> findByRecordStatus(Integer recordStatus);

	// List<SourcePO> findByRecordStatusAndDelStatusNot(Integer recordStatus, Integer delStatus);

	// 分页jpa返回的是page对象，page对象的getContent方法中包含List（直接用List接收会出错）
	Page<SourcePO> findAll(Pageable pageRequest);

	Page<SourcePO> findAllByNameContaining(Pageable pageRequest, String name);

	Page<SourcePO> findAllByNameContainingAndDelStatusNot(Pageable pageRequest, String name, Integer delStatus);

	List<SourcePO> findByName(String name);

	List<SourcePO> findByLocalIp(String localIp);

	List<SourcePO> findByUrl(String url);

	// List<SourcePO> findByNameAndProgramName(String name, String programName);

	// List<SourcePO> findByUniTaskId(String uniTaskId);

	List<SourcePO> findByUrlAndDelStatus(String url, Integer delStatus);

	List<SourcePO> findByDelStatusNot(Integer delStatus);
}
