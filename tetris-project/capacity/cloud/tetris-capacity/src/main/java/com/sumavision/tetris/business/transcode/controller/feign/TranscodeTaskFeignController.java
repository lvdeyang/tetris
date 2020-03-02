package com.sumavision.tetris.business.transcode.controller.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.AnalysisInputVO;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/capacity/transcode/feign")
public class TranscodeTaskFeignController {
	
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
	@RequestMapping(value = "/add")
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
	@RequestMapping(value = "/delete")
	public Object delete(
			String id,
			HttpServletRequest request) throws Exception{
		
		transcodeTaskService.deleteTranscodeTask(id);
		
		return null;
	}
	
	/**
	 * 刷源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午5:08:41
	 * @param String analysisInput
	 * @return String 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/analysis/input")
	public Object analysisInput(
			String analysisInput,
			HttpServletRequest request) throws Exception{
		
		System.out.println("刷源信息" + analysisInput);
		AnalysisInputVO analysisInputVO = JSONObject.parseObject(analysisInput, AnalysisInputVO.class);
		String response = transcodeTaskService.analysisInput(analysisInputVO);
		System.out.println("刷源返回" + response);
		return response;
	}
	
	/**
	 * 切换备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午8:38:48
	 * @param String inputId 输入id
	 * @param String index 备份源索引
	 * @param String capacityIp 能力ip
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/backup")
	public Object changeBackup(
			String inputId,
			String index,
			String capacityIp,
			HttpServletRequest request) throws Exception{
		
		transcodeTaskService.changeBackUp(inputId, index, capacityIp);
		
		return null;
	}
	

	/**
	 * 添加转码任务盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:11:27
	 * @param String taskId 集群任务id
	 * @param String input cover输入
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/cover")
	public Object addCover(
			String taskId,
			String input,
			HttpServletRequest request) throws Exception{
		
		InputBO inputBO = JSONObject.parseObject(input, InputBO.class);
		transcodeTaskService.addCover(taskId, inputBO);
		
		return null;
	}
	
	/**
	 * 删除转码任务盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:16:04
	 * @param String taskId 集群任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/cover")
	public Object changeBackup(
			String taskId,
			HttpServletRequest request) throws Exception{
		
		transcodeTaskService.deleteCover(taskId);
		
		return null;
	}
	
}
