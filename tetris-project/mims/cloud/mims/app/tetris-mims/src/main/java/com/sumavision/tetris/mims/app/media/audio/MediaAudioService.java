package com.sumavision.tetris.mims.app.media.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.audio.DoTTSUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioErrorWhenChangeFromTxtException;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtDAO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtPO;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
import com.sumavision.tetris.mims.app.store.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.store.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.store.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserVO;

/**
 * 音频媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaAudioService {

	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;
	
	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	@Autowired
	private Path path;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired MediaTxtDAO mediaTxtDAO;
	
	/**
	 * 音频媒资删除<br/>
	 * <p>
	 * 	初步设想，考虑到文件夹下可能包含大文件以及文件数量等<br/>
	 * 	情况，这里采用线程删除文件，主要步骤如下：<br/>
	 * 	  1.生成待删除存储文件数据<br/>
	 *    2.删除素材文件元数据<br/>
	 *    3.保存待删除存储文件数据<br/>
	 *    4.调用flush使sql生效<br/>
	 *    5.将待删除存储文件数据押入存储文件删除队列<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:43:03
	 * @param Collection<MediaAudioPO> audios 音频媒资列表
	 */
	public void remove(Collection<MediaAudioPO> audios) throws Exception{
		
		//生成待删除存储文件数据
		List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaAudios(audios);
		
		//删除素材文件元数据
		mediaAudioDao.deleteInBatch(audios);
		
		//保存待删除存储文件数据
		preRemoveFileDao.save(preRemoveFiles);
		
		//调用flush使sql生效
		preRemoveFileDao.flush();
		
		//将待删除存储文件数据押入存储文件删除队列
		storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
		
		Set<Long> pictureIds = new HashSet<Long>();
		for(MediaAudioPO audio:audios){
			pictureIds.add(audio.getId());
		}
		
		//删除临时文件
		for(MediaAudioPO audio:audios){
			List<MediaAudioPO> results = mediaAudioDao.findByUploadTmpPathAndIdNotIn(audio.getUploadTmpPath(), pictureIds);
			if(results==null || results.size()<=0){
				File file = new File(new File(audio.getUploadTmpPath()).getParent());
				File[] children = file.listFiles();
				if(children != null){
					for(File sub:children){
						if(sub.exists()) sub.delete();
					}
				}
				if(file.exists()) file.delete();
			}
		}
	}
	
	/**
	 * 添加音频媒资上传任务(从文本媒资转换)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param Long txtId 
	 * @param FolderPO folder 文件夹
	 */
	public MediaAudioPO addTaskFromTxt(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			Long txtId, 
			FolderPO folder) throws Exception{
		
		MediaTxtPO txt = mediaTxtDAO.findOne(txtId);
		
		if (txt == null) throw new MediaTxtNotExistException(txtId);
		
		String audioName = new StringBufferWrapper().append(name).append(".wav").toString();
		
		MediaAudioTaskVO task = new MediaAudioTaskVO().setName(audioName);
		
		MediaAudioPO audio = addTask(user, name, tags, keyWords, remark, task, folder);
		
		//change
		String txtContent = txt.getContent();
		String audioPath = audio.getUploadTmpPath();
		String changeReturn = DoTTSUtil.doTTS(audioPath, txtContent);
		if (changeReturn != null) {
			File audioFile = new File(audioPath);
			audio.setLastModified(audioFile.lastModified());
			audio.setSize(audioFile.length());
			audio.setUploadStatus(UploadStatus.COMPLETE);
			audio.setMimetype(new MimetypesFileTypeMap().getContentType(new File(audioPath)));
			mediaAudioDao.save(audio);
			return audio;
		} else {
			mediaAudioDao.delete(audio);
			throw new MediaAudioErrorWhenChangeFromTxtException(txt.getName());
		}
	}
	
	/**
	 * 添加音频媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param MediaAudioTaskVO task 上传任务
	 * @param FolderPO folder 文件夹
	 */
	public MediaAudioPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaAudioTaskVO task, 
			FolderPO folder) throws Exception{
		String separator = File.separator;
		//临时路径采取/base/companyName/folderuuid/fileNamePrefix/version
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath)
												   .append("upload")
												   .append(separator)
												   .append("tmp")
												   .append(separator).append(user.getGroupName())
												   .append(separator).append(folder.getUuid())
												   .toString();
		Date date = new Date();
		String version = new StringBufferWrapper().append(MediaAudioPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String fileNamePrefix = task.getName().split("\\.")[0];
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(fileNamePrefix).append(separator).append(version).toString();
		File file = new File(folderPath);
		if(!file.exists()) file.mkdirs();
		//这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		MediaAudioPO entity = new MediaAudioPO();
		entity.setLastModified(task.getLastModified());
		entity.setName(name);
		entity.setTags(StringUtils.join(tags.toArray(), MediaAudioPO.SEPARATOR_TAG));
		entity.setKeyWords("");
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setVersion(version);
		entity.setFileName(task.getName());
		entity.setSize(task.getSize());
		entity.setMimetype(task.getMimetype());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.UPLOADING);
		entity.setStoreType(StoreType.LOCAL);
		entity.setUploadTmpPath(new StringBufferWrapper().append(folderPath)
												   .append(separator)
												   .append(task.getName())
												   .toString());
		entity.setPreviewUrl(new StringBufferWrapper().append("upload/tmp/")
													  .append(user.getGroupName())
													  .append("/")
													  .append(folder.getUuid())
													  .append("/")
													  .append(fileNamePrefix)
													  .append("/")
													  .append(version)
													  .append("/")
													  .append(task.getName())
													  .toString());
		entity.setUpdateTime(date);
		
		mediaAudioDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 编辑音频媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午4:40:57
	 * @param user 用户
	 * @param audio 音频媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaAudioPO 音频媒资
	 */
	public MediaAudioPO editAudio(
			UserVO user, 
			MediaAudioPO audio,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark) throws Exception{
		
		audio.setName(name);
		audio.setRemarks(remark);
		audio.setTags(StringUtils.join(tags.toArray(), MediaAudioPO.SEPARATOR_TAG));
		mediaAudioDao.save(audio);
		
		return audio;
	}
	
	/**
	 * 音频媒资上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MediaAudioPO task 音频媒资上传任务
	 */
	public void uploadCancel(MediaAudioPO task) throws Exception{
		File file = new File(new File(task.getUploadTmpPath()).getParent());
		File[] children = file.listFiles();
		if(children!=null && children.length>0){
			for(File sub:children){
				sub.delete();
			}
		}
		file.delete();
		mediaAudioDao.delete(task);
	}
	
	/**
	 * 复制音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaAudioPO media 待复制的音频媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaAudioPO 复制后的音频媒资
	 */
	public MediaAudioPO copy(MediaAudioPO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaAudioPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaAudioDao.save(copiedMedia);
		
		return copiedMedia;
	}
	
	/**
	 * 添加已上传好的媒资任务<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 下午1:34:18
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param String fileName 文件名
 	 * @param Long size 文件大小
	 * @param String folderType 文件夹类型 
	 * @param String mimeType 文件类型
	 * @param String uploadTempPath 存储位置
	 * @return MediaAudioPO 音频媒资
	 */
	public MediaAudioPO add(
			UserVO user,
			String name,
			String fileName,
			Long size,
			String folderType,
			String mimeType,
			String uploadTempPath
			) throws Exception{
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), type.toString());
		
		Date date = new Date();
		String version = new StringBufferWrapper().append(MediaAudioPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		
		MediaAudioPO entity = new MediaAudioPO();
		entity.setName(name);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setVersion(version);
		entity.setFileName(fileName);
		entity.setSize(size);
		entity.setMimetype(mimeType);
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setStoreType(StoreType.LOCAL);
		entity.setUploadTmpPath(uploadTempPath);
		entity.setPreviewUrl(new StringBufferWrapper().append("upload").append(uploadTempPath.split("upload")[1]).toString().replace("\\", "/"));
		entity.setUpdateTime(date);
		
		mediaAudioDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 根据音频媒资列表批量加载的音频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @param user UserVO 用户信息
	 * @param List<String> urlList 音频媒资http地址列表
	 * @param folderId 音频媒资存放路径
	 * @return List<MediaAudioVO> 音频媒资列表
	 */
	public List<MediaAudioPO> addList(UserVO user, List<String> urlList, Long folderId) throws Exception{
		
		if (urlList == null || urlList.size() <= 0) return null;
		
		String folderType = "audio";
		
		List<MediaAudioPO> audios = new ArrayList<MediaAudioPO>();
		for (String url : urlList) {
			File file = new File(url);
			String folderPath = new StringBufferWrapper().append(path.webappPath()).append("upload").append(file.getPath().split("upload")[1]).toString();
			File localFile = new File(folderPath);
			File[] fileList = localFile.listFiles();
			for (File childFile : fileList) {
				String fileNameSuffix = childFile.getName().split("\\.")[1];
				if (fileNameSuffix.equals("xml")) {
					continue;
				}else {
					String fileName = childFile.getName();
					Long size = childFile.length();
					String uploadTempPath = childFile.getPath();
					String mimeType = new MimetypesFileTypeMap().getContentType(childFile);
					
					MediaAudioPO audio = this.add(user, file.getName(), fileName, size, folderType, mimeType,uploadTempPath);
					audios.add(audio);
				}
			}	
		}
		return audios;
	}
}
