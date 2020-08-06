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
import com.sumavision.signal.bvc.network.bo.CutSwitchResponseBO;
import com.sumavision.signal.bvc.network.bo.SwitchRequestBO;
import com.sumavision.signal.bvc.network.bo.SwitchResponseBO;

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
	
	/**
	 * 切换数据流<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 上午10:19:53
	 * @param String netIp 网络ip
	 * @param List<SwitchRequestBO> requests 切换数据流请求
	 * @return List<SwitchResponseBO> 返回
	 */
	public List<SwitchResponseBO> switchData(String netIp, List<SwitchRequestBO> requests) throws Exception{
		
		String response = HttpClient.put("http://" + netIp + ":8100" + "/nm/Switch/Sid", JSON.toJSONString(requests));
		
		List<SwitchResponseBO> switchResponse = JSONArray.parseArray(response, SwitchResponseBO.class);
		
		return switchResponse;
	}
	
	/**
	 * 销毁网络资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 上午10:38:52
	 * @param String netIp 网络ip
	 * @param List<Integer> sids 会话id数组
	 */
	public void destory(String netIp, List<Integer> sids) throws Exception{
		
		HttpClient.delete("http://" + netIp + ":8100" + "/nm/DestroyResource", JSON.toJSONString(sids));
		
	}
	
	/**
	 * 断开切换<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午3:29:27
	 * @param String netIp 网络ip
	 * @param List<Long> sids 输出sid列表
	 * @return List<CutSwitchResponseBO> 断开切换返回
	 */
	public List<CutSwitchResponseBO> cutSwitch(String netIp, List<Long> sids) throws Exception{
		
		List<JSONObject> requests = new ArrayList<JSONObject>();
		for(Long sid: sids){
			JSONObject request = new JSONObject();
			request.put("sid_out", sid);
			requests.add(request);
		}
		
		String response = HttpClient.put("http://" + netIp + ":8100" + "/nm/BreakSwitch/Sid", JSON.toJSONString(requests));
		
		List<CutSwitchResponseBO> cutSwitchResponse = JSONArray.parseArray(response, CutSwitchResponseBO.class);
		
		return cutSwitchResponse;
	}
	
}
