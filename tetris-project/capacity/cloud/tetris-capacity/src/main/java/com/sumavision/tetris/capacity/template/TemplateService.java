package com.sumavision.tetris.capacity.template;/**
 * Created by Poemafar on 2020/7/28 10:05
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.exception.CommonException;
import com.sumavision.tetris.capacity.constant.EncodeConstant.*;
import com.sumavision.tetris.sts.transformTemplate.jni.TransformJniLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TemplateService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/7/28 10:05
 */
@Service
public class TemplateService {


    private static final Logger LOG = LoggerFactory.getLogger(TemplateService.class);

    /**
     * 根据编码类型，获取视频编码参数，{bitrate:1000,fps:25,x264:{}}
     * @param encType
     * @return json串
     * @throws CommonException
     */
    public String getVideoEncodeMap(TplVideoEncoder encType) throws CommonException {
        //获取模板库
        String tpl = TransformJniLib.getInstance().GetVideoEncParamTemplate(encType.ordinal(),"main","4",0);
        LOG.debug("transform template json : {} , param: {} ",tpl,encType.name());

        JSONObject encObj = JSONObject.parseObject(tpl);
        VideoType videoType = VideoType.getVideoType(encType);//编码类型
        String libName = TplVideoEncoder.getProtoName(encType);//库类型

        JSONObject encTpl = encObj.getJSONObject(videoType.name());
        List<String> params =  getAllParamsByEncodeTpl(encTpl);
        Map<String,Object> encodeParamMap = getParamMapByTpl(encTpl,params);

        JSONObject constraint = encTpl.getJSONObject("constraint");
        JSONObject libObj = constraint.getJSONObject(libName);
        //获取编码参数
        List<String> libParams = getAllParamsByEncodeTpl(libObj);
        //获取编码参数及对应的默认值
        Map<String,Object> libMap = getParamMapByTpl(libObj,libParams);
        encodeParamMap.put(libName,JSONObject.parseObject(JSON.toJSONString(libMap)));

        return JSON.toJSONString(encodeParamMap);
    }

    public String getAudioEncodeMap(String encodeType) throws CommonException {
        TplAudioEncoder audioEncoder = TplAudioEncoder.getTplAudioEncoder(encodeType);
        String tpl = "";
        AudioType audioType;
        if (audioEncoder.equals(TplAudioEncoder.AENCODER_MP3)) {
            tpl = TransformJniLib.getInstance().GetMp3EncParamTemplate("44.1");
            audioType = AudioType.getAudioType(audioEncoder);
        }else{
            tpl = TransformJniLib.getInstance().GetAudioEncParamTemplate(audioEncoder.ordinal(),0);
            audioType = AudioType.getAudioType(audioEncoder);
        }
        JSONObject tplObj = JSONObject.parseObject(tpl);
        JSONObject encodeTpl = tplObj.getJSONObject(audioType.name());
        List<String> params =  getAllParamsByEncodeTpl(encodeTpl);
        Map<String,Object> encodeParamMap = getParamMapByTpl(encodeTpl, params);
        return JSON.toJSONString(encodeParamMap);
    }


    /**
     * 获取模板里的所有参数
     * @param encodeTpl
     * @return
     */
    public List getAllParamsByEncodeTpl(JSONObject encodeTpl){
        List<String> params = new ArrayList<>();
        JSONObject items = encodeTpl.getJSONObject("items");
        JSONObject constraint = items.getJSONObject("constraint");
        JSONArray values = constraint.getJSONArray("values");
        for (int i = 0; i < values.size(); i++) {
            params.add((String) values.get(i));
        }
        return params;
    }

    public Map getParamMapByTpl(JSONObject encodeTpl, List<String> params){
        Map<String,Object> map = new HashMap<>();
        JSONObject constantParam = encodeTpl.getJSONObject("constraint");
        for (int i = 0; i < params.size(); i++) {
            String key = params.get(i);
            Object value = null;
            JSONObject tplParam = constantParam.getJSONObject(key);
            JSONObject constraint = tplParam.getJSONObject("constraint");
            if (constraint.containsKey("values")){
                JSONArray values = constraint.getJSONArray("values");
                Integer defaultIndex = constraint.getInteger("default_value");
                value = values.get(defaultIndex);
            }else if (constraint.containsKey("enum_constraint")){
                JSONObject enum_constraint = constraint.getJSONObject("enum_constraint");
                JSONArray values = enum_constraint.getJSONArray("values");
                Integer defaultIndex = enum_constraint.getInteger("default_value");
                value = values.get(defaultIndex);
            } else if(constraint.containsKey("default_value")){
                value = constraint.get("default_value");
            }  else{

            }
            map.put(key,value);
        }
        return map;
    }

    public static void main(String[] args) {
        TemplateService templateService = new TemplateService();
        String mp2Map = null;
        try {
            mp2Map = templateService.getAudioEncodeMap("aac");
        } catch (CommonException e) {
            e.printStackTrace();
        }
        JSONObject mp2Obj = JSONObject.parseObject(mp2Map);
//        mp2Obj.put("bitrate",String.valueOf(1200/1000));
//        mp2Obj.put("sample_rate",String.valueOf(3400/1000));

        System.out.println(mp2Obj);
    }

}
