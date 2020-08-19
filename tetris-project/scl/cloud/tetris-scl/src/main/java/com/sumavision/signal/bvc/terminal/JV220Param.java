package com.sumavision.signal.bvc.terminal;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * jv220Http Json<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年9月4日 下午4:01:35
 */
@Service
public class JV220Param {

	/**
	 * 获取getjv220编解码参数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月4日 下午5:32:19
	 */
	public JSONObject getJv220Param(){
		
		JSONObject getJson = new JSONObject();
		getJson.put("jsonrpc", "2.0");
    	getJson.put("method", "multimedia.get");
    	getJson.put("params", new JSONObject());
    	getJson.put("id", "suma");
		
		return getJson;
		
	}
	
	/**
	 * 设置multimedia参数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月9日 上午10:05:46
	 */
	public JSONObject multimediaSet(JSONObject params){
	
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "multimedia.set");
		jsonObject.put("params", params);
		jsonObject.put("id", "suma");
		
		return jsonObject;
	}
	
	/**
	 * 设置receive参数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月9日 上午10:09:27
	 * @param Long num 序列号
	 * @param JsonObject params 设置参数
	 */
	public JSONObject receiveSet(Long num, JSONObject params){
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "receive"+ num +".set");
		jsonObject.put("params", params);
		jsonObject.put("id", "suma");
		
		return jsonObject;
	}
	
	/**
	 * 心跳探测<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月11日 上午9:00:51
	 */
	public JSONObject test(){
		
		JSONObject jsonObject = new JSONObject();
		
		JSONArray paramArray = new JSONArray();
		JSONObject param = new JSONObject();
		param.put("seq", "123");
		param.put("ability_id", 1);
		paramArray.add(param);
		
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "test");
		jsonObject.put("params", paramArray);
		jsonObject.put("id", "suma");
		
		return jsonObject;
		
	}
	
}
