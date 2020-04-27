package com.sumavision.tetris.capacity.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class PushService {
	
	@Autowired
	private CapacityFeign capacityFeign;

	/**
	 * 添加push转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 上午9:10:23
	 * @param String task push任务信息
	 * @return String taskId任务标识
	 */
	public String addPushTask(String task) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addPushTask(task), String.class);
	}
	
	/**
	 * 删除push转码任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 上午9:13:52
	 * @param String taskId 任务标识
	 */
	public void deletePushTask(String taskId) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deletePushTask(taskId), null);
	}
	
	/**
	 * 切换push备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 上午9:15:34
	 * @param String taskId 任务标识
	 * @param String index 切换到源的索引
	 */
	public void changePushBackup(String taskId, String index) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.changePushBackUp(taskId, index), null);
	}
	
	/**
	 * 批量删除push任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月27日 上午11:21:29
	 * @param List<String> taskIds 任务ids
	 */
	public void batchDeleteTask(List<String> taskIds) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.batchDeletePushTask(JSON.toJSONString(taskIds)), null);
	}
	
}
