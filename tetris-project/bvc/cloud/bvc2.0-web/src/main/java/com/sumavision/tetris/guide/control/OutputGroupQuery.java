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
 * <b>日期：</b>2020年9月27日 下午3:09:02
 */
@Component
public class OutputGroupQuery {
	
	@Autowired
	OutputGroupDAO outputGroupDAO;
	
	/**
	 * 
	 * 查询输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:57:17
	 * @param guideId 导播任务id
	 * @return 输出组VO集合
	 * @throws Exception
	 */
	public List<OutputGroupVO> query(Long guideId) throws Exception{
		List<OutputGroupPO> list = outputGroupDAO.findByGuideId(guideId);
		return OutputGroupVO.getConverter(OutputGroupVO.class).convert(list, OutputGroupVO.class);
	}
}
