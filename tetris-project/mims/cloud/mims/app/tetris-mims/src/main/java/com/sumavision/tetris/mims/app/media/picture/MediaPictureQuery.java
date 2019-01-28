package com.sumavision.tetris.mims.app.media.picture;

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
	
}
