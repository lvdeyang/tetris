package com.sumavision.tetris.streamTranscoding.extension.deal.editor;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TranscodeExtensionParamPermissionPO.class, idClass = Long.class)
public interface TranscodeExtensionParamPermissionDAO extends BaseDAO<TranscodeExtensionParamPermissionPO>{
	public TranscodeExtensionParamPermissionPO findByExtension(String extendsion);
}
