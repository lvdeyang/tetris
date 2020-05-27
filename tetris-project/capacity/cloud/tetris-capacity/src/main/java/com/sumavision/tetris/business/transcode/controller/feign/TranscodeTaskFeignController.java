package com.sumavision.tetris.business.transcode.controller.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.transcode.service.ExternalTaskService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.AnalysisInputVO;
import com.sumavision.tetris.business.transcode.vo.TaskVO;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/capacity/transcode/feign")
public class TranscodeTaskFeignController {
	
	@Autowired
	private TranscodeTaskService transcodeTaskService;
	
	@Autowired
	private ExternalTaskService externalTaskService;
	
	@Autowired
	private SyncService syncService;

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
		
		if(transcode.getSystem_type() == null || transcode.getSystem_type().equals(0)){
			transcodeTaskService.addTranscodeTask(transcode);
		}else if(transcode.getSystem_type().equals(1)){
			return externalTaskService.addExternalTask(transcode);
		}
		
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
			String task,
			HttpServletRequest request) throws Exception{
		
		TaskVO taskVO = JSONObject.parseObject(task, TaskVO.class);
		
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			transcodeTaskService.deleteTranscodeTask(taskVO.getTask_id());
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.deleteExternalTask(taskVO);
		}
		
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
	
	/**
	 * 重启转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午10:46:18
	 * @param String task 任务信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/reboot")
	public Object reboot(
			String task,
			HttpServletRequest request) throws Exception{
		
		TaskVO taskVO = JSONObject.parseObject(task, TaskVO.class);
		
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.rebootExternalTask(taskVO);
		}
		
		return null;
	}
	
	/**
	 * 停止转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午10:46:18
	 * @param String task 任务信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String task,
			HttpServletRequest request) throws Exception{
		
		TaskVO taskVO = JSONObject.parseObject(task, TaskVO.class);
		
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.stopExternalTask(taskVO);
		}
		
		return null;
	}
	
	/**
	 * 转码添加输出br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 上午11:42:33
	 * @param Long taskId 任务id
	 * @param Integer systemType 系统类型
	 * @param String output 添加输出(数组)
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(
			Long taskId,
			Integer systemType,
			String output,
			HttpServletRequest request) throws Exception{
		
		if(systemType == 0){
			List<OutputBO> outputs = JSONArray.parseArray(output, OutputBO.class);
			transcodeTaskService.addOutput(taskId, outputs);
		}else{
			//TODO
		}
		
		return null;
	}
	
	/**
	 * 删除流转码输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午1:34:04
	 * @param Long taskId 任务id
	 * @param Long outputId 输出id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output")
	public Object deleteOutput(
			Long taskId,
			Long outputId,
			HttpServletRequest request) throws Exception{
		
		transcodeTaskService.deleteOutput(taskId, outputId);
		
		return null;
	}
	
	/**
	 * 同步转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月8日 下午4:43:31
	 * @param String deviceIp 转换模块ip
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/sync")
	public Object sync(
			String deviceIp,
			HttpServletRequest request) throws Exception{
		
		syncService.sync(deviceIp);
		return null;
	}
}
