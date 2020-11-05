package com.sumavision.tetris.mims.app.operation.statistic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/operation/statistic/strategy")
public class OperationStatisticStrategyController {
	@Autowired
	private OperationStatisticStrategyService statisticStrategyService;
	
	@Autowired
	private OperationStatisticStrategyQuery statisticStrategyQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 设置结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午3:12:55
	 * @param Long id 结算策略id
	 * @param Integer operator 运营商占比数
	 * @param Integer producer 生产者占比数
	 * @return OperationStatisticStrategyVO 结算策略信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object setStrategy(Long id, Integer operator, Integer producer, HttpServletRequest request) throws Exception {
		return id == null ? statisticStrategyService.add(producer, operator) : statisticStrategyService.edit(id, producer, operator);
	}
	
	/**
	 * 获取结算策略列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午3:16:55
	 * @return List<OperationStatisticStrategyVO> 结算策略信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/list")
	public Object getStrategyList(HttpServletRequest request) throws Exception {
		return statisticStrategyQuery.queryAvailable();
	}
	
	/**
	 * 根据id获取结算策略<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月2日 下午3:18:40
	 * @param Long id 结算策略id
	 * @return OperationStatisticStrategyVO 结算策略信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/{id}")
	public Object getById(@PathVariable Long id, HttpServletRequest request) throws Exception {
		return statisticStrategyQuery.queryById(id);
	}
	
	/**
	 * 获取用户列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午1:47:40
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/user/list")
	public Object questUserList(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<UserVO> users = 
				userQuery.listByCompanyIdWithExceptAndClassify(Long.parseLong(user.getGroupId()), null, UserClassify.COMPANY);
		
		return users;
	}
}
