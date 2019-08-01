package com.sumavision.tetris.transcoding;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskQuery;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionQuery;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionService;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionVO;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.CachedHttpServletRequestWrapper;
import com.sumavision.tetris.transcoding.addTask.AddTaskService;
import com.sumavision.tetris.transcoding.getStatus.GetStatusService;
import com.sumavision.tetris.transcoding.getTemplates.TemplatesRequest;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/qt/transcoding")
public class TranscodingController {
	
	@Autowired
	UserQuery userQuery;

	@Autowired
	AddTaskService addTaskService;
	
	@Autowired
	GetStatusService getStatusService;

	@Autowired
	TemplatesRequest templatesRequest;
	
	@Autowired
	ProcessService processService;
	
	@Autowired
	MediaEditorTaskQuery mediaEditorTaskQuery;
	
	@Autowired
	MediaEditorTaskRatePermissionService mediaEditorTaskRatePermissionService;
	
	@Autowired
	MediaEditorTaskRatePermissionQuery mediaEditorTaskRatePermissionQuery;
	
	@Autowired
	Adapter adapter;

	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/template/list")
	public Object getTemplates(HttpServletRequest request) {
		return templatesRequest.getTemplates();
	}

	/**
	 * 添加转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param transcodeJobs 转码任务信息
	 * @param __processInstanceId__ 流程任务id
	 * @param __accessPointId__ 流程节点id
	 * @return transcodeIds 流程任务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(String transcodeJob, Long folderId, String tags, String __processInstanceId__,
			Long __accessPointId__, HttpServletRequest request) throws Exception {

		HashMapWrapper<String, MediaEditorTaskRatePermissionVO> ids = addTaskService.add(__processInstanceId__, __accessPointId__, transcodeJob , folderId, tags);
		
		return ids != null && ids.size() > 0 ? new HashMapWrapper<String, HashMapWrapper<String, MediaEditorTaskRatePermissionVO>>().put("transcodeIds", ids)
				   .getMap() : null;
	}
	
	/**
	 * 流程任务完成回调<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/complete/notify")
	public Object addTask(HttpServletRequest request) throws Exception{

		CachedHttpServletRequestWrapper requestWrapper = new CachedHttpServletRequestWrapper(request);
		String completeString = new String(requestWrapper.getBytes(), "utf-8");
		
		adapter.completeNotiry(completeString);

		return null;
	}
	
	/**
	 * 添加转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param transcodeJobs 转码任务信息
	 * @return processId 流程任务id
	 * @return mediaTask 流程任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/process")
	public Object start(String transcodeJob, Long folderId, String tags, HttpServletRequest request) throws Exception{
		JSONObject variables = new JSONObject();
		variables.put("_pa3_transcodeJob", transcodeJob);
		variables.put("_pa3_folderId", folderId);
		
		List<String> tagList = (tags == null || tags.isEmpty()) ? new ArrayList<String>() : JSONObject.parseArray(tags, String.class);
		variables.put("_pa3_tags", StringUtils.join(tagList.toArray(), ","));
		
		String processInstanceId = processService.startByKey("_media_editor_transcoding_by_qt", variables.toJSONString(), null, null);
		
		MediaEditorTaskVO task = mediaEditorTaskQuery.getByProcessId(processInstanceId);
		
		return new HashMapWrapper<String, Object>().put("processId", processInstanceId)
				.put("mediaTask", task)
				.getMap();
	}
	
	/**
	 * 根据用户获取流程任务列表列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<MediaEditorTaskVO> 流程任务列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/status/list")
	public Object getStatusList(HttpServletRequest request) throws Exception {
		UserVO userVO = userQuery.current();
		
		return mediaEditorTaskQuery.getTaskTree(userVO);
	}
}
