package com.sumavision.tetris.business.transcode.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.transcode.vo.TaskVO;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.util.http.HttpUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExternalTaskService {

	/**
	 * 添加外部集群任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月10日 下午3:03:57
	 * @param TranscodeTaskVO transcodeTask 转码信息
	 * @return String response 返回
	 */
	public String addExternalTask(TranscodeTaskVO transcodeTask) throws Exception{
		
		String external_url = transcodeTask.getUrl();
		Object external_task = transcodeTask.getExternal_task_detail();
		
		JSONObject response = HttpUtil.httpPost(external_url, JSON.parseObject(JSON.toJSONString(external_task)));
		
		return JSON.toJSONString(response);
	}
	
	/**
	 * 删除外部集群任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午10:03:06
	 * @param TaskVO task 任务信息
	 * @return String response 返回
	 */
	public String deleteExternalTask(TaskVO task) throws Exception{
		
		String external_url = task.getUrl();
		String task_id = task.getTask_id();
		
		JSONObject post = new JSONObject();
		post.put("taskId", task_id);
		
		JSONObject response = HttpUtil.httpPost(external_url, post);
		
		return JSON.toJSONString(response);
	}
	
	/**
	 * 重启外部集群任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午10:42:16
	 * @param TaskVO task 任务信息
	 * @return String response 返回
	 */
	public String rebootExternalTask(TaskVO task) throws Exception{
		
		String external_url = task.getUrl();
		String task_id = task.getTask_id();
		
		JSONObject post = new JSONObject();
		post.put("taskId", task_id);
		
		JSONObject response = HttpUtil.httpPost(external_url, post);
		
		return JSON.toJSONString(response);
	}
	
	/**
	 * 停止外部集群任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午10:42:33
	 * @param TaskVO task 任务信息
	 * @return String response 返回
	 */
	public String stopExternalTask(TaskVO task) throws Exception{
		
		String external_url = task.getUrl();
		String task_id = task.getTask_id();
		
		JSONObject post = new JSONObject();
		post.put("taskId", task_id);
		
		JSONObject response = HttpUtil.httpPost(external_url, post);
		
		return JSON.toJSONString(response);
	}
	
}
