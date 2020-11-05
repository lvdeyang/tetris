package com.sumavision.bvc.control.device.monitor.live;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.live.MonitorLiveSplitConfigDAO;
import com.sumavision.bvc.device.monitor.live.MonitorLiveSplitConfigPO;
import com.sumavision.bvc.device.monitor.live.MonitorLiveSplitConfigService;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/live/split/config")
public class MonitorLiveSplitConfigController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorLiveSplitConfigDAO monitorLiveSplitConfigDAO;
	
	@Autowired
	private MonitorLiveSplitConfigService monitorLiveSplitConfigService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	/**
	 * 查询当前用户某个屏幕的配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午1:09:00
	 * @param Integer serial 分屏序号
	 * @return List<MonitorLiveSplitConfigPO> 配置列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/serial")
	public Object findBySerial(
			Integer serial,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<MonitorLiveSplitConfigPO> entities = monitorLiveSplitConfigDAO.findByUserIdAndSerial(userId, serial);
		
		return MonitorLiveSplitConfigVO.getConverter(MonitorLiveSplitConfigVO.class).convert(entities, MonitorLiveSplitConfigVO.class);
	}
	
	/**
	 * 添加屏幕配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午1:46:06
	 * @param Integer serial 屏幕序号
	 * @param String dstVideoBundleId 目标视频设备id
	 * @param String dstVideoBundleName 目标视频设备名称
	 * @param String dstVideoBundleType 目标视频设备类型
	 * @param String dstVideoLayerId 目标视频设备层id
	 * @param String dstVideoChannelId 目标视频设备通道id
	 * @param String dstVideoBaseType 目标视频设备通道类型
	 * @param String dstVideoChannelName 目标视频设备通道名称
	 * @param String dstAudioBundleId 目标音频设备id
	 * @param String dstAudioBundleName 目标音频设备名称
	 * @param String dstAudioBundleType 目标音频设备类型
	 * @param String dstAudioLayerId 目标音频设备层id
	 * @param String dstAudioChannelId 目标音频设备通道id
	 * @param String dstAudioBaseType 目标音频设备通道类型
	 * @param String dstAudioChannelName 目标音频设备通道名称
	 * @return MonitorLiveSplitConfigVO 屏幕配置
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Integer serial,
			String dstVideoBundleId,
			String dstVideoBundleName,
			String dstVideoBundleType,
			String dstVideoLayerId,
			String dstVideoChannelId,
			String dstVideoBaseType,
			String dstVideoChannelName,
			String dstAudioBundleId,
			String dstAudioBundleName,
			String dstAudioBundleType,
			String dstAudioLayerId,
			String dstAudioChannelId,
			String dstAudioBaseType,
			String dstAudioChannelName,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorLiveSplitConfigPO entity = monitorLiveSplitConfigService.add(
				userId, serial, 
				dstVideoBundleId, dstVideoBundleName, dstVideoBundleType, dstVideoLayerId, dstVideoChannelId, dstVideoBaseType, dstVideoChannelName, 
				dstAudioBundleId, dstAudioBundleName, dstAudioBundleType, dstAudioLayerId, dstAudioChannelId, dstAudioBaseType, dstAudioChannelName);
		
		return new MonitorLiveSplitConfigVO().set(entity); 
	}
	/**
	 * 删除配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午2:23:43
	 * @param Long id 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id, 
			HttpServletRequest request) throws Exception{
		monitorLiveSplitConfigService.remove(id);
		return null;
	}
	
	/**
	 * 查询解码设备组织树（过滤配置）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午2:04:32
	 * @param Integer serial 屏幕序号
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/decode/institution/tree")
	public Object findDecodeInstitutionTree(
			Integer serial,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();	
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		
		if(queryBundles==null || queryBundles.size()<=0) return _roots;
		List<String> bundleIds = new ArrayList<String>();
		for(BundlePO bundleBody:queryBundles){
			if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
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
		}
		
		//查询配置
		List<MonitorLiveSplitConfigPO> configs = monitorLiveSplitConfigDAO.findByUserIdAndSerial(userId, serial);
		
		//根据bundleIds从资源层查询channels，并过滤已经配置的设备通道
		List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.findByBundleIdsAndChannelType(bundleIds, 1);
		if(queryChannels != null){
			for(ChannelSchemeDTO channel:queryChannels){
				if(configs != null){
					boolean finded = false;
					for(MonitorLiveSplitConfigPO config:configs){
						if((config.getDstVideoBundleId().equals(channel.getBundleId())&&config.getDstVideoChannelId().equals(channel.getChannelId())) ||
								(config.getDstAudioBundleId()!=null&&config.getDstAudioBundleId().equals(channel.getBundleId())&&config.getDstAudioChannelId().equals(channel.getChannelId()))){
							finded = true;
							break;
						}
					}
					if(finded) continue;
				}
				ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													 //起别名
												     .setName(ChannelType.transChannelName(channel.getChannelId()))
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
			recursionFolder(_root, folders, filteredBundles, channels);
		}
		
		return _roots;
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
	
	/**
	 * 递归组文件夹层级<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月30日 下午2:21:08
	 * @param TreeNodeVO root 根节点
	 * @param List<FolderBO> folders 文件夹列表
	 * @param List<BundleBO> bundles 设备列表
	 * @param List<ChannelBO> channels 通道列表
	 */ 
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels);
			}
		}
		
		//往里装设备
		if(bundles!=null && bundles.size()>0){
			for(BundleBO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
															.setChildren(new ArrayList<TreeNodeVO>());
					root.getChildren().add(bundleNode);
					if(!BundleBO.BundleRealType.XT.toString().equals(bundle.getRealType()) && channels!=null && channels.size()>0){
						for(ChannelBO channel:channels){
							if(channel.getBundleId().equals(bundleNode.getId())){
								TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
								bundleNode.getChildren().add(channelNode);
							}
						}
					}
				}
			}
		}
		
	}
	
}
