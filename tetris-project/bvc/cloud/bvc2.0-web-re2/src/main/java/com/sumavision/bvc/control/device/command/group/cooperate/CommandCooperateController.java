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
import com.sumavision.tetris.bvc.business.special.cooperate.GroupCooperateService;
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

		//commandCooperateServiceImpl.start(Long.parseLong(id), userIdArray);
		businessReturnService.init(Boolean.TRUE);
		groupCooperateService.startU(Long.parseLong(id), userIdArray);

		return null;
	}

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
		groupCooperateService.revokeBatchM(Long.parseLong(id),userIdArray);

		//应该是一个空数组
		return null;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object revokeBatch(
			String id,
			HttpServletRequest request) throws Exception{

		businessReturnService.init(Boolean.TRUE);
		groupCooperateService.revokeBatchM(Long.parseLong(id), null);//第二个参数没有用

		//应该是一个空数组
		return null;
	}

}
