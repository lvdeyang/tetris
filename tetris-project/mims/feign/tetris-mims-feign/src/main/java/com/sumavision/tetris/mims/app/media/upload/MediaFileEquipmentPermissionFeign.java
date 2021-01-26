package com.sumavision.tetris.mims.app.media.upload;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaFileEquipmentPermissionFeign {

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
	@RequestMapping(value = "/media/file/equipment/permission/feign/upload/media")
	public JSONObject uploadMedia(
			@RequestParam("ids") String ids,
			@RequestParam("mediaType") String mediaType,
			@RequestParam("equipmentIp") String equipmentIp) throws Exception;
	
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
	@RequestMapping(value = "/media/file/equipment/permission/feign/upload/folder")
	public JSONObject uploadFolder(
			@RequestParam("folderId") Long folderId,
			@RequestParam("mediaType") String mediaType,
			@RequestParam("equipmentIp") String equipmentIp) throws Exception;
	
	/**
	 * 根据资源id、类型和设备id删除同步文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午5:28:51
	 * @param String ids 资源id数组
	 * @param String mediaType 资源类型
	 * @param String equipmentIp 设备ip
	 */
	@RequestMapping(value = "/media/file/equipment/permission/feign/delete/media")
	public JSONObject deleteMedia(
			@RequestParam("ids") String ids,
			@RequestParam("mediaType") String mediaType,
			@RequestParam("equipmentIp") String equipmentIp) throws Exception;

	/**
	 * 根据媒资目录id和资源类型删除同步文件<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月12日 下午5:39:34
	 * @param Long folderId 目录id 
	 * @param String mediaType 资源类型
	 * @param String equipmentIp 设备ip
	 */
	@RequestMapping(value = "/media/file/equipment/permission/feign/delete/folder")
	public JSONObject deleteFolder(
			@RequestParam("folderId") Long folderId,
			@RequestParam("mediaType") String mediaType,
			@RequestParam("equipmentIp") String equipmentIp) throws Exception;
}
