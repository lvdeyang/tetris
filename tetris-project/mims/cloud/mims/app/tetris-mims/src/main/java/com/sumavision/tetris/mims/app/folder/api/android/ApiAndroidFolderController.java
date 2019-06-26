package com.sumavision.tetris.mims.app.folder.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/android/folder")
public class ApiAndroidFolderController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private FolderDAO folderDao;
	
	/**
	 * 添加媒资图片文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午1:43:00
	 * @param @PathVariable String folderType FolderType.primaryKey 文件夹类型
	 * @param Long parentFolderId 父文件夹id
	 * @param String folderName 文件夹名称
	 * @return MediaPictureVO 新增的文件夹
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/add/{folderType}")
	public Object mediaAdd(
			@PathVariable String folderType,
			Long parentFolderId,
			String folderName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		FolderPO parent = folderDao.findOne(parentFolderId);
		
		if(parent == null){
			throw new FolderNotExistException(parentFolderId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), parent.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		
		FolderPO folder = folderService.addMediaFolder(user.getGroupId(), parent.getId(), folderName, type);
		
		return new MediaPictureVO().set(folder);
	}
	
	/**
	 * 删除媒资库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午1:50:15
	 * @param PathVariable Long folderId 待删除的文件夹id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/remove/{folderId}")
	public Object mediaRemove(
			@PathVariable Long folderId, 
			HttpServletRequest request) throws Exception{
		
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
	 * 重命名媒资库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午1:55:53
	 * @param PathVariable Long folderId 待处理文件夹id
	 * @param String folderName 新文件夹名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/rename/{folderId}")
	public Object mediaRename(
			@PathVariable Long folderId,
			String folderName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folder.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		folderService.rename(folder, folderName);
		
		return null;
	}
}
