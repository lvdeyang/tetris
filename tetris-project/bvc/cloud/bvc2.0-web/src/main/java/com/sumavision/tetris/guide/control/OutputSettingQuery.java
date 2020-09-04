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
 * <b>日期：</b>2020年9月4日 上午10:38:06
 */
public class OutputSettingQuery {
	
	@Autowired
	OutputSettingDAO outputSettingDAO;
	
	public List<OutputSettingVO> query() throws Exception{
		List<OutputSettingPO> list = outputSettingDAO.findAll();
		return OutputSettingVO.getConverter(OutputSettingVO.class).convert(list, OutputSettingVO.class);
	}

}
