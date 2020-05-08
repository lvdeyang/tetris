package com.sumavision.tetris.mims.app.folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
