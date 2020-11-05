package com.sumavision.tetris.bvc.business.group.forward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class Jv230ForwardQuery {

	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private Jv230ForwardDAO jv230ForwardDao;
	
	@Autowired
	private QtTerminalForwardDAO qtTerminalForwardDao;
	
	@Autowired
	private QtTerminalCombineVideoDAO qtTerminalCombineVideoDao;
	
	@Autowired
	private QtTerminalCombineVideoSrcDAO qtTerminalCombineVideoSrcDao;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 查询用户有权限的设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月27日 下午2:04:30
	 * @return List<TreeNodeVO> 设备列表
	 */
	public List<TreeNodeVO> queryUsableBundles() throws Exception{
		UserVO user = userQuery.current();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹以及内部文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType()) && SOURCE_TYPE.SYSTEM.equals(folder.getSourceType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(user.getId());
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			//过滤无分组设备
			if(bundleBody.getFolderId() == null) continue;
			//过滤外部设备（不要走连网）
			if(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())) continue;
			BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
					.setName(bundleBody.getBundleName())
					.setFolderId(bundleBody.getFolderId())
					.setBundleId(bundleBody.getBundleId())
					.setModel(bundleBody.getDeviceModel())
					.setNodeUid(bundleBody.getAccessNodeUid())
					.setOnlineStatus(bundleBody.getOnlineStatus().toString())
					.setLockStatus(bundleBody.getLockStatus())
					.setType(bundleBody.getBundleType())
					.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());
			bundles.add(bundle);
			bundleIds.add(bundleBody.getBundleId());
		}
		
		//根据bundleIds从资源层查询channels
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, 1);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 .setBundleId(channel.getBundleId())
													 .setChannelName(channel.getChannelName())
													 .setChannelType(channel.getBaseType());
				channels.add(channelBO);
			}
		}
		
		//过滤无通道设备
		Set<String> filteredBundleIds = new HashSet<String>();
		for(ChannelBO channel:channels){
			filteredBundleIds.add(channel.getBundleId());
		}
		List<BundleBO> filteredBundles = new ArrayList<BundleBO>();
		for(BundleBO bundle:bundles){
			if(filteredBundleIds.contains(bundle.getBundleId())){
				filteredBundles.add(bundle);
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, filteredBundles);
		}
		
		return _roots;
	}
	
	/**
	 * 查询上屏的jv230<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月17日 上午11:33:15
	 * @return List<TreeNodeVO> jv230列表
	 */
	public List<TreeNodeVO> queryForwardBundles() throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<String> bundleIds = new ArrayList<String>();
		List<String> jv230BundleIds = jv230ForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
		List<String> exceptJv230BundleIds = qtTerminalForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(user.getId().toString(), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
		if(jv230BundleIds!=null && jv230BundleIds.size()>0) bundleIds.addAll(jv230BundleIds);
		if(exceptJv230BundleIds!=null && exceptJv230BundleIds.size()>0) bundleIds.addAll(exceptJv230BundleIds);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹以及内部文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType()) && SOURCE_TYPE.SYSTEM.equals(folder.getSourceType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		if(bundleIds==null || bundleIds.size()<=0) return _roots;
		
		if(bundleIds.size()>0){
			List<BundlePO> queryBundles = bundleDao.findByBundleIdIn(bundleIds);
			if(queryBundles==null || queryBundles.size()<=0) return _roots;
			for(BundlePO bundleBody:queryBundles){
				BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
						.setName(bundleBody.getBundleName())
						.setFolderId(bundleBody.getFolderId())
						.setBundleId(bundleBody.getBundleId())
						.setModel(bundleBody.getDeviceModel())
						.setNodeUid(bundleBody.getAccessNodeUid())
						.setOnlineStatus(bundleBody.getOnlineStatus().toString())
						.setLockStatus(bundleBody.getLockStatus())
						.setType(bundleBody.getBundleType())
						.setRealType(SOURCE_TYPE.EXTERNAL.equals(bundleBody.getSourceType())?BundleBO.BundleRealType.XT.toString():BundleBO.BundleRealType.BVC.toString());
				bundles.add(bundle);
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles);
		}
		
		return _roots;
	}
	
	/**
	 * 查询jv230转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月17日 下午1:24:52
	 * @param String bundleId jv230 bundleId
	 * @return List<Jv230ForwardVO> 转发列表
	 */
	public List<Jv230ForwardVO> queryJv230Forwards(String bundleId) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		
		List<Jv230ForwardPO> forwardEntities = jv230ForwardDao.findByBundleIdAndUserIdAndTerminalIdAndBusinessTypeOrderBySerialNum(bundleId, String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
		List<Jv230ForwardVO> forwards = new ArrayList<Jv230ForwardVO>();
		if(forwardEntities!=null && forwardEntities.size()>0){
			for(int i=0; i<forwardEntities.size(); i++){
				forwards.add(new Jv230ForwardVO().set(forwardEntities.get(i)));
			}
		}
		return forwards;
	}
	
	/**
	 * 查询终端合屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月29日 上午9:23:41
	 * @return List<Jv230ForwardVO> 合屏源列表
	 */
	public List<Jv230ForwardVO> queryCombineVideo() throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<QtTerminalCombineVideoPO> combineVideos = qtTerminalCombineVideoDao.findByUserIdAndTerminalId(user.getId().toString(), terminalEntity.getId());
		QtTerminalCombineVideoPO combineVideo = null;
		if(combineVideos!=null && combineVideos.size()>0){
			combineVideo = combineVideos.get(0);
			List<QtTerminalCombineVideoSrcPO> combineVideoSrcs = qtTerminalCombineVideoSrcDao.findByQtTerminalCombineVideoId(combineVideo.getId());
			List<Jv230ForwardVO> forwards = new ArrayList<Jv230ForwardVO>();
			if(combineVideoSrcs!=null && combineVideoSrcs.size()>0){
				for(QtTerminalCombineVideoSrcPO combineVideoSrc:combineVideoSrcs){
					Jv230ForwardVO forward = new Jv230ForwardVO();
					forward.setSerialNum(combineVideoSrc.getSerialNum())
							.setX(combineVideoSrc.getX())
							.setY(combineVideoSrc.getY())
							.setW(combineVideoSrc.getW())
							.setH(combineVideoSrc.getH())
							.setBusinessName(combineVideoSrc.getBusinessName())
							.setSourceLayerId(combineVideoSrc.getSourceLayerId())
							.setSourceBundleId(combineVideoSrc.getSourceBundleId())
							.setSourceChannelId(combineVideoSrc.getSourceChannelId());
					forwards.add(forward);
				}
			}
			return forwards;
		} 
		return null;
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
			for(BundleBO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
															.setChildren(new ArrayList<TreeNodeVO>());
					root.getChildren().add(bundleNode);
				}
			}
		}
		
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:32:53
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
}
