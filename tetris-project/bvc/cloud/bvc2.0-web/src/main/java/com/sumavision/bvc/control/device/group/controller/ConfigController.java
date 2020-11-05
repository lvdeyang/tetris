package com.sumavision.bvc.control.device.group.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.service.ConfigServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * @ClassName: 议程和方案通用的接口<br> 
 * @author lvdeyang 
 * @date 2018年9月4日 下午2:08:31 
 */
@Controller
@RequestMapping(value = "/config")
public class ConfigController {

	@Autowired
	private DeviceGroupConfigVideoDAO deviceGroupConfigVideoDao;
	
	@Autowired
	private ConfigServiceImpl configService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * @Title: 删除一个视频配置<br/> 
	 * @param id 视频
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/video/{id}")
	public Object removeVideo(@PathVariable Long id, HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		deviceGroupConfigVideoDao.delete(id);
		
		logService.logsHandle(user.getName(), "删除一个视频配置", "视频id："+id);
		
		return null;
	}
	
	/**
	 * @Title: 设置分屏轮询状态 <br/>
	 * @param videoId 视频id
	 * @param serialNum 屏幕序号
	 * @param pollingStatus 视频轮询状态
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/set/polling/status/{videoId}")
	public Object setPollingStatus(
			@PathVariable Long videoId,
			int serialNum,
			String pollingStatus,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO configVideo = configService.switchPollingNext(videoId, serialNum);
		
		logService.logsHandle(user.getName(), "设置分屏轮询状态 ", "视频名称："+configVideo.getName()+"屏幕序号："+serialNum);
		
		return null;
	}
	
	/**
	 * @Title: 切换到轮询中的下一画面 <br/>
	 * @param videoId 视频id
	 * @param serialNum 屏幕序号
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/switch/polling/next/{videoId}")
	public Object switchPollingNext(
			@PathVariable Long videoId,
			int serialNum,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO configVideo = configService.switchPollingNext(videoId, serialNum);
		
		logService.logsHandle(user.getName(), "轮询屏切换至下一个画面", "视频名称："+configVideo.getName()+"屏幕序号："+serialNum);
		
		return null;
	}
	
	/**
	 * @Title: 切换到轮询中的某一指定画面 <br/>
	 * @param videoId 视频id
	 * @param serialNum 屏幕序号
	 * @param pollingIndex 画面序号
	 * @param sourceParam 源的参数，json串
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/switch/polling/index/{videoId}")
	public Object switchPollingIndex(
			@PathVariable Long videoId,
			int serialNum,
			int pollingIndex,
			String sourceParam,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		JSONObject param = JSON.parseObject(sourceParam);
		
		DeviceGroupConfigVideoPO configVideo = configService.switchPollingIndex(videoId, serialNum, pollingIndex, param);
		
		logService.logsHandle(user.getName(), "轮询屏切换至指定画面", "视频名称："+configVideo.getName()+"屏幕序号："+serialNum+"切换索引index："+pollingIndex);
		
		return null;
	}
	
}
