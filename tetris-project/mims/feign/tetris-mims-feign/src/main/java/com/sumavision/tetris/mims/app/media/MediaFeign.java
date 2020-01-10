package com.sumavision.tetris.mims.app.media;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaFeign {

	/**
	 * 根据条件查询媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月19日 下午3:17:30
	 * @param Long id 媒资id
	 * @param String name 名称(模糊匹配)
	 * @param String startTime updateTime起始查询
	 * @param Stinrg endTime updateTime终止查询
	 * @param Long tagId 标签id
	 */
	@RequestMapping(value = "/media/feign/query/by/condition")
	public JSONObject queryByCondition(
			@RequestParam("id") Long id,
			@RequestParam("name") String name,
			@RequestParam("type") String type,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam("tagId") Long tagId) throws Exception;
	
	/**
	 * 从http目录获取目录下资源http地址(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月10日 上午9:04:08
	 * @param String url 目录地址
	 * @return String 文件http地址
	 */
	@RequestMapping(value = "/media/feign/query/by/url")
	public JSONObject queryByUrl(
			@RequestParam("url") String url) throws Exception;
}
