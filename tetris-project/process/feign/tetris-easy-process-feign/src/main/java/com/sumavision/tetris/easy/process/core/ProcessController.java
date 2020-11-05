package com.sumavision.tetris.easy.process.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.config.server.EasyProcessServerPropsQuery;
import com.sumavision.tetris.easy.process.config.server.ServerProps;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/process")
public class ProcessController {

	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private EasyProcessServerPropsQuery easyProcessServerPropsQuery;
	
	/**
	 * 生成流程url<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月31日 下午2:10:20
	 * @param String processInstanceId 流程id
	 * @return String 流程url
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/url")
	public Object generateUrl(
			String processInstanceId,
			HttpServletRequest request) throws Exception{
		String token = request.getHeader(HttpConstant.HEADER_AUTH_TOKEN);
		String sessionId = request.getHeader(HttpConstant.HEADER_SESSION_ID);
		ServerProps serverProps = easyProcessServerPropsQuery.queryProps();
		String url = new StringBufferWrapper().append("http://")
											  .append(serverProps.getIp())
											  .append(":")
											  .append(serverProps.getPort())
											  .append("/index/display/")
											  .append(token)
											  .append("/")
											  .append(sessionId)
											  .append("/")
											  .append(processInstanceId)
											  .toString();
		return url;
	}
	
	/**
	 * 根据流程id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:33:53
	 * @param Long id 流程id
	 * @return ProcessVO 流程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/id")
	public Object findById(
			Long id,
			HttpServletRequest request) throws Exception{
		return processQuery.findById(id);
	}
	
	/**
	 * 根据id查询流程bpmn文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 上午9:39:00
	 * @param Long id 流程id
	 * @return String bpmn文件
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/bpmn/by/id")
	public Object findBpmnById(
			Long id, 
			HttpServletRequest request) throws Exception{
		return processQuery.findBpmnById(id);
	}
	
	/**
	 * 分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<ProcessVO> 流程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return processQuery.findByCompanyId(currentPage, pageSize);
	}
	
	/**
	 * 分页查询流程（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @param JSONArray except 例外流程id列表
	 * @return int total 总数据量
	 * @return List<ProcessVO> 流程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except")
	public Object listWithExcept(
			int currentPage,
			int pageSize,
			String except,
			HttpServletRequest request) throws Exception{
		List<Long> ids = null;
		if(except != null){
			ids = JSON.parseArray(except, Long.class);
		}
		return processQuery.findByCompanyIdWithExcept(currentPage, pageSize, ids);
	}
	
}
