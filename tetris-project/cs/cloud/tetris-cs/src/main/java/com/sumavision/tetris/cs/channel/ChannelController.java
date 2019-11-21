package com.sumavision.tetris.cs.channel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.cs.channel.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.ability.BroadAbilityQueryType;
import com.sumavision.tetris.cs.channel.exception.ChannelNotExistsException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/cs/channel")
public class ChannelController {

	@Autowired
	private ChannelQuery channelQuery;

	@Autowired
	private ChannelDAO channelDao;

	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private UserQuery userQuery;

	/**
	 * 分页获取频道列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Integer currentPage 当前页
	 * @param Integer pageSize 分页大小
	 * @return List<ChannelVO> channels 频道列表
	 * @return Long total 频道总数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object channelList(Integer currentPage, Integer pageSize, HttpServletRequest request) throws Exception {

		return channelQuery.findAll(currentPage, pageSize, ChannelType.LOCAL);
	}

	/**
	 * 添加频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param String name 频道名称
	 * @param String date 日期
	 * @param String broadWay 播发方式(参考BroadWay枚举)
	 * @param String previewUrlIp 能力的输出地址ip(仅限能力播发)
	 * @param String previewUrlPort 能力的输出地址port(仅限能力播发)
	 * @param String remark 备注
	 * @return ChannelVO 频道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(String name,
			String date,
			String broadWay,
			String outputUsers,
			String outputUserPort,
			String output,
			String remark,
			Boolean encryption,
			Boolean autoBroad,
			Boolean autoBroadShuffle,
			Integer autoBroadDuration,
			String autoBroadStart,
			HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs = JSONArray.parseArray(output, BroadAbilityBroadInfoVO.class);
		
		List<UserVO> outputUserList = new ArrayList<UserVO>();
		if (outputUsers != null) {
			outputUserList = JSONArray.parseArray(outputUsers, UserVO.class);
		}

		ChannelPO channel = channelService.add(name, date, broadWay, remark, ChannelType.LOCAL, encryption, autoBroad, autoBroadShuffle, autoBroadDuration, autoBroadStart, outputUserPort, outputUserList, abilityBroadInfoVOs);
		
		if (BroadWay.fromName(broadWay).equals(BroadWay.ABILITY_BROAD) && autoBroad) {
			channelService.autoAddSchedulesAndBroad(channel.getId());
		}

		return new ChannelVO().set(channel).setOutput(abilityBroadInfoVOs).setOutputUsers(outputUserList);
	}

	/**
	 * 编辑频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 频道id
	 * @param String name 频道名称
	 * @param String broadWay 播发方式(参考BroadWay枚举)
	 * @param String previewUrlIp 能力的输出地址ip(仅限能力播发)
	 * @param String previewUrlPort 能力的输出地址port(仅限能力播发)
	 * @param String remark 备注
	 * @return ChannelVO 频道
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object rename(
			Long id,
			String name,
			String outputUsers,
			String outputUserPort,
			String output,
			String remark,
			Boolean encryption,
			Boolean autoBroad,
			Boolean autoBroadShuffle,
			Integer autoBroadDuration,
			String autoBroadStart,
			HttpServletRequest request) throws Exception {
		
		List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs = JSONArray.parseArray(output, BroadAbilityBroadInfoVO.class);
		
		List<UserVO> outputUserList = new ArrayList<UserVO>();
		if (outputUsers != null) {
			outputUserList = JSONArray.parseArray(outputUsers, UserVO.class);
		}

		ChannelPO channel = channelService.edit(id, name, remark, encryption, autoBroad, autoBroadShuffle, autoBroadDuration, autoBroadStart, outputUserPort, outputUserList, abilityBroadInfoVOs);
		
		if (BroadWay.fromName(channel.getBroadWay()).equals(BroadWay.ABILITY_BROAD) && autoBroad) {
			channelService.autoAddSchedulesAndBroad(channel.getId());
		}

		return new ChannelVO().set(channel);
	}

	/**
	 * 删除频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 频道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(Long id, HttpServletRequest request) throws Exception {

		channelService.remove(id);

		return "";
	}
	
	/**
	 * 开始播发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/start")
	public Object broadcastStart(Long channelId, HttpServletRequest request) throws Exception {
		channelService.startBroadcast(channelId);
		return "";
	}
	
	/**
	 * 停止播发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/stop")
	public Object broadcastStop(Long channelId, HttpServletRequest request) throws Exception {
		channelService.stopBroadcast(channelId);
		return "";
	}
	
	/**
	 * 查询播发状态<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/status")
	public Object broadcastStatus(Long channelId, HttpServletRequest request) throws Exception {
		
		ChannelPO channelPO = channelDao.findOne(channelId);
		if (channelPO == null) {
			throw new ChannelNotExistsException(channelId);
		}
		
		if (channelPO.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName())) {
			return channelPO.getBroadcastStatus();
		}

		return channelService.getChannelBroadstatus(channelId);
	}
	
	/**
	 * 重新播发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/restart")
	public Object broadcastRestart(Long channelId, HttpServletRequest request) throws Exception {

		return channelService.restartBroadcast(channelId);
	}
	
	/**
	 * 播发跳转(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param Long duration 跳转量(单位s。支持负值)
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/seek")
	public Object seek(Long channelId, Long duration, HttpServletRequest request) throws Exception {
		
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) {
			throw new ChannelNotExistsException(channelId);
		}

		return channelQuery.sendAbilityRequest(BroadAbilityQueryType.SEEK, channel, duration);
	}
	
	/**
	 * 获取用户列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月31日 下午1:47:40
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/user/list")
	public Object questUserList(HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<UserVO> users = 
				userQuery.listByCompanyIdWithExceptAndClassify(Long.parseLong(user.getGroupId()), null, UserClassify.COMPANY);
		
		return users;
	}
}
