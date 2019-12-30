package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.date.DateUtil;
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
import com.sumavision.tetris.mims.app.media.tag.TagDAO;
import com.sumavision.tetris.mims.app.media.tag.TagPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 视频流流媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaVideoStreamQuery {

	@Autowired
	private MediaVideoStreamDAO mediaVideoStreamDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private MediaVideoStreamUrlRelationQuery mediaVideoStreamUrlRelationQuery;
	
	@Autowired
	private MediaVideoStreamUrlRelationDAO mediaVideoStreamUrlRelationDao;
	
	/**
	 * 根据文件夹id查询文件夹以及视频流媒资<br/>
	 * <p>
	 * 	-如果folderId是0：查询有权限的根目录，只返回目录列表
	 * 	-如果folderId不是0：查询当前文件夹下有权限的目录以及目录下所有的媒资
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:14:37
	 * @param UserVO user 用户
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaVideoStreamVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<MediaVideoStreamVO> rows = null;
		
		//处理根面包屑
		FolderBreadCrumbVO breadCrumb = new FolderBreadCrumbVO().setId(0l)
																.setUuid("0")
																.setName("根目录")
																.setType(FolderType.COMPANY_VIDEO_STREAM.toString());
		
		if(user.getBusinessRoles() == null){
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
		if(folderId.equals(0l)){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
			if(folders==null || folders.size()<=0){
				return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
			}
			List<FolderPO> rootFolders = folderQuery.findRoots(folders);
			rows = new ArrayList<MediaVideoStreamVO>();
			for(FolderPO folder:rootFolders){
				MediaVideoStreamVO row = new MediaVideoStreamVO().set(folder);
				rows.add(row);
			}
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}else{
			FolderPO current = folderDao.findOne(folderId);
			if(current == null) throw new FolderNotExistException(folderId);
			
			rows = new ArrayList<MediaVideoStreamVO>();
			
			//子文件夹
			List<FolderPO> folders = folderQuery.findPermissionCompanyFolderByParentIdOrderByNameAsc(current.getId());
			if(folders!=null && folders.size()>0){
				for(FolderPO folder:folders){
					MediaVideoStreamVO row = new MediaVideoStreamVO().set(folder);
					rows.add(row);
				}
			}
			
			//文件夹内音频
			List<MediaVideoStreamPO> videoStreams = mediaVideoStreamDao.findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorId(
					new ArrayListWrapper<Long>().add(current.getId()).getList(), 
					UploadStatus.COMPLETE.toString(), 
					new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList(),
					user.getId().toString());
			if(videoStreams!=null && videoStreams.size()>0){
				rows.addAll(getVOWithPreviewUrlFromPO(videoStreams));
			}
			
			FolderBreadCrumbVO subBreadCrumb = null;
			if(current.getParentPath() == null){
				subBreadCrumb = folderQuery.generateFolderBreadCrumb(new ArrayListWrapper<FolderPO>().add(current).getList());
			}else{
				List<Long> parentIds = JSON.parseArray(new StringBufferWrapper().append("[")
																			    .append(current.getParentPath().substring(1, current.getParentPath().length()).replaceAll("/", ","))
																			    .append("]")
																			    .toString(), Long.class);
				List<FolderPO> breadCrumbFolders = folderQuery.findPermissionCompanyFolderByIdIn(parentIds, FolderType.COMPANY_VIDEO_STREAM.toString());
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
	 * 根据协议获取列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月12日 下午4:55:49
	 * @param String uri 协议头
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 */
	public List<MediaVideoStreamVO> loadByProtocol(String uri) throws Exception {
		return loadByCondition(null, null, null, null, null, uri);
	}
	
	/**
	 * 加载所有的视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 上午11:24:24
	 * @return List<MediaVideoVO> 视频流媒资列表
	 */
	public List<MediaVideoStreamVO> loadAll() throws Exception{
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
		
		if (folderTree.isEmpty()) return new ArrayList<MediaVideoStreamVO>();
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaVideoStreamPO> videos = mediaVideoStreamDao.findByFolderIdIn(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaVideoStreamVO> medias = new ArrayList<MediaVideoStreamVO>();
		for(FolderPO root:roots){
			medias.add(new MediaVideoStreamVO().set(root));
		}
		
		packMediaVideoStreamTree(medias, folderTree, videos);
		
		return medias;
	}
	
	/**
	 * 生成媒资树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月31日 上午11:29:34
	 * @param roots 根
	 * @param folders 所有文件夹
	 * @param medias 所有视频媒资
	 */
	public void packMediaVideoStreamTree(List<MediaVideoStreamVO> roots, List<FolderPO> folders, List<MediaVideoStreamPO> medias) throws Exception{
		if(roots == null || roots.size() <= 0){
			return;
		}
		
		for(MediaVideoStreamVO root: roots){
			if(root.getType().equals(MediaVideoStreamItemType.FOLDER.toString())){
				if(root.getChildren() == null) root.setChildren(new ArrayList<MediaVideoStreamVO>());
				for(FolderPO folder: folders){
					if(folder.getParentId() != null && folder.getParentId().equals(root.getId())){
						root.getChildren().add(new MediaVideoStreamVO().set(folder));
					}
				}
				for(MediaVideoStreamPO media: medias){
					if(media.getFolderId() != null && media.getFolderId().equals(root.getId())){
						root.getChildren().add(new MediaVideoStreamVO().set(media).setPreviewUrl(mediaVideoStreamUrlRelationQuery.getAllUrlFromStreamId(media.getId())));
					}
				}
				if(root.getChildren().size() > 0){
					packMediaVideoStreamTree(root.getChildren(), folders, medias);
				}
			}
		}
	}
	
	/**
	 * 根据创建时间筛选<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:08:38
	 * @param Long startTime 筛选起始时间
	 * @param Long endTime 筛选终止时间
	 * @return List<MediaVideoStreamVO> 筛选结果
	 */
	public List<MediaVideoStreamVO> loadByCreateTime(Long startTime, Long endTime) throws Exception{
		return loadByCondition(null, null, DateUtil.format(DateUtil.getDateByMillisecond(startTime), DateUtil.dateTimePattern), DateUtil.format(DateUtil.getDateByMillisecond(endTime), DateUtil.dateTimePattern), null, null);
	}
	
	/**
	 * 根据条件查询媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月19日 下午3:17:30
	 * @param Long id 媒资id
	 * @param String name 名称(模糊匹配)
	 * @param String startTime updateTime起始查询
	 * @param Stinrg endTime updateTime终止查询
	 * @param Long tagId 标签id
	 * @return List<MediaVideoStreamVO> 查询结果
	 */
	public List<MediaVideoStreamVO> loadByCondition(Long id, String name, String startTime, String endTime, Long tagId) throws Exception{
		return loadByCondition(id, name, startTime, endTime, tagId, null);
	}
	
	public List<MediaVideoStreamVO> loadByCondition(Long id, String name, String startTime, String endTime, Long tagId, String uri) throws Exception {
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO_STREAM.toString());
		if (folderTree == null || folderTree.isEmpty()) return new ArrayList<MediaVideoStreamVO>();
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		TagPO tag = tagDAO.findByIdAndGroupId(tagId, user.getGroupId());
		String tagName = tag != null ? tag.getName() : null;
		
		List<MediaVideoStreamPO> medias = mediaVideoStreamDao.findByCondition(id, name, startTime, endTime, tagName, folderIds, uri, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		return getVOWithPreviewUrlFromPO(medias.stream().distinct().collect(Collectors.toList()));
	}
	
	/**
	 * 根据id查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 下午1:48:42
	 * @param Long id 视频流媒资id
	 * @return MediaVideoStreamVO 视频流媒资信息
	 */
	public MediaVideoStreamVO findById(Long id) throws Exception {
		MediaVideoStreamPO videoStreamPO = mediaVideoStreamDao.findOne(id);
		
		if (videoStreamPO == null) return null;
		
		return new MediaVideoStreamVO().set(videoStreamPO).setPreviewUrl(mediaVideoStreamUrlRelationDao.findUrlsByVideoStreamId(id));
	}
	
	/**
	 * 查询文件夹下上传完成的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 视频流媒资
	 */
	public List<MediaVideoStreamPO> findCompleteByFolderId(Long folderId){
		return mediaVideoStreamDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 视频流媒资
	 */
	public List<MediaVideoStreamPO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaVideoStreamDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的视频流媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaVideoStreamPO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaVideoStreamDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资视频流（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaVideoStreamPO> pictures 查找范围
	 * @return MediaVideoStreamPO 查找结果
	 */
	public MediaVideoStreamPO loopForUuid(String uuid, Collection<MediaVideoStreamPO> videoStreams){
		if(videoStreams==null || videoStreams.size()<=0) return null;
		for(MediaVideoStreamPO videoStream:videoStreams){
			if(videoStream.getUuid().equals(uuid)){
				return videoStream;
			}
		}
		return null;
	}
	
	/**
	 * 根据预览地址查询视频流<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午11:28:10
	 * @param Collection<String> previewUrls 预览地址列表
	 * @return List<MediaVideoStreamVO> 视频流列表
	 */
	public List<MediaVideoStreamVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		List<MediaVideoStreamPO> entities = mediaVideoStreamDao.findByPreviewUrlIn(previewUrls);
		return MediaVideoStreamVO.getConverter(MediaVideoStreamVO.class).convert(entities, MediaVideoStreamVO.class);
	}
	
	/**
	 * 批量获取previewUrl<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月12日 上午10:31:40
	 * @param List<MediaVideoStreamPO> videoStreams 视频流媒资列表
	 * @return List<MediaVideoStreamVO> 视频流媒资列表
	 */
	private List<MediaVideoStreamVO> getVOWithPreviewUrlFromPO(List<MediaVideoStreamPO> videoStreams) throws Exception {
		List<MediaVideoStreamVO> returnList = new ArrayList<MediaVideoStreamVO>();
		if (videoStreams == null || videoStreams.isEmpty()) return returnList;
		List<Long> videoStreamIds = videoStreams.stream().map(MediaVideoStreamPO::getId).collect(Collectors.toList());
		List<MediaVideoStreamUrlRelationPO> totalUrls = mediaVideoStreamUrlRelationDao.findByVideoStreamIdIn(videoStreamIds);
		for(MediaVideoStreamPO videoStream:videoStreams){
			List<String> urls = new ArrayList<String>();
			if(totalUrls!=null && totalUrls.size()>0){
				for(MediaVideoStreamUrlRelationPO url:totalUrls){
					if(url.getVideoStreamId().equals(videoStream.getId())){
						urls.add(url.getUrl());
					}
				}
			}
			MediaVideoStreamVO videoStreamVO = new MediaVideoStreamVO().set(videoStream)
																	   .setPreviewUrl(urls);
			returnList.add(videoStreamVO);
		}
		return returnList;
	}
}
