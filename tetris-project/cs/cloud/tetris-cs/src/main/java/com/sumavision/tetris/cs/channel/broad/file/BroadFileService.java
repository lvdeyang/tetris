package com.sumavision.tetris.cs.channel.broad.file;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.area.AreaQuery;
import com.sumavision.tetris.cs.bak.AreaSendQuery;
import com.sumavision.tetris.cs.bak.VersionSendQuery;
import com.sumavision.tetris.cs.bak.VersionSendType;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalQueryType;
import com.sumavision.tetris.cs.channel.exception.ChannelAlreadyBroadException;
import com.sumavision.tetris.cs.channel.exception.ChannelTerminalRequestErrorException;
import com.sumavision.tetris.cs.menu.CsResourceQuery;
import com.sumavision.tetris.cs.menu.CsResourceVO;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenContentType;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.cs.schedule.ScheduleVO;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNoneToBroadException;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtVO;
import com.sumavision.tetris.user.UserEquipType;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.websocket.message.WebsocketMessageType;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadFileService {
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ChannelDAO channelDAO;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private CsResourceQuery csResourceQuery;
	
	@Autowired
	private AreaQuery areaQuery;
	
	@Autowired
	private AreaSendQuery areaSendQuery;
	
	@Autowired
	private VersionSendQuery versionSendQuery;
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private BroadFileBroadInfoService broadFileBroadInfoService;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private MediaTxtService mediaTxtService;
	
	/**
	 * 文件下载开始播发<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月27日 下午3:07:26
	 * @param channelId 频道id
	 */
	public void startFileBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) throw new ChannelAlreadyBroadException(channel.getName());
		
		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
		if (scheduleVOs == null || scheduleVOs.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
		
		List<BroadFileBroadInfoVO> broadInfoVOs = broadFileBroadInfoService.queryFromChannelId(channelId);
//		if (broadInfoVOs == null || broadInfoVOs.isEmpty()) throw new ChannelBroadNoneOutputException();
		
		sendWebsocket(broadInfoVOs, channel);
		sendPush(channelId, false);
		
		if (channel.getAutoBroad() == null || !channel.getAutoBroad()){
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED.getName());
		} else {
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName());
		}
		channelDAO.save(channel);
	}
	
	public void stopFileBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED.getName());
		channelDAO.save(channel);
	}
	
	/**
	 * 给终端用户webSocket下节目单<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月3日 下午3:24:34
	 * @param broadInfoVOs 预播发信息列表
	 * @param channel 频道信息
	 */
	public void sendWebsocket(List<BroadFileBroadInfoVO> broadInfoVOs, ChannelPO channel) throws Exception {
		if (broadInfoVOs == null || broadInfoVOs.isEmpty()) return;
		List<BroadFileBroadInfoVO> qtUsers = new ArrayList<BroadFileBroadInfoVO>();
		List<BroadFileBroadInfoVO> terminalUsers = new ArrayList<BroadFileBroadInfoVO>();
		for (BroadFileBroadInfoVO infoVO : broadInfoVOs) {
			if (infoVO.getUserEquipType().equals(UserEquipType.QT.toString())) {
				qtUsers.add(infoVO);
			} else if (infoVO.getUserEquipType().equals(UserEquipType.PUSH.toString())) {
				terminalUsers.add(infoVO);
			}
		}
		if (!qtUsers.isEmpty()) {
			List<Long> qtUserIds = qtUsers.stream().map(BroadFileBroadInfoVO::getUserId).collect(Collectors.toList());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("channelId", channel.getId());
			for (Long id : qtUserIds) {
				websocketMessageService.send(id, jsonObject.toJSONString(), WebsocketMessageType.COMMAND);
			}
		}
//		if (!terminalUsers.equals(UserEquipType.PUSH.toString())) {
//			List<Long> teminalUserIds = terminalUsers.stream().map(BroadFileBroadInfoVO::getUserId).collect(Collectors.toList());
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("channelId", channel.getId());
//			for (Long id : teminalUserIds) {
//				websocketMessageService.send(id, jsonObject.toJSONString(), WebsocketMessageType.COMMAND);
//			}
//		}
	}
	
	/**
	 * 给push终端用户下节目单<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午9:02:54
	 * @param ChannelPO channel 频道信息
	 */
	public void sendPush(Long channelId, Boolean clear) throws Exception {
		List<String> areaVOs = areaQuery.getCheckAreaIdList(channelId);
		if (areaVOs == null || areaVOs.size() <= 0) return;
		
		//播发参数
		JSONObject broadJsonObject = new JSONObject();
		broadJsonObject.put("fileType", "0");
		if (clear != null && clear) {
			broadJsonObject.put("regionList", areaVOs);
			HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.IP_PUSH_SEND), broadJsonObject);
		} else {
			JSONObject json = getNewBroadJSON(channelId, false, true);
			MediaTxtVO txtVO = mediaTxtService.addJson(json.toJSONString(), null, "");
//			String txtFtpUrl = adapter.changeHttpToFtp(txtVO.getPreviewUrl());
			String txtFtpUrl = txtVO.getPreviewUrl();
			String newVersion = versionSendQuery.getNewVersion(versionSendQuery.getLastVersion(channelId));
			String broadIdString = new StringBufferWrapper().append(channelId)
					.append(DateUtil.format(new Date(), DateUtil.currentDateTimePattern))
					.toString();
			
//			broadJsonObject.put("id", broadIdString);
			broadJsonObject.put("regionList", areaVOs);
			broadJsonObject.put("filepath", adapter.getEncodeUrl(txtFtpUrl));
			broadJsonObject.put("fileSize", txtVO.getSize());
			JSONObject response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.IP_PUSH_SEND), broadJsonObject);
			if (response != null && response.containsKey("result") && response.getString("result").equals("1")){
				// 备份播发地区
				areaSendQuery.saveArea(channelId);
		
				// 保存播发版本
				versionSendQuery.addVersion(channelId, newVersion, broadIdString, null, "", VersionSendType.DOWNLOAD_FILE, "", "", txtVO);
			} else {
				throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.IP_PUSH_SEND.getAction(), response != null ? response.getString("message") : "");
			}
		}
	}
	
	/**
	 * 排期单添加时的处理<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午10:56:43
	 * @param Long channelId 频道id
	 */
	public void addScheduleDeal(Long channelId) throws Exception {
		startFileBroadcast(channelId);
	}
	
	/**
	 * 通过播发用户查询用户被占用频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月26日 上午10:57:16
	 * @param Long userId 用户id
	 * @return Long 频道id
	 */
	public Long getChannelIdFromUser(Long userId) throws Exception {
		List<BroadFileBroadInfoVO> broadInfoVOs = broadFileBroadInfoService.queryFromUserIds(new ArrayListWrapper<Long>().add(userId).getList());
		return (broadInfoVOs == null || broadInfoVOs.isEmpty()) ? null : broadInfoVOs.get(0).getChannelId();
	}
	
	/**
	 * 终端获取节目单<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午5:16:10
	 * @param Long channelId 频道id
	 * @return JSONObject
	 */
	public JSONObject getNewBroadJSON(Long channelId, Boolean ifFtpUrl, Boolean ifEncode) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		JSONObject textJson = new JSONObject();

