package com.sumavision.tetris.mims.app.media.live;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.sumavision.tetris.mims.app.media.tag.TagDAO;
import com.sumavision.tetris.mims.app.media.tag.TagPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class MediaPushLiveQuery {
	@Autowired
	private MediaPushLiveDAO mediaPushLiveDAO;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	/**
	 * 根据文件夹id查询文件夹以及push直播媒资<br/>
	 * <p>
	 * 	-如果folderId是0：查询有权限的根目录，只返回目录列表
	 * 	-如果folderId不是0：查询当前文件夹下有权限的目录以及目录下所有的媒资
	 * </p>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:14:37
	 * @param UserVO user 用户
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaPushLiveVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	public Map<String, Object> load(Long folderId) throws Exception{

		UserVO user = userQuery.current();
		
		List<MediaPushLiveVO> rows = null;
		
		//处理根面包屑
		FolderBreadCrumbVO breadCrumb = new FolderBreadCrumbVO().setId(0l)
																.setUuid("0")
																.setName("根目录")
																.setType(FolderType.COMPANY_PUSH_LIVE.toString());
		
		if(user.getBusinessRoles() == null){
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}
		
		if(folderId.equals(0l)){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PUSH_LIVE.toString());
			if(folders==null || folders.size()<=0){
				return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
			}
			List<FolderPO> rootFolders = folderQuery.findRoots(folders);
			rows = new ArrayList<MediaPushLiveVO>();
			for(FolderPO folder:rootFolders){
				MediaPushLiveVO row = new MediaPushLiveVO().set(folder);
				rows.add(row);
			}
			return new HashMapWrapper<String, Object>().put("rows", rows).put("breadCrumb", breadCrumb).getMap();
		}else{
			FolderPO current = folderDao.findOne(folderId);
			if(current == null) throw new FolderNotExistException(folderId);
			
			rows = new ArrayList<MediaPushLiveVO>();
			
			//子文件夹
			List<FolderPO> folders = folderQuery.findPermissionCompanyFolderByParentIdOrderByNameAsc(current.getId());
			if(folders!=null && folders.size()>0){
				for(FolderPO folder:folders){
					MediaPushLiveVO row = new MediaPushLiveVO().set(folder);
					rows.add(row);
				}
			}
			
			//文件夹内音频
			List<MediaPushLivePO> pushLives = mediaPushLiveDAO.findByFolderIdInAndReviewStatusNotInOrAuthorId(
					new ArrayListWrapper<Long>().add(current.getId()).getList(), 
					new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList(),
					user.getId().toString());
			if(pushLives!=null && pushLives.size()>0){
				for(MediaPushLivePO pushLive:pushLives){
					rows.add(new MediaPushLiveVO().set(pushLive));
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
				List<FolderPO> breadCrumbFolders = folderQuery.findPermissionCompanyFolderByIdIn(parentIds, FolderType.COMPANY_PUSH_LIVE.toString());
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
	 * 加载所有的push直播媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 上午11:24:24
	 * @return List<MediaPushLiveVO> push直播媒资列表
	 */
	public List<MediaPushLiveVO> loadAll() throws Exception{
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PUSH_LIVE.toString());
		
		if (folderTree.isEmpty()) return new ArrayList<MediaPushLiveVO>();
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		List<MediaPushLivePO> videos = mediaPushLiveDAO.findByFolderIdIn(folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		List<FolderPO> roots = folderQuery.findRoots(folderTree);
		List<MediaPushLiveVO> medias = new ArrayList<MediaPushLiveVO>();
		for(FolderPO root:roots){
			medias.add(new MediaPushLiveVO().set(root));
		}
		
		packMediaPushLiveTree(medias, folderTree, videos);
		
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
	public void packMediaPushLiveTree(List<MediaPushLiveVO> roots, List<FolderPO> folders, List<MediaPushLivePO> medias) throws Exception{
		if(roots == null || roots.size() <= 0){
			return;
		}
		
		for(MediaPushLiveVO root: roots){
			if(root.getType().equals(MediaPushLiveItemType.FOLDER.toString())){
				if(root.getChildren() == null) root.setChildren(new ArrayList<MediaPushLiveVO>());
				for(FolderPO folder: folders){
					if(folder.getParentId() != null && folder.getParentId().equals(root.getId())){
						root.getChildren().add(new MediaPushLiveVO().set(folder));
					}
				}
				for(MediaPushLivePO media: medias){
					if(media.getFolderId() != null && media.getFolderId().equals(root.getId())){
						root.getChildren().add(new MediaPushLiveVO().set(media));
					}
				}
				if(root.getChildren().size() > 0){
					packMediaPushLiveTree(root.getChildren(), folders, medias);
				}
			}
		}
	}
	
	/**
	 * 根据id查询<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 下午1:48:42
	 * @param Long id push直播媒资id
	 * @return MediaPushLiveVO push直播媒资信息
	 */
	public MediaPushLiveVO findById(Long id) throws Exception {
		MediaPushLivePO livePushPO = mediaPushLiveDAO.findOne(id);
		
		if (livePushPO == null) return null;
		
		return new MediaPushLiveVO().set(livePushPO);
	}
	
	/**
	 * 根据创建时间筛选<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月5日 下午3:08:38
	 * @param Long startTime 筛选起始时间
	 * @param Long endTime 筛选终止时间
	 * @return List<MediaPushLiveVO> 筛选结果
	 */
	public List<MediaPushLiveVO> loadByCreateTime(Long startTime, Long endTime) throws Exception{
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
	 * @return List<MediaPushLiveVO> 查询结果
	 */
	public List<MediaPushLiveVO> loadByCondition(Long id, String name, String startTime, String endTime, Long tagId) throws Exception{
		UserVO user = userQuery.current();
		
		//TODO 权限校验		
		List<FolderPO> folderTree = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PUSH_LIVE.toString());
		
		List<Long> folderIds = new ArrayList<Long>();
		for(FolderPO folderPO: folderTree){
			folderIds.add(folderPO.getId());
		}
		
		TagPO tag = tagDAO.findByIdAndGroupId(tagId, user.getGroupId());
		String tagName = tag != null ? tag.getName() : null;
		
		List<MediaPushLivePO> medias = mediaPushLiveDAO.findByCondition(id, name, startTime, endTime, tagName, folderIds, new ArrayListWrapper<String>().add(ReviewStatus.REVIEW_UPLOAD_WAITING.toString()).add(ReviewStatus.REVIEW_UPLOAD_REFUSE.toString()).getList());
		
		return MediaPushLiveVO.getConverter(MediaPushLiveVO.class).convert(medias, MediaPushLiveVO.class);
	}
	
	/**
	 * 查询文件夹下上传完成的视频流媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:38:53
	 * @param Long folderId 文件夹id
	 * @return List<MediaPushLivePO> 视频流媒资
	 */
	public List<MediaPushLivePO> findCompleteByFolderId(Long folderId){
		return mediaPushLiveDAO.findByFolderIdOrderByName(folderId);
	}
	
	/**
	 * 查询文件夹下上传完成的视频流媒资（批量）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:36:37
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPushLivePO> 视频流媒资
	 */
	public List<MediaPushLivePO> findCompleteByFolderIds(Collection<Long> folderIds){
		return mediaPushLiveDAO.findByFolderIdIn(folderIds);
	}
	
	/**
	 * 获取文件夹（多个）下的视频流媒资上传任务（上传未完成的）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:25:31
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @return List<MediaPushLivePO> 上传任务列表
	 */
	public List<MediaPushLivePO> findTasksByFolderIds(Collection<Long> folderIds){
		return mediaPushLiveDAO.findByFolderIdIn(folderIds);
	}
	
	/**
	 * 根据uuid查找媒资视频流（内存循环）<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月26日 上午11:52:58
	 * @param String uuid 图片uuid
	 * @param Collection<MediaPushLivePO> medias 查找范围
	 * @return MediaPushLivePO 查找结果
	 */
	public MediaPushLivePO loopForUuid(String uuid, Collection<MediaPushLivePO> medias){
		if(medias==null || medias.size()<=0) return null;
		for(MediaPushLivePO media:medias){
			if(media.getUuid().equals(uuid)){
				return media;
			}
		}
		return null;
	}
}
