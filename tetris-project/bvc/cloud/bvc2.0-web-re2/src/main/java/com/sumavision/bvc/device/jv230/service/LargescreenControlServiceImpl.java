package com.sumavision.bvc.device.jv230.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.bo.AudioParamBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.CombineAudioBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.SourceBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.jv230.bo.Jv230BaseParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ChannelParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ForwardBO;
import com.sumavision.bvc.device.jv230.bo.Jv230SourceBO;
import com.sumavision.bvc.device.jv230.bo.PositionBO;
import com.sumavision.bvc.device.jv230.dao.CombineJv230ConfigDAO;
import com.sumavision.bvc.device.jv230.dao.Jv230ChannelDAO;
import com.sumavision.bvc.device.jv230.dto.Jv230ChannelDTO;
import com.sumavision.bvc.device.jv230.po.CombineJv230ConfigPO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.ConfigLocationPO;
import com.sumavision.bvc.device.jv230.po.ConfigTaskPO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;

/**
 * @ClassName: 大屏控制逻辑
 * @author wjw
 * @date 2018年11月30日 上午9:01:36 
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class LargescreenControlServiceImpl {
	
	//任务类型
	public static final String TASK_TYPE_DEVICE = "device";
	
	public static final String TASK_TYPE_POLLING = "polling";
	
	public static final String TASK_TYPE_AUDIO = "audio";
	
	//默认上屏状态:off	
	public static final String DEFAULT_STATUS_OFF = "未上屏";
	
	//默认上屏状态:on
	public static final String DEFAULT_STATUS_ON = "上屏中";
	
	@Autowired
	private CombineJv230ConfigDAO combineJv230ConfigDao;
	
	@Autowired
	private Jv230ChannelDAO jv230ChannelDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	/**
	 * 保存大屏配置 
	 * @Title: saveLargescreenConfigList 
	 * @param largescreenId 大屏id
	 * @param largescreenConfigId 大屏配置id
	 * @param task 任务
	 * @return CombineJv230ConfigPO 大屏配置
	 * @throws
	 */
	public CombineJv230ConfigPO saveLargescreenConfigList(Long largescreenConfigId, String tasks) throws Exception{
		List<JSONObject> taskList = JSON.parseArray(tasks, JSONObject.class);
		
		CombineJv230ConfigPO larConfigList = combineJv230ConfigDao.findOne(largescreenConfigId);;
			
		if(larConfigList != null){
			
			larConfigList.setUpdateTime(new Date());
			
			Set <ConfigTaskPO> delConfigList = larConfigList.getConfigTasks();
			Set <ConfigTaskPO> delConfigListExceptAudio = new HashSet<ConfigTaskPO>();
			if(delConfigList != null && delConfigList.size()>0){
				for(ConfigTaskPO delConfig:delConfigList){
					//type区分audio，audio一旦创建好，只做修改
					if(!delConfig.getType().equals(TASK_TYPE_AUDIO)){
						delConfig.setCombineJv230Config(null);
						delConfigListExceptAudio.add(delConfig);
					}					
				}
				larConfigList.getConfigTasks().removeAll(delConfigListExceptAudio);
				combineJv230ConfigDao.save(larConfigList);
			}
				
			if(larConfigList.getConfigTasks() == null) larConfigList.setConfigTasks(new HashSet<ConfigTaskPO>());
				
			if(taskList != null && taskList.size()>0){		
				for(JSONObject task:taskList){
					String content = task.getString("content");
					String type = task.getString("type");
					String time = task.getString("time");
					List <JSONObject> locationList = JSON.parseArray(task.getString("location"), JSONObject.class);
					
					ConfigTaskPO configTask = new ConfigTaskPO();
					configTask.setType(type);
					configTask.setTime(time);
					configTask.setContent(content.getBytes());
					configTask.setCombineJv230Config(larConfigList);
					
					Set <ConfigLocationPO> locations = new HashSet<ConfigLocationPO>();
					if(locationList != null && locationList.size() > 0){
						for(JSONObject location:locationList){
							ConfigLocationPO locationPO = new ConfigLocationPO();
							locationPO.setLocation(location.toString().getBytes());
							locationPO.setConfigTask(configTask);
							locations.add(locationPO);
						}
						configTask.getConfigLocations().addAll(locations);
					}
					
					larConfigList.getConfigTasks().add(configTask);
				}
			}
						
			combineJv230ConfigDao.save(larConfigList);
		}
		
		return larConfigList;			
	}
	
	/**
	 * 保存混音配置 (音频+增益) </br>
	 * @Title: saveLargescreenAudio 
	 * @Description: 协议下发：混音设备列表变、音量变 -- 下混音协议并caudio设置音量值
	 * 					           混音设备列表变、音量不变 -- 下混音协议并caudio不设置音量值
	 * 					           混音设备列表不变、音量变 -- 下音量增益协议
	 * @param largescreenConfigId 大屏配置id
	 * @param task audio任务
	 * @return void    返回类型 
	 * @throws
	 */
	public void saveLargescreenAudio(Long largescreenConfigId, String task){
		
		LogicBO logic = new LogicBO();
		
		JSONObject taskObject = JSONObject.parseObject(task);
		String type = taskObject.getString("type");
		String content = taskObject.getString("content");	
		//上屏标识：若在上屏中，考虑协议下发
		boolean viewFlag = taskObject.getBoolean("viewFlag");
		JSONObject contentObject = JSONObject.parseObject(content);
		List<String> uuidList = JSON.parseArray(contentObject.get("uuid").toString(), String.class);
		String volume = contentObject.getString("volume");
		
		CombineJv230ConfigPO combineJv230Config = combineJv230ConfigDao.findOne(largescreenConfigId);
		
		List<Jv230ChannelPO> jv230Channels = new ArrayList<Jv230ChannelPO>();
		
		CombineJv230PO combineJv230 = combineJv230Config.getCombineJv230();
		Set<Jv230PO> jv230s = combineJv230.getBundles();
		for(Jv230PO jv230: jv230s){
			jv230Channels.addAll(jv230.getChannels());
		}
		
		ConfigTaskPO configTask = null;
		ConfigTaskPO existConfigTask = null;
		Set<ConfigTaskPO> configTaskPOList = combineJv230Config.getConfigTasks();
		
		if(configTaskPOList != null && configTaskPOList.size() > 0){
			for(ConfigTaskPO configTaskPO: configTaskPOList){
				if(configTaskPO.getType().equals(type)){
					existConfigTask = configTaskPO;
					break;
				}
			}
			if(existConfigTask == null){
				configTask = new ConfigTaskPO();	
				configTask.setType(type);
				configTask.setContent(content.getBytes());
				configTask.setCombineJv230Config(combineJv230Config);
				
				configTaskPOList.add(configTask);
				
				if(viewFlag){
					//创建混音
					logic.merge(createCombineAudio(configTask, jv230Channels));
						
				}
			}else{
				configTask = existConfigTask;
				String preContent = new String(existConfigTask.getContent());
				JSONObject preContentObject = JSONObject.parseObject(preContent);
				List<String> preUuidList = JSON.parseArray(preContentObject.get("uuid").toString(), String.class);
				String preVolume = preContentObject.getString("volume");
				
				preContentObject.put("volume", volume);
				preContentObject.put("uuid", uuidList);
				
				boolean newFlag = compareDeviceList(uuidList, preUuidList);
				
				if(!newFlag || (!volume.equals(preVolume))){
					configTask.setType(type);
					configTask.setContent(content.getBytes());
					configTask.setCombineJv230Config(combineJv230Config);
					
					if(viewFlag){
						//修改混音
						logic.merge(createCombineAudio(configTask, jv230Channels));	
					}								
				}				
			}			
		}else{
			configTask = new ConfigTaskPO();
			configTask.setType(type);
			configTask.setContent(content.getBytes());
			configTask.setCombineJv230Config(combineJv230Config);
			
			configTaskPOList.add(configTask);
			
			if(viewFlag){
				//创建混音
				logic.merge(createCombineAudio(configTask, jv230Channels));
			}				
		}	
				
		combineJv230ConfigDao.save(combineJv230Config);
	}
	
	/**
	 * 下屏<br/>
	 * @Title: stoptLargescreenControl 
	 * @param largescreenConfigId 大屏配置id
	 * @return void
	 * @throws
	 */
	public void stopLargescreenControl(Long largescreenConfigId, Long userId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		CombineJv230ConfigPO largescreenConfig = combineJv230ConfigDao.findOne(largescreenConfigId);
		
		if(largescreenConfig.getStatus().equals(DEFAULT_STATUS_OFF)) return;
		
		List<Long> combineJv230Ids = new ArrayList<Long>();
		combineJv230Ids.add(largescreenConfig.getCombineJv230().getId());
		
		//清除混音
		CombineAudioBO CombineAudioDel = new CombineAudioBO();
		logic.setCombineAudioDel(new ArrayList<CombineAudioBO>());		
		Set<ConfigTaskPO> tasks = largescreenConfig.getConfigTasks();
		for(ConfigTaskPO task: tasks){
			if(task.getType().equals(TASK_TYPE_AUDIO)){
				CombineAudioDel.setUuid(task.getUuid());
				logic.getCombineAudioDel().add(CombineAudioDel);
			}
		}
		
		//获取所有的jv230通道
		List<Jv230ChannelDTO> jv230Channels = null;
		if(combineJv230Ids!=null && combineJv230Ids.size()>0) jv230Channels = jv230ChannelDao.findByCombineJv230Ids(combineJv230Ids);
		
		//下屏:所有jv230所有通道关闭
		logic.setJv230Disconnect(jv230Channels);
		
		largescreenConfig.setStatus(DEFAULT_STATUS_OFF);	
		
		logic.setUserId(userId.toString());
		
		executeBusiness.execute(logic, "下屏：");
	}
	
	/**
	 * 上屏<br/>
	 * @Title: startLargescreenControl 
	 * @param largescreenConfigId 大屏配置id
	 * @return void
	 * @throws
	 */
	public void startLargescreenControl(Long largescreenConfigId, Long userId) throws Exception{
		
		LogicBO logic = new LogicBO();
		
		CombineJv230ConfigPO largescreenConfig = combineJv230ConfigDao.findOne(largescreenConfigId);
		CombineJv230PO combineJv230 = largescreenConfig.getCombineJv230();
		Set<CombineJv230ConfigPO> largescreenConfigList = combineJv230.getConfigs();
		
		List<Jv230ChannelPO> jv230Channels = new ArrayList<Jv230ChannelPO>();
		Set<Jv230PO> jv230s = combineJv230.getBundles();
		for(Jv230PO jv230: jv230s){
			jv230Channels.addAll(jv230.getChannels());
		}
		
		for(CombineJv230ConfigPO largeConfig:largescreenConfigList){
			if(largeConfig.getStatus().equals(DEFAULT_STATUS_ON)){
				
				//TODO:清除
				//删除上屏任务协议
//				largescreenControlServiceImpl.stopLargescreen(largeConfig.getId());
				
				//清除混音
//				Map<String, Object> removeAudioMap = new HashMap<String, Object>();
//				removeAudioMap.put("largescreenConfigId", largeConfig.getId());
//				largescreenProcessControlServiceImpl.removeLargescreenAudio(removeAudioMap);
				
				//挂断设备
//				Map<String, Object> disconnectMap = new HashMap<String, Object>();
//				disconnectMap.put("largescreenConfigId", largeConfig.getId());
//				largescreenProcessControlServiceImpl.disconnectLargescreenDevice(disconnectMap);
			}
		}
		
		//呼叫Jv230
		logic.setJv230Connect(jv230Channels);
		
		//上屏协议
		logic.merge(startLargescreen(largescreenConfigId, userId));
		
		largescreenConfig.setStatus(DEFAULT_STATUS_ON);
		
		//jv230协议过滤重复的通道
		filterJv230ForwardSet(logic);
		
		logic.setUserId(userId.toString());
		
		executeBusiness.execute(logic, "上屏：");
		
	}
	
	/**
	 * 上屏协议生成
	 * @Title: startLargescreen 
	 * @param configId 大屏配置id
	 * @return LogicBO 协议
	 * @throws
	 */
	public LogicBO startLargescreen(Long configId, Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		
		if(logic.getJv230ForwardSet() == null) logic.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		if(logic.getCombineAudioSet() == null) logic.setCombineAudioSet(new ArrayList<CombineAudioBO>());
		if(logic.getJv230AudioSet() == null) logic.setJv230AudioSet(new ArrayList<Jv230ForwardBO>());
		
		CombineJv230ConfigPO config = combineJv230ConfigDao.findOne(configId);	
		
		List<Jv230ChannelPO> jv230Channels = new ArrayList<Jv230ChannelPO>();
		
		CombineJv230PO combineJv230 = config.getCombineJv230();
		Set<Jv230PO> jv230s = combineJv230.getBundles();
		for(Jv230PO jv230: jv230s){
			jv230Channels.addAll(jv230.getChannels());
		}
		
		Set<ConfigTaskPO> tasks = config.getConfigTasks();
		
		for(ConfigTaskPO configTask: tasks){		
			byte[] taskByte = configTask.getContent();
			String task = new String(taskByte);
			JSONObject taskObject = JSONObject.parseObject(task);
			
			if(configTask.getType().equals(TASK_TYPE_AUDIO)){

				//大屏创建混音协议
				logic.merge(createCombineAudio(configTask, jv230Channels));
				
			}else{
				Set<ConfigLocationPO> locations = configTask.getConfigLocations();
				
				if(locations != null && locations.size() > 0){				
					for(ConfigLocationPO configLocation: locations){
						
						Jv230BaseParamBO baseParam = new Jv230BaseParamBO();
						if(baseParam.getSources() == null) baseParam.setSources(new ArrayList<Jv230SourceBO>());
						
						byte[] locationByte = configLocation.getLocation();
						String location = new String(locationByte);
						
						JSONObject locationObject = JSONObject.parseObject(location);
						JSONArray locationArray = (JSONArray) locationObject.get("relation");
						JSONObject src = (JSONObject) locationArray.getJSONObject(0).get("src");
						JSONObject dst = (JSONObject) locationArray.getJSONObject(0).get("dst");
						
						Jv230ChannelPO locationChannel = queryVideoDecodeJv230ChannelByBundleIdAndDno(jv230Channels, dst.getString("uuid"), dst.getLong("dno"));
						
						PositionBO src_cut = new PositionBO().setHeight(Math.round(src.getFloat("h") * 10000))
															 .setWidth(Math.round(src.getFloat("w") * 10000))
															 .setX(Math.round(src.getFloat("x") * 10000))
															 .setY(Math.round(src.getFloat("y") * 10000));
						
						PositionBO display_rect = new PositionBO().setHeight(Math.round(dst.getFloat("h") * 10000))
								 								  .setWidth(Math.round(dst.getFloat("w") * 10000))
								 								  .setX(Math.round(dst.getFloat("x") * 10000))
								 								  .setY(Math.round(dst.getFloat("y") * 10000))
								 								  .setZ_index(dst.getIntValue("zindex"));
						
						baseParam.setCodec_type("h264")
								 .setSrc_mode(src.getIntValue("prop"))
								 .setSrc_identify(configTask.getUuid())
								 .setSrc_share_cnt(locations.size())
								 .setSrc_cut(src_cut)
								 .setDisplay_rect(display_rect);
						
						JSONObject srcTask = (JSONObject) taskObject.get("src");
						
						if(configTask.getType().equals(TASK_TYPE_DEVICE)){
							
							baseParam.setIs_polling("false");
							
							String srcBundleId = srcTask.getString("uuid").split("@@")[0];
							String srcLayerId = srcTask.getString("uuid").split("@@")[1];
							String srcChannelId = srcTask.getString("uuid").split("@@")[2];
							
							Jv230SourceBO jv230Source = new Jv230SourceBO().setBundle_id(srcBundleId)
																		   .setLayer_id(srcLayerId)
																		   .setChannel_id(srcChannelId);
							baseParam.getSources().add(jv230Source);
							
						}else if(configTask.getType().equals(TASK_TYPE_POLLING)){
							
							baseParam.setIs_polling("true")
									 .setInterval(Integer.parseInt(configTask.getTime()));
							
							List<String> srcUuids = JSONArray.parseArray(srcTask.getString("uuid"), String.class);

							for(String srcUuidSting: srcUuids){
								String srcBundleId = srcUuidSting.split("@@")[1];
								String srcLayerId = srcUuidSting.split("@@")[4];
								String srcChannelId = srcUuidSting.split("@@")[5];

								Jv230SourceBO jv230Source = new Jv230SourceBO().setBundle_id(srcBundleId)
																			   .setLayer_id(srcLayerId)
																			   .setChannel_id(srcChannelId);
								
								baseParam.getSources().add(jv230Source);
							}
							
						}
						
						Jv230ChannelParamBO channelParam = new Jv230ChannelParamBO().set(locationChannel, baseParam);
						Jv230ForwardBO forward = new Jv230ForwardBO().set(locationChannel, channelParam);
						logic.getJv230ForwardSet().add(forward);
					}
				}
			}		
		}
		
		return logic;
	}
	
	/**
	 * 根据bundleId和serialNum查询Jv230channel
	 * @Title: queryVideoDecodeJv230ChannelByBundleIdAndDno 
	 * @param channels 通道并集
	 * @param bundleId 设备id
	 * @param serialNum 序号
	 * @return Jv230ChannelPO Jv230Channel
	 * @throws
	 */
	public Jv230ChannelPO queryVideoDecodeJv230ChannelByBundleIdAndDno(Collection<Jv230ChannelPO> channels, String bundleId, Long serialNum){
		if(channels != null && channels.size()>0){
			for(Jv230ChannelPO channel: channels){
				if(channel.getType().isVideoDecode() && channel.getBundleId().equals(bundleId) && channel.getSerialNum().equals(serialNum)){
					return channel;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 选取默认的jv230音频解码通道</br>
	 * @Title: queryDefaultAudioDecodeJv230Channel 
	 * @Description: 默认选择大屏中序号为1的jv230
	 * @param channels 所有通道并集
	 * @return Jv230ChannelPO 音频解码通道 
	 * @throws
	 */
	public Jv230ChannelPO queryDefaultAudioDecodeJv230Channel(Collection<Jv230ChannelPO> channels){
		if(channels != null && channels.size()>0){
			for(Jv230ChannelPO channel: channels){
				if(channel.getType().isAudioDecode() && channel.getJv230().getSerialnum() == 1){
					return channel;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 根据bundleId查询设备信息
	 * @Title: queryBundleByBundleId 
	 * @param bundles 设备并集
	 * @param bundleId 设备id
	 * @return BundlePO 设备信息
	 * @throws
	 */
	public BundlePO queryBundleByBundleId(Collection<BundlePO> bundles, String bundleId){
		if(bundles != null && bundles.size() > 0){
			for(BundlePO bundle: bundles){
				if(bundle.getBundleId().equals(bundleId)){
					return bundle;
				}
			}
		}
		
		return null;
	}
	
	//过滤logic中重复的jv230转发
	public LogicBO filterJv230ForwardSet(LogicBO logic){
		
		List<Jv230ForwardBO> removeForwards = new ArrayList<Jv230ForwardBO>();
		List<Jv230ForwardBO> removeAudioForwards = new ArrayList<Jv230ForwardBO>();
		
		List<Jv230ForwardBO> jv230Forwards = logic.getJv230ForwardSet();
		List<Jv230ForwardBO> jv230AudioForwards = logic.getJv230AudioSet();
		
		for(Jv230ForwardBO forward: jv230Forwards){
			if(forward.getChannel_param().getBase_param().getSources().size()>0){
				for(Jv230ForwardBO _forward: jv230Forwards){
					if(_forward.getBundleId().equals(forward.getBundleId()) && _forward.getChannelId().equals(forward.getChannelId()) && _forward.getChannel_param().getBase_param().getSources().size() == 0){
						removeForwards.add(_forward);
					}
				}
			}
		}
		
		for(Jv230ForwardBO forward: jv230AudioForwards){
			if(forward.getChannel_param().getBase_param().getSource().getType() != null){
				for(Jv230ForwardBO _forward: jv230AudioForwards){
					if(_forward.getBundleId().equals(forward.getBundleId()) && _forward.getChannelId().equals(forward.getChannelId()) && _forward.getChannel_param().getBase_param().getSource().getType() == null){
						removeAudioForwards.add(_forward);
					}
				}
			}
		}
		
		jv230Forwards.removeAll(removeForwards);
		jv230AudioForwards.removeAll(removeAudioForwards);
		
		return logic;
	}
	
	//判断两个数组内容（无重复）是否相等
	public boolean compareDeviceList(List<String> newList, List<String> oldList){
		if(newList.size() == oldList.size()){
			for(String newElement: newList){
				if(!oldList.contains(newElement)){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 处理大屏音频
	 * @Title: createCombineAudio 
	 * @Description: 转发+混音
	 * @param configTask 音频任务
	 * @param jv230Channels jv230通道并集
	 * @return LogicBO  
	 * @throws
	 */
	public LogicBO createCombineAudio(ConfigTaskPO configTask, List<Jv230ChannelPO> jv230Channels){
		
		LogicBO logic = new LogicBO();
		
		byte[] taskByte = configTask.getContent();
		String task = new String(taskByte);
		JSONObject taskObject = JSONObject.parseObject(task);
		
		if(configTask.getType().equals(TASK_TYPE_AUDIO)){

			int volumn = taskObject.getIntValue("volume");
			List<JSONObject> audioList = JSONArray.parseArray(taskObject.getString("audioList"), JSONObject.class);
			
			Jv230BaseParamBO baseParam = new Jv230BaseParamBO();
			if(audioList.size() == 1){
				String srcBundleId = audioList.get(0).getString("uuid").split("@@")[0];
				String srcLayerId = audioList.get(0).getString("uuid").split("@@")[1];
				String srcChannelId = audioList.get(0).getString("uuid").split("@@")[2];
				
				//转发
				Jv230SourceBO sourceBO = new Jv230SourceBO().setType("channel")
															.setLayer_id(srcLayerId)
															.setBundle_id(srcBundleId)
															.setChannel_id(srcChannelId);
				
				baseParam.setSource(sourceBO)
					     .setCodec("aac");
				
			}else if(audioList.size()>1){
				AudioParamBO audio_param = new AudioParamBO().setCodec("aac")
											 				 .setGain(volumn);
				CodecParamBO codec_param = new CodecParamBO().setAudio_param(audio_param);
				
				CombineAudioBO combineAudioBO = new CombineAudioBO().setUuid(configTask.getUuid())
																	.setSrc(new ArrayList<SourceBO>())
																	.setCodec_param(codec_param);
				//混音
				for(JSONObject audio: audioList){
					String srcBundleId = audio.getString("uuid").split("@@")[0];
					String srcLayerId = audio.getString("uuid").split("@@")[1];
					String srcChannelId = audio.getString("uuid").split("@@")[2];
					
					SourceBO sourceBO = new SourceBO().setLayerId(srcLayerId)
													  .setBundleId(srcBundleId)
													  .setChannelId(srcChannelId);
					
					combineAudioBO.getSrc().add(sourceBO);
				}
				
				logic.getCombineAudioSet().add(combineAudioBO);
				
				Jv230SourceBO sourceBO = new Jv230SourceBO().setType("combineAudio")
															.setUuid(configTask.getUuid());

				baseParam.setSource(sourceBO)
						 .setCodec("aac");
			}
			
			Jv230ChannelPO audioChannel = queryDefaultAudioDecodeJv230Channel(jv230Channels);
			Jv230ChannelParamBO channelParam = new Jv230ChannelParamBO().set(audioChannel, baseParam);
			Jv230ForwardBO forward = new Jv230ForwardBO().set(audioChannel, channelParam);
			
			if(logic.getJv230AudioSet() == null) logic.setJv230AudioSet(new ArrayList<Jv230ForwardBO>()); 
			
			logic.getJv230AudioSet().add(forward);
		}
		
		return logic;
	}
}
