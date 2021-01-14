package com.sumavision.bvc.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.log.OperationLogService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/operation/log")
public class OperationLogController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private OperationLogService operationLogService;
	
	/**
	 * 添加任意操作日志<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月5日 下午5:43:41
	 * @param operationName 概要，操作名称
	 * @param detail 详细描述
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String operationName,
			String detail,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		operationLogService.send(user.getName(), operationName, user.getName() + detail);
//		operationLogService.send(user.getName(), "下载录制任务", user.getName()+"下载录制任务：" +file.getFileName());
		
		return null;
	}

}
