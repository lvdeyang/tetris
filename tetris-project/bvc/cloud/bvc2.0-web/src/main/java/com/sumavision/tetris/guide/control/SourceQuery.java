/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceQuery {
	
	@Autowired
	private SourceDAO sourceDao;
	
	public List<SourceVO> query(Long guideId) throws Exception{
		
		List<SourcePO> list = sourceDao.findByGuideIdOrderBySourceNumber(guideId);
		return SourceVO.getConverter(SourceVO.class).convert(list, SourceVO.class);
	}
}
