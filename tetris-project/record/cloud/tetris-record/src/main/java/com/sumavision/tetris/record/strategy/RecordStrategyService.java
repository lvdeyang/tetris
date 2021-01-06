package com.sumavision.tetris.record.strategy;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.record.SpringBeanFactory;
import com.sumavision.tetris.record.device.DeviceDAO;
import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.file.RecordFileService;
import com.sumavision.tetris.record.storage.StorageDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EAutoFFMpegTranscode;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EAutoInject;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyStatus;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyType;
import com.sumavision.tetris.record.task.service.RecordCapacityService;
import com.sumavision.tetris.record.task.service.RecordTimerBO;
import com.sumavision.tetris.record.task.service.RecordTimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class RecordStrategyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordStrategyService.class);

	@Autowired
	private RecordStrategyDAO recordStrategyDAO;

	@Autowired
	private RecordStrategyItemDAO recordStrategyItemDAO;

	// @Autowired
	// private RecordInfoService recordInfoService;

	@Autowired
	private RecordTimerTask recordTimerTask;

	@Autowired
	private RecordStrategyService recordStrategyService;

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private RecordCapacityService recordCapacityService;

	@Autowired
	private DeviceDAO deviceDAO;

	@Autowired
	private StorageDAO storageDAO;

	@Autowired
	private RecordFileService recordFileService;

	/**
	 * 外部接口，提供页面行为操作
	 * 
	 * @param recordStrategyPO
	 * @throws Exception
	 * @throws ParseException
	 */
	public void startManualRecord(RecordStrategyPO recordStrategyPO) throws Exception, ParseException {

		if (recordStrategyPO == null) {
			LOGGER.error("RecordTimerTask startContinueRecord() error; recordStrategy is null");
			return;
		}

		Calendar calendar = Calendar.getInstance();
		Date recordTime = calendar.getTime();

		RecordTimerBO recordTimerBO = RecordTimerBO.transFromRecordStrategyPO(recordStrategyPO, deviceDAO, storageDAO);
		recordTimerBO.setOperateTime(recordTime);

		// 先创建收录能力上的任务，再处理其它
		recordCapacityService.addRecordTask(recordTimerBO);
		LOGGER.info("RecordTimerTask startContinueRecord(), addrecordTask return=" + recordTimerBO.getCapacityTaskId());

		if (StringUtils.isEmpty(recordTimerBO.getCapacityTaskId())) {
			// TODO 错误，处理
			LOGGER.error("RecordTimerTask startContinueRecord() error; addRecordTask return="
					+ recordTimerBO.getCapacityTaskId());
			return;
		}

		// 保存策略状态，新建recordFilePO
		updateStrategyAndRecordFile(recordStrategyPO, recordTimerBO, recordTime, true);

		/*
		 * if (null == sourcePO.getAnalyzeId()) {
		 * autoAnalyzeTimerTask.startAutoAnalyze(temp.getRecordInfoId()); }
		 */

		// 直接定时下一个停止时间定时器, 持续策略在每日24时自动分割，这里有优化空见
		// TODO 第一次在24时自动分割，第二次可以直接使用能力的机制来分割，更加准确？
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DATE, 1);
		Date operateTime = dateFormat.parse(dateFormat.format(calendar.getTime()));

		recordTimerBO.setOperateTime(operateTime);
		recordTimerBO.setStart(false);
		recordTimerBO.setCut(true);

		Timer timer = new Timer();
		TimerTask task = new RecordTimerTask(timer, recordTimerBO);
		timer.schedule(task, operateTime);

	}

	public void startScheduleTimerTask(RecordStrategyPO recordStrategyPO) throws Exception {
		addRecordTimerSchedule(recordStrategyPO, null, true);
	}

	/*
	 * @MethodName: startTimerTaskByRecovery
	 * 
	 * @Description: TODO 流恢复的时候启动录制 并返回录制的recordinfoId
	 * 
	 * @param sourceId 1
	 * 
	 * @param deviceIp 2
	 * 
	 * @Return: void
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/2 17:01
	 **/
	public void startSchedulerTimerTaskByRecovery(RecordStrategyPO recordStrategyPO) {

		Date operateTime = recordStrategyService.getStrategyNextTimePoint(recordStrategyPO, false);
		try {
			// 当前时间在自定义录制的某个任务内，开启录制
			RecordTimerBO recordTimerBO = RecordTimerBO.transFromRecordStrategyPO(recordStrategyPO, deviceDAO,
					storageDAO);

			// 创建任务
			recordCapacityService.addRecordTask(recordTimerBO);

			if (StringUtils.isEmpty(recordTimerBO.getCapacityTaskId())) {
				return;
			}

			// TODO recovery的处理
			recordStrategyPO.setCapacityTaskId(recordTimerBO.getCapacityTaskId());
			recordStrategyPO.setStatus(EStrategyStatus.RUNNING);
			recordStrategyDAO.save(recordStrategyPO);

			// 设置定时器任务并启动定时器
			recordTimerBO.setOperateTime(operateTime);
			recordTimerBO.setStart(false);

			Timer timer = new Timer();
			TimerTask task = new RecordTimerTask(timer, recordTimerBO);
			timer.schedule(task, operateTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加输入源或者添加策略时候调用，添加一个 开始录制的定时器
	 * 
	 * @param inputId
	 * @param deviceIp
	 * @throws CommonException
	 */
	private void addRecordTimerSchedule(RecordStrategyPO recordStrategyPO, Date opreateTimeParam, boolean isStart)
			throws Exception {

		Date operateTime;

		if (opreateTimeParam == null) {
			operateTime = recordStrategyService.getStrategyNextTimePoint(recordStrategyPO, isStart);

			if (null == operateTime) {
				System.out.println("无实际执行策略时间。不启动定时器");
				return;
			}

		} else {
			operateTime = opreateTimeParam;
		}

		RecordTimerBO recordTimerBO = RecordTimerBO.transFromRecordStrategyPO(recordStrategyPO, deviceDAO, storageDAO);
		recordTimerBO.setOperateTime(operateTime);
		recordTimerBO.setStart(true);

		Timer timer = RecordTimerTask.timerMap.get(recordStrategyPO.getId());

		if (null != timer) {
			timer.cancel();
			RecordTimerTask.timerMap.remove(recordStrategyPO.getId());
		}

		timer = new Timer();
		TimerTask task = new RecordTimerTask(timer, recordTimerBO);
		timer.schedule(task, operateTime);
	}

	public void stopContinueRecord(RecordStrategyPO recordStrategyPO) throws Exception {

		recordTimerTask.stopTimerTask(recordStrategyPO.getId());

		recordCapacityService.delRecordTask(recordStrategyPO.getCapacityTaskId());

		// 没必要再starttimerTask一次
		// TODO
		addRecordTimerSchedule(recordStrategyPO, null, true);

		// TODO 处理recordFileId
		List<RecordFilePO> recordFilePOs = recordFileDAO.findByRecordStrategyIdAndStatus(recordStrategyPO.getId(),
				ERecordFileStatus.RECORD_RUN);
		if (null == recordFilePOs || recordFilePOs.isEmpty()) {
			return;
		}

		RecordFilePO recordFilePO = recordFilePOs.get(0);
		recordFilePO.setStatus(ERecordFileStatus.RECORD_SUC);
		recordFilePO.setStopTime(new Date());
		recordFileDAO.save(recordFilePO);

		if (recordStrategyPO.getAutoInjectSel().equals(EAutoInject.AUTO_INJECT_MIMS)) {
			System.out.println("auto upload to mims");
			try {
				recordFileService.uploadMims(recordFilePO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("auto upload to mims exception");
			}
		}

		if (recordStrategyPO.getAutoFFMpegTranscode() != null
				&& recordStrategyPO.getAutoFFMpegTranscode().equals(EAutoFFMpegTranscode.AUTO_FFMPEG_TRANSCODE)) {
			LOGGER.info("auto ffmpeg transcode");
			try {
				recordFileService.startffMpegTrans(recordFilePO, recordStrategyPO);
			} catch (Exception e) {
				// TODO: handle exception
				LOGGER.info("uto ffmpeg transcode exception=" + e.getStackTrace());
			}
		}
	}

	public void delAllStgy(List<RecordStrategyPO> recordStrategyPOs) {

		for (RecordStrategyPO recordStrategyPO : recordStrategyPOs) {
			recordFileDAO.deleteByStgyId(recordStrategyPO.getId());
			recordStrategyItemDAO.deleteByRecordStrategyId(recordStrategyPO.getId());
			recordStrategyDAO.delete(recordStrategyPO);
		}
	}

	// TODO
	public void clearDeviceTask(Long deviceId) throws Exception {
		List<RecordStrategyPO> recordStrategyPOs = recordStrategyDAO.findByDeviceId(deviceId);

		if (CollectionUtils.isEmpty(recordStrategyPOs)) {
			return;
		}

		// 停止录制任务,清空定时任务
		for (RecordStrategyPO recordStrategyPO : recordStrategyPOs) {
			if (recordStrategyPO.getStatus().equals(EStrategyStatus.RUNNING)) {
				recordCapacityService.delRecordTask(recordStrategyPO.getCapacityTaskId());
			}
			recordTimerTask.stopTimerTask(recordStrategyPO.getId());

		}

		// 删除所有录制，删除策略
		delAllStgy(recordStrategyPOs);
	}

	// 增加一个从最近的半夜0点 切分录制的定时器任务
	public void addCutRecordTimerSchedule(RecordTimerBO recordTimerBO) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar now = Calendar.getInstance();
//			now.add(Calendar.DATE, 1);
			now.add(Calendar.DATE, 1);
			Date operateTime = dateFormat.parse(dateFormat.format(now.getTime()));
			LOGGER.info("跨天切换处理时间：" + operateTime);
			recordTimerBO.setOperateTime(operateTime);
			recordTimerBO.setCut(true);

			Timer timer = new Timer();
			TimerTask timerTask = new RecordTimerTask(timer, recordTimerBO);
			timer.schedule(timerTask, recordTimerBO.getOperateTime());
		} catch (Exception e) {
			LOGGER.error("定时器停止，录制任务终止:" + e.toString());
		}
	}

	/**
	 * 策略的下一个开始或结束时间点
	 */
	public Map<String, Date> getStrategyNextTimePoint(RecordStrategyPO recordStrategyPO) {

		// TODO 这里的逻辑需要细化
		if (RecordStrategyPO.EStrategyType.MANUAL == recordStrategyPO.getType()
				&& RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
			return null;
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		generateTimePoints(recordStrategyPO, timePoints);

		return findNextTimePoint(timePoints, currentTime);

	}

	/**
	 * 策略的下一个开始或结束时间点
	 */
	public Date getStrategyNextTimePoint(RecordStrategyPO recordStrategyPO, boolean isStart) {
		// TODO 考虑停止但未删除文件的遗留策略
		LOGGER.info("recordTimerService getStrategyNextTimePoint in");
		if (RecordStrategyPO.EStrategyType.MANUAL == recordStrategyPO.getType()
				&& RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
			return null;
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		// if (!isStart) {
		// 停止节点，那么立即停止
		// LOGGER.info("recordTimerService getStrategyNextTimePoint, isStart=false,
		// return time");

		// return Calendar.getInstance().getTime();
		// }

		generateTimePoints(recordStrategyPO, timePoints);

		return findNextTimePoint(timePoints, currentTime, isStart);
	}

	/*
	 * @MethodName:
	 * 
	 * @Description: TODO 由于策略的表结构更改,自定义策略思路更改，导致生成时间点方法更改。
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/3/15 18:13
	 **/
	public void generateTimePoints(RecordStrategyPO stgPO, List<Map<String, Date>> timePoints) {

		// 循环定时机制
		// TODO 待优化
		try {
			if (RecordStrategyPO.EStrategyType.CYCLE_SCHEDULE == stgPO.getType()) {
				JSONArray jsonPeriod = JSONArray.parseArray(stgPO.getRecordTimeSlotJson());
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDate = sdfDate.parse(stgPO.getStartDate());
				Date endDate = sdfDate.parse(stgPO.getEndDate());
				String[] loopCycles = stgPO.getLoopCycles().split(",");
				for (int i = 0; i < loopCycles.length; i++) {
					if (loopCycles[i].isEmpty()) {
						continue;
					}
					if (loopCycles[i].equals("7")) {
						while (startDate.before(endDate)) {
							for (int j = 0; j < jsonPeriod.size(); j++) {
								String startTime = jsonPeriod.getJSONObject(j).getString("startTime");
								String endTime = jsonPeriod.getJSONObject(j).getString("endTime");
								Map<String, Date> startPoint = new HashMap<>();
								Map<String, Date> endPoint = new HashMap<>();
								startPoint.put("s", sdfDateTime.parse(sdfDate.format(startDate) + " " + startTime));
								endPoint.put("e", sdfDateTime.parse(sdfDate.format(startDate) + " " + endTime));
								timePoints.add(startPoint);
								timePoints.add(endPoint);
							}
							startDate.setTime(startDate.getTime() + 24 * 60 * 60 * 1000);
						}
					} else {
						while (startDate.before(endDate)) {
							calendar.setTime(startDate);
							if (calendar.get(Calendar.DAY_OF_WEEK) - 1 == Integer.parseInt(loopCycles[i])) {
								for (int j = 0; j < jsonPeriod.size(); j++) {
									String startTime = jsonPeriod.getJSONObject(j).getString("startTime");
									String endTime = jsonPeriod.getJSONObject(j).getString("endTime");
									Map<String, Date> startPoint = new HashMap<>();
									Map<String, Date> endPoint = new HashMap<>();
									startPoint.put("s", sdfDateTime.parse(sdfDate.format(startDate) + " " + startTime));
									endPoint.put("e", sdfDateTime.parse(sdfDate.format(startDate) + " " + endTime));
									timePoints.add(startPoint);
									timePoints.add(endPoint);
								}
							}
							startDate.setTime(startDate.getTime() + 24 * 60 * 60 * 1000);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (RecordStrategyPO.EStrategyType.CUSTOM_SCHEDULE == stgPO.getType()) {
			List<RecordStrategyItemPO> itemPOS = recordStrategyItemDAO.findByRecordStrategyId(stgPO.getId());
			for (RecordStrategyItemPO itemPO : itemPOS) {
				Map<String, Date> startPoint = new HashMap<>();
				Map<String, Date> endPoint = new HashMap<>();
				startPoint.put("s", new Date(itemPO.getStartTime().getTime()));
				endPoint.put("e", new Date(itemPO.getStopTime().getTime()));
				timePoints.add(startPoint);
				timePoints.add(endPoint);
			}
		}

	}

	/**
	 * 获取所有录制时段的并集的下一个开始或结束时间点
	 */
	private Date findNextTimePoint(List<Map<String, Date>> timePoints, Date currentTime, boolean isStart) {
		List<Map<String, Date>> tpList = makeSumTimePoints(timePoints, currentTime);
		LOGGER.info("findNextTimePoint tpList=" + JSON.toJSONString(tpList));

		Map<String, Date> nextPoint = new HashMap<>();
		Date nextDate = null;
		boolean afterCurrent = false;

		for (int i = 0; i < tpList.size(); ++i) {
			if (tpList.get(i).containsKey("c")) {
				afterCurrent = true;
				continue;
			}

			if (afterCurrent) {
				if (!isStart) {
					if (tpList.get(i).containsKey("e")) {
						nextPoint = tpList.get(i);
						nextDate = nextPoint.get("e");
						break;
					}
				} else {
					if (tpList.get(i).containsKey("s")) {
						nextPoint = tpList.get(i);
						nextDate = nextPoint.get("s");
						break;
					}
				}
			}
		}

		return nextDate;
	}

	/**
	 * 生成一组时间点的并集
	 */
	private List<Map<String, Date>> makeSumTimePoints(List<Map<String, Date>> timePoints, Date currentTime) {
		Map<String, Date> currentPoint = new HashMap<>();
		currentPoint.put("c", currentTime);
		timePoints.add(currentPoint);
		timePoints.sort(new Comparator<Map<String, Date>>() {

			@Override
			public int compare(Map<String, Date> o1, Map<String, Date> o2) {
				Date date1 = null, date2 = null;
				if (o1.containsKey("s")) {
					date1 = o1.get("s");
				} else if (o1.containsKey("e")) {
					date1 = o1.get("e");
				} else if (o1.containsKey("c")) {
					date1 = o1.get("c");
				}

				if (o2.containsKey("s")) {
					date2 = o2.get("s");
				} else if (o2.containsKey("e")) {
					date2 = o2.get("e");
				} else if (o2.containsKey("c")) {
					date2 = o2.get("c");
				}

				if (date1.after(date2)) {
					return 1;
				}
				if (date1.before(date2)) {
					return -1;
				}
				return 0;
			}
		});

		Stack<Map<String, Date>> stack = new Stack<>();
		List<Map<String, Date>> tpList = new ArrayList<>();

		for (int i = 0; i < timePoints.size(); ++i) {
			if (timePoints.get(i).containsKey("s")) {
				if (stack.isEmpty()) {
					tpList.add(timePoints.get(i));
				}
				stack.push(timePoints.get(i));
			}
			if (timePoints.get(i).containsKey("e")) {
				stack.pop();
				if (stack.isEmpty()) {
					tpList.add(timePoints.get(i));
				}
			}
			if (timePoints.get(i).containsKey("c")) {
				tpList.add(timePoints.get(i));
			}
		}
		return tpList;
	}

	/**
	 * 获取所有录制时段的并集的下一个开始或结束时间点
	 */
	public Map<String, Date> findNextTimePoint(List<Map<String, Date>> timePoints, Date currentTime) {
		List<Map<String, Date>> tpList = makeSumTimePoints(timePoints, currentTime);

		Map<String, Date> nextPoint = new HashMap<>();
		boolean afterCurrent = false;

		for (int i = 0; i < tpList.size(); ++i) {
			if (tpList.get(i).containsKey("c")) {
				afterCurrent = true;
				continue;
			}

			if (afterCurrent) {
				nextPoint = tpList.get(i);
				return nextPoint;
			}
		}

		return null;
	}

	/*
	 * @MethodName: nowInScheduleStgItemPeriod
	 * 
	 * @Description: TODO 判断当前时间是否在定时策略的某一个任务段内
	 * 
	 * @param stgPO 自定义策略
	 * 
	 * @Return: java.lang.Boolean
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/2 18:01
	 **/
	public Boolean nowInScheduleStgItemPeriod(RecordStrategyPO stgPO) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String startTime = "";
		String endTime = "";
		try {
			JSONArray jsonPeriod = JSONArray.parseArray(stgPO.getRecordTimeSlotJson());
			Calendar calendar = Calendar.getInstance();
			Date startDate = sdfDate.parse(stgPO.getStartDate());
			Date endDate = sdfDate.parse(stgPO.getEndDate());
			String[] loopCycles = stgPO.getLoopCycles().split(",");
			for (int i = 0; i < loopCycles.length; i++) {
				if (loopCycles[i].isEmpty()) {
					continue;
				}
				if (loopCycles[i].equals("7")) {
					while (startDate.before(endDate)) {
						for (int j = 0; j < jsonPeriod.size(); j++) {
							startTime = jsonPeriod.getJSONObject(j).getString("startTime");
							endTime = jsonPeriod.getJSONObject(j).getString("endTime");
							Date start = sdfDateTime.parse(sdfDate.format(startDate) + " " + startTime);
							Date stop = sdfDateTime.parse(sdfDate.format(startDate) + " " + endTime);
							if (now.after(start) && now.before(stop)) {
								return true;
							}
						}
						startDate.setTime(startDate.getTime() + 24 * 60 * 60 * 1000);
					}
				} else {
					while (startDate.before(endDate)) {
						calendar.setTime(startDate);
						if (calendar.get(Calendar.DAY_OF_WEEK) - 1 == Integer.parseInt(loopCycles[i])) {
							for (int j = 0; j < jsonPeriod.size(); j++) {
								startTime = jsonPeriod.getJSONObject(j).getString("startTime");
								endTime = jsonPeriod.getJSONObject(j).getString("endTime");
								Date start = sdfDateTime.parse(sdfDate.format(startDate) + " " + startTime);
								Date stop = sdfDateTime.parse(sdfDate.format(startDate) + " " + endTime);
								if (now.after(start) && now.before(stop)) {
									return true;
								}
							}
						}
						startDate.setTime(startDate.getTime() + 24 * 60 * 60 * 1000);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * @MethodName: nowInCustomStgItemPeriod
	 * 
	 * @Description: TODO 判断当前时间是否在自定义策略的某一个任务段内
	 * 
	 * @param stgPO 1
	 * 
	 * @Return: java.lang.Boolean
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/2 18:41
	 **/
	public Boolean nowInCustomStgItemPeriod(RecordStrategyPO stgPO) {
		Date now = new Date();
		List<RecordStrategyItemPO> recordStgItemPOS = recordStrategyItemDAO.findByRecordStrategyId(stgPO.getId());
		if (null == recordStgItemPOS || recordStgItemPOS.isEmpty()) {
			return false;
		}
		for (RecordStrategyItemPO recordStgItemPO : recordStgItemPOS) {
			if (now.after(recordStgItemPO.getStartTime()) && now.before(recordStgItemPO.getStopTime())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * @Description: TODO 删除策略：删除对应的数据库记录（stg,stgItem,info,file）和录制的磁盘文件
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/10 14:15
	 **/
	@Transactional(rollbackFor = IOException.class) // 回滚异常默认会包含RuntimeException
	public void delStgContainRecordAndFile(Long stgId) throws IOException {
		// 删磁盘文件,一定保证file里的infoid不为空

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		RecordFileDAO recordFileDao = SpringBeanFactory.getBean(RecordFileDAO.class);

		// TODO删除文件

		// 删recordfile记录
		recordFileDao.deleteByStgyId(stgId);
		recordStrategyItemDAO.deleteByRecordStrategyId(stgId);
		recordStrategyDAO.delete(stgId);
	}

	public void stopStrategyRecord(RecordStrategyPO stgPO) throws Exception {
		// 没有运行无需停止
		if (stgPO.getStatus() != EStrategyStatus.RUNNING) {
			return;
		}

		if (stgPO.getType() == EStrategyType.MANUAL) {
			stopRecordOfManualStg(stgPO);
		}
		if (stgPO.getType() == EStrategyType.CYCLE_SCHEDULE) {
			stopRecordOfScheduleStg(stgPO);
		}
		if (stgPO.getType() == EStrategyType.CUSTOM_SCHEDULE) {
			stopRecordOfCustomStg(stgPO);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void stopRecordOfManualStg(RecordStrategyPO stgPO) throws Exception {
		stopContinueRecord(stgPO);
	}

	public void stopRecordOfScheduleStg(RecordStrategyPO stgPO) throws Exception {
		stgPO.setStatus(RecordStrategyPO.EStrategyStatus.STOP);
		recordStrategyDAO.save(stgPO);
		stopContinueRecord(stgPO);
		// parseStrategyTimerTask.stopStgyRecord(stgPO.getId());
	}

	public void stopRecordOfCustomStg(RecordStrategyPO stgPO) throws Exception {
		stopRecordOfScheduleStg(stgPO);
	}

	public void updateStgRecordForDeleteStg(RecordStrategyPO stgPO) {
		stgPO.setDelStatus(1);
		recordStrategyDAO.save(stgPO);
	}

	public void updateStrategyAndRecordFile(RecordStrategyPO recordStrategyPO, RecordTimerBO recordTimerBOTemp,
			Date operateTime, boolean isStartRecord) {

		if (recordStrategyPO == null) {
			recordStrategyPO = recordStrategyDAO.findOne(recordTimerBOTemp.getRecordStrategyId());
		}

		if (isStartRecord) {
			recordStrategyPO.setStatus(EStrategyStatus.RUNNING);
			recordStrategyPO.setCapacityTaskId(recordTimerBOTemp.getCapacityTaskId());

			// 记录 recordFilePO
			// TODO 缺
			RecordFilePO recordFilePO = new RecordFilePO();
			recordFilePO.setStartTime(operateTime);
			recordFilePO.setRecordStrategyId(recordTimerBOTemp.getRecordStrategyId());
			recordFilePO.setStatus(ERecordFileStatus.RECORD_RUN);
			recordFilePO.setFilePath(recordTimerBOTemp.getFilePath());
			recordFilePO.setStorageId(recordStrategyPO.getStorageId());

			// TODO set预览路径，通过record.xml??
			recordFileDAO.save(recordFilePO);

		} else {

			recordStrategyPO.setStatus(EStrategyStatus.STOP);

			List<RecordFilePO> recordFilePOs = recordFileDAO.findByRecordStrategyIdAndStatus(recordStrategyPO.getId(),
					ERecordFileStatus.RECORD_RUN);
			if (null == recordFilePOs || recordFilePOs.isEmpty()) {
				return;
			}

			// TODO
			// recordFilePOs 不应该大于1

			RecordFilePO recordFilePO = recordFilePOs.get(0);
			recordFilePO.setStatus(ERecordFileStatus.RECORD_SUC);
			recordFilePO.setStopTime(operateTime);
			recordFileDAO.save(recordFilePO);

			if (recordStrategyPO.getAutoInjectSel().equals(EAutoInject.AUTO_INJECT_MIMS)) {
				System.out.println("auto upload to mims");
				try {
					recordFileService.uploadMims(recordFilePO);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("auto upload to mims exception");
				}
			}

		}

		recordStrategyDAO.save(recordStrategyPO);

	}

	public void save(RecordStrategyPO po) throws Exception {
		List<RecordStrategyPO> pos = recordStrategyDAO.findByNameAndSourceIdAndDelStatusNot(po.getName(),
				po.getSourceId(), 1);
		if (pos != null && !pos.isEmpty()) {
			throw new Exception("已存在相同的名称");
		}
		recordStrategyDAO.save(po);
	}

}
