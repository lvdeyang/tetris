/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


public class SourceQuery {
	
	@Autowired
	private SourceDAO sourceDao;
	
	/**
	 * 根据导播任务id查源<br/>
	 * <b>作者:</b>贾军<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 下午2:31:02
	 * @param Long id 导播任务id
	 * @return List<SourceVO> 源集合
	 */
	public List<SourceVO> query(Long id) throws Exception{
		
		List<SourcePO> list = sourceDao.findByGuidePO(id);
		return SourceVO.getConverter(SourceVO.class).convert(list, SourceVO.class);
	}
}
