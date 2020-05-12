package com.sumavision.tetris.cs.channel.broad.terminal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.area.AreaQuery;
import com.sumavision.tetris.cs.bak.AreaSendQuery;
import com.sumavision.tetris.cs.bak.ResourceSendQuery;
import com.sumavision.tetris.cs.bak.VersionSendPO;
import com.sumavision.tetris.cs.bak.VersionSendQuery;
import com.sumavision.tetris.cs.bak.VersionSendType;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelBroadStatus;
import com.sumavision.tetris.cs.channel.ChannelDAO;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.ChannelType;
import com.sumavision.tetris.cs.channel.SetTerminalBroadBO;
import com.sumavision.tetris.cs.channel.broad.terminal.requestBO.RequestServerBO;
import com.sumavision.tetris.cs.channel.broad.terminal.requestBO.RequestTerminalBO;
import com.sumavision.tetris.cs.channel.broad.terminal.requestBO.RequestTerminalDirBO;
import com.sumavision.tetris.cs.channel.broad.terminal.requestBO.RequestTerminalScheduleBO;
import com.sumavision.tetris.cs.channel.broad.terminal.requestBO.RequestTerminalScheduleScreenBO;
import com.sumavision.tetris.cs.channel.broad.terminal.requestBO.RequestTerminalScheduleScreenProgramBO;
import com.sumavision.tetris.cs.channel.exception.ChannelAlreadyBroadException;
import com.sumavision.tetris.cs.channel.exception.ChannelAlreadyStopException;
import com.sumavision.tetris.cs.channel.exception.ChannelTerminalNoneAreaException;
import com.sumavision.tetris.cs.channel.exception.ChannelTerminalRequestErrorException;
import com.sumavision.tetris.cs.menu.CsMenuQuery;
import com.sumavision.tetris.cs.menu.CsMenuVO;
import com.sumavision.tetris.cs.menu.CsResourceQuery;
import com.sumavision.tetris.cs.menu.CsResourceVO;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenContentType;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.cs.schedule.ScheduleVO;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNoneToBroadException;
import com.sumavision.tetris.mims.app.media.compress.FileCompressVO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressService;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mims.config.server.ServerProps;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class BroadTerminalService {
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private ChannelDAO channelDAO;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private BroadTerminalQuery broadTerminalQuery;
	
	@Autowired
	private CsMenuQuery csMenuQuery;
	
	@Autowired
	private CsResourceQuery csResourceQuery;
	
	@Autowired
	private AreaQuery areaQuery;
	
	@Autowired
	private ResourceSendQuery resourceSendQuery;
	
	@Autowired
	private VersionSendQuery versionSendQuery;
	
	@Autowired
	private AreaSendQuery areaSendQuery;
	
	@Autowired
	private BroadTerminalBroadInfoQuery broadTerminalBroadInfoQuery;
	
	@Autowired
	private BroadTerminalBroadInfoService broadTerminalBroadInfoService;
	
	@Autowired
	private MimsServerPropsQuery mimsServerPropsQuery;
	
	@Autowired
	private com.sumavision.tetris.cs.config.ServerProps csServerProps;
	
	@Autowired
	private MediaCompressService mediaCompressService;
	
	@Autowired
	private Adapter adapter;
	
	/**
	 * 创建终端播发频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月19日 下午2:12:27
	 * @param UserVO user 用户信息
	 * @param String name 频道名称
	 * @param String date 创建日期
	 * @param String remark 备注
	 * @param String level 播发等级
	 * @param Boolean hasFile 是否携带文件
	 * @param ChannelType type 本地或远程创建频道
	 * @param Boolean encryption 是否加密播发
	 * @param Boolean autoBroad 是否智能播发
	 * @return ChannelPO 创建的频道
	 */
	public ChannelPO add(
			UserVO user,
			String name,
			String date,
			String remark,
			SetTerminalBroadBO terminalBroadBO,
			ChannelType type,
			Boolean encryption,
			Boolean autoBroad) throws Exception {
		ChannelPO channel = new ChannelPO();
		channel.setName(name);
		channel.setRemark(remark);
		channel.setDate(date);
		channel.setBroadWay(BroadWay.TERMINAL_BROAD.getName());
		channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_INIT.getName());
		channel.setGroupId(user.getGroupId());
		channel.setUpdateTime(new Date());
		channel.setEncryption(encryption);
		channel.setAutoBroad(autoBroad);
		channel.setType(type.toString());

		channelDAO.save(channel);
		
		broadTerminalBroadInfoService.saveInfo(channel.getId(), terminalBroadBO);
		
		return channel;
	}
	
	/**
	 * 开始播发(终端播发，有排期后未修改完成)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param String 资源id数组
	 */
	public JSONObject startTerminalBroadcast(Long channelId, String resourceIds) throws Exception {
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		if (ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName().equals(channel.getBroadcastStatus())) throw new ChannelAlreadyBroadException(channel.getName());
		
		//是否携带文件播发
		BroadTerminalBroadInfoPO broadInfoVO = broadTerminalBroadInfoQuery.findByChannelId(channelId);
		Boolean hasFile = broadInfoVO.getHasFile() == null || broadInfoVO.getHasFile();
		
		//获取新播发版本号
		String newVersion = versionSendQuery.getNewVersion(versionSendQuery.getLastVersion(channelId));
		
		//获取播发地区信息
		List<String> areaVOs = areaQuery.getCheckAreaIdList(channelId);
		if (areaVOs == null || areaVOs.size() <= 0) throw new ChannelTerminalNoneAreaException();
		
		//初始化播发协议
		String broadIdString = new StringBufferWrapper().append(channelId)
				.append(DateUtil.format(new Date(), DateUtil.currentDateTimePattern))
				.toString();
		RequestServerBO requestServerBO = new RequestServerBO()
				.setHasFile(hasFile ? 1 : 0)
				.setLevel(BroadTerminalLevelType.fromName(broadInfoVO.getLevel()).getLevel())
				.setId(broadIdString)
				.setRegionList(areaVOs)
				.setFileType("0");
		
//		JSONObject broadJsonObject = new JSONObject();
//		broadJsonObject.put("hasfile", hasFile ? 1 : 0);
//		broadJsonObject.put("level", BroadTerminalLevelType.fromName(broadInfoVO.getLevel()).getLevel());
//		broadJsonObject.put("id", broadIdString);
//		broadJsonObject.put("regionList", areaVOs);
//		broadJsonObject.put("fileType", "0");
		
		//获取排期单
		List<ScheduleVO> schedules = scheduleQuery.getByChannelId(channelId);
		if (schedules == null || schedules.isEmpty()) throw new ScheduleNoneToBroadException(channel.getName());
		
		MediaCompressVO mediaCompressVO = null;
		String filePath = "";
		String zoneStorePath = "";
		String zoneDownloadPath = "";
		if (hasFile) {
			// 获取媒资增量
			//List<CsResourceVO> addResourceList = resourceSendQuery.getAddResource(channelId, false);
			//获取媒资全量或手选资源
			List<CsResourceVO> addResourceList = new ArrayList<CsResourceVO>();
			if (resourceIds == null){
				addResourceList = csResourceQuery.getResourcesFromChannelId(channelId);}
			else if(resourceIds.isEmpty()){
				
			} else {
				List<Long> resourceIdList = JSONArray.parseArray(resourceIds, Long.class);
				addResourceList = csResourceQuery.queryResourceByIds(resourceIdList);
			}
//			Map<String, CsResourceVO> resourceMap = new HashMap<String, CsResourceVO>();
//			for (CsResourceVO item : addResourceList) {
//				String[] previewUrl = item.getPreviewUrl().split("/");
//				resourceMap.put(previewUrl[previewUrl.length - 1], item);
//			}
			// 生成json字符串
//			JSONObject textJson = new JSONObject();
//			String effectTime = "null";

//			textJson.put("fileSize", "");
//			textJson.put("version", newVersion);
//			textJson.put("dir", this.getMenuAndResourcePath(channelId));
////			textJson.put("files", this.getFilesPath(addResourceList));
//			Long scheduleId = schedules.get(0).getId();
//			ProgramVO programVO = programQuery.getProgram(scheduleId);
//			textJson.put("screens", this.programText(programVO));
//			textJson.put("screensOrient", programVO.getOrient() == null || programVO.getOrient().isEmpty() ? "horizontal" : programVO.getOrient());
			RequestTerminalBO terminalScheduleBO = new RequestTerminalBO()
					.setFileSize("")
					.setVersion(newVersion)
					.setDir(this.getMenuAndResourcePathToBO(channelId));
			List<RequestTerminalScheduleBO> scheduleBOs = new ArrayList<RequestTerminalScheduleBO>();
			for (ScheduleVO schedule : schedules) {
				RequestTerminalScheduleBO scheduleBO = new RequestTerminalScheduleBO()
						.setEffectTime(schedule.getBroadDate())
						.setEndTime(schedule.getEndDate());
				Long scheduleId = schedule.getId();
				ProgramVO programVO = programQuery.getProgram(scheduleId);
				if (programVO == null) continue;
				scheduleBO.setScreensOrient(programVO.getOrient() == null || programVO.getOrient().isEmpty() ? "horizontal" : programVO.getOrient());
				scheduleBO.setScreens(programTextToBO(programVO));
				scheduleBOs.add(scheduleBO);
			}
			terminalScheduleBO.setSchedules(scheduleBOs);
					
			// 打包
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(currentTime);
//			textJson.put("file", dateString + ".tar");
			terminalScheduleBO.setFile(dateString + ".tar");

			List<String> mimsUuidList = new ArrayList<String>();
			for (CsResourceVO item : addResourceList) {
				mimsUuidList.add(item.getMimsUuid());
			}
			
			List<FileCompressVO> fileCompressVOs = new ArrayList<FileCompressVO>();
			if (addResourceList!= null && addResourceList.size() > 0) {
				for(CsResourceVO item : addResourceList){
					fileCompressVOs.add(new FileCompressVO().setPath(item.getParentPath()).setUuid(item.getMimsUuid()));
				}
			}
			
//			mediaCompressVO = mediaCompressService.packageTar(textJson.toString(), fileCompressVOs);
			mediaCompressVO = mediaCompressService.packageTar(JSONObject.toJSONString(terminalScheduleBO), fileCompressVOs);
			
			filePath = "";
			if (adapter.getBroadcastIfLocal()) {
				filePath = mediaCompressVO.getUploadTmpPath();
			}else {
				String previewUrl = mediaCompressVO.getPreviewUrl();
				ServerProps mimsServerProps = mimsServerPropsQuery.queryProps();
				String netIp = mimsServerProps.getIp();
				String localIp = mimsServerProps.getFtpIp();
				String[] split = previewUrl.split(netIp);
				StringBufferWrapper newPreviewUrl = new StringBufferWrapper();
				if (split.length == 2){
					newPreviewUrl.append(split[0]).append(localIp).append(split[1]);
				} else {
					newPreviewUrl.append(previewUrl);
				}
				filePath = newPreviewUrl.toString();
			}
			requestServerBO.setFilePath(filePath).setFileSize(mediaCompressVO.getSize());
//			broadJsonObject.put("filePath", filePath);
//			broadJsonObject.put("fileSize", mediaCompressVO.getSize());
			
//			Map<String, String> zonePath = versionSendQuery.getZonePath(channel);
//			zoneStorePath = zonePath.get("zoneStorePath");
//			zoneDownloadPath = zonePath.get("zoneDownloadUrl");
		} else {
			Long scheduleId = schedules.get(0).getId();
//			broadJsonObject.put("filePath", "");
//			broadJsonObject.put("fileSize", "");
			ScreenVO screen = liveScreen(programQuery.getProgram(scheduleId));
//			broadJsonObject.put("freq", screen.getFreq());
//			broadJsonObject.put("audioPid", screen.getAudioPid());
//			broadJsonObject.put("videoPid", screen.getVideoPid());
			requestServerBO.setFilePath("")
			.setFileSize("")
			.setFreq(screen.getFreq())
			.setAudioPid(screen.getAudioPid())
			.setVideoPid(screen.getVideoPid());
		}

//		JSONObject response = HttpRequestUtil.httpJsonPost(adapter.getTerminalUrl(BroadTerminalQueryType.START_SEND_FILE), JSONObject.toJSONString(requestServerBO));
//
//		if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
//			// 播发成功处理
//			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName());
//			channelDAO.save(channel);
//
//			// 备份播发媒资全量
//			resourceSendQuery.getAddResource(channelId, true);
//
//			// 备份播发地区
//			areaSendQuery.saveArea(channelId);
//
//			// 保存播发版本
//			versionSendQuery.addVersion(channelId, newVersion, broadIdString, mediaCompressVO, filePath, hasFile ? VersionSendType.BROAD_FILE : VersionSendType.BROAD_LIVE, zoneStorePath, zoneDownloadPath, null);

			return getReturnJSON(true, "");
//		} else {
//			throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.START_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
//		}
	}
	
	/**
	 * 重新播发(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public JSONObject restartBroadcast(Long channelId) throws Exception {
		JSONObject broadJsonObject = new JSONObject();
		VersionSendPO versionSendPO = versionSendQuery.getLastBroadVersionPO(channelId);
		if (versionSendPO == null) {
			throw new ChannelTerminalRequestErrorException("重新播发", "当前频道没有被储存的版本信息");
		}
		broadJsonObject.put("id", versionSendQuery.getLastBroadId(channelId));
		broadJsonObject.put("filePath", versionSendPO.getFilePath());
		broadJsonObject.put("fileSize", versionSendPO.getFileSize());
		broadJsonObject.put("regionList", areaSendQuery.getAreaIdList(channelId));

		JSONObject response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.RESTART_SEND_FILE), broadJsonObject);
		if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
			return getReturnJSON(true, "");
		} else {
			throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.RESTART_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
		}
	}
	
	/**
	 * 资源目录同步到终端<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月15日 上午11:50:26
	 * @param Long channelId 频道id
	 */
	public void updateToTerminal(Long channelId) throws Exception {
		// 校验播发条件
		List<String> areaVOs = areaQuery.getCheckAreaIdList(channelId);
		if (areaVOs == null || areaVOs.size() <= 0) throw new ChannelTerminalNoneAreaException();
		//获取媒资全量或手选资源
//		List<CsResourceVO> addResourceList = new ArrayList<CsResourceVO>();
//		addResourceList = csResourceQuery.getResourcesFromChannelId(channelId);
//		Map<String, CsResourceVO> resourceMap = new HashMap<String, CsResourceVO>();
//		for (CsResourceVO item : addResourceList) {
//			String[] previewUrl = item.getPreviewUrl().split("/");
//			resourceMap.put(previewUrl[previewUrl.length - 1], item);
//		}
		// 生成json字符串
		JSONObject textJson = new JSONObject();
		String newVersion = versionSendQuery.getNewVersion(versionSendQuery.getLastVersion(channelId));

		textJson.put("updateDir", this.getMenuAndResourcePath(channelId));

		// 打包
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		textJson.put("file", dateString + ".tar");

		List<FileCompressVO> fileCompressVOs = new ArrayList<FileCompressVO>();
//		if (addResourceList!= null && addResourceList.size() > 0) {
//			for(CsResourceVO item : addResourceList){
//				fileCompressVOs.add(new FileCompressVO().setPath(item.getParentPath()).setUuid(item.getMimsUuid()));
//			}
//		}
		
		MediaCompressVO mediaCompressVO = mediaCompressService.packageTar(textJson.toString(), fileCompressVOs);

		// 请求播发
		String broadIdString = channelId.toString() + newVersion.split("v")[1];
		JSONObject broadJsonObject = new JSONObject();
		broadJsonObject.put("hasfile", 1);
		broadJsonObject.put("level", 9);

		broadJsonObject.put("id", broadIdString);
		String filePath = "";
		if (adapter.getBroadcastIfLocal()) {
			filePath = mediaCompressVO.getUploadTmpPath();
		}else {
			filePath = mediaCompressVO.getPreviewUrl();
		}
		broadJsonObject.put("filePath", filePath);
		broadJsonObject.put("fileSize", mediaCompressVO.getSize());
		broadJsonObject.put("regionList", areaVOs);

		JSONObject response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.START_SEND_FILE), broadJsonObject);

		if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
			// 保存播发版本
			versionSendQuery.addUpdateVersion(channelId, newVersion, broadIdString, mediaCompressVO, filePath);
		} else {
			throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.START_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
		}
	}
	
	/**
	 * 停止播发(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public JSONObject stopTerminalBroadcast(Long channelId) throws Exception {
		Map<Long, Timer> timerMap = channelService.getTimerMap();
		ChannelPO channel = channelQuery.findByChannelId(channelId);
		Timer timer = timerMap.get(channelId);
		if (timer != null) {
			timer.cancel();
		}
		String versionSendNum = versionSendQuery.getLastBroadId(channelId);
		if (!versionSendNum.isEmpty()
				&& broadTerminalQuery.getChannelBroadstatus(channelId).equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING.getName())) {
			JSONObject jsonParam = new JSONObject();
			List<String> ids = new ArrayList<String>();
			ids.add(versionSendNum);
			jsonParam.put("ids", ids);
			jsonParam.put("stopFlag", false);
			JSONObject response = HttpRequestUtil.httpPost(adapter.getTerminalUrl(BroadTerminalQueryType.STOP_SEND_FILE), jsonParam);
			if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
				return getReturnJSON(true, "");
			} else {
				throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.STOP_SEND_FILE.getAction(), response != null ? response.getString("message") : "");
			}
		} else {
			throw new ChannelAlreadyStopException(channelQuery.findByChannelId(channelId).getName());
		}
	}
	
	/**
	 * 重置终端补包地址<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月18日 下午4:04:23
	 */
	public void resetZonePath() throws Exception {
		JSONObject jsonParam = new JSONObject();
		String ip = csServerProps.getIp();
		String port = csServerProps.getPort();
		jsonParam.put("ip", ip);
		jsonParam.put("port", port);
//		JSONObject response = HttpRequestUtil.httpPost(BroadTerminalQueryType.RESET_ZONE_URL.getUrl(), jsonParam);
//		if (response == null || !response.containsKey("result") || !response.getString("result").equals("1")) {
//			throw new ChannelTerminalRequestErrorException(BroadTerminalQueryType.RESET_ZONE_URL.getAction(), response != null ? response.getString("message") : "");
//		}
	}
	
	private JSONObject getReturnJSON(Boolean ifSuccess, String message) {
		JSONObject returnJsonObject = new JSONObject();
		returnJsonObject.put("success", ifSuccess);
		returnJsonObject.put("message", message);
		return returnJsonObject;
	}
	
	/**
	 * 播发时媒资排表字段内容(终端文件播发，单个排期的节目列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param ProgramVO program 分屏信息
	 */
	private List<JSONObject> programText(ProgramVO program) throws Exception {
		List<JSONObject> returnList = new ArrayList<JSONObject>();
		if (program != null) {
			//获取分屏模板信息
			JSONObject useTemplate = adapter.screenTemplate(program.getScreenId());
			if (useTemplate == null) return null;
			
			//遍历每一个分屏(i直接标识分屏序号)
			for (int i = 1; i <= program.getScreenNum(); i++) {
				JSONObject returnItem = adapter.serial(useTemplate, i);
				List<JSONObject> scheduleList = new ArrayList<JSONObject>();
				if (program.getScreenInfo() != null && program.getScreenInfo().size() > 0) {
					
					//遍历所有分屏节目列表把节目对应到分屏下
					for (ScreenVO item : program.getScreenInfo()) {
						if (item.getSerialNum() != i)
							continue;
						JSONObject schedule = new JSONObject();
						
						//区分节目内容类型
						String contentType = item.getContentType();
						if (contentType == null
								|| contentType.isEmpty()
								|| ScreenContentType.fromName(contentType).getType().equals("mims")) {
							CsResourceVO resource = csResourceQuery.queryResourceById(item.getResourceId());
							if (resource.getType().equals("PUSH_LIVE")) {
								schedule.put("freq", resource.getFreq());
								schedule.put("audioPid", resource.getAudioPid());
								schedule.put("videoPid", resource.getVideoPid());
								schedule.put("audioType", resource.getAudioType());
								schedule.put("videoType", resource.getVideoType());
							} else {
								String[] previewUrlSplit = resource.getPreviewUrl().split("/");
								schedule.put("fileName", resource.getParentPath() + "/" + previewUrlSplit[previewUrlSplit.length - 1]);
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
				returnItem.put("schedule", scheduleList);
				returnList.add(returnItem);
			}
		}
		return returnList;
	}
	
	/**
	 * 播发时媒资排表字段内容(终端文件播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param ProgramVO program 分屏信息
	 */
	private ScreenVO liveScreen(ProgramVO program) throws Exception {
		if (program != null) {
			List<ScreenVO> screens = program.getScreenInfo();
			if (screens != null && !screens.isEmpty()){
				for (ScreenVO screenVO : screens) {
					CsResourceVO resourceVO = csResourceQuery.queryResourceById(screenVO.getResourceId());
					if (resourceVO.getType().equals("PUSH_LIVE")) {
						return screenVO;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 播发时cs媒资目录树字段内容(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	private List<JSONObject> getMenuAndResourcePath(Long channelId) throws Exception {
		List<CsMenuVO> menuTree = csMenuQuery.queryMenuTree(channelId);
		List<JSONObject> dirList = new ArrayList<JSONObject>();
		if (menuTree != null && menuTree.size() > 0) {
			dirList = getDirList(menuTree);
		}
		return dirList;
	}
	
	/**
	 * 递归获取目录树(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<CsMenuVO> parentSubs 目录列表
	 */
	private List<JSONObject> getDirList(List<CsMenuVO> parentSubs) throws Exception {
		List<JSONObject> returnObject = new ArrayList<JSONObject>();
		if (parentSubs != null && parentSubs.size() > 0) {
			for (CsMenuVO item : parentSubs) {
				JSONObject sub = new JSONObject();
				sub.put("path", item.getParentPath() + "/" + item.getName());
				sub.put("type", "folder");
				List<JSONObject> subs = new ArrayList<JSONObject>();
				subs.addAll(this.getDirList(item.getSubColumns()));
				subs.addAll(this.getFileList(item.getId()));
				sub.put("subs", subs);
				returnObject.add(sub);
			}
		}
		return returnObject;
	}
	
	/**
	 * 根据目录id获取cs媒资(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long menuId 目录id
	 */
	private List<JSONObject> getFileList(Long menuId) throws Exception {
		List<JSONObject> returnObject = new ArrayList<JSONObject>();
		List<CsResourceVO> resourceList = csResourceQuery.queryMenuResources(menuId);
		if (resourceList != null && resourceList.size() > 0) {
			for (CsResourceVO item : resourceList) {
				if (item.getType().equals("PUSH_LIVE")) continue;
				JSONObject resource = new JSONObject();
				String[] previewUrl = item.getPreviewUrl().split("/");
				resource.put("path", item.getParentPath() + "/" + previewUrl[previewUrl.length - 1]);
				resource.put("type", "file");
				returnObject.add(resource);
			}
		}
		return returnObject;
	}
	
	/**
	 * 播发时媒资排表字段内容(终端文件播发，单个排期的节目列表)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param ProgramVO program 分屏信息
	 */
	private List<RequestTerminalScheduleScreenBO> programTextToBO(ProgramVO program) throws Exception {
		List<RequestTerminalScheduleScreenBO> returnList = new ArrayList<RequestTerminalScheduleScreenBO>();
		if (program != null) {
			//获取分屏模板信息
			JSONObject useTemplate = adapter.screenTemplate(program.getScreenId());
			if (useTemplate == null) return null;
			
			//遍历每一个分屏(i直接标识分屏序号)
			for (int i = 1; i <= program.getScreenNum(); i++) {
				JSONObject programItem = adapter.serial(useTemplate, i);
				RequestTerminalScheduleScreenBO returnItem = new RequestTerminalScheduleScreenBO().setFromJson(programItem);
				List<RequestTerminalScheduleScreenProgramBO> programBOs = new ArrayList<RequestTerminalScheduleScreenProgramBO>();
				if (program.getScreenInfo() != null && program.getScreenInfo().size() > 0) {
					
					//遍历所有分屏节目列表把节目对应到分屏下
					for (ScreenVO item : program.getScreenInfo()) {
						if (item.getSerialNum() != i)
							continue;
						RequestTerminalScheduleScreenProgramBO programBO = new RequestTerminalScheduleScreenProgramBO();
						
						//区分节目内容类型
						String contentType = item.getContentType();
						if (contentType == null
								|| contentType.isEmpty()
								|| ScreenContentType.fromName(contentType).getType().equals("file")) {
							CsResourceVO resource = csResourceQuery.queryResourceById(item.getResourceId());
							programBO.setFromResource(resource).setIndex(item.getIndex());
							String duration = item.getDuration();
							if (duration != null && !duration.isEmpty() && !duration.equals("-")) programBO.setDuration(Long.parseLong(duration));
						} else if (ScreenContentType.fromName(contentType) == ScreenContentType.TEXT){
							programBO.setIndex(item.getIndex()).setTextContent(item.getTextContent()).setMediaType(ScreenContentType.TEXT.getType());
						} else {
							programBO.setIndex(item.getIndex()).setMediaType(ScreenContentType.fromName(contentType).getType());
						}
						programBOs.add(programBO);
					}
				}
				returnItem.setProgram(programBOs);
				returnList.add(returnItem);
			}
		}
		return returnList;
	}
	
	/**
	 * 播发时cs媒资目录树字段内容(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	private List<RequestTerminalDirBO> getMenuAndResourcePathToBO(Long channelId) throws Exception {
		List<CsMenuVO> menuTree = csMenuQuery.queryMenuTree(channelId);
		List<RequestTerminalDirBO> dirList = new ArrayList<RequestTerminalDirBO>();
		if (menuTree != null && menuTree.size() > 0) {
			dirList = getDirListToBO(menuTree);
		}
		return dirList;
	}
	
	/**
	 * 递归获取目录树(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param List<CsMenuVO> parentSubs 目录列表
	 */
	private List<RequestTerminalDirBO> getDirListToBO(List<CsMenuVO> parentSubs) throws Exception {
		List<RequestTerminalDirBO> returnObject = new ArrayList<RequestTerminalDirBO>();
		if (parentSubs != null && parentSubs.size() > 0) {
			for (CsMenuVO item : parentSubs) {
				RequestTerminalDirBO sub = new RequestTerminalDirBO();
				List<RequestTerminalDirBO> subs = new ArrayList<RequestTerminalDirBO>();
				subs.addAll(this.getDirListToBO(item.getSubColumns()));
				subs.addAll(this.getFileListToBO(item.getId()));
				sub.setPath(item.getParentPath() + "/" + item.getName())
				.setType("folder")
				.setSubs(subs);
				returnObject.add(sub);
			}
		}
		return returnObject;
	}
	
	/**
	 * 根据目录id获取cs媒资(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long menuId 目录id
	 */
	private List<RequestTerminalDirBO> getFileListToBO(Long menuId) throws Exception {
		List<RequestTerminalDirBO> returnObject = new ArrayList<RequestTerminalDirBO>();
		List<CsResourceVO> resourceList = csResourceQuery.queryMenuResources(menuId);
		if (resourceList != null && resourceList.size() > 0) {
			for (CsResourceVO item : resourceList) {
				if (item.getType().equals("PUSH_LIVE")) continue;
				RequestTerminalDirBO resource = new RequestTerminalDirBO();
				String[] previewUrl = item.getPreviewUrl().split("/");
				resource.setPath(item.getParentPath() + "/" + previewUrl[previewUrl.length - 1]).setType("file");
				returnObject.add(resource);
			}
		}
		return returnObject;
	}
	
//	private List<JSONObject> getFilesPath(List<CsResourceVO> files) {
//		List<JSONObject> filesPath = new ArrayList<JSONObject>();
//		if (files != null && files.size() > 0) {
//			for (CsResourceVO item : files) {
//				JSONObject file = new JSONObject();
//				String[] previewUrl = item.getPreviewUrl().split("/");
//				file.put("sourcePath", "./" + previewUrl[previewUrl.length - 1]);
//				file.put("destPath", item.getParentPath() + "/" + previewUrl[previewUrl.length - 1]);
//				filesPath.add(file);
//			}
//		}
//		return filesPath;
//	}
}
