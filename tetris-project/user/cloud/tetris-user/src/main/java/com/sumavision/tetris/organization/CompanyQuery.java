package com.sumavision.tetris.organization;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

/**
 * 公司查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月24日 上午11:34:06
 */
@Component
public class CompanyQuery {

	@Autowired
	private CompanyDAO companyDao;
	
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
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", view_companies)
												   .getMap();
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
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<CompanyPO> companies = companyDao.findAllOrderByUpdateTimeDesc(page);
		return companies.getContent();
	}
	
}
