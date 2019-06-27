package com.sumavision.tetris.subordinate.role.feign;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.predicate_return;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.subordinate.role.SubordinateRoleClassify;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.subordinate.role.SubordinateRoleService;
import com.sumavision.tetris.subordinate.role.SubordinateRoleVO;
import com.sumavision.tetris.subordinate.role.UserSubordinateRolePermissionDAO;
import com.sumavision.tetris.subordinate.role.UserSubordinateRolePermissionPO;
import com.sumavision.tetris.subordinate.role.UserSubordinateRolePermissionQuery;

/**
 * 公司角色rest接口<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 上午10:54:34
 */
@Controller
@RequestMapping(value = "/subordinate/role/feign")
public class SubordinateRoleFeignController {
	
	@Autowired
	private SubordinateRoleQuery subordinateRoleQuery;
	@Autowired
	private SubordinateRoleService subordinateRoleService;
	@Autowired
	private UserSubordinateRolePermissionDAO userSubordinateRolePermissionDAO;
	/**
	 * 通过公司id查询公司管理员角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午12:54:29
	 * @param JSONString companyId 公司id
	 * @return SubordinateRoleVO 公司管理员角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/role/by/company")
	public Object roleByCompany(
			String companyId, 
			HttpServletRequest request) throws Exception{
		SubordinateRoleVO roleVO = subordinateRoleQuery.getRoleCompany(Long.parseLong(companyId));
		return roleVO;
	}
	/**
	 * 通过公司id查找所有公司角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param company 用户id
	 * @return List<SubordinaryRoleVO> 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/roles/by/company")
	public Object rolesByCompany(
			String companyId, 
			HttpServletRequest request) throws Exception{
		List<SubordinateRoleVO> roleVOs = subordinateRoleQuery.getListFromCompany(Long.parseLong(companyId));
		return roleVOs;
	}
	
	/**
	 * 通过角色id查询角色组<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午12:54:29
	 * @param JSONString ids 角色ids
	 * @return List<SubordinateRoleVO> 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/roles/by/ids")
	public Object rolesByIds(String ids,HttpServletRequest request)throws Exception{
		List<Long> roleIds = JSON.parseArray(ids, Long.class);
		List<SubordinateRoleVO>	list = subordinateRoleQuery.getRolesByIds(roleIds);
		return list;
	}
	
	/**
	 * 通过角色id列表查找角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @param  Long 用户id
	 * @return SubordinaryRoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/role/by/id")
	public Object roleById(String id,HttpServletRequest request)throws Exception{
		Long roleId = JSON.parseObject(id, Long.class);
		SubordinateRoleVO Vo = subordinateRoleQuery.getRoleById(roleId);
		return Vo;
	}
	
	/**
	 * 通过角色id列表和公司id查找角色组<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return List<SubordinaryRoleVO> 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/roles/by/company/and/ids")
	public Object rolesByCompanyAndIds(String company,String ids,HttpServletRequest request)throws Exception{
		Long companyId = JSON.parseObject(company, Long.class);
		List<Long> roleIds = JSON.parseArray(ids, Long.class);
		List<SubordinateRoleVO> list = subordinateRoleQuery.getListFromCompany(companyId);
		List<SubordinateRoleVO> result = new ArrayList<SubordinateRoleVO>();
		for(int i = 0;i<roleIds.size();i++)
		{
			Long roleId = roleIds.get(i);
			for(int j = 0;j<list.size();j++)
			{
				SubordinateRoleVO vo = list.get(j);
				if(vo.getId().equals(roleId))
				{
					result.add(vo);
				}
			}
		}
		return result;
	}

	/**
	 * 添加角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return SubordinaryRoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String userId,String companyId, String roleName,String upDate,String Removeable,String Serial,HttpServletRequest request) throws Exception {
		SubordinateRoleVO vo = subordinateRoleService.addRoleWithUserId(Long.parseLong(userId), Long.parseLong(companyId), roleName, SubordinateRoleClassify.INTERNAL_COMPANY_ORDINARY_ROLE, upDate, Removeable, Serial);
		return vo;
	}
	
	/**
	 * 修改角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return SubordinaryRoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(String roleId,String roleName,HttpServletRequest request)throws Exception{
		SubordinateRoleVO vo = subordinateRoleService.editRole(Long.parseLong(roleId), roleName);
		return vo;
	}
	
	/**
	 * 删角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return SubordinaryRoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delet")
	public Object delet(String roleId,HttpServletRequest request)throws Exception{
		SubordinateRoleVO vo = subordinateRoleService.removeRole(Long.parseLong(roleId));
		return vo;
	}
	
	/**
	 * 通过用户id查找角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return List<SubordinaryRoleVO> 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/user")
	public Object queryByUser(String userId,HttpServletRequest request)throws Exception{
		Long result = userSubordinateRolePermissionDAO.getRoleIdFromUserId(Long.parseLong(userId));
		return result.toString();
	}
}
