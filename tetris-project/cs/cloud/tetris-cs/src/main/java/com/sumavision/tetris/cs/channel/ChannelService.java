package com.sumavision.tetris.cs.channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.area.AreaQuery;
import com.sumavision.tetris.cs.area.AreaService;
import com.sumavision.tetris.cs.area.AreaVO;
import com.sumavision.tetris.cs.bak.SendBakService;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoDAO;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.ChannelServerType;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityQuery;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityQueryType;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityRemoteDAO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityRemotePO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityService;
import com.sumavision.tetris.cs.channel.broad.ability.BroadStreamWay;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.file.BroadFileService;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalLevelType;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalService;
import com.sumavision.tetris.cs.channel.exception.ChannelStatusErrorException;
import com.sumavision.tetris.cs.menu.CsMenuPO;
import com.sumavision.tetris.cs.menu.CsMenuService;
import com.sumavision.tetris.cs.menu.CsResourceService;
import com.sumavision.tetris.cs.menu.CsResourceVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelService {
	@Autowired
	private ChannelDAO channelDao;

	@Autowired
	private ChannelQuery channelQuery;

	@Autowired
	private CsMenuService csMenuService;
	
	@Autowired
	private CsResourceService csResourceService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private AreaQuery areaQuery;

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SendBakService sendBakService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ChannelAutoBroadInfoDAO channelAutoBroadInfoDAO;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	@Autowired
	private BroadAbilityRemoteDAO broadAbilityRemoteDAO;
	
	@Autowired
	private BroadAbilityService broadAbilityService;
	
	@Autowired
	private BroadAbilityQuery broadAbilityQuery;
	
	@Autowired
	private BroadFileBroadInfoService broadFileBroadInfoService;
	
	@Autowired
	private BroadFileService broadFileService;
	
	@Autowired
	private BroadTerminalBroadInfoService broadTerminalBroadInfoService;
	
	@Autowired
	private BroadTerminalService broadTerminalService;
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	private Map<Long, Timer> timerMap = new HashMapWrapper<Long, Timer>().getMap();

	/**
	 * 添加频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param String name 频道名称
	 * @param String date 日期
	 * @param String broadWay 播发方式(参考BroadWay枚举)
	 * @param String level 播发优先级(仅限终端播发)
	 * @param Boolean hasFile 是否携带文件播发(仅限终端播发)
	 * @param ChannelTyep type 平台添加还是其他服务添加
	 * @param Boolean encryption 是否加密播发(仅限轮播推流)
	 * @param Boolean autoBroad 是否智能播发
	 * @param Boolean autoBroadShuffle 是否乱序(仅限智能播发)
	 * @param Integer autoBroadDuration 生效时长(仅限智能播发，单位：天)
	 * @param String autoBroadStart 智能播发生效时间(仅限智能播发)
	 * @param String outputUserPort 能力的输出地址port(仅限轮播推流)
	 * @param List<UserVO> outputUserList 预播发的用户列表
	 * List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs 预播发的ip和port对(仅限轮播推流)
	 * @param String remark 备注
	 * @return ChannelPO 频道
	 */
	public ChannelPO add(
			String name,
			String date,
			String broadWay,
			String remark,
			SetTerminalBroadBO terminalBroadBO,
			ChannelType type,
			Boolean encryption,
			SetAutoBroadBO autoBroadBO,
			SetOutputBO outputBO) throws Exception {
		UserVO user = userQuery.current();
		
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
		channel.setAutoBroad(autoBroadBO.getAutoBroad());
		channel.setType(type.toString());
		
		//校验用户是否被占用
		if (channelBroadWay != BroadWay.TERMINAL_BROAD) {
			broadAbilityBroadInfoService.checkUserUse(null, outputBO.getOutputUsers());
			broadFileBroadInfoService.checkUserUse(null, outputBO.getOutputUsers());
		}
		
		//校验ip和端口对是否被占用，设置轮播推流id
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			broadAbilityBroadInfoService.checkIpAndPortExists(null, outputBO.getOutput());
			channel.setAbilityBroadId(adapter.getNewId(channelDao.getAllAbilityId()));
		}

		channelDao.save(channel);
		
		//保存播发输出信息
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			List<BroadAbilityBroadInfoVO> saveinInfoVOs = new ArrayList<BroadAbilityBroadInfoVO>();
			if (outputBO.getOutput() != null && !outputBO.getOutput().isEmpty()) saveinInfoVOs.addAll(outputBO.getOutput());
			saveinInfoVOs.addAll(broadAbilityBroadInfoService.changeVO(outputBO));
			broadAbilityBroadInfoService.saveInfoList(channel.getId(), saveinInfoVOs);
		} else if (channelBroadWay == BroadWay.FILE_DOWNLOAD_BROAD) {
			broadFileBroadInfoService.saveInfoList(channel.getId(), broadFileBroadInfoService.changeVO(outputBO.getOutputUsers()));
		} else {
			broadTerminalBroadInfoService.saveInfo(channel.getId(), terminalBroadBO);
		}
		
		//保存智能播发信息
		if (autoBroadBO.getAutoBroad() != null && autoBroadBO.getAutoBroad()) {
			ChannelAutoBroadInfoPO autoBroadInfoPO = new ChannelAutoBroadInfoPO();
			autoBroadInfoPO.setUpdateTime(new Date());
			autoBroadInfoPO.setChannelId(channel.getId());
			autoBroadInfoPO.setShuffle(autoBroadBO.getAutoBroadShuffle());
			autoBroadInfoPO.setDuration(autoBroadBO.getAutoBroadDuration());
			autoBroadInfoPO.setStartTime(autoBroadBO.getAutoBroadStart());
			autoBroadInfoPO.setStartDate(DateUtil.getYearmonthDay(new Date()));
			channelAutoBroadInfoDAO.save(autoBroadInfoPO);
		}

		return channel;
	}
	
	/**
	 * 添加频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param String name 频道名称
	 * @param String date 日期
	 * @param String broadWay 播发方式(参考BroadWay枚举)
	 * @param String level 播发优先级(仅限终端播发)
	 * @param Boolean hasFile 是否携带文件播发(仅限终端播发)
	 * @param ChannelTyep type 平台添加还是其他服务添加
	 * @param Boolean encryption 是否加密播发(仅限轮播推流)
	 * @param Boolean autoBroad 是否智能播发
	 * @param Boolean autoBroadShuffle 是否乱序(仅限智能播发)
	 * @param Integer autoBroadDuration 生效时长(仅限智能播发，单位：天)
	 * @param String autoBroadStart 智能播发生效时间(仅限智能播发)
	 * @param String outputUserPort 能力的输出地址port(仅限轮播推流)
	 * @param List<UserVO> outputUserList 预播发的用户列表
	 * List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs 预播发的ip和port对(仅限轮播推流)
	 * @param String remark 备注
	 * @return ChannelPO 频道
	 */
	public ChannelPO addYjbgChannel(
			String name,
			String date,
			String broadWay,
			String remark,
			Boolean encryption,
			Boolean autoBroad,
			Boolean autoBroadShuffle,
			Integer autoBroadDuration,
			String autoBroadStart,
			String outputUserPort,
			List<UserVO> outputUserList,
			List<BroadAbilityBroadInfoVO> abilityBroadInfoVOs) throws Exception {
		UserVO user = userQuery.current();
		BroadWay channelBroadWay = BroadWay.fromName(broadWay);
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			ChannelPO channel = broadAbilityService.add(
					user,
					name,
					date,
					broadWay,
					remark,
					ChannelType.YJGB,
					encryption,
					autoBroad,
					autoBroadShuffle,
					autoBroadDuration,
					autoBroadStart,
					outputUserPort,
					outputUserList,
					abilityBroadInfoVOs);
			return channel;
		}
		return null;
	}

	/**
	 * 删除频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 频道id
	 */
	public void remove(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		broadAbilityBroadInfoService.remove(channelId);
		broadFileBroadInfoService.remove(channelId);
		if (ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) stopBroadcast(channelId);
		if (BroadWay.ABILITY_BROAD.getName().equals(channel.getBroadWay()) && !ChannelBroadStatus.CHANNEL_BROAD_STATUS_INIT.getName().equals(channel.getBroadcastStatus())) {
			Boolean sendStop = true;
			BroadAbilityRemotePO broadAbilityRemotePO = broadAbilityRemoteDAO.findByChannelId(channelId);
			if (broadAbilityRemotePO != null && broadAbilityRemotePO.getBroadStreamWay() != BroadStreamWay.ABILITY_STREAM) sendStop = false;
			broadAbilityRemoteDAO.deleteByChannelId(channelId);
			if (sendStop) broadAbilityQuery.sendAbilityRequest(BroadAbilityQueryType.DELETE, channel, null, null);
		} else if (channel.getBroadWay().equals(BroadWay.FILE_DOWNLOAD_BROAD.getName())) {
			broadFileService.stopFileBroadcast(channelId);
		}
		channelDao.delete(channel);
		ChannelAutoBroadInfoPO channelAutoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(channelId);
		if (channelAutoBroadInfoPO != null) channelAutoBroadInfoDAO.delete(channelAutoBroadInfoPO);
		csMenuService.removeMenuByChannelId(channelId);
		areaService.removeByChannelId(channelId);
		scheduleService.removeByChannelId(channelId);
		sendBakService.removeBakFromChannel(channelId);
	}

	/**
	 * 编辑频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 频道id
	 * @param String name 频道名称
	 * @param String broadWay 播发方式(参考BroadWay枚举)
	 * @param String previewUrlIp 能力的输出地址ip(仅限轮播推流)
	 * @param String previewUrlPort 能力的输出地址port(仅限轮播推流)
	 * @param String remark 备注
	 * @return ChannelVO 频道
	 */
	public ChannelPO edit(
			Long id,
			String name,
			String remark,
			SetTerminalBroadBO terminalBroadBO,
			Boolean encryption,
			SetAutoBroadBO autoBroadBO,
			SetOutputBO outputBO) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(id);
		
		if (channel.getAutoBroad()
				&& autoBroadBO.getAutoBroad() != null
				&& !autoBroadBO.getAutoBroad()
				&& ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) {
			timerMap.get(id).cancel();
			stopBroadcast(id);
		}
		
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay != BroadWay.TERMINAL_BROAD) {
			broadAbilityBroadInfoService.checkUserUse(id, outputBO.getOutputUsers());
			broadFileBroadInfoService.checkUserUse(id, outputBO.getOutputUsers());
		}
		
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			broadAbilityBroadInfoService.checkIpAndPortExists(id, outputBO.getOutput());
			List<BroadAbilityBroadInfoVO> saveInfoVOs = new ArrayList<BroadAbilityBroadInfoVO>();
			if (outputBO.getOutput() != null) saveInfoVOs.addAll(outputBO.getOutput());
			saveInfoVOs.addAll(broadAbilityBroadInfoService.changeVO(outputBO));
			broadAbilityBroadInfoService.saveInfoList(id, saveInfoVOs);
		} else if (channelBroadWay == BroadWay.FILE_DOWNLOAD_BROAD) {
			broadFileBroadInfoService.saveInfoList(channel.getId(), broadFileBroadInfoService.changeVO(outputBO.getOutputUsers()));
		} else {
			broadTerminalBroadInfoService.saveInfo(channel.getId(), terminalBroadBO);
			areaQuery.checkAreaUsed(channel.getId(), false);
		}
		channel.setName(name);
		channel.setRemark(remark);
		channel.setUpdateTime(new Date());
		channel.setAutoBroad(autoBroadBO.getAutoBroad());
		if (encryption != null) channel.setEncryption(encryption);
		channelDao.save(channel);
		
		ChannelAutoBroadInfoPO autoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(id);
		if (autoBroadBO.getAutoBroad()) {
			if (autoBroadInfoPO == null) {
				autoBroadInfoPO = new ChannelAutoBroadInfoPO();
				autoBroadInfoPO.setUpdateTime(new Date());
				autoBroadInfoPO.setChannelId(id);
			}
			autoBroadInfoPO.setShuffle(autoBroadBO.getAutoBroadShuffle());
			autoBroadInfoPO.setDuration(autoBroadBO.getAutoBroadDuration());
			autoBroadInfoPO.setStartTime(autoBroadBO.getAutoBroadStart());
			autoBroadInfoPO.setStartDate(DateUtil.getYearmonthDay(new Date()));
			channelAutoBroadInfoDAO.save(autoBroadInfoPO);
		} else {
			if (autoBroadInfoPO != null) channelAutoBroadInfoDAO.delete(autoBroadInfoPO);
		}

		return channel;
	}
	
	/**
	 * 根据不同播发方式执行开始播发命令<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 下午3:56:48
	 * @param Long channelId 频道id
	 * @param Stirng resourceIds 播发手选资源数组 
	 */
	public void startBroadcast(Long channelId, String resourceIds) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			if (ChannelType.YJGB.toString().equals(channel.getType())) {
				broadAbilityService.startAbilityYjgbBroad(channelId);
			} else {
				broadAbilityService.startAbilityBroadcast(channelId);
			}
		} else if (channelBroadWay == BroadWay.FILE_DOWNLOAD_BROAD){
			broadFileService.startFileBroadcast(channelId);
		} else {
			broadTerminalService.startTerminalBroadcast(channelId, resourceIds);
		}
	}
	
	/**
	 * 重新播发(当前只终端播发提供该业务)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午4:30:47
	 * @param Long channelId 频道id
	 */
	public void restartBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (!ChannelBroadStatus.CHANNEL_BROAD_STATUS_STOPPED.getName().equals(channel.getBroadcastStatus()))
			throw new ChannelStatusErrorException(channel.getBroadcastStatus(), "重新播发");
		
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay == BroadWay.TERMINAL_BROAD) {
			broadTerminalService.restartBroadcast(channelId);
		}
	}
	
	/**
	 * 重置终端补包地址<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午4:04:23
	 */
	public void resetZonePath() throws Exception {
		broadTerminalService.resetZonePath();
	}
	
	/**
	 * 资源目录同步到终端<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月15日 上午11:49:20
	 * @param Long channelId 频道id
	 */
	public void updateToTerminal(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay == BroadWay.TERMINAL_BROAD) {
			broadTerminalService.updateToTerminal(channelId);
		}
	}
	
	/**
	 * 根据不同播发方式执行停止播发命令<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 下午3:58:55
	 * @param channelId 频道Id
	 * @throws Exception
	 */
	public void stopBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (!ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) 
