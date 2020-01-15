package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVirtualVideoSrcVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVirtualVideoVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberChannelVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoPositionDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardSrcType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.PictureType;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.VideoServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

@Controller
@RequestMapping(value = "/virtual")
public class VirtualController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired 
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private DeviceGroupConfigVideoDAO deviceGroupConfigVideoDao;
	
	@Autowired
	private DeviceGroupConfigVideoPositionDAO deviceGroupConfigVideoPositionDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	/**
	 * @Title: 设备组成员树<br/>
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 设备组树信息
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/tree/{groupId}", method = RequestMethod.GET)
	public Object queryTree(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//返回会议基本信息
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//返回会议成员
		Set<DeviceGroupMemberPO> members = group.getMembers();
		Set<GroupMemberChannelVO> channelsVO = new HashSet<GroupMemberChannelVO>();
		
		Set<Long> folderIds = new HashSet<Long>();
		for(DeviceGroupMemberPO member: members){
			Set<DeviceGroupMemberChannelPO> channelsPO = member.getChannels();
			
			List<GroupMemberChannelVO> channelVOsList = GroupMemberChannelVO.getConverter(GroupMemberChannelVO.class).convert(channelsPO, GroupMemberChannelVO.class);
			channelsVO.addAll(channelVOsList);
			
			for(DeviceGroupMemberChannelPO channelPO:channelsPO){
				ChannelBO channelBO = new ChannelBO().set(channelPO)
													.setMemberId(member.getId());
				channels.add(channelBO);
			}
			folderIds.add(member.getFolderId());
			bundles.add(new BundleBO().set(member));
		}
		
		//根据新folderIds查询所有层级（文件夹）
		List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

		for(FolderPO allFolderPO: allFolders){
			if(allFolderPO == null) continue;
			FolderBO folderBO = new FolderBO().set(allFolderPO);
			folders.add(folderBO);			
		}
		
		//排序
		Collections.sort(bundles, new BundleBO.BundleIdComparator());
		Collections.sort(bundles, new BundleBO.BundleStatusComparator());
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, channels);
		}
		
		return _roots;
	}
	
	/**
	 * @Title: 加载虚拟源列表
	 * @param groupId 设备组id
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load/{groupId}")
	public Object load(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
        List<DeviceGroupConfigPO> configs = deviceGroupConfigDao.findByGroupIdAndType(groupId, ConfigType.VIRTUAL);

        List<DeviceGroupConfigVideoPO> videos =  new ArrayList<DeviceGroupConfigVideoPO>();
        if(configs != null && configs.size()>0){
        	for(DeviceGroupConfigPO config: configs){
        		videos.addAll(config.getVideos());
        	}
        }
        
        List<DeviceGroupVirtualVideoVO> _videos = DeviceGroupVirtualVideoVO.getConverter(DeviceGroupVirtualVideoVO.class).convert(videos, DeviceGroupVirtualVideoVO.class);
        
		JSONObject data = new JSONObject();
		data.put("rows", _videos);
		
		return data;
	}
	
	/**
	 * @Title: 保存虚拟源<br/> 
	 * @param name 虚拟源名称
	 * @throws Exception 
	 * @return DeviceGroupVirtualVideoVO 虚拟源数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/{groupId}")
	public Object save(
			@PathVariable Long groupId,
			String name,
			HttpServletRequest request) throws Exception{
		
		List<DeviceGroupConfigPO> virtualConfigs = deviceGroupConfigDao.findByGroupIdAndType(groupId, ConfigType.VIRTUAL);
		DeviceGroupConfigPO virtualConfig = virtualConfigs.get(0);
		
		DeviceGroupConfigVideoPO video = new DeviceGroupConfigVideoPO();
		video.setName(name);
		video.setConfig(virtualConfig);
		virtualConfig.getVideos().add(video);
		deviceGroupConfigVideoDao.save(video);
		
		DeviceGroupVirtualVideoVO _video = new DeviceGroupVirtualVideoVO().set(video);
		
		return _video;
	}
	
	/**
	 * @Title: 更新虚拟源<br/> 
	 * @param name 虚拟源名称
	 * @throws Exception 
	 * @return DeviceGroupVirtualVideoVO 虚拟源数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{videoId}")
	public Object update(
			@PathVariable Long videoId,
			String name,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupConfigVideoPO video = deviceGroupConfigVideoDao.findOne(videoId);
		video.setName(name);
		deviceGroupConfigVideoDao.save(video);
		
		return null;
	}
	
	/**
	 * @Title: 配置虚拟源<br/> 
	 * @param webSiteDraw 前台布局
	 * @param config 虚拟源配置
	 * @throws Exception 
	 * @return DeviceGroupVirtualVideoVO 虚拟源数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/do/config/{videoId}")
	public Object doConfig(
			@PathVariable Long videoId,
			String webSiteDraw,
			String config,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupConfigVideoPO video = deviceGroupConfigVideoDao.findOne(videoId);
		video.setWebsiteDraw(webSiteDraw);
		
		JSONArray configs = JSON.parseArray(config);
		if(configs.size()>1){
			video.setVideoOperation(VideoOperationType.COMBINE);
		}else{
			video.setVideoOperation(VideoOperationType.FORWARD);
		}
		
		//清除已有配置
		Set<DeviceGroupConfigVideoPositionPO> oldPositions = video.getPositions();
		if(oldPositions != null && oldPositions.size()>0){
			for(DeviceGroupConfigVideoPositionPO oldPosition: oldPositions){
				oldPosition.setVideo(null);
			}
			video.getPositions().removeAll(oldPositions);
			
			deviceGroupConfigVideoPositionDao.deleteInBatch(oldPositions);
		}
		
		//重新生成配置
		for(int i=0;i<configs.size();i++){
			JSONObject scopeConfig = configs.getJSONObject(i);
			JSONObject scopeSrcInfo = scopeConfig.getJSONObject("data");
			
			DeviceGroupConfigVideoPositionPO position = new DeviceGroupConfigVideoPositionPO();
			position.setUpdateTime(new Date());
			position.setPictureType(PictureType.STATIC);
			position.setH(scopeConfig.getString("h"));
			position.setW(scopeConfig.getString("w"));
			position.setX(scopeConfig.getString("x"));
			position.setY(scopeConfig.getString("y"));
			position.setSerialnum(scopeConfig.getIntValue("serialNum"));
			position.setSrcs(new ArrayList<DeviceGroupConfigVideoSrcPO>());
			position.setVideo(video);
			
			if(scopeSrcInfo != null){
				DeviceGroupConfigVideoSrcPO src = new DeviceGroupConfigVideoSrcPO();
				src.setBundleId(scopeSrcInfo.getString("bundleId"));
				src.setBundleName(scopeSrcInfo.getString("bundleName"));
				src.setChannelId(scopeSrcInfo.getString("channelId"));
				src.setChannelName(scopeSrcInfo.getString("channelName"));
				src.setMemberChannelName(scopeSrcInfo.getString("memberChannelName"));
				src.setLayerId(scopeSrcInfo.getString("layerId"));
				src.setMemberChannelId(scopeSrcInfo.getLong("channelMemberId"));
				src.setMemberId(scopeSrcInfo.getLong("memberId"));
				src.setType(ForwardSrcType.CHANNEL);
				src.setPosition(position);
				position.getSrcs().add(src);
			}
					
			video.getPositions().add(position);
		}
		
		deviceGroupConfigVideoDao.save(video);
		
		DeviceGroupPO group = video.getConfig().getGroup();
		
		if(group.getStatus().equals(GroupStatus.START)){
			LogicBO logic = videoServiceImpl.setCombineVideo(group, video, true, true, false);
			executeBusiness.execute(logic, "配置虚拟源：" + video.getName());
		}
		
		return null;
	}
	
	/**
	 * @Title: 查某个虚拟源配置<br/> 
	 * @param id 虚拟源id
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/setted/layout/{id}")
	public Object querySettedLayout(
			@PathVariable Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupConfigVideoPO video = deviceGroupConfigVideoDao.findOne(id);
		
		List<DeviceGroupConfigVideoSrcPO> srcs = new ArrayList<DeviceGroupConfigVideoSrcPO>();
		Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
		for(DeviceGroupConfigVideoPositionPO position: positions){
			srcs.addAll(position.getSrcs());
		}
		
		DeviceGroupVirtualVideoVO _video = new DeviceGroupVirtualVideoVO().set(video);
		List<DeviceGroupVirtualVideoSrcVO> _srcs = DeviceGroupVirtualVideoSrcVO.getConverter(DeviceGroupVirtualVideoSrcVO.class).convert(srcs, DeviceGroupVirtualVideoSrcVO.class);
		_video.getSrcs().addAll(_srcs);
		
		return _video;
	}
	
	/**
	 * @Title: 删除一个虚拟源<br/> 
	 * @param id 虚拟源id
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupConfigVideoPO virtualVideo = deviceGroupConfigVideoDao.findOne(id);
		DeviceGroupPO group = virtualVideo.getConfig().getGroup();
		
		LogicBO logic = new LogicBO();
	
		Set<DeviceGroupConfigPO> allConfigs = group.getConfigs();
		
		//找需要更新的合屏
		List<DeviceGroupConfigVideoPO> needUpdateVideos = new ArrayList<DeviceGroupConfigVideoPO>();
		for(DeviceGroupConfigPO config: allConfigs){
			List<DeviceGroupConfigVideoPO> needRemoveVideos = new ArrayList<DeviceGroupConfigVideoPO>();
			if(config.getVideos() != null && config.getVideos().size()>0){
				for(DeviceGroupConfigVideoPO video: config.getVideos()){
					boolean flag = false;
					if(video.getId().equals(id)){
						needRemoveVideos.add(video);
					}
					for(DeviceGroupConfigVideoPositionPO position: video.getPositions()){
						List<DeviceGroupConfigVideoSrcPO> needRemoveSrcs = new ArrayList<DeviceGroupConfigVideoSrcPO>();
						for(DeviceGroupConfigVideoSrcPO src: position.getSrcs()){	
							if(src.getType().equals(ForwardSrcType.VIRTUAL) && src.getVirtualUuid().equals(virtualVideo.getUuid())){
								needRemoveSrcs.add(src);
							}
						}
						for(DeviceGroupConfigVideoSrcPO src: needRemoveSrcs){
							src.setPosition(null);
						}
						if(needRemoveSrcs.size()>0){
							flag = true;
							position.getSrcs().removeAll(needRemoveSrcs);
						}
					}
					if(flag) needUpdateVideos.add(video);
				}

				if(needRemoveVideos.size()>0){
					for(DeviceGroupConfigVideoPO removeVideo: needRemoveVideos){
						removeVideo.setConfig(null);
						logic.merge(videoServiceImpl.removeCombineVideo(group, removeVideo.getUuid()));
					}
					config.getVideos().removeAll(needRemoveVideos);
				}
			}		
		}	
		
		if(needUpdateVideos.size()>0){
			for(DeviceGroupConfigVideoPO video: needUpdateVideos){
				logic.merge(videoServiceImpl.setCombineVideo(group, video, false, true, false));
			}
		}

		deviceGroupDao.save(group);
		
		executeBusiness.execute(logic, "删除虚拟源：" + virtualVideo.getName());
		
		return null;
	}
	
	/**
	 * @Title: 批量删除虚拟源<br/> 
	 * @param ids 虚拟源id数组
	 * @throws Exception 
	 * @return
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		deviceGroupConfigVideoDao.deleteByIdIn(ids);
		return null;
	}
	
	/**找到根节点*/
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
		for(BundleBO bundle:bundles){
			if(bundle.getFolderId().toString().equals(root.getId())){
				TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(bundleNode);
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
