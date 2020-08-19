package com.sumavision.tetris.record;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = StreamTranscodingTaskRecordPermissionPO.class, idClass = Long.class)
public interface StreamTranscodingTaskRecordPermissionDAO extends BaseDAO<StreamTranscodingTaskRecordPermissionPO>{
	public StreamTranscodingTaskRecordPermissionPO findByMessageId(Long messageId);
}
