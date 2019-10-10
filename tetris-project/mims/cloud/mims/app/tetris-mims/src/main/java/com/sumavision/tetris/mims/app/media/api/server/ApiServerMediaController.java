package com.sumavision.tetris.mims.app.media.api.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.FolderTypeCannotMatchException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioTaskVO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioCannotMatchException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressDAO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressPO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressService;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressTaskVO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;
import com.sumavision.tetris.mims.app.media.compress.exception.MediaCompressCannotMatchException;
import com.sumavision.tetris.mims.app.media.compress.exception.MediaCompressErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.compress.exception.MediaCompressNotExistException;
import com.sumavision.tetris.mims.app.media.compress.exception.MediaCompressStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureDAO;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureTaskVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureCannotMatchException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureNotExistException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamPO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamService;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.tag.TagDAO;
import com.sumavision.tetris.mims.app.media.tag.TagPO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoTaskVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoCannotMatchException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoNotExistException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoStatusErrorWhenUploadingException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 外部Server素材库接口<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月11日 下午1:12:05
 */
@Controller
@RequestMapping(value = "/api/server/media")
public class ApiServerMediaController {
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaPictureService mediaPictureService;
	
	@Autowired
	private MediaTxtService mediaTxtService;
	
	@Autowired
	private MediaAudioStreamService mediaAudioStreamService;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private MediaCompressService mediaCompressService;
	
	@Autowired
	private MediaPictureDAO mediaPictureDao;
	
