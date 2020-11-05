package com.sumavision.tetris.capacity.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-capacity", configuration = FeignConfiguration.class)
public interface CapacityFeign{

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
	 * 修改流转码<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午16:15:20
	 * @param String taskInfo 流转码信息
	 */
	@RequestMapping(value = "/capacity/transcode/feign/modify")
	public JSONObject modifyTranscode(@RequestParam("taskInfo") String taskInfo) throws Exception;

	/**
	 * 修改流转码输入<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月24日 下午16:15:20
	 * @param String taskInfo 流转码信息
	 */
	@RequestMapping(value = "/capacity/transcode/feign/put/input")
	public JSONObject modifyTranscodeInput(@RequestParam("inputInfo") String inputInfo) throws Exception;

	/**
	 * 获取支持的平台<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午16:15:20
	 * @param String taskInfo 流转码信息
	 */
	@RequestMapping(value = "/capacity/transcode/feign/platform")
	public JSONObject getPlatform(@RequestParam("ip") String ip) throws Exception;

	/**
	 * 删除流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:15:51
	 * @param String id 流转码任务标识
	 */
	@RequestMapping(value = "/capacity/transcode/feign/delete")
	public JSONObject deleteTranscode(@RequestParam("task") String task) throws Exception;
	
	/**
	 * 添加流转码输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 上午11:53:32
	 * @param Long task_id 任务id
	 * @param Integer system_type 系统类型
	 * @param String output_array 输出数组
	 */
	@RequestMapping(value = "/capacity/transcode/feign/add/output")
	public JSONObject addTranscodeOutput(@RequestParam("taskId") Long taskId,
										 @RequestParam("systemType") Integer systemType,
										 @RequestParam("output") String output) throws Exception;
	
	/**
	 * 删除流转码任务输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午1:36:46
	 * @param Long taskId 任务id
	 * @param Long outputId 输出id
	 */
	@RequestMapping(value = "/capacity/transcode/feign/delete/output")
	public JSONObject deleteTranscodeOutput(@RequestParam("taskId") Long taskId,
											@RequestParam("outputId") Long outputId) throws Exception;

	
	/**
	 * 重启流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午9:15:51
	 * @param String id 流转码任务标识
	 */
	@RequestMapping(value = "/capacity/transcode/feign/reboot")
	public JSONObject rebootTranscode(@RequestParam("task") String task) throws Exception;
	
	/**
	 * 停止流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午10:15:51
	 * @param String id 流转码任务标识
	 */
	@RequestMapping(value = "/capacity/transcode/feign/stop")
	public JSONObject stopTranscode(@RequestParam("task") String task) throws Exception;
	
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
	 * 同步转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月8日 下午5:28:51
	 * @param String deviceIp 转换模块ip
	 */
	@RequestMapping(value = "/capacity/transcode/feign/sync")
	public JSONObject sync(@RequestParam("deviceIp") String deviceIp) throws Exception;
	
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
	
	/**
	 * 添加push任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 上午8:54:26
	 * @param String task push任务信息
	 * @return String 任务标识
	 */
	@RequestMapping(value = "/push/task/feign/add/task")
	public JSONObject addPushTask(@RequestParam("task") String task) throws Exception;
	
	/**
	 * 删除push任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 上午8:57:34
	 * @param String taskId 任务标识
	 */
	@RequestMapping(value = "/push/task/feign/delete/task")
	public JSONObject deletePushTask(@RequestParam("taskId") String taskId) throws Exception;
	
	/**
	 * 切换push节目单<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 上午9:00:15
	 * @param String changeSchedule 节目单信息
	 */
	@RequestMapping(value = "/push/task/feign/change/schedule")
	public JSONObject changePushSchedule(@RequestParam("changeSchedule") String changeSchedule) throws Exception;
	
	/**
	 * 批量删除push任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月27日 上午11:19:10
	 * @param String taskIds 任务ids
	 */
	@RequestMapping(value = "/push/task/feign/batch/delete/task")
	public JSONObject batchDeletePushTask(@RequestParam("taskIds") String taskIds) throws Exception;

	/**
	 * 请求排期任务
	 * @param taskId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/push/task/feign/clear/task")
	public JSONObject clearPushTask(@RequestParam("taskId") String taskId) throws Exception;

	/**
	 * 设置告警列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月29日 下午3:36:45
	 * @param String ip 转换模块ip
	 * @param String alarmlist 告警列表
	 */
	@RequestMapping(value = "/capacity/transcode/feign/put/alarmlist")
	public JSONObject putAlarmlist(@RequestParam("ip") String ip,
									@RequestParam("alarmlist") String alarmlist) throws Exception;
	
	/**
	 * 重置转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午5:26:18
	 * @param ip
	 */
	@RequestMapping(value = "/capacity/transcode/feign/remove/all")
	public JSONObject removeAll(@RequestParam("ip") String ip) throws Exception;
	
	@RequestMapping(value = "/capacity/package/feign/add/task")
	public JSONObject packageAddTask(
			@RequestParam("deviceIp") String deviceIp,
			@RequestParam("port") String port,
			@RequestParam("dstIp") String dstIp,
			@RequestParam("dstPort") String dstPort) throws Exception;
	
	@RequestMapping(value = "/capacity/package/feign/delete/task")
	public JSONObject packageDeleteTask(@RequestParam("taskId") String taskId) throws Exception;

	@RequestMapping(value = "/capacity/transcode/feign/streamAnalysis")
	public JSONObject streamAnalysis(@RequestParam("analysis") String analysis) throws Exception;


	@RequestMapping(value = "/capacity/transcode/feign/add/input")
	public JSONObject addInputs(@RequestParam("inputInfo") String inputInfo) throws Exception;

	@RequestMapping(value = "/capacity/transcode/feign/preview/input")
	public JSONObject previewInput(@RequestParam("inputInfo") String inputInfo) throws Exception;


	@RequestMapping(value = "/director/task/feign/get/encode/template")
	public JSONObject getEncodeTemplate(@RequestParam("encodeType") String encodeType) throws Exception;

	@RequestMapping(value = "/director/task/feign/task/add")
	public JSONObject addDirectorTask(@RequestParam("taskInfo") String taskInfo) throws Exception;

	@RequestMapping(value = "/director/task/feign/task/delete")
	public JSONObject delDirectorTask(@RequestParam("task") String taskInfo) throws Exception;

	@RequestMapping(value = "/director/task/feign/task/switch")
	public JSONObject switchDirectorTask(@RequestParam("task") String taskInfo) throws Exception;

	@RequestMapping(value = "/director/task/feign/task/transfer")
	public JSONObject transferTask(@RequestParam("task") String taskInfo) throws Exception;


}
