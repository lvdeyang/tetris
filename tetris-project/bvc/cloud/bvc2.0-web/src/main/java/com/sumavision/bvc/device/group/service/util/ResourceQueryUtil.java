package com.sumavision.bvc.device.group.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.PlayerBundleBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.VirtualResourceDao;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dao.ResourceScreenDAO;
import com.sumavision.bvc.resource.dto.BundleOnlineStatusDTO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;


/**
 * @ClassName: 从资源层查询数据<br/> 
 * @author lvdeyang
 * @date 2018年8月25日 下午3:36:06 
 */
@Service
public class ResourceQueryUtil {

	@Autowired
	private FolderDao folderDao;
	
	@Autowired
	private ResourceBundleDAO bundleDao;
	
	@Autowired
	private BundleDao nativeBundleDao;

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private ResourceChannelDAO channelDao;
	
	@Autowired
	private ResourceScreenDAO screenDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	@Autowired
	private UserQueryFeign userFeign;
	
	@Autowired
	UserQueryService userQueryService;
	
	/**
	 * @Title 根据id集合查询整个文件夹树<br/> 注意对结果判空！
	 * @Description 1.根据id查询id的parentPath<br/>
	 *              2.结果与参数取并集<br/> 
	 *              3.返回根据并集查询结果<br/> 
	 * @param folderIds 文件夹id集合
	 * @throws Exception
	 * @return List<FolderPO> 注意对结果判空！
	 */
	public List<FolderPO> queryFoldersTree(Collection<Long> folderIds) throws Exception{
		if(folderIds == null || folderIds.size() <= 0) return null;
		Set<Long> ids = new HashSetWrapper<Long>().addAll(folderIds).getSet();
		List<String> paths = folderDao.queryParentPathByIds(ids);
		if(paths!=null && paths.size()>0){
			for(String path:paths){
				if(path==null || "".equals(path)) continue;
				String[] idArr = path.split("/");
				for(String scopeId:idArr){
					if(!"".equals(scopeId)) ids.add(Long.valueOf(scopeId));
				}
			}
		}
		List<FolderPO> folders = folderDao.findAll(ids);
		return folders;
	}
	
	/**
	 * @Title: 查询用户有权限的设备<br/> 
	 * @param userId 用户id
	 * @throws Exception
	 * @return List<BundlePO> 设备数据
	 */
	public List<BundlePO> queryUseableBundles(Long userId) throws Exception{
		
		Set<String> bundleIds = queryUseableBundleIds(userId);
		
		if(bundleIds.size() > 0){
			List<BundlePO> bundles = nativeBundleDao.findByBundleIdIn(bundleIds);
			return bundles;
		}
		
		return null;
	}
	
	/**
	 * @Title: 查询文件夹下用户有权限的设备<br/> 
	 * @param userId 用户id
	 * @param folderId 文件夹id
	 * @throws Exception
	 * @return List<BundlePO> 设备数据
	 */
	public List<BundlePO> queryInstUseableBundles(Long userId, Long folderId) throws Exception{
		
		Set<String> bundleIds = queryUseableBundleIds(userId);
		
		if(bundleIds.size() > 0){
			List<BundlePO> bundles = bundleDao.findByBundleIdsAndFolderId(bundleIds, folderId);
			return bundles;
		}
		return null;
	}
	
	/**
	 * @Title: 查询用户有权限的jv230设备<br/> 
	 * @param userId 用户id
	 * @throws Exception
	 * @return List<BundlePO> jv230设备
	 */
	public List<BundlePO> queryUseableJv230s(Long userId) throws Exception{
		
		Set<String> bundleIds = queryUseableBundleIds(userId);
		
		if(bundleIds.size() > 0){
			List<BundlePO> bundles = bundleDao.findJv230ByBundelIds(bundleIds);
			return bundles;
		}
		
		return null;
	}
	
