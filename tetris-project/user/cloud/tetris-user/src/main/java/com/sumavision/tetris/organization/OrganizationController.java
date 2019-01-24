package com.sumavision.tetris.organization;

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

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.organization.exception.CompanyNotExistException;
import com.sumavision.tetris.organization.exception.OrganizationNotExistException;
import com.sumavision.tetris.organization.exception.UserHasNoPermissionForOrganizationException;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/organization")
public class OrganizationController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private OrganizationDAO organizationDao;
	
	@Autowired
	private OrganizationUserPermissionDAO organizationUserPermissionDao;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private OrganizationQuery organizationQuery;
	
	@Autowired
	private CompanyDAO companyDao;
	
	/**
	 * 查询公司内部门树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 下午5:07:15
	 * @param Long companyId 公司id
	 * @return List<OrganizationVO> 树列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/tree")
	public Object queryTree(
			Long companyId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//权限校验
		
		List<OrganizationVO> roots = organizationQuery.queryTree(companyId);
		
		return roots;
	}
	
	/**
	 * 添加一个组织机构<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午2:57:11
	 * @param Long companyId 公司id
	 * @param Long parentId 上级部门id
	 * @param String name 部门名称
	 * @return OrganizationVO 部门数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long companyId,
			Long parentId,
			String name, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		CompanyPO company = companyDao.findOne(companyId);
		if(company == null){
			throw new CompanyNotExistException(companyId);
		}
		
		OrganizationPO parent = null;
		if(parentId != null){
			parent = organizationDao.findOne(parentId);
			if(parent == null){
				throw new OrganizationNotExistException(parentId);
			}
		}
		
		OrganizationPO organization = new OrganizationPO();
		organization.setCompanyId(company.getId());
		organization.setName(name);
		organization.setUpdateTime(new Date());
		if(parent != null){
			organization.setParentId(parent.getId());
			StringBufferWrapper parentPath = new StringBufferWrapper();
			if(parent.getParentId() == null){
				parentPath.append("/").append(parent.getId());
			}else{
				parentPath.append(parent.getParentPath()).append("/").append(parent.getId());
			}
			organization.setParentPath(parentPath.toString());
			organization.setLevel(parent.getLevel() + 1);
		}else{
			organization.setLevel(1);
		}
		organizationDao.save(organization);
		
		return new OrganizationVO().set(organization);
	}
	
	/**
	 * 修改部门名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午3:18:28
	 * @param PathVariable Long id 部门id
	 * @param String name 部门名称
	 * @return OrganizationVO 部门数据
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
		
		OrganizationPO organization = organizationDao.findOne(id);
		if(organization == null){
			throw new OrganizationNotExistException(id);
		}
		
		organization.setName(name);
		organization.setUpdateTime(new Date());
		
		organizationDao.save(organization);
		
		return new OrganizationVO().set(organization);
	}
	
	/**
	 * 删除部门<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午3:40:24
	 * @param @PathVariable Long id 部门id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		OrganizationPO organization = organizationDao.findOne(id);
		
		if(organization != null){
			organizationService.delete(organization);
		}
		
		return null;
	}
	
	/******************************************************************************
	 ****************************************************************************** 
	 ******************************************************************************/
	
	/**
	 * 获取组织机构<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 上午9:43:22
	 * @return List<OrganizationVO> 组织架构
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	@Deprecated
	public Object list(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		/*if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}*/
		
		List<UserVO> users = userQuery.list(user.getGroupId());
		
		List<OrganizationPO> organizatins = organizationDao.findByGroupIdOrderBySerialAsc(user.getGroupId());
		
		List<OrganizationVO> view_organizatins = OrganizationVO.getConverter(OrganizationVO.class).convert(organizatins, OrganizationVO.class);
		
		if(organizatins!=null && organizatins.size()>0){
			Set<String> userIds = new HashSet<String>();
			for(UserVO scope:users){
				userIds.add(scope.getUuid());
			}
			
			List<OrganizationUserPermissionPO> permissions = organizationUserPermissionDao.findByUserIdIn(userIds);
			
			if(permissions!=null && permissions.size()>0){
				for(OrganizationVO scope0:view_organizatins){
					for(OrganizationUserPermissionPO scope1:permissions){
						if(scope1.getOrganizationId().equals(scope0.getId())){
							UserVO target = null;
							for(UserVO scope2:users){
								if(scope1.getUserId().equals(scope2.getUuid())){
									target = scope2;
									break;
								}
							}
							if(scope0.getUsers()==null) scope0.setUsers(new ArrayList<UserVO>());
							scope0.getUsers().add(target);
						}
					}
				}
			}
			
			return view_organizatins;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 绑定用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午6:09:59
	 * @param Long id 部门id
	 * @param String users 用户id数组[id, id]
	 * @return List<UserVO> 绑定后部门内所有用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/binding")
	public Object binding(
			Long id, 
			String users, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		OrganizationPO organization = organizationDao.findOne(id);
		if(organization == null){
			throw new OrganizationNotExistException(id);
		}
		
		if(!organization.getGroupId().equals(user.getGroupId())){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		List<String> userIds = JSON.parseArray(users, String.class);
		
		organizationService.binding(organization, userIds);
		
		List<OrganizationUserPermissionPO> permissions = organizationUserPermissionDao.findByOrganizationId(organization.getId());
		
		Set<String> ids = new HashSet<String>();
		if(permissions!=null && permissions.size()>0){
			for(OrganizationUserPermissionPO permission:permissions){
				ids.add(permission.getUserId());
			}
		}
		
		List<UserVO> findUsers = userQuery.find(ids);
		
		return findUsers;
	}
	
	/**
	 * 用户部门解绑<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 上午9:19:29
	 * @param Long id 部门id
	 * @param String userId 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/unbinding")
	public Object unbinding(
			Long id,
			String userId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!UserClassify.COMPANY_ADMIN.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		OrganizationPO organization = organizationDao.findOne(id);
		if(organization == null){
			throw new OrganizationNotExistException(id);
		}
		
		if(!organization.getGroupId().equals(user.getGroupId())){
			throw new UserHasNoPermissionForOrganizationException(user.getUuid());
		}
		
		List<OrganizationUserPermissionPO> permissions = organizationUserPermissionDao.findByOrganizationIdAndUserId(organization.getId(), userId);
	
		if(permissions!=null && permissions.size()>0){
			organizationUserPermissionDao.deleteInBatch(permissions);
		}
		
		return null;
	}
	
}
