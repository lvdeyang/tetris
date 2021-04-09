package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupRecordPO.class, idClass = long.class)
public interface CommandGroupRecordDAO extends MetBaseDAO<CommandGroupRecordPO>{
	
	List<CommandGroupRecordPO> findByGroupIdAndRun(Long groupId, boolean run);
	
	List<CommandGroupRecordPO> findByGroupIdAndRecordUserIdAndRun(Long groupId, Long recordUserId, boolean run);
	
	List<CommandGroupRecordPO> findByRecordUserIdAndRun(Long recordUserId, boolean run);
}
