package com.sumavision.tetris.mims.app.media.picture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.FolderBreadCrumbVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionDAO;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 图片媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaPictureQuery {

	@Autowired
	private MediaPictureDAO mediaPictureDao;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private UserQuery userQuery;

	@Autowired 
	private SubordinateRoleQuery subordinateRoleQuery;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDAO;
	/**
	 * 根据文件夹id查询文件夹以及图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:14:37
	 * @param UserVO user 用户
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaPictureVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		Long role = subordinateRoleQuery.queryRolesByUserId(user.getId());
		if (role == null) {
			return new HashMapWrapper<String, Object>().put("rows", new ArrayList<MediaPictureVO>())
			  		 .put("breadCrumb", new FolderBreadCrumbVO())
			  		 .getMap();
		}
		List<Long> folderIdsList = new ArrayList<Long>();
		List<FolderRolePermissionPO> list = folderRolePermissionDAO.findByRoleId(role);
		for (int j = 0; j < list.size(); j++) {
			folderIdsList.add(list.get(j).getFolderId());
		}
		//具有权限的文件夹
		List<FolderPO> permissFolders = folderDao.findByIdIn(folderIdsList);
		//按照文件夹类型过滤
		List<FolderPO> permissFolders1 = new ArrayList<FolderPO>();
		for (int i = 0; i < permissFolders.size(); i++) {
			FolderPO po = permissFolders.get(i);
			if (po.getType() == FolderType.COMPANY_PICTURE) {
				permissFolders1.add(po);
			}
		}		
		if(folderId == null){
			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_PICTURE.toString());
			folderId = folder.getId();
		}
		
		FolderPO current = folderDao.findOne(folderId);
		
		if(current == null) throw new FolderNotExistException(folderId);
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), current.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//获取当前文件夹的所有父目录
		List<FolderPO> parentFolders = folderQuery.getParentFolders(current);
		List<FolderPO> parentFolders1 = new ArrayList<FolderPO>();
		for (int i = 0; i < parentFolders.size(); i++) {
			FolderPO po = parentFolders.get(i);
			if (permissFolders1.contains(po)) {
				parentFolders1.add(po);
			}
		}
		List<FolderPO> filteredParentFolders = new ArrayList<FolderPO>();
		if(parentFolders1==null || parentFolders1.size()<=0){
			parentFolders = new ArrayList<FolderPO>();
		}
		for(FolderPO parentFolder:parentFolders1){
			if(!FolderType.COMPANY.equals(parentFolder.getType())){
				filteredParentFolders.add(parentFolder);
			}
		}
		filteredParentFolders.add(current);
		
		//生成面包屑数据
		FolderBreadCrumbVO folderBreadCrumb = folderQuery.generateFolderBreadCrumb(filteredParentFolders);
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByRoleId(role.toString(), folderId, FolderType.COMPANY_PICTURE.toString());
		
		List<MediaPicturePO> pictures = mediaPictureQuery.findCompleteByFolderId(current.getId());
		
		List<MediaPictureVO> medias = new ArrayList<MediaPictureVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaPictureVO().set(folder));
			}
		}
		if(pictures!=null && pictures.size()>0){
			for(MediaPicturePO picture:pictures){
				medias.add(new MediaPictureVO().set(picture));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
	
	
	/**
	 * 查询文件夹下上传完成的图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 图片媒资
	 */
	public List<MediaPicturePO> findCompleteByFolderId(Long folderId){
		return mediaPictureDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的图片媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 图片媒资
	 */
	public List<MediaPicturePO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaPictureDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的图片媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaPicturePO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaPictureDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资图片（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaPicturePO> pictures 查找范围
	 * @return MediaPicturePO 查找结果
	 */
	public MediaPicturePO loopForUuid(String uuid, Collection<MediaPicturePO> pictures){
		if(pictures==null || pictures.size()<=0) return null;
		for(MediaPicturePO picture:pictures){
			if(picture.getUuid().equals(uuid)){
				return picture;
			}
		}
		return null;
	}
	
	/**
	 * 根据文件夹id查询文件夹以及图片媒资（供android使用）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月6日 下午5:14:37
	 * @param UserVO user 用户
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaPictureVO> 媒资项目列表
	 * @return breadCrumb List<FolderBreadCrumbVO> 面包屑数据
	 */
	public Map<String, Object> loadForAndroid(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(folderId == null){
			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_PICTURE.toString());
			folderId = folder.getId();
		}
		
		FolderPO current = folderDao.findOne(folderId);
		
		if(current == null) throw new FolderNotExistException(folderId);
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), current.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//获取当前文件夹的所有父目录
		List<FolderPO> parentFolders = folderQuery.getParentFolders(current);
		
		List<FolderPO> filteredParentFolders = new ArrayList<FolderPO>();
		if(parentFolders==null || parentFolders.size()<=0){
			parentFolders = new ArrayList<FolderPO>();
		}
		for(FolderPO parentFolder:parentFolders){
			if(!FolderType.COMPANY.equals(parentFolder.getType())){
				filteredParentFolders.add(parentFolder);
			}
		}
		filteredParentFolders.add(current);
		
		//生成面包屑数据
		List<FolderBreadCrumbVO> folderBreadCrumb = folderQuery.generateFolderBreadCrumbForAndroid(filteredParentFolders);
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByParentId(user.getUuid(), folderId, FolderType.COMPANY_PICTURE.toString());
		
		List<MediaPicturePO> pictures = mediaPictureQuery.findCompleteByFolderId(current.getId());
		
		List<MediaPictureVO> medias = new ArrayList<MediaPictureVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaPictureVO().set(folder));
			}
		}
		if(pictures!=null && pictures.size()>0){
			for(MediaPicturePO picture:pictures){
				medias.add(new MediaPictureVO().set(picture));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
}
