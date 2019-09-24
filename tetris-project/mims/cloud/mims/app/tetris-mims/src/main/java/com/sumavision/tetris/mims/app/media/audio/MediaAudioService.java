package com.sumavision.tetris.mims.app.media.audio;

import java.io.File;
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
import com.sumavision.tetris.commons.util.audio.DoTTSUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.file.FileUtil;
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
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioErrorWhenChangeFromTxtException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.media.tag.TagDownloadPermissionDAO;
import com.sumavision.tetris.mims.app.media.tag.TagDownloadPermissionPO;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtDAO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtPO;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
import com.sumavision.tetris.mims.app.store.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.store.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.store.StoreQuery;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
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
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired 
	private MediaTxtDAO mediaTxtDAO;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery; 
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private TagDownloadPermissionDAO tagDownloadPermissionDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 音频媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaAudioPO media = mediaAudioDao.findOne(id);
		media.setReviewStatus(null);
		mediaAudioDao.save(media);
	}
	
	/**
	 * 音频媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaAudioPO media = mediaAudioDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaAudioDao.save(media);
	}
	
	/**
	 * 音频媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 音频媒资id
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
		MediaAudioPO media = mediaAudioDao.findOne(id);
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remarks);
		media.setReviewStatus(null);
		mediaAudioDao.save(media);
	}
	
	/**
	 * 音频媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 音频媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaAudioPO media = mediaAudioDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaAudioDao.save(media);
	}
	
	/**
	 * 音频媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 音频媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaAudioPO media = mediaAudioDao.findOne(id);
		if(media != null){
			List<MediaAudioPO> audiosCanBeDeleted = new ArrayListWrapper<MediaAudioPO>().add(media).getList();
			
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaAudios(audiosCanBeDeleted);
			
			//删除素材文件元数据
			mediaAudioDao.deleteInBatch(audiosCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> pictureIds = new HashSet<Long>();
			for(MediaAudioPO audio:audiosCanBeDeleted){
				pictureIds.add(audio.getId());
			}
			
			//删除临时文件
			for(MediaAudioPO audio:audiosCanBeDeleted){
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
	}
	
	/**
	 * 音频媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 音频媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaAudioPO media = mediaAudioDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaAudioDao.save(media);
	}
	
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
	 * @return deleted List<MediaAudioVO> 删除的数据列表
	 * @return processed List<MediaAudioVO> 待审核的数据列表
	 */
	public Map<String, Object> remove(Collection<MediaAudioPO> audios) throws Exception{
		
		UserVO user = userQuery.current();
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_AUDIO);
		
		List<MediaAudioPO> audiosCanBeDeleted = new ArrayList<MediaAudioPO>();
		List<MediaAudioPO> audiosNeedProcess = new ArrayList<MediaAudioPO>();
		
		if(needProcess){
			for(MediaAudioPO audio:audios){
				if(audio.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(audio.getReviewStatus())){
					audiosCanBeDeleted.add(audio);
				}else{
					audiosNeedProcess.add(audio);
				}
			}
			if(audiosNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_AUDIO);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaAudioPO audio:audiosNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", audio.getName());
					variables.put("tags", audio.getTags());
					variables.put("keyWords", audio.getKeyWords());
					variables.put("remark", audio.getRemarks());
					variables.put("media", serverPropsQuery.generateHttpPreviewUrl(audio.getPreviewUrl()));
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(audio.getFolderId()));
					
					//接口参数
					variables.put("_pa13_id", audio.getId());
					
					String category = new StringBufferWrapper().append("删除音频：").append(audio.getName()).toString();
					String business = new StringBufferWrapper().append("mediaAudio:").append(audio.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					audio.setProcessInstanceId(processInstanceId);
					audio.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaAudioDao.save(audiosNeedProcess);
			}
		}else{
			audiosCanBeDeleted.addAll(audios);
		}
		
		if(audiosCanBeDeleted.size() > 0){
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaAudios(audiosCanBeDeleted);
			
			//删除素材文件元数据
			mediaAudioDao.deleteInBatch(audiosCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> pictureIds = new HashSet<Long>();
			for(MediaAudioPO audio:audiosCanBeDeleted){
				pictureIds.add(audio.getId());
			}
			
			//删除临时文件
			for(MediaAudioPO audio:audiosCanBeDeleted){
				List<MediaAudioPO> results = mediaAudioDao.findByUploadTmpPathAndIdNotIn(audio.getUploadTmpPath(), pictureIds);
				if(results==null || results.size()<=0 && audio.getStoreType()!=StoreType.REMOTE){
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
		return new HashMapWrapper<String, Object>().put("deleted", MediaAudioVO.getConverter(MediaAudioVO.class).convert(audiosCanBeDeleted, MediaAudioVO.class))
												   .put("processed", MediaAudioVO.getConverter(MediaAudioVO.class).convert(audiosNeedProcess, MediaAudioVO.class))
												   .getMap();
	}
	
	/**
	 * 从文本添加音频媒资并启动流程<br/>
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
		String txtContent = txt.getContent();
		String audioName = new StringBufferWrapper().append(name).append(".wav").toString();
		MediaAudioTaskVO task = new MediaAudioTaskVO().setName(audioName);
		MediaAudioPO audio = addTask(user, name, tags, keyWords, remark, task, folder);
		
		//修改属性
		String audioPath = audio.getUploadTmpPath();
		String changeReturn = DoTTSUtil.doTTS(audioPath, txtContent);
		if(changeReturn == null){
			throw new MediaAudioErrorWhenChangeFromTxtException(name);
		}
		
		File audioFile = new File(audioPath);
		audio.setLastModified(audioFile.lastModified());
		audio.setSize(audioFile.length());
		audio.setMimetype(new MimetypesFileTypeMap().getContentType(new File(audioPath)));
		audio.setUploadStatus(UploadStatus.COMPLETE);
		if(audio.getReviewStatus() != null){
			//开启审核流程--这里会保存
			startUploadProcess(audio);
		}else{
			mediaAudioDao.save(audio);
		}
		
		return audio;
	}
	
	/**
	 * 从文本文件转换音频文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月9日 上午10:41:12
	 * @param MediaAudioPO media 上传了文本文件的音频媒资
	 * @return MediaAudioPO media 转换后的音频媒资
	 */
	public MediaAudioPO convertTxtMeidaToAudioMedia(MediaAudioPO media) throws Exception{
		String txtStorePath = media.getUploadTmpPath();
		String txtPreviewUrl = media.getPreviewUrl();
		String txt = FileUtil.readAsString(txtStorePath);
		String audioFileName = new StringBufferWrapper().append(media.getName()).append(".wav").toString();
		
		String audioStorePath = new StringBufferWrapper().append(txtStorePath.replace(media.getFileName(), "")).append(audioFileName).toString();
		String audioPreviewUrl = new StringBufferWrapper().append(txtPreviewUrl.replace(media.getFileName(), "")).append(audioFileName).toString();
		
		String changeReturn = DoTTSUtil.doTTS(audioStorePath, txt);
		if(changeReturn != null){
			File audioFile = new File(audioStorePath);
			media.setLastModified(audioFile.lastModified());
			media.setSize(audioFile.length());
			media.setMimetype(new MimetypesFileTypeMap().getContentType(audioFile));
			media.setSuffix("wav");
			media.setFileName(audioFileName);
			media.setPreviewUrl(audioPreviewUrl);
			media.setUploadTmpPath(audioStorePath);
			media.setDownloadCount(0l);
			media.setUploadStatus(UploadStatus.COMPLETE);
		}else{
			media.setUploadStatus(UploadStatus.ERROR);
		}
		mediaAudioDao.save(media);
		return media;
	}
	
	/**
	 * 添加音频媒资(远程音频媒资)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午1:44:06
	 * @param String name 媒资名称
	 * @param String httpUrl 媒资预览地址
	 * @param String ftpUrl 媒资存储ftp路径
	 * @return MediaAudioVO 音频媒资信息
	 * @throws Exception 
	 */
	public MediaAudioVO addTask(
			UserVO user,
			String name,
			String tags,
			String previewUrl,
			String ftpUrl) throws Exception{
		String version = new StringBufferWrapper().append(MediaAudioPO.VERSION_OF_ORIGIN).append(".").append(new Date().getTime()).toString();
		FolderPO folder = folderDao.findCompanyFolderByTypeAndName(user.getGroupId(), FolderType.COMPANY_AUDIO.toString(), "快编目录");
		if (folder == null) throw new FolderNotFoundException();
		MediaAudioPO mediaAudioPO = new MediaAudioPO();
		mediaAudioPO.setUpdateTime(new Date());
		mediaAudioPO.setName(name);
		mediaAudioPO.setTags(tags);
		mediaAudioPO.setKeyWords("");
		mediaAudioPO.setAuthorId(user.getUuid());
		mediaAudioPO.setVersion(version);
		mediaAudioPO.setFolderId(folder.getId());
		mediaAudioPO.setMimetype("application/vnd.apple.mpegurl");
		mediaAudioPO.setUploadStatus(UploadStatus.COMPLETE);
		mediaAudioPO.setPreviewUrl(previewUrl);
		mediaAudioPO.setUploadTmpPath(ftpUrl);
		mediaAudioPO.setStoreType(StoreType.REMOTE);
		mediaAudioPO.setAuthorName(user.getNickname());
		mediaAudioPO.setDownloadCount(0l);
		mediaAudioDao.save(mediaAudioPO);
		
		return new MediaAudioVO().set(mediaAudioPO);
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
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_AUDIO);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaAudioPO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaAudioPO.SEPARATOR_KEYWORDS);
		
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
		entity.setDownloadCount(0l);
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
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_AUDIO);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaAudioPO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaAudioPO.SEPARATOR_KEYWORDS);
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_AUDIO);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", audio.getName());
			variables.put("oldTags", audio.getTags());
			variables.put("oldKeyWords", audio.getKeyWords());
			variables.put("oldRemark", audio.getRemarks());
			variables.put("oldMedia", serverPropsQuery.generateHttpPreviewUrl(audio.getPreviewUrl()));
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(audio.getFolderId()));
			
			//接口参数
			variables.put("_pa10_id", audio.getId());
			variables.put("_pa10_name", name);
			variables.put("_pa10_tags", transTags);
			variables.put("_pa10_keyWords", transKeyWords);
			variables.put("_pa10_remarks", remark);
			
			String category = new StringBufferWrapper().append("修改音频：").append(audio.getName()).toString();
			String business = new StringBufferWrapper().append("mediaAudio:").append(audio.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			audio.setProcessInstanceId(processInstanceId);
			audio.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
		}else{
			audio.setName(name);
			audio.setRemarks(remark);
			audio.setTags(transTags);
			audio.setKeyWords(transKeyWords);
		}
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
	 * 添加已上传好的媒资任务--并启动流程<br/>
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
			String uploadTempPath,
			String tags,
			Long ... folderId
			) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_AUDIO);
		
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
		entity.setDownloadCount(0l);
		
		if(needProcess){
			//启动流程
			startUploadProcess(entity);
			System.out.println(needProcess);
		}else{
			mediaAudioDao.save(entity);
		}
		
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
	public List<MediaAudioPO> addList(UserVO user, List<String> urlList, Long folderId, String tags) throws Exception{
		
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
					
					MediaAudioPO audio = this.add(user, localFile.getParentFile().getName(), fileName, size, folderType, mimeType,uploadTempPath, tags, folderId);
					audios.add(audio);
				}
			}	
		}
		return audios;
	}
	
	/**
	 * 启动上传审核流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月9日 上午10:54:37
	 * @param MediaAudioPO audio 音频媒资
	 */
	public void startUploadProcess(MediaAudioPO audio) throws Exception{
		mediaAudioDao.save(audio);
		UserVO user = userQuery.current();
		Long companyId = Long.valueOf(user.getGroupId());
		MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_AUDIO);
		Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
		ProcessVO process = processQuery.findById(processId);
		JSONObject variables = new JSONObject();
		variables.put("name", audio.getName());
		variables.put("tags", audio.getTags());
		variables.put("keyWords", audio.getKeyWords());
		variables.put("media", serverPropsQuery.generateHttpPreviewUrl(audio.getPreviewUrl()));
		variables.put("remark", audio.getRemarks());
		variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(audio.getFolderId()));
		variables.put("_pa8_id", audio.getId());
		String category = new StringBufferWrapper().append("上传音频：").append(audio.getName()).toString();
		String business = new StringBufferWrapper().append("mediaAudio:").append(audio.getId()).toString();
		String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
		audio.setProcessInstanceId(processInstanceId);
		mediaAudioDao.save(audio);
	}
	
	/**
	 * 增加下载数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月15日 下午5:11:49
	 * @param Long id 下载的音频id
	 * @return MediaAudioVO 音频
	 */
	public MediaAudioVO downloadAdd(UserVO user, Long id) throws Exception {
		MediaAudioPO media = mediaAudioDao.findOne(id);
		if(media == null) throw new MediaAudioNotExistException(id);
		
		Long downloadCount = media.getDownloadCount();
		if (media.getDownloadCount() == null) {
			downloadCount = 1l;
		} else {
			downloadCount++;
		}
		media.setDownloadCount(downloadCount);
		mediaAudioDao.save(media);
		
		MediaAudioVO audio = new MediaAudioVO().set(media);
		List<String> tagNames = audio.getTags();
		if (tagNames != null && !tagNames.isEmpty()) {
			List<TagVO> allTag = tagQuery.queryFromNameAndGroupId(user.getGroupId(), tagNames);
			List<TagDownloadPermissionPO> savePO = new ArrayList<TagDownloadPermissionPO>();
			List<String> userTag = user.getTags();
			List<String> userAddTag = new ArrayList<String>();
			for (TagVO tagVO : allTag) {
				TagDownloadPermissionPO permission = tagDownloadPermissionDAO.findByUserIdAndTagId(user.getId(), tagVO.getId());
				if (permission == null) {
					permission = new TagDownloadPermissionPO();
					permission.setUpdateTime(new Date());
					permission.setTagId(tagVO.getId());
					permission.setType(FolderType.COMPANY_AUDIO.getPrimaryKey());
					permission.setUserId(user.getId());
					permission.setDownloadCount(0l);
				}
				Long tagDownloadCount = permission.getDownloadCount() + 1;
				if (tagDownloadCount/10 > 0 && tagDownloadCount % 10 == 0 && !userTag.contains(tagVO.getName())) {
					userAddTag.add(tagVO.getName());
				}
				permission.setDownloadCount(tagDownloadCount);
				savePO.add(permission);
			}
			tagDownloadPermissionDAO.save(savePO);
			
			if (!userAddTag.isEmpty()) {
				userTag.addAll(userAddTag);
				String newUserTag = StringUtils.join(userTag.toArray(), ",");
				userQuery.edit(id, newUserTag);
			}
		}
		
		return audio.set(media);
	}
}
