package com.sumavision.bvc.device.monitor.record;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.commons.util.date.DateUtil;

@Service
public class MonitorRecordManyTimesRelationService {
	
	@Autowired
	private MonitorRecordManyTimesRelationDAO monitorRecordManyTimesRelationDao;
	
	/**
	 * 更新排期表中的下次录制时间<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月17日 下午7:45:18
	 * @param relation
	 */
	public void updateNextTime(MonitorRecordManyTimesRelationPO relation){
		if(MonitorRecordManyTimesMode.DAY.equals(relation.getMode())){
			DateUtil.addDay(relation.getNextStartTime(), 1);
			DateUtil.addDay(relation.getNextEndTime(), 1);
		}else if(MonitorRecordManyTimesMode.WEEK.equals(relation.getMode())){
			DateUtil.addDay(relation.getNextStartTime(), 7);
			DateUtil.addDay(relation.getNextEndTime(), 7);
		}else if(MonitorRecordManyTimesMode.MONTH.equals(relation.getMode())){
			DateUtil.addMonth(relation.getNextStartTime(), 1);
			DateUtil.addMonth(relation.getNextEndTime(), 1);
		}
		relation.setIndexNumber(relation.getIndexNumber()+1);
		monitorRecordManyTimesRelationDao.save(relation);
	}

}
