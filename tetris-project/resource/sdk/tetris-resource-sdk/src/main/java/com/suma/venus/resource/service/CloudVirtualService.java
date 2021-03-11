package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;


@Service
public class CloudVirtualService {
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	@Autowired
	private ChannelSchemeDao channelSchemeDao;
	
	@Autowired
	private ExtraInfoDao extraInfoDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private PrivilegeDAO privilegeDAO;
	

	/**
	 * 添加虚拟设备-输出<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月2日 下午5:41:37
	 * @param bundleName 设备名称
	 * @param url 输入源地址
	 * @param rateCtrl 码率控制方式 
	 * @param bitrate 系统码率
	 * @param videos 视频通道
	 * @param audios 音频通道
	 * @return
	 */
	public Long outputAdd(String bundleName,String type, String url, String rateCtrl, String bitrate, String videos, String audios) throws Exception {

		BundlePO bundlePO = new BundlePO();
		bundlePO.setBundleName(bundleName);
		bundlePO.setBundleId(BundlePO.createBundleId());
		bundlePO.setRateCtrl(rateCtrl);
        bundlePO.setStreamUrl(url);
        bundlePO.setType(type);
		bundlePO.setBitrate(bitrate);
		bundlePO.setDeviceModel("virtualOut");
		bundleDao.save(bundlePO);
		createAudioAndVideoChannelOut(bundlePO,videos,audios);
		UserVO userVO = userQuery.current();
		SystemRoleVO systemRoleVO = userQueryService.queryPrivateRoleId(userVO.getId());
		List<String> resource = new ArrayList<String>();
		resource.add(bundlePO.getBundleId() + "-r");
		RoleAndResourceIdBO bo = new RoleAndResourceIdBO();
		bo.setRoleId(Long.valueOf(systemRoleVO.getId()));
		bo.setResourceCodes(resource);
		userQueryService.bindRolePrivilege(bo);
		return bundlePO.getId();
	}

	/**
	 * 添加虚拟设备-输入<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:13:22
	 * @param bundleJson 虚拟输入设备信息
	 */
	public Object inputAdd(JSONObject bundleJson) throws Exception {
		BundlePO bundlePO = new BundlePO();
		bundlePO.setBundleId(bundleJson.getString("BundleId"));
		bundlePO.setBundleName(bundleJson.getString("BundleName"));
		bundlePO.setUrl(bundleJson.getString("url"));
		bundlePO.setType(bundleJson.getString("type"));
		bundlePO.setDeviceModel("virtualIn");
		JSONArray programs = bundleJson.getJSONArray("programs");
		bundleDao.save(bundlePO);
		createAudioAndVideoChannelIn(bundlePO,programs);
		
		bundleDao.save(bundlePO);
		
		return null;
	}

