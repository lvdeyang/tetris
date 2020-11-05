package com.sumavision.signal.bvc.network.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sumavision.signal.bvc.mq.bo.ChannelBO;
import com.sumavision.signal.bvc.network.po.NetworkInputPO;
import com.sumavision.signal.bvc.network.po.NetworkMapPO;
import com.sumavision.signal.bvc.network.po.NetworkOutputPO;

@Service
public class NetworkQuery {

	/**
	 * 判断输入资源是否创建<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月23日 上午11:29:04
	 * @param List<NetworkInputPO> inputs 已创建的输入资源
	 * @param String channelId 通道id
	 * @return boolean 是/否
	 */
	public boolean isCreateInput(List<NetworkInputPO> inputs, String channelId) throws Exception{
		
		if(inputs != null && inputs.size() > 0){
			for(NetworkInputPO input: inputs){
				if(input.getChannelId().equals(channelId)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 查询网络调度输入<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 上午11:15:41
	 * @param List<NetworkInputPO> inputs 输入列表
	 * @param String bundleId 设备id
	 * @param String channelId 通道id
	 * @return NetworkInputPO 网络调度输入
	 */
	public NetworkInputPO queryInput(List<NetworkInputPO> inputs, String bundleId, String channelId) throws Exception{
		
		for(NetworkInputPO input: inputs){
			if(input.getBundleId().equals(bundleId) && input.getChannelId().equals(channelId)){
				return input;
			}
		}
		
		return null;
	}
	
	/**
	 * 查询网络调度输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 上午11:39:18
	 * @param List<NetworkOutputPO> outputs 输出列表
	 * @param String bundleId 设备id
	 * @param String channelId 通道id
	 * @return NetworkOutputPO 网络调度输出
	 */
	public NetworkOutputPO queryOutput(List<NetworkOutputPO> outputs, String bundleId, String channelId) throws Exception{
		
		for(NetworkOutputPO output: outputs){
			if(output.getBundleId().equals(bundleId) && output.getChannelId().equals(channelId)){
				return output;
			}
		}
		
		return null;
	}
	
	/**
	 * 查询网络调度切换数据<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月24日 下午1:33:01
	 * @param List<NetworkMapPO> maps 切换数据列表
	 * @param Long outputId 输出id
	 * @return NetworkMapPO 切换数据信息
	 */
	public NetworkMapPO queryMap(List<NetworkMapPO> maps, Long outputId) throws Exception{
		
		for(NetworkMapPO map: maps){
			if(map.getOutputId().equals(outputId)){
				return map;
			}
		}
		
		return null;
	}
	
}
