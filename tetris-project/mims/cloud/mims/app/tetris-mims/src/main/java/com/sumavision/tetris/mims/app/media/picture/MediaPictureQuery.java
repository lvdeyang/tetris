package com.sumavision.tetris.mims.app.media.picture;

import java.util.ArrayList;
import java.util.Collection;
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
import com.sumavision.tetris.mims.app.media.video.MediaVideoItemType;
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
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TagDAO tagDAO;

	/**
	 * 根据文件夹id查询文件夹以及图片媒资<br/>
	 * <p>
	 * 	-如果folderId是0：查询有权限的根目录，只返回目录列表
	 * 	-如果folderId不是0：查询当前文件夹下有权限的目录以及目录下所有的媒资
	 * </p>
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
		
		List<MediaPictureVO> rows = null;
		
		//处理根面包屑
		FolderBreadCrumbVO breadCrumb = new FolderBreadCrumbVO().setId(0l)
																.setUuid("0")
																.setName("根目录")
																.setType(FolderType.COMPANY_PICTURE.toString());
		
		if(user.getBusinessRoles() == null){
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
		if(folderId.equals(0l)){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
			if(folders==null || folders.size()<=0){
				return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
			}
			List<FolderPO> rootFolders = folderQuery.findRoots(folders);
			rows = new ArrayList<MediaPictureVO>();
			for(FolderPO folder:rootFolders){
				MediaPictureVO row = new MediaPictureVO().set(folder);
				rows.add(row);
			}
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}else{
			FolderPO current = folderDao.findOne(folderId);
			if(current == null) throw new FolderNotExistException(folderId);
			
			rows = new ArrayList<MediaPictureVO>();
			
			//子文件夹
			List<FolderPO> folders = folderQuery.findPermissionCompanyFolderByParentIdOrderByNameAsc(current.getId());
			if(folders!=null && folders.size()>0){
				for(FolderPO folder:folders){
					MediaPictureVO row = new MediaPictureVO().set(folder);
					rows.add(row);
				}
			}
			
			//文件夹内图片
			List<MediaPicturePO> pictures = mediaPictureDao.findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorId(
					new ArrayListWrapper<Long>().add(current.getId()).getList(), 
					UploadStatus.COMPLETE.toString(), 
					new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList(),
					user.getId().toString());
			if(pictures!=null && pictures.size()>0){
				for(MediaPicturePO picture:pictures){
					rows.add(new MediaPictureVO().set(picture));
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
				List<FolderPO> breadCrumbFolders = folderQuery.findPermissionCompanyFolderByIdIn(parentIds, FolderType.COMPANY_PICTURE.toString());
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
	 * 加载所有的图片媒资--树形<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月27日 上午9:48:20
	 * @return List<MediaPictureVO>
	 * @throws Exception
	 */
	public List<MediaPictureVO> loadAll(Long ... id) throws Exception{
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
		if (id != null && id.length > 0 && id[0] != null) {
			folderTree = folderQuery.findSubFolders(id[0]);
			folderTree.add(folderDao.findOne(id[0]));
		}
		
		List<Long> folderIds = new ArrayList<Long>();
		folderIds.add(-1l);
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaPicturePO> pictures = mediaPictureDao.findByFolderIdIn(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaPictureVO> medias = new ArrayList<MediaPictureVO>();
		for(FolderPO root:roots){
			medias.add(new MediaPictureVO().set(root));
		}
		
		packMediaVideoTree(medias, folderTree, pictures);
		
		return id != null && id.length > 0 ? medias.get(0).getChildren() : medias;
	}
	
	/**
	 * 加载所有的图片媒资--表行<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月25日 下午6:22:10
	 * @param id
	 * @return List<MediaPictureVO> 
	 */
	public List<MediaPictureVO> loadAllList(Long ... id) throws Exception{
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
		if (id != null && id.length > 0 && id[0] != null) {
			folderTree = folderQuery.findSubFolders(id[0]);
			folderTree.add(folderDao.findOne(id[0]));
		}
		
		List<Long> folderIds = new ArrayList<Long>();
		folderIds.add(-1l);
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaPicturePO> pictures = mediaPictureDao.findByFolderIdIn(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		List<MediaPictureVO> vos = new ArrayList<MediaPictureVO>();
		for(MediaPicturePO picture: pictures){
			vos.add(new MediaPictureVO().set(picture));
		}
		
		return vos;
	}
	
	/**
	 * 生成媒资图片树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月31日 上午11:29:34
	 * @param roots 根
	 * @param folders 所有文件夹
	 * @param medias 所有视频媒资
	 */
	public void packMediaVideoTree(List<MediaPictureVO> roots, List<FolderPO> folders, List<MediaPicturePO> medias) throws Exception{
		if(roots == null || roots.size() <= 0){
			return;
		}
		
		for(MediaPictureVO root: roots){
			if(root.getType().equals(MediaVideoItemType.FOLDER.toString())){
				if(root.getChildren() == null) root.setChildren(new ArrayList<MediaPictureVO>());
				for(FolderPO folder: folders){
					if(folder.getParentId() != null && folder.getParentId().equals(root.getId())){
						root.getChildren().add(new MediaPictureVO().set(folder));
					}
				}
				for(MediaPicturePO media: medias){
					if(media.getFolderId() != null && media.getFolderId().equals(root.getId())){
						root.getChildren().add(new MediaPictureVO().set(media));
					}
				}
				if(root.getChildren().size() > 0){
					packMediaVideoTree(root.getChildren(), folders, medias);
				}
			}
		}
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaPictureVO
	 */
	public MediaPictureVO loadPictureCollection(Long folderId) throws Exception {
		MediaPictureVO pictureFolder = null;
		if (folderId != null) {
			List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
			for (FolderPO folderPO : folderTree) {
				if (folderPO.getId() == folderId) {
					pictureFolder = new MediaPictureVO().set(folderPO);
					break;
				}
			}
			if (pictureFolder != null) {
				Map<String, Object> map = load(folderId);
				List<MediaPictureVO> pictureVOs = new ArrayList<MediaPictureVO>();
				if (map.containsKey("rows")) {
					pictureVOs = JSONArray.parseArray(JSONObject.toJSONString(map.get("rows")), MediaPictureVO.class);
				}
				pictureFolder.setChildren(pictureVOs);
			}
		} else {
			List<MediaPictureVO> medias = loadAll();
			if (medias != null && !medias.isEmpty()) pictureFolder = medias.get(0);
			List<MediaPictureVO> children = pictureFolder.getChildren();
			for (MediaPictureVO mediaPictureVO : children) {
				mediaPictureVO.setChildren(null);
			}
		}
		return pictureFolder;
	}
	
	/**
	 * 根据创建时间筛选<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:08:38
	 * @param Long startTime 筛选起始时间
	 * @param Long endTime 筛选终止时间
	 * @return List<MediaPictureVO> 筛选结果
	 */
	public List<MediaPictureVO> loadByCreateTime(Long startTime, Long endTime) throws Exception{
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
	 * @return List<MediaAudioVO> 查询结果
	 */
	public List<MediaPictureVO> loadByCondition(Long id, String name, String startTime, String endTime, Long tagId) throws Exception{
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		TagPO tag = tagDAO.findByIdAndGroupId(tagId, user.getGroupId());
		String tagName = tag != null ? tag.getName() : null;
		
		List<MediaPicturePO> pictures = mediaPictureDao.findByCondition(id, name, startTime, endTime, tagName, folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		return MediaPictureVO.getConverter(MediaPictureVO.class).convert(pictures, MediaPictureVO.class);
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
	 * 根据预览地址查询图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:34:58
	 * @param Collection<String> preivewUrls 预览地址列表
	 * @return List<MediaPictureVO> 图片列表
	 */
	public List<MediaPictureVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		List<MediaPicturePO> entities = mediaPictureDao.findByPreviewUrlIn(previewUrls);
		return MediaPictureVO.getConverter(MediaPictureVO.class).convert(entities, MediaPictureVO.class);
	}
	
}
