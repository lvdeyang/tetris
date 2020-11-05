package com.sumavision.tetris.business.director.controller.feign;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.director.vo.SwitchSourceVO;
import com.sumavision.tetris.business.director.vo.TransferVO;
import com.sumavision.tetris.business.transcode.vo.AnalysisStreamVO;
import com.sumavision.tetris.business.transcode.vo.TaskVO;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.business.director.service.DirectorTaskService;
import com.sumavision.tetris.business.director.vo.DirectorTaskVO;
import com.sumavision.tetris.business.director.vo.OutputsVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/director/task/feign")
public class DirectorTaskFeignController {

	private static final Logger LOG = LoggerFactory.getLogger(DirectorTaskFeignController.class);

	@Autowired
	private DirectorTaskService directorTaskService;

	/**
	 * 添加导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午4:20:59
	 * @param String directors 导播任务参数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String tasks,
			HttpServletRequest request) throws Exception{
		
		List<DirectorTaskVO> directorTasks = JSONArray.parseArray(tasks, DirectorTaskVO.class);
		directorTaskService.addDirectorTask(directorTasks);
		
		return null;
		
	}
	
	/**
	 * 删除导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午5:50:36
	 * @param String tasks 需要删除的 导播任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			String tasks,
			HttpServletRequest request) throws Exception{
		
		List<String> taskIds = JSONArray.parseArray(tasks, String.class);
		directorTaskService.deleteDirectorTask(taskIds);
		
		return null;
	}
	
	/**
	 * 添加导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:26:42
	 * @param String outputs
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(
			String outputs,
			HttpServletRequest request) throws Exception{
		
		List<OutputsVO> addOutputs = JSONArray.parseArray(outputs, OutputsVO.class);
		directorTaskService.addOutput(addOutputs);
		
		return null;
	}
	
	/**
	 * 删除导播输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:28:53
	 * @param String outputs
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output")
	public Object deleteOutput(
			String outputs,
			HttpServletRequest request) throws Exception{
		
		List<OutputsVO> deleteOutputs = JSONArray.parseArray(outputs, OutputsVO.class);
		directorTaskService.deleteOutput(deleteOutputs);
		
		return null;
	}


	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/encode/template")
	public Object getEncodeTemplate(
			String encodeType,
			HttpServletRequest request) throws Exception{
		LOG.info("[director]<get-encode-template>(req) hash:{} \n encodeType: {}",encodeType.hashCode(),encodeType);
		String response = directorTaskService.getEncodeTemplateParamByEncodeType(encodeType);
		LOG.info("[director]<get-encode-tempalte>(resp) hash:{}, result:{}",encodeType.hashCode(),response);
		return response;
	}


	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String taskInfo,
			HttpServletRequest request) throws Exception{
		String uuid = UUID.randomUUID().toString();
		LOG.info("[director]<add-task>(req) msg: {}\nbody: {}",uuid,taskInfo);
		TranscodeTaskVO transcode = JSONObject.parseObject(taskInfo, TranscodeTaskVO.class);
		directorTaskService.addTask(transcode);
		LOG.info("[director]<add-task>(resp). hash: {}",uuid);
		return null;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/delete")
	public Object deleteTask(
			String task,
			HttpServletRequest request) throws Exception{
		String uuid = UUID.randomUUID().toString();
		LOG.info("[director]<delete-task>(req) msg: {}\nbody: {}",uuid,task);
		TaskVO taskVO = JSONObject.parseObject(task, TaskVO.class);
		directorTaskService.delTask(taskVO.getTask_id());
		LOG.info("[director]<delete-task>(resp). hash: {}",uuid);
		return null;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/switch")
	public Object switchTask(
			String task,
			HttpServletRequest request) throws Exception{
		String uuid = UUID.randomUUID().toString();
		LOG.info("[director]<switch-task>(req) msg: {}\nbody: {}",uuid,task);
		SwitchSourceVO switchSourceVO = JSONObject.parseObject(task, SwitchSourceVO.class);
		directorTaskService.switchTask(switchSourceVO.getJobId(),switchSourceVO.getInputId());
		LOG.info("[director]<switch-task>(resp). hash: {}",uuid);
		return null;
	}


	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/transfer")
	public Object transferTask(
			String task,
			HttpServletRequest request) throws Exception{
		String uuid = UUID.randomUUID().toString();
		LOG.info("[director]<transfer-task>(req) msg: {}\nbody: {}",uuid,task);
		TransferVO transferVO = JSONObject.parseObject(task, TransferVO.class);
		directorTaskService.transferTask(transferVO);
		LOG.info("[director]<transfer-task>(resp). hash: {}",uuid);
		return null;
	}

}
