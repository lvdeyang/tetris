package com.sumavision.tetris.cs.channel.ability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoDAO;
import com.sumavision.tetris.cs.channel.autoBroad.ChannelAutoBroadInfoPO;
import com.sumavision.tetris.cs.channel.exception.ChannelAbilityNoneOutputException;
import com.sumavision.tetris.cs.channel.exception.ChannelNotExistsException;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.ScheduleVO;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNoneToBroadException;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioQuery;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.encode.MediaEncodeQuery;
import com.sumavision.tetris.orm.exception.ErrorTypeException;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadAbilityService {
	
	@Autowired
	private ChannelDAO channelDao;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	@Autowired
	private ChannelAutoBroadInfoDAO channelAutoBroadInfoDAO;
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaAudioQuery mediaAudioQuery;
	
	@Autowired
	private MediaEncodeQuery mediaEncodeQuery;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	public void startAbilityBroadcast(Long channelId) throws Exception {
		startAbilityBroadTimer(channelId);
	}
	
	/**
	 * 停止播发(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public void stopAbilityBroadcast(Long channelId) throws Exception{
		Map<Long, Timer> timerMap = channelService.getTimerMap();
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		
		if (channelQuery.sendAbilityRequest(BroadAbilityQueryType.STOP, channel, null, null)) {
			Timer timer = timerMap.get(channelId);
			if (timer != null) {
				timer.cancel();
			}
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
			channelDao.save(channel);	
		}
	}
	
	public void autoSetSchedule(Long channelId) throws Exception {
		Long now = DateUtil.getLongDate();
		Map<Long, Timer> timerMap = channelService.getTimerMap();
		scheduleService.removeFromNowByChannelId(channelId);
		
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		ChannelAutoBroadInfoPO autoBroadInfoPO = channelAutoBroadInfoDAO.findByChannelId(channelId);
		if (autoBroadInfoPO == null) return;
		
		if (timerMap.containsKey(channel)) timerMap.get(channel).cancel();
		
		List<MediaAudioVO> recommends = mediaAudioQuery.loadRecommend();
		if (recommends == null || recommends.isEmpty()) return;
		
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
			if (autoBroadInfoPO.getShuffle()) Collections.shuffle(recommends);
			List<MediaAudioVO> audios = recommends.size() > 10 ? recommends.subList(0, 10) : recommends;
			List<ScreenVO> screens = new ArrayList<ScreenVO>();
			for (MediaAudioVO audio : audios) {
				ScreenVO screen = new ScreenVO();
				screen.setName(audio.getName());
				screen.setPreviewUrl(audio.getPreviewUrl());
				screen.setEncryption(audio.getEncryption() != null && audio.getEncryption() ? "true" : "false");
				screen.setEncryptionUrl(audio.getEncryptionUrl());
				screen.setHotWeight(audio.getHotWeight());
				screen.setDownloadCount(audio.getDownloadCount());
				screen.setDuration(audio.getDuration());
				screens.add(screen);
			}
			scheduleService.addSchedule(channelId, broadTime, screens);
		}
	}
	
	public void addScheduleDeal(Long channelId) throws Exception {
		Long now = DateUtil.getLongDate();
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		if (!channel.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName()) || channel.getBroadcastStatus() != ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING) return;
		Map<Long, Timer> timerMap = channelService.getTimerMap();
		if (!timerMap.containsKey(channelId)) return;
		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
		if (scheduleVOs == null || scheduleVOs.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
		int size = scheduleVOs.size();
		ScheduleVO lastButOne = scheduleVOs.get(size - 2);
		Long broadTime = DateUtil.parse(lastButOne.getBroadDate(), DateUtil.dateTimePattern).getTime();
		if (broadTime > now) return;
		Long playTime = querySchedulePlayTime(lastButOne.getId());
		Long finishTime = playTime + broadTime;
		ScheduleVO last = scheduleVOs.get(size - 1);
		Long lastBroadTime = DateUtil.parse(last.getBroadDate(), DateUtil.dateTimePattern).getTime();
		if (lastBroadTime - 5000l < finishTime) {
			timerMap.get(channelId).cancel();
			Timer nTimer = new Timer();
			TimerTask timerTask = new TimerTask() {
				
				@Override
				public void run() {
					try {
						startAbilityBroadTimer(channelId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Long nNow = DateUtil.getLongDate();
			if (lastBroadTime - 5000l > nNow) {
				nTimer.schedule(timerTask, lastBroadTime - nNow - 5000l);
			} else {
				nTimer.schedule(timerTask, 0l);
			}
		}
	}
	
	/**
	 * 循环获取节目单下发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月1日 下午4:51:09
	 * @param channelId
	 */
	private void startAbilityBroadTimer(Long channelId) throws Exception {
		Map<Long, Timer> timerMap = channelService.getTimerMap();
		//以防用户选择"此刻"为播发时间的时候，由于手动点击"播发"和程序执行时间导致播发不生效的预留时间
		Long dealTime = 15000l;
		BroadAbilityQueryType type = BroadAbilityQueryType.COVER;
		
		Long now = DateUtil.getLongDate();
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
		if (scheduleVOs == null || scheduleVOs.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
		
		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
		List<UserVO> webSocketSendUsers = new ArrayList<UserVO>();
		String streamUrlPort = "";
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
			Long userId = broadAbilityBroadInfoVO.getUserId();
			if (userId != null) {
				List<UserVO> userVOs = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList());
				if (userVOs != null && !userVOs.isEmpty()) {
					UserVO user = userVOs.get(0);
					webSocketSendUsers.add(user);
					if (streamUrlPort.isEmpty()) streamUrlPort = broadAbilityBroadInfoVO.getPreviewUrlPort();
					broadAbilityBroadInfoVO.setPreviewUrlIp(user.getIp());
				}
			}
		}
		if (!channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
			switch (channel.getBroadcastStatus()) {
			case ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED:
				type = BroadAbilityQueryType.CHANGE;
				break;
			case ChannelBroadStatus.CHANNEL_BROAD_STATUS_INIT:
				type = BroadAbilityQueryType.NEW;
				break;
			default:
				throw new ErrorTypeException("播发状态", channel.getBroadcastStatus());
			}
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
			channelDao.save(channel);
			if (!webSocketSendUsers.isEmpty()) sendWebSocket(webSocketSendUsers, scheduleVOs, streamUrlPort, channel);
		}
		
		if (timerMap.containsKey(channelId)) timerMap.get(channelId).cancel();
		
		for (ScheduleVO scheduleVO : scheduleVOs) {
			Date broadDate = DateUtil.parse(scheduleVO.getBroadDate(), DateUtil.dateTimePattern);
			Long broadDateLong = broadDate.getTime();
			//以防用户选择"此刻"为播发时间的时候，由于手动点击"播发"和程序执行时间导致播发不生效，因此预留10秒。
			if (broadDateLong + dealTime > now || scheduleVOs.indexOf(scheduleVO) == scheduleVOs.size() - 1) {
				Timer timer = timerMap.get(channelId);
				if (timer != null) timer.cancel();
				Timer nTimer = new Timer();
				if (broadDateLong + dealTime  > now) {
					final BroadAbilityQueryType queryType = type;
					TimerTask timerTask = new TimerTask() {
						
						@Override
						public void run() {
							try {
								if (channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)){
									System.out.println(channelId + ":" + scheduleVO.getBroadDate());
									startSendSchedule(channelId, scheduleVO.getId(), broadAbilityBroadInfoVOs, queryType);
									Long nNow = DateUtil.getLongDate();
									if (broadDateLong > nNow) Thread.sleep(broadDateLong - nNow);
									startSendStream(channelId);
									//如果当前节目单结束时间超过下一个节目单开始时间，则睡眠到下一个节目单开始前5s,给能力解析节目单的时间;否则睡眠到当前节目单结束
									Long playTime = querySchedulePlayTime(scheduleVO.getId());
									Long finishTime = broadDateLong + playTime;
									int next = scheduleVOs.indexOf(scheduleVO) + 1;
									if (next < scheduleVOs.size()) {
										Long nextBroadTime = DateUtil.parse(scheduleVOs.get(next).getBroadDate(), DateUtil.dateTimePattern).getTime();
										if (nextBroadTime - 5000l < finishTime) {
											Thread.sleep(nextBroadTime - 5000l);
										} else {
											Thread.sleep(playTime);
										}
									} else {
										Thread.sleep(playTime);
									}
									startAbilityBroadTimer(channelId);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					Long nNow = DateUtil.getLongDate();
					//提前1分钟下发，给推流服务器解析命令的时间
					Long delayTime = 1l*60*1000;
					if (broadDateLong > nNow + delayTime){
						nTimer.schedule(timerTask, broadDateLong - nNow - delayTime);
					} else {
						nTimer.schedule(timerTask, 0);
					}
				} else {
					TimerTask timerTask = new TimerTask() {
						
						@Override
						public void run() {
							try {
								startAbilityBroadTimer(channelId);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					if (channel.getType().equals(ChannelType.REMOTE.toString()) || channel.getAutoBroad()) {
						channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
						channelDao.save(channel);
					} else {
						nTimer.schedule(timerTask, dealTime - 5000l);
					}
				}
				timerMap.put(channelId, nTimer);
				break;
			}
		}
	}
	
	/**
	 * 给PC端用户webSocket下节目单<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月3日 下午3:24:34
	 * @param userVOs 用户列表
	 * @param scheduleVOs 节目单列表
	 */
	private void sendWebSocket(List<UserVO> userVOs, List<ScheduleVO> scheduleVOs, String port, ChannelPO channel) throws Exception {
		if (userVOs == null || userVOs.isEmpty() || scheduleVOs == null || scheduleVOs.isEmpty()) return;
		List<Long> scheduleIds = scheduleVOs.stream().map(ScheduleVO::getId).collect(Collectors.toList());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("channelId", channel.getId());
		for (UserVO userVO : userVOs) {
			websocketMessageService.send(userVO.getId(), jsonObject.toJSONString(), WebsocketMessageType.COMMAND);
		}
		System.out.println(JSONArray.toJSONString(scheduleVOs));
	}
	
	/**
	 * 下发准备的流的信息(提供解析时间)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月1日 下午4:50:02
	 * @param channelId
	 * @param scheduleId
	 * @param type
	 */
	private void startSendSchedule(Long channelId, Long scheduleId, List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs, BroadAbilityQueryType type) throws Exception {
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		
		JSONObject output = new JSONObject();
		output.put("proto-type", "udp-ts");
		List<JSONObject> destList = new ArrayList<JSONObject>();
		String localIp = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.ABILITY_BROAD).split(":")[0];
		if (broadAbilityBroadInfoVOs == null || broadAbilityBroadInfoVOs.isEmpty()) throw new ChannelAbilityNoneOutputException();
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
			JSONObject dest = new JSONObject();
			dest.put("local_ip", localIp);
			dest.put("ipv4", broadAbilityBroadInfoVO.getPreviewUrlIp());
			dest.put("port", broadAbilityBroadInfoVO.getPreviewUrlPort());
			dest.put("vport", "");
			dest.put("aport", "");
			destList.add(dest);
		}
		output.put("dest_list", destList);
		
		output.put("scramble", channel.getEncryption() != null && channel.getEncryption() ? "AES-128" : "none");
		output.put("key", channel.getEncryption() != null && channel.getEncryption() ? getKey() : "");
		
		if (channelQuery.sendAbilityRequest(type, channel, abilityProgramText(programQuery.getProgram(scheduleId)), output)) {
			if(type != BroadAbilityQueryType.COVER) channelQuery.saveBroad(channelId, broadAbilityBroadInfoVOs);
		}
	}
	
	/**
	 * 下发开始推流命令<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月1日 下午4:49:06
	 * @param channelId 频道id
	 */
	private void startSendStream(Long channelId) throws Exception {
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) throw new ChannelNotExistsException(channelId);
		channelQuery.sendAbilityRequest(BroadAbilityQueryType.START, channel);
	}
	
	/**
	 * 播发时媒资排表字段内容(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param ProgramVO program 分屏信息
	 */
	private List<String> abilityProgramText(ProgramVO program) throws Exception{
		List<String> returnList = new ArrayList<String>();
		if (program != null) {
			for (int i = 1; i <= program.getScreenNum(); i++) {
				if (program.getScreenInfo() != null && program.getScreenInfo().size() > 0) {
					List<ScreenVO> screens = program.getScreenInfo();
					Collections.sort(screens, new ScreenVO.ScreenVOOrderComparator());
					for (ScreenVO item : program.getScreenInfo()) {
						if (item.getSerialNum() != i)
							continue;
						returnList.add(adapter.changeHttpToFtp(item.getPreviewUrl()));
					}
				}
			}
		}
		return returnList;
	}
	
	private Long querySchedulePlayTime(Long schedulId) throws Exception {
		Long playTime = 0l;
		ProgramVO programVO = programQuery.getProgram(schedulId);
		List<ScreenVO> screenVOs = programVO.getScreenInfo();
		if (screenVOs == null || screenVOs.isEmpty()) return playTime;
		for (ScreenVO screenVO : screenVOs) {
			if (screenVO.getDuration() != null && !screenVO.getDuration().isEmpty() && !screenVO.getDuration().equals("-"))
				playTime += Long.parseLong(screenVO.getDuration());
		}
		return playTime;
	}
	
	/**
	 * 获取加密密钥(密钥在这里再加密)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月1日 下午4:49:26
	 * @return
	 */
	private String getKey() {
		byte[] bytes = {0x0d, 0x5d, (byte) 0xe3, 0x45, (byte) 0xd4, 0x00, 0x60, 0x7d, (byte) 0xc9, 0x3f, (byte) 0xe5, (byte) 0xd7, 0x17, 0x49, 0x3a, (byte) 0xf4};
		try{
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(mediaEncodeQuery.queryKey().getBytes("utf-8"));
            return ByteUtil.bytesToHexString(encrypted).toUpperCase();
        }catch(Exception ex){
        	return null;
        }
	}
	
//	private void startAbilityBroadcast(Long channelId) throws Exception {
//		Map<Long, Timer> timerMap = channelService.getTimerMap();
//		if (timerMap.containsKey(channelId)) timerMap.get(channelId).cancel();
//		ChannelPO channel = channelDao.findOne(channelId);
//		if (channel == null) throw new ChannelNotExistsException(channelId);
//		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
//		if (scheduleVOs == null || scheduleVOs.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
//		if (!channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
//			channelDao.save(channel);
//		}
//		JSONObject output = new JSONObject();
//		output.put("proto-type", "udp-ts");
//		output.put("scramble", channel.getEncryption() != null && channel.getEncryption() ? "AES-128" : "none");
//		output.put("key", channel.getEncryption() != null && channel.getEncryption() ? mediaEncodeQuery.queryKey() : "");
//		List<JSONObject> destList = new ArrayList<JSONObject>();
//		String localIp = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.ABILITY_BROAD).split(":")[0];
//		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
//		if (broadAbilityBroadInfoVOs == null || broadAbilityBroadInfoVOs.isEmpty()) throw new ChannelAbilityNoneOutputException();
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
//		List<String> urlList = new ArrayList<String>();
//		for (ScheduleVO scheduleVO : scheduleVOs) {
//			urlList.addAll(abilityProgramText(programQuery.getProgram(scheduleVO.getId())));
//		}
//		BroadAbilityQueryType type = channelQuery.broadCmd(channelId);
//		channelQuery.sendAbilityRequest(type, channel, urlList, output);
//		if(type != BroadAbilityQueryType.COVER) channelQuery.saveBroad(channelId);
//		startAbilityBroadTimer(channelId);
//	}
}
