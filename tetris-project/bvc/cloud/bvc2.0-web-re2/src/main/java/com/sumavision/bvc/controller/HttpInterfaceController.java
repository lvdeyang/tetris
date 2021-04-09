package com.sumavision.bvc.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import com.suma.venus.message.util.MessageIds;
import com.sumavision.bvc.BO.IncomingCallResp;

/**
 * Http对外接口
 * @author sx
 *
 */
@Controller
@RequestMapping("/api")
public class HttpInterfaceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpInterfaceController.class);
	
	@RequestMapping(method = RequestMethod.POST, value = "/incomingReturn", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
	public com.sumavision.bvc.BO.ResponseBody incomingReturn(@RequestBody IncomingCallResp incomingCallResp){
		if(incomingCallResp == null || incomingCallResp.getIncoming_call_response() == null){
			LOGGER.error("incomingcallResp is null");
			return new com.sumavision.bvc.BO.ResponseBody(com.sumavision.bvc.BO.ResponseBody.FAIL, "param is null");
		}
		try{
			MessageIds.messageInfoMap.put(incomingCallResp.getIncoming_call_response().getUuid(), incomingCallResp);			
			return new com.sumavision.bvc.BO.ResponseBody(com.sumavision.bvc.BO.ResponseBody.SUCCESS);
		}catch(Exception e){
			LOGGER.error("incomingReturn failed", e);
		}
		return new com.sumavision.bvc.BO.ResponseBody(com.sumavision.bvc.BO.ResponseBody.FAIL, "some exceptions occur");
	}
	
	
}
