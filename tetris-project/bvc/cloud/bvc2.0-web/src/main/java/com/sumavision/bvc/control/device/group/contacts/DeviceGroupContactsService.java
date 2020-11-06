package com.sumavision.bvc.control.device.group.contacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.FolderBO;

@Service
public class DeviceGroupContactsService {
	
	@Autowired
	private DeviceGroupContactsDAO deviceGroupContactsDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ExtraInfoService extraInfoService;

	/**
	 * 添加联系人对应的设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午9:15:41
	 * @param userId 用户Id
	 * @param bundleId 设备id
	 */
	public DeviceGroupContactsVO add(
			Long userId,
			String bundleId) throws Exception {
		//需要在这里加判重么?
		DeviceGroupContactsPO deviceGroupContactsPo= new DeviceGroupContactsPO().setUserId(userId).setBundleId(bundleId);
		
		deviceGroupContactsDao.save(deviceGroupContactsPo);
		
		return new DeviceGroupContactsVO().set(deviceGroupContactsPo);
	}
	
	/**
	 * 查询联系人对应的设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午9:56:02
	 * @param userId
	 */
	public List<TreeNodeVO> query(Long userId) {
		
		List<FolderBO> folders = resourceService.queryAllFolders().stream().filter(folder->{
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())) return true;
			return false;
		}).map(new FolderBO()::set).collect(Collectors.toList());
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		List<String> bundleIds = deviceGroupContactsDao.findBundleIdByUserId(userId);
		List<BundlePO> queryBundles = bundleDao.findByBundleIdIn(bundleIds);
		
		List<BundleBO> bundles = queryBundles.stream().map(bundleBody->{
			return new BundleBO().setId(bundleBody.getBundleId())										
					.setName(bundleBody.getBundleName())
					.setFolderId(bundleBody.getFolderId())
					.setBundleId(bundleBody.getBundleId())
					.setModel(bundleBody.getDeviceModel())
					.setNodeUid(bundleBody.getAccessNodeUid())
					.setOnlineStatus(bundleBody.getOnlineStatus().toString())
					.setLockStatus(bundleBody.getLockStatus())
					.setType(bundleBody.getBundleType())
					.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());
		}).collect(Collectors.toList());
		
		//排除空文件夹
		excludeEmptyFolder(folders, bundles);
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		
		//组件文件夹
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			recursionFolder(_root, folders, bundles);
			
			_roots.add(_root);
		}
		
		return _roots;
		
	}

	/**
	 * 删除联系人对应的设备<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午9:15:41
	 * @param userId 用户Id
	 * @param bundleId 设备id
	 */
	public void delete(
			Long userId, 
			String bundleId) {
		
		deviceGroupContactsDao.deleteByUserIdAndBundleId(userId, bundleId);
	}

	/**
	 * 去除空文件夹<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午10:33:22
	 * @param folders 文件夹BO集合
	 * @param bundles 设备BO集合
	 */
	public void  excludeEmptyFolder(
			List<FolderBO> folders,
			List<BundleBO> bundles) {
		
		Set<Long> folderIds=new HashSet<Long>();//所有文件夹id
		Map<Long,FolderBO> folderMap=folders.stream().collect(Collectors.toMap(FolderBO::getId, Function.identity()));
		bundles.stream().map(BundleBO::getFolderId).collect(Collectors.toSet()).stream().map(folderId->{
			Optional<FolderBO> folderBo=Optional.ofNullable(folderMap).map(folderMAP->{return folderMAP.get(folderId);});
			if(folderBo.isPresent()){//空值校验
				folderIds.add(folderId);
				String parentPath=folderBo.get().getParentPath();
				if(parentPath!=null && !"".equals(parentPath)){
					folderIds.addAll(Arrays.asList(parentPath.replaceFirst("/", "").split("/")).stream().map(Long::valueOf).collect(Collectors.toList()));
				} 
			}
			return  folderId;
		}).collect(Collectors.toSet());
		
		folders =folderIds.stream().map(folderId->{
			return folderMap.get(folderId);
		}).collect(Collectors.toList());
		
		Collections.sort(folders, Comparator.comparing(FolderBO::getId));
		Collections.sort(folders, Comparator.comparing(FolderBO::getFolderIndex));
		
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月6日 上午10:35:37
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	public List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles);
			}
		}
		
		//往里装设备
		if(bundles!=null && bundles.size()>0){
			
			//查到所有的扩展字段
			List<String> bundleIds = new ArrayList<String>();
			for(BundleBO bundle : bundles){
				bundleIds.add(bundle.getBundleId());
			}			
			List<ExtraInfoPO> allExtraInfos = extraInfoService.findByBundleIdIn(bundleIds);
			
			for(BundleBO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
					List<ExtraInfoPO> extraInfos = extraInfoService.queryExtraInfoBundleId(allExtraInfos, bundle.getBundleId());
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle, extraInfos)
															.setChildren(new ArrayList<TreeNodeVO>());
					root.getChildren().add(bundleNode);
				}
			}
		}
	}
	
}
