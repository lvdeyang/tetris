package com.sumavision.tetris.cs.channel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
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
	private ChannelService channelService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private AlarmFeignClientService alarmFeignClientService;
	
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
	 * 获取本地网卡列表
	 * 方法概述<br/>
	 * <p>获取本地网卡IP列表</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月26日 上午9:17:54
	 * @return List<String>
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/netcard/get")
	public Object getNetCard() throws Exception {
		List<String> netcards=new ArrayList<String>();
		 Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
         NetworkInterface networkInterface;
         Enumeration<InetAddress> inetAddresses;
         InetAddress inetAddress;
         String ip;
         while (networkInterfaces.hasMoreElements()) {
             networkInterface = networkInterfaces.nextElement();
             inetAddresses = networkInterface.getInetAddresses();
             while (inetAddresses.hasMoreElements()) {
                 inetAddress = inetAddresses.nextElement();
                 if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                     ip = inetAddress.getHostAddress();
                     netcards.add(ip);
                 }
             }
         }
		return netcards;
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
			String outputUserEndPort,
			String output,
			String remark,
			String level,
			Boolean hasFile,
			Boolean encryption,
			Boolean autoBroad,
			Boolean autoBroadShuffle,
			Integer autoBroadDuration,
			String autoBroadStart,
			HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs = JSONArray.parseArray(output, BroadAbilityBroadInfoVO.class);
		
		List<UserVO> outputUserList = new ArrayList<UserVO>();
		if (outputUsers != null) outputUserList = JSONArray.parseArray(outputUsers, UserVO.class);
		
		SetOutputBO outputBO = new SetOutputBO()
				.setOutput(abilityBroadInfoVOs)
				.setOutputUserPort(outputUserPort)
				.setOutputUserEndPort(outputUserEndPort)
				.setOutputUsers(outputUserList);
		
		SetAutoBroadBO autoBroadBO = new SetAutoBroadBO()
				.setAutoBroad(autoBroad)
				.setAutoBroadShuffle(autoBroadShuffle)
				.setAutoBroadDuration(autoBroadDuration)
				.setAutoBroadStart(autoBroadStart);
		
		SetTerminalBroadBO terminalBroadBO = new SetTerminalBroadBO()
				.setHasFile(hasFile)
				.setLevel(level);

		ChannelPO channel = channelService.add(
				name,
				date,
				broadWay,
				remark,
				terminalBroadBO,
				ChannelType.LOCAL,
				encryption,
				autoBroadBO,
				outputBO);
		
		if (!BroadWay.fromName(broadWay).equals(BroadWay.TERMINAL_BROAD) && autoBroad) channelService.autoAddSchedulesAndBroad(channel.getId());

		return new ChannelVO().set(channel)
				.setOutput(abilityBroadInfoVOs)
				.setOutputUsers(outputUserList)
				.setOutputUserPort(outputUserPort)
				.setOutputUserEndPort(outputUserEndPort)
				.setAutoBroadDuration(autoBroadDuration)
				.setAutoBroadStart(autoBroadStart)
				.setLevel(level)
				.setHasFile(hasFile);
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
			String outputUserEndPort,
			String output,
			String remark,
			String level,
			Boolean hasFile,
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
		
		SetOutputBO outputBO = new SetOutputBO()
				.setOutput(abilityBroadInfoVOs)
				.setOutputUserPort(outputUserPort)
				.setOutputUserEndPort(outputUserEndPort)
				.setOutputUsers(outputUserList);
		
		SetAutoBroadBO autoBroadBO = new SetAutoBroadBO()
				.setAutoBroad(autoBroad)
				.setAutoBroadShuffle(autoBroadShuffle)
				.setAutoBroadDuration(autoBroadDuration)
				.setAutoBroadStart(autoBroadStart);
		
		SetTerminalBroadBO terminalBroadBO = new SetTerminalBroadBO()
				.setHasFile(hasFile)
				.setLevel(level);

		ChannelPO channel = channelService.edit(
				id,
				name,
				remark,
				terminalBroadBO,
				encryption,
				autoBroadBO,
				outputBO);
		
		if (BroadWay.fromName(channel.getBroadWay()) != BroadWay.TERMINAL_BROAD && autoBroad) {
			channelService.autoAddSchedulesAndBroad(channel.getId());
		}

		return new ChannelVO().set(channel)
				.setOutput(abilityBroadInfoVOs)
				.setOutputUsers(outputUserList)
				.setOutputUserPort(outputUserPort)
				.setOutputUserEndPort(outputUserEndPort)
				.setAutoBroadDuration(autoBroadDuration)
				.setAutoBroadStart(autoBroadStart)
				.setLevel(level)
				.setHasFile(hasFile);
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
	public Object broadcastStart(Long channelId, String resourceIds, HttpServletRequest request) throws Exception {
		channelService.startBroadcast(channelId, resourceIds);
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
	 * 刷新播发列表
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月2日 上午10:17:54
	 * @param channelId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/modify")
	public Object broadcastModify(Long channelId, HttpServletRequest request) throws Exception {
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
		
		return channelQuery.getBroadstatus(channelId);
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

		channelService.restartBroadcast(channelId);
		
		return "";
	}
	
	/**
	 * 同步资源目录到终端<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月15日 上午11:34:28
	 * @param Long channelId 频道id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/update/to/terminal")
	public Object updateToTerminal(Long channelId, HttpServletRequest request) throws Exception {
		channelService.updateToTerminal(channelId);
		return null;
	}
	
	/**
	 * 重置终端补包地址<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午4:04:23
	 * @param request
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/reset/zone/url")
	public Object setZoneUrl(HttpServletRequest request) throws Exception {
		channelService.resetZonePath();
		return null;
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
		channelService.seekBroadcast(channelId, duration);
		
		return "";
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
				userQuery.listByCompanyIdWithExceptAndClassify(Long.parseLong(user.getGroupId()), TerminalType.QT_MEDIA_EDITOR.getName(), null, UserClassify.COMPANY);
		
		Map<String, List<UserVO>> map = new HashMap<String, List<UserVO>>();
		for (UserVO userVO : users) {
			String equipType = userVO.getEquipType() == null ? "" : userVO.getEquipType();
			if (map.containsKey(equipType)){
				List<UserVO> userlist = map.get(equipType);
				userlist.add(userVO);
			} else {
				map.put(equipType, new ArrayListWrapper<UserVO>().add(userVO).getList());
			}
		}
		
		return map;
	}
	
	/**
	 * 获取分屏模板<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月11日 下午5:20:27
	 * @param Long id 频道id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/template")
	public Object questTemplate(Long channelId, Long scheduleId, HttpServletRequest request) throws Exception {
		return channelQuery.getTemplate(channelId, scheduleId);
	}
	
	/**
	 * 获取系统时间<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月9日 上午10:08:40
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/time")
	public Object getTime(HttpServletRequest request) throws Exception {
		return DateUtil.now();
	}
}
