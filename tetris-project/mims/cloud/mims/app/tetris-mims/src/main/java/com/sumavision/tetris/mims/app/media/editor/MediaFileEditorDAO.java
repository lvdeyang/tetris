package com.sumavision.tetris.mims.app.media.editor;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaFileEditorPO.class, idClass = Long.class)
public interface MediaFileEditorDAO extends BaseDAO<MediaFileEditorPO>{
	public MediaFileEditorPO findByMediaIdAndMediaType(Long mediaId, FolderType mediaType);
	
	public MediaFileEditorPO findByProcessInstanceId(String processInstanceId);
	
	public List<MediaFileEditorPO> findByMediaIdInAndMediaType(List<Long> mediaIds, FolderType mediaType);
	
	public void deleteByMediaIdAndMediaType(Long mediaId, FolderType mediaType);
}
