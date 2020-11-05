package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.dao.TerminalBindRepeaterDAO;
import com.sumavision.signal.bvc.entity.enumeration.ChannelType;
import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.signal.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.signal.bvc.resource.util.ResourceQueryUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class TerminalMappingService {
	
	@Autowired
	private PortMappingDAO portMappingDao;
	
	@Autowired
	private TerminalBindRepeaterDAO terminalBindRepeaterDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private RepeaterDAO repeaterDao;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;
	
	@Autowired
	private QueryUtilService queryUtilService;
	
	/**
	 * 删除终端绑定网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:39:23
	 * @param Long id
	 */
	public void remove(Long id) throws Exception{
		
		TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findOne(id);
		
		List<PortMappingPO> portMappingPOs = portMappingDao.findBySrcBundleIdOrDstBundleId(bind.getBundleId(), bind.getBundleId()); 
		
		List<Long> mappingIds = new ArrayList<Long>();
		for(PortMappingPO portMapping: portMappingPOs){
			mappingIds.add(portMapping.getId());
		}
		
		List<Long> needRemoveTaskIds = new ArrayList<Long>();
		
		List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
		for(TaskPO task: tasks){
			if(task.getStatus().equals(TaskStatus.zero.getStatus())){
				taskExecuteService.taskDestory(task);
			}else{
				needRemoveTaskIds.add(task.getId());
			}

		}
		
		taskDao.deleteByIdIn(needRemoveTaskIds);
		terminalBindRepeaterDao.delete(bind);
		portMappingDao.deleteInBatch(portMappingPOs);
		
	}
	
	/**
	 * 批量删除终端绑定网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月25日 下午2:20:23
	 */
	public void removeAll(List<Long> ids) throws Exception{
		
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findAll(ids);
		
		List<String> bundleIds = new ArrayList<String>();
		for(TerminalBindRepeaterPO bind: binds){
			bundleIds.add(bind.getBundleId());
		}
		
		List<PortMappingPO> portMappingPOs = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(bundleIds, bundleIds); 
		
		List<Long> mappingIds = new ArrayList<Long>();
		for(PortMappingPO portMapping: portMappingPOs){
			mappingIds.add(portMapping.getId());
		}
		
		List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
		List<Long> removeTaskIds = new ArrayList<Long>();
		for(TaskPO task: tasks){
			if(task.getStatus().equals(TaskStatus.zero.getStatus())){
				taskExecuteService.taskDestory(task);
			}else{
				removeTaskIds.add(task.getId());
			}
		}
		
		taskDao.deleteByIdIn(removeTaskIds);
		terminalBindRepeaterDao.deleteByIdIn(ids);
		portMappingDao.deleteByIdIn(mappingIds);
		
	}
	
	/**
	 * 生成端口映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:20:18
	 * @param InternetAccessPO access 网口
	 * @param List<TerminalBindRepeaterPO> binds 终端绑定网口关系
	 */
	public List<PortMappingPO> generatePortMapping(InternetAccessPO access, List<TerminalBindRepeaterPO> binds) throws Exception{
		
		String nodeId = RegisterStatus.getNodeId();
		
		//端口维护
		ConcurrentHashMap<String, ArrayList<Long>> mapping = new ConcurrentHashMap<String, ArrayList<Long>>();
		mapping.put(access.getAddress(), new ArrayList<Long>());
		
		List<PortMappingPO> allMappings = portMappingDao.findAll();
		
		//查portMapping
		List<PortMappingPO> accessPorts = queryByDstAddressAndDstType(allMappings, access.getAddress(), DstType.REPEATER);
		for(PortMappingPO accessPort: accessPorts){
			mapping.get(access.getAddress()).add(accessPort.getDstPort());
		}
		
		List<String> bundleIds = new ArrayList<String>();
		for(TerminalBindRepeaterPO bind: binds){
			bundleIds.add(bind.getBundleId());
		}
		
		List<ChannelSchemeDTO> channels = resourceQueryUtil.queryAllChannelsByBundleIds(bundleIds);
		for(ChannelSchemeDTO channel:channels){
			TerminalBindRepeaterPO bind = queryByBundleId(binds, channel.getBundleId());
			
			if(bind.getDeviceModel().equals("virtual")){
				
				if(bind.getLayerId() != null && bind.getLayerId().equals(nodeId)){
					
					PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
					
					if(mappingPO == null){
						//协商端口
						Long newPort = generatePort(mapping.get(access.getAddress()));
						
						PortMappingPO portPO = new PortMappingPO();
						portPO.setUpdateTime(new Date());
						portPO.setSrcType(SrcType.ROLE);
						portPO.setSrcBundleId(channel.getBundleId());
						portPO.setSrcBundleName(bind.getBundleName());
						portPO.setSrcChannelId(channel.getChannelId());
						portPO.setDstType(DstType.REPEATER);
						portPO.setDstRepeaterId(access.getRepeaterId());
						portPO.setDstAccessId(access.getId());
						portPO.setDstAddress(access.getAddress());
						portPO.setDstPort(newPort);
						
						allMappings.add(portPO);
						
						mapping.get(access.getAddress()).add(newPort);
					}
				}else{
					
					PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
					
					if(mappingPO == null){
						//协商端口
						Long newPort = generatePort(mapping.get(access.getAddress()));
						
						PortMappingPO portPO = new PortMappingPO();
						portPO.setUpdateTime(new Date());
						portPO.setSrcType(SrcType.TERMINAL);
						portPO.setSrcBundleId(channel.getBundleId());
						portPO.setSrcBundleName(bind.getBundleName());
						portPO.setSrcChannelId(channel.getChannelId());
						portPO.setDstType(DstType.REPEATER);
						portPO.setDstRepeaterId(access.getRepeaterId());
						portPO.setDstAccessId(access.getId());
						portPO.setDstAddress(access.getAddress());
						portPO.setDstPort(newPort);
						
						allMappings.add(portPO);
						
						mapping.get(access.getAddress()).add(newPort);
					}
				}

				
			}else{
				
				if(bind.getLayerId() != null && bind.getLayerId().equals(nodeId)){
					if(channel.getChannelName().equals("VenusAudioIn") || channel.getChannelName().equals("VenusVideoIn")){
						
						PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							//协商端口
							Long newPort = generatePort(mapping.get(access.getAddress()));
							
							PortMappingPO portPO = new PortMappingPO();
							portPO.setUpdateTime(new Date());
							portPO.setSrcType(SrcType.TERMINAL);
							portPO.setSrcBundleId(channel.getBundleId());
							portPO.setSrcBundleName(bind.getBundleName());
							portPO.setSrcChannelId(channel.getChannelId());
							portPO.setDstType(DstType.REPEATER);
							portPO.setDstRepeaterId(access.getRepeaterId());
							portPO.setDstAccessId(access.getId());
							portPO.setDstAddress(access.getAddress());
							portPO.setDstPort(newPort);
							
							allMappings.add(portPO);
							
							mapping.get(access.getAddress()).add(newPort);
						}
						
					}
					
					if(channel.getChannelName().equals("VenusAudioOut") || channel.getChannelName().equals("VenusVideoOut")){
						
						PortMappingPO mappingPO = queryByDstBundleIdAndDstChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							
							//区分代理设备（proxy）
							if(channel.getDeviceModel().equals("proxy")){
								
								String channelId = channel.getChannelId();
								//协商端口
								Long newPort = generatePort(mapping.get(access.getAddress()));
								PortMappingPO portPO = new PortMappingPO();
								portPO.setUpdateTime(new Date());
								portPO.setDstType(DstType.TERMINAL);
								portPO.setDstBundleId(channel.getBundleId());
								portPO.setDstBundleName(bind.getBundleName());
								portPO.setDstChannelId(channelId);
								portPO.setDstAddress(bind.getBundleIp());
								portPO.setDstPort(newPort);
								
								allMappings.add(portPO);
								
								mapping.get(access.getAddress()).add(newPort);
							}else{
								
								String channelId = channel.getChannelId();
								Long port = ChannelType.fromType(channelId).getPort();
								PortMappingPO portPO = new PortMappingPO();
								portPO.setUpdateTime(new Date());
								portPO.setDstType(DstType.TERMINAL);
								portPO.setDstBundleId(channel.getBundleId());
								portPO.setDstBundleName(bind.getBundleName());
								portPO.setDstChannelId(channelId);
								portPO.setDstAddress(bind.getBundleIp());
								portPO.setDstPort(port);
								
								allMappings.add(portPO);
							}
						}
					}
				}else{
					
					if(channel.getChannelName().equals("VenusAudioIn") || channel.getChannelName().equals("VenusVideoIn")){
						
						PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							//协商端口
							Long newPort = generatePort(mapping.get(access.getAddress()));
							
							PortMappingPO portPO = new PortMappingPO();
							portPO.setUpdateTime(new Date());
							portPO.setSrcType(SrcType.TERMINAL);
							portPO.setSrcBundleId(channel.getBundleId());
							portPO.setSrcBundleName(bind.getBundleName());
							portPO.setSrcChannelId(channel.getChannelId());
							portPO.setDstType(DstType.REPEATER);
							portPO.setDstRepeaterId(access.getRepeaterId());
							portPO.setDstAccessId(access.getId());
							portPO.setDstAddress(access.getAddress());
							portPO.setDstPort(newPort);
							
							allMappings.add(portPO);
							
							mapping.get(access.getAddress()).add(newPort);
						}
						
					}

					if(channel.getChannelName().equals("VenusVideoMix") || channel.getChannelName().equals("VenusAudioMix")){
						
						PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							//协商端口
							Long newPort = generatePort(mapping.get(access.getAddress()));
							
							PortMappingPO portPO = new PortMappingPO();
							portPO.setUpdateTime(new Date());
							portPO.setSrcType(SrcType.TERMINAL);
							portPO.setSrcBundleId(channel.getBundleId());
							portPO.setSrcChannelId(channel.getChannelId());
							portPO.setSrcBundleName(bind.getBundleName());
							portPO.setDstType(DstType.REPEATER);
							portPO.setDstRepeaterId(access.getRepeaterId());
							portPO.setDstAccessId(access.getId());
							portPO.setDstAddress(access.getAddress());
							portPO.setDstPort(newPort);
							
							allMappings.add(portPO);
							
							mapping.get(access.getAddress()).add(newPort);
						}
					}
				}
			}
			
		}
		
		portMappingDao.save(allMappings);
		
		return allMappings;
	}
	
	/**
	 * 生成端口映射--全量<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:20:18
	 * @param List<TerminalBindRepeaterPO> binds 终端绑定网口关系
	 */
	public List<PortMappingPO> generatePortMapping(List<TerminalBindRepeaterPO> binds) throws Exception{
		
		String nodeId = RegisterStatus.getNodeId();
		
		//端口维护
		ConcurrentHashMap<String, ArrayList<Long>> mapping = new ConcurrentHashMap<String, ArrayList<Long>>();
		Set<String> addresses = new HashSet<String>();
		for(TerminalBindRepeaterPO bind: binds){
			addresses.add(bind.getAccessAddress());
		}	
		
		List<PortMappingPO> allMappings = portMappingDao.findAll();
		
		for(String address: addresses){
			mapping.put(address, new ArrayList<Long>());
			
			//查portMapping
			List<PortMappingPO> accessPorts = queryByDstAddressAndDstType(allMappings, address, DstType.REPEATER);
			for(PortMappingPO accessPort: accessPorts){
				mapping.get(address).add(accessPort.getDstPort());
			}
		}
		
		
		List<String> bundleIds = new ArrayList<String>();
		for(TerminalBindRepeaterPO bind: binds){
			bundleIds.add(bind.getBundleId());
		}
		
		List<ChannelSchemeDTO> channels = resourceQueryUtil.queryAllChannelsByBundleIds(bundleIds);
		for(ChannelSchemeDTO channel:channels){
			
			TerminalBindRepeaterPO bind = queryByBundleId(binds, channel.getBundleId());
			
			if(bind.getDeviceModel().equals("virtual")){
				
				if(bind.getLayerId() != null && bind.getLayerId().equals(nodeId)){
					
					PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
					
					if(mappingPO == null){
						//协商端口
						Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
						
						PortMappingPO portPO = new PortMappingPO();
						portPO.setUpdateTime(new Date());
						portPO.setSrcType(SrcType.ROLE);
						portPO.setSrcBundleId(channel.getBundleId());
						portPO.setSrcBundleName(bind.getBundleName());
						portPO.setSrcChannelId(channel.getChannelId());
						portPO.setDstType(DstType.REPEATER);
						portPO.setDstRepeaterId(bind.getRepeaterId());
						portPO.setDstAccessId(bind.getAccessId());
						portPO.setDstAddress(bind.getAccessAddress());
						portPO.setDstPort(newPort);
						
						allMappings.add(portPO);
						
						mapping.get(bind.getAccessAddress()).add(newPort);
					}
				}else{
					
					PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
					
					if(mappingPO == null){
						//协商端口
						Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
						
						PortMappingPO portPO = new PortMappingPO();
						portPO.setUpdateTime(new Date());
						portPO.setSrcType(SrcType.TERMINAL);
						portPO.setSrcBundleId(channel.getBundleId());
						portPO.setSrcBundleName(bind.getBundleName());
						portPO.setSrcChannelId(channel.getChannelId());
						portPO.setDstType(DstType.REPEATER);
						portPO.setDstRepeaterId(bind.getRepeaterId());
						portPO.setDstAccessId(bind.getAccessId());
						portPO.setDstAddress(bind.getAccessAddress());
						portPO.setDstPort(newPort);
						
						allMappings.add(portPO);
						
						mapping.get(bind.getAccessAddress()).add(newPort);
					}
				}

				
			}else{
			
				if(bind.getLayerId() != null && bind.getLayerId().equals(nodeId)){
					if(channel.getChannelName().equals("VenusAudioIn") || channel.getChannelName().equals("VenusVideoIn")){
						
						PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							//协商端口
							Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
							
							PortMappingPO portPO = new PortMappingPO();
							portPO.setUpdateTime(new Date());
							portPO.setSrcType(SrcType.TERMINAL);
							portPO.setSrcBundleId(channel.getBundleId());
							portPO.setSrcBundleName(bind.getBundleName());
							portPO.setSrcChannelId(channel.getChannelId());
							portPO.setDstType(DstType.REPEATER);
							portPO.setDstRepeaterId(bind.getRepeaterId());
							portPO.setDstAccessId(bind.getAccessId());
							portPO.setDstAddress(bind.getAccessAddress());
							portPO.setDstPort(newPort);
							
							allMappings.add(portPO);
							
							mapping.get(bind.getAccessAddress()).add(newPort);
						}
						
					}
					
					if(channel.getChannelName().equals("VenusAudioOut") || channel.getChannelName().equals("VenusVideoOut")){
						
						PortMappingPO mappingPO = queryByDstBundleIdAndDstChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							
							//区分代理设备（proxy）
							if(channel.getDeviceModel().equals("proxy")){
								
								String channelId = channel.getChannelId();
								//协商端口
								Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
								PortMappingPO portPO = new PortMappingPO();
								portPO.setUpdateTime(new Date());
								portPO.setDstType(DstType.TERMINAL);
								portPO.setDstBundleId(channel.getBundleId());
								portPO.setDstBundleName(bind.getBundleName());
								portPO.setDstChannelId(channelId);
								portPO.setDstAddress(bind.getBundleIp());
								portPO.setDstPort(newPort);
								
								allMappings.add(portPO);
								
								mapping.get(bind.getAccessAddress()).add(newPort);
							}else{
								
								String channelId = channel.getChannelId();
								Long port = ChannelType.fromType(channelId).getPort();
								PortMappingPO portPO = new PortMappingPO();
								portPO.setUpdateTime(new Date());
								portPO.setDstType(DstType.TERMINAL);
								portPO.setDstBundleId(channel.getBundleId());
								portPO.setDstBundleName(bind.getBundleName());
								portPO.setDstChannelId(channelId);
								portPO.setDstAddress(bind.getBundleIp());
								portPO.setDstPort(port);
								
								allMappings.add(portPO);
							}
						}
					}
				}else{
					
					if(channel.getChannelName().equals("VenusAudioIn") || channel.getChannelName().equals("VenusVideoIn")){
						
						PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							//协商端口
							Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
							
							PortMappingPO portPO = new PortMappingPO();
							portPO.setUpdateTime(new Date());
							portPO.setSrcType(SrcType.TERMINAL);
							portPO.setSrcBundleId(channel.getBundleId());
							portPO.setSrcBundleName(bind.getBundleName());
							portPO.setSrcChannelId(channel.getChannelId());
							portPO.setDstType(DstType.REPEATER);
							portPO.setDstRepeaterId(bind.getRepeaterId());
							portPO.setDstAccessId(bind.getAccessId());
							portPO.setDstAddress(bind.getAccessAddress());
							portPO.setDstPort(newPort);
							
							allMappings.add(portPO);
							
							mapping.get(bind.getAccessAddress()).add(newPort);
						}
						
					}
	
					if(channel.getChannelName().equals("VenusVideoMix") || channel.getChannelName().equals("VenusAudioMix")){
						
						PortMappingPO mappingPO = queryBySrcBundleIdAndSrcChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							//协商端口
							Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
							
							PortMappingPO portPO = new PortMappingPO();
							portPO.setUpdateTime(new Date());
							portPO.setSrcType(SrcType.TERMINAL);
							portPO.setSrcBundleId(channel.getBundleId());
							portPO.setSrcChannelId(channel.getChannelId());
							portPO.setSrcBundleName(bind.getBundleName());
							portPO.setDstType(DstType.REPEATER);
							portPO.setDstRepeaterId(bind.getRepeaterId());
							portPO.setDstAccessId(bind.getAccessId());
							portPO.setDstAddress(bind.getAccessAddress());
							portPO.setDstPort(newPort);
							
							allMappings.add(portPO);
							
							mapping.get(bind.getAccessAddress()).add(newPort);
						}
					}
				}
			}
		}
		
		portMappingDao.save(allMappings);
		
		return allMappings;
	}
	
	/**
	 * 根据bundleId查询绑定<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午9:26:55
	 * @param List<TerminalBindRepeaterPO> binds
	 * @param String bundleId
	 * @return TerminalBindRepeaterPO
	 */
	public TerminalBindRepeaterPO queryByBundleId(List<TerminalBindRepeaterPO> binds, String bundleId){
		for(TerminalBindRepeaterPO bind: binds){
			if(bind.getBundleId().equals(bundleId)){
				return bind;
			}
		}
		
		return null;
	}
	
	/**
	 * 协商新端口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午8:49:49
	 * @param List<Long> ports
	 * @return Long 
	 */
	public Long generatePort(List<Long> ports){
		if(ports == null || ports.size() <= 0){
			return 10000l;
		}
		
		Collections.sort(ports);
		
		Long newPort = ports.get(ports.size()-1) + 2l;
		
		return newPort;
	}
	
	/**
	 * 同步转码器任务下发--已有的任务不变，新增的任务下发<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午1:47:53
	 * @throws Exception
	 */
	public void generateTask() throws Exception{
		
		//暂时这么取
		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		
		RepeaterPO main = null;
		RepeaterPO backup = null;
		if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
		if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);
		
		//查询已存在的任务
		List<TaskPO> tasks = taskDao.findAll();
		
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findAll();
		if(binds == null || binds.size() == 0) throw new Exception("未绑定终端！");
		
		//获取端口映射列表
		List<PortMappingPO> mappings =  generatePortMapping(binds);
		if(mappings != null && mappings.size() > 0){
			for(PortMappingPO mapping: mappings){
				if(mapping.getSrcType() != null && mapping.getSrcType().equals(SrcType.TERMINAL) && mapping.getDstType() != null && mapping.getDstType().equals(DstType.REPEATER)){
					
				}else{	
					if(main != null){
						TaskPO mainTask = queryUtilService.queryTask(tasks, mapping.getId(), main.getIp());
						if(mainTask == null || !mainTask.getStatus().equals(TaskStatus.zero.getStatus())){
							taskExecuteService.taskCreatePost(main.getIp(), mapping, null, null);
						}
					}
					if(backup != null){
						TaskPO backupTask = queryUtilService.queryTask(tasks, mapping.getId(), backup.getIp());
						if(backupTask == null || !backupTask.getStatus().equals(TaskStatus.zero.getStatus())){
							taskExecuteService.taskCreatePost(backup.getIp(), mapping, null, null);
						}
					}					
				}
			}
		}
		
