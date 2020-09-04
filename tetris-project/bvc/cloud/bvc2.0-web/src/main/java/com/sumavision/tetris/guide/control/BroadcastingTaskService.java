/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.sumavision.tetris.guide.control.OutputSettingDAO;
import com.sumavision.tetris.guide.control.VideoParametersDAO;
import com.sumavision.tetris.guide.control.VideoParametersPO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午5:30:51
 */
public class BroadcastingTaskService {
	
	@Autowired
	GuideDAO guideDAO;
	
	@Autowired
	SourceDAO sourceDAO;
	
	@Autowired
	VideoParametersDAO videoParametersDAO;
	
	@Autowired
	OutputSettingDAO outputSettingDAO;
	
	public GuidePO add(String taskName){
		GuidePO guidePO = new GuidePO();
		guidePO.setTaskName(taskName);
		guidePO.setCreationTime(new Date());
		guidePO.setParameterNumber(new VideoParametersPO().getParameterNumber());
		guideDAO.save(guidePO);
		for(Long i = 1L; i <= 12; i++){
			SourcePO sourcePO = new SourcePO();
			sourcePO.setId(i);
			sourceDAO.save(sourcePO);
		}
		return guidePO;
	}
	
	public GuidePO edit(
			Long id,
			String taskName
			){
		GuidePO guidePO = guideDAO.findOne(id);
		guidePO.setTaskName(taskName);
		return guideDAO.save(guidePO);
	}
	
	public void delete(Long id){		
		videoParametersDAO.delete(guideDAO.findOne(id).getParameterNumber());
		outputSettingDAO.delete(outputSettingDAO.findByTaskNumber(id));
		guideDAO.delete(id);
	}

}
