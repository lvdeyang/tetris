package com.sumavision.tetris.mims.app.folder.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderService;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/folder/feign")
public class FolderFeignController {
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private UserQuery userQuery;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/remove")
	public Object mediaRemove(Long folderId, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folder.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		folderService.removeMediaFolder(folder, user);
				
		return null;
	}
	
	/**
	 * 创建素材库文件夹<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月9日 下午1:43:00
	 * @param @PathVariable String folderType FolderType.primaryKey 文件夹类型
	 * @param Long parentFolderId 父文件夹id
	 * @param String folderName 文件夹名称
	 * @return MediaPictureVO 新增的文件夹
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/by/folderType")
	public Object add(
			String folderType,
			Long parentFolderId,
			String folderName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		if (parentFolderId == null) {
			List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
			if (folderTree != null && !folderTree.isEmpty()) parentFolderId = folderTree.get(0).getId();
		} else if(parentFolderId.longValue() == 0l){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.PARENT_CREATE);
		}
		
		FolderPO parent = folderDao.findOne(parentFolderId);
		
		if(parent == null){
			throw new FolderNotExistException(parentFolderId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), parent.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		
		FolderPO folder = folderService.addMediaFolderBindRole(user.getId(), user.getGroupId(), parent.getId(), folderName, type);
		
		return new MediaPictureVO().set(folder);
	}
}
