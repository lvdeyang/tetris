package com.sumavision.tetris.mims.app.folder.api.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/server/folder")
public class ApiServerFolderController {
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	@Autowired
	private MediaVideoDAO mediaVideoDAO;
	@Autowired
	private MediaAudioDAO mediaAudioDAO;
	@Autowired
	private FolderDAO folderDAO;
	@Autowired
	private UserQuery userQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get")
	public Object getChildren(Long id, HttpServletRequest request) throws Exception {
		
		if (id == null || id == 0) {
			List<MediaVideoVO> videoVOs = mediaVideoQuery.loadAll();
			List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAll();
			List<MediaPictureVO> pictureVOs = mediaPictureQuery.loadAll();
			List<MediaTxtVO> txtVOs = mediaTxtQuery.loadAll();
			List<MediaVideoStreamVO> videoStreamVOs = mediaVideoStreamQuery.loadAll();
			List<MediaAudioStreamVO> audioStreamVOs = mediaAudioStreamQuery.loadAll();
			return new ArrayListWrapper<Object>().addAll(videoVOs).addAll(audioVOs).addAll(pictureVOs).addAll(txtVOs).addAll(videoStreamVOs).addAll(audioStreamVOs).getList();
		} else {
			FolderPO folderPO = folderDAO.findOne(id);
			if (folderPO != null) {
				switch (folderPO.getType()) {
				case COMPANY_VIDEO:
					return mediaVideoQuery.loadAll(id);
				case COMPANY_AUDIO:
					return mediaAudioQuery.loadAll(id);
				case COMPANY_PICTURE:
					return mediaPictureQuery.loadAll(id);
				case COMPANY_TXT:
					return mediaTxtQuery.loadAll(id);
				case COMPANY_VIDEO_STREAM:
					return mediaVideoStreamQuery.loadAll(id);
				case COMPANY_AUDIO_STREAM:
					return mediaAudioStreamQuery.loadAll(id);
				default:
					return new ArrayList<Object>();
				}
			} else {
				throw new FolderNotExistException(id);
			}
		}
		
//		Map<String, Object> audios = null;
//		Map<String, Object> videos = null;
//		
//		if (id == null || id == 0) {
//			audios = mediaAudioQuery.load(0l);
//			videos = mediaVideoQuery.load(0l);
//		} else {
//			FolderPO folderPO = folderDAO.findOne(id);
//			if (folderPO != null) {
//				switch (folderPO.getType()) {
//				case COMPANY_VIDEO:
//					videos = mediaVideoQuery.load(id);
//					break;
//				case COMPANY_AUDIO:
//					
//				default:
//					break;
//				}
//				if (folderPO.getType() == FolderType.COMPANY_VIDEO) {
//					videos = mediaVideoQuery.load(id);
//				} else if (folderPO.getType() == FolderType.COMPANY_AUDIO) {
//					audios = mediaAudioQuery.load(id);
//				} else if (folderPO.get) {
//					
//				}
//			} else {
//				throw new FolderNotExistException(id);
//			}
//		}
//		
//		ArrayListWrapper<Object> returnList = new ArrayListWrapper<Object>();
//		
//		if (videos != null) returnList.getList().addAll((List<Object>)videos.get("rows"));
//		if (audios != null) returnList.getList().addAll((List<Object>)audios.get("rows"));
		
//		return returnList.getList();
		
//		return new HashMapWrapper<String, Object>().put("videos", videos == null ? videos : videos.get("rows"))
//				   .put("audios", audios == null ? audios : audios.get("rows"))
//				   .getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/all")
	public Object getChildren(HttpServletRequest request) throws Exception {
		
		UserVO user = userQuery.current();
		
		List<MediaVideoVO> videoVOs = mediaVideoQuery.loadAll();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadAll();
		List<MediaPictureVO> pictureVOs = mediaPictureQuery.loadAll();
		List<MediaTxtVO> txtVOs = mediaTxtQuery.loadAll();
		List<MediaVideoStreamVO> videoStreamVOs = mediaVideoStreamQuery.loadAll();
		List<MediaAudioStreamVO> audioStreamVOs = mediaAudioStreamQuery.loadAll();
		
		return new ArrayListWrapper<Object>().addAll(videoVOs).addAll(audioVOs).addAll(pictureVOs).addAll(txtVOs).addAll(videoStreamVOs).addAll(audioStreamVOs).getList();
	}
}
