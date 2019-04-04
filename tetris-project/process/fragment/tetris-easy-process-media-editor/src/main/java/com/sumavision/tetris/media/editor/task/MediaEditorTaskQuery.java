package com.sumavision.tetris.media.editor.task;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

public class MediaEditorTaskQuery {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaEditorTaskDAO mediaEditorTaskDao;
	
	/**
	 * 根据用户以及任务状态分页查询任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午10:58:21
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @param MediaEditorTaskStatus status 任务状态
	 * @return rows List<MediaEditorTaskPO> 任务列表
	 * @return total int 总数据量
	 */
	public Map<String, Object> list(
			int currentPage,
			int pageSize,
			MediaEditorTaskStatus status) throws Exception{
		
		UserVO user = userQuery.current();
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		Page<MediaEditorTaskPO> pagedTasks = mediaEditorTaskDao.findByUserIdAndStatus(user.getUuid(), status, page);
		
		int total = mediaEditorTaskDao.countByUserIdAndStatus(user.getUuid(), status);
		
		return new HashMapWrapper<String, Object>().put("rows", pagedTasks.getContent())
												   .put("total", total)
												   .getMap();
	}
	
	/**
	 * 查询任务进度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午11:06:57
	 * @param Long id 任务id
	 * @return 
	 */
	public Object queryProgress(Long id) throws Exception{
		
		MediaEditorTaskPO task = mediaEditorTaskDao.findOne(id);
		
		if(MediaEditorTaskStatus.APPROVING.equals(task.getStatus())){
			String processInstanceId = task.getProcessInstanceId();
			
			
			
		}else{
			return task.getStatus().toString();
		}
		
		return null;
	}
	
}
