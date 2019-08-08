package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
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
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	/**
	 * 视频流媒资删除<br/>
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
	 * @param Collection<MediaVideoStreamPO> videos 视频流媒资列表
	 */
	public void remove(Collection<MediaVideoStreamPO> videoStreams) throws Exception{
		
		//删除视频流元数据
		mediaVideoStreamDao.deleteInBatch(videoStreams);
		
		List<Long> ids = new ArrayList<Long>();
		for(MediaVideoStreamPO mediaStream:videoStreams){
			ids.add(mediaStream.getId());
		}
		mediaVideoStreamUrlRelationService.remove(ids);
	}
	
	/**
	 * 添加视频流媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String previewUrl 视频流地址
	 * @param FolderPO folder 文件夹
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	public MediaVideoStreamPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String previewUrl,
			FolderPO folder) throws Exception{
		
		Date date = new Date();
		
		MediaVideoStreamPO entity = new MediaVideoStreamPO();
		entity.setName(name);
		entity.setTags("");
		entity.setKeyWords("");
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setPreviewUrl(previewUrl);
		entity.setUpdateTime(date);
		mediaVideoStreamDao.save(entity);
		
		return entity;
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
		
		Date date = new Date();
		
		MediaVideoStreamPO entity = new MediaVideoStreamPO();
		entity.setName(name);
		entity.setTags("");
		entity.setKeyWords("");
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.COMPLETE);
		entity.setUpdateTime(date);
		mediaVideoStreamDao.save(entity);
		
		mediaVideoStreamUrlRelationService.add(previewUrl, entity.getId());
		
		return new MediaVideoStreamVO().set(entity).setPreviewUrl(previewUrl);
	}
	
	/**
	 * 编辑视频流媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月26日 上午8:57:13
	 * @param user 用户
	 * @param videoStream 视频流媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @param previewUrl 视频流地址
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	public MediaVideoStreamPO editTask(
			UserVO user, 
			MediaVideoStreamPO videoStream,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			String previewUrl) throws Exception{
		
		videoStream.setName(name);
		videoStream.setRemarks(remark);
		videoStream.setPreviewUrl(previewUrl);
		mediaVideoStreamDao.save(videoStream);
		
		return videoStream;
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
			MediaVideoStreamPO videoStream,
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark, 
			List<String> previewUrl) throws Exception{
		
		videoStream.setName(name);
		videoStream.setRemarks(remark);
		mediaVideoStreamDao.save(videoStream);
		
		mediaVideoStreamUrlRelationService.update(previewUrl, videoStream.getId());
		
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
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param String previewUrl 视频流地址
	 * @param FolderPO folder 文件夹
	 * @return MediaVideoStreamPO 视频流媒资
	 */
	public MediaVideoStreamVO addVideoStreamTask(
			String previewUrl, 
			String name) throws Exception{
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_VIDEO_STREAM.toString());
		
		return  addTask(user, name, null, null, "", new ArrayListWrapper<String>().add(previewUrl).getList(), folder);
	}
	
	/**
	 * 视频流媒资删除(为feign提供)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月17日 下午3:43:03
	 * @param MediaVideoStreamPO videos 视频流媒资列表
	 */
	public void remove(String mediaIds) throws Exception{
		
		List<Long> mediaIdList = JSON.parseArray(mediaIds, Long.class);
		
		List<MediaVideoStreamPO> medias = mediaVideoStreamDao.findAll(mediaIdList);
		
		if (medias == null || medias.isEmpty()) return; 
		
		remove(medias);
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
		
		MediaVideoStreamPO videoStream = mediaVideoStreamDao.findOne(mediaId);
		
		return editTask(user, videoStream, name, null, null, "", new ArrayListWrapper<String>().add(previewUrl).getList());
	}
}
