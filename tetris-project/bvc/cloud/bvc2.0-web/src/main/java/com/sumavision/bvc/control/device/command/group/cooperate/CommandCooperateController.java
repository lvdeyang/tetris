package com.sumavision.bvc.control.device.command.group.cooperate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.cooperate.CommandCooperateServiceImpl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/cooperation")
public class CommandCooperateController {
	
	@Autowired
	private CommandCooperateServiceImpl commandCooperateServiceImpl;
	
	@Autowired
	private UserUtils userUtils;

	/**
	 * 发起协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月29日 上午11:04:50
	 * @param id 指挥id
	 * @param members 协同成员
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/grant")
	public Object start(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
		commandCooperateServiceImpl.start(Long.parseLong(id), userIdArray);
		
		return null;
	}
	
	/**
	 * 同意协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:29:44
	 * @param id 指挥id
	 * @return
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/agree")
	public Object agree(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandCooperateServiceImpl.agree(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 拒绝协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:34:04
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/refuse")
	public Object refuse(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandCooperateServiceImpl.refuse(userId, Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 撤销授权协同指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:36:49
	 * @param id 指挥id
	 * @param businessId 指挥id-成员id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/revoke")
	public Object revoke(
			String id,
			String businessId,
			HttpServletRequest request) throws Exception{
		
		String userIdString = businessId.split("-")[1];
		
		commandCooperateServiceImpl.revoke(Long.parseLong(userIdString), Long.parseLong(id));
		
		return null;
	}
	
}
