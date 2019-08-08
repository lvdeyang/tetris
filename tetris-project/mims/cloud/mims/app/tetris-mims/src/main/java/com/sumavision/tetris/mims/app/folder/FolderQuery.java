package com.sumavision.tetris.mims.app.folder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 文件夹相关操作（主查询或数据转换）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 上午11:37:00
 */
@Component
public class FolderQuery {

	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderUserPermissionDAO folderUserPermissionDao;
	
	@Autowired
	private FolderGroupPermissionDAO folderGroupPermissionDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 根据uuid查找文件夹（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:49:48
	 * @param String uuid 文件夹uuid
	 * @param Collection<FolderPO> folders 查找范围
	 * @return FolderPO 目标文件夹
	 */
	public FolderPO loopByUuid(String uuid, Collection<FolderPO> folders){
		if(folders==null || folders.size()<=0) return null;
		for(FolderPO folder:folders){
			if(folder.getUuid().equals(uuid)){
				return folder;
			}
		}
		return null;
	}
	
	public List<FolderPO> getPermissionParentFolders(FolderPO current, String userId){
		if(current.getParentPath() == null) return null;
		Set<Long> parentIds = new HashSet<Long>();
		String[] path = current.getParentPath().split("/");
		for(int i=1; i<path.length; i++){
			parentIds.add(Long.valueOf(path[i]));
		}
		return folderDao.findAll(parentIds);
	}
	
	/**
	 * 获取一个文件夹的所有父文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午11:35:39
	 * @param FolderPO current 当前文件夹
	 * @return List<FolderPO> 父文件夹列表
	 */
	public List<FolderPO> getParentFolders(FolderPO current){
		if(current.getParentPath() == null) return null;
		Set<Long> parentIds = new HashSet<Long>();
		String[] path = current.getParentPath().split("/");
		for(int i=1; i<path.length; i++){
			parentIds.add(Long.valueOf(path[i]));
		}
		return folderDao.findAll(parentIds);
	}
	
	/**
	 * 获取一个文件夹的所有子文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午5:20:17
	 * @param Long folderId 当前文件夹id
	 * @return List<FolderPO> 子文件夹列表
	 */
	public List<FolderPO> findSubFolders(Long folderId){
		return folderDao.findSubFolders(splicePathReg(folderId));
	}
	
	/**
	 * 获取一个文件夹的所有子文件夹（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:58:16
	 * @param Long folderId 文件夹id
	 * @param Long except 例外文件夹id
	 * @return List<FolderPO> 子文件夹列表
	 */
	public List<FolderPO> findSubFoldersWithExcept(Long folderId, Long except){
		return folderDao.findSubFoldersWithExcept(splicePathReg(folderId), except, splicePathReg(except));
	}
	
	/**
	 * 根据id查询有权限的企业文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月29日 上午10:26:58
	 * @param Collection<Long> ids 文件夹id列表
	 * @return List<FolderPO> 文件夹列表
	 */
	public List<FolderPO> findPermissionCompanyFolderByIdIn(Collection<Long> ids, String type) throws Exception{
		UserVO user = userQuery.current();
		if(user.getBusinessRoles() == null){
			return null;
		}else{
			List<Long> businessRoleIds = JSON.parseArray(new StringBufferWrapper().append("[")
																				  .append(user.getBusinessRoles())
																				  .append("]")
																				  .toString(), Long.class);
			return folderDao.findPermissionCompanyFolderByIdIn(businessRoleIds, ids, type);
		}
	}
	
	/**
	 * 获取文件夹下有权限的企业文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月29日 上午9:59:29
	 * @param Long parentId 附文件夹id
	 * @return List<FolderPO> 文件夹列表
	 */
	public List<FolderPO> findPermissionCompanyFolderByParentIdOrderByNameAsc(Long parentId) throws Exception{
		UserVO user = userQuery.current();
		if(user.getBusinessRoles() == null){
			return null;
		}else{
			List<Long> businessRoleIds = JSON.parseArray(new StringBufferWrapper().append("[")
																				  .append(user.getBusinessRoles())
																				  .append("]")
																				  .toString(), Long.class);
			return folderDao.findPermissionCompanyFolderByParentIdOrderByNameAsc(businessRoleIds, parentId);
		}
	}
	
	/**
	 * 查询素材库文件夹树（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:34:51
	 * @param Long userId 用户id
	 * @param Long except 例外文件夹id
	 * @return List<FolderPO> 文件夹列表
	 */
	public List<FolderPO> findMaterialTreeByUserIdWithExcept(String userId, Long except){
		return folderDao.findMaterialTreeByUserIdWithExcept(userId, except, splicePathReg(except));
	}
	
