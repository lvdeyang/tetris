package com.sumavision.tetris.mims.app.media.live;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.live.exception.MediaPushLiveNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/push/live")
public class MediaPushLiveController {
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaPushLiveQuery mediaPushLiveQuery;
	
	@Autowired
	private MediaPushLiveService mediaPushLiveService;
	
	@Autowired
	private MediaPushLiveDAO mediaPushLiveDAO;
	
	/**
	 * 加载文件夹下的push直播媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaPushLiveVO> push直播媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaPushLiveQuery.load(folderId);
	}
	
	/**
	 * 添加上传push直播媒资任务<br/>
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
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType,
			String name,
            String tags,
            String keyWords,
            String remark,
			Long folderId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<String> tagList = new ArrayList<String>();
		if(tags != null){
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaPushLivePO entity = mediaPushLiveService.addTask(user, name, tagList, keyWordList, remark, freq, audioPid, videoPid, audioType, videoType, folder);
		
		return new MediaPushLiveVO().set(entity);
		
	}
	
	/**
	 * 编辑push直播媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 上午8:44:32
	 * @param id push直播媒资id
	 * @param previewUrl 流地址
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaPushLiveVO push直播媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/edit/{id}")
	public Object editTask(
			@PathVariable Long id,
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType,
			String name,
            String tags,
            String keyWords,
            String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		MediaPushLivePO audioStream = mediaPushLiveDAO.findOne(id);
		if(audioStream == null){
			throw new MediaPushLiveNotExistException(id);
		}
		
		List<String> tagList = new ArrayList<String>();
		if(tags != null){
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaPushLivePO entity = mediaPushLiveService.editTask(user, audioStream, name, tagList, keyWordList, remark, freq, audioPid, videoPid, audioType, videoType);
		
		return new MediaPushLiveVO().set(entity);
		
	}
	
	/**
	 * 删除push直播媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午9:07:53
	 * @param @PathVariable Long id 媒资id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		
		if(media == null){
			throw new MediaPushLiveNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return mediaPushLiveService.remove(new ArrayListWrapper<MediaPushLivePO>().add(media).getList());
	}
	
	/**
	 * 获取流类型数组<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:51:57
	 * @return List<String> 流类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/stream/type/{type}")
	public Object getStreamType(@PathVariable String type, HttpServletRequest request) throws Exception {
		if ("audio".equals(type)) return MediaPushLiveTypeAudio.queryAllType();
		if ("video".equals(type)) return MediaPushLiveTypeVideo.queryAllType();
		return new ArrayList<String>();
	}
}
