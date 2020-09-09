/**
 * 
 */
package com.sumavision.tetris.guide.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午7:21:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SourceService {

	@Autowired
	SourceDAO sourceDAO;
	
	public SourcePO edit(
			Long id,
			SourceType sourceType,
			String sourceName,
			String source){
		SourcePO sourcePO = sourceDAO.findOne(id);
		sourcePO.setSourceType(sourceType);
		sourcePO.setSourceName(sourceName);
		sourcePO.setSource(source);
		return sourceDAO.save(sourcePO);
	}
	
	public void delete(Long id){
		sourceDAO.delete(id);
	}
	
	public Object cut(
			Long id, 
			Long sourceNumber){
		return null;
	}
}
