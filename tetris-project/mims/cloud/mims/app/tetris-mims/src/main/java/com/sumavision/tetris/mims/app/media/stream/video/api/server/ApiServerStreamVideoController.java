package com.sumavision.tetris.mims.app.media.stream.video.api.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamUrlRelationQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/media/video/stream")
public class ApiServerStreamVideoController {
	@Autowired
	UserQuery userQuery;
	
	@Autowired
	FolderDAO folderDao;
	
	@Autowired
	FolderQuery folderQuery;
	
	@Autowired
	MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	MediaVideoStreamUrlRelationQuery mediaVideoStreamUrlRelationQuery;

	/**
	 * 添加上传视频流媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param String previewUrl 流地址
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String previewUrl, 
			String name,
            String tags,
            String keyWords,
            String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<FolderPO> folderPOs = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
		if (folderPOs == null || folderPOs.isEmpty()) {
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		List<String> previewUrls = JSON.parseArray(previewUrl,String.class);
		
		return  mediaVideoStreamService.addTask(user, name, null, null, remark, previewUrls, folderPOs.get(0));
		
	}
	
	/**
	 * 添加上传视频流媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param String previewUrl 流地址
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addTask(
			String previewUrl, 
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<FolderPO> folderPOs = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
		if (folderPOs == null || folderPOs.isEmpty()) {
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return  mediaVideoStreamService.addTask(user, name, null, null, "", new ArrayListWrapper<String>().add(previewUrl).getList(), folderPOs.get(0));
		
	}
	
	/**
	 * 以列表形式获取所有视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月12日 下午4:03:27
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		return mediaVideoStreamQuery.load(0l);
	}
	
	/**
	 * 获取视频流媒资url播放<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月12日 下午1:44:06
	 * @param id 视频流媒资id
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/play")
	public Object addTask(
			Long id,
			HttpServletRequest request) throws Exception{
		
		return  mediaVideoStreamUrlRelationQuery.getUrlFromStreamId(id);
	}
	
	/**
	 * 视频流媒资删除<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param JSONString mediaIds 视频流id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String mediaIds, 
			HttpServletRequest request) throws Exception{
		List<Long> ids = JSON.parseArray(mediaIds, Long.class);
		List<MediaVideoStreamVO> medias = mediaVideoStreamService.removeByIds(ids);
		
		List<String> mediaNames = new ArrayList<String>();
		for (MediaVideoStreamVO media : medias) {
			List<String> urls = media.getPreviewUrl();
			if (urls != null && !urls.isEmpty()) {
				String[] paths = urls.get(0).split("/");
				String previewUrlFileName = paths[paths.length - 1];
				String mediaName = previewUrlFileName.split("\\.")[0];
				mediaNames.add(mediaName);
			}
		}
		if (mediaNames.isEmpty()) return null;
		
		String filePath = "/usr/sbin/sumavision/SRS-2.0-R4/srs-2.0-r4/trunk/objs/nginx/html/live";
		File hlsFile = new File(filePath);
		if (hlsFile.exists()) {
			File[] fileList = hlsFile.listFiles();
			for (File file : fileList) {
				String fileName = file.getName();
				for (String mediaName : mediaNames) {
					if (file.exists() && (fileName.matches(mediaName + "-.*\\.ts") || fileName.equals(mediaName + ".m3u8"))) {
						file.delete();
					}
				}
			}
		}
		
		return null;
	}
}
