package com.sumavision.tetris.mims.app.media.picture;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaPictureFeign {

	/**
	 * 查询文件夹下的文件夹以及图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:46:46
	 * @param Long folderId 当前文件夹
	 * @return JSONObject 图片媒资列表以及面包屑
	 */
	@RequestMapping(value = "/media/picture/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 加载所有的图片媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaPictureVO> 视频媒资列表
	 */
	@RequestMapping(value = "/media/picture/feign/load/all")
	public JSONObject loadAll() throws Exception;
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return JSONObject
	 */
	@RequestMapping(value = "/media/picture/feign/load/collection")
	public JSONObject loadCollection(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 根据预览地址查询图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:34:58
	 * @param JSONString preivewUrls 预览地址列表
	 * @return List<MediaPictureVO> 图片列表
	 */
	@RequestMapping(value = "/media/picture/feign/find/by/preview/url/in")
	public JSONObject findByPreviewUrlIn(@RequestParam("previewUrls") String previewUrls) throws Exception;
	
	/**
	 * 删除图片媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:07:53
	 * @param Long id 媒资id
	 * @return deleted List<MediaPictureVO> 删除列表
	 * @return processed List<MediaPictureVO> 待审核列表
	 */
	@RequestMapping(value = "/media/picture/feign/remove")
	public JSONObject remove(@RequestParam("ids") String ids) throws Exception;
	
	/**
	 * 添加上传图片媒资任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param JSONString task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return MediaPictureVO 图片媒资
	 */
	@RequestMapping(value = "/media/picture/feign/task/add")
	public JSONObject addTask(
			@RequestParam("task") String task, 
			@RequestParam("name") String name,
			@RequestParam("tags") String tags,
			@RequestParam("keyWords") String keyWords,
			@RequestParam("remark") String remark,
			@RequestParam("folderId") Long folderId, 
			@RequestParam("addition") String addition) throws Exception;
	
	/**
	 * 素材分片上传<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午3:32:40
	 * @param String uuid 任务uuid
	 * @param String name 文件名称
	 * @param long lastModified 最后修改日期
	 * @param long beginOffset 文件分片的起始位置
	 * @param long  endOffset 文件分片的结束位置
	 * @param long blockSize 文件分片大小
	 * @param long size 文件大小
	 * @param String type 文件的mimetype
	 * @return MediaPictureVO 图片媒资
	 */
	@RequestMapping(value = "/media/picture/feign/upload", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
