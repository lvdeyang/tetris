package com.sumavision.tetris.cs.channel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import com.sumavision.tetris.commons.util.httprequest.HttpRequestUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.area.AreaQuery;
import com.sumavision.tetris.cs.bak.AreaSendQuery;
import com.sumavision.tetris.cs.bak.ResourceSendQuery;
import com.sumavision.tetris.cs.bak.SendBakService;
import com.sumavision.tetris.cs.bak.VersionSendPO;
import com.sumavision.tetris.cs.bak.VersionSendQuery;
import com.sumavision.tetris.cs.channel.exception.ChannelAbilityRequestErrorException;
import com.sumavision.tetris.cs.channel.exception.ChannelNotExistsException;
import com.sumavision.tetris.cs.channel.exception.ChannelUdpIpAndPortAlreadyExistException;
import com.sumavision.tetris.cs.menu.CsMenuQuery;
import com.sumavision.tetris.cs.menu.CsMenuService;
import com.sumavision.tetris.cs.menu.CsMenuVO;
import com.sumavision.tetris.cs.menu.CsResourceQuery;
import com.sumavision.tetris.cs.menu.CsResourceVO;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.ScheduleQuery;
import com.sumavision.tetris.cs.schedule.ScheduleService;
import com.sumavision.tetris.cs.schedule.ScheduleVO;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNoneToBroadException;
import com.sumavision.tetris.mims.app.media.compress.FileCompressVO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressService;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamService;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
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
	private CsMenuQuery csMenuQuery;

	@Autowired
	private CsResourceQuery csResourceQuery;

	@Autowired
	private AreaSendQuery areaSendQuery;

	@Autowired
	private ResourceSendQuery resourceSendQuery;

	@Autowired
	private VersionSendQuery versionSendQuery;

	@Autowired
	private ProgramQuery programQuery;

	@Autowired
	private AreaQuery areaQuery;

	@Autowired
	private MediaCompressService mediaCompressService;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SendBakService sendBakService;
	
	@Autowired
	private MediaVideoStreamService mediaVideoStreamService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private Adapter adapter;
	
	private Map<Long, Timer> timerMap = new HashMapWrapper<Long, Timer>().getMap();

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
	 * @return ChannelPO 频道
	 */
	public ChannelPO add(String name, String date, String broadWay, String previewUrlIp, String previewUrlPort, String remark) throws Exception {
		UserVO user = userQuery.current();
		
		ChannelPO channel = new ChannelPO();
		channel.setName(name);
		channel.setRemark(remark);
		channel.setDate(date);
		channel.setBroadWay(BroadWay.fromName(broadWay).getName());
		channel.setBroadcastStatus("未播发");
		channel.setGroupId(user.getGroupId());
		channel.setUpdateTime(new Date());
		
		if (BroadWay.fromName(broadWay) == BroadWay.ABILITY_BROAD) {
			if (channelDao.checkIpAndPortExists(previewUrlIp, previewUrlPort) == null) {
				MediaVideoStreamVO mediaVideoStream = mediaVideoStreamService.addVideoStreamTask(adapter.getUdpUrlFromIpAndPort(previewUrlIp, previewUrlPort), name);
				channel.setMediaId(mediaVideoStream.getId());
				channel.setPreviewUrlIp(previewUrlIp);
				channel.setPreviewUrlPort(previewUrlPort);
				channel.setBroadId(adapter.getNewId(channelDao.getAllAbilityId()));
			}else {
				throw new ChannelUdpIpAndPortAlreadyExistException();
			}
		}

		channelDao.save(channel);

		return channel;
	}

	/**
	 * 删除频道<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 频道id
	 */
	public void remove(Long id) throws Exception {
		ChannelPO channel = channelDao.findOne(id);
		if (channel.getMediaId() != null) mediaVideoStreamService.remove(channel.getMediaId());
		if (!channelQuery.sendAbilityRequest(BroadAbilityQueryType.DELETE, channel, null, null)) throw new ChannelAbilityRequestErrorException(BroadAbilityQueryType.DELETE.getRemark());
		channelDao.delete(channel);
		csMenuService.removeMenuByChannelId(id);
		scheduleService.removeByChannelId(id);
		sendBakService.removeBakFromChannel(id.toString());
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
	public ChannelPO edit(Long id, String name, String previewUrlIp, String previewUrlPort, String remark) throws Exception {
		ChannelPO channel = channelDao.findOne(id);
		if (channel == null) return null;
		
		if (BroadWay.fromName(channel.getBroadWay()) == BroadWay.ABILITY_BROAD) {
			if (channelDao.checkIpAndPortExists(id, previewUrlIp, previewUrlPort) == null) {
				channel.setPreviewUrlIp(previewUrlIp);
				channel.setPreviewUrlPort(previewUrlPort);
				mediaVideoStreamService.edit(channel.getMediaId(), adapter.getUdpUrlFromIpAndPort(previewUrlIp, previewUrlPort), name);
			}else {
				throw new ChannelUdpIpAndPortAlreadyExistException();
			}
		}
		channel.setName(name);
		channel.setRemark(remark);
		channel.setUpdateTime(new Date());
		channelDao.save(channel);

		return channel;
	}
	
	/**
	 * 停止播发(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public JSONObject stopAbilityBroadcast(Long channelId) throws Exception{
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) return getReturnJSON(false, "播发频道数据错误");
		
		if (channelQuery.sendAbilityRequest(BroadAbilityQueryType.STOP, channel, null, null)) {
			Timer timer = timerMap.get(channelId);
			if (timer != null) {
				timer.cancel();
			}
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
			channelDao.save(channel);
			
			return getReturnJSON(true, "");
		}else {
			return getReturnJSON(false, "请求能力失败");
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
		String versionSendNum = versionSendQuery.getBroadId(channelId);
		if (!versionSendNum.isEmpty()
				&& getChannelBroadstatus(channelId).equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
			JSONObject jsonParam = new JSONObject();
			List<String> ids = new ArrayList<String>();
			ids.add(versionSendNum);
			jsonParam.put("ids", ids);
			String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
			if (!url.isEmpty()) {
				JSONObject response = HttpRequestUtil
						.httpPost("http://" + url + "/ed/speaker/stopSendFile", jsonParam);
				if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
					return getReturnJSON(true, "");
				} else {
					return getReturnJSON(false, "未知错误，播发失败");
				}
			}else {
				return getReturnJSON(false, "播发地址为空");
			}
		} else {
			return getReturnJSON(false, "当前频道未处于可播发状态");
		}
	}
	
	/**
	 * 开始播发(能力播发，根据排期)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public JSONObject startBroad(Long channelId) throws Exception {
		ChannelPO channel = channelDao.findOne(channelId);
		
		if (channel == null) {
			throw new ChannelNotExistsException(channelId);
		}
		
		Long now = DateUtil.getLongDate();
		
		List<ScheduleVO> scheduleVOs = scheduleQuery.getByChannelId(channelId);
		
		if (scheduleVOs == null || scheduleVOs.isEmpty()) {
			throw new ScheduleNoneToBroadException(channel.getName());
		}
		
		if (!channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)) {
			channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING);
			channelDao.save(channel);
		}
		
		if (timerMap.containsKey(channelId)) {
			timerMap.get(channelId).cancel();
		}
		for (ScheduleVO scheduleVO : scheduleVOs) {
			Date broadDate = DateUtil.parse(scheduleVO.getBroadDate(), DateUtil.dateTimePattern);
			Long broadDateLong = broadDate.getTime();
			if (broadDateLong > now) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						try {
							if (channel.getBroadcastStatus().equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADING)){
								System.out.println(channelId + ":" + scheduleVO.getBroadDate());
								startAbilityBroadcast(channelId, scheduleVO.getId());
								startBroad(channelId);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, broadDate);
				timerMap.put(channelId, timer);
				break;
			}
			
			if (scheduleVOs.indexOf(scheduleVO) == scheduleVOs.size() - 1) {
				channel.setBroadcastStatus(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED);
				channelDao.save(channel);
			}
		}
		
		return getReturnJSON(true, ""); 
	}
	
	/**
	 * 开始播发(能力播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param Long scheduleId 排期id
	 */
	public JSONObject startAbilityBroadcast(Long channelId, Long scheduleId) throws Exception{
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) return getReturnJSON(false, "播发频道数据错误");
		
		JSONObject output = new JSONObject();
		output.put("proto-type", "udp-ts");
		output.put("ipv4", channel.getPreviewUrlIp());
		output.put("port", channel.getPreviewUrlPort());
		output.put("vport", "");
		output.put("aport", "");
		output.put("scramble", "none");
		output.put("key", "");
		if (channelQuery.sendAbilityRequest(channelQuery.broadCmd(channelId), channel, abilityProgramText(programQuery.getProgram(scheduleId)), output)) {
			channelQuery.saveBroad(channelId);
			return getReturnJSON(true, "");
		}else {
			return getReturnJSON(false, "请求能力失败");
		}
	}

	/**
	 * 开始播发(终端播发，有排期后未修改完成)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public JSONObject startTerminalBroadcast(Long channelId) throws Exception {
		ChannelPO channel = channelDao.findOne(channelId);
		if (channel == null) return getReturnJSON(false, "播发频道数据错误");
		// 校验播发状态
		String broadStatus = getChannelBroadstatus(channelId);
		if (!broadStatus.isEmpty() && !broadStatus.equals(ChannelBroadStatus.CHANNEL_BROAD_STATUS_BROADED)) {
			return getReturnJSON(false, "当前频道未处于可播发状态");
		}
		// 校验播发条件
		List<String> areaVOs = areaQuery.getCheckAreaIdList(channelId);
		if (areaVOs == null || areaVOs.size() <= 0) {
			return getReturnJSON(false, "播发地区为空，播发任务自动取消");
		}
		// 获取媒资增量
		List<CsResourceVO> addResourceList = resourceSendQuery.getAddResource(channelId, false);
		Map<String, CsResourceVO> resourceMap = new HashMap<String, CsResourceVO>();
		for (CsResourceVO item : addResourceList) {
			String[] previewUrl = item.getPreviewUrl().split("/");
			resourceMap.put(previewUrl[previewUrl.length - 1], item);
		}
		// 生成json字符串
		JSONObject textJson = new JSONObject();
		String newVersion = versionSendQuery.getNewVersion(versionSendQuery.getLastVersion(channelId));
		String effectTime = "null";

		textJson.put("fileSize", "");
		textJson.put("version", newVersion);
		textJson.put("effectTime", effectTime);
		textJson.put("dir", this.getMenuAndResourcePath(channelId));
//		textJson.put("files", this.getFilesPath(addResourceList));
		textJson.put("screens", this.programText(programQuery.getProgram(channelId)));

		// 打包
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		textJson.put("file", dateString + ".tar");

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
		
		MediaCompressVO mediaCompressVO = mediaCompressService.packageTar(textJson.toString(), fileCompressVOs);

		// 请求播发
		String broadIdString = channelId.toString() + newVersion.split("v")[1];
		JSONObject broadJsonObject = new JSONObject();

		broadJsonObject.put("id", broadIdString);
		String filePath = "";
		if (ChannelBroadStatus.getBroadcastIfLocal()) {
			filePath = mediaCompressVO.getUploadTmpPath();
		}else {
			filePath = mediaCompressVO.getPreviewUrl();
		}
		broadJsonObject.put("filePath", filePath);
		broadJsonObject.put("fileSize", mediaCompressVO.getSize());
		broadJsonObject.put("regionList", areaVOs);

		String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
		if (!url.isEmpty()) {
			JSONObject response = HttpRequestUtil
					.httpPost("http://" + url + "/ed/speaker/startSendFile", broadJsonObject);

			if (response != null && response.containsKey("result") && response.getString("result").equals("1")) {
				// 播发成功处理

				// 备份播发媒资全量
				resourceSendQuery.getAddResource(channelId, true);

				// 备份播发地区
				areaSendQuery.saveArea(channelId);

				// 保存播发版本
				versionSendQuery.addVersion(channelId, newVersion, broadIdString, mediaCompressVO, filePath);

				return getReturnJSON(true, "");
			} else {
				return getReturnJSON(false, "播发未知错误");
			}
		}else {
			return getReturnJSON(false, "播发地址为空");
		}
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
		VersionSendPO versionSendPO = versionSendQuery.getLastVersionSendPO(channelId);
		if (versionSendPO == null) {
			return getReturnJSON(false, "当前频道没有被储存的版本信息");
		}
		broadJsonObject.put("id", versionSendQuery.getBroadId(channelId));
		broadJsonObject.put("filePath", versionSendPO.getFilePath());
		broadJsonObject.put("fileSize", versionSendPO.getFileSize());
		broadJsonObject.put("regionList", areaSendQuery.getAreaIdList(channelId));

		String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
		if (!url.isEmpty()) {
			JSONObject response = HttpRequestUtil
					.httpPost("http://" + url + "/ed/speaker/startSendFile", broadJsonObject);

			return (response != null && response.containsKey("result") && response.getString("result").equals("1"))
					? getReturnJSON(true, "") : getReturnJSON(false, "播发未知错误");
		}else {
			return getReturnJSON(false, "播发服务器地址为空");
		}
	}

	private JSONObject getReturnJSON(Boolean ifSuccess, String message) {
		JSONObject returnJsonObject = new JSONObject();
		returnJsonObject.put("success", ifSuccess);
		returnJsonObject.put("message", message);
		return returnJsonObject;
	}

	/**
	 * 查询播发状态(终端播发状态)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 */
	public String getChannelBroadstatus(Long channelId) throws Exception {
		String status = "";
		
		String versionSendNum = versionSendQuery.getBroadId(channelId);
		if (!versionSendNum.isEmpty()) {
			List<String> ids = new ArrayList<String>();
			ids.add(versionSendNum);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ids", ids);
			String url = ChannelBroadStatus.getBroadcastIPAndPort(BroadWay.TERMINAL_BROAD);
			if (!url.isEmpty()) {
				JSONObject response = HttpRequestUtil
						.httpPost("http://" + url + "/ed/speaker/querySendFile", jsonObject);
				if (response != null && response.get("result").toString().equals("1") && response.get("data") != null) {
					JSONArray statusArray = (JSONArray) response.get("data");
					if (statusArray != null && statusArray.size() > 0) {
						JSONObject item = (JSONObject) statusArray.get(0);
						status = (item.containsKey("status") && item.get("status") != null)
								? channelQuery.getStatusFromNum(item.getString("status")) : "";
					}
				}
			}
		}

		return status;
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

	/**
	 * 播发时媒资排表字段内容(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param ProgramVO program 分屏信息
	 */
	private List<JSONObject> programText(ProgramVO program) throws Exception {
		List<JSONObject> returnList = new ArrayList<JSONObject>();
		if (program != null) {
			for (int i = 1; i <= program.getScreenNum(); i++) {
				JSONObject returnItem = this.screenItemInit(program.getScreenNum(), i);
				List<JSONObject> scheduleList = new ArrayList<JSONObject>();
				if (program.getScreenInfo() != null && program.getScreenInfo().size() > 0) {
					for (ScreenVO item : program.getScreenInfo()) {
						if (item.getSerialNum() != i)
							continue;
						JSONObject schedule = new JSONObject();
						CsResourceVO resource = csResourceQuery.queryResourceById(item.getResourceId());
						String[] previewUrlSplit = resource.getPreviewUrl().split("/");
						schedule.put("fileName", resource.getParentPath() + "/" + previewUrlSplit[previewUrlSplit.length - 1]);
						schedule.put("index", item.getIndex());
						scheduleList.add(schedule);
					}
				}
				returnItem.put("schedule", scheduleList);
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
				JSONObject resource = new JSONObject();
				String[] previewUrl = item.getPreviewUrl().split("/");
				resource.put("path", item.getParentPath() + "/" + previewUrl[previewUrl.length - 1]);
				resource.put("type", "file");
				returnObject.add(resource);
			}
		}
		return returnObject;
	}

	private List<JSONObject> getFilesPath(List<CsResourceVO> files) {
		List<JSONObject> filesPath = new ArrayList<JSONObject>();
		if (files != null && files.size() > 0) {
			for (CsResourceVO item : files) {
				JSONObject file = new JSONObject();
				String[] previewUrl = item.getPreviewUrl().split("/");
				file.put("sourcePath", "./" + previewUrl[previewUrl.length - 1]);
				file.put("destPath", item.getParentPath() + "/" + previewUrl[previewUrl.length - 1]);
				filesPath.add(file);
			}
		}
		return filesPath;
	}

	/**
	 * 分屏字段内容(终端播发)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long screenNum 分屏数
	 * @param int serialNum 屏幕位置
	 */
	private JSONObject screenItemInit(Long screenNum, int serialNum) {
		JSONObject returnItem = new JSONObject();
		returnItem.put("no", serialNum);
		switch (screenNum.toString()) {
		case "1":
			returnItem.put("width", "100%");
			returnItem.put("height", "100%");
			returnItem.put("top", "0%");
			returnItem.put("left", "0%");
			break;
		case "4":
			returnItem.put("width", "50%");
			returnItem.put("height", "50%");
			switch (serialNum) {
			case 1:
				returnItem.put("top", "0%");
				returnItem.put("left", "0%");
				break;
			case 2:
				returnItem.put("top", "0%");
				returnItem.put("left", "50%");
				break;
			case 3:
				returnItem.put("top", "50%");
				returnItem.put("left", "0%");
				break;
			case 4:
				returnItem.put("top", "50%");
				returnItem.put("left", "50%");
				break;
			}
			break;
		case "6":
			switch (serialNum) {
			case 1:
				returnItem.put("width", "66%");
				returnItem.put("height", "66%");
				returnItem.put("top", "0%");
				returnItem.put("left", "0%");
				break;
			case 2:
				returnItem.put("width", "34%");
				returnItem.put("height", "33%");
				returnItem.put("top", "0%");
				returnItem.put("left", "66%");
				break;
			case 3:
				returnItem.put("width", "34%");
				returnItem.put("height", "33%");
				returnItem.put("top", "33%");
				returnItem.put("left", "66%");
				break;
			case 4:
				returnItem.put("width", "33%");
				returnItem.put("height", "34%");
				returnItem.put("top", "66%");
				returnItem.put("left", "0%");
				break;
			case 5:
				returnItem.put("width", "33%");
				returnItem.put("height", "34%");
				returnItem.put("top", "66%");
				returnItem.put("left", "33%");
				break;
			case 6:
				returnItem.put("width", "34%");
				returnItem.put("height", "34%");
				returnItem.put("top", "66%");
				returnItem.put("left", "66%");
				break;
			}
			break;
		case "9":
			switch (serialNum) {
			case 1:
				returnItem.put("width", "33%");
				returnItem.put("height", "33%");
				returnItem.put("top", "0%");
				returnItem.put("left", "0%");
				break;
			case 2:
				returnItem.put("width", "33%");
				returnItem.put("height", "33%");
				returnItem.put("top", "0%");
				returnItem.put("left", "33%");
				break;
			case 3:
				returnItem.put("width", "34%");
				returnItem.put("height", "33%");
				returnItem.put("top", "0%");
				returnItem.put("left", "66%");
				break;
			case 4:
				returnItem.put("width", "33%");
				returnItem.put("height", "33%");
				returnItem.put("top", "33%");
				returnItem.put("left", "0%");
				break;
			case 5:
				returnItem.put("width", "33%");
				returnItem.put("height", "33%");
				returnItem.put("top", "33%");
				returnItem.put("left", "33%");
				break;
			case 6:
				returnItem.put("width", "34%");
				returnItem.put("height", "33%");
				returnItem.put("top", "33%");
				returnItem.put("left", "66%");
				break;
			case 7:
				returnItem.put("width", "33%");
				returnItem.put("height", "34%");
				returnItem.put("top", "66%");
				returnItem.put("left", "0%");
				break;
			case 8:
				returnItem.put("width", "33%");
				returnItem.put("height", "34%");
				returnItem.put("top", "66%");
				returnItem.put("left", "33%");
				break;
			case 9:
				returnItem.put("width", "34%");
				returnItem.put("height", "34%");
				returnItem.put("top", "66%");
				returnItem.put("left", "66%");
				break;
			}
			break;
		}
		return returnItem;
	}
}
