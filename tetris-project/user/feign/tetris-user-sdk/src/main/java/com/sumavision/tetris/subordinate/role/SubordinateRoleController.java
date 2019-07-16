package com.sumavision.tetris.subordinate.role;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/subordinate/role")
public class SubordinateRoleController {

	@Autowired
	private SubordinateRoleQuery subordinateRoleQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 根据id查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午4:58:18
	 * @param JSONArray ids 角色id列表
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id/in")
	public Object findByIdIn(
			String ids,
			HttpServletRequest request) throws Exception{
		List<Long> roleIds = JSON.parseArray(ids, Long.class);
		return subordinateRoleQuery.findByIdIn(roleIds);
	}
	
	/**
	 * 查询公司下的业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:29:22
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/company/id")
	public Object findByCompanyId(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return subordinateRoleQuery.findByCompanyId(Long.valueOf(user.getGroupId()));
	}
	
	/**
	 * 查询公司下的业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:34:08
	 * @param JSONArray except 例外角色id列表
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/company/id/with/except")
	public Object findByCompanyIdWithExcept(
			String except, 
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<Long> ids = null;
		if(except != null){
			ids = JSON.parseArray(except, Long.class);
		}
		return subordinateRoleQuery.findByCompanyIdWithExcept(Long.valueOf(user.getGroupId()), ids);
	}
	
}
