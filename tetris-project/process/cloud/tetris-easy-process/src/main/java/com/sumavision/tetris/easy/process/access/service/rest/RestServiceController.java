package com.sumavision.tetris.easy.process.access.service.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.easy.process.access.service.exception.ServiceNotExistException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/rest/service")
public class RestServiceController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private RestServiceQuery restServiceTool;
	
	@Autowired
	private RestServiceDAO restServiceDao;
	
	@Autowired
	private RestService restService;
	
	/**
	 * 获取所有的rest服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月17日 上午11:29:20
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return long total 数据总量
	 * @return List<RestServiceVO> rows 服务数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		List<RestServicePO> entities = restServiceTool.findAll(currentPage, pageSize);
		
		List<RestServiceVO> rows = RestServiceVO.getConverter(RestServiceVO.class).convert(entities, RestServiceVO.class);
		
		long total = restServiceDao.count();
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		
		return result;
	}

	/**
	 * 添加一个rest服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 上午9:58:09
	 * @param String name rest服务名称
	 * @param String host rest服务域名或ip
	 * @param String port rest服务端口
	 * @param String contextPath rest服务根
	 * @param String remarks rest服务备注
	 * @return RestServiceVO rest服务数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String host,
			String port,
			String contextPath,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		RestServicePO service = new RestServicePO();
		service.setName(name);
		service.setHost(host);
		service.setPort(port);
		service.setContextPath(RestServicePO.formatContextPath(contextPath));
		service.setRemarks(remarks);
		service.setUpdateTime(new Date());
		restServiceDao.save(service);
		
		return new RestServiceVO().set(service);
	}
	
	/**
	 * 编辑服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 上午11:42:02
	 * @param @PathVariable id 服务id
	 * @param String name 服务名称
	 * @param String host 域名或ip
	 * @param String port 服务端口
	 * @param String contextPath 服务根
	 * @param String remarks 备注
	 * @return RestServiceVO 服务数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String host,
			String port,
			String contextPath,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		RestServicePO service = restServiceDao.findOne(id);
		
		if(service == null){
			throw new ServiceNotExistException(id);
		}
		
		service.setName(name);
		service.setHost(host);
		service.setPort(port);
		service.setContextPath(RestServicePO.formatContextPath(contextPath));
		service.setRemarks(remarks);
		service.setUpdateTime(new Date());
		restServiceDao.save(service);
		
		return new RestServiceVO().set(service);
	}
	
	/**
	 * 删除服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 上午11:58:52
	 * @param @PathVariable Long id 服务id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		RestServicePO service = restServiceDao.findOne(id);
		
		if(service == null){
			throw new ServiceNotExistException(id);
		}
		
		restService.delete(service);
		
		return null;
	}
	
}
