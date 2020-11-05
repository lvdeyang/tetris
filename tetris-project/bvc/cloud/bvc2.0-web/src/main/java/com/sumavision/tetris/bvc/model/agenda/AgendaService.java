package com.sumavision.tetris.bvc.model.agenda;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import com.sumavision.tetris.bvc.model.agenda.combine.AutoCombineService;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineAudioDAO;
import com.sumavision.tetris.bvc.model.agenda.combine.CombineVideoDAO;
import com.sumavision.tetris.bvc.model.agenda.exception.AgendaNotFoundException;
import com.suma.venus.resource.dao.BundleDao;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.CommonForwardDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberRolePermissionDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelDAO;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageTaskDAO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import lombok.extern.slf4j.Slf4j;

/**
 * AgendaService<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月22日 上午10:03:43
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class AgendaService {
	
	@Autowired
	private CombineVideoDAO combineVideoDao;
	
	@Autowired
	private CombineAudioDAO combineAudioDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private RoleChannelDAO roleChannelDao;
	
//	@Autowired
//	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private CommonForwardDAO commonForwardDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private AgendaForwardDAO agendaForwardDao;
	
	@Autowired
	private GroupMemberRolePermissionDAO groupMemberRolePermissionDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private GroupMemberDAO groupMemberDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private RunningAgendaDAO runningAgendaDao;
	
	@Autowired
	private AutoCombineService autoCombineService;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	private Map<String, TerminalPO> terminalPOMap = new HashMap<String, TerminalPO>();
	
	@Autowired
	private AgendaDAO agendaDao;
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @param String businessInfoTypeName 业务类型
	 * @param String agendaModeTypeName 模式
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO add(
			String name,
			String remark,
			Integer volume,
			String audioOperationType,
			String businessInfoTypeName,
			String agendaModeTypeName) throws Exception{
		AgendaPO agenda = new AgendaPO();
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(volume);
		agenda.setAudioOperationType(AudioOperationType.valueOf(audioOperationType));
		if(businessInfoTypeName!=null && !"".equals(businessInfoTypeName)){
			agenda.setBusinessInfoType(BusinessInfoType.fromName(businessInfoTypeName));
		}
		if(agendaModeTypeName!=null && !"".equals(agendaModeTypeName)){
			agenda.setAgendaModeType(AgendaModeType.fromName(agendaModeTypeName));
		}
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Integer volume 音量
	 * @param String audioOperationType 音频操作类型
	 * @param String businessInfoTypeName 业务类型
	 * @param String agendaModeTypeName 模式
	 * @return AgendaVO 议程
	 */
	@Transactional(rollbackFor = Exception.class)
	public AgendaVO edit(
			Long id,
			String name,
			String remark,
			Integer volume,
			String audioOperationType,
			String businessInfoTypeName,
			String agendaModeTypeName) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda == null){
			throw new AgendaNotFoundException(id);
		}
		agenda.setName(name);
		agenda.setRemark(remark);
		agenda.setVolume(volume);
		agenda.setAudioOperationType(AudioOperationType.valueOf(audioOperationType));
		if(businessInfoTypeName!=null && !"".equals(businessInfoTypeName)){
			agenda.setBusinessInfoType(BusinessInfoType.fromName(businessInfoTypeName));
		}
		if(agendaModeTypeName!=null && !"".equals(agendaModeTypeName)){
			agenda.setAgendaModeType(AgendaModeType.fromName(agendaModeTypeName));
		}
		agenda.setUpdateTime(new Date());
		agendaDao.save(agenda);
		return new AgendaVO().set(agenda);
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		AgendaPO agenda = agendaDao.findOne(id);
		if(agenda != null){
			agendaDao.delete(agenda);
		}
	}
		
}
