package com.sumavision.bvc.control.device.command.group.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.CommandForwardServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.basic.remind.CommandRemindServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceServiceImpl;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/basic")
public class CommandBasicController {

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;

	@Autowired
	private CommandSilenceServiceImpl commandSilenceServiceImpl;

	@Autowired
	private CommandRemindServiceImpl commandRemindServiceImpl;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 
	 * 查询指挥的所有成员及状态<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:20:15
	 * @param id 指挥组id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/members")
	public Object queryMembers(
			String id,
			HttpServletRequest request) throws Exception{
		
//		UserVO user = userUtils.getUserFromSession(request);
		
		CommandGroupPO group = commandGroupDao.findOne(Long.parseLong(id));
		
		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		Object membersArray = queryGroupTree(group.getId());
		info.put("members", membersArray);
		List<CommandGroupRecordPO> runningRecords = commandGroupRecordDao.findByGroupIdAndRun(group.getId(), true);
		if(runningRecords.size() > 0){
			info.put("isRecord", true);
		}else{
			info.put("isRecord", false);
		}
				
		return info;
	}
		
	/**
	 * 新建指挥<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param userIdList json格式的用户id列表
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object save(
			String members,
			HttpServletRequest request) throws Exception{
		//TODO:区分创建者和主席
		UserVO user = userUtils.getUserFromSession(request);
		
		//生成一个name
		Date date = new Date();
		String createTime = DateUtil.format(date, DateUtil.dateTimePattern);
		String name = new StringBuilder().append(user.getName())
				   .append(" ")
				   .append(createTime)
				   .append(" ")
				   .append("指挥")
				   .toString();
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		
		CommandGroupPO group = commandBasicServiceImpl.save(user.getId(), user.getId(), user.getName(), name, GroupType.BASIC, userIdArray);
		
		JSONObject info = new JSONObject();
		info.put("id", group.getId().toString());
		info.put("name", group.getName());
		info.put("status", "状态得扩展");
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		Object membersArray = queryGroupTree(group.getId());
		info.put("members", membersArray);
				
		return info;
	}
	
	/**
	 * 删除指挥（批量）<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>日期：</b>2019年9月26日
	 * @param ids
	 * @return 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String ids,
			HttpServletRequest request) throws Exception{

		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray idsArray = JSON.parseArray(ids);
		List<Long> groupIds = new ArrayList<Long>();
		for(int i=0; i<idsArray.size(); i++){
			Long id = Long.parseLong(idsArray.getString(i));
			groupIds.add(id);
		}		
		
		commandBasicServiceImpl.remove(userId, groupIds);
		
		return null;
	}
		
	/**
	 * 进入指挥，获取指挥信息（批量）<br/>
	 * <p>非主席的成员会按照“接听”处理，主席不处理</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:48:08
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/enter")
	public Object enter(
			String ids,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		JSONArray idsArray = JSON.parseArray(ids);
		List<Long> groupIds = new ArrayList<Long>();
		for(int i=0; i<idsArray.size(); i++){
			Long id = Long.parseLong(idsArray.getString(i));
			groupIds.add(id);
		}		
		
		JSONArray groupInfos = commandBasicServiceImpl.enter(userId, groupIds);
		
		for(int i=0; i<groupInfos.size(); i++){
			JSONObject groupInfo = groupInfos.getJSONObject(i);
			Long groupId = Long.parseLong(groupInfo.getString("id"));
			Object members = queryGroupTree(groupId);
			groupInfo.put("members", members);
		}
		
		return groupInfos;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/refuse")
	public Object refuse(
			String id,
			HttpServletRequest request) throws Exception{

		Long userId = userUtils.getUserIdFromSession(request);
		
		commandBasicServiceImpl.refuse(userId, Long.parseLong(id));
		return null;
	}
	
	
	//查询某个指挥的所有成员及状态（方法名暂定）
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/tree/{groupId}", method = RequestMethod.GET)
	public Object queryTree(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		return null;
	}
	
	
	//查询当前指挥外的所有成员
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/except/group/{groupId}")
	public Object queryMembersExceptGroup(
			HttpServletRequest request) throws Exception{
		return null;
	}
	
	/**
	 * 开始指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:49:25
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start")
	public Object start(
			String id,
			HttpServletRequest request) throws Exception{
		
		Object chairSplits = commandBasicServiceImpl.start(Long.parseLong(id), -1);
		return chairSplits;
	}
	
	/**
	 * 停止指挥<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月24日 上午11:49:57
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String id,
			HttpServletRequest request) throws Exception{

		Long userId = userUtils.getUserIdFromSession(request);
		Object chairSplits = commandBasicServiceImpl.stop(userId, Long.parseLong(id), 0);
		return chairSplits;
	}
	
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/pause")
	public Object pause(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONArray chairSplits = commandBasicServiceImpl.pause(Long.parseLong(id));
		return chairSplits;
	}
	
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/pause/recover")
	public Object pauseRecover(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONArray chairSplits = commandBasicServiceImpl.pauseRecover(Long.parseLong(id));
		return chairSplits;
	}	
	
	
	//退出指挥
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/exit")
	public Object exit(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		List<Long> userIdList = new ArrayListWrapper<Long>().add(userId).getList();
		Object splits = commandBasicServiceImpl.removeMembers(Long.parseLong(id), userIdList, 0);
		return splits;
	}
	
	
	//添加成员
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/members")
	public Object addMembers(
			String id,
			String members,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		Object splits = commandBasicServiceImpl.addMembers(Long.parseLong(id), userIdArray);
		return splits;
	}	
		
	/**
	 * 强推成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:59:36
	 * @param id
	 * @param members
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/members")
	public Object removeMembers(
			String id,
			String members,
			HttpServletRequest request) throws Exception{
		
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		Object splits = commandBasicServiceImpl.removeMembers(Long.parseLong(id), userIdArray, 1);
		return splits;
	}
	
	
	/**
	 * 指挥中的一个成员开启对上静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:12:52
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/up/start")
	public Object silenceToHigher(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandSilenceServiceImpl.startSilence(Long.parseLong(id), userId, true, false);
		
		return null;
	}
	
	
	/**
	 * 指挥中的一个成员停止对上静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:13:25
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/up/stop")
	public Object stopSilenceToHigher(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandSilenceServiceImpl.stopSilence(Long.parseLong(id), userId, true, false);
		
		return null;
	}
	
	
	/**
	 * 指挥中的一个成员开启对下静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:13:42
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/down/start")
	public Object silenceToLower(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandSilenceServiceImpl.startSilence(Long.parseLong(id), userId, false, true);
		
		return null;
	}
	
	
	/**
	 * 指挥中的一个成员停止对下静默<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 上午11:13:56
	 * @param id 指挥id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/silence/down/stop")
	public Object stopSilenceToLower(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandSilenceServiceImpl.stopSilence(Long.parseLong(id), userId, false, true);
		
		return null;
	}
	
	/**
	 * 主席点播成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午1:51:11
	 * @param id
	 * @param userId 被点播成员的userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/vod/member/start")
	public Object vodMemberStart(
			String id,
			String userId,
			HttpServletRequest request) throws Exception{
		
		Long dstUserId = userUtils.getUserIdFromSession(request);
		UserBO userBO = userUtils.queryUserById(dstUserId);
		
		CommandGroupUserPlayerPO player = commandBasicServiceImpl.vodMemberStart(userBO, Long.parseLong(id), Long.parseLong(userId));
		
		return new CommandGroupUserPlayerSettingVO().set(player);
	}
	
	/**
	 * 主席关闭点播成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月2日 下午1:50:42
	 * @param businessId 指挥id-成员userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/vod/member/stop")
	public Object vodMemberStop(
			String businessId,
			HttpServletRequest request) throws Exception{
		
		Long dstUserId = userUtils.getUserIdFromSession(request);
		String groupId = businessId.split("-")[0];
		String srcUserId = businessId.split("-")[1];
		
		CommandGroupUserPlayerPO player = commandBasicServiceImpl.vodMemberStop(dstUserId, Long.parseLong(groupId), Long.parseLong(srcUserId));
		
		if(player != null){
			return JSON.toJSONString(new HashMapWrapper<String, Object>().put("serial", player.getLocationIndex()).getMap());
		}else{
			return null;
		}
	}
	
	/**
	 * 指挥转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:59:58
	 * @param id
	 * @param src
	 * @param dst
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/device")
	public Object forwardDevice(
			String id,
			String src,
			String dst,
			HttpServletRequest request) throws Exception{

		List<String> bundleIds = JSONArray.parseArray(src, String.class);
		List<Long> userIds = JSONArray.parseArray(dst, Long.class);
		
		List<ForwardReturnBO> result = commandForwardServiceImpl.forwardDevice(Long.parseLong(id), bundleIds, userIds);
		
		return result;
	}
	
	/**
	 * 成员同意转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:00:14
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/device/agree")
	public Object forwardDeviceAgree(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		List<Object> result = commandForwardServiceImpl.forwardAgree(userId, Long.parseLong(id), demandIds);
		
		return result;
	}
	
	/**
	 * 成员拒绝转发设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午11:37:43
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/device/refuse")
	public Object forwardDeviceRefuse(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		commandForwardServiceImpl.forwardRefuse(Long.parseLong(id), demandIds);
		
		return null;
	}
	
	/**
	 * 指挥转发文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:00:30
	 * @param id
	 * @param src
	 * @param dst
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/file")
	public Object forwardFile(
			String id,
			String src,
			String dst,
			HttpServletRequest request) throws Exception{

		List<String> resourceIds = JSONArray.parseArray(src, String.class);
		List<Long> userIds = JSONArray.parseArray(dst, Long.class);
		
		List<ForwardReturnBO> result = commandForwardServiceImpl.forwardFile(Long.parseLong(id), resourceIds, userIds);
		
		return result;
	}
	
	/**
	 * 成员同意转发文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:00:43
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/file/agree")
	public Object forwardFileAgree(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		List<Object> result = commandForwardServiceImpl.forwardAgree(userId, Long.parseLong(id), demandIds);
		
		return result;
	}
	
	/**
	 * 成员拒绝转发文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午11:38:13
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/file/refuse")
	public Object forwardFileRefuse(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		commandForwardServiceImpl.forwardRefuse(Long.parseLong(id), demandIds);
		
		return null;
	}
	
	/**
	 * 主席停止一个指挥中的多个转发<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:01:05
	 * @param id
	 * @param forwardIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/stop/by/chairman")
	public Object forwardStopByChairman(
			String id,
			String forwardIds,
			HttpServletRequest request) throws Exception{
		
		List<Long> demandIds = JSONArray.parseArray(forwardIds, Long.class);
		
		commandForwardServiceImpl.stopByChairman(Long.parseLong(id), demandIds);
		
		return null;
	}
	
	/**
	 * 成员停止多个转发<br/>
	 * <p>支持不同指挥中的转发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午2:01:28
	 * @param businessId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/forward/stop/by/member")
	public Object forwardStopByMember(
			String businessId,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		Long demandId = Long.parseLong(businessId.split("-")[1]);
		List<Long> demandIds = new ArrayListWrapper<Long>().add(demandId).getList();
		
		JSONArray splits = commandForwardServiceImpl.stopByMember(userId, demandIds);
		
		return splits;
	}
	
	/**
	 * 开启指挥提醒<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月20日 上午11:04:02
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remind")
	public Object remind(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONObject chairSplit = commandRemindServiceImpl.remind(Long.parseLong(id));
		return chairSplit;
	}
	
	/**
	 * 关闭指挥提醒<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月20日 上午11:04:16
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remind/stop")
	public Object remindStop(
			String id,
			HttpServletRequest request) throws Exception{
		
		JSONObject chairSplit = commandRemindServiceImpl.remindStop(Long.parseLong(id));
		return chairSplit;
	}
	
	/**
	 * @Title: 设备组成员树<br/>
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 设备组树信息
	 */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/query/tree/{groupId}", method = RequestMethod.GET)
	private Object queryGroupTree(Long groupId) throws Exception{
		
//		List<UserBO> filteredUsers = new ArrayList<UserBO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		CommandGroupPO group = commandGroupDao.findOne(groupId);
		Set<CommandGroupMemberPO> members = group.getMembers();
		
//		//查询有权限的用户
//		List<UserBO> users = resourceService.queryUserresByUserId(userId);
//		if(users!=null && users.size()>0){
//			for(UserBO user:users){
//				if(user.getId().equals(userId)) continue;
//				if(("ldap".equals(user.getCreater()) && user.getDecoderId()!=null) ||
//				   (!"ldap".equals(user.getCreater()) && user.getEncoderId()!=null)){// && user.getDecoderId()!=null)){
//					filteredUsers.add(user);
//				}
//			}
//		}
		
		//查询所有非点播的文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		
		//过滤掉无用的文件夹
		List<FolderBO> usefulFolders = filterUsefulFolders(folders, null, members);
		
		//找所有的根
		List<FolderBO> roots = findRoots(usefulFolders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, usefulFolders, null, null, members);
		}
		
