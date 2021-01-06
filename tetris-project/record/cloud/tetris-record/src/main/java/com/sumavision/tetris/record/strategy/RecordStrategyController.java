package com.sumavision.tetris.record.strategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mims.app.media.MediaQuery;
import com.sumavision.tetris.record.device.DeviceDAO;
import com.sumavision.tetris.record.device.DevicePO;
import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.source.SourceVO;
import com.sumavision.tetris.record.storage.StorageDAO;
import com.sumavision.tetris.record.storage.StoragePO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EAutoInject;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyStatus;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyType;
import com.sumavision.tetris.user.UserQuery;

@Controller
@RequestMapping(value = "/record/strategy")
public class RecordStrategyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordStrategyController.class);

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private RecordStrategyDAO recordStrategyDAO;

	@Autowired
	private RecordStrategyService recordStragegyService;

	@Autowired
	private RecordStrategyItemDAO recordStrategyItemDAO;

	// @Autowired
	// private RecordTimerTask recordTimerTask;

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private DeviceDAO deviceDAO;

	@Autowired
	private StorageDAO storageDAO;

	@Autowired
	private MediaQuery mediaQuery;

	// @Autowired
	// private ParseStrategyService parseStrategyService;

	/**
	 * 查询现有收录任务列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryStrategy")
	public Object queryStrategy(Integer pageIndex, Integer pageSize) {

		Map<String, Object> data = new HashMap<String, Object>();

		Pageable pageable = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id");
		try {
			Page<RecordStrategyPO> recordStrategyPOPage = recordStrategyDAO.findAll(pageable);
			data.put("errMsg", "");
			data.put("totalNum", recordStrategyPOPage.getTotalElements());
			data.put("recordStrategyVOs", RecordStrategyVO.fromPOList(recordStrategyPOPage.getContent()));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;

	}

	@ResponseBody
	@RequestMapping(value = "/queryAll")
	public Object queryStrategy() {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<RecordStrategyPO> recordStrategyPOList = recordStrategyDAO.findAll();
			data.put("errMsg", "");
			data.put("recordStrategyVOs", RecordStrategyVO.fromPOList(recordStrategyPOList));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;

	}

	@RequestMapping("/queryStrategyItems")
	@ResponseBody
	public Object queryStrategyItems(Long strategyId) {

		Map<String, Object> data = new HashMap<String, Object>();

		if (strategyId == null || strategyId == 0) {
			data.put("errMsg", "参数错误");
			return data;
		}

		try {
			List<RecordStrategyItemPO> recordStrategyItemPOs = recordStrategyItemDAO.findByRecordStrategyId(strategyId);
			data.put("recordStrategyItemVOs", recordStrategyItemPOs);

		} catch (Exception e) {
			data.put("errMsg", "内部错误");
		}

		data.put("errMsg", "");
		return data;

	}

	@RequestMapping("/querySourceFromMims")
	@ResponseBody
	public Object querySourceFromMims() {
		Map<String, Object> data = new HashMap<String, Object>();

		// TODO
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		try {
			jsonList = mediaQuery.queryByCondition(null, null, "videoStream", null, null, null);
		} catch (Exception e1) {
			e1.printStackTrace();
			data.put("errMsg", "内部错误");
			return data;
		}

		List<SourceVO> sourceVos = new ArrayList<SourceVO>();

		for (int i = 0; i < jsonList.size(); i++) {
			SourceVO vo = JSONObject.toJavaObject(jsonList.get(i), SourceVO.class);
			sourceVos.add(vo);
		}

		if (CollectionUtils.isEmpty(sourceVos)) {
			data.put("errMsg", "找不到源");
		}

		data.put("sourceFeignVOs", sourceVos);
		data.put("errMsg", "");

		return data;
	}

	@RequestMapping("/addRecordStrategy")
	@ResponseBody
	public Map<String, Object> addRecordStrategy(@RequestParam(value = "id", required = false) Long stgId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "status", required = false) String statusStr,
			@RequestParam(value = "type") String typeStr,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "loopCycles", required = false) String loopCycles,
			@RequestParam(value = "taskListJson", required = false) String taskListJson,
			@RequestParam(value = "recordTimeSlotJson", required = false) String recordTimeSlotJson,
			@RequestParam(value = "delStgItemId", required = false) String delStgItemId,
			@RequestParam(value = "sourceId", required = false) String sourceId,
			@RequestParam(value = "sourceName", required = false) String sourceName,
			@RequestParam(value = "sourceUrl") String sourceUrl, @RequestParam(value = "sourceType") String sourceType,
			@RequestParam(value = "deviceId", required = false) Long deviceId,
			@RequestParam(value = "storageId", required = false) Long storageId,
			@RequestParam(value = "manualStrategyStart", required = false) boolean manualStgStart,
			@RequestParam(value = "autoInjectToMims", required = false) boolean autoInjectToMims) {

		Map<String, Object> data = new HashMap<String, Object>();
		if (name == "") {
			data.put("errMsg", "参数错误");
			return data;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		RecordStrategyPO stgPo = null;
		RecordStrategyPO stgPoBackUp = new RecordStrategyPO(); // 修改出异常需要回滚，暂存修改前的stg

		try {
			// 新增/修改策略，保存数据库
			if (stgId == null) {
				stgPo = new RecordStrategyPO();
				stgPo.setName(name);
				stgPo.setSourceId(sourceId);
				stgPo.setSourceUrl(sourceUrl);
				stgPo.setSourceType(sourceType);
				stgPo.setSourceName(sourceName);
				stgPo.setDeviceId(deviceId);
				// stgPo.setStartTime(startTime);
				stgPo.setStatus(EStrategyStatus.fromString(statusStr));
				stgPo.setType(EStrategyType.fromString(typeStr));
				stgPo.setStartDate(startDate);
				stgPo.setEndDate(endDate);
				stgPo.setLoopCycles(loopCycles);
				stgPo.setRecordTimeSlotJson(recordTimeSlotJson);
				String now = sdf.format(new Date());
				stgPo.setCreateTime(now);

				if (deviceId == null || deviceId == 0) {

					List<DevicePO> devicePOs = deviceDAO.findAll();

					if (CollectionUtils.isEmpty(devicePOs)) {
						data.put("errMsg", "没有录制设备");
						return data;
					}

					deviceId = devicePOs.get(0).getId();
				}

				stgPo.setDeviceId(deviceId);

				if (storageId == null || storageId == 0) {

					List<StoragePO> storagePOs = storageDAO.findAll();
					if (CollectionUtils.isEmpty(storagePOs)) {
						data.put("errMsg", "没有配置仓库");
						return data;
					}

					storageId = storagePOs.get(0).getId();
				}

				stgPo.setStorageId(storageId);

				// stgPo.setCreater(username);
				// stgPo.setUpdater(username);

				if (autoInjectToMims) {
					stgPo.setAutoInjectSel(EAutoInject.AUTO_INJECT_MIMS);
				} else {
					stgPo.setAutoInjectSel(EAutoInject.MANUAL);
				}

				recordStragegyService.save(stgPo);

			} else {
				// 定时或自定义策略不能修改有策略项在录制中的策略，策略项录制完才能改
				if (typeStr.equals(EStrategyType.CUSTOM_SCHEDULE.toString())
						|| typeStr.equals(EStrategyType.CYCLE_SCHEDULE.toString())) {
					List<RecordFilePO> recordFilePOS = recordFileDAO.findByRecordStrategyIdAndStatus(stgId,
							ERecordFileStatus.RECORD_RUN);
					if (null != recordFilePOS && !recordFilePOS.isEmpty()) {
						data.put("errMsg", "该策略存在正在录制的任务，请录制完再修改策略");
						return data;
					}
				}

				stgPo = recordStrategyDAO.findOne(stgId);
				stgPoBackUp = stgPo;
				if (typeStr.equals(EStrategyType.CYCLE_SCHEDULE.toString())) {
					stgPo.setStartDate(startDate);
					stgPo.setEndDate(endDate);
					stgPo.setLoopCycles(loopCycles);
					stgPo.setRecordTimeSlotJson(recordTimeSlotJson);
				}
				stgPo.setName(name);
				stgPo.setUpdateTime(Calendar.getInstance().getTime());
				// stgPo.setUpdater(username);
				recordStragegyService.save(stgPo);
			}
		} catch (Exception e) {
			data.put("errMsg", e.getMessage());
			return data;
		}

		data.put("id", stgPo.getId());

		// TODO 上报用户操作日志

		// 启动录制定时器，向能力发录制请求
		try {
			if (stgPo.getType().equals(EStrategyType.MANUAL) && manualStgStart) {
				recordStragegyService.startManualRecord(stgPo);
			} else {
				if (!taskListJson.isEmpty()) {
					JSONArray jsonArray = JSONArray.parseArray(taskListJson);
					for (int i = 0; i < jsonArray.size(); i++) {
						Long stgItemId = null;
						if (stgPo.getType() == EStrategyType.CUSTOM_SCHEDULE) {
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
					//
					if (null != stgId) {
						String[] strings = delStgItemId.split(",");
						for (int i = 0; i < strings.length; i++) {
							if (!strings[i].isEmpty()) {
								recordStrategyItemDAO.delete(Long.parseLong(strings[i]));
							}
						}
					}
				}

				// 更新当前策略
				recordStragegyService.startScheduleTimerTask(stgPo);
			}

		} catch (Exception e1) {
			if (null == stgId) {// 新增出异常，删除策略
				recordStrategyDAO.delete(stgPo.getId());
				recordStrategyItemDAO.deleteByRecordStrategyId(stgPo.getId());
			} else {
				recordStrategyDAO.save(stgPoBackUp);
			}

			data.put("errMsg", e1.toString());
		}

		data.put("errMsg", "");
		return data;
	}

	/**
	 * 
	 * 
	 * @param recordStrategyId
	 * @param delDiskFile
	 * @return
	 */
	@RequestMapping("/delRecordStrategy")
	@ResponseBody
	public Object delRecordStrategy(@RequestParam(value = "recordStrategyId") Long recordStrategyId,
			@RequestParam(value = "delDiskFile") Boolean delDiskFile) {

		Map<String, Object> data = new HashMap<String, Object>();
		// TODO check running status
		try {
			RecordStrategyPO stgPO = recordStrategyDAO.findOne(recordStrategyId);

			// 停止录制
			recordStragegyService.stopStrategyRecord(stgPO);

			// 删除策略
			if (delDiskFile) {
				recordStragegyService.delStgContainRecordAndFile(recordStrategyId);
			} else {
				recordStragegyService.updateStgRecordForDeleteStg(stgPO);
			}

			// TODO 发送操作日志

		} catch (Exception e) {
			data.put("errMsg", "策略删除失败");
			e.printStackTrace();
		}

		data.put("errMsg", "");
		return data;
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
					.findByIdAndRecordStrategyIdAndStartTimeAndStopTime(id, recordStgId, startTime, stopTime);
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

		// RecordStrategyPO recordStrategyPO = recordStrategyDAO.findOne(recordStgId);

		if (isModify) {

		} else {
			// TODO
			// 发送操作日志
			// LoggerSend.oprlog(username, "40020303", "为输入源 " + sourcePO.getName() + " 中 "
			// + recordStrategyPO.getName()
			// + "策略添加录制时段 " + stgItemPO.getName() + "，时间范围为：" + stTime + " - " + spTime);
		}
	}

	/**
	 * @param recordStrategyId
	 * @return
	 */
	@RequestMapping("/startRecord")
	@ResponseBody
	public Map<String, Object> startRecord(@RequestParam(value = "recordStrategyId") Long recordStrategyId) {

		Map<String, Object> data = new HashMap<>();
		try {
			RecordStrategyPO recordStrategyPO = recordStrategyDAO.findOne(recordStrategyId);
			recordStragegyService.startManualRecord(recordStrategyPO);

			// TODO 操作日志

		} catch (Exception e) {
			LOGGER.error("recordStrategyController startRecord() error=", e);
			data.put("errMsg", "内部错误");
		}

		data.put("errMsg", "");
		return data;
	}

	/**
	 * @param recordStrategyId
	 * @return
	 */
	@RequestMapping("/stopRecord")
	@ResponseBody
	public Map<String, Object> stopRecord(@RequestParam(value = "recordStrategyId") Long recordStrategyId) {
		Map<String, Object> data = new HashMap<>();
		try {
			RecordStrategyPO recordStrategyPO = recordStrategyDAO.findOne(recordStrategyId);
			recordStragegyService.stopRecordOfManualStg(recordStrategyPO);

			recordStrategyPO.setStatus(EStrategyStatus.STOP);
			recordStrategyDAO.save(recordStrategyPO);
			// TODO 操作日志

		} catch (Exception e) {
			LOGGER.error("recordStrategyController startRecord() error=", e);
			data.put("errMsg", "内部错误");
		}

		data.put("errMsg", "");
		return data;
	}

}
