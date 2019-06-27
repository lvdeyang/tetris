package com.sumavision.tetris.mims.app.media.video;

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
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 图片媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaVideoQuery {

	@Autowired
	private MediaVideoDAO mediaVideoDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	/**
	 * 加载文件夹下的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoVO> 视频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(folderId == null){
			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_VIDEO.toString());
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
		FolderBreadCrumbVO folderBreadCrumb = folderQuery.generateFolderBreadCrumb(filteredParentFolders);
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByParentId(user.getUuid(), folderId, FolderType.COMPANY_VIDEO.toString());
		
		List<MediaVideoPO> videos = findCompleteByFolderId(current.getId());
		
		List<MediaVideoVO> medias = new ArrayList<MediaVideoVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaVideoVO().set(folder));
			}
		}
		if(videos!=null && videos.size()>0){
			for(MediaVideoPO video:videos){
				medias.add(new MediaVideoVO().set(video));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
	
	/**
	 * 加载所有的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	public List<MediaVideoVO> loadAll() throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderDao.findPermissionCompanyTree(user.getUuid(), FolderType.COMPANY_VIDEO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaVideoPO> videos = mediaVideoDao.findByFolderIdIn(folderIds);
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaVideoVO> medias = new ArrayList<MediaVideoVO>();
		for(FolderPO root:roots){
			medias.add(new MediaVideoVO().set(root));
		}
		
		packMediaVideoTree(medias, folderTree, videos);
		
		return medias;
	}
	
	/**
	 * 加载所有的视频媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	public List<MediaVideoVO> loadAllFolder() throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderDao.findPermissionCompanyTree(user.getUuid(), FolderType.COMPANY_VIDEO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaVideoVO> medias = new ArrayList<MediaVideoVO>();
		for(FolderPO root:roots){
			medias.add(new MediaVideoVO().set(root));
		}
		
		packMediaVideoTree(medias, folderTree, new ArrayList<MediaVideoPO>());
		
		return medias;
	}
	
	/**
	 * 查询文件夹下上传完成的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 视频媒资
	 */
	public List<MediaVideoPO> findCompleteByFolderId(Long folderId){
		return mediaVideoDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的视频媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 视频媒资
	 */
	public List<MediaVideoPO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaVideoDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的视频媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaVideoPO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaVideoDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资视频（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaVideoPO> pictures 查找范围
	 * @return MediaPicturePO 查找结果
	 */
	public MediaVideoPO loopForUuid(String uuid, Collection<MediaVideoPO> videos){
		if(videos==null || videos.size()<=0) return null;
		for(MediaVideoPO video:videos){
			if(video.getUuid().equals(uuid)){
				return video;
			}
		}
		return null;
	}
	
	/**
	 * 生成媒资视频树<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月31日 上午11:29:34
	 * @param roots 根
	 * @param folders 所有文件夹
	 * @param medias 所有视频媒资
	 */
	public void packMediaVideoTree(List<MediaVideoVO> roots, List<FolderPO> folders, List<MediaVideoPO> medias) throws Exception{
		if(roots == null || roots.size() <= 0){
			return;
		}
		
		for(MediaVideoVO root: roots){
			if(root.getType().equals(MediaVideoItemType.FOLDER.toString())){
				if(root.getChildren() == null) root.setChildren(new ArrayList<MediaVideoVO>());
				for(FolderPO folder: folders){
					if(folder.getParentId() != null && folder.getParentId().equals(root.getId())){
						root.getChildren().add(new MediaVideoVO().set(folder));
					}
				}
				for(MediaVideoPO media: medias){
					if(media.getFolderId() != null && media.getFolderId().equals(root.getId())){
						root.getChildren().add(new MediaVideoVO().set(media));
					}
				}
				if(root.getChildren().size() > 0){
					packMediaVideoTree(root.getChildren(), folders, medias);
				}
			}
		}
	}
	
	/**
	 * 加载文件夹下的视频媒资（供采集终端使用）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoVO> 视频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> loadForAndroid(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(folderId == null){
			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_VIDEO.toString());
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
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByParentId(user.getUuid(), folderId, FolderType.COMPANY_VIDEO.toString());
		
		List<MediaVideoPO> videos = findCompleteByFolderId(current.getId());
		
		List<MediaVideoVO> medias = new ArrayList<MediaVideoVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaVideoVO().set(folder));
			}
		}
		if(videos!=null && videos.size()>0){
			for(MediaVideoPO video:videos){
				medias.add(new MediaVideoVO().set(video));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
}
