package com.sumavision.tetris.mims.app.media.video;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
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
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private Path path;
	
	@Autowired
	private ServerProps serverProps;
	
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
		
		List<MediaVideoVO> rows = null;
		
		//处理根面包屑
		FolderBreadCrumbVO breadCrumb = new FolderBreadCrumbVO().setId(0l)
																.setUuid("0")
																.setName("根目录")
																.setType(FolderType.COMPANY_AUDIO.toString());
		
		if(user.getBusinessRoles() == null){
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
		if(folderId.equals(0l)){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
			if(folders==null || folders.size()<=0){
				return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
			}
			List<FolderPO> rootFolders = folderQuery.findRoots(folders);
			rows = new ArrayList<MediaVideoVO>();
			for(FolderPO folder:rootFolders){
				MediaVideoVO row = new MediaVideoVO().set(folder);
				rows.add(row);
			}
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}else{
			FolderPO current = folderDao.findOne(folderId);
			if(current == null) throw new FolderNotExistException(folderId);
			
			rows = new ArrayList<MediaVideoVO>();
			
			//子文件夹
			List<FolderPO> folders = folderQuery.findPermissionCompanyFolderByParentIdOrderByNameAsc(current.getId());
			if(folders!=null && folders.size()>0){
				for(FolderPO folder:folders){
					MediaVideoVO row = new MediaVideoVO().set(folder);
					rows.add(row);
				}
			}
			
			//文件夹内音频
			List<MediaVideoPO> videos = mediaVideoDao.findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorId(
					new ArrayListWrapper<Long>().add(current.getId()).getList(), 
					UploadStatus.COMPLETE.toString(), 
					new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList(),
					user.getId().toString());
			if(videos!=null && videos.size()>0){
				for(MediaVideoPO video:videos){
					rows.add(new MediaVideoVO().set(video));
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
				List<FolderPO> breadCrumbFolders = folderQuery.findPermissionCompanyFolderByIdIn(parentIds, FolderType.COMPANY_VIDEO.toString());
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
	 * 加载所有的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	public List<MediaVideoVO> loadAll(Long ... id) throws Exception{
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
		if (id != null && id.length > 0 && id[0] != null) {
			folderTree = folderQuery.findSubFolders(id[0]);
			folderTree.add(folderDao.findOne(id[0]));
		}
		
		if (folderTree.isEmpty()) return new ArrayList<MediaVideoVO>();
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaVideoPO> videos = mediaVideoDao.findByFolderIdIn(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaVideoVO> medias = new ArrayList<MediaVideoVO>();
		for(FolderPO root:roots){
			medias.add(new MediaVideoVO().set(root));
		}
		
		packMediaVideoTree(medias, folderTree, videos);
		
		return id != null && id.length > 0 ? medias.get(0).getChildren() : medias;
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaVideoVO
	 */
	public MediaVideoVO loadCollection(Long folderId) throws Exception {
		MediaVideoVO videoFolder = null;
		if (folderId != null) {
			List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
			for (FolderPO folderPO : folderTree) {
				if (folderPO.getId() == folderId) {
					videoFolder = new MediaVideoVO().set(folderPO);
					break;
				}
			}
			if (videoFolder != null) {
				Map<String, Object> map = load(folderId);
				List<MediaVideoVO> videoVOs = new ArrayList<MediaVideoVO>();
				if (map.containsKey("rows")) {
					videoVOs = JSONArray.parseArray(JSONObject.toJSONString(map.get("rows")), MediaVideoVO.class);
				}
				videoFolder.setChildren(videoVOs);
			}
		} else {
			List<MediaVideoVO> medias = loadAll();
			if (medias != null && !medias.isEmpty()) videoFolder = medias.get(0);
			List<MediaVideoVO> children = videoFolder.getChildren();
			for (MediaVideoVO mediaVideoVO : children) {
				mediaVideoVO.setChildren(null);
			}
		}
		return videoFolder;
	}
	
	/**
	 * 加载所有的视频媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	public List<MediaVideoVO> loadAllFolder() throws Exception{
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
		
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
	 * 根据创建时间筛选<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:08:38
	 * @param Long startTime 筛选起始时间
	 * @param Long endTime 筛选终止时间
	 * @return List<MediaVideoVO> 筛选结果
	 */
	public List<MediaVideoVO> loadByCreateTime(Long startTime, Long endTime) throws Exception{
		return loadByCondition(null, null, DateUtil.format(DateUtil.getDateByMillisecond(startTime), DateUtil.dateTimePattern), DateUtil.format(DateUtil.getDateByMillisecond(endTime), DateUtil.dateTimePattern), null);
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
	 * @return List<MediaVideoVO> 查询结果
	 */
	public List<MediaVideoVO> loadByCondition(Long id, String name, String startTime, String endTime, Long tagId) throws Exception{
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		TagPO tag = tagDAO.findByIdAndGroupId(tagId, user.getGroupId());
		String tagName = tag != null ? tag.getName() : null;
		
		List<MediaVideoPO> videos = mediaVideoDao.findByCondition(id, name, startTime, endTime, tagName, folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		return MediaVideoVO.getConverter(MediaVideoVO.class).convert(videos, MediaVideoVO.class);
	}
	
	/**
	 * 获取标签的视频媒资数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 */
	public void queryCountByTags(UserVO user, List<TagVO> tags) throws Exception{
		if (tags == null || tags.isEmpty()) return;
		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		for (TagVO tag : tags) {
			List<MediaVideoPO> childVideos = mediaVideoDao.findByFolderIdInAndTag(folderIds, tag.getName(), new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
			if (childVideos == null || childVideos.isEmpty()) continue;
			int num = tag.getSubMediaNum();
			tag.setSubMediaNum(num + childVideos.size());
		}
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
	 * @return MediaVideoPO 查找结果
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
	 * 根据uuid查找媒资视频<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 上午11:52:58
	 * @param List<String> uuids 视频uuids
	 * @return List<MediaVideoPO> 查找结果
	 */
	public List<MediaVideoPO> questByUuid(List<String> uuids){
		if(uuids==null || uuids.size()<=0) return null;
		return mediaVideoDao.findByUuidIn(uuids);
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
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	public String buildUrl(String name, String folderUuid) throws Exception {
		UserVO user = userQuery.current();
		
		String separator = File.separator;
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath)
												   .append("upload")
												   .append(separator)
												   .append("tmp")
												   .append(separator).append(user.getGroupName())
												   .append(separator).append(folderUuid)
												   .toString();
		Date date = new Date();
		String version = new StringBufferWrapper().append(MediaVideoPO.VERSION_OF_ORIGIN).append(".").append(date.getTime()).toString();
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(name).append(separator).append(version).toString();
		File file = new File(folderPath);
		if(!file.exists()) file.mkdirs();
		//这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		
		return new StringBufferWrapper().append("upload/tmp/")
												     .append(user.getGroupName())
												     .append("/")
												     .append(folderUuid)
												     .append("/")
												     .append(name)
												     .append("/")
												     .append(version)
												     .toString();
	}
	
	/**
	 * 根据预览地址查询视频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:49:17
	 * @param Collection<String> previewUrls 预览地址列表
	 * @return List<MediaVideoVO> 视频列表
	 */
	public List<MediaVideoVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		ArrayList<String> searchPreivewList = new ArrayList<String>();
		if (previewUrls == null || previewUrls.isEmpty()) return new ArrayList<MediaVideoVO>();
		String localUrl = new StringBufferWrapper()
				.append("http://")
				.append(serverProps.getFtpIp())
				.append(":")
				.append(serverProps.getPort())
				.append("/").toString();
		String netUrl = new StringBufferWrapper()
				.append("http://")
				.append(serverProps.getIp())
				.append(":")
				.append(serverProps.getPort())
				.append("/")
				.toString();
		for (String previewUrl : previewUrls) {
			if (previewUrl.startsWith(localUrl)){
				searchPreivewList.add(previewUrl.split(localUrl)[1]);
			} else if (previewUrl.startsWith(netUrl)) {
				searchPreivewList.add(previewUrl.split(netUrl)[1]);
			}
		}
		List<MediaVideoPO> entities = mediaVideoDao.findByPreviewUrlIn(previewUrls);
		return MediaVideoVO.getConverter(MediaVideoVO.class).convert(entities, MediaVideoVO.class);
	}
}
