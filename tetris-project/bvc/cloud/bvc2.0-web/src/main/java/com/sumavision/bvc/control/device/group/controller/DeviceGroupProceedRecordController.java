package com.sumavision.bvc.control.device.group.controller;

import java.util.ArrayList;
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
import com.sumavision.bvc.control.device.group.vo.DeviceGroupProceedRecordVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.bvc.device.group.service.DeviceGroupProceedRecordQueryServiceImpl;
import com.sumavision.bvc.device.group.vo.DeviceGroupProceedRecordStatisticsBO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/device/group/proceed/record")
public class DeviceGroupProceedRecordController {

	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;

	@Autowired
	private DeviceGroupProceedRecordQueryServiceImpl deviceGroupProceedRecordQueryServiceImpl;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 查询会议记录<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 下午3:11:28
	 * @param groupId
	 * @param pageSize
	 * @param currentPage
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/records/{groupId}", method = RequestMethod.POST)
	public Object queryRecords(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<DeviceGroupProceedRecordPO> records = deviceGroupProceedRecordDao.findByGroupIdOrderByStartTimeDesc(groupId, page);
		long total = records.getTotalElements();
		List<DeviceGroupProceedRecordVO> vos = new ArrayList<DeviceGroupProceedRecordVO>();
		for(DeviceGroupProceedRecordPO record : records.getContent()){
			vos.add(new DeviceGroupProceedRecordVO().set(record));
		}		
		
		JSONObject data = new JSONObject();
		data.put("rows", vos);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * 统计当前用户的开会总长，总场次，总人数<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月23日 上午9:27:40
	 * @return map中key：time、totalProceedRecord、totalPeople
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/statistics/total/of/record/time/people", method = RequestMethod.POST)
	public Object statisticsTotalOfRecordTimePeople(
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		return deviceGroupProceedRecordQueryServiceImpl.statisticsTimeAndNumberOfPeopleAndNumberOfProceedRecord(userId);
	}
	
//	/**
//	 * 统计最近开会人数和开会时长<br/>
//	 * <b>作者:</b>lx<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年10月23日 上午9:33:53
//	 * @return DeviceGroupProceedRecordStatisticsVO 
//	 */
//	@ResponseBody
//	@JsonBody
//	@RequestMapping(value = "/statistics/recently/people/time", method = RequestMethod.POST)
//	public Object statistics(
//			HttpServletRequest request) throws Exception{
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		return deviceGroupProceedRecordQueryServiceImpl.statisticsRecentlyTimeAndNumberOfPeople(userId);
//	}
	
	/**
	 * 根据userid查询会议记录<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 下午3:11:28
	 * @param groupId
	 * @param pageSize
	 * @param currentPage
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/records/by/userid", method = RequestMethod.POST)
	public Object queryRecordsByUserId(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<DeviceGroupProceedRecordPO> records = deviceGroupProceedRecordDao.findByUserIdOrderByStartTimeDesc(userId, page);
		long total = records.getTotalElements();
		List<DeviceGroupProceedRecordVO> vos = new ArrayList<DeviceGroupProceedRecordVO>();
		for(DeviceGroupProceedRecordPO record : records.getContent()){
			vos.add(new DeviceGroupProceedRecordVO().set(record));
		}		
		
		JSONObject data = new JSONObject();
		data.put("rows", vos);
		data.put("total", total);
		
		return data;
	}
}