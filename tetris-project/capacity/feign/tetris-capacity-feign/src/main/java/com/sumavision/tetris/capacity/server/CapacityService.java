package com.sumavision.tetris.capacity.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class CapacityService {

	@Autowired
	private CapacityFeign capacityFeign;

	/**
	 * 添加收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:46:43
	 * @param String recordInfo 收录信息
	 * @return String 收录标识
	 */
	public String addRecord(String recordInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addRecord(recordInfo), String.class);
	}
	
	/**
	 * 停止收录<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午10:47:20
	 * @param String id 收录标识
	 */
	public void deleteRecord(String id) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteRecord(id), null);
	}
	
	/**
	 * 添加流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:19:33
	 * @param String transcodeInfo 流转码信息
	 */
	public String addTranscode(String transcodeInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addTranscode(transcodeInfo), String.class);
	}

	/**
	 * 修改流转码<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午16:15:20
	 * @param String taskInfo 流转码信息
	 */
	public String modifyTranscode(String taskInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.modifyTranscode(taskInfo), String.class);
	}

	/**
	 * 修改流转码输入<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月24日 下午16:15:20
	 * @param String taskInfo 流转码信息
	 */
	public String modifyTranscodeInput(String inputInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.modifyTranscodeInput(inputInfo), String.class);
	}

	/**
	 * 获取支持的平台<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午16:15:20
	 * @param String ip 流转码信息
	 */
	public String getPlatform(String ip) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.getPlatform(ip), String.class);
	}

	/**
	 * 删除流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 上午9:20:04
	 * @param String task 任务信息
	 */
	public String deleteTranscode(String task) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.deleteTranscode(task), String.class);
	}
	
	/**
	 * 重启流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午9:20:04
	 * @param String task 任务信息
	 */
	public String rebootTranscode(String task) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.rebootTranscode(task), String.class);
	}
	
	/**
	 * 停止流转码<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月14日 上午9:20:04
	 * @param String task 任务信息
	 */
	public String stopTranscode(String task) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.stopTranscode(task), String.class);
	}
	
	/**
	 * 添加流转码输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午12:00:06
	 * @param Long task_id 任务id
	 * @param Integer system_type 系统类型
	 * @param String output_array 输出数组
	 */
	public void addTranscodeOutput(Long task_id, Integer system_type, String output_array) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.addTranscodeOutput(task_id, system_type, output_array), null);
	}
	
	/**
	 * 删除流转码输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月22日 下午1:39:04
	 * @param Long task_id 任务id
	 * @param Long output_id 输出id
	 */
	public void deleteTranscodeOutput(Long task_id, Long output_id) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteTranscodeOutput(task_id, output_id), null);
	}
	
	/**
	 * 转码刷源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午5:11:50
	 * @param String analysisInput
	 * @return String
	 */
	public String analysisInput(String analysisInput) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.analysisInput(analysisInput), String.class);
	}

	/**
	 * 媒资刷源，接口从媒资服务调的（应急广播业务需求）<br/>
	 * <b>作者:</b>yzx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 上午9:11:50
	 * @param String analysisInput
	 * @return String
	 */
	public String analysisInput(String deviceIp,String url) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.analysisInput(deviceIp,url), String.class);
	}
	
	/**
	 * 设置告警地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月13日 下午3:03:15
	 * @param String capacityIp 能力ip
	 */
	public void setAlarmUrl(String capacityIp) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.setAlarmUrl(capacityIp), null);
	}
	
	/**
	 * 设置心跳地址<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午11:10:50
	 * @param String capacityIp 能力ip
	 */
	public void setHeartbeatUrl(String capacityIp, String heartbeatUrl) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.setHeartbeatUrl(capacityIp, heartbeatUrl), null);
	}
	
	/**
	 * 切换备份源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 上午9:26:06
	 * @param String inputId 备份源id
	 * @param String index 索引
	 * @param String capacityIp 能力ip
	 */
	public void changeBackup(String inputId, String index, String capacityIp) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.changeBackup(inputId, index, capacityIp), null);
	}
	
	/**
	 * 转码任务添加盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:23:16
	 * @param String taskId 集群转码任务id
	 * @param String input cover输入
	 */
	public void addCover(String taskId, String input) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.addCover(taskId, input), null);
	}
	
	/**
	 * 转码任务删除盖播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:23:53
	 * @param String taskId 集群转码任务id
	 */
	public void deleteCover(String taskId) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.deleteCover(taskId), null);
	}
	
	/**
	 * 同步转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月8日 下午5:31:15
	 * @param String deviceIp 转换模块ip
	 */
	public String sync(String syncObj) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.sync(syncObj), String.class);
	}
	
	/**
	 * 设置告警列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月29日 下午3:40:36
	 * @param String deviceIp 转换模块ip
	 * @param String alarmlist 告警列表
	 */
	public void putAlarmlist(String deviceIp, String alarmlist) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.putAlarmlist(deviceIp, alarmlist), null);
	}
	
	/**
	 * 重置转换模块<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午6:36:47
	 * @param String deviceIp 转换模块ip
	 */
	public void removeAll(String deviceIp) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.removeAll(deviceIp), null);
	}

	public String streamAnalysis(String analysis) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.streamAnalysis(analysis), String.class);
	}

	public String addInputs(String inputInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addInputs(inputInfo), String.class);
	}

	public String previewInput(String inputInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.previewInput(inputInfo), String.class);
	}

	public String getEncodeTemplate(String encodeType) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.getEncodeTemplate(encodeType), String.class);
	}

	/**
	 * @MethodName: addTask
	 * @Description: 通过模板下发任务
	 * @param taskInfo 任务参数
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2020/12/10 16:27
	 **/
	public String addTaskByTemplate(String taskInfo) throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addTaskByTemplate(taskInfo), String.class);
	}

	/**
	 * @MethodName: getAllTemplate
	 * @Description: 获取所有模板
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2020/12/11 13:18
	 **/
	public String getAllTemplate() throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.getAllTemplate(), String.class);
	}

