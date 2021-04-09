package com.sumavision.bvc.api.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.common.group.dao.CommonConfigDAO;
import com.sumavision.bvc.common.group.po.CommonBusinessRolePO;
import com.sumavision.bvc.common.group.po.CommonConfigPO;
import com.sumavision.bvc.common.group.po.CommonGroupPO;
import com.sumavision.bvc.config.Constant;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAgendaVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleParamVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberChannelVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.system.vo.BusinessRoleVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.BundleScreenBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupAlreadyStartedException;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.AgendaServiceImpl;
import com.sumavision.bvc.device.group.service.CommonServiceImpl;
import com.sumavision.bvc.device.group.service.DeviceGroupServiceImpl;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.bvc.device.group.service.MultiplayerChatServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * @ClassName: API的业务（会议、通话等）接口
 * @author zsy
 * @date 2018年12月8日 下午2:27:00
 */
@Controller
@RequestMapping(value = "/api/common")
public class CommonController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupConfigDAO deviceGroupConfigDao;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MeetingUtil meetingUtil;
	
	@Autowired
	private MultiplayerChatServiceImpl multiplayerChatServiceImpl;
	
	@Autowired
	private MeetingServiceImpl meetingServiceImpl;
	
	@Autowired
	private CommonServiceImpl commonServiceImpl;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private DeviceGroupServiceImpl deviceGroupServiceImpl;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	@Autowired
	private DeviceGroupRecordSchemeDAO deviceGroupRecordSchemeDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private CommonConfigDAO commonConfigDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * @Title: 大会中创建普通议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/setAgenda")
	public Object setAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		Long userId = Long.parseLong(jsonParam.getString("userId"));
		String userName = jsonParam.getString("userName");
//		String groupUuid = jsonParam.getString("groupUuid");
		String agendaName = jsonParam.getString("name");
		String remark = jsonParam.getString("remark");
		JSONObject audio = jsonParam.getJSONObject("audio");
		List<JSONObject> videos = JSONObject.parseArray(jsonParam.getJSONArray("videos").toString(), JSONObject.class);
		String forwardMode = jsonParam.getString("forwardMode");
	    
//	    DeviceGroupPO group = deviceGroupDao.findByUuid(groupUuid);
	    
//	    meetingUtil.incorrectGroupUserIdHandle(group, Long.valueOf(userId), userName);
	    
	    CommonConfigPO agenda = commonServiceImpl.setAgenda(userId, agendaName, remark, audio, videos, forwardMode);
		
		return agenda;
	}
	
	/**
	 * @Title: 删除议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/removeAgenda")
	public Object removeAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String userId = jsonParam.getString("userId");
		Long agendaId = jsonParam.getLong("agendaId");
		
		//解关联
		commonServiceImpl.removeAgenda(agendaId);
		
		return null;
	}
	
	/**
	 * @Title: 执行议程
	 * @param jsonParam 参数
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/runAgenda")
	public Object runAgenda(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String userId = jsonParam.getString("userId");
		String userName = jsonParam.getString("userName");
		Long groupId = jsonParam.getLong("groupId");
		Long agendaId = jsonParam.getLong("agendaId");
		
		DeviceGroupConfigPO agenda = agendaServiceImpl.run(groupId, agendaId);
		
		logService.logsHandle(userName, "执行议程", "议程名称："+agenda.getName());
		
		return null;
	}
	
	/**
	 * @Title: 在会议中新建角色
	 * @param groupId
	 * @param name
	 * @param special
	 * @param type
	 * @param request
	 * @throws Exception
	 * @return DeviceGroupBusinessRoleParamVO
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/new/role")
	public Object saveNewRole(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{
		
		String forwardMode = jsonParam.getString("forwardMode");
		String name = jsonParam.getString("name");
		String special = jsonParam.getString("special");
		String type = jsonParam.getString("type");
		
		CommonBusinessRolePO role = commonServiceImpl.saveNewRole(forwardMode, name, special, type);
		
		DeviceGroupBusinessRoleParamVO _role = new DeviceGroupBusinessRoleParamVO().set(role);
		
		return _role;
//		return null;
	}
	
	/**
	 * @Title: 删除会议中的角色
	 * @param groupId
	 * @param roleId
	 * @param request
	 * @throws Exception
	 * @return 结果
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/role")
	public Object removeRole(
			@RequestBody JSONObject jsonParam,
			HttpServletRequest request) throws Exception{

		Long roleId = Long.parseLong(jsonParam.getString("roleId"));
		
		commonServiceImpl.removeRole(roleId);
		
		return null;
	}
	
	/**找到根节点*/
	public List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<BundleBO> bundles, 
			List<ChannelBO> channels,
			List<BundleScreenBO> screens){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, bundles, channels, screens);
			}
		}
		
		//往里装设备
		for(BundleBO bundle:bundles){
			if(bundle.getFolderId().toString().equals(root.getId())){
				TreeNodeVO bundleNode = new TreeNodeVO().set(bundle)
														.setChildren(new ArrayList<TreeNodeVO>())
														.setScreens(new ArrayList<TreeNodeVO>());
				root.getChildren().add(bundleNode);
				if(channels!=null && channels.size()>0){
					for(ChannelBO channel:channels){
						if(channel.getBundleId().equals(bundleNode.getId())){
							TreeNodeVO channelNode = new TreeNodeVO().set(channel, bundle);
							bundleNode.getChildren().add(channelNode);
						}
					}
				}
				if(screens != null && screens.size()>0){
					for(BundleScreenBO screen: screens){
						if(screen.getBundleId().equals(bundleNode.getId())){
							TreeNodeVO screenNode = new TreeNodeVO().set(screen);
							bundleNode.getScreens().add(screenNode);
						}
					}
				}
			}
		}
	}
}
