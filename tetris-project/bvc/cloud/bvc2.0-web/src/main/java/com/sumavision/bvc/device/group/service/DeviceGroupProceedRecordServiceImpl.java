package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.config.Constant;
import com.sumavision.bvc.device.group.bo.AudioParamBO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PassByContentBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.bo.RecordSourceBO;
import com.sumavision.bvc.device.group.bo.VideoParamBO;
import com.sumavision.bvc.device.group.dao.ChannelForwardDAO;
import com.sumavision.bvc.device.group.dao.CombineAudioDAO;
import com.sumavision.bvc.device.group.dao.CombineVideoDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigAudioDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.enumeration.RecordToVodType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.group.exception.DeviceGroupAlreadyStartedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.exception.DeviceGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.group.exception.TplIsNullException;
import com.sumavision.bvc.device.group.po.ChannelForwardPO;
import com.sumavision.bvc.device.group.po.CombineAudioPO;
import com.sumavision.bvc.device.group.po.CombineAudioSrcPO;
import com.sumavision.bvc.device.group.po.CombineVideoPO;
import com.sumavision.bvc.device.group.po.CombineVideoPositionPO;
import com.sumavision.bvc.device.group.po.CombineVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigAudioPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoDstPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPositionPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoSrcPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberChannelPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberScreenRectPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.jv230.dao.CombineJv230DAO;
import com.sumavision.bvc.device.jv230.dao.Jv230ChannelDAO;
import com.sumavision.bvc.device.jv230.dto.Jv230ChannelDTO;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.device.jv230.po.Jv230ChannelPO;
import com.sumavision.bvc.device.jv230.po.Jv230PO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO;
import com.sumavision.bvc.meeting.logic.ExecuteBusinessReturnBO.ResultDstBO;
import com.sumavision.bvc.meeting.logic.record.mims.MimsService;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.bvc.system.dao.AuthorizationDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.bvc.system.dao.RecordSchemeDAO;
import com.sumavision.bvc.system.dao.ScreenLayoutDAO;
import com.sumavision.bvc.system.dao.TplDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.ServLevel;
import com.sumavision.bvc.system.enumeration.TplContentType;
import com.sumavision.bvc.system.po.AuthorizationMemberPO;
import com.sumavision.bvc.system.po.AuthorizationPO;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.bvc.system.po.DictionaryPO;
import com.sumavision.bvc.system.po.RecordSchemePO;
import com.sumavision.bvc.system.po.ScreenLayoutPO;
import com.sumavision.bvc.system.po.ScreenPositionPO;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashSetWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 会议执行记录<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月19日 上午11:11:30
 */
@Service("com.sumavision.bvc.device.group.DeviceGroupProceedRecordServiceImpl")
public class DeviceGroupProceedRecordServiceImpl {

	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;

	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDAO;
	
	@Autowired
	private AvtplDAO sysAvtplDao;
	
	@Autowired
	private TplDAO sysTplDao;
	
	@Autowired
	private BusinessRoleDAO sysBusinessRoleDao;
	
	@Autowired
	private ScreenLayoutDAO sysScreenLayoutDao;
	
	@Autowired
	private RecordSchemeDAO sysRecordSchemeDao;
	
	@Autowired
	private com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO gBusinessRoleDao;
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private ChannelForwardDAO channelForwardDao;
	
	@Autowired
	private DeviceGroupConfigAudioDAO deviceGroupConfigAudioDao;
	
	@Autowired
	private DeviceGroupConfigVideoSrcDAO deviceGroupConfigVideoSrcDao;
	
	@Autowired
	private DeviceGroupConfigVideoDstDAO deviceGroupConfigVideoDstDao;
	
	@Autowired
	private CombineJv230DAO combineJv230Dao;
	
	@Autowired
	private Jv230ChannelDAO jv230ChannelDao;
	
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	
	@Autowired
	private RecordDAO recordDao;
	
	@Autowired
	private QueryUtil queryUtil;
		
	@Autowired
	private VideoServiceImpl videoServiceImpl;
	
	@Autowired
	private AudioServiceImpl audioServiceImpl;
	
	@Autowired
	private RecordServiceImpl recordServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private ChannelNameDAO channelNameDAO;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ExtraInfoService extraInfoService;
	
	@Autowired
	private DictionaryDAO conn_dictionary;
	
	@Autowired
	private AuthorizationDAO authorizationDao;
	
	@Autowired
	private DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	private AuthorizationServiceImpl authorizationServiceImpl;
	
	@Autowired
	private NumberOfMembersServiceImpl numberOfMembersServiceImpl;
	
	@Autowired
	private AutoBuildAgendaServiceImpl autoBuildAgendaServiceImpl;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private MimsService mimsService;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	/**
	 * 开会记录<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 上午11:33:46
	 * @param group
	 * @throws Exception
	 */
	public void saveStart(
			DeviceGroupPO group
			) throws Exception{
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		for(DeviceGroupMemberPO member: members){
			bundles.add(new BundleBO().set(member));
		}
		bundles = resourceQueryUtil.appendBundleOnlineStatusIntoBundleBos(bundles);
		int onlineCount = BundleBO.countOnline(bundles);
		DeviceGroupAuthorizationPO auth = deviceGroupAuthorizationDao.findByGroupUuid(group.getUuid());
		
		DeviceGroupProceedRecordPO recordPO = new DeviceGroupProceedRecordPO();
		recordPO.setUserId(group.getUserId());
		recordPO.setStartTime(new Date());
		recordPO.setAuthorizationMemberNumber(auth==null?0:auth.getAuthorizationMembers().size());
		recordPO.setTotalMemberNumber(group.getMembers().size());
		recordPO.setOnlineMemberNumber(onlineCount);
		recordPO.setGroupId(group.getId());
		recordPO.setGroupName(group.getName());
		deviceGroupProceedRecordDao.save(recordPO);
	}
	

	public void saveStop(
			DeviceGroupPO group
			) throws Exception{
				
		DeviceGroupProceedRecordPO recordPO = deviceGroupProceedRecordDao.findByGroupIdAndFinished(group.getId(), false);
		if(recordPO == null) return;
		recordPO.setEndTime(new Date());
		recordPO.setFinished(true);
		deviceGroupProceedRecordDao.save(recordPO);
	}
	
}
