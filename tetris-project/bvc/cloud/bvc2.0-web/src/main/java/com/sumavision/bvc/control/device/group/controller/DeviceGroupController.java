package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupMemberVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupVO;
import com.sumavision.bvc.control.device.group.vo.GroupMemberChannelVO;
import com.sumavision.bvc.control.device.group.vo.RecordVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.system.vo.AuthorizationVO;
import com.sumavision.bvc.control.system.vo.AvtplVO;
import com.sumavision.bvc.control.system.vo.DictionaryVO;
import com.sumavision.bvc.control.system.vo.TplVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.BundleScreenBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.ScreenRectBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.dto.DeviceGroupDTO;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.RecordToVodType;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.group.exception.DeviceGroupAlreadyStartedException;
import com.sumavision.bvc.device.group.exception.TplIsNullException;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.AuthorizationServiceImpl;
import com.sumavision.bvc.device.group.service.AutoBuildAgendaServiceImpl;
import com.sumavision.bvc.device.group.service.ConfigServiceImpl;
import com.sumavision.bvc.device.group.service.DeviceGroupServiceImpl;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.bvc.device.group.service.RecordServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AuthorizationDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.dao.TplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.enumeration.ServLevel;
import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * @ClassName: 设备组前端接口
 * @author lvdeyang
 * @date 2018年8月1日 下午1:05:25 
 */
@Controller
@RequestMapping(value = "/device/group")
public class DeviceGroupController {

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDAO;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	@Autowired
	private AvtplDAO sysAvtplDao;
	
	@Autowired
	private TplDAO sysTplDao;
	
	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private DeviceGroupServiceImpl deviceGroupServiceImpl;
	
	@Autowired
	private RecordServiceImpl recordServiceImpl;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private RecordDAO recordDao;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MeetingServiceImpl meetingServiceImpl;
	
	@Autowired
	private AuthorizationDAO authorizationDao;
	
	@Autowired
	private AuthorizationServiceImpl authorizationServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ConfigServiceImpl configServiceImpl;
	
	@Autowired
	private AutoBuildAgendaServiceImpl autoBuildAgendaServiceImpl;
	
	@Autowired
	private ChannelNameDAO channelNameDao;
	
