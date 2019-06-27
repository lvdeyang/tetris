package com.sumavision.tetris.subordinate.role;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.sumavision.tetris.organization.CompanyVO;
import com.sumavision.tetris.user.UserVO;
import com.thoughtworks.xstream.mapper.Mapper.Null;

/**
 * 隶属角色查询操作<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月29日 上午11:34:06
 */
@Component
public class SubordinateRoleQuery {
	@Autowired
	private SubordinateRoleDAO subordinateRoleDAO;
	
	public List<SubordinateRoleVO> getListFromCompany(Long companyId) throws Exception{
		List<SubordinateRolePO> rolePOs = subordinateRoleDAO.findByCompanyId(companyId);
		
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
		List<SubordinateRolePO> rolePOs = subordinateRoleDAO.findByCompanyIdAndClassify(companyId, SubordinateRoleClassify.INTERNAL_COMPANY_ORDINARY_ROLE);
		
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
		List<SubordinateRolePO> rolePOs = subordinateRoleDAO.findByCompanyIdAndClassify(companyId, SubordinateRoleClassify.INTERNAL_COMPANY_ADMIN_ROLE);
		if (rolePOs.size()>0) {
			List<SubordinateRoleVO> roleVOs = SubordinateRoleVO.getConverter(SubordinateRoleVO.class).convert(rolePOs, SubordinateRoleVO.class);
			return roleVOs.get(0);
		}
		return null;
		
	}
	
	/**
	 * 根据ids获取角色组<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午 10:54:22
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> getRolesByIds(List<Long> ids)throws Exception{
		List<SubordinateRoleVO> list = new ArrayList<SubordinateRoleVO>();;
		for(int i =0;i<ids.size();i++){
			Long roleId = ids.get(i);
			SubordinateRolePO po = subordinateRoleDAO.findById(roleId);
			if (!(po == null)){
				SubordinateRoleVO vo = new SubordinateRoleVO();
				vo = vo.set(po);
				list.add(vo);
			}	
		}
		return list;
	}
	
	/**
	 * 根据id获取角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午 10:54:22
	 * @return SubordinateRoleVO 角色列表
	 */
	public SubordinateRoleVO getRoleById(Long id)throws Exception{
		SubordinateRolePO po = subordinateRoleDAO.findById(id);
		SubordinateRoleVO vo = new SubordinateRoleVO();
		vo = vo.set(po);
		return vo;
	}
	
}
