package com.sumavision.tetris.easy.process.media.editor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-easy-process", configuration = FeignConfiguration.class)
public interface MediaEditorFeign {
	
	/**
	 * 获取云转码模板列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return List<String>
	 */
	@RequestMapping(value = "/media/editor/feign/template/list")
	public JSONObject getTemplates() throws Exception;
	
	/**
	 * 添加转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param transcodeJobs 转码任务信息
	 * @return processId 流程任务id
	 * @return mediaTask 流程任务
	 */
	@RequestMapping(value = "/media/editor/feign/start/process")
	public JSONObject start(
			@RequestParam("transcodeJob") String transcodeJob,
			@RequestParam("param") String param,
			@RequestParam("name") String name,
			@RequestParam("folderId") Long folderId,
			@RequestParam("tags") String tags) throws Exception;
}
