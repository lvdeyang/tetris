package com.sumavision.signal.bvc.service;

import java.util.Date;

import org.apache.http.message.BasicHttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.http.HttpCallBack;

/**
 * 切换任务异步返回<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月28日 上午11:04:04
 */
public class TaskSwitchCallBack extends HttpCallBack<Long, JSONObject, Object, Object, Object, Object, Object>{
	
	public TaskSwitchCallBack(Long param1, JSONObject param2, Object param3) {
		super(param1, param2, param3);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			Long taskId = this.getParam1();			
			//切换任务参数
			JSONObject json = this.getParam2();
			JSONObject body = json.getJSONObject("body");
			String sip = body.getString("sip");
			String sport = body.getString("sport");
			
			JSONObject jsonObject = JSON.parseObject(res);
			jsonObject.put("sip", sip);
			jsonObject.put("sport", sport);
			jsonObject.put("taskId", taskId);
			
			TaskSwitchQueue.getInstance().put(jsonObject.toJSONString());
			
			System.out.println(new Date().getTime() + " taskId为:" + taskId +"的切换任务返回：" + res);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void failed(Exception ex) {
		Long taskId = this.getParam1();
		System.out.println("切换任务异常，任务id：" + taskId);
		ex.printStackTrace();
		
	}

	@Override
	public void cancelled() {
		Long taskId = this.getParam1();
		System.out.println("切换任务连接关闭，任务id：" + taskId);		
	}

}

