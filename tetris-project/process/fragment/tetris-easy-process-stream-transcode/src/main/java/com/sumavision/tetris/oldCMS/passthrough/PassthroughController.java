package com.sumavision.tetris.oldCMS.passthrough;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/server/passthrough")
public class PassthroughController {
	
	@Autowired
	private PassthroughService passthroughService;
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(Boolean record, String fileName, Long assetId, String callBackURL, String recAddr, String  destAddrs, HttpServletRequest request) throws Exception {
		Long uniqId = passthroughService.addTask(record, fileName, assetId, callBackURL, recAddr, destAddrs);
		
		return new HashMapWrapper<String, Long>().put("uniqId", uniqId).getMap();
	}
	
	/**
	 * 流透传添加输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:42:58
	 * @param uniqId 发布任务的id
	 * @param destAddrs 添加的输出任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(Long uniqId, String destAddrs, HttpServletRequest request) throws Exception {
		passthroughService.addOutput(uniqId, destAddrs);
		
		return "";
	}
	
	/**
	 * 流透传删除输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 上午10:42:14
	 * @param uniqId 发布任务的id
	 * @param destAddrs 删除的输出任务
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output")
	public Object deleteOutput(Long uniqId, String destAddrs, HttpServletRequest request) throws Exception {
		passthroughService.deleteOutput(uniqId, destAddrs);
		
		return "";
	}
}
