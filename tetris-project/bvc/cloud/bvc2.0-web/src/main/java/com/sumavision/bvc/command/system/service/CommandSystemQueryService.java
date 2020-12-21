package com.sumavision.bvc.command.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceQuery;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
public class CommandSystemQueryService {

	@Autowired
	private MonitorLiveDeviceQuery monitorLiveDeviceQuery;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	/**
	 * 方法概述<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 下午1:50:05
	 */
	
	public Long queryCountOfTransmit() {
		Long count = monitorLiveDeviceQuery.queryCountOfTransmit();
		if(count==null){
			return 0L;
		}
		return count;
	}

	/**
	 * 查询已经占用的回放路数<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 下午1:50:34
	 * @return
	 */
	public Long queryCountOfReview() {
		List<BusinessInfoType> types=new ArrayListWrapper<BusinessInfoType>().add(BusinessInfoType.PLAY_RECORD).add(BusinessInfoType.PLAY_COMMAND_RECORD).getList();
		if(types == null){
			return 0L;
		}
		return pageTaskDao.countByBusinessInfoTypeIn(types);
	}

}
