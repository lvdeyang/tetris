package com.sumavision.tetris.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.context.HttpSessionContext;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class UserQuery {
	
	@Autowired
	private UserFeign userFeign;

	/**
	 * 用户登录校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午2:39:14
	 * @param String token 登录token
	 * @return boolean 判断结果
	 */
	/*public boolean checkToken(String token) throws Exception{
		JSONObject response = userFeign.checkToken(token);
		return JsonBodyResponseParser.parseObject(response, Boolean.class);
	}*/
	
	/**
	 * 获取当前登录用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午10:14:26
	 * @return UserVO 当前用户
	 */
	public UserVO current()throws Exception{
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		HttpSession session = null;
		String sessionId = request.getHeader(HttpConstant.HEADER_SESSION_ID);
		if(sessionId == null){
			//临时session 5秒超时
			session = HttpSessionContext.build(null, HttpConstant.TEMPORARY_SESSION_TIMEOUT);
		}else{
			session = HttpSessionContext.get(sessionId);
			if(session == null){
				//feign调用统一sessionid 超时时间
				session = HttpSessionContext.build(sessionId, HttpConstant.SESSION_TIMEOUT);
			}
		}
		
		String token = request.getHeader(HttpConstant.HEADER_AUTH_TOKEN);
		UserVO user = (UserVO)session.getAttribute(HttpConstant.ATTRIBUTE_USER);
		boolean needQuery = false;
		if(user == null) needQuery = true;
		else if(!user.getToken().equals(token)) needQuery = true;
		if(!needQuery) return user;
		
		//查询用户
		user = JsonBodyResponseParser.parseObject(userFeign.current(), UserVO.class);
		session.setAttribute(HttpConstant.ATTRIBUTE_USER, user);
		return user;
	}
	
	/**
	 * 根据token查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午11:12:39
	 * @param String token 用户登录令牌
	 * @return UserPO 用户
	 */
	public UserVO findByToken(String token) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.findByToken(token), UserVO.class);
	}
	
	/**
	 * 根据id查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午3:33:06
	 * @param Collection<Long> ids 用户id列表
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> findByIdIn(Collection<Long> ids) throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.findByIdIn(JSON.toJSONString(ids)), UserVO.class);
	}
	
	/**
	 * 分页查询公司下的用户列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:17:57
	 * @param Long companyId 公司id
	 * @param Collection<Long> except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	public Map<String, Object> listByCompanyIdWithExcept(Long companyId, Collection<Long> except, int currentPage, int pageSize) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.listByCompanyIdWithExcept(companyId, JSON.toJSONString(except), currentPage, pageSize), Map.class);
	}
	
	/**
	 * 根据公司id和类型查询用户列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:28:37
	 * @param Long companyId 公司id
	 * @param String except 例外
	 * @param String classify 类型
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> listByCompanyIdWithExceptAndClassify(Long companyId, Collection<Long> except, UserClassify classify) throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.listByCompanyIdWithExceptAndClassify(companyId, except != null && !except.isEmpty() ? JSON.toJSONString(except) : null, classify.getName()), UserVO.class);
	}
	
	/**
	 * 根据公司id和类型查询用户列表（带例外-类型）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:28:37
	 * @param Long companyId 公司id
	 * @param String except 例外
	 * @param String classify 类型
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> listByCompanyIdWithExceptAndClassify(Long companyId, String terminalType, Collection<Long> except, UserClassify classify) throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.listByCompanyIdWithExceptAndClassify(companyId, terminalType, except != null && !except.isEmpty() ? JSON.toJSONString(except) : null, classify.getName()), UserVO.class);
	}
	
	/**
	 * 修改一个用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:54:11
	 * @param Long id 用户id
	 * @param String tags 修改的标签(全量)
	 * @return UserVO 修改后的数据
	 */
	public UserVO edit(Long id, String tags) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.edit(id, tags), UserVO.class);
	}
	
	/**
	 * 查询所有用户信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 上午9:22:21
	 * @return List<UserVO> 用户列表
	 */
	public Map<String, Object> queryAllUserBaseInfo(int currentPage, int pageSize) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.queryAllUserBaseInfo(currentPage, pageSize), Map.class);
	}
	
	/**
	 * 查询所有用户信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月31日 下午2:01:49
	 * @return List<UserVO>
	 */
	public List<UserVO> queryAllUserBaseInfo() throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.queryAllUserBaseInfo(), UserVO.class);
	}
	
	/**
	 * 根据用户名模糊查询--分页<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:25:58
	 * @param String userName 用户名
	 * @return List<UserVO> 用户列表
	 */
	public Map<String, Object> queryUsersByNameLike(String userName, String except, int currentPage, int pageSize) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.queryUsersByNameLike(userName, except, currentPage, pageSize), Map.class);
	}
	
	/**
	 * 根据用户名模糊查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:25:58
	 * @param String userName 用户名
	 * @return List<UserVO> 用户列表
	 */
	public List<UserVO> queryUsersByNameLike(String userName) throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.queryUsersByNameLike(userName), UserVO.class);
	}
	
	/**
	 * 根据用户名查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:25:58
	 * @param String userName 用户名
	 * @return UserVO 用户
	 */
	public UserVO queryUserByName(String userName) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.queryUserByName(userName), UserVO.class);
	}
	
	/**
	 * 根据用户id查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:25:58
	 * @param Long id 用户id
	 * @return UserVO 用户
	 */
	public UserVO queryUserById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.queryUserById(id), UserVO.class);
	}
	
	/**
	 * 根据用户号码查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午7:21:55
	 * @param String userno 用户号码
	 * @return UserVO 用户
	 */
	public UserVO queryUserByNo(String userno) throws Exception{
		return JsonBodyResponseParser.parseObject(userFeign.queryUserByNo(userno), UserVO.class);
	}
	
	/**
	 * 根据角色查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 上午11:15:03
	 * @param Long roleId jueseid
	 * @return List<UserVO>
	 */
	public List<UserVO> queryUsersByRole(Long roleId) throws Exception{
		return JsonBodyResponseParser.parseArray(userFeign.queryUserByRole(roleId), UserVO.class);
	}
	
	/***************************************************
	 ***************************************************
	 ***************************************************/
	
	/**
	 * 获取用户组下所有的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:53:11
	 * @param String groupId 用户组id
	 * @return List<UserVO> 用户列表
	 */
	@Deprecated
	public List<UserVO> list(String groupId){
		return this.users;
	}
	
	/**
	 * 分页获取用户组下的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午5:24:19
	 * @param String groupId 用户组id
	 * @param Integer pageSize 每页数量
	 * @param Integer currentSize 当前页码
	 * @oaran Collection<String> except 例外用户
	 * @return List<UserVO> 用户列表
	 */
	@Deprecated
	public List<UserVO> list(String groupId, Integer pageSize, Integer currentPage, Collection<String> except){
		if(except==null || except.size()<=0) return this.users;
		List<UserVO> users = this.users;
		List<UserVO> filtered = new ArrayList<UserVO>();
		for(UserVO user:users){
			if(!except.contains(user.getUuid())){
				filtered.add(user);
			}
		}
		return filtered;
	}
	
	/**
	 * 计算企业内部用户总数（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 下午2:19:03
	 * @param String groupId 企业id
	 * @param Collection<String> except 例外用户id列表
	 * @return Integer 总数
	 */
	@Deprecated
	public Integer count(String groupId, Collection<String> except){
		if(except==null || except.size()<=0) return count(groupId);
		List<UserVO> users = this.users;
		int count = 0;
		for(UserVO user:users){
			if(!except.contains(user.getUuid())){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 获取企业内用户总数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午5:26:09
	 * @param String groupId 用户组id
	 * @return Integer 用户数量
	 */
	@Deprecated
	public Integer count(String groupId){
		return this.users.size();
	}
	
	/**
	 * 根据userId查找用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午6:20:07
	 * @param Collection<String> userIds 用户id列表
	 * @return List<UserVO> 用户列表
	 */
	@Deprecated
	public List<UserVO> find(Collection<String> userIds){
		List<UserVO> users = this.users;
		List<UserVO> findUsers = new ArrayList<UserVO>();
		for(String userId:userIds){
			for(UserVO user:users){
				if(user.getUuid().equals(userId)){
					findUsers.add(user);
					break;
				}
			}
		}
		return findUsers;
	}
	
	/**
	 * 根据userId分页查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 上午10:08:22
	 * @param Collection<String> userIds 用户id列表
	 * @param Integer pageSize 每页数据量
	 * @param Integer currentPage 当前页码
	 * @return List<UserVO> 用户列表
	 */
	@Deprecated
	public List<UserVO> find(Collection<String> userIds, Integer pageSize, Integer currentPage){
		return find(userIds);
	}
	
	/** 测试数据 */
	private List<UserVO> users = new ArrayListWrapper<UserVO>().add(new UserVO().setUuid("1")
																        .setNickname("用户1")
																        .setStatus(UserStatus.ONLINE.getName())
																        .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("2")
															            .setNickname("用户2")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("3")
															            .setNickname("用户3")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("4")
														                .setNickname("用户4")
															            .setStatus(UserStatus.ONLINE.getName())
															            .setNumbersOfMessage(13))
														.add(new UserVO().setUuid("5")
															            .setNickname("用户5")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(0))
														.add(new UserVO().setUuid("6")
														                .setNickname("用户6")
															            .setStatus(UserStatus.OFFLINE.getName())
															            .setNumbersOfMessage(2))
														.getList();
	
}

