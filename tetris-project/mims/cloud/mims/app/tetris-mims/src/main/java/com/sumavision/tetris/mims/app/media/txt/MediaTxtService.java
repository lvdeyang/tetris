package com.sumavision.tetris.mims.app.media.txt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.store.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.store.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.store.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserVO;

/**
 * 文本媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaTxtService {
	
	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;

	@Autowired
	private MediaTxtDAO mediaTxtDao;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private Path path;
	
	/**
	 * 文本媒资删除<br/>
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
	 * @param Collection<MediaTxtPO> videos 文本媒资列表
	 */
	public void remove(Collection<MediaTxtPO> txtStreams) throws Exception{
		
		//生成待删除存储文件数据
		List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaTxts(txtStreams);
				
		//删除文本元数据
		mediaTxtDao.deleteInBatch(txtStreams);
				
		//保存待删除存储文件数据
		preRemoveFileDao.save(preRemoveFiles);
				
		//调用flush使sql生效
		preRemoveFileDao.flush();
				
		//将待删除存储文件数据押入存储文件删除队列
		storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
				
		Set<Long> txtIds = new HashSet<Long>();
		for(MediaTxtPO txt:txtStreams){
			txtIds.add(txt.getId());
		}
				
		//删除临时文件
		for(MediaTxtPO txt:txtStreams){
			List<MediaTxtPO> results = mediaTxtDao.findByUploadTmpPathAndIdNotIn(txt.getUploadTmpPath(), txtIds);
			if(results==null || results.size()<=0){
				File file = new File(new File(txt.getUploadTmpPath()).getParent());
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
	 * 添加文本媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String content 文本内容
	 * @param FolderPO folder 文件夹
	 * @return MediaTxtPO 文本媒资
	 */
	public MediaTxtPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String content,
			FolderPO folder) throws Exception{
		
		String fileName = null;
		String[] splite = name.split("\\.");
		if (splite.length == 2) {
			fileName = name;
		}else {
			fileName = new StringBufferWrapper().append(name).append(".txt").toString();
		}
		
		MediaTxtPO mediaTxt = this.addTask(user, name, new MediaTxtTaskVO().setName(fileName).setSize(0l), tags, keyWords, remark, content, folder);
		
		File file = new File(mediaTxt.getUploadTmpPath());
		writeTxt(file, content);
		
		mediaTxt.setSize(file.length());
		mediaTxt.setUploadStatus(UploadStatus.COMPLETE);
		mediaTxt.setLastModified(file.lastModified());
		
		return mediaTxt;
	}
	
	/**
	 * 添加文本媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月9日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String content 文本内容
	 * @param FolderPO folder 文件夹
	 * @return MediaTxtPO 文本媒资
	 */
	public MediaTxtPO addTask(
			UserVO user,
			String name,
			MediaTxtTaskVO task, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String content,
			FolderPO folder) throws Exception{
		
		String fileName = task.getName();
		String separator = File.separator;
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper()
				.append(webappPath)
				.append("upload")
				.append(separator)
				.append("tmp")
				.append(separator)
				.append(user.getGroupName())
				.append(separator)
				.append(folder.getUuid())
				.toString();
		
		Date date = new Date();
		String version = new StringBufferWrapper()
				.append(MediaTxtPO.VERSION_OF_ORIGIN)
				.append(".")
				.append(date.getTime())
				.toString();
		String fileNamePrefix = fileName.split("\\.")[0];
		String folderPath = new StringBufferWrapper()
				.append(basePath)
				.append(separator)
				.append(fileNamePrefix)
				.append(separator)
				.append(version)
				.toString();
		File file = new File(folderPath);
		if (!file.exists()) file.mkdirs();
		Thread.sleep(1);
		String storePath = new StringBufferWrapper()
				.append(folderPath)
				.append(separator)
				.append(fileName)
				.toString();
		String previewUrl = new StringBufferWrapper()
				.append("upload/tmp/")
				.append(user.getGroupName())
				.append("/")
				.append(folder.getUuid())
				.append("/")
				.append(fileNamePrefix)
				.append("/")
				.append(version)
				.append("/")
				.append(fileName)
				.toString();
		
		MediaTxtPO entity = new MediaTxtPO();
		entity.setName(name);
		entity.setTags("");
		entity.setKeyWords("");
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.UPLOADING);
		entity.setContent(content);
		entity.setUpdateTime(date);
		entity.setUploadTmpPath(storePath);
		entity.setPreviewUrl(previewUrl);
		entity.setSize(task.getSize());
		entity.setFileName(fileName);
		mediaTxtDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 编辑文本媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午5:01:28
	 * @param user 用户
	 * @param txt 文本媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键词列表
	 * @param remark 备注
	 * @param content 文本内容
	 * @return MediaTxtPO 文本媒资
	 */
	public MediaTxtPO editTask(
			UserVO user,
			MediaTxtPO txt,
			String name,
			List<String> tags, 
			List<String> keyWords, 
			String remark,
			String content) throws Exception{

		txt.setName(name);
		txt.setRemarks(remark);
		txt.setContent(content);
		if (txt.getUploadTmpPath() != null && !txt.getUploadTmpPath().isEmpty()) {
			File file = new File(txt.getUploadTmpPath());
			writeTxt(file, content);
			txt.setSize(file.length());
			txt.setLastModified(file.lastModified());
		}
		mediaTxtDao.save(txt);
		
		return txt;
	}
	
	/**
	 * 编辑文本媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午5:01:28
	 * @param user 用户
	 * @param txt 文本媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键词列表
	 * @param remark 备注
	 * @param content 文本内容
	 * @return MediaTxtPO 文本媒资
	 */
	public MediaTxtPO editTaskForAndroid(
			UserVO user,
			MediaTxtPO txt,
			String name,
			List<String> tags, 
			List<String> keyWords, 
			String remark) throws Exception{

		txt.setName(name);
		txt.setRemarks(remark);
		mediaTxtDao.save(txt);
		
		return txt;
	}
	
	/**
	 * 复制文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaAudioStreamPO media 待复制的文本媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaTxtPO 复制后的文本媒资
	 */
	public MediaTxtPO copy(MediaTxtPO media, FolderPO target) throws Exception{
		
		MediaTxtPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		String newName = new StringBufferWrapper().append(media.getName().split("\\.")[0])
																           .append("-副本")
																           .toString();
		copiedMedia.setName(newName);
		copiedMedia.setFolderId(target.getId());
		String fileUrl = new StringBufferWrapper().append(media.getUploadTmpPath().split(media.getFileName())[0]).append(newName).append(".txt").toString();
		File file = new File(fileUrl);
		writeTxt(file, media.getContent());
		copiedMedia.setPreviewUrl(new StringBufferWrapper().append(media.getPreviewUrl().split(media.getFileName())[0]).append(newName).append(".txt").toString());
		copiedMedia.setUploadTmpPath(fileUrl);
		copiedMedia.setFileName(file.getName());
		copiedMedia.setLastModified(file.lastModified());
		
		mediaTxtDao.save(copiedMedia);
		
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
	 * @param String folderType 文件夹类型 
	 * @param String content 内容
	 * @return MediaPicturePO 图片媒资
	 */
	public MediaTxtPO add(
			UserVO user,
			String name,
			String folderType,
			String content
			) throws Exception{
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), type.toString());
		
		Date date = new Date();
		
		MediaTxtPO entity = new MediaTxtPO();
		entity.setName(name);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setContent(content);
		entity.setFolderId(folder.getId());
		entity.setUpdateTime(date);
		
		return entity;
	}
	
	/**
	 * 素材上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MediaAudioPO task 素材上传任务
	 */
	public void uploadCancelForAndroid(MediaTxtPO task) throws Exception{
		File file = new File(new File(task.getUploadTmpPath()).getParent());
		File[] children = file.listFiles();
		if(children!=null && children.length>0){
			for(File sub:children){
				sub.delete();
			}
		}
		file.delete();
		mediaTxtDao.delete(task);
	}
	
	public void writeTxt(File file, String content) throws IOException{
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file.getPath());
			fWriter.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fWriter.flush();
				fWriter.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
}
