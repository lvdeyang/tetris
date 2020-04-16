package com.sumavision.bvc.control.device.command.group.record;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.bvc.command.group.dao.CommandGroupRecordDAO;
import com.sumavision.bvc.command.group.record.CommandGroupRecordPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.control.device.command.group.vo.record.GroupVO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerSettingVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.basic.CommandBasicServiceImpl;
import com.sumavision.bvc.device.command.record.CommandRecordServiceImpl;
import com.sumavision.bvc.device.command.secret.CommandSecretServiceImpl;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/record")
public class CommandRecordController {

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private CommandGroupRecordDAO commandGroupRecordDao;
	
	@Autowired
	CommandBasicServiceImpl commandBasicServiceImpl;

	
	@Autowired
	CommandRecordServiceImpl commandRecordServiceImpl;

	
	@Autowired
	CommandSecretServiceImpl commandSecretServiceImpl;
	
	/**
	 * 开始录制<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:19:46
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object start(
			String id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		commandRecordServiceImpl.start(user.getId(), Long.parseLong(id));
		
		return null;
	}
	
	/**
	 * 停止录制<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:19:22
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		commandRecordServiceImpl.stop(user.getId(), Long.parseLong(id), true);
		return null;
		
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String recordId,
			HttpServletRequest request) throws Exception{
		commandRecordServiceImpl.remove(Long.parseLong(recordId));
		return null;
	}
	
	/**
	 * 查询指挥的录像<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:12:18
	 * @param request
	 * @return GroupVO 数组
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object query(HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		//根据userId查找record
		List<CommandGroupRecordPO> records = commandGroupRecordDao.findAll();//findByRecordUserIdAndRun(user.getId(), false);
		HashMap<Long, List<CommandGroupRecordPO>> groupRecordMap = new HashMap<Long, List<CommandGroupRecordPO>>();
		for(CommandGroupRecordPO record : records){
			List<CommandGroupRecordPO> groupRecords = groupRecordMap.get(record.getGroupId());
			if(groupRecords == null){
				groupRecords = new ArrayListWrapper<CommandGroupRecordPO>().add(record).getList();
				groupRecordMap.put(record.getGroupId(), groupRecords);
			}else{
				groupRecords.add(record);
			}
		}
		
		JSONArray groups = new JSONArray();
		for(List<CommandGroupRecordPO> groupRecords : groupRecordMap.values()){
			GroupVO group = new GroupVO().set(groupRecords);
			groups.add(group);
		}
		
		return new HashMapWrapper<String, Object>().put("groups", groups).getMap();
	}
	
	/**
	 * 开始播放指挥录像<br/>
	 * <p>完整的一段指挥录像，包含多个片段</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:13:01
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/playback")
	public Object startPlayback(
			String recordId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<CommandGroupUserPlayerPO> players = commandRecordServiceImpl.startPlayGroupRecord(user.getId(), Long.parseLong(recordId));
		JSONArray result = new JSONArray();
		for(CommandGroupUserPlayerPO player : players){
			result.add(new CommandGroupUserPlayerSettingVO().set(player));
		}
		return result;
		
	}
	
	/**
	 * 开始播放多段指挥录像<br/>
	 * <p>任选的多个指挥录像片段</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 上午10:13:01
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start/playback/fragments")
	public Object startPlaybackFragments(
			String fragmentIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		List<Long> fragmentIdsArray = JSONArray.parseArray(fragmentIds, Long.class);
		
		List<CommandGroupUserPlayerPO> players = commandRecordServiceImpl.startPlayFragments(user.getId(), fragmentIdsArray);
		JSONArray result = new JSONArray();
		for(CommandGroupUserPlayerPO player : players){
			result.add(new CommandGroupUserPlayerSettingVO().set(player));
		}
		return result;
		
	}

	/**
	 * 停止一个片段播放<br/>
	 * <p>通常点击页面上播放器的叉子触发</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月26日 上午10:18:15
	 * @param businessId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/playback/fragment")
	public Object stopPlaybackFragment(
			String businessId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		List<CommandGroupUserPlayerPO> players = commandRecordServiceImpl.stopPlayFragments(user.getId(), new ArrayListWrapper<String>().add(businessId).getList());
		if(players.size() > 0){
			CommandGroupUserPlayerPO player = players.get(0);
			return new CommandGroupUserPlayerSettingVO().set(player);
		}
		return null;
		
	}
	
}
