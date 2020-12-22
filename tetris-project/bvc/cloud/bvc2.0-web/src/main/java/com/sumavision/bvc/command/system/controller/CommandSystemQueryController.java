package com.sumavision.bvc.command.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.command.system.service.CommandSystemQueryImp;
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
	
	@Autowired
	private CommandSystemQueryImp commandSystemQueryImp;
	
	/**
	 * 查询已经占用的转发路数<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 下午1:55:40
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/count/of/transmit")
	public Object queryCountOfTransmit(){
		return commandSystemQueryImp.queryCountOfTransmit();
	}
	
	/**
	 * 查询已经占用的回放路数<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 下午1:50:34
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/count/of/review")
	public Object queryCountOfReview(){
		return commandSystemQueryImp.queryCountOfReview();
	}
	
}
