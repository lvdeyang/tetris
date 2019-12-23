package com.sumavision.tetris.sts.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
}