//		taskDao.save(tasks);
	}
	/**
	 * 销毁任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午3:05:03
	 * @throws Exception
	 */
	public void removeTask() throws Exception{
		
		//暂时这么取
		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		
		RepeaterPO main = null;
		RepeaterPO backup = null;
		if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
		if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);
		
		List<TaskPO> needRemoveTasks = new ArrayList<TaskPO>(); 
		//批量删除改为重置任务
		if(main != null){
			String res = taskExecuteService.resetDevice(main);
			Long status = JSON.parseObject(res).getLong("result");
			if(status.equals(0l)){
				List<TaskPO> tasks = taskDao.findByIp(main.getIp());
				needRemoveTasks.addAll(tasks);
			}
		}
		
		if(backup != null){
			String res = taskExecuteService.resetDevice(backup);
			Long status = JSON.parseObject(res).getLong("result");
			if(status.equals(0l)){
				List<TaskPO> tasks = taskDao.findByIp(backup.getIp());
				needRemoveTasks.addAll(tasks);
			}
		}
		
		if(needRemoveTasks != null && needRemoveTasks.size() > 0){
			List<Long> ids = new ArrayList<Long>();
			for(TaskPO taskPO: needRemoveTasks){
				ids.add(taskPO.getId());
			}
			taskDao.deleteByIdIn(ids);
		}
		
		//查询已存在的任务
