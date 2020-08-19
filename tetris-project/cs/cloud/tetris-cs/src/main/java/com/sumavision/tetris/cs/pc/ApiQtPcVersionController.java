package com.sumavision.tetris.cs.pc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityQuery;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileService;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/qt/cs/pcversion")
public class ApiQtPcVersionController {
	
	@Autowired
	private VersionQuery versionQuery;
	
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取pc终端当前版本<autor by mr.>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest")
	public Object questVersion(HttpServletRequest request) throws Exception {
		
		return versionQuery.getVersion();
	}
}