	/**
	 * 查询素材库文件夹树（带例外，带深度）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:31:45
	 * @param Long userId 用户id
	 * @param Long except 例外文件夹id
	 * @param Integer depth 查询深度
	 * @return List<FolderPO> 文件夹列表
	 */
	public List<FolderPO> findMaterialTreeByUserIdWithExceptAndDepth(String userId, Long except, Integer depth){
		return folderDao.findMaterialTreeByUserIdWithExceptAndDepth(userId, except, splicePathReg(except), depth);
	}
	
	/**
	 * 获取有权限的企业文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月29日 上午9:46:19
	 * @param String type 文件夹类型
	 * @return List<FolderPO> 文件夹列表
	 */
	public List<FolderPO> findPermissionCompanyTree(String type) throws Exception{
		UserVO user = userQuery.current();
		if(user.getBusinessRoles() == null){
			return null;
		}else{
			List<Long> businessRoleIds = JSON.parseArray(new StringBufferWrapper().append("[")
																				  .append(user.getBusinessRoles())
																				  .append("]")
																				  .toString(), Long.class);
			return folderDao.findPermissionCompanyTree(businessRoleIds, type);
		}
	} 
	
	/**
	 * 获取有权限的企业文件夹（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月29日 上午9:41:07
	 * @param String type 文件夹类型
	 * @param Long except 例外文件夹id
	 * @return List<FolderPO> 文件夹列表
	 */
	public List<FolderPO> findPermissionCompanyTreeWithExcept(String type, Long except) throws Exception{
		UserVO user = userQuery.current();
		if(user.getBusinessRoles() == null){
			return null;
		}else{
			List<Long> businessRoleIds = JSON.parseArray(new StringBufferWrapper().append("[")
																				  .append(user.getBusinessRoles())
																				  .append("]")
																				  .toString(), Long.class);
			return folderDao.findPermissionCompanyTreeWithExcept(businessRoleIds, type, except, splicePathReg(except));
		}
	}
	
	/**
	 * 生成文件夹面包屑<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午1:24:00
	 * @param List<FolderPO> folders 散排的文件夹
	 * @return FolderBreadCrumbVO 文件夹面包屑
	 */
	public FolderBreadCrumbVO generateFolderBreadCrumb(List<FolderPO> folders) throws Exception{
		FolderBreadCrumbVO root = new FolderBreadCrumbVO().set(findRoots(folders).get(0));
		FolderBreadCrumbVO current = root;
		while(true){
			boolean hasNext = false;
			for(FolderPO folder:folders){
				if(current.getId().equals(folder.getParentId())){
					current.setNext(new FolderBreadCrumbVO().set(folder));
					current = current.getNext();
					hasNext = true;
					break;
				}
			}
			if(!hasNext) break;
		}
		return root;
	}
	
	/**
	 * 将面包屑数据转换成列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月8日 上午9:12:37
	 * @param FolderBreadCrumbVO breadCrumb 面包屑数据
	 * @return List<FolderBreadCrumbVO> 面包屑列表
	 */
	public List<FolderBreadCrumbVO> convertFolderBreadCrumbToList(FolderBreadCrumbVO breadCrumb) throws Exception{
		List<FolderBreadCrumbVO> breadCrumbList = new ArrayList<FolderBreadCrumbVO>();
		FolderBreadCrumbVO current = breadCrumb;
		breadCrumbList.add(current);
		while(current.getNext() != null){
			current = current.getNext();
			breadCrumbList.add(current);
		}
		return breadCrumbList;
	}
	
	/**
	 * 生成当前文件夹面包屑路径<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月23日 上午10:56:08
	 * @param Long folderId 文件夹id
	 * @return String 面包屑路径
	 */
	public String generateFolderBreadCrumb(Long folderId) throws Exception{
		FolderPO currentFolder = folderDao.findOne(folderId);
		String parentPath = currentFolder.getParentPath();
		if(parentPath == null){
			return new StringBufferWrapper().append("/").append(currentFolder.getName()).toString();
		}else{
			String[] parentIds = parentPath.substring(1, parentPath.length()).split("/");
			List<Long> parentFolderIds = new ArrayList<Long>();
			for(String parentId:parentIds){
				parentFolderIds.add(Long.valueOf(parentId));
			}
			List<FolderPO> parentFolders = folderDao.findByIdIn(parentFolderIds);
			StringBufferWrapper breadCrumb = new StringBufferWrapper();
			for(int i=0; i<parentIds.length; i++){
				Long currentFolderId = Long.valueOf(parentIds[i]);
				for(FolderPO folder:parentFolders){
					if(folder.getId().equals(currentFolderId)){
						breadCrumb.append("/").append(folder.getName());
						break;
					}
				}
			}
			return breadCrumb.toString();
		}
	}
	
