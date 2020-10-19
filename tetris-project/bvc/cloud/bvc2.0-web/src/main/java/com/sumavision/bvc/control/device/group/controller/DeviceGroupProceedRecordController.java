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
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/device/group/proceed/record")
public class DeviceGroupProceedRecordController {

	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;

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
		
		Page<DeviceGroupProceedRecordPO> records = deviceGroupProceedRecordDao.findByGroupId(groupId, page);
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