package com.sumavision.bvc.command.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceQuery;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/system/query")
public class CommandSystemQueryController {
	
	@Autowired
	private MonitorLiveDeviceQuery monitorLiveDeviceQuery;

	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/count/of/transmit")
	public Object queryCountOfTransmit(){
		Long count = monitorLiveDeviceQuery.queryCountOfTransmit();
		if(count==null){
			return 0;
		}
		return count;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/count/of/review")
	public Object queryCountOfReview(){
		
		List<BusinessInfoType> types=new ArrayListWrapper().add(BusinessInfoType.PLAY_RECORD).add(BusinessInfoType.PLAY_COMMAND_RECORD).getList();
		
		if(types==null){
			return 0;
		}
		
		return pageTaskDao.countByBusinessInfoTypeIn(types);
	}
	
}
