/**
 * 
 */
package com.sumavision.tetris.guide.control;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午7:21:20
 */
public class SourceService {

	@Autowired
	SourceDAO sourceDAO;
	
	public SourcePO edit(
			Long id,
			SourceType sourceType,
			String sourceName,
			String sourceAddress){
		SourcePO sourcePO = sourceDAO.findOne(id);
		sourcePO.setSourceType(sourceType);
		sourcePO.setSourceName(sourceName);
		sourcePO.setSourceAddress(sourceAddress);
		return sourceDAO.save(sourcePO);
	}
	
	public void delete(Long id){
		sourceDAO.delete(id);
	}
}
