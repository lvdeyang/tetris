/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
	OutputSettingDAO outputSettingDAO;
	
	@Autowired
	VideoParametersDAO videoParametersDAO;
	
	@Autowired
	AudioParametersDAO audioParametersDAO;
	
	@Autowired
	private OutputGroupDAO outputGroupDAO;
	
	/**
	 * 
	 * 添加导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:48:17
	 * @param taskName 导播任务名称
	 * @return 添加的导播任务VO
	 * @throws Exception
	 */
	public GuideVO add(String taskName) throws Exception{
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
		OutputGroupPO outputGroupPO = new OutputGroupPO();
		outputGroupPO.setGuideId(guidePO.getId());
		outputGroupPO.setMonitorUuid(UUID.randomUUID().toString().replace("-", ""));
		outputGroupDAO.save(outputGroupPO);
		OutputSettingPO outputSettingPO = new OutputSettingPO();
		outputSettingPO.setGroupId(outputGroupPO.getId());
		outputSettingDAO.save(outputSettingPO);
		/*VideoParametersPO videoParametersPO = new VideoParametersPO();
		videoParametersPO.setGroupId(outputGroupPO.getId());
		videoParametersDAO.save(videoParametersPO);
		AudioParametersPO audioParametersPO = new AudioParametersPO();
		audioParametersPO.setGroupId(outputGroupPO.getId());
		audioParametersDAO.save(audioParametersPO);*/
		return new GuideVO().set(guidePO);
	}
	
	/**
	 * 
	 * 修改导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:49:42
	 * @param id 导播任务id
	 * @param taskName 导播任务名称
	 * @return 修改的导播任务VO
	 * @throws Exception
	 */
	public GuideVO edit(
			Long id,
			String taskName
			) throws Exception{
		GuidePO guidePO = guideDAO.findOne(id);
		guidePO.setTaskName(taskName);
		guideDAO.save(guidePO);
		return new GuideVO().set(guidePO);
	}
	
	/**
	 * 
	 * 删除导播任务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:50:42
	 * @param id 导播任务id
	 */
	public void delete(Long id){
/*		VideoParametersPO videoParametersPO = videoParametersDAO.findByGuideId(id);
		if(videoParametersPO != null){
			videoParametersDAO.delete(videoParametersPO);
		}
		AudioParametersPO audioParametersPO = audioParametersDAO.findByGuideId(id);
		if(audioParametersPO != null){
			audioParametersDAO.delete(audioParametersPO);
		}
		List<OutputSettingPO> list1= outputSettingDAO.findByGuideId(id);
		if(list1.size() != 0){
			outputSettingDAO.deleteInBatch(list1);
		}*/
		List<OutputGroupPO> list1 = outputGroupDAO.findByGuideId(id);
		if(list1.size() != 0){
			outputGroupDAO.deleteInBatch(list1);
		}
		
		List<SourcePO> list2 = sourceDAO.findByGuideIdOrderBySourceNumber(id);
		if(list2.size() != 0){
			sourceDAO.deleteInBatch(list2);
		}
		List<OutputSettingPO> list3 = new ArrayList<OutputSettingPO>();
		for (OutputGroupPO outputGroupPO : list1) {
			List<OutputSettingPO> list = outputSettingDAO.findByGroupId(outputGroupPO.getId());
			list3.addAll(list);
		}
		outputSettingDAO.deleteInBatch(list3);
		guideDAO.delete(id);
	}
	
	/**
	 * 
	 * 开始直播<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:51:26
	 * @param id 导播任务id
	 * @return
	 * @throws Exception
	 */
	public Object start(Long id) throws Exception{
		guidePlayService.start(id);
		return null;
	}
	
	/**
	 * 
	 * 停止直播<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午3:51:47
	 * @param id 导播任务id
	 * @return
	 * @throws Exception
	 */
	public Object stop(Long id) throws Exception{
		guidePlayService.stop(id);
		return null;
	}

}
