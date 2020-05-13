package com.sumavision.tetris.mims.app.media.upload;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaFileEquipmentPermissionQuery {
	@Autowired
	private MediaFileEquipmentPermissionDAO mediaFileEquipmentPermissionDAO;
	
	/**
	 * 根据媒资描述<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 下午5:49:16
	 * @param permissionBO
	 * @return
	 */
	public List<MediaFileEquipmentPermissionPO> queryList(MediaFileEquipmentPermissionBO permissionBO) throws Exception {
		List<MediaFileEquipmentPermissionPO> permissionPOs = mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaType(permissionBO.getMediaId(), permissionBO.getMediaType());
		return permissionPOs != null ? permissionPOs : new ArrayList<MediaFileEquipmentPermissionPO>();
	}
}
