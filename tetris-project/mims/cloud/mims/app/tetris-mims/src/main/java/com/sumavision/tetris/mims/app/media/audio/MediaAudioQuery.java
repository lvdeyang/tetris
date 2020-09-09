package com.sumavision.tetris.mims.app.media.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
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
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO.MediaAudioHotOrderComparator;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mims.app.media.encode.AudioFileEncodePO;
import com.sumavision.tetris.mims.app.media.encode.AudioFileEncodeQuery;
import com.sumavision.tetris.mims.app.media.tag.TagDAO;
import com.sumavision.tetris.mims.app.media.tag.TagDownloadPermissionDAO;
import com.sumavision.tetris.mims.app.media.tag.TagDownloadPermissionPO;
import com.sumavision.tetris.mims.app.media.tag.TagPO;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoItemType;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 音频媒资查询操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月28日 上午10:38:08
 */
@Component
public class MediaAudioQuery {

	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private AudioFileEncodeQuery audioFileEncodeQuery;
	
	@Autowired
	private TagDownloadPermissionDAO tagDownloadPermissionDAO;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private ServerProps serverProps;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery;
	
	@Autowired
	private Path path;
	
	/**
	 * 根据文件夹id查询文件夹以及音频媒资<br/>
	 * <p>
	 * 	-如果folderId是0：查询有权限的根目录，只返回目录列表
	 * 	-如果folderId不是0：查询当前文件夹下有权限的目录以及目录下所有的媒资
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:14:37
	 * @param UserVO user 用户
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaAudioVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{
		
		UserVO user = userQuery.current();
		
		List<MediaAudioVO> rows = null;
		
		//处理根面包屑
		FolderBreadCrumbVO breadCrumb = new FolderBreadCrumbVO().setId(0l)
																.setUuid("0")
																.setName("根目录")
																.setType(FolderType.COMPANY_AUDIO.toString());
		
		if(user.getBusinessRoles() == null){
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
		if(folderId.equals(0l)){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
			if(folders==null || folders.size()<=0){
				return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
			}
			List<FolderPO> rootFolders = folderQuery.findRoots(folders);
			rows = new ArrayList<MediaAudioVO>();
			for(FolderPO folder:rootFolders){
				MediaAudioVO row = new MediaAudioVO().set(folder);
				rows.add(row);
			}
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}else{
			FolderPO current = folderDao.findOne(folderId);
			if(current == null) throw new FolderNotExistException(folderId);
			
			rows = new ArrayList<MediaAudioVO>();
			
			//子文件夹
			List<FolderPO> folders = folderQuery.findPermissionCompanyFolderByParentIdOrderByNameAsc(current.getId());
			if(folders!=null && folders.size()>0){
				for(FolderPO folder:folders){
					MediaAudioVO row = new MediaAudioVO().set(folder);
					rows.add(row);
				}
			}
			
			//文件夹内音频
			List<MediaAudioPO> audios = mediaAudioDao.findByFolderIdInAndUploadStatusAndReviewStatusNotInOrAuthorIdOrderByDesc(
					new ArrayListWrapper<Long>().add(current.getId()).getList(), 
					UploadStatus.COMPLETE.toString(), 
					new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList(),
					user.getId().toString());
			if(audios!=null && audios.size()>0){
				for(MediaAudioPO audio:audios){
					rows.add(new MediaAudioVO().set(audio));
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
				List<FolderPO> breadCrumbFolders = folderQuery.findPermissionCompanyFolderByIdIn(parentIds, FolderType.COMPANY_AUDIO.toString());
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
	 * 根据id获取音频<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午3:13:13
	 * @param id 音频id
	 */
	public MediaAudioPO loadById(Long id) throws Exception {
		MediaAudioPO media = mediaAudioDao.findOne(id);
		
		if(media == null){
			throw new MediaAudioNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return media;
	}
	
	/**
	 * 加载所有的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	public List<MediaAudioVO> loadAll(Long ... id) throws Exception{
		
		//TODO 权限校验
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		if (id != null && id.length > 0) {
			folderTree = folderQuery.findSubFolders(id[0]);
			folderTree.add(folderDao.findOne(id[0]));
		}
		
		if (folderTree.isEmpty()) return new ArrayList<MediaAudioVO>();
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaAudioPO> audios = mediaAudioDao.findByFolderIdIn(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		
		List<MediaAudioVO> medias = new ArrayList<MediaAudioVO>();
		
		for(FolderPO root:roots){
			medias.add(new MediaAudioVO().set(root));
		}
		
		packMediaAudioTree(medias, folderTree, audios);
		
		return id != null && id.length > 0 ? medias.get(0).getChildren() : medias;
	}
	
	/**
	 * 加载所有的音频媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 下午4:03:27
	 * @return List<MediaAudioVO> 音频媒资列表
	 */
	public List<MediaAudioVO> loadAllFolder() throws Exception{
		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaAudioVO> medias = new ArrayList<MediaAudioVO>();
		for(FolderPO root:roots){
			medias.add(new MediaAudioVO().set(root));
		}
		
		packMediaAudioTree(medias, folderTree, new ArrayList<MediaAudioPO>());
		
		return medias;
	}
	
	/**
	 * 根据创建时间筛选<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:08:38
	 * @param Long startTime 筛选起始时间
	 * @param Long endTime 筛选终止时间
	 * @return List<MediaAudioVO> 筛选结果
	 */
	public List<MediaAudioVO> loadByCreateTime(Long startTime, Long endTime) throws Exception{
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
	public List<MediaAudioVO> loadByCondition(Long id, String name, String startTime, String endTime, Long tagId) throws Exception{
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		TagPO tag = tagDAO.findByIdAndGroupId(tagId, user.getGroupId());
		String tagName = tag != null ? tag.getName() : null;
		
		List<MediaAudioPO> audios = mediaAudioDao.findByCondition(id, name, startTime, endTime, tagName, folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		return MediaAudioVO.getConverter(MediaAudioVO.class).convert(audios, MediaAudioVO.class);
	}
	
	public List<MediaAudioVO> loadRecommendWithWeight(UserVO user) throws Exception{
		JSONObject tagAndParent = tagQuery.queryTagAndParent(user, null, null);
		
		List<MediaAudioVO> recommends = new ArrayList<MediaAudioVO>();

		//根据用户标签获取媒资列表(每个标签权重为100)
		Map<String, List<MediaAudioVO>> fromUserTags = loadAllByTags(user, user.getTags());
		if (fromUserTags != null) {
//			List<MediaAudioVO> audios = fromUserTags.get("list");
			List<MediaAudioVO> audios = fromUserTags.get("tree");
			Long weight = 100l;
			for (MediaAudioVO tagMediaList : audios) {
				List<MediaAudioVO> children = tagMediaList.getChildren();
				String tagName = tagMediaList.getName();
				if (tagAndParent.containsKey(tagName)) tagName = tagAndParent.getString(tagName);
				if (children != null && !children.isEmpty()) {
					for (MediaAudioVO mediaAudioVO : children) {
						if (recommends.contains(mediaAudioVO)) {
							MediaAudioVO audio = recommends.get(recommends.indexOf(mediaAudioVO));
							audio.setHotWeight(audio.getHotWeight() + weight);
							JSONObject tagToHotWeight = audio.getTagToHotWeight();
							if (tagToHotWeight == null) tagToHotWeight = new JSONObject();
							Long hotWeight = tagToHotWeight.containsKey(tagName) ? tagToHotWeight.getLong(tagName) + weight : weight;
							tagToHotWeight.put(tagName, hotWeight);
							tagToHotWeight.put("user", tagToHotWeight.containsKey("user") ? tagToHotWeight.getLong("user") + weight : weight);
							audio.setTagToHotWeight(tagToHotWeight);
						} else {
							mediaAudioVO.setHotWeight(weight);
							JSONObject tagToHotWeight = new JSONObject();
							tagToHotWeight.put(tagName, weight);
							tagToHotWeight.put("user", weight);
							mediaAudioVO.setTagToHotWeight(tagToHotWeight);
							recommends.add(mediaAudioVO);
						}
					}
				}
			}
		}
		
		//获取下载量排序列表，依次添加下载量大于0的媒资(下载一次为1)
		List<MediaAudioVO> fromHot = loadHotList();
		for (MediaAudioVO mediaAudioVO : fromHot) {
			if (mediaAudioVO.getDownloadCount() == null || mediaAudioVO.getDownloadCount() == 0) break;
			if (recommends.contains(mediaAudioVO)) {
				MediaAudioVO audio = recommends.get(recommends.indexOf(mediaAudioVO));
				audio.setHotWeight(audio.getHotWeight() + audio.getDownloadCount());
			} else {
				mediaAudioVO.setHotWeight(mediaAudioVO.getDownloadCount());
				recommends.add(mediaAudioVO);
			}
		}
		
		//根据同相同最大下载量标签的其他用户的其他标签获取(重复权重均加50)
//		List<MediaAudioVO> otherAudios = loadRecommendFromOthor(user);
		
		//遍历所有同组织用户，有下载且A/B用户均没有的标签(该标签所有媒资加50)
		List<MediaAudioVO> otherAudios = loadRecommendFromOtherUser(user);
		if (otherAudios != null && !otherAudios.isEmpty()) {
			Long weight = 50l;
			for (MediaAudioVO tagMediaList : otherAudios) {
				List<MediaAudioVO> children = tagMediaList.getChildren();
				String tagName = tagMediaList.getName();
				if (tagAndParent.containsKey(tagName)) tagName = tagAndParent.getString(tagName);
				if (children != null && !children.isEmpty()) {
					for (MediaAudioVO mediaAudioVO : children) {
						if (recommends.contains(mediaAudioVO)) {
							MediaAudioVO audio = recommends.get(recommends.indexOf(mediaAudioVO));
							audio.setHotWeight(audio.getHotWeight() + weight);
							JSONObject tagToHotWeight = audio.getTagToHotWeight();
							if (tagToHotWeight == null) tagToHotWeight = new JSONObject();
							Long hotWeight = tagToHotWeight.containsKey(tagName) ? tagToHotWeight.getLong(tagName) + weight : weight;
							tagToHotWeight.put(tagName, hotWeight);
							audio.setTagToHotWeight(tagToHotWeight);
						} else {
							mediaAudioVO.setHotWeight(weight);
							JSONObject tagToHotWeight = new JSONObject();
							tagToHotWeight.put(tagName, weight);
							mediaAudioVO.setTagToHotWeight(tagToHotWeight);
							recommends.add(mediaAudioVO);
						}
					}
				}
			}
		}
		
		Collections.sort(recommends, new MediaAudioHotOrderComparator());
		
		return recommends.size() > 30 ? recommends.subList(0, 30) : recommends;
	}
	
	/**
	 * 根据用户标签加载所有的音频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	public List<MediaAudioVO> loadAllByUserTags(UserVO user) throws Exception{
		List<String> tags = user.getTags();
		Map<String, List<MediaAudioVO>> map = loadAllByTags(user, tags);
		return map == null ? null : map.get("tree");
	}
	
	/**
	 * 根据标签加载所有的音频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 * @return List<MediaAudioVO> 视频媒资列表
	 */
	public Map<String, List<MediaAudioVO>> loadAllByTags(UserVO user, List<String> tags) throws Exception{
		if (tags == null || tags.isEmpty()) return null;
		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		ArrayListWrapper<MediaAudioVO> audiosTree = new ArrayListWrapper<MediaAudioVO>();
		
		ArrayListWrapper<MediaAudioVO> audiosList = new ArrayListWrapper<MediaAudioVO>();
		
		for (String tag : tags) {
			List<MediaAudioPO> childAudios = mediaAudioDao.findByFolderIdInAndTagByDownloadCountOrderDesc(folderIds, tag, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
			if (childAudios == null || childAudios.isEmpty()) continue;
			MediaAudioVO audio = new MediaAudioVO();
			audio.setName(tag);
			audio.setType(MediaAudioItemType.FOLDER.toString());
			List<MediaAudioVO> audioVOs = MediaAudioVO.getConverter(MediaAudioVO.class).convert(childAudios, MediaAudioVO.class);
			audio.setChildren(audioVOs);
			audiosTree.add(audio);
			audiosList.addAll(audioVOs);
		}
		
		return new HashMapWrapper<String, List<MediaAudioVO>>().put("tree", audiosTree.getList()).put("list", audiosList.getList()).getMap();
	}
	
	/**
	 * 获取标签的音频媒资数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月11日 下午4:03:27
	 */
	public void queryCountByTags(UserVO user, List<TagVO> tags) throws Exception{
		if (tags == null || tags.isEmpty()) return;
		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		for (TagVO tag : tags) {
			List<MediaAudioPO> childAudios = mediaAudioDao.findByFolderIdInAndTagByDownloadCountOrderDesc(folderIds, tag.getName(), new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
			if (childAudios == null || childAudios.isEmpty()) continue;
			int num = tag.getSubMediaNum();
			tag.setSubMediaNum(num + childAudios.size());
		}
	}
	
	/**
	 * 获取标签的音频媒资数和总下载量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月19日 下午4:03:27
	 */
	public void queryMediaCountAndDownloadByTags(UserVO user, List<TagVO> tags) throws Exception{
		if (tags == null || tags.isEmpty()) return;
		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		for (TagVO tag : tags) {
			List<MediaAudioPO> childAudios = mediaAudioDao.findByFolderIdInAndTagByDownloadCountOrderDesc(folderIds, tag.getName(), new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
			if (childAudios == null || childAudios.isEmpty()) continue;
			int num = tag.getSubMediaNum();
			Long download = tag.getDownloadCount();
			tag.setSubMediaNum(num + childAudios.size());
			for (MediaAudioPO mediaAudioPO : childAudios) {
				download += mediaAudioPO.getDownloadCount();
			}
			tag.setDownloadCount(download);
		}
	}
	
	/**
	 * 根据同最高下载量标签的其他用户获取推荐<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 上午11:30:44
	 * @param UserVO user 用户
	 * @return List<MediaAudioVO> 推荐列表
	 */
	public List<MediaAudioVO> loadRecommendFromOthor(UserVO user) throws Exception{
		List<MediaAudioVO> returnAudioVos = new ArrayList<MediaAudioVO>();
		
		List<TagDownloadPermissionPO> userToTagPermission = tagDownloadPermissionDAO.findByUserIdOrderByDownloadCountDesc(user.getId());
		if (userToTagPermission == null || userToTagPermission.isEmpty()) return returnAudioVos;
		for (int i = 0; i < userToTagPermission.size() && i < 10; i++) {
			Long tagId = userToTagPermission.get(i).getTagId();
			List<TagDownloadPermissionPO> tagToUserPermission = tagDownloadPermissionDAO.findByTagIdOrderByDownloadCountDesc(tagId);
			if (tagToUserPermission != null) {
				for (int j = 0; j < tagToUserPermission.size() && j < 10; j++) {
					List<TagDownloadPermissionPO> userToTagPermission2 = 
							tagDownloadPermissionDAO.findByUserIdWithExceptTagIdOrderByDownloadCountDesc(tagToUserPermission.get(j).getUserId(), tagId);
					if (userToTagPermission2 != null) {
						List<String> tags = new ArrayList<String>();
						for (int k = 0; k < userToTagPermission2.size() && k < 10; k++) {
							TagPO tag = tagDAO.findOne(userToTagPermission2.get(k).getTagId());
							if (tag == null) continue;
							tags.add(tag.getName());
						}
//						if (j == 0) {
//							TagPO tag = tagDAO.findOne(tagId);
//							if (tag != null) tags.add(tag.getName());
//						}
						Map<String, List<MediaAudioVO>> map = loadAllByTags(user, tags);
						if (map != null) {
							List<MediaAudioVO> audios = map.get("list");
							returnAudioVos.addAll(audios);
						}
					}
				}
			}
		}
		
		return returnAudioVos;
	}
	
	/**
	 * 遍历所有同组织用户，有下载且A/B用户均没有的标签<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月22日 下午4:49:15
	 * @param user
	 */
	public List<MediaAudioVO> loadRecommendFromOtherUser(UserVO user) throws Exception {
		List<MediaAudioVO> returnAudioVOs = new ArrayList<MediaAudioVO>();
		List<String> localTags = user.getTags();

		List<UserVO> userVOs = userQuery.listByCompanyIdWithExceptAndClassify(Long.parseLong(user.getGroupId()), new ArrayListWrapper<Long>().add(user.getId()).getList(), UserClassify.COMPANY);
		
		if(userVOs == null || userVOs.isEmpty()) return returnAudioVOs;
		
		for (UserVO userVO : userVOs) {
			List<String> userTags = userVO.getTags();
			Long userId = userVO.getId();
			
			List<String> sameTags = new ArrayList<String>();
			for (String tag : userTags) {
				if (localTags.contains(tag)) {
					sameTags.add(tag);
				}
			}
			
			List<TagDownloadPermissionPO> userToTagPermission = tagDownloadPermissionDAO.findByUserIdOrderByDownloadCountDesc(userId);
			
			if (userToTagPermission == null || userToTagPermission.isEmpty()) continue;
			
			List<String> tags = new ArrayList<String>();
			for (TagDownloadPermissionPO tagDownloadPermissionPO : userToTagPermission) {
				TagPO tag = tagDAO.findOne(tagDownloadPermissionPO.getTagId());
				if (tag == null || sameTags.contains(tag.getName())) continue;
				tags.add(tag.getName());
			}
			
			Map<String, List<MediaAudioVO>> map = loadAllByTags(user, tags);
			if (map != null) {
//				List<MediaAudioVO> audios = map.get("list");
				List<MediaAudioVO> audios = map.get("tree");
				returnAudioVOs.addAll(audios);
			}
		}
		
		return returnAudioVOs;
	}
	
	/**
	 * 查询文件夹下上传完成的音频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPicturePO> 音频媒资
	 */
	public List<MediaAudioPO> findCompleteByFolderId(Long folderId){
		return mediaAudioDao.findByFolderIdAndUploadStatusOrderByName(folderId, UploadStatus.COMPLETE);
	}
	
	/**
	 * 查询文件夹下上传完成的音频媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 音频媒资
	 */
	public List<MediaAudioPO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaAudioDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.COMPLETE);
	}
	
	/**
	 * 获取文件夹（多个）下的音频媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPicturePO> 上传任务列表
	 */
	public List<MediaAudioPO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaAudioDao.findByFolderIdInAndUploadStatus(folderIds, UploadStatus.UPLOADING);
	}
	
	/**
	 * 根据uuid查找媒资音频（内存循环）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaAudioPO> pictures 查找范围
	 * @return MediaPicturePO 查找结果
	 */
	public MediaAudioPO loopForUuid(String uuid, Collection<MediaAudioPO> audios){
		if(audios==null || audios.size()<=0) return null;
		for(MediaAudioPO audio:audios){
			if(audio.getUuid().equals(uuid)){
				return audio;
			}
		}
		return null;
	}
	
	/**
	 * 根据uuid查找媒资音频<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 上午11:52:58
	 * @param List<String> uuids 音频uuids
	 * @return List<MediaAudioPO> 查找结果
	 */
	public List<MediaAudioPO> questByUuid(List<String> uuids){
		if(uuids==null || uuids.size()<=0) return null;
		return mediaAudioDao.findByUuidIn(uuids);
	}
	
	/**
	 * 生成媒资音频树<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月31日 上午11:29:34
	 * @param List<MediaAudioVO> roots 根
	 * @param List<FolderPO> folders 所有文件夹
	 * @param List<MediaAudioPO> medias 所有视频媒资
	 */
	public void packMediaAudioTree(List<MediaAudioVO> roots, List<FolderPO> folders, List<MediaAudioPO> medias) throws Exception{
		if(roots == null || roots.size() <= 0){
			return;
		}
		for(MediaAudioVO root: roots){
			if(root.getType().equals(MediaVideoItemType.FOLDER.toString())){
				if(root.getChildren() == null) root.setChildren(new ArrayList<MediaAudioVO>());
				for(FolderPO folder: folders){
					if(folder.getParentId() != null && folder.getParentId().equals(root.getId())){
						root.getChildren().add(new MediaAudioVO().set(folder));
					}
				}
				for(MediaAudioPO media: medias){
					if(media.getFolderId() != null && media.getFolderId().equals(root.getId())){
						root.getChildren().add(new MediaAudioVO().set(media));
					}
				}
				if(root.getChildren().size() > 0){
					packMediaAudioTree(root.getChildren(), folders, medias);
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
	 * 获取热门列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月15日 下午5:26:50
	 * @param int currentPage 当前页面
	 * @return List<MediaAudioVO> 热门媒资列表
	 */
	public List<MediaAudioVO> loadHotList() throws Exception{
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_AUDIO.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaAudioPO> audios = mediaAudioDao.findByFolderIdInOrderByDownloadCountDesc(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		return MediaAudioVO.getConverter(MediaAudioVO.class).convert(audios, MediaAudioVO.class);
	}
	
	/**
	 * 获取文件加密地址<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 下午5:12:15
	 * @param List<MediaAudioVO> audioVOs 文件数组
	 */
	public void queryEncodeUrl(List<MediaAudioVO> audioVOs) throws Exception {
		//从音频数组中获取id列表
		List<Long> mediaIds = audioVOs.stream().map(MediaAudioVO::getId).collect(Collectors.toList());
		List<AudioFileEncodePO> encodePOs = audioFileEncodeQuery.queryFromMediaIds(mediaIds);
		
		for (MediaAudioVO audioVO : audioVOs) {
			if (audioVO.getChildren() != null && !audioVO.getChildren().isEmpty()) {
				queryEncodeUrl(audioVO.getChildren());
			} else {
				for (AudioFileEncodePO audioFileEncodePO : encodePOs) {
					if (audioVO.getId().equals(audioFileEncodePO.getMediaId())) {
						String encryptionUrl = audioVO.getStoreType() == StoreType.REMOTE 
								? audioFileEncodePO.getPreviewUrl() 
										: new StringBufferWrapper()
										.append("http://")
										.append(serverPropsQuery.queryProps().getFtpIp())
										.append(":")
										.append(serverProps.getPort())
										.append("/")
										.append(audioFileEncodePO.getPreviewUrl())
										.toString();
						audioVO.setEncryptionUrl(encryptionUrl);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 根据预览地址查询音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:13:14
	 * @param Collection<String> previewUrls 预览地址列表
	 * @return List<MediaAudioVO> 音频列表
	 */
	public List<MediaAudioVO> findByPreviewUrlIn(Collection<String> previewUrls) throws Exception{
		ArrayList<String> searchPreivewList = new ArrayList<String>();
		if (previewUrls == null || previewUrls.isEmpty()) return new ArrayList<MediaAudioVO>();
		String localUrl = new StringBufferWrapper()
				.append("http://")
				.append(serverPropsQuery.queryProps().getFtpIp())
				.append(":")
				.append(serverProps.getPort())
				.append("/").toString();
		String netUrl = new StringBufferWrapper()
				.append("http://")
				.append(serverPropsQuery.queryProps().getFtpIp())
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
		List<MediaAudioPO> entities = mediaAudioDao.findByPreviewUrlIn(searchPreivewList);
		return MediaAudioVO.getConverter(MediaAudioVO.class).convert(entities, MediaAudioVO.class);
	}
	
	/**
	 * 获取资源上传者的资源上传量<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 下午5:10:15
	 */
	public JSONObject loadAuthorPermission() throws Exception {
		UserVO user = userQuery.current();
		List<UserVO> userVOs = userQuery.listByCompanyIdWithExceptAndClassify(Long.parseLong(user.getGroupId()), null, UserClassify.COMPANY);
		if (userVOs == null || userVOs.isEmpty()) return null;
		List<String> userIds = new ArrayList<String>();
		for (UserVO userVO : userVOs) {
			userIds.add(userVO.getId().toString());
		}
		List<MediaAudioPO> statisticsPOs = mediaAudioDao.findByAuthorIdIn(userIds);
		if (statisticsPOs == null || statisticsPOs.isEmpty()) return null; 
		Map<String, Long> map = new HashMap<String, Long>();
		for (MediaAudioPO media : statisticsPOs) {
			String userId = media.getAuthorId();
			map.put(userId, map.containsKey(userId) ? map.get(userId) + 1 : 1);
		}
		JSONObject tagAndParent = tagQuery.queryTagAndParent(user, null, null);
		JSONObject returnObject = new JSONObject();
		for (UserVO userVO : userVOs) {
			String userId = userVO.getId().toString();
			if (!map.containsKey(userId)) continue;
			Long count = map.get(userId);
			List<String> tags = userVO.getTags();
			if (tags == null || tags.isEmpty()) continue;
			for (String tag : tags) {
				if (tagAndParent.containsKey(tag) && tagAndParent.getString(tag).equals("行政区域")) {
					returnObject.put(tag, returnObject.containsKey(tag) ? returnObject.getLong(tag) + count : count);
					break;
				}
			}
		}
		return returnObject;
	}
}
