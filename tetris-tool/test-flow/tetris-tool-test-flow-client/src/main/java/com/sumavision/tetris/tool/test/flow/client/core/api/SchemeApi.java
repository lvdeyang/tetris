package com.sumavision.tetris.tool.test.flow.client.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.tool.test.flow.client.core.metadata.ServiceMetadata;
import com.sumavision.tetris.tool.test.flow.client.core.rmi.SchemeFeign;

/**
 * @ClassName: 接口录制相关api<br/> 
 * @author lvdeyang
 * @date 2018年8月31日 下午2:15:59 
 */
@Service
public class SchemeApi {

	@Autowired
	private SchemeFeign schemeFeign;
	
	/**
	 * @Title: 添加一个 接口录制<br/>
	 * @param metadata 服务元数据
	 * @param className 接口所在类名
	 * @param methodName 接口方法名
	 * @param uri 接口调用地址
	 * @param param 接口参数
	 * @param expect 接口期望返回值
	 * @throws Exception
	 */
	public void save(
			ServiceMetadata metadata,
			String className,
			String methodName,
			String uri,
			String param,
			String expect) throws Exception{
		
		schemeFeign.save(metadata.getServiceUuid(), metadata.getServiceName(), className, methodName, uri, param, expect);
		
	}
	
}
