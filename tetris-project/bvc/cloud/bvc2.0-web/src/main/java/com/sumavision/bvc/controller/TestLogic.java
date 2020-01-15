package com.sumavision.bvc.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.record.GroupVO;
import com.sumavision.bvc.control.system.vo.AvtplGearsVO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskDAO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskPO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskService;
import com.sumavision.bvc.device.system.AvtplService;
import com.sumavision.bvc.meeting.logic.ExecuteBusiness;
import com.sumavision.bvc.meeting.logic.MeetingEntity;
import com.sumavision.bvc.meeting.logic.record.RecordService;
import com.sumavision.bvc.meeting.logic.record.omc.BoService;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;


/**
 * @className 测试逻辑处理
 * @author zsy
 * @date 2018.08.04
 *
 */
@Controller
@RequestMapping(value="/logic")
public class TestLogic {
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	CommandGroupRecordDAO commandGroupRecordDao;
	
	@Autowired
	CommandGroupMemberDAO commandGroupMemberDao;
	
	@Autowired
	private AVtplGearsDAO avtplGearsDAO;
	
	@Autowired
	private AvtplDAO avtplDAO;
	
	@Autowired
	private MeetingEntity meetingEntity;
	
	@Autowired
	private ExecuteBusiness executeBusiness;
	
	@Autowired
	private RecordService recordService;
	
	@Autowired
	private BoService boService;
	
	@Autowired
	private MonitorRecordPlaybackTaskService monitorRecordPlaybackTaskService;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private CommandCommonUtil commandCommonUtil;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;
	
