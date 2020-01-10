package com.suma.venus.resource.controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerBase {
	
	protected static final String ERRMSG = "errMsg";
	
	protected static final String WARNMSG = "warnMsg";
	
	protected Map<String, Object> makeAjaxData() {
        Map<String, Object> data = new HashMap<>();
        data.put(ERRMSG, null);

        return data;
    }
}
