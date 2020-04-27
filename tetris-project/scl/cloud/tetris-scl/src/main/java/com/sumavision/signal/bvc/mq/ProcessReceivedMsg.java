package com.sumavision.signal.bvc.mq;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead;
import com.suma.venus.message.bo.VenusMessageHead.MsgType;
import com.suma.venus.message.mq.ResponseBO;
import com.suma.venus.message.util.RegisterStatus;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.common.enumeration.NodeType;
import com.sumavision.signal.bvc.config.CapacityProps;
import com.sumavision.signal.bvc.entity.dao.CapacityPermissionPortDAO;
import com.sumavision.signal.bvc.entity.dao.InternetAccessDAO;
import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.dao.TerminalBindRepeaterDAO;
import com.sumavision.signal.bvc.entity.enumeration.ChannelType;
import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.Jv210Param;
import com.sumavision.signal.bvc.entity.enumeration.Jv220Param;
import com.sumavision.signal.bvc.entity.enumeration.OneOneFiveMParam;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.CapacityPermissionPortPO;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.signal.bvc.entity.vo.TranscodeVO;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.http.HttpClient;
import com.sumavision.signal.bvc.mq.bo.BaseParamBO;
import com.sumavision.signal.bvc.mq.bo.BundleBO;
import com.sumavision.signal.bvc.mq.bo.ChannelBO;
import com.sumavision.signal.bvc.mq.bo.PassbyBO;
import com.sumavision.signal.bvc.mq.bo.PlayerBO;
import com.sumavision.signal.bvc.mq.bo.RectBO;
import com.sumavision.signal.bvc.mq.bo.ScreenBO;
import com.sumavision.signal.bvc.mq.bo.SourceBO;
import com.sumavision.signal.bvc.network.service.NetworkService;
import com.sumavision.signal.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.signal.bvc.service.MQSendService;
import com.sumavision.signal.bvc.service.MqExecutorService;
import com.sumavision.signal.bvc.service.QueryUtilService;
import com.sumavision.signal.bvc.service.TaskExecuteService;
import com.sumavision.signal.bvc.service.TerminalCallingCallBack;
import com.sumavision.signal.bvc.service.TerminalMappingService;
import com.sumavision.signal.bvc.terminal.JV220Param;
import com.sumavision.signal.bvc.terminal.OneOneFiveMTerminal;
import com.sumavision.signal.bvc.terminal.TerminalParam;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 消息处理类，处理接收到的activemq消息
 *
 * @author wjw 2019年5月17日
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcessReceivedMsg {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessReceivedMsg.class);
	
	@Autowired
	private PortMappingDAO portMappingDao;
	
	@Autowired
	private TerminalBindRepeaterDAO terminalBindRepeaterDao;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private RepeaterDAO repeaterDao;
	
	@Autowired
	private InternetAccessDAO internetAccessDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;
	
	@Autowired
	private TerminalMappingService terminalMappingService;
	
	@Autowired
	private MQSendService mqSendService;
	
	@Autowired
	private QueryUtilService queryUtilService;
	
	@Autowired
	private JV220Param jv220Param;
	
	@Autowired
	private OneOneFiveMTerminal oneOneFiveMTerminal;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private CapacityProps capacityProps;
	
	@Autowired
	private CapacityPermissionPortDAO capacityPermissionPortDao;
	
	@Autowired
	private NetworkService networkService;
    
    /*
     * 根据接口协议的数据类型解析msg中的方法名，根据相应方法名注入上层解析接口进行处理
     */
    public void process(String msg) throws Exception {
    	ResponseBO responseBo = JSONObject.parseObject(msg, ResponseBO.class);
    	String msgType = responseBo.getMessage().getMessage_header().getMessage_type();
    	if(MsgType.request.toString().equalsIgnoreCase(msgType) || MsgType.notification.toString().equalsIgnoreCase(msgType) 
        		|| MsgType.alert.toString().equalsIgnoreCase(msgType) || MsgType.passby.toString().equalsIgnoreCase(msgType)){
            switch (responseBo.getMessage().getMessage_header().getMessage_name()) {
            case "open_bundle":
            	processOpenBundleMsg(responseBo);
            	break;
            case "close_bundle":
            	processCloseBundleMsg(responseBo);
            	break;
            case "passby":
            	processPassByMsg(responseBo);
            	break;
            case "pullData":
            	processPullDataMsg(responseBo);
            	break;
            case "cancelData":
            	processCancelDataMsg(responseBo);
            	break;
            default:
                break;
            }
        }else if(MsgType.response.toString().equalsIgnoreCase(msgType)){
        	switch (responseBo.getMessage().getMessage_header().getMessage_name()) {
        	case "pullData":
            	processResPullDataMsg(responseBo);
            	break;
        	default:
        		break;
        	}
        }
    }
    
    /**处理open_bundle信息
     * @throws Exception */
    private void processOpenBundleMsg(ResponseBO responseBo) throws Exception{
    	
    	String openBundleRequestString = responseBo.getMessage().getMessage_body().getString("open_bundle_request");
    	JSONObject openBundleRequest = JSON.parseObject(openBundleRequestString);
    	BundleBO bundle = openBundleRequest.getObject("bundle", BundleBO.class);
    	String bundleType = bundle.getBundle_type();
    	String deviceModel = bundle.getDevice_model();
    	
    	String layerId = responseBo.getMessage().getMessage_header().getDestination_id();
    	
    	if(deviceModel != null){
    		if(deviceModel.equals("5G")){
        		processOpenBundle5GMsg(bundle);
    		}else if(deviceModel.equals("player")){
    			processOpenBundlePlayerMsg(bundle);
    		}
    	}else{
    		switch (bundleType) {
			case "VenusTerminal":
				processOpenBundleVenusTerminalMsg(bundle, layerId);
				break;
			case "VenusVirtual":
				processOpenBundleVenusVirtualMsg(bundle);
				break;
			case "VenusProxy":
				processOpenBundleVenusProxyMsg(bundle);
				break;
			default:
				break;
			}
    	}
    }
    
    /**处理colse_bundle信息
     * @throws Exception */
    private void processCloseBundleMsg(ResponseBO responseBo) throws Exception{
    	
    	String closeBundleRequestString = responseBo.getMessage().getMessage_body().getString("close_bundle_request");
    	JSONObject openBundleRequest = JSON.parseObject(closeBundleRequestString);
    	BundleBO bundle = openBundleRequest.getObject("bundle", BundleBO.class);
    	
    	String bundleType = bundle.getBundle_type();
    	String deviceModel = bundle.getDevice_model();
    	
    	String layerId = responseBo.getMessage().getMessage_header().getDestination_id();
    	
    	if(deviceModel != null){
    		if(deviceModel.equals("5G")){
        		processCloseBundle5GMsg(bundle);
    		}else if(deviceModel.equals("player")){
    			processCloseBundlePlayerMsg(bundle);
    		}
    	}else{
	    	switch (bundleType) {
			case "VenusTerminal":
				processCloseBundleVenusTerminalMsg(bundle, layerId);
				break;
			case "VenusVirtual":
				processCloseBundleVenusVirtualMsg(bundle);
				break;
			case "VenusProxy":
				processCloseBundleVenusProxyMsg(bundle);
				break;
			default:
				break;
			}
    	}
    }
    
    /**处理pass_by信息信息
     * @throws Exception */
    private void processPassByMsg(ResponseBO responseBo) throws Exception{
    	
    	String passbyString = responseBo.getMessage().getMessage_body().getString("pass_by");
    	PassbyBO passby = JSON.parseObject(passbyString, PassbyBO.class);
    	
    	String type = passby.getType();
    	switch (type) {
		case "create_repeater_node":
			processPassByCreateNodeMsg(passby);
			break;
		case "remove_repeater_node":
			processPassByRemoveNodeMsg(passby);
			break;
		default:
			break;
		}
    }
    
    /**处理pullData信息信息
    * @throws Exception */
    private void processPullDataMsg(ResponseBO responseBo) throws Exception{
    	
    	synchronized (this) {
    		//暂时这么取
    		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
    		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
    		
    		RepeaterPO main = null;
    		RepeaterPO backup = null;
    		if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
    		if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);
    		
    		VenusMessageHead head = responseBo.getMessage().getMessage_header();
        	
        	JSONObject messageBody = responseBo.getMessage().getMessage_body();
        	
        	String seq = messageBody.getString("seq");
        	
        	//接入层有共享流机制，得到的bundleId和channelId可能不认识
        	String peerBundleId = messageBody.getString("peerBundleId");
        	String peerChannelId = messageBody.getString("peerChannelId");
        	String localBundleId = messageBody.getString("localBundleId");
        	String localChannelID = messageBody.getString("localChannelID");
        	String peerEndpoint = messageBody.getString("peerEndpoint");
        	String peerReflectEndpoint = messageBody.getString("peerReflectEndpoint");
        	
        	
        	String[] address = peerReflectEndpoint.split(":");
        	String ip = "0.0.0.0";
        	String port = "60000";
        	
        	if(address.length > 1){
        		ip = address[0];
        		port = address[1];
        	}else{
        		return;
        	}
        	
        	PortMappingPO mapping = portMappingDao.findByDstAddressAndDstPort(ip, Long.valueOf(port));
        	PortMappingPO srcMapping = portMappingDao.findBySrcBundleIdAndSrcChannelId(localBundleId, localChannelID);
        	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(peerBundleId);

        	if(srcMapping == null) throw new Exception(localBundleId + "该终端未绑定转发器！");
        	
        	if(mapping == null){
            	mapping = new PortMappingPO();
            	mapping.setDstType(DstType.LAYER);
            	mapping.setDstBundleId(peerBundleId);
            	mapping.setDstBundleName(peerBundleId);
            	mapping.setDstChannelId(peerChannelId);
            	if(bind != null){
                	mapping.setDstBundleName(bind.getBundleName());
            	}
            	mapping.setDstAddress(ip);
            	mapping.setDstPort(Long.valueOf(port));
            	mapping.setUpdateTime(new Date());
            	portMappingDao.save(mapping);
            	
            	List<TaskPO> newTasks = new ArrayList<TaskPO>();
            	if(main != null){
            		TaskPO task = new TaskPO();
    				task.setMappingId(mapping.getId());
    				task.setIp(main.getIp());
    				task.setDstIp(ip);
    				task.setDstPort(port);
    				task.setStatus(TaskStatus.twenty.getStatus());
    				
    				newTasks.add(task);
            	}
            	if(backup != null){
            		TaskPO task = new TaskPO();
    				task.setMappingId(mapping.getId());
    				task.setIp(backup.getIp());
    				task.setDstIp(ip);
    				task.setDstPort(port);
    				task.setStatus(TaskStatus.twenty.getStatus());
    				
    				newTasks.add(task);
            	}
            	
            	taskDao.save(newTasks);
            	
            	if(main != null){
                	taskExecuteService.taskCreatePost(main.getIp(), mapping, srcMapping.getDstAddress(), srcMapping.getDstPort());
            	}
            	if(backup != null){
            		taskExecuteService.taskCreatePost(backup.getIp(), mapping, srcMapping.getDstAddress(), srcMapping.getDstPort());
            	}
        	}else{
        		mapping.setDstBundleId(peerBundleId);
        		mapping.setDstBundleName(peerBundleId);
            	mapping.setDstChannelId(peerChannelId);
            	portMappingDao.save(mapping);
            	
        		List<TaskPO> tasks = taskDao.findByMappingId(mapping.getId());
        		if(tasks == null || tasks.size() <= 0){
        			
        			List<TaskPO> newTasks = new ArrayList<TaskPO>();
                	if(main != null){
                		TaskPO task = new TaskPO();
        				task.setMappingId(mapping.getId());
        				task.setIp(main.getIp());
        				task.setDstIp(ip);
        				task.setDstPort(port);
        				task.setStatus(TaskStatus.twenty.getStatus());
        				
        				newTasks.add(task);
                	}
                	if(backup != null){
                		TaskPO task = new TaskPO();
        				task.setMappingId(mapping.getId());
        				task.setIp(backup.getIp());
        				task.setDstIp(ip);
        				task.setDstPort(port);
        				task.setStatus(TaskStatus.twenty.getStatus());
        				
        				newTasks.add(task);
                	}
                	
                	taskDao.save(newTasks);
                	
        			if(main != null){
                    	taskExecuteService.taskCreatePost(main.getIp(), mapping, srcMapping.getDstAddress(), srcMapping.getDstPort());
                	}
                	if(backup != null){
                		taskExecuteService.taskCreatePost(backup.getIp(), mapping, srcMapping.getDstAddress(), srcMapping.getDstPort());
                	}
        		}else{
        			for(TaskPO task: tasks){
        				if(task.getStatus().equals(TaskStatus.zero.getStatus()) && (!task.getSrcIp().equals(srcMapping.getDstAddress()) || !task.getSrcPort().equals(srcMapping.getDstPort().toString()))){
        					taskExecuteService.taskSwitch(task, srcMapping.getDstAddress(), srcMapping.getDstPort());
        				}else if(task.getStatus().equals(TaskStatus.negative.getStatus())){
        					taskExecuteService.taskCreatePost(task.getIp(), mapping, srcMapping.getDstAddress(), srcMapping.getDstPort());
        				}
        			}
        		}
        	}
        	
        	//pulldata -- response
        	pullDataResponseRunable(mapping, head.getSource_id(), head.getDestination_id(), localBundleId, localChannelID, peerBundleId, peerChannelId, peerEndpoint, peerReflectEndpoint, seq);

		}
    }
    
    /**处理cancelData信息信息
     * @throws Exception */
	private void processCancelDataMsg(ResponseBO responseBo) throws Exception{
		
		synchronized (this) {
			 
			JSONObject messageBody = responseBo.getMessage().getMessage_body();
			
			String peerBundleId = messageBody.getString("peerBundleId");
			String peerChannelId = messageBody.getString("peerChannelId");
			String localBundleId = messageBody.getString("localBundleId");
			String localChannelID = messageBody.getString("localChannelID");
			
			List<PortMappingPO> mappings = portMappingDao.findByDstBundleIdAndDstChannelId(peerBundleId, peerChannelId);
			PortMappingPO mappingSrc = portMappingDao.findBySrcBundleIdAndSrcChannelId(localBundleId, localChannelID);
			List<Long> ids = new ArrayList<Long>();
			for(PortMappingPO mapping: mappings){
				ids.add(mapping.getId());
			}
			
			System.out.println("cancelData: " + peerBundleId + "-" + peerChannelId);
			
		 	List<TaskPO> tasks = taskDao.findByMappingIdIn(ids);
		 	for(TaskPO task: tasks){
		 		if(task.getStatus().equals(TaskStatus.zero.getStatus())){
			 		//销毁任务
//			 		taskExecuteService.taskDestory(task);
			 		//切换为无效
			 		taskExecuteService.taskSwitch(task, mappingSrc.getDstAddress(), 60000l);
		 		}
		 	}
//		 	taskDao.deleteInBatch(tasks);
//		 	portMappingDao.deleteByIdIn(ids);
		}
	}
    
    /**处理pass_by信息的switch_repeater_node信息
     * @throws Exception */
    private void processPassBySwitchNodeMsg(BundleBO bundle) throws Exception{
    	System.out.println("switch_repeater_node！");
    	
    	String bundleId = bundle.getBundle_id();
    	
    	List<PortMappingPO> mappings = portMappingDao.findAll();
    	
    	List<TaskPO> tasks = taskDao.findByStatus(TaskStatus.zero.getStatus());
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	String bundlesString = passbyBO.getPass_by_content().getString("bundles");
    	String[] bundles = bundlesString.split("%");
    	List<String> bundleIds = new ArrayList<String>();
    	for(String bundleString: bundles){
    		bundleIds.add(bundleString);
    	}
    	
    	List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findAll();
    	TerminalBindRepeaterPO abind = queryUtilService.queryBindByBundleId(binds, bundleId);
		
    	//标识（设置大小屏）
    	boolean screenFlag = false;
    	
    	//显示模式：单画面--"0",二等分--"1",画中画--"2"
    	String screenLayout = "0";
    	//画面：远端1--"0",远端2--"1",本地1--"2",本地2--"3"
    	String picture1 = "0";
    	String picture2 = "0";
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				String srcChannelId = source.getChannel_id();
				String srcLayerId = source.getLayer_id();
				TerminalBindRepeaterPO srcBind = queryUtilService.queryBindByBundleId(binds, srcBundleId);
				
				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId, SrcType.TERMINAL);
				PortMappingPO mappingDst = queryUtilService.querySrcPortMapping(mappings, bundleId, channelId, SrcType.ROLE); 
				
				//合屏源pullData
				if(passbyBO.getLayer_id() != null && srcLayerId != null && !passbyBO.getLayer_id().equals(srcLayerId)){
					processPullDataRunable(srcLayerId, passbyBO.getLayer_id(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
				}
				
				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				if(taskPOs != null && taskPOs.size() > 0){
					//任务切换
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
					}
				}
				
			}else{		
				
				PortMappingPO mappingDst = queryUtilService.querySrcPortMapping(mappings, bundleId, channelId, SrcType.ROLE); 
				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				if(taskPOs != null && taskPOs.size() > 0){
					//任务切换
					for(TaskPO task: taskPOs){
						//切换为无效
						taskExecuteService.taskSwitch(task, mappingDst.getDstAddress(), 60000l);
					}
				}
		
				screenFlag = true;
	    		screenLayout = "0";
	    		picture1 = "2";			
			}
		}  
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			boolean isSingle = true;
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						picture1 = Jv210Param.fromName(rect.getChannel()).getValue();
    					}else{
    						picture2 = Jv210Param.fromName(rect.getChannel()).getValue();
    						if(rect.getType().equals("single")){
    							isSingle = false;
    						}
    					}
    				}
    			}
    			if(isSingle){
    				screenLayout = "0";
    			}else{
    				screenLayout = "2";
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(screenFlag){
    		for(TerminalBindRepeaterPO bind: binds){
    			
    			if(bind.getDeviceModel().equals("jv210") && bundleIds.contains(bind.getBundleId())){
        			
    				//通话请求异步处理
    				String postUrl = "http://" + bind.getBundleIp() + TerminalParam.POST_JV210_CALLSETTING_SUFFIX;
    				HttpAsyncClient.getInstance().formGet("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_CALLSETTING_SUFFIX, null, new TerminalCallingCallBack(postUrl, screenLayout, picture1, picture2));
    				
    			}
    		}
        }
    }
    
    /**处理pass_by信息的bind_repeater_node信息
     * @throws Exception */
    private void processPassByBindNodeMsg(BundleBO bundle) throws Exception{
    	
    	String passby = bundle.getPass_by_str();
    	String roleBundleId = bundle.getBundle_id();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	String terminalBundles = passbyBO.getPass_by_content().getString("bundles");
    	String[] terminalBundleIds = terminalBundles.split("%");
    	
    	List<String> bundleIds = new ArrayList<String>();
    	for(String bundleId: terminalBundleIds){
    		bundleIds.add(bundleId);
    	}
    	
    	List<PortMappingPO> roleMappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, roleBundleId);
    	List<PortMappingPO> terminalMappings = portMappingDao.findByDstTypeAndDstBundleIdIn(DstType.TERMINAL, bundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO mapping: terminalMappings){
    		mappingIds.add(mapping.getId());
    	}
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(mappingIds, TaskStatus.zero.getStatus());
    	for(PortMappingPO roleMapping: roleMappings){
    		String roleChannelId = roleMapping.getSrcChannelId();
    		for(PortMappingPO terminalMapping: terminalMappings){
    			if(terminalMapping.getDstChannelId().equals(roleChannelId)){
    				for(TaskPO task: tasks){
    					if(task.getMappingId().equals(terminalMapping.getId())){
    						taskExecuteService.taskSwitch(task, roleMapping.getDstAddress(), roleMapping.getDstPort());
    					}
    				}
    			}
    		}
    	}
    	
    	//显示模式：单画面--"0",二等分--"1",画中画--"2"
    	String screenLayout = "0";
    	//画面：远端1--"0",远端2--"1",本地1--"2",本地2--"3"
    	String picture1 = "0";
    	String picture2 = "0";
    	
    	boolean screenFlag = false;
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						picture1 = Jv210Param.fromName(rect.getChannel()).getValue();
    					}else{
    						picture2 = Jv210Param.fromName(rect.getChannel()).getValue();
    					}
    				}
    			}
    			if(rects.size() == 1){
    				screenLayout = "0";
    			}else{
    				screenLayout = "2";
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(screenFlag){
    		
    		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findByBundleIdIn(bundleIds);
    		
    		for(TerminalBindRepeaterPO bind: binds){
  
				//通话请求异步处理
				String postUrl = "http://" + bind.getBundleIp() + TerminalParam.POST_JV210_CALLSETTING_SUFFIX;
				HttpAsyncClient.getInstance().formGet("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_CALLSETTING_SUFFIX, null, new TerminalCallingCallBack(postUrl, screenLayout, picture1, picture2));

    		}
    	}
    	
    	System.out.println("终端绑定二级节点完毕！");
    	
    }
    
    /**处理pass_by信息的unbind_repeater_node信息
     * @throws Exception */
    private void processPassByUnbindNodeMsg(BundleBO bundle) throws Exception{
    	
    	String passby = bundle.getPass_by_str();
    	String roleBundleId = bundle.getBundle_id();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	String terminalBundles = passbyBO.getPass_by_content().getString("bundles");
    	String[] terminalBundleIds = terminalBundles.split("%");
    	
    	List<String> bundleIds = new ArrayList<String>();
    	for(String bundleId: terminalBundleIds){
    		bundleIds.add(bundleId);
    	}
    	
    	List<PortMappingPO> roleMappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, roleBundleId);
    	List<PortMappingPO> terminalMappings = portMappingDao.findByDstTypeAndDstBundleIdIn(DstType.TERMINAL, bundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO mapping: terminalMappings){
    		mappingIds.add(mapping.getId());
    	}
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(mappingIds, TaskStatus.zero.getStatus());
    	for(PortMappingPO roleMapping: roleMappings){
    		String roleChannelId = roleMapping.getSrcChannelId();
    		for(PortMappingPO terminalMapping: terminalMappings){
    			if(terminalMapping.getDstChannelId().equals(roleChannelId)){
    				for(TaskPO task: tasks){
    					if(task.getMappingId().equals(terminalMapping.getId())){
    						//切换为无效
    						taskExecuteService.taskSwitch(task, roleMapping.getDstAddress(), 60000l);
    					}
    				}
    			}
    		}
    	}
    	
    	System.out.println("终端绑定二级节点完毕！");
    }
    
    /**处理pass_by信息的create_repeater_node信息
     * @throws Exception */
    private void processPassByCreateNodeMsg(PassbyBO passby) throws Exception{
    	
    	synchronized (this) {
    		//暂时这么取
    		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
    		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
    		
    		RepeaterPO main = null;
    		RepeaterPO backup = null;
    		if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
    		if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);
        	
        	String bundleId = passby.getBundle_id();
        	String channels = passby.getPass_by_content().getString("channels");
        	String[] channelIds = channels.split("%");
        	
        	//TODO:取一个转发器的输入网口
    		InternetAccessPO access = internetAccessDao.findByMainRepeater();
    		//查询portMapping是否已经存在
    		List<PortMappingPO> mappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, bundleId);
    		
        	//端口维护
    		ConcurrentHashMap<String, ArrayList<Long>> mapping = new ConcurrentHashMap<String, ArrayList<Long>>();
    		mapping.put(access.getAddress(), new ArrayList<Long>());
    		
    		//查portMapping
    		List<PortMappingPO> accessPorts = portMappingDao.findByDstAddressAndDstType(access.getAddress(), DstType.REPEATER);
    		for(PortMappingPO accessPort: accessPorts){
    			mapping.get(access.getAddress()).add(accessPort.getDstPort());
    		}
    		
    		List<PortMappingPO> mappingPOs = new ArrayList<PortMappingPO>();
    		List<Long> mappingIds = new ArrayList<Long>();
    		for(String channelId: channelIds){
    			
    			PortMappingPO mappingPO = queryUtilService.queryMappingBySrcChannelId(mappings, channelId);
    			if(mappingPO == null){
        			//协商端口
        			Long newPort = terminalMappingService.generatePort(mapping.get(access.getAddress()));
        			
        			PortMappingPO portPO = new PortMappingPO();
        			portPO.setUpdateTime(new Date());
        			portPO.setSrcType(SrcType.ROLE);
        			portPO.setSrcBundleId(bundleId);
        			portPO.setSrcChannelId(channelId);
        			portPO.setDstType(DstType.REPEATER);
        			portPO.setDstRepeaterId(access.getRepeaterId());
        			portPO.setDstAccessId(access.getId());
        			portPO.setDstAddress(access.getAddress());
        			portPO.setDstPort(newPort);
        			
        			mapping.get(access.getAddress()).add(newPort);
        			mappingPOs.add(portPO);
    			}else{
    				mappingPOs.add(mappingPO);
    			}
    		}

    		portMappingDao.save(mappingPOs);
    		
    		for(PortMappingPO mappingPO: mappingPOs){
    			mappingIds.add(mappingPO.getId());
    		}
    		
    		List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
    		
    		for(PortMappingPO mappingPO: mappingPOs){
    			
    			if(main != null){
					TaskPO mainTask = queryUtilService.queryTask(tasks, mappingPO.getId(), main.getIp());
					if(mainTask == null || !mainTask.getStatus().equals(TaskStatus.zero.getStatus())){
						taskExecuteService.taskCreatePost(main.getIp(), mappingPO, null, null);
					}
				}
				if(backup != null){
					TaskPO backupTask = queryUtilService.queryTask(tasks, mappingPO.getId(), backup.getIp());
					if(backupTask == null || !backupTask.getStatus().equals(TaskStatus.zero.getStatus())){
						taskExecuteService.taskCreatePost(backup.getIp(), mappingPO, null, null);
					}
				}
    		}
    		
    		System.out.println("二级节点任务创建成功，节点bundleId为：" + bundleId);
        }
	}
    
    /**处理pass_by信息的remove_repeater_node信息
     * @throws Exception */
    private void processPassByRemoveNodeMsg(PassbyBO passby) throws Exception{
    	
    	String bundleId = passby.getBundle_id();
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, bundleId);
    	
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO mapping: mappings){
    		mappingIds.add(mapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(mappingIds, TaskStatus.zero.getStatus());
    	for(TaskPO task: tasks){
    		taskExecuteService.taskDestory(task);
    	}
    	
    	portMappingDao.deleteInBatch(mappings);
    	
    	System.out.println("二级节点任务销毁成功，节点bundleId为：" + bundleId);
    }
    
    /**处理open_bundle信息的VenusTerminal类型信息
     * @throws Exception */
    private void processOpenBundleVenusTerminalMsg(BundleBO bundle, String layerId) throws Exception{
    	
    	String bundleId = bundle.getBundle_id();
    	
    	if(layerId.equals(NodeType.NETWORK.getId())){
    		
    		networkService.processOpenBundleVenusTerminalMsg(bundle);
    		
    	}else{
    		
        	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
        	
        	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
        	
        	if(bind.getDeviceModel().equals("jv210")){
        		
        		processOpenBundleJv210Msg(bundle, bind);
        		
        	}else if(bind.getDeviceModel().equals("jv220")){
        		
        		processOpenBundleJv220Msg(bundle, bind);
        		
        	}
        	
    	}
    }
    
    /**处理open_bundle信息的jv220类型信息
     * @throws Exception */
    private void processOpenBundleJv220Msg(BundleBO bundle, TerminalBindRepeaterPO bind) throws Exception{
      	    	
    	String bundleId = bundle.getBundle_id();
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	//获取Jv220参数
//    	String getparam = HttpClient.post("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.getJv220Param().toJSONString());
//    	String result = JSONObject.parseObject(getparam).getString("result");
//    	String v_send_0 = JSONObject.parseObject(result).getString("v_send_0");
//		switchButton = JSONObject.parseObject(v_send_0).getString("ctrl");
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	//显示模式：单画面--0,画中画--1,二等分--2
    	Long screenLayout = 0l;
    	Long window1 = 8l;
    	Long window2 = 0l;
    	
    	boolean screenFlag = false;
    	
    	Set<String> srcBundleIds = new HashSet<String>();
    	Set<String> dstBundleIds = new HashSet<String>();
    	srcBundleIds.add(bundleId);
    	dstBundleIds.add(bundleId);
    	//取bundleId和channelId用于查询
    	for(ChannelBO channel:channels){
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				srcBundleIds.add(srcBundleId);
			}
    	}
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(srcBundleIds, dstBundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO portMapping: mappings){
    		mappingIds.add(portMapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
    	
    	JSONObject multimediaParam = new JSONObject();
    	
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){
				
    			Long main_bitrate = baseParam.getBitrate();
    	       	Long trasferMainBitrate = main_bitrate/1000l;
    	       	//视频码率
            	Long mainVideoBitrate = (long) (trasferMainBitrate * (7.0f/10.0f) > 4000l?4000l: trasferMainBitrate * (7.0f/10.0f));
            	//发送码率
            	Long mainAllBitrate = (long) (mainVideoBitrate * (28.0f/10.0f) > 10000l?10000l: mainVideoBitrate * (28.0f/10.0f));
    			//编码分辨率
            	String main_resoluton = baseParam.getResolution();
    			//编码格式
            	String main_codec = baseParam.getCodec();
    			
    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
    			String main_video_encode_address = mappingPO.getDstAddress();
    	    	Long main_video_encode_port = mappingPO.getDstPort();
    	    	
    	    	JSONObject v_send_0 = new JSONObject();
    	    	v_send_0.put("ip_send_addr", main_video_encode_address);
    	    	v_send_0.put("ip_send_port", main_video_encode_port);
    	    	v_send_0.put("sys_band", mainAllBitrate);
    	    	v_send_0.put("ctrl", 1l);
    	    	v_send_0.put("rtp_video_pt", Jv220Param.fromName(main_codec).getRelation());
    	    	
    	    	JSONObject v_enc_0 = new JSONObject();
    	    	v_enc_0.put("enc_bitrate", mainVideoBitrate);
    	    	v_enc_0.put("enc_reso", Jv220Param.fromName(main_resoluton).getProtocal());
    	    	v_enc_0.put("codec", Jv220Param.fromName(main_codec).getProtocal());
    	    	multimediaParam.put("v_send_0", v_send_0);
    	    	multimediaParam.put("v_enc_0", v_enc_0);
    	    	
    		}else if(channelId.equals(ChannelType.VIDEOENCODE2.getType())){
    			
    			Long sub_bitrate = baseParam.getBitrate();
    			Long trasferSubBitrate = sub_bitrate/1000l;
    			//视频码率
            	Long subVideoBitrate = (long) (trasferSubBitrate * (7.0f/10.0f) > 4000l?4000l: trasferSubBitrate * (7.0f/10.0f));
            	//发送码率
            	Long subAllBitrate = (long) (subVideoBitrate * (28.0f/10.0f) > 10000l?10000l: subVideoBitrate * (28.0f/10.0f));
            	//编码分辨率
    			String sub_resoluton = baseParam.getResolution();
    			//编码格式
            	String sub_codec = baseParam.getCodec();
            	
    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
    			String sub_video_encode_address = mappingPO.getDstAddress();
    	    	Long sub_video_encode_port = mappingPO.getDstPort(); 
    	    	
    	    	JSONObject v_send_1 = new JSONObject();
    	    	v_send_1.put("ip_send_addr", sub_video_encode_address);
    	    	v_send_1.put("ip_send_port", sub_video_encode_port);
    	    	v_send_1.put("sys_band", subAllBitrate);
    	    	v_send_1.put("ctrl", 1l);
    	    	v_send_1.put("rtp_video_pt", Jv220Param.fromName(sub_codec).getRelation());
    	    	
    	    	JSONObject v_enc_1 = new JSONObject();
    	    	v_enc_1.put("enc_bitrate", subVideoBitrate);
    	    	v_enc_1.put("enc_reso", Jv220Param.fromName(sub_resoluton).getProtocal());
    	    	v_enc_1.put("codec", Jv220Param.fromName(sub_codec).getProtocal());
    	    	multimediaParam.put("v_send_1", v_send_1);
    	    	multimediaParam.put("v_enc_1", v_enc_1);
    	    	
    		}else if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
    			
    			String audio_codec = baseParam.getCodec();
    			String gain = baseParam.getGain();
    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
    			String audio_encode_address = mappingPO.getDstAddress();
    	    	Long audio_encode_port = mappingPO.getDstPort();  
    	    	
    	    	JSONObject a_send= new JSONObject();
    	    	a_send.put("ip_send_addr", audio_encode_address);
    	    	a_send.put("ip_send_port", audio_encode_port);
    	    	a_send.put("ctrl", 1l);
    	    	a_send.put("codec", Jv220Param.fromName(audio_codec).getProtocal());
    	    	a_send.put("rtp_audio_pt", Jv220Param.fromName(audio_codec).getRelation());
    	    	
    	    	multimediaParam.put("a_send", a_send);
    	    	
    		}else if(channelId.equals(ChannelType.VIDEODECODE1.getType())){
    			
    			String codec = baseParam.getCodec();
    			
    	    	JSONObject vdec_0 = new JSONObject();
    	    	vdec_0.put("codec", Jv220Param.fromName(codec).getProtocal());
    	    	multimediaParam.put("vdec_0", vdec_0);
    	    	
    	    	JSONObject recv_0 = multimediaParam.getJSONObject("recv_0");
    	    	if(recv_0 == null){
    	    		recv_0 = new JSONObject();
    	    	}
    	    	recv_0.put("rtp_video_pt", Jv220Param.fromName(codec).getRelation());
    	    	multimediaParam.put("recv_0", recv_0);
    	    	
    	    	JSONObject videoDecodeParam0 = new JSONObject();
    	    	JSONObject ctrl = new JSONObject();
    	    	ctrl.put("ctrl", 1l);
    	    	videoDecodeParam0.put("udp_recv", ctrl);
    	    	
    	    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(0l, videoDecodeParam0).toJSONString(), null, null);
    	    	
    		}else if(channelId.equals(ChannelType.VIDEODECODE2.getType())){
    			
    			String codec = baseParam.getCodec();
    	    	
    	    	JSONObject vdec_1 = new JSONObject();
    	    	vdec_1.put("codec", Jv220Param.fromName(codec).getProtocal());
    	    	multimediaParam.put("vdec_1", vdec_1);
    	    	
    	    	JSONObject recv_1 = new JSONObject();
    	    	recv_1.put("rtp_video_pt", Jv220Param.fromName(codec).getRelation());
    	    	multimediaParam.put("recv_1", recv_1);
    	    	
    	    	JSONObject videoDecodeParam1 = new JSONObject();
    	    	JSONObject ctrl = new JSONObject();
    	    	ctrl.put("ctrl", 1l);
    	    	videoDecodeParam1.put("udp_recv", ctrl);
    	    	
    	    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(1l, videoDecodeParam1).toJSONString(), null, null);
    	    	
    		}else if(channelId.equals(ChannelType.AUDIODECODE1.getType())){
				
    			String codec = baseParam.getCodec();
    			
    	    	JSONObject adec_0 = new JSONObject();
    	    	adec_0.put("codec", Jv220Param.fromName(codec).getProtocal());
    	    	multimediaParam.put("adec_0", adec_0);
    	    	
    	    	JSONObject recv_0 = multimediaParam.getJSONObject("recv_0");
    	    	if(recv_0 == null){
    	    		recv_0 = new JSONObject();
    	    	}
    	    	recv_0.put("rtp_audio_pt", Jv220Param.fromName(codec).getRelation());
    	    	multimediaParam.put("recv_0", recv_0);
    	    	
    	    	JSONObject audioDecodeParam0 = new JSONObject();
    	    	JSONObject ctrl = new JSONObject();
    	    	ctrl.put("ctrl", 1l);
    	    	audioDecodeParam0.put("udp_recv", ctrl);
    	    	
    	    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(0l, audioDecodeParam0).toJSONString(), null, null);
    	    	
    		}
			
			PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				String srcChannelId = source.getChannel_id();
				String srcLayerId = source.getLayer_id();
				
				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
				
				if(mappingPO == null) throw new Exception(srcBundleId + "源终端未绑定转发器！");
				
				//layerId不相同，向其接入发pulldata
				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){

					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
							
				}
				
				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				if(taskPOs != null && taskPOs.size() > 0){
					//任务切换
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
					}
				}
			}else{
				
				List<TaskPO> taskPOs = null;
				if(mappingDst != null){
					taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				}
				
				if(taskPOs != null){
					//切换为无效--(需要切换--占用带宽)
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
					}
				}
			}			
		}  
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			boolean isSingle = true; 
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						window1 = Jv220Param.fromName(rect.getChannel()).getProtocal();
    					}else{
    						window2 = Jv220Param.fromName(rect.getChannel()).getProtocal();
    						if(rect.getType().equals("single")){
    							isSingle = false;
    						}
    					}
    				}
    			}
    			if(isSingle){
    				screenLayout = 0l;
    			}else{
    				screenLayout = 1l;
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(passbyBO != null){
        	String type = passbyBO.getType();
        	if(type.equals("bind_repeater_node")){
        		screenFlag = false;
        	}
    	}
    	
    	if(screenFlag){

    		JSONObject v_disp_0 = new JSONObject();
    		v_disp_0.put("mode", screenLayout);
    		v_disp_0.put("win_0_ch", window1);
    		v_disp_0.put("win_1_ch", window2);
    		
    		multimediaParam.put("v_disp_0", v_disp_0);
    	}
    	
    	//multimedia参数设置
    	if(!multimediaParam.isEmpty()){
    		HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.multimediaSet(multimediaParam).toJSONString(), null, null);
    	}
    	
    }
    
    /**处理open_bundle信息的jv220类型信息
     * @throws Exception */
    private void processOpenBundle5GMsg(BundleBO bundle) throws Exception{
    
    	String bundleId = bundle.getBundle_id();
    	
    	List<String> bundleIds = new ArrayList<String>();
    	bundleIds.add(bundleId);
    	
    	List<BundlePO> bundles = resourceBundleDao.findByBundleIds(bundleIds);
    	if(bundles == null || bundles.size() <= 0){
    		throw new BaseException(StatusCode.ERROR, "bundleId为：" + bundleId + "的设备不存在！");
    	}
    	
    	BundlePO bundlePO = bundles.get(0);
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	boolean isEncode = false;
    	
    	for(ChannelBO channel: channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){
				
				//视频码率
    			Long bitrate = baseParam.getBitrate();
    			//发送码率
    			Long send_bitrate = bitrate * 4;
    			//编码分辨率
            	String resoluton = baseParam.getResolution();
    			//编码格式
            	String codec = baseParam.getCodec();
            	//帧率
            	String fps = baseParam.getFps();
            	
            	String format_resolution = new StringBufferWrapper().append(resoluton)
            			              							    .append("P")
            			              							    .append(fps)
            			              							    .toString();
            	
            	//视频编码参数设置
            	JSONObject vidEncParams = new JSONObject();
            	JSONObject prog1vidEnc = new JSONObject();
            	prog1vidEnc.put("onoff", 1);
            	prog1vidEnc.put("vidEncRes", OneOneFiveMParam.fromName(format_resolution).getProtocal());
            	prog1vidEnc.put("vidEncBR", bitrate);
            	prog1vidEnc.put("vidSysBR", send_bitrate);
            	prog1vidEnc.put("vidEncStd", OneOneFiveMParam.fromName(codec).getProtocal());
            	vidEncParams.put("prog1vidEnc", prog1vidEnc);
            	
        		HttpAsyncClient.getInstance().httpAsyncPost("http://" + bundlePO.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.vidEncSet(vidEncParams).toJSONString(), null, null);
            	
            	isEncode = true;
            	
			}
			
			if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
				//音频编码
				String codec = baseParam.getCodec();
				//采样率
				Long sample_rate = baseParam.getSample_rate();
				//音频码率
				Long bitrate = baseParam.getBitrate();
				
				//为了保证enum的name唯一
				String format_bitrate = new StringBufferWrapper().append(bitrate)
																 .append("abr")
																 .toString();
				String format_sample_rate = new StringBufferWrapper().append(sample_rate)
																	 .append("sr")
																	 .toString();
				
				//音频编码参数设置
				JSONObject audEncParams = new JSONObject();
				JSONObject prog1audEnc = new JSONObject();
				prog1audEnc.put("audSampleRate", OneOneFiveMParam.fromName(format_sample_rate).getProtocal());
				JSONArray audEncPara = new JSONArray();
				JSONObject audEnc = new JSONObject();
				audEnc.put("audOnOff", 1);
				audEnc.put("audRate", OneOneFiveMParam.fromName(format_bitrate).getProtocal());
				audEnc.put("audStd", OneOneFiveMParam.fromName(codec).getProtocal());
				audEncPara.add(audEnc);
				prog1audEnc.put("audEncPara", audEncPara);
				audEncParams.put("prog1audEnc", prog1audEnc);
				
				HttpAsyncClient.getInstance().httpAsyncPost("http://" + bundlePO.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.audEncSet(audEncParams).toJSONString(), null, null);
				
				isEncode = true;
				
			}
			
			//TODO:解码暂时设备不支持
    	}
    	
    	if(isEncode){
    		//协商端口
    		CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(bundleId);
    		if(permission == null){
    			//协商端口
    			permission = new CapacityPermissionPortPO();
    			
    			String srtIp = capacityProps.getSrtIp();
        		String transcodeIp = capacityProps.getTranscodeIp();
        		
        		List<Long> ports = new ArrayList<Long>();
        		List<CapacityPermissionPortPO> capacityPermissionPorts = new ArrayList<CapacityPermissionPortPO>();
        		for(CapacityPermissionPortPO capacityPermissionPort: capacityPermissionPorts){
        			ports.add(capacityPermissionPort.getSrtPort());
        		}
        		
        		Long newport = terminalMappingService.generatePort(ports);
        		
        		permission.setBundleId(bundleId);
        		permission.setBundleIp(bundlePO.getDeviceIp());
        		permission.setSrtIp(srtIp);
        		permission.setSrtPort(newport);
        		
        		capacityPermissionPortDao.save(permission);
    		}
    		
    		//GBE设置参数
    		JSONObject gbeNet0PortCfg = new JSONObject();
    		JSONArray portCfg = new JSONArray();
    		JSONObject port = new JSONObject();
    		port.put("portIdx", 1);
    		port.put("dstIP", permission.getSrtIp());
    		port.put("dstUDP", permission.getSrtPort());
    		port.put("switch", 1);
    		portCfg.add(port);
    		gbeNet0PortCfg.put("gbeNet0PortCfg", gbeNet0PortCfg);
    		
    		HttpAsyncClient.getInstance().httpAsyncPost("http://" + bundlePO.getDeviceIp() + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.gbePortCfg(gbeNet0PortCfg).toJSONString(), null, null);
	    	
    	}
    	
    }
    
    /**处理open_bundle信息的player类型信息
     * @throws Exception */
    private void processOpenBundlePlayerMsg(BundleBO bundle) throws Exception{
        
    	String bundleId = bundle.getBundle_id();
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	if(passbyBO.getType().equals("udp_decode")){
    		
    		String playerBundleId = passbyBO.getBundle_id();
    		PlayerBO player = JSON.parseObject(passbyBO.getPass_by_content().toJSONString(), PlayerBO.class);
    		String udpUrl = player.getUdpUrl();
    		SourceBO source = player.getSource();
    		if(source != null){
    			String srcBundleId = source.getBundle_id();
    			String srcLayerId = source.getLayer_id();
    			//进行一个简单的接入id校验
    			if(srcLayerId.equals(RegisterStatus.getNodeId())){
    				
    				List<String> bundleIds = new ArrayList<String>();
    		    	bundleIds.add(srcBundleId);
    		    	
    		    	List<BundlePO> bundles = resourceBundleDao.findByBundleIds(bundleIds);
    		    	if(bundles == null || bundles.size() <= 0){
    		    		throw new BaseException(StatusCode.ERROR, "bundleId为：" + bundleId + "的设备不存在！");
    		    	}
    		    	
    		    	BundlePO bundlePO = bundles.get(0);
    				
    				//协商端口
    	    		CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(srcBundleId);
    	    		if(permission == null){
    	    			//协商端口
    	    			permission = new CapacityPermissionPortPO();
    	    			
    	    			String srtIp = capacityProps.getSrtIp();
    	        		String transcodeIp = capacityProps.getTranscodeIp();
    	        		
    	        		List<Long> ports = new ArrayList<Long>();
    	        		List<CapacityPermissionPortPO> capacityPermissionPorts = new ArrayList<CapacityPermissionPortPO>();
    	        		for(CapacityPermissionPortPO capacityPermissionPort: capacityPermissionPorts){
    	        			ports.add(capacityPermissionPort.getSrtPort());
    	        		}
    	        		
    	        		Long newport = terminalMappingService.generatePort(ports);
    	        		
    	        		permission.setBundleId(bundleId);
    	        		permission.setBundleIp(bundlePO.getDeviceIp());
    	        		permission.setSrtIp(srtIp);
    	        		permission.setSrtPort(newport);
    	        		
    	        		capacityPermissionPortDao.save(permission);
    	    		}
    	    		
    	    		TranscodeVO transcode = new TranscodeVO().setIp(permission.getSrtIp())
    	    												 .setPort(permission.getSrtPort())
    	    												 .setUdp(udpUrl)
    	    												 .setBundleId(bundleId)
    	    												 .setVideo(player.getVideo_param())
    	    												 .setAudio(player.getAudio_param());
    	    		
    	    		String timeTamp = String.valueOf(new Date().getTime());
    				String sign = sign(capacityProps.getAppId(), timeTamp, capacityProps.getAppSecret());
    	    		
    	    		String url = new StringBufferWrapper().append("http://")
    	    											  .append(capacityProps.getTranscodeIp())
    	    											  .append(":")
    	    											  .append(capacityProps.getTranscodePort())
    	    											  .append(TerminalParam.CAPACITY_URL_SUFFIX)
    	    											  .append("/add?appId=")
    	    											  .append(capacityProps.getAppId())
    	    											  .append("&timestamp=")
    	    											  .append(timeTamp)
    	    											  .append("&sign=")
    	    											  .append(sign)
    	    											  .toString();
    	    		JSONObject post = new JSONObject();
    	    		post.put("bvcInfo", transcode);
    	    		
    	    		//媒资平台请求
					HttpClient.encodepost(url, post);
    	    		
    			}
    		}
    	}
    }
    
    /**处理close_bundle信息的player类型信息
     * @throws Exception */
    private void processCloseBundlePlayerMsg(BundleBO bundle) throws Exception{
        
    	String bundleId = bundle.getBundle_id();
    	
    	String timeTamp = String.valueOf(new Date().getTime());
		String sign = sign(capacityProps.getAppId(), timeTamp, capacityProps.getAppSecret());
    	
    	String url = new StringBufferWrapper().append("http://")
											  .append(capacityProps.getTranscodeIp())
											  .append(":")
											  .append(capacityProps.getTranscodePort())
											  .append(TerminalParam.CAPACITY_URL_SUFFIX)
											  .append("/delete?appId=")
											  .append(capacityProps.getAppId())
											  .append("&timestamp=")
											  .append(timeTamp)
											  .append("&sign=")
											  .append(sign)
											  .toString();

    	JSONObject post = new JSONObject();
    	post.put("id", bundleId);
    	
		//媒资平台请求
		HttpClient.encodepost(url, post);
    	
    }
    
    /**处理open_bundle信息的jv210类型信息
     * @throws Exception */
    private void processOpenBundleJv210Msg(BundleBO bundle, TerminalBindRepeaterPO bind) throws Exception{
    	
    	String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	String[][] TerminalDecodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalDecodeParam), String[][].class);
    	String[][] TerminalCallingParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalCallingParam), String[][].class);
    	    	
    	String bundleId = bundle.getBundle_id();
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	//通话设置请求
    	String callingRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_CALLSETTING_SUFFIX);
    	String[][] callingParam = TerminalParam.html2Data(callingRes, TerminalCallingParam);
    	
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//解码设置请求
    	String decodeRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	String[][] decodeParam = TerminalParam.html2Data(decodeRes, TerminalDecodeParam);
    	
    	//参数定义（video）
    	Long main_bitrate = 4000000l;
    	String main_resoluton = "1920x1080";
    	Long main_video_encode_port = 10000l;
    	String main_video_encode_address = "";
    	Long main_video_decode_port = 10000l;
    	Long sub_bitrate = 4000000l;
    	String sub_resoluton = "1920x1080";
    	Long sub_video_encode_port = 10000l;
    	String sub_video_encode_address = "";
    	Long sub_video_decode_port = 10000l;
    	//参数定义（audio）
    	String audio_codec = "aac";
    	Long sample_rate = 48000l;
    	String gain = "0";
    	Long audio_encode_port = 10000l;
    	String audio_encode_address = "";
    	Long audio_decode_port = 10000l;
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	//标识（是否"呼叫"--设置参数）
    	boolean paramFlag = false;
    	
    	//标识（设置大小屏）
    	boolean screenFlag = false;
    	
    	//显示模式：单画面--"0",二等分--"1",画中画--"2"
    	String screenLayout = "0";
    	//画面：远端1--"0",远端2--"1",本地1--"2",本地2--"3"
    	String picture1 = "0";
    	String picture2 = "0";
    	
    	Set<String> srcBundleIds = new HashSet<String>();
    	Set<String> dstBundleIds = new HashSet<String>();
    	srcBundleIds.add(bundleId);
    	dstBundleIds.add(bundleId);
    	//取bundleId和channelId用于查询
    	for(ChannelBO channel:channels){
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				srcBundleIds.add(srcBundleId);
			}
    	}
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(srcBundleIds, dstBundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO portMapping: mappings){
    		mappingIds.add(portMapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
    	
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				String srcChannelId = source.getChannel_id();
				String srcLayerId = source.getLayer_id();
				
				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
				
				if(mappingPO == null) throw new Exception(srcBundleId + "源终端未绑定转发器！");
				
				//layerId不相同，向其接入发pulldata
				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){

					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
							
				}
				
				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				if(taskPOs != null && taskPOs.size() > 0){
					//任务切换
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
					}
				}
				
			}
			
			//校验是否被已经"呼叫"：
			if(encodeParam[0][0].equals("1") && encodeParam[1][0].equals("1") && encodeParam[4][0].equals("1") && decodeParam[0][0].equals("1")
					&& decodeParam[1][0].equals("1") && decodeParam[2][0].equals("1") && decodeParam[3][0].equals("1")){
				
				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
				
				if(source == null){
					List<TaskPO> taskPOs = null;
					if(mappingDst != null){
						taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
					}
					
					if(taskPOs != null){
						//切换为无效--(需要切换--占用带宽)
						for(TaskPO task: taskPOs){
							taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
						}
					}
					
					paramFlag = false; 					
					screenFlag = true;
		    		screenLayout = "0";
		    		picture1 = "2";
				}
					
			}else{
				if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){
	    			main_bitrate = baseParam.getBitrate();
	    			main_resoluton = baseParam.getResolution();
	    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
	    			main_video_encode_address = mappingPO.getDstAddress();
	    	    	main_video_encode_port = mappingPO.getDstPort();
	    		}else if(channelId.equals(ChannelType.VIDEOENCODE2.getType())){
	    			sub_bitrate = baseParam.getBitrate();
	    			sub_resoluton = baseParam.getResolution();
	    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
	    			sub_video_encode_address = mappingPO.getDstAddress();
	    	    	sub_video_encode_port = mappingPO.getDstPort(); 
	    		}else if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
	    			audio_codec = baseParam.getCodec();
	    			sample_rate = baseParam.getSample_rate();
	    			gain = baseParam.getGain();
	    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
	    			audio_encode_address = mappingPO.getDstAddress();
	    	    	audio_encode_port = mappingPO.getDstPort();  
	    		}else if(channelId.equals(ChannelType.VIDEODECODE1.getType())){
	    			PortMappingPO mappingPO = queryUtilService.queryDecodePortMappingByChannel(mappings, bundleId, channelId);
	    	    	main_video_decode_port = mappingPO.getDstPort();
	    		}else if(channelId.equals(ChannelType.VIDEODECODE2.getType())){
	    			PortMappingPO mappingPO = queryUtilService.queryDecodePortMappingByChannel(mappings, bundleId, channelId);
	    	    	sub_video_decode_port = mappingPO.getDstPort(); 
	    		}else if(channelId.equals(ChannelType.AUDIODECODE1.getType())){
	    			PortMappingPO mappingPO = queryUtilService.queryDecodePortMappingByChannel(mappings, bundleId, channelId);
	    	    	audio_decode_port = mappingPO.getDstPort();  
	    		}
				
				paramFlag = true;
			}
		}  
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			boolean isSingle = true; 
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						picture1 = Jv210Param.fromName(rect.getChannel()).getValue();
    					}else{
    						picture2 = Jv210Param.fromName(rect.getChannel()).getValue();
    						if(rect.getType().equals("single")){
    							isSingle = false;
    						}
    					}
    				}
    			}
    			if(isSingle){
    				screenLayout = "0";
    			}else{
    				screenLayout = "2";
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(paramFlag){
    		
    		screenFlag = true;
    		screenLayout = "0";
    		picture1 = "2";
        	
        	Long trasferMainBitrate = main_bitrate/1000l;
        	Long mainVideoBitrate = (long) (trasferMainBitrate * (7.0f/10.0f) > 4000l?4000l: trasferMainBitrate * (7.0f/10.0f));
        	Long mainAllBitrate = (long) (mainVideoBitrate * (28.0f/10.0f) > 10000l?10000l: mainVideoBitrate * (28.0f/10.0f));
        	String mainWidth = main_resoluton.split("x")[0];
        	String mainHeight = main_resoluton.split("x")[1];
        	
        	Long trasferSubBitrate = sub_bitrate/1000l;
        	Long subVideoBitrate = (long) (trasferSubBitrate * (7.0f/10.0f) > 4000l?4000l: trasferSubBitrate * (7.0f/10.0f));
        	Long subAllBitrate = (long) (subVideoBitrate * (28.0f/10.0f) > 10000l?10000l: subVideoBitrate * (28.0f/10.0f));
        	String subWidth = sub_resoluton.split("x")[0];
        	String subHeight = sub_resoluton.split("x")[1];
        	
        	//设置jv210编码
        	encodeParam[0][0] = "1";
        	encodeParam[0][1] = main_video_encode_address;
        	encodeParam[0][2] = main_video_encode_port.toString();
        	encodeParam[0][3] = audio_encode_address;
        	encodeParam[0][4] = audio_encode_port.toString();
        	
        	encodeParam[1][0] = "1";
        	encodeParam[1][1] = sub_video_encode_address;
        	encodeParam[1][2] = sub_video_encode_port.toString();

        	encodeParam[2][0] = mainAllBitrate.toString();
    		encodeParam[2][1] = mainVideoBitrate.toString();
    		encodeParam[2][2] = mainWidth;
    		encodeParam[2][3] = mainHeight;
    		
    		encodeParam[3][0] = subAllBitrate.toString();
    		encodeParam[3][1] = subVideoBitrate.toString();
    		encodeParam[3][2] = subWidth;
    		encodeParam[3][3] = subHeight;
    		
        	encodeParam[4][0] = "1";
    		encodeParam[4][1] = Jv210Param.fromName(audio_codec).getValue();
    		encodeParam[4][4] = Jv210Param.fromName(audio_codec).getRelation();
    		
    		//设置jv210解码
    		decodeParam[0][0] = "1";
    		decodeParam[0][4] = main_video_decode_port.toString();
    		decodeParam[0][5] = audio_decode_port.toString();
    		
    		decodeParam[1][0] = "1";
    		decodeParam[1][4] = sub_video_decode_port.toString();
    		
    		decodeParam[2][0] = "1";
    		
    		decodeParam[3][0] = "1";
    		decodeParam[3][3] = gain;
    		
    		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
    		encodeBody.add(encodePair);
    		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
        	
    		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair decodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(decodeParam));
    		decodeBody.add(decodePair);
    		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
    	  		
    	}
    	
    	if(passbyBO != null){
        	String type = passbyBO.getType();
        	if(type.equals("bind_repeater_node")){
        		screenFlag = false;
        	}
    	}
    	
    	if(screenFlag){
    		
    		callingParam[1][0] = screenLayout;
			callingParam[1][3] = picture1;
			callingParam[1][5] = picture2;
			
			callingParam[5][0] = decodeParam[0][5];
			callingParam[5][1] = decodeParam[0][4];
			callingParam[5][2] = decodeParam[1][4];
    		
    		List<BasicNameValuePair> callingBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair callingPair = new BasicNameValuePair("setString", TerminalParam.array2Data(callingParam));
    		callingBody.add(callingPair);
    		System.out.println("画面：" + screenLayout);
    		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_CALLSETTING_SUFFIX, null, callingBody, null);
    	}
    }
    
    /**处理close_bundle信息的VenusTerminal类型信息
     * @throws Exception */
    private void processCloseBundleVenusTerminalMsg(BundleBO bundle, String layerId) throws Exception{
    	
    	String bundleId = bundle.getBundle_id();
    	
		if(layerId.equals(NodeType.NETWORK.getId())){
    		
    		networkService.processOpenBundleVenusTerminalMsg(bundle);
    		
    	}else{
        	
        	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
        	
        	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
        	
        	if(bind.getDeviceModel().equals("jv210")){
        		
        		processCloseBundleJv210Msg(bundle, bind);
        		
        	}else if(bind.getDeviceModel().equals("jv220")){
        		
        		processCloseBundleJv220Msg(bundle, bind);
        		
        	}
    	}
    }
    
    /**处理close_bundle信息的jv210类型信息
     * @throws Exception */
    private void processCloseBundleJv210Msg(BundleBO bundle, TerminalBindRepeaterPO bind) throws Exception{
    	
    	String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	String[][] TerminalDecodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalDecodeParam), String[][].class);
    	
    	String bundleId = bundle.getBundle_id();
    	
    	List<PortMappingPO> mappings = portMappingDao.findByDstTypeAndDstBundleId(DstType.TERMINAL, bundleId);
    	
    	List<Long> ids = new ArrayList<Long>();
    	for(PortMappingPO mapping: mappings){
    		ids.add(mapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(ids, TaskStatus.zero.getStatus());
    	
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//通话设置请求
    	String decodeRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	String[][] decodeParam = TerminalParam.html2Data(decodeRes, TerminalDecodeParam);
    	
    	encodeParam[0][0] = "0";
    	encodeParam[1][0] = "0";
    	encodeParam[4][0] = "0";
    	decodeParam[0][0] = "0";
    	decodeParam[1][0] = "0";
    	decodeParam[2][0] = "0";
    	decodeParam[3][0] = "0";
    	
		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
		encodeBody.add(encodePair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
    	
		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair decodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(decodeParam));
		decodeBody.add(decodePair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
		
		for(TaskPO task: tasks){
			taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
		}
    }
    
    /**处理close_bundle信息的5G类型信息
     * @throws Exception */
    private void processCloseBundle5GMsg(BundleBO bundle) throws Exception{
    
    	String bundleId = bundle.getBundle_id();
    	
    	CapacityPermissionPortPO permission = capacityPermissionPortDao.findByBundleId(bundleId);
    	
    	if(permission != null){
    		String deviceIp = permission.getBundleIp();
    		
    		//GBE设置参数
    		JSONObject gbeNet0PortCfg = new JSONObject();
    		JSONArray portCfg = new JSONArray();
    		JSONObject port = new JSONObject();
    		port.put("portIdx", 1);
    		port.put("switch", 0);
    		portCfg.add(port);
    		gbeNet0PortCfg.put("gbeNet0PortCfg", gbeNet0PortCfg);
    		
    		HttpAsyncClient.getInstance().httpAsyncPost("http://" + deviceIp + TerminalParam.FIVEG_URL_SUFFIX, oneOneFiveMTerminal.gbePortCfg(gbeNet0PortCfg).toJSONString(), null, null);
	    	
    		//TODO:解码参数--设备暂时不支持
    		
    		capacityPermissionPortDao.delete(permission);
    	}
    	
    }
    
    /**处理close_bundle信息的jv210类型信息
     * @throws Exception */
    private void processCloseBundleJv220Msg(BundleBO bundle, TerminalBindRepeaterPO bind) throws Exception{
    		
    	String bundleId = bundle.getBundle_id();
    	
    	List<PortMappingPO> mappings = portMappingDao.findByDstTypeAndDstBundleId(DstType.TERMINAL, bundleId);
    	
    	List<Long> ids = new ArrayList<Long>();
    	for(PortMappingPO mapping: mappings){
    		ids.add(mapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(ids, TaskStatus.zero.getStatus());
    	
    	//关闭解码开关
    	for(Long i = 0l;i < 2l;i++){
        	JSONObject decodeParam = new JSONObject();
        	JSONObject ctrl = new JSONObject();
        	ctrl.put("ctrl", 0l);
        	decodeParam.put("udp_recv", ctrl);
        	
        	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(i, decodeParam).toJSONString(), null, null);

    	}
    	
    	//关闭编码开关
    	JSONObject mutimediaParam = new JSONObject();
    	JSONObject v_send_0 = new JSONObject();
    	v_send_0.put("ctrl", 0l);
    	mutimediaParam.put("v_send_0", v_send_0);
    	
    	JSONObject v_send_1 = new JSONObject();
    	v_send_1.put("ctrl", 0l);
    	mutimediaParam.put("v_send_1", v_send_1);
    	
    	JSONObject a_send = new JSONObject();
    	a_send.put("ctrl", 0l);
    	mutimediaParam.put("a_send", a_send);
    	
    	//画面设为单画面本地
    	JSONObject v_disp_0 = new JSONObject();
		v_disp_0.put("mode", 0l);
		v_disp_0.put("win_0_ch", Jv220Param.VIDEOENCODE1.getProtocal());
		mutimediaParam.put("v_disp_0", v_disp_0);
    	
		System.out.println(mutimediaParam.toJSONString());
    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.multimediaSet(mutimediaParam).toJSONString(), null, null);

    	//任务切换
		for(TaskPO task: tasks){
			taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
		}
    }
    
    /**处理open_bundle信息的VenusVirtual类型信息
     * @throws Exception */
    private void processOpenBundleVenusVirtualMsg(BundleBO bundle) throws Exception{
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	String type = passbyBO.getType();
    	switch (type) {
		case "bind_repeater_node":
			processPassByBindNodeMsg(bundle);
			break;
		case "unbind_repeater_node":
			processPassByUnbindNodeMsg(bundle);
			break;
		case "switch_repeater_node":
			processPassBySwitchNodeMsg(bundle);
			break;
		default:
			break;
		}   	
    }
    
    /**处理close_bundle信息的VenusTerminal类型信息
     * @throws Exception */
    private void processCloseBundleVenusVirtualMsg(BundleBO bundle) throws Exception{
    	
    	String bundleId = bundle.getBundle_id();
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcTypeAndSrcBundleId(SrcType.ROLE, bundleId);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO mapping: mappings){
    		mappingIds.add(mapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(mappingIds, TaskStatus.zero.getStatus());
    	
		for(TaskPO task: tasks){
			for(PortMappingPO mapping: mappings){
				if(mapping.getId().equals(task.getMappingId())){
			    	taskExecuteService.taskSwitch(task, mapping.getDstAddress(), 60000l);
				}
			}
		}
    }
    
    /**处理open_bundle信息的VenusProxy类型信息
     * @throws Exception */
    private void processOpenBundleVenusProxyMsg(BundleBO bundle) throws Exception{
    	
    	String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	    	
    	String bundleId = bundle.getBundle_id();
    	
    	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
    	
    	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	Set<String> srcBundleIds = new HashSet<String>();
    	Set<String> dstBundleIds = new HashSet<String>();
    	srcBundleIds.add(bundleId);
    	dstBundleIds.add(bundleId);
    	//取bundleId和channelId用于查询
    	for(ChannelBO channel:channels){
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				srcBundleIds.add(srcBundleId);
			}
    	}
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(srcBundleIds, dstBundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO portMapping: mappings){
    		mappingIds.add(portMapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);

    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				String srcChannelId = source.getChannel_id();
				String srcLayerId = source.getLayer_id();
				
				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
				
				//合屏源pullData
				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){
					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
				}
				
				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				if(taskPOs != null && taskPOs.size() > 0){
					//任务切换
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
					}
				}
				
			}else{	
				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
				
				List<TaskPO> taskPOs = null;
				if(mappingDst != null){
					taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				}
				
				if(taskPOs != null){
					//切换为无效
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
					}
				}
			}
		}  
    	
    	//通话设置请求
    	String callingRes = HttpClient.get("http://" + bind.getBundleIp() + ":8981" + TerminalParam.GET_JV210_CALLSETTING_SUFFIX);
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + bind.getBundleIp() + ":8981" + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//解码设置请求
    	String decodeRes = HttpClient.get("http://" + bind.getBundleIp() + ":8981" + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	
  		List<BasicNameValuePair> callingBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair callingPair = new BasicNameValuePair("setString", callingRes);
		callingBody.add(callingPair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + ":8981" + TerminalParam.POST_JV210_CALLSETTING_SUFFIX, null, callingBody, null);
		
		PortMappingPO mappingChannel = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, "VenusVideoIn_1");
		String main_video_encode_address = mappingChannel.getDstAddress();
		Long main_video_encode_port = mappingChannel.getDstPort();
    	encodeParam[0][1] = main_video_encode_address;
    	encodeParam[0][2] = main_video_encode_port.toString();
		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
		encodeBody.add(encodePair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + ":8981" + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
		
		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair decodePair = new BasicNameValuePair("setString", decodeRes);
		decodeBody.add(decodePair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + ":8981" + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
		
    }
    
    /**处理close_bundle信息的VenusProxy类型信息
     * @throws Exception */
    private void processCloseBundleVenusProxyMsg(BundleBO bundle) throws Exception{
    	
    	String bundleId = bundle.getBundle_id();
    	
    	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
    	
    	List<PortMappingPO> mappings = portMappingDao.findByDstTypeAndDstBundleId(DstType.TERMINAL, bundleId);
    	
    	List<Long> ids = new ArrayList<Long>();
    	for(PortMappingPO mapping: mappings){
    		ids.add(mapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdInAndStatus(ids, TaskStatus.zero.getStatus());
    	
		for(TaskPO task: tasks){
			taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
		}
    }
    
    /**处理pullData线程
     * @throws Exception */
    public void processPullDataRunable(String dstId, String srcId, String srcBundleId, String srcChannelId,
			String dstBundleId, String dstChannelId, String ip, String port) throws Exception{
    	
    	Thread pullDataThread = new Thread(new Runnable() {
    		int i = 0; 
			@Override
			public void run() {
				try{
					mqSendService.pullDataRequestMessage(dstId, srcId, srcBundleId, srcChannelId, dstBundleId, dstChannelId, ip, port);
					if(i > 20){
						MqExecutorService.getInstance().terminal(srcBundleId + "-" +srcChannelId);
					}
					i++;
				}catch(Exception e){
					System.err.println("pullData异常！");
				}
			}
		});
		MqExecutorService.getInstance().sheduler(pullDataThread, srcBundleId + "-" +srcChannelId);
    }
    
    /**处理pullDataResponse线程
     * @throws Exception */
    private void pullDataResponseRunable(PortMappingPO mapping, String dstId, String srcId, String srcBundleId, String srcChannelId,
    		String dstBundleId, String dstChannelId, String dstIp, String dstPort,  String seqString) throws Exception{
    	
    	Thread pullDataResponse = new Thread(new Runnable() {
    		int i = 0;
    		@Override
    		public void run() {
    			try {
    				System.out.println("mappingId为：" + mapping.getId() + "检测outip。。。");
					List<TaskPO> tasks = taskDao.findByMappingIdAndStatus(mapping.getId(), TaskStatus.zero.getStatus());
					if(tasks != null && tasks.size() > 0){
						TaskPO task = tasks.get(0);
						if(task.getOutIp() != null){
							mqSendService.pullDataResponseMessage(dstId, srcId, srcBundleId, srcChannelId, dstBundleId, dstChannelId, dstIp, dstPort, "0.0.0.0:" + "60000", task.getOutIp() + ":" + "60000", seqString);
							MqExecutorService.getInstance().terminal(srcBundleId + "-" +srcChannelId);
						}
					}
					if(i > 20){
						MqExecutorService.getInstance().terminal(srcBundleId + "-" +srcChannelId);
					}
					i++;
					System.out.println(i);
				} catch (Exception e) {
					System.err.println("pullData response 异常！");
				}
    		}
    	});
    	
    	MqExecutorService.getInstance().sheduler(pullDataResponse, srcBundleId + "-" +srcChannelId);
    }
    
    /**处理收到的响应类消息*/
    private void processRespMsg(String textMessage) {

    } 
    
    /**处理收到的响应pullData类消息*/
    private void processResPullDataMsg(ResponseBO responseBo){
    	
    	JSONObject messageBody = responseBo.getMessage().getMessage_body();
    	System.out.println("+++++pulldata response+++++" + responseBo.getMessage().getMessage_body().toJSONString());
    	String result = messageBody.getString("result");
    	String localBundleId = messageBody.getString("localBundleId");
    	String localChannelID = messageBody.getString("localChannelID");
    	if(result.equals("1")){
    		String futureName = localBundleId + "-" + localChannelID;
    		MqExecutorService.getInstance().terminal(futureName);
    	}
    }
    
    /**
     * proxy设备恢复流程<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年8月9日 上午9:01:06
     */
    public void bundleProxyResume(BundleBO bundle) throws Exception{
    	
    	String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	String[][] TerminalDecodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalDecodeParam), String[][].class);
    	String[][] TerminalCallingParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalCallingParam), String[][].class);
    	    	
    	String bundleId = bundle.getBundle_id();
    	
    	String passby = bundle.getPass_by_str();
    	
    	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
    	
    	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
    	
    	List<PortMappingPO> mappings = portMappingDao.findAll();
    	
    	List<TaskPO> tasks = taskDao.findAll();
    	
    	//通话设置请求
    	String callingRes = HttpClient.get("http://" + bind.getBundleIp() + ":8981" + TerminalParam.GET_JV210_CALLSETTING_SUFFIX);
    	String[][] callingParam = TerminalParam.html2Data(callingRes, TerminalCallingParam);
    	
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + bind.getBundleIp() + ":8981" + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//解码设置请求
    	String decodeRes = HttpClient.get("http://" + bind.getBundleIp() + ":8981" + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	String[][] decodeParam = TerminalParam.html2Data(decodeRes, TerminalDecodeParam);
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
    		if(channel.getChannel_param() != null){
    			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
    			SourceBO source = baseParam.getSource();
    			
    			if(source != null){
    					
    				String srcBundleId = source.getBundle_id();
    				String srcChannelId = source.getChannel_id();
    				String srcLayerId = source.getLayer_id();
    				
    				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
    				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
    				
    				//layerId不相同，向其接入发pulldata
    				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){

    					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
    							
    				}
    				
    				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
    				if(taskPOs != null && taskPOs.size() > 0){
    					//任务切换
    					for(TaskPO task: taskPOs){
    						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
    					}
    				}
    			}
    		}
		}  
    	
    	PortMappingPO mappingChannel = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, "VenusVideoIn_1");
    	if(mappingChannel == null) throw new Exception("没找到协商端口,bundleId: " + bundle.getBundle_id());
		String main_video_encode_address = mappingChannel.getDstAddress();
		Long main_video_encode_port = mappingChannel.getDstPort();
    	encodeParam[0][1] = main_video_encode_address;
    	encodeParam[0][2] = main_video_encode_port.toString();
		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
		encodeBody.add(encodePair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
    	
		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair decodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(decodeParam));
		decodeBody.add(decodePair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
    		
		List<BasicNameValuePair> callingBody = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair callingPair = new BasicNameValuePair("setString", TerminalParam.array2Data(callingParam));
		callingBody.add(callingPair);
		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_CALLSETTING_SUFFIX, null, callingBody, null);
    }
    
    /**
     * jv210设备恢复流程<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年8月9日 上午9:01:06
     */
    public void bundleJv210Resume(BundleBO bundle) throws Exception{
    	
    	String[][] TerminalEncodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalEncodeParam), String[][].class);
    	String[][] TerminalDecodeParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalDecodeParam), String[][].class);
    	String[][] TerminalCallingParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalCallingParam), String[][].class);
    	    	
    	String bundleId = bundle.getBundle_id();
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
    	
    	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
    	
    	//通话设置请求
    	String callingRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_CALLSETTING_SUFFIX);
    	String[][] callingParam = TerminalParam.html2Data(callingRes, TerminalCallingParam);
    	
    	//编码设置请求
    	String encodeRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_ENCODEPARAM_SUFFIX);
    	String[][] encodeParam = TerminalParam.html2Data(encodeRes, TerminalEncodeParam);
    	
    	//解码设置请求
    	String decodeRes = HttpClient.get("http://" + bind.getBundleIp() + TerminalParam.GET_JV210_DECODEPARAM_SUFFIX);
    	String[][] decodeParam = TerminalParam.html2Data(decodeRes, TerminalDecodeParam);
    	
    	//参数定义（video）
    	Long main_bitrate = 4000000l;
    	String main_resoluton = "1920x1080";
    	Long main_video_encode_port = 10000l;
    	String main_video_encode_address = "";
    	Long main_video_decode_port = 10000l;
    	Long sub_bitrate = 4000000l;
    	String sub_resoluton = "1920x1080";
    	Long sub_video_encode_port = 10000l;
    	String sub_video_encode_address = "";
    	Long sub_video_decode_port = 10000l;
    	//参数定义（audio）
    	String audio_codec = "aac";
    	Long sample_rate = 48000l;
    	String gain = "0";
    	Long audio_encode_port = 10000l;
    	String audio_encode_address = "";
    	Long audio_decode_port = 10000l;
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	//标识（是否"呼叫"--设置参数）
    	boolean paramFlag = false;
    	
    	//标识（设置大小屏）
    	boolean screenFlag = false;
    	
    	//显示模式：单画面--"0",二等分--"1",画中画--"2"
    	String screenLayout = "0";
    	//画面：远端1--"0",远端2--"1",本地1--"2",本地2--"3"
    	String picture1 = "0";
    	String picture2 = "0";
    	
    	Set<String> srcBundleIds = new HashSet<String>();
    	Set<String> dstBundleIds = new HashSet<String>();
    	srcBundleIds.add(bundleId);
    	dstBundleIds.add(bundleId);
    	//取bundleId和channelId用于查询
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
    		if(channel.getChannel_param() != null){
				BaseParamBO baseParam = channel.getChannel_param().getBase_param();
				SourceBO source = baseParam.getSource();
				
				if(source != null){
						
					String srcBundleId = source.getBundle_id();
					srcBundleIds.add(srcBundleId);
				}
    		}
    	}
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(srcBundleIds, dstBundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO portMapping: mappings){
    		mappingIds.add(portMapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
    	
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
    		if(channel.getChannel_param() != null){
    			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
    			SourceBO source = baseParam.getSource();
    			
    			if(source != null){
    					
    				String srcBundleId = source.getBundle_id();
    				String srcChannelId = source.getChannel_id();
    				String srcLayerId = source.getLayer_id();
    				
    				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
    				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
    				
    				//layerId不相同，向其接入发pulldata
    				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){

    					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
    							
    				}
    				
    				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
    				if(taskPOs != null && taskPOs.size() > 0){
    					//任务切换
    					for(TaskPO task: taskPOs){
    						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
    					}
    				}
    			}
    			
    			if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){
        			main_bitrate = baseParam.getBitrate();
        			main_resoluton = baseParam.getResolution();
        			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
        			main_video_encode_address = mappingPO.getDstAddress();
        	    	main_video_encode_port = mappingPO.getDstPort();
        		}else if(channelId.equals(ChannelType.VIDEOENCODE2.getType())){
        			sub_bitrate = baseParam.getBitrate();
        			sub_resoluton = baseParam.getResolution();
        			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
        			sub_video_encode_address = mappingPO.getDstAddress();
        	    	sub_video_encode_port = mappingPO.getDstPort(); 
        		}else if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
        			audio_codec = baseParam.getCodec();
        			sample_rate = baseParam.getSample_rate();
        			gain = baseParam.getGain();
        			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
        			audio_encode_address = mappingPO.getDstAddress();
        	    	audio_encode_port = mappingPO.getDstPort();  
        		}else if(channelId.equals(ChannelType.VIDEODECODE1.getType())){
        			PortMappingPO mappingPO = queryUtilService.queryDecodePortMappingByChannel(mappings, bundleId, channelId);
        	    	main_video_decode_port = mappingPO.getDstPort();
        		}else if(channelId.equals(ChannelType.VIDEODECODE2.getType())){
        			PortMappingPO mappingPO = queryUtilService.queryDecodePortMappingByChannel(mappings, bundleId, channelId);
        	    	sub_video_decode_port = mappingPO.getDstPort(); 
        		}else if(channelId.equals(ChannelType.AUDIODECODE1.getType())){
        			PortMappingPO mappingPO = queryUtilService.queryDecodePortMappingByChannel(mappings, bundleId, channelId);
        	    	audio_decode_port = mappingPO.getDstPort();  
        		}
    			
    			paramFlag = true;
    			
    		}

		}  
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			boolean isSingle = true; 
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						picture1 = Jv210Param.fromName(rect.getChannel()).getValue();
    					}else{
    						picture2 = Jv210Param.fromName(rect.getChannel()).getValue();
    						if(rect.getType().equals("single")){
    							isSingle = false;
    						}
    					}
    				}
    			}
    			if(isSingle){
    				screenLayout = "0";
    			}else{
    				screenLayout = "2";
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(paramFlag){
        	
        	Long trasferMainBitrate = main_bitrate/1000l;
        	Long mainVideoBitrate = (long) (trasferMainBitrate * (7.0f/10.0f) > 4000l?4000l: trasferMainBitrate * (7.0f/10.0f));
        	Long mainAllBitrate = (long) (mainVideoBitrate * (28.0f/10.0f) > 10000l?10000l: mainVideoBitrate * (28.0f/10.0f));
        	String mainWidth = main_resoluton.split("x")[0];
        	String mainHeight = main_resoluton.split("x")[1];
        	
        	Long trasferSubBitrate = sub_bitrate/1000l;
        	Long subVideoBitrate = (long) (trasferSubBitrate * (7.0f/10.0f) > 4000l?4000l: trasferSubBitrate * (7.0f/10.0f));
        	Long subAllBitrate = (long) (subVideoBitrate * (28.0f/10.0f) > 10000l?10000l: subVideoBitrate * (28.0f/10.0f));
        	String subWidth = sub_resoluton.split("x")[0];
        	String subHeight = sub_resoluton.split("x")[1];
        	
        	//设置jv210编码
        	encodeParam[0][0] = "1";
        	encodeParam[0][1] = main_video_encode_address;
        	encodeParam[0][2] = main_video_encode_port.toString();
        	encodeParam[0][3] = audio_encode_address;
        	encodeParam[0][4] = audio_encode_port.toString();
        	
        	encodeParam[1][0] = "1";
        	encodeParam[1][1] = sub_video_encode_address;
        	encodeParam[1][2] = sub_video_encode_port.toString();

        	encodeParam[2][0] = mainAllBitrate.toString();
    		encodeParam[2][1] = mainVideoBitrate.toString();
    		encodeParam[2][2] = mainWidth;
    		encodeParam[2][3] = mainHeight;
    		
    		encodeParam[3][0] = subAllBitrate.toString();
    		encodeParam[3][1] = subVideoBitrate.toString();
    		encodeParam[3][2] = subWidth;
    		encodeParam[3][3] = subHeight;
    		
        	encodeParam[4][0] = "1";
    		encodeParam[4][1] = Jv210Param.fromName(audio_codec).getValue();
    		encodeParam[4][4] = Jv210Param.fromName(audio_codec).getRelation();
    		
    		//设置jv210解码
    		decodeParam[0][0] = "1";
    		decodeParam[0][4] = main_video_decode_port.toString();
    		decodeParam[0][5] = audio_decode_port.toString();
    		
    		decodeParam[1][0] = "1";
    		decodeParam[1][4] = sub_video_decode_port.toString();
    		
    		decodeParam[2][0] = "1";
    		
    		decodeParam[3][0] = "1";
    		decodeParam[3][3] = gain;
    		
    		List<BasicNameValuePair> encodeBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair encodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(encodeParam));
    		encodeBody.add(encodePair);
    		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_ENCODEPARAM_SUFFIX, null, encodeBody, null);
        	
    		List<BasicNameValuePair> decodeBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair decodePair = new BasicNameValuePair("setString", TerminalParam.array2Data(decodeParam));
    		decodeBody.add(decodePair);
    		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_DECODEPARAM_SUFFIX, null, decodeBody, null);
    	  		
    	}
    	
    	if(passbyBO != null){
        	String type = passbyBO.getType();
        	if(type.equals("bind_repeater_node")){
        		screenFlag = false;
        	}
    	}
    	
    	if(screenFlag){
    		
    		callingParam[1][0] = screenLayout;
			callingParam[1][3] = picture1;
			callingParam[1][5] = picture2;
			
			callingParam[5][0] = decodeParam[0][5];
			callingParam[5][1] = decodeParam[0][4];
			callingParam[5][2] = decodeParam[1][4];
    		
    		List<BasicNameValuePair> callingBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair callingPair = new BasicNameValuePair("setString", TerminalParam.array2Data(callingParam));
    		callingBody.add(callingPair);
    		HttpAsyncClient.getInstance().formPost("http://" + bind.getBundleIp() + TerminalParam.POST_JV210_CALLSETTING_SUFFIX, null, callingBody, null);
    	}
    }
    
    /**
     * Jv220设备恢复流程<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年9月11日 上午8:51:44
     */
    public void bundleJv220Resume(BundleBO bundle) throws Exception{
      	    	
    	String bundleId = bundle.getBundle_id();
    	
    	String passby = bundle.getPass_by_str();
    	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
    	
    	TerminalBindRepeaterPO bind = terminalBindRepeaterDao.findByBundleId(bundleId);
    	
    	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
    	
    	List<ChannelBO> channels = bundle.getChannels();
    	
    	//显示模式：单画面--0,画中画--1,二等分--2
    	Long screenLayout = 0l;
    	Long window1 = 8l;
    	Long window2 = 0l;
    	
    	boolean screenFlag = false;
    	
    	Set<String> srcBundleIds = new HashSet<String>();
    	Set<String> dstBundleIds = new HashSet<String>();
    	srcBundleIds.add(bundleId);
    	dstBundleIds.add(bundleId);
    	//取bundleId和channelId用于查询
    	for(ChannelBO channel:channels){
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				srcBundleIds.add(srcBundleId);
			}
    	}
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(srcBundleIds, dstBundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO portMapping: mappings){
    		mappingIds.add(portMapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
    	
    	JSONObject multimediaParam = new JSONObject();
    	
    	for(ChannelBO channel:channels){
    		String channelId = channel.getChannel_id();
			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
			SourceBO source = baseParam.getSource();
			
			if(channelId.equals(ChannelType.VIDEOENCODE1.getType())){
				
    			Long main_bitrate = baseParam.getBitrate();
    	       	Long trasferMainBitrate = main_bitrate/1000l;
    	       	//视频码率
            	Long mainVideoBitrate = (long) (trasferMainBitrate * (7.0f/10.0f) > 4000l?4000l: trasferMainBitrate * (7.0f/10.0f));
            	//发送码率
            	Long mainAllBitrate = (long) (mainVideoBitrate * (28.0f/10.0f) > 10000l?10000l: mainVideoBitrate * (28.0f/10.0f));
    			//编码分辨率
            	String main_resoluton = baseParam.getResolution();
    			//编码格式
            	String main_codec = baseParam.getCodec();
    			
    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
    			String main_video_encode_address = mappingPO.getDstAddress();
    	    	Long main_video_encode_port = mappingPO.getDstPort();
    	    	
    	    	JSONObject v_send_0 = new JSONObject();
    	    	v_send_0.put("ip_send_addr", main_video_encode_address);
    	    	v_send_0.put("ip_send_port", main_video_encode_port);
    	    	v_send_0.put("sys_band", mainAllBitrate);
    	    	v_send_0.put("ctrl", 1l);
    	    	v_send_0.put("rtp_video_pt", Jv220Param.fromName(main_codec).getRelation());
    	    	
    	    	JSONObject v_enc_0 = new JSONObject();
    	    	v_enc_0.put("enc_bitrate", mainVideoBitrate);
    	    	v_enc_0.put("enc_reso", Jv220Param.fromName(main_resoluton).getProtocal());
    	    	v_enc_0.put("codec", Jv220Param.fromName(main_codec).getProtocal());
    	    	multimediaParam.put("v_send_0", v_send_0);
    	    	multimediaParam.put("v_enc_0", v_enc_0);
    	    	
    		}else if(channelId.equals(ChannelType.VIDEOENCODE2.getType())){
    			
    			Long sub_bitrate = baseParam.getBitrate();
    			Long trasferSubBitrate = sub_bitrate/1000l;
    			//视频码率
            	Long subVideoBitrate = (long) (trasferSubBitrate * (7.0f/10.0f) > 4000l?4000l: trasferSubBitrate * (7.0f/10.0f));
            	//发送码率
            	Long subAllBitrate = (long) (subVideoBitrate * (28.0f/10.0f) > 10000l?10000l: subVideoBitrate * (28.0f/10.0f));
            	//编码分辨率
    			String sub_resoluton = baseParam.getResolution();
    			//编码格式
            	String sub_codec = baseParam.getCodec();
            	
    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
    			String sub_video_encode_address = mappingPO.getDstAddress();
    	    	Long sub_video_encode_port = mappingPO.getDstPort(); 
    	    	
    	    	JSONObject v_send_1 = new JSONObject();
    	    	v_send_1.put("ip_send_addr", sub_video_encode_address);
    	    	v_send_1.put("ip_send_port", sub_video_encode_port);
    	    	v_send_1.put("sys_band", subAllBitrate);
    	    	v_send_1.put("ctrl", 1l);
    	    	v_send_1.put("rtp_video_pt", Jv220Param.fromName(sub_codec).getRelation());
    	    	
    	    	JSONObject v_enc_1 = new JSONObject();
    	    	v_enc_1.put("enc_bitrate", subVideoBitrate);
    	    	v_enc_1.put("enc_reso", Jv220Param.fromName(sub_resoluton).getProtocal());
    	    	v_enc_1.put("codec", Jv220Param.fromName(sub_codec).getProtocal());
    	    	multimediaParam.put("v_send_1", v_send_1);
    	    	multimediaParam.put("v_enc_1", v_enc_1);
    	    	
    		}else if(channelId.equals(ChannelType.AUDIOENCODE1.getType())){
    			
    			String audio_codec = baseParam.getCodec();
    			String gain = baseParam.getGain();
    			PortMappingPO mappingPO = queryUtilService.queryEncodePortMappingByChannel(mappings, bundleId, channelId);
    			String audio_encode_address = mappingPO.getDstAddress();
    	    	Long audio_encode_port = mappingPO.getDstPort();  
    	    	
    	    	JSONObject a_send= new JSONObject();
    	    	a_send.put("ip_send_addr", audio_encode_address);
    	    	a_send.put("ip_send_port", audio_encode_port);
    	    	a_send.put("ctrl", 1l);
    	    	a_send.put("codec", Jv220Param.fromName(audio_codec).getProtocal());
    	    	a_send.put("rtp_audio_pt", Jv220Param.fromName(audio_codec).getRelation());
    	    	
    	    	multimediaParam.put("a_send", a_send);
    	    	
    		}else if(channelId.equals(ChannelType.VIDEODECODE1.getType())){
    			
    			String codec = baseParam.getCodec();
    			
    	    	JSONObject vdec_0 = new JSONObject();
    	    	vdec_0.put("codec", Jv220Param.fromName(codec).getProtocal());
    	    	multimediaParam.put("vdec_0", vdec_0);
    	    	
    	    	JSONObject recv_0 = multimediaParam.getJSONObject("recv_0");
    	    	if(recv_0 == null){
    	    		recv_0 = new JSONObject();
    	    	}
    	    	recv_0.put("rtp_video_pt", Jv220Param.fromName(codec).getRelation());
    	    	multimediaParam.put("recv_0", recv_0);
    	    	
    	    	JSONObject videoDecodeParam0 = new JSONObject();
    	    	JSONObject ctrl = new JSONObject();
    	    	ctrl.put("ctrl", 1l);
    	    	videoDecodeParam0.put("udp_recv", ctrl);
    	    	
    	    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(0l, videoDecodeParam0).toJSONString(), null, null);
    	    	
    		}else if(channelId.equals(ChannelType.VIDEODECODE2.getType())){
    			
    			String codec = baseParam.getCodec();
    	    	
    	    	JSONObject vdec_1 = new JSONObject();
    	    	vdec_1.put("codec", Jv220Param.fromName(codec).getProtocal());
    	    	multimediaParam.put("vdec_1", vdec_1);
    	    	
    	    	JSONObject recv_1 = new JSONObject();
    	    	recv_1.put("rtp_video_pt", Jv220Param.fromName(codec).getRelation());
    	    	multimediaParam.put("recv_1", recv_1);
    	    	
    	    	JSONObject videoDecodeParam1 = new JSONObject();
    	    	JSONObject ctrl = new JSONObject();
    	    	ctrl.put("ctrl", 1l);
    	    	videoDecodeParam1.put("udp_recv", ctrl);
    	    	
    	    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(1l, videoDecodeParam1).toJSONString(), null, null);
    	    	
    		}else if(channelId.equals(ChannelType.AUDIODECODE1.getType())){
				
    			String codec = baseParam.getCodec();
    			
    	    	JSONObject adec_0 = new JSONObject();
    	    	adec_0.put("codec", Jv220Param.fromName(codec).getProtocal());
    	    	multimediaParam.put("adec_0", adec_0);
    	    	
    	    	JSONObject recv_0 = multimediaParam.getJSONObject("recv_0");
    	    	if(recv_0 == null){
    	    		recv_0 = new JSONObject();
    	    	}
    	    	recv_0.put("rtp_audio_pt", Jv220Param.fromName(codec).getRelation());
    	    	multimediaParam.put("recv_0", recv_0);
    	    	
    	    	JSONObject audioDecodeParam0 = new JSONObject();
    	    	JSONObject ctrl = new JSONObject();
    	    	ctrl.put("ctrl", 1l);
    	    	audioDecodeParam0.put("udp_recv", ctrl);
    	    	
    	    	HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.receiveSet(0l, audioDecodeParam0).toJSONString(), null, null);
    	    	
    		}
			
			PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
			if(source != null){
					
				String srcBundleId = source.getBundle_id();
				String srcChannelId = source.getChannel_id();
				String srcLayerId = source.getLayer_id();
				
				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
				
				if(mappingPO == null) throw new Exception(srcBundleId + "源终端未绑定转发器！");
				
				//layerId不相同，向其接入发pulldata
				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){

					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
							
				}
				
				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				if(taskPOs != null && taskPOs.size() > 0){
					//任务切换
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
					}
				}
			}else{
				
				List<TaskPO> taskPOs = null;
				if(mappingDst != null){
					taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
				}
				
				if(taskPOs != null){
					//切换为无效--(需要切换--占用带宽)
					for(TaskPO task: taskPOs){
						taskExecuteService.taskSwitch(task, bind.getAccessAddress(), 60000l);
					}
				}
			}			
		}  
    	
    	List<ScreenBO> screens = bundle.getScreens();
    	if(screens != null && screens.size() > 0){
    		//TODO:现在做的是对单屏处理（JV210）
    		for(ScreenBO screen: screens){
    			List<RectBO> rects = screen.getRects();
    			boolean isSingle = true; 
    			if(rects != null && rects.size() > 0){
    				for(RectBO rect: rects){
    					if(rect.getWidth().equals(10000l) && rect.getHeight().equals(10000l)){
    						window1 = Jv220Param.fromName(rect.getChannel()).getProtocal();
    					}else{
    						window2 = Jv220Param.fromName(rect.getChannel()).getProtocal();
    						if(rect.getType().equals("single")){
    							isSingle = false;
    						}
    					}
    				}
    			}
    			if(isSingle){
    				screenLayout = 0l;
    			}else{
    				screenLayout = 1l;
    			}
    		}
    		
    		screenFlag = true;
    	}
    	
    	if(passbyBO != null){
        	String type = passbyBO.getType();
        	if(type.equals("bind_repeater_node")){
        		screenFlag = false;
        	}
    	}
    	
    	if(screenFlag){

    		JSONObject v_disp_0 = new JSONObject();
    		v_disp_0.put("mode", screenLayout);
    		v_disp_0.put("win_0_ch", window1);
    		v_disp_0.put("win_1_ch", window2);
    		
    		multimediaParam.put("v_disp_0", v_disp_0);
    	}
    	
    	//multimedia参数设置
    	if(!multimediaParam.isEmpty()){
    		System.out.println(multimediaParam.toJSONString());
    		HttpAsyncClient.getInstance().httpAsyncPost("http://" + bind.getBundleIp() + TerminalParam.JV220_URL_SUFFIX, jv220Param.multimediaSet(multimediaParam).toJSONString(), null, null);
    	}
    	
    }
    
    /**
     * 用于S100恢复所有业务数据<br/>
     * <b>作者:</b>wjw<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年9月3日 下午1:43:18
     */
    public void allBundleResume(List<BundleBO> bundles) throws Exception{
    	
    	Set<String> srcBundleIds = new HashSet<String>();
    	Set<String> dstBundleIds = new HashSet<String>();
     	
    	for(BundleBO bundle: bundles){
    		
    		String bundleId = bundle.getBundle_id();
    		
    		List<ChannelBO> channels = bundle.getChannels();
    		
    		srcBundleIds.add(bundleId);
        	dstBundleIds.add(bundleId);
        	//取bundleId和channelId用于查询
        	for(ChannelBO channel:channels){String channelId = channel.getChannel_id();
    			if(channel.getChannel_param() != null){
	    			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
	    			SourceBO source = baseParam.getSource();
	    			
	    			if(source != null){
	    					
	    				String srcBundleId = source.getBundle_id();
	    				srcBundleIds.add(srcBundleId);
	    			}
    			}
        	}
    	}
    	
    	List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findByBundleIdIn(dstBundleIds);
    	
    	List<PortMappingPO> mappings = portMappingDao.findBySrcBundleIdInOrDstBundleIdIn(srcBundleIds, dstBundleIds);
    	List<Long> mappingIds = new ArrayList<Long>();
    	for(PortMappingPO portMapping: mappings){
    		mappingIds.add(portMapping.getId());
    	}
    	
    	List<TaskPO> tasks = taskDao.findByMappingIdIn(mappingIds);
    	
    	for(BundleBO bundle: bundles){
    		
    		String bundleId = bundle.getBundle_id();
        	
        	String passby = bundle.getPass_by_str();
        	PassbyBO passbyBO = JSON.parseObject(passby, PassbyBO.class);
        	
        	TerminalBindRepeaterPO bind = queryUtilService.queryBindByBundleId(binds, bundleId);
        	
        	if(bind == null) throw new Exception(bundle.getBundle_id() + "该终端未绑定转发器！");
        	
        	List<ChannelBO> channels = bundle.getChannels();
        	
        	for(ChannelBO channel:channels){
        		String channelId = channel.getChannel_id();
        		if(channel.getChannel_param() != null){
        			BaseParamBO baseParam = channel.getChannel_param().getBase_param();
        			SourceBO source = baseParam.getSource();
        			
        			if(source != null){
        					
        				String srcBundleId = source.getBundle_id();
        				String srcChannelId = source.getChannel_id();
        				String srcLayerId = source.getLayer_id();
        				
        				PortMappingPO mappingPO = queryUtilService.querySrcPortMapping(mappings, srcBundleId, srcChannelId);
        				PortMappingPO mappingDst = queryUtilService.queryDstPortMapping(mappings, bundleId, channelId); 
        				
        				//layerId不相同，向其接入发pulldata
        				if(bind.getLayerId() != null && srcLayerId != null && !bind.getLayerId().equals(srcLayerId)){

        					processPullDataRunable(srcLayerId, bind.getLayerId(), srcBundleId, srcChannelId, bundleId, channelId, mappingPO.getDstAddress(), mappingPO.getDstPort().toString());
        							
        				}
        				
        				List<TaskPO> taskPOs = queryUtilService.queryTaskByMappingId(tasks, mappingDst.getId());
        				if(taskPOs != null && taskPOs.size() > 0){
        					//任务切换
        					for(TaskPO task: taskPOs){
        						taskExecuteService.taskSwitch(task, mappingPO.getDstAddress(), mappingPO.getDstPort());
        					}
        				}
        			}
        		}
    		}  
    	}
    	
    }
    
    /**
	 * byte[]转16进制字符串<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:49:48
	 * @param byte[] src 
	 * @return String 16进制字符串
	 */
	public static String bytesToHexString(byte[] src){      
        StringBuilder stringBuilder = new StringBuilder();      
        if (src == null || src.length <= 0) {      
            return null;      
        }      
        for (int i = 0; i < src.length; i++) {      
            int v = src[i] & 0xFF;      
            String hv = Integer.toHexString(v);      
            if (hv.length() < 2) {      
                stringBuilder.append(0);      
            }      
            stringBuilder.append(hv);      
        }      
        return stringBuilder.toString();      
    } 
	
	/**
	 * 字符串编码<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:32:58
	 * @param String message 待编码字符串
	 * @return String 编码后十六进制
	 */
	public String encode(String message) throws Exception{
		if(message == null) return null;
		MessageDigest messageDigest = null;
		String encodeMessage = "";
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(message.getBytes("UTF-8"));
		encodeMessage = ByteUtil.bytesToHexString(messageDigest.digest());
		return encodeMessage;
	}
	
	public String sign(String appId, String timestamp, String appSecret) throws Exception{
		List<String> resources = new ArrayListWrapper<String>().add(appId)
															   .add(timestamp)
															   .add(appSecret)
															   .getList();
		Collections.sort(resources, new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		String unSigned = new StringBufferWrapper().append(resources.get(0))
												   .append(resources.get(1))
												   .append(resources.get(2))
												   .toString();
		return encode(unSigned);
	}
    
}
