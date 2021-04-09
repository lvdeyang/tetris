package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/custom/audio")
public class CustomAudioController {

	@Autowired
	private CustomAudioService customAudioService;
	
	/**
	 * 添加议程音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午5:25:12
	 * @param Long agendaId 议程id
	 * @param JSONString roleChannelIds 角色通道id列表
	 * @param JSONString groupMemberChannelIds 会议成员通道id列表
	 * @return ist<CustomAudioVO> 音频列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/agenda/audio")
	public Object addAgendaAudio(
			Long agendaId,
			String roleChannelIds,
			String groupMemberChannelIds,
			HttpServletRequest request) throws Exception{
		
		return customAudioService.addAgendaAudio(agendaId, roleChannelIds, groupMemberChannelIds);
	}

	/**
	 * 删除议程自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 上午10:37:30
	 * @param Long id 自定义音频id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			HttpServletRequest request) throws Exception{
		
		customAudioService.remove(id);
		return null;
	}
	
}