	@Autowired
	private AvtplService avtplService;
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/authorizedPlaylist", method = RequestMethod.GET)
	public Object authorizedPlaylist(HttpServletRequest request) throws Exception{
		
		avtplService.generateDefaultAvtpls();
		
		//根据userId查找record
		Long userId = 1091L;
		List<CommandGroupRecordPO> records = commandGroupRecordDao.findByRecordUserIdAndRun(userId, false);
		HashMap<Long, List<CommandGroupRecordPO>> groupRecordMap = new HashMap<Long, List<CommandGroupRecordPO>>();
		for(CommandGroupRecordPO record : records){
			List<CommandGroupRecordPO> groupRecords = groupRecordMap.get(record.getGroupId());
			if(groupRecords == null){
				groupRecords = new ArrayListWrapper<CommandGroupRecordPO>().add(record).getList();
				groupRecordMap.put(record.getGroupId(), groupRecords);
			}else{
				groupRecords.add(record);
			}
		}
		
		JSONArray groups = new JSONArray();
		for(List<CommandGroupRecordPO> groupRecords : groupRecordMap.values()){
			GroupVO group = new GroupVO().set(groupRecords);
			groups.add(group);
		}		
		
		
		
		
		List<Long> memberUserIds = commandGroupMemberDao.findUserIdsByGroupId(78L);
		Long id0 = 2277L;
		for(Long id : memberUserIds){
			System.out.println(id.equals(id0));
		}
//		for(Object id : memberUserIds){
//			System.out.println(Long.valueOf(id.toString()).equals(id0));
//		}
//		Long id1 = Long.valueOf(memberUserIds.get(0).toString());
//		System.out.println(memberUserIds.contains(2277L));
//		System.out.println(memberUserIds.get(0).equals(id0));
//		System.out.println(id1.equals(id0));
		
		String bundleId = "JV210-124";
		
		List<String> cids = deviceGroupAuthorizationDao.findAuthorizationCidsByBundleId(bundleId);
		List<String> pids = deviceGroupAuthorizationDao.findAuthorizationPidsByBundleId(bundleId);
		
		JSONObject datas = new JSONObject();		
		datas.put("liveChannel", cids);
		datas.put("asset", pids);
//		return datas;
		return new HashMapWrapper<String, Object>().put("groups", groups).getMap();
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/boapi/{index}",method=RequestMethod.GET)
	public Object boapi(
			@PathVariable String index,
			HttpServletRequest request) throws Exception{
		
		switch(index){
		case "1":
			boService.createLocation(null);
			break;
		case "2":
			boService.createChannelCategory(null);
			break;
		case "3":
			boService.createVideoCategory(null);
		}
		
		JSONObject data = new JSONObject();
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/record", method = RequestMethod.GET)
	public Object record(HttpServletRequest request) throws Exception{
		String jsonStr = "{\"recordSet\":[ {\"uuid\": \"v-b-c111\",\"layerId\": 3,\"bundleId\": 1,\"channelId\": 2}]}";
		JSONObject recordJson = readJsonFromFile("record");
//		recordService.startOmcRecord(recordJson);
//		meetingEntity.recordOperationCombine(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/recordSuccess", method = RequestMethod.GET)
	public Object recordSuccess(HttpServletRequest request) throws Exception{
		String jsonStr = "{\"recordDel\":[ {\"uuid\": \"v-b-c111\",\"layerId\": 3,\"bundleId\": 1,\"channelId\": 2}]}";
		JSONObject recordJson = readJsonFromFile("excuteReturn");
//		recordService.startOmcRecord(recordJson);
//		meetingEntity.recordOperationCombine(recordJson);
		JSONObject successJson = new JSONObject();
		successJson.put("executeResult", recordJson);
		successJson.put("combinedOperation", JSON.parse(jsonStr));
		meetingEntity.executeSuccess(successJson);

		JSONObject data = new JSONObject();
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/recordCdn", method = RequestMethod.GET)
	public Object recordCdn(HttpServletRequest request) throws Exception{
		String jsonStr = "{\"recordSet\":[ {\"uuid\": \"v-b-c111\",\"layerId\": 3,\"bundleId\": 1,\"channelId\": 2}]}";
		JSONObject recordJson = readJsonFromFile("recordCdn");
		recordService.startOmcRecord(recordJson);
//		meetingEntity.recordOperationProcess(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/removePlayback/{id}", method = RequestMethod.GET)
	public Object removePlayback(
			@PathVariable String id,
			HttpServletRequest request) throws Exception{
		
		MonitorRecordPlaybackTaskPO task = monitorRecordPlaybackTaskDao.findOne(Long.parseLong(id));
		monitorRecordPlaybackTaskService.remove(Long.parseLong(id), Long.parseLong(task.getUserId()));
		return null;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		JSONObject orgOperation1 = readJsonFromFile("logicFromBusiness");
		executeBusiness.executeJsonOrder(orgOperation1);
		JSONObject combinedOperation = null;
		
//		JSONObject jsonObj = new JSONObject();
//		
//		JSONObject executeResult = new JSONObject();
//		String jsonStr = "{\"combineVideoLayout\":[ {\"uuid\": \"v-b-c111\",\"layerId\": 3,\"bundleId\": 1,\"channelId\": 2}]}";
//		executeResult = JSON.parseObject(jsonStr);
//		executeResult = readJsonFromFile("excuteReturn");
//		executeResult = new JSONObject();
//		jsonObj.put("executeResult", executeResult);
//		
//		JSONObject orgOperation = new JSONObject();
//		String jsonStr2 = "{\"combineVideoDel\":{\"encode\":[ {\"uuid\": \"v-b-c111\",\"layerId\": 3,\"bundleId\": 1,\"channelId\": 2}]}}";
//		orgOperation = JSON.parseObject(jsonStr2);
//		orgOperation = readJsonFromFile("logicFromBusiness");
//		combinedOperation = meetingEntity.operationCombine(orgOperation);
//		String cString = combinedOperation.toJSONString();
//		jsonObj.put("combinedOperation", combinedOperation);
//		
//		JSONObject data = new JSONObject();
		
		return combinedOperation;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/combineAudioSet", method = RequestMethod.GET)
	public Object combineAudioSet(HttpServletRequest request) throws Exception{
		JSONObject recordJson = readJsonFromFile("combineAudioSet");
		executeBusiness.executeJsonOrder(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/combineAudioUpdate", method = RequestMethod.GET)
	public Object combineAudioUpdate(HttpServletRequest request) throws Exception{
		JSONObject recordJson = readJsonFromFile("combineAudioUpdate");
		executeBusiness.executeJsonOrder(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/combineAudioDel", method = RequestMethod.GET)
	public Object combineAudioDel(HttpServletRequest request) throws Exception{
		JSONObject recordJson = readJsonFromFile("combineAudioDel");
		executeBusiness.executeJsonOrder(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/combineVideoSet", method = RequestMethod.GET)
	public Object combineVideoSet(HttpServletRequest request) throws Exception{
		JSONObject recordJson = readJsonFromFile("combineVideoSet");
		executeBusiness.executeJsonOrder(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/combineVideoUpdate", method = RequestMethod.GET)
	public Object combineVideoUpdate(HttpServletRequest request) throws Exception{
		JSONObject recordJson = readJsonFromFile("combineVideoUpdate");
		executeBusiness.executeJsonOrder(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/combineVideoDel", method = RequestMethod.GET)
	public Object combineVideoDel(HttpServletRequest request) throws Exception{
		JSONObject recordJson = readJsonFromFile("combineVideoDel");
		executeBusiness.executeJsonOrder(recordJson);

		JSONObject data = new JSONObject();
		return data;
	}
	
	/**
	 * @Title: 模板查询档位
	 * @param id 模板id
	 * @return avtplG:List<AvtplGearsVO> 业务角色
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load/{id}",method=RequestMethod.GET)
	public Object load(
			@PathVariable Long id,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		Set<AvtplGearsPO> gears = avtplDAO.findOne(id).getGears();
		
		List<AvtplGearsVO> _gears = AvtplGearsVO.getConverter(AvtplGearsVO.class).convert(gears, AvtplGearsVO.class);
		
		return new HashMapWrapper<String, Object>().put("rows", _gears)
												   .put("total", _gears.size())
												   .getMap();
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name 档位名称
	 * @param id  参数模板id
	 * @param videoBitRate
	 * @param videoBitRateSpare
	 * @param videoResolution
	 * @param videoResolutionSpare
	 * @param audioBitRate
	 * @param level 
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/{id}")
	public Object save(
			@PathVariable Long id,
			String name,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String audioBitRate,
			String level,
			HttpServletRequest request) throws Exception{
		
		AvtplPO tpl = avtplDAO.findOne(id);
		tpl.setUpdateTime(new Date());
		if(tpl.getGears() == null) tpl.setGears(new HashSet<AvtplGearsPO>());
		
		AvtplGearsPO gear = new AvtplGearsPO();
		gear.setName(name);
		gear.setVideoBitRate(videoBitRate);
		gear.setVideoBitRateSpare(videoBitRateSpare);
		gear.setVideoResolution(Resolution.fromName(videoResolution));
		gear.setVideoResolutionSpare(Resolution.fromName(videoResolutionSpare));
		gear.setAudioBitRate(audioBitRate);
		gear.setLevel(GearsLevel.fromName(level));
		gear.setUpdateTime(new Date());
		gear.setAvtpl(tpl);
		tpl.getGears().add(gear);
		avtplGearsDAO.save(gear);
		
		AvtplGearsVO _gear = new AvtplGearsVO().set(gear);
		
		return _gear;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param name 档位名称
	 * @param id  档位的id
	 * @param avtplId
	 * @param videoBitRate
	 * @param videoBitRateSpare
	 * @param videoResolution
	 * @param videoResolutionSpare
	 * @param audioBitRate
	 * @param level 
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String audioBitRate,
			String level,
			HttpServletRequest request) throws Exception{
		
		AvtplGearsPO gear = avtplGearsDAO.findOne(id);
		gear.setName(name);
		gear.setVideoBitRate(videoBitRate);
		gear.setVideoBitRateSpare(videoBitRateSpare);
		gear.setVideoResolution(Resolution.fromName(videoResolution));
		gear.setVideoResolutionSpare(Resolution.fromName(videoResolutionSpare));
		gear.setAudioBitRate(audioBitRate);
		gear.setLevel(GearsLevel.fromName(level));
		gear.setUpdateTime(new Date());
		avtplGearsDAO.save(gear);
		
		AvtplGearsVO _avtplGtpl = new AvtplGearsVO().set(gear);
		
		return _avtplGtpl;
	}
	
	/**
	 * @Title: 根据档位id删除数据 
	 * @param id
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		AvtplGearsPO gear = avtplGearsDAO.findOne(id);
		
		//解关联
		AvtplPO tpl = gear.getAvtpl();
		tpl.getGears().remove(gear);
		gear.setAvtpl(null);
		avtplDAO.save(tpl);
		
		avtplGearsDAO.delete(gear);
		return null;
	}
	
	/**
	 * @Title: 根据id批量删除 
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		
		//解关联
		List<AvtplGearsPO> gears = avtplGearsDAO.findAll(ids);
		AvtplPO tpl = gears.get(0).getAvtpl();
		for(AvtplGearsPO gear:gears){
			tpl.getGears().remove(gear);
			gear.setAvtpl(null);
		}
		avtplDAO.save(tpl);
		
		avtplGearsDAO.deleteInBatch(gears);
		return null;
	}
	
	/**
	 * 紧急情况使用：重置所有播放器<br/>
	 * <p>清空所有播放状态，如果播放器数量少于16，则重建</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午8:59:52
	 * @param keepSelf 是否保留观看自己的播放器，建议true
	 * @param userName
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/reset/all/players/{keepSelf}/{userName}",method=RequestMethod.GET)
	public Object resetAllPlayers(
			@PathVariable boolean keepSelf,
			@PathVariable String userName,
			HttpServletRequest request) throws Exception{
		
		commandCommonServiceImpl.resetAllPlayers(keepSelf, userName);
		return null;
	}
	
	public JSONObject readJsonFromFile(String name){
//		File file = new File(Thread.currentThread().getContextClassLoader().getResource(name + ".json").getFile());
//		File file = new File("/" + name + ".json");
//		String content = FileUtils.readFileToString(file,"UTF-8");
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name + ".json");
			String content = new BufferedReader(new InputStreamReader(inputStream))
					  .lines().parallel().collect(Collectors.joining("\n"));
			JSONObject json = JSON.parseObject(content);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