	/**
	 * 生成文件夹树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午10:36:51
	 * @param List<FolderPO> folders 散排的文件夹
	 * @return List<FolderTreeVO> 树的根节点列表
	 */
	public List<FolderTreeVO> generateFolderTree(List<FolderPO> folders) throws Exception{
		List<FolderTreeVO> roots = new ArrayList<FolderTreeVO>();
		List<FolderPO> rootEntities = findRoots(folders);
		for(FolderPO rootEntity:rootEntities){
			roots.add(new FolderTreeVO().set(rootEntity));
		}
		packSubFolders(roots, folders);
		return roots;
	}
	private void packSubFolders(List<FolderTreeVO> roots, List<FolderPO> totalFolders) throws Exception{
		if(roots==null || roots.size()<=0) return;
		if(totalFolders==null || totalFolders.size()<=0) return;
		for(FolderTreeVO root:roots){
			for(FolderPO folder:totalFolders){
				if(root.getId().equals(folder.getParentId())){
					if(root.getChildren() == null) root.setChildren(new ArrayList<FolderTreeVO>());
					root.getChildren().add(new FolderTreeVO().set(folder));
				}
			}
			if(root.getChildren()!=null && root.getChildren().size()>0) packSubFolders(root.getChildren(), totalFolders);
		}
	}
	
	/**
	 * 判断用户对文件夹是否有权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午3:24:47
	 * @param String userId 用户id
	 * @param String folderId 文件夹id
	 * @return boolean 判断结果
	 */
	public boolean hasPermission(String userId, Long folderId){
		FolderUserPermissionPO permission = folderUserPermissionDao.findByUserIdAndFolderId(userId, folderId);
		if(permission == null) return false;
		return true;
	}
	
	/**
	 * 判断企业对文件夹是否有权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月11日 下午3:00:36
	 * @param String groupId 企业id
	 * @param Long folderId 文件夹id
	 * @return boolean 判断结果
	 */
	public boolean hasGroupPermission(String groupId, Long folderId){
		FolderGroupPermissionPO permission = folderGroupPermissionDao.findByGroupIdAndFolderId(groupId, folderId);
		if(permission == null) return false;
		return true;
	}
	
	/**
	 * 判断用户对媒资文件夹是否有权限<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午2:43:03
	 * @param String userId 用户id
	 * @param Long folderId 媒资文件夹id
	 * @return boolean 判断结果
	 */
	public boolean hasMediaPermission(String userId, Long folderId){
		List<FolderRolePermissionPO> permissions = folderRolePermissionDao.findByFolderIdAndUserId(userId, folderId);
		if(permissions==null || permissions.size()<=0) return false;
		return true;
	}
	
	/**
	 * 拼接路径查询规则<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:56:53
	 * @param Long folderId 文件夹id
	 * @return String 查询规则
	 */
	private String splicePathReg(Long folderId){
		return new StringBufferWrapper().append("%")
								  	    .append(folderId)
								  	    .append("%")
								  	    .toString();
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午2:19:56
	 * @param Collection<FolderPO> folders 查找范围
	 * @return List<FolderPO> 根节点
	 */
	public List<FolderPO> findRoots(Collection<FolderPO> folders) throws Exception{
		List<FolderPO> roots = new ArrayList<FolderPO>();
		if(folders==null || folders.size()<=0) return roots;
		for(FolderPO folder:folders){
			Long parentId = folder.getParentId();
			boolean finded = false;
			for(FolderPO target:folders){
				if(target.getId().equals(parentId)){
					finded = true;
					break;
				}
			}
			if(!finded) roots.add(folder);
		}
		return roots;
	}
	
	/**
	 * 生成文件夹面包屑<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月6日 下午1:24:00
	 * @param List<FolderPO> folders 散排的文件夹
	 * @return List<FolderBreadCrumbVO> 文件夹面包屑
	 */
	public List<FolderBreadCrumbVO> generateFolderBreadCrumbForAndroid(List<FolderPO> folders) throws Exception{
		FolderBreadCrumbVO root = new FolderBreadCrumbVO().set(findRoots(folders).get(0));
		FolderBreadCrumbVO current = root;
		
		List<FolderBreadCrumbVO> breadCrumbs = new ArrayList<FolderBreadCrumbVO>();
		breadCrumbs.add(current);
		
		while(true){
			boolean hasNext = false;
			for(FolderPO folder:folders){
				if(current.getId().equals(folder.getParentId())){
					current.setChildId(folder.getId());
					current = new FolderBreadCrumbVO().set(folder).setParentId(folder.getParentId());;
					breadCrumbs.add(current);
					hasNext = true;
					break;
				}
			}
			if(!hasNext) break;
		}
		return breadCrumbs;
	}
	
}
