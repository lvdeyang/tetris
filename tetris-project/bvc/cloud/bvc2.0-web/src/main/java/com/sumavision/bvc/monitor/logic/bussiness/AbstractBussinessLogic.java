
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-service 下午1:03:57  
* All right reserved.  
*  
*/

package com.sumavision.bvc.monitor.logic.bussiness;

import java.util.List;

import javax.servlet.ServletContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.DO.PreviewDO;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.monitor.logic.manager.AcessLayerTask;
import com.sumavision.bvc.monitor.logic.manager.ResourseLayerTask;
import com.sumavision.bvc.monitor.logic.resourceStrategy.AbstractResourceStrategy;
import com.sumavision.bvc.monitor.logic.resourceStrategy.Context;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc: bvc-monitor-service
 * @author: kpchen
 * @createTime: 2018年6月11日 下午1:03:57
 * @history:
 * @version: v1.0
 */
@Service
@Slf4j
public abstract class AbstractBussinessLogic<K> {
	//private static ServletContext servletContext;
	@Autowired
	ResourseLayerTask resourseLayerTask;
	@Autowired
	AcessLayerTask acessLayerTask;
	@Autowired
	ResourceService resourceService;

	K dto;

//	@Override
//	public void setServletContext(ServletContext servletContext) {
//		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		resourseLayerTask = ctx.getBean(ResourseLayerTask.class);
//		resourceService = ctx.getBean(ResourceService.class);
//		acessLayerTask =  ctx.getBean(AcessLayerTask.class);
//	}

	public abstract ResultMap executeBussiness(K dto);


	
}
