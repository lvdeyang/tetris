package com.sumavision.tetris.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class CompanyQuery {

	@Autowired
	private CompanyFeign companyFeign;
	
	/**
	 * 根据id查询企业<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年2月23日 下午4:54:26
	 * @param Long id 企业id
	 * @return CompanyVO 企业基本信息
	 */
	public CompanyVO findById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(companyFeign.findById(id), CompanyVO.class);
	}
	
}
