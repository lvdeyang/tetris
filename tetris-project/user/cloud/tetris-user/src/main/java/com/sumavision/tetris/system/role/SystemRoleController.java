package com.sumavision.tetris.system.role;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.system.role.exception.AutoGenerationRoleCannotDeleteException;
import com.sumavision.tetris.system.role.exception.SystemRoleGroupNotExistException;
import com.sumavision.tetris.system.role.exception.SystemRoleNotExistException;
import com.sumavision.tetris.user.OperationLogService;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/system/role")
public class SystemRoleController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SystemRoleQuery systemRoleQuery;
	
	@Autowired
	private SystemRoleGroupDAO systemRoleGroupDao;
	
	@Autowired
	private SystemRoleDAO systemRoleDao;
	
	@Autowired
	private SystemRoleService systemRoleService;
	
	@Autowired
	private OperationLogService operationLogService;
	
	/**
	 * 查询全部系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月18日 上午9:38:30
	 * @return List<SystemRoleVO> 系统角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/all")
	public Object listAll(HttpServletRequest request) throws Exception{
		
		return systemRoleQuery.listAll();
	}
	
	/**
	 * 分页查询系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 上午10:25:27
	 * @param Long groupId 系统角色组id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 系统角色数据总量 
	 * @return List<SystemRoleVO> rows 系统角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			Long groupId,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return systemRoleQuery.list(groupId, currentPage, pageSize);
	}
	
	/**
	 * 分类查询系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月22日 下午5:30:46
	 * @param String createType 系统角色创建类型
	 * @param Long companyId 企业id
	 * @param Long targetUserId 目标用户id
	 * @param String exceptIds 例外角色id
	 * @param Boolean packGroup 是否按照分组组装
	 * @return List<SystemRoleVO> 系统角色列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/create/type")
	public Object queryByCreateType(
			String createType, 
			Long companyId,
			Long targetUserId,
			String exceptIds,
			Boolean packGroup,
			HttpServletRequest request) throws Exception{
		
		return systemRoleQuery.queryByCreateType(createType, companyId, targetUserId, exceptIds, packGroup);
	}
	
	/**
	 * 添加一个系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午4:04:18
	 * @param Long groupId 系统角色组id
	 * @param String name 系统角色名
	 * @param String createType 系统角色的创建类型 SYSTEM_ADMIN, COMPANY_ADMIN
	 * @param Long companyId 企业id
	 * @return SystemRoleVO 系统角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long groupId,
			String name,
			String createType,
			Long companyId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		SystemRoleGroupPO group = systemRoleGroupDao.findById(groupId);
		
		if(group == null){
			throw new SystemRoleGroupNotExistException(groupId);
		}
		
		SystemRolePO role = new SystemRolePO();
		role.setName(name);
		role.setUpdateTime(new Date());
		role.setSystemRoleGroupId(group.getId());
		role.setType(SystemRoleType.SYSTEM);
		
		SystemRoleCreateType parsedCreateType = SystemRoleCreateType.valueOf(createType);
		if(SystemRoleCreateType.SYSTEM_ADMIN.equals(parsedCreateType)){
			role.setCreateType(SystemRoleCreateType.SYSTEM_ADMIN);
		}else{
			role.setCreateType(SystemRoleCreateType.COMPANY_ADMIN);
			if(companyId != null){
				role.setCompanyId(companyId);
			}else{
				role.setCompanyId(user.getGroupId()==null?null:Long.valueOf(user.getGroupId()));
			}
		}
		
		systemRoleDao.save(role);
		
		//添加角色日志
		UserVO userVO = userQuery.current();
		operationLogService.send(userVO.getUsername(), "添加角色", "用户  " + userVO.getUsername() + " 添加了  " + role.getName(), EOprlogType.ROLE_OPR);
		
		return new SystemRoleVO().set(role);
	}
	
	/**
	 * 修改一个系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午4:10:20
	 * @param @PathVariable Long id 系统角色id
	 * @param String name 系统角色名称
	 * @return SystemRoleVO 系统角色
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
		
		SystemRolePO role = systemRoleDao.findById(id);
		
		if(role == null){
			throw new SystemRoleNotExistException(id);
		}
		String oldname = role.getName();
		
		role.setName(name);
		systemRoleDao.save(role);
		
		//修改角色日志
		UserVO userVO = userQuery.current();
		operationLogService.send(userVO.getUsername(), "修改角色", "用户  " + userVO.getUsername() + " 修改  " + oldname + " 为 " + name, EOprlogType.ROLE_OPR);
		
		return new SystemRoleVO().set(role);
	}
	
	/**
	 * 删除系统角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午4:13:30
	 * @param @PathVariable Long id 系统角色id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		SystemRolePO role = systemRoleDao.findByIdAndType(id, SystemRoleType.SYSTEM);
		
		if(role != null){
			if(role.isAutoGeneration()){
				throw new AutoGenerationRoleCannotDeleteException();
			}
			
			systemRoleService.delete(new ArrayListWrapper<SystemRolePO>().add(role).getList());
			
			//删除角色日志
			UserVO userVO = userQuery.current();
			operationLogService.send(userVO.getUsername(), "修改角色", "用户  " + userVO.getUsername() + " 删除了  " + role.getName() + " 角色", EOprlogType.ROLE_OPR);
		}
		
		return null;
	}
	
	
}
