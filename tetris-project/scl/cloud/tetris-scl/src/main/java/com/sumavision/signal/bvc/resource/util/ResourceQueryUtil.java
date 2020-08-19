package com.sumavision.signal.bvc.resource.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.service.UserQueryService;
import com.sumavision.signal.bvc.network.bo.NetBundleBO;
import com.sumavision.signal.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.signal.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.signal.bvc.resource.dao.ResourceFolderDAO;
import com.sumavision.signal.bvc.resource.dao.ResourceScreenDAO;
import com.sumavision.signal.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;


/**
 * @ClassName: 从资源层查询数据<br/> 
 * @author wjw
 * @date 2019年5月22日 下午3:36:06 
 */
@Service
public class ResourceQueryUtil {

	@Autowired
	private ResourceFolderDAO folderDao;
	
	@Autowired
	private ResourceBundleDAO bundleDao;
	
	@Autowired
	private ResourceChannelDAO channelDao;
	
	@Autowired
	private ResourceScreenDAO screenDao;
	
	@Autowired
	private UserQueryService userQueryService;
	
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
			List<BundlePO> bundles = bundleDao.findByBundleIds(bundleIds);
			return bundles;
		}
		
		return null;
	}
	
	/**
	 * 根据接入id查询设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 上午10:08:15
	 * @param String accessNodeUid 接入id
	 * @return List<BundlePO> 设备数据
	 */
	public List<BundlePO> queryBundlesByAccessNodeUid(String accessNodeUid, String deviceModel) throws Exception{
		
		List<BundlePO> bundles = bundleDao.findByAccessNodeUid(accessNodeUid, deviceModel);
		return bundles;

	}
	
	/**
	 * 根据接入id查询设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 上午10:08:15
	 * @param String accessNodeUid 接入id
	 * @return List<BundlePO> 设备数据
	 */
	public List<BundlePO> queryBundlesByDeviceModel(String deviceModel) throws Exception{
		
		List<BundlePO> bundles = bundleDao.findByDeviceModel(deviceModel);
		return bundles;

	}
	
	public List<BundlePO> queryBundlesByDeviceModel(String deviceModel, String bundleAlias) throws Exception{
		
		List<BundlePO> bundles = bundleDao.findByDeviceModelAndNotBundleAlias(deviceModel, bundleAlias);
		return bundles;

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
	 * 根据bundleId查询网络信息设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午4:44:52
	 * @param String bundleId 设备id
	 * @return NetBundleBO
	 */
	public NetBundleBO queryBundleByBundleId(String bundleId) throws Exception{
		
		BundlePO bundle = bundleDao.findByBundleId(bundleId);
		if(bundle == null){
			return null;
		}else{
			return new NetBundleBO().toBo(bundle);
		}

	}
	
}
