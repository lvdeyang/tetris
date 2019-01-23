package com.sumavision.tetris.user;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 分页查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 上午8:29:31
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return userQuery.list(currentPage, pageSize);
	}
	
	/**
	 * 分页查询用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午5:33:31
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except")
	public Object listWithExceptIds(
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(except == null){
			return userQuery.list(currentPage, pageSize);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return userQuery.listWithExcept(exceptIds, currentPage, pageSize);
		}
		
	}
	
	/**
	 * 添加一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午3:55:30
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 密码确认
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @return UserVO 用户数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String nickname,
            String username,
            String password,
            String repeat,
            String mobile,
            String mail) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return userService.add(nickname, username, password, repeat, mobile, mail);
	}
	
	/**
	 * 删除一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:39:20
	 * @param @PathVariable id 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		userService.delete(id);
		
		return null;
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
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String nickname,
            String mobile,
            String mail,
            boolean editPassword,
            String oldPassword,
            String newPassword,
            String repeat) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return userService.edit(id, nickname, mobile, mail, editPassword, oldPassword, newPassword, repeat);
	}
	
}
