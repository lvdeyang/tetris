package com.sumavision.tetris.test.flow.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.test.flow.dao.SchemeDAO;
import com.sumavision.tetris.test.flow.po.SchemePO;

@Controller
@RequestMapping(value = "/scheme")
public class SchemeController {

	@Autowired
	private SchemeDAO schemeDao;
	
	/**
	 * @Title: 录制一个接口<br> 
	 * @param serviceUuid 服务唯一标识
	 * @param name 接口名称
	 * @param className 接口类名
	 * @param methodName 接口方法名
	 * @param uri 去掉context-path后的调用地址
	 * @param param 参数
	 * @param expect 期望返回结果
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String serviceUuid,
			String name,
			String className,
			String methodName,
			String uri,
			String param,
			String expect,
			HttpServletRequest request) throws Exception{
		SchemePO scheme = null;
		scheme = schemeDao.findByServiceUuidAndUri(serviceUuid, uri);
		if(scheme == null){
			scheme = new SchemePO();
		}
		scheme.setServiceUuid(serviceUuid);
		scheme.setName(name);
		scheme.setClassName(className);
		scheme.setMethodName(methodName);
		scheme.setUri(uri);
		scheme.setParam(param);
		scheme.setExpect(expect.getBytes());
		scheme.setUpdateTime(new Date());
		schemeDao.save(scheme);
		return null;
	}
	
}
