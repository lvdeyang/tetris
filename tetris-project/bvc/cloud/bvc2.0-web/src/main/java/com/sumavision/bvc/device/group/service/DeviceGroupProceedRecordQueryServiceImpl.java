package com.sumavision.bvc.device.group.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.bvc.device.group.vo.DeviceGroupProceedRecordStatisticsBO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Service
public class DeviceGroupProceedRecordQueryServiceImpl {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;

	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;

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

//		List<DeviceGroupPO> deviceGroups = deviceGroupDao.findByUserId(userId);
		Map<String, Long> statisticsResult = new HashMapWrapper<String, Long>().put("time", 0L).put("totalPeople", 0L)
				.put("totalProceedRecord", 0L).getMap();
//		if (deviceGroups == null || deviceGroups.size() < 0) {
//			return statisticsResult;
//		}

		List<DeviceGroupProceedRecordPO> proceedRecords = deviceGroupProceedRecordDao.findByUserIdOrderByStartTimeDesc(userId);
//				new ArrayListWrapper<DeviceGroupProceedRecordPO>()
//				.addAll(deviceGroupProceedRecordDao
//						.findByGroupIdIn(deviceGroups.stream().map(DeviceGroupPO::getId).collect(Collectors.toList())))
//				.getList();

		if (proceedRecords == null || proceedRecords.size() < 0) {
			return statisticsResult;
		}

		Long time = 0L;
		Long totalPeople = 0L;
		Long totalProceedRecord = 0L;

		for (DeviceGroupProceedRecordPO proceedRecord : proceedRecords) {
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

	/**
	 * 统计最近20次的开会总人数和每次时长<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月22日 下午7:26:51
	 * 
	 * @return
	 */
	public DeviceGroupProceedRecordStatisticsBO statisticsRecentlyTimeAndNumberOfPeople(Long userId) {

		List<DeviceGroupPO> deviceGroups = deviceGroupDao.findByUserId(userId);
		DeviceGroupProceedRecordStatisticsBO statisticsBo = new DeviceGroupProceedRecordStatisticsBO();
		
		if (deviceGroups == null || deviceGroups.size() < 0) {
			return statisticsBo;
		}

		List<DeviceGroupProceedRecordPO> proceedRecords = new ArrayListWrapper<DeviceGroupProceedRecordPO>()
				.addAll(deviceGroupProceedRecordDao
						.findByGroupIdIn(deviceGroups.stream().map(DeviceGroupPO::getId).collect(Collectors.toList())))
				.getList();

		if (deviceGroups == null || deviceGroups.size() < 0) {
			return statisticsBo;
		}
		
		proceedRecords.stream().sorted(Comparator.comparing(DeviceGroupProceedRecordPO::getStartTime)).limit(20).forEach(record->{
			statisticsBo.getStatisticsRecetlyNumberOfPeople().add(record.getOnlineMemberNumber());
			if (record.getEndTime() == null) {
				statisticsBo.getStatisticsRecetlyTime().add(System.currentTimeMillis()-record.getStartTime().getTime());
			} else {
				statisticsBo.getStatisticsRecetlyTime().add(record.getEndTime().getTime()-record.getStartTime().getTime());
			}
		});

		return statisticsBo;
	}
}
