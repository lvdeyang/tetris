package com.sumavision.tetris.streamTranscoding.extension.deal.stream;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TranscodeStreamExtensionPO.class, idClass = Long.class)
public interface TranscodeStreamExtensionDAO extends BaseDAO<TranscodeStreamExtensionPO>{
	public TranscodeStreamExtensionPO findByExtension(String extendsion);
}
