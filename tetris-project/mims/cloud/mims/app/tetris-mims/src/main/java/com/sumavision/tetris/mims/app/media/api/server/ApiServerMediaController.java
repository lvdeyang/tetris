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
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.StoreType;
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
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureTaskVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureCannotMatchException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureNotExistException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamPO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamService;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mims.app.media.tag.TagPO;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtDAO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtPO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtStatusErrorWhenUploadingException;
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
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

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
	private ServerProps serverProps;
	
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
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaTxtService mediaTxtService;
	
	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	
	@Autowired
	private MediaAudioStreamService mediaAudioStreamService;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
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
	private MediaTxtDAO mediaTxtDao;
	
	@Autowired
	private TagQuery tagQuery;
	
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
            String addition,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
	    FolderType type = FolderType.fromPrimaryKey(folderType);
	    FolderPO folder;
	    if (folderId == null) {
	    	List<FolderPO> folders = folderQuery.findPermissionCompanyTree(type.toString());
			if(folders == null || folders.isEmpty()){
				throw new FolderNotExistException(new StringBufferWrapper().append("企业")
																		   .append(type.getName())
																		   .append("根文件夹不存在！")
																		   .toString());
			}
			folder = folders.get(0);
		} else {
			folder = folderDao.findOne(folderId);
			if(folder == null){
				throw new FolderNotExistException(new StringBufferWrapper().append("文件夹")
																		   .append(folderId)
																		   .append("不存在！")
																		   .toString());
			}
		}
	    
	    List<String> tagNames = new ArrayList<String>();
	    if (tagId != null) {
	    	List<Long> tagIds = Arrays.asList(tagId.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		    List<TagVO> tags = tagQuery.queryByIds(tagIds);
		    if (tags != null) {
		    	for (TagVO tagPO : tags) {
					tagNames.add(tagPO.getName());
				}
			}
		}
		
		if(type.equals(FolderType.COMPANY_AUDIO)){
			MediaAudioTaskVO taskParam = JSON.parseObject(task, MediaAudioTaskVO.class);
			MediaAudioPO entity = mediaAudioService.addTask(user, name, tagNames, null, remark, false, taskParam, folder, addition);			
			return new MediaAudioVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_AUDIO_STREAM)){
			MediaAudioStreamPO entity = mediaAudioStreamService.addTask(user, name, null, null, remark, task, folder, addition);
			return new MediaAudioStreamVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_PICTURE)){
			MediaPictureTaskVO taskParam = JSON.parseObject(task, MediaPictureTaskVO.class);
			MediaPicturePO entity = mediaPictureService.addTask(user, name, tagNames, null, remark, taskParam, folder, addition);
			return new MediaPictureVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_TXT)){
			MediaTxtPO entity = mediaTxtService.addTask(user, name, tagNames, null, remark, task, folder, false, addition);
			return new MediaTxtVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_VIDEO)){
			MediaVideoTaskVO taskParam = JSON.parseObject(task, MediaVideoTaskVO.class); 
			MediaVideoPO entity = mediaVideoService.addTask(user, name, tagNames, null, remark, taskParam, folder, addition);
			return new MediaVideoVO().set(entity);
		}else if(type.equals(FolderType.COMPANY_VIDEO_STREAM)){
			return mediaVideoStreamService.addTask(user, name, null, null, remark, new ArrayListWrapper<String>().add(task).getList(), folder, addition);			
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
				MultimediaInfo multimediaInfo = new Encoder().getInfo(file);
				task.setDuration(multimediaInfo.getDuration());
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
				MultimediaInfo multimediaInfo = new Encoder().getInfo(file);
				task.setDuration(multimediaInfo.getDuration());
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
		} else if (folderType.equals(FolderType.COMPANY_TXT)) {
			MediaTxtPO task = mediaTxtDao.findByUuid(uuid);
			
			if(task == null){
				throw new MediaTxtNotExistException(uuid);
			}
			
			//状态错误
			if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
				throw new MediaTxtStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
			}
			
			//文件起始位置错误
			File file = new File(task.getUploadTmpPath());
			if((!file.exists() && beginOffset!=0l) 
					|| (file.length() != beginOffset)){
				throw new MediaTxtErrorBeginOffsetException(uuid, beginOffset, file.length());
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
				
				String content = FileUtil.readAsString(task.getUploadTmpPath());
				task.setContent(content);
				mediaTxtDao.save(task);
			}
			
	        return new MediaTxtVO().set(task);
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
	public Object remove(String deletions, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		if (deletions == null || deletions.isEmpty()) return null;
		
		List<ServerRemoveVO> removeVOs = JSON.parseArray(deletions, ServerRemoveVO.class);
		
		for (ServerRemoveVO serverRemoveVO : removeVOs) {
			String type = serverRemoveVO.getType();
			String idsString = serverRemoveVO.getMaterialIds();
			if (idsString == null || idsString.isEmpty()) continue;
			List<Long> ids = Arrays.asList(idsString.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			switch (type.toLowerCase()) {
			case "audio":
				List<MediaAudioPO> audios = mediaAudioDao.findAll(ids);
				mediaAudioService.remove(audios);
				break;
			case "video":
				List<MediaVideoPO> videos = mediaVideoDao.findAll(ids);
				mediaVideoService.remove(videos);
				break;
			case "picture":
				List<MediaPicturePO> pictures = mediaPictureDao.findAll(ids);
				mediaPictureService.remove(pictures);
				break;
			case "txt":
				List<MediaTxtPO> txts = mediaTxtDao.findAll(ids);
				mediaTxtService.remove(txts);
				break;
			case "videostream":
				mediaVideoStreamService.removeByIds(ids);
				break;
			case "audiostream":
				mediaAudioStreamService.removeByIds(ids);
				break;
			default:
				break;
			}
		}
		
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
	public Object addRemote(String name, String type, Long tagId, String httpUrl, String ftpUrl, String addition, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		TagVO tag = tagQuery.queryById(tagId);
		
		switch (type.toLowerCase()) {
//		case "video":
//			return mediaVideoService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
//		case "audio":
//			return mediaAudioService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
		case "videostream":
			return mediaVideoStreamService.addVideoStreamTask(httpUrl, tag == null ? "" : tag.getName(), name, addition);
		case "audiostream":
			return mediaAudioStreamService.addAudioStreamTask(httpUrl, tag == null ? "" : tag.getName(), name, addition);
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
		
		List<MediaVideoVO> videoVOs = mediaVideoQuery.loadByCreateTime(startLong, endLong);
		List<MediaAudioVO> audioVOs = mediaAudioQuery.loadByCreateTime(startLong, endLong);
		List<MediaPictureVO> mediaPictureVOs = mediaPictureQuery.loadByCreateTime(startLong, endLong);
		List<MediaTxtVO> mediaTxtVOs = mediaTxtQuery.loadByCreateTime(startLong, endLong);
		List<MediaVideoStreamVO> mediaVideoStreamVOs = mediaVideoStreamQuery.loadByCreateTime(startLong, endLong);
		List<MediaAudioStreamVO> mediaAudioStreamVOs = mediaAudioStreamQuery.loadByCreateTime(startLong, endLong);
		
		HashMapWrapper<String, List<Object>> map = new HashMapWrapper<String, List<Object>>();
		for (MediaVideoVO mediaVideoVO : videoVOs) {
			String mimeType = mediaVideoVO.getMimetype();
			if (mimeType == null || mimeType.isEmpty()) continue;
			if (map.containsKey(mimeType)) {
				map.getMap().get(mimeType).add(mediaVideoVO);
			}else {
				map.put(mimeType, new ArrayListWrapper<Object>().add(mediaVideoVO).getList());
			}
		}
		
		for (MediaAudioVO mediaAudioVO : audioVOs) {
			String mimeType = mediaAudioVO.getMimetype();
			if (mimeType == null || mimeType.isEmpty()) continue;
			if (map.containsKey(mimeType)) {
				map.getMap().get(mimeType).add(mediaAudioVO);
			}else {
				map.put(mimeType, new ArrayListWrapper<Object>().add(mediaAudioVO).getList());
			}
		}
		
		for (MediaPictureVO mediaPictureVO : mediaPictureVOs) {
			String mimeType = mediaPictureVO.getMimetype();
			if (mimeType == null || mimeType.isEmpty()) continue;
			if (map.containsKey(mimeType)) {
				map.getMap().get(mimeType).add(mediaPictureVO);
			}else {
				map.put(mimeType, new ArrayListWrapper<Object>().add(mediaPictureVO).getList());
			}
		}
		
		for (MediaTxtVO mediaTxtVO : mediaTxtVOs) {
			String mimeType = "text/plain";
			if (map.containsKey(mimeType)) {
				map.getMap().get(mimeType).add(mediaTxtVO);
			}else {
				map.put(mimeType, new ArrayListWrapper<Object>().add(mediaTxtVO).getList());
			}
		}
		
		for (MediaVideoStreamVO mediaVideoStreamVO : mediaVideoStreamVOs) {
			String mimeType = "application/x-mpegURL";
			if (map.containsKey(mimeType)) {
				map.getMap().get(mimeType).add(mediaVideoStreamVO);
			}else {
				map.put(mimeType, new ArrayListWrapper<Object>().add(mediaVideoStreamVO).getList());
			}
		}
		
		for (MediaAudioStreamVO mediaAudioStreamVO : mediaAudioStreamVOs) {
			String mimeType = "application/x-mpegURL";
			if (map.containsKey(mimeType)) {
				map.getMap().get(mimeType).add(mediaAudioStreamVO);
			}else {
				map.put(mimeType, new ArrayListWrapper<Object>().add(mediaAudioStreamVO).getList());
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		for (String mimetype : map.getMap().keySet()) {
			jsonObject.put(mimetype, map.getMap().get(mimetype));
		}
		
		return jsonObject;
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
		List<MediaPictureVO> pictureVOs = new ArrayList<MediaPictureVO>();
		List<MediaTxtVO> txtVOs = new ArrayList<MediaTxtVO>();
		List<MediaVideoStreamVO> videoStreamVOs = new ArrayList<MediaVideoStreamVO>();
		List<MediaAudioStreamVO> audioStreamVOs = new ArrayList<MediaAudioStreamVO>();
		
		if (type == null) {
			videoVOs = mediaVideoQuery.loadByCondition(id, name, startTime, endTime, tagId);
			audioVOs = mediaAudioQuery.loadByCondition(id, name, startTime, endTime, tagId);
			pictureVOs = mediaPictureQuery.loadByCondition(id, name, startTime, endTime, tagId);
			txtVOs = mediaTxtQuery.loadByCondition(id, name, startTime, endTime, tagId);
			videoStreamVOs = mediaVideoStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
			audioStreamVOs = mediaAudioStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
		} else {
			switch (type.toLowerCase()) {
			case "video":
				videoVOs = mediaVideoQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "audio":
				audioVOs = mediaAudioQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "picture":
				pictureVOs = mediaPictureQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "txt":
				txtVOs = mediaTxtQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "videostream":
				videoStreamVOs = mediaVideoStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			case "audiostream":
				audioStreamVOs = mediaAudioStreamQuery.loadByCondition(id, name, startTime, endTime, tagId);
				break;
			default:
				throw new ErrorTypeException("type",type);
			}
		}
		
		return new ArrayListWrapper<Object>()
				.addAll(videoVOs)
				.addAll(audioVOs)
				.addAll(pictureVOs)
				.addAll(txtVOs)
				.addAll(videoStreamVOs)
				.addAll(audioStreamVOs)
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
		String duration = "-";
		
		StringBufferWrapper stringBufferWrapper = new StringBufferWrapper().append("http://")
				.append(serverProps.getIp())
				.append(":")
				.append(serverProps.getPort())
				.append("/");
		
		switch (type) {
		case "video":
			MediaVideoPO video = mediaVideoDao.findOne(id);
			if(video == null){
				throw new MediaVideoNotExistException(id);
			}
			folderId = video.getFolderId();
			name = video.getFileName();
			uri = (video.getStoreType() == StoreType.REMOTE) ? video.getPreviewUrl() : stringBufferWrapper.append(video.getPreviewUrl()).toString();
			if (video.getDuration() != null) duration = video.getDuration().toString();
			break;
		case "audio":
			MediaAudioPO audio = mediaAudioDao.findOne(id);
			if(audio == null){
				throw new MediaVideoNotExistException(id);
			}
			folderId = audio.getFolderId();
			name = audio.getFileName();
			uri = (audio.getStoreType() == StoreType.REMOTE) ? audio.getPreviewUrl() : stringBufferWrapper.append(audio.getPreviewUrl()).toString();
			if (audio.getDuration() != null) duration = audio.getDuration().toString();
			break;
		case "picture":
			MediaPicturePO picture = mediaPictureDao.findOne(id);
			if(picture == null){
				throw new MediaVideoNotExistException(id);
			}
			folderId = picture.getFolderId();
			name = picture.getFileName();
			uri = stringBufferWrapper.append(picture.getPreviewUrl()).toString();
			break;
		case "txt":
			MediaTxtPO txt = mediaTxtDao.findOne(id);
			if(txt == null){
				throw new MediaVideoNotExistException(id);
			}
			folderId = txt.getFolderId();
			name = txt.getFileName();
			uri = stringBufferWrapper.append(txt.getPreviewUrl()).toString();
			break;
		case "videostream":
			MediaVideoStreamVO videoStreamVO = mediaVideoStreamQuery.findById(id);
			name = videoStreamVO.getName();
			uri = videoStreamVO.getPreviewUrl().isEmpty() ? "" : videoStreamVO.getPreviewUrl().get(0);
			break;
		case "audiostream":
			MediaAudioStreamVO audioStreamVO = mediaAudioStreamQuery.findById(id);
			name = audioStreamVO.getName();
			uri = audioStreamVO.getPreviewUrl();
		default:
			throw new ErrorTypeException("type",type);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		Map<String, String> result = new HashMapWrapper<String, String>().put("name", name)
																		 .put("uri", uri)
																		 .put("duration", duration)
																		 .getMap();
		
		return result;
	}
}
