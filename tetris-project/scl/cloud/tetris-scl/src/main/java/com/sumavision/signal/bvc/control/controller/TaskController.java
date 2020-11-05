package com.sumavision.signal.bvc.control.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.util.RegisterStatus;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.dao.TerminalBindRepeaterDAO;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.po.TerminalBindRepeaterPO;
import com.sumavision.signal.bvc.feign.ResourceServiceClient;
import com.sumavision.signal.bvc.mq.ProcessReceivedMsg;
import com.sumavision.signal.bvc.mq.bo.BundleBO;
import com.sumavision.signal.bvc.service.TaskExecuteService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/signal/control/task")
public class TaskController {

	@Autowired
	private RepeaterDAO repeaterDao;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;
	
	@Autowired
	private ProcessReceivedMsg processReceivedMsg;
	
	@Autowired
	private ResourceServiceClient resourceServiceClient;
	
	@Autowired
	private TerminalBindRepeaterDAO terminalBindRepeaterDao;
	
	/**
	 * 分页查询主机的所有任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月3日 下午3:31:41
	 * @param int pageSize 每页数量
	 * @param int currentPage 当前页数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/main/all")
	public Object queryMainAll(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		List<RepeaterPO> repeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<String> ips = new ArrayList<String>();
		for(RepeaterPO repeater: repeaters){
			ips.add(repeater.getIp());
		}
		
		if(ips != null && ips.size() > 0){
			PageRequest page = new PageRequest(currentPage-1, pageSize);
			
			Page<TaskPO> pages = taskDao.findByIpIn(ips, page);
			List<TaskPO> tasks = pages.getContent();
			long total = pages.getTotalElements();
			
			return new HashMapWrapper<String, Object>().put("rows", tasks)
													   .put("total", total)
													   .getMap();
		}
		
		return null;
	}
	
	/**
	 * 分页查询备机的所有任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月3日 下午3:31:41
	 * @param int pageSize 每页数量
	 * @param int currentPage 当前页数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/backup/all")
	public Object queryBackupAll(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		List<RepeaterPO> repeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		List<String> ips = new ArrayList<String>();
		for(RepeaterPO repeater: repeaters){
			ips.add(repeater.getIp());
		}
		
		if(ips != null && ips.size() > 0){
			PageRequest page = new PageRequest(currentPage-1, pageSize);
			
			Page<TaskPO> pages = taskDao.findByIpIn(ips, page);
			List<TaskPO> tasks = pages.getContent();
			long total = pages.getTotalElements();
			
			return new HashMapWrapper<String, Object>().put("rows", tasks)
													   .put("total", total)
													   .getMap();
		}
		
		return null;
	}
	
	/**
	 * 手动切换任务br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月4日 下午3:37:19
	 * @param String taskId 任务号
	 * @param String newIp 要切换的ip
	 * @param String newPort 要切换的port
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/switch")
	public Object switchTask(
			String taskId,
			String newIp,
			Long newPort,
			HttpServletRequest request) throws Exception{
		
		TaskPO task = taskDao.findByTaskId(taskId);
		
		taskExecuteService.taskSwitch(task, newIp, newPort);
		
		return null;
	}
	
	/**
	 * 任务计数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月20日 下午1:31:03
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/count")
	public Object count(HttpServletRequest request) throws Exception{
		
		List<TaskPO> tasks = taskDao.findAll();
		int total = tasks.size();
		
		int num = 0;
		if(tasks != null && tasks.size() > 0){
			for(TaskPO task: tasks){
				if(task.getStatus().equals(TaskStatus.zero.getStatus())){
					num ++ ;
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("success", num )
			   									   .put("total", total)
			   									   .getMap();
	}
	
	/**
	 * 任务恢复<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月22日 上午10:21:17
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resume")
	public Object resume(HttpServletRequest request) throws Exception{
		
		String layerId = RegisterStatus.getNodeId();
		
		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		
		RepeaterPO main = null;
		RepeaterPO backup = null;
		if(mainRepeaters.size() > 0) main = mainRepeaters.get(0);
		if(backupRepeaters.size() > 0) backup = backupRepeaters.get(0);
		
		List<TaskPO> tasks = taskDao.findByStatus(TaskStatus.zero.getStatus());
		
		List<TaskPO> mainNeedCommitTasks = new ArrayList<TaskPO>();
		if(main != null){
			List<Long> mainTaskIds = taskExecuteService.getTaskIds(main);
			List<TaskPO> mainTasks = new ArrayList<TaskPO>();
			for(TaskPO task: tasks){
				if(task.getIp().equals(main.getIp())){
					mainTasks.add(task);
				}
			}
			
			if(mainTasks.size() > 0){
				for(TaskPO mainTask: mainTasks){
					boolean need = true;
					for(Long mainTaskId: mainTaskIds){
						if(mainTaskId.toString().equals(mainTask.getTaskId())){
							need = false;
							break;
						}
					}
					
					if(need){
						mainNeedCommitTasks.add(mainTask);
					}
				}
			}
		}
		
		List<TaskPO> backupNeedCommitTasks = new ArrayList<TaskPO>();
		if(backup != null){
			List<Long> backupTaskIds = taskExecuteService.getTaskIds(backup);
			List<TaskPO> backupTasks = new ArrayList<TaskPO>();
			for(TaskPO task: tasks){
				if(task.getIp().equals(backup.getIp())){
					backupTasks.add(task);
				}
			}
			
			if(backupTasks.size() > 0){
				for(TaskPO backupTask: backupTasks){
					boolean need = true;
					for(Long backupTaskId: backupTaskIds){
						if(backupTaskId.toString().equals(backupTask.getTaskId())){
							need = false;
							break;
						}
					}
					
					if(need){
						backupNeedCommitTasks.add(backupTask);
					}
				}
			}
		}
		
		if(main != null){
			if(mainNeedCommitTasks.size() > 0){
				for(TaskPO mainNeedCommitTask: mainNeedCommitTasks){
					if(mainNeedCommitTask.getSrcPort() != null && !mainNeedCommitTask.getSrcPort().equals("60000")){
						taskExecuteService.taskCreatePost(main.getIp(), mainNeedCommitTask.getMappingId(), mainNeedCommitTask.getDstIp(), Long.valueOf(mainNeedCommitTask.getDstPort()), mainNeedCommitTask.getSrcIp(), Long.valueOf(mainNeedCommitTask.getSrcPort()));
					}else{
						taskExecuteService.taskCreatePost(main.getIp(), mainNeedCommitTask.getMappingId(), mainNeedCommitTask.getDstIp(), Long.valueOf(mainNeedCommitTask.getDstPort()), null, null);
					}
				}
			}
		}
		
		if(backup != null){
			if(backupNeedCommitTasks.size() > 0){
				for(TaskPO backupNeedCommitTask: backupNeedCommitTasks){
					if(backupNeedCommitTask.getSrcPort() != null && !backupNeedCommitTask.getSrcPort().equals("60000")){
						taskExecuteService.taskCreatePost(backup.getIp(), backupNeedCommitTask.getMappingId(), backupNeedCommitTask.getDstIp(), Long.valueOf(backupNeedCommitTask.getDstPort()), backupNeedCommitTask.getSrcIp(), Long.valueOf(backupNeedCommitTask.getSrcPort()));
					}else{
						taskExecuteService.taskCreatePost(backup.getIp(), backupNeedCommitTask.getMappingId(), backupNeedCommitTask.getDstIp(), Long.valueOf(backupNeedCommitTask.getDstPort()), null, null);
					}
				}
			}
		}
		
		//恢复业务数据 -- 资源查询所有openBundle信息
		List<TerminalBindRepeaterPO> binds = terminalBindRepeaterDao.findByLayerId(layerId);
		List<String> bundleIds = new ArrayList<String>();
		for(TerminalBindRepeaterPO bind: binds){
			bundleIds.add(bind.getBundleId());
		}
		
		Map<String, Object> bundleResult = resourceServiceClient.getBatchBundleInfos(bundleIds);
		List<BundleBO> bundles = JSONArray.parseArray(JSONObject.toJSONString(bundleResult.get("get_bundle_info_response")),BundleBO.class);
				
		processReceivedMsg.allBundleResume(bundles);
		
		return null;
	}
	
	/**
	 * 所有任务切换接口 <br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月6日 上午9:02:52
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/test")
	public Object test(HttpServletRequest request) throws Exception{
		
		List<TaskPO> tasks = taskDao.findAll();
		for(TaskPO task: tasks){
			taskExecuteService.taskSwitch(task, "192.165.56.144", 60000l);
		}
		
		return null;
	}
	
}
