package com.sumavision.tetris.organization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.system.role.SystemRoleDAO;
import com.sumavision.tetris.system.role.SystemRolePO;
import com.sumavision.tetris.system.theme.SystemThemeDAO;
import com.sumavision.tetris.system.theme.SystemThemePO;

/**
 * 公司查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月24日 上午11:34:06
 */
@Component
public class CompanyQuery {

	@Autowired
	private SystemRoleDAO systemRoleDao;
	
	@Autowired
	private CompanyDAO companyDao;
	
	@Autowired
	private SystemThemeDAO systemThemeDao;
	
	/**
	 * 分页查询公司<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 上午11:43:25
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<CompanyVO> rows 公司列表
	 */
	public Map<String, Object> list(int currentPage, int pageSize) throws Exception{
		long total = companyDao.count();
		List<CompanyPO> companies = findAllOrderByUpdateTimeDesc(currentPage, pageSize);
		List<CompanyVO> view_companies = CompanyVO.getConverter(CompanyVO.class).convert(companies, CompanyVO.class);
		
		if(view_companies!=null && view_companies.size()>0){
			Set<Long> systemRoleIds = new HashSetWrapper<Long>().add(-1l).getSet();
			for(CompanyVO component:view_companies){
				if(component.getSystemRoleId() != null) systemRoleIds.add(component.getSystemRoleId());
			}
			List<SystemRolePO> systemRoles = systemRoleDao.findByIdIn(systemRoleIds);
			if(systemRoles!=null && systemRoles.size()>0){
				for(CompanyVO component:view_companies){
					for(SystemRolePO systemRole:systemRoles){
						if(systemRole.getId().equals(component.getSystemRoleId())){
							component.setSystemRoleName(systemRole.getName());
							break;
						}
					}
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", view_companies)
												   .getMap();
	}
	
	/**
	 * 查询全部的企业<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午2:22:09
	 * @return List<CompanyVO> 企业列表
	 */
	public List<CompanyVO> listAll() throws Exception{
		List<CompanyPO> companyEntities = companyDao.findAll();
		return CompanyVO.getConverter(CompanyVO.class).convert(companyEntities, CompanyVO.class);
	}
	
	/**
	 * 分页查询公司<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 上午11:36:54
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<CompanyPO> 公司列表
	 */
	public List<CompanyPO> findAllOrderByUpdateTimeDesc(int currentPage, int pageSize) throws Exception{
		Pageable page = PageRequest.of(currentPage-1, pageSize);
		Page<CompanyPO> companies = companyDao.findAllOrderByUpdateTimeDesc(page);
		return companies.getContent();
	}
	
	/**
	 * 查询用户的公司<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月8日 下午1:24:36
	 * @param Long userId 用户id
	 * @return CompanyVO 公司
	 */
	public CompanyVO findByUserId(Long userId) throws Exception{
		CompanyVO company = null;
		
		CompanyPO entity = companyDao.findByUserId(userId);
		if (entity != null) {
			company = new CompanyVO().set(entity);
		}
		
		SystemThemePO theme = null;
		if(company.getThemeId() == null){
			theme = systemThemeDao.findByUrl("");
		}else{
			theme = systemThemeDao.findById(company.getThemeId());
		}
		
		company.setThemeId(theme.getId())
			   .setThemeName(theme.getName())
			   .setThemeUrl(theme.getUrl());
		
		return company;
	}
	
	/**
	 * 根据id查询企业<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:54:26
	 * @param Long id 企业id
	 * @return CompanyVO 企业基本信息
	 */
	public CompanyVO findById(Long id) throws Exception{
		CompanyPO companyEntity = companyDao.findById(id);
		return new CompanyVO().set(companyEntity);
	}
}
