package com.sumavision.tetris.record.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/record/task")
public class RecordTaskController {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private RecordTaskDAO recordTaskDAO;
	
	

	/**
	 * 
	 * 从媒资系统查询直播源信息
	 * 
	 * @param keyword
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/querySource")
	public Object querySourceInfo(String keyword, Integer currentPage, Integer pageSize) {

		// TODO

		return null;
	}

	/**
	 * 查询现有收录任务列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTask")
	public Object queryTask(Integer currentPage, Integer pageSize) {

		// TODO

		return null;
	}

	@RequestMapping("/queryTaskItem")
	@ResponseBody
	public Object queryTaskItemByTaskId(Long taskId, Integer currentPage, Integer pageSize) {

		// TODO

		return null;
	}

	@RequestMapping("/addTask")
	@ResponseBody
	public Object addRecordTask(Integer currentPage, Integer pageSize) {

		// TODO

		return null;
	}

	@RequestMapping("/modifyTask")
	@ResponseBody
	public Object modifyRecordTask(Integer currentPage, Integer pageSize) {

		// TODO

		return null;
	}

	@RequestMapping("/delTask")
	@ResponseBody
	public Object delRecordTask(Long taskId) {

		// TODO

		return null;
	}
	
	@RequestMapping("/uploadEpgInfo")
	@ResponseBody
	public Object uploadEpgInfo() {

		// TODO
		return null;
	}

}
