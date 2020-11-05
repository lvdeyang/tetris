package com.sumavision.tetris.tool.test.flow.client.core.metadata;

import com.sumavision.tetris.tool.test.flow.client.core.annotation.enumeration.TestFlowSwitch;

/**
 * @ClassName: 服务元数据描述 <br/>
 * @author lvdeyang
 * @date 2018年8月29日 下午6:53:26 
 */
public interface ServiceMetadata {
	
	/** 定义bean的名称 */
	public static final String BEANNAME = "serviceMetadata";

	/** 服务状态 */
	public TestFlowSwitch getSwitch() throws Exception;
	
	/** 服务名称，自定义 */
	public String getServiceName() throws Exception;
	
	/** 服务唯一标识，最好与springboot配置相同 */
	public String getServiceUuid() throws Exception;
	
	/** 服务ip，最好与springboot配置相同 */
	public String getServiceIp() throws Exception;
	
	/** 服务端口，最好与springboot配置相同 */
	public String getServicePort() throws Exception;
	
	/** context-path：必须以/开头，与springboot配置相同 */
	public String getContextPath() throws Exception;
	
}
