package com.sumavision.tetris.mims.app.media.video;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaVideoFeign {

	/**
	 * 加载文件夹下的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoVO> 视频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/video/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 加载所有视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@RequestMapping(value = "/media/video/feign/load/all")
	public JSONObject loadAll() throws Exception;
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return JSONObject
	 */
	@RequestMapping(value = "/media/video/feign/load/collection")
	public JSONObject loadCollection(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 根据uuid获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@RequestMapping(value = "/media/video/feign/quest/by/uuid")
	public JSONObject getByUuids(@RequestParam("uuids") String uuids) throws Exception;
	
	/**
	 * 根据id获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月9日 下午2:35:54
	 * @param id 媒资id
	 * @return MediaVideoVO 音频媒资列表
	 */
	@RequestMapping(value = "/media/video/feign/quest/by/id")
	public JSONObject getById(@RequestParam("id") Long id) throws Exception;
	
	/**
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	@RequestMapping(value = "/media/video/feign/build/url")
	public JSONObject buildUrl(@RequestParam("name") String name, @RequestParam("folderUuid") String folderUuid) throws Exception;

	/**
	 * 根据预览地址查询视频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:49:17
	 * @param String previewUrl 预览地址
	 * @return List<MediaVideoVO> 视频列表
	 */
	@RequestMapping(value = "/media/video/feign/find/by/preview/url/in")
	public JSONObject findByPreviewUrlIn(@RequestParam("previewUrls") String previewUrls) throws Exception;
	
	/**
	 * 添加远程媒资(供收录系统使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午4:23:53
	 * @param String name 媒资名称
	 * @param Long tagId 标签id(可不传)
	 * @param String httpUrl 预览地址
	 * @param String ftpUrl ftp下载地址(可不传)
	 */
	@RequestMapping(value = "/media/video/feign/add/remote")
	public JSONObject addRemote(
			@RequestParam("name") String name,
			@RequestParam("tagId") Long tagId,
			@RequestParam("httpUrl") String httpUrl,
			@RequestParam("ftpUrl") String ftpUrl) throws Exception;
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午9:12:35
	 * @param String ids 预删除媒资id数组的JSON字符串
	 */
	@RequestMapping(value = "/media/video/feign/remove")
	public JSONObject remove(@RequestParam("ids") String ids) throws Exception;
}
