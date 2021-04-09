package com.sumavision.tetris.bvc.business.dispatch.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.tetris.bvc.business.dispatch.TetrisDispatchService;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchBO;
import com.sumavision.tetris.bvc.business.dispatch.bo.DispatchResponseBO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/tetris/dispatch")
public class TetrisDispatchController {

	
	@Autowired
	TetrisDispatchService tetrisDispatchService;
		
	/**
	 * 执行调度<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午4:20:07
	 * @param dispatch
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/execute")
	public Object execute(
			String dispatch,
			HttpServletRequest request) throws Exception{
		
		DispatchBO dispatchBO = JSONObject.parseObject(dispatch, DispatchBO.class);
		DispatchResponseBO dispatchResponseBO = tetrisDispatchService.dispatch(dispatchBO);

		return dispatchResponseBO;
	}
	
	/**
	 * 通过消息服务透传passby消息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月12日 下午4:52:33
	 * @param passbyArray
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/passby")
	public Object passby(
			@RequestBody String passbyArray,
			HttpServletRequest request) throws Exception{
		
		List<PassByBO> passbyList = JSONObject.parseArray(passbyArray, PassByBO.class);
		tetrisDispatchService.passby(passbyList);

		return null;
	}
	
}
