package com.sumavision.tetris.record.strategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.record.file.RecordFileDAO;
import com.sumavision.tetris.record.file.RecordFilePO;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.source.SourceDAO;
import com.sumavision.tetris.record.source.SourcePO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyStatus;
import com.sumavision.tetris.record.strategy.RecordStrategyPO.EStrategyType;

@Service
public class ParseStrategyService {

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private RecordStrategyDAO recordStrategyDAO;

	@Autowired
	private RecordStrategyService recordStrategyService;

	@Autowired
	private RecordStrategyItemDAO recordStgItemDAO;

	@Autowired
	private SourceDAO sourceDao;

	// 老策略重启有问题，对于定时和自定义任务，策略逻辑有改动并且应该要启动策略定时器，yzx edit on 20190404
	public void rebootStrategy(Long inputId, Date date) throws ParseException {
		// 生成策略条目，根据策略Id生成某一天的条目
		List<RecordStrategyPO> recordStrategyPOs = recordStrategyDAO.findBySourceIdAndDelStatusNot(inputId, 1);
		if (null == recordStrategyPOs || recordStrategyPOs.isEmpty()) {
			return;
		}
		SourcePO sourcePO = sourceDao.findOne(inputId);

		for (RecordStrategyPO recordStrategyPO : recordStrategyPOs) {
			// if (recordStrategyPO.getType() == EStrategyType.CONTINUOUS) {
			// 	createContinueStgyItem(recordStrategyPO.getId());
			// }
			
			//TODO 新 手动策略处理
//			if(recordStrategyPO.getType() == Type.SCHEDULE) {
//				loadScheduleStgyItemByTime(inputId, date);
//			}
			if (recordStrategyPO.getType() == EStrategyType.CYCLE_SCHEDULE) {
				// loadScheduleOrCustomStgAndStartStgTimerTask(sourcePO, recordStrategyPO);
			}
			if (recordStrategyPO.getType() == EStrategyType.CUSTOM_SCHEDULE) {
				// loadScheduleOrCustomStgAndStartStgTimerTask(sourcePO, recordStrategyPO);
			}
		}
	}

	/*
	 * @MethodName: loadScheduleOrCustomStgAndStartStgTimerTask
	 * 
	 * @Description: TODO 导入定时或自定义策略并开启策略定时器（策略定时器的任务就是定时更新recordfile）
	 * 
	 * @param sourcePO 1
	 * 
	 * @param stgPO 2
	 * 
	 * @Return: void
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/4 11:24
	 **/
	public void loadScheduleOrCustomStgAndStartStgTimerTask(RecordStrategyPO stgPO) {
		boolean flag = false;
		// 先检测当前时间是否有任务在执行，flag=true表示有，有任务执行但是实际未执行，就要添加新的recordfile，然后设置策略录制状态，启动策略定时器
		if (stgPO.getType() == EStrategyType.CYCLE_SCHEDULE) {
			flag = recordStrategyService.nowInScheduleStgItemPeriod(stgPO);
		} else if (stgPO.getType() == EStrategyType.CUSTOM_SCHEDULE) {
			flag = recordStrategyService.nowInCustomStgItemPeriod(stgPO);
		} else {
			return;// 该方法只实现定时和自定义策略重启逻辑，别的类型策略直接返回
		}
		try {
			if (flag) {
				recordStrategyService.startSchedulerTimerTaskByRecovery(stgPO);
				generateNewRecordFile(stgPO);
				stgPO.setStatus(EStrategyStatus.RUNNING);
				recordStrategyDAO.save(stgPO);
			} else {
				recordStrategyService.startScheduleTimerTask(stgPO);
				stgPO.setStatus(EStrategyStatus.STOP);
				recordStrategyDAO.save(stgPO);
			}
			// parseStrategyTimerTask.startTimerTask(stgPO.getId());
		} catch (Exception ce) {
			ce.printStackTrace();
		}
	}

	/*
	 * @MethodName: generateNewRecordFile
	 * 
	 * @Description: TODO 对于某个recordinfo生成一个开始时间为当前时间的新的录制文件，主要用于任务恢复（流中断恢复时和应用启动时）
	 * 
	 * @param sourcePO 1
	 * 
	 * @param stgPO 2
	 * 
	 * @param recordInfoId 3
	 * 
	 * @Return: void
	 * 
	 * @Author: Poemafar
	 * 
	 * @Date: 2019/4/4 11:06
	 **/
	public void generateNewRecordFile(RecordStrategyPO stgPO) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		RecordFilePO recordFilePO = new RecordFilePO();
		recordFilePO.setStatus(ERecordFileStatus.RECORD_RUN);
		// TODO
		recordFilePO.setRecordStrategyId(stgPO.getId());
		recordFilePO.setStartTime(new Date());
		// recordFilePO.setDayStr(sdf.format(recordFilePO.getStartTime()));
		// recordFilePO.setPath
		recordFileDAO.save(recordFilePO);
	}

	public void delRecordByStgyId(Long stgyId) {
		recordFileDAO.deleteByStgyId(stgyId);
	}

	// 根据录制条目展示录制信息
	// public List<RecordInfoPO> queryRecordByStgyItemId(Long stgyItemId) {
	// return null;
	// }

	// 查询按天时间划分
	public List<String> queryRecordTimes(Long stgyId) {
		// 先统计多少天的内容，然后按天来计算分页
		// return recordFileDAO.listDayStrField(stgyId);
		return null;
	}

	// 按天展示录制信息,当前时间之前的
	public List<RecordFilePO> queryRecordByTime(Long stgyId, String day) {
		// 先统计多少天的内容，然后按天来计算分页
		// return recordFileDAO.findByStrategyIdAndDayStr(stgyId, day);
		return null;
	}

	private void splitContinueStgy(RecordStrategyPO recordStrategyPO, Date date) {
		List<RecordFilePO> recordFilePOs = recordFileDAO.findByRecordStrategyIdAndStatus(recordStrategyPO.getId(),
				ERecordFileStatus.RECORD_RUN);
		if (null != recordFilePOs && !recordFilePOs.isEmpty()) {
			for (RecordFilePO recordFilePO : recordFilePOs) {
				recordFilePO.setStopTime(date);
				recordFilePO.setStatus(ERecordFileStatus.RECORD_SUC);
				recordFileDAO.save(recordFilePO);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		RecordFilePO nextPO = new RecordFilePO();
		nextPO.setStartTime(date);
		nextPO.setRecordStrategyId(recordStrategyPO.getId());
		// TODO
		nextPO.setStatus(ERecordFileStatus.RECORD_RUN);
		recordFileDAO.save(nextPO);

	}
}
