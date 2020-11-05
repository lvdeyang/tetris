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
	
	/**
	 * 
	 * 查询输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:18:10
	 * @param groupId 输出组id
	 * @return 输出VO集合
	 * @throws Exception
	 */
	public List<OutputSettingVO> query(Long groupId) throws Exception{
		List<OutputSettingPO> list = outputSettingDAO.findByGroupId(groupId);
		return OutputSettingVO.getConverter(OutputSettingVO.class).convert(list, OutputSettingVO.class);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:20:33
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public VideoParametersVO queryVideo(Long groupId) throws Exception{
		VideoParametersPO videoParametersPO = videoParametersDAO.findByGroupId(groupId);
		return new VideoParametersVO().set(videoParametersPO);
	}
	
	/**
	 * 
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月30日 下午4:21:09
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public AudioParametersVO queryAudio(Long groupId) throws Exception{
		AudioParametersPO audioParametersPO = audioParametersDAO.findByGroupId(groupId);
		return new AudioParametersVO().set(audioParametersPO);
	}

}