	/**
	 * 创建虚拟输入设备的通道<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:14:21
	 * @param bundlePO 虚拟设备
	 * @param programs 通道参数
	 * @return
	 */
	public List<ChannelSchemePO> createAudioAndVideoChannelIn(BundlePO bundlePO,JSONArray programs)throws Exception{
		ChannelTemplatePO audioChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundlePO.getDeviceModel(), "VenusAudioIn");
		ChannelTemplatePO videoChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundlePO.getDeviceModel(), "VenusVideoIn");
		List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
		if(programs != null && programs.size()>0){
			List<ExtraInfoPO> extraInfoPOs = new ArrayList<ExtraInfoPO>();
			for (int i = 0; i < programs.size(); i++) {
				List<ChannelSchemePO> audioChannelSchemePOs = new ArrayList<ChannelSchemePO>();
				List<ChannelSchemePO> videoChannelSchemePOs = new ArrayList<ChannelSchemePO>();
				JSONObject program = programs.getJSONObject(i);
				JSONArray videos = program.getJSONArray("videos");
				JSONArray audios = program.getJSONArray("audios");
				String num = program.getString("num");
				String name = program.getString("name");
				if (null != audioChannelTemplate && null != videoChannelTemplate) {
					audioChannelSchemePOs.addAll(getAudioChannelSchemePO(bundlePO.getBundleId(), audioChannelTemplate,audios));
					videoChannelSchemePOs.addAll(getVideoChannelSchemePO(bundlePO.getBundleId(), videoChannelTemplate,videos));
				}
				Map<String, Object> pro = new HashMap<String, Object>();
				pro.put("num", num);
				pro.put("name", name);
				pro.put("videos", videoChannelSchemePOs);
				pro.put("audios", audioChannelSchemePOs);
				
				channelSchemePOs.addAll(videoChannelSchemePOs);
				channelSchemePOs.addAll(audioChannelSchemePOs);
				
				ExtraInfoPO extraInfoPO = new ExtraInfoPO();
				extraInfoPO.setBundleId(bundlePO.getBundleId());
				extraInfoPO.setName("program");
				extraInfoPO.setValue(pro.toString());
				extraInfoPOs.add(extraInfoPO);
			}
			channelSchemeDao.save(channelSchemePOs);
			extraInfoDao.save(extraInfoPOs);
		}
		
		return channelSchemePOs;
	}
	
	/**
	 * 创建虚拟输出设备的通道<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:14:21
	 * @param bundlePO 虚拟设备
	 * @param videos 视频通道参数
	 * @param audios 音频通道参数
	 * @return
	 */
	public List<ChannelSchemePO> createAudioAndVideoChannelOut(BundlePO bundlePO, String videos, String audios) {
		ChannelTemplatePO audioChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundlePO.getDeviceModel(), "VenusAudioOut");
		ChannelTemplatePO videoChannelTemplate = channelTemplateDao.findByDeviceModelAndChannelName(bundlePO.getDeviceModel(), "VenusVideoOut");
		JSONArray videosArray = JSONArray.parseArray(videos);
		JSONArray audiosArray = JSONArray.parseArray(audios);
		List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
		channelSchemePOs.addAll(getVideoChannelSchemePO(bundlePO.getBundleId(), videoChannelTemplate, videosArray));
		channelSchemePOs.addAll(getAudioChannelSchemePO(bundlePO.getBundleId(), audioChannelTemplate, audiosArray));
		channelSchemeDao.save(channelSchemePOs);
		return channelSchemePOs;
	}
	
	/**
	 * 添加视频通道<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:17:33
	 * @param bundleId 设备id
	 * @param channelTemplatePO 通道模板
	 * @param jsonArray 通道参数
	 * @return
	 */
	public List<ChannelSchemePO> getVideoChannelSchemePO(String bundleId, ChannelTemplatePO channelTemplatePO,JSONArray jsonArray){
		List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
		if (jsonArray != null && jsonArray.size()>0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				ChannelSchemePO channelSchemePO = new ChannelSchemePO();
				
				JSONObject channel = jsonArray.getJSONObject(i);
				channelSchemePO.setBundleId(bundleId);
				channelSchemePO.setChannelId(channelTemplatePO.getBaseType() + "_" + 1);
				channelSchemePO.setChannelName(channelTemplatePO.getChannelName());
				channelSchemePO.setChannelTemplateID(channelTemplatePO.getId());
				channelSchemePO.setCodec(channel.getString("codec"));
				channelSchemePO.setResolution(channel.getString("resolution"));
				channelSchemePO.setBitrate(channel.getString("bitrate"));
				channelSchemePO.setFps(channel.getString("fps"));
				channelSchemePOs.add(channelSchemePO);
			}
		}
		return channelSchemePOs;
	}
	
	/**
	 * 创建音频通道<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:18:21
	 * @param bundleId 设备id
	 * @param channelTemplatePO 通道模板
	 * @param jsonArray 通道参数
	 */
	public List<ChannelSchemePO> getAudioChannelSchemePO(String bundleId, ChannelTemplatePO channelTemplatePO,JSONArray jsonArray){
		List<ChannelSchemePO> channelSchemePOs = new ArrayList<ChannelSchemePO>();
		if (jsonArray != null && jsonArray.size()>0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				ChannelSchemePO channelSchemePO = new ChannelSchemePO();
				
				JSONObject channel = jsonArray.getJSONObject(i);
				channelSchemePO.setBundleId(bundleId);
				
				channelSchemePO.setChannelId(channelTemplatePO.getBaseType() + "_" + i++);
				channelSchemePO.setChannelName(channelTemplatePO.getChannelName());
				channelSchemePO.setChannelTemplateID(channelTemplatePO.getId());
				channelSchemePO.setCodec(channel.getString("codec"));
				channelSchemePO.setResolution(channel.getString("samplateRate"));
				channelSchemePO.setBitrate(channel.getString("bitrate"));
				channelSchemePOs.add(channelSchemePO);
			}
		}
		return channelSchemePOs;
	}

	/**
	 * 添加虚拟设备-输出<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午1:59:27
	 * @param bundleName 设备名称
	 * @param bundleId 设备id
	 * @param url 输入源地址
	 * @param rateCtrl 码率控制方式 
	 * @param bitrate 系统码率
	 * @param videos 视频通道
	 * @param audios 音频通道
	 * @return
	 */
	public Object outputModify(String bundleName,String bundleId, String url, String rateCtrl, String bitrate, String videos, String audios) throws Exception {
		BundlePO bundlePO = bundleDao.findByBundleId(bundleId);
		bundlePO.setBundleName(bundleName);
		bundlePO.setUrl(url);
		bundlePO.setRateCtrl(rateCtrl);
		bundlePO.setBitrate(bitrate);
		bundleDao.save(bundlePO);
		List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findByBundleId(bundleId);
		JSONArray videosArray = JSONArray.parseArray(videos);
		JSONArray audiosArray = JSONArray.parseArray(audios);
		List<ChannelSchemePO> newChannelSchemePOs = new ArrayList<ChannelSchemePO>();
		if (videosArray != null&& videosArray.size()>0) {
			for (int i = 0; i < videosArray.size(); i++) {
				JSONObject channel = videosArray.getJSONObject(i);
				
				ChannelSchemePO channelSchemePO = new ChannelSchemePO();
				channelSchemePO.setBundleId(bundleId);
				channelSchemePO.setChannelId(channel.getString("channelId"));
				channelSchemePO.setCodec(channel.getString("codec"));
				channelSchemePO.setResolution(channel.getString("resolution"));
				channelSchemePO.setBitrate(channel.getString("bitrate"));
				channelSchemePO.setFps(channel.getString("fps"));
				newChannelSchemePOs.add(channelSchemePO);
			}
		}
		if (audiosArray != null&& audiosArray.size()>0) {
			for (int i = 0; i < audiosArray.size(); i++) {
				JSONObject channel = audiosArray.getJSONObject(i);
				
				ChannelSchemePO channelSchemePO = new ChannelSchemePO();
				channelSchemePO.setBundleId(bundleId);
				channelSchemePO.setChannelId(channel.getString("channelId"));
				channelSchemePO.setCodec(channel.getString("codec"));
				channelSchemePO.setSamplateRate(channel.getString("samplateRate"));
				channelSchemePO.setBitrate(channel.getString("bitrate"));
				newChannelSchemePOs.add(channelSchemePO);
			}
		}
		updateChannel(channelSchemePOs,newChannelSchemePOs);
		
		return null;
	}
	
	
	public void updateChannel(List<ChannelSchemePO> old,List<ChannelSchemePO> now)throws Exception{
		List<ChannelSchemePO> change = new ArrayList<ChannelSchemePO>();
		List<ChannelSchemePO> useless = new ArrayList<ChannelSchemePO>();
		if (now != null && now.size() >0) {
			for (ChannelSchemePO channelSchemePO : now) {
				if (old != null && old.size() >0) {
					for (ChannelSchemePO channelPO : old) {
						if (channelSchemePO.getBundleId().equals(channelPO.getBundleId()) && channelSchemePO.getChannelId().equals(channelPO.getChannelId())) {
							channelPO.setCodec(channelSchemePO.getCodec());
							channelPO.setBitrate(channelSchemePO.getBitrate());
							channelPO.setFps(channelSchemePO.getFps());
							channelPO.setResolution(channelSchemePO.getResolution());
							channelPO.setSamplateRate(channelSchemePO.getSamplateRate());
							change.add(channelPO);
							useless.add(channelSchemePO);
						}
					}
				}
			}
			
		}
		if (now != null && now.size() >0) {
			now.removeAll(useless);
			channelSchemeDao.save(now);
		}
		if (old != null && old.size() >0) {
			channelSchemeDao.save(old);
			old.removeAll(change);
			channelSchemeDao.delete(old);
		}
	}

	/**
	 * 删除虚拟设备-输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月3日 下午2:20:09
	 * @param bundleId 设备id
	 */
	public void outputDelete(String bundleId) throws Exception{
		
		BundlePO bundlePO = bundleDao.findByBundleId(bundleId);
		if (bundlePO != null) {
			List<ExtraInfoPO> extraInfoPOs = extraInfoDao.findByBundleId(bundleId);
			if (extraInfoPOs != null&& extraInfoPOs.size() >0) {
				extraInfoDao.delete(extraInfoPOs);
			}
			List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findByBundleId(bundleId);
			if (channelSchemePOs != null && channelSchemePOs.size() >0) {
				channelSchemeDao.delete(channelSchemePOs);
			}
			bundleDao.delete(bundlePO);
		}
		
	}

	/**
	 * 查询有权限的虚拟设备-输入<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:01:44
	 * @return data 虚拟输入设备列表
	 */
	public Object inputQuery() throws Exception{
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		UserVO userVO = userQuery.current();
		List<SystemRoleVO> systemRoleVOs = userQuery.queryRolesByUser(userVO.getId());
		List<Long> roleIds = new ArrayList<Long>();
 		if (systemRoleVOs != null && systemRoleVOs.size()>0) {
			for (SystemRoleVO systemRoleVO : systemRoleVOs) {
				roleIds.add(Long.valueOf(systemRoleVO.getId()));
			}
			List<PrivilegePO> privilegePOs = privilegeDAO.findByRoleIdIn(roleIds);
			List<String> bundleIds = new ArrayList<String>();
			if (privilegePOs != null && privilegePOs.size()>0) {
				for (PrivilegePO privilegePO : privilegePOs) {
					if (privilegePO.getResourceIndentity().endsWith("-r")) {
						bundleIds.add(privilegePO.getResourceIndentity().substring(0,privilegePO.getResourceIndentity().length() - 2));
					}
				}
			}
			data.addAll(listBundle(bundleDao.findByBundleIdIn(bundleIds)));
		}
 		
		
		return data;
	}
	
	public List<Map<String, Object>> listBundle(List<BundlePO> bundles)throws Exception{
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		List<String> bundleIds = new ArrayList<String>();
		if (bundles != null && bundles.size()>0) {
			for (BundlePO bundlePO : bundles) {
				if (bundlePO.getDeviceModel().equalsIgnoreCase("virtualIn") || bundlePO.getDeviceModel().equalsIgnoreCase("5g")) {
					bundleIds.add(bundlePO.getBundleId());
				}
			}
			
			List<ExtraInfoPO> extraInfoPOs = extraInfoDao.findByBundleIdIn(bundleIds);
			List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIds);
			if (bundlePOs != null && bundlePOs.size() > 0) {
				for (BundlePO bundlePO : bundlePOs) {
					Map<String, Object> bund = new HashMap<String, Object>();
					bund.put("bundleName",bundlePO.getBundleName());
					bund.put("bundleId", bundlePO.getBundleId());
					bund.put("type", bundlePO.getType());
					bund.put("url", bundlePO.getUrl());
					if (extraInfoPOs != null && extraInfoPOs.size() >0) {
						for (ExtraInfoPO extraInfoPO : extraInfoPOs) {
							if (bundlePO.getBundleId().equals(extraInfoPO.getBundleId())) {
								bund.put("programs", extraInfoPO.getValue());
							}
						}
					}
					data.add(bund);
				}
			}
		}
		
		return data;
	}

	/**
	 * 根据设备id列表查询设备<br/>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:40:20
	 * @param bundleIds 设备id列表
	 */
	public Object inputBundleId(String bundleIds) throws Exception {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		if (bundleIds != null && !bundleIds.equals("")) {
			List<String> bundle = new ArrayList<String>();
			String[] bundleId = bundleIds.split(",");
			for (String string : bundleId) {
				bundle.add(string);
			}
			data.addAll(listBundle(bundleDao.findByBundleIdIn(bundle)));
		}
		return data;
	}

	/**
	 * 查询有权限虚拟设备-输出<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>614<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月4日 上午10:44:05
	 * @return data 虚拟输出设备信息
	 */
	public List<Map<String, Object>> outputQuery() throws Exception {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		UserVO userVO = userQuery.current();
		SystemRoleVO systemRoleVO = userQueryService.queryPrivateRoleId(userVO.getId());
		List<PrivilegePO> privilegePOs = privilegeDAO.findByRoleId(Long.valueOf(systemRoleVO.getId()));
		List<String> bundleIds = new ArrayList<String>();
		if (privilegePOs != null && privilegePOs.size()>0) {
			for (PrivilegePO privilegePO : privilegePOs) {
				if (privilegePO.getResourceIndentity().endsWith("-r")) {
					bundleIds.add(privilegePO.getResourceIndentity().substring(0,privilegePO.getResourceIndentity().length() - 2));
				}
			}
			List<BundlePO> bundlePOs = bundleDao.findByBundleIdIn(bundleIds);
			List<ChannelSchemePO> channelSchemePOs = channelSchemeDao.findByBundleIdIn(bundleIds);
			if (bundlePOs != null && bundlePOs.size() > 0) {
				for (BundlePO bundlePO : bundlePOs) {
					Map<String, Object> bundle = new HashMap<String, Object>();
					bundle.put("bundleName", bundlePO.getBundleName());
					bundle.put("bundleId", bundlePO.getBundleId());
					bundle.put("url", bundlePO.getUrl());
					bundle.put("rateCtrl", bundlePO.getRateCtrl());
					bundle.put("bitrate", bundlePO.getBitrate());
					List<ChannelSchemePO> videos = new ArrayList<ChannelSchemePO>();
					List<ChannelSchemePO> audios = new ArrayList<ChannelSchemePO>();
					if (channelSchemePOs != null&& channelSchemePOs.size() > 0) {
						for (ChannelSchemePO channelSchemePO : channelSchemePOs) {
							if (bundlePO.getBundleId().equals(channelSchemePO.getBundleId()) && channelSchemePO.getChannelName().contains("video")) {
								videos.add(channelSchemePO);
							}else if (bundlePO.getBundleId().equals(channelSchemePO.getBundleId()) && channelSchemePO.getChannelName().contains("audio")) {
								audios.add(channelSchemePO);
							}
						}
					}
					bundle.put("videos", videos);
					bundle.put("audios", audios);
					data.add(bundle);
				}
			}
		}
		return data;
	}
}
