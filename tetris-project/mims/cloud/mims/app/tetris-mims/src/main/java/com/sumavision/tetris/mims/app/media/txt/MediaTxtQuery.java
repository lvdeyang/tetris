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
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.txt.exception.MediaTxtNotExistException;
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
		
		//TODO 权限校验
		
		if(folderId == null){
			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_TXT.toString());
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
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByParentId(user.getUuid(), folderId, FolderType.COMPANY_TXT.toString());
		
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
	
}
