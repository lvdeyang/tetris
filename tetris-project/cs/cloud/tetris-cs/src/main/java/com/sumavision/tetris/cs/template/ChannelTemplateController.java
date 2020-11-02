package com.sumavision.tetris.cs.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.channel.ChannelVO;
import com.sumavision.tetris.cs.channel.SetAutoBroadBO;
import com.sumavision.tetris.cs.channel.SetOutputBO;
import com.sumavision.tetris.cs.channel.SetTerminalBroadBO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.live.MediaPushLiveVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cs/channel/template")
public class ChannelTemplateController {
	@Autowired
	ChannelTemplateService channelTemplateService;
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object tempList(Integer currentPage, Integer pageSize, HttpServletRequest request) throws Exception {
		return channelTemplateService.findAll(currentPage, pageSize);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String name,HttpServletRequest request) throws Exception {
		
		ChannelTemplatePO channelTemplatePO=channelTemplateService.add(name);
		return new ChannelTemplateVo().set(channelTemplatePO);
		
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id, HttpServletRequest request) throws Exception {

		channelTemplateService.remove(id);

		return "";
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/pro/list")
	public Object proList(Long tempId,Integer currentPage, Integer pageSize, HttpServletRequest request) throws Exception {
		return channelTemplateService.findAllPros(currentPage, pageSize,tempId);
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/pro/add")
	public Object proadd(long templateId,
			String startTime,
			String endTime,
			String programeType,
			String labelIds,
			String labelNames,
			String mimsId,
			String mimsName,
			String url,HttpServletRequest request) throws Exception {
		
		TemplateProgramePO templateProgramePO=channelTemplateService.addpro(templateId, 
				DateUtil.parse(startTime,"yyyy-MM-dd HH:mm:ss"),
				DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss"),
				ProgrameType.fromName(programeType), labelIds, labelNames, mimsId, mimsName, url);
		return new TemplateProgrameVo().set(templateProgramePO);
		
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/pro/remove")
	public Object proremove(Long id, HttpServletRequest request) throws Exception {

		channelTemplateService.removepro(id);

		return "";
	}
	
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/get/mims")
	public Object getMIMSResource(Long id, Long channelId, HttpServletRequest request) throws Exception {

		List<MediaAVideoVO> resources = channelTemplateService.getMIMSResources();
		List<MediaPushLiveVO> lives = channelTemplateService.getMIMSLiveResources();
		List<MediaPictureVO> pictures = channelTemplateService.getMIMSPictureResources();
		
		List<MediaVideoStreamVO> videoStreams = channelTemplateService.getMIMSVideoStreamResources();
		List<MediaAudioStreamVO> audioStreams = channelTemplateService.getMIMSAudioStreamResources();

		return new ArrayListWrapper<Object>()
				.addAll(resources)
				.addAll(lives)
				.addAll(pictures)
				.addAll(videoStreams)
				.addAll(audioStreams)
				.getList();
	}
	
}
