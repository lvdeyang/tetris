package com.sumavision.tetris.bvc.cascade.api;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.AgendaServiceImpl;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 提供给接入层的接口<br/>
 * <p>包括“终端呼叫”等</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月30日 下午4:25:06
 */
@Controller
@RequestMapping(value = "/api/thirdpart/bvc/terminal")
public class ApiThirdpartBvcTerminalController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private MeetingServiceImpl meetingServiceImpl;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	/**
	 * 收到终端呼叫，执行一个会议<br/>
	 * <p>查找名字包含“会议.”的会议，执行第1个</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月30日 下午4:21:50
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/call")
	public Object terminalCall(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		String from = requestWrapper.getString("from");
		String to = requestWrapper.getString("to");
		System.out.println("/api/thirdpart/bvc/terminal " + "from: " + from + ", to: " + to);
		
		List<DeviceGroupPO> groups = deviceGroupDao.findByNameLike("%会议.");
		if(groups == null || groups.size() == 0){
			System.out.println("没有查询到会议");
			return null;
		}
		
		DeviceGroupPO group = groups.get(0);
		System.out.println("查询到" + groups.size() + "个会议，执行第1个：" + group.getName());
		DeviceGroupPO group2 = meetingServiceImpl.start(group.getId(), group.getUserId(), group.getUserName());
		
		Set<DeviceGroupConfigPO> configs = group2.getConfigs();
		if(configs == null || configs.size() == 0){
			System.out.println("没有查询到议程");
			return null;
		}
		for(DeviceGroupConfigPO config : configs){
			if(ConfigType.AGENDA.equals(config.getType())){
				Thread.sleep(1500);
				System.out.println("执行议程：" + config.getName());
				agendaServiceImpl.run(group.getId(), config.getId());
				break;
			}
		}
		
		return null;
	}
	
}
