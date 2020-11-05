package com.sumavision.tetris.mims.app.media.video;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;
import com.sumavision.tetris.config.feign.MultipartSupportConfig;

@FeignClient(name = "tetris-mims", configuration = {FeignConfiguration.class, MultipartSupportConfig.class})
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
	
	/**
	 * 添加上传视频媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param MediaVideoTaskVO task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return MediaVideoVO 视频媒资 
	 */
	@RequestMapping(value = "/media/video/feign/task/add")
	public JSONObject addTask(
			@RequestParam("task") String task, 
			@RequestParam("name") String name,
			@RequestParam("tags") String tags,
			@RequestParam("keyWords") String keyWords,
			@RequestParam("remark") String remark,
			@RequestParam("folderId") Long folderId, 
			@RequestParam("thumbnail") String thumbnail,
			@RequestParam("addition") String addition) throws Exception;
	
	/**
	 * 视频媒资上传<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午3:32:40
	 * @param MultipartFile file 文件描述
	 * @param String uuid 任务uuid
	 * @param String name 文件名称
	 * @param long lastModified 最后修改日期
	 * @param long beginOffset 文件分片的起始位置
	 * @param long  endOffset 文件分片的结束位置
	 * @param long blockSize 文件分片大小
	 * @param long size 文件大小
	 * @param String type 文件的mimetype
	 * @param blob block 文件分片数据
	 * @return MediaVideoVO 视频媒资
	 */
	@RequestMapping(value = "/media/video/feign/upload", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public JSONObject upload(
			@RequestPart("file") MultipartFile file,
			@RequestParam("uuid") String uuid,
			@RequestParam("name") String name,
			@RequestParam("blockSize") Long blockSize,
			@RequestParam("lastModified") Long lastModified,
			@RequestParam("size") Long size,
			@RequestParam("type") String type,
			@RequestParam("beginOffset") Long beginOffset,
			@RequestParam("endOffset") Long endOffset) throws Exception;
}
