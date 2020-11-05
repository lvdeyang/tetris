package com.sumavision.tetris.mims.app.media.live;

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
import com.sumavision.tetris.mims.app.media.live.exception.MediaPushLiveNotExistException;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaPushLiveService {

	@Autowired
	private MediaPushLiveDAO mediaPushLiveDAO;
	
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
	 * push直播媒资上传审核通过<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		media.setReviewStatus(null);
		mediaPushLiveDAO.save(media);
	}
	
	/**
	 * push直播媒资上传审核拒绝<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaPushLiveDAO.save(media);
	}
	
	/**
	 * push直播媒资修改审核通过<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id push直播媒资id
	 * @param String name 媒资名称
	 * @param String tags 贴标签，以“,”分隔
	 * @param String keyWords 关键字，以“,”分隔
	 * @param String remarks 备注
	 * @param String freq push直播频点
	 * @param String audioPid 音频pid
	 * @param String videoPid 视频pid
	 * @param String audioType 音频类型
	 * @param String videoType 视频类型
	 */
	public void editReviewPassed(
			Long id,
			String name, 
			String tags, 
			String keyWords, 
			String remark, 
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType) throws Exception{
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		if(media == null){
			throw new MediaPushLiveNotExistException(id);
		}
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remark);
		media.setReviewStatus(null);
		media.setFreq(freq);
		media.setAudioPid(audioPid);
		media.setVideoPid(videoPid);
		media.setAudioType(audioType);
		media.setVideoType(videoType);
		mediaPushLiveDAO.save(media);
	}
	
	/**
	 * push直播媒资修改审核拒绝<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id push直播媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaPushLiveDAO.save(media);
	}
	
	/**
	 * push直播媒资删除审核通过<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id push直播媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		if(media != null){
			mediaPushLiveDAO.delete(media);
		}
	}
	
	/**
	 * push直播媒资删除审核拒绝<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id push直播媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaPushLivePO media = mediaPushLiveDAO.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaPushLiveDAO.save(media);
	}
	
	/**
	 * push直播媒资删除<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param Collection<Long> ids push直播id列表
	 * @return List<MediaPushLiveVO> 要删除的push直播媒资
	 */
	public List<MediaPushLiveVO> removeByIds(Collection<Long> ids) throws Exception{
		List<MediaPushLivePO> medias = mediaPushLiveDAO.findAll(ids);
		if (medias == null || medias.isEmpty()) return null; 
		remove(medias);
		return MediaPushLiveVO.getConverter(MediaPushLiveVO.class).convert(medias, MediaPushLiveVO.class);
	}
	
	/**
	 * push直播媒资删除<br/>
	 * <p>
	 * 	初步设想，考虑到文件夹下可能包含大文件以及文件数量等<br/>
	 * 	情况，这里采用线程删除文件，主要步骤如下：<br/>
	 * 	  1.生成待删除存储文件数据<br/>
	 *    2.删除素材文件元数据<br/>
	 *    3.保存待删除存储文件数据<br/>
	 *    4.调用flush使sql生效<br/>
	 *    5.将待删除存储文件数据押入存储文件删除队列<br/>
	 * </p>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:43:03
	 * @param Collection<MediaPushLivePO> pushLives push直播媒资列表
	 */
	public Map<String, Object> remove(Collection<MediaPushLivePO> pushLives) throws Exception{
		
		UserVO user = userQuery.current();
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_PUSH_LIVE);
		
		List<MediaPushLivePO> pushLiveCanBeDeleted = new ArrayList<MediaPushLivePO>();
		List<MediaPushLivePO> pushLiveNeedProcess = new ArrayList<MediaPushLivePO>();
		
		if(needProcess){
			for(MediaPushLivePO pushLive:pushLives){
				if(pushLive.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(pushLive.getReviewStatus())){
					pushLiveCanBeDeleted.add(pushLive);
				}else{
					pushLiveNeedProcess.add(pushLive);
				}
			}
			
			if(pushLiveNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_PUSH_LIVE);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaPushLivePO pushLive:pushLiveNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", pushLive.getName());
					variables.put("tags", pushLive.getTags());
					variables.put("keyWords", pushLive.getKeyWords());
					variables.put("remark", pushLive.getRemarks());
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(pushLive.getFolderId()));
					
					//接口参数
					variables.put("_pa34_id", pushLive.getId());
					
					String category = new StringBufferWrapper().append("删除push直播：").append(pushLive.getName()).toString();
					String business = new StringBufferWrapper().append("mediaPushLive:").append(pushLive.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					pushLive.setProcessInstanceId(processInstanceId);
					pushLive.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaPushLiveDAO.save(pushLiveNeedProcess);
			}
			
		}else{
			pushLiveCanBeDeleted.addAll(pushLives);
		}
		
		if(pushLiveCanBeDeleted.size() > 0){
			mediaPushLiveDAO.deleteInBatch(pushLiveCanBeDeleted);
		}
		return new HashMapWrapper<String, Object>().put("deleted", MediaPushLiveVO.getConverter(MediaPushLiveVO.class).convert(pushLiveCanBeDeleted, MediaPushLiveVO.class))
												   .put("processed", MediaPushLiveVO.getConverter(MediaPushLiveVO.class).convert(pushLiveNeedProcess, MediaPushLiveVO.class))
												   .getMap();		
	}
	
	/**
	 * 添加push直播媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String freq push直播频点
	 * @param String audioPid 音频pid
	 * @param String videoPid 视频pid
	 * @param String audioType 音频类型
	 * @param String videoType 视频类型
	 * @param FolderPO folder 文件夹
	 * @return MediaPushLivePO push直播媒资
	 */
	public MediaPushLivePO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType,
			FolderPO folder,
			String addition) throws Exception{
		MediaPushLivePO MediaPushLivePO = addTask(user, name, tags, keyWords, remark, freq, audioPid, videoPid, audioType, videoType, folder);
		if (addition != null) {
			mediaPushLiveDAO.save(MediaPushLivePO);
		}
		return MediaPushLivePO;
	}
	
	/**
	 * 添加push直播媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String freq push直播频点
	 * @param String audioPid 音频pid
	 * @param String videoPid 视频pid
	 * @param String audioType 音频类型
	 * @param String videoType 视频类型
	 * @param FolderPO folder 文件夹
	 * @return MediaPushLivePO push直播媒资
	 */
	public MediaPushLivePO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType,
			FolderPO folder) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_PUSH_LIVE);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaPushLivePO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaPushLivePO.SEPARATOR_KEYWORDS);
		
		Date date = new Date();
		MediaPushLivePO entity = new MediaPushLivePO();
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
		entity.setRemarks(remark);
		entity.setFreq(freq);
		entity.setAudioPid(audioPid);
		entity.setVideoPid(videoPid);
		entity.setAudioType(audioType);
		entity.setVideoType(videoType);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		mediaPushLiveDAO.save(entity);
		
		//开启审核流程
		if(needProcess){
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_PUSH_LIVE);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			variables.put("name", entity.getName());
			variables.put("tags", entity.getTags());
			variables.put("keyWords", entity.getKeyWords());
			variables.put("remark", entity.getRemarks());
			variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(entity.getFolderId()));
			variables.put("_pa30_id", entity.getId());
			String category = new StringBufferWrapper().append("添加push直播媒资：").append(entity.getName()).toString();
			String business = new StringBufferWrapper().append("mediaPushLive:").append(entity.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			entity.setProcessInstanceId(processInstanceId);
			mediaPushLiveDAO.save(entity);
		}
		
		return entity;
	}
	
	/**
	 * 编辑push直播媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 上午8:42:12
	 * @param user 用户
	 * @param pushLive push直播媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @param String freq push直播频点
	 * @param String audioPid 音频pid
	 * @param String videoPid 视频pid
	 * @param String audioType 音频类型
	 * @param String videoType 视频类型
	 * @return MediaPushLivePO push直播媒资
	 */
	public MediaPushLivePO editTask(
			UserVO user, 
			MediaPushLivePO pushLive,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark,
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType) throws Exception{
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_PUSH_LIVE);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaPushLivePO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaPushLivePO.SEPARATOR_KEYWORDS);
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_PUSH_LIVE);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", pushLive.getName());
			variables.put("oldTags", pushLive.getTags());
			variables.put("oldKeyWords", pushLive.getKeyWords());
			variables.put("oldRemark", pushLive.getRemarks());
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(pushLive.getFolderId()));
			
			//接口参数
			variables.put("_pa32_id", pushLive.getId());
			variables.put("_pa32_name", name);
			variables.put("_pa32_tags", transTags);
			variables.put("_pa32_keyWords", transKeyWords);
			variables.put("_pa32_remarks", remark);
			
			String category = new StringBufferWrapper().append("修改push直播：").append(pushLive.getName()).toString();
			String business = new StringBufferWrapper().append("mediaPushLive:").append(pushLive.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			pushLive.setProcessInstanceId(processInstanceId);
			pushLive.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
		}else{
			pushLive.setName(name);
			pushLive.setTags(transTags);
			pushLive.setKeyWords(transKeyWords);
			pushLive.setRemarks(remark);
			pushLive.setFreq(freq);
			pushLive.setAudioPid(audioPid);
			pushLive.setVideoPid(videoPid);
			pushLive.setAudioType(audioType);
			pushLive.setVideoType(videoType);
		}
		mediaPushLiveDAO.save(pushLive);
		
		return pushLive;
	}
	
	/**
	 * 复制push直播媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaPushLivePO media 待复制的push直播媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaPushLivePO 复制后的push直播媒资
	 */
	public MediaPushLivePO copy(MediaPushLivePO media, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaPushLivePO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved?media.getName():new StringBufferWrapper().append(media.getName())
																           .append("（副本：")
																           .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																           .append("）")
																           .toString());
		copiedMedia.setFolderId(target.getId());
		
		mediaPushLiveDAO.save(copiedMedia);
		
		return copiedMedia;
	}
	
	/**
	 * 添加push直播媒资上传任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param String name 媒资名称
	 * @param String freq push直播频点
	 * @param String audioPid 音频pid
	 * @param String videoPid 视频pid
	 * @param String audioType 音频类型
	 * @param String videoType 视频类型
	 * @param String tag 标签名称
	 * @return MediaPushLiveVO push直播媒资
	 */
	public MediaPushLiveVO addPushLiveTask(
			String freq,
			String audioPid,
			String videoPid,
			String audioType,
			String videoType,
			String tag,
			String name) throws Exception{
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDAO.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_PUSH_LIVE.toString());
		
		return new MediaPushLiveVO().set(addTask(user, name, new ArrayListWrapper<String>().add(tag).getList(), null, "", freq, audioPid, videoPid, audioType, videoType, folder));
	}
}
