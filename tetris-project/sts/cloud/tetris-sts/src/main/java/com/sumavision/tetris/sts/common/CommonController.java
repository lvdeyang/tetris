package com.sumavision.tetris.sts.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Created by Poemafar on 2019/12/16 13:47
 */
public class CommonController {
    protected static Logger logger = LogManager.getLogger(CommonController.class);

    protected static final String ERRMSG = "errMsg";
    
    protected Map<String, Object> makeAjaxData() {
        Map<String, Object> data = new HashMap<>();
        data.put(ERRMSG, null);

        return data;
    }
    
    protected String errorMsg(HttpServletRequest request , String str) {
        RequestContext requestContext = new RequestContext(request);
        String errMsg = requestContext.getMessage(str);
        return errMsg;
    }
    
    protected HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
}
