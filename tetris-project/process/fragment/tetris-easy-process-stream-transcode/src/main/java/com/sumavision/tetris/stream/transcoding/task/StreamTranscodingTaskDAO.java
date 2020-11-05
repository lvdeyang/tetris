package com.sumavision.tetris.stream.transcoding.task;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = StreamTranscodingTaskPO.class, idClass = Long.class)
public interface StreamTranscodingTaskDAO extends BaseDAO<StreamTranscodingTaskPO>{
	public StreamTranscodingTaskPO findByInputId(Long inputId);
}
