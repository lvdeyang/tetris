package com.sumavision.tetris.cs.template;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = TemplateProgramePO.class, idClass = Long.class)

public interface TemplateProgrameDao extends BaseDAO<TemplateProgramePO>{
	@Query(value = "SELECT * FROM TETRIS_CS_TEMPLATE_PROGRAME WHERE template_id = ?1 \n#pageable\n",
			countQuery = "SELECT count(*) FROM TETRIS_CS_TEMPLATE_PROGRAME WHERE template_id = ?1",
			nativeQuery = true)
	public Page<TemplateProgramePO> PagefindAllByTemplateId(long templateId, Pageable pageable);
}
