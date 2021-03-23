package com.sumavision.tetris.cs.channel;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.cs.bak.AbilityInfoSendDAO;
import com.sumavision.tetris.cs.bak.AbilityInfoSendPO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityService;
import com.sumavision.tetris.cs.template.ChannelTemplatePO;
import com.sumavision.tetris.cs.template.ChannelTemplateService;
import com.sumavision.tetris.cs.template.ChannelTemplateVo;
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
	private CapacityService capacityService;
	
	@Autowired
	private AbilityInfoSendDAO abilityInfoSendDAO;
	
	@Autowired
	private BroadAbilityService broadAbilityService;
	
	//@Autowired
	//private AlarmFeignClientService alarmFeignClientService;
	
	/**
	 * 分页获取频道列表<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
	 * @param Integer currentPage 当前�?
	 * @param Integer pageSize 分页大小
	 * @return List<ChannelVO> channels 频道列表
	 * @return Long total 频道总数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object channelList(Integer currentPage, Integer pageSize, HttpServletRequest request) throws Exception {
		//初始化时设置转换告警地址
		capacityService.setAlarmUrl("192.165.56.17");
		return channelQuery.findAll(currentPage, pageSize, ChannelType.LOCAL);
	}
	/**
	 * 获取本地网卡列表
	 * 方法概述<br/>
	 * <p>获取本地网卡IP列表</p>
	 * <b>作�??:</b>Mr.h<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2020�?8�?26�? 上午9:17:54
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
	
	@Autowired
	ChannelTemplateService ChannelTemplateService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/template/get")
	public Object getTemplate() throws Exception {
		List<ChannelTemplateVo> channelTemplateVos=ChannelTemplateService.findAllNopage();
		return channelTemplateVos;
	}


	/**
	 * 添加频道<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
	 * @param String name 频道名称
	 * @param String date 日期
	 * @param String broadWay 播发方式(参�?�BroadWay枚举)
	 * @param String previewUrlIp 能力的输出地�?ip(仅限能力播发)
	 * @param String previewUrlPort 能力的输出地�?port(仅限能力播发)
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
			Long autoBroadTemplateId,
			String autoBroadStart,
			String taskTemple,
			String rateCtrl,
			String rate,
			String videoCodec,
			String audioCodec,
			String backfileUrl,
			String backfileDuration,
			String backfileName,
			String backfileType,
			String codeRate,
			String resolution,
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
				.setAutoBroadTemplateId(autoBroadTemplateId==null?0:autoBroadTemplateId)
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
				outputBO,
				taskTemple,
				rateCtrl,
				rate,
				videoCodec,
				audioCodec,
				backfileUrl,
				backfileDuration,
				backfileName,
				backfileType,
				codeRate,
				resolution);
		
		if (!BroadWay.fromName(broadWay).equals(BroadWay.TERMINAL_BROAD) && autoBroad) channelService.autoAddSchedulesAndBroad(channel.getId());

		return new ChannelVO().set(channel)
				.setOutput(abilityBroadInfoVOs)
				.setOutputUsers(outputUserList)
				.setOutputUserPort(outputUserPort)
				.setOutputUserEndPort(outputUserEndPort)
				.setAutoBroadDuration(autoBroadDuration)
				.setAutoBroadStart(autoBroadStart)
				.setLevel(level)
				.setHasFile(hasFile)
				.setTaskTemple(taskTemple)
				.setRate(rate)
				.setRateCtrl(rateCtrl)
				.setCodeRate(codeRate)
				.setResolution(resolution)
				.setAudioCodec(audioCodec)
				.setVideoCodec(videoCodec);
	}

	/**
	 * 编辑频道<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
	 * @param Long id 频道id
	 * @param String name 频道名称
	 * @param String broadWay 播发方式(参�?�BroadWay枚举)
	 * @param String previewUrlIp 能力的输出地�?ip(仅限能力播发)
	 * @param String previewUrlPort 能力的输出地�?port(仅限能力播发)
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
			Long autoBroadTemplateId,
			String taskTemple,
			String rateCtrl,
			String rate,
			String codeRate,
			String resolution,
			String videoCodec,
			String audioCodec,
			String backfileUrl,
			String backfileDuration,
			String backfileName,
			String backfileType,
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
				.setAutoBroadTemplateId(autoBroadTemplateId)
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
				outputBO,
				taskTemple,
				rateCtrl,
				rate,
				codeRate,
				resolution,
				backfileUrl,
				backfileDuration,
				backfileName,
				backfileType,
				videoCodec,
				audioCodec);
		
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
				.setHasFile(hasFile)
				.setTaskTemple(taskTemple)
				.setRate(rate)
				.setRateCtrl(rateCtrl)
				.setCodeRate(codeRate)
				.setResolution(resolution);
	}

	/**
	 * 删除频道<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
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
	 * �?始播�?<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
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
	 * <b>作�??:</b>Mr.h<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2020�?9�?2�? 上午10:17:54
	 * @param channelId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/modify")
	public Object broadcastModify(Long channelId, HttpServletRequest request) throws Exception {
		channelService.modifyBroadcast(channelId);
		return "";
	}
	
	/**
	 * 查询播发状�??<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
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
	 * 同步资源目录到终�?<br/>
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2020�?1�?15�? 上午11:34:28
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2020�?2�?18�? 下午4:04:23
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?6�?25�? 上午11:06:57
	 * @param Long channelId 频道id
	 * @param Long duration 跳转�?(单位s。支持负�?)
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?10�?31�? 下午1:47:40
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2019�?12�?11�? 下午5:20:27
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
	 * <b>作�??:</b>lzp<br/>
	 * <b>版本�?</b>1.0<br/>
	 * <b>日期�?</b>2020�?5�?9�? 上午10:08:40
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/time")
	public Object getTime(HttpServletRequest request) throws Exception {
		return DateUtil.now();
	}
	
	/**
	 * 同步任务单 <br/>
	 * <b>作者:</b>zhouaining<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月11日 上午9:50:20
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/sync")
	public Object syncTask() throws Exception {
		
		syncVO syncVo = new syncVO();
		syncVo.setDeviceIp("192.165.56.17");
		syncVo.setBusinesstype("PUSH");
		//获取所有已下发的任务
		List<AbilityInfoSendPO> abilityInfoSendPOs = abilityInfoSendDAO.findAll();
		List<String> taskList = new ArrayList<String>();
		//获取taskId列表
		for (AbilityInfoSendPO abilityInfoSendPO : abilityInfoSendPOs) {
			taskList.add(abilityInfoSendPO.getTaskId());
		}
		syncVo.setJobIds(taskList);
		String syncVoString = JSONObject.toJSONString(syncVo);
		//同步节目单
		String taskIds = capacityService.sync(syncVoString);
		//解析获取需要重下的lessJobids集合
		JSONObject taskJSONObject = JSON.parseObject(taskIds);
		String taskIdList = taskJSONObject.getString("lessJobIds");
		
		//测试
		//String taskIds = JSONObject.toJSONString(taskList);
		
		List restartTaskList = JSONArray.parseArray(taskIdList);
		List<Long> channelList = new ArrayList<Long>();
		if(restartTaskList.size()>0){
			for(int i = 0;i<restartTaskList.size();i++){
				//同步后返回值不为空,获取缺失任务的频道id
				List<AbilityInfoSendPO> missAbilityInfoSendPOs = abilityInfoSendDAO.findByTaskId((String)restartTaskList.get(i));
				//获取缺失的频道id
				if (!missAbilityInfoSendPOs.isEmpty()) {
					Long channelId = missAbilityInfoSendPOs.get(0).getChannelId();
					channelList.add(channelId);
				}
			}
		}
		//未停止成功的能力端删除
		//未下发成功的重新播发
		for (Long channelId : channelList) {
			//先停再重发
			channelService.stopBroadcast(channelId);
			broadAbilityService.startAbilityBroadcast(channelId);
		}
		
		return "";
	}

}
