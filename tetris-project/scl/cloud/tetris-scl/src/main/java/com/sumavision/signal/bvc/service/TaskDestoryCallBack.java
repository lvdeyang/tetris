package com.sumavision.signal.bvc.service;

import org.apache.http.message.BasicHttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.http.HttpCallBack;

/**
 * 转发器任务销毁返回<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月28日 下午2:55:42
 */
public class TaskDestoryCallBack extends HttpCallBack<String, Object, String, Object, Object, Object, Object>{

	public TaskDestoryCallBack(String param1) {
		super(param1);
	}
	
	public TaskDestoryCallBack(String param1, Object param2) {
		super(param1, param2);
	}
	
	public TaskDestoryCallBack(String param1, Object param2, String param3) {
		super(param1, param2, param3);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			String taskId = this.getParam1();
			
			System.out.println("taskId为:" + taskId +"的销毁任务返回：" + res);
			
			JSONObject jsonObject = JSON.parseObject(res);
			jsonObject.put("taskId", taskId);
			
			TaskDestoryQueue.getInstance().put(jsonObject.toJSONString());
			
//			String status = JSON.parseObject(res).getString("result");
//			
//			//0代表销毁成功，3代表任务不存在，都需要删除任务数据
//			if(status.equals("0") || status.equals("3")){
//				taskDao.deleteByTaskId(taskId);
//			}else{
//				System.out.println("taskId为" + taskId + "销毁失败！");
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void failed(Exception ex) {
		String taskId = this.getParam1();
		System.out.println("销毁任务异步请求失败，任务id" + taskId);
		ex.printStackTrace();
	}

	@Override
	public void cancelled() {
		String taskId = this.getParam1();
		System.out.println("销毁任务连接关闭，任务id：" + taskId);
	}
}

