package com.sumavision.signal.bvc.entity.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = TaskPO.class, idClass = long.class)
public interface TaskDAO extends BaseDAO<TaskPO>{

	/**
	 * 根据mappingId查询任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午4:33:13
	 * @param mappingId 映射关系id
	 */
	public List<TaskPO> findByMappingId(Long mappingId);
	
	public List<TaskPO> findByMappingIdAndStatus(Long mappingId, String status);
	
	/**
	 * 根据ids查询任务列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午7:26:08
	 * @param ids
	 * @return
	 */
	public List<TaskPO> findByMappingIdIn(List<Long> ids);
	
	public List<TaskPO> findByMappingIdInAndStatus(List<Long> ids, String status);
	
	/**
	 * 根据集群ip查询任务列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 下午6:50:13
	 * @param String ip 集群ip
	 * @return List<TaskPO>
	 */
	public List<TaskPO> findByIp(String ip);
	
	/**
	 * 根据taskId删除任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午3:00:13
	 * @param taskId
	 */
	public void deleteByTaskId(String taskId);
	
	/**
	 * 方法概述<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月15日 下午4:43:19
	 * @param String ip 集群ip
	 */
	public void deleteByIp(String ip);
	
	/**
	 * 根据taskId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午2:17:42
	 * @param String taskId
	 * @return TaskPO
	 */
	public TaskPO findByTaskId(String taskId);
	
	public List<TaskPO> findByTaskIdIn(Collection<String> taskIds);
	
	/**
	 * 根据mappingId和ip查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月20日 下午2:25:52
	 * @param Long mappingId
	 * @param String ip
	 */
	public TaskPO findByMappingIdAndIp(Long mappingId, String ip);
	
	public List<TaskPO> findByMappingIdInAndIpIn(Collection<Long> mappingIds, Collection<String> ips);
	
	/**
	 * 根据集群ips分页查询任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月3日 下午3:24:17
	 * @param Collection<String> ips 集群ips
	 * @param Pageable page 分页信息
	 * @return Page<TaskPO>
	 */
	@Query("select task from TaskPO task where task.ip in ?1")
	public Page<TaskPO> findByIpIn(Collection<String> ips, Pageable page);
	
	/**
	 * 根据任务状态查询任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月15日 下午4:36:12
	 */
	public List<TaskPO> findByStatus(String status);
	
	/**
	 * 根据目的ip和目的端口查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月19日 下午5:18:30
	 */
	public List<TaskPO> findByDstIpAndDstPort(String ip, String port);
	
	/**
	 * 根据目的ip列表查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月19日 下午5:20:22
	 */
	public List<TaskPO> findByDstIpIn(Collection<String> ips);
	
	@Modifying
	public void deleteByIdIn(Collection<Long> ids);
	
}
