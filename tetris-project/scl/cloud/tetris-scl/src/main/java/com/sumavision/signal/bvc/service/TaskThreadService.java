package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.tetris.commons.context.SpringContext;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskThreadService {
	
	private static final int INTERVAL = 1000;
	
	private static final Logger LOG = LoggerFactory.getLogger(TaskThreadService.class);
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private PortMappingDAO portMappingDao;
	
	@Autowired
	private QueryUtilService queryUtilService;

	public void init(){
		
		Thread taskCreateThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(INTERVAL);
					}catch(Exception e){
						LOG.error("创建任务线程被打断！", e);
					}
					try{
						TaskCreateQueue queue = TaskCreateQueue.getInstance();
						Object[] objects = queue.get();
						if(objects != null){
							
							Set<Long> mappingIds = new HashSet<Long>();
							Set<String> ips = new HashSet<String>();
							for(Object object: objects){
								String res = object.toString();
								JSONObject jsonObject = JSONObject.parseObject(res);
								Long mappingId = jsonObject.getLong("mappingId");
								String ip = jsonObject.getString("ip");
								mappingIds.add(mappingId);
								ips.add(ip);
							}
							
							List<TaskPO> tasks = taskDao.findByMappingIdInAndIpIn(mappingIds, ips);
							List<TaskPO> needAddTasks = new ArrayList<TaskPO>();
							for(Object object: objects){
								String res = object.toString();
								
								JSONObject jsonObject = JSONObject.parseObject(res);
								
								Long mappingId = jsonObject.getLong("mappingId");
								String ip = jsonObject.getString("ip");
								String dip = jsonObject.getString("dip");
								String dport = jsonObject.getString("dport");
								Long status = jsonObject.getLong("result");
								
								TaskPO task = queryUtilService.queryTask(tasks, mappingId, ip);
								if(task == null){
									task = new TaskPO();
									task.setMappingId(mappingId);
									task.setIp(ip);
									task.setDstIp(dip);
									task.setDstPort(dport);
								}
								
								task.setStatus(TaskStatus.fromNum(status).getStatus());
								task.setMessage(TaskStatus.fromNum(status).getMessage());
								
								if(status.equals(0l)){
					
									JSONObject bodyJson = JSON.parseObject(jsonObject.getString("body"));
									
									String taskId = bodyJson.getString("tid");
									String outIp = bodyJson.getString("outip");
									
									task.setTaskId(taskId);
									task.setOutIp(outIp);
									
									String address = jsonObject.getString("address");
									Long port = jsonObject.getLong("port");
									
									if(address != null && port != null){
										TaskExecuteService taskExecuteService = SpringContext.getBean(TaskExecuteService.class);
										taskExecuteService.taskSwitch(task, address, port);
									}
									
								}else{
									
									System.out.println("新建任务失败！");
								}	
								
								queue.take(res);
								needAddTasks.add(task);
							}
							
							if(needAddTasks.size() > 0){
								taskDao.save(needAddTasks);
							}
						}
						
					}catch(Exception e){
						LOG.error("创建任务线程被打断！", e);
					}
				}
				
			}
		});
		
		Thread taskSwitchThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(INTERVAL);
					}catch(Exception e){
						LOG.error("切换任务线程被打断！", e);
					}
					try{
						TaskSwitchQueue queue = TaskSwitchQueue.getInstance();
						Object[] objects = queue.get();
						
						if(objects != null){
							Set<String> taskIds = new HashSet<String>();
							List<TaskPO> needUpdateTasks = new ArrayList<TaskPO>();
							for(Object object: objects){
								String res = object.toString();
								JSONObject jsonObject = JSONObject.parseObject(res);
								String taskId = jsonObject.getString("taskId");
								taskIds.add(taskId);
							}
							
							List<TaskPO> tasks = taskDao.findByTaskIdIn(taskIds);
							for(Object object: objects){
								String res = object.toString();
								JSONObject jsonObject = JSONObject.parseObject(res);
								String taskId = jsonObject.getString("taskId");
								TaskPO task = queryUtilService.queryTaskByTaskId(tasks, taskId);
								if(task != null){
									String sip = jsonObject.getString("sip");
									String sport = jsonObject.getString("sport");
									task.setSrcIp(sip);
									task.setSrcPort(sport);
									task.setUpdateTime(new Date());
									needUpdateTasks.add(task);
								}
								
								queue.take(res);
							}
							
							if(needUpdateTasks.size() > 0){
								taskDao.save(needUpdateTasks);
							}
						}
						
					}catch(Exception e){
						LOG.error("切换任务线程异常！", e);
					}
				}
				
			}
		});
		
		Thread taskDestoryThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(INTERVAL);
					}catch(Exception e){
						LOG.error("销毁任务线程被打断！", e);
					}
					try{
						TaskDestoryQueue queue = TaskDestoryQueue.getInstance();
						Object[] objects = queue.get();
						
						List<String> taskIds = new ArrayList<String>();
						if(objects != null){
							for(Object object: objects){
								String res = object.toString();
								JSONObject jsonObject = JSONObject.parseObject(res);
								String taskId = jsonObject.getString("taskId");
								taskIds.add(taskId);
							}
						}
						
						List<TaskPO> tasks = taskDao.findByTaskIdIn(taskIds);
						if(objects != null){
							Set<Long> needRemoveTaskIds = new HashSet<Long>();
							Set<Long> needRemoveMappingIds = new HashSet<Long>(); 
							for(Object object: objects){
								String res = object.toString();
								JSONObject jsonObject = JSONObject.parseObject(res);
								String taskId = jsonObject.getString("taskId");
								TaskPO task = queryUtilService.queryTaskByTaskId(tasks, taskId);
								if(task != null){
									String status = jsonObject.getString("result");
									//0代表销毁成功，3代表任务不存在，都需要删除任务数据
									if(status.equals("0") || status.equals("3")){
										needRemoveTaskIds.add(task.getId());
										needRemoveMappingIds.add(task.getMappingId());
									}else{
										LOG.error("taskId为" + taskId + "销毁失败！");
									}
								}
								
								queue.take(res);
							}
							
							if(needRemoveTaskIds.size() > 0){
								taskDao.deleteByIdIn(needRemoveTaskIds);
							}
							
							if(needRemoveMappingIds.size() > 0){
								portMappingDao.deleteByIdIn(needRemoveMappingIds);
							}
						}
						
					}catch(Exception e){
						LOG.error("销毁任务线程异常！", e);
					}
				}
			}
		});
		
		taskDestoryThread.start();
		taskCreateThread.start();
		taskSwitchThread.start();
		LOG.info("任务监控线程启动！");
		
	}
	
}
