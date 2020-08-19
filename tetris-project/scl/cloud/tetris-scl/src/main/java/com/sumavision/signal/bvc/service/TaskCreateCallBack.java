package com.sumavision.signal.bvc.service;

import java.util.Date;

import org.apache.http.message.BasicHttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.http.HttpCallBack;

/**
 * 创建任务异步返回<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月24日 上午11:04:04
 */
public class TaskCreateCallBack extends HttpCallBack<Long, Object, String, String, Long, Object, JSONObject>{ 
	
	public TaskCreateCallBack(Long param1, Object param2, String param3, String param4, Long param5, Object param6, JSONObject param7) {
		super(param1, param2, param3, param4, param5, param6, param7);
	}

	@Override
	public void completed(Object result) {
		try{
			BasicHttpResponse response = (BasicHttpResponse)result;
			String res = this.parseResponse(response);
			Long mappingId = this.getParam1();
			String ip = this.getParam3();
			JSONObject json = this.getParam7();
			String address = this.getParam4();
			Long port = this.getParam5();
			
			//创建任务的参数
			JSONObject body = json.getJSONObject("body");
			String dip = body.getString("dip");
			String dport = body.getString("dport");
			
			JSONObject jsonObject = JSON.parseObject(res);
			jsonObject.put("mappingId", mappingId);
			jsonObject.put("ip", ip);
			jsonObject.put("address", address);
			jsonObject.put("port", port);
			jsonObject.put("dip", dip);
			jsonObject.put("dport", dport);
			
			TaskCreateQueue.getInstance().put(jsonObject.toJSONString());
			
//			TaskDAO taskDao = SpringContext.getBean(TaskDAO.class);
//			
			System.out.println(new Date().getTime() + "  mappingId为:" + mappingId + "ip为" + ip + "的新建任务返回：" + res);
//			
//			Long status = JSON.parseObject(res).getLong("result");
//			
//			TaskPO task = taskDao.findByMappingIdAndIp(mappingId, ip);
//			if(task == null){
//				task = new TaskPO();
//				task.setMappingId(mappingId);
//				task.setIp(ip);
//				task.setDstIp(dip);
//				task.setDstPort(dport);
//			}
//			
//			task.setStatus(TaskStatus.fromNum(status).getStatus());
//			task.setMessage(TaskStatus.fromNum(status).getMessage());
//			
//			if(status.equals(0l)){
//
//				JSONObject bodyJson = JSON.parseObject(JSON.parseObject(res).getString("body"));
//				
//				String taskId = bodyJson.getString("tid");
//				String outIp = bodyJson.getString("outip");
//				
//				task.setTaskId(taskId);
//				task.setOutIp(outIp);
//				
//				if(address != null && port != null){
//					TaskExecuteService taskExecuteService = SpringContext.getBean(TaskExecuteService.class);
//					taskExecuteService.taskSwitch(task, address, port);
//				}
//				
//			}else{
//				
//				System.out.println("新建任务失败！");
//			}	
//			
//			taskDao.save(task);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void failed(Exception ex) {
		Long mappingId = this.getParam1();
		String ip = this.getParam3();
		JSONObject json = this.getParam7();
		
		//创建任务的参数
		JSONObject body = json.getJSONObject("body");
		String dip = body.getString("dip");
		String dport = body.getString("dport");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", TaskStatus.negative.getNum());
		jsonObject.put("mappingId", mappingId);
		jsonObject.put("ip", ip);
		jsonObject.put("dip", dip);
		jsonObject.put("dport", dport);
		
		TaskCreateQueue.getInstance().put(jsonObject.toJSONString());
		
//		TaskDAO taskDao = SpringContext.getBean(TaskDAO.class);
//		
//		TaskPO task = taskDao.findByMappingIdAndIp(mappingId, ip);
//		if(task == null){
//			task = new TaskPO();
//			task.setMappingId(mappingId);
//			task.setIp(ip);
//			task.setDstIp(dip);
//			task.setDstPort(dport);
//		}
//		task.setStatus(TaskStatus.negative.getStatus());
//		task.setMessage(TaskStatus.negative.getMessage());
//		
//		taskDao.save(task);
		
		System.out.println("新建任务异常,集群超时，mappingId为：" + mappingId);
		ex.printStackTrace();
		
	}

	@Override
	public void cancelled() {
		Long mappingId = this.getParam1();
		String ip = this.getParam3();
		JSONObject json = this.getParam7();
		
		//创建任务的参数
		JSONObject body = json.getJSONObject("body");
		String dip = body.getString("dip");
		String dport = body.getString("dport");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", TaskStatus.negative);
		jsonObject.put("mappingId", mappingId);
		jsonObject.put("ip", ip);
		jsonObject.put("dip", dip);
		jsonObject.put("dport", dport);
		
		TaskCreateQueue.getInstance().put(jsonObject.toJSONString());
		
//		TaskDAO taskDao = SpringContext.getBean(TaskDAO.class);
//		
//		TaskPO task = taskDao.findByMappingIdAndIp(mappingId, ip);
//		if(task == null){
//			task = new TaskPO();
//			task.setMappingId(mappingId);
//			task.setIp(ip);
//			task.setDstIp(dip);
//			task.setDstPort(dport);
//		}
//		task.setStatus(TaskStatus.negative.getStatus());
//		task.setMessage(TaskStatus.negative.getMessage());
//		
//		taskDao.save(task);	
		
		System.out.println("新建任务连接关闭，mappingId为：" + mappingId);
		
	}

}
