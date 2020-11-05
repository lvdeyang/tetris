package com.sumavision.tetris.bvc.business.terminal.hall;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/tetris/bvc/business/terminal/bundle/conference/hall/permission")
public class TerminalBundleConferenceHallPermissionController {

	@Autowired
	private TerminalBundleConferenceHallPermissionQuery terminalBundleConferenceHallPermissionQuery;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionService terminalBundleConferenceHallPermissionService;
	
	/**
	 * 查询会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:28:53
	 * @param Long conferenceHallId 会场id
	 * @param Long terminalBundleId 终端设备id
	 * @return TerminalBundleConferenceHallPermissionVO 设备绑定信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long conferenceHallId, 
			Long terminalBundleId,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleConferenceHallPermissionQuery.load(conferenceHallId, terminalBundleId);
	}
	
	/**
	 * 添加会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:33:43
	 * @param Long conferenceHallId 会场id
	 * @param Long terminalBundleId 终端设备id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleConferenceHallPermissionVO 会场设备绑定
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long conferenceHallId, 
			Long terminalBundleId,
			String bundleType,
			String bundleId,
			String bundleName,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleConferenceHallPermissionService.add(conferenceHallId, terminalBundleId, bundleType, bundleId, bundleName);
	}
	
	/**
	 * 修改会场设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:33:43
	 * @param Long id 会场设备绑定id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleConferenceHallPermissionVO 会场设备绑定
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String bundleType,
			String bundleId,
			String bundleName,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleConferenceHallPermissionService.edit(id, bundleType, bundleId, bundleName);
	}
	
	/**
	 * 删除会场设备绑定<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月13日 下午2:38:56
	 * @param Long id 会场设备绑定id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id,
			HttpServletRequest request) throws Exception{
		
		terminalBundleConferenceHallPermissionService.delete(id);
		return null;
	}
	
}
