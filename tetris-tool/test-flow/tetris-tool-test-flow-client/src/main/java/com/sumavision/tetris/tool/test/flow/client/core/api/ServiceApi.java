package com.sumavision.tetris.tool.test.flow.client.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sumavision.tetris.tool.test.flow.client.core.metadata.ServiceMetadata;
import com.sumavision.tetris.tool.test.flow.client.core.rmi.ServiceFeign;

/**
 * @ClassName: 服务相关api<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午7:29:12 
 */
@Service
public class ServiceApi {

	@Autowired
	private ServiceFeign serviceFeign;
	
	/**
	 * @Title: 向测试服务中注册服务<br/> 
	 * @param metadata 服务元数据
	 * @return void 
	 */
	public void doRegiste(ServiceMetadata metadata) throws Exception{
		
		//远程调用
		serviceFeign.doRegiste(
				metadata.getServiceName(), 
				metadata.getServiceUuid(), 
				metadata.getServiceIp(), 
				metadata.getServicePort());
		
	}
	
}
