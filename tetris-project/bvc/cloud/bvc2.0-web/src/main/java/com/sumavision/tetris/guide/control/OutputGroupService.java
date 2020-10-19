/**
 * 
 */
package com.sumavision.tetris.guide.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月27日 下午3:21:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OutputGroupService {
	
	@Autowired
	private OutputGroupDAO outputGroupDAO;
	
//	@Autowired
//	private VideoParametersDAO videoParametersDAO;
	
//	@Autowired
//	private AudioParametersDAO audioParametersDAO;
	
	@Autowired
	private OutputSettingDAO outputSettingDAO;
	
	/**
	 * 
	 * 添加输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:01:42
	 * @param name 输出组名称
	 * @return 输出组VO
	 * @throws Exception
	 */
	public OutputGroupVO add(String name) throws Exception{
		OutputGroupPO outputGroupPO = new OutputGroupPO();
		outputGroupPO.setName(name);
		outputGroupDAO.save(outputGroupPO);
		/*VideoParametersPO videoParametersPO = new VideoParametersPO();
		videoParametersPO.setGroupId(outputGroupPO.getId());
		videoParametersDAO.save(videoParametersPO);
		AudioParametersPO audioParametersPO = new AudioParametersPO();
		audioParametersPO.setGroupId(outputGroupPO.getId());
		audioParametersDAO.save(audioParametersPO);*/
		OutputSettingPO outputSettingPO = new OutputSettingPO();
		outputSettingPO.setGroupId(outputGroupPO.getId());
		outputSettingDAO.save(outputSettingPO);
		return new OutputGroupVO().set(outputGroupPO);
	}
	
	/**
	 * 
	 * 修改输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:02:29
	 * @param groups 输出组VO集合
	 * @return
	 * @throws Exception
	 */
	public 	Object edit(String groups) throws Exception{
		List<OutputGroupVO> list1 = JSON.parseArray(groups, OutputGroupVO.class);
		
		List<OutputGroupPO> list2 = new ArrayList<OutputGroupPO>();
		
		for (OutputGroupVO e : list1) {
			OutputGroupPO outputGroupPO = outputGroupDAO.findOne(e.getId());
			outputGroupPO.setName(e.getName());
			SwitchingMode mode = SwitchingMode.fromName(e.getSwitchingModeName());
			outputGroupPO.setSwitchingMode(mode);
			if(e.getSwitchingModeName().equals("转码")){
				outputGroupPO.setTranscodingTemplate(e.getTranscodingTemplate());
			}else{
				outputGroupPO.setTranscodingTemplate(null);
			}
			list2.add(outputGroupPO);
		}
		outputGroupDAO.save(list2);
		return null;
	}
	
	/**
	 * 
	 * 删除输出组<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:08:19
	 * @param id 输出组id
	 */
	public void delete(Long id){
		/*VideoParametersPO videoParametersPO = videoParametersDAO.findByGroupId(id);
		if(videoParametersPO != null){
			videoParametersDAO.delete(videoParametersPO);
		}
		AudioParametersPO audioParametersPO = audioParametersDAO.findByGroupId(id);
		if(audioParametersPO != null){
			audioParametersDAO.delete(audioParametersPO);
		}*/
		List<OutputSettingPO> list= outputSettingDAO.findByGroupId(id);
		if(list.size() != 0){
			outputSettingDAO.deleteInBatch(list);
		}
		outputGroupDAO.delete(id);
	}
}
