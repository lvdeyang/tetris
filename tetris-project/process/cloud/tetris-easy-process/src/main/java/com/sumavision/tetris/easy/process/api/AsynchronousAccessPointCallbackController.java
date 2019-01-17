package com.sumavision.tetris.easy.process.api;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sumavision.tetris.easy.process.access.point.AccessPointDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamDAO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 异步接入点回调<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月10日 下午4:11:45
 */
@Controller
@RequestMapping(value = "/asynchronous/access/point/callback")
public class AsynchronousAccessPointCallbackController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	/**
	 * 异步接口调用成功<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月10日 下午4:10:19
	 * @param String __processId__ 流程实例id
	 * @param Long __accessPointId__ 接入点id
	 * @param JSONString variables 变量列表
	 */
	public Object complete(
			String __processId__,
			Long __accessPointId__,
			String variables,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		//用户流程校验？？
		
		
		
		
		return null;
	}
	
}
