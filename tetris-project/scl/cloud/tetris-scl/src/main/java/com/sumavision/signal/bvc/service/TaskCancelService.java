package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.TaskPO;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskCancelService {
	
	//任务销毁间隔
	public static int INTERVAL = 20000;
	//最大任务数--单个ip
	public static int MAX = 2;
	//时间差
	public static int TIME = 2 * 1000;
	//任务cancel的端口号
	public static final String USELESS_PORT = "60000";
	
	private static final Logger LOG = LoggerFactory.getLogger(TaskCancelService.class);
	
	@Autowired
	private PortMappingDAO portMappingDao;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;

	/**
	 * 初始化销毁任务线程<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月19日 下午2:47:29
	 */
	public void initCancelTask() throws Exception{
		
		Properties prop = new Properties();
		try {
			prop.load(TaskCancelService.class.getClassLoader().getResourceAsStream("properties/task.properties"));
			INTERVAL = Integer.valueOf(prop.getProperty("task.interval"));
			TIME = Integer.valueOf(prop.getProperty("task.time"));
			MAX = Integer.valueOf(prop.getProperty("task.max"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("线程时间间隔：" + INTERVAL + " 任务销毁时间差：" + TIME + " 单个接入最大任务个数：" + MAX);
		
		Thread taskCancelThread = new Thread(new Runnable() {
			
			@Override
			public void run() {

				while(true){
					try {
						Thread.sleep(INTERVAL);
					} catch (Exception e){
						LOG.error("任务销毁线程被打断！", e);
					}
					
					try {
						
						Date date = new Date();
						
						List<String> ips = portMappingDao.findDstAddressByDstType(DstType.LAYER);
						List<TaskPO> tasks = taskDao.findByDstIpIn(ips);
						
						List<TaskPO> needRemoveTasks = new ArrayList<TaskPO>();
						
						for(String ip: ips){
							List<TaskPO> ipTasks = new ArrayList<TaskPO>();
							for(TaskPO task: tasks){
								if(ip.equals(task.getDstIp())){
									ipTasks.add(task);
								}
							}
							
							if(ipTasks.size() > MAX){
								for(TaskPO task: ipTasks){
									if(date.getTime() - task.getUpdateTime().getTime() > TIME && task.getSrcPort().equals(USELESS_PORT)){
										needRemoveTasks.add(task);
									}
								}
							}
						}
						
						List<Long> needCommitTaskIds = new ArrayList<Long>();
						List<Long> needCommitMappingIds = new ArrayList<Long>();
						if(needRemoveTasks.size() > 0){
							
							LOG.info("清除 " + needRemoveTasks.size() + " 条任务！");
							
							for(TaskPO task: needRemoveTasks){
								if(task.getStatus().equals(TaskStatus.zero.getStatus())){
									taskExecuteService.taskDestory(task);
								}else{
									needCommitTaskIds.add(task.getId());
								}
								
								if(task.getMappingId() != null){
									needCommitMappingIds.add(task.getMappingId());
								}
							}
						}
						
						if(needCommitTaskIds.size() > 0){
							taskDao.deleteByIdIn(needCommitTaskIds);
						}
						if(needCommitMappingIds.size() > 0){
							portMappingDao.deleteByIdIn(needCommitMappingIds);
						}

					} catch (Exception e) {
						LOG.error("任务销毁线程执行异常！", e);
					}
				}
				
			}
		});
		
		taskCancelThread.start();
		LOG.info("任务销毁线程启动！");
	}
}
