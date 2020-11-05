package com.sumavision.tetris.mims.app.media.stream.audio;

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
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.exception.MediaVideoStreamNotExistException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 音频流流媒资操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 下午3:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaAudioStreamService {

	@Autowired
	private MediaAudioStreamDAO mediaAudioStreamDao;
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDAO;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 音频流媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaAudioStreamPO media = mediaAudioStreamDao.findOne(id);
		media.setReviewStatus(null);
		mediaAudioStreamDao.save(media);
	}
	
	/**
	 * 音频流媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaAudioStreamPO media = mediaAudioStreamDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaAudioStreamDao.save(media);
	}
	
	/**
	 * 音频流媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 音频流媒资id
	 * @param String name 媒资名称
	 * @param String tags 贴标签，以“,”分隔
	 * @param String keyWords 关键字，以“,”分隔
	 * @param String remarks 备注
	 * @param String previewUrl 流地址
	 */
	public void editReviewPassed(
			Long id,
			String name, 
			String tags, 
			String keyWords, 
			String remark, 
			String previewUrl) throws Exception{
		MediaAudioStreamPO media = mediaAudioStreamDao.findOne(id);
		if(media == null){
			throw new MediaVideoStreamNotExistException(id);
		}
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remark);
		media.setPreviewUrl(previewUrl);
		media.setReviewStatus(null);
		mediaAudioStreamDao.save(media);
	}
	
	/**
	 * 音频流媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 音频流媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaAudioStreamPO media = mediaAudioStreamDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaAudioStreamDao.save(media);
	}
	
	/**
	 * 音频流媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 音频流媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaAudioStreamPO media = mediaAudioStreamDao.findOne(id);
		if(media != null){
			mediaAudioStreamDao.delete(media);
		}
	}
	
	/**
	 * 音频流媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 音频流媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaAudioStreamPO media = mediaAudioStreamDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaAudioStreamDao.save(media);
	}
	
	/**
	 * 音频流媒资删除<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param Collection<Long> ids 音频流id列表
	 * @return List<MediaVideoStreamVO> 要删除的音频流媒资
	 */
	public List<MediaAudioStreamVO> removeByIds(Collection<Long> ids) throws Exception{
		List<MediaAudioStreamPO> medias = mediaAudioStreamDao.findAll(ids);
//		for (MediaAudioStreamPO media : medias) {
//			media.setPreviewUrl(mediaVideoStreamUrlRelationQuery.getUrlFromStreamId(media.getId()));
//		}
		if (medias == null || medias.isEmpty()) return null; 
		remove(medias);
		return MediaVideoStreamVO.getConverter(MediaAudioStreamVO.class).convert(medias, MediaAudioStreamVO.class);
	}
	
	/**
	 * 音频流媒资删除<br/>
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
	 * @param Collection<MediaAudioStreamPO> audioStreams 音频流媒资列表
	 */
	public Map<String, Object> remove(Collection<MediaAudioStreamPO> audioStreams) throws Exception{
		
		UserVO user = userQuery.current();
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_AUDIO_STREAM);
		
		List<MediaAudioStreamPO> audioStreamsCanBeDeleted = new ArrayList<MediaAudioStreamPO>();
		List<MediaAudioStreamPO> audioStreamsNeedProcess = new ArrayList<MediaAudioStreamPO>();
		
		if(needProcess){
			for(MediaAudioStreamPO audioStream:audioStreams){
				if(audioStream.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(audioStream.getReviewStatus())){
					audioStreamsCanBeDeleted.add(audioStream);
				}else{
					audioStreamsNeedProcess.add(audioStream);
				}
			}
			
			if(audioStreamsNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_AUDIO_STREAM);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaAudioStreamPO audioStream:audioStreamsNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", audioStream.getName());
					variables.put("tags", audioStream.getTags());
					variables.put("keyWords", audioStream.getKeyWords());
					variables.put("previewUrl", audioStream.getPreviewUrl());
					variables.put("remark", audioStream.getRemarks());
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(audioStream.getFolderId()));
					
					//接口参数
					variables.put("_pa34_id", audioStream.getId());
					
					String category = new StringBufferWrapper().append("删除音频流：").append(audioStream.getName()).toString();
					String business = new StringBufferWrapper().append("mediaAudioStream:").append(audioStream.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					audioStream.setProcessInstanceId(processInstanceId);
					audioStream.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaAudioStreamDao.save(audioStreamsNeedProcess);
			}
			
		}else{
			audioStreamsCanBeDeleted.addAll(audioStreams);
		}
		
		if(audioStreamsCanBeDeleted.size() > 0){
			mediaAudioStreamDao.deleteInBatch(audioStreamsCanBeDeleted);
		}
		return new HashMapWrapper<String, Object>().put("deleted", MediaAudioStreamVO.getConverter(MediaAudioStreamVO.class).convert(audioStreamsCanBeDeleted, MediaAudioStreamVO.class))
												   .put("processed", MediaAudioStreamVO.getConverter(MediaAudioStreamVO.class).convert(audioStreamsNeedProcess, MediaAudioStreamVO.class))
												   .getMap();		
	}
	
	/**
	 * 添加音频流媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String previewUrl 音频流地址
	 * @param FolderPO folder 文件夹
	 * @return MediaAudioStreamPO 音频流媒资
	 */
	public MediaAudioStreamPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String previewUrl,
			FolderPO folder,
			String streamType,
			String addition) throws Exception{
		MediaAudioStreamPO mediaAudioStreamPO = addTask(user, name, tags, keyWords, remark, previewUrl, folder, streamType);
		if (addition != null) {
			mediaAudioStreamPO.setAddition(addition);
			mediaAudioStreamDao.save(mediaAudioStreamPO);
		}
		return mediaAudioStreamPO;
	}
	
	/**
	 * 添加音频流媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String previewUrl 音频流地址
	 * @param FolderPO folder 文件夹
	 * @return MediaAudioStreamPO 音频流媒资
	 */
	public MediaAudioStreamPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String previewUrl,
			FolderPO folder,
			String streamType) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_AUDIO_STREAM);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		
		Date date = new Date();
		MediaAudioStreamPO entity = new MediaAudioStreamPO();
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setPreviewUrl(previewUrl);
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		entity.setStreamType(streamType);
		mediaAudioStreamDao.save(entity);
		
		//开启审核流程
		if(needProcess){
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_AUDIO_STREAM);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			variables.put("name", entity.getName());
			variables.put("tags", entity.getTags());
			variables.put("keyWords", entity.getKeyWords());
			variables.put("media", entity.getPreviewUrl());
			variables.put("remark", entity.getRemarks());
			variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(entity.getFolderId()));
			variables.put("_pa30_id", entity.getId());
			String category = new StringBufferWrapper().append("添加音频流媒资：").append(entity.getName()).toString();
			String business = new StringBufferWrapper().append("mediaAudioStream:").append(entity.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			entity.setProcessInstanceId(processInstanceId);
			mediaAudioStreamDao.save(entity);
		}
		
		return entity;
	}
	
	/**
	 * 编辑音频流媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 上午8:42:12
	 * @param user 用户
	 * @param audioStream 音频流媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @param previewUrl 流地址
	 * @return MediaAudioStreamPO 音频流媒资
	 */
	public MediaAudioStreamPO editTask(
			UserVO user, 
			MediaAudioStreamPO audioStream,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String previewUrl,
			String streamType) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_AUDIO_STREAM);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_AUDIO_STREAM);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", audioStream.getName());
			variables.put("oldTags", audioStream.getTags());
			variables.put("oldKeyWords", audioStream.getKeyWords());
			variables.put("oldRemark", audioStream.getRemarks());
			variables.put("oldMedia", audioStream.getPreviewUrl());
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(audioStream.getFolderId()));
			
			//接口参数
			variables.put("_pa32_id", audioStream.getId());
			variables.put("_pa32_name", name);
			variables.put("_pa32_tags", transTags);
			variables.put("_pa32_keyWords", transKeyWords);
			variables.put("_pa32_remarks", remark);
			variables.put("_pa32_previewUrl", previewUrl);
			
			String category = new StringBufferWrapper().append("修改音频流：").append(audioStream.getName()).toString();
			String business = new StringBufferWrapper().append("mediaAudioStream:").append(audioStream.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			audioStream.setProcessInstanceId(processInstanceId);
			audioStream.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
		}else{
			audioStream.setName(name);
			audioStream.setTags(transTags);
			audioStream.setKeyWords(transKeyWords);
			audioStream.setRemarks(remark);
			audioStream.setPreviewUrl(previewUrl);
			audioStream.setStreamType(streamType);
		}
		mediaAudioStreamDao.save(audioStream);
		
		return audioStream;
	}
	
	/**
	 * 复制音频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaAudioStreamPO media 待复制的音频流媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaAudioStreamPO 复制后的音频流媒资
	 */
	public MediaAudioStreamPO copy(MediaAudioStreamPO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaAudioStreamPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaAudioStreamDao.save(copiedMedia);
		
		return copiedMedia;
	}
	
	/**
	 * 添加音频流媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param String name 媒资名称
	 * @param String previewUrl 音频流地址
	 * @param String tag 标签名称
	 * @return MediaAudioStreamVO 音频流媒资
	 */
	public MediaAudioStreamVO addAudioStreamTask(
			String previewUrl, 
			String tag,
			String name,
			String addition,
			String streamType) throws Exception{
		
		if (addition == null) return addAudioStreamTask(previewUrl, tag, name, streamType);
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDAO.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_AUDIO_STREAM.toString());
		
		MediaAudioStreamPO mediaAudioStreamPO = addTask(user, name, new ArrayListWrapper<String>().add(tag).getList(), null, "", previewUrl, folder, streamType);
		
		mediaAudioStreamPO.setAddition(addition);
		mediaAudioStreamDao.save(mediaAudioStreamPO);
		
		return new MediaAudioStreamVO().set(mediaAudioStreamPO);
	}
	
	/**
	 * 添加音频流媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param String name 媒资名称
	 * @param String previewUrl 音频流地址
	 * @param String tag 标签名称
	 * @return MediaAudioStreamVO 音频流媒资
	 */
	public MediaAudioStreamVO addAudioStreamTask(
			String previewUrl, 
			String tag,
			String name,
			String streamType) throws Exception{
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDAO.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_AUDIO_STREAM.toString());
		
		return new MediaAudioStreamVO().set(addTask(user, name, new ArrayListWrapper<String>().add(tag).getList(), null, "", previewUrl, folder, streamType));
	}
	
}
