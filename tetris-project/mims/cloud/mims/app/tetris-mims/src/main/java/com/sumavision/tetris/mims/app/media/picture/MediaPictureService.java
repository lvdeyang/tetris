package com.sumavision.tetris.mims.app.media.picture;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.core.ProcessQuery;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.easy.process.core.ProcessVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionService;
import com.sumavision.tetris.mims.app.storage.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.storage.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.storage.StoreQuery;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 图片媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaPictureService {

	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;
	
	@Autowired
	private MediaPictureDAO mediaPictureDao;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private Path path;
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MediaFileEquipmentPermissionService mediaFileEquipmentPermissionService;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 图片媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaPicturePO media = mediaPictureDao.findOne(id);
		media.setReviewStatus(null);
		mediaPictureDao.save(media);
	}
	
	/**
	 * 图片媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaPicturePO media = mediaPictureDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaPictureDao.save(media);
	}
	
	/**
	 * 图片媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 图片媒资id
	 * @param String name 媒资名称
	 * @param String tags 贴标签，以“,”分隔
	 * @param String keyWords 关键字，以“,”分隔
	 * @param String remarks 备注
	 */
	public void editReviewPassed(
			Long id,
			String name,
			String tags,
			String keyWords,
			String remarks) throws Exception{
		MediaPicturePO media = mediaPictureDao.findOne(id);
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remarks);
		media.setReviewStatus(null);
		mediaPictureDao.save(media);
	}
	
	/**
	 * 图片媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 图片媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaPicturePO media = mediaPictureDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaPictureDao.save(media);
	}
	
	/**
	 * 图片媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 图片媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaPicturePO media = mediaPictureDao.findOne(id);
		if(media != null){
			List<MediaPicturePO> picturesCanBeDeleted = new ArrayListWrapper<MediaPicturePO>().add(media).getList();
			
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaPictures(picturesCanBeDeleted);
			
			//删除素材文件元数据
			mediaPictureDao.deleteInBatch(picturesCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> pictureIds = new HashSet<Long>();
			for(MediaPicturePO picture:picturesCanBeDeleted){
				pictureIds.add(picture.getId());
			}
			
			//删除临时文件
			for(MediaPicturePO picture:picturesCanBeDeleted){
				List<MediaPicturePO> results = mediaPictureDao.findByUploadTmpPathAndIdNotIn(picture.getUploadTmpPath(), pictureIds);
				if(results==null || results.size()<=0){
					File file = new File(new File(picture.getUploadTmpPath()).getParent());
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
	}
	
	/**
	 * 图片媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 图片媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaPicturePO media = mediaPictureDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaPictureDao.save(media);
	}
	
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
	 * @return deleted List<MediaPictureVO> 删除列表
	 * @return processed List<MediaPictureVO> 待审核列表
	 */
	public Map<String, Object> remove(Collection<MediaPicturePO> pictures) throws Exception{
		
		UserVO user = userQuery.current();
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_PICTURE);
		
		List<MediaPicturePO> picturesCanBeDeleted = new ArrayList<MediaPicturePO>();
		List<MediaPicturePO> picturesNeedProcess = new ArrayList<MediaPicturePO>();
		
		if(needProcess){
			for(MediaPicturePO picture:pictures){
				if(picture.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(picture.getReviewStatus())){
					picturesCanBeDeleted.add(picture);
				}else{
					picturesNeedProcess.add(picture);
				}
			}
			if(picturesNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_PICTURE);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaPicturePO picture:picturesNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", picture.getName());
					variables.put("tags", picture.getTags());
					variables.put("keyWords", picture.getKeyWords());
					variables.put("remark", picture.getRemarks());
					variables.put("media", serverPropsQuery.generateHttpPreviewUrl(picture.getPreviewUrl()));
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(picture.getFolderId()));
					
					//接口参数
					variables.put("_pa16_id", picture.getId());
					
					String category = new StringBufferWrapper().append("删除图片：").append(picture.getName()).toString();
					String business = new StringBufferWrapper().append("mediaPicture:").append(picture.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					picture.setProcessInstanceId(processInstanceId);
					picture.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaPictureDao.save(picturesNeedProcess);
			}
		}else{
			picturesCanBeDeleted.addAll(pictures);
		}
		
		if(picturesCanBeDeleted.size() > 0){
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaPictures(picturesCanBeDeleted);
			
			//删除素材文件元数据
			mediaPictureDao.deleteInBatch(picturesCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> pictureIds = new HashSet<Long>();
			for(MediaPicturePO picture:picturesCanBeDeleted){
				pictureIds.add(picture.getId());
			}
			
			//删除同步到设备的资源和数据
			mediaFileEquipmentPermissionService.removePermission(new ArrayList<Long>(pictureIds), "picture", null);
			
			//删除临时文件
			for(MediaPicturePO picture:picturesCanBeDeleted){
				List<MediaPicturePO> results = mediaPictureDao.findByUploadTmpPathAndIdNotIn(picture.getUploadTmpPath(), pictureIds);
				if(results==null || results.size()<=0){
					File file = new File(new File(picture.getUploadTmpPath()).getParent());
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
		return new HashMapWrapper<String, Object>().put("deleted", MediaPictureVO.getConverter(MediaPictureVO.class).convert(picturesCanBeDeleted, MediaPictureVO.class))
												   .put("processed", MediaPictureVO.getConverter(MediaPictureVO.class).convert(picturesNeedProcess, MediaPictureVO.class))
												   .getMap();
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
	 * @return MediaPicturePO 图片媒资
	 */
	public MediaPicturePO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaPictureTaskVO task, 
			FolderPO folder,
			String addition) throws Exception{
		MediaPicturePO mediaPicturePO = addTask(user, name, tags, keyWords, remark, task, folder);
		if (addition != null) {
			mediaPicturePO.setAddition(addition);
			mediaPictureDao.save(mediaPicturePO);
		}
		return mediaPicturePO;
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
	 * @return MediaPicturePO 图片媒资
	 */
	public MediaPicturePO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaPictureTaskVO task, 
			FolderPO folder) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_PICTURE);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		
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
		String version = new StringBufferWrapper().append(MediaPicturePO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String fileNamePrefix = task.getName().split("\\.")[0];
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(fileNamePrefix).append(separator).append(version).toString();
		File file = new File(folderPath);
		if(!file.exists()) file.mkdirs();
		//这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		MediaPicturePO entity = new MediaPicturePO();
		entity.setLastModified(task.getLastModified());
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
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
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		
		entity.setSynchro(task.getSynchro());
		
		mediaPictureDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 编辑图片媒资信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午3:19:38
	 * @param picture 图片媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaPicturePO 图片媒资
	 */
	public MediaPicturePO editTask(
			UserVO user,
			MediaPicturePO picture,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark) throws Exception{
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_PICTURE);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_PICTURE);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", picture.getName());
			variables.put("oldTags", picture.getTags());
			variables.put("oldKeyWords", picture.getKeyWords());
			variables.put("oldRemark", picture.getRemarks());
			variables.put("oldMedia", serverPropsQuery.generateHttpPreviewUrl(picture.getPreviewUrl()));
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(picture.getFolderId()));
			
			//接口参数
			variables.put("_pa14_id", picture.getId());
			variables.put("_pa14_name", name);
			variables.put("_pa14_tags", transTags);
			variables.put("_pa14_keyWords", transKeyWords);
			variables.put("_pa14_remarks", remark);
			
			String category = new StringBufferWrapper().append("修改图片：").append(picture.getName()).toString();
			String business = new StringBufferWrapper().append("mediaPicture:").append(picture.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			picture.setProcessInstanceId(processInstanceId);
			picture.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
		}else{
			picture.setName(name);
			picture.setRemarks(remark);
			picture.setTags(transTags);
			picture.setKeyWords(transKeyWords);
		}
		mediaPictureDao.save(picture);
		return picture;
	}
	
	/**
	 * 素材上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MediaPicturePO task 素材上传任务
	 */
	public void uploadCancel(MediaPicturePO task) throws Exception{
		File file = new File(new File(task.getUploadTmpPath()).getParent());
		File[] children = file.listFiles();
		for(File sub:children){
			sub.delete();
		}
		file.delete();
		mediaPictureDao.delete(task);
	}
	
	/**
	 * 复制图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaPicturePO media 待复制的图片媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaPicturePO 复制后的图片媒资
	 */
	public MediaPicturePO copy(MediaPicturePO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaPicturePO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaPictureDao.save(copiedMedia);
		
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
	 * @return MediaPicturePO 图片媒资
	 */
	public MediaPicturePO add(
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
		String version = new StringBufferWrapper().append(MediaPicturePO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		
		MediaPicturePO entity = new MediaPicturePO();
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
		
		mediaPictureDao.save(entity);
		
		return entity;
	}
	
}
