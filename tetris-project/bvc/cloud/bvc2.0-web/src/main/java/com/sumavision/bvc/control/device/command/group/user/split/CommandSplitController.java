package com.sumavision.bvc.control.device.command.group.user.split;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.split.CommandSplitServiceImpl;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.page.PageInfoDAO;
import com.sumavision.tetris.bvc.page.PageInfoPO;
import com.sumavision.tetris.bvc.page.PageTaskService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/split")
public class CommandSplitController {
	
	@Autowired
	private CommandSplitServiceImpl commandSplitServiceImpl;
	
	@Autowired
	private PageTaskService pageTaskService;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private PageInfoDAO	pageInfoDao;
	
	@Autowired
	
	private UserUtils userUtils;
	
	/**
	 * 切换分屏方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:30:27
	 * @param split 分屏方案的id，见 PlayerSplitLayout
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/change")
	public Object change(
			int split,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		//设置CommandGroupUserInfoPO、CommandGroupUserLayoutShemePO数据，后续使用新数据结构
		commandSplitServiceImpl.changeLayoutScheme_re(userId, split);
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(userId.toString(), terminal.getId());
		PlayerSplitLayout newSplitLayout = PlayerSplitLayout.fromId(split);
		int pageSize = newSplitLayout.getPlayerCount();
		
		pageTaskService.jumpToPageAndChangeSplit(pageInfo, 1, pageSize);
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/next/page")
	public Object nextPage(
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(userId.toString(), terminal.getId());
		
		pageTaskService.jumpToPageAndChangeSplit(pageInfo, pageInfo.getCurrentPage()+1, pageInfo.getPageSize());
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/previous/page")
	public Object previousPage(
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		TerminalPO terminal = terminalDao.findByType(com.sumavision.tetris.bvc.model.terminal.TerminalType.QT_ZK);
		PageInfoPO pageInfo = pageInfoDao.findByOriginIdAndTerminalId(userId.toString(), terminal.getId());
		
		pageTaskService.jumpToPageAndChangeSplit(pageInfo, pageInfo.getCurrentPage()-1, pageInfo.getPageSize());
		
		return null;
	}
	
	/**
	 * 交换2个分屏的位置<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 上午9:31:41
	 * @param serial
	 * @param toSerial
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/exchange")
	public Object agree(
			int serial,
			int toSerial,
			HttpServletRequest request) throws Exception{
		
		throw new BaseException(StatusCode.FORBIDDEN, "不能移动窗口");
		
//		Long userId = userUtils.getUserIdFromSession(request);
//		
//		commandSplitServiceImpl.exchangeTwoSplit(userId, serial, toSerial);
//		
//		return null;
	}
	
	/**
	 * 清空所有的播放器及其业务<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月25日 上午11:49:44
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/clear/all/players")
	public Object clearAllPlayers(
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandSplitServiceImpl.clearAllPlayers(userId);
		
		return null;
	}
}
