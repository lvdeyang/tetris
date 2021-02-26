package com.sumavision.tetris.user.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.exception.UserNotExistException;

@Controller
@RequestMapping(value = "/user/feign")
public class UserFeignController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TokenQuery tokenQuery;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserService userService;
	
	
	/**
	 * 用户登录校验<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午2:39:14
	 * @param String token 登录token
	 * @param String terminalType 终端类型
	 * @return boolean 判断结果
	 */
	/*@JsonBody
	@ResponseBody
	@RequestMapping(value = "/check/token")
	public Object checkToken(
			String token, 
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		return tokenQuery.checkToken(token, TerminalType.valueOf(terminalType));
	}*/
	
	/**
	 * 查询当前登录用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 上午10:23:32
	 * @return UserVO 当前登录用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/current")
	public Object current(HttpServletRequest request) throws Exception{
		
		return userQuery.current();
	}
	
	/**
	 * 根据token查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月7日 下午6:23:15
	 * @param String token 用户登录token
	 * @return UserVO 用户数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/token")
	public Object findByToken(
			String token,
			HttpServletRequest request) throws Exception{
		
		return userQuery.findByToken(token);
	}
	
	/**
	 * 根据用户id查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午3:25:59
	 * @param JSONArray ids 用户id列表
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id/in")
	public Object findByIdIn(
			String ids,
			HttpServletRequest request) throws Exception{
		List<Long> userIds = JSON.parseArray(ids, Long.class);
		return userQuery.findByIdIn(userIds);
	}
	
	/**
	 * 根据用户昵称列表查询用户列表<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 下午4:33:33
	 * @param String names
	 * @return List<UserVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/nickname/in")
	public Object findByNickNameIn(
			String nicknames,
			HttpServletRequest request) throws Exception{
		List<String> nickNames = JSON.parseArray(nicknames, String.class);
		return userQuery.queryUsersByNicknameIn(nickNames);
	}
	
	/**
	 * 根据id和类型查询用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月19日 下午3:30:27
	 * @param JSONArray ids 用户id列表
	 * @param String terminalType 查询类型
	 * @return JSONObject 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id/in/and/type")
	public Object findByIdIn(
			String ids,
			String terminalType,
			HttpServletRequest request) throws Exception{
		List<Long> userIds = JSON.parseArray(ids, Long.class);
		TerminalType type = TerminalType.fromName(terminalType);
		return userQuery.findByIdInAndType(userIds, type);
	}
	
	/**
	 * 分页查询公司下的用户列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:17:57
	 * @param Long companyId 公司id
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/by/{companyId}/with/except")
	public Object listByCompanyIdWithExcept(
			@PathVariable Long companyId,
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		if(except == null){
			return userQuery.listByCompanyId(companyId, currentPage, pageSize);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return userQuery.listByCompanyIdWithExcept(companyId, exceptIds, currentPage, pageSize);
		}
	}
	
	/**
	 *  根据用户类型查询公司下的用户列表（带例外）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午9:41:44
	 * @param Long companyId 公司id
	 * @param JSONString except 例外用户id列表
	 * @param String classify 用户类型
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/by/{companyId}/with/except/and/classify")
	public Object listByCompanyIdWithExceptAndClassify(
			@PathVariable Long companyId,
			String except,
			String classify,
			HttpServletRequest request) throws Exception{
		
		UserClassify userClassify = UserClassify.fromName(classify);
		if(except == null){
			return userQuery.listByCompanyIdAndClassify(companyId, userClassify);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return userQuery.listByCompanyIdWithExceptAndClassify(companyId, exceptIds, userClassify);
		}
	}
	
	/**
	 *  根据用户类型查询公司下的用户列表（带例外-类型）<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午9:41:44
	 * @param Long companyId 公司id
	 * @param JSONString except 例外用户id列表
	 * @param String classify 用户类型
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/by/{companyId}/with/except/and/type/and/classify")
	public Object listByCompanyIdWithExceptAndTypeAndClassify(
			@PathVariable Long companyId,
			String terminalType,
			String except,
			String classify,
			HttpServletRequest request) throws Exception{
		
		UserClassify userClassify = UserClassify.fromName(classify);
		if(except == null){
			return userQuery.listByCompanyIdAndClassify(companyId, terminalType, userClassify);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return userQuery.listByCompanyIdWithExceptAndClassify(companyId, terminalType, exceptIds, userClassify);
		}
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
            String tags) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		UserPO userPO = userDAO.findById(id);
		
		if(userPO == null) throw new UserNotExistException(id);
		
		UserVO user = new UserVO().set(userPO);
		
		//TODO 权限校验
		
		return userService.edit(id, user.getNickname(), user.getMobile(), user.getMail(), null, tags, false, "", "", "", "", "", false, null,null);
	}
	
	/**
	 * 查询所有用户基本信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月31日 下午1:57:48
	 * @return List<UserVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/user/baseInfo")
	public Object queryAllUserBaseInfo(
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryAllUserBaseInfo(userVO.getId(), terminalType);
	}
	
	/**
	 * 查询所有用户基本信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 上午9:15:14
	 * @return List<UserVO> 所有用户信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/user/baseInfo/by/page")
	public Object queryAllUserBaseInfo(
			String terminalType,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryAllUserBaseInfo(userVO.getId(), terminalType, currentPage, pageSize);
	}
	
	/**
	 * 根据用户名模糊查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月24日 下午1:21:35
	 * @param String userName 用户名
	 * @return List<UserVO> 用户信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/users/by/name/like/by/page")
	public Object queryUsersByNameLike(
			String userName,
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		List<Long> exceptIds = JSON.parseArray(except, Long.class);
		
		return userQuery.queryUsersByNameLike(userVO.getId(), userName, exceptIds, currentPage, pageSize);
	}
	
	/**
	 * 根据用户名模糊查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午10:25:33
	 * @param String userName 用户名
	 * @return List<UserVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/users/by/name/like")
	public Object queryUsersByNameLike(
			String userName,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryUsersByNameLike(userVO.getId(), userName);
	}
	
	/**
	 * 根据用户名查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午10:25:33
	 * @param String userName 用户名
	 * @return UserVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/by/name")
	public Object queryUsersByName(
			String userName,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryUserByName(userVO.getId(), userName);
	}
	
	/**
	 * 根据用户id查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午10:25:33
	 * @param String userName 用户名
	 * @return UserVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/by/id/and/type")
	public Object queryUsersByIdAndTerminalType(
			Long id,
			String terminalType,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryUserByIdAndType(id, terminalType);
	}
	
	/**
	 * 根据用户号码查询用户--同一公司下<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午7:14:09
	 * @param String userno 用户号码
	 * @return UserVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/by/no")
	public Object queryUsersByNo(
			String userno,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryUserByUserno(userVO.getId(), userno);
	}
	
	/**
	 * 根据角色查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 上午11:10:05
	 * @param Long roleId 角色id
	 * @return List<UserVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/by/role")
	public Object queryUsersByRole(
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryUsersByRole(roleId);
	}
	
	/**
	 * 添加游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:46:11
	 * @param String nickname 游客昵称
	 * @return UserVO 用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/tourist")
	public Object addTourist(
			String nickname,
			HttpServletRequest request) throws Exception{
		
		return userService.addTourist(nickname);
	}
	
	/**
	 * 删除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:48:34
	 * @param Long userId 游客id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/tourist")
	public Object removeTourist(
			Long userId, 
			HttpServletRequest request) throws Exception{
		
		userService.removeTourist(userId);
		return null;
	}
	
	/**
	 * 批量删除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午1:56:02
	 * @param JSONString userIds 游客id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/tourist/batch")
	public Object removeTouristBatch(
			String userIds,
			HttpServletRequest request) throws Exception{
		
		userService.removeTouristBatch(JSON.parseArray(userIds, Long.class));
		return null;
	}
	
	/**
	 * 根据游客id查询游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午5:14:21
	 * @param String userUuId 游客uuid
	 * @return UserVO 游客
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/tourist")
	public Object findTourist(
			String userUuId,
			HttpServletRequest request) throws Exception{
		
		return userQuery.findTourist(userUuId);
	}
	
	/**
	 * 根据用户号码查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 上午10:43:03
	 * @param String userno 用户号码
	 * @return UserVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/userno")
	public Object findByUserno(
			String userno,
			HttpServletRequest request) throws Exception{
		
		return userQuery.findByUserno(userno);
	}
	
	/**
	 * 根据用户号码批量查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 上午10:43:03
	 * @param String usernos 用户号码
	 * @return List<UserVO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/userno/in")
	public Object findByUsernoIn(
			@RequestBody String usernos,
			HttpServletRequest request) throws Exception{
		
		List<String> usernoList = JSONArray.parseArray(usernos, String.class);
		return userQuery.findByUsernoIn(usernoList);
	}
	
	/**
	 * 根据用户ids删除ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午2:15:22
	 * @param String userIds 用户ids
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/ldap/user/by/ids")
	public Object deleteLdapUser(
			String userIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdList = JSONArray.parseArray(userIds, Long.class);
		userDAO.deleteByIdInAndClassify(userIdList, UserClassify.LDAP);
		return null;
	}
	
	/**
	 * 删除所有ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午5:23:24
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/ldap/user")
	public Object deleteLdapUsers(HttpServletRequest request) throws Exception{
		
		userDAO.deleteByClassify(UserClassify.LDAP);
		return null;
	}
	
	/**
	 * 添加ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午3:52:42
	 * @param String users 用户信息
	 * @return List<UserVO> 持久化过的用户信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/ldap/user")
	public Object addLdapUser(
			String users,
			HttpServletRequest request) throws Exception{
		
		List<UserVO> userVOs = JSONArray.parseArray(users, UserVO.class);
		List<UserPO> userPOs = userService.addLdapUser(userVOs);
		
		List<UserVO> view_users = UserVO.getConverter(UserVO.class).convert(userPOs, UserVO.class);
		return view_users;
	}
	
	/**
	 * 根据公司和条件查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 上午11:05:41
	 * @param String nickname 用户昵称
	 * @param String userno 用户号码
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/company/id/and/condition")
	public Object findByCompanyIdAndCondition(
			String nickname, 
			String userno, 
			int currentPage, 
			int pageSize) throws Exception{
		
		UserVO user = userQuery.current();
		
		return userQuery.findByCompanyIdAndCondition(Long.valueOf(user.getGroupId()), nickname, userno, currentPage, pageSize);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/online")
	public Object queryUserOnline() throws Exception{
		return userQuery.queryUserOnline();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/user/tags")
	public Object queryUserTags(Long userId) throws Exception{
		return userQuery.queryUserTags(userId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/tag/hotcount")
	public Object addTagHotcount(
			Long userId, 
			String tagNames,
			HttpServletRequest request) throws Exception{
		
		
		return userService.addTagHotCount(userId,tagNames);
	}
	
	/**
	 * 查询所有角色<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午5:24:10
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/all")
	public Object findAll()throws Exception{
		return userDAO.findAll();
	}
	
	/**
	 * 查询角色下的用户<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午12:30:34
	 * @param roleIds 角色ids
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/roleId/in")
	public Object findByRoleIdIn(List<Long> roleIds) throws Exception{
		return userDAO.findByRoleIdIn(roleIds);
	}
	
	/**
	 * 设置修改用户在线人数上限<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月18日 上午11:33:22
	 * @param userCapatity 人数上限数量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/userCapacity" )
	public Object setUserCapacity(Long userCapacity)throws Exception{
		
		userService.setUserCapacity(userCapacity);
		
		return null;
	}
}
