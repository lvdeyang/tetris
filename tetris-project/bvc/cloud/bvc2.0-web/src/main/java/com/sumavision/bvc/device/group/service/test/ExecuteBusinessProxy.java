package com.sumavision.bvc.device.group.service.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusiness;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
//import com.sumavision.tetris.tool.test.flow.client.core.annotation.TestResult;

/**
 * @ClassName: 逻辑层接口代理<br/> 
 * @author lvdeyang
 * @date 2018年8月31日 下午3:42:34 
 */
@Service
public class ExecuteBusinessProxy {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExecuteBusinessProxy.class);

	@Autowired
	private ExecuteBusiness executeBusiness;
	
	/**
	 * @Title: 调用逻辑层<br/> 
	 * @param protocal 协议
	 * @param message 日志标题
	 * @throws Exception
	 * @return LogicBO 测试时替换返回结果
	 */
	//@TestResult
	public synchronized ExecuteBusinessReturnBO  execute(
			LogicBO protocal, 
			String message) throws Exception{
		
		String jsonProtocal = JSON.toJSONString(protocal);
		LOG.info(message);
		LOG.info(jsonProtocal);
		//System.out.println(message);
		//System.out.println(jsonProtocal);
		ExecuteBusinessReturnBO response = null;
		response = executeBusiness.executeJsonOrder(JSON.parseObject(jsonProtocal));
		
		return response;
	}
	
}
