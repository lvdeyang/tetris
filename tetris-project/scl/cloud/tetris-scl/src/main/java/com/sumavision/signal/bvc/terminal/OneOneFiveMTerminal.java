package com.sumavision.signal.bvc.terminal;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class OneOneFiveMTerminal {

	/**
	 * 视频编码啊设置参数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 下午4:32:18
	 * @param JSONObject params 视频编码设置参数
	 * @return JSONObject
	 */
	public JSONObject vidEncSet(JSONObject params){
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "vidEnc.set");
		jsonObject.put("params", params);
		jsonObject.put("id", "vidEnc");
		
		return jsonObject;
	}
	
	/**
	 * 音频编码设置参数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 下午4:34:33
	 * @param JSONObject params 音频编码设置参数
	 * @return JSONObject
	 */
	public JSONObject audEncSet(JSONObject params){
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "audEnc.set");
		jsonObject.put("params", params);
		jsonObject.put("id", "audEnc");
		
		return jsonObject;
	}
	
	/**
	 * GBE设置参数<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月16日 下午4:39:21
	 * @param JSONObject params GBE设置参数
	 * @return JSONObject
	 */
	public JSONObject gbePortCfg(JSONObject params){
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "gbePortCfg.set");
		jsonObject.put("params", params);
		jsonObject.put("id", "gbePortCfg");
		
		return jsonObject;
	}
	
}
