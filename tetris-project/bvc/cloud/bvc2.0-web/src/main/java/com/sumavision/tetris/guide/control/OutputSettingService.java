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
 * <b>日期：</b>2020年9月4日 上午10:38:24
 */
public class OutputSettingService {
	
	@Autowired
	OutputSettingDAO outputSettingDAO;
	
	public OutputSettingPO edit(
			Long id,
			String outputProtocol,
			String outputAddress
			){
		OutputSettingPO outputSettingPO = outputSettingDAO.findOne(id);
		outputSettingPO.setId(id);
		outputSettingPO.setOutputProtocol(outputProtocol);
		outputSettingPO.setOutputAddress(outputAddress);
		return outputSettingDAO.save(outputSettingPO);
	}
	
	public void delete(Long id){
		outputSettingDAO.delete(id);
	}

}
