package com.sumavision.bvc.control.device.command.group.replace;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.special.replace.GroupReplaceService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/replace")
public class CommandReplaceController {

	@Autowired
	private GroupReplaceService groupReplaceService;


	@Autowired
	private UserUtils userUtils;

	@Autowired
	private BusinessReturnService businessReturnService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/grant")
	public Object start(
			String id,
			String opId,
			String targetId,
			HttpServletRequest request) throws Exception{

		//commandCooperateServiceImpl.start(Long.parseLong(id), userIdArray);
		businessReturnService.init(Boolean.TRUE);
		groupReplaceService.startM(Long.parseLong(id), Long.parseLong(opId), Long.parseLong(targetId));

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
	@RequestMapping(value = "/stop")
	public Object revokeBatch(
			String id,
			HttpServletRequest request) throws Exception{

//		JSONArray result = commandCooperateServiceImpl.revokeBatch(userIdArray, Long.parseLong(id));
		businessReturnService.init(Boolean.TRUE);
		groupReplaceService.stop(Long.parseLong(id));

		//应该是一个空数组
		return null;
	}

}
