package com.sumavision.tetris.mims.app.folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class FolderService {
	@Autowired
	private FolderFeign folderFeign;
	
	/**
	 * 删除媒资库文件夹<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午5:05:08
	 * @param Long folderId 待删除的文件夹id
	 */
	public void delete(Long folderId) throws Exception {
		folderFeign.delete(folderId);
	}
	
	/**
	 * 创建资源库文件夹并绑定权限<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月9日 下午3:13:03
	 * @param String folderType 资源类型
	 * @param Long parentFolderId 父目录id
	 * @param String folderName 目录名称
	 */
	public MediaPictureVO addByFolderType(String folderType,
			Long parentFolderId,
			String folderName) throws Exception {
		return JsonBodyResponseParser.parseObject(folderFeign.addByFolderType(folderType, parentFolderId, folderName), MediaPictureVO.class);
	}
}
