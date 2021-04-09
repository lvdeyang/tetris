package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.editor.TerminalGraphNodeVO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
public class TerminalChannelService {

	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private TerminalEncodeAudioVideoChannelPermissionDAO terminalEncodeAudioVideoChannelPermissionDao;
	
	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDao;
	
	@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDao;
	
	@Autowired
	private TerminalAudioOutputChannelPermissionDAO terminalAudioOutputChannelPermissionDao;
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	/**
	 * 添加视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 上午8:59:46
	 * @param Long terminalId 终端id
	 * @param String name 通道名称
	 * @param Long terminalAudioEncodeChannelId 关联终端音频编码通道
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO addVideoEncode(
			Long terminalId,
			String name,
			Long terminalAudioEncodeChannelId,
			String paramsPermissions) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = new TerminalChannelPO();
		terminalChannelEntity.setUpdateTime(new Date());
		terminalChannelEntity.setName(name);
		terminalChannelEntity.setType(TerminalChannelType.VIDEO_ENCODE);
		terminalChannelEntity.setTerminalId(terminalId);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		if(terminalAudioEncodeChannelId != null){
			TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionEntity = new TerminalEncodeAudioVideoChannelPermissionPO();
			terminalEncodeAudioVideoChannelPermissionEntity.setUpdateTime(new Date());
			terminalEncodeAudioVideoChannelPermissionEntity.setTerminalId(terminalId);
			terminalEncodeAudioVideoChannelPermissionEntity.setTerminalVideoChannelId(terminalChannelEntity.getId());
			terminalEncodeAudioVideoChannelPermissionEntity.setTerminalAudioChannelId(terminalAudioEncodeChannelId);
			terminalEncodeAudioVideoChannelPermissionDao.save(terminalEncodeAudioVideoChannelPermissionEntity);
		}
		
		if(paramsPermissions != null){
			JSONArray parsedPermissions = JSON.parseArray(paramsPermissions);
			List<Long> terminalBundleIds = new ArrayList<Long>();
			List<Long> terminalBundleChannelIds = new ArrayList<Long>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				terminalBundleIds.add(parsedPermission.getLong("terminalBundleId"));
				terminalBundleChannelIds.add(parsedPermission.getLong("terminalBundleChannelId"));
			}
			List<TerminalBundlePO> terminalBundleEntities = terminalBundleDao.findAll(terminalBundleIds);
			List<TerminalBundleChannelPO> terminalBundleChannelEntities = terminalBundleChannelDao.findAll(terminalBundleChannelIds);
			
			List<TerminalChannelBundleChannelPermissionPO> permissionEntities = new ArrayList<TerminalChannelBundleChannelPermissionPO>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
				permissionEntity.setUpdateTime(new Date());
				permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
				permissionEntity.setTerminalId(terminalId);
				permissionEntity.setTerminalBundleId(parsedPermission.getLong("terminalBundleId"));
				permissionEntity.setTerminalBundleChannelId(parsedPermission.getLong("terminalBundleChannelId"));
				permissionEntity.setChannelParamsType(ChannelParamsType.valueOf(parsedPermission.getString("channelParams")));
				permissionEntities.add(permissionEntity);
			}
			terminalChannelBundleChannelPermissionDao.save(permissionEntities);
			
			for(TerminalChannelBundleChannelPermissionPO permissionEntity:permissionEntities){
				TerminalBundleChannelPO targetChannel = null;
				for(TerminalBundleChannelPO terminalBundleChannelEntity:terminalBundleChannelEntities){
					if(terminalBundleChannelEntity.getId().equals(permissionEntity.getTerminalBundleChannelId())){
						targetChannel = terminalBundleChannelEntity;
						break;
					}
				}
				TerminalBundlePO targetBundle = null;
				for(TerminalBundlePO terminalBundleEntity:terminalBundleEntities){
					if(terminalBundleEntity.getId().equals(permissionEntity.getTerminalBundleId())){
						targetBundle = terminalBundleEntity;
						break;
					}
				}
				terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(targetChannel, targetBundle, permissionEntity));
			}
		}
		
		return terminalChannelNode;
	}
	
	/**
	 * 修改视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 上午8:59:46
	 * @param Long id 视频编码通道id
	 * @param String name 通道名称
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO editVideoEncode(
			Long id,
			String name,
			String paramsPermissions) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		terminalChannelEntity.setName(name);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		if(paramsPermissions != null){
			JSONArray parsedPermissions = JSON.parseArray(paramsPermissions);
			List<Long> terminalBundleIds = new ArrayList<Long>();
			List<Long> terminalBundleChannelIds = new ArrayList<Long>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				terminalBundleIds.add(parsedPermission.getLong("terminalBundleId"));
				terminalBundleChannelIds.add(parsedPermission.getLong("terminalBundleChannelId"));
			}
			List<TerminalBundlePO> terminalBundleEntities = terminalBundleDao.findAll(terminalBundleIds);
			List<TerminalBundleChannelPO> terminalBundleChannelEntities = terminalBundleChannelDao.findAll(terminalBundleChannelIds);
			
			List<TerminalChannelBundleChannelPermissionPO> permissionEntities = new ArrayList<TerminalChannelBundleChannelPermissionPO>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
				permissionEntity.setUpdateTime(new Date());
				permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
				permissionEntity.setTerminalId(terminalChannelEntity.getTerminalId());
				permissionEntity.setTerminalBundleId(parsedPermission.getLong("terminalBundleId"));
				permissionEntity.setTerminalBundleChannelId(parsedPermission.getLong("terminalBundleChannelId"));
				permissionEntity.setChannelParamsType(ChannelParamsType.valueOf(parsedPermission.getString("channelParams")));
				permissionEntities.add(permissionEntity);
			}
			terminalChannelBundleChannelPermissionDao.save(permissionEntities);
			
			for(TerminalChannelBundleChannelPermissionPO permissionEntity:permissionEntities){
				TerminalBundleChannelPO targetChannel = null;
				for(TerminalBundleChannelPO terminalBundleChannelEntity:terminalBundleChannelEntities){
					if(terminalBundleChannelEntity.getId().equals(permissionEntity.getTerminalBundleChannelId())){
						targetChannel = terminalBundleChannelEntity;
						break;
					}
				}
				TerminalBundlePO targetBundle = null;
				for(TerminalBundlePO terminalBundleEntity:terminalBundleEntities){
					if(terminalBundleEntity.getId().equals(permissionEntity.getTerminalBundleId())){
						targetBundle = terminalBundleEntity;
						break;
					}
				}
				terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(targetChannel, targetBundle, permissionEntity));
			}
		}
		
		return terminalChannelNode;
	}
	
	/**
	 * 移动视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:40:04
	 * @param Long id 视频编码通道id
	 * @param Long terminalAudioEncodeChannelId 终端音频编码通道id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void moveVideoEncode(
			Long id,
			Long terminalAudioEncodeChannelId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		
		List<TerminalEncodeAudioVideoChannelPermissionPO> existPermissions = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalVideoChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalEncodeAudioVideoChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		if(terminalAudioEncodeChannelId != null){
			TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoChannelPermissionEntity = new TerminalEncodeAudioVideoChannelPermissionPO();
			terminalEncodeAudioVideoChannelPermissionEntity.setUpdateTime(new Date());
			terminalEncodeAudioVideoChannelPermissionEntity.setTerminalId(terminalChannelEntity.getTerminalId());
			terminalEncodeAudioVideoChannelPermissionEntity.setTerminalVideoChannelId(terminalChannelEntity.getId());
			terminalEncodeAudioVideoChannelPermissionEntity.setTerminalAudioChannelId(terminalAudioEncodeChannelId);
			terminalEncodeAudioVideoChannelPermissionDao.save(terminalEncodeAudioVideoChannelPermissionEntity);
		}
		
	}
	
	/**
	 * 删除视频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午1:40:04
	 * @param Long id 视频编码通道id
	 */
	public void removeVideoEncode(Long id) throws Exception{
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		if(terminalChannelEntity != null){
			terminalChannelDao.delete(terminalChannelEntity);
		}
		List<TerminalEncodeAudioVideoChannelPermissionPO> existTerminalEncodeAudioVideoPermissions = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalVideoChannelId(terminalChannelEntity.getId());
		if(existTerminalEncodeAudioVideoPermissions!=null && existTerminalEncodeAudioVideoPermissions.size()>0){
			terminalEncodeAudioVideoChannelPermissionDao.deleteInBatch(existTerminalEncodeAudioVideoPermissions);
		}
		List<TerminalChannelBundleChannelPermissionPO> existTerminalChannelBundleChannelPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(id);
		if(existTerminalChannelBundleChannelPermissions!=null && existTerminalChannelBundleChannelPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existTerminalChannelBundleChannelPermissions);
		}
		List<RoleChannelTerminalChannelPermissionPO> roleChannelTerminalChannelPermissionEntities = roleChannelTerminalChannelPermissionDao.findByTerminalChannelIdIn(new ArrayListWrapper<Long>().add(id).getList());
		if(roleChannelTerminalChannelPermissionEntities!=null && roleChannelTerminalChannelPermissionEntities.size()>0){
			roleChannelTerminalChannelPermissionDao.deleteInBatch(roleChannelTerminalChannelPermissionEntities);
		}
	}
	
	/**
	 * 添加视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:41:00
	 * @param Long terminalId 终端id
	 * @param String name 解码通道名称
	 * @param Long terminalPhysicalScreenId 关联物理屏幕id
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO addVideoDecode(
			Long terminalId,
			String name,
			Long terminalPhysicalScreenId,
			String paramsPermissions) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = new TerminalChannelPO();
		terminalChannelEntity.setUpdateTime(new Date());
		terminalChannelEntity.setName(name);
		terminalChannelEntity.setType(TerminalChannelType.VIDEO_DECODE);
		terminalChannelEntity.setTerminalId(terminalId);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		if(terminalPhysicalScreenId != null){
			TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionEntity = new TerminalPhysicalScreenChannelPermissionPO();
			terminalPhysicalScreenChannelPermissionEntity.setUpdateTime(new Date());
			terminalPhysicalScreenChannelPermissionEntity.setTerminalId(terminalId);
			terminalPhysicalScreenChannelPermissionEntity.setTerminalPhysicalScreenId(terminalPhysicalScreenId);
			terminalPhysicalScreenChannelPermissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
			terminalPhysicalScreenChannelPermissionDao.save(terminalPhysicalScreenChannelPermissionEntity);
		}
		
		if(paramsPermissions != null){
			JSONArray parsedPermissions = JSON.parseArray(paramsPermissions);
			List<Long> terminalBundleIds = new ArrayList<Long>();
			List<Long> terminalBundleChannelIds = new ArrayList<Long>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				terminalBundleIds.add(parsedPermission.getLong("terminalBundleId"));
				terminalBundleChannelIds.add(parsedPermission.getLong("terminalBundleChannelId"));
			}
			List<TerminalBundlePO> terminalBundleEntities = terminalBundleDao.findAll(terminalBundleIds);
			List<TerminalBundleChannelPO> terminalBundleChannelEntities = terminalBundleChannelDao.findAll(terminalBundleChannelIds);
			
			List<TerminalChannelBundleChannelPermissionPO> permissionEntities = new ArrayList<TerminalChannelBundleChannelPermissionPO>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
				permissionEntity.setUpdateTime(new Date());
				permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
				permissionEntity.setTerminalId(terminalId);
				permissionEntity.setTerminalBundleId(parsedPermission.getLong("terminalBundleId"));
				permissionEntity.setTerminalBundleChannelId(parsedPermission.getLong("terminalBundleChannelId"));
				permissionEntity.setChannelParamsType(ChannelParamsType.valueOf(parsedPermission.getString("channelParams")));
				permissionEntities.add(permissionEntity);
			}
			terminalChannelBundleChannelPermissionDao.save(permissionEntities);
			
			for(TerminalChannelBundleChannelPermissionPO permissionEntity:permissionEntities){
				TerminalBundleChannelPO targetChannel = null;
				for(TerminalBundleChannelPO terminalBundleChannelEntity:terminalBundleChannelEntities){
					if(terminalBundleChannelEntity.getId().equals(permissionEntity.getTerminalBundleChannelId())){
						targetChannel = terminalBundleChannelEntity;
						break;
					}
				}
				TerminalBundlePO targetBundle = null;
				for(TerminalBundlePO terminalBundleEntity:terminalBundleEntities){
					if(terminalBundleEntity.getId().equals(permissionEntity.getTerminalBundleId())){
						targetBundle = terminalBundleEntity;
						break;
					}
				}
				terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(targetChannel, targetBundle, permissionEntity));
			}
		}
		return terminalChannelNode;
	}
	
	/**
	 * 修改视频解码通道 <br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午3:34:41
	 * @param Long id 视频解码通道id
	 * @param String name 名称
	 * @param JSONArray paramsPermissions [{channelParams:"参数", terminalBundleId:"终端设备id", terminalBundleChannelId:"终端设备通道id"}]
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO editVideoDecode(
			Long id,
			String name,
			String paramsPermissions) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		terminalChannelEntity.setName(name);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		if(paramsPermissions != null){
			JSONArray parsedPermissions = JSON.parseArray(paramsPermissions);
			List<Long> terminalBundleIds = new ArrayList<Long>();
			List<Long> terminalBundleChannelIds = new ArrayList<Long>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				terminalBundleIds.add(parsedPermission.getLong("terminalBundleId"));
				terminalBundleChannelIds.add(parsedPermission.getLong("terminalBundleChannelId"));
			}
			List<TerminalBundlePO> terminalBundleEntities = terminalBundleDao.findAll(terminalBundleIds);
			List<TerminalBundleChannelPO> terminalBundleChannelEntities = terminalBundleChannelDao.findAll(terminalBundleChannelIds);
			
			List<TerminalChannelBundleChannelPermissionPO> permissionEntities = new ArrayList<TerminalChannelBundleChannelPermissionPO>();
			for(int i=0; i<parsedPermissions.size(); i++){
				JSONObject parsedPermission = parsedPermissions.getJSONObject(i);
				TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
				permissionEntity.setUpdateTime(new Date());
				permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
				permissionEntity.setTerminalId(terminalChannelEntity.getTerminalId());
				permissionEntity.setTerminalBundleId(parsedPermission.getLong("terminalBundleId"));
				permissionEntity.setTerminalBundleChannelId(parsedPermission.getLong("terminalBundleChannelId"));
				permissionEntity.setChannelParamsType(ChannelParamsType.valueOf(parsedPermission.getString("channelParams")));
				permissionEntities.add(permissionEntity);
			}
			terminalChannelBundleChannelPermissionDao.save(permissionEntities);
			
			for(TerminalChannelBundleChannelPermissionPO permissionEntity:permissionEntities){
				TerminalBundleChannelPO targetChannel = null;
				for(TerminalBundleChannelPO terminalBundleChannelEntity:terminalBundleChannelEntities){
					if(terminalBundleChannelEntity.getId().equals(permissionEntity.getTerminalBundleChannelId())){
						targetChannel = terminalBundleChannelEntity;
						break;
					}
				}
				TerminalBundlePO targetBundle = null;
				for(TerminalBundlePO terminalBundleEntity:terminalBundleEntities){
					if(terminalBundleEntity.getId().equals(permissionEntity.getTerminalBundleId())){
						targetBundle = terminalBundleEntity;
						break;
					}
				}
				terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(targetChannel, targetBundle, permissionEntity));
			}
		}
		
		return terminalChannelNode;
	}
	
	/**
	 * 移动视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:15:03
	 * @param Long id 视频解码通道id
	 * @param Long terminalPhysicalScreenId 物理屏幕id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void moveVideoDecode(
			Long id,
			Long terminalPhysicalScreenId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		
		List<TerminalPhysicalScreenChannelPermissionPO> existTerminalPhysicalScreenChannelPermissionsEntities = terminalPhysicalScreenChannelPermissionDao.findByTerminalChannelId(id);
		if(existTerminalPhysicalScreenChannelPermissionsEntities!=null && existTerminalPhysicalScreenChannelPermissionsEntities.size()>0){
			terminalPhysicalScreenChannelPermissionDao.deleteInBatch(existTerminalPhysicalScreenChannelPermissionsEntities);
		}
		
		if(terminalPhysicalScreenId != null){
			TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionsEntity = new TerminalPhysicalScreenChannelPermissionPO();
			terminalPhysicalScreenChannelPermissionsEntity.setUpdateTime(new Date());
			terminalPhysicalScreenChannelPermissionsEntity.setTerminalChannelId(id);
			terminalPhysicalScreenChannelPermissionsEntity.setTerminalPhysicalScreenId(terminalPhysicalScreenId);
			terminalPhysicalScreenChannelPermissionsEntity.setTerminalId(terminalChannelEntity.getTerminalId());
			terminalPhysicalScreenChannelPermissionDao.save(terminalPhysicalScreenChannelPermissionsEntity);
		}
		
	}
	
	/**
	 * 删除视频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午4:18:07
	 * @param Long id 视频解码通道id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeVideoDecode(Long id) throws Exception{
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		if(terminalChannelEntity != null){
			terminalChannelDao.delete(terminalChannelEntity);
		}
		List<TerminalPhysicalScreenChannelPermissionPO> existTerminalPhysicalScreenChannelPermissionsEntities = terminalPhysicalScreenChannelPermissionDao.findByTerminalChannelId(id);
		if(existTerminalPhysicalScreenChannelPermissionsEntities!=null && existTerminalPhysicalScreenChannelPermissionsEntities.size()>0){
			terminalPhysicalScreenChannelPermissionDao.deleteInBatch(existTerminalPhysicalScreenChannelPermissionsEntities);
		}
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		List<RoleChannelTerminalChannelPermissionPO> roleChannelTerminalChannelPermissionEntities = roleChannelTerminalChannelPermissionDao.findByTerminalChannelIdIn(new ArrayListWrapper<Long>().add(id).getList());
		if(roleChannelTerminalChannelPermissionEntities!=null && roleChannelTerminalChannelPermissionEntities.size()>0){
			roleChannelTerminalChannelPermissionDao.deleteInBatch(roleChannelTerminalChannelPermissionEntities);
		}
	}
	
	/**
	 * 添加音频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:41:00
	 * @param Long terminalId 终端id
	 * @param String name 音频编码通道名称
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO addAudioEncode(
			Long terminalId,
			String name,
			Long terminalBundleChannelId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = new TerminalChannelPO();
		terminalChannelEntity.setUpdateTime(new Date());
		terminalChannelEntity.setName(name);
		terminalChannelEntity.setType(TerminalChannelType.AUDIO_ENCODE);
		terminalChannelEntity.setTerminalId(terminalId);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		TerminalBundleChannelPO terminalBundleChannelEntity = terminalBundleChannelDao.findOne(terminalBundleChannelId);
		TerminalBundlePO terminalBundleEntity = terminalBundleDao.findOne(terminalBundleChannelEntity.getTerminalBundleId());
		
		TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
		permissionEntity.setUpdateTime(new Date());
		permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
		permissionEntity.setTerminalId(terminalId);
		permissionEntity.setTerminalBundleId(terminalBundleEntity.getId());
		permissionEntity.setTerminalBundleChannelId(terminalBundleChannelEntity.getId());
		permissionEntity.setChannelParamsType(ChannelParamsType.DEFAULT);
		terminalChannelBundleChannelPermissionDao.save(permissionEntity);
		
		terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity, terminalBundleEntity, permissionEntity));
		return terminalChannelNode;
	}
	
	/**
	 * 修改音频编码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月17日 下午7:14:17
	 * @param Long id 音频编码通道id
	 * @param String name 通道名称
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 终端拓补图
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO editAudioEncode(
			Long id,
			String name,
			Long terminalBundleChannelId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		terminalChannelEntity.setName(name);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		TerminalBundleChannelPO terminalBundleChannelEntity = terminalBundleChannelDao.findOne(terminalBundleChannelId);
		TerminalBundlePO terminalBundleEntity = terminalBundleDao.findOne(terminalBundleChannelEntity.getTerminalBundleId());
		
		TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
		permissionEntity.setUpdateTime(new Date());
		permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
		permissionEntity.setTerminalId(terminalChannelEntity.getTerminalId());
		permissionEntity.setTerminalBundleId(terminalBundleEntity.getId());
		permissionEntity.setTerminalBundleChannelId(terminalBundleChannelEntity.getId());
		permissionEntity.setChannelParamsType(ChannelParamsType.DEFAULT);
		terminalChannelBundleChannelPermissionDao.save(permissionEntity);
		
		terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity, terminalBundleEntity, permissionEntity));
		
		return terminalChannelNode;
	}
	
	/**
	 * 删除音频编码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午9:54:33
	 * @param Long id 音频编码通道id
	 * @param Boolean removeChildren 是否删除子节点
	 * 	1.音频编码通道
	 *  2.音频编码通道视频编码通道关联
	 * 子节点包括：
	 *  3.视频编码通道
	 *  4.视频编码通道设备通道关联
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeAudioEncode(
			Long id,
			Boolean removeChildren) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		if(terminalChannelEntity != null){
			terminalChannelDao.delete(terminalChannelEntity);
		}
		
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoPermissionEntities = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalAudioChannelId(id);
		List<Long> terminalVideoChannelIds = new ArrayList<Long>();
		if(terminalEncodeAudioVideoPermissionEntities!=null && terminalEncodeAudioVideoPermissionEntities.size()>0){
			for(TerminalEncodeAudioVideoChannelPermissionPO terminalEncodeAudioVideoPermissionEntity:terminalEncodeAudioVideoPermissionEntities){
				terminalVideoChannelIds.add(terminalEncodeAudioVideoPermissionEntity.getTerminalVideoChannelId());
			}
			terminalEncodeAudioVideoChannelPermissionDao.deleteInBatch(terminalEncodeAudioVideoPermissionEntities);
		}
		
		if(removeChildren){
			if(terminalVideoChannelIds.size() > 0){
				List<TerminalChannelPO> terminalVideoChannelEntities = terminalChannelDao.findAll(terminalVideoChannelIds);
				if(terminalVideoChannelEntities!=null && terminalVideoChannelEntities.size()>0){
					terminalChannelDao.deleteInBatch(terminalVideoChannelEntities);
				}
				List<TerminalChannelBundleChannelPermissionPO> existTerminalChannelBundleChannelPermissionEntities = terminalChannelBundleChannelPermissionDao.findByTerminalChannelIdIn(terminalVideoChannelIds);
				if(existTerminalChannelBundleChannelPermissionEntities!=null && existTerminalChannelBundleChannelPermissionEntities.size()>0){
					terminalChannelBundleChannelPermissionDao.deleteInBatch(existTerminalChannelBundleChannelPermissionEntities);
				}
			}
		}
		
		List<RoleChannelTerminalChannelPermissionPO> roleChannelTerminalChannelPermissionEntities = roleChannelTerminalChannelPermissionDao.findByTerminalChannelIdIn(new ArrayListWrapper<Long>().add(id).getList());
		if(roleChannelTerminalChannelPermissionEntities!=null && roleChannelTerminalChannelPermissionEntities.size()>0){
			roleChannelTerminalChannelPermissionDao.deleteInBatch(roleChannelTerminalChannelPermissionEntities);
		}
	}
	
	/**
	 * 添加音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月13日 下午3:41:00
	 * @param Long terminalId 终端id
	 * @param String name 音频编码通道名称
	 * @param Long terminalAudioOutputId 音频输出id
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 拓补图节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO addAudioDecode(
			Long terminalId,
			String name,
			Long terminalAudioOutputId,
			Long terminalBundleChannelId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = new TerminalChannelPO();
		terminalChannelEntity.setUpdateTime(new Date());
		terminalChannelEntity.setName(name);
		terminalChannelEntity.setType(TerminalChannelType.AUDIO_DECODE);
		terminalChannelEntity.setTerminalId(terminalId);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		if(terminalAudioOutputId != null){
			TerminalAudioOutputChannelPermissionPO terminalAudioOutputChannelPermissionEntity = new TerminalAudioOutputChannelPermissionPO();
			terminalAudioOutputChannelPermissionEntity.setTerminalId(terminalId);
			terminalAudioOutputChannelPermissionEntity.setTerminalAudioOutputId(terminalAudioOutputId);
			terminalAudioOutputChannelPermissionEntity.setTerminalAudioChannelId(terminalChannelEntity.getId());
			terminalAudioOutputChannelPermissionDao.save(terminalAudioOutputChannelPermissionEntity);
		}
		
		TerminalBundleChannelPO terminalBundleChannelEntity = terminalBundleChannelDao.findOne(terminalBundleChannelId);
		TerminalBundlePO terminalBundleEntity = terminalBundleDao.findOne(terminalBundleChannelEntity.getTerminalBundleId());
		
		TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
		permissionEntity.setUpdateTime(new Date());
		permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
		permissionEntity.setTerminalId(terminalId);
		permissionEntity.setTerminalBundleId(terminalBundleEntity.getId());
		permissionEntity.setTerminalBundleChannelId(terminalBundleChannelEntity.getId());
		permissionEntity.setChannelParamsType(ChannelParamsType.DEFAULT);
		terminalChannelBundleChannelPermissionDao.save(permissionEntity);
		
		terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity, terminalBundleEntity, permissionEntity));
		return terminalChannelNode;
	}
	
	/**
	 * 修改音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:18:39
	 * @param Long id 音频解码通道id
	 * @param String name 音频解码通道名称
	 * @param Long terminalBundleChannelId 终端设备通道id
	 * @return TerminalGraphNodeVO 拓补图音频解码通道节点
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalGraphNodeVO editAudioDecode(
			Long id,
			String name,
			Long terminalBundleChannelId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		terminalChannelEntity.setName(name);
		terminalChannelDao.save(terminalChannelEntity);
		TerminalGraphNodeVO terminalChannelNode = new TerminalGraphNodeVO().set(terminalChannelEntity);
		
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		TerminalBundleChannelPO terminalBundleChannelEntity = terminalBundleChannelDao.findOne(terminalBundleChannelId);
		TerminalBundlePO terminalBundleEntity = terminalBundleDao.findOne(terminalBundleChannelEntity.getTerminalBundleId());
		
		TerminalChannelBundleChannelPermissionPO permissionEntity = new TerminalChannelBundleChannelPermissionPO();
		permissionEntity.setUpdateTime(new Date());
		permissionEntity.setTerminalChannelId(terminalChannelEntity.getId());
		permissionEntity.setTerminalId(terminalChannelEntity.getTerminalId());
		permissionEntity.setTerminalBundleId(terminalBundleEntity.getId());
		permissionEntity.setTerminalBundleChannelId(terminalBundleChannelEntity.getId());
		permissionEntity.setChannelParamsType(ChannelParamsType.DEFAULT);
		terminalChannelBundleChannelPermissionDao.save(permissionEntity);
		
		terminalChannelNode.getChildren().add(new TerminalGraphNodeVO().set(terminalBundleChannelEntity, terminalBundleEntity, permissionEntity));
		
		return terminalChannelNode;
	}
	
	/**
	 * 移动音频解码通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:27:59
	 * @param Long id 音频解码通道id
	 * @param Long terminalAudioOutputId 音频输出id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void moveAudioDecode(
			Long id,
			Long terminalAudioOutputId) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		
		List<TerminalAudioOutputChannelPermissionPO> existTerminalAudioOutputChannelPermissionEntities = terminalAudioOutputChannelPermissionDao.findByTerminalAudioChannelId(id);
		if(existTerminalAudioOutputChannelPermissionEntities!=null && existTerminalAudioOutputChannelPermissionEntities.size()>0){
			terminalAudioOutputChannelPermissionDao.deleteInBatch(existTerminalAudioOutputChannelPermissionEntities);
		}
		
		if(terminalAudioOutputId != null){
			TerminalAudioOutputChannelPermissionPO terminalAudioOutputChannelPermissionEntity = new TerminalAudioOutputChannelPermissionPO();
			terminalAudioOutputChannelPermissionEntity.setTerminalId(terminalChannelEntity.getTerminalId());
			terminalAudioOutputChannelPermissionEntity.setTerminalAudioOutputId(terminalAudioOutputId);
			terminalAudioOutputChannelPermissionEntity.setTerminalAudioChannelId(terminalChannelEntity.getId());
			terminalAudioOutputChannelPermissionDao.save(terminalAudioOutputChannelPermissionEntity);
		}
	}
	
	/**
	 * 删除音频解码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月18日 上午11:37:23
	 * @param Long id 音频解码通道id
	 * 1.音频解码通道
	 * 2.音频输出与音频解码通道关联
	 * 3.音频解码通道与终端设备通道关联
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeAudioDecode(Long id) throws Exception{
		
		TerminalChannelPO terminalChannelEntity = terminalChannelDao.findOne(id);
		if(terminalChannelEntity != null){
			terminalChannelDao.delete(terminalChannelEntity);
		}
		
		List<TerminalAudioOutputChannelPermissionPO> existTerminalAudioOutputChannelPermissionEntities = terminalAudioOutputChannelPermissionDao.findByTerminalAudioChannelId(id);
		if(existTerminalAudioOutputChannelPermissionEntities!=null && existTerminalAudioOutputChannelPermissionEntities.size()>0){
			terminalAudioOutputChannelPermissionDao.deleteInBatch(existTerminalAudioOutputChannelPermissionEntities);
		}
		
		List<TerminalChannelBundleChannelPermissionPO> existPermissions = terminalChannelBundleChannelPermissionDao.findByTerminalChannelId(terminalChannelEntity.getId());
		if(existPermissions!=null && existPermissions.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(existPermissions);
		}
		
		List<RoleChannelTerminalChannelPermissionPO> roleChannelTerminalChannelPermissionEntities = roleChannelTerminalChannelPermissionDao.findByTerminalChannelIdIn(new ArrayListWrapper<Long>().add(id).getList());
		if(roleChannelTerminalChannelPermissionEntities!=null && roleChannelTerminalChannelPermissionEntities.size()>0){
			roleChannelTerminalChannelPermissionDao.deleteInBatch(roleChannelTerminalChannelPermissionEntities);
		}
	}
	
	/**
	 * 添加终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:33:08
	 * @param String name 通道名称
	 * @param String type 通道类型
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String realChannelId 终端设备通道id
	 * @return TerminalChannelVO 通道
	 */
	/*public TerminalChannelVO add(
			String name, 
			String type, 
			Long terminalId, 
			Long terminalBundleId, 
			String realChannelId) throws Exception{
		
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		TerminalBundlePO terminalBundle = terminalBundleDao.findOne(terminalBundleId);
		if(terminalBundle == null){
			throw new TerminalBundleNotFoundException(terminalBundleId);
		}
		List<TerminalBundleChannelPO> terminalBundleChannel = terminalBundleChannelDao.findByTerminalBundleIdAndChannelId(terminalBundleId, realChannelId);
		if(terminalBundleChannel == null){
			throw new TerminalBundleChannelNotFoundException(terminalBundleId, realChannelId);
		}
		
		TerminalChannelPO channel = new TerminalChannelPO();
		channel.setName(name);
		channel.setType(TerminalChannelType.valueOf(type));
		channel.setTerminalId(terminalId);
		channel.setTerminalBundleId(terminalBundleId);
		channel.setRealChannelId(realChannelId);
		channel.setUpdateTime(new Date());
		terminalChannelDao.save(channel);
		
		return new TerminalChannelVO().set(channel)
									  .setTerminalBundleName(terminalBundle.getName());
	}*/
	
	/**
	 * 修改终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:33:08
	 * @param Long id 终端通道id
	 * @param String name 通道名称
	 * @param String type 通道类型
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String realChannelId 终端设备通道id
	 * @return TerminalChannelVO 通道
	 */
	/*public TerminalChannelVO edit(
			Long id,
			String name,
			String type,
			Long terminalId,
			Long terminalBundleId,
			String realChannelId) throws Exception{
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		TerminalBundlePO terminalBundle = terminalBundleDao.findOne(terminalBundleId);
		if(terminalBundle == null){
			throw new TerminalBundleNotFoundException(terminalBundleId);
		}
		List<TerminalBundleChannelPO> terminalBundleChannel = terminalBundleChannelDao.findByTerminalBundleIdAndChannelId(terminalBundleId, realChannelId);
		if(terminalBundleChannel == null){
			throw new TerminalBundleChannelNotFoundException(terminalBundleId, realChannelId);
		}
		TerminalChannelPO channel = terminalChannelDao.findOne(id);
		if(channel == null){
			throw new TerminalChannelNotFoundException(id);
		}
		channel.setName(name);
		channel.setType(TerminalChannelType.valueOf(type));
		channel.setTerminalId(terminalId);
		channel.setTerminalBundleId(terminalBundleId);
		channel.setRealChannelId(realChannelId);
		channel.setUpdateTime(new Date());
		terminalChannelDao.save(channel);
		return new TerminalChannelVO().set(channel)
									  .setTerminalBundleName(terminalBundle.getName());
	}*/
	
	/**
	 * 删除终端通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:42:03
	 * @param Long id 通道id
	 */
	/*public void delete(Long id) throws Exception{
		TerminalChannelPO channel = terminalChannelDao.findOne(id);
		if(channel != null){
			terminalChannelDao.delete(channel);
		}
	}
	
	public TerminalChannelVO addChannelPermission()throws Exception{
		TerminalChannelVO terminalChannelVO = new TerminalChannelVO();
		return terminalChannelVO;
	}*/
}
