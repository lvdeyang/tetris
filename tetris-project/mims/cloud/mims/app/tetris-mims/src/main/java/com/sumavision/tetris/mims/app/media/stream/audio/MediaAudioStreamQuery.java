package com.sumavision.tetris.mims.app.media.stream.audio;

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
 * 视频流流媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaAudioStreamQuery {

	@Autowired
	private MediaAudioStreamDAO mediaAudioStreamDao;
	
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
	public Map<String, Object> load(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		Long role = subordinateRoleQuery.queryRolesByUserId(user.getId());
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
			if (po.getType() == FolderType.COMPANY_AUDIO_STREAM) {
				permissFolders1.add(po);
			}
		}
		if(folderId == null){
			FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_AUDIO_STREAM.toString());
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
		
		List<FolderPO> folders = folderDao.findPermissionCompanyFoldersByRoleId(role.toString(), folderId, FolderType.COMPANY_AUDIO_STREAM.toString());
		
		List<MediaAudioStreamPO> videos = findCompleteByFolderId(current.getId());
		
		List<MediaAudioStreamVO> medias = new ArrayList<MediaAudioStreamVO>();
		if(folders!=null && folders.size()>0){
			for(FolderPO folder:folders){
				medias.add(new MediaAudioStreamVO().set(folder));
			}
		}
		if(videos!=null && videos.size()>0){
			for(MediaAudioStreamPO video:videos){
				medias.add(new MediaAudioStreamVO().set(video));
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("rows", medias)
																  		 .put("breadCrumb", folderBreadCrumb)
																  		 .getMap();
		
		return result;
	}
	
	/**
	 * 查询文件夹下上传完成的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 视频流媒资
	 */
	public List<MediaAudioStreamPO> findCompleteByFolderId(Long folderId){
		return mediaAudioStreamDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 视频流媒资
	 */
	public List<MediaAudioStreamPO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaAudioStreamDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的视频流媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaAudioStreamPO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaAudioStreamDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资视频流（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaAudioStreamPO> audioStreams 查找范围
	 * @return MediaAudioStreamPO 查找结果
	 */
	public MediaAudioStreamPO loopForUuid(String uuid, Collection<MediaAudioStreamPO> audioStreams){
		if(audioStreams==null || audioStreams.size()<=0) return null;
		for(MediaAudioStreamPO audioStream:audioStreams){
			if(audioStream.getUuid().equals(uuid)){
				return audioStream;
			}
		}
		return null;
	}
	
}