//			throw new ChannelAlreadyStopException(channel.getName());
			return;
		
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			if (ChannelType.YJGB.toString().equals(channel.getType())) {
				broadAbilityService.stopAbilityYjgbBroadcast(channelId);
			} else {
				broadAbilityService.stopAbilityBroadcast(channelId);
			}
		} else if (channelBroadWay == BroadWay.FILE_DOWNLOAD_BROAD) {
			broadFileService.stopFileBroadcast(channelId);
		} else {
			broadTerminalService.stopTerminalBroadcast(channelId);
		}
	}
	
	public void seekBroadcast(Long channelId, Long duration) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (!ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus()))
			throw new ChannelStatusErrorException(channel.getBroadcastStatus(), "跳转");
		
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			broadAbilityService.seekAbilityBroadcast(channel, duration);
		}
	}
	
	public void autoAddSchedulesAndBroad(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) stopBroadcast(channelId);
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay != BroadWay.TERMINAL_BROAD) {
			autoSetSchedule(channelId);
			startBroadcast(channelId, null);
		}
	}
	
	public void autoSetSchedule(Long channelId) throws Exception {
		Long now = DateUtil.getLongDate();
		scheduleService.removeFromNowByChannelId(channelId);
		
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		ChannelAutoBroadInfoPO autoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(channelId);
		if (autoBroadInfoPO == null) return;
		
		if (timerMap.containsKey(channel)) timerMap.get(channel).cancel();
		
		List<MediaAudioVO> recommends = mediaAudioQuery.loadRecommend();
		if (recommends != null && !recommends.isEmpty()){
			List<MediaAVideoVO> medias = JSONArray.parseArray(JSONArray.toJSONString(recommends), MediaAVideoVO.class);
			csMenuService.autoAddMenuAndSource(channelId, "audioCommend", medias);
		}
		
		String broadStartTime = new StringBufferWrapper().append(autoBroadInfoPO.getStartDate())
				.append(" ")
				.append(autoBroadInfoPO.getStartTime())
				.toString();
		Long broadStartTimeLong = DateUtil.parse(broadStartTime, DateUtil.dateTimePattern).getTime() + 30000;
		Boolean fromToday = now < broadStartTimeLong;
		
		for (int i = 0; i < autoBroadInfoPO.getDuration(); i++) {
			String broadTime = new StringBufferWrapper()
					.append(DateUtil.addDateStr(autoBroadInfoPO.getStartDate(), i + (fromToday ? 0 : 1)))
					.append(" ")
					.append(autoBroadInfoPO.getStartTime())
					.toString();
			List<ScreenVO> screens = new ArrayList<ScreenVO>();
			if (recommends != null && !recommends.isEmpty()) {
				if (autoBroadInfoPO.getShuffle()) Collections.shuffle(recommends);
				List<MediaAudioVO> audios = recommends.size() > 10 ? recommends.subList(0, 10) : recommends;
				for (MediaAudioVO audio : audios) {
					screens.add(new ScreenVO().getFromAudioVO(audio));
				}
			}
			scheduleService.addSchedule(channelId, broadTime, screens);
		}
	}
	
	/**
	 * 设置排期表和排期表内容时调用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午1:39:25
	 * @param Long channelId 频道id
	 */
	public void changeScheduleDeal(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (!ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) return;
		BroadWay channelBroadWay = BroadWay.fromName(channel.getBroadWay());
		if (channelBroadWay == BroadWay.ABILITY_BROAD) {
			broadAbilityService.addScheduleDeal(channelId);
		} else if (channelBroadWay == BroadWay.FILE_DOWNLOAD_BROAD) {
			
		} else {
			
		}
	}
	
	/**
	 * 服务器重启调用<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月5日 下午1:40:38
	 */
	public void rebootServer(ChannelServerType serverType, String serverIp) throws Exception {
		switch (serverType) {
		case CS_LOCAL_SERVER:
			broadAbilityService.abilityReboot(null);
			break;
		case ABILITY_STREAM:
			broadAbilityService.abilityReboot(serverIp);
			break;
		case PUSH_TERMINAL:
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 根据应急广播下发tar包创建push终端播发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月19日 下午2:14:48
	 * @param String name 频道名
	 * @param String author 作者
	 * @param String publishTime 创建时间
	 * @param String remark 备注
	 * @param String keywords 关键字
	 * @param String contents 内容
	 * @param String regions 地区
	 * @param UsuerVO user 创建者
	 */
	public void generateWithInternalTemplate(
			String name,
			String author,
			String publishTime,
			String remark,
			String keywords,
			List<JSONObject> contents,
			List<String> regions,
			UserVO user) throws Exception {
		//创建频道
		ChannelPO channel = broadTerminalService.add(user, name, publishTime, remark, new SetTerminalBroadBO().setHasFile(true).setLevel(BroadTerminalLevelType.ESPECIALLY.getName()), ChannelType.LOCAL, false, false);
		Long channelId = channel.getId();
		
		//创建目录结构
		CsMenuPO menu = csMenuService.addRoot(channelId, "yjgbPush");
		List<String> videoUrlList = new ArrayList<String>();
		List<String> audioUrlList = new ArrayList<String>();
		List<MediaAVideoVO> resourceList = new ArrayList<MediaAVideoVO>();
		for (JSONObject content : contents) {
			if (content.getString("type").equals("yjgb_video")) {
				videoUrlList.add(content.getString("value"));
			} else if (content.getString("type").equals("yjgb_audio")) {
				audioUrlList.add(content.getString("value"));
			}
		}
		if (!videoUrlList.isEmpty()) resourceList.addAll(mediaAVideoQuery.findByPreviewUrlIn(videoUrlList, "video"));
		if (!audioUrlList.isEmpty()) resourceList.addAll(mediaAVideoQuery.findByPreviewUrlIn(audioUrlList, "audio"));
		List<CsResourceVO> resourceVOs = csResourceService.addResources(resourceList, menu.getId(), channelId);
		
		//添加排期
		Long duration = 3000l;
		List<ScreenVO> screens = new ArrayList<ScreenVO>();
		for (int i = 0; i < resourceVOs.size(); i++) {
			CsResourceVO resource = resourceVOs.get(i);
			screens.add(new ScreenVO().getFromCsResourceVO(resource).setIndex(i+1l).setSerialNum(1l));
			String mediaDuration = resource.getDuration();
			if (mediaDuration != null && !mediaDuration.isEmpty() && mediaDuration.equals("-")) duration += Long.parseLong(mediaDuration);
		}
		scheduleService.addSchedule(channelId, publishTime, screens);
		
		//设置地区
		List<AreaVO> areaList = new ArrayList<AreaVO>();
		for (String region : regions) {
			areaList.add(new AreaVO().setAreaId(region).setChannelId(channelId));
		}
		areaService.setCheckArea(channelId, areaList, true);
		
		//开始播发
		startBroadcast(channelId, null);
		
		//定时停止播发，3s预留给终端开始播发时下载tar包
		if (duration != 3000l) {
			if (timerMap.containsKey(channelId)) {
				Timer timer = timerMap.get(channelId);
				if (timer != null) {
					timer.cancel();
				}
			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					try {
						stopBroadcast(channelId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, duration);
		}
	}
	
//	/**
//	 * 一次性下发所有排期<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月23日 上午11:50:24
//	 * @param channelId
//	 * @throws Exception
//	 */
//	public void autoAddSchedulesAndStart(Long channelId) throws Exception {
//		Long now = DateUtil.getLongDate();
//		
//		ChannelPO channel = channelQuery.findByChannelId(channelId);
//		if (!channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
//			channelDao.save(channel);
//		}
//		
//		ChannelAutoBroadInfoPO autoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(channelId);
//		if (autoBroadInfoPO == null) return;
//		
//		if (timerMap.containsKey(channel)) timerMap.get(channel).cancel();
//		
//		List<MediaAudioVO> recommends = mediaAudioQuery.loadRecommend();
//		if (recommends == null || recommends.isEmpty()) return;
//		
//		String broadStartTime = new StringBufferWrapper().append(autoBroadInfoPO.getStartDate())
//				.append(" ")
//				.append(autoBroadInfoPO.getStartTime())
//				.toString();
//		Long broadStartTimeLong = DateUtil.parse(broadStartTime, DateUtil.dateTimePattern).getTime() + 30000;
//		Boolean fromToday = now < broadStartTimeLong;
//		
//		List<ScheduleVO> scheduleVOs = new ArrayList<ScheduleVO>();
//		String lastDate = null;
//		for (int i = 0; i < autoBroadInfoPO.getDuration(); i++) {
//			String broadTime = new StringBufferWrapper()
//					.append(DateUtil.addDateStr(autoBroadInfoPO.getStartDate(), i + (fromToday ? 0 : 1)))
//					.append(" ")
//					.append(autoBroadInfoPO.getStartTime())
//					.toString();
//			if (i == autoBroadInfoPO.getDuration() - 1) lastDate = broadTime;
//			if (autoBroadInfoPO.getShuffle()) Collections.shuffle(recommends);
//			List<MediaAudioVO> audios = recommends.size() > 10 ? recommends.subList(0, 10) : recommends;
//			List<ScreenVO> screens = new ArrayList<ScreenVO>();
//			for (MediaAudioVO audio : audios) {
//				ScreenVO screen = new ScreenVO();
//				screen.setName(audio.getName());
//				screen.setPreviewUrl(channel.getEncryption() ? audio.getEncryptionUrl() : audio.getPreviewUrl());
//				screen.setHotWeight(audio.getHotWeight());
//				screen.setDownloadCount(audio.getDownloadCount());
//				screen.setDuration(audio.getDuration());
//				screens.add(screen);
//			}
//			ScheduleVO scheduleVO = scheduleService.addSchedule(channelId, broadTime, screens);
//			scheduleVOs.add(scheduleVO);
//		}
//		
//		System.out.println(JSONObject.toJSONString(scheduleVOs));
//		
//		if (lastDate != null && !lastDate.isEmpty()) {
//			Timer nTimer = new Timer();
//			TimerTask timerTask = new TimerTask() {
//				
//				@Override
//				public void run() {
//					try {
//						channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
//						channelDao.save(channel);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			};
//			Long delayTime = (DateUtil.parse(lastDate, DateUtil.dateTimePattern)).getTime() - DateUtil.getLongDate() + 1000;
//			nTimer.schedule(timerTask, delayTime > 0 ? delayTime : 0);
//			timerMap.put(channelId, nTimer);
//		}
//	}
//	
//	/**
//	 * 根据频道id和音频推荐自动生成节目单<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月14日 上午11:10:01
//	 * @param user 用户信息
//	 * @param channelId 频道id
//	 */
//	public void autoAddScheduleAndStart(Long channelId) throws Exception{
//		Long now = DateUtil.getLongDate();
//		
//		ChannelPO channel = channelQuery.findByChannelId(channelId);
//		if (!channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
//			channelDao.save(channel);
//		}
//		
//		ChannelAutoBroadInfoPO autoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(channelId);
//		if (autoBroadInfoPO == null) return;
//		
//		if (timerMap.containsKey(channel)) timerMap.get(channel).cancel();
//		
//		String date = autoBroadInfoPO.getStartDate();
//		String durationDate = DateUtil.addDateStr(date, autoBroadInfoPO.getDuration() - 1);
//		String durationTime = new StringBufferWrapper().append(durationDate)
//				.append(" ")
//				.append(autoBroadInfoPO.getStartTime())
//				.toString();
//		//生效时间推后一秒防止代码时间导致排期失效
//		Long durationDateLong = DateUtil.parse(durationTime, DateUtil.dateTimePattern).getTime() + 1000;
//		
//		if (now <= durationDateLong) {
//			String todayBroadTime = new StringBufferWrapper().append(DateUtil.getYearmonthDay(new Date()))
//					.append(" ")
//					.append(autoBroadInfoPO.getStartTime())
//					.toString();
//			Long todayBroadTimeLong = DateUtil.parse(todayBroadTime, DateUtil.dateTimePattern).getTime();
//			
//			if (todayBroadTimeLong + 5000 >= now) {
//				List<MediaAudioVO> recommends;
//				recommends = mediaAudioQuery.loadRecommend();
//				if (recommends == null || recommends.isEmpty()) return;
//				
//				if (autoBroadInfoPO.getShuffle()) Collections.shuffle(recommends);
//				List<MediaAudioVO> audios = recommends.size() > 10 ? recommends.subList(0, 10) : recommends;
//				List<ScreenVO> screens = new ArrayList<ScreenVO>(); 
//				for (MediaAudioVO audio : audios) {
//					ScreenVO screen = new ScreenVO();
//					screen.setName(audio.getName());
//					screen.setPreviewUrl(audio.getPreviewUrl());
//					screen.setHotWeight(audio.getHotWeight());
//					screen.setDownloadCount(audio.getDownloadCount());
//					screen.setDuration(audio.getDuration());
//					screens.add(screen);
//				}
//				
//				ScheduleVO scheduleVO = scheduleService.addSchedule(channelId, todayBroadTime, screens);
//				
//				Timer nTimer = new Timer();
//				TimerTask timerTask = new TimerTask() {
//					
//					@Override
//					public void run() {
//						try {
//							startPcBroadcast(channelId, scheduleVO.getId());
//							Long delayTime = DateUtil.getEndDate(DateUtil.getYearmonthDay(DateUtil.parse(todayBroadTime, DateUtil.dateTimePattern))).getTime() - DateUtil.getLongDate() + 1000;
//							Thread.sleep(delayTime > 0 ? delayTime : 0);
//							autoAddScheduleAndStart(channelId);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				};
//				Long nNow = DateUtil.getLongDate();
//				if (nNow >= todayBroadTimeLong) {
//					nTimer.schedule(timerTask, 0);
//				} else {
//					nTimer.schedule(timerTask, todayBroadTimeLong - nNow);
//				}
//				
//				timerMap.put(channelId, nTimer);
//			} else {
//				Timer nTimer = new Timer();
//				TimerTask timerTask = new TimerTask() {
//					
//					@Override
//					public void run() {
//						try {
//							autoAddScheduleAndStart(channelId);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				};
//				Long delayTime = DateUtil.getEndDate(DateUtil.getYearmonthDay(DateUtil.parse(todayBroadTime, DateUtil.dateTimePattern))).getTime() - DateUtil.getLongDate() + 1000;
//				nTimer.schedule(timerTask, delayTime > 0 ? delayTime : 0);
//				timerMap.put(channelId, nTimer);
//			}
//		} else {
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
//			channelDao.save(channel);
//		}
//	}
//	
//	/**
//	 * 停止播发(轮播推流)<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年6月25日 上午11:06:57
//	 * @param Long channelId 频道id
//	 */
//	public JSONObject stopAbilityBroadcast(Long channelId) throws Exception{
//		ChannelPO channel = channelDao.findOne(channelId);
//		if (channel == null) return getReturnJSON(false, "播发频道数据错误");
//		
//		if (channelQuery.sendAbilityRequest(BroadAbilityQueryType.STOP, channel, null, null)) {
//			Timer timer = timerMap.get(channelId);
//			if (timer != null) {
//				timer.cancel();
//			}
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
//			channelDao.save(channel);
//			
//			return getReturnJSON(true, "");
//		}else {
//			return getReturnJSON(false, "请求能力失败");
//		}
//	}
//
//	/**
//	 * 停止PC播发<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月21日 下午4:35:37
//	 * @param channelId 频道id
//	 */
//	public JSONObject stopPcBroadcast(Long channelId) throws Exception {
//		ChannelPO channel = channelDao.findOne(channelId);
//		if (channel == null) return getReturnJSON(false, "播发频道数据错误");
//		
//		Timer timer = timerMap.get(channelId);
//		if (timer != null) {
//			timer.cancel();
//		}
//		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
//		channelDao.save(channel);
//			
//		return getReturnJSON(true, "");
//	}
//	
//	/**
//	 * 开始播发(PC播发，根据排期)<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月14日 下午2:25:54
//	 * @param Long channelId 频道id
//	 */
//	public JSONObject startPcBroadTimer(Long channelId) throws Exception {
//		return startAbilityBroadTimer(channelId);
//	}
//	
//	/**
//	 * 开始PC播发<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年10月14日 下午2:25:54
//	 * @param channelId 频道id
//	 * @param scheduleId 排期id
//	 */
//	public JSONObject startPcBroadcast(Long channelId, Long scheduleId) throws Exception {
//		return getReturnJSON(true, "");
//	}
//	
//	/**
//	 * 开始播发(轮播推流，根据排期)<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年6月25日 上午11:06:57
//	 * @param Long channelId 频道id
//	 */
//	public JSONObject startAbilityBroadTimer(Long channelId) throws Exception {
//		ChannelPO channel = channelDao.findOne(channelId);
//		
//		if (channel == null) {
//			throw new ChannelNotExistsException(channelId);
//		}
//		
//		Long now = DateUtil.getLongDate();
//		
//		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
//		
//		if (scheduleVOs == null || scheduleVOs.isEmpty()) {
//			throw new ScheduleNoneToBroadException(channel.getName());
//		}
//		
//		if (!channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
//			channelDao.save(channel);
//		}
//		
//		if (timerMap.containsKey(channelId)) {
//			timerMap.get(channelId).cancel();
//		}
//		for (ScheduleVO scheduleVO : scheduleVOs) {
//			Date broadDate = DateUtil.parse(scheduleVO.getBroadDate(), DateUtil.dateTimePattern);
//			Long broadDateLong = broadDate.getTime();
//			//以防用户选择"此刻"为播发时间的时候，由于手动点击"播发"和程序执行时间导致播发不生效，因此预留5秒。
//			if (broadDateLong + 5000 > now || scheduleVOs.indexOf(scheduleVO) == scheduleVOs.size() - 1) {
//				Timer timer = timerMap.get(channelId);
//				if (timer != null) {
//					timer.cancel();
//				}
//				Timer nTimer = new Timer();
//				TimerTask timerTask = new TimerTask() {
//					
//					@Override
//					public void run() {
//						try {
//							if (channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)){
//								System.out.println(channelId + ":" + scheduleVO.getBroadDate());
//								if (channel.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName())){
//									startAbilityBroadcast(channelId, scheduleVO.getId());
//								} else if (channel.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName())) {
//									startPcBroadcast(channelId, scheduleVO.getId());
//								}
//								Long nNow = DateUtil.getLongDate();
//								//如播发时间与此刻间隔小于5s,则延时5秒进入下一个播发，以防排期被二次播发
//								if (broadDateLong < nNow - 5000) {
//									Thread.sleep(nNow - broadDateLong);
//								}
//								startAbilityBroadTimer(channelId);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				};
//				if (broadDateLong + 5000 > now) {
//					Long nNow = DateUtil.getLongDate();
//					nTimer.schedule(timerTask, broadDateLong > nNow ? broadDateLong - nNow : 0);
//				} else {
//					if (channel.getType().equals(ChannelType.REMOTE.toString())) {
//						channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
//						channelDao.save(channel);
//					} else {
//						nTimer.schedule(timerTask, 1000l);
//					}
//				}
//				timerMap.put(channelId, nTimer);
//				break;
//			}
//		}
//		
//		return getReturnJSON(true, ""); 
//	}
//	
//	/**
//	 * 开始播发(轮播推流)<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年6月25日 上午11:06:57
//	 * @param Long channelId 频道id
//	 * @param Long scheduleId 排期id
//	 */
//	public JSONObject startAbilityBroadcast(Long channelId, Long scheduleId) throws Exception{
//		ChannelPO channel = channelDao.findOne(channelId);
//		if (channel == null) return getReturnJSON(false, "播发频道数据错误");
//		
//		JSONObject output = new JSONObject();
//		output.put("proto-type", "udp-ts");
//		List<JSONObject> destList = new ArrayList<JSONObject>();
//		String localIp = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.ABILITY_BROAD).split(":")[0];
//		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
//		if (broadAbilityBroadInfoVOs == null || broadAbilityBroadInfoVOs.isEmpty()) return getReturnJSON(false, "无输出");
//		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
//			JSONObject dest = new JSONObject();
//			dest.put("local_ip", localIp);
//			dest.put("ipv4", broadAbilityBroadInfoVO.getPreviewUrlIp());
//			dest.put("port", broadAbilityBroadInfoVO.getPreviewUrlPort());
//			dest.put("vport", "");
//			dest.put("aport", "");
//			destList.add(dest);
//		}
//		output.put("dest_list", destList);
//		
//		output.put("scramble", channel.getEncryption() != null && channel.getEncryption() ? "AES-128" : "none");
//		output.put("key", channel.getEncryption() != null && channel.getEncryption() ? mediaEncodeQuery.queryKey() : "");
//		BroadAbilityQueryType type = channelQuery.broadCmd(channelId);
//		
//		JSONObject responseJSON;
//		if (channelQuery.sendAbilityRequest(type, channel, abilityProgramText(programQuery.getProgram(scheduleId)), output)) {
//			if(type != BroadAbilityQueryType.COVER) channelQuery.saveBroad(channelId);
//			responseJSON = getReturnJSON(true, "");
//		}else {
//			responseJSON = getReturnJSON(false, "请求能力失败");
//		}
//		
//		return responseJSON;
//	}
//
//	private JSONObject getReturnJSON(Boolean ifSuccess, String message) {
//		JSONObject returnJsonObject = new JSONObject();
//		returnJsonObject.put("success", ifSuccess);
//		returnJsonObject.put("message", message);
//		return returnJsonObject;
//	}
//
//	/**
//	 * 播发时媒资排表字段内容(能力播发)<br/>
//	 * <b>作者:</b>lzp<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2019年6月25日 上午11:06:57
//	 * @param ProgramVO program 分屏信息
//	 */
//	private List<String> abilityProgramText(ProgramVO program) throws Exception{
//		List<String> returnList = new ArrayList<String>();
//		if (program != null) {
//			for (int i = 1; i <= program.getScreenNum(); i++) {
//				if (program.getScreenInfo() != null && program.getScreenInfo().size() > 0) {
//					List<ScreenVO> screens = program.getScreenInfo();
//					Collections.sort(screens, new ScreenVO.ScreenVOOrderComparator());
//					for (ScreenVO item : program.getScreenInfo()) {
//						if (item.getSerialNum() != i)
//							continue;
//						returnList.add(adapter.changeHttpToFtp(item.getPreviewUrl()));
//					}
//				}
//			}
//		}
//		return returnList;
//	}

	public Map<Long, Timer> getTimerMap() {
		return timerMap;
	}

	public void setTimerMap(Map<Long, Timer> timerMap) {
		this.timerMap = timerMap;
	}
}
