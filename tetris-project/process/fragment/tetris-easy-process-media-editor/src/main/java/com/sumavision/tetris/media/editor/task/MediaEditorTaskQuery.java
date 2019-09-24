package com.sumavision.tetris.media.editor.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class MediaEditorTaskQuery {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private MediaEditorTaskDAO mediaEditorTaskDao;
	
	@Autowired
	private MediaEditorTaskRatePermissionQuery mediaEditorTaskRatePermissionQuery;

	/**
	 * 根据用户以及任务状态分页查询任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午10:58:21
	 * 
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @param MediaEditorTaskStatus status 任务状态
	 * @return rows List<MediaEditorTaskPO> 任务列表
	 * @return total int 总数据量
	 */
	public Map<String, Object> list(int currentPage, int pageSize, MediaEditorTaskStatus status) throws Exception {

		UserVO user = userQuery.current();

		Pageable page = new PageRequest(currentPage - 1, pageSize);

		Page<MediaEditorTaskPO> pagedTasks = mediaEditorTaskDao.findByUserIdAndStatus(user.getUuid(), status, page);

		int total = mediaEditorTaskDao.countByUserIdAndStatus(user.getUuid(), status);

		return new HashMapWrapper<String, Object>().put("rows", pagedTasks.getContent()).put("total", total).getMap();
	}

	/**
	 * 查询任务进度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午11:06:57
	 * 
	 * @param Long id 任务id
	 * @return
	 */
	public Object queryProgress(Long id) throws Exception {

		MediaEditorTaskPO task = mediaEditorTaskDao.findOne(id);

		if (MediaEditorTaskStatus.APPROVING.equals(task.getStatus())) {
			String processInstanceId = task.getProcessInstanceId();

		} else {
			return task.getStatus().toString();
		}

		return null;
	}
	
	/**
	 * 根据流程id查询任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @param String processId 流程id
	 * @return MediaEditorTaskVO 任务
	 */
	public MediaEditorTaskVO getByProcessId(String processId) throws Exception{
		MediaEditorTaskPO task = mediaEditorTaskDao.findByProcessInstanceId(processId);
		
		if (task == null) return null;
		
		return new MediaEditorTaskVO().set(task);
	}
	
	/**
	 * 查询未完成任务列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<MediaEditorTaskPO> 任务列表
	 */
	public List<MediaEditorTaskPO> getExcepteTemplete(){
		return mediaEditorTaskDao.findAllExceptTempleteRate();
	}
	
	/**
	 * 根据用户获取任务和任务下的子转码任务树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<MediaEditorTaskVO> 任务列表
	 */
	public List<MediaEditorTaskVO> getTaskTree(UserVO userVO) throws Exception{
		List<MediaEditorTaskPO> taskPOs = mediaEditorTaskDao.findByUserId(userVO.getUuid());
		
		if (taskPOs == null || taskPOs.size() <= 0) return null;
		
		List<MediaEditorTaskVO> taskVOs = new ArrayList<MediaEditorTaskVO>();
		
		for (MediaEditorTaskPO task : taskPOs) {
			List<MediaEditorTaskRatePermissionVO> column = mediaEditorTaskRatePermissionQuery.getByTaskId(task.getId());
			
			taskVOs.add(new MediaEditorTaskVO().set(task).setTranscodes(column));
		}
		
		return taskVOs;
	}
	
	/**
	 * 根据任务id查询任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<MediaEditorTaskPO> 任务列表
	 */
	public List<MediaEditorTaskPO> getByIds(List<Long> ids){
		return mediaEditorTaskDao.findAll(ids);
	}
}
