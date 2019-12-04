package com.sumavision.tetris.mims.app.media.video.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/video/feign")
public class MediaVideoFeignController {

	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@Autowired
	private MediaVideoDAO mediaVideoDAO;
	
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 加载文件夹下的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoVO> 视频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaVideoQuery.load(folderId);
	}
	
	/**
	 * 加载所有视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(
			HttpServletRequest request) throws Exception{
		
		return mediaVideoQuery.loadAll();
	}
	
	/**
	 * 根据uuid获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/by/uuid")
	public Object getByUuids(String uuids) throws Exception{
		List<String> uuidList = JSON.parseArray(uuids, String.class);
		
		return MediaVideoVO.getConverter(MediaVideoVO.class).convert(mediaVideoQuery.questByUuid(uuidList), MediaVideoVO.class);
	}
	
	/**
	 * 根据id获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月9日 下午2:32:55
	 * @param id 媒资id
	 * @return MediaoVideoPO 媒资信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/by/id")
	public Object getById(Long id) throws Exception{
		MediaVideoPO audio = mediaVideoDAO.findOne(id);
		if (audio == null) throw new MediaVideoNotExistException(id);
		
		return new MediaVideoVO().set(audio);
	}
	
	/**
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/build/url")
	public Object buildUrl(String name, String folderUuid, HttpServletRequest request) throws Exception {
		return mediaVideoQuery.buildUrl(name, folderUuid);
	};
	
	/**
	 * 根据预览地址查询视频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:49:17
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaVideoVO> 视频列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls, 
			HttpServletRequest request) throws Exception{
		return mediaVideoQuery.findByPreviewUrlIn(JSON.parseArray(previewUrls, String.class));
	}
 	
	/**
	 * 添加远程媒资(收录系统使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 下午5:24:19
	 * @param String name 媒资名称
	 * @param Long tagId 标签id 
	 * @param String httpUrl 媒资预览地址
	 * @param String ftpUrl 媒资ftp地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/remote")
	public Object addRemote(String name, Long tagId, String httpUrl, String ftpUrl, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		TagVO tag = tagQuery.queryById(tagId);
		
		return mediaVideoService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
	}
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午9:12:35
	 * @param List<Long> ids 预删除媒资id数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public void remove(String ids, HttpServletRequest request) throws Exception {
		if (ids == null || ids.isEmpty()) return;
		
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		
		mediaVideoService.remove(idList);
	}
}
