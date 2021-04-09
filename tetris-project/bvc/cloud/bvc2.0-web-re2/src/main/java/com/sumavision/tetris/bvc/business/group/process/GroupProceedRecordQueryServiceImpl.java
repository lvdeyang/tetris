package com.sumavision.tetris.bvc.business.group.process;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Service
public class GroupProceedRecordQueryServiceImpl {

	@Autowired
	private GroupProceedRecordDAO groupProceedRecordDao;

	/**
	 * 统计当前用户开会总时长、总场次、总人数<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月22日 下午7:20:39
	 * 
	 * @param 当前操作用户id
	 * @return
	 */
	public Map<String, Long> statisticsTimeAndNumberOfPeopleAndNumberOfProceedRecord(Long userId) {

		Map<String, Long> statisticsResult = new HashMapWrapper<String, Long>().put("time", 0L).put("totalPeople", 0L)
				.put("totalProceedRecord", 0L).getMap();

		List<GroupProceedRecordPO> proceedRecords = groupProceedRecordDao.findByUserIdOrderByStartTimeDesc(userId);

		if (proceedRecords == null || proceedRecords.size() < 0) {
			return statisticsResult;
		}

		Long time = 0L;
		Long totalPeople = 0L;
		Long totalProceedRecord = 0L;

		for (GroupProceedRecordPO proceedRecord : proceedRecords) {
			if (proceedRecord.getEndTime() == null) {
				time += System.currentTimeMillis() - proceedRecord.getStartTime().getTime();
			} else {
				time += proceedRecord.getEndTime().getTime() - proceedRecord.getStartTime().getTime();
			}
			totalPeople += proceedRecord.getOnlineMemberNumber();
			totalProceedRecord += 1;
		}

		time = time / 1000;

		statisticsResult.put("time", time);
		statisticsResult.put("totalPeople", totalPeople);
		statisticsResult.put("totalProceedRecord", totalProceedRecord);

		return statisticsResult;
	}

}
