package com.sumavision.tetris.mims.app.media.stream.video.api.process;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBuilderWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamPO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/process/media/stream/video")
public class ApiProcessStreamVideoController {
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;
	
	@Autowired
	private Path path;
	
	/**
	 * 添加视频流媒资流程节点<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月25日 下午5:51:27
	 * @param String previewUrls 视频流媒资地址列表
	 * @param String mimsName 名称
	 * @param String tags 标签列表
	 * @param String keyWords 关键字
	 * @param String remark 备注
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String previewUrls, 
			String mimsName,
            String tags,
            String keyWords,
            String remark,
			HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<FolderPO> folderPOs = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
		if (folderPOs == null || folderPOs.isEmpty()) {
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		List<String> previewUrlList = new ArrayList<String>();
		if (previewUrls != null) previewUrlList = JSONArray.parseArray(previewUrls, String.class);
		
		MediaVideoStreamVO media = mediaVideoStreamService.addTask(user, mimsName, null, null, remark, previewUrlList, folderPOs.get(0));
		
		String separator = File.separator;
		String webappPath = path.webappPath();
		String version = new StringBufferWrapper().append(MediaVideoStreamPO.VERSION_OF_ORIGIN).append(".").append(new Date().getTime()).toString();
		String storageUrl = new StringBuilderWrapper()
				.append(webappPath)
				.append("upload")
				.append(separator)
				.append("tmp")
				.append(separator)
				.append(user.getGroupName())
				.append(separator)
				.append(folderPOs.get(0).getUuid())
				.append(separator)
				.append(mimsName.split("\\.")[0])
				.append(separator)
				.append(version)
				.toString();
		File file = new File(storageUrl);
		if (!file.exists()) file.mkdirs();
		String previewUrl = mimsServerPropsQuery.generateFtpPreviewUrl(new StringBufferWrapper()
				.append("upload/tmp/")
				.append(user.getGroupName())
				.append("/")
				.append(folderPOs.get(0).getUuid())
				.append("/")
				.append(mimsName.split("\\.")[0])
				.append("/")
				.append(version)
				.toString());
		
		return new HashMapWrapper<String, String>()
				.put("rtmpUrl", previewUrlList.get(0))
				.put("name", mimsName)
				.put("storageUrl", previewUrl)
				.getMap();
	}
	
	/**
	 * 根据url列表删除视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月25日 下午5:52:47
	 * @param List<String> previewUrls 视频流媒资url列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/delete")
	public Object deleteMedias(
			String previewUrls,
			HttpServletRequest request) throws Exception {
		if (previewUrls == null || previewUrls.isEmpty()) return null;
		
		List<String> previewUrlList = JSONArray.parseArray(previewUrls, String.class);
		
		mediaVideoStreamService.removeByUrls(previewUrlList);
		
		return null;
	}
}
