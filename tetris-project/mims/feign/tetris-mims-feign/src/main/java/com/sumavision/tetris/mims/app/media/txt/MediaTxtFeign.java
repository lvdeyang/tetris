package com.sumavision.tetris.mims.app.media.txt;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaTxtFeign {

	/**
	 * 加载文件夹下的文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaTxtVO> 文本媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/txt/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 查询文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 上午9:25:28
	 * @param Long id 媒资id
	 * @return String 文本内容
	 */
	@RequestMapping(value = "/media/txt/feign/query/content")
	public JSONObject queryContent(@RequestParam("id") Long id) throws Exception;
	
	/**
	 * 数据库添加json文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午10:26:53
	 * @param String jsonContent json内容
	 * @param Long folderId 目录id
	 * @param String name 文件名
	 * @return MediaTxtVO
	 */
	@RequestMapping(value = "/media/txt/feign/add/json")
	public JSONObject addJson(
			@RequestParam("json") String json,
			@RequestParam("folderId") Long folderId,
			@RequestParam("name") String name) throws Exception;
}
