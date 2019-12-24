package com.sumavision.tetris.sts.communication.httptool;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Lost on 2018/1/25.
 */
@Component
public class XToolHttpUnit{

	@Autowired
	HttpUtil httpUtil;
	
    public JSONObject httpPost(String url , JSONObject object) throws BaseException {
        JSONObject jsonObject = httpUtil.httpPost(url, object);

        if (jsonObject.getInteger("result").equals(0)) {
            return jsonObject;
        } else {
            throw new BaseException(StatusCode.ERROR,jsonObject.getString("errormsg"));
        }
    }
    
    public JSONObject getMsg(String deviceIp , String type) throws BaseException {
        return httpPost(XToolMsgType.getUrl(deviceIp , type) , new JSONObject());
    }

    public void postMsg(String deviceIp , String type , JSONObject object) throws BaseException {
        httpPost(XToolMsgType.getUrl(deviceIp , type) , object);
    }

}
