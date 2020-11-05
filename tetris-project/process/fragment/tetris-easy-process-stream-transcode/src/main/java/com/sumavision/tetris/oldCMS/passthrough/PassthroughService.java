package com.sumavision.tetris.oldCMS.passthrough;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.streamTranscoding.exception.HttpRequestErrorException;

@Service
@Transactional(rollbackFor = Exception.class)
public class PassthroughService {
	
	/**
	 * 流透传添加任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:43:38
	 * @param record 是否录制(默认不录制)
	 * @param fileName 录制的文件名(可不设置，则使用默认名称)
	 * @param assetId 转发源素材id
	 * @param callBackURL 录制文件采集完成后回调地址
	 * @param recAddr 转发流源地址
	 * @param destAddrs 转发输出地址
	 * @return Long uniqId 添加的任务id
	 */
	public Long addTask(Boolean record, String fileName, Long assetId, String callBackURL, String recAddr, String  destAddrs) throws Exception {
		JSONObject requestJSON = new JSONObject();
		
		requestJSON.put("record", record);
		requestJSON.put("fileName", fileName);
		requestJSON.put("assetId", assetId);
		requestJSON.put("callBackURL", callBackURL);
		requestJSON.put("recAddr", recAddr);
		requestJSON.put("destAddrs", destAddrs);
		
		JSONObject responseJSON = HttpRequestUtil.httpPost(PassthroughRequestType.ADD_TASK.getUrl(), requestJSON);
		
		if (responseJSON.get("errMsg").equals("success")) {
			Long uniqId = responseJSON.getLong("uniqId");
			return uniqId;
		} else {
			throw new HttpRequestErrorException(PassthroughRequestType.ADD_TASK.getAction());
		}
	}
	
	/**
	 * 流透传添加输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:42:58
	 * @param uniqId 发布任务的id
	 * @param destAddrs 添加的输出任务
	 */
	public void addOutput(Long uniqId, String destAddrs) throws Exception {
		JSONObject requestJSON = new JSONObject();
		
		requestJSON.put("uniqId", uniqId);
		requestJSON.put("destAddrs", destAddrs);
		
		JSONObject responseJSON = HttpRequestUtil.httpPost(PassthroughRequestType.ADD_OUTPUT.getUrl(), requestJSON);
		
		if (!responseJSON.get("errMsg").equals("success")) {
			throw new HttpRequestErrorException(PassthroughRequestType.ADD_OUTPUT.getAction());
		}
	}
	
	/**
	 * 流透传删除输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:42:14
	 * @param uniqId 发布任务的id
	 * @param destAddrs 删除的输出任务
	 */
	public void deleteOutput(Long uniqId, String destAddrs) throws Exception {
		JSONObject requestJSON = new JSONObject();
		
		requestJSON.put("uniqId", uniqId);
		requestJSON.put("destAddrs", destAddrs);
		
		JSONObject responseJSON = HttpRequestUtil.httpPost(PassthroughRequestType.DELETE_OUTPUT.getUrl(), requestJSON);
		
		if (!responseJSON.get("errMsg").equals("success")) {
			throw new HttpRequestErrorException(PassthroughRequestType.DELETE_OUTPUT.getAction());
		}
	}
}