		return _roots;
	}
	
	/**
	 * 过滤出有用的文件夹<br/>
	 * <p>即子孙含有bundles或users的文件夹</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月3日 上午11:39:48
	 * @param allFolders 过滤前的文件夹
	 * @param bundles
	 * @param users
	 * @return 过滤后的文件夹
	 */
	private List<FolderBO> filterUsefulFolders(
			List<FolderBO> allFolders,
			List<BundleBO> bundles,
			Set<CommandGroupMemberPO> users){
		
		Set<FolderBO> usefulFolders = new HashSet<FolderBO>();
		Set<Long> ids = new HashSet<Long>();
		if(bundles!=null && bundles.size()>0){
			for(BundleBO bundle : bundles){
				ids.add(bundle.getFolderId());
			}
		}
		if(users!=null && users.size()>0){
			for(CommandGroupMemberPO user : users){
				ids.add(user.getFolderId());
			}
		}
		
		Set<Long> newIds = new HashSet<Long>();
		for(Long id : ids){
			for(FolderBO folder : allFolders){
				if(folder.getId().equals(id)){
					usefulFolders.add(folder);
					
					String parentPath = folder.getParentPath();
					if(parentPath != null){
						String[] parentPathStrings = parentPath.split("/");
						for(String path : parentPathStrings){
							if(!path.equals("")){
								newIds.add(Long.parseLong(path));
							}
						}
					}
				}
			}
		}
		
		if(newIds.size() > 0){
			for(Long id : newIds){
				for(FolderBO folder : allFolders){
					if(folder.getId().equals(id)){
						usefulFolders.add(folder);
					}
				}
			}
		}
		
		return new ArrayList<FolderBO>(usefulFolders);
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels,
			Set<CommandGroupMemberPO> users){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels, users);
			}
		}
		
		//往里装设备
		if(bundles!=null && bundles.size()>0){
			for(BundleBO bundle:bundles){
				if(bundle.getFolderId()!=null && root.getId().equals(bundle.getFolderId().toString())){
					TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
															.setChildren(new ArrayList<TreeNodeVO>());
					root.getChildren().add(bundleNode);
					if(!BundleBO.BundleRealType.XT.toString().equals(bundle.getRealType()) && channels!=null && channels.size()>0){
						for(ChannelBO channel:channels){
							if(channel.getBundleId().equals(bundleNode.getId())){
								TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
								bundleNode.getChildren().add(channelNode);
							}
						}
					}
				}
			}
		}
		
		//往里装用户
		if(users!=null && users.size()>0){
			for(CommandGroupMemberPO user:users){
				if(user.getFolderId()!=null && root.getId().equals(user.getFolderId().toString())){
					TreeNodeVO userNode = new TreeNodeVO().set(user);
					root.getChildren().add(userNode);
				}
			}
		}
		
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:32:53
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
}
