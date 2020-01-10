package com.sumavision.tetris.transcoding.api.process;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.transcoding.addTask.AddTaskService;

@Controller
@RequestMapping(value = "/api/process/transcoding")
public class ApiProcessTranscodingController {
	@Autowired
	private AddTaskService addTaskService;
	
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
	public Object addTask(String transcodeJob, String param, String name, Long folderId, String tags, String __processInstanceId__,
			Long __accessPointId__, HttpServletRequest request) throws Exception {

		HashMapWrapper<String, MediaEditorTaskRatePermissionVO> ids = addTaskService.add(__processInstanceId__, __accessPointId__, transcodeJob, param, name, folderId, tags);
		
		return ids != null && ids.size() > 0 ? new HashMapWrapper<String, HashMapWrapper<String, MediaEditorTaskRatePermissionVO>>().put("transcodeIds", ids)
				   .getMap() : null;
	}
}
