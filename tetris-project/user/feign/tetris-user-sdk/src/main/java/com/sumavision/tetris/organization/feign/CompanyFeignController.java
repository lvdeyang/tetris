package com.sumavision.tetris.organization.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.organization.CompanyQuery;

public class CompanyFeignController {

	@Autowired
	private CompanyQuery companyQuery;
	
	/**
	 * 根据id查询企业<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:54:26
	 * @param Long id 企业id
	 * @return CompanyVO 企业基本信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id")
	public Object findById(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		return companyQuery.findById(id);
	}
	
}
