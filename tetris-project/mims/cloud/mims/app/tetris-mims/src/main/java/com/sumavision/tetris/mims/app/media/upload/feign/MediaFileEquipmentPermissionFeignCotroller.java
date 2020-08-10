package com.sumavision.tetris.mims.app.media.upload.feign;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureDAO;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionBO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionPO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/media/file/equipment/permission/feign")
public class MediaFileEquipmentPermissionFeignCotroller {
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaVideoDAO mediaVideoDAO;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaPictureDAO mediaPictureDAO;
	
	@Autowired
	private MediaFileEquipmentPermissionService permissionService;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/media")
	public Object uploadMedia(String ids, String mediaType, String equipmentIp, HttpServletRequest request) throws Exception {
		if (ids == null || ids.isEmpty() || mediaType == null || mediaType.isEmpty()) return null;
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		List<MediaFileEquipmentPermissionBO> permissionBOs = new ArrayList<MediaFileEquipmentPermissionBO>();
		switch (mediaType) {
		case "picture":
			List<MediaPicturePO> mediaPicturePOs = mediaPictureDAO.findAll(idList);
			for (MediaPicturePO mediaPicturePO : mediaPicturePOs) {
				permissionBOs.add(new MediaFileEquipmentPermissionBO().setFromPicturePO(mediaPicturePO));
			}
			break;
		case "video":
			List<MediaVideoPO> mediaVideoVOs = mediaVideoDAO.findAll(idList);
			for (MediaVideoPO mediaVideoPO : mediaVideoVOs) {
				permissionBOs.add(new MediaFileEquipmentPermissionBO().setFromVideoPO(mediaVideoPO));
			}
			break;
		case "folder":
			//写这样是为了加文件夹folder
			List<FolderPO> folders = folderDao.findByIdIn(idList);
			
			List<MediaFileEquipmentPermissionPO> permissions = new ArrayList<MediaFileEquipmentPermissionPO>(); 
			for(FolderPO folder: folders){
				permissions.add(permissionService.addPermissionSend(folder, equipmentIp));
			}
			
			return permissions;
		default:
			break;
		}
		return permissionService.addPermissionSend(permissionBOs, equipmentIp);
	}
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/folder")
	public Object uploadFolder(Long folderId, String mediaType, String equipmentIp, HttpServletRequest request) throws Exception {
		if (mediaType == null || equipmentIp == null) return null;
		
		//添加文件夹
		if(folderId != null){
			FolderPO folder = folderDao.findOne(folderId);
			if(folder != null){
				permissionService.addPermissionSend(folder, equipmentIp);
			}
		}
		
		//TODO：同步所有，暂时不需要
		/*List<FolderPO> folders = folderQuery.findSubFolders(folderId);
		if(!CollectionUtils.isEmpty(folders)){
			for(FolderPO subFolder: folders){
				permissionService.addPermissionSend(subFolder, equipmentIp);
			}
		}*/

		List<MediaFileEquipmentPermissionBO> permissionBOs = new ArrayList<MediaFileEquipmentPermissionBO>();
		switch (mediaType) {
		case "picture":
			List<MediaPictureVO> mediaPictureVOs = mediaPictureQuery.loadAll(folderId);
			for (MediaPictureVO mediaPictureVO : mediaPictureVOs) {
				if ("PICTURE".equals(mediaPictureVO.getType()) && "ture".equals(mediaPictureVO.getSynchro())) {
					permissionBOs.add(new MediaFileEquipmentPermissionBO().setFromPictureVO(mediaPictureVO));
				}
			}
			break;
		case "video":
			List<MediaVideoVO> mediaVideoVOs = mediaVideoQuery.loadAll(folderId);
			for (MediaVideoVO mediaVideoVO : mediaVideoVOs) {
				if ("VIDEO".equals(mediaVideoVO.getType())&& "ture".equals(mediaVideoVO.getSynchro())) {
					permissionBOs.add(new MediaFileEquipmentPermissionBO().setFromVideoVO(mediaVideoVO));
				}
				
			}
			break;
		default:
			break;
		}
		return permissionService.addPermissionSend(permissionBOs, equipmentIp);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/media")
	public Object deleteMedia(String ids, String mediaType, String equipmentIp, HttpServletRequest request) throws Exception {
		if (ids == null || mediaType == null) return null;
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		//视频、图片
		return permissionService.removePermission(idList, mediaType, equipmentIp);
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/folder")
	public Object deleteFolder(Long folderId, String mediaType, String equipmentIp, HttpServletRequest request) throws Exception {
		if (mediaType == null || equipmentIp == null) return null;
		List<Long> idList = new ArrayList<Long>();
		switch (mediaType) {
		case "picture":
			List<MediaPictureVO> mediaPictureVOs = mediaPictureQuery.loadAllList(folderId);
			for (MediaPictureVO mediaPictureVO : mediaPictureVOs) {
				if ("PICTURE".equals(mediaPictureVO.getType())) {
					idList.add(mediaPictureVO.getId());
				}
			}
			break;
		case "video":
			List<MediaVideoVO> mediaVideoVOs = mediaVideoQuery.loadAllList(folderId);
			for (MediaVideoVO mediaVideoVO : mediaVideoVOs) {
				if ("VIDEO".equals(mediaVideoVO.getType())) {
					idList.add(mediaVideoVO.getId());
				}
			}
			break;
		default:
			break;
		}
		
		List<FolderPO> folders = folderQuery.findSubFolders(folderId);
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folder: folders){
			folderIds.add(folder.getId());
		}
		
		return permissionService.removePermission(folderId, folderIds, idList, mediaType, equipmentIp);
	}

}
