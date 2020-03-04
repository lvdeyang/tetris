package com.sumavision.tetris.capacity.server;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-capacity", configuration = FeignConfiguration.class)
public interface CapacityFeign {

	/**
	 * 添加收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:40:40
	 * @param String recordInfo 收录信息
	 * @return String 收录标识
	 */
	@RequestMapping(value = "/capacity/record/feign/add")
	public JSONObject addRecord(@RequestParam("recordInfo") String recordInfo) throws Exception;
	
	/**
	 * 停止收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:42:22
	 * @param String id 收录标识
	 */
	@RequestMapping(value = "/capacity/record/feign/delete")
	public JSONObject deleteRecord(@RequestParam("id") String id) throws Exception;
	
	/**
	 * 添加流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:15:20
	 * @param String transcodeInfo 流转码信息
	 */
	@RequestMapping(value = "/capacity/transcode/feign/add")
	public JSONObject addTranscode(@RequestParam("transcodeInfo") String transcodeInfo) throws Exception;
	
	/**
	 * 删除流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:15:51
	 * @param String id 流转码任务标识
	 */
	@RequestMapping(value = "/capacity/transcode/feign/delete")
	public JSONObject deleteTranscode(@RequestParam("id") String id) throws Exception;
	
	/**
	 * 刷源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午5:10:24
	 * @param String analysisInput 源信息
	 */
	@RequestMapping(value = "/capacity/transcode/feign/analysis/input")
	public JSONObject analysisInput(@RequestParam("analysisInput") String analysisInput) throws Exception;
	
	/**
	 * 设置告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午3:00:44
	 * @param String capacityIp 能力ip
	 */
	@RequestMapping(value = "/capacity/alarm/feign/set/alarmUrl")
	public JSONObject setAlarmUrl(@RequestParam("capacityIp") String capacityIp) throws Exception;
	
	/**
	 * 设置心跳地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午11:09:52
	 * @param String capacityIp 能力ip
	 */
	@RequestMapping(value = "/capacity/heartbeat/feign/set/heartbeatUrl")
	public JSONObject setHeartbeatUrl(
			@RequestParam("capacityIp") String capacityIp,
			@RequestParam("heartbeatUrl") String heartbeatUrl) throws Exception;
	
	/**
	 * 切换备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午9:06:43
	 * @param String inputId 备份源输入id
	 * @param String index 索引
	 * @param String capacityIp 能力ip
	 */
	@RequestMapping(value = "/capacity/transcode/feign/change/backup")
	public JSONObject changeBackup(
			@RequestParam("inputId") String inputId,
			@RequestParam("index") String index,
			@RequestParam("capacityIp") String capacityIp) throws Exception;
	
	/**
	 * 转码任务添加盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:18:54
	 * @param String taskId 集群转码任务id
	 * @param String input cover输入
	 */
	@RequestMapping(value = "/capacity/transcode/feign/add/cover")
	public JSONObject addCover(
			@RequestParam("taskId") String taskId,
			@RequestParam("input") String input) throws Exception;
	
	/**
	 * 转码任务删除盖播<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:20:06
	 * @param String taskId 集群转码任务id
	 */
	@RequestMapping(value = "/capacity/transcode/feign/delete/cover")
	public JSONObject deleteCover(@RequestParam("taskId") String taskId) throws Exception;
	
	/**
	 * 添加导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:33:50
	 * @param String tasks 导播任务
	 */
	@RequestMapping(value = "/director/task/feign/add")
	public JSONObject addDirector(@RequestParam("tasks") String tasks) throws Exception;
	
	/**
	 * 删除导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:33:50
	 * @param String tasks 导播任务
	 */
	@RequestMapping(value = "/director/task/feign/delete")
	public JSONObject deleteDirector(@RequestParam("tasks") String tasks) throws Exception;
	
	/**
	 * 添加导播任务输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:33:50
	 * @param String outputs 导播输出
	 */
	@RequestMapping(value = "/director/task/feign/add/output")
	public JSONObject addOutput(@RequestParam("outputs") String outputs) throws Exception;
	
	/**
	 * 添加导播任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午11:33:50
	 * @param String outputs 导播输出
	 */
	@RequestMapping(value = "/director/task/feign/delete/output")
	public JSONObject deleteOutput(@RequestParam("outputs") String outputs) throws Exception;
	
	/**
	 * 添加应急广播任务输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午8:34:36
	 * @param String id 任务标识
	 * @param String outputParam 输出
	 */
	@RequestMapping(value = "/capacity/transform/feign/add/output")
	public JSONObject addTransformOutput(
			@RequestParam("id") String id,
			@RequestParam("outputParam") String outputParam) throws Exception;
	
	/**
	 * 删除应急广播任务输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午8:35:44
	 * @param String id 任务标识
	 * @param String outputParam 输出
	 */
	@RequestMapping(value = "/capacity/transform/feign/delete/output")
	public JSONObject deleteTransformOutput(
			@RequestParam("id") String id,
			@RequestParam("outputParam") String outputParam) throws Exception;
	
	/**
	 * 删除应急广播任务全部输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午8:37:00
	 * @param String id 任务标识
	 */
	@RequestMapping(value = "/capacity/transform/feign/delete/all")
	public JSONObject deleteTransformAllOutput(@RequestParam("id") String id) throws Exception;
}
