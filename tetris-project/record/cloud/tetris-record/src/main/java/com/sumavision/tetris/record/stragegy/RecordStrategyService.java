package com.sumavision.tetris.record.stragegy;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.record.SpringBeanFactory;
import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.stragegy.RecordStrategyPO.EStrategyStatus;
import com.sumavision.tetris.record.stragegy.RecordStrategyPO.EStrategyType;
import com.sumavision.tetris.record.task.handler.RecordTimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordStrategyService {

	@Autowired
	private RecordStrategyDAO recordStgDao;

	@Autowired
	private RecordStrategyItemDAO recordStgItemDao;

	// @Autowired
	// private RecordInfoService recordInfoService;

	@Autowired
	private RecordTimerTask recordTimerTask;

	@Autowired
	private ParseStrategyService parseStrategyService;

	@Autowired
	private ParseStrategyTimerTask parseStrategyTimerTask;

	public int stgySize(Long sourceId) {
		List<RecordStrategyPO> recordStrategyPOs = recordStgDao.findBySourceId(sourceId);
		return recordStrategyPOs.size();
	}

	public void delAllStgy(Long sourceId) {
		RecordFileDAO recordFileDao = SpringBeanFactory.getBean(RecordFileDAO.class);
		List<RecordStrategyPO> recordStrategyPOs = recordStgDao.findBySourceId(sourceId);
		for (RecordStrategyPO recordStrategyPO : recordStrategyPOs) {
			recordFileDao.deleteByStgyId(recordStrategyPO.getId());
			recordFileDao.deleteBySourceId(sourceId);
			recordStgItemDao.deleteByRecordStgId(recordStrategyPO.getId());
			recordStgDao.delete(recordStrategyPO);
		}
	}

	/**
	 * 获取输入源的所有录制时段的并集的下一个开始或结束时间点
	 */
	public Date getNextTimePoint(Long sourceId, boolean isStart) {
		// 优先判断持续录制和已经启动的实时启停
		List<RecordStrategyPO> recordStrategyPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		for (RecordStrategyPO recordStrategyPO : recordStrategyPOs) {
			if (EStrategyType.CONTINUOUS == recordStrategyPO.getType()) {
				if (RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
					return null;
				}
			}
			if (RecordStrategyPO.EStrategyType.MANUAL == recordStrategyPO.getType()
					&& RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
				return null;
			}
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		List<RecordStrategyPO> stgPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		if (null == stgPOs || stgPOs.isEmpty()) {
			if (!isStart) {
				// 停止节点，那么立即停止
				return new Date();
			}
			return null;
		}

		for (RecordStrategyPO stgPO : stgPOs) {
			generateTimePoints(stgPO, timePoints);
		}

		return findNextTimePoint(timePoints, currentTime, isStart);
	}

	public Date getNextTimePointByReboot(Long sourceId) {
		List<RecordStrategyPO> recordStrategyPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		for (RecordStrategyPO recordStrategyPO : recordStrategyPOs) {
			if (RecordStrategyPO.EStrategyType.CONTINUOUS == recordStrategyPO.getType()) {
				return new Date();
			}
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		List<RecordStrategyPO> stgPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		if (null == stgPOs || stgPOs.isEmpty()) {
			return null;
		}

		for (RecordStrategyPO stgPO : stgPOs) {
			generateTimePoints(stgPO, timePoints);
		}

		return findNextTimePoint(timePoints, currentTime, true);
	}

	/**
	 * 获取录制策略的所有录制时段的并集的下一个开始或结束时间点
	 */
	public Date getNextStgTimePoint(Long stgId, boolean isStart) {
		RecordStrategyPO stgPO = recordStgDao.findByIdAndDelStatusNot(stgId, 1);
		if (null == stgPO) {
			return null;
		}

		if (EStrategyType.CONTINUOUS == stgPO.getType() || EStrategyType.MANUAL == stgPO.getType()) {
			// 持续的永远无下一动作，手动的由页面驱动定时器
			return null;
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		generateTimePoints(stgPO, timePoints);

		return findNextTimePoint(timePoints, currentTime, isStart);
	}

	/**
	 * 根据策略条目生成时间点
	 */
//	private void generateTimePoints(RecordStrategyPO stgPO, List<Map<String, Date>> timePoints){
//		Long loopTime = stgPO.getLoopPeriod() * 24 * 3600 * 1000;
//		List<RecordStgItemPO> itemPOs = recordStgItemDao.findByRecordStgId(stgPO.getId());
//		for(RecordStgItemPO itemPO : itemPOs){
//			Date itemStartTime = new Date(itemPO.getStartTime().getTime());
//			Date itemEndTime = new Date(itemPO.getStopTime().getTime());
//			for(int i=0; i<stgPO.getLoopCnt(); ++i){
//				Map<String, Date> startPoint = new HashMap<>();
//				Map<String, Date> endPoint = new HashMap<>();
//				startPoint.put("s", new Date(itemStartTime.getTime()));
//				endPoint.put("e", new Date(itemEndTime.getTime()));
//				timePoints.add(startPoint);
//				timePoints.add(endPoint);
//				itemStartTime.setTime(itemStartTime.getTime() + loopTime);
//				itemEndTime.setTime(itemEndTime.getTime() + loopTime);
//			}
//		}
//	}

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
		// 只是自定义的，定时录制的怎么写
		try {
			if (RecordStrategyPO.EStrategyType.SCHEDULE == stgPO.getType()) {
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
		if (RecordStrategyPO.EStrategyType.CUSTOM == stgPO.getType()) {
			List<RecordStrategyItemPO> itemPOS = recordStgItemDao.findByRecordStgId(stgPO.getId());
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
		List<RecordStrategyItemPO> recordStgItemPOS = recordStgItemDao.findByRecordStgId(stgPO.getId());
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

	/**
	 * 获取所有录制时段的并集的下一个开始或结束时间点
	 */
	private Date findNextTimePoint(List<Map<String, Date>> timePoints, Date currentTime, boolean isStart) {
		List<Map<String, Date>> tpList = makeSumTimePoints(timePoints, currentTime);

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
	 * 获取所有录制时段的并集的下一个开始或结束时间点
	 */
	private Map<String, Date> findNextTimePoint(List<Map<String, Date>> timePoints, Date currentTime) {
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
	 * 判断源的下一个时间点是否是开始点
	 */
	public Boolean isNextTimePointStart(Long sourceId) {
		// 优先判断持续录制和已经启动的实时启停
		List<RecordStrategyPO> stgPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		// 源没有策略，就找不到下一个开始点，应该返回null，原先返回false
		if (null == stgPOs || stgPOs.isEmpty()) {
			return null;
		}

		for (RecordStrategyPO recordStrategyPO : stgPOs) {
			// 持续也可能error（比如流异常）,也可能stop（比如停录制）
			if (RecordStrategyPO.EStrategyType.CONTINUOUS == recordStrategyPO.getType()) {
				if (RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
					return null;
				}
			}

			if (RecordStrategyPO.EStrategyType.MANUAL == recordStrategyPO.getType()
					&& RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
				return null;
			}
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		for (RecordStrategyPO stgPO : stgPOs) {
			generateTimePoints(stgPO, timePoints);
		}

		Map<String, Date> timePoint = findNextTimePoint(timePoints, currentTime);
		if (null != timePoint && timePoint.containsKey("s")) {
			return true;
		}
		if (null != timePoint && timePoint.containsKey("e")) {
			return false;
		}
		return null;
	}

	/**
	 * 判断源的下一个时间点是否是开始点
	 */
	public boolean isNextTimePointStartByReboot(Long sourceId) {
		// 优先判断持续录制和已经启动的实时启停
		List<RecordStrategyPO> stgPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		if (null == stgPOs || stgPOs.isEmpty()) {
			return false;
		}

		for (RecordStrategyPO recordStrategyPO : stgPOs) {
			if (RecordStrategyPO.EStrategyType.CONTINUOUS == recordStrategyPO.getType()) {
				return true;
			}

			if (RecordStrategyPO.EStrategyType.MANUAL == recordStrategyPO.getType()
					&& RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
				return true;
			}
		}

		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();
		for (RecordStrategyPO stgPO : stgPOs) {
			generateTimePoints(stgPO, timePoints);
		}
		Map<String, Date> timePoint = findNextTimePoint(timePoints, currentTime);
		if (null != timePoint && timePoint.containsKey("s")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断策略的下一个时间点是否是开始点
	 */
	public Boolean isNextStgTimePointStart(Long stgId) {
		RecordStrategyPO stgPO = recordStgDao.findByIdAndDelStatusNot(stgId, 1);
		if (null == stgPO) {
			return null;
		}
		if (RecordStrategyPO.EStrategyType.CONTINUOUS == stgPO.getType() || RecordStrategyPO.EStrategyType.MANUAL == stgPO.getType()) {
			// 持续的永远无下一动作，手动的由页面驱动定时器
			return null;
		}
		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();
		generateTimePoints(stgPO, timePoints);
		Map<String, Date> timePoint = findNextTimePoint(timePoints, currentTime);
		if (null == timePoint) {
			return null;
		}
		if (timePoint.containsKey("s")) {
			return true;
		}
		if (timePoint.containsKey("e")) {
			return false;
		}
		return null;
	}

	/**
	 * 判断源是否还有下一个时间点
	 */
	public boolean hasNextRecord(Long sourceId) {
		// 优先判断持续录制和已经启动的实时启停
		List<RecordStrategyPO> stgPOs = recordStgDao.findBySourceIdAndDelStatusNot(sourceId, 1);
		if (null == stgPOs || stgPOs.isEmpty()) {
			return false;
		}
		for (RecordStrategyPO recordStrategyPO : stgPOs) {
			if (RecordStrategyPO.EStrategyType.CONTINUOUS == recordStrategyPO.getType()) {
				return true;
			}

			if (RecordStrategyPO.EStrategyType.MANUAL == recordStrategyPO.getType()
					&& RecordStrategyPO.EStrategyStatus.RUNNING == recordStrategyPO.getStatus()) {
				return true;
			}
		}
		Date currentTime = new Date();
		List<Map<String, Date>> timePoints = new ArrayList<>();

		for (RecordStrategyPO stgPO : stgPOs) {
			generateTimePoints(stgPO, timePoints);
		}

		if (null == findNextTimePoint(timePoints, currentTime)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断源的下一个时间点是否是开始点
	 */
	public void save(RecordStrategyPO po) throws Exception {
		List<RecordStrategyPO> pos = recordStgDao.findByNameAndSourceIdAndDelStatusNot(po.getName(), po.getSourceId(),
				1);
		if (pos != null && !pos.isEmpty()) {
			throw new Exception("已存在相同的名称");
		}
		recordStgDao.save(po);
	}

	/**
	 * 根据周期策略与日期，返回当日的录制时段
	 */
	// TODO
	public List<TimeSegmentBO> getDayTimeSegments(Date day, Long stgId) throws Exception {
		RecordStrategyPO stg = recordStgDao.findByIdAndDelStatusNot(stgId, 1);
		List<RecordStrategyItemPO> items = recordStgItemDao.findByRecordStgId(stgId);

		// int period = stg.getLoopPeriod().intValue();
		// long periodTime = period * 24 * 60 * 60 * 1000;
		// Date loopStart = stg.getStartTime();
		// int loopCnt = stg.getLoopCnt();

		// int dayCnt = (int) ((day.getTime() - loopStart.getTime()) / (24 * 60 * 60 * 1000));

		// 如果超出结束时间
		// if (dayCnt > loopCnt * period) {
		//	return null;
		//}

		List<TimeSegmentBO> timeSegs = new ArrayList<>();

		// 周期为1天
		// if (period == 1) {
		//	for (RecordStrategyItemPO item : items) {
		//		TimeSegmentBO ts = new TimeSegmentBO(item.getStartTime(), item.getStopTime(), item.getName());
		//		timeSegs.add(ts);
		//	}
		//	return timeSegs;
		// } else {
		//	int dayCntMod = dayCnt % period;
		//	Date top = new Date(loopStart.getTime() + dayCntMod * 24 * 60 * 60 * 1000);
		//	Date bottom = new Date(top.getTime() + 24 * 60 * 60 * 1000 - 1000);
			// 取day与item时段的交集
		//	for (RecordStrategyItemPO item : items) {
		//		Date st = item.getStartTime();
		//		Date et = item.getStopTime();
		//		TimeSegmentBO ts;
		//		if (st.before(top) && et.after(bottom)) {
		//			ts = new TimeSegmentBO(top, bottom, item.getName());
		//			timeSegs.add(ts);
		//		} else if (st.before(top) && et.before(bottom) && et.after(top)) {
		//			ts = new TimeSegmentBO(top, et, item.getName());
		//			timeSegs.add(ts);
		//		} else if (st.after(top) && st.before(bottom) && et.after(bottom)) {
		//			ts = new TimeSegmentBO(st, bottom, item.getName());
		//			timeSegs.add(ts);
		//		} else if (st.after(top) && st.before(bottom) && et.before(bottom)) {
		//			ts = new TimeSegmentBO(st, et, item.getName());
		//			timeSegs.add(ts);
		//		}
		//	}
		//	return timeSegs;
		//}
		
		// TODO
		return null;

	}

	/**
	 * 根据策略条目与日期，返回当日的录制时段
	 */
	public TimeSegmentBO getDayTimeSegment(Date day, Long itemId) throws Exception {
		RecordStrategyItemPO item = recordStgItemDao.findOne(itemId);
		/*
		RecordStrategyPO stg = recordStgDao.findOne(item.getRecordStgId());

		int period = stg.getLoopPeriod().intValue();
		long periodTime = period * 24 * 60 * 60 * 1000;
		Date loopStart = stg.getStartTime();
		int loopCnt = stg.getLoopCnt();

		int dayCnt = (int) ((day.getTime() - loopStart.getTime()) / (24 * 60 * 60 * 1000));

		// 如果超出结束时间
		if (dayCnt > loopCnt * period) {
			return null;
		}

		// 周期为1天
		if (period == 1) {
			TimeSegmentBO ts = new TimeSegmentBO(item.getStartTime(), item.getStopTime(), item.getName());
			return ts;
		}

		else {
			int dayCntMod = dayCnt % period;
			Date top = new Date(loopStart.getTime() + dayCntMod * 24 * 60 * 60 * 1000);
			Date bottom = new Date(top.getTime() + 24 * 60 * 60 * 1000 - 1000);
			// 取day与item时段的交集
			Date st = item.getStartTime();
			Date et = item.getStopTime();
			TimeSegmentBO ts = null;
			if (st.before(top) && et.after(bottom)) {
				ts = new TimeSegmentBO(top, bottom, item.getName());
			} else if (st.before(top) && et.before(bottom) && et.after(top)) {
				ts = new TimeSegmentBO(top, et, item.getName());
			} else if (st.after(top) && st.before(bottom) && et.after(bottom)) {
				ts = new TimeSegmentBO(st, bottom, item.getName());
			} else if (st.after(top) && st.before(bottom) && et.before(bottom)) {
				ts = new TimeSegmentBO(st, et, item.getName());
			}
			return ts;
		}
		*/
		
		//TODO
		return null;

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
		/*
		StorageDao storageDao = SpringBeanFactory.getBean(StorageDao.class);
		List<RecordFilePO> recordFilePOS = recordFileDao.findByStrategyId(stgId);
		for (RecordFilePO filePO : recordFilePOS) {
			recordFileDao.delete(filePO);
			if (filePO.getRecordInfoId() != null) {
				Long infoId = filePO.getRecordInfoId();
				RecordInfoPO recordInfoPO = recordInfoService.findOne(infoId);
				if (recordInfoPO == null) {
					continue;
				}
				List<RecordFilePO> recordFilePOList = recordFileDao.findByStrategyIdAndRecordInfoId(stgId, infoId);
				// info的file都为空了，就删除磁盘文件
				if (recordFilePOList.isEmpty()) {
					List<StoragePO> storages = storageDao.findAll();
					;
					String fileName = recordInfoPO.getName() + "_"
							+ simpleDateFormat.format(recordInfoPO.getStartTime());
					// 文件删成功了再删info
					FTPUtil.INSTANCE.deleteFileInSpecifyDir(storages.get(0).getAddress(), fileName);
					recordInfoService.delRecordInfo(fileName);
				}
			}
		}
		*/
		// 删recordfile记录
		recordFileDao.deleteByStgyId(stgId);
		recordStgItemDao.deleteByRecordStgId(stgId);
		recordStgDao.delete(stgId);
	}

	public void stopStrategyRecord(RecordStrategyPO stgPO) throws Exception {
		// 没有运行无需停止
		if (stgPO.getStatus() != EStrategyStatus.RUNNING) {
			return;
		}
		if (stgPO.getType() == EStrategyType.CONTINUOUS) {
			stopRecordOfContinueStg(stgPO);
		}
		if (stgPO.getType() == EStrategyType.MANUAL) {
			stopRecordOfManualStg(stgPO);
		}
		if (stgPO.getType() == EStrategyType.SCHEDULE) {
			stopRecordOfScheduleStg(stgPO);
		}
		if (stgPO.getType() == EStrategyType.CUSTOM) {
			stopRecordOfCustomStg(stgPO);
		}
	}

	/*
	 * @Description: TODO 停止持续录制任务：停任务(更stg状态)-更新file状态，加事务（停止策略失败的话回滚操作）
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/11 13:53
	 **/
	@Transactional(rollbackFor = Exception.class)
	public void stopRecordOfContinueStg(RecordStrategyPO stgPO) throws Exception {
		updateStgRecordForStopRecord(stgPO);
		// 停录制任务
		recordTimerTask.stopContinueRecord(stgPO.getSourceId(), stgPO.getId());
		// 更新file记录
		parseStrategyService.stopManualStgyItem(stgPO.getId());
	}

	public void stopRecordOfManualStg(RecordStrategyPO stgPO) throws Exception {
		stopRecordOfContinueStg(stgPO);
	}

	public void stopRecordOfScheduleStg(RecordStrategyPO stgPO) throws Exception {
		updateStgRecordForStopRecord(stgPO);
		recordTimerTask.stopContinueRecord(stgPO.getSourceId(), stgPO.getId());
		parseStrategyTimerTask.stopStgyRecord(stgPO.getId());
	}

	public void stopRecordOfCustomStg(RecordStrategyPO stgPO) throws Exception {
		stopRecordOfScheduleStg(stgPO);
	}

	/*
	 * @Description: TODO 因为停止录制，更新策略状态为停止
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/11 14:00
	 **/
	public void updateStgRecordForStopRecord(RecordStrategyPO stgPO) {
		stgPO.setStatus(RecordStrategyPO.EStrategyStatus.STOP);
		recordStgDao.save(stgPO);
	}

	public void updateStgRecordForStartRecord(RecordStrategyPO stgPO) {
		stgPO.setStatus(RecordStrategyPO.EStrategyStatus.RUNNING);
		recordStgDao.save(stgPO);
	}

	public void updateStgRecordForDeleteStg(RecordStrategyPO stgPO) {
		// TODO
		// stgPO.setDelStatus(1);
		recordStgDao.save(stgPO);
	}

	@Transactional(rollbackFor = { Exception.class, ParseException.class })
	public void startRecordOfManualStg(RecordStrategyPO stgPO) throws Exception, ParseException {
		if (!stgPO.getType().equals(RecordStrategyPO.EStrategyType.MANUAL)) {
			return;
		}
		updateStgRecordForStartRecord(stgPO);
		recordTimerTask.startContinueRecord(stgPO.getSourceId());
		parseStrategyService.startManualStgyItem(stgPO.getId());
	}

	@Transactional(rollbackFor = { Exception.class, ParseException.class })
	public void startRecordOfContinueStg(RecordStrategyPO stgPO) throws Exception, ParseException {
		if (!stgPO.getType().equals(RecordStrategyPO.EStrategyType.CONTINUOUS)) {
			return;
		}
		updateStgRecordForStartRecord(stgPO);
		recordTimerTask.startContinueRecord(stgPO.getSourceId());
		parseStrategyService.createContinueStgyItem(stgPO.getId());
	}

	public List<RecordStrategyPO> findBySourceId(Long sourceId) {
		return recordStgDao.findBySourceId(sourceId);
	}

	public void saveStrategy(List<RecordStrategyPO> stgPOS) {
		recordStgDao.save(stgPOS);
	}
}
