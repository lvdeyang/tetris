package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
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
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.media.stream.video.exception.MediaVideoStreamNotExistException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 视频流流媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaVideoStreamService {

	@Autowired
	private MediaVideoStreamDAO mediaVideoStreamDao;
	
	@Autowired
	private MediaVideoStreamUrlRelationService mediaVideoStreamUrlRelationService;
	
	@Autowired
	private MediaVideoStreamUrlRelationQuery mediaVideoStreamUrlRelationQuery;
	
	@Autowired
	private MediaVideoStreamUrlRelationDAO mediaVideoStreamUrlRelationDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private ProcessService processService;
	
	/**
	 * 视频流媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaVideoStreamPO media = mediaVideoStreamDao.findOne(id);
		media.setReviewStatus(null);
		mediaVideoStreamDao.save(media);
	}
	
	/**
	 * 视频流媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaVideoStreamPO media = mediaVideoStreamDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaVideoStreamDao.save(media);
	}
	
	/**
	 * 视频流媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 视频流媒资id
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
			String remark, 
			List<String> previewUrl) throws Exception{
		MediaVideoStreamPO media = mediaVideoStreamDao.findOne(id);
		if(media == null){
			throw new MediaVideoStreamNotExistException(id);
		}
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remark);
		media.setReviewStatus(null);
		mediaVideoStreamDao.save(media);
		mediaVideoStreamUrlRelationService.update(previewUrl, media.getId());
	}
	
	/**
	 * 视频流媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 视频流媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaVideoStreamPO media = mediaVideoStreamDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaVideoStreamDao.save(media);
	}
	
	/**
	 * 视频流媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 视频流媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaVideoStreamPO media = mediaVideoStreamDao.findOne(id);
		if(media != null){
			mediaVideoStreamDao.delete(media);
		}
		mediaVideoStreamUrlRelationDao.removeByStreamId(id);
	}
	
	/**
	 * 视频流媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 视频流媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaVideoStreamPO media = mediaVideoStreamDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaVideoStreamDao.save(media);
	}
	
	/**
	 * 视频流媒资删除(为feign提供)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param Collection<Long> ids 视频流id列表
	 * @return List<MediaVideoStreamVO> 要删除的视频流媒资
	 */
	public List<MediaVideoStreamVO> removeByIds(Collection<Long> ids) throws Exception{
		List<MediaVideoStreamPO> medias = mediaVideoStreamDao.findAll(ids);
		for (MediaVideoStreamPO media : medias) {
			media.setPreviewUrl(mediaVideoStreamUrlRelationQuery.getUrlFromStreamId(media.getId()));
		}
		if (medias == null || medias.isEmpty()) return null; 
		remove(medias);
		return MediaVideoStreamVO.getConverter(MediaVideoStreamVO.class).convert(medias, MediaVideoStreamVO.class);
	}
	
	/**
	 * 视频流媒资删除<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:43:03
	 * @param Collection<MediaVideoStreamPO> videoStreams 视频流媒资列表
	 * @return deleted List<MediaVideoStreamVO> 删除列表
	 * @return processed List<MediaVideoStreamVO> 待审核列表
	 */
	public Map<String, Object> remove(Collection<MediaVideoStreamPO> videoStreams) throws Exception{
		
		UserVO user = userQuery.current();
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_VIDEO_STREAM);
		
		List<MediaVideoStreamPO> videoStreamsCanBeDeleted = new ArrayList<MediaVideoStreamPO>();
		List<MediaVideoStreamPO> videoStreamsNeedProcess = new ArrayList<MediaVideoStreamPO>();
		
		if(needProcess){
			for(MediaVideoStreamPO videoStream:videoStreams){
				if(videoStream.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(videoStream.getReviewStatus())){
					videoStreamsCanBeDeleted.add(videoStream);
				}else{
					videoStreamsNeedProcess.add(videoStream);
				}
			}
			
			if(videoStreamsNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_VIDEO_STREAM);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaVideoStreamPO videoStream:videoStreamsNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", videoStream.getName());
					variables.put("tags", videoStream.getTags());
					variables.put("keyWords", videoStream.getKeyWords());
					variables.put("remark", videoStream.getRemarks());
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(videoStream.getFolderId()));
					
					//接口参数
					variables.put("_pa29_id", videoStream.getId());
					
					String category = new StringBufferWrapper().append("删除视频流：").append(videoStream.getName()).toString();
					String business = new StringBufferWrapper().append("mediaVideoStream:").append(videoStream.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					videoStream.setProcessInstanceId(processInstanceId);
					videoStream.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaVideoStreamDao.save(videoStreamsNeedProcess);
			}
			
		}else{
			videoStreamsCanBeDeleted.addAll(videoStreams);
		}
		
		if(videoStreamsCanBeDeleted.size() > 0){
			mediaVideoStreamDao.deleteInBatch(videoStreamsCanBeDeleted);
			List<Long> ids = new ArrayList<Long>();
			for(MediaVideoStreamPO mediaStream:videoStreamsCanBeDeleted){
				ids.add(mediaStream.getId());
			}
			mediaVideoStreamUrlRelationDao.removeByStreamIdIn(ids);
		}
		return new HashMapWrapper<String, Object>().put("deleted", MediaVideoStreamVO.getConverter(MediaVideoStreamVO.class).convert(videoStreamsCanBeDeleted, MediaVideoStreamVO.class))
												   .put("processed", MediaVideoStreamVO.getConverter(MediaVideoStreamVO.class).convert(videoStreamsNeedProcess, MediaVideoStreamVO.class))
												   .getMap();		
	}
	
	/**
	 * 添加视频流媒资上传任务(多url)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param List<String> previewUrl 视频流地址
	 * @param FolderPO folder 文件夹
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	public MediaVideoStreamVO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			List<String> previewUrl,
			FolderPO folder) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_VIDEO_STREAM);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		
		Date date = new Date();
		MediaVideoStreamPO entity = new MediaVideoStreamPO();
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		mediaVideoStreamDao.save(entity);
		mediaVideoStreamUrlRelationService.add(previewUrl, entity.getId());
		
		//开启审核流程
		if(needProcess){
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_VIDEO_STREAM);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			variables.put("name", entity.getName());
			variables.put("tags", entity.getTags());
			variables.put("keyWords", entity.getKeyWords());
			variables.put("media", previewUrl.get(0));
			variables.put("remark", entity.getRemarks());
			variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(entity.getFolderId()));
			variables.put("_pa24_id", entity.getId());
			String category = new StringBufferWrapper().append("添加视频流媒资：").append(entity.getName()).toString();
			String business = new StringBufferWrapper().append("mediaVideoStream:").append(entity.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			entity.setProcessInstanceId(processInstanceId);
			mediaVideoStreamDao.save(entity);
		}
		
		return new MediaVideoStreamVO().set(entity).setPreviewUrl(previewUrl);
	}
	
	/**
	 * 编辑视频流媒资(多url)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午8:57:13
	 * @param user 用户
	 * @param videoStream 视频流媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @param previewUrl 视频流地址
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	public MediaVideoStreamVO editTask(
			UserVO user, 
			Long id,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			List<String> previewUrl) throws Exception{
		
		MediaVideoStreamPO videoStream = mediaVideoStreamDao.findOne(id);
		if(videoStream == null){
			throw new MediaVideoStreamNotExistException(id);
		}
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_VIDEO_STREAM);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_VIDEO_STREAM);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", videoStream.getName());
			variables.put("oldTags", videoStream.getTags());
			variables.put("oldKeyWords", videoStream.getKeyWords());
			variables.put("oldRemark", videoStream.getRemarks());
			variables.put("oldMedia", previewUrl==null?"":previewUrl.get(0));
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(videoStream.getFolderId()));
			
			//接口参数
			variables.put("_pa26_id", videoStream.getId());
			variables.put("_pa26_name", name);
			variables.put("_pa26_tags", transTags);
			variables.put("_pa26_keyWords", transKeyWords);
			variables.put("_pa26_remarks", remark);
			if(previewUrl!=null && previewUrl.size()>0){
				variables.put("_pa26_previewUrls", JSON.toJSONString(previewUrl));
			}
			
			String category = new StringBufferWrapper().append("修改视频流：").append(videoStream.getName()).toString();
			String business = new StringBufferWrapper().append("mediaVideoStream:").append(videoStream.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			videoStream.setProcessInstanceId(processInstanceId);
			videoStream.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
			mediaVideoStreamDao.save(videoStream);
		}else{
			videoStream.setName(name);
			videoStream.setTags(transTags);
			videoStream.setKeyWords(transKeyWords);
			videoStream.setRemarks(remark);
			mediaVideoStreamDao.save(videoStream);
			mediaVideoStreamUrlRelationService.update(previewUrl, videoStream.getId());
		}
		return new MediaVideoStreamVO().set(videoStream).setPreviewUrl(previewUrl);
	}
	
	/**
	 * 复制视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaAudioStreamPO media 待复制的视频流媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaVideoStreamPO 复制后的视频流媒资
	 */
	public MediaVideoStreamPO copy(MediaVideoStreamPO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaVideoStreamPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaVideoStreamDao.save(copiedMedia);
		
		return copiedMedia;
	}
	
	/**
	 * 复制视频流媒资(多url)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 下午1:21:15
	 * @param MediaAudioStreamPO media 待复制的视频流媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaVideoStreamPO 复制后的视频流媒资
	 */
	public MediaVideoStreamVO copyByCountlessUrl(MediaVideoStreamPO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaVideoStreamPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaVideoStreamDao.save(copiedMedia);
		
		List<String> mediaUrls = mediaVideoStreamUrlRelationService.copy(media.getId(), copiedMedia.getId());
		
		return new MediaVideoStreamVO().set(copiedMedia).setPreviewUrl(mediaUrls);
	}
	
	/**
	 * 添加视频流媒资上传任务(为feign提供)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param String name 媒资名称
	 * @param String previewUrl 视频流地址
	 * @return MediaVideoStreamVO 视频流媒资
	 */
	public MediaVideoStreamVO addVideoStreamTask(
			String previewUrl, 
			String name) throws Exception{
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_VIDEO_STREAM.toString());
		
		return  addTask(user, name, null, null, "", new ArrayListWrapper<String>().add(previewUrl).getList(), folder);
	}
	
	/** 
	 * 修改媒资
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param Long mediaId 媒资名称
	 * @param String previewUrl 媒资地址
	 * @param String name 媒资名称
	 * @return MediaVideoStreamVO
	 */
	public MediaVideoStreamVO edit(Long mediaId, String previewUrl, String name) throws Exception{
		UserVO user = userQuery.current();
		return editTask(user, mediaId, name, null, null, "", new ArrayListWrapper<String>().add(previewUrl).getList());
	}
}
