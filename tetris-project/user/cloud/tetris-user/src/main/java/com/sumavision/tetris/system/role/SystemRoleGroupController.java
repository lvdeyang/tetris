package com.sumavision.tetris.system.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.organization.CompanyDAO;
import com.sumavision.tetris.organization.CompanyPO;
import com.sumavision.tetris.system.role.exception.SystemRoleGroupNotExistException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/system/role/group")
public class SystemRoleGroupController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SystemRoleGroupDAO systemRoleGroupDao;
	
	@Autowired
	private SystemRoleGroupService systemRoleGroupService;
	
	@Autowired
	private CompanyDAO companyDao;
	
	/**
	 * 获取全部系统角色组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 上午11:10:54
	 * @return List<SystemRoleGroupVO> 系统角色组列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<SystemRoleGroupPO> roles = systemRoleGroupDao.findAllOrderByUpdateTimeDesc();
		List<SystemRoleGroupVO> view_roles = new ArrayList<SystemRoleGroupVO>();
		if(roles!=null && roles.size()>0){
			Set<Long> companyIds = new HashSetWrapper<Long>().add(-1l).getSet();
			for(SystemRoleGroupPO role:roles){
				view_roles.add(new SystemRoleGroupVO().set(role));
				companyIds.add(role.getCompanyId());
			}
			List<CompanyPO> companyEntities = companyDao.findAllById(companyIds);
			if(companyEntities!=null && companyEntities.size()>0){
				for(CompanyPO companyEntity:companyEntities){
					for(SystemRoleGroupVO view_role:view_roles){
						if(companyEntity.getId().equals(view_role.getCompanyId())){
							view_role.setCompanyName(companyEntity.getName());
						}
					}
				}
			}
		}
		
		return view_roles;
	}
	
	/**
	 * 添加一个系统角色组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 上午11:34:07
	 * @param String name 系统角色组名称
	 * @param Long companyId 企业id
	 * @return SystemRoleGroupVO 系统角色组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			Long companyId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		SystemRoleGroupPO group = new SystemRoleGroupPO();
		group.setName(name);
		if(companyId != null) group.setCompanyId(companyId);
		group.setAutoGeneration(false);
		group.setUpdateTime(new Date());
		systemRoleGroupDao.save(group);
		
		SystemRoleGroupVO roleGroup = new SystemRoleGroupVO().set(group);
		
		if(companyId != null){
			CompanyPO companyEntity = companyDao.findById(companyId);
			roleGroup.setCompanyName(companyEntity.getName());
		}
		
		return roleGroup;
	}
	
	/**
	 * 修改系统角色组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午1:43:24
	 * @param @PathVariable Long id 角色组id
	 * @param String name 角色组名称
	 * @return SystemRoleGroupVO 角色组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		SystemRoleGroupPO group = systemRoleGroupDao.findById(id);
		if(group == null){
			throw new SystemRoleGroupNotExistException(id);
		}
		
		group.setName(name);
		group.setUpdateTime(new Date());
		systemRoleGroupDao.save(group);
	
		return new SystemRoleGroupVO().set(group);
	}
	
	/**
	 * 删除系统角色组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午2:22:17
	 * @param @PathVariable Long id 系统角色组id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		systemRoleGroupService.delete(id);
		
		return null;
	}
	
}
