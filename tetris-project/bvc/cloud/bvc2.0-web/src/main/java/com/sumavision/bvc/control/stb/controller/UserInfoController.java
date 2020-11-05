package com.sumavision.bvc.control.stb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.client.http.HttpRequest;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.control.device.group.controller.DeviceGroupControlController;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dao.ResourceFolderDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/api/tvos/userInfo")
public class UserInfoController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private ResourceFolderDAO resourceFolderDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private DeviceGroupControlController deviceGroupControllerController;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	/**
	 * @Title: getAvtplInfo 
	 * @Description: 根据userId获取根文件夹信息
	 * 				   根据usageType获取机顶盒参数模板信息
	 * 				   获取系统时间
	 * @param userId
	 * @return datas json数组
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping("/userAvtplInfo")
	public Object getAvtplInfo(
			@RequestBody JSONObject acceptJson,
			HttpServletRequest request) throws Exception {
		
//		Long userId = Long.parseLong(acceptJson.getString("userId"));
		Long userId = userUtils.getUserIdFromSession(request);
		
		System.out.println("************ acceptParam userId : " + userId + " ************");
		
		JSONObject data = new JSONObject();
		//添加根文件夹信息
		List<BundlePO> bundles = resourceQueryUtil.queryUseableBundles(userId);
	
		List<Long> folderIds = new ArrayList<Long>();
		for(BundlePO bundle : bundles){
			if(bundle.getFolderId()!=null){
				folderIds.add(bundle.getFolderId());
			}
		}
		
		List<FolderPO> folders = resourceQueryUtil.queryFoldersTree(folderIds);
		List<FolderBO> folderBOs = new ArrayList<FolderBO>();
		if(folders != null){
			for(FolderPO folderPO : folders){
				FolderBO folderBO = new FolderBO();
				folderBOs.add(folderBO.set(folderPO));
			}
		}
		List<FolderBO> roots = deviceGroupControllerController.findRoots(folderBOs);
		
		for(FolderBO folderBO : roots){
			
			data.put("rootFolderId", folderBO.getId());
			data.put("rootFloderName", folderBO.getName());
			//datas.add(folderJson);
		}
		
		//添加参数模板信息
		List<AvtplPO> avtpls = avtplDao.findByUsageType(AvtplUsageType.STB);
		for(AvtplPO avtpl : avtpls){
			data.put("avtplId", avtpl.getId());
			data.put("avtplName", avtpl.getName());
			//datas.add(data);
		}
		
		//添加系统时间
		String serverTime = DateUtil.format(new Date(), DateUtil.dateTimePattern);
		data.put("serverTime",serverTime);

		return data;
	}
	
	
	/**
	 * @Title: getGroupInfo 
	 * @Description: 获取会议室设备组列表    
	 * 			     ???startTime&&stopTime 的查询待完成???
	 * @param userId 
	  		  
	 * @return id 会议室设备组id, 
	 *         name 会议室设备组名称,
     *         creatorId 创建用户id, 
     *         creatorName 创建用户名称,
     *         isRun 设备组是否开始,
     *         startTime 设备组开始时间,yyyy-MM-dd hh:mm:ss,
     *         endTime 结束时间,yyyy-MM-dd hh:mm:ss
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping("/groupInfo")
	// startTime&&stopTime 的查询待完成......
	public Object getGroupInfo(
			@RequestBody JSONObject acceptJson,
			HttpServletRequest request) throws Exception{
		
		String bundleId = acceptJson.getString("bundleId");
		
		System.out.println("************ acceptParam : " + bundleId + " ************");
		
		JSONArray datas = new JSONArray();
		List<Long> groupIds = deviceGroupMemberDao.findGroupIdByBundleId(bundleId);
		List<DeviceGroupPO> deviceGroupPOs = deviceGroupDao.findAll(groupIds);
		for(DeviceGroupPO deviceGroupPO : deviceGroupPOs){
			JSONObject data = new JSONObject();
			data.put("id", deviceGroupPO.getId());
			data.put("name", deviceGroupPO.getName());
			data.put("creatorId", deviceGroupPO.getUserId());
			data.put("creatorName", deviceGroupPO.getUserName());
			data.put("isRun", deviceGroupPO.getStatus());
			//data.put("groupRegion", deviceGroupPO.getRegion());
			datas.add(data);
		}
		
		return datas;
	}
	
	
	/**
	 * @Title: getAvtpl 
	 * @Description: 查找所有参数模板
	 * @param  request
	 * @return avtplId 参数模板id, avtplName 参数模板name
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping("/allAvtplInfo")
	public Object getAvtpl(HttpRequest request) throws Exception {
		
		List<AvtplPO> avtplPOs = avtplDao.findAll();
		JSONArray datas = new JSONArray();
		
		for(AvtplPO avtplPO : avtplPOs){
			JSONObject data = new JSONObject();
			data.put("id", avtplPO.getId());
			data.put("name", avtplPO.getName());
			datas.add(data);
		}
		return datas;
	}
	
	
	/**
	 * @Title: getUseableDevice 
	 * @Description: 根据userId和folderI获取文件夹下的可通信的设备（只要手机端、机顶盒、摄像头）
	 * @param userId
	          folderId
	          bundleId
	 * @return datas json数组
	 * @throws Exception
	 */

	@ResponseBody
	@JsonBody	
	@RequestMapping(value = "/useableDevice", method = RequestMethod.POST)
	public Object getUseableDevice(
			@RequestBody JSONObject acceptJson,
			HttpServletRequest request) throws Exception {
		
//        Long userId = Long.parseLong(acceptJson.getString("userId"));
        Long userId = userUtils.getUserIdFromSession(request);
        Long folderId = Long.parseLong(acceptJson.getString("folderId"));
        String bundleId = acceptJson.getString("bundleId");

        System.out.println("************ acceptParam userId : " + userId + " ************");
        System.out.println("************ acceptParam folderId : " + folderId + " ************");
        System.out.println("************ acceptParam bundleId : " + bundleId + " ************");
        
		List<BundlePO> bundles = resourceQueryUtil.queryUseableBundles(userId);
		List<FolderPO> folders = resourceFolderDao.findChildFolderByFolderId(folderId);
		
		JSONObject data = new JSONObject();
		JSONArray folderData = new JSONArray();

		//获取floder信息
		for(FolderPO folder : folders){
			JSONObject data1 = new JSONObject();
			data1.put("id", folder.getId());
			data1.put("name", folder.getName());
			folderData.add(data1);
		}
		data.put("folders", folderData);
		
		//获取bundleType为tvos/mobile/ipc的bundle信息
		JSONArray bundleData = new JSONArray();
		for(BundlePO bundle : bundles){
			if(bundle.getFolderId()==(folderId) && !bundle.getBundleId().equals(bundleId)){
				if(bundle.getDeviceModel().equals("tvos")
				   || bundle.getDeviceModel().equals("mobile")){
					JSONObject data2 = new JSONObject();
					data2.put("id", bundle.getBundleId());
					data2.put("name", bundle.getBundleName());
					data2.put("type", bundle.getDeviceModel());
					data2.put("status", bundle.getOnlineStatus());
					bundleData.add(data2);
				}
			}
		}
		data.put("device", bundleData);
		
		return data;
	}
	
	
	/**
	 * 
	 * @Title: getMeetingInfo 
	 * @Description: 获取机顶盒状态信息
	 * @param:  request
	 * @return: caller_name 会议名称
	 * 			type 会议类型（1-会议，2-点对点通话）
	 * 			mode video（多人通话类型:video视屏,audio音频）
	 * 			groupUuid 会议/点对点通话唯一标识
	 * 			callee_bundle_id "xxxxxxx"
	 * 			callee_layer_id "b-i-d"
	 * 			uuid 通知的唯一标识，用于反馈 
	 * 			deniable 呼叫是否可拒绝
	 * @throws: Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/meetingInfo", method = RequestMethod.POST)
	public Object getMeetingInfo(
			@RequestBody JSONObject acceptJson,
			HttpServletRequest request) throws Exception {
		
		String bundleId = acceptJson.getString("bundleId");
		
		List<Long> groupIds = deviceGroupMemberDao.findGroupIdByBundleId(bundleId);
		List<DeviceGroupPO> deviceGroupPOs = deviceGroupDao.findAll(groupIds);
		JSONObject data = new JSONObject();
		for(DeviceGroupPO deviceGroupPO : deviceGroupPOs){
			Set<DeviceGroupMemberPO> members = deviceGroupPO.getMembers(); 
			DeviceGroupMemberPO member = queryUtil.queryMemberPOByBundleId(members, bundleId);
			if(deviceGroupPO.getStatus().equals(GroupStatus.START) && member.getMemberStatus().equals(MemberStatus.CONNECT)){
				
				data.put("groupName", deviceGroupPO.getName());
				data.put("type", deviceGroupPO.getType().getProtocalId());
				data.put("mode", deviceGroupPO.getType());
				data.put("groupUuid", deviceGroupPO.getUuid());
				data.put("administratorId", deviceGroupPO.getUserId());
				data.put("administratorName", deviceGroupPO.getUserName());
				data.put("uuid", "");

				break;
			}
		}
		return data;
		
	}
	
}
