package com.sumavision.tetris.mims.app.folder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.role.BusinessRolePermissionQuery;
import com.sumavision.tetris.business.role.BusinessRoleQuery;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.material.MaterialFileDAO;
import com.sumavision.tetris.mims.app.material.MaterialFilePO;
import com.sumavision.tetris.mims.app.material.MaterialFileQuery;
import com.sumavision.tetris.mims.app.material.MaterialFileService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureDAO;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamDAO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamPO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamDAO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamPO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtDAO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtPO;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtQuery;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionVO;
//import com.sumavision.tetris.mims.app.role.RoleDAO;
//import com.sumavision.tetris.mims.app.role.RolePO;
//import com.sumavision.tetris.mims.app.role.RoleUserPermissionDAO;
//import com.sumavision.tetris.mims.app.role.RoleUserPermissionPO;
//import com.sumavision.tetris.mims.app.role.exception.RoleNotExistException;
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
	private MaterialFileQuery materialFileQuery;
	
	@Autowired
	private MaterialFileService materialFileService;
	
	@Autowired
	private MediaPictureDAO mediaPictureDao;

	@Autowired
	private MediaPictureService mediaPictureService;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaVideoDAO mediaVideoDao;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private MediaVideoStreamDAO mediaVideoStreamDao;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	@Autowired
	private MediaAudioStreamService mediaAudioStreamService;
	
	@Autowired
	private MediaAudioStreamDAO mediaAudioStreamDao;
	
	@Autowired
	private MediaTxtQuery mediaTxtQuery;
	
	@Autowired
	private MediaTxtService mediaTxtService;
	
	@Autowired
	private MediaTxtDAO mediaTxtDao;
	
	@Autowired
	private BusinessRoleQuery businessRoleQuery;
	
	@Autowired
	private BusinessRolePermissionQuery businessRolePermissionQuery;
	
	/**
	 * 新增私人文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午3:42:28
	 * @param String userId 创建文件夹的用户
	 * @param Long parentFolderId 父文件夹
	 * @param String folderName 文件夹名称
	 * @return FolderPO 新建的文件夹
	 * @exception FolderNotExistException 未查询到父文件夹
	 */
	public FolderPO addPersionalFolder(String userId, Long parentFolderId, String folderName, FolderType type) throws Exception{
		
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
	 * 新增媒资文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午1:32:48
	 * @param String companyId 公司id
	 * @param Long parentFolderId 父文件夹id
	 * @param String folderName 文件夹名称
	 * @param FolderType type 媒资文件夹类型
	 * @return FolderPO 新建的文件夹
	 */
	public FolderPO addMediaFolder(String userId, String companyId, Long parentFolderId, String folderName, FolderType type) throws Exception{
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
		
		FolderGroupPermissionPO groupPermission = new FolderGroupPermissionPO();
		groupPermission.setFolderId(folder.getId());
		groupPermission.setGroupId(companyId);
		groupPermission.setUpdateTime(new Date());
		folderGroupPermissionDao.save(groupPermission);
		
		SystemRoleVO roleAdmin = businessRoleQuery.findCompanyAdminRole();
		
		FolderRolePermissionPO adminPermission = new FolderRolePermissionPO();
		adminPermission.setFolderId(folder.getId());
		adminPermission.setRoleId(Long.valueOf(roleAdmin.getId()));
		adminPermission.setRoleName(roleAdmin.getName());
		adminPermission.setUpdateTime(new Date());
		folderRolePermissionDao.save(adminPermission);
		
		return folder;
	}
	
	/**
	 * 新增媒资文件夹绑定用户角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午1:32:48
	 * @param String companyId 公司id
	 * @param Long parentFolderId 父文件夹id
	 * @param String folderName 文件夹名称
	 * @param FolderType type 媒资文件夹类型
	 * @return FolderPO 新建的文件夹
	 */
	public FolderPO addMediaFolderBindRole(Long userId, String companyId, Long parentFolderId, String folderName, FolderType type) throws Exception{
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
		
		FolderGroupPermissionPO groupPermission = new FolderGroupPermissionPO();
		groupPermission.setFolderId(folder.getId());
		groupPermission.setGroupId(companyId);
		groupPermission.setUpdateTime(new Date());
		folderGroupPermissionDao.save(groupPermission);
		
		SystemRoleVO roleAdmin = businessRoleQuery.findCompanyAdminRole();
		
		FolderRolePermissionPO adminPermission = new FolderRolePermissionPO();
		adminPermission.setFolderId(folder.getId());
		adminPermission.setRoleId(Long.valueOf(roleAdmin.getId()));
		adminPermission.setRoleName(roleAdmin.getName());
		adminPermission.setUpdateTime(new Date());
		folderRolePermissionDao.save(adminPermission);
		
		if (userId != null) {
			Map<String, Object> permission = businessRolePermissionQuery.listByUserId(userId, 1, 10);
			if (permission != null && permission.containsKey("rows")) {
				Object obj = permission.get("rows");
				if (permission != null) {
					List<UserSystemRolePermissionVO> permissionVOs = JSONArray.parseArray(JSONObject.toJSONString(obj), UserSystemRolePermissionVO.class);
					if (!permissionVOs.isEmpty()) {
						UserSystemRolePermissionVO userRole = permissionVOs.get(0);
						FolderRolePermissionPO userPermission = new FolderRolePermissionPO();
						userPermission.setFolderId(folder.getId());
						userPermission.setRoleId(Long.valueOf(userRole.getRoleId()));
						userPermission.setRoleName(userRole.getRoleName());
						userPermission.setUpdateTime(new Date());
						folderRolePermissionDao.save(userPermission);
					}
				}
			}
		}
		
		return folder;
	}
	
	/**
	 * 删除素材库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 上午10:32:06
	 * @param FolderPO folder 待删除文件夹
	 * @param UserVO user 操作用户
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
	 * 删除媒资库文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 上午10:32:06
	 * @param FolderPO folder 待删除文件夹
	 * @param UserVO user 操作用户
	 */
	public void removeMediaFolder(FolderPO folder, UserVO user) throws Exception{
		
		//获取所有的子文件夹
		List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
		List<FolderPO> totalFolders = new ArrayListWrapper<FolderPO>().add(folder).getList();
		if(subFolders!=null && subFolders.size()>0) totalFolders.addAll(subFolders);
		
		//获取所有的文件夹id
		Set<Long> folderIds = new HashSet<Long>();
		for(FolderPO scope:totalFolders){
			folderIds.add(scope.getId());
		}
		
		//获取所有的组权限数据
		List<FolderGroupPermissionPO> permissions0 = folderGroupPermissionDao.findByFolderIdIn(folderIds);
		
		//获取所有的角色权限数据
		List<FolderRolePermissionPO> permissions1 = folderRolePermissionDao.findByFolderIdIn(folderIds);
		
		//获取图片媒资
		List<MediaPicturePO> pictures = mediaPictureDao.findByFolderIdIn(folderIds);
		
		//获取视频媒资
		List<MediaVideoPO> videos = mediaVideoDao.findByFolderIdIn(folderIds);
		
		//获取音频媒资
		List<MediaAudioPO> audios = mediaAudioDao.findByFolderIdIn(folderIds);
		
		//获取视频流媒资
		List<MediaVideoStreamPO> videoStreams = mediaVideoStreamDao.findByFolderIdIn(folderIds);
		
		//获取音频流媒资
		List<MediaAudioStreamPO> audioStreams = mediaAudioStreamDao.findByFolderIdIn(folderIds);
		
		//获取文本媒资
		List<MediaTxtPO> txts = mediaTxtDao.findByFolderIdIn(folderIds);
		
		//删除所有文件夹
		folderDao.deleteInBatch(totalFolders);
		
		//删除所有组权限
		folderGroupPermissionDao.deleteInBatch(permissions0);
		
		//删除所有角色权限
		folderRolePermissionDao.deleteInBatch(permissions1);
		
		//删除所有图片媒资
		if(pictures!=null && pictures.size()>0) mediaPictureService.remove(pictures);
		
		//删除所有视频媒资
		if(videos!=null && videos.size()>0) mediaVideoService.remove(videos);
		
		//删除所有音频媒资
		if(audios!=null && audios.size()>0) mediaAudioService.remove(audios);
		
		//删除所有视频流媒资
		if(videoStreams!=null && videoStreams.size()>0) mediaVideoStreamService.remove(videoStreams);
		
		//删除所有音频流媒资
		if(audioStreams!=null && audioStreams.size()>0) mediaAudioStreamService.remove(audioStreams);
		
		//删除所有文本媒资
		if(txts!=null && txts.size()>0) mediaTxtService.remove(txts);
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
	 * 复制素材文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午9:35:20
	 * @param FolderPO folder 待复制文件夹
	 * @param FolderPO target 目标文件夹
	 * @return boolean 是否复制到其他文件夹中
	 */
	public FolderPO copyMaterialFolder(String userId, FolderPO folder, FolderPO target) throws Exception{
		
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
		List<MaterialFilePO> materials = materialFileQuery.findMaterialsByFolderIds(totalFolderIds);
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
		setMaterialFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, materials, totalCopyMaterials);
		
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
	 * 复制媒资文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午9:35:20
	 * @param FolderPO folder 待复制文件夹
	 * @param FolderPO target 目标文件夹
	 * @return boolean 是否复制到其他文件夹中
	 */
	public FolderPO copyMediaFolder(String userId, String companyId, FolderPO folder, FolderPO target) throws Exception{
		
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
		
		//生成公司权限
		List<FolderGroupPermissionPO> permissions0 = new ArrayList<FolderGroupPermissionPO>();
		for(FolderPO copyFolder:totalCopyFolders){
			FolderGroupPermissionPO permission0 = new FolderGroupPermissionPO();
			permission0.setFolderId(copyFolder.getId());
			permission0.setGroupId(companyId);
			permission0.setUpdateTime(new Date());
			permissions0.add(permission0);
		}
		folderGroupPermissionDao.save(permissions0);
		
		//生成管理员权限
		SystemRoleVO roleAdmin = businessRoleQuery.findCompanyAdminRole();
		List<FolderRolePermissionPO> permissions1 = new ArrayList<FolderRolePermissionPO>();
		for(FolderPO copyFolder:totalCopyFolders){
			FolderRolePermissionPO permission1 = new FolderRolePermissionPO();
			permission1.setRoleId(Long.valueOf(roleAdmin.getId()));
			permission1.setRoleName(roleAdmin.getName());
			permission1.setFolderId(copyFolder.getId());
			permission1.setUpdateTime(new Date());
			permissions1.add(permission1);
		}
		folderRolePermissionDao.save(permissions1);
		
		List<FolderPO> rootFolders = new ArrayListWrapper<FolderPO>().add(folder).getList();
		List<FolderPO> rootCopyFolders = new ArrayListWrapper<FolderPO>().add(folderTool.loopByUuid(folder.getUuid(), totalCopyFolders)).getList();
		
		boolean resetFolderChain = false;
		
		//复制图片媒资
		List<MediaPicturePO> pictures = mediaPictureQuery.findCompleteByFolderIds(totalFolderIds);
		if(pictures!=null && pictures.size()>0){
			List<MediaPicturePO> totalCopyPictures = new ArrayList<MediaPicturePO>();
			for(MediaPicturePO picture:pictures){
				totalCopyPictures.add(picture.copy());
			}
			mediaPictureDao.save(totalCopyPictures);
			//重组文件夹链
			setMediaPictureFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, pictures, totalCopyPictures);
			//生成新的uuid
			for(MediaPicturePO picture:totalCopyPictures){
				picture.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			mediaPictureDao.save(totalCopyPictures);
			resetFolderChain = true;
		}
		
		//复制视频媒资
		List<MediaVideoPO> videos = mediaVideoQuery.findCompleteByFolderIds(totalFolderIds);
		if(videos!=null && videos.size()>0){
			List<MediaVideoPO> totalCopyVideos = new ArrayList<MediaVideoPO>();
			for(MediaVideoPO video:videos){
				totalCopyVideos.add(video.copy());
			}
			mediaVideoDao.save(totalCopyVideos);
			//重组文件夹链
			setMediaVideoFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, videos, totalCopyVideos);
			//生成新的uuid
			for(MediaVideoPO video:totalCopyVideos){
				video.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			mediaVideoDao.save(totalCopyVideos);
			resetFolderChain = true;
		}
		
		//复制音频媒资
		List<MediaAudioPO> audios = mediaAudioQuery.findCompleteByFolderIds(totalFolderIds);
		if(audios!=null && audios.size()>0){
			List<MediaAudioPO> totalCopyAudios = new ArrayList<MediaAudioPO>();
			for(MediaAudioPO audio:audios){
				totalCopyAudios.add(audio.copy());
			}
			mediaAudioDao.save(totalCopyAudios);
			//重组文件夹链
			setMediaAudioFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, audios, totalCopyAudios);
			//生成新的uuid
			for(MediaAudioPO audio:totalCopyAudios){
				audio.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			mediaAudioDao.save(totalCopyAudios);
			resetFolderChain = true;
		}
		
		//复制视频流媒资
		List<MediaVideoStreamPO> videoStreams = mediaVideoStreamQuery.findCompleteByFolderIds(totalFolderIds);
		if(videoStreams!=null && videoStreams.size()>0){
			List<MediaVideoStreamPO> totalCopyVideoStreams = new ArrayList<MediaVideoStreamPO>();
			for(MediaVideoStreamPO videoStream:videoStreams){
				totalCopyVideoStreams.add(videoStream.copy());
			}
			mediaVideoStreamDao.save(totalCopyVideoStreams);
			//重组文件夹链
			setMediaVideoStreamFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, videoStreams, totalCopyVideoStreams);
			//生成新的uuid
			for(MediaVideoStreamPO videoStream:totalCopyVideoStreams){
				videoStream.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			mediaVideoStreamDao.save(totalCopyVideoStreams);
			resetFolderChain = true;
		}
		
		//复制音频流媒资
		List<MediaAudioStreamPO> audioStreams = mediaAudioStreamQuery.findCompleteByFolderIds(totalFolderIds);
		if(audioStreams!=null && audioStreams.size()>0){
			List<MediaAudioStreamPO> totalCopyAudioStreams = new ArrayList<MediaAudioStreamPO>();
			for(MediaAudioStreamPO audioStream:audioStreams){
				totalCopyAudioStreams.add(audioStream.copy());
			}
			mediaAudioStreamDao.save(totalCopyAudioStreams);
			//重组文件夹链
			setMediaAudioStreamFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, audioStreams, totalCopyAudioStreams);
			//生成新的uuid
			for(MediaAudioStreamPO audioStream:totalCopyAudioStreams){
				audioStream.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			mediaAudioStreamDao.save(totalCopyAudioStreams);
			resetFolderChain = true;
		}
		
		//复制文本媒资
		List<MediaTxtPO> txts = mediaTxtQuery.findCompleteByFolderIds(totalFolderIds);
		if(txts!=null && txts.size()>0){
			List<MediaTxtPO> totalCopyTxts = new ArrayList<MediaTxtPO>();
			for(MediaTxtPO txt:txts){
				totalCopyTxts.add(txt.copy());
			}
			mediaTxtDao.save(totalCopyTxts);
			//重组文件夹链
			setMediaTxtFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders, txts, totalCopyTxts);
			//生成新的uuid
			for(MediaTxtPO txt:totalCopyTxts){
				txt.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			mediaTxtDao.save(totalCopyTxts);
			resetFolderChain = true;
		}
		
		if(!resetFolderChain){
			setFolderChain(rootFolders, rootCopyFolders, target, subFolders, totalCopyFolders);
		}
		
		//生成新的uuid
		for(FolderPO copyFolder:totalCopyFolders){
			copyFolder.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		
		//保存数据
		folderDao.save(totalCopyFolders);
		
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
	private void setMaterialFolderChain(
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
				setMaterialFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, materials, copiedMaterials);
			}
			
			//重组素材文件
			if(materials!=null && materials.size()>0){
				for(MaterialFilePO material:materials){
					if(material.getFolderId().equals(root.getId())){
						MaterialFilePO copiedMaterial = materialFileQuery.loopForUuid(material.getUuid(), copiedMaterials);
						copiedMaterial.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
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
	 */
	private void setFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders){
		
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
				setFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders);
			}
		}
	}
	
	/**
	 * 重组图片媒资文件夹链<br/>
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
	private void setMediaPictureFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MediaPicturePO> picutres,
			Collection<MediaPicturePO> copiedPictures){
		
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
				setMediaPictureFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, picutres, copiedPictures);
			}
			
			//重组素材文件
			if(picutres!=null && picutres.size()>0){
				for(MediaPicturePO picture:picutres){
					if(picture.getFolderId().equals(root.getId())){
						MediaPicturePO copiedPicture = mediaPictureQuery.loopForUuid(picture.getUuid(), copiedPictures);
						copiedPicture.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 重组视频媒资文件夹链<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:14:48
	 * @param Collection<FolderPO> roots 待重组的文件夹（复制前）
	 * @param Collection<FolderPO> copiedRoots 待重组的文件夹（复制后的）
	 * @param FolderPO parent 待重组文件夹的父文件夹
	 * @param Collection<FolderPO> copiedSubFolders 复制之后的子文件夹
	 * @param Collection<FolderPO> subFolders 原子文件夹
	 * @param Collection<MediaVideoPO> copiedVideos 复制之后的视频媒资
	 * @param Collection<MediaVideoPO> videos 原视频媒资
	 */
	private void setMediaVideoFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MediaVideoPO> videos,
			Collection<MediaVideoPO> copiedVideos){
		
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
				setMediaVideoFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, videos, copiedVideos);
			}
			
			//重组素材文件
			if(videos!=null && videos.size()>0){
				for(MediaVideoPO video:videos){
					if(video.getFolderId().equals(root.getId())){
						MediaVideoPO copiedVideo = mediaVideoQuery.loopForUuid(video.getUuid(), copiedVideos);
						copiedVideo.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 重组音频媒资文件夹链<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:14:48
	 * @param Collection<FolderPO> roots 待重组的文件夹（复制前）
	 * @param Collection<FolderPO> copiedRoots 待重组的文件夹（复制后的）
	 * @param FolderPO parent 待重组文件夹的父文件夹
	 * @param Collection<FolderPO> copiedSubFolders 复制之后的子文件夹
	 * @param Collection<FolderPO> subFolders 原子文件夹
	 * @param Collection<MediaAudioPO> copiedAudios 复制之后的音频媒资
	 * @param Collection<MediaAudioPO> videos 原音频媒资
	 */
	private void setMediaAudioFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MediaAudioPO> audios,
			Collection<MediaAudioPO> copiedAudios){
		
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
				setMediaAudioFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, audios, copiedAudios);
			}
			
			//重组音频媒资
			if(audios!=null && audios.size()>0){
				for(MediaAudioPO audio:audios){
					if(audio.getFolderId().equals(root.getId())){
						MediaAudioPO copiedAudio = mediaAudioQuery.loopForUuid(audio.getUuid(), copiedAudios);
						copiedAudio.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 重组视频流媒资文件夹链<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:14:48
	 * @param Collection<FolderPO> roots 待重组的文件夹（复制前）
	 * @param Collection<FolderPO> copiedRoots 待重组的文件夹（复制后的）
	 * @param FolderPO parent 待重组文件夹的父文件夹
	 * @param Collection<FolderPO> copiedSubFolders 复制之后的子文件夹
	 * @param Collection<FolderPO> subFolders 原子文件夹
	 * @param Collection<MediaVideoStreamPO> copiedVideoStreams 复制之后的视频流媒资
	 * @param Collection<MediaVideoStreamPO> videoStreams 原视频流媒资
	 */
	private void setMediaVideoStreamFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MediaVideoStreamPO> videoStreams,
			Collection<MediaVideoStreamPO> copiedVideoStreams){
		
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
				setMediaVideoStreamFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, videoStreams, copiedVideoStreams);
			}
			
			//重组视频流媒资
			if(videoStreams!=null && videoStreams.size()>0){
				for(MediaVideoStreamPO videoStream:videoStreams){
					if(videoStream.getFolderId().equals(root.getId())){
						MediaVideoStreamPO copiedVideoStream = mediaVideoStreamQuery.loopForUuid(videoStream.getUuid(), copiedVideoStreams);
						copiedVideoStream.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 重组音频流媒资文件夹链<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:14:48
	 * @param Collection<FolderPO> roots 待重组的文件夹（复制前）
	 * @param Collection<FolderPO> copiedRoots 待重组的文件夹（复制后的）
	 * @param FolderPO parent 待重组文件夹的父文件夹
	 * @param Collection<FolderPO> copiedSubFolders 复制之后的子文件夹
	 * @param Collection<FolderPO> subFolders 原子文件夹
	 * @param Collection<MediaAudioStreamPO> copiedAudioStreams 复制之后的音频流媒资
	 * @param Collection<MediaAudioStreamPO> audioStreams 原音频流媒资
	 */
	private void setMediaAudioStreamFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MediaAudioStreamPO> audioStreams,
			Collection<MediaAudioStreamPO> copiedAudioStreams){
		
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
				setMediaAudioStreamFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, audioStreams, copiedAudioStreams);
			}
			
			//重组视频流媒资
			if(audioStreams!=null && audioStreams.size()>0){
				for(MediaAudioStreamPO audioStream:audioStreams){
					if(audioStream.getFolderId().equals(root.getId())){
						MediaAudioStreamPO copiedAudioStream = mediaAudioStreamQuery.loopForUuid(audioStream.getUuid(), copiedAudioStreams);
						copiedAudioStream.setFolderId(copiedRoot.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 重组文本媒资文件夹链<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 下午1:14:48
	 * @param Collection<FolderPO> roots 待重组的文件夹（复制前）
	 * @param Collection<FolderPO> copiedRoots 待重组的文件夹（复制后的）
	 * @param FolderPO parent 待重组文件夹的父文件夹
	 * @param Collection<FolderPO> copiedSubFolders 复制之后的子文件夹
	 * @param Collection<FolderPO> subFolders 原子文件夹
	 * @param Collection<MediaTxtPO> copiedTxts 复制之后的文本媒资
	 * @param Collection<MediaTxtPO> txts 原文本媒资
	 */
	private void setMediaTxtFolderChain(
			Collection<FolderPO> roots,
			Collection<FolderPO> copiedRoots, 
			FolderPO parent,
			Collection<FolderPO> subFolders,
			Collection<FolderPO> copiedSubFolders,
			Collection<MediaTxtPO> txts,
			Collection<MediaTxtPO> copiedTxts){
		
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
				setMediaTxtFolderChain(filteredSubFolders, filteredCopiedSubFolders, copiedRoot, subFolders, copiedSubFolders, txts, copiedTxts);
			}
			
			//重组视频流媒资
			if(txts!=null && txts.size()>0){
				for(MediaTxtPO txt:txts){
					if(txt.getFolderId().equals(root.getId())){
						MediaTxtPO copiedTxt = mediaTxtQuery.loopForUuid(txt.getUuid(), copiedTxts);
						copiedTxt.setFolderId(copiedRoot.getId());
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
		//判重
		FolderPO existFolder = folderDao.findMaterialRootByUserId(userId);
		if(existFolder != null) return existFolder;
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
			Long roleId,
			String roleName) throws Exception{
		FolderPO folder = new FolderPO();
		folder.setName(new StringBufferWrapper().append(companyName).append("（").append(type.getName()).append("）").toString());
		folder.setType(type);
		folder.setUpdateTime(new Date());
		folder.setParentId(parentId);
		folder.setParentPath(parentPath);
		folder.setDepth();
		folderDao.save(folder);
		FolderGroupPermissionPO permission0 = new FolderGroupPermissionPO();
		permission0.setFolderId(folder.getId());
		permission0.setGroupId(companyId);
		permission0.setUpdateTime(new Date());
		folderGroupPermissionDao.save(permission0);
		FolderRolePermissionPO permission1 = new FolderRolePermissionPO();
		permission1.setFolderId(folder.getId());
		permission1.setRoleId(roleId);
		permission1.setRoleName(roleName);
		permission1.setAutoGeneration(true);
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
	 * 
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>2.0<br/>
	 * <b>日期：</b>2019年6月17日 下午1:48:36
	 * @param String companyId 公司id
	 * @param String companyName 公司名称
	 * @param String userId 用户id
	 * @param String roleId 管理员角色id 
	 * @param String roleName 管理员角色名称
	 */
	public void createCompanyDisk(String companyId, String companyName, String userId, String roleId, String roleName) throws Exception{
		//判重
		FolderPO existFolder = folderDao.findCompanyRootFolderByType(companyId, FolderType.COMPANY_PICTURE.toString());
		if(existFolder != null){
			return;
		}
		
		//企业根目录
		FolderPO root = createCompanyFolder(companyId, companyName, FolderType.COMPANY, null, null, Long.parseLong(roleId), roleName);
		
		String parentPath = new StringBufferWrapper().append("/").append(root.getId()).toString();
		
		//图片
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_PICTURE, root.getId(), parentPath, Long.parseLong(roleId), roleName);
		
		//视频
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_VIDEO, root.getId(), parentPath, Long.parseLong(roleId), roleName);
		
		//音频
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_AUDIO, root.getId(), parentPath, Long.parseLong(roleId), roleName);
		
		//视频流
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_VIDEO_STREAM, root.getId(), parentPath,Long.parseLong(roleId), roleName);
		
		//音频流
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_AUDIO_STREAM, root.getId(), parentPath, Long.parseLong(roleId), roleName);
		
		//push直播
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_PUSH_LIVE, root.getId(), parentPath, Long.parseLong(roleId), roleName);
		
		//文本
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_TXT, root.getId(), parentPath, Long.parseLong(roleId), roleName);
	
		//压缩文件
		createCompanyFolder(companyId, companyName, FolderType.COMPANY_COMPRESS, root.getId(), parentPath, Long.parseLong(roleId), roleName);
	}
	
}
