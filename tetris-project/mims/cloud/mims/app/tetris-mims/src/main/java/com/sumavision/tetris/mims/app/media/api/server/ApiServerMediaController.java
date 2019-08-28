package com.sumavision.tetris.mims.app.media.api.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
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
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoTaskVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoCannotMatchException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoNotExistException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoStatusErrorWhenUploadingException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
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
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
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
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
	    FolderType type = FolderType.fromPrimaryKey(folderType);
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), type.toString());
		if(folder == null){
			throw new FolderNotExistException(new StringBufferWrapper().append("企业")
																	   .append(type.getName())
																	   .append("根文件夹不存在！")
																	   .toString());
		}
		
		if(type.equals(FolderType.COMPANY_AUDIO)){
			MediaAudioTaskVO taskParam = JSON.parseObject(task, MediaAudioTaskVO.class);
			MediaAudioPO entity = mediaAudioService.addTask(user, name, null, null, remark, taskParam, folder);			
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
			MediaVideoPO entity = mediaVideoService.addTask(user, name, null, null, remark, taskParam, folder);
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
	
}
