package com.sumavision.tetris.mims.app.media.txt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderBreadCrumbVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
import com.sumavision.tetris.mims.app.media.video.MediaVideoItemType;
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
		
		List<MediaTxtVO> rows = null;
		
		//处理根面包屑
		FolderBreadCrumbVO breadCrumb = new FolderBreadCrumbVO().setId(0l)
																.setUuid("0")
																.setName("根目录")
																.setType(FolderType.COMPANY_TXT.toString());
		
		if(user.getBusinessRoles() == null){
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
		if(folderId.equals(0l)){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_TXT.toString());
			if(folders==null || folders.size()<=0){
				return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
			}
			List<FolderPO> rootFolders = folderQuery.findRoots(folders);
			rows = new ArrayList<MediaTxtVO>();
			for(FolderPO folder:rootFolders){
				MediaTxtVO row = new MediaTxtVO().set(folder);
				rows.add(row);
			}
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}else{
			FolderPO current = folderDao.findOne(folderId);
			if(current == null) throw new FolderNotExistException(folderId);
			
			rows = new ArrayList<MediaTxtVO>();
			
			//子文件夹
			List<FolderPO> folders = folderQuery.findPermissionCompanyFolderByParentIdOrderByNameAsc(current.getId());
			if(folders!=null && folders.size()>0){
				for(FolderPO folder:folders){
					MediaTxtVO row = new MediaTxtVO().set(folder);
					rows.add(row);
				}
			}
			
			//文件夹内文本
			List<MediaTxtPO> txtes = mediaTxtDao.findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorId(
					new ArrayListWrapper<Long>().add(current.getId()).getList(), 
					UploadStatus.COMPLETE.toString(), 
					new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList(),
					user.getId().toString());
			if(txtes!=null && txtes.size()>0){
				for(MediaTxtPO txt:txtes){
					rows.add(new MediaTxtVO().set(txt));
				}
			}
			
			FolderBreadCrumbVO subBreadCrumb = null;
			if(current.getParentPath() == null){
				subBreadCrumb = folderQuery.generateFolderBreadCrumb(new ArrayListWrapper<FolderPO>().add(current).getList());
			}else{
				List<Long> parentIds = JSON.parseArray(new StringBufferWrapper().append("[")
																			    .append(current.getParentPath().substring(1, current.getParentPath().length()).replaceAll("/", ","))
																			    .append("]")
																			    .toString(), Long.class);
				List<FolderPO> breadCrumbFolders = folderQuery.findPermissionCompanyFolderByIdIn(parentIds, FolderType.COMPANY_TXT.toString());
				if(breadCrumbFolders == null){
					breadCrumbFolders = new ArrayList<FolderPO>();
				}
				breadCrumbFolders.add(current);
				subBreadCrumb = folderQuery.generateFolderBreadCrumb(breadCrumbFolders);
			}
			breadCrumb.setNext(subBreadCrumb);
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
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
		
		String businessRoles = user.getBusinessRoles();
		
		if(businessRoles == null){
			return null;
		}else{
			List<Long> roleIds = JSON.parseArray(businessRoles, Long.class);
			List<FolderPO> folderTree = folderDao.findPermissionCompanyTree(roleIds, FolderType.COMPANY_TXT.toString());
			
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
	
}
