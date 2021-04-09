package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaAudioVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVideoDstVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVideoSmallScreenSrcVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVideoSrcVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVideoVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigAudioDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSmallSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDstDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSmallScreenSrcDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSrcDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigAudioPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.AgendaServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/agenda")
public class AgendaController {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private DeviceGroupConfigVideoDAO deviceGroupConfigVideoDao;
	
	@Autowired
	private DeviceGroupConfigAudioDAO deviceGroupConfigAudioDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private DeviceGroupConfigVideoSmallSrcDAO deviceGroupConfigVideoSmallSrcDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * @Title: 查询设备组中的议程 
	 * @param groupId 设备组id
	 * @param pageSize 每页数据量
	 * @param currentPage 当前页
	 * @throws Exception 
	 * @return rows:List<DeviceGroupAgendaVO> 议程
	 * @return total 总数据量
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load/{groupId}")
	public Object load(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		Page<DeviceGroupConfigDTO> pageAgendas = deviceGroupConfigDao.findAgendaByGroupIdOutPutDTO(groupId, page);
		
		long total = pageAgendas.getTotalElements();
		List<DeviceGroupAgendaVO> _agendas = DeviceGroupAgendaVO.getConverter(DeviceGroupAgendaVO.class).convert(pageAgendas.getContent(), DeviceGroupAgendaVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _agendas);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 添加议程 
	 * @param groupId 设备组id
	 * @param name 议程名称
	 * @param remark 议程描述
	 * @throws Exception 
	 * @return DeviceGroupAgendaVO 议程
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/{groupId}")
	public Object save(
			@PathVariable Long groupId,
			String name,
			String remark,
			String audioOperation,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigPO agenda = agendaServiceImpl.save(groupId, name, remark, audioOperation);
		
		logService.logsHandle(user.getName(), "添加议程", "设备组名称："+agenda.getGroup().getName()+"议程名称："+agenda.getName());
		
		return new DeviceGroupAgendaVO().set(agenda);
	}
	
	/**
	 * @Title: 根据id删除数据 
	 * @param id
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		//解关联
		DeviceGroupConfigPO agenda = deviceGroupConfigDao.findOne(id);
		DeviceGroupPO group = agenda.getGroup();
		group.getConfigs().remove(agenda);
		agenda.setGroup(null);
		
		logService.logsHandle(user.getName(), "删除议程", "设备组名称："+group.getName()+"议程名称："+agenda.getName());
		
		deviceGroupConfigDao.delete(agenda);
	
		return null;
	}
	
	/**
	 * @Title: 获取议程中的视频配置 
	 * @param agendaId 议程id
	 * @throws Exception 
	 * @return List<DeviceGroupAgendaVideoVO> 视频配置 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/videos/{agendaId}")
	public Object queryVideos(
			@PathVariable Long agendaId,
			HttpServletRequest request) throws Exception{
		
		//查询全部的视频配置
		List<DeviceGroupConfigVideoDTO> videos = deviceGroupConfigVideoDao.findByConfigIdOutPutDTO(agendaId);
		List<DeviceGroupAgendaVideoVO> _videos = DeviceGroupAgendaVideoVO.getConverter(DeviceGroupAgendaVideoVO.class).convert(videos, DeviceGroupAgendaVideoVO.class);
		
		Long groupId = deviceGroupConfigDao.findGroupIdByConfigId(agendaId);
		List<Long> roleIds = deviceGroupRecordSchemeDao.findRoleIdsByGroupId(groupId);
		
		if(_videos!=null && _videos.size()>0){
			//获取视频id
			Set<Long> videoIds = new HashSet<Long>();
			for(DeviceGroupConfigVideoDTO video:videos){
				videoIds.add(video.getId());
			}
			
			//获取全部的视频配置源
			List<DeviceGroupConfigVideoSrcDTO> srcs = deviceGroupConfigVideoSrcDao.findByVideoIds(videoIds);
			//获取全部视频配置转发目的
			List<DeviceGroupConfigVideoDstDTO> dsts = deviceGroupConfigVideoDstDao.findByVideoIds(videoIds);
			//获取全部的视频小屏配置源
			List<DeviceGroupConfigVideoSmallScreenSrcDTO> smalls = deviceGroupConfigVideoSmallSrcDao.findByVideoIds(videoIds);
			
			for(DeviceGroupAgendaVideoVO _video:_videos){
				_video.setSrcs(new ArrayList<DeviceGroupAgendaVideoSrcVO>());
				_video.setDsts(new ArrayList<DeviceGroupAgendaVideoDstVO>());
				
				//添加源
				for(DeviceGroupConfigVideoSrcDTO src:srcs){
					if(src.getVideoId().equals(_video.getId())){
						_video.getSrcs().add(new DeviceGroupAgendaVideoSrcVO().set(src));
					}
				}
				
				//添加小屏源
				for(DeviceGroupConfigVideoSmallScreenSrcDTO small: smalls){
					if(small.getVideoId().equals(_video.getId())){
						_video.setSmall(new DeviceGroupAgendaVideoSmallScreenSrcVO().set(small));
					}
				}
				
				//添加目的
				for(DeviceGroupConfigVideoDstDTO dst:dsts){
					if(dst.getVideoId().equals(_video.getId())){
						if(dst.getRoleId() != null){
							if(roleIds.contains(dst.getRoleId())){
								_video.getDsts().add(new DeviceGroupAgendaVideoDstVO().set(dst)
																					  .setRoleType(BusinessRoleType.RECORDABLE));
							}else{
								_video.getDsts().add(new DeviceGroupAgendaVideoDstVO().set(dst)
										  											  .setRoleType(BusinessRoleType.DEFAULT));
							}
						}else{
							_video.getDsts().add(new DeviceGroupAgendaVideoDstVO().set(dst));
						}
					}
				}
				
				//排序
				Collections.sort(_video.getSrcs(), new DeviceGroupAgendaVideoSrcVO.SerialNumAscSorter());
			}
		}
		
		return _videos;
	}
	
	/**
	 * @Title: 获取议程中的音频配置 
	 * @param agendaId 议程id
	 * @throws Exception 
	 * @return List<DeviceGroupAgendaVideoVO> 视频配置 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/audio/{groupId}/{agendaId}")
	public Object queryAudio(
			@PathVariable Long groupId,
			@PathVariable Long agendaId,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//返回会议基本信息
		List<TreeNodeVO> bundleTrees = new ArrayList<TreeNodeVO>();
		List<DeviceGroupMemberDTO> members = deviceGroupMemberDao.findGroupMembersByGroupId(groupId);
		
		for(DeviceGroupMemberDTO member: members){
			DeviceGroupMemberChannelPO audioChannel = queryUtil.queryEncodeAudioChannel(group, member.getId());
			if(audioChannel != null){
				BundleBO bundle = new BundleBO().set(member);
				TreeNodeVO node = new TreeNodeVO().set(bundle); 
				//扩展param
				JSONObject param = JSON.parseObject(node.getParam());
				param.put("channelId", audioChannel.getChannelId());
				param.put("channelName", audioChannel.getChannelName());
				param.put("memberChannelId", audioChannel.getId());
				node.setParam(param.toJSONString());
				bundleTrees.add(node);
			}
		}
		
		List<DeviceGroupConfigAudioPO> audioPOs = deviceGroupConfigAudioDao.findByConfigId(agendaId);
		List<DeviceGroupAgendaAudioVO> audioVOs = DeviceGroupAgendaAudioVO.getConverter(DeviceGroupAgendaAudioVO.class).convert(audioPOs, DeviceGroupAgendaAudioVO.class);
		
		int volume = deviceGroupConfigDao.findVolumeByConfigId(agendaId);
		
		return new HashMapWrapper<String, Object>().put("bundles", bundleTrees)
												   .put("audios", audioVOs)
												   .put("volume", volume)
				   								   .getMap();
	}
	
	/**
	 * @Title: 添加默认的视频配置  这个东西现在只加不删<br/>
	 * @Description: 默认配置成2*2四分屏
	 * @param agendaId 议程id
	 * @param name 视频名称
	 * @throws Exception 
	 * @return DeviceGroupAgendaVideoVO 议程视频
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/video/{agendaId}")
	public Object addDefaultVideo(
			@PathVariable Long agendaId,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO video = agendaServiceImpl.addDefaultVideo(agendaId, name);
		
		logService.logsHandle(user.getName(), "添加议程视频配置", "议程名称："+video.getConfig().getName()+"视频名称："+video.getName());
		
		return new DeviceGroupAgendaVideoVO().set(video)
											 .setDsts(new ArrayList<DeviceGroupAgendaVideoDstVO>());
	}
	
	/**
	 * @Title: 修改一个议程的视频配置<br/> 
	 * @param videoId 议程视频id
	 * @param websiteDraw 前端渲染
	 * @param position 分屏布局以及源
	 * @param dst 转发的设备目的
	 * @param roleDst 转发的角色目的
	 * @throws Exception 
	 * @param DeviceGroupAgendaVideoVO 议程视频
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/video/{videoId}")
	public Object updateVideo(
			@PathVariable Long videoId,
			String websiteDraw,
			String position,
			String dst,
			String roleDst,
			String layout,
			String smallScreen,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO video = agendaServiceImpl.updateVideo(videoId, websiteDraw, position, dst, roleDst, layout, smallScreen);
		
		List<Long> roleIds = deviceGroupRecordSchemeDao.findRoleIdsByGroupId(video.getConfig().getGroup().getId());
		
		DeviceGroupAgendaVideoVO _video = new DeviceGroupAgendaVideoVO().set(video)
																		.setSrcs(new ArrayList<DeviceGroupAgendaVideoSrcVO>())
																		.setDsts(new ArrayList<DeviceGroupAgendaVideoDstVO>());
		Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
		for(DeviceGroupConfigVideoPositionPO p:positions){
			List<DeviceGroupConfigVideoSrcPO> srcs = p.getSrcs();
			if(srcs != null){
				for(DeviceGroupConfigVideoSrcPO src:srcs){
					_video.getSrcs().add(new DeviceGroupAgendaVideoSrcVO().set(src, p.getSerialnum(), p.getPictureType(), p.getPollingTime(), p.getPollingStatus()));
				}
			}
		}
		
		Set<DeviceGroupConfigVideoDstPO> dstPOs = video.getDsts();
		if(dstPOs != null){
			for(DeviceGroupConfigVideoDstPO dstPO: dstPOs){
				if(dstPO.getRoleId() != null){
					if(roleIds.contains(dstPO.getRoleId())){
						_video.getDsts().add(new DeviceGroupAgendaVideoDstVO().set(dstPO)
																			  .setRoleType(BusinessRoleType.RECORDABLE));
					}else{
						_video.getDsts().add(new DeviceGroupAgendaVideoDstVO().set(dstPO)
								  											  .setRoleType(BusinessRoleType.DEFAULT));
					}
				}else{
					_video.getDsts().add(new DeviceGroupAgendaVideoDstVO().set(dstPO));
				}				
			}
		}
		
		if(video.getSmall() != null){
			_video.setSmall(new DeviceGroupAgendaVideoSmallScreenSrcVO().set(video.getSmall()));
		}
		
		logService.logsHandle(user.getName(), "修改议程视频配置", "议程名称："+video.getConfig().getName()+"视频名称："+video.getName());
		
		return _video;
	}
	
	/**
	 * @Title: 修改一个议程的音频<br/> 
	 * @param agendaId 议程id
	 * @param audioList 音频列表
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/audio/{agendaId}")
	public Object updateAudio(
			@PathVariable Long agendaId,
			String audioList,
			int volume,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigPO agendaPO = deviceGroupConfigDao.findOne(agendaId);	
		
		//设置音量
		agendaPO.setVolume(volume);
		
		//清除音频
		Set<DeviceGroupConfigAudioPO> audios = agendaPO.getAudios();
		if(audios != null){
			for(DeviceGroupConfigAudioPO audio: audios){
				audio.setConfig(null);
			}
			agendaPO.getAudios().removeAll(audios);
		}else{
			agendaPO.setAudios(new HashSet<DeviceGroupConfigAudioPO>());
		}
		
		//解析音频
		JSONArray audioArr = JSON.parseArray(audioList);
		if(audioArr != null && audioArr.size()>0){
			for(int i=0;i<audioArr.size();i++){
				JSONObject audioObject = audioArr.getJSONObject(i);
				String paramStr = audioObject.getString("param");
				JSONObject paramObject = JSONObject.parseObject(paramStr);
//				JSONObject paramObject = audioObject.getJSONObject("param");
				DeviceGroupConfigAudioPO audioPO = new DeviceGroupConfigAudioPO();
				audioPO.setBundleId(paramObject.getString("bundleId"));
				audioPO.setBundleName(audioObject.getString("name"));
				audioPO.setLayerId(paramObject.getString("nodeUid"));
				audioPO.setMemberId(paramObject.getLong("memberId"));
				audioPO.setChannelId(paramObject.getString("channelId"));
				audioPO.setChannelName(paramObject.getString("channelName"));
				audioPO.setMemberChannelId(paramObject.getLong("memberChannelId"));
				audioPO.setConfig(agendaPO);
				audios.add(audioPO);
			}
		}
		
		deviceGroupConfigDao.save(agendaPO);
		
		logService.logsHandle(user.getName(), "修改议程音频", "议程名称："+agendaPO.getName());
		
		return null;
	}
	
	/**
	 * @Title: 执行议程 
	 * @param groupId 设备组id
	 * @param agendaId 议程id
	 * @throws Exception 
	 * @return Object 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/run/{groupId}/{agendaId}")
	public Object run(
			@PathVariable Long groupId,
			@PathVariable Long agendaId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigPO agenda = agendaServiceImpl.run(groupId, agendaId);
		
		logService.logsHandle(user.getName(), "执行议程", "议程名称："+agenda.getName());
		
		return null;
	}
	
}
