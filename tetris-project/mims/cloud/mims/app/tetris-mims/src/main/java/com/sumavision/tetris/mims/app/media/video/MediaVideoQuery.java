package com.sumavision.tetris.mims.app.media.video;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.media.UploadStatus;

/**
 * 图片媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaVideoQuery {

	@Autowired
	private MediaVideoDAO mediaVideoDao;
	
	/**
	 * 查询文件夹下上传完成的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 视频媒资
	 */
	public List<MediaVideoPO> findCompleteByFolderId(Long folderId){
		return mediaVideoDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的视频媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 视频媒资
	 */
	public List<MediaVideoPO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaVideoDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的视频媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaVideoPO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaVideoDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资视频（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaVideoPO> pictures 查找范围
	 * @return MediaPicturePO 查找结果
	 */
	public MediaVideoPO loopForUuid(String uuid, Collection<MediaVideoPO> videos){
		if(videos==null || videos.size()<=0) return null;
		for(MediaVideoPO video:videos){
			if(video.getUuid().equals(uuid)){
				return video;
			}
		}
		return null;
	}
	
}
