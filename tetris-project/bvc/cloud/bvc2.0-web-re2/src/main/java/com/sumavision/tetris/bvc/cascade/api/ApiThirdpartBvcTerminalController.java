package com.sumavision.tetris.bvc.cascade.api;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.ConfigType;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.AgendaServiceImpl;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.group.GroupLock;
import com.sumavision.tetris.bvc.business.group.GroupMemberType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.bvc.business.group.GroupStatus;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallPO;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallService;
import com.sumavision.tetris.bvc.business.terminal.hall.ConferenceHallVO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;

/**
 * 提供给接入层的接口<br/>
 * <p>包括“终端呼叫”等</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年7月30日 下午4:25:06
 */
@Controller
@RequestMapping(value = "/api/thirdpart/bvc/terminal")
public class ApiThirdpartBvcTerminalController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private MeetingServiceImpl meetingServiceImpl;
	
	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	@Autowired
	private GroupDAO groupDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private ConferenceHallService conferenceHallService;
	
	@Autowired
	private GroupService groupService;
	
	/**
	 * 收到终端呼叫，执行一个会议<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月30日 下午4:21:50
	 * @param request
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/call")
	public Object terminalCall(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
		
		String from = requestWrapper.getString("from");
		String to = requestWrapper.getString("to");
		System.out.println("/api/thirdpart/bvc/terminal " + "from: " + from + ", to: " + to);
		boolean joinSuccess = false;
		
		GroupPO group = groupDao.findByGroupNumber(Long.valueOf(to));
		BundlePO bundle = bundleDao.findByUsername(from);
		if(group == null ){
			throw new BaseException(StatusCode.FORBIDDEN, "会议不存在，会议号码："+to);
		}
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, group.getName()+"  会议没有开启");
		}
		if(bundle == null){
			throw new BaseException(StatusCode.FORBIDDEN, "设备不存在,设备号码"+from);
		}
		
		ConferenceHallPO hall = conferenceHallService.bundleExchangeToHall(new ArrayListWrapper<BundlePO>().add(bundle).getList()).get(0);
		
		List<GroupLock> grooLocks = Stream.of(GroupLock.values()).collect(Collectors.toList());
		if(!grooLocks.contains(group.getGroupLock())){
			groupService.addOrEnterMembers(group.getId(), null, new ArrayListWrapper<Long>().add(hall.getId()).getList(), null);
			joinSuccess = true;
		}else if (GroupLock.COMMON_LOCK.equals(group.getGroupLock())) {
			List<BusinessGroupMemberPO> members = group.getMembers();
			for(BusinessGroupMemberPO member : members){
				if(member.getOriginId().equals(hall.getId().toString()) && member.getGroupMemberType().equals(GroupMemberType.MEMBER_HALL)){
					groupService.addOrEnterMembers(group.getId(), null, new ArrayListWrapper<Long>().add(hall.getId()).getList(), null);
					joinSuccess = true;
				}
			}
			if(!joinSuccess){
				throw new BaseException(StatusCode.FORBIDDEN, "会议锁定，设备不能加会,设备号码"+from);
			}
		}
		
		return joinSuccess;
	}
	
}
