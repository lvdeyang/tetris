package com.sumavision.tetris.sts.communication.httptool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.ErrorCodes;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpUtil {

    static Logger logger = LogManager.getLogger(HttpUtil.class);

    private OkHttpClient okHttpClient = new OkHttpClient();

    public JSONObject httpPost(String url , JSONObject object) throws BaseException {
        RequestBody body = format(object);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        JSONObject jsonObject = null;
        try {
            logger.info("send message to {}.\n{}" , url , object);
            response = okHttpClient.newCall(request).execute();
            jsonObject = JSON.parseObject(response.body().string());
            logger.info("ack from {}.\n{}" , url , jsonObject);
        } catch (IOException e) {
            throw new BaseException(StatusCode.ERROR,ErrorCodes.RESPONSE_ERROR);
        } catch (Exception e){
        	logger.error("msg ack err",e);
            throw new BaseException(StatusCode.ERROR,ErrorCodes.SYS_ERR);
        }
        return jsonObject;
    }
    
    public FormBody format(JSONObject jsonObject) {
        FormBody.Builder builder = new FormBody.Builder();
        format(builder , "" , jsonObject);
        return builder.build();
    }

    private void format(FormBody.Builder builder , String pre , JSONObject jsonObject) {
        jsonObject.entrySet().stream().forEach(objectEntry -> {
            String key = null == pre || pre.isEmpty() ? objectEntry.getKey() : pre + "[" + objectEntry.getKey() + "]";
            Object obj = objectEntry.getValue();
            formatObj(builder , key , obj);
        });
    }

    private void format(FormBody.Builder builder , String pre , JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            String key = pre + "[" + i + "]";
            Object obj = jsonArray.get(i);
            formatObj(builder , key , obj);
        }
    }

    private void formatObj(FormBody.Builder builder , String key , Object obj) {
        if (obj instanceof JSONArray)
            format (builder , key , (JSONArray)obj);
        else if (obj instanceof JSONObject)
            format (builder , key , (JSONObject)obj);
        else {
            builder.add(key , obj.toString());
//            System.out.println(key + "=" + obj);
        }
    }
    
}
