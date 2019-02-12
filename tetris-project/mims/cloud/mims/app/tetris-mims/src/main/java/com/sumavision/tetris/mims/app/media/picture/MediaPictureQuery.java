package com.sumavision.tetris.mims.app.media.picture;

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
public class MediaPictureQuery {

	@Autowired
	private MediaPictureDAO mediaPictureDao;
	
	/**
	 * 查询文件夹下上传完成的图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 图片媒资
	 */
	public List<MediaPicturePO> findCompleteByFolderId(Long folderId){
		return mediaPictureDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的图片媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 图片媒资
	 */
	public List<MediaPicturePO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaPictureDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的图片媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaPicturePO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaPictureDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资图片（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaPicturePO> pictures 查找范围
	 * @return MediaPicturePO 查找结果
	 */
	public MediaPicturePO loopForUuid(String uuid, Collection<MediaPicturePO> pictures){
		if(pictures==null || pictures.size()<=0) return null;
		for(MediaPicturePO picture:pictures){
			if(picture.getUuid().equals(uuid)){
				return picture;
			}
		}
		return null;
	}
	
}
