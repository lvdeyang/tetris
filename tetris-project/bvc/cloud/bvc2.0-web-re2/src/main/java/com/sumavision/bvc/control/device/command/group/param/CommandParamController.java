package com.sumavision.bvc.control.device.command.group.param;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.sumavision.bvc.device.group.service.DeviceGroupAvtplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAvtplGearsVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupAvtplVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleParamVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupBusinessRoleVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupRecordSchemeVO;
import com.sumavision.bvc.control.device.group.vo.DeviceGroupScreenLayoutVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAvtplDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAvtplGearsDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoDstDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupConfigVideoSrcDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupRecordSchemeDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupScreenLayoutDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupRecordSchemePO;
import com.sumavision.bvc.device.group.po.DeviceGroupScreenLayoutPO;
import com.sumavision.bvc.device.group.po.DeviceGroupScreenPositionPO;
import com.sumavision.bvc.device.group.service.MeetingServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

@Controller
@RequestMapping(value = "/command/param")
public class CommandParamController {

	@Autowired
	private DeviceGroupAvtplDAO deviceGroupAvtplDao;

	@Autowired
	private DeviceGroupAvtplService deviceGroupAvtplService;
	

	/**
	 * @Title: 查询设备组参数方案
	 * @param groupId
	 * @param pageSize
	 * @param currentPage
	 * @throws Exception
	 * @return Object 返回类型 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/avtpl/{groupId}")
	public Object queryAvtpl(
			@PathVariable Long groupId,
			HttpServletRequest request) throws Exception{
		DeviceGroupAvtplPO avtplPO = deviceGroupAvtplDao.findByBusinessGroupId(groupId);
		DeviceGroupAvtplVO avtpl = new DeviceGroupAvtplVO().set(avtplPO);
		return avtpl;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/modify/avtpl/{avtplId}")
	public Object modifyAvtpl(
			@PathVariable Long avtplId,
			String videoFormat,
			String audioFormat,
			boolean portReuse,
			String adaptionJson,
			HttpServletRequest request) throws Exception{
		DeviceGroupAvtplPO avtplPO = deviceGroupAvtplService.modify(avtplId,
				videoFormat,
				audioFormat,
				portReuse,
				adaptionJson);

		return "success";
	}
}
