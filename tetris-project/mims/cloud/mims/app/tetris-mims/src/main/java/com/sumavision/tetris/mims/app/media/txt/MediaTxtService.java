package com.sumavision.tetris.mims.app.media.txt;

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
import com.sumavision.tetris.commons.util.file.FileUtil;
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
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.media.stream.video.exception.MediaVideoStreamNotExistException;
import com.sumavision.tetris.mims.app.storage.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.storage.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.storage.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
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
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 文本媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaTxtPO media = mediaTxtDao.findOne(id);
		if(media.getUploadTmpPath() != null){
			File file = FileUtil.writeString(media.getUploadTmpPath(), media.getContent());
			media.setSize(file.length());
			media.setLastModified(file.lastModified());
		}
		media.setReviewStatus(null);
		mediaTxtDao.save(media);
	}
	
	/**
	 * 文本媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaTxtPO media = mediaTxtDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaTxtDao.save(media);
	}
	
	/**
	 * 文本媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 文本媒资id
	 * @param String name 媒资名称
	 * @param String tags 贴标签，以“,”分隔
	 * @param String keyWords 关键字，以“,”分隔
	 * @param String remarks 备注
	 * @param String content 文本内容
	 */
	public void editReviewPassed(
			Long id,
			String name, 
			String tags, 
			String keyWords, 
			String remark, 
			String content) throws Exception{
		MediaTxtPO media = mediaTxtDao.findOne(id);
		if(media == null){
			throw new MediaVideoStreamNotExistException(id);
		}
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remark);
		media.setContent(content);
		media.setReviewStatus(null);
		
		if(media.getUploadTmpPath() != null){
			FileUtil.writeString(media.getUploadTmpPath(), media.getContent());
			media.setSize(Long.valueOf(media.getContent().getBytes().length));
			media.setLastModified(new Date().getTime());
		}
		
		mediaTxtDao.save(media);
	}
	
	/**
	 * 文本媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 文本媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaTxtPO media = mediaTxtDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaTxtDao.save(media);
	}
	
	/**
	 * 文本媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 文本媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaTxtPO media = mediaTxtDao.findOne(id);
		if(media != null){
			mediaTxtDao.delete(media);
		}
	}
	
	/**
	 * 文本媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 文本媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaTxtPO media = mediaTxtDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaTxtDao.save(media);
	}
	
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
	public Map<String, Object> remove(Collection<MediaTxtPO> txtStreams) throws Exception{
		UserVO user = userQuery.current();
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_TXT);
		List<MediaTxtPO> txtCanBeDeleted = new ArrayList<MediaTxtPO>();
		List<MediaTxtPO> txtNeedProcess = new ArrayList<MediaTxtPO>();
		
		if(needProcess){
			for(MediaTxtPO txt:txtStreams){
				if(txt.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(txt.getReviewStatus())){
					txtCanBeDeleted.add(txt);
				}else{
					txtNeedProcess.add(txt);
				}
			}
			if(txtNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_TXT);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaTxtPO txt:txtNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", txt.getName());
					variables.put("tags", txt.getTags());
					variables.put("keyWords", txt.getKeyWords());
					variables.put("remark", txt.getRemarks());
					variables.put("media", txt.getContent());
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(txt.getFolderId()));
					
					//接口参数
					variables.put("_pa40_id", txt.getId());
					
					String category = new StringBufferWrapper().append("删除文本：").append(txt.getName()).toString();
					String business = new StringBufferWrapper().append("mediaTxt:").append(txt.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					txt.setProcessInstanceId(processInstanceId);
					txt.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaTxtDao.save(txtNeedProcess);
			}
		}else{
			txtCanBeDeleted.addAll(txtStreams);
		}
		
		if(txtCanBeDeleted.size() > 0){
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaTxts(txtCanBeDeleted);
			
			//删除素材文件元数据
			mediaTxtDao.deleteInBatch(txtCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> txtIds = new HashSet<Long>();
			for(MediaTxtPO picture:txtCanBeDeleted){
				txtIds.add(picture.getId());
			}
			
			//删除临时文件
			for(MediaTxtPO txt:txtCanBeDeleted){
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
		
		return new HashMapWrapper<String, Object>().put("deleted", MediaTxtVO.getConverter(MediaTxtVO.class).convert(txtCanBeDeleted, MediaTxtVO.class))
												   .put("processed", MediaTxtVO.getConverter(MediaTxtVO.class).convert(txtNeedProcess, MediaTxtVO.class))
												   .getMap();
	}
	
	/**
	 * 添加文本媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param MediaTxtTaskVO task 上传任务
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param FolderPO folder 文件夹
	 * @return MediaTxtVO 文本媒资
	 */
	public MediaTxtVO addTask(
			UserVO user,
			MediaTxtTaskVO task,
			String name,
			List<String> tags, 
			List<String> keyWords,
			String remark, 
			FolderPO folder) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_TXT);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaTxtPO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaTxtPO.SEPARATOR_KEYWORDS);
		
		Date date = new Date();
		
		String fileName = task.getName();
		String separator = File.separator;
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath)
												   .append("upload").append(separator)
												   .append("tmp").append(separator)
												   .append(user.getGroupName()).append(separator)
												   .append(folder.getUuid()).toString();
		String version = new StringBufferWrapper().append(MediaTxtPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String folderPath = new StringBufferWrapper().append(basePath).append(separator)
													 .append(name).append(separator)
													 .append(version).toString();
		File file = new File(folderPath);
		if (!file.exists()) file.mkdirs();
		Thread.sleep(1);
		String storePath = new StringBufferWrapper().append(folderPath).append(separator).append(fileName).toString();
		String previewUrl = new StringBufferWrapper().append("upload/tmp/")
													 .append(user.getGroupName()).append("/")
													 .append(folder.getUuid()).append("/")
													 .append(name).append("/")
													 .append(version).append("/")
													 .append(fileName).toString();
		
		MediaTxtPO entity = new MediaTxtPO();
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.UPLOADING);
		entity.setPreviewUrl(previewUrl);
		entity.setUploadTmpPath(storePath);
		entity.setSize(task.getSize());
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		mediaTxtDao.save(entity);
		
		return new MediaTxtVO().set(entity);
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
	 * @param boolean saveFile 是否保存文件
	 * @return MediaTxtVO 文本媒资
	 */
	public MediaTxtPO addTask(
			UserVO user,
			String name,
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String content,
			FolderPO folder,
			boolean saveFile,
			String addition) throws Exception{
		MediaTxtPO mediaTxtPO = addTask(user, name, tags, keyWords, remark, content, folder, saveFile);
		if (addition != null) {
			mediaTxtPO.setAddition(addition);
			mediaTxtDao.save(mediaTxtPO);
		}
		return mediaTxtPO;
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
	 * @param boolean saveFile 是否保存文件
	 * @return MediaTxtPO 文本媒资
	 */
	public MediaTxtPO addTask(
			UserVO user,
			String name,
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String content,
			FolderPO folder,
			boolean saveFile) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_TXT);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaTxtPO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaTxtPO.SEPARATOR_KEYWORDS);
		
		MediaTxtPO entity = new MediaTxtPO();
		Date date = new Date();
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setContent(content);
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		
		if(saveFile){
			String fileName = new StringBufferWrapper().append(name).append(".txt").toString();
			String separator = File.separator;
			String webappPath = path.webappPath();
			String basePath = new StringBufferWrapper().append(webappPath)
													   .append("upload").append(separator)
													   .append("tmp").append(separator)
													   .append(user.getGroupName()).append(separator)
													   .append(folder.getUuid()).toString();
			String version = new StringBufferWrapper().append(MediaTxtPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
			String folderPath = new StringBufferWrapper().append(basePath).append(separator)
														 .append(name).append(separator)
														 .append(version).toString();
			File file = new File(folderPath);
			if (!file.exists()) file.mkdirs();
			Thread.sleep(1);
			String storePath = new StringBufferWrapper().append(folderPath).append(separator).append(fileName).toString();
			String previewUrl = new StringBufferWrapper().append("upload/tmp/")
														 .append(user.getGroupName()).append("/")
														 .append(folder.getUuid()).append("/")
														 .append(name).append("/")
														 .append(version).append("/")
														 .append(fileName).toString();
			entity.setUploadTmpPath(storePath);
			entity.setPreviewUrl(previewUrl);
			entity.setFileName(fileName);
		}
		mediaTxtDao.save(entity);
		
		//开启审核流程
		if(needProcess){
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_TXT);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			variables.put("name", entity.getName());
			variables.put("tags", entity.getTags());
			variables.put("keyWords", entity.getKeyWords());
			variables.put("media", content);
			variables.put("remark", entity.getRemarks());
			variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(entity.getFolderId()));
			variables.put("_pa36_id", entity.getId());
			String category = new StringBufferWrapper().append("添加文本媒资：").append(entity.getName()).toString();
			String business = new StringBufferWrapper().append("mediaTxt:").append(entity.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			entity.setProcessInstanceId(processInstanceId);
			mediaTxtDao.save(entity);
		}
		
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
	 * @param changeContent 是否修改内容
	 * @return MediaTxtPO 文本媒资
	 */
	public MediaTxtPO editTask(
			UserVO user,
			MediaTxtPO txt,
			String name,
			List<String> tags, 
			List<String> keyWords, 
			String remark,
			String content,
			boolean changeContent) throws Exception{

		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_TXT);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		content = content==null?"":content;
		
		if(needProcess){
			
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_TXT);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			if(changeContent){
				variables.put("content", content);
			}else{
				variables.put("content", txt.getContent());
			}
			
			//展示修改前参数
			variables.put("oldName", txt.getName());
			variables.put("oldTags", txt.getTags());
			variables.put("oldKeyWords", txt.getKeyWords());
			variables.put("oldRemark", txt.getRemarks());
			variables.put("oldContent", txt.getContent());
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(txt.getFolderId()));
			
			//接口参数
			variables.put("_pa38_id", txt.getId());
			variables.put("_pa38_name", name);
			variables.put("_pa38_tags", transTags);
			variables.put("_pa38_keyWords", transKeyWords);
			variables.put("_pa38_remark", remark);
			variables.put("_pa38_content", content);
			
			String category = new StringBufferWrapper().append("修改文本：").append(txt.getName()).toString();
			String business = new StringBufferWrapper().append("mediaTxt:").append(txt.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			txt.setProcessInstanceId(processInstanceId);
			txt.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
			mediaTxtDao.save(txt);
		}else{
			txt.setName(name);
			txt.setTags(transTags);
			txt.setKeyWords(transKeyWords);
			txt.setRemarks(remark);
			if(changeContent){
				txt.setContent(content);
				if(txt.getUploadTmpPath() != null && !txt.getUploadTmpPath().isEmpty()){
					File file = FileUtil.writeString(txt.getUploadTmpPath(), txt.getContent());
					txt.setSize(file.length());
					txt.setLastModified(file.lastModified());
				}
			}
			mediaTxtDao.save(txt);
		}
		
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
		File file = FileUtil.writeString(fileUrl, media.getContent());
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
	
}
