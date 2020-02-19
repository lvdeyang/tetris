package com.sumavision.tetris.transcoding.feign;

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
import com.sumavision.tetris.media.editor.task.MediaEditorTaskVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.transcoding.getTemplates.TemplatesRequest;

@Controller
@RequestMapping(value = "/media/editor/feign")
public class ApiFeignMediaEditorController {
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MediaEditorTaskQuery mediaEditorTaskQuery;
	
	@Autowired
	private TemplatesRequest templatesRequest;
	
	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<String>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/template/list")
	public Object getTemplates(HttpServletRequest request) throws Exception {
		return templatesRequest.getTemplates();
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
	public Object start(String transcodeJob, String param, String name, Long folderId, String tags, HttpServletRequest request) throws Exception{
		JSONObject variables = new JSONObject();
		variables.put("_pa3_transcodeJob", transcodeJob);
		variables.put("_pa3_param", param);
		variables.put("_pa3_name", name);
		variables.put("_pa3_folderId", folderId);
		variables.put("_pa3_tags", (tags == null || tags.isEmpty()) ? "" :StringUtils.join(JSONObject.parseArray(tags, String.class).toArray(), ","));
		
		String processInstanceId = processService.startByKey("_media_editor_transcoding_by_qt", variables.toJSONString(), null, null);
		
		MediaEditorTaskVO task = mediaEditorTaskQuery.getByProcessId(processInstanceId);
		
		return new HashMapWrapper<String, Object>().put("processId", processInstanceId)
				.put("mediaTask", task)
				.getMap();
	}
}
