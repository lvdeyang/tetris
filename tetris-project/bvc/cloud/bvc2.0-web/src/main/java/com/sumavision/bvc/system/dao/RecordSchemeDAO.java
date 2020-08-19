package com.sumavision.bvc.system.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.system.dto.RecordSchemeDTO;
import com.sumavision.bvc.system.po.RecordSchemePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = RecordSchemePO.class, idClass = long.class)
public interface RecordSchemeDAO extends MetBaseDAO<RecordSchemePO>{

	@Query(value = "select new com.sumavision.bvc.system.dto.RecordSchemeDTO(record.id, record.uuid, record.updateTime, record.name, role.id, role.name) from RecordSchemePO record left join record.role role")
	public Page<RecordSchemeDTO> findAllOutputDTO(Pageable pageable);
	
	@Query(value = "select new com.sumavision.bvc.system.dto.RecordSchemeDTO(record.id, record.uuid, record.updateTime, record.name, role.id, role.name) from RecordSchemePO record left join record.role role")
	public List<RecordSchemeDTO> findAllOutputDTO();
	
	public List<RecordSchemePO> findByName(String name);
	
}
