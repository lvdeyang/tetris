package com.sumavision.bvc.control.device.command.group.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.enumeration.GroupType;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.bvc.control.utils.TreeUtils;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.CommandForwardServiceImpl;
import com.sumavision.bvc.device.command.basic.forward.ForwardReturnBO;
import com.sumavision.bvc.device.command.basic.osd.CommandOsdServiceImpl;
import com.sumavision.bvc.device.command.basic.page.CommandPageServiceImpl;
import com.sumavision.bvc.device.command.basic.remind.CommandRemindServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceLocalServiceImpl;
import com.sumavision.bvc.device.command.basic.silence.CommandSilenceServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.GroupMemberDAO;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupMemberPO;
import com.sumavision.tetris.bvc.business.group.GroupMemberService;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.GroupVO;
//import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.function.GroupFunctionService;
import com.sumavision.tetris.bvc.model.agenda.AgendaExecuteService;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/agenda")
public class CommandAgendaController {
	
	@Autowired
	private BusinessCommonService businessCommonService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupMemberService groupMemberService;
	
	@Autowired
	private GroupFunctionService groupFunctionService;

	@Autowired
	private CommandBasicServiceImpl commandBasicServiceImpl;

	@Autowired
	private CommandForwardServiceImpl commandForwardServiceImpl;

	@Autowired
	private CommandSilenceServiceImpl commandSilenceServiceImpl;

	@Autowired
	private CommandRemindServiceImpl commandRemindServiceImpl;

	@Autowired
	private CommandSilenceLocalServiceImpl commandSilenceLocalServiceImpl;

	@Autowired
	private CommandPageServiceImpl commandPageServiceImpl;

	@Autowired
	private GroupDAO groupDao;

	@Autowired
	private GroupMemberDAO groupMemberDao;

	@Autowired
	private CommandGroupDAO commandGroupDao;

	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private TreeUtils treeUtils;

	@Autowired
	private QueryUtil queryUtil;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private CommandOsdServiceImpl commandOsdServiceImpl;
	
	@Autowired
	private BusinessReturnService businessReturnService;

	@Autowired
	private AgendaExecuteService agendaExecuteService;
	
	/**
	 * 执行议程<br/>
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
	@RequestMapping(value = "/run")
	public Object runAgenda(
			String groupId,
			String agendaId,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(true);
		agendaExecuteService.runAgenda_ST(Long.parseLong(groupId), Long.parseLong(agendaId), null, true);
		
		return null;		
	}
	
}
