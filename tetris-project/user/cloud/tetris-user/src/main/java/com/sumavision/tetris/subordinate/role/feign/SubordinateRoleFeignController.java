package com.sumavision.tetris.subordinate.role.feign;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.subordinate.role.SubordinateRoleClassify;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.subordinate.role.SubordinateRoleService;
import com.sumavision.tetris.subordinate.role.SubordinateRoleVO;
import com.sumavision.tetris.subordinate.role.UserSubordinateRolePermissionDAO;

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
		return result != null ? result.toString() : null;
	}
	
	/**
	 * 通过角色id查询角色组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午12:54:29
	 * @param JSONString ids 角色ids
	 * @return List<SubordinateRoleVO> 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id/in")
	public Object findByIdIn(String ids,HttpServletRequest request)throws Exception{
		List<Long> roleIds = JSON.parseArray(ids, Long.class);
		List<SubordinateRoleVO>	list = subordinateRoleQuery.findByIdIn(roleIds);
		return list;
	}
	
	/**
	 * 通过角色id列表查找角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @param  Long 用户id
	 * @return SubordinaryRoleVO 角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id")
	public Object findById(String id,HttpServletRequest request)throws Exception{
		Long roleId = JSON.parseObject(id, Long.class);
		SubordinateRoleVO Vo = subordinateRoleQuery.findById(roleId);
		return Vo;
	}
	
	/**
	 * 查询公司下的业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午8:53:15
	 * @param Long companyId 公司id
	 * @return List<SubordinateRoleVO> 业务角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/company/id")
	public Object findByCompanyId(
			Long companyId, 
			HttpServletRequest request) throws Exception{
		return subordinateRoleQuery.findByCompanyId(companyId);
	}
	
	/**
	 * 查询公司下的业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:01:32
	 * @param Long companyId 公司id
	 * @param JSONArray except 例外角色id列表
	 * @return List<SubordinateRoleVO> 业务角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/company/id/with/except")
	public Object findByCompanyIdWithExcept(
			Long companyId,
			String except,
			HttpServletRequest request) throws Exception{
		
		List<Long> ids = null;
		if(except != null){
			ids = JSON.parseArray(except, Long.class);
		}
		return subordinateRoleQuery.findByCompanyIdWithExcept(companyId, ids);
	}
}
