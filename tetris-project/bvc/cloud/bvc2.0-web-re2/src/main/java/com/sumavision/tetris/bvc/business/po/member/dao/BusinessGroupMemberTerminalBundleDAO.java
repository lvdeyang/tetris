package com.sumavision.tetris.bvc.business.po.member.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = BusinessGroupMemberTerminalBundlePO.class, idClass = Long.class)
public interface BusinessGroupMemberTerminalBundleDAO extends MetBaseDAO<BusinessGroupMemberTerminalBundlePO> {

}
