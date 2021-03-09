package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sumavision.tetris.mims.app.media.stream.video.program.ResultCode;
import com.sumavision.tetris.mims.app.media.stream.video.program.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.stream.MediaStreamType;
import com.sumavision.tetris.mims.app.media.stream.video.exception.MediaVideoStreamNotExistException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/video/stream")
public class MediaVideoStreamController {

	private static final Logger LOG = LoggerFactory.getLogger(MediaVideoStreamController.class);

	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private MediaVideoStreamDAO mediaVideoStreamDao;
	
	/**
	 * 加载文件夹下的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoStreamVO> 视频流媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaVideoStreamQuery.load(folderId);
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
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String previewUrl, 
			String name,
            String tags,
            String keyWords,
            String remark,
			Long folderId,
			String streamType,
			String thumbnail,
			String addition,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findById(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<String> tagList = new ArrayList<String>();
		if(tags!=null && !tags.isEmpty()){
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		List<String> previewUrls = JSON.parseArray(previewUrl, String.class);
		MediaVideoStreamPO entity = mediaVideoStreamService.addTask(user, name, tagList, keyWordList, remark, previewUrls, folder, streamType);
		if (thumbnail != null) entity.setThumbnail(thumbnail);
		if (addition != null) entity.setAddition(addition);
		mediaVideoStreamDao.save(entity);
		return new MediaVideoStreamVO().set(entity).setPreviewUrl(previewUrls);
	}
	
	/**
	 * 编辑视频流媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 上午9:02:08
	 * @param id 视频流媒资id
	 * @param previewUrl 视频流地址
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaVideoStreamVO 视频流媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/edit/{id}")
	public Object editTask(
			@PathVariable Long id,
			String previewUrl, 
			String name,
            String tags,
            String keyWords,
            String remark,
            String streamType,
            String thumbnail,
            String addition,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<String> tagList = new ArrayList<String>();
		if (tags!=null && !tags.isEmpty()) {
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		return mediaVideoStreamService.editTask(
				user,
				id,
				name,
				tagList,
				keyWordList,
				remark,
				JSON.parseArray(previewUrl, String.class),
				streamType,
				thumbnail,
				addition);
	}
	
	/**
	 * 删除视频流媒资<br/>
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
		
		MediaVideoStreamPO media = mediaVideoStreamDao.findById(id);
		
		if(media == null){
			throw new MediaVideoStreamNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return mediaVideoStreamService.remove(new ArrayListWrapper<MediaVideoStreamPO>().add(media).getList());
	}
	
	/**
	 * 移动视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午11:33:56
	 * @param Long mediaId 视频流媒资id
	 * @param Long targetId 目标文件夹id
	 * @return boolean 是否移动
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(
			Long mediaId,
			Long targetId,
			HttpServletRequest request) throws Exception{
	
		MediaVideoStreamPO media = mediaVideoStreamDao.findById(mediaId);
		
		if(media == null){
			throw new MediaVideoNotExistException(mediaId);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO target = folderDao.findById(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), target.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		if(target.getId().equals(media.getFolderId())) return false;
		
		media.setFolderId(target.getId());
		mediaVideoStreamDao.save(media);
		
		return true;
	}
	
	/**
	 * 复制视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:36:40
	 * @param Long mediaId 待复制视频流媒资id
	 * @param Long targetId 目标文件夹id
	 * @return boolean moved 标识文件是否复制到其他文件夹中
	 * @return MeidaVideoVO copied 复制后的视频流媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/copy")
	public Object copy(
			Long mediaId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		MediaVideoStreamPO media = mediaVideoStreamDao.findById(mediaId);
		
		if(media == null){
			throw new MediaVideoNotExistException(mediaId);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO target = folderDao.findById(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), target.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		boolean moved = true;
		
		//判断是否被复制到其他文件夹中
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaVideoStreamVO copiedMedia  = mediaVideoStreamService.copyByCountlessUrl(media, target);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("moved", moved)
																		 .put("copied", copiedMedia)
																		 .getMap();
		return result;
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
	@RequestMapping(value = "/list/stream/type")
	public Object getStreamType(HttpServletRequest request) throws Exception {
		return MediaStreamType.queryAllType();
	}

    /**
     * @MethodName: refreshStream
     * @Description: 刷源
     * @param id 1 视频流ID
     * @Return: java.lang.Object 节目信息
     * @Author: Poemafar
     * @Date: 2021/2/25 8:56
     **/
    @JsonBody
    @ResponseBody
    @RequestMapping(value = "/refresh/uri/{id}")
    public Object refreshStream(@PathVariable Long id,HttpServletRequest request) throws Exception {
        MediaVideoStreamPO media = mediaVideoStreamQuery.loadById(id);
		try {
			mediaVideoStreamService.refresh(media);
		} catch (Exception e) {
			LOG.error("fail to refresh source",e);
			return new ResultVO(ResultCode.FAIL).setMessage(e.getMessage());
		}
		return new ResultVO(ResultCode.SUCCESS);
	}

	/**
	 * @MethodName: refreshStream
	 * @Description: 刷源
	 * @param id 1 视频流ID
	 * @Return: java.lang.Object 节目信息
	 * @Author: Poemafar
	 * @Date: 2021/2/25 8:56
	 **/
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/uri/{id}")
	public Object lookStreamDetail(@PathVariable Long id,HttpServletRequest request) throws Exception {
		MediaVideoStreamPO media = mediaVideoStreamQuery.loadById(id);
		return mediaVideoStreamService.getDetail(media);
	}

	/**
	 * @MethodName: injectSource
	 * @Description: 将视频流注入资源
	 * @param id 1
	 * @param request 2
	 * @Return: java.lang.Object
	 * @Author: Poemafar
	 * @Date: 2021/3/5 15:44
	 **/
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/inject/{id}")
	public Object injectSource(@PathVariable Long id,HttpServletRequest request) throws Exception {
		MediaVideoStreamPO media = mediaVideoStreamQuery.loadById(id);
		try {
			mediaVideoStreamService.injectStreamToResource(media);
		} catch (Exception e) {
			LOG.error("fail to inject source",e);
			return new ResultVO(ResultCode.FAIL).setMessage(e.getMessage());
		}
		return new ResultVO(ResultCode.SUCCESS);
	}
}