	/**
	 * 查询代码类型 
	 * @throws Exception
	 * @return type:Set<String> 组类型
	 * @return transmissionMode:Set<String> 发流类型
	 * @return avtpl:List<AvtplVO> 参数模板
	 * @return tpl:List<TplVO> 会议模板
	 * @return region:Set<String>会议地区类型
	 * @return classification:Set<String>会议分类类型
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code", method = RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		//组类型
		Set<String> _types = new HashSet<String>();
		GroupType[] types = GroupType.values();
		for(GroupType type:types){
			if(type.getShow()) _types.add(type.getName());
		}
		
		//传输类型
		Set<String> _transmissionMode = new HashSet<String>();
		TransmissionMode[] transmissionModes = TransmissionMode.values();
		for(TransmissionMode mode:transmissionModes){
			_transmissionMode.add(mode.getName());
		}
		
		//转发模式类型
		Set<String> _forwardMode = new HashSet<String>();
		ForwardMode[] forwardModes = ForwardMode.values();
		for(ForwardMode forwardMode: forwardModes){
			_forwardMode.add(forwardMode.getName());
		}
		
		//参数方案
		List<AvtplPO> avtpls = sysAvtplDao.findAll();
		List<AvtplVO> _avtpls = AvtplVO.getConverter(AvtplVO.class).convert(avtpls, AvtplVO.class);
		
		//会议模板
		List<TplPO> tpls = sysTplDao.findAll();
		List<TplVO> _tpls = TplVO.getConverter(TplVO.class).convert(tpls, TplVO.class);
		
		//会议模板
		List<AuthorizationPO> authtpls = authorizationDao.findAll();
		List<AuthorizationVO> _authtpls = AuthorizationVO.getConverter(AuthorizationVO.class).convert(authtpls, AuthorizationVO.class);
		
		//会议区域（改名为：组织）
		List<DictionaryPO> regions = dictionaryDao.findByDicType(DicType.REGION);
		List<DictionaryVO> _regions = DictionaryVO.getConverter(DictionaryVO.class).convert(regions, DictionaryVO.class);
		
		//存储位置
		List<DictionaryPO> storageLocations = dictionaryDao.findByDicType(DicType.STORAGE_LOCATION);
		List<DictionaryVO> _storageLocations = DictionaryVO.getConverter(DictionaryVO.class).convert(storageLocations, DictionaryVO.class);
		_storageLocations.add(new DictionaryVO().setContent("默认")
				.setCode("<default>"));

		//点播二级栏目（改名为：会议类型）
		List<DictionaryPO> programs = dictionaryDao.findByDicTypeAndServLevel(DicType.DEMAND, ServLevel.LEVEL_TWO);
		List<DictionaryVO> _programs = DictionaryVO.getConverter(DictionaryVO.class).convert(programs, DictionaryVO.class);
        _programs.add(new DictionaryVO().setContent("默认")
        								.setBoId("<default>")
        								.setParentRegionId(""));
      
        
        //直播栏目
  		List<DictionaryPO> categoryLives = dictionaryDao.findByDicType(DicType.LIVE);
  		List<DictionaryVO> _categoryLives = DictionaryVO.getConverter(DictionaryVO.class).convert(categoryLives, DictionaryVO.class);
  		_categoryLives.add(new DictionaryVO().setContent("默认")
          								.setLiveBoId("<default>"));

  		//系统版本号
		String version = configServiceImpl.getVersion();
		
		return new HashMapWrapper<String, Object>().put("type", _types)
				.put("transmissionMode", _transmissionMode)
				.put("forwardMode", _forwardMode)
				.put("avtpl", _avtpls)
				.put("tpl", _tpls)
				.put("authtpl", _authtpls)
				.put("region", _regions)
				.put("program", _programs)
				.put("categoryLives", _categoryLives)
				.put("storageLocations", _storageLocations)
				.put("version", version)
				.getMap();
	}
	
	/**
	 * 查询会议设备
	 * @param @param groupId
	 * @param @param request
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<TreeNodeVO>
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/bundle/{groupId}", method = RequestMethod.GET)
	public Object queryBundle(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		//返回会议基本信息
		List<TreeNodeVO> bundleTrees = new ArrayList<TreeNodeVO>();
		List<DeviceGroupMemberDTO> members = deviceGroupMemberDAO.findGroupMembersByGroupId(groupId);
		
		for(DeviceGroupMemberDTO member: members){
			TreeNodeVO node = new TreeNodeVO().set(member); 
			bundleTrees.add(node);
		}
		
		int volume = deviceGroupDao.findGroupVolumeByGroupId(groupId);
	
		return new HashMapWrapper<String, Object>().put("bundles", bundleTrees)
												   .put("volume", volume)
												   .getMap();
	}
	
	/**
	 * 设备组数据分页查询 
	 * @param pageSize 每页数据量
	 * @param currentPage 当前页
	 * @throws Exception
	 * @return rows:List<DeviceGroupVO> 设备组数据
	 * @return total:总数据量 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load")
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		Page<DeviceGroupDTO> pagedGroups;
		if(user.isAdmin()){
			pagedGroups = deviceGroupDao.findAllOutputDTO(page);
		}else{
			Long userId = user.getId();
			pagedGroups = deviceGroupDao.findAllByUserIdOutputDTO(userId, page);
		}
		long total = pagedGroups.getTotalElements();
		List<DeviceGroupVO> _groups = DeviceGroupVO.getConverter(DeviceGroupVO.class).convert(pagedGroups.getContent(), DeviceGroupVO.class);
		List<DictionaryPO> regions = dictionaryDao.findByDicType(DicType.REGION);
		if(_groups.size() > 0){
			//多地区显示
			for(DeviceGroupVO group : _groups){
				if(group.getRegions() == null){
					group.setRegions(new ArrayList<DictionaryVO>());
				}				
				String[] groupRegionIds = new String[]{};
				if(group.getDicRegionId()!=null && !group.getDicRegionId().equals("")){
					String dicRegionId = group.getDicRegionId();
					if(dicRegionId.contains(",,") || dicRegionId.startsWith(",") || dicRegionId.endsWith(",")){
						group.getRegions().add(new DictionaryVO().setContent("默认")
								  .setBoId(""));
					}
					groupRegionIds = dicRegionId.split(",");
				}else{
					group.getRegions().add(new DictionaryVO().setContent("默认")
							  .setBoId(""));
				}
				for(String groupRegionId : groupRegionIds){
					for(DictionaryPO region : regions){
						if(groupRegionId.equals(region.getBoId())){
							group.getRegions().add(new DictionaryVO().set(region));
							break;
						}
					}
				}
			}
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", _groups);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * 保存设备组 
	 * @param name 组名称
	 * @param type 组类型
	 * @param transmissionMode 发流模式
	 * @param avtplId 参数方案id
	 * @param systemTplId 会议模板id
	 * @param dicRegionId 会议地区
	 * @param classification 会议分类
	 * @throws Exception
	 * @return DeviceGroupVO 设备组数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String type,
			String transmissionMode,
			String forwardMode,
			Long avtplId,
			Long systemTplId,
			Long authtplId,
			String regions,
			String dicCategoryLiveId,
			String dicProgramId,
			String dicStorageLocationCode,
			String sourceList,
			HttpServletRequest request) throws Exception{		
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<DictionaryVO> _regions = JSON.parseArray(regions, DictionaryVO.class);
		String regionId = "", regionContent = "";
		if(_regions.size()>0){
			StringBuilder regionIdBuilder = new StringBuilder();
			StringBuilder regionContentBuilder = new StringBuilder();
			if(_regions.size() == 1){
				regionId = regionIdBuilder.append(_regions.get(0).getBoId()).toString();
				regionContent = regionContentBuilder.append(_regions.get(0).getContent()).toString();
			}else {
				for(int i=0; i < _regions.size(); i++){
					if(i == _regions.size() - 1){
						regionIdBuilder.append(_regions.get(i).getBoId());
						regionContentBuilder.append(_regions.get(i).getContent());
					}else {
						regionIdBuilder.append(_regions.get(i).getBoId() + ",");
						regionContentBuilder.append(_regions.get(i).getContent() + ",");
					}
				}
				regionId = regionIdBuilder.toString();
				regionContent = regionContentBuilder.toString();
			}
		}
		
		DeviceGroupPO group = deviceGroupServiceImpl.save(
				user.getId(), 
				user.getName(), 
				name,
				type, 
				transmissionMode, 
				forwardMode,
				avtplId, 
				systemTplId, 
				authtplId,
				regionId,
				regionContent,
				dicCategoryLiveId,
				dicProgramId,
				dicStorageLocationCode,
				sourceList);
		
		
		DeviceGroupVO _group = new DeviceGroupVO().set(group);
		if(regionId != null){
			_group.setRegions(new ArrayList<DictionaryVO>());
			List<DictionaryPO> baseRegions = dictionaryDao.findByDicType(DicType.REGION);
			for(DictionaryPO region : baseRegions){
				if(regionId.contains(region.getBoId())){
					_group.getRegions().add(new DictionaryVO().set(region));
				}
			}
		}
		
		logService.logsHandle(user.getName(), "新建设备组", "设备组名称："+group.getName());
		
		return _group;
	}
	
	/**
	 * 修改设备组 
	 * @param groupId
	 * @param name
	 * @param type
	 * @param transmissionMode
	 * @param avtplId
	 * @param systemTplId
	 * @param region 会议地区
	 * @param classification 会议分类
	 * @throws Exception 
	 * @return DeviceGroupVO 设备组数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping("/update/{groupId}")
	public Object Update(
			@PathVariable Long groupId,
			String name,
			String type,
			String transmissionMode,
			String forwardMode,
			Long avtplId,
			Long systemTplId,
			String sourceList,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.update(
				groupId, 
				name, 
				type, 
				transmissionMode, 
				forwardMode,
				avtplId, 
				systemTplId, 
				sourceList);
		
		DeviceGroupVO _group = new DeviceGroupVO().set(group);
		
		logService.logsHandle(user.getName(), "更新设备组", "设备组名称："+group.getName());
		
		return _group;
	}	
	
	/**
	 * 根据id删除数据 
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
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupDao.findOne(id);
		if(GroupStatus.START.equals(group.getStatus())){
			throw new DeviceGroupAlreadyStartedException(group.getId(), group.getName());
		}
		//删除无用的看会权限
		authorizationServiceImpl.removeUnusefulAuthorizationByGroupUuid(group.getUuid());
		
		Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
		LogicBO logic = new LogicBO();
		if(logic.getPass_by() == null || logic.getPass_by().size() <= 0) logic.setPass_by(new ArrayList<PassByBO>());
		
		if(group.getForwardMode().equals(ForwardMode.ROLE)){
			for(DeviceGroupBusinessRolePO role: roles){
				if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){

					if(role.getBundleId() != null){
						//解绑
						resourceService.unBindVirtualDev(role.getBundleId());
					}
				}
			}
		}
		
		deviceGroupDao.delete(group);
		
		logService.logsHandle(user.getName(), "删除设备组", "设备组名称："+group.getName());
				
		return null;
	}
	
	/**
	 * 根据id批量删除 
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		List<DeviceGroupPO> groups = deviceGroupDao.findAll(ids);
		for(DeviceGroupPO group:groups){
			if(GroupStatus.START.equals(group)){
				throw new DeviceGroupAlreadyStartedException(group.getId(), group.getName());
			}
			
			//角色解绑虚拟设备
			if(group.getForwardMode().equals(ForwardMode.ROLE)){
				Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
				for(DeviceGroupBusinessRolePO role: roles){
					if(role.getBundleId() != null){
						resourceService.unBindVirtualDev(role.getBundleId());
					}
				}
			}
		}
		//deviceGroupDao.deleteInBatch(groups);
		deviceGroupDao.deleteByIdIn(ids);
		
		logService.logsHandle(user.getName(), "批量删除设备组", "设备组ids："+ids);
		
		return null;
	}
	
	/**
	 * 设备组启动<br/> 
	 * @Description: 打开并占用所有的设备组成员通道<br/> 
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start/{groupId}")
	public Object start(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = meetingServiceImpl.start(groupId, user.getId(), user.getName());
		
		logService.logsHandle(user.getName(), "设备组启动", "设备组名称："+group.getName());
		
		return new DeviceGroupVO().set(group);
	}
	
	/**
	 * 设备组停止<br/> 
	 * @Description: 关闭所有的设备组成员通道<br/> 
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/{groupId}")
	public Object stop(
			@PathVariable Long groupId,
			String transferToVod,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);

		if(transferToVod == null){
			transferToVod = RecordToVodType.NOTTOVOD.getProtocalId();
		}
		
		DeviceGroupPO group = deviceGroupServiceImpl.stop(groupId, transferToVod);
		
		logService.logsHandle(user.getName(), "设备组停止", "设备组名称："+group.getName());
		
		Long delayTime = (long) (2000 + 20 * group.getMembers().size());
		Thread.sleep(delayTime);
		
		return new DeviceGroupVO().set(group);
	}
	
	/**
	 * 执行设备组录制方案<br/> 
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return DeviceGroupVO 设备组数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/run/record/scheme/{groupId}")
	public Object runRecordScheme(
			@PathVariable Long groupId,
			String name,
			String describe,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		recordServiceImpl.runRecordScheme(group, name, describe);
		
		logService.logsHandle(user.getName(), "执行设备组录制方案", "设备组名称："+group.getName());
		
		return new DeviceGroupVO().set(group);
	}
	
	/**
	 * 停止设备组录制方案<br/> 
	 * @param groupId 设备组id
	 * @throws Exception 
	 * @return DeviceGroupVO 设备组数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/record/scheme/{groupId}")
	public Object stopRecordScheme(
			@PathVariable Long groupId,
			String transferToVod,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		if(transferToVod == null){
			transferToVod = RecordToVodType.NOTTOVOD.getProtocalId();
		}
		
		recordServiceImpl.stopRecordScheme(group, transferToVod);
		
		logService.logsHandle(user.getName(), "停止设备组录制方案", "设备组名称："+group.getName());
		
		return new DeviceGroupVO().set(group);
	}
	
	/**
	 * 查询所有的设备列表
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all/source/tree", method = RequestMethod.GET)
	public Object queryAllSourceTree(HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();		
		List<BundleScreenBO> screens = new ArrayList<BundleScreenBO>();
		
		//从资源层查bundles（需要userId）
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		List<String> bundleIds = new ArrayList<String>();
		Set<Long> folderIds = new HashSet<Long>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		if(queryBundles != null && queryBundles.size() > 0){
			for(BundlePO bundleBody:queryBundles){
				if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
					BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
							.setName(bundleBody.getBundleName())
							.setFolderId(bundleBody.getFolderId())
							.setBundleId(bundleBody.getBundleId())
							.setModel(bundleBody.getDeviceModel())
							.setNodeUid(bundleBody.getAccessNodeUid())
							.setOnlineStatus(bundleBody.getOnlineStatus().toString())
							.setType(bundleBody.getBundleType());

					bundles.add(bundle);
					
					bundleIds.add(bundleBody.getBundleId());

					folderIds.add(bundleBody.getFolderId());
				}		
			}
			
			//根据bundleIds从资源层查询channels
			List<ChannelSchemeDTO> queryChannels = resourceQueryUtil.queryAllChannelsByBundleIds(bundleIds);
			if(queryChannels != null){
				for(ChannelSchemeDTO channel:queryChannels){
					ChannelBO channelBO = new ChannelBO().setChannelId(channel.getChannelId())
													     .setName(channel.getChannelName())
														 .setBundleId(channel.getBundleId())
														 .setChannelName(channel.getChannelName())
														 .setChannelType(channel.getBaseType());
		
					channels.add(channelBO);
				}
			}
			
			//根据bundleIds从资源层查询screens
			List<ScreenSchemePO> queryScreens = resourceQueryUtil.queryScreensByBundleIds(bundleIds);
			Set<String> screenIds = new HashSet<String>();
			if(queryScreens != null){
				for(ScreenSchemePO screen: queryScreens){
					screenIds.add(screen.getScreenId());
				}
			}
			List<ScreenRectTemplatePO> queryRects = resourceQueryUtil.queryRectsByScreenIds(screenIds);
			if(queryScreens!=null && queryRects!=null){
				for(ScreenSchemePO screen: queryScreens){
					BundleScreenBO screenBO = new BundleScreenBO().set(screen);
					for(ScreenRectTemplatePO rect: queryRects){
						if(rect.getDeviceModel().equals(screen.getDeviceModel()) && rect.getScreenId().equals(screen.getScreenId())){
							ScreenRectBO rectBO = new ScreenRectBO().set(rect);
							screenBO.getRects().add(rectBO);
						}
					}
					screens.add(screenBO);
				}
			}
			
			//根据folderIds查询父级信息
			List<FolderPO> foldersTree = resourceQueryUtil.queryFoldersTree(folderIds);

			if(foldersTree != null && foldersTree.size()>0){
				for(FolderPO allFolderPO: foldersTree){
					FolderBO folderBO = new FolderBO().set(allFolderPO);					  						  
					folders.add(folderBO);			
				}
			}
			
			//找所有的根
			List<FolderBO> roots = findRoots(folders);
			for(FolderBO root:roots){
				TreeNodeVO _root = new TreeNodeVO().set(root)
												   .setChildren(new ArrayList<TreeNodeVO>());
				_roots.add(_root);
				recursionFolder(_root, folders, bundles, channels, screens);
			}
			
			//获取当前用的所有拼接屏
			List<CombineJv230PO> combineJv230s = combineJv230Dao.findByUserId(userId);
			if(combineJv230s!=null && combineJv230s.size()>0){
				TreeNodeVO folderNode = new TreeNodeVO().set(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"))
														.setChildren(new ArrayList<TreeNodeVO>());
				_roots.add(folderNode);
				for(CombineJv230PO combineJv230:combineJv230s){
					BundleBO bundle = new BundleBO().setId(combineJv230.getId().toString())
													.setName(combineJv230.getName())
													.setModel(combineJv230.getModel())
													.setType(combineJv230.getType())
													.setBundleId(combineJv230.getId().toString())
													.setFolderId(TreeNodeVO.FOLDERID_COMBINEJV230);
					TreeNodeVO combineJv230Node = new TreeNodeVO().set(bundle);
					folderNode.getChildren().add(combineJv230Node);
				}
			}
		}
		
		return _roots;
	}
	
	/**
	 * @Title: 查询所有的设备列表，不带channel
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all/bundle/tree", method = RequestMethod.GET)
	public Object queryAllBundleTree(HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();	
		
		//从资源层查bundles（需要userId）
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		List<String> bundleIds = new ArrayList<String>();
		Set<Long> folderIds = new HashSet<Long>();
		for(BundlePO bundleBody:queryBundles){
			if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
				BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
						.setName(bundleBody.getBundleName())
						.setFolderId(bundleBody.getFolderId())
						.setBundleId(bundleBody.getBundleId())
						.setModel(bundleBody.getDeviceModel())
						.setNodeUid(bundleBody.getAccessNodeUid());

				bundles.add(bundle);
				
				bundleIds.add(bundleBody.getBundleId());

				folderIds.add(bundleBody.getFolderId());
			}		
		}
		
		//根据folderIds查询父级信息
		List<FolderPO> foldersTree = resourceQueryUtil.queryFoldersTree(folderIds);

		if(foldersTree != null && foldersTree.size()>0){
			for(FolderPO allFolderPO: foldersTree){
				FolderBO folderBO = new FolderBO().set(allFolderPO);					  						  
				folders.add(folderBO);			
			}
		}
		
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, null, null);
		}
		
		//获取当前用的所有拼接屏
		List<CombineJv230PO> combineJv230s = combineJv230Dao.findByUserId(userId);
		if(combineJv230s!=null && combineJv230s.size()>0){
			TreeNodeVO folderNode = new TreeNodeVO().set(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"))
													.setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(folderNode);
			for(CombineJv230PO combineJv230:combineJv230s){
				BundleBO bundle = new BundleBO().setId(combineJv230.getId().toString())
												.setName(combineJv230.getName())
												.setModel(combineJv230.getModel())
												.setBundleId(combineJv230.getId().toString())
												.setFolderId(TreeNodeVO.FOLDERID_COMBINEJV230);
				TreeNodeVO combineJv230Node = new TreeNodeVO().set(bundle);
				folderNode.getChildren().add(combineJv230Node);
			}
		}
		
		return _roots;
	}
	
	/**
	 * @Title: 根据设备组id查询角色 
	 * @param @param groupId 设备组id
	 * @return Object 返回类型 
	 * @throws
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/role/{groupId}", method = RequestMethod.GET)
	public Object queryRole(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<DeviceGroupBusinessRolePO> groupRolePOs = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		List<DeviceGroupMemberDTO> groupMemberDTOs = deviceGroupMemberDAO.findGroupMembersByGroupId(groupId);
		
		List<DeviceGroupBusinessRoleVO> groupRoleVOs = DeviceGroupBusinessRoleVO.getConverter(DeviceGroupBusinessRoleVO.class).convert(groupRolePOs, DeviceGroupBusinessRoleVO.class);
		List<DeviceGroupMemberVO> groupMemberVOs = DeviceGroupMemberVO.getConverter(DeviceGroupMemberVO.class).convert(groupMemberDTOs, DeviceGroupMemberVO.class);
		
		for(DeviceGroupBusinessRoleVO groupRoleVO: groupRoleVOs){
			for(DeviceGroupMemberVO groupMemberVO: groupMemberVOs){
				if(groupMemberVO.getRoleId() != null ){
					if(groupMemberVO.getRoleId().equals(groupRoleVO.getId())){
						groupRoleVO.getMembers().add(groupMemberVO);
					}
				}				
			}
		}
		
		return new HashMapWrapper<String, Object>().put("members", groupMemberVOs)
												   .put("roles", groupRoleVOs)
												   .getMap();
	}
	
	/**
	 * 设置发言人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 上午11:50:32
	 * @param groupId 设备组id
	 * @param memberId 成员id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/spokesman/set/{groupId}")
	public Object spokesmanSet(
			@PathVariable Long groupId,
			Long memberId,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		deviceGroupServiceImpl.spokesmanSet(groupId, memberId, roleId);
		
		return null;
	}
	
	/**
	 * 设置角色<br/>
	 * @param groupId 设备组id
	 * @param memberIds 成员ids
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roles/set/{groupId}")
	public Object rolesSet(
			@PathVariable Long groupId,
			String memberIds,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		List<Long> memberArray = JSONArray.parseArray(memberIds, Long.class);
		
		deviceGroupServiceImpl.rolesSet(groupId, memberArray, roleId);
		
		return null;
	}
	
	/**
	 * 移除角色成员<br/>
	 * @param groupId 设备组id
	 * @param memberId 成员id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roles/member/remove/{groupId}")
	public Object rolesRemove(
			@PathVariable Long groupId,
			Long memberId,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		List<Long> memberIds = new ArrayList<Long>();
		memberIds.add(memberId);
		deviceGroupServiceImpl.rolesRemove(groupId, memberIds, roleId);
		
		return null;
	}
	
	/**
	 * 移除角色所有成员<br/>
	 * @param groupId 设备组id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/roles/remove/{groupId}")
	public Object roleMembersRemove(
			@PathVariable Long groupId,
			Long roleId,
			HttpServletRequest request) throws Exception{

		deviceGroupServiceImpl.roleMembersRemove(groupId, roleId);
		
		return null;
	}
	
	/**
	 * 移发言人所有成员<br/>
	 * @param groupId 设备组id
	 * @param roleId 角色id
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/spokesman/remove/{groupId}")
	public Object spokesmanMembersRemove(
			@PathVariable Long groupId,
			Long roleId,
			HttpServletRequest request) throws Exception{

		deviceGroupServiceImpl.spokesmanMembersRemove(groupId, roleId);
		
		return null;
	}
	
	/**
	 * 获取非发言人[|主席]的成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月17日 上午9:25:34
	 * @param groupId 设备组id
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/except/spokesman/{groupId}")
	public Object queryMembersExceptSpokesman(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		//查询会议成员
		List<DeviceGroupMemberDTO> members = deviceGroupMemberDAO.findGroupMembersByGroupId(groupId);
		
		//获取发言人以及主席
		List<DeviceGroupBusinessRolePO> chairmans =  deviceGroupBusinessRoleDao.findByGroupIdAndSpecial(groupId, BusinessRoleSpecial.CHAIRMAN);
		List<DeviceGroupBusinessRolePO> spokesmans = deviceGroupBusinessRoleDao.findByGroupIdAndSpecial(groupId, BusinessRoleSpecial.SPOKESMAN);
		List<DeviceGroupBusinessRolePO> roles = new ArrayList<DeviceGroupBusinessRolePO>();
		if(chairmans!=null && chairmans.size()>0){
			roles.addAll(chairmans);
		}
		if(spokesmans!=null && spokesmans.size()>0){
			roles.addAll(spokesmans);
		}
		
		//过滤发言人成员
		List<BundleBO> filteredMembers = new ArrayList<BundleBO>();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberDTO member:members){
				boolean isSpokeksman = false;
				if(roles!=null && roles.size()>0){
					for(DeviceGroupBusinessRolePO role:roles){
						if(role.getId().equals(member.getRoleId())){
							isSpokeksman = true;
							break;
						}
					}
				}
				if(!isSpokeksman) {
					BundleBO filteredMember = new BundleBO().set(member);
					filteredMembers.add(filteredMember);
				}
			}
		}
		
		//在线离线状态
		filteredMembers = resourceQueryUtil.appendBundleOnlineStatusIntoBundleBos(filteredMembers);
		
		//查询文件夹
		Set<Long> folderIds = new HashSet<Long>();
		if(filteredMembers!=null && filteredMembers.size()>0){
			for(BundleBO member:filteredMembers){
				if(member.getFolderId()!=null){
					folderIds.add(member.getFolderId());
				}
			}
		}
		List<FolderPO> folderEntities = resourceQueryUtil.queryFoldersTree(folderIds);
		List<FolderBO> folders = new ArrayList<FolderBO>();
		if(folderEntities!=null && folderEntities.size()>0){
			for(FolderPO folderEntity:folderEntities){
				FolderBO folder = new FolderBO().set(folderEntity);
				folders.add(folder);
			}
		}
		
		//组树
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, filteredMembers, null, null);
		}
		
		return _roots;
	}
	
	/**
	 * 获取非绑定角色的成员<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月19日 上午9:25:34
	 * @param groupId 设备组id
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/except/roles/{groupId}")
	public Object queryMembersExceptRoles(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		//查询会议成员
		List<DeviceGroupMemberDTO> members = deviceGroupMemberDAO.findGroupMembersByGroupId(groupId);
		List<DeviceGroupBusinessRolePO> roles = deviceGroupBusinessRoleDao.findByGroupId(groupId);
		
		//过滤已绑定角色成员
		List<BundleBO> filteredMembers = new ArrayList<BundleBO>();
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberDTO member:members){
				boolean isRole = false;
				if(roles!=null && roles.size()>0){
					for(DeviceGroupBusinessRolePO role:roles){
						if(role.getId().equals(member.getRoleId())){
							isRole = true;
							break;
						}
					}
				}
				if(!isRole) {
					BundleBO filteredMember = new BundleBO().set(member);
					filteredMembers.add(filteredMember);
				}
			}
		}
		
		//在线离线状态
		filteredMembers = resourceQueryUtil.appendBundleOnlineStatusIntoBundleBos(filteredMembers);
		
		//查询文件夹
		Set<Long> folderIds = new HashSet<Long>();
		if(filteredMembers!=null && filteredMembers.size()>0){
			for(BundleBO member:filteredMembers){
				if(member.getFolderId()!=null){
					folderIds.add(member.getFolderId());
				}
			}
		}
		List<FolderPO> folderEntities = resourceQueryUtil.queryFoldersTree(folderIds);
		List<FolderBO> folders = new ArrayList<FolderBO>();
		if(folderEntities!=null && folderEntities.size()>0){
			for(FolderPO folderEntity:folderEntities){
				FolderBO folder = new FolderBO().set(folderEntity);
				folders.add(folder);
			}
		}
		//加入大屏文件夹
		for(BundleBO bundle:filteredMembers){
			if("combineJv230".equals(bundle.getModel())){
				folders.add(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"));
				break;
			}
		}

		//组树
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();		
		List<FolderBO> roots = findRoots(folders);	
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, filteredMembers, null, null);
		}
		
		return _roots;
	}
	
	/**
	 * 获取非会议的成员<br/>
	 * @param groupId 设备组id
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/members/except/group/{groupId}")
	public Object queryMembersExceptGroup(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		//获取userId
		long userId = userUtils.getUserIdFromSession(request);
		
		List<DeviceGroupMemberDTO> members = deviceGroupMemberDAO.findGroupMembersByGroupId(groupId);
		List<String> exsitBundleIds = new ArrayList<String>();
		for(DeviceGroupMemberDTO member: members){
			exsitBundleIds.add(member.getBundleId());
		}
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();		
		
		//从资源层查bundles（需要userId）
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(userId);
		List<String> bundleIds = new ArrayList<String>();
		Set<Long> folderIds = new HashSet<Long>();
		for(BundlePO bundleBody:queryBundles){
			if(!"jv230".equals(bundleBody.getDeviceModel()) && bundleBody.getFolderId() != null){
				if(!exsitBundleIds.contains(bundleBody.getBundleId())){
					BundleBO bundle = new BundleBO().setId(bundleBody.getBundleId())										
							.setName(bundleBody.getBundleName())
							.setFolderId(bundleBody.getFolderId())
							.setBundleId(bundleBody.getBundleId())
							.setModel(bundleBody.getDeviceModel())
							.setNodeUid(bundleBody.getAccessNodeUid())
							.setOnlineStatus(bundleBody.getOnlineStatus().toString());

					bundles.add(bundle);
					
					bundleIds.add(bundleBody.getBundleId());

					folderIds.add(bundleBody.getFolderId());
				}			
			}		
		}
		
		//根据新folderIds查询所有层级（文件夹）
		List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

		if(allFolders != null && allFolders.size()>0){
			for(FolderPO allFolderPO: allFolders){
				if(allFolderPO == null) continue;
				FolderBO folderBO = new FolderBO().set(allFolderPO);
				folders.add(folderBO);			
			}
		}
		
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, channels, null);
		}
		
		//获取当前用的所有拼接屏
		List<CombineJv230PO> combineJv230s = combineJv230Dao.findByUserId(userId);
		if(combineJv230s!=null && combineJv230s.size()>0){
			TreeNodeVO folderNode = new TreeNodeVO().set(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"))
													.setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(folderNode);
			for(CombineJv230PO combineJv230:combineJv230s){
				if(!exsitBundleIds.contains(combineJv230.getId().toString())){
					BundleBO bundle = new BundleBO().setId(combineJv230.getId().toString())
							.setName(combineJv230.getName())
							.setModel(combineJv230.getModel())
							.setBundleId(combineJv230.getId().toString())
							.setFolderId(TreeNodeVO.FOLDERID_COMBINEJV230);
					TreeNodeVO combineJv230Node = new TreeNodeVO().set(bundle);
					folderNode.getChildren().add(combineJv230Node);
				}			
			}
		}
		
		return _roots;
	}
	
	/**
	 * 添加成员<br/>
	 * @param groupId 设备组id
	 * @param bundleIds 添加的设备bundleId数组
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/add/members/{groupId}")
	public Object addMembers(
			@PathVariable Long groupId,
			String bundleIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<String> bundleIdArray = JSONArray.parseArray(bundleIds, String.class);
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		//添加成员
		meetingServiceImpl.addMembers(group, bundleIdArray);
		
		Set<DeviceGroupMemberPO> allMembers = group.getMembers();
		
		List<DeviceGroupMemberPO> members = new ArrayList<DeviceGroupMemberPO>();
		List<DeviceGroupMemberChannelPO> addChannels = new ArrayList<DeviceGroupMemberChannelPO>();
		for(String bundleId: bundleIdArray){
			DeviceGroupMemberPO memberPO = queryUtil.queryMemberPOByBundleId(allMembers, bundleId);
			if(!memberPO.getBundleType().equals("combineJv230")){
				members.add(memberPO);
				addChannels.addAll(memberPO.getChannels());
			}
		}
		List<GroupMemberChannelVO> _channels = GroupMemberChannelVO.getConverter(GroupMemberChannelVO.class).convert(addChannels, GroupMemberChannelVO.class);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		List<ChannelBO> channels = new ArrayList<ChannelBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		Set<Long> folderIds = new HashSet<Long>();
		for(DeviceGroupMemberPO member: members){
			Set<DeviceGroupMemberChannelPO> channelsPO = member.getChannels();
			
			for(DeviceGroupMemberChannelPO channelPO:channelsPO){
				ChannelBO channelBO = new ChannelBO().set(channelPO)
													.setMemberId(member.getId());
				channels.add(channelBO);
			}
			folderIds.add(member.getFolderId());
			bundles.add(new BundleBO().set(member));
		}
		
		//根据新folderIds查询所有层级（文件夹）
		List<FolderPO> allFolders = resourceQueryUtil.queryFoldersTree(folderIds);

		if(allFolders != null && allFolders.size()>0){
			for(FolderPO allFolderPO: allFolders){
				if(allFolderPO == null) continue;
				FolderBO folderBO = new FolderBO().set(allFolderPO);
				folders.add(folderBO);			
			}
		}
		
		//加入大屏文件夹
		for(BundleBO bundle:bundles){
			if("combineJv230".equals(bundle.getModel())){
				folders.add(new FolderBO().setId(TreeNodeVO.FOLDERID_COMBINEJV230).setName("拼接屏设备"));
				break;
			}
		}
		
		//排序
		Collections.sort(bundles, new BundleBO.BundleIdComparator());
		Collections.sort(bundles, new BundleBO.BundleStatusComparator());
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, bundles, channels, null);
		}
		
		logService.logsHandle(user.getName(), "设备组添加成员", "设备组名称："+group.getName());
		
		return new HashMapWrapper<String, Object>().put("membersTree", _roots)
											  	   .put("members", _channels)
												   .getMap();
	}
	
	/**
	 * 删除成员<br/>
	 * @param groupId 设备组id
	 * @param memberIds 删除成员memberId数组
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/members/{groupId}")
	public Object removeMembers(
			@PathVariable Long groupId,
			String memberIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<Long> memberIdArray = JSONArray.parseArray(memberIds, Long.class);
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		
		group = deviceGroupServiceImpl.removeMembers(group, memberIdArray);
		
		logService.logsHandle(user.getName(), "设备组删除成员", "设备组名称："+group.getName());
		
		return null;
	}
	
	/**
	 * 获取设备组中的发言人角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月16日 下午5:40:42
	 * @param groupId 设备组id
	 * @return List<DeviceGroupBusinessRoleVO> 角色列表
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/spokesman/{groupId}", method = RequestMethod.GET)
	public Object querySpokesman(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		//获取主席
		DeviceGroupBusinessRolePO chairman =  null;
		List<DeviceGroupBusinessRolePO> chairmans =  deviceGroupBusinessRoleDao.findByGroupIdAndSpecial(groupId, BusinessRoleSpecial.CHAIRMAN);
		if(chairmans!=null && chairmans.size()>0){
			chairman = chairmans.get(0);
		}
		
		//获取发言人
		List<DeviceGroupBusinessRolePO> spokesmans = deviceGroupBusinessRoleDao.findByGroupIdAndSpecial(groupId, BusinessRoleSpecial.SPOKESMAN);
		
		//角色id
		Set<Long> roleIds = new HashSet<Long>();
		if(chairman != null) roleIds.add(chairman.getId());
		if(spokesmans!=null && spokesmans.size()>0){
			for(DeviceGroupBusinessRolePO spokesman:spokesmans){
				roleIds.add(spokesman.getId());
			}
		}
		
		List<DeviceGroupBusinessRoleVO> _roles = new ArrayList<DeviceGroupBusinessRoleVO>();
		if(roleIds.size() <= 0) return _roles;
		
		List<DeviceGroupMemberPO> members = deviceGroupMemberDAO.findByRoleIdIn(roleIds);
		DeviceGroupBusinessRoleVO _chairman = new DeviceGroupBusinessRoleVO().set(chairman);
		if(members!=null && members.size()>0){
			for(DeviceGroupMemberPO member:members){
				if(member.getRoleId().equals(chairman.getId())){
					DeviceGroupMemberVO _member = new DeviceGroupMemberVO().set(member);
					_chairman.getMembers().add(_member);
					break;
				}
			}
		}
		_roles.add(_chairman);
		
		if(spokesmans!=null && spokesmans.size()>0){
			for(DeviceGroupBusinessRolePO spokesman:spokesmans){
				DeviceGroupBusinessRoleVO _spokesman = new DeviceGroupBusinessRoleVO().set(spokesman);
				if(members!=null && members.size()>0){
					for(DeviceGroupMemberPO member:members){
						if(member.getRoleId().equals(spokesman.getId())){
							DeviceGroupMemberVO _member = new DeviceGroupMemberVO().set(member);
							_spokesman.getMembers().add(_member);
							break;
						}
					}
				}
				_roles.add(_spokesman);
			}
		}
		
		Collections.sort(_roles, new DeviceGroupBusinessRoleVO.SpokesmanSorter());
		
		return _roles;
	}
	
	/**
	 * 获取设备组中除去发言人角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月19日 下午5:40:42
	 * @param groupId 设备组id
	 * @return List<DeviceGroupBusinessRoleVO> 角色列表
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/roles/except/spokesman/{groupId}", method = RequestMethod.GET)
	public Object queryRolesExceptSpokesman(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<BusinessRoleSpecial> specials = new ArrayList<BusinessRoleSpecial>();
		specials.add(BusinessRoleSpecial.CHAIRMAN);
		specials.add(BusinessRoleSpecial.SPOKESMAN);
		
		//获取非主席和发言人的角色
		List<DeviceGroupBusinessRolePO> roles = deviceGroupBusinessRoleDao.findByGroupIdAndSpecialNotIn(groupId, specials);
		
		//角色id
		Set<Long> roleIds = new HashSet<Long>();
		if(roles!=null && roles.size()>0){
			for(DeviceGroupBusinessRolePO role:roles){
				roleIds.add(role.getId());
			}
		}
		
		List<DeviceGroupBusinessRoleVO> _roles = new ArrayList<DeviceGroupBusinessRoleVO>();
		if(roleIds.size() <= 0) return _roles;
		
		List<DeviceGroupMemberPO> members = deviceGroupMemberDAO.findByRoleIdIn(roleIds);
		
		if(roles!=null && roles.size()>0){
			for(DeviceGroupBusinessRolePO role:roles){
				DeviceGroupBusinessRoleVO _spokesman = new DeviceGroupBusinessRoleVO().set(role);
				if(members!=null && members.size()>0){
					for(DeviceGroupMemberPO member:members){
						if(member.getRoleId().equals(role.getId())){
							DeviceGroupMemberVO _member = new DeviceGroupMemberVO().set(member);
							_spokesman.getMembers().add(_member);
						}
					}
				}
				_roles.add(_spokesman);
			}
		}
		
		return _roles;
	}
	
	/**
	 * 保存配置的角色<br/>
	 * @param members 成员列表，里面包含roleId和roleName<br/>
	 * @throws Exception 
	 * @return Object
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/role")
	public Object updateRole(
			String members,
			HttpServletRequest request) throws Exception{
		
		deviceGroupServiceImpl.updateRole(members);
		
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
	
	/**
	 * 
	 * 查询录制的方案
	 * @param groupId
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/record/{groupId}", method = RequestMethod.GET)
	public Object queryRecord(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		List<RecordPO> recordPOs = recordDao.findByGroupId(groupId);
		List<RecordVO> recordVOs = new ArrayList<RecordVO>();
		for(RecordPO recordPO: recordPOs){
			if(recordPO.isRun()){
				RecordVO recordVO = new RecordVO().set(recordPO);
				recordVOs.add(recordVO);
			}
		}
		
		return new HashMapWrapper<String, Object>().put("records", recordVOs)
												   .getMap();
	}
	
	/**
	 * 一键重置设备组<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月23日 下午5:00:24
	 * @param Long groupId 设备组 id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/reset/{groupId}", method = RequestMethod.POST)
	public Object resetGroup(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		Set<DeviceGroupMemberPO> members = group.getMembers();
		
		List<String> bundleIds = new ArrayList<String>();
		for(DeviceGroupMemberPO member: members){
			if(member.getBundleId() != null){
				bundleIds.add(member.getBundleId());
			}
		}
		
		if(group.getForwardMode().equals(ForwardMode.ROLE)){
			Set<DeviceGroupBusinessRolePO> roles = group.getRoles();
			for(DeviceGroupBusinessRolePO role: roles){
				if(role.getSpecial().equals(BusinessRoleSpecial.AUDIENCE) || role.getSpecial().equals(BusinessRoleSpecial.CUSTOM)){
					if(role.getBundleId() != null){
						bundleIds.add(role.getBundleId());
					}
				}
			}
		}
		
		resourceService.batchClearBundles(bundleIds);
		
		return null;
	}
	
	/**
	 * 一键组会<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月16日 上午10:23:28
	 * @param String name 会议名称
	 * @param String chairman 主席
	 * @param String sourceList 成员列表
	 * @return DeviceGroupVO _group 会议信息
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/quick/group/save")
	public Object quickGroupSave(
			String name,
			String chairman,
			String sourceList,
			HttpServletRequest request) throws Exception{		
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<TplPO> tpls = sysTplDao.findAll();
		if(tpls == null || tpls.size() == 0){
			throw new TplIsNullException();
		}
		
		List<AvtplPO> avtpls = avtplDao.findByUsageType(AvtplUsageType.DEVICE_GROUP);
		if(avtpls == null || avtpls.size() == 0){
			throw new TplIsNullException();
		}
		
		JSONArray sourceArray = JSONArray.parseArray(sourceList);
		
		//建会
		DeviceGroupPO group = meetingServiceImpl.save(
				user.getId(), 
				user.getName(), 
				name, 
				"会议室", 
				"单播", 
				"设备转发模式", 
				"default", 
				"MEETING1", 
				tpls.get(0).getId(), 
				sourceArray, 
				chairman);
		
		List<ChannelNamePO> channelNames = channelNameDao.findAll();
		
		//找到主席,观众,发言人
		DeviceGroupBusinessRolePO roleChairman = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.CHAIRMAN).get(0);
		DeviceGroupBusinessRolePO roleAudience = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.AUDIENCE).get(0);
		List<DeviceGroupBusinessRolePO> roleSpokenmans = queryUtil.queryRoleBySpecial(group, BusinessRoleSpecial.SPOKESMAN);
		
		DeviceGroupMemberPO chairmanMember = queryUtil.queryMemberByRole(group, roleChairman.getId()).get(0);
		List<DeviceGroupMemberPO> audienceMembers = queryUtil.queryMemberByRole(group, roleAudience.getId());
		
		//建议程1
		JSONObject agenda1audio = autoBuildAgendaServiceImpl.generateAudio();
		List<JSONObject> agendaVideos = new ArrayList<JSONObject>();
		
		JSONObject agendaVideo1 = autoBuildAgendaServiceImpl.generatePollingChairman(audienceMembers, roleChairman);
		JSONObject agendaVideo2 = autoBuildAgendaServiceImpl.generateChairmanAudience(roleChairman, roleAudience, roleSpokenmans);
		agendaVideos.add(agendaVideo1);
		agendaVideos.add(agendaVideo2);
		
		meetingServiceImpl.setAgenda(group, "主席议程", "主席看轮询，观众看主席", agenda1audio, agendaVideos, channelNames);
		
		//建议程2
		List<JSONObject> agendaVideos2 = new ArrayList<JSONObject>();
		
		JSONObject agendaVideo3 = autoBuildAgendaServiceImpl.generateSpokenmanAll(roleChairman, roleSpokenmans, roleAudience);
		agendaVideos2.add(agendaVideo3);
		
		meetingServiceImpl.setAgenda(group, "发言人议程", "所有人看发言人", agenda1audio, agendaVideos2, channelNames);
		
		DeviceGroupVO _group = new DeviceGroupVO().set(group);
		
		logService.logsHandle(user.getName(), "一键组会", "设备组名称："+group.getName());
		
		return _group;
	}
	
}
