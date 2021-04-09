package com.sumavision.bvc.command.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CommandGroupMemberPO.class, idClass = long.class)
public interface CommandGroupMemberDAO extends MetBaseDAO<CommandGroupMemberPO>{
	
//	@Query(value="SELECT m.user_id FROM bvc_command_group_member m WHERE m.group_id=?1", nativeQuery=true)
	@Query(value="select m.userId from com.sumavision.bvc.command.group.basic.CommandGroupMemberPO m where m.group.id=?1")
	public List<Long> findUserIdsByGroupId(Long id);
	
	@Query(value="select m.userName from com.sumavision.bvc.command.group.basic.CommandGroupMemberPO m where m.group.id=?1 and m.userId in ?2")
	public List<String> findUserNamesByGroupIdAndUserIds(Long id, List<Long> userIds);
	
}
