package com.sumavision.tetris.business.transcode.controller.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sumavision.tetris.business.transcode.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.transcode.service.ExternalTaskService;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/capacity/transcode/feign")
public class TranscodeTaskFeignController {

	private static final Logger LOG = LoggerFactory.getLogger(TranscodeTaskFeignController.class);

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
		
		LOG.info("[sts]<add-task>(req) hash: {}\nbody: {}",transcodeInfo.hashCode(),transcodeInfo);
		TranscodeTaskVO transcode = JSONObject.parseObject(transcodeInfo, TranscodeTaskVO.class);
		if(transcode.getSystem_type() == null || transcode.getSystem_type().equals(0)){
			transcodeTaskService.addTranscodeTask(transcode);
		}else if(transcode.getSystem_type().equals(1)){
			return externalTaskService.addExternalTask(transcode);
		}
		LOG.info("[sts]<add-task>(resp). hash: {}",transcodeInfo.hashCode());
		return null;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/input")
	public Object addInput(
			String inputInfo,
			HttpServletRequest request) throws Exception{

		LOG.info("[sts]<create-inputs>(req) hash: {}\nbody: {}",inputInfo.hashCode(),inputInfo);
		CreateInputsVO inputsVO = JSONObject.parseObject(inputInfo, CreateInputsVO.class);
		String result = transcodeTaskService.addInputs(inputsVO);
		LOG.info("[sts]<create-inputs>(resp). hash: {}",inputInfo.hashCode());
		return result;
	}

    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/preview/input")
    public Object previewInput(
            String inputInfo,
            HttpServletRequest request) throws Exception{

        LOG.info("[sts]<preview-input>(req) hash: {}, body: {}",inputInfo.hashCode(),inputInfo);
        CreateInputPreviewVO inputVO = JSONObject.parseObject(inputInfo, CreateInputPreviewVO.class);
        transcodeTaskService.previewInput(inputVO);
        LOG.info("[sts]<preview-input>(resp). hash: {}",inputInfo.hashCode());
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
		LOG.info("[sts]<delete-task>(req) \n {}", task);
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			transcodeTaskService.deleteTranscodeTask(taskVO.getTask_id());
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.deleteExternalTask(taskVO);
		}
		LOG.info("[sts]<delete-task>(resp) \n {}", task);
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
		LOG.info("[sts]<analysis-input>(req) \n input: {}", analysisInput);
		AnalysisInputVO analysisInputVO = JSONObject.parseObject(analysisInput, AnalysisInputVO.class);
		String response = transcodeTaskService.analysisInput(analysisInputVO);
		LOG.info("[sts]<analysis-input>(resp) \n  {}", response);
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
			String mode,
			String capacityIp,
			HttpServletRequest request) throws Exception{
		LOG.info("[sts]<change-backup>(req) \n inputId: {}, index: {}", inputId,index);
		transcodeTaskService.changeBackUp(inputId, index,mode, capacityIp);
		LOG.info("[sts]<change-backup>(resp) \n inputId: {}, index: {}", inputId,index);

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
		LOG.info("[sts]<add-cover>(req) \n taskId: {}, input: {}", taskId,input);
		InputBO inputBO = JSONObject.parseObject(input, InputBO.class);
		transcodeTaskService.addCover(taskId, inputBO);
		LOG.info("[sts]<add-cover>(resp) \n taskId: {}", taskId,input);
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
		LOG.info("[sts]<delete-cover>(req) \n taskId: {}", taskId);
		transcodeTaskService.deleteCover(taskId);
		LOG.info("[sts]<delete-cover>(resp) \n taskId: {}", taskId);
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
		LOG.info("[sts]<restart-task>(req) \n task: {}", task);
		TaskVO taskVO = JSONObject.parseObject(task, TaskVO.class);
		
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.rebootExternalTask(taskVO);
		}
		LOG.info("[sts]<restart-task>(resp) \n task: {}", task);
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
		LOG.info("[sts]<stop-task>(req) \n task: {}", task);
		TaskVO taskVO = JSONObject.parseObject(task, TaskVO.class);
		
		if(taskVO.getSystem_type() == null || taskVO.getSystem_type().equals(0)){
			
		}else if(taskVO.getSystem_type().equals(1)){
			return externalTaskService.stopExternalTask(taskVO);
		}
		LOG.info("[sts]<stop-task>(resp) \n task: {}", task);
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
		LOG.info("[sts]<add-output>(req) \n taskId: {}, output: {}", taskId,output);
		if(systemType == 0){
			List<OutputBO> outputs = JSONArray.parseArray(output, OutputBO.class);
			transcodeTaskService.addOutput(taskId, outputs);
		}else{
			//TODO
		}
		LOG.info("[sts]<add-output>(resp) \n taskId: {}", taskId);
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
		LOG.info("[sts]<delete-output>(req) \n taskId: {}, outputId: {}", taskId,outputId);
		transcodeTaskService.deleteOutput(taskId, outputId);
		LOG.info("[sts]<delete-output>(resp) \n taskId: {}, outputId: {}", taskId,outputId);
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
		LOG.info("[sts]<sync>(req) \n deviceIp: {}", deviceIp);
		syncService.sync(deviceIp);
		LOG.info("[sts]<sync>(resp) \n deviceIp: {}", deviceIp);
		return null;
	}
	
	/**
	 * 设置告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月29日 下午3:34:09
	 * @param String ip 转换模块ip
	 * @param String alarmlist 告警列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/put/alarmlist")
	public Object putAlarmlist(
			String ip,
			String alarmlist,
			HttpServletRequest request) throws Exception{
		LOG.info("[sts]<put-alarm-list>(req) \n ip: {}, alarmList: {}", ip,alarmlist);
		transcodeTaskService.putAlarmList(ip, alarmlist);
		LOG.info("[sts]<put-alarm-list>(resp) \n ip: {}", ip);
		return null;
	}
	
	/**
	 * 重置转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午4:42:32
	 * @param String ip 转换模块ip
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(
			String ip,
			HttpServletRequest request) throws Exception{
		LOG.info("[sts]<remove-all>(req) \n ip: {}",ip);
		transcodeTaskService.removeAll(ip);
		LOG.info("[sts]<remove-all>(resp) \n ip: {}", ip);
		return null;
	}

	/**
	 * 修改任务<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午4:42:32
	 * @param String ip 转换模块ip
	 */
	@JsonBody
    @ResponseBody
    @RequestMapping(value = "/modify")
    public Object modifyTask(
            String taskInfo,
            HttpServletRequest request) throws Exception{
		LOG.info("[sts]<modify-task>(req) hash:{} \n task: {}",taskInfo.hashCode(),taskInfo);
        TaskSetVO taskSetVO = JSONObject.parseObject(taskInfo, TaskSetVO.class);
        transcodeTaskService.modifyTranscodeTask(taskSetVO);
		LOG.info("[sts]<modify-task>(resp) hash:{}",taskInfo.hashCode());
        return null;
    }

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/put/input")
	public Object modifyInput(
			String inputInfo,
			HttpServletRequest request) throws Exception{
		LOG.info("[sts]<modify-input>(req) hash:{} \n task: {}",inputInfo.hashCode(),inputInfo);
		InputSetVO inputSetVO = JSONObject.parseObject(inputInfo, InputSetVO.class);
		transcodeTaskService.modifyTranscodeInput(inputSetVO);
		LOG.info("[sts]<modify-input>(resp) hash:{}",inputInfo.hashCode());
		return null;
	}

    /**
     * 获取硬件平台<br/>
     * <b>作者:</b>yzx<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年6月5日 下午4:42:32
     * @param String ip 转换模块ip
     */
    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/platform")
    public Object getPlatform(
            String ip,
            HttpServletRequest request) throws Exception{
		LOG.info("[sts]<get-platform>(req) hash:{} \n ip: {}",ip.hashCode(),ip);
		String response = transcodeTaskService.getPlatform(ip);
		LOG.info("[sts]<get-platform>(resp) hash:{}",ip.hashCode());
        return response;
    }

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/streamAnalysis")
	public Object streamAnalysis(
			String analysis,
			HttpServletRequest request) throws Exception{
		LOG.info("[sts]<analysis-stream>(req) hash:{} \n analysis: {}",analysis.hashCode(),analysis);
		AnalysisStreamVO analysisStreamVO = JSONObject.parseObject(analysis, AnalysisStreamVO.class);
		String response = transcodeTaskService.analysisStream(analysisStreamVO);
		LOG.info("[sts]<analysis-stream>(resp) hash:{}, result:{}",analysis.hashCode(),response);
		return response;
	}




}
