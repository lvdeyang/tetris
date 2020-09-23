/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午4:32:29
 */
@Component
public class GuideQuery {
	
	@Autowired
	GuideDAO guideDAO;
	
	public List<GuideVO> queryTask() throws Exception{
		List<GuidePO> list = guideDAO.findAll();
		return GuideVO.getConverter(GuideVO.class).convert(list, GuideVO.class);
	}
}
