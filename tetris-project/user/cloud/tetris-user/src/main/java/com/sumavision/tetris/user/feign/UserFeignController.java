package com.sumavision.tetris.user.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
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
		
		UserPO userPO = userDAO.findOne(id);
		
		if(userPO == null) throw new UserNotExistException(id);
		
		UserVO user = new UserVO().set(userPO);
		
		//TODO 权限校验
		
		return userService.edit(id, user.getNickname(), user.getMobile(), user.getMail(), tags, false, "", "", "");
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
	public Object queryAllUserBaseInfo(HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryAllUserBaseInfo(userVO.getId());
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
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return userQuery.queryAllUserBaseInfo(userVO.getId(), currentPage, pageSize);
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
	@RequestMapping(value = "/query/user/by/id")
	public Object queryUsersById(
			Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO userVO = userQuery.current();
		
		return new UserVO().set(userDAO.findOne(id));
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
}
