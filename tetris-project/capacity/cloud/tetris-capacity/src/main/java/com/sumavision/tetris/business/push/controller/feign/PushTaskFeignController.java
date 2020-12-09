package com.sumavision.tetris.business.push.controller.feign;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.sumavision.tetris.application.annotation.OprLog;
import org.hibernate.AssertionFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.push.service.ScheduleService;
import com.sumavision.tetris.business.push.vo.ScheduleTaskVO;
import com.sumavision.tetris.business.push.vo.SwitchScheduleVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/push/task/feign")
public class PushTaskFeignController {

	private static final Logger LOG = LoggerFactory.getLogger(PushTaskFeignController.class);

	@Autowired
	private ScheduleService scheduleService;

	/**
	 * 添加排期任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午5:17:56
	 * @param String task push任务信息
	 * @return String 任务标识
	 */
	@OprLog(name = "push")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/task")
	public Object addTask(
			String task) throws Exception{
		ScheduleTaskVO pushTask = JSONObject.parseObject(task, ScheduleTaskVO.class);
		String result = addPushTaskTrascation(pushTask);
		return result;
	}
	
	/**
	 * 删除排期任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午5:17:56
	 * @param String taskId push任务id
	 */
	@OprLog(name = "push")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/task")
	public Object deleteTask(
			String taskId) throws Exception{
		scheduleService.deletePushTask(taskId);
		return null;
	}

	@OprLog(name = "push")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/clear/task")
	public Object clearTask(
			String taskId) throws Exception{
		scheduleService.clearPushTask(taskId);
		return null;
	}
	
	/**
	 * 切换节目单<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月25日 下午5:28:41
	 * @param String changeSchedule 节目单信息
	 */
	@OprLog(name = "push")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change/schedule")
	public Object changeSchedule(
			String changeSchedule) throws Exception{
		SwitchScheduleVO schedule = JSONObject.parseObject(changeSchedule, SwitchScheduleVO.class);
		changeScheduleTrascation(schedule);
		return null;
	}
	
	/**
	 * 批量删除push编单任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月27日 上午11:17:04
	 * @param String taskIds 任务ids
	 */
	@OprLog(name = "push")
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/batch/delete/task")
	public Object batchDeleteTask(
			String taskIds,
			HttpServletRequest request) throws Exception{
		List<String> taskUuids = JSONArray.parseArray(taskIds, String.class);
		scheduleService.batchDeletePushTask(taskUuids);
		return null;
	}
	
	/**
	 * 添加schedule排期任务--保证事务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午2:48:10
	 * @param ScheduleTaskVO task push排期任务
	 * @return String 任务标识
	 */
	public String addPushTaskTrascation(ScheduleTaskVO task) throws Exception{
		
		try {
			
			Random random = new Random();
			Thread.sleep(random.nextInt(300) + 300);
			return scheduleService.addPushTask(task);
			
		} catch (DataIntegrityViolationException e) {
			
			System.out.println("校验输入已存在DataIntegrityViolationException");
			return addPushTaskTrascation(task);
			
		}catch (ObjectOptimisticLockingFailureException e) {
			
			System.out.println("校验输入已存在ObjectOptimisticLockingFailureException");
			return addPushTaskTrascation(task);
		} catch(CannotAcquireLockException e){
			
			System.out.println("校验输入已存在CannotAcquireLockException");
			return addPushTaskTrascation(task);
		}catch (AssertionFailure e) {
			
			System.out.println("校验输入已存在AssertionFailure");
			return addPushTaskTrascation(task);
		}catch (Exception e) {
			
			throw e;
		}
		
	}
	
	/**
	 * 追加排期--保证事务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午2:49:29
	 * @param SwitchScheduleVO schedule 
	 */
	public void changeScheduleTrascation(SwitchScheduleVO schedule) throws Exception{
		
		try {
			
			Random random = new Random();
			Thread.sleep(random.nextInt(300) + 300);
			scheduleService.changeSchedule(schedule.getTaskId(), schedule.getMediaType(), schedule.getProgram());
			
		} catch (DataIntegrityViolationException e) {
			
			System.out.println("校验输入已存在DataIntegrityViolationException");
			changeScheduleTrascation(schedule);
			
		}catch (ObjectOptimisticLockingFailureException e) {
			
			System.out.println("校验输入已存在ObjectOptimisticLockingFailureException");
			changeScheduleTrascation(schedule);
		} catch(CannotAcquireLockException e){
			
			System.out.println("校验输入已存在CannotAcquireLockException");
			changeScheduleTrascation(schedule);
		}catch (AssertionFailure e) {
			
			System.out.println("校验输入已存在AssertionFailure");
			changeScheduleTrascation(schedule);
		}catch (JpaSystemException e) {
			
			System.out.println("校验输入已存在JpaSystemException");
			changeScheduleTrascation(schedule);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	/**
	 * 批量删除任务--保证事务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月6日 下午2:56:04
	 * @param List<String> taskUuids 任务标识s
	 */
	public void batchDeletePushTaskTranscation(List<String> taskUuids) throws Exception{
		
		try {
			
			Random random = new Random();
			Thread.sleep(random.nextInt(300) + 300);
			scheduleService.batchDeletePushTask(taskUuids);
			
		} catch (DataIntegrityViolationException e) {
			
			System.out.println("校验输入已存在DataIntegrityViolationException");
			batchDeletePushTaskTranscation(taskUuids);
		}catch (ObjectOptimisticLockingFailureException e) {
			
			System.out.println("校验输入已存在ObjectOptimisticLockingFailureException");
			batchDeletePushTaskTranscation(taskUuids);
		} catch(CannotAcquireLockException e){
			
			System.out.println("校验输入已存在CannotAcquireLockException");
			batchDeletePushTaskTranscation(taskUuids);
		}catch (AssertionFailure e) {
			
			System.out.println("校验输入已存在AssertionFailure");
			batchDeletePushTaskTranscation(taskUuids);
		}catch (Exception e) {
			
			throw e;
		}
		
	}
	
}
