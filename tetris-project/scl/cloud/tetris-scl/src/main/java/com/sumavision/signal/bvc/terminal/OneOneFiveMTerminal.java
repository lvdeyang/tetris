package com.sumavision.signal.bvc.terminal;

import com.sumavision.signal.bvc.http.HttpAsyncClient;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * 5G背包就一个编码通道
 */

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


	/**
	 * 对应接口管理页面-获取参数
	 * @return
	 */
	public static JSONObject getGBECfg(){

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "intf5g.getall");
		jsonObject.put("params", "{}");
		jsonObject.put("id", "getGBECfg");

		return jsonObject;
	}

	/**
	 * 接口管理页面-参数设置
	 * @param params
	 * @return
	 */
	public static JSONObject setGBECfg(JSONObject params){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "intf5g.set");
		jsonObject.put("params", params);
		jsonObject.put("id", "setGBECfg");

		return jsonObject;
	}

	/**
	 * 通道1监测页面-获取参数
	 * @return
	 */
	public static JSONObject getMonitorInfo(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "busMonitor.getall");
		jsonObject.put("params", "{}");
		jsonObject.put("id", "busMonitor");

		return jsonObject;
	}

	/**
	 * 通道1设置页面-获取参数
	 * @return
	 */
	public static JSONObject getSuiBus(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "suiBus.getall");
		jsonObject.put("params", "{}");
		jsonObject.put("id", "getSuiBus");

		return jsonObject;
	}

	public static JSONObject setSuiBus(JSONObject params){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonrpc", "2.0");
		jsonObject.put("method", "suiBus.set");
		jsonObject.put("params", params);
		jsonObject.put("id", "setSuiBus");

		return jsonObject;
	}

	public static void main(String[] args) throws Exception {
		String deviceIp = "10.10.40.222";
		HttpAsyncClient.getInstance().httpAsyncPost("http://" + deviceIp + TerminalParam.FIVEG_URL_SUFFIX, getGBECfg().toJSONString(), null, null);

	}

}
