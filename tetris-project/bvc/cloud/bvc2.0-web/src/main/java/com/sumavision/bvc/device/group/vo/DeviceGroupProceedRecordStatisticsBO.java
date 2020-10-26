package com.sumavision.bvc.device.group.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * DeviceGroupProceedRecord的统计信息
 * </p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月22日 下午7:32:03
 */
@Deprecated
public class DeviceGroupProceedRecordStatisticsBO {

	/** 最近20次会议人数 */
	private List<Integer> statisticsRecetlyNumberOfPeople = new ArrayList<Integer>();

	/** 最近20次会议时长 */
	private List<Long> statisticsRecetlyTime = new ArrayList<Long>();

	public List<Integer> getStatisticsRecetlyNumberOfPeople() {
		return statisticsRecetlyNumberOfPeople;
	}

	public DeviceGroupProceedRecordStatisticsBO setStatisticsRecetlyNumberOfPeople(
			List<Integer> statisticsRecetlyNumberOfPeople) {
		this.statisticsRecetlyNumberOfPeople = statisticsRecetlyNumberOfPeople;
		return this;
	}

	public List<Long> getStatisticsRecetlyTime() {
		return statisticsRecetlyTime;
	}

	public DeviceGroupProceedRecordStatisticsBO setStatisticsRecetlyTime(List<Long> statisticsRecetlyTime) {
		this.statisticsRecetlyTime = statisticsRecetlyTime;
		return this;
	}

}
