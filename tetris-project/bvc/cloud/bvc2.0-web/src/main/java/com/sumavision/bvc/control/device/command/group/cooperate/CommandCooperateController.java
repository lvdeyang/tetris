package com.sumavision.bvc.control.device.command.group.cooperate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.group.cooperate.GroupCooperateService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/cooperation")
public class CommandCooperateController {
	
//	@Autowired
//	private CommandCooperateServiceImpl commandCooperateServiceImpl;
	
	@Autowired
	private GroupCooperateService groupCooperateService;
	
	@Autowired
	private UserUtils userUtils;

	@Autowired
	private BusinessReturnService businessReturnService;
	/**
	 * 发起协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月29日 上午11:04:50
	 * @param id 会议groupId
	 * @param userIds 协同成员memberId
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
		
//		commandCooperateServiceImpl.start(Long.parseLong(id), userIdArray);
		businessReturnService.init(Boolean.TRUE);
		groupCooperateService.start(Long.parseLong(id), userIdArray);
		
		return null;
	}
//	
//	/**
//	 * 同意协同会议<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月5日 上午9:29:44
//	 * @param id 会议id
//	 * @return
//	 * @throws Exception 
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/agree")
//	public Object agree(
//			String id,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
////		commandCooperateServiceImpl.agree(userId, Long.parseLong(id));
//		groupCooperateService.agree(userId, Long.parseLong(id));
//		
//		return null;
//	}
	
//	/**
//	 * 拒绝协同会议<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>zsy<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月5日 上午9:34:04
//	 * @param id 会议id
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/refuse")
//	public Object refuse(
//			String id,
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
////		commandCooperateServiceImpl.refuse(userId, Long.parseLong(id));
//		groupCooperateService.refuse(userId, Long.parseLong(id));
//		
//		return null;
//	}
	
//	/**
//	 * 撤销授权协同会议<br/>
//	 * <p>详细描述</p>
//	 * <b>作者:</b>Administrator<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年11月5日 上午9:36:49
//	 * @param id 会议id
//	 * @param businessId 会议id-成员id
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/revoke")
//	public Object revoke(
//			String id,
//			String businessId,
//			HttpServletRequest request) throws Exception{
//		
//		String userIdString = businessId.split("-")[1];
//		
////		JSONArray result = commandCooperateServiceImpl.revoke(Long.parseLong(userIdString), Long.parseLong(id));
//		JSONArray result = groupCooperateService.revoke(Long.parseLong(userIdString), Long.parseLong(id));
//		
//		//应该是一个空数组
//		return result;
//	}
	
	/**
	 * 批量撤销授权协同会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午8:49:31
	 * @param id 组id
	 * @param userIds 用户id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/revoke/batch")
	public Object revokeBatch(
			String id,
			String userIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(userIds, Long.class);
		
//		JSONArray result = commandCooperateServiceImpl.revokeBatch(userIdArray, Long.parseLong(id));
		businessReturnService.init(Boolean.TRUE);
		groupCooperateService.revokeBatch(Long.parseLong(id),userIdArray);
		
		//应该是一个空数组
		return null;
	}
	
}
