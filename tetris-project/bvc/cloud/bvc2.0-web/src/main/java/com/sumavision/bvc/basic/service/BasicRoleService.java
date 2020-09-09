package com.sumavision.bvc.basic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.basic.bo.BasicRoleBO;
import com.sumavision.bvc.basic.dao.BasicRoleDAO;
import com.sumavision.bvc.basic.enumeration.RoleSpecial;
import com.sumavision.bvc.basic.exception.RoleNameAlreadyExsitedException;
import com.sumavision.bvc.basic.po.BasicRolePO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class BasicRoleService {
	
	@Autowired
	private BasicRoleDAO basicRoleDao;

	/**
	 * 新建角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午9:52:47
	 * @param BasicRoleBO roleBo 角色信息
	 * @param boolean doPersistence 是否持久化
	 * @return BasicRolePO _role 角色信息
	 */
	public BasicRolePO saveRole(BasicRoleBO roleBo, boolean doPersistence) throws Exception{
		
		//校验
		BasicRolePO role = basicRoleDao.findByName(roleBo.getName());
		if(role != null){
			throw new RoleNameAlreadyExsitedException(roleBo.getName());
		}
		
		BasicRolePO _role = new BasicRolePO();
		_role.setName(roleBo.getName());
		_role.setSpecial(RoleSpecial.fromName(roleBo.getSpecial()));
		_role.setVirtualDevice(roleBo.isVirtualDevice());
		
		if(doPersistence) basicRoleDao.save(_role);
		
		return _role;
	}
	
	/**
	 * 批量新建角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午10:17:45
	 * @param Collection<BasicRoleBO> roleBOs roleBOs
	 * @param boolean doPersistence 是否持久化
	 * @return List<BasicRolePO>
	 */
	public List<BasicRolePO> batchSaveRoles(Collection<BasicRoleBO> roleBOs, boolean doPersistence) throws Exception{
		
		//校验
		List<String> names = new ArrayList<String>();
		for(BasicRoleBO roleBO: roleBOs){
			names.add(roleBO.getName());
		}
		
		List<BasicRolePO> roles = basicRoleDao.findByNameIn(names);
		if(roles != null && roles.size() > 0){
			StringBufferWrapper sameNames = new StringBufferWrapper();;
			for(BasicRolePO role: roles){
				sameNames.append(role.getName()).append(" ");
			}
			throw new RoleNameAlreadyExsitedException(sameNames.toString());
		}
		
		List<BasicRolePO> _roles = new ArrayList<BasicRolePO>();
		for(BasicRoleBO roleBO: roleBOs){
			BasicRolePO _role = new BasicRolePO();
			_role.setName(roleBO.getName());
			_role.setSpecial(RoleSpecial.fromName(roleBO.getSpecial()));
			_role.setVirtualDevice(roleBO.isVirtualDevice());
			
			_roles.add(_role);
		}
		
		if(doPersistence) basicRoleDao.save(_roles);
		
		return _roles;
	}
	
	/**
	 * 根据id删除角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午10:53:36
	 * @param Long id 角色id
	 */
	public void removeRole(Long id) throws Exception{
		
		basicRoleDao.delete(id);
		
	}
	
	/**
	 * 根据id批量删除<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 上午10:54:31
	 * @param Collection<Long> ids 角色id列表
	 */
	public void batchRemoveRoles(Collection<Long> ids) throws Exception{
		
		basicRoleDao.deleteByIdIn(ids);
	
	}
	
//	@Autowired
	public void generateDefaultRoles(){
		List<BasicRoleBO> _roles = new ArrayList<BasicRoleBO>();
		if(null == basicRoleDao.findByName("主席")){
			BasicRoleBO role = new BasicRoleBO("主席", "发言人", false);
			_roles.add(role);
			BasicRoleBO role1 = new BasicRoleBO();
		}
		if(null == basicRoleDao.findByName("会议员")){
			BasicRoleBO role = new BasicRoleBO("会议员", "观众", false);
			_roles.add(role);
		}
		if(_roles.size() > 0){
			try {
				batchSaveRoles(_roles, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
