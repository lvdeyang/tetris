package com.sumavision.tetris.media.editor.task.api.qt;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskDAO;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskQuery;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskStatus;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;

//@Controller
//@RequestMapping(value = "/api/qt/media/editor/task")
public class ApiQtMediaEditorTaskController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaEditorTaskDAO mediaEditorTaskDao;
	
	@Autowired
	private MediaEditorTaskQuery mediaEditorTaskQuery;
	
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/{status}")
	public Object list(
			@PathVariable String status,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return mediaEditorTaskQuery.list(currentPage, pageSize, MediaEditorTaskStatus.valueOf(status.toUpperCase()));
	}
	
	/**
	 * 查询任务进度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 上午11:06:57
	 * @param @PathVariable Long id 任务id
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/progress/{id}")
	public Object queryProgress(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		return mediaEditorTaskQuery.queryProgress(id);
	}
	
}
