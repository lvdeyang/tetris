package com.sumavision.tetris.mims.app.media.stream.video.api.tvos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamDAO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamPO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamUrlRelationQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/tvos/media/video/stream")
public class ApiTvosStreamVideoController {
	
	@Autowired
	private MediaVideoStreamDAO mediaVideoStreamDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private MediaVideoStreamUrlRelationQuery mediaVideoStreamUrlRelationQuery;
	
	/**
	 * 以列表形式获取所有视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月12日 下午4:03:27
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
		List<MediaVideoStreamVO> rows = new ArrayList<MediaVideoStreamVO>();
		if(folders!=null && folders.size()>0){
			Set<Long> folderIds = new HashSet<Long>();
			for(FolderPO folder:folders){
				folderIds.add(folder.getId());
			}
			List<MediaVideoStreamPO> entities = mediaVideoStreamDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
			rows = MediaVideoStreamVO.getConverter(MediaVideoStreamVO.class).convert(entities, MediaVideoStreamVO.class);
		}
		
		return rows;
	}
	
	/**
	 * 获取视频流媒资url播放<br/>
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
	@RequestMapping(value = "/play")
	public Object addTask(
			Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		return  mediaVideoStreamUrlRelationQuery.getUrlFromStreamId(id);
		
	}
}
