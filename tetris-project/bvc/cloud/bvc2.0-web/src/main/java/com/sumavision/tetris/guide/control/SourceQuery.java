/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午7:14:44
 */
public class SourceQuery {
	
	@Autowired
	SourceDAO sourceDAO;
	
	public List<SourceVO> query() throws Exception{
		List<SourcePO> list = sourceDAO.findAll();
		return SourceVO.getConverter(SourceVO.class).convert(list, SourceVO.class);
	}
}
