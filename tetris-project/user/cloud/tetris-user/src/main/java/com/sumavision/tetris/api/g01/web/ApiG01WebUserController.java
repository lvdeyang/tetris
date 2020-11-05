                                                                       package com.sumavision.tetris.api.g01.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserService;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/g01/web/user")
public class ApiG01WebUserController {
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 分页查询用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 上午8:29:31
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/subordinate/list")
	public Object listBySubordinate(
			int currentPage,
			int pageSize,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		if(user.getId() != null){
			return userQuery.list(currentPage, pageSize,user.getId());
		}else if(user.getUuid() != null){
			return userQuery.list(currentPage, pageSize,Long.parseLong(user.getUuid()));
		}else {
			return null;
		}
	}
	
	/**
	 * 修改一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:54:11
	 * @param @PathVariable Long id 用户id
	 * @param String nickname 昵称
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param boolean editPassword 是否修改密码
	 * @param String oldPassword 旧密码
	 * @param String newPassword 新密码
	 * @param String repeat 重复新密码
	 * @return UserVO 修改后的数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String nickname,
            String mobile,
            String mail,
            HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return userService.edit(id, nickname, mobile, mail, null, null, false, "", "", "", "", "", false, null);
	}
	
	/**
	 * 根据用户id修改用户密码<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 下午2:39:07
	 * @param Long id 用户id
	 * @param String oldPassword 旧密码
	 * @param String newPassword 新密码
	 * @param String repeat 确认密码
	 * @return UserVO 用户信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/password/modify")
	public Object modifyPassword(Long id, String oldPassword, String newPassword, String repeat, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		return userService.modifyPassword(id, oldPassword, newPassword, repeat);
	}
}