	/**
	 * @Title: 获取有权限的设备id列表<br/> 
	 * @param userId 用户id
	 * @throws Exception 
	 * @return Set<String>
	 */
	private Set<String> queryUseableBundleIds(Long userId) throws Exception{
		//获取用户权限
//		ResourceIdListBO bo = userFeign.queryResourceByUserId(userId);
		ResourceIdListBO bo = userQueryService.queryUserPrivilege(userId);
		
		Set<String> bundleIds = new HashSet<String>();
		if(null != bo && null != bo.getResourceCodes()){
			for (String resourceCode : bo.getResourceCodes()) {
				//用户绑定的bundle资源的resouceCode有一种特别格式:bundleId-r(可写)/bundleId-w(可读)
				if(resourceCode.endsWith("-r") || resourceCode.endsWith("-w")){
					bundleIds.add(resourceCode.substring(0, resourceCode.length()-2));
				}else{
					bundleIds.add(resourceCode);
				}
			}
		}
		return bundleIds;
	}
	
	/**
	 * @Title 根据设备id列表获取所有通道<br/> 注意对返回判空！
	 * @param bundleIds 设备ids
	 * @throws Exception 
	 * @return List<ChannelSchemeDTO> 注意对返回判空！
	 */
	public List<ChannelSchemeDTO> queryAllChannelsByBundleIds(Collection<String> bundleIds) throws Exception{
		
		if(bundleIds != null && bundleIds.size()>0){
			List<ChannelSchemeDTO> channels = channelDao.findByBundleIds(bundleIds);
			return channels;
		}
		
		return null;
	}	
	
	/**
	 * 查询特定类型的设备通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:19:39
	 * @param Collection<String> bundleIds 设备id列表
	 * @param int channelType 通道类型
	 * @return List<ChannelSchemeDTO> 通道列表
	 */
	public List<ChannelSchemeDTO> findByBundleIdsAndChannelType(Collection<String> bundleIds, int channelType) throws Exception{
		
		if(bundleIds != null && bundleIds.size()>0){
			List<ChannelSchemeDTO> channels = channelDao.findByBundleIdsAndChannelType(bundleIds, channelType);
			return channels;
		}
		
		return null;
	}
	
	/**
	 * @Title: 根据设备id列表获取所有屏幕 <br/>
	 * @param bundleIds 设备ids
	 * @throws Exception 
	 * @return List<ScreenSchemePO> 
	 */
	public List<ScreenSchemePO> queryScreensByBundleIds(Collection<String> bundleIds) throws Exception{
		
		if(bundleIds != null && bundleIds.size()>0){
			List<ScreenSchemePO> screens = screenDao.findByBundleIds(bundleIds);
			return screens;
		}
		
		return null;
	}
	
	/**
	 * @Title: 根据屏幕id列表获取所有区域 <br/>
	 * @param screenIds 屏幕ids
	 * @throws Exception 
	 * @return List<ScreenRectTemplatePO> 
	 */
	public List<ScreenRectTemplatePO> queryRectsByScreenIds(Collection<String> screenIds) throws Exception{
		 
		 if(screenIds != null && screenIds.size()>0){
			 List<ScreenRectTemplatePO> rects = screenDao.findByScreenIds(screenIds);
			 return rects;
		 }
		 
		 return null;
	}
	
	/**
	 * @Title: 根据bundleIds列表获取所有设备<br/> 
	 * @param bundleIds
	 * @throws Exception
	 * @return List<BundlePO>
	 */
	public List<BundlePO> queryAllBundlesByBundleIds(Collection<String> bundleIds) throws Exception{
		
		if(bundleIds != null && bundleIds.size() > 0){
			List<BundlePO> bundles = bundleDao.findByBundleIds(bundleIds);
			return bundles;
		}
		
		return null;
		
	}
	
	/**
	 * @Title: 在BundleBO中添加OnlineStatus<br/> 
	 * @param List<BundlePO>
	 * @throws Exception
	 * @return List<BundlePO>即输入参数本身
	 */
	public List<BundleBO> appendBundleOnlineStatusIntoBundleBos(List<BundleBO> bundleBos) throws Exception{
		
		List<String> bundleIds = new ArrayList<String>();
		for(BundleBO bundleBo : bundleBos){
			bundleIds.add(bundleBo.getBundleId());
		}
		List<BundleOnlineStatusDTO> bundleOnlineStatuses = bundleDao.findBundleOnlineStatusByBundleIds(bundleIds);
		
		for(BundleBO bundleBo : bundleBos){
			for(BundleOnlineStatusDTO dto : bundleOnlineStatuses){
				if(bundleBo.getBundleId().equals(dto.getBundleId())){
					bundleBo.setOnlineStatus(dto.getOnlineStatus().toString())
							.setDeviceIp(dto.getDeviceIp())
							.setDevicePort(dto.getDevicePort())
							.setDeviceModel(dto.getDeviceModel())
							.setLayerIp(dto.getLayerIp())
							.setLayerType(dto.getLayerType());
					bundleBo.generateExtraInfo();
					break;
				}
			}
		}
		
		return bundleBos;
	}
	
