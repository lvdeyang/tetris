package com.sumavision.tetris.subordinate.role;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 隶属角色查询操作<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月29日 上午11:34:06
 */
@Component
public class SubordinateRoleQuery {
	
	@Autowired
	private SubordinateRoleDAO subordinateRoleDao;
	
	/**
	 * 根据公司id查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月12日 下午4:57:47
	 * @param Long companyId 公司id
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> getListFromCompany(Long companyId) throws Exception{
		List<SubordinateRolePO> rolePOs = subordinateRoleDao.findByCompanyId(companyId);
		List<SubordinateRoleVO> roleVOs = SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(rolePOs, SubordinateRoleVO.class);
		return roleVOs;
	}
	
	/**
	 * 获取普通角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午 10:54:22
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> getOrdinaryListFromCompany(Long companyId) throws Exception{
		List<SubordinateRolePO> rolePOs = subordinateRoleDao.findByCompanyIdAndClassify(companyId, SubordinateRoleClassify.INTERNAL_COMPANY_ORDINARY_ROLE);
		
		List<SubordinateRoleVO> roleVOs = SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(rolePOs, SubordinateRoleVO.class);
		
		return roleVOs;
	}
	
	/**
	 * 获取公司角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午 10:54:22
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public SubordinateRoleVO getRoleCompany(Long companyId) throws Exception{
		List<SubordinateRolePO> rolePOs = subordinateRoleDao.findByCompanyIdAndClassify(companyId, SubordinateRoleClassify.INTERNAL_COMPANY_ADMIN_ROLE);
		if (rolePOs.size()>0) {
			List<SubordinateRoleVO> roleVOs = SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(rolePOs, SubordinateRoleVO.class);
			return roleVOs.get(0);
		}
		return null;
		
	}
	
	/**
	 * 根据ids获取角色组<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午 10:54:22
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> findByIdIn(Collection<Long> ids)throws Exception{
		List<SubordinateRolePO> entities = subordinateRoleDao.findByIdIn(ids);
		return SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(entities, SubordinateRoleVO.class);
	}
	
	/**
	 * 根据id获取角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午 10:54:22
	 * @return SubordinateRoleVO 角色列表
	 */
	public SubordinateRoleVO findById(Long id)throws Exception{
		SubordinateRolePO entity = subordinateRoleDao.findOne(id);
		return new SubordinateRoleVO().set(entity);
	}
	
	/**
	 * 根据公司查询业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午8:42:36
	 * @param Long companyId 公司id
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> findByCompanyId(Long companyId) throws Exception{
		List<SubordinateRolePO> entities = subordinateRoleDao.findByCompanyId(companyId);
		return SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(entities, SubordinateRoleVO.class);
	}
	
	/**
	 * 根据公司查询业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午8:48:19
	 * @param Long companyId 公司id
	 * @param Collection<Long> except 例外角色列表
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> findByCompanyIdWithExcept(Long companyId, Collection<Long> except) throws Exception{
		if(except==null || except.size()<=0) return findByCompanyId(companyId);
		List<SubordinateRolePO> entities = subordinateRoleDao.findByCompanyIdWithExcept(companyId, except);
		return SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(entities, SubordinateRoleVO.class);
	}
	
}