	@Autowired
	private MediaVideoDAO mediaVideoDao;
	
	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	@Autowired
	private MediaCompressDAO mediaCompressDao;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private UserQuery userQuery;
	/**
	 * 添加上传任务<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年3月11日 下午1:44:06
	 * @param JSONString task 根据文件类型区分:audio,picture,video -- {name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * 									   audioStream,videoStream -- 流地址url
	 * 									   txt -- 文本内容		
	 * @param String folderType 文件类型
	 * @param String name 媒资名称
	 * @param String remark 备注	
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String task, 
			String folderType,
			String name,
            String remark,
            Long folderId,
            String tagId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
	    FolderType type = FolderType.fromPrimaryKey(folderType);
	    FolderPO folder;
	    if (folderId == null) {
	    	folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), type.toString());
			if(folder == null){
				throw new FolderNotExistException(new StringBufferWrapper().append("企业")
																		   .append(type.getName())
																		   .append("根文件夹不存在！")
																		   .toString());
			}
		} else {
			folder = folderDao.findOne(folderId);
			if(folder == null){
				throw new FolderNotExistException(new StringBufferWrapper().append("文件夹")
																		   .append(folderId)
																		   .append("不存在！")
																		   .toString());
			}
		}
	    List<Long> tagIds = Arrays.asList(tagId.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
	    List<TagPO> tag = tagDAO.findAll(tagIds);
	    List<String> tagNames = new ArrayList<String>();
	    if (tag != null) {
	    	for (TagPO tagPO : tag) {
				tagNames.add(tagPO.getName());
			}
		}
		
		if(type.equals(FolderType.COMPANY_AUDIO)){
			MediaAudioTaskVO taskParam = JSON.parseObject(task, MediaAudioTaskVO.class);
			MediaAudioPO entity = mediaAudioService.addTask(user, name, tagNames, null, remark, false, taskParam, folder);			
			return new MediaAudioVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_AUDIO_STREAM)){
			MediaAudioStreamPO entity = mediaAudioStreamService.addTask(user, name, null, null, remark, task, folder);
			return new MediaAudioStreamVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_PICTURE)){
			MediaPictureTaskVO taskParam = JSON.parseObject(task, MediaPictureTaskVO.class);
			MediaPicturePO entity = mediaPictureService.addTask(user, name, null, null, remark, taskParam, folder);
			return new MediaPictureVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_TXT)){
			return mediaTxtService.addTask(user, name, null, null, remark, task, folder, false);
		}else if(type.equals(FolderType.COMPANY_VIDEO)){
			MediaVideoTaskVO taskParam = JSON.parseObject(task, MediaVideoTaskVO.class); 
			MediaVideoPO entity = mediaVideoService.addTask(user, name, tagNames, null, remark, taskParam, folder);
			return new MediaVideoVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_VIDEO_STREAM)){
			return mediaVideoStreamService.addTask(user, name, null, null, remark, new ArrayListWrapper<String>().add(task).getList(), folder);			
		}else if(type.equals(FolderType.COMPANY_COMPRESS)){
			MediaCompressTaskVO taskParam = JSON.parseObject(task, MediaCompressTaskVO.class);
			MediaCompressPO entity = mediaCompressService.addTask(user, name, null, null, remark, taskParam, folder);
			return new MediaCompressVO().set(entity);
		}
		
		return null;
	}
	
	/**
	 * 素材分片上传<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午3:32:40
	 * @param String uuid 任务uuid
	 * @param String folderType 文件类型--picture/video/audio/compress 
	 * @param String name 文件名称
	 * @param long lastModified 最后修改日期
	 * @param long beginOffset 文件分片的起始位置
	 * @param long  endOffset 文件分片的结束位置
	 * @param long blockSize 文件分片大小
	 * @param long size 文件大小
	 * @param String type 文件的mimetype
	 * @param blob block 文件分片数据
	 * @return MediaPictureVO 图片媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload")
	public Object upload(HttpServletRequest nativeRequest) throws Exception{
		
		MultipartHttpServletRequestWrapper request = new MultipartHttpServletRequestWrapper(nativeRequest);
		
		FolderType folderType = FolderType.fromPrimaryKey(request.getString("folderType"));
		String uuid = request.getString("uuid");
		String name = request.getString("name");
		long blockSize = request.getLongValue("blockSize");
		long lastModified = request.getLongValue("lastModified");
		long size = request.getLongValue("size");
		String type = request.getString("type");
		long beginOffset = request.getLongValue("beginOffset");
		long endOffset = request.getLongValue("endOffset");
		
		//参数错误
		if((beginOffset + endOffset) != blockSize){
			new OffsetCannotMatchSizeException(beginOffset, endOffset, blockSize);
		}
		
		if(folderType.equals(FolderType.COMPANY_PICTURE)){
			MediaPicturePO task = mediaPictureDao.findByUuid(uuid);
			
			if(task == null){
				throw new MediaPictureNotExistException(uuid);
			}
			
			//状态错误
			if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
				throw new MediaPictureStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
			}
			
			//文件不是一个
			if(!name.equals(task.getFileName()) 
					|| lastModified!=task.getLastModified() 
					|| size!=task.getSize() 
					|| !type.equals(task.getMimetype())){
				throw new MediaPictureCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
												   task.getLastModified(), task.getSize(), task.getMimetype());
			}
			
			//文件起始位置错误
			File file = new File(task.getUploadTmpPath());
			if((!file.exists() && beginOffset!=0l) 
					|| (file.length() != beginOffset)){
				throw new MediaPictureErrorBeginOffsetException(uuid, beginOffset, file.length());
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
				mediaPictureDao.save(task);
			}
			
	        return new MediaPictureVO().set(task);
	        
		}else if(folderType.equals(FolderType.COMPANY_AUDIO)){
			MediaAudioPO task = mediaAudioDao.findByUuid(uuid);
			
			if(task == null){
				throw new MediaAudioNotExistException(uuid);
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
				mediaAudioDao.save(task);
			}
			
	        return new MediaAudioVO().set(task);
		
		}else if(folderType.equals(FolderType.COMPANY_VIDEO)){
			MediaVideoPO task = mediaVideoDao.findByUuid(uuid);
			
			if(task == null){
				throw new MediaVideoNotExistException(uuid);
			}
			
			//状态错误
			if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
				throw new MediaVideoStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
			}
			
			//文件不是一个
			if(!name.equals(task.getFileName()) 
					|| lastModified!=task.getLastModified() 
					|| size!=task.getSize() 
					|| !type.equals(task.getMimetype())){
				throw new MediaVideoCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
												   task.getLastModified(), task.getSize(), task.getMimetype());
			}
			
			//文件起始位置错误
			File file = new File(task.getUploadTmpPath());
			if((!file.exists() && beginOffset!=0l) 
					|| (file.length() != beginOffset)){
				throw new MediaVideoErrorBeginOffsetException(uuid, beginOffset, file.length());
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
				mediaVideoDao.save(task);
			}
			
	        return new MediaVideoVO().set(task);
	        
		}else if(folderType.equals(FolderType.COMPANY_COMPRESS)){
			MediaCompressPO task = mediaCompressDao.findByUuid(uuid);
			
			if(task == null){
				throw new MediaCompressNotExistException(uuid);
			}
			
			//状态错误
			if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
				throw new MediaCompressStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
			}
			
			//文件不是一个
			if(!name.equals(task.getFileName()) 
					|| lastModified!=task.getLastModified() 
					|| size!=task.getSize() 
					|| !type.equals(task.getMimetype())){
				throw new MediaCompressCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
												   task.getLastModified(), task.getSize(), task.getMimetype());
			}
			
			//文件起始位置错误
			File file = new File(task.getUploadTmpPath());
			if((!file.exists() && beginOffset!=0l) 
					|| (file.length() != beginOffset)){
				throw new MediaCompressErrorBeginOffsetException(uuid, beginOffset, file.length());
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
				mediaCompressDao.save(task);
			}
			
			return new MediaCompressVO().set(task);
		}
		
		return null;		
	}
	
	/**
	 * 删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 下午4:50:29
	 * @param String materialUuids 媒资uuid(由","拼接)
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(String materialUuids, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (materialUuids == null || materialUuids.isEmpty()) return null;
		
		List<String> uuids = Arrays.asList(materialUuids.split(","));
		
		List<MediaAudioPO> audios = new ArrayList<MediaAudioPO>();
		List<MediaVideoPO> videos = new ArrayList<MediaVideoPO>();
		for (String uuid : uuids) {
			MediaAudioPO audioPO = mediaAudioDao.findByUuid(uuid);
			if (audioPO != null) {
				audios.add(audioPO);
			} else {
				MediaVideoPO videoPO = mediaVideoDao.findByUuid(uuid);
				if (videoPO != null) {
					videos.add(videoPO);
				}
			}
		}
		mediaAudioDao.deleteInBatch(audios);
		mediaVideoDao.deleteInBatch(videos);
		return "";
	}
	
	/**
	 * 添加远程媒资(应急广播使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 下午5:24:19
	 * @param String name 媒资名称
	 * @param String type 媒资类型
	 * @param Long tagId 标签id 
	 * @param String url 媒资地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/stream")
	public Object addRemote(String name, String type, Long tagId, String httpUrl, String ftpUrl, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		TagPO tag = tagDAO.findOne(tagId);
		
		switch (type) {
		case "video":
			return mediaVideoService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
		case "audio":
			return mediaAudioService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
		default:
			return null;
		}
	}
	
	/**
	 * 根据创建时间筛选<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:08:38
	 * @param Long startTime 筛选起始时间
	 * @param Long endTime 筛选终止时间
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/time")
	public Object queryByTime(String startTime, String endTime, HttpServletRequest request) throws Exception {
		Long startLong = 0l;
		Long endLong = 32503651200000l;
		if (startTime != null && !startTime.isEmpty()) {
			startLong = DateUtil.parse(startTime, DateUtil.dateTimePattern).getTime();
		}
		if (endTime != null && !endTime.isEmpty()) {
			endLong = DateUtil.parse(endTime, DateUtil.dateTimePattern).getTime();
		}
		
		List<Object> returnList = new ArrayListWrapper<Object>()
				.addAll(mediaVideoQuery.loadByCreateTime(startLong, endLong))
				.addAll(mediaAudioQuery.loadByCreateTime(startLong, endLong))
				.getList();
		
		return returnList;
	}
	
	/**
	 * 根据条件查询媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月19日 下午3:17:30
	 * @param Long id 媒资id
	 * @param String name 名称(模糊匹配)
	 * @param String startTime updateTime起始查询
	 * @param Stinrg endTime updateTime终止查询
	 * @param Long tagId 标签id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/condition")
	public Object queryByCondition(Long id, String name, String type, String startTime, String endTime, Long tagId, HttpServletRequest request) throws Exception{
		
		List<MediaVideoVO> videoVOs = new ArrayList<MediaVideoVO>();
		List<MediaAudioVO> audioVOs = new ArrayList<MediaAudioVO>();
		
		if (type == null || !type.equals("video")) {
			audioVOs = mediaAudioQuery.loadByCondition(id, name, startTime, endTime, tagId);
		}
		
		if (type == null || !type.equals("audio")) {
			videoVOs = mediaVideoQuery.loadByCondition(id, name, startTime, endTime, tagId);
		}
		
		return new ArrayListWrapper<Object>()
				.addAll(videoVOs)
				.addAll(audioVOs)
				.getList();
	}
	
	/**
	 * 获取视频媒资预览地址<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月20日 下午2:44:22
	 * @param String type 媒资类型
	 * @param Long id 素材文件id
	 * @return String name 文件名称
	 * @return String uri 预览uri
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/uri")
	public Object downloadUri(String type, Long id) throws Exception {
		
		Long folderId = null;
		String name = null;
		String uri = null;
		if (type.equals("video")) {
			MediaVideoPO media = mediaVideoDao.findOne(id);
			
			if(media == null){
				throw new MediaVideoNotExistException(id);
			}
			folderId = media.getFolderId();
			name = media.getFileName();
			uri = media.getPreviewUrl();
		} else if (type.equals("audio")) {
			MediaAudioPO media = mediaAudioDao.findOne(id);
			
			if(media == null){
				throw new MediaVideoNotExistException(id);
			}
			folderId = media.getFolderId();
			name = media.getFileName();
			uri = media.getPreviewUrl();
		} else {
			throw new ErrorTypeException("type",type);
		}
		
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		Map<String, String> result = new HashMapWrapper<String, String>().put("name", name)
																		 .put("uri", uri)
																		 .getMap();
		
		return result;
	}
}