//		List<TaskPO> tasks = taskDao.findAll();
//		List<Long> needRemoveTaskIds = new ArrayList<Long>();
//		
//		for(TaskPO task: tasks){
//			if(task.getStatus().equals(TaskStatus.zero.getStatus())){
//				taskExecuteService.taskDestory(task);
//			}else{
//				needRemoveTaskIds.add(task.getId());
//			}
//		}
		
		//删除未建成功的任务
//		if(needRemoveTaskIds != null && needRemoveTaskIds.size() > 0){
//			taskDao.deleteByIdIn(needRemoveTaskIds);
//		}
//		
		List<PortMappingPO> portMappings = portMappingDao.findAll();
		if(portMappings != null && portMappings.size() > 0){
			List<Long> ids = new ArrayList<Long>();
			for(PortMappingPO portMappingPO: portMappings){
				ids.add(portMappingPO.getId());
			}
			portMappingDao.deleteByIdIn(ids);
		}

	}
	
	/**
	 * 更新设备信息,layerId变化则会出现创建和销毁任务</p>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月27日 下午5:19:20
	 */
	public List<TerminalBindRepeaterPO> updateBundle(Long id) throws Exception{
		
		String layerId = RegisterStatus.getNodeId();
		
		//暂时这么取
		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		
		RepeaterPO main = null;
		RepeaterPO backup = null;
		if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
		if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);
		
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findByAccessId(id);
		
		List<String> bundleIds = new ArrayList<String>();
		if(binds != null && binds.size() > 0){
			
			//端口维护
			ConcurrentHashMap<String, ArrayList<Long>> mapping = new ConcurrentHashMap<String, ArrayList<Long>>();
			Set<String> addresses = new HashSet<String>();
			for(TerminalBindRepeaterPO bind: binds){
				addresses.add(bind.getAccessAddress());
				bundleIds.add(bind.getBundleId());
			}	
			
			List<PortMappingPO> allMappings = portMappingDao.findAll();
			
			for(String address: addresses){
				mapping.put(address, new ArrayList<Long>());
				
				//查portMapping
				List<PortMappingPO> accessPorts = queryByDstAddressAndDstType(allMappings, address, DstType.REPEATER);
				for(PortMappingPO accessPort: accessPorts){
					mapping.get(address).add(accessPort.getDstPort());
				}
			}
			
			List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(bundleIds);
			List<TerminalBindRepeaterPO> needRemoveBinds = new ArrayList<TerminalBindRepeaterPO>();
			List<TerminalBindRepeaterPO> layerUpdateBinds = new ArrayList<TerminalBindRepeaterPO>();
			for(TerminalBindRepeaterPO bind: binds){
				boolean flag = true;
				for(BundlePO bundle: bundles){
					if(bundle.getBundleId().equals(bind.getBundleId())){
						
						bind.setBundleName(bundle.getBundleName());

						if(bundle.getAccessNodeUid() != null && !bundle.getAccessNodeUid().equals(bind.getLayerId())){
							bind.setLayerId(bundle.getAccessNodeUid());
							layerUpdateBinds.add(bind);
						}
						
						flag = false;
						break;
					}
				}
				
				if(flag){
					needRemoveBinds.add(bind);
				}
			}
			
			List<String> needRemoveBundleIds = new ArrayList<String>();
			List<String> needAddBundleIds = new ArrayList<String>();
			if(layerUpdateBinds.size() > 0){
				for(TerminalBindRepeaterPO bind: layerUpdateBinds){
					if(bind.getLayerId().equals(layerId)){
						needAddBundleIds.add(bind.getBundleId());
					}else{
						needRemoveBundleIds.add(bind.getBundleId());
					}
				}
			}
			
			List<Long> mappingIds = new ArrayList<Long>();
			if(needRemoveBinds.size() > 0){
				List<String> removeBundleIds = new ArrayList<String>();
				for(TerminalBindRepeaterPO bind: needRemoveBinds){
					removeBundleIds.add(bind.getBundleId());
				}
				
				List<PortMappingPO> mappings = queryByDstBundleIdInAndSrcBundleIdIn(allMappings, removeBundleIds);
				for(PortMappingPO mappingPO: mappings){
					mappingIds.add(mappingPO.getId());
				}
				
				binds.removeAll(needRemoveBinds);
			}
			
			if(needRemoveBundleIds.size() > 0){
				List<PortMappingPO> mappings = queryByDstBundleIdIn(allMappings, needRemoveBundleIds);
				for(PortMappingPO mappingPO: mappings){
					mappingIds.add(mappingPO.getId());
				}
			}
			
			if(mappingIds.size() > 0){
				List<TaskPO> needRemoveTasks = taskDao.findByMappingIdIn(mappingIds);
				for(TaskPO task: needRemoveTasks){
					if(task.getStatus().equals(TaskStatus.zero.getStatus())){
						taskExecuteService.taskDestory(task);
					}
				}
				
				taskDao.deleteInBatch(needRemoveTasks);
				portMappingDao.deleteByIdIn(mappingIds);
			}
			
			if(needAddBundleIds.size() > 0){
				List<PortMappingPO> needAddMappings = new ArrayList<PortMappingPO>();
				List<ChannelSchemeDTO> channels = resourceQueryUtil.queryAllChannelsByBundleIds(needAddBundleIds);
				for(ChannelSchemeDTO channel: channels){

					TerminalBindRepeaterPO bind = queryByBundleId(binds, channel.getBundleId());
						
					if(channel.getChannelName().equals("VenusAudioOut") || channel.getChannelName().equals("VenusVideoOut")){
						
						PortMappingPO mappingPO = queryByDstBundleIdAndDstChannelId(allMappings, channel.getBundleId(), channel.getChannelId());
						
						if(mappingPO == null){
							
							//区分代理设备（proxy）
							if(channel.getDeviceModel().equals("proxy")){
								
								String channelId = channel.getChannelId();
								//协商端口
								Long newPort = generatePort(mapping.get(bind.getAccessAddress()));
								PortMappingPO portPO = new PortMappingPO();
								portPO.setUpdateTime(new Date());
								portPO.setDstType(DstType.TERMINAL);
								portPO.setDstBundleId(channel.getBundleId());
								portPO.setDstBundleName(bind.getBundleName());
								portPO.setDstChannelId(channelId);
								portPO.setDstAddress(bind.getBundleIp());
								portPO.setDstPort(newPort);
								
								needAddMappings.add(portPO);
								
								mapping.get(bind.getAccessAddress()).add(newPort);
							}else{
								
								String channelId = channel.getChannelId();
								Long port = ChannelType.fromType(channelId).getPort();
								PortMappingPO portPO = new PortMappingPO();
								portPO.setUpdateTime(new Date());
								portPO.setDstType(DstType.TERMINAL);
								portPO.setDstBundleId(channel.getBundleId());
								portPO.setDstBundleName(bind.getBundleName());
								portPO.setDstChannelId(channelId);
								portPO.setDstAddress(bind.getBundleIp());
								portPO.setDstPort(port);
								
								needAddMappings.add(portPO);
							}
						}
					}
				}
				
				portMappingDao.save(needAddMappings);
				
				for(PortMappingPO mappingPO: needAddMappings){
					if(main != null){
						taskExecuteService.taskCreatePost(main.getIp(), mappingPO, null, null);
					}
					if(backup != null){
						taskExecuteService.taskCreatePost(backup.getIp(), mappingPO, null, null);
					}

				}
			}
			
			terminalBindRepeaterDao.save(binds);
		}
		
		return binds;
	}
	
	/**
	 * 判断映射关系是否已经建立映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午1:49:36
	 * @param List<TaskPO> tasks 任务列表
	 * @param Long mappingId 映射关系id
	 * @return boolean
	 */
	public boolean isTask(List<TaskPO> tasks, Long mappingId){
		for(TaskPO task: tasks){
			if(task.getMappingId().equals(mappingId)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断映射关系是否已经建立映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午1:49:36
	 * @param List<TaskPO> tasks 任务列表
	 * @param PortMappingPO mapping 映射关系
	 * @return boolean
	 */
	public boolean isTask(List<TaskPO> tasks, PortMappingPO mapping){
		for(TaskPO task: tasks){
			if(task.getMappingId().equals(mapping.getId())){
				return true;
			}else if(task.getDstIp().equals(mapping.getDstAddress()) && task.getDstPort().equals(mapping.getDstPort())){
				task.setMappingId(mapping.getId());
				return true;
			}
		}
		return false;
	}
	
	public List<PortMappingPO> queryByDstAddressAndDstType(List<PortMappingPO> mappings, String dstAddress, DstType dstType){
		List<PortMappingPO> filterMappings = new ArrayList<PortMappingPO>();
		if(mappings != null && mappings.size() > 0){
			for(PortMappingPO mapping: mappings){
				if(mapping.getDstAddress() != null && mapping.getDstAddress().equals(dstAddress) && mapping.getDstType() != null && mapping.getDstType().equals(dstType)){
					filterMappings.add(mapping);
				}
			}
		}
		
		return filterMappings;
	}
	
	public PortMappingPO queryBySrcBundleIdAndSrcChannelId(List<PortMappingPO> mappings, String bundleId, String channelId){
		if(mappings != null && mappings.size() > 0){
			for(PortMappingPO mapping: mappings){
				if(mapping.getSrcBundleId() != null && mapping.getSrcBundleId().equals(bundleId) && mapping.getSrcChannelId() != null && mapping.getSrcChannelId().equals(channelId)){
					return mapping;
				}
			}
		}
		return null;
	}
	
	public PortMappingPO queryByDstBundleIdAndDstChannelId(List<PortMappingPO> mappings, String bundleId, String channelId){
		if(mappings != null && mappings.size() > 0){
			for(PortMappingPO mapping: mappings){
				if(mapping.getDstBundleId() != null && mapping.getDstBundleId().equals(bundleId) && mapping.getDstChannelId() != null && mapping.getDstChannelId().equals(channelId)){
					return mapping;
				}
			}
		}
		return null;
	}
	
	public List<PortMappingPO> queryByDstBundleIdIn(List<PortMappingPO> mappings, List<String> bundleIds){
		List<PortMappingPO> _mappings = new ArrayList<PortMappingPO>();
		for(PortMappingPO mapping: mappings){
			if(mapping.getDstBundleId() != null && bundleIds.contains(mapping.getDstBundleId())){
				_mappings.add(mapping);
			}
		}
		
		return _mappings;
	}
	
	public List<PortMappingPO> queryByDstBundleIdInAndSrcBundleIdIn(List<PortMappingPO> mappings, List<String> bundleIds){
		List<PortMappingPO> _mappings = new ArrayList<PortMappingPO>();
		for(PortMappingPO mapping: mappings){
			if((mapping.getDstBundleId() != null && bundleIds.contains(mapping.getDstBundleId())) ||
					(mapping.getDstBundleId() != null && bundleIds.contains(mapping.getSrcBundleId()))){
				_mappings.add(mapping);
			}
		}
		
		return _mappings;
	}
}
