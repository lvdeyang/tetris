package com.sumavision.tetris.test.flow.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.test.flow.dao.ServiceDAO;
import com.sumavision.tetris.test.flow.po.ServicePO;

@Controller
@RequestMapping(value = "/service")
public class ServiceController {

	@Autowired
	private ServiceDAO serviceDao;
	
	/**
	 * @Title: 服务注册<br/> 
	 * @param name 服务名称
	 * @param uuid 服务唯一标识
	 * @param ip 服务ip
	 * @param port 服务端口
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/do/registe")
	public Object doRegiste(
			String name,
			String uuid,
			String ip,
			String port,
			HttpServletRequest request) throws Exception{
		
		ServicePO service = null;
		List<ServicePO> srevices = serviceDao.findByUuid(uuid);
		if(srevices==null || srevices.size()<=0){
			service = new ServicePO();
		}else{
			service = srevices.get(0);
		}
		service.setUuid(uuid);
		service.setName(name);
		service.setIp(ip);
		service.setPort(port);
		service.setUpdateTime(new Date());
		serviceDao.save(service);
		
		return null;
	}
	
}
