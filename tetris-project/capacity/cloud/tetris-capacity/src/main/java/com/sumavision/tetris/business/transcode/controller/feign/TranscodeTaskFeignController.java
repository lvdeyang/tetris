package com.sumavision.tetris.business.transcode.controller.feign;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.application.annotation.OprLog;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.common.service.TaskModifyService;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.business.common.vo.SyncVO;
import com.sumavision.tetris.business.transcode.service.ExternalTaskService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.*;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.response.AnalysisResponse;
import com.sumavision.tetris.capacity.constant.Constant;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/capacity/transcode/feign")
public class TranscodeTaskFeignController {

	@Autowired
	private TranscodeTaskService transcodeTaskService;

	@Autowired
	private ExternalTaskService externalTaskService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskModifyService taskModifyService;

	@Autowired
	private SyncService syncService;

	/**
	 * 添加流转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:08:59
	 * @param String transcodeInfo 流转码信息
	 */
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addTask(
			String transcodeInfo) throws Exception{
		TranscodeTaskVO transcode = JSON.parseObject(transcodeInfo, TranscodeTaskVO.class);
		if(transcode.getSystem_type() == null || transcode.getSystem_type().equals(0)){
			transcodeTaskService.addTranscodeTask(transcode);
		}else if(transcode.getSystem_type().equals(1)){
			return externalTaskService.addExternalTask(transcode);
		}else {
			throw new IllegalArgumentException("system type:"+transcode.getSystem_type());
		}
		return null;
	}

	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/input")
	public Object addInput(
			String inputInfo,
			HttpServletRequest request) throws Exception{
		CreateInputsVO inputsVO = JSON.parseObject(inputInfo, CreateInputsVO.class);
		String result = transcodeTaskService.addInputs(inputsVO);
		return result;
	}

	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/preview/input")
	public Object previewInput(
			String inputInfo) throws Exception{
		InputPreviewVO inputVO = JSON.parseObject(inputInfo, InputPreviewVO.class);
		String result = taskService.previewInput(inputVO);
		return result;
	}

	/**
	 * 删除流转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:10:39
	 * @param String id 任务标识
	 */
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object deleteTask(
			String task) throws Exception{

		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			taskService.deleteTranscodeTask(taskVO);
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.deleteExternalTask(taskVO);
		}else {
			throw new IllegalArgumentException("system type:"+taskVO.getSystem_type());
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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/analysis/input")
	public Object analysisInput(
			String analysisInput) throws Exception{
		AnalysisInputVO analysisInputVO = JSON.parseObject(analysisInput, AnalysisInputVO.class);
		AnalysisResponse response = transcodeTaskService.analysisInput(analysisInputVO);
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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/backup")
	public Object changeBackup(
			String inputId,
			String index,
			String mode,
			String capacityIp,
			HttpServletRequest request) throws Exception{
		transcodeTaskService.changeBackUp(inputId, index,mode, capacityIp);

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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/cover")
	public Object addCover(
			String taskId,
			String input,
			HttpServletRequest request) throws Exception{
		InputBO inputBO = JSON.parseObject(input, InputBO.class);
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
	@OprLog(name = "sts")
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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/reboot")
	public Object restartTask(
			String task,
			HttpServletRequest request) throws Exception{
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);

		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){

		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.rebootExternalTask(taskVO);
		}else {
			throw new IllegalArgumentException("system type:"+taskVO.getSystem_type());
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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stopTask(
			String task,
			HttpServletRequest request) throws Exception{
		TaskVO taskVO = JSON.parseObject(task, TaskVO.class);

		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){

		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.stopExternalTask(taskVO);
		}else {
			throw new IllegalArgumentException("system type:"+taskVO.getSystem_type());
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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(
			Long taskId,
			Integer systemType,
			String output,
			HttpServletRequest request) throws Exception{
		if(systemType == 0){
			List<OutputBO> outputs = JSON.parseArray(output, OutputBO.class);
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
	@OprLog(name = "sts")
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
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/sync")
	public Object syncBusiness(
			String syncObj,
			HttpServletRequest request) throws Exception{
		SyncVO syncVO = JSON.parseObject(syncObj, SyncVO.class);
		String result = syncService.syncBusiness(syncVO);
		return result;
	}

	/**
	 * 设置告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月29日 下午3:34:09
	 * @param String ip 转换模块ip
	 * @param String alarmlist 告警列表
	 */
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/put/alarmlist")
	public Object putAlarmlist(
			String ip,
			String alarmlist,
			HttpServletRequest request) throws Exception{
		transcodeTaskService.putAlarmList(ip, alarmlist);
		return null;
	}

	/**
	 * 重置转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午4:42:32
	 * @param String ip 转换模块ip
	 */
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(
			String ip) throws Exception{
		taskService.removeAll(ip);
		return null;
	}

	/**
	 * 修改任务<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午4:42:32
	 * @param String ip 转换模块ip
	 */
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/modify")
	public Object modifyTask(
			String taskInfo) throws Exception{
		TaskSetVO taskSetVO = JSON.parseObject(taskInfo, TaskSetVO.class);
		taskModifyService.modifyTask(taskSetVO,BusinessType.TRANSCODE);
		return null;
	}

	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/put/input")
	public Object modifyInput(
			String inputInfo) throws Exception{
		InputSetVO inputSetVO = JSON.parseObject(inputInfo, InputSetVO.class);
		transcodeTaskService.modifyTranscodeInput(inputSetVO);
		return null;
	}

	/**
	 * 获取硬件平台<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午4:42:32
	 * @param String ip 转换模块ip
	 */
	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/platform")
	public Object getPlatform(
			String ip,
			HttpServletRequest request) throws Exception{
		Integer port = Constant.TRANSFORM_PORT;
		String response = transcodeTaskService.getPlatform(ip,port);
		return response;
	}

	@OprLog(name = "sts")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/streamAnalysis")
	public Object streamAnalysis(
			String analysis,
			HttpServletRequest request) throws Exception{
		AnalysisStreamVO analysisStreamVO = JSON.parseObject(analysis, AnalysisStreamVO.class);
		String response = transcodeTaskService.analysisStream(analysisStreamVO,BusinessType.TRANSCODE);
		return response;
	}

}
