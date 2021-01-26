package com.sumavision.tetris.mims.app.media.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.boss.MediaType;
import com.sumavision.tetris.mims.app.boss.QdBossService;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioCannotMatchException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioStatusErrorWhenUploadCancelException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioStatusErrorWhenUploadErrorException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.encode.AudioFileEncodeDAO;
import com.sumavision.tetris.mims.app.media.encode.AudioFileEncodePO;
import com.sumavision.tetris.mims.app.media.encode.FileEncodeService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

@Controller
@RequestMapping(value = "/media/audio")
public class MediaAudioController {

	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	@Autowired
	private AudioFileEncodeDAO audioFileEncodeDao;
	
	@Autowired
	private FileEncodeService fileEncodeService;
	
	@Autowired
	QdBossService bossService;
	
	/**
	 * 加载文件夹下的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaAudioVO> 音频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaAudioQuery.load(folderId);
	}
	
	/**
	 * 添加上传图片媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param JSONString task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
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
			String task, 
			String name,
            String tags,
            String keyWords,
            String remark,
			Long folderId, 
			boolean encryption,
			String thumbnail,
			String addition,
			HttpServletRequest request) throws Exception{
		
		MediaAudioTaskVO taskParam = JSON.parseObject(task, MediaAudioTaskVO.class);
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findById(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<String> tagList = new ArrayList<String>();
		if (tags!=null && !tags.isEmpty()) {
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaAudioPO entity = mediaAudioService.addTask(user, name, tagList, keyWordList, remark, encryption, taskParam, folder);
		if (thumbnail != null) entity.setThumbnail(thumbnail);
		if (addition != null) entity.setAddition(addition);
		mediaAudioDao.save(entity);
		//向boss同步一个产品
		bossService.addMedia(entity.getId(), MediaType.AUDIO);
		
		return new MediaAudioVO().set(entity);
		
	}
	
	/**
	 * 添加上传图片媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param JSONString task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add/from/txt")
	public Object addTaskFromTxt(
			Long txtId,
			String name,
            String tags,
            String keyWords,
            String remark,
            boolean encryption,
			Long folderId, 
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
		if (tags!=null && !tags.isEmpty()) {
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaAudioPO entity = mediaAudioService.addTaskFromTxt(user, name, tagList, keyWordList, remark, txtId, encryption, folder);
		if (thumbnail != null) entity.setThumbnail(thumbnail);
		if (addition != null) entity.setAddition(addition);
		mediaAudioDao.save(entity);
		
		return new MediaAudioVO().set(entity);
		
	}
	
	/**
	 * 编辑音频媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午4:42:43
	 * @param id 音频媒资id
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaAudioVO 音频媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/edit/{id}")
	public Object editTask(
			@PathVariable Long id,
			String name,
            String tags,
            String keyWords,
            String remark,
            String thumbnail,
			String addition,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();

		MediaAudioPO audio = mediaAudioDao.findById(id);
		if(audio == null){
			throw new MediaAudioNotExistException(id);
		}
		
		List<String> tagList = new ArrayList<String>();
		if(tags != null){
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = null;
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaAudioPO entity = mediaAudioService.editAudio(user, audio, name, tagList, keyWordList, remark);
		if (thumbnail != null) entity.setThumbnail(thumbnail);
		if(addition != null) entity.setAddition(addition);
		mediaAudioDao.save(entity);
		
		return new MediaAudioVO().set(entity);
		
	}
	
	/**
	 * 
	 * 推荐状态设置<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月11日 下午7:51:26
	 * @param id
	 * @param status 0 取消推荐 1推荐
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/recomm/{id}")
	public Object recommStatus(
			@PathVariable Long id,
			int status,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();

		MediaAudioPO audio = mediaAudioDao.findById(id);
		audio.setIsTop(status);
		mediaAudioDao.save(audio);
		
		return new MediaAudioVO().set(audio);
		
	}
	
	/**
	 * 查询图片媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月30日 上午10:10:56
	 * @return List<MediaAudioVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/tasks")
	public Object queryTasks(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		Set<Long> folderIds = new HashSet<Long>();
		
		//获取图片媒资库根目录
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_AUDIO.toString());
		folderIds.add(folder.getId());
		List<FolderPO> subFolders = folderQuery.findSubFolders(folder.getId());
		if(subFolders!=null && subFolders.size()>0){
			for(FolderPO subFolder:subFolders){
				folderIds.add(subFolder.getId());
			}
		}
		
		List<MediaAudioPO> tasks = mediaAudioQuery.findTasksByFolderIds(folderIds);
		
		List<MediaAudioVO> view_tasks = new ArrayList<MediaAudioVO>();
		
		if(tasks!=null && tasks.size()>0){
			for(MediaAudioPO task:tasks){
				MediaAudioVO view_task = new MediaAudioVO().set(task);
				File file = new File(task.getUploadTmpPath());
				Long currentSize = file==null?0l:file.length();
				Long totalSize = task.getSize();
				view_task.setProgress((int)((((float)currentSize)/((float)totalSize))*100));
				view_tasks.add(view_task);
			}
		}
		
		return view_tasks;
	}
	
	/**
	 * 音频媒资上传<br/>
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
	 * @param blob block 文件分片数据
	 * @return MediaAudioVO 音频媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload")
	public Object upload(HttpServletRequest nativeRequest) throws Exception{
		
		MultipartHttpServletRequestWrapper request = new MultipartHttpServletRequestWrapper(nativeRequest);
		
		String uuid = request.getString("uuid");
		String name = request.getString("name");
		long blockSize = request.getLongValue("blockSize");
		long lastModified = request.getLongValue("lastModified");
		long size = request.getLongValue("size");
		String type = request.getString("type");
		long beginOffset = request.getLongValue("beginOffset");
		long endOffset = request.getLongValue("endOffset");
		
		//参数错误
		if((beginOffset + blockSize) != endOffset){
			throw new OffsetCannotMatchSizeException(beginOffset, endOffset, blockSize);
		}
		
		MediaAudioPO task = mediaAudioDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaAudioNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//状态错误
		if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new MediaAudioStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
		}
		
		//文件不是一个
		if(!name.equals(task.getFileName()) 
				|| lastModified!=task.getLastModified() 
				|| size!=task.getSize() 
				|| !type.equals(task.getMimetype())){
			throw new MediaAudioCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
											   task.getLastModified(), task.getSize(), task.getMimetype());
		}
		
		//文件起始位置错误
		File file = new File(task.getUploadTmpPath());
		if((!file.exists() && beginOffset!=0l) 
				|| (file.length() != beginOffset)){
			throw new MediaAudioErrorBeginOffsetException(uuid, beginOffset, file.length());
		}
		
		//分块
		InputStream block = null;
		FileOutputStream out = null;
		try{
			if(!file.exists()) file.createNewFile();
			block = request.getInputStream("block");
			byte[] blockBytes = ByteUtil.inputStreamToBytes(block);
			out = new FileOutputStream(file, true);
			out.write(blockBytes);
		}finally{
			if(block != null) block.close();
			if(out != null) out.close();
		}
		
		if(endOffset == size){
			//上传完成
			task.setUploadStatus(UploadStatus.COMPLETE);
			
			//如果是从文本文件上传需要转换成音频
			if(task.getMimetype().equals("text/plain")){
				mediaAudioService.convertTxtMeidaToAudioMedia(task);
			} else {
				MultimediaInfo multimediaInfo = new Encoder().getInfo(file);
				task.setDuration(multimediaInfo.getDuration());
			}
			
			if(task.getReviewStatus() != null){
				//开启审核流程--这里会保存媒资
				mediaAudioService.startUploadProcess(task);
			}else{
				mediaAudioDao.save(task);
				if(task.getEncryption()){
					fileEncodeService.encodeAudioFile(task);
				}
			}

			mediaAudioService.checkMediaEdit(task);
//			if (task.getMediaEdit() != null && task.getMediaEdit()) {
//				mediaAudioService.startMediaEdit(task);
//			}
		}
		
        return new MediaAudioVO().set(task);
	}
	
	/**
	 * 上传出错<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午5:59:53
	 * @param String uuid 任务uuid
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/error/{uuid}")
	public Object uploadError(
			@PathVariable String uuid, 
			HttpServletRequest request) throws Exception{
		
		MediaAudioPO task = mediaAudioDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaAudioNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//状态错误
		if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new MediaAudioStatusErrorWhenUploadErrorException(uuid, task.getUploadStatus());
		}
		
		task.setUploadStatus(UploadStatus.ERROR);
		
		mediaAudioDao.save(task);
		
		return null;
	}
	
	/**
	 * 关闭上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:52:06
	 * @param String uuid 任务uuid
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/cancel/{uuid}")
	public Object uploadCancel(
			@PathVariable String uuid,
			HttpServletRequest request) throws Exception{
		
		MediaAudioPO task = mediaAudioDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaAudioNotExistException(uuid);
		}
		
		if(UploadStatus.COMPLETE.equals(task.getUploadStatus())){
			throw new MediaAudioStatusErrorWhenUploadCancelException(uuid, task.getUploadStatus());
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		mediaAudioService.uploadCancel(task);
		
		return null;
	}
	
	/**
	 * 查询文件的上传情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 下午5:05:32
	 * @param String uuid 任务uuid
	 * @return MaterialFileUploadInfoVO 文件上传信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/upload/info/{uuid}")
	public Object queryUploadInfo(
			@PathVariable String uuid,
			HttpServletRequest request) throws Exception{
		
		MediaAudioPO task = mediaAudioDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaAudioNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return new MediaAudioUploadInfoVO().set(task);
	}
	
	/**
	 * 删除音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午9:07:53
	 * @param @PathVariable Long id 媒资id
	 * @return deleted List<MediaAudioVO> 删除的数据列表
	 * @return processed List<MediaAudioVO> 待审核的数据列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MediaAudioPO media = mediaAudioDao.findById(id);
		
		if(media == null){
			throw new MediaAudioNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return mediaAudioService.remove(new ArrayListWrapper<MediaAudioPO>().add(media).getList());
	}
	
	/**
	 * 移动音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午11:33:56
	 * @param Long mediaId 音频媒资id
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
	
		MediaAudioPO media = mediaAudioDao.findById(mediaId);
		
		if(media == null){
			throw new MediaAudioNotExistException(mediaId);
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
		mediaAudioDao.save(media);
		
		return true;
	}
	
	/**
	 * 复制音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:36:40
	 * @param Long mediaId 待复制音频媒资id
	 * @param Long targetId 目标文件夹id
	 * @return boolean moved 标识文件是否复制到其他文件夹中
	 * @return MeidaVideoVO copied 复制后的音频媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/copy")
	public Object copy(
			Long mediaId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		MediaAudioPO media = mediaAudioDao.findById(mediaId);
		
		if(media == null){
			throw new MediaAudioNotExistException(mediaId);
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
		
		MediaAudioPO copiedMedia  = mediaAudioService.copy(media, target);
		
		//判断是否加密,加密需要复制加密信息
		if(media.getEncryption() != null && media.getEncryption()){
			AudioFileEncodePO audioEncode = audioFileEncodeDao.findByMediaId(media.getId());
			if(audioEncode != null){
				fileEncodeService.copy(audioEncode, copiedMedia);
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("moved", moved)
																		 .put("copied", new MediaAudioVO().set(copiedMedia))
																		 .getMap();
		return result;
	}
	
	/**
	 * 获取音频媒资预览地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:44:22
	 * @param @PathVariable Long id 素材文件id
	 * @return String name 文件名称
	 * @return String uri 预览uri
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/preview/uri/{id}")
	public Object previewUri(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MediaAudioPO media = mediaAudioQuery.loadById(id);
		
		Map<String, String> result = new HashMapWrapper<String, String>().put("name", media.getFileName())
																		 .put("uri", media.getPreviewUrl())
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 获取音频媒资下载地址(区别于预览，添加下载量)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:44:22
	 * @param @PathVariable Long id 素材文件id
	 * @return String name 文件名称
	 * @return String uri 预览uri
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/uri/{id}")
	public Object downloadUri(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		MediaAudioPO media = mediaAudioQuery.loadById(id);
		
		mediaAudioService.downloadAdd(user, id);
		
		Map<String, String> result = new HashMapWrapper<String, String>().put("name", media.getFileName())
																		 .put("uri", media.getPreviewUrl())
																		 .getMap();
		
		return result;
	}
	
	
	/**
	 * 文件刷表
	 * 文件刷表，返回编码格式，码率，采样率等等信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 下午5:56:09
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/refresh/uri/{id}")
	public Object refreshUri(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		MediaAudioPO media = mediaAudioQuery.loadById(id);
		
		return mediaAudioService.refresh(id);
	}
	
	
	/**
	 * 增加下载数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月15日 下午5:11:49
	 * @param Long id 下载的音频id
	 * @return MediaAudioVO 音频
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download")
	public Object downloadAdd(Long id, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		return mediaAudioService.downloadAdd(user, id);
	}
	
	/**
	 * 根据条件检索<br/>
	 * <b>作者:</b>sms<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月13日 上午9:52:56
	 * @param id 媒资id
	 * @param name 媒资名称包含
	 * @param startTime 创建起始时间
	 * @param endTime 创建终止时间
	 * @param tagId	标签id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/search")
	public Object search(Long id, String name, String startTime, String endTime, Long tagId, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadByCondition(id, name, startTime, endTime, tagId);
		return audioVOs;
	}
	
	
}
