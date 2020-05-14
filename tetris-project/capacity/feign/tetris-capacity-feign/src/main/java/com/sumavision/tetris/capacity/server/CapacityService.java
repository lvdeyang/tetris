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
	 * 刷源<br/>
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
	public void sync(String deviceIp) throws Exception{
		JsonBodyResponseParser.parseObject(capacityFeign.sync(deviceIp), null);
	}
	
}
