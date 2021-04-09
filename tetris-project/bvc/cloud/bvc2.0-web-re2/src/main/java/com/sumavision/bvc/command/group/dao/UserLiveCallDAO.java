package com.sumavision.bvc.command.group.dao;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.command.group.user.UserLiveCallPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = UserLiveCallPO.class, idClass = Long.class)
public interface UserLiveCallDAO extends MetBaseDAO<UserLiveCallPO>{
	
	/**
	 * 根据呼叫方用户id和被呼叫方用户id获取呼叫业务信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月17日 下午4:00:31
	 * @param Long calledUserId 被呼叫方用户id
	 * @param Long callUserId 呼叫方用户id
	 * @return UserLiveCallPO 
	 */
	public UserLiveCallPO findByCalledUserIdAndCallUserId(Long calledUserId, Long callUserId);
	
	public UserLiveCallPO findByCalledDecoderBundleId(String calledDecoderBundleId);
	
	public UserLiveCallPO findByCallDecoderBundleId(String callDecoderBundleId);

}
