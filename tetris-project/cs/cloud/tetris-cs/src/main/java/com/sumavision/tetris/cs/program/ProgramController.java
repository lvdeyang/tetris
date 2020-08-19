package com.sumavision.tetris.cs.program;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/program")
public class ProgramController {
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	/**
	 * 获取分屏信息(附带排单列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object programGet(Long id,HttpServletRequest request) throws Exception {
		
		ProgramVO program = programQuery.getProgram(id);

		return program;
	}
	
	/**
	 * 设置分屏信息(附带排单列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programInfo 整体分屏信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object programSet(Long scheduleId, String programInfo, String orient, HttpServletRequest request) throws Exception {
		
		TemplateVO templateVO = JSON.parseObject(programInfo, TemplateVO.class);
		
		channelService.changeScheduleDeal(scheduleQuery.getById(scheduleId).getChannelId());
		
		return programService.setProgram(scheduleId, templateVO, orient);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/content/type/get")
	public Object getContentType(Long channelId, HttpServletRequest request) throws Exception {
		List<ScreenContentTypeVO> screenContentTypes = new ArrayList<ScreenContentTypeVO>();
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (BroadWay.fromName(channel.getBroadWay()) != BroadWay.ABILITY_BROAD) {
			for (ScreenContentType screenContentType : ScreenContentType.values()) {
				switch (screenContentType) {
				case TERMINAL_MIMS:
				case TEXT:
				case TIME:
					screenContentTypes.add(new ScreenContentTypeVO().set(screenContentType));
					break;
				default:
					break;
				}
			}
		} else {
			for (ScreenContentType screenContentType : ScreenContentType.values()) {
				switch (screenContentType) {
				case ABILITY_AUDIO:
				case ABILITY_VIDEO:
				case ABILITY_PICTURE:
				case TEXT:
				case TIME:
					screenContentTypes.add(new ScreenContentTypeVO().set(screenContentType));
					break;
				default:
					break;
				}
			}
		}
		return screenContentTypes;
	}
}