	public String queryEncodeBundleIdByUserId(Long userId){
		if(userId == null) return null;
		EncoderDecoderUserMap userMap = encoderDecoderUserMapDao.findByUserId(userId);
		if(userMap == null) return null;
		return userMap.getEncodeBundleId();
	}
	
	public String queryDecodeBundleIdByUserId(Long userId){
		if(userId == null) return null;
		EncoderDecoderUserMap userMap = encoderDecoderUserMapDao.findByUserId(userId);
		if(userMap == null) return null;
		return userMap.getDecodeBundleId();
	}
	
	/** 根据userId查询播放器资源bundle **/
	public List<PlayerBundleBO> queryPlayerBundlesByUserId(Long userId) {
		List<PlayerBundleBO> playerBundles = new ArrayList<>();
		List<BundlePO> playerBundlePOs = bundleDao.findByUserId(userId);
		if (playerBundlePOs == null) {
			return playerBundles;
		}
		for (BundlePO bundlePO : playerBundlePOs) {
			// 过滤掉第17个播放器
			if (bundlePO.getUsername().endsWith("_17")) {
				continue;
			}
			PlayerBundleBO playerBundle = new PlayerBundleBO();
			playerBundle.setBundleId(bundlePO.getBundleId());
			playerBundle.setBundleNum(bundlePO.getBundleNum());
			playerBundle.setBundleName(bundlePO.getBundleName());
			playerBundle.setUsername(bundlePO.getUsername());
			playerBundle.setPassword(bundlePO.getOnlinePassword());
			playerBundle.setBundleType(bundlePO.getBundleType());
			playerBundle.setChannelIds(channelSchemeDao.findChannelIdsByBundleId(bundlePO.getBundleId()));
			playerBundle.setAccessLayerId(bundlePO.getAccessNodeUid());
			WorkNodePO accessLayer = workNodeDao.findByNodeUid(bundlePO.getAccessNodeUid());
			if (null != accessLayer) {
				playerBundle.setAccessLayerIp(accessLayer.getIp());
				playerBundle.setAccessLayerPort(accessLayer.getPort());
			}
			playerBundles.add(playerBundle);
		}
		return playerBundles;
	}
	
	/** 根据userId查询第17个播放器资源 **/
	public PlayerBundleBO querySpecifiedPlayerBundle(Long userId) {
//		List<BundlePO> bundles = bundleDao.findByUserId(userId);
//		List<BundlePO> playerBundlePOs = bundleService.queryPlayerBundlesByUserId(userId);
		List<BundlePO> playerBundlePOs = bundleDao.findByUserId(userId);
		if (playerBundlePOs == null) {
			return null;
		}
		for (BundlePO bundlePO : playerBundlePOs) {
			// 过滤出第17个播放器
			if (bundlePO.getUsername().endsWith("_17")) {
				PlayerBundleBO playerBundle = new PlayerBundleBO();
				playerBundle.setBundleId(bundlePO.getBundleId());
				playerBundle.setBundleNum(bundlePO.getBundleNum());
				playerBundle.setBundleName(bundlePO.getBundleName());
				playerBundle.setUsername(bundlePO.getUsername());
				playerBundle.setPassword(bundlePO.getOnlinePassword());
				playerBundle.setBundleType(bundlePO.getBundleType());
				playerBundle.setChannelIds(channelSchemeDao.findChannelIdsByBundleId(bundlePO.getBundleId()));
				playerBundle.setAccessLayerId(bundlePO.getAccessNodeUid());
				WorkNodePO accessLayer = workNodeDao.findByNodeUid(bundlePO.getAccessNodeUid());
				if (null != accessLayer) {
					playerBundle.setAccessLayerIp(accessLayer.getIp());
					playerBundle.setAccessLayerPort(accessLayer.getPort());
				}
				return playerBundle;
			}
		}
		return null;
	}
	
}
