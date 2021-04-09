package com.sumavision.tetris.bvc.business.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.bvc.business.call.UserCallPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = UserCallPO.class, idClass = long.class)
public interface UserCallDAO extends MetBaseDAO<UserCallPO>{

	public UserCallPO findByGroupId(Long groupId);

	public UserCallPO findByCalledUserIdAndCallUserId(Long calledUserId, Long callUserId);
	
//	public UserCallPO findByCalledDecoderBundleId(String calledDecoderBundleId);
//	
//	public UserCallPO findByCallDecoderBundleId(String callDecoderBundleId);
	
}