//		textJson.put("file", "");
//		textJson.put("fileSize", "");
		JSONArray scheduleJsons = new JSONArray();
		List<ScheduleVO> schedules = scheduleQuery.getByChannelId(channelId);
		if (schedules == null || schedules.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
		for (ScheduleVO schedule : schedules) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("effectTime", schedule.getBroadDate());
			jsonObject.put("screens", this.programText(channel, programQuery.getProgram(schedule.getId()), ifFtpUrl, ifEncode));
			scheduleJsons.add(jsonObject);
		}
		
		textJson.put("schedules", scheduleJsons);
		return textJson;
	}
	
	/**
	 * 播发时媒资排表字段内容(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param ProgramVO program 分屏信息
	 */
	private List<JSONObject> programText(ChannelPO channel, ProgramVO program, Boolean ifFtpUrl, Boolean ifEncode) throws Exception {
		List<JSONObject> returnList = new ArrayList<JSONObject>();
		if (program != null) {
			JSONObject useTemplate = adapter.screenTemplate(program.getScreenId());
			if (useTemplate == null) return null;
			for (int i = 1; i <= program.getScreenNum(); i++) {
				JSONObject returnItem = adapter.serial(useTemplate, i);
				List<JSONObject> scheduleList = new ArrayList<JSONObject>();
				if (program.getScreenInfo() != null && program.getScreenInfo().size() > 0) {
					for (ScreenVO item : program.getScreenInfo()) {
						if (item.getSerialNum() != i)
							continue;
						JSONObject schedule = new JSONObject();
//						CsResourceVO resource = csResourceQuery.queryResourceById(item.getResourceId());
//						schedule.put("name", resource.getName());
//						if (resource.getType().equals("PUSH_LIVE")) {
//							schedule.put("freq", resource.getFreq());
//							schedule.put("audioPid", resource.getAudioPid());
//							schedule.put("videoPid", resource.getVideoPid());
//							schedule.put("audioType", resource.getAudioType());
//							schedule.put("videoType", resource.getVideoType());
//						} else {
//							String preiviewUrl = resource.getPreviewUrl();
//							if (ifFtpUrl) preiviewUrl = adapter.changeHttpToFtp(preiviewUrl);
//							if (ifEncode) preiviewUrl = adapter.getEncodeUrl(preiviewUrl);
//							schedule.put("previewUrl", preiviewUrl);
//							schedule.put("fileSize", resource.getSize());
//						}
//						schedule.put("index", item.getIndex());
//						scheduleList.add(schedule);
						
						String contentType = item.getContentType();
						if (contentType == null
								|| contentType.isEmpty()
								|| ScreenContentType.fromName(contentType).getType().equals("mims")
								|| ScreenContentType.fromName(contentType).equals(ScreenContentType.TERMINAL_MIMS)) {
							CsResourceVO resource = csResourceQuery.queryResourceById(item.getResourceId());
							if (resource.getType().equals("PUSH_LIVE")) {
								schedule.put("freq", resource.getFreq());
								schedule.put("audioPid", resource.getAudioPid());
								schedule.put("videoPid", resource.getVideoPid());
								schedule.put("audioType", resource.getAudioType());
								schedule.put("videoType", resource.getVideoType());
							} else {
								String preiviewUrl = resource.getPreviewUrl();
								if (ifFtpUrl) preiviewUrl = adapter.changeHttpToFtp(preiviewUrl);
								if (ifEncode) preiviewUrl = adapter.getEncodeUrl(preiviewUrl);
								schedule.put("previewUrl", preiviewUrl);
								schedule.put("fileSize", resource.getSize());
							}
							schedule.put("index", item.getIndex());
							scheduleList.add(schedule);
						} else if (ScreenContentType.fromName(contentType) == ScreenContentType.TEXT){
							schedule.put("index", item.getIndex());
							schedule.put("textContent", item.getTextContent());
							scheduleList.add(schedule);
						}
						if (!returnItem.containsKey("contentType") && contentType != null && !contentType.isEmpty()) {
							returnItem.put("contentType", ScreenContentType.fromName(contentType).getType());
						}
					}
				}
				if (!returnItem.containsKey("contentType")) {
					returnItem.put("contentType", ScreenContentType.TERMINAL_MIMS.getType());
				}
				returnItem.put("program", scheduleList);
				returnList.add(returnItem);
			}
		}
		return returnList;
	}
}
