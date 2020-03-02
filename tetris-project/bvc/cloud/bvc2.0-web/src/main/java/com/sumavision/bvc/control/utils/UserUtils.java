package com.sumavision.bvc.control.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.tetris.auth.token.TerminalType;

/**
 * 用户相关操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月19日 下午4:52:14
 */
@Component
public class UserUtils {

	private static final String USER_IN_SESSION = "user_in_session"; 
	
	@Autowired
	private UserQueryFeign userFeign;
	
	@Autowired
	private UserQueryService userQueryService;
	
	/**
	 * 向session中缓存用户信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月19日 下午4:54:05
	 * @param request 请求
	 * @param user 用户信息
	 * @throws Exception
	 */
	public void setUserToSession(HttpServletRequest request, UserVO user) throws Exception{
		HttpSession session = request.getSession();
		session.setAttribute(USER_IN_SESSION, user);
		//session超时设置为半小时
		session.setMaxInactiveInterval(300*60);
	}
	
	/**
	 * 从session中获取缓存的用户信息
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月19日 下午4:57:48
	 * @param request 请求
	 * @return UserVO 用户信息
	 * @throws Exception
	 */
	public UserVO getUserFromSession(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		Object user = session.getAttribute(USER_IN_SESSION);
		if(user == null){
//			String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//			Map<String, UserBO> users = userQueryService.queryUserInfo(username);
//			if(users==null || users.size()<=0 || users.get("user")==null){
//				throw new UserDroppedException(username);
//			}
			
			UserBO userBO = userQueryService.current();
			
			//包装一下
			user = new UserVO().set(userBO);
			setUserToSession(request, (UserVO)user);
		}
		return (UserVO)user;
	}
	
	/**
	 * 从session中获取缓存的用户id<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月19日 下午4:59:57
	 * @param request 请求
	 * @return Long 用户id
	 * @throws Exception
	 */
	public Long getUserIdFromSession(HttpServletRequest request) throws Exception{
		UserVO user = getUserFromSession(request);
		return user.getId();
	}
	
	/**
	 * 根据用户id查询用户<br/>
	 * <p>用户的在线状态按照 QT_ZK 查找</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 下午2:41:44
	 * @param Long userId 用户id
	 * @return UserBO 用户
	 */
	public UserBO queryUserById(Long userId) throws Exception{
		try{			
			return userQueryService.queryUserByUserId(userId, TerminalType.QT_ZK);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 根据用户id查询用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月24日 下午4:51:36
	 * @param userId
	 * @param terminalType
	 * @return
	 * @throws Exception
	 */
	public UserBO queryUserById(Long userId, TerminalType terminalType) throws Exception{
		try{			
			return userQueryService.queryUserByUserId(userId, terminalType);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 根据用户号码查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 下午2:41:44
	 * @param String userno 用户号码
	 * @return UserBO 用户
	 */
	public UserBO queryUserByUserno(String userno) throws Exception{
		try{
			Map<String, UserBO> resultMap = userFeign.queryLdapUserByUserNo(userno);
			if(resultMap==null || resultMap.size()<=0 || resultMap.get("user")==null) return null;
			return resultMap.get("user");
		}catch(Exception e){
			return null;
		}
	}
	
}
