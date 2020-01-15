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
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.split.CommandSplitServiceImpl;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/split")
public class CommandSplitController {
	
	@Autowired
	private CommandSplitServiceImpl commandSplitServiceImpl;
	
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
		
		CommandGroupUserInfoPO userInfo = commandSplitServiceImpl.changeLayoutScheme(userId, split);
		
		List<CommandGroupUserPlayerPO> players = userInfo.obtainUsingSchemePlayers();
		List<CommandGroupUserPlayerSettingVO> settings = new ArrayList<CommandGroupUserPlayerSettingVO>();
		for(CommandGroupUserPlayerPO player : players){
			CommandGroupUserPlayerSettingVO setting = new CommandGroupUserPlayerSettingVO().set(player);
			settings.add(setting);
		}		
		return JSON.toJSONString(new HashMapWrapper<String, Object>().put("settings", settings)
				  .getMap());
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
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		commandSplitServiceImpl.exchangeTwoSplit(userId, serial, toSerial);
		
		return null;
	}
}
