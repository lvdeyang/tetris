package com.sumavision.tetris.cs.channel.broad.ability;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.cs.bak.AbilityInfoSendPO;
import com.sumavision.tetris.cs.bak.AbilityInfoSendQuery;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.broad.ability.transcode.BroadTranscodeOutputDAO;
import com.sumavision.tetris.cs.channel.broad.ability.transcode.BroadTranscodeOutputPO;
import com.sumavision.tetris.cs.channel.exception.ChannelAbilityRequestErrorException;
import com.sumavision.tetris.easy.process.stream.transcode.OutParamVO;
import com.sumavision.tetris.easy.process.stream.transcode.TaskVO;

@Component
public class BroadAbilityQuery {
	@Autowired
	private AbilityInfoSendQuery abilityInfoSendQuery;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	@Autowired
	private BroadTranscodeOutputDAO broadTranscodeOutputDAO;
	
	@Autowired
	private BroadAbilityRemoteDAO broadAbilityRemoteDAO;
	
	@Autowired
	private ChannelDAO channelDao;
	
	@Autowired
	private Adapter adapter;

	/**
	 * 根据用户id获取播发信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月6日 下午5:10:08
	 * @param Long userId 用户id
	 * @return List<BroadAbilityBroadInfoVO> 播发信息
	 */
	public Long getChannelIdFromUser(Long userId) throws Exception {
		List<BroadAbilityBroadInfoVO> broadInfoVOs = broadAbilityBroadInfoService.queryFromUserIds(new ArrayListWrapper<Long>().add(userId).getList());
		return (broadInfoVOs == null || broadInfoVOs.isEmpty()) ? null : broadInfoVOs.get(0).getChannelId();
	}
	
	/**
	 * 根据频道id查询能力播发下发类型<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return 下发类型
	 */
	public BroadAbilityQueryType broadCmd(Long channelId) throws Exception{
		List<AbilityInfoSendPO> info = abilityInfoSendQuery.getByChannelId(channelId);
		if (info == null || info.isEmpty()) {
			return BroadAbilityQueryType.NEW;
		}else if (abilityInfoSendQuery.checkBroadInfoChanged(channelId)) {
			return BroadAbilityQueryType.CHANGE;
		}else {
			return BroadAbilityQueryType.COVER;
		}
	}
	
	/**
	 * 播发成功后保留播发信息(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public void saveBroad(Long channelId) throws Exception{
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel != null) {
			abilityInfoSendQuery.save(channelId, channel.getEncryption());
		}
	}
	
	/**
	 * 播发成功后保留播发信息(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public void saveBroad(Long channelId, List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs) throws Exception{
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel != null) {
			abilityInfoSendQuery.save(channelId, channel.getEncryption(), broadAbilityBroadInfoVOs);
		}
	}
	
	/**
	 * 能力播发相关http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param BroadAbilityQueryType type 请求类型
	 * @param ChannelPO channel 频道
	 */
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel) throws Exception{
		return sendAbilityRequest(type, channel, null, null, null);
	}
	
	/**
	 * 能力播发相关http请求(用于跳转)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param BroadAbilityQueryType type 请求类型
	 * @param ChannelPO channel 频道
	 * @param Long duration 跳转值(仅SEEK使用)
	 */
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, Long duration) throws Exception{
		return sendAbilityRequest(type, channel, null, null, duration);
	}
	
	/**
	 * 能力播发相关http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param BroadAbilityQueryType type 请求类型
	 * @param ChannelPO channel 频道
	 * @param List<String> input 播发媒资列表(有顺序，仅COVER和NEW播发使用)
	 * @param JSONObject output 能力端的输出(仅NEW播发使用)
	 */
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, List<String> input, JSONObject output) throws Exception{
		return sendAbilityRequest(type, channel, input, output, null);
	}
	
	/**
	 * 能力播发相关http请求<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param BroadAbilityQueryType type 请求类型
	 * @param ChannelPO channel 频道
	 * @param List<String> input 播发媒资列表(有顺序，仅COVER和NEW播发使用)
	 * @param JSONObject output 能力端的输出(仅NEW播发使用)
	 * @param Long duration 跳转值(仅SEEK使用)
	 * @throws Exception 
	 */
	public boolean sendAbilityRequest(BroadAbilityQueryType type, ChannelPO channel, List<String> input, JSONObject output, Long duration) throws Exception{
		JSONObject request = new JSONObject();
		request.put("id", channel.getAbilityBroadId());
		if (BroadAbilityQueryType.COVER == type) {
			request.put("cmd", type.getCmd());
			request.put("input", input);
			request.put("stat", 2);
		} else if (BroadAbilityQueryType.NEW == type) {
			request.put("cmd", type.getCmd());
			request.put("output", output);
			request.put("loop_count", "1");
			request.put("input", input);
			request.put("stat", 2);
		} else if (BroadAbilityQueryType.START == type) {
			request.put("cmd", type.getCmd());
		}else if (BroadAbilityQueryType.STOP == type || BroadAbilityQueryType.DELETE == type) {
			request.put("cmd", type.getCmd());
		} else if (BroadAbilityQueryType.CHANGE == type) {
			if (sendAbilityRequest(BroadAbilityQueryType.DELETE, channel)) {
				return sendAbilityRequest(BroadAbilityQueryType.NEW, channel, input, output);
			} else {
				throw new ChannelAbilityRequestErrorException(BroadAbilityQueryType.NEW.getRemark());
			}
		} else if (BroadAbilityQueryType.SEEK == type) {
			request.put("cmd", type.getCmd());
			request.put("duration", duration);
		}
		System.out.println(request.toJSONString());
		JSONObject response = HttpRequestUtil.httpPost("http://" + adapter.getServerUrl(BroadWay.ABILITY_BROAD), request);
//		if (response != null && response.containsKey("stat") && response.getString("stat").equals("success")) {
			return true;
//		}else {
//			throw new ChannelAbilityRequestErrorException(type.getRemark());
//		}
	}
	
	/**
	 * 根据频道id获取流转码参数<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午5:19:54
	 * @param Long channelId 频道id
	 */
	public TaskVO queryTaskFromChannelId(Long channelId) throws Exception {
		//获取流转码参数
		BroadAbilityRemotePO abilityRemotePO = broadAbilityRemoteDAO.findByChannelId(channelId);
		if (abilityRemotePO == null) return null;
		String streamTranscodeInfo = abilityRemotePO.getTranscodeInfo();
		TaskVO taskVO = JSONObject.parseObject(streamTranscodeInfo, TaskVO.class);
		List<OutParamVO> outParamVOs = taskVO.getOutParam() == null ? new ArrayList<OutParamVO>() : taskVO.getOutParam();
		outParamVOs.addAll(queryNewOutPutFromChannelId(channelId));
		taskVO.setOutParam(outParamVOs);
		return taskVO;
	}
	
	/**
	 * 根据频道id获取后添加的输出<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午5:16:36
	 * @param Long channelId 频道id
	 * @return List<OutParamVO> 输出数组
	 */
	public List<OutParamVO> queryNewOutPutFromChannelId(Long channelId) throws Exception {
		List<BroadTranscodeOutputPO> outputPOs = broadTranscodeOutputDAO.findByChannelId(channelId);
		List<OutParamVO> outParamVOs = new ArrayList<OutParamVO>();
		for (BroadTranscodeOutputPO outputPO : outputPOs) {
			OutParamVO outParamVO = new OutParamVO();
			outParamVO.setLocalIp(outputPO.getLocalIp())
			.setOutputUrl(outputPO.getOutputUrl());
			outParamVOs.add(outParamVO);
		}
		return outParamVOs;
	}
}
