package com.sumavision.signal.bvc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.http.HttpCallBack;
import com.sumavision.signal.bvc.terminal.TerminalParam;

/**
 * 终端通话Get异步请求回调<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月21日 上午10:35:05
 */
public class TerminalCallingCallBack extends HttpCallBack<String, String, String, String, String, String, String>{

	public TerminalCallingCallBack(String param1, String param2, String param3, String param4) {
		super(param1, param2, param3, param4);
	}

	@Override
	public void completed(Object result) {
		
		try {
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			
			String url = this.getParam1();
			String screenLayout = this.getParam2();
			String picture1 = this.getParam3();
			String picture2 = this.getParam4();
			
			//通话设置请求
	    	String[][] TerminalCallingParam = JSON.parseObject(JSON.toJSONString(TerminalParam.TerminalCallingParam), String[][].class);
	 
        	String[][] callingParam = TerminalParam.html2Data(res, TerminalCallingParam);
    		
    		callingParam[1][0] = screenLayout;
			callingParam[1][3] = picture1;
			callingParam[1][5] = picture2;
    		
    		List<BasicNameValuePair> callingBody = new ArrayList<BasicNameValuePair>();
    		BasicNameValuePair callingPair = new BasicNameValuePair("setString", TerminalParam.array2Data(callingParam));
    		callingBody.add(callingPair);
    		HttpAsyncClient.getInstance().formPost(url, null, callingBody, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Exception ex) {
		System.out.println("终端通话GET异常");
		ex.printStackTrace();
	}

	@Override
	public void cancelled() {
		System.out.println("终端通话连接关闭");
		
	}

}
