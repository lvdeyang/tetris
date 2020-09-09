package com.sumavision.tetris.zoom.call.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class CallUserQuery {

	@Autowired
	private CallUserDAO callUserDao;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 用户查询呼叫任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午9:59:54
	 * @return List<CallUserVO> 用户呼叫任务列表
	 */
	public List<CallUserVO> queryUserCalls() throws Exception{
		UserVO user = userQuery.current();
		List<CallUserPO> entities = findByUserId(user.getId());
		return CallUserVO.getConverter(CallUserVO.class).convert(entities, CallUserVO.class);
	}
	
	/**
	 * 查询用户的呼叫任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 上午9:57:44
	 * @param Long userId 用户id
	 * @return List<CallUserPO> 用户呼叫业务列表
	 */
	private List<CallUserPO> findByUserId(Long userId) throws Exception{
		return callUserDao.findByUniqueKeyLike(new StringBufferWrapper().append("%").append(userId).append("%").toString());
	}
	
}
