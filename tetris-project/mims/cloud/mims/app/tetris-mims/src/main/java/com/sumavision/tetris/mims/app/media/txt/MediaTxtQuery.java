package com.sumavision.tetris.mims.app.media.txt;

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
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
import com.sumavision.tetris.mims.app.media.video.MediaVideoItemType;
import com.sumavision.tetris.subordinate.role.SubordinateRoleQuery;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 文本流媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaTxtQuery {

	@Autowired
	private MediaTxtDAO mediaTxtDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired 
	private SubordinateRoleQuery subordinateRoleQuery;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDAO;
	/**
	 * 加载文件夹下的文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaTxtVO> 文本媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		Long role = subordinateRoleQuery.queryRolesByUserId(user.getId());
		if (role == null) {
			return new HashMapWrapper<String, Object>().put("rows", new ArrayList<MediaTxtVO>())
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
			if (po.getType() == FolderType.COMPANY_TXT) {
				permissFolders1.add(po);
			}
		}
		if(folderId == null){
//			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_TXT.toString());
//			folderId = folder.getId();
			
			if (permissFolders1.size()>1) {
				for (int i = 0; i < permissFolders1.size(); i++) {
					for (int j = 0; j < permissFolders1.size()-i-1; j++) {
						FolderPO po = permissFolders1.get(j);
						Integer depth = po.getDepth();
						FolderPO po1 = permissFolders1.get(j+1);
						Integer depth1 = po1.getDepth();
						if (depth>depth1) {
							permissFolders1.set(j, po1);
							permissFolders1.set(j+1, po);
						}
					}
				}
				folderId = permissFolders1.get(0).getId();
			}else if (permissFolders1.size()==1) {
				folderId = permissFolders1.get(0).getId();
			} else {
				
			}
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
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByRoleId(role.toString(), folderId, FolderType.COMPANY_TXT.toString());
		
		List<MediaTxtPO> txts = findCompleteByFolderId(current.getId());
		
		List<MediaTxtVO> medias = new ArrayList<MediaTxtVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaTxtVO().set(folder));
			}
		}
		if(txts!=null && txts.size()>0){
			for(MediaTxtPO txt:txts){
				medias.add(new MediaTxtVO().set(txt));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
	
	/**
	 * 加载所有的文本媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaTxtVO> 文本媒资列表
	 */
	public List<MediaTxtVO> loadAll() throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		Long roleId = subordinateRoleQuery.queryRolesByUserId(Long.parseLong(user.getUuid()));
		List<FolderPO> folderTree = folderDao.findPermissionCompanyTree(roleId, FolderType.COMPANY_TXT.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaTxtPO> txts = mediaTxtDao.findByFolderIdIn(folderIds);
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		
		List<MediaTxtVO> medias = new ArrayList<MediaTxtVO>();
		
		for(FolderPO root:roots){
			medias.add(new MediaTxtVO().set(root));
		}
		
		packMediaTxtTree(medias, folderTree, txts);
		
		return medias;
	}
	
	/**
	 * 生成文本媒资树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月31日 上午11:29:34
	 * @param List<MediaAudioVO> roots 根
	 * @param List<FolderPO> folders 所有文件夹
	 * @param List<MediaAudioPO> medias 所有视频媒资
	 */
	public void packMediaTxtTree(List<MediaTxtVO> roots, List<FolderPO> folders, List<MediaTxtPO> medias) throws Exception{
		if(roots == null || roots.size() <= 0){
			return;
		}
		for(MediaTxtVO root: roots){
			if(root.getType().equals(MediaVideoItemType.FOLDER.toString())){
				if(root.getChildren() == null) root.setChildren(new ArrayList<MediaTxtVO>());
				for(FolderPO folder: folders){
					if(folder.getParentId() != null && folder.getParentId().equals(root.getId())){
						root.getChildren().add(new MediaTxtVO().set(folder));
					}
				}
				for(MediaTxtPO media: medias){
					if(media.getFolderId() != null && media.getFolderId().equals(root.getId())){
						root.getChildren().add(new MediaTxtVO().set(media));
					}
				}
				if(root.getChildren().size() > 0){
					packMediaTxtTree(root.getChildren(), folders, medias);
				}
			}
		}
	}
	
	/**
	 * 查询文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月14日 上午9:25:28
	 * @param @PathVariable Long id 媒资id
	 * @return String 文本内容
	 */
	public String queryContent(Long id) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		MediaTxtPO txt = mediaTxtDao.findOne(id);
		
		if(txt == null){
			throw new MediaTxtNotExistException(id);
		}
		
		return txt.getContent();
	}
	
	/**
	 * 查询文件夹下上传完成的文本媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 文本媒资
	 */
	public List<MediaTxtPO> findCompleteByFolderId(Long folderId){
		return mediaTxtDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的文本媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 文本媒资
	 */
	public List<MediaTxtPO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaTxtDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的文本媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaTxtPO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaTxtDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资文本（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 文本uuid
	 * @param Collection<MediaTxtPO> txts 查找范围
	 * @return MediaTxtPO 查找结果
	 */
	public MediaTxtPO loopForUuid(String uuid, Collection<MediaTxtPO> txts){
		if(txts==null || txts.size()<=0) return null;
		for(MediaTxtPO txt:txts){
			if(txt.getUuid().equals(uuid)){
				return txt;
			}
		}
		return null;
	}
	
	/**
	 * 加载文件夹下的文本媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaTxtVO> 文本媒资列表
	 * @return breadCrumb List<FolderBreadCrumbVO> 面包屑数据
	 */
	public Map<String, Object> loadForAndroid(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		Long role = subordinateRoleQuery.queryRolesByUserId(user.getId());
		if (role == null) {
			return new HashMapWrapper<String, Object>().put("rows", new ArrayList<MediaTxtVO>())
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
			if (po.getType() == FolderType.COMPANY_TXT) {
				permissFolders1.add(po);
			}
		}
		if(folderId == null){
//			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_TXT.toString());
//			folderId = folder.getId();
			
			if (permissFolders1.size()>1) {
				for (int i = 0; i < permissFolders1.size(); i++) {
					for (int j = 0; j < permissFolders1.size()-i-1; j++) {
						FolderPO po = permissFolders1.get(j);
						Integer depth = po.getDepth();
						FolderPO po1 = permissFolders1.get(j+1);
						Integer depth1 = po1.getDepth();
						if (depth>depth1) {
							permissFolders1.set(j, po1);
							permissFolders1.set(j+1, po);
						}
					}
				}
				folderId = permissFolders1.get(0).getId();
			}else if (permissFolders1.size()==1) {
				folderId = permissFolders1.get(0).getId();
			} else {
				
			}
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
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByRoleId(role.toString(), folderId, FolderType.COMPANY_TXT.toString());
		
		List<MediaTxtPO> txts = findCompleteByFolderId(current.getId());
		
		List<MediaTxtVO> medias = new ArrayList<MediaTxtVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaTxtVO().set(folder));
			}
		}
		if(txts!=null && txts.size()>0){
			for(MediaTxtPO txt:txts){
				medias.add(new MediaTxtVO().set(txt).setContent(null));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
}
