package com.sumavision.tetris.mims.app.folder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.material.MaterialFileDAO;
import com.sumavision.tetris.mims.app.material.MaterialFilePO;
import com.sumavision.tetris.mims.app.material.MaterialFileQuery;
import com.sumavision.tetris.mims.app.material.MaterialFileService;
import com.sumavision.tetris.mims.app.role.RoleClassify;
import com.sumavision.tetris.mims.app.role.RoleDAO;
import com.sumavision.tetris.mims.app.role.RolePO;
import com.sumavision.tetris.mims.app.role.RoleUserPermissionDAO;
import com.sumavision.tetris.mims.app.role.RoleUserPermissionPO;
import com.sumavision.tetris.user.UserVO;

/**
 * 文件夹操作（主增删改）<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 下午3:37:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FolderService {

	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderTool;
	
	@Autowired
	private FolderUserPermissionDAO folderUserPermissionDao;
	
	@Autowired
	private FolderGroupPermissionDAO folderGroupPermissionDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	@Autowired
	private MaterialFileDAO materialFileDao;
	
	@Autowired
	private MaterialFileQuery materialFileTool;
	
	@Autowired
	private MaterialFileService materialFileService;
	
	@Autowired
	private MaterialFileQuery materialTool;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleUserPermissionDAO roleUserPermissionDao;

	/**
	 * 新增文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午3:42:28
	 * @param String userId 创建文件夹的用户
	 * @param Long parentFolderId 父文件夹
	 * @param String folderName 文件夹名称
	 * @return FolderPO 新建的文件夹
	 * @exception FolderNotExistException 未查询到父文件夹
	 */
	public FolderPO add(String userId, Long parentFolderId, String folderName, FolderType type) throws FolderNotExistException{
		
		FolderPO parentFolder = folderDao.findOne(parentFolderId);
		if(parentFolder == null) throw new FolderNotExistException(parentFolderId);
		String basePath = parentFolder.getParentPath()==null?"":parentFolder.getParentPath();
		
		FolderPO folder = new FolderPO();
		folder.setName(folderName);
		folder.setParentId(parentFolderId);
		folder.setParentPath(new StringBufferWrapper().append(basePath).append("/").append(parentFolderId).toString());
		folder.setDepth();
		folder.setType(type);
		folder.setUpdateTime(new Date());
		folderDao.save(folder);
		
		FolderUserPermissionPO permission = new FolderUserPermissionPO();
		permission.setFolderId(folder.getId());
		permission.setUserId(userId);
		permission.setUpdateTime(new Date());
		folderUserPermissionDao.save(permission);
		
		return folder;
	}
	
	/**
	 * 删除素材库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 上午10:32:06
	 * @param FolderPO folder 待删除文件夹
	 * @param UserVO user 操作用户
	 * @throws Exception
	 */
	public void removeMaterialFolder(FolderPO folder, UserVO user) throws Exception{
		
		//获取所有的子文件夹
		List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
		List<FolderPO> totalFolders = new ArrayListWrapper<FolderPO>().add(folder).getList();
		if(subFolders!=null && subFolders.size()>0) totalFolders.addAll(subFolders);
		
		//获取所有的文件夹id
		Set<Long> folderIds = new HashSet<Long>();
		for(FolderPO scope:totalFolders){
			folderIds.add(scope.getId());
		}
		
		//获取所有的权限数据
		List<FolderUserPermissionPO> permissions = folderUserPermissionDao.findByUserIdAndFolderIdIn(user.getUuid(), folderIds);
		
		//获取文件夹下所有的素材
		List<MaterialFilePO> materials = materialFileDao.findByFolderIdIn(folderIds);
		
		//删除所有文件夹
		folderDao.deleteInBatch(totalFolders);
		
		//删除所有权限
		folderUserPermissionDao.deleteInBatch(permissions);
		
		//删除所有素材
		materialFileService.remove(materials);
		
	}
	
	/**
	 * 重命名文件夹<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午1:54:36
	 * @param FolderPO folder 待处理文件夹
	 * @param String name 新文件夹名称
	 */
	public void rename(FolderPO folder, String name) throws Exception{
		folder.setName(name);
		folderDao.save(folder);
	}
	
	/**
	 * 移动文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 下午4:49:31
	 * @param FolderPO folder 被移动的文件夹
	 * @param FolderPO target 目标文件夹
	 */
	public void move(FolderPO folder, FolderPO target) throws Exception{
		
		//移动当前文件夹
		folder.setParentId(target.getId());
		StringBufferWrapper parentPath = new StringBufferWrapper();
		if(target.getParentPath() != null) parentPath.append(target.getParentPath());
		parentPath.append("/").append(target.getId());
		folder.setParentPath(parentPath.toString());
		
		//移动当前文件夹的子文件夹
		List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
		if(subFolders!=null && subFolders.size()>0){
			String basePath = new StringBufferWrapper().append(folder.getParentPath())
													   .append("/")
													   .append(folder.getId())
													   .toString();
			String separator0 = new StringBufferWrapper().append("/")
													     .append(folder.getId())
													     .toString();
			String separator1 = new StringBufferWrapper().append("/")
													     .append(folder.getId())
													     .append("/")
													     .toString();
			for(FolderPO subFolder:subFolders){
				if(subFolder.getParentPath().endsWith(separator0)){
					subFolder.setParentPath(basePath);
				}else{
					String[] pathInfo = subFolder.getParentPath().split(separator1);
					subFolder.setParentPath(new StringBufferWrapper().append(basePath).append("/").append(pathInfo[1]).toString());
				}
			}
		}
		
		List<FolderPO> totalFolders = new ArrayList<FolderPO>();
		totalFolders.add(folder);
		if(subFolders!=null && subFolders.size()>0) totalFolders.addAll(subFolders);
		folderDao.save(totalFolders);
	}
	
	/**
	 * 复制文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午9:35:20
	 * @param FolderPO folder 待复制文件夹
	 * @param FolderPO target 目标文件夹
	 * @return boolean 是否复制到其他文件夹中
	 */
	public FolderPO copy(String userId, FolderPO folder, FolderPO target) throws Exception{
		
		boolean moved = true;
		
		//判断当前文件夹的父文件夹是否是目标文件夹
		if(folder.getParentId()!=null && folder.getParentId().equals(target.getId())) moved = false;
		
		//所有文件夹id
		Set<Long> totalFolderIds = new HashSetWrapper<Long>().add(folder.getId()).getSet();
		
		//复制文件夹
		FolderPO copiedFolder = folder.copy(moved?null:new StringBufferWrapper().append(folder.getName())
																		        .append("（副本：")
																		        .append(DateUtil.format(new Date(), DateUtil.dateTimePattern))
																		        .append("）")
																		        .toString());
		List<FolderPO> totalCopyFolders = new ArrayListWrapper<FolderPO>().add(copiedFolder).getList();
		
		//获取子文件夹
		List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
		if(subFolders!=null && subFolders.size()>0){
			for(FolderPO subFolder:subFolders){
				totalCopyFolders.add(subFolder.copy(null));
				totalFolderIds.add(subFolder.getId());
			}
		}
		
		//保存一下
		folderDao.save(totalCopyFolders);
		
		//生成权限
		List<FolderUserPermissionPO> permissions = new ArrayList<FolderUserPermissionPO>();
		for(FolderPO copyFolder:totalCopyFolders){
			FolderUserPermissionPO permission = new FolderUserPermissionPO();
			permission.setFolderId(copyFolder.getId());
			permission.setUserId(userId);
			permission.setUpdateTime(new Date());
			permissions.add(permission);
		}
		folderUserPermissionDao.save(permissions);
		
		//复制素材
		List<MaterialFilePO> materials = materialFileTool.findMaterialsByFolderIds(totalFolderIds);
		List<MaterialFilePO> totalCopyMaterials = new ArrayList<MaterialFilePO>();
		if(materials!=null && materials.size()>0){
			for(MaterialFilePO material:materials){
				totalCopyMaterials.add(material.copy());
			}
		}
		materialFileDao.save(totalCopyMaterials);
		
		//重组文件夹链
		List<FolderPO> rootFolders = new ArrayListWrapper<FolderPO>().add(folder).getList();
		List<FolderPO> rootCopyFolders = new ArrayListWrapper<FolderPO>().add(folderTool.loopByUuid(folder.getUuid(), totalCopyFolders)).getList();
		setFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, materials, totalCopyMaterials);
		
		//生成新的uuid
		for(FolderPO copyFolder:totalCopyFolders){
			copyFolder.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		
		for(MaterialFilePO material:materials){
			material.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		
		//保存数据
		folderDao.save(totalCopyFolders);
		materialFileDao.save(totalCopyMaterials);
		
		return copiedFolder;
	}
	
	/**
	 * 重组文件夹链<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:14:48
	 * @param Collection<FolderPO> roots 待重组的文件夹（复制前）
	 * @param Collection<FolderPO> copiedRoots 待重组的文件夹（复制后的）
	 * @param FolderPO parent 待重组文件夹的父文件夹
	 * @param Collection<FolderPO> copiedSubFolders 复制之后的子文件夹
	 * @param Collection<FolderPO> subFolders 原子文件夹
	 * @param Collection<MaterialFilePO> copiedMaterials 复制之后的素材
	 * @param Collection<MaterialFilePO> materials 原素材
	 */
	private void setFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MaterialFilePO> materials,
			Collection<MaterialFilePO> copiedMaterials){
		
		if(roots==null || roots.size()<=0 || copiedRoots==null || copiedRoots.size()<=0) return;
		
		for(FolderPO copiedRoot:copiedRoots){
			copiedRoot.setParentId(parent==null?null:parent.getId());
			copiedRoot.setParentPath(parent==null?
										null
										:(parent.getParentPath()==null?
												new StringBufferWrapper().append("/")
																	     .append(parent.getId())
																	     .toString()
										       :new StringBufferWrapper().append(parent.getParentPath())
										       							 .append("/")
										       							 .append(parent.getId())
										       							 .toString()));
			copiedRoot.setDepth();
			
			FolderPO root = folderTool.loopByUuid(copiedRoot.getUuid(), roots);
			
			//重组子文件夹
			if(subFolders!=null && subFolders.size()>0){
				List<FolderPO> filteredSubFolders = new ArrayList<FolderPO>();
				List<FolderPO> filteredCopiedSubFolders = new ArrayList<FolderPO>();
				for(FolderPO subFolder:subFolders){
					if(root.getId().equals(subFolder.getParentId())){
						FolderPO copiedSubFolder = folderTool.loopByUuid(subFolder.getUuid(), copiedSubFolders);
						filteredSubFolders.add(subFolder);
						filteredCopiedSubFolders.add(copiedSubFolder);
					}
				}
				setFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, materials, copiedMaterials);
			}
			
			//重组素材文件
			if(materials!=null && materials.size()>0){
				for(MaterialFilePO material:materials){
					if(material.getFolderId().equals(root.getId())){
						MaterialFilePO copiedMaterial = materialTool.loopForUuid(material.getUuid(), copiedMaterials);
						copiedMaterial.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 创建个人网盘<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午3:27:20
	 * @param String userId 用户id
	 * @param String nickname 昵称 
	 */
	public FolderPO createPersonalDisk(String userId, String nickname) throws Exception{
		FolderPO folder = new FolderPO();
		folder.setName(new StringBufferWrapper().append(nickname).append("（").append(FolderType.PERSONAL.getName()).append("）").toString());
		folder.setDepth();
		folder.setType(FolderType.PERSONAL);
		folder.setUpdateTime(new Date());
		folderDao.save(folder);
		FolderUserPermissionPO permission = new FolderUserPermissionPO();
		permission.setFolderId(folder.getId());
		permission.setUserId(userId);
		permission.setUpdateTime(new Date());
		folderUserPermissionDao.save(permission);
		return folder;
	}
	
	/**
	 * 创建企业文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午3:30:15
	 * @param String companyId 公司id
	 * @param String companyName 公司名称
	 * @param FolderType type 文件夹类型
	 * @param Long parentId 父文件夹
	 * @param String parentPath 上级文件夹路径
	 * @param Long roleId 媒资内角色id
	 * @return FolderPO 文件夹数据
	 */
	public FolderPO createCompanyFolder(
			String companyId, 
			String companyName, 
			FolderType type, 
			Long parentId, 
			String parentPath,
			Long roleId) throws Exception{
		FolderPO folder = new FolderPO();
		folder.setName(new StringBufferWrapper().append(companyName).append("（").append(type.getName()).append("）").toString());
		folder.setDepth();
		folder.setType(type);
		folder.setUpdateTime(new Date());
		folder.setParentId(parentId);
		folder.setParentPath(parentPath);
		folderDao.save(folder);
		FolderGroupPermissionPO permission0 = new FolderGroupPermissionPO();
		permission0.setFolderId(folder.getId());
		permission0.setGroupId(companyId);
		permission0.setUpdateTime(new Date());
		folderGroupPermissionDao.save(permission0);
		FolderRolePermissionPO permission1 = new FolderRolePermissionPO();
		permission1.setFolderId(folder.getId());
		permission1.setRoleId(roleId);
		permission1.setUpdateTime(new Date());
		folderRolePermissionDao.save(permission1);
		return folder;
	}
	
	/**
	 * 创建企业网盘<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 下午3:35:36
	 * @param String companyId 公司id
	 * @param String companyName 公司名称
	 * @param String userId 用户id
	 */
	public void createCompanyDisk(String companyId, String companyName, String userId) throws Exception{
		//创建管理员
		RolePO role = new RolePO();
		role.setGroupId(companyId);
		role.setName("管理员");
		role.setRemoveable(false);
		role.setSerial(0);
		role.setUpdateTime(new Date());
		role.setClassify(RoleClassify.INTERNAL_COMPANY_ADMIN_ROLE);
		roleDao.save(role);
		
		//绑定用户
		RoleUserPermissionPO permission1 = new RoleUserPermissionPO();
		permission1.setRoleId(role.getId());
		permission1.setUserId(userId);
		permission1.setUpdateTime(new Date());
		roleUserPermissionDao.save(permission1);
		
		//企业根目录
		FolderPO root = createCompanyFolder(companyId, companyName, FolderType.COMPANY, null, null, role.getId());
		
		String parentPath = new StringBufferWrapper().append("/").append(root.getId()).toString();
		
		//图片
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_PICTURE, root.getId(), parentPath, role.getId());
		
		//视频
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_VIDEO, root.getId(), parentPath, role.getId());
		
		//音频
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_AUDIO, root.getId(), parentPath, role.getId());
		
		//视频流
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_VIDEO_STREAM, root.getId(), parentPath, role.getId());
		
		//音频流
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_AUDIO_STREAM, root.getId(), parentPath, role.getId());
		
		//文本
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_TXT, root.getId(), parentPath, role.getId());
	}
	
}
