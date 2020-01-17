package com.sumavision.tetris.test.flow.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.tool.test.flow.client.core.annotation.enumeration.TestFlowSwitch;
import com.sumavision.tetris.tool.test.flow.client.core.metadata.ServiceMetadata;

@Component(ServiceMetadata.BEANNAME)
@PropertySource(value = {"classpath:tetris-all.properties"}, ignoreResourceNotFound = true, encoding = "utf-8")
public class TestFlowMetadata implements ServiceMetadata{

	@Value("${TestFlow.status}")
	private String status;
	
	@Override
	public TestFlowSwitch getSwitch() throws Exception{
		if(this.status == null){
			return TestFlowSwitch.OFF;
		}
		return TestFlowSwitch.fromName(this.status);
	}

	@Override
	public String getServiceName() {
		return "自动化测试服务";
	}

	@Override
	public String getServiceUuid() {
		return "tetris-test-flow";
	}

	@Override
	public String getServiceIp() {
		return "192.165.56.71";
	}

	@Override
	public String getServicePort() {
		return "8086";
	}

	@Override
	public String getContextPath() {
		//return "/tetris";
		return null;
	}
	
}
