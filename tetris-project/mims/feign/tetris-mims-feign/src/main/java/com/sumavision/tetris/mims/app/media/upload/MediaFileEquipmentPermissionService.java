package com.sumavision.tetris.mims.app.media.upload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaFileEquipmentPermissionService {
	@Autowired
	private MediaFileEquipmentPermissionFeign mediaFileEquipmentPermissionFeign;
	
	/**
	 * 批量同步文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 上午10:11:59
	 * @param String ids 资源id数组
	 * @param String mediaType 资源类型
	 * @param String equipmentIp 设备ip
	 * @return List<MediaFileEquipmentPermissionPO> 资源关联关系
	 */
	public List<MediaFileEquipmentPermissionPO> uploadMedia(
			List<Long> ids,
			String mediaType,
			String equipmentIp) throws Exception{
		return JsonBodyResponseParser.parseArray(mediaFileEquipmentPermissionFeign.uploadMedia(JSONArray.toJSONString(ids), mediaType, equipmentIp), MediaFileEquipmentPermissionPO.class);
	};
	
	/**
	 * 批量同步目录下资源(仅一层子文件)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 上午10:13:08
	 * @param Long folderId 目录id
	 * @param String mediaType 资源类型
	 * @param String equipmentIp 设备ip
	 * @return List<MediaFileEquipmentPermissionPO> 资源关联关系
	 */
	public List<MediaFileEquipmentPermissionPO> uploadFolder(Long folderId, String mediaType, String equipmentIp) throws Exception {
		return JsonBodyResponseParser.parseArray(mediaFileEquipmentPermissionFeign.uploadFolder(folderId, mediaType, equipmentIp), MediaFileEquipmentPermissionPO.class);
	}
	
	/**
	 * 根据资源id、类型和设备id删除同步文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午5:28:51
	 * @param String ids 资源id数组
	 * @param String mediaType 资源类型
	 * @param String equipmentIp 设备ip
	 */
	public List<MediaFileEquipmentPermissionPO> deleteMedia(List<Long> ids, String mediaType, String equipmentIp) throws Exception {
		return JsonBodyResponseParser.parseArray(mediaFileEquipmentPermissionFeign.deleteMedia(JSONArray.toJSONString(ids), mediaType, equipmentIp), MediaFileEquipmentPermissionPO.class);
	}
	
	/**
	 * 根据媒资目录id和资源类型删除同步文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午5:39:34
	 * @param Long folderId 目录id 
	 * @param String mediaType 资源类型
	 * @param String equipmentIp 设备ip
	 */
	public List<MediaFileEquipmentPermissionPO> deleteFolder(Long folderId, String mediaType, String equipmentIp) throws Exception {
		return JsonBodyResponseParser.parseArray(mediaFileEquipmentPermissionFeign.deleteFolder(folderId, mediaType, equipmentIp), MediaFileEquipmentPermissionPO.class);
	}
}
