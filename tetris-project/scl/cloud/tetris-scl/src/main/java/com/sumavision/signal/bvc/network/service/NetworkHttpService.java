package com.sumavision.signal.bvc.network.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.http.HttpClient;
import com.sumavision.signal.bvc.network.bo.CreateInputResponseBO;
import com.sumavision.signal.bvc.network.bo.CreateOutputResponseBO;

@Service
public class NetworkHttpService {

	/**
	 * 创建输入网络资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 上午11:41:17
	 * @param String netIp 网络ip
	 * @param String netId 网络id
	 * @param int count 个数
	 * @return List<CreateInputResponseBO> 返回数据
	 */
	public List<CreateInputResponseBO> createInput(String netIp, String netId, int count) throws Exception{
		
		List<JSONObject> requests = new ArrayList<JSONObject>();
		
		for(int i=0; i<count; i++){
			JSONObject request = new JSONObject();
			request.put("net_id", netId);
			request.put("stream_type", 0);
			requests.add(request);
		}
		
		String response = HttpClient.post("http://" + netIp + ":8100" + "/nm/CreateResource/0", JSON.toJSONString(requests));
		
		List<CreateInputResponseBO> createInputResponse = JSONArray.parseArray(response, CreateInputResponseBO.class);
		
		return createInputResponse;
	}
	
	/**
	 * 创建输出网络资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月16日 下午1:28:13
	 * @param String netIp 网络ip
	 * @param String netId 网络id
	 * @param String deviceIp 设备ip
	 * @param List<String> ports 设备端口
	 * @return List<CreateOutputResponseBO>
	 */
	public List<CreateOutputResponseBO> createOutput(String netIp, String netId, String deviceIp, List<String> ports) throws Exception{
		
		List<JSONObject> requests = new ArrayList<JSONObject>();
		
		for(String port: ports){
			JSONObject request = new JSONObject();
			request.put("net_id", netId);
			request.put("ip", deviceIp);
			request.put("port", port);
			request.put("stream_type", 0);
			requests.add(request);
		}
		
		String response = HttpClient.post("http://" + netIp + ":8100" + "/nm/CreateResource/1", JSON.toJSONString(requests));
		
		List<CreateOutputResponseBO> createOutputResponse = JSONArray.parseArray(response, CreateOutputResponseBO.class);
		
		return createOutputResponse;
	}
	
	
}
