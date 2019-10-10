package com.sumavision.tetris.mims.app.media.api.process;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.encode.FileEncodeService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/process/media")
public class ApiProcessMediaController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	/**
	 * 根据视音频媒资列表批量加载的视频媒资（给转码添加媒资提供）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @param String urlList 视音频媒资http地址由","连接
	 * @return null
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/by/transcoding")
	public Object addByTranscoding(String urlList, Long parentFolderId,String mediaTags, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<String> urls = Arrays.asList(urlList.split(","));
		
		FolderPO folder = folderDao.findOne(parentFolderId);
		
		if (folder == null) return null;
		
		if(folder.getType() == FolderType.COMPANY_VIDEO){
			mediaVideoService.addList(user, urls, parentFolderId, mediaTags);
		}else if (folder.getType() == FolderType.COMPANY_AUDIO) {
			mediaAudioService.addList(user, urls, parentFolderId, mediaTags);
		}
		
		return null;
	}
	
}
