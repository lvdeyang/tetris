package com.sumavision.tetris.mims.app.user;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.organization.OrganizationUserPermissionDAO;
import com.sumavision.tetris.mims.app.organization.exception.UserHasNoPermissionForOrganizationException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 用户相关操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月10日 下午2:49:57
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private OrganizationUserPermissionDAO organizationUserPermissionDao;
	
	/**
	 * 部门绑定用户时的查询接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午4:58:47
	 * @param Integer pageSize 每页数量
	 * @param Integer currentPage 当前页码
	 * @return List<UserVO> rows 用户列表
	 * @return Integer total 用户总数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/except/organization")
	public Object listExceptOrganization(
			Integer pageSize, 
			Integer currentPage, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		if(user.getGroupId() == null){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		List<String> except = organizationUserPermissionDao.findPermissionedUser(user.getGroupId());
		
		Integer total = userTool.count(user.getGroupId(), except);
		
		List<UserVO> users = userTool.list(user.getGroupId(), pageSize, currentPage, except);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", users)
																		 .put("total", total)
																		 .getMap();
		return result;
	}
	
	/**
	 * 获取全部用户列表（分页）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月10日 下午2:22:19
	 * @param Integer pageSize 每页数量
	 * @param Integer currentPage 当前页码
	 * @return List<UserVO> rows 用户列表
	 * @return Integer total 用户总数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Integer pageSize, 
			Integer currentPage, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		if(user.getGroupId() == null){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		List<UserVO> users = userTool.list(user.getGroupId(), pageSize, currentPage, null);

		Integer total = userTool.count(user.getGroupId());
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", users)
																		 .put("total", total)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 查询组织下所有的用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月3日 下午2:38:36
	 * @return List<UserVO> users 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/all")
	public Object listAll(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		List<UserVO> users = userTool.list(user.getGroupId());
		
		return users;
	}
	
}
