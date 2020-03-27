package com.sumavision.tetris.user;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

	@Autowired
	private UserFeign userFeign;
	
	/**
	 * 添加一个游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:40:58
	 * @param String nickname 游客昵称
	 * @return UserVO 用户
	 */
	public UserVO addTourist(String nickname) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.addTourist(nickname), UserVO.class);
	}
	
	/**
	 * 清除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:44:32
	 * @param Long userId 游客id
	 */
	public void removeTourist(Long userId) throws Exception{
		userFeign.removeTourist(userId);
	}
	
	/**
	 * 批量清除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午1:59:06
	 * @param Collection<Long> userIds 游客id列表
	 */
	public void removeTouristBatch(Collection<Long> userIds) throws Exception{
		userFeign.removeTouristBatch(JSON.toJSONString(userIds));
	}
	
	/**
	 * 添加ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午4:04:50
	 * @param List<UserVO> users ldap用户信息
	 * @return List<UserVO> 持久化过后的用户信息
	 */
	public List<UserVO> addLdapUser(List<UserVO> users) throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.addLdapUser(JSON.toJSONString(users)), UserVO.class);
	}
	
	/**
	 * 删除ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:25:23
	 */
	public void deleteLdapUser() throws Exception{
		userFeign.deleteLdapUsers();
	}
	
}
