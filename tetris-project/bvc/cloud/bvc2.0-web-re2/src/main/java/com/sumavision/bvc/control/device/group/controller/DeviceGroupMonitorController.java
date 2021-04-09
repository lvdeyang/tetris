package com.sumavision.bvc.control.device.group.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupMonitorMemberVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.RecordStatus;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.DeviceGroupServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/device/group/monitor")
public class DeviceGroupMonitorController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	private RecordDAO recordDao;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private DeviceGroupServiceImpl deviceGroupServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;

	/**
	 * @Title: 查询监控录制设备<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/bundles/{groupId}", method = RequestMethod.GET)
	public Object queryMonitorBundles(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<DeviceGroupMemberDTO> pageMembers = deviceGroupMemberDao.findGroupRecordMembersByGroupId(groupId, page);
		List<RecordPO> records= recordDao.findBundleRecordByGroupId(groupId, RecordType.BUNDLE);
		
		List<DeviceGroupMemberDTO> members = pageMembers.getContent();
		long total = pageMembers.getTotalElements();
		
		List<DeviceGroupMonitorMemberVO> monitorMembers = DeviceGroupMonitorMemberVO.getConverter(DeviceGroupMonitorMemberVO.class).convert(members, DeviceGroupMonitorMemberVO.class);
		
		if(records != null && records.size() > 0){
			for(RecordPO record: records){
				for(DeviceGroupMonitorMemberVO monitorMember: monitorMembers){
					//videoMemberId作为查询录制设备标志
					if(record.getVideoMemberId().equals(monitorMember.getId()) && record.isRun()){
						monitorMember.setRecordStatus(RecordStatus.RUN.getName());
					}
				}
			}
		}
	
		JSONObject data = new JSONObject();
		data.put("rows", monitorMembers);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 开始监控录制<br/>
	 * @param groupId 设备组id
	 * @param memberId 设备memberId
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start/record/{groupId}")
	public Object startRecord(
			@PathVariable Long groupId,
			Long memberId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.startMonitorRecord(groupId, memberId);	
		
		logService.logsHandle(user.getName(), "开始监控录制", "设备组名称："+group.getName()+"设备memberId"+memberId);
		
		return null;
	}
	
	/**
	 * @Title: 停止监控录制<br/>
	 * @param groupId 设备组id
	 * @param memberId 设备memberId
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/record/{groupId}")
	public Object stopRecord(
			@PathVariable Long groupId,
			Long memberId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.stopMonitorRecord(groupId, memberId);
		
		logService.logsHandle(user.getName(), "停止监控录制", "设备组名称："+group.getName()+"设备memberId"+memberId);
		
		return null;
	}
}
