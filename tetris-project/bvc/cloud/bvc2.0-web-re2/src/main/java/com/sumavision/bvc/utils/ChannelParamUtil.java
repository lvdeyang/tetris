package com.sumavision.bvc.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 主要解析channel_param里的属性
 * @ClassName:  ChannelParamUtil   
 * @Description:TODO  
 * @author: 
 * @date:   2018年7月26日 下午1:19:19   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class ChannelParamUtil {
	
	public static JSONObject getBase_param(JSONObject channel_param){
		if(channel_param == null){
			return null;
		}
		return channel_param.getJSONObject("base_param");
	}
	
	public static JSONObject getExtern_param(JSONObject channel_param){
		if(channel_param == null){
			return null;
		}
		return channel_param.getJSONObject("extern_param");
	}
	
	public static String getBase_type(JSONObject channel_param){
		JSONObject base_param = getBase_param(channel_param);
		if(base_param == null){
			return null;
		}
		return base_param.getString("base_type");
	}
	
	public static String getExtern_type(JSONObject channel_param){
		JSONObject extern_param = getExtern_param(channel_param);
		if(extern_param == null){
			return null;
		}
		return extern_param.getString("extern_type");
	}

	public static JSONObject getBase_source_param(JSONObject channel_param){
		JSONObject base_param = getBase_param(channel_param);
		if(base_param == null){
			return null;
		}
		return base_param.getJSONObject("source_param");
	}
	
	public static JSONObject getBase_codec_param(JSONObject channel_param){
		JSONObject base_param = getBase_param(channel_param);
		if(base_param == null){
			return null;
		}
		return base_param.getJSONObject("codec_param");
	}
	
	/**
	 * 获取channel_param里base_codec_param里的基于codec_type的内容，例如："h264":{"bitrate":12000, "resolution":"1920*1080"}
	 * @param channel_param
	 * @return
	 */
	public static JSONObject getBase_codec_param_content(JSONObject channel_param){
		JSONObject codec_param = getBase_codec_param(channel_param);
		if(codec_param == null){
			return null;
		}
		String codec_type = codec_param.getString("codec_type");
		if(codec_type != null){
			return codec_param.getJSONObject(codec_type);
		}
		return null;
	}
	
	/**
	 * 获取codec_param_content里的resolution中的宽和高
	 * @param codec_param_content
	 * @return 例子：width-1920,height-1080
	 */
	public static Map<String, Integer> getResolutionWH(JSONObject codec_param_content){
		Map<String, Integer> whMap = new HashMap<String, Integer>();
		if(codec_param_content == null){
			return whMap;
		}
		String resolution = codec_param_content.getString("resolution");
		if(resolution == null){
			return whMap;
		}
		try{
			
			if(resolution.startsWith("[")){
				resolution = resolution.substring(1,resolution.length()-1);
				whMap.put("width", Integer.valueOf(resolution.split(",")[0]));
				whMap.put("height", Integer.valueOf(resolution.split(",")[1]));
			}else{
				if(resolution.contains("*")){
					whMap.put("width", Integer.valueOf(resolution.split("*")[0]));
					whMap.put("height", Integer.valueOf(resolution.split("*")[1]));
				}else if(resolution.contains("x")){
					whMap.put("width", Integer.valueOf(resolution.split("x")[0]));
					whMap.put("height", Integer.valueOf(resolution.split("x")[1]));					
				}
			}
		}catch(Exception e){	
			log.error("getResolutionWH failed", e);
		}
		return whMap;		
	}
}
