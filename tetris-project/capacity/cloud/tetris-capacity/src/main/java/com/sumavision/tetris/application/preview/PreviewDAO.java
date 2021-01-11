package com.sumavision.tetris.application.preview;

import com.sumavision.tetris.application.preview.PreviewPO;
import com.sumavision.tetris.orm.dao.BaseDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RepositoryDefinition(domainClass = PreviewPO.class, idClass = Long.class)
public interface PreviewDAO extends BaseDAO<PreviewPO>{
    PreviewPO findByInputId(String inputId);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
    @Query(value = "update PreviewPO p set p.previewTaskId = ?2 where p.id = ?1")
    public Integer updatePreviewTaskIdById(Long id,String taskId);

    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
    @Query(value = "update PreviewPO p set p.transferTaskId = ?2 where p.id = ?1")
    public Integer updateTransferTaskIdById(Long id,String taskId);
}
