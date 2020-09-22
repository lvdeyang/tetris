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
 * <b>日期：</b>2020年9月4日 上午10:38:06
 */

@Component
public class OutputSettingQuery {
	
	@Autowired
	OutputSettingDAO outputSettingDAO;
	
	@Autowired
	VideoParametersDAO videoParametersDAO;
	
	@Autowired
	AudioParametersDAO audioParametersDAO;
	
	public List<OutputSettingVO> query(Long guideId) throws Exception{
		List<OutputSettingPO> list = outputSettingDAO.findByGuideId(guideId);
		return OutputSettingVO.getConverter(OutputSettingVO.class).convert(list, OutputSettingVO.class);
	}
	
	public VideoParametersVO queryVideo(Long guideId) throws Exception{
		VideoParametersPO videoParametersPO = videoParametersDAO.findByGuideId(guideId);
		return new VideoParametersVO().set(videoParametersPO);
	}
	
	public AudioParametersVO queryAudio(Long guideId) throws Exception{
		AudioParametersPO audioParametersPO = audioParametersDAO.findByGuideId(guideId);
		return new AudioParametersVO().set(audioParametersPO);
	}

}
