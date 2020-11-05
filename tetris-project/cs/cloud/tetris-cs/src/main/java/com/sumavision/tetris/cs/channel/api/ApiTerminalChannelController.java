package com.sumavision.tetris.cs.channel.api;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.channel.ChannelVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/api/terminal/cs/channel")
public class ApiTerminalChannelController {
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ChannelDAO channelDAO;
	
	@Autowired
	private UserQuery userQuery;

	/**
	 * 根据id查询频道信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @return ChannelVO channel 频道信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest")
	public Object quest(HttpServletRequest request) throws Exception {
		JSONHttpServletRequestWrapper httpServletRequestWrapper = new JSONHttpServletRequestWrapper(request);
		Long id = httpServletRequestWrapper.getLong("channelId");
		return new ChannelVO().set(channelQuery.findByChannelId(id));
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
	public Object add(HttpServletRequest request) throws Exception {
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		UserVO user = userQuery.current();
		String date = requestWrapper.getString("date");
		String broadWay = requestWrapper.getString("broadWay");
		String remark = requestWrapper.getString("remark");
		String name = requestWrapper.getString("name");
		Boolean encryption = requestWrapper.getBoolean("encryption");
		
		if (date == null) date = DateUtil.now();
		if (broadWay == null) broadWay = "轮播推流";
		if (remark == null) remark = "";
		
//		ChannelPO channel = channelService.add(name, date, broadWay, remark, ChannelType.REMOTE, encryption, false, null, null, null, null, null, null);
		BroadWay channelBroadWay = BroadWay.fromName(broadWay);
		ChannelPO channel = new ChannelPO();
		channel.setName(name);
		channel.setRemark(remark);
		channel.setDate(date);
		channel.setBroadWay(channelBroadWay.getName());
		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_INIT.getName());
		channel.setGroupId(user.getGroupId());
		channel.setUpdateTime(new Date());
		channel.setEncryption(encryption);
		channel.setAutoBroad(null);
		channel.setType(ChannelType.REMOTE.toString());
		
		channelDAO.save(channel);

		return new ChannelVO().set(channel);
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
	public Object broadcastStart(HttpServletRequest request) throws Exception {
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		Long channelId = requestWrapper.getLong("channelId");
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName());
		channelDAO.save(channel);
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
	public Object broadcastStop(HttpServletRequest request) throws Exception {
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		Long channelId = requestWrapper.getLong("channelId");
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED.getName());
		channelDAO.save(channel);
		return "";
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

		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		Long channelId = requestWrapper.getLong("channelId");
		channelDAO.delete(channelId);
		return "";
	}
}
