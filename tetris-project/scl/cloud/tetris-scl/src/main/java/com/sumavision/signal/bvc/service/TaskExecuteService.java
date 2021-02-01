package com.sumavision.signal.bvc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.entity.dao.TaskDAO;
import com.sumavision.signal.bvc.entity.enumeration.TaskStatus;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.po.TaskPO;
import com.sumavision.signal.bvc.http.HttpAsyncClient;
import com.sumavision.signal.bvc.http.HttpClient;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskExecuteService {
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TaskExecuteService taskExecuteService;
	
	/**
	 * 转发器授权<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午7:16:43
	 * @param repeater
	 * @return
	 * @throws Exception
	 */
	public String taskAuthPost(RepeaterPO repeater) throws Exception{
		
		Long id = 123l;
		
		JSONObject param = new JSONObject();
		param.put("id", id);
		param.put("cmd", "ts_task_auth");
		param.put("authid", "kysx");
		
		//授权
		String response = HttpClient.post("http://" + repeater.getIp() + ":8290", param.toJSONString());

		return response;
	}
	
	/**
	 * 转发器创建任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午7:18:01
	 * @param repeater
	 * @param authId
	 * @param mapping
	 * @throws Exception
	 */
	public void taskCreatePost(String ip, PortMappingPO mapping, String param4, Long param5) throws Exception{
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("dip", mapping.getDstAddress());
		body.put("dport", mapping.getDstPort());
		
		json.put("id", id);
		json.put("cmd", "ts_task_create");
		json.put("authid", "suma");
		json.put("body", body);
		
		System.out.println("创建任务发送， 集群ip:" + ip + "," + json.toJSONString());
		
		HttpAsyncClient.getInstance().httpAsyncPost("http://" + ip + ":8290", json.toJSONString(), null, new TaskCreateCallBack(mapping.getId(), taskDao, ip, param4, param5, taskExecuteService, json));

	}
	
	/**
	 * 转发器创建任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午7:18:01
	 */
	public void taskCreatePost(String ip, Long mappingId, String dstIp, Long dstPort, String param4, Long param5) throws Exception{
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("dip", dstIp);
		body.put("dport", dstPort);
		
		json.put("id", id);
		json.put("cmd", "ts_task_create");
		json.put("authid", "suma");
		json.put("body", body);
		
		System.out.println("创建任务发送， 集群ip:" + ip + "," + json.toJSONString());
		
		HttpAsyncClient.getInstance().httpAsyncPost("http://" + ip + ":8290", json.toJSONString(), null, new TaskCreateCallBack(mappingId, taskDao, ip, param4, param5, taskExecuteService, json));

	}
	
	/**
	 * 转发器切换任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午7:18:35
	 * @param repeater
	 * @param task
	 * @param mappingPO
	 * @throws Exception
	 */
	public void taskSwitch(TaskPO task, String address, Long port) throws Exception{
		
		if(!task.getStatus().equals(TaskStatus.zero.getStatus())) return;
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		JSONObject body = new JSONObject();
		JSONObject expand = new JSONObject();
		
		expand.put("V-type", 0l);
		expand.put("A-type1", 0l);
		expand.put("A-type2", 0l);
		expand.put("PMT", 0l);
		
		body.put("tid", Long.valueOf(task.getTaskId()));
		body.put("sip", address);
		body.put("sport", port);
		body.put("rcvip", address);
		body.put("expand", expand);
		
		json.put("id", id);
		json.put("cmd", "ts_switch");
		json.put("authid", "suma");
		json.put("body", body);
		
		System.out.println("切换任务发送， 集群ip:" + task.getIp() + "," + json.toJSONString());
		
		HttpAsyncClient.getInstance().httpAsyncPost("http://" + task.getIp() + ":8290", json.toJSONString(), null, new TaskSwitchCallBack(Long.valueOf(task.getTaskId()), json, taskDao));
	}
	
	/**
	 * 转发器销毁任务<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月28日 下午3:03:50
	 * @param repeater
	 * @param task
	 * @throws Exception
	 */
	public void taskDestory(TaskPO task) throws Exception{
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		JSONObject body = new JSONObject();
		
		body.put("tid", Long.valueOf(task.getTaskId()));
		
		json.put("id", id);
		json.put("cmd", "ts_task_destroy");
		json.put("authid", "suma");
		json.put("body", body);
		
		System.out.println("销毁任务发送， 集群ip:" + task.getIp() + "," + json.toJSONString());
		
		HttpAsyncClient.getInstance().httpAsyncPost("http://" + task.getIp() + ":8290", json.toJSONString(), null, new TaskDestoryCallBack(task.getTaskId(), taskDao));
		
	}
	
	/**
	 * 控制网口开关<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月10日 下午2:27:42
	 * @param RepeaterPO repeater 流转发器
	 * @param boolean open 打开/关闭
	 * @throws Exception
	 */
	public void gbeControl(RepeaterPO repeater, boolean open) throws Exception{
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		JSONObject body = new JSONObject();
		
		body.put("devIp", repeater.getAddress());
		body.put("port", -1l);
		
		if(open){
			body.put("flag", 1l);
		}else{
			body.put("flag", 0l);
		}
		
		json.put("id", id);
		json.put("cmd", "ts_dev_gbe_control");
		json.put("authid", "suma");
		json.put("body", body);
		
		System.out.println("控制网口开关， 集群ip:" + repeater.getIp() + "," + json.toJSONString());
		
		HttpAsyncClient.getInstance().httpAsyncPost("http://" + repeater.getIp()+ ":8290", json.toJSONString(), null, null);

	}
	
	/**
	 * 流转发器任务重置<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月15日 下午4:39:59
	 * @param RepeaterPO repeater 流转发器信息
	 */
	public String resetDevice(RepeaterPO repeater) throws Exception{
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		JSONObject body = new JSONObject();
		
		body.put("devIp", repeater.getAddress());
		
		json.put("id", id);
		json.put("cmd", "ts_dev_reset");
		json.put("authid", "suma");
		json.put("body", body);
		
		System.out.println("流转发器任务重置， 集群ip:" + repeater.getIp() + "," + json.toJSONString());
		
		String response = HttpClient.post("http://" + repeater.getIp()+ ":8290", json.toJSONString());
		//HttpAsyncClient.getInstance().httpAsyncPost("http://" + repeater.getIp()+ ":8290", json.toJSONString(), null, null);
		
		return response;
	}
	
	/**
	 * 获取一个转发器的所有任务id<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月21日 上午9:31:48
	 */
	public List<Long> getTaskIds(RepeaterPO repeater) throws Exception{
		
		Long id = 123l;
		
		JSONObject json = new JSONObject();
		
		json.put("id", id);
		json.put("authid", "suma");
		json.put("cmd", "ts_task_id_get");
		
		System.out.println("流转发器任务获取， 集群ip:" + repeater.getIp() + "," + json.toJSONString());
		
		String response = HttpClient.post("http://" + repeater.getIp()+ ":8290", json.toJSONString());
		
		Long status = JSON.parseObject(response).getLong("result");
		if(status.equals(0l)){
			return JSONArray.parseArray(JSON.parseObject(response).getString("body"), Long.class);
		}else{
			throw new Exception(TaskStatus.fromNum(status).getMessage());
		}
	}
	
	public static void main(String[] args) throws Exception{
		
//		String baseUrl = "http://192.165.58.17:8290";
//		
//		Long id = 123l;
//		HttpPost httpPost = new HttpPost(baseUrl);
//		JSONObject json = new JSONObject();
//		JSONObject body = new JSONObject();
//		body.put("dip", "192.165.56.84");
//		body.put("dport", "12345");
//		
//		json.put("id", id);
//		json.put("cmd", "ts_task_create");
//		json.put("authid", "suma");
//		json.put("body", body);
//		StringEntity entity = new StringEntity(json.toJSONString(), "UTF-8");  
//		entity.setContentEncoding("UTF-8");  
//		entity.setContentType("application/json");  
//		httpPost.setEntity(entity); 
		
		/*HttpPost httpPost1 = new HttpPost("/");
		
		JSONObject json1 = new JSONObject();
		JSONObject body1 = new JSONObject();
		body1.put("dip", "192.165.56.84");
		body1.put("dport", "12346");
		
		json1.put("id", id);
		json1.put("cmd", "ts_task_create");
		json1.put("authid", "suma");
		json1.put("body", body1);
		
		StringEntity entity1 = new StringEntity(json1.toJSONString(), "UTF-8");  
		entity1.setContentEncoding("UTF-8");  
		entity1.setContentType("application/json");  
		httpPost1.setEntity(entity1);*/  
		
//		CloseableHttpPipeliningClient client = HttpAsyncClients.createPipelining();
//		client.start();
//		HttpHost target = new HttpHost("192.165.58.17", 8290);
//		List<HttpRequest> posts = new ArrayList<HttpRequest>();
//		for(int i=0; i<1; i++){
//			posts.add(httpPost);
//		}
//		Future<List<HttpResponse>> f = client.execute(target, posts, null);
//		List<HttpResponse> responses = f.get();
//		for(HttpResponse response:responses){
//			System.out.println(response.getEntity().toString());
//		}
		
		/*Thread.sleep(5000);
		client.execute(target, posts, null);*/
		
//		for(int i=0; i<2; i++){
//			HttpAsyncClient.getInstance().httpAsyncPost(httpPost, json.toJSONString(), null, null);
//		}
		
		//http://192.165.56.71:8087/demo/test
		
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
		HttpPost httpPost = new HttpPost("http://192.165.56.136:8290");
		
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setMaxTotal(1);
		cm.setDefaultMaxPerRoute(5);
		
		ConnectionKeepAliveStrategy kaStrategy = new DefaultConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (keepAlive == -1) {
					keepAlive = 60000;
				}
				return keepAlive;
			}

		};
		
		CloseableHttpAsyncClient client = HttpAsyncClients.custom()
														  .setConnectionManager(cm)
														  .setKeepAliveStrategy(kaStrategy)
														  .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
														  .build();
		client.start();
		for(int i=0; i<1000; i++){
			
			int id = 123;

			JSONObject json = new JSONObject();
			JSONObject body = new JSONObject();
			body.put("dip", "192.165.56.84");
			body.put("dport", 33333 + i);
			
			json.put("id", id);
			json.put("cmd", "ts_task_create");
			json.put("authid", "oooo");
			json.put("body", body);
			
			StringEntity entity = new StringEntity(json.toJSONString(), "UTF-8");  
			/*entity.setContentEncoding("UTF-8");*/  
			entity.setContentType("application/json"); 
			httpPost.setEntity(entity); 
			
			client.execute(httpPost, new FutureCallback<HttpResponse>(){

				@Override
				public void completed(HttpResponse result) {
					BufferedReader bReader = null;
					try{
						HttpEntity entity = result.getEntity();
				 	    InputStream in = entity.getContent();
				 	    StringBuilder sBuilder = new StringBuilder();
				 	    String line = "";
				 	    bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				 	    while ((line=bReader.readLine()) != null) {
				 	    	sBuilder.append(line);
				 		}
				 	    System.out.println("*****************************");
				 	    System.out.println(sBuilder.toString());
				 	    EntityUtils.consume(entity);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if(bReader != null){
							try {
								bReader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				}

				@Override
				public void failed(Exception ex) {
					ex.printStackTrace();
					
				}

				@Override
				public void cancelled() {
					System.out.println("异常拉");
					
				}
				
			});
			//Thread.sleep(1000);
		}

		/*HttpAsyncClient.getInstance().httpAsyncPost(httpPost, json.toJSONString(), null, new FutureCallback<HttpResponse>(){

			@Override
			public void completed(HttpResponse result) {
				BufferedReader bReader = null;
				try{
					HttpEntity entity = result.getEntity();
			 	    InputStream in = entity.getContent();
			 	    StringBuilder sBuilder = new StringBuilder();
			 	    String line = "";
			 	    bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			 	    while ((line=bReader.readLine()) != null) {
			 	    	sBuilder.append(line);
			 		}
			 	    System.out.println("*****************************");
			 	    System.out.println(sBuilder.toString());
			 	    EntityUtils.consume(entity);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(bReader != null){
						try {
							bReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}

			@Override
			public void failed(Exception ex) {
				ex.printStackTrace();
				
			}

			@Override
			public void cancelled() {
				System.out.println("异常拉");
				
			}
			
		});*/
	}
}
