package com.sumavision.bvc.control.device.command.group.time;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.device.command.time.CommandFightTimeServiceImpl;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 
* @ClassName: CommandFightTimeController 
* @Description: 作战时间
* @author zsy
* @date 2020年2月11日 上午10:53:06 
*
 */
@Controller
@RequestMapping(value = "/command/fight/time")
public class CommandFightTimeController {

	@Autowired
	private CommandFightTimeServiceImpl commandFightTimeServiceImpl;

	/**
	 * 对某个指挥设置作战时间<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午3:20:12
	 * @param id
	 * @param fightTime
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object setFightTime(
			String id,
			String fightTime,
			HttpServletRequest request) throws Exception{
		
		Date fightTimeDate = DateUtil.parse(fightTime, DateUtil.dateTimePattern);
		
		commandFightTimeServiceImpl.setFightTime(Long.parseLong(id), fightTimeDate);
		
		return null;
	}
	
}
