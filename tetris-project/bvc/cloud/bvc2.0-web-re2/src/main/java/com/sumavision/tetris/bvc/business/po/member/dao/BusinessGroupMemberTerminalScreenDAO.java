package com.sumavision.tetris.bvc.business.po.member.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalScreenPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessGroupMemberTerminalScreenPO.class, idClass = Long.class )
public interface BusinessGroupMemberTerminalScreenDAO extends MetBaseDAO<BusinessGroupMemberTerminalScreenPO> {

}
