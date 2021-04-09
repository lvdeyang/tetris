package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.bvc.device.group.dao.GroupRecordInfoDAO;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.SourceType;
import com.sumavision.bvc.device.group.po.GroupRecordInfoPO;
import com.sumavision.bvc.device.group.po.GroupRecordPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.tetris.bvc.business.bo.BusinessDeliverBO;
import com.sumavision.tetris.bvc.business.dao.CombineAudioPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.CombineTemplateGroupAgendeForwardPermissionDAO;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.dao.RunningAgendaDAO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.RunningAgendaPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioSrcPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.CombineAudioPermissionPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundleChannelPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalBundlePO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberTerminalChannelPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardDestinationPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardPO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourceDAO;
import com.sumavision.tetris.bvc.model.agenda.AgendaForwardSourcePO;
import com.sumavision.tetris.bvc.model.agenda.AgendaPO;
import com.sumavision.tetris.bvc.model.agenda.AudioPriority;
import com.sumavision.tetris.bvc.model.agenda.CustomAudioPermissionType;
import com.sumavision.tetris.bvc.model.agenda.DestinationType;
import com.sumavision.tetris.bvc.model.role.InternalRoleType;
import com.sumavision.tetris.bvc.model.role.RoleDAO;
import com.sumavision.tetris.bvc.model.role.RolePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.channel.ChannelParamsType;
import com.sumavision.tetris.bvc.util.AgendaRoleMemberUtil;
import com.sumavision.tetris.bvc.util.MultiRateUtil;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

public class RecordServiceImpl2 {
	

	
	
}
	