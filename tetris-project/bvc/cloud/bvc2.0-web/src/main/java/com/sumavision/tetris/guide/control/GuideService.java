/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.guide.business.GuidePlayService;
import com.sumavision.tetris.guide.control.OutputSettingDAO;
import com.sumavision.tetris.guide.control.VideoParametersDAO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月3日 下午5:30:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GuideService {
	
	@Autowired
	private GuidePlayService guidePlayService;
	
	@Autowired
	private GuideDAO guideDAO;
	
	@Autowired
	private SourceDAO sourceDAO;
	
	@Autowired
	private OutputSettingDAO outputSettingDAO;
	
	@Autowired
	private VideoParametersDAO videoParametersDAO;
	
	@Autowired
	private AudioParametersDAO audioParametersDAO;
	
//	@Autowired
//	GuidePlayService guidePlayService;
	
	public GuidePO add(String taskName){
		GuidePO guidePO = new GuidePO();
		guidePO.setTaskName(taskName);
		guidePO.setCreationTime(new Date());
		guideDAO.save(guidePO);
		List<SourcePO> list = new ArrayList<SourcePO>();
		for(Long i = 1L; i <= 12; i++){
			SourcePO sourcePO = new SourcePO();
			sourcePO.setSourceNumber(i);
			sourcePO.setGuideId(guidePO.getId());
			list.add(sourcePO);
		}
		sourceDAO.save(list);
		OutputSettingPO outputSettingPO = new OutputSettingPO();
		outputSettingDAO.save(outputSettingPO);
		VideoParametersPO videoParametersPO = new VideoParametersPO();
		videoParametersDAO.save(videoParametersPO);
		AudioParametersPO audioParametersPO = new AudioParametersPO();
		audioParametersDAO.save(audioParametersPO);
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
		videoParametersDAO.delete(videoParametersDAO.findByGuideId(id));
		audioParametersDAO.delete(audioParametersDAO.findByGuideId(id));
		outputSettingDAO.deleteInBatch(outputSettingDAO.findByGuideId(id));
		sourceDAO.deleteInBatch(sourceDAO.findByGuideIdOrderBySourceNumber(id));
		guideDAO.delete(id);
	}
	
	public Object start(Long id) throws Exception{
		guidePlayService.start(id);
		return null;
	}
	
	public Object stop(Long id) throws Exception{
		guidePlayService.stop(id);
		return null;
	}

}
