package com.sumavision.tetris.mims.app.folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.FolderTypeCannotMatchException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.MaterialVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/folder")
public class FolderController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private SubordinateRoleQuery subordinateRoleQuery;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDAO;
	/**
	 * 创建素材库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午3:56:22
	 * @param Long parentFolderId 父文件夹id
	 * @param String folderName 新文件夹名称
	 * @return FolderVO 新建的文件夹数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/personal/add")
	public Object personalAdd(
			Long parentFolderId,
			String folderName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasPermission(user.getUuid(), parentFolderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.PARENT_CREATE);
		}
		
		FolderPO folder = folderService.addPersionalFolder(user.getUuid(), parentFolderId, folderName, FolderType.PERSONAL);
		
		MaterialVO material = new MaterialVO().set(folder);
		
		return material;
	}
	
	/**
	 * 删除素材库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午1:50:15
	 * @param PathVariable Long folderId 待删除的文件夹id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/personal/remove/{folderId}")
	public Object personalRemove(
			@PathVariable Long folderId, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!FolderType.PERSONAL.equals(folder.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		folderService.removeMaterialFolder(folder, user);
				
		return null;
	}
	
	/**
	 * 重命名素材库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午1:55:53
	 * @param PathVariable Long folderId 待处理文件夹id
	 * @param String folderName 新文件夹名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/personal/rename/{folderId}")
	public Object personalRename(
			@PathVariable Long folderId,
			String folderName,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!FolderType.PERSONAL.equals(folder.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		folderService.rename(folder, folderName);
		
		return null;
	}

	/**
	 * 移动素材库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 下午5:20:58
	 * @param Long folderId 被移动文件夹
	 * @param Long targetId 目标文件夹
	 * @return boolean 文件夹移动是否有效
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/personal/move")
	public Object personalMove(
			Long folderId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!FolderType.PERSONAL.equals(folder.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		FolderPO target = folderDao.findOne(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!FolderType.PERSONAL.equals(target.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		if(folder.getParentId()!=null && folder.getParentId().equals(target.getId())) return false;
		
		folderService.move(folder, target);
		
		return true;
	}
	
	/**
	 * 复制文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:59:56
	 * @param Long folderId 待复制文件夹id
	 * @param Long targetId 目的文件夹id
	 * @return boolean moved 记录是否复制到其他文件夹中
	 * @return MaterialVO copied 复制后的文件夹信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/personal/copy")
	public Object personalCopy(
			Long folderId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!FolderType.PERSONAL.equals(folder.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		FolderPO target = folderDao.findOne(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!FolderType.PERSONAL.equals(target.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		boolean moved = true;
		
		//判断当前文件夹的父文件夹是否是目标文件夹
		if(folder.getParentId()!=null && folder.getParentId().equals(target.getId())) moved = false;
		
		FolderPO copiedFolder = folderService.copyMaterialFolder(user.getUuid(), folder, target);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("moved", moved)
																		 .put("copied", new MaterialVO().set(copiedFolder))
																		 .getMap();
		return result;
	}
	
	/**
	 * 获取素材文件夹树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午10:57:10
	 * @param Long except 例外文件夹
	 * @param Integer maxLevel 查询的最大层级
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/personal/tree")
	public Object personalTree(
			Long except, 
			Integer depth, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<FolderPO> folderTree = null;
		if(except==null && depth==null){
			folderTree = folderDao.findMaterialTreeByUserId(user.getUuid());
		}else if(except==null && depth!=null){
			folderTree = folderDao.findMaterialTreeByUserIdWithDepth(user.getUuid(), depth);
		}else if(except!=null && depth==null){
			folderTree = folderQuery.findMaterialTreeByUserIdWithExcept(user.getUuid(), except);
		}else{
			folderTree = folderQuery.findMaterialTreeByUserIdWithExceptAndDepth(user.getUuid(), except, depth);
		}
		
		List<FolderTreeVO> roots = folderQuery.generateFolderTree(folderTree);
		
		return roots;
	}
	
	/**
	 * 获取媒资文件夹树（完整树）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午2:34:47
	 * @return List<FolderTreeVO> 根节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/tree")
	public Object mediaTree(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		//根据user->role->folder;
		Long role = subordinateRoleQuery.queryRolesByUserId(user.getId());
		List<Long> folderIdsList = new ArrayList<Long>();
		List<FolderRolePermissionPO> list = folderRolePermissionDAO.findByRoleId(role);
		for (int j = 0; j < list.size(); j++) {
			folderIdsList.add(list.get(j).getFolderId());
		}
		
		List<FolderPO> folderTree = folderDao.findByIdIn(folderIdsList);
		//List<FolderPO> folderTree = folderDao.findCompanyTreeByGroupId(user.getGroupId());
		
		List<FolderTreeVO> roots = folderQuery.generateFolderTree(folderTree);
		
		return roots;
	}
	
	/**
	 * 查询有权限的媒资库树（特定类型，全量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午1:43:58
	 * @param @PathVariable String folderType 媒资文件夹类型
	 * @return List<FolderTreeVO> 根节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/permission/media/tree/{folderType}")
	public Object permissionMediaTree(
			@PathVariable String folderType,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		
		Long roleId = subordinateRoleQuery.queryRolesByUserId(Long.parseLong(user.getUuid()));
		List<FolderPO> folderTree = folderDao.findPermissionCompanyTree(roleId, type.toString());
		
		List<FolderTreeVO> roots = folderQuery.generateFolderTree(folderTree);
		
		return roots;
	}
	
	/**
	 * 查询有权限的媒资库树（特定类型，全量，带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午1:43:58
	 * @param @PathVariable String folderType 媒资文件夹类型
	 * @param JSONString except 例外文件夹id列表
	 * @return List<FolderTreeVO> 根节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/permission/media/tree/with/except/{folderType}")
	public Object permissionMediaTreeWithExcept(
			@PathVariable String folderType,
			Long except,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		FolderType type = FolderType.fromPrimaryKey(folderType);
		
		List<FolderPO> folderTree = null;
		
		Long roleId = subordinateRoleQuery.queryRolesByUserId(Long.parseLong(user.getUuid()));
		if(except == null){
			folderTree = folderDao.findPermissionCompanyTree(roleId, type.toString());
		}else{
			folderTree = folderQuery.findPermissionCompanyTreeWithExcept(roleId, type.toString(), except);
		}
		
		List<FolderTreeVO> roots = folderQuery.generateFolderTree(folderTree);
		
		return roots;
	}
	
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
		
		FolderPO folder = folderService.addMediaFolder(user.getUuid(), user.getGroupId(), parent.getId(), folderName, type);
		
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
	
	/**
	 * 移动媒资库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 下午5:20:58
	 * @param Long folderId 被移动文件夹
	 * @param Long targetId 目标文件夹
	 * @return boolean 文件夹移动是否有效
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/move")
	public Object mediaMove(
			Long folderId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasMediaPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		FolderPO target = folderDao.findOne(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folder.getType().equals(target.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.GROUP);
		}
		
		if(folder.getParentId()!=null && folder.getParentId().equals(target.getId())) return false;
		
		folderService.move(folder, target);
		
		return true;
	}
	
	/**
	 * 复制媒资库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:59:56
	 * @param Long folderId 待复制文件夹id
	 * @param Long targetId 目的文件夹id
	 * @return boolean moved 记录是否复制到其他文件夹中
	 * @return MaterialVO copied 复制后的文件夹信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/media/copy")
	public Object mediaCopy(
			Long folderId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasMediaPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		FolderPO target = folderDao.findOne(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folder.getType().equals(target.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.GROUP);
		}
		
		boolean moved = true;
		
		//判断当前文件夹的父文件夹是否是目标文件夹
		if(folder.getParentId()!=null && folder.getParentId().equals(target.getId())) moved = false;
		
		FolderPO copiedFolder = folderService.copyMediaFolder(user.getUuid(), user.getGroupId(), folder, target);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("moved", moved)
																		 .put("copied", new MaterialVO().set(copiedFolder))
																		 .getMap();
		return result;
	}
	
}
