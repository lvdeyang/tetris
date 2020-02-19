package com.sumavision.tetris.business.transcode.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 为了postman测试<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月17日 下午3:44:06
 */
@Controller
@RequestMapping(value = "/api/process")
public class TestController {
	
	@Autowired
	private TranscodeTaskService transcodeTaskService;

	/**
	 * 添加流转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:08:59
	 * @param String transcodeInfo 流转码信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/transcode/add")
	public Object add(
			String transcodeInfo,
			HttpServletRequest request) throws Exception{
		
		TranscodeTaskVO transcode = JSONObject.parseObject(transcodeInfo, TranscodeTaskVO.class);
		
		transcodeTaskService.addTranscodeTask(transcode);
		
		return null;
	}
	
	/**
	 * 删除流转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:10:39
	 * @param String id 任务标识
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/transcode/delete")
	public Object delete(
			String id,
			HttpServletRequest request) throws Exception{
		
		transcodeTaskService.deleteTranscodeTask(id);
		
		return null;
	}
	
}
