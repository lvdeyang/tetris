package com.sumavision.tetris.record.task.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.record.SpringBeanFactory;
import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;
import com.sumavision.tetris.record.strategy.RecordStrategyService;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyStatus;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyType;

@Service
public class RecordTimerTask extends TimerTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordTimerTask.class);

	@Autowired
	private RecordStrategyService recordStrategyService;

	// @Autowired
	// private ParseStrategyService parseStrategyService;

	@Autowired
	private RecordStrategyDAO recordStrategyDAO;

	@Autowired
	private RecordCapacityService recordCapacityService;

	@Autowired
	private RecordFileDAO recordFileDAO;

	public static Map<Long, Timer> timerMap = new HashMap<Long, Timer>();

	/**
	 * 针对外部使用，因为本类中，存在线程内部变量和service自身变量
	 */
	public static Map<Long, RecordTimerBO> RecordTimerBOMap = new HashMap<Long, RecordTimerBO>();

	/**
	 * 定时器内部使用的变量
	 */
	private RecordTimerBO recordTimerBOInside;

	// private DeviceDAO deviceDAO;

	/*
	 * public void resetTimerTask(Long inputId, String deviceIp) throws Exception {
	 * this.stopTimerTask(inputId); this.startTimerTask(inputId, deviceIp); }
	 */

	public void rebootTimerTask(RecordStrategyPO recordStrategyPO) throws ParseException, Exception {
		this.startTimerTaskByReboot(recordStrategyPO);
	}

	public void stopTimerTask(Long recordStrategyId) {
		try {
			Timer timer = timerMap.get(recordStrategyId);
			if (null != timer) {
				timerMap.get(recordStrategyId).cancel();
				timerMap.remove(recordStrategyId);
				LOGGER.info("停止定时器任务:" + recordStrategyId);
			}
		} catch (Exception e) {
			LOGGER.error("停止定时器任务异常:" + recordStrategyId + e.toString());
		}
	}

	/**
	 * 系统重重启时，启动定时任务（开启录制或者停止录制定时任务）,这里只负责持续策略的重启
	 * 
	 * @param inputId
	 * @param deviceIp
	 * @throws CommonException
	 */
	private void startTimerTaskByReboot(RecordStrategyPO recordStrategyPO) throws Exception, ParseException {

		Map<String, Date> nextTimeMap = recordStrategyService.getStrategyNextTimePoint(recordStrategyPO);

		if (CollectionUtils.isEmpty(nextTimeMap) || !nextTimeMap.containsKey("s")) {
			// 无下一个动作时间点，保持当前动作不变，不加载任何定时任务
			System.out.println("无下一个执行点。不启动定时器");
			return;
		}

		// 手动策略的操作，如果系统启动时候，手动策略状态是录制，则应该先停止，把各种状态置好，再重启任务
		if (recordStrategyPO.getType().equals(EStrategyType.MANUAL)
				&& recordStrategyPO.getStatus().equals(EStrategyStatus.RUNNING)) {
			recordStrategyService.startManualRecord(recordStrategyPO);
		}

		// Date operateTime =
		// recordStrategyService.getNextTimePointByReboot(recordStrategyPO);
		// if (null == operateTime) {
		// System.out.println("无实际执行策略时间。不启动定时器");
		// return;
		// }

		// RecordTimerBO recordTimerBO =
		// RecordTimerBO.transFromRecordStrategyPO(recordStrategyPO);
		// recordTimerBO.setOperateTime(operateTime);
		// recordTimerBO.setStart(isStart);

		// Timer timer = new Timer();
		// TimerTask task = new RecordTimerTask(timer, recordTimerBO);
		// timer.schedule(task, operateTime);

	}

	@Override
	public void run() {
		LOGGER.info("run in=" + JSONObject.toJSONString(recordTimerBOInside));
		LOGGER.info("timerMap size=" + timerMap.size());

		// 执行定时器任务
		this.executeCurrentTimerTask();

		this.changeObjectStatus();

		// 启动下一个定时器任务
		this.setNextTimerTask(recordTimerBOInside);
	}

	private void changeObjectStatus() {
		if (recordTimerBOInside.isCut()) {
			return;
		}
		// 改变下一定时任务启动停止类型
		recordTimerBOInside.setStart(!recordTimerBOInside.isStart());
	}

	private void setNextTimerTask(RecordTimerBO recordTimerBO) {
		LOGGER.info("setNextTimerTask nextTime in");

		RecordStrategyPO recordStrategyPO = recordStrategyDAO.findOne(recordTimerBO.getRecordStrategyId());

		if (recordStrategyPO == null) {
			return;
		}

		Date nextTime = recordStrategyService.getStrategyNextTimePoint(recordStrategyPO, recordTimerBO.isStart());
		LOGGER.info(
				"setNextTimerTask nextTime=" + JSONObject.toJSONStringWithDateFormat(nextTime, "yyyy-MM-dd hh:mm:ss"));

		if (null == nextTime) {
			if (recordTimerBO.isStart()) {
				// 停止当前所有正在解析的策略

				stopTimerTask(recordTimerBO.getRecordStrategyId());
				// try {
				// List<RecordFilePO> recordFilePOs =
				// recordFileDAO.findByRecordStrategyIdAndStatus(
				// recordTimerBO.getRecordStrategyId(), ERecordFileStatus.RECORD_RUN);

				// if (null != recordFilePOs && !recordFilePOs.isEmpty()) {
				// RecordFilePO recordFilePO = recordFilePOs.get(0);

				// recordFilePO.setStopTime(new Date());
				// recordFilePO.setStatus(ERecordFileStatus.RECORD_SUC);
				// recordFileDAO.save(recordFilePO);
				// }
				// } catch (Exception e) {
				// LOGGER.error("停止策略录制条目任务异常:" + recordTimerBO.getRecordStrategyId());
				// }

			} else {
				// 从策略PO查询找不到下一个操作时间点，但是并且下一个操作点是停止的情况，基本就是似手动录制
				// 那就只终止录制定时器的加载，不干预策略定时器
				recordStrategyService.addCutRecordTimerSchedule(recordTimerBO);
			}
			return;
		}

		// 下一个执行操作是停止并且跨天了需要分割处理
		if (!recordTimerBO.isStart() && !isSameDay(nextTime, new Date())) {
			recordStrategyService.addCutRecordTimerSchedule(recordTimerBO);
			return;
		}

		recordTimerBO.setOperateTime(nextTime);
		recordTimerBO.setCut(false);
		try {
			Timer timer = new Timer();
			TimerTask timerTask = new RecordTimerTask(timer, recordTimerBO);
			timer.schedule(timerTask, recordTimerBO.getOperateTime());
		} catch (Exception e) {
			LOGGER.error("定时器停止，录制任务终止" + e.toString());
		}
	}

	private void executeCurrentTimerTask() {
		try {
			/** 跨天时间点切分任务 **/
			if (recordTimerBOInside.isCut()) {
				LOGGER.info("开始发送命令，执行跨天分割停止/开始录制:{开启时间:" + recordTimerBOInside.getOperateTime() + "}   该时间类型："
						+ recordTimerBOInside.isStart());
				// 停止旧的收录任务，如果出错，每隔10秒重试一次，共重试3次
				for (int i = 0; i < 3; i++) {
					try {
						recordCapacityService.delRecordTask(recordTimerBOInside.getCapacityTaskId());

						recordStrategyService.updateStrategyAndRecordFile(null, recordTimerBOInside,
								recordTimerBOInside.getOperateTime(), false);

						break;
					} catch (Exception e) {
						LOGGER.error("停止收录能力的录制任务失败,", e);
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e1) {
						}
					}
				}

				// 开始新的收录任务，如果出错，每隔10秒重试一次，共重试3次
				for (int i = 0; i < 3; i++) {
					try {

						recordCapacityService.addRecordTask(recordTimerBOInside);

						recordStrategyService.updateStrategyAndRecordFile(null, recordTimerBOInside,
								recordTimerBOInside.getOperateTime(), true);

						// TODO 更新跨天file条目??
						// parseStrategyService.loadAllItemByInput(recordTimerBOInside.getRecordStrategyId(),
						// recordTimerBOInside.getOperateTime());
						break;
					} catch (Exception e) {
						LOGGER.error("启动收录能力的录制任务失败,", e);
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e1) {
						}
					}
				}

				// 更新跨天条目//创建失败就不用更新跨天条目了
				// parseStrategyService.loadAllItemByInput(RecordTimerBO.getInputId(),
				// RecordTimerBO.getOperateTime());
				return;
			}

			// 启动普通录制任务
			if (recordTimerBOInside.isStart()) {
				LOGGER.info("开始发送命令，执行添加录制任务:{开启时间:" + recordTimerBOInside.getOperateTime() + "}   该时间类型："
						+ recordTimerBOInside.isStart());
				// 开始新的收录任务，如果出错，每隔10秒重试一次，共重试3次
				for (int i = 0; i < 3; i++) {
					try {
						recordCapacityService.addRecordTask(recordTimerBOInside);
						recordStrategyService.updateStrategyAndRecordFile(null, recordTimerBOInside,
								recordTimerBOInside.getOperateTime(), true);

						break;
					} catch (Exception e) {
						LOGGER.error("启动收录能力的录制任务失败,", e);
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e1) {
						}
					}
				}
				// TODO
				// autoAnalyzeTimerTask.startAutoAnalyze(RecordTimerBO.getRecordInfoId());
			}

			// 停止普通录制任务
			if (!recordTimerBOInside.isStart()) {
				LOGGER.info("开始发送命令，执行删除录制任务:{开启时间:" + recordTimerBOInside.getOperateTime() + "}   该时间类型："
						+ recordTimerBOInside.isStart());
				// 停止旧的收录任务，如果出错，每隔10秒重试一次，共重试3次
				for (int i = 0; i < 3; i++) {
					try {
						recordCapacityService.delRecordTask(recordTimerBOInside.getCapacityTaskId());
						recordStrategyService.updateStrategyAndRecordFile(null, recordTimerBOInside,
								recordTimerBOInside.getOperateTime(), false);
						break;
					} catch (Exception e) {
						LOGGER.error("停止收录能力的录制任务失败,", e);
						try {
							Thread.sleep(10 * 1000);
						} catch (InterruptedException e1) {
						}
					}
				}
			}
		} catch (Exception e) {
			if (recordTimerBOInside.isStart()) {
				LOGGER.error("启动录制任务失败,", e);
			} else {
				LOGGER.error("停止录制任务失败,", e);
			}
		}
	}

	public RecordTimerTask() {

	}

	public RecordTimerTask(Timer timer, RecordTimerBO RecordTimerBO) throws Exception {
		LOGGER.info("--------加载定时录制任务{开启时间:" + RecordTimerBO.getOperateTime() + "}   该时间类型：" + RecordTimerBO.isStart());

		if (null == timer || null == RecordTimerBO) {
			throw new Exception();
		}

		this.recordTimerBOInside = RecordTimerBO;
		RecordTimerBOMap.put(RecordTimerBO.getRecordStrategyId(), RecordTimerBO);

		if (null == timerMap.get(RecordTimerBO.getRecordStrategyId())) {
			LOGGER.info(
					"增加一个录制定时器：" + timerMap.size() + "-----recordStrategyId:" + RecordTimerBO.getRecordStrategyId());
			timerMap.put(RecordTimerBO.getRecordStrategyId(), timer);
		} else {
			Timer temptimer = timerMap.get(RecordTimerBO.getRecordStrategyId());
			temptimer.cancel();
			timerMap.remove(RecordTimerBO.getRecordStrategyId());
			timerMap.put(RecordTimerBO.getRecordStrategyId(), timer);
		}

		recordStrategyService = SpringBeanFactory.getBean(RecordStrategyService.class);
		// parseStrategyService = SpringBeanFactory.getBean(ParseStrategyService.class);
		recordCapacityService = SpringBeanFactory.getBean(RecordCapacityService.class);
		recordStrategyDAO = SpringBeanFactory.getBean(RecordStrategyDAO.class);
		// autoAnalyzeTimerTask = SpringBeanFactory.getBean(AutoAnalyzeTimerTask.class);

	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 != null && date2 != null) {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			return isSameDay(cal1, cal2);
		} else {
			throw new IllegalArgumentException("The date must not be null");
		}
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 != null && cal2 != null) {
			return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
		} else {
			throw new IllegalArgumentException("The date must not be null");
		}
	}
}
