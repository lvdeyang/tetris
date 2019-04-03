package com.sumavision.tetris.mims.app.media.compress;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.store.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.store.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.store.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserVO;

/**
 * 播发媒资操作（主增删改）<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月31日 下午5:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaCompressService {

	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;
	
	@Autowired
	private MediaCompressDAO mediaCompressDao;
	
	@Autowired
	private Path path;
	
	/**
	 * 图片媒资删除<br/>
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
	 * @param Collection<MaterialFilePO> materials 素材列表
	 */
	public void remove(Collection<MediaCompressPO> compresses) throws Exception{
		
		//生成待删除存储文件数据
		List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaCompresses(compresses);
		
		//删除素材文件元数据
		mediaCompressDao.deleteInBatch(compresses);
		
		//保存待删除存储文件数据
		preRemoveFileDao.save(preRemoveFiles);
		
		//调用flush使sql生效
		preRemoveFileDao.flush();
		
		//将待删除存储文件数据押入存储文件删除队列
		storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
		
		Set<Long> compressIds = new HashSet<Long>();
		for(MediaCompressPO compress:compresses){
			compressIds.add(compress.getId());
		}
		
		//删除临时文件
		for(MediaCompressPO compress:compresses){
			List<MediaCompressPO> results = mediaCompressDao.findByUploadTmpPathAndIdNotIn(compress.getUploadTmpPath(), compressIds);
			if(results==null || results.size()<=0){
				File file = new File(new File(compress.getUploadTmpPath()).getParent());
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
	 * 添加图片媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param MediaVideoTaskVO task 上传任务
	 * @param FolderPO folder 文件夹
	 * @return MediaCompressPO 图片媒资
	 */
	public MediaCompressPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaCompressTaskVO task, 
			FolderPO folder) throws Exception{
		String separator = File.separator;
		//临时路径采取/base/companyName/folderuuid/fileNamePrefix/version
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath)
												   .append(separator)
												   .append("upload")
												   .append(separator)
												   .append("tmp")
												   .append(separator).append(user.getGroupName())
												   .append(separator).append(folder.getUuid())
												   .toString();
		Date date = new Date();
		String version = new StringBufferWrapper().append(MediaCompressPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String fileNamePrefix = task.getName().split("\\.")[0];
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(fileNamePrefix).append(separator).append(version).toString();
		File file = new File(folderPath);
		if(!file.exists()) file.mkdirs();
		//这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		MediaCompressPO entity = new MediaCompressPO();
		entity.setLastModified(task.getLastModified());
		entity.setName(name);
		entity.setTags("");
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
		entity.setPreviewUrl(new StringBufferWrapper().append("/upload/tmp/")
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
		
		mediaCompressDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 编辑图片媒资信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午3:19:38
	 * @param compress 图片媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaCompressPO 图片媒资
	 */
	public MediaCompressPO editTask(
			UserVO user,
			MediaCompressPO compress,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark) throws Exception{
		
		compress.setName(name);
		compress.setRemarks(remark);
		
		mediaCompressDao.save(compress);
		return compress;
	}
	
	/**
	 * 素材上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MediaCompressPO task 素材上传任务
	 */
	public void uploadCancel(MediaCompressPO task) throws Exception{
		File file = new File(new File(task.getUploadTmpPath()).getParent());
		File[] children = file.listFiles();
		for(File sub:children){
			sub.delete();
		}
		file.delete();
		mediaCompressDao.delete(task);
	}
	
	/**
	 * 复制图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaCompressPO media 待复制的图片媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaCompressPO 复制后的图片媒资
	 */
	public MediaCompressPO copy(MediaCompressPO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaCompressPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaCompressDao.save(copiedMedia);
		
		return copiedMedia;
	}
	
}
