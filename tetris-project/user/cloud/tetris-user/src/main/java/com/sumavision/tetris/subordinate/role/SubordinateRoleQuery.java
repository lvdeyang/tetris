package com.sumavision.tetris.subordinate.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.organization.CompanyVO;
import com.sumavision.tetris.user.UserVO;

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
}
