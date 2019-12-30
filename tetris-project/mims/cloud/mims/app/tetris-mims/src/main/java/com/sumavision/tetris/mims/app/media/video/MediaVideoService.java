package com.sumavision.tetris.mims.app.media.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.FolderNotFoundException;

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
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.storage.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.storage.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.storage.StoreQuery;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

/**
 * 图片媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaVideoService {

	@Autowired
	private StoreQuery storeTool;
	
	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;
	
	@Autowired
	private MediaVideoDAO mediaVideoDao;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private Path path;
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private ProcessService processService;
	
	/**
	 * 视频媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaVideoPO media = mediaVideoDao.findOne(id);
		media.setReviewStatus(null);
		mediaVideoDao.save(media);
	}
	
	/**
	 * 视频媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaVideoPO media = mediaVideoDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaVideoDao.save(media);
	}
	
	/**
	 * 视频媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 视频媒资id
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
		MediaVideoPO media = mediaVideoDao.findOne(id);
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remarks);
		media.setReviewStatus(null);
		mediaVideoDao.save(media);
	}
	
	/**
	 * 视频媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 视频媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaVideoPO media = mediaVideoDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaVideoDao.save(media);
	}
	
	/**
	 * 视频媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 视频媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaVideoPO media = mediaVideoDao.findOne(id);
		if(media != null){
			List<MediaVideoPO> videosCanBeDeleted = new ArrayListWrapper<MediaVideoPO>().add(media).getList();
			
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaVideos(videosCanBeDeleted);
			
			//删除素材文件元数据
			mediaVideoDao.deleteInBatch(videosCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> videoIds = new HashSet<Long>();
			for(MediaVideoPO video:videosCanBeDeleted){
				videoIds.add(video.getId());
			}
			
			//删除临时文件
			for(MediaVideoPO video:videosCanBeDeleted){
				List<MediaVideoPO> results = mediaVideoDao.findByUploadTmpPathAndIdNotIn(video.getUploadTmpPath(), videoIds);
				if(results==null || results.size()<=0){
					File file = new File(new File(video.getUploadTmpPath()).getParent());
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
	 * 视频媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 视频媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaVideoPO media = mediaVideoDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaVideoDao.save(media);
	}
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午9:12:35
	 * @param List<Long> ids 预删除媒资id数组
	 * @return deleted List<MediaVideoVO> 删除的数据列表
	 * @return processed List<MediaVideoVO> 待审核的数据列表
	 */
	public Map<String, Object> remove(List<Long> ids) throws Exception {
		List<MediaVideoPO> mediaVideoPOs = mediaVideoDao.findAll(ids);
		if (mediaVideoPOs == null || mediaVideoPOs.isEmpty()) return null;
		return remove(mediaVideoPOs);
	}
	
	/**
	 * 视频媒资删除<br/>
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
	 * @param Collection<MediaVideoPO> videos 视频媒资列表
	 * @return deleted List<MediaVideoVO> 删除的数据列表
	 * @return processed List<MediaVideoVO> 待审核的数据列表
	 */
	public Map<String, Object> remove(Collection<MediaVideoPO> videos) throws Exception{
		
		UserVO user = userQuery.current();
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_VIDEO);
		
		List<MediaVideoPO> videosCanBeDeleted = new ArrayList<MediaVideoPO>();
		List<MediaVideoPO> videosNeedProcess = new ArrayList<MediaVideoPO>();
		
		if(needProcess){
			for(MediaVideoPO video:videos){
				if(video.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(video.getReviewStatus())){
					videosCanBeDeleted.add(video);
				}else{
					videosNeedProcess.add(video);
				}
			}
			if(videosNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_VIDEO);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaVideoPO video:videosNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", video.getName());
					variables.put("tags", video.getTags());
					variables.put("keyWords", video.getKeyWords());
					variables.put("remark", video.getRemarks());
					variables.put("media", serverPropsQuery.generateHttpPreviewUrl(video.getPreviewUrl()));
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(video.getFolderId()));
					
					//接口参数
					variables.put("_pa22_id", video.getId());
					
					String category = new StringBufferWrapper().append("删除视频：").append(video.getName()).toString();
					String business = new StringBufferWrapper().append("mediaVideo:").append(video.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					video.setProcessInstanceId(processInstanceId);
					video.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaVideoDao.save(videosNeedProcess);
			}
		}else{
			videosCanBeDeleted.addAll(videos);
		}
		
		if(videosCanBeDeleted.size() > 0){
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaVideos(videosCanBeDeleted);
			
			//删除素材文件元数据
			mediaVideoDao.deleteInBatch(videosCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> videoIds = new HashSet<Long>();
			for(MediaVideoPO video:videosCanBeDeleted){
				videoIds.add(video.getId());
			}
			
			//删除临时文件
			for(MediaVideoPO video:videosCanBeDeleted){
				List<MediaVideoPO> results = mediaVideoDao.findByUploadTmpPathAndIdNotIn(video.getUploadTmpPath(), videoIds);
				if(results==null || results.size()<=0 && video.getStoreType()!=StoreType.REMOTE){
					File file = new File(new File(video.getUploadTmpPath()).getParent());
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
		return new HashMapWrapper<String, Object>().put("deleted", MediaVideoVO.getConverter(MediaVideoVO.class).convert(videosCanBeDeleted, MediaVideoVO.class))
												   .put("processed", MediaVideoVO.getConverter(MediaVideoVO.class).convert(videosNeedProcess, MediaVideoVO.class))
												   .getMap();
	}
	
	/**
	 * 添加视频媒资(远程视频媒资)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午1:44:06
	 * @param String name 媒资名称
	 * @param String httpUrl 媒资预览地址
	 * @param String ftpUrl 媒资存储ftp路径
	 * @param String tags 标签
	 * @return MediaAudioVO 视频媒资信息
	 * @throws Exception 
	 */
	public MediaVideoVO addTask(
			UserVO user,
			String name,
			String tags,
			String previewUrl,
			String ftpUrl) throws Exception{
		String version = new StringBufferWrapper().append(MediaVideoPO.VERSION_OF_ORIGIN).append(".").append(new Date().getTime()).toString();
		List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
		if(folders==null || folders.size()<=0) throw new FolderNotFoundException();
		List<FolderPO> rootFolders = folderQuery.findRoots(folders);
		FolderPO folder = rootFolders.get(0);
		if (folder == null) throw new FolderNotFoundException();
		
		MediaVideoPO mediaVideoPO = new MediaVideoPO();
		mediaVideoPO.setUpdateTime(new Date());
		mediaVideoPO.setName(name);
		mediaVideoPO.setTags(tags);
		mediaVideoPO.setKeyWords("");
		mediaVideoPO.setAuthorId(user.getUuid());
		mediaVideoPO.setVersion(version);
		mediaVideoPO.setFolderId(folder.getId());
		mediaVideoPO.setMimetype("application/x-mpegURL");
		mediaVideoPO.setUploadStatus(UploadStatus.COMPLETE);
		mediaVideoPO.setPreviewUrl(previewUrl);
		mediaVideoPO.setUploadTmpPath(ftpUrl);
		mediaVideoPO.setStoreType(StoreType.REMOTE);
		if (previewUrl.endsWith(".m3u8")){
			Long duration = getHlsDuration(previewUrl, null);
			if (duration != 0l) mediaVideoPO.setDuration(duration);
		}
		mediaVideoDao.save(mediaVideoPO);
		
		return new MediaVideoVO().set(mediaVideoPO);
	}
	
	/**
	 * 添加视频媒资上传任务<br/>
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
	 * @return MediaVideoPO 视频媒资
	 */
	public MediaVideoPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaVideoTaskVO task, 
			FolderPO folder,
			String addition) throws Exception{
		MediaVideoPO mediaVideoPO = addTask(user, name, tags, keyWords, remark, task, folder);
		if (addition != null) {
			mediaVideoPO.setAddition(addition);
			mediaVideoDao.save(mediaVideoPO);
		}
		return mediaVideoPO;
	}
	
	/**
	 * 添加视频媒资上传任务<br/>
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
	 * @return MediaVideoPO 视频媒资
	 */
	public MediaVideoPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaVideoTaskVO task, 
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
		String version = new StringBufferWrapper().append(MediaVideoPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String fileNamePrefix = task.getName().split("\\.")[0];
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(fileNamePrefix).append(separator).append(version).toString();
		File file = new File(folderPath);
		if(!file.exists()) file.mkdirs();
		//这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		
		String storePath = new StringBufferWrapper().append(folderPath)
												    .append(separator)
												    .append(task.getName())
												    .toString();
		
		String previewUrl = new StringBufferWrapper().append("upload/tmp/")
												     .append(user.getGroupName())
												     .append("/")
												     .append(folder.getUuid())
												     .append("/")
												     .append(fileNamePrefix)
												     .append("/")
												     .append(version)
												     .append("/")
												     .append(task.getName())
												     .toString();
		
		return addTask(user, name, tags, keyWords, remark, task, folder, version, storePath, previewUrl, date);
	}
	
	/**
	 * 添加视频媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 下午1:33:53
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param MediaAudioTaskVO task 上传任务
	 * @param FolderPO folder 文件夹
	 * @param String version 版本
	 * @param String storePath 存储路径
	 * @param String previewUrl 预览地址
	 * @param Date date 当前时间
	 * @return MediaVideoPO 视频媒资
	 */
	public MediaVideoPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			MediaVideoTaskVO task, 
			FolderPO folder,
			String version,
			String storePath,
			String previewUrl,
			Date date) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_VIDEO);
		MediaVideoPO entity = new MediaVideoPO();
		entity.setLastModified(task.getLastModified());
		entity.setName(name);
		StringBufferWrapper transTag = new StringBufferWrapper();
		if(tags!=null && tags.size()>0){
			for(int i=0; i<tags.size(); i++){
				transTag.append(tags.get(i));
				if(i != tags.size()-1){
					transTag.append(MediaVideoPO.SEPARATOR_TAG);
				}
			}
		}
		entity.setTags(transTag.toString());
		StringBufferWrapper transKeyWords = new StringBufferWrapper();
		if(keyWords!=null && keyWords.size()>0){
			for(int i=0; i<keyWords.size(); i++){
				transKeyWords.append(keyWords.get(i));
				if(i != keyWords.size()-1){
					transKeyWords.append(MediaVideoPO.SEPARATOR_KEYWORDS);
				}
			}
		}
		entity.setKeyWords(transKeyWords.toString());
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
		entity.setUploadTmpPath(storePath);
		entity.setPreviewUrl(previewUrl);
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		mediaVideoDao.save(entity);
		
		return entity;
	}
	
	/**
	 * 编辑视频媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午4:49:01
	 * @param user 用户
	 * @param video 视频媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaVideoPO 视频媒资
	 */
	public MediaVideoPO editTask(
			UserVO user, 
			MediaVideoPO video,
			String name,
			List<String> tags, 
			List<String> keyWords, 
			String remark) throws Exception{
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_VIDEO);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaAudioPO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaAudioPO.SEPARATOR_KEYWORDS);
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_VIDEO);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", video.getName());
			variables.put("oldTags", video.getTags());
			variables.put("oldKeyWords", video.getKeyWords());
			variables.put("oldRemark", video.getRemarks());
			variables.put("oldMedia", serverPropsQuery.generateHttpPreviewUrl(video.getPreviewUrl()));
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(video.getFolderId()));
			
			//接口参数
			variables.put("_pa20_id", video.getId());
			variables.put("_pa20_name", name);
			variables.put("_pa20_tags", transTags);
			variables.put("_pa20_keyWords", transKeyWords);
			variables.put("_pa20_remarks", remark);
			
			String category = new StringBufferWrapper().append("修改视频：").append(video.getName()).toString();
			String business = new StringBufferWrapper().append("mediaVideo:").append(video.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			video.setProcessInstanceId(processInstanceId);
			video.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
		}else{
			video.setName(name);
			video.setRemarks(remark);
			video.setTags(transTags);
			video.setKeyWords(transKeyWords);
		}
		mediaVideoDao.save(video);
		return video;
	}
	
	/**
	 * 素材上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MediaAudioPO task 素材上传任务
	 */
	public void uploadCancel(MediaVideoPO task) throws Exception{
		File file = new File(new File(task.getUploadTmpPath()).getParent());
		File[] children = file.listFiles();
		if(children!=null && children.length>0){
			for(File sub:children){
				sub.delete();
			}
		}
		file.delete();
		mediaVideoDao.delete(task);
	}
	
	/**
	 * 复制图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaAudioPO media 待复制的图片媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaVideoPO 复制后的图片媒资
	 */
	public MediaVideoPO copy(MediaVideoPO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaVideoPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaVideoDao.save(copiedMedia);
		
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
	 * @return MediaVideoPO 视频媒资
	 */
	public MediaVideoPO add(
			UserVO user,
			String name,
			String fileName,
			Long size,
			String folderType,
			String mimeType,
			String uploadTempPath,
			String tags,
			Long... folderId) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_VIDEO);
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), type.toString());
		
		Date date = new Date();
		String version = new StringBufferWrapper().append(MediaVideoPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		
		MediaVideoPO entity = new MediaVideoPO();
		entity.setName(name);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setVersion(version);
		entity.setFileName(fileName);
		entity.setSize(size);
		entity.setMimetype(mimeType);
		if (folderId != null && folderId.length > 0) {
			entity.setFolderId(folderId[0]);
		}else {
			entity.setFolderId(folder.getId());
		}
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setStoreType(StoreType.LOCAL);
		entity.setUploadTmpPath(uploadTempPath);
		entity.setPreviewUrl(new StringBufferWrapper().append("upload").append(uploadTempPath.split("upload")[1]).toString().replace("\\", "/"));
		entity.setUpdateTime(date);
		entity.setTags(tags);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		
		File file = new File(uploadTempPath);
		if (file.exists() && file.isFile()) {
			MultimediaInfo multimediaInfo = new Encoder().getInfo(file);
			entity.setDuration(multimediaInfo.getDuration());
		}
		
		if(needProcess){
			//启动流程
			startUploadProcess(entity);
			System.out.println(needProcess);
		}else{
			mediaVideoDao.save(entity);
		}
		
		return entity;
	}
	
	/**
	 * 根据视频媒资列表批量加载的视频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @param user UserVO 用户信息
	 * @param List<String> urlList 视频媒资http地址列表
	 * @param folderId 视频媒资存放路径
	 * @return List<MediaVideoPO> 视频媒资列表
	 */
	public List<MediaVideoPO> addList(UserVO user, List<String> urlList, Long folderId, String tags) throws Exception{
		
		if (urlList == null || urlList.size() <= 0) return null;
		
		String folderType = "video";
		
		List<MediaVideoPO> videos = new ArrayList<MediaVideoPO>();
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
					
					MediaVideoPO video = this.add(user, localFile.getParentFile().getName(), fileName, size, folderType, mimeType, uploadTempPath, tags, folderId);
					videos.add(video);
				}
			}	
		}
		return videos;
	}
	
	/**
	 * 启动上传审核流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月9日 上午10:54:37
	 * @param MediaVideoPO video 视频媒资
	 */
	public void startUploadProcess(MediaVideoPO video) throws Exception{
		mediaVideoDao.save(video);
		UserVO user = userQuery.current();
		Long companyId = Long.valueOf(user.getGroupId());
		MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_VIDEO);
		Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
		ProcessVO process = processQuery.findById(processId);
		JSONObject variables = new JSONObject();
		variables.put("name", video.getName());
		variables.put("tags", video.getTags());
		variables.put("keyWords", video.getKeyWords());
		variables.put("media", serverPropsQuery.generateHttpPreviewUrl(video.getPreviewUrl()));
		variables.put("remark", video.getRemarks());
		variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(video.getFolderId()));
		variables.put("_pa18_id", video.getId());
		String category = new StringBufferWrapper().append("上传视频：").append(video.getName()).toString();
		String business = new StringBufferWrapper().append("mediaVideo:").append(video.getId()).toString();
		String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
		video.setProcessInstanceId(processInstanceId);
		mediaVideoDao.save(video);
	}
	
	/**
	 * 读取远程m3u8文件获取播放时长<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 上午11:19:14
	 * @param String uri m3u8远程地址
	 * @param String encode 编码格式
	 * @return Long 时长
	 */
	public Long getHlsDuration(String uri, String encode) throws Exception {
		Long duration = 0l;
		URL url = new URL(uri);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, encode != null && !encode.isEmpty() ? encode : "utf-8"));
		String sLine = null;
		try {
			while ((sLine = br.readLine()) != null) {
				if (sLine.startsWith("#EXTINF:")) {
					duration = duration + (long)(Double.parseDouble(sLine.split("#EXTINF:")[1].split(",")[0]) * 1000);
				}
			}
		} catch (Exception e) {
		} finally {
			br.close();
		}
		return duration;
	}
}
