package com.sumavision.bvc.control.netmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.bvc.communication.http.HttpClient;
import com.sumavision.bvc.control.system.vo.LayerVO;
import com.sumavision.bvc.resource.dao.ResourceLayerDAO;
import com.sumavision.bvc.utils.ThreadPool;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/net")
public class NetManagerController {
	
	@Autowired
	ResourceLayerDAO conn_layer;

	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/get/{server}/{path}/{method}")
	public Object get(@PathVariable String server,@PathVariable String path, @PathVariable String method, HttpServletRequest request)
			throws Exception {
		RequestConfig config=RequestConfig.custom().setConnectionRequestTimeout(5).setConnectTimeout(1000).setSocketTimeout(1000).build();
		String header="https";
		String result = "";
		if(server.indexOf("8007")==-1){
			header="http";
			result = HttpClient.get(header+"://"+server+ "/" + path + "/" + method,config);
		}else{
//			String server2 = server.replace("8007", "8014");
			result = HttpClient.getHttps(header+"://"+server+ "/" + path + "/" + method,config);
		}
//		httpClient.get(header+"://"+server+ "/" + path + "/" + method,config);
		JSONObject data = JSONObject.parseObject(result);
		return data;
	}

	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/post/{server}/{path}/{method}")
	public Object post(@PathVariable String server,@PathVariable String path, @PathVariable String method, String layerID, String local_num,
			String local_sip, String message_service, String turn_service, String resource_ipport,
			HttpServletRequest request) throws Exception {

		String params = "{layerID:'" + layerID + "',local_num:'" + local_num + "',local_sip:'" + local_sip
				+ "',message_service:'" + message_service + "',turn_service:'" + turn_service + "',resource_ipport:'"
				+ resource_ipport + "'}";
		RequestConfig config=RequestConfig.custom().setConnectionRequestTimeout(5).build();
		String header="https";
		String result = "";
		if(path.indexOf("8007")==-1&&path.indexOf("8006")==-1){
			header="http";
			result=  HttpClient.post(header+"://"+server+"/" + path + "/" + method, params,Locale.ENGLISH,config);
		}else{
			//result=  httpClient.postHttps(header+"://"+server+"/" + path + "/" + method, params,Locale.ENGLISH,config);
		}
		
		JSONObject data = JSONObject.parseObject(result);
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/layer/load", method=RequestMethod.GET)
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		List<LayerVO> _layers = new ArrayList<LayerVO>();
		
		List<WorkNodePO> workNodes = conn_layer.findAll();
		for (WorkNodePO workNodePO : workNodes) {
			LayerVO vo=new LayerVO();
			vo.setLayerId(workNodePO.getNodeUid());
			vo.setLayerIp(workNodePO.getIp());
			vo.setTypeShowName(workNodePO.getType().getName());
			vo.setType(workNodePO.getType().toString());
			int port=0;
			String header="http";
			if(workNodePO.getType().equals(NodeType.ACCESS_JV210)){
				port=8007;
				header="https";
			}else if(workNodePO.getType().equals(NodeType.ACCESS_CDN)){
				port=8006;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_JV230)){
				port=8010;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_MIXER)){
				port=8008;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_TVOS)){
				port=8009;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_IPC)){
				
			}
			try {
				RequestConfig config=RequestConfig.custom().setConnectionRequestTimeout(5).setConnectTimeout(100).setSocketTimeout(100).build();
				String result="";
				if("https".equals(header)){
					result = HttpClient.getHttps(header+"://"+workNodePO.getIp()+":"+port +"/action/get_config",config);
				}else{
					result = HttpClient.get(header+"://"+workNodePO.getIp()+":"+port +"/action/get_config",config);
				}
				
				JSONObject data = JSONObject.parseObject(result);
				vo.setVersion(data.getString("version"));
			} catch (Exception e) {
				// TODO: handle exception
//				System.out.println(e.getMessage());
				log.error(e.getMessage());
				vo.setVersion("-");
			}
			
			_layers.add(vo);
		}
		/*List<BundlePO> bundlePOs=conn_layer.findAllMixer();
		for (BundlePO bundlePO : bundlePOs) {
			LayerVO vo=new LayerVO();
			vo.setLayerId(bundlePO.getBundleId());
			vo.setLayerIp(bundlePO.getDeviceIp());
			vo.setTypeShowName("合屏器");
			vo.setType("MIXER");
			_layers.add(vo);
		}*/
		JSONObject data = new JSONObject();
		data.put("rows", _layers);
		data.put("total", _layers.size());
		return data;
	}

	/** load()的优化，异步同时查询 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/layer/load/new", method=RequestMethod.GET)
	public Object loadNew(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		List<LayerVO> _layers = new ArrayList<LayerVO>();
		
		List<Future<String>> resultFs = new ArrayList<Future<String>>();
		
		List<WorkNodePO> workNodes = conn_layer.findAll();
		for (WorkNodePO workNodePO : workNodes) {
			int port=0;
			String header="http";
			if(workNodePO.getType().equals(NodeType.ACCESS_JV210)){
				port=8007;
				header="https";
			}else if(workNodePO.getType().equals(NodeType.ACCESS_CDN)){
				port=8006;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_JV230)){
				port=8010;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_MIXER)){
				port=8008;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_TVOS)){
				port=8009;
			}else if(workNodePO.getType().equals(NodeType.ACCESS_IPC)){
				
			}
			try {
				RequestConfig config=RequestConfig.custom().setConnectionRequestTimeout(5).setConnectTimeout(100).setSocketTimeout(100).build();
				if("https".equals(header)){
//					result = HttpClient.getHttps(header+"://"+workNodePO.getIp()+":"+port +"/action/get_config",config);
					String url = header+"://"+workNodePO.getIp()+":"+port +"/action/get_config";
					resultFs.add(CompletableFuture.supplyAsync(()->HttpClient.getHttpsNoException(url, "utf-8", config), ThreadPool.getScheduledExecutor()));
				}else{
//					result = HttpClient.get(header+"://"+workNodePO.getIp()+":"+port +"/action/get_config",config);
					String url = header+"://"+workNodePO.getIp()+":"+port +"/action/get_config";
					resultFs.add(CompletableFuture.supplyAsync(()->HttpClient.getNoException(url, config), ThreadPool.getScheduledExecutor()));
				}
			} catch (Exception e) {
//				System.out.println(e.getMessage());
				log.error(e.getMessage());
			}
		}
		
		//从异步结果中取出各接入层的version
		List<String> results = new ArrayList<String>();
		Map<String, String> versionMap = new HashMap<String, String>();
		for(Future<String> resultF : resultFs){
			String result = null;
			try {
				result = resultF.get();
				if(result != null){
					results.add(result);
					JSONObject data = JSONObject.parseObject(result);
					String layerID = data.getString("layerID");
					String version = data.getString("version");
					versionMap.put(layerID, version);
				}
			} catch (Exception e) {
				log.error("", e);
			}		
		}
		
		for (WorkNodePO workNodePO : workNodes) {
			LayerVO vo=new LayerVO();
			vo.setLayerId(workNodePO.getNodeUid());
			vo.setLayerIp(workNodePO.getIp());
			vo.setTypeShowName(workNodePO.getType().getName());
			vo.setType(workNodePO.getType().toString());
			
			String version = versionMap.get(workNodePO.getNodeUid());
			if(version != null){
				vo.setVersion(version);
			}else{
				vo.setVersion("-");
			}
			
			_layers.add(vo);
		}
		/*List<BundlePO> bundlePOs=conn_layer.findAllMixer();
		for (BundlePO bundlePO : bundlePOs) {
			LayerVO vo=new LayerVO();
			vo.setLayerId(bundlePO.getBundleId());
			vo.setLayerIp(bundlePO.getDeviceIp());
			vo.setTypeShowName("合屏器");
			vo.setType("MIXER");
			_layers.add(vo);
		}*/
		JSONObject data = new JSONObject();
		data.put("rows", _layers);
		data.put("total", _layers.size());
		return data;
	}
}
