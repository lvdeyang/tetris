package com.sumavision.tetris.record.stragegy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.source.SourceDAO;
import com.sumavision.tetris.record.source.SourcePO;
import com.sumavision.tetris.record.stragegy.RecordStrategyPO.EStrategyStatus;
import com.sumavision.tetris.record.stragegy.RecordStrategyPO.EStrategyType;
import com.sumavision.tetris.record.task.handler.RecordTimerTask;
import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/record/task")
public class RecordStrategyController {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private RecordStrategyDAO recordStrategyDAO;

	@Autowired
	private RecordStrategyService recordstragegyService;

	@Autowired
	private RecordStrategyItemDAO recordStrategyItemDAO;

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private RecordTimerTask recordTimerTask;

	@Autowired
	private ParseStrategyService parseStrategyService;

	@Autowired
	private ParseStrategyTimerTask parseStrategyTimerTask;

	@Autowired
	private SourceDAO sourceDAO;

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

	@RequestMapping("/addRecordStragegy")
	@ResponseBody
	public Object addRecordStragegy(@RequestParam(value = "id") Long stgId, @RequestParam(value = "name") String name,
			@RequestParam(value = "loopPeriod") Long lpPeriod, @RequestParam(value = "loopCnt") Integer loopCnt,
			@RequestParam(value = "startTime") String stTime, @RequestParam(value = "status") String statusStr,
			@RequestParam(value = "type") String typeStr, @RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate, @RequestParam(value = "loopCycles") String loopCycles,
			@RequestParam(value = "taskListJson") String taskListJson,
			@RequestParam(value = "recordTimeSlotJson") String recordTimeSlotJson,
			@RequestParam(value = "delStgItemId") String delStgItemId,
			@RequestParam(value = "sourceId") String sourceId) {

		Map<String, Object> data = new HashMap<String, Object>();
		if (sourceId == null || name == "") {
			data.put("errMsg", "参数错误");
			return data;
		}

		if (stgId == null && typeStr.equals(EStrategyType.CONTINUOUS.toString())) {
			List<RecordStrategyPO> continuousStgs = recordStrategyDAO
					.findByTypeAndSourceIdAndDelStatusNot(EStrategyType.CONTINUOUS, sourceId, 1);
			if (continuousStgs != null && !continuousStgs.isEmpty()) {
				data.put("errMsg", "该输入源已存在持续录制策略");
				return data;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		try {
			if (stTime != null && !stTime.equals("")) {
				startTime = sdf.parse(stTime);
			}
		} catch (ParseException e) {
			// logger.info("[add record strategy]: startTime is invalid");
			data.put("errMsg", "时间格式错误");
			return data;
		}
		RecordStrategyPO stgPo = null;
		RecordStrategyPO stgPoBackUp = new RecordStrategyPO(); // 修改出异常需要回滚，暂存修改前的stg
		boolean isModify = false;
		String orgName = null;
		try {
			// 新增/修改策略，保存数据库
			if (stgId == null) {
				stgPo = new RecordStrategyPO();
				// stgPo.setLoopCnt(loopCnt);
				// stgPo.setLoopPeriod(lpPeriod);
				stgPo.setName(name);
				stgPo.setSourceId(sourceId);
				// stgPo.setStartTime(startTime);
				stgPo.setStatus(EStrategyStatus.fromString(statusStr));
				stgPo.setType(EStrategyType.fromString(typeStr));
				stgPo.setStartDate(startDate);
				stgPo.setEndDate(endDate);
				stgPo.setLoopCycles(loopCycles);
				stgPo.setRecordTimeSlotJson(recordTimeSlotJson);
				String now = sdf.format(new Date());
				stgPo.setCreateTime(now);
				// stgPo.setCreater(username);
				// stgPo.setUpdater(username);
				recordstragegyService.save(stgPo);
			} else {
				isModify = true;
				// 定时或自定义策略不能修改有策略项在录制中的策略，策略项录制完才能改
				if (typeStr.equals(EStrategyType.CUSTOM.toString())
						|| typeStr.equals(EStrategyType.SCHEDULE.toString())) {
					List<RecordFilePO> recordFilePOS = recordFileDAO.findByStrategyIdAndStatus(stgId, 1);
					if (null != recordFilePOS && !recordFilePOS.isEmpty()) {
						data.put("errMsg", "该策略存在正在录制的任务，请录制完再修改策略");
						return data;
					}
				}

				stgPo = recordStrategyDAO.findOne(stgId);
				stgPoBackUp = stgPo;
				if (typeStr.equals(EStrategyType.SCHEDULE.toString())) {
					stgPo.setStartDate(startDate);
					stgPo.setEndDate(endDate);
					stgPo.setLoopCycles(loopCycles);
					stgPo.setRecordTimeSlotJson(recordTimeSlotJson);
				}
				stgPo.setName(name);
				stgPo.setUpdateTime(Calendar.getInstance().getTime());
				// stgPo.setUpdater(username);
				recordstragegyService.save(stgPo);
			}
		} catch (Exception e) {
			data.put("errMsg", e.getMessage());
			return data;
		}
		data.put("id", stgPo.getId());

		SourcePO sourcePO = sourceDAO.findOne(Long.valueOf(sourceId));

		// TODO 上报用户操作日志

		// 启动录制定时器，向能力发录制请求
		try {
			if (stgPo.getType() == EStrategyType.CONTINUOUS) {
				recordTimerTask.startContinueRecord(sourceId);
				parseStrategyService.createContinueStgyItem(stgPo.getId());
			}

			if (!taskListJson.isEmpty()) {
				JSONArray jsonArray = JSONArray.parseArray(taskListJson);
				for (int i = 0; i < jsonArray.size(); i++) {
					Long stgItemId = null;
					if (stgPo.getType() == EStrategyType.CUSTOM) {
						if (!"undefined".equals(jsonArray.getJSONObject(i).getString("id"))
								&& !jsonArray.getJSONObject(i).getString("id").isEmpty()) {
							stgItemId = Long.parseLong(jsonArray.getJSONObject(i).getString("id"));
						}
						String recordDate = jsonArray.getJSONObject(i).getString("recordDate");
						String start = jsonArray.getJSONObject(i).getString("startTime");
						String end = jsonArray.getJSONObject(i).getString("endTime");
						saveStrategyItem(stgItemId, "1", stgPo.getId(), recordDate + " " + start,
								recordDate + " " + end);
					}
				}
				// 修改的时候，可能要删除部分策略
				if (null == stgId) {
					String[] strings = delStgItemId.split(",");
					for (int i = 0; i < strings.length; i++) {
						if (!strings[i].isEmpty()) {
							recordStrategyItemDAO.delete(Long.parseLong(strings[i]));
						}
					}
				}
				// 更新当前策略，自定义策略优先级低于手动和持续，不会影响到手动和持续
				recordTimerTask.startScheduleTimerTask(sourcePO.getId(), sourcePO.getLocalIp());
				// 自定义策略和定时策略虽然录制了，但是没保存recordfile，开启策略定时器保存recordFile
				parseStrategyTimerTask.startTimerTask(stgPo.getId());
			}
		} catch (Exception e1) {
			if (null == stgId) {// 新增出异常，删除策略
				recordStrategyDAO.delete(stgPo.getId());
				recordStrategyItemDAO.deleteByRecordStgId(stgPo.getId());
			} else {
				recordStrategyDAO.save(stgPoBackUp);
			}
			data.put("errMsg", e1.toString());
		}

		return data;
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

	public void saveStrategyItem(Long id, String name, Long recordStgId, String stTime, String spTime)
			throws Exception {
		// String username = CASUserAttr.getLoginUserAttr("name");
		Map<String, Object> data = new HashMap<String, Object>();
		if (recordStgId == null || name == "") {
			throw new Exception("参数错误");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null, stopTime = null;
		try {
			if (null != stTime && !stTime.equals("")) {
				startTime = sdf.parse(stTime);
			}
			if (null != spTime && !spTime.equals("")) {
				stopTime = sdf.parse(spTime);
			}
		} catch (ParseException e) {
			// logger.info("[add record stgItem]: startTime/stopTime is invalid");
			throw new Exception("时间格式错误");
		}

		RecordStrategyItemPO stgItemPO = null;
		boolean isModify = false;
		if (id != null) {
			isModify = true;
			// 全都一样就不用更新了pos != null && !pos.isEmpty()
			List<RecordStrategyItemPO> recordStgItemPOS = recordStrategyItemDAO
					.findByIdAndRecordStgIdAndStartTimeAndStopTime(id, recordStgId, startTime, stopTime);
			if (recordStgItemPOS != null && !recordStgItemPOS.isEmpty()) {
				return;
			}
			stgItemPO = recordStrategyItemDAO.findOne(id);
			stgItemPO.setStartTime(startTime);
			stgItemPO.setStopTime(stopTime);
			recordStrategyItemDAO.save(stgItemPO);
		} else {
			stgItemPO = new RecordStrategyItemPO();
			stgItemPO.setName(name);
			stgItemPO.setRecordStrategyId(recordStgId);
			stgItemPO.setStartTime(startTime);
			stgItemPO.setStopTime(stopTime);
			recordStrategyItemDAO.save(stgItemPO);
		}
		data.put("id", stgItemPO.getId());
		RecordStrategyPO recordStrategyPO = recordStrategyDAO.findOne(recordStgId);
		SourcePO sourcePO = sourceDAO.findOne(Long.valueOf(recordStrategyPO.getSourceId()));
		if (isModify) {

		} else {
			// TODO
			// 发送操作日志
			// LoggerSend.oprlog(username, "40020303", "为输入源 " + sourcePO.getName() + " 中 "
			// + recordStrategyPO.getName()
			// + "策略添加录制时段 " + stgItemPO.getName() + "，时间范围为：" + stTime + " - " + spTime);
		}
	}

}
