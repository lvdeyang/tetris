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

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupSchemeVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupSchemeVideoDstVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupSchemeVideoSrcVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupSchemeVideoVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoDstDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupConfigVideoSrcDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.SchemeServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/scheme")
public class SchemeController {

	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private DeviceGroupConfigVideoDAO deviceGroupConfigVideoDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private SchemeServiceImpl schemeServiceImpl;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * @Title: 查询设备组中的方案
	 * @param groupId 设备组id
	 * @param pageSize 每页数据量
	 * @param currentPage 当前页
	 * @throws Exception 
	 * @return rows:List<DeviceGroupSchemeVO> 议程
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
		Page<DeviceGroupConfigDTO> pageSchemes = deviceGroupConfigDao.findSchemeByGroupIdOutPutDTO(groupId, page);
		
		long total = pageSchemes.getTotalElements();
		List<DeviceGroupSchemeVO> _schemes = DeviceGroupSchemeVO.getConverter(DeviceGroupSchemeVO.class).convert(pageSchemes.getContent(), DeviceGroupSchemeVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _schemes);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 添加方案 
	 * @param groupId 设备组id
	 * @param name 方案名称
	 * @param remark 方案描述
	 * @throws Exception 
	 * @return DeviceGroupAgendaVO 方案
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/{groupId}")
	public Object save(
			@PathVariable Long groupId,
			String name,
			String remark,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigPO scheme = schemeServiceImpl.save(groupId, name, remark);
		
		logService.logsHandle(user.getName(), "添加方案", "设备组名称："+scheme.getGroup().getName()+"方案名称："+scheme.getName());
		
		return new DeviceGroupSchemeVO().set(scheme);
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
		DeviceGroupConfigPO scheme = deviceGroupConfigDao.findOne(id);
		DeviceGroupPO group = scheme.getGroup();
		group.getConfigs().remove(scheme);
		scheme.setGroup(null);
		deviceGroupConfigDao.delete(scheme);
		
		logService.logsHandle(user.getName(), "删除方案", "设备组名称："+group.getName()+"方案名称："+scheme.getName());
		
		return null;
	}
	
