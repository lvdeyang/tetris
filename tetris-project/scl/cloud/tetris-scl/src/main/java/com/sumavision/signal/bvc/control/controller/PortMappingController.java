package com.sumavision.signal.bvc.control.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.signal.bvc.entity.dao.PortMappingDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.entity.vo.PortMappingVO;
import com.sumavision.signal.bvc.service.TaskExecuteService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/signal/control/mapping")
public class PortMappingController {
	
	@Autowired
	private PortMappingDAO portMappingDao;
	
	@Autowired
	private RepeaterDAO repeaterDao;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;

	/**
	 * 查询主机任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 下午4:47:21
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/main/all")
	public Object queryMainAll(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		List<RepeaterPO> repeaters = repeaterDao.findByType(RepeaterType.MAIN);
		Page<PortMappingPO> mappingPages = portMappingDao.findByDstType(DstType.TERMINAL, page);
		List<PortMappingPO> mappings = mappingPages.getContent();
		long total = 0;
		List<PortMappingVO> mappingVos = new ArrayList<PortMappingVO>();
		List<TaskPO> tasks = new ArrayList<TaskPO>();
		String repeaterNetIp = null;
		
		if(repeaters != null && repeaters.size() > 0){
			repeaterNetIp = repeaters.get(0).getIp();
			tasks = taskDao.findByIp(repeaterNetIp);
			
			total = mappingPages.getTotalElements();
		
			if(mappings != null && mappings.size() > 0){
				for(PortMappingPO mapping: mappings){
					PortMappingVO mappingVO = new PortMappingVO().set(mapping);
					mappingVO.setNetIp(repeaterNetIp);
					for(TaskPO task: tasks){
						if(task.getMappingId().equals(mapping.getId())){
							mappingVO.setTaskId(task.getTaskId());
						}
					}
					mappingVos.add(mappingVO);
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("rows", mappingVos)
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 查询备机任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 下午4:47:21
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/backup/all")
	public Object queryBackupAll(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		List<RepeaterPO> repeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		Page<PortMappingPO> mappingPages = portMappingDao.findByDstType(DstType.TERMINAL, page);
		List<PortMappingPO> mappings = mappingPages.getContent();
		long total = 0;
		List<PortMappingVO> mappingVos = new ArrayList<PortMappingVO>();
		List<TaskPO> tasks = new ArrayList<TaskPO>();
		String repeaterNetIp = null;
		
		if(repeaters != null && repeaters.size() > 0){
			repeaterNetIp = repeaters.get(0).getIp();
			tasks = taskDao.findByIp(repeaterNetIp);
			
			total = mappingPages.getTotalElements();
		
			if(mappings != null && mappings.size() > 0){
				for(PortMappingPO mapping: mappings){
					PortMappingVO mappingVO = new PortMappingVO().set(mapping);
					mappingVO.setNetIp(repeaterNetIp);
					for(TaskPO task: tasks){
						if(task.getMappingId().equals(mapping.getId())){
							mappingVO.setTaskId(task.getTaskId());
						}
					}
					mappingVos.add(mappingVO);
				}
			}
		}
		
		return new HashMapWrapper<String, Object>().put("rows", mappingVos)
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 单个任务创建<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午2:18:57
	 * @param String taskId
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			Long id,
			String netIp,
			HttpServletRequest request) throws Exception{
		
		PortMappingPO mapping = portMappingDao.findOne(id);
		
		TaskPO task = taskDao.findByMappingIdAndIp(id, netIp);
		
		if(task == null || !task.getStatus().equals(TaskStatus.zero.getStatus())){
			taskExecuteService.taskCreatePost(netIp, mapping, null, null);
		}
		
		return null;
	}
	
	/**
	 * 单个任务删除<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午2:18:57
	 * @param String taskId
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/task")
	public Object removeTask(
			String taskId,
			HttpServletRequest request) throws Exception{
		
		TaskPO task = taskDao.findByTaskId(taskId);
		
		taskExecuteService.taskDestory(task);
		
		return null;
	}
	
}