/**
 * @MethodName: refreshSource
 * @Description: TODO 通用刷源接口
 * @param source 1
 * @Return: java.lang.String
 * @Author: Poemafar
 * @Date: 2021/2/1 11:49
 **/
	public String refreshSource(String source)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.refreshSource(source),String.class);
	}

	/**
	 * @MethodName: deleteTask
	 * @Description: TODO 通用删任务接口：导播用
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2021/1/29 14:26
	 **/
	public String deleteTask(String task)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.deleteTask(task),String.class);
	}

	/**
	 * @MethodName: switchBackupForTask
	 * @Description: TODO 任务切换备源
	 * @param task 1
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2021/1/29 15:38
	 **/
	public String switchBackupForTask(String task)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.switchBackupForTask(task),String.class);
	}

	/**
	 * @MethodName: addSourcesForTask
	 * @Description: TODO 已有任务添加源
	 * @param task 1
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2021/1/29 15:40
	 **/
	public String addSourcesForTask(String task)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addSourcesForTask(task),String.class);
	}

	/**
	 * @MethodName: deleteSourceForTask
	 * @Description: TODO 已有任务删除源
	 * @param task 1
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2021/1/29 15:43
	 **/
	public String deleteSourceForTask(String task)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.deleteSourceForTask(task),String.class);
	}

	/**
	 * @MethodName: addOutputsForTask
	 * @Description: TODO 已有任务添加输出
	 * @param task 1
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2021/1/29 15:44
	 **/
	public String addOutputsForTask(String task)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.addOutputsForTask(task),String.class);
	}

	/**
	 * @MethodName: deleteOutputForTask
	 * @Description: TODO 已有任务删除输出
	 * @param task 1
	 * @Return: java.lang.String
	 * @Author: Poemafar
	 * @Date: 2021/1/29 15:45
	 **/
	public String deleteOutputForTask(String task)throws Exception{
		return JsonBodyResponseParser.parseObject(capacityFeign.deleteOutputForTask(task),String.class);
	}


}