	/**
	* @Title 获取方案中的视频配置
	* @param schemeId 方案id
	* @throws Exception    
	* @return List<DeviceGroupSchemeVideoVO> 视频配置 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/videos/{schemeId}")
	public Object queryVideos(
			@PathVariable Long schemeId,
			HttpServletRequest request) throws Exception{
		
		//查询全部的视频配置
		List<DeviceGroupConfigVideoDTO> videos = deviceGroupConfigVideoDao.findByConfigIdOutPutDTO(schemeId);
		List<DeviceGroupSchemeVideoVO> _videos = DeviceGroupSchemeVideoVO.getConverter(DeviceGroupSchemeVideoVO.class).convert(videos, DeviceGroupSchemeVideoVO.class);

		Long groupId = deviceGroupConfigDao.findGroupIdByConfigId(schemeId);
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
			
			
			for(DeviceGroupSchemeVideoVO _video:_videos){
				_video.setSrcs(new ArrayList<DeviceGroupSchemeVideoSrcVO>());
				_video.setDsts(new ArrayList<DeviceGroupSchemeVideoDstVO>());
				
				//添加源
				for(DeviceGroupConfigVideoSrcDTO src:srcs){
					if(src.getVideoId().equals(_video.getId())){
						_video.getSrcs().add(new DeviceGroupSchemeVideoSrcVO().set(src));
					}
				}
				
				//添加目的
				for(DeviceGroupConfigVideoDstDTO dst:dsts){
					if(dst.getVideoId().equals(_video.getId())){
						if(dst.getRoleId() != null){
							if(roleIds.contains(dst.getRoleId())){
								_video.getDsts().add(new DeviceGroupSchemeVideoDstVO().set(dst)
																					  .setRoleType(BusinessRoleType.RECORDABLE));
							}else{
								_video.getDsts().add(new DeviceGroupSchemeVideoDstVO().set(dst)
										  											  .setRoleType(BusinessRoleType.DEFAULT));
							}
						}else{
							_video.getDsts().add(new DeviceGroupSchemeVideoDstVO().set(dst));
						}
					}
				}
				
				//排序
				Collections.sort(_video.getSrcs(), new DeviceGroupSchemeVideoSrcVO.SerialNumAscSorter());
			}
		}
		
		return _videos;
	}
	
	/**
	 * @Title: 添加默认的视频配置  这个东西现在只加不删<br/>
	 * @Description: 默认配置成2*2四分屏
	 * @param schemeId 方案id
	 * @param name 视频名称
	 * @throws Exception 
	 * @return DeviceGroupSchemeVideoVO 方案视频
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/video/{schemeId}")
	public Object addDefaultVideo(
			@PathVariable Long schemeId,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO video = schemeServiceImpl.addDefaultVideo(schemeId, name);
		
		logService.logsHandle(user.getName(), "添加方案视频配置", "方案名称："+video.getConfig().getName()+"视频名称："+video.getName());
		
		return new DeviceGroupSchemeVideoVO().set(video)
											 .setDsts(new ArrayList<DeviceGroupSchemeVideoDstVO>());
	}
	
	/**
	 * @Title: 修改一个方案的视频配置<br/> 
	 * @param videoId 方案视频id
	 * @param websiteDraw 前端渲染
	 * @param position 分屏布局以及源
	 * @throws Exception 
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
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO video = schemeServiceImpl.updateVideo(videoId, websiteDraw, position, dst, roleDst, layout);
		
		List<Long> roleIds = deviceGroupRecordSchemeDao.findRoleIdsByGroupId(video.getConfig().getGroup().getId());
		
		DeviceGroupSchemeVideoVO _video = new DeviceGroupSchemeVideoVO().set(video)
																		.setSrcs(new ArrayList<DeviceGroupSchemeVideoSrcVO>())
																		.setDsts(new ArrayList<DeviceGroupSchemeVideoDstVO>());
		Set<DeviceGroupConfigVideoPositionPO> positions = video.getPositions();
		for(DeviceGroupConfigVideoPositionPO p:positions){
			List<DeviceGroupConfigVideoSrcPO> srcs = p.getSrcs();
			if(srcs != null){
				for(DeviceGroupConfigVideoSrcPO src:srcs){
					_video.getSrcs().add(new DeviceGroupSchemeVideoSrcVO().set(src, p.getSerialnum(), p.getPictureType(), p.getPollingTime(), p.getPollingStatus()));
				}
			}
		}
		
		Set<DeviceGroupConfigVideoDstPO> dstPOs = video.getDsts();
		if(dstPOs != null){
			for(DeviceGroupConfigVideoDstPO dstPO: dstPOs){
				if(dstPO.getRoleId() != null){
					if(roleIds.contains(dstPO.getRoleId())){
						_video.getDsts().add(new DeviceGroupSchemeVideoDstVO().set(dstPO)
																			  .setRoleType(BusinessRoleType.RECORDABLE));
					}else{
						_video.getDsts().add(new DeviceGroupSchemeVideoDstVO().set(dstPO)
								  											  .setRoleType(BusinessRoleType.DEFAULT));
					}
				}else{
					_video.getDsts().add(new DeviceGroupSchemeVideoDstVO().set(dstPO));
				}				
			}
		}
		
		logService.logsHandle(user.getName(), "修改方案视频配置", "方案名称："+video.getConfig().getName()+"视频名称："+video.getName());
		
		return _video;
	}
	
	/**
	 * @Title: 执行方案
	 * @param groupId 设备组id
	 * @param schemeId 方案id
	 * @throws Exception 
	 * @return Object 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/run/{groupId}/{videoId}")
	public Object run(
			@PathVariable Long groupId,
			@PathVariable Long videoId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupConfigVideoPO video = schemeServiceImpl.run(groupId, videoId);
		
		logService.logsHandle(user.getName(), "执行方案", "方案视频名称："+video.getName());
		
		return null;
	}
	
	/**
	 * @Title: 开始录制一个视频 
	 * @param groupId 设备组id
	 * @param videoId 视频id
	 * @throws Exception 
	 * @return Object 返回类型 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start/record/{groupId}/{videoId}")
	public Object startRecord(
			@PathVariable Long groupId,
			@PathVariable Long videoId,
			HttpServletRequest request) throws Exception{
		
		schemeServiceImpl.startRecord(groupId, videoId);
		
		return null;
	}
	
	/**
	 * @Title: 停止录制一个视频 
	 * @param groupId 设备组id
	 * @param videoId 视频id
	 * @throws Exception 
	 * @return Object 返回类型 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/record/{groupId}/{videoId}")
	public Object stopRecord(
			@PathVariable Long groupId,
			@PathVariable Long videoId,
			HttpServletRequest request) throws Exception{
		
		schemeServiceImpl.stopRecord(groupId, videoId);
		
		return null;
	}
	
}
