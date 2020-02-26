package com.sumavision.bvc.control.device.command.group.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.command.group.dao.CommandGroupMemberDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMessageDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupMessageStyleDAO;
import com.sumavision.bvc.command.group.message.CommandGroupMessagePO;
import com.sumavision.bvc.command.group.message.CommandGroupMessageStylePO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.message.CommandMessageServiceImpl;
import com.sumavision.bvc.device.command.message.CommandMessageStyleServiceImpl;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/message")
public class CommandMessageController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandMessageServiceImpl commandMessageServiceImpl;
	
	@Autowired
	private CommandMessageStyleServiceImpl commandMessageStyleServiceImpl;
	
	@Autowired
	private CommandGroupMessageDAO commandGroupMessageDao;
	
	@Autowired
	private CommandGroupMessageStyleDAO commandGroupMessageStyleDao;
	
	@Autowired
	private CommandGroupMemberDAO commandGroupMemberDao;
	
	/**
	 * 新建通知并发送<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:31:18
	 * @param id
	 * @param content
	 * @param forcedRolling
	 * @param style
	 * @param members
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/send")
	public Object sendMessage(
			String id,
			String content,
			boolean forcedRolling,
			String style,
		    String members,
		    HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		List<Long> dstUserIds = JSONArray.parseArray(members, Long.class);
		CommandGroupMessagePO messagePO = commandMessageServiceImpl.sendMessage(Long.parseLong(id), userId, content, forcedRolling, style, dstUserIds);
		
		return JSON.toJSON(new HashMapWrapper<String, String>()
				.put("id", messagePO.getId().toString())
				.put("date", DateUtil.format(messagePO.getUpdateTime(), DateUtil.dateTimePattern))
				.put("content", messagePO.getContent())
				.put("members", members)
				.put("status", messagePO.getStatus().getName())
				.getMap());
	}
	
	/**
	 * 再次通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:31:36
	 * @param messageId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/again")
	public Object resend(
			String messageId,
		    HttpServletRequest request) throws Exception{
		
		commandMessageServiceImpl.resend(Long.parseLong(messageId));
		return null;
	}
	
	/**
	 * 停止一个通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:31:50
	 * @param messageId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String messageId,
		    HttpServletRequest request) throws Exception{
		
		commandMessageServiceImpl.stop(Long.parseLong(messageId));
		return null;
	}
	
	/**
	 * 停止一个指挥中的全部通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:32:12
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/all")
	public Object stopAll(
			String id,
		    HttpServletRequest request) throws Exception{
		
		commandMessageServiceImpl.stopAll(Long.parseLong(id));
		return null;
	}
	
	/**
	 * 删除多个通知<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:32:27
	 * @param messageIds
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String messageIds,
		    HttpServletRequest request) throws Exception{
		
		commandMessageServiceImpl.remove(JSONArray.parseArray(messageIds, Long.class));
		return null;
	}
	
	/**
	 * 分页查询指挥中的通知列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:32:53
	 * @param id
	 * @param currentPage
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/message")
	public Object queryMessage(
			String id,
			int currentPage,
			int pageSize,
		    HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<CommandGroupMessagePO> messages = commandGroupMessageDao.findByGroupId(Long.parseLong(id), page);
		if(messages == null){
			return null;
		}
		
		JSONObject result = new JSONObject();
		result.put("total", messages.getTotalElements());
		JSONArray rows = new JSONArray();
		
		for(CommandGroupMessagePO messagePO : messages){
			
			//查询并拼接用户名
			String userIdsStr = messagePO.getUserIds();
			List<String> userIdsStrList= Arrays.asList(userIdsStr.split("-"));
			List<Long> userIds = new ArrayList<Long>();
			for(String userIdStr : userIdsStrList){
				userIds.add(Long.parseLong(userIdStr));
			}			
			List<String> userNames = commandGroupMemberDao.findUserNamesByGroupIdAndUserIds(Long.parseLong(id), userIds);
			String userNamesStr = StringUtils.join(userNames.toArray(), ", ");
			
			Map<String, String> row = new HashMapWrapper<String, String>()
					.put("id", messagePO.getId().toString())
					.put("date", DateUtil.format(messagePO.getUpdateTime(), DateUtil.dateTimePattern))
					.put("content", messagePO.getContent())
					.put("members", userNamesStr)
					.put("status", messagePO.getStatus().getName())
					.getMap();			
			rows.add(row);
		}
		
		result.put("rows", rows);
		
		return result;
	}
	
	/**
	 * 添加一个模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:33:31
	 * @param name
	 * @param fontFamily
	 * @param fontSize
	 * @param textDecoration
	 * @param color
	 * @param rollingSpeed
	 * @param rollingMode
	 * @param rollingLocation
	 * @param rollingTime
	 * @param rollingTimeUnlimited
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/style/templates/add")
	public Object addStyleTemplate(
			String name,
			String fontFamily,
		    String fontSize,
			String textDecoration,
			String color,
			String rollingSpeed,
			String rollingMode,
			String rollingLocation,
			String rollingTime,
			boolean rollingTimeUnlimited,
		    HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		CommandGroupMessageStylePO style = commandMessageStyleServiceImpl.addStyleTemplate(
				userId,
				name,
				fontFamily,
			    fontSize,
				textDecoration,
				color,
				rollingSpeed,
				rollingMode,
				rollingLocation,
				rollingTime,
				rollingTimeUnlimited);
		
//		String styleStr = JSON.toJSONString(style);
		JSONObject styleJson = (JSONObject) JSON.toJSON(style);
		styleJson.put("id", style.getId().toString());
		return styleJson;
	}

	/**
	 * 修改一个模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:33:43
	 * @param id
	 * @param name
	 * @param fontFamily
	 * @param fontSize
	 * @param textDecoration
	 * @param color
	 * @param rollingSpeed
	 * @param rollingMode
	 * @param rollingLocation
	 * @param rollingTime
	 * @param rollingTimeUnlimited
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/style/templates/edit")
	public Object editStyleTemplate(
			String id,
			String name,
			String fontFamily,
		    String fontSize,
			String textDecoration,
			String color,
			String rollingSpeed,
			String rollingMode,
			String rollingLocation,
			String rollingTime,
			boolean rollingTimeUnlimited,
		    HttpServletRequest request) throws Exception{
		
		commandMessageStyleServiceImpl.editStyleTemplate(
				Long.parseLong(id),
//				userId,
				name,
				fontFamily,
			    fontSize,
				textDecoration,
				color,
				rollingSpeed,
				rollingMode,
				rollingLocation,
				rollingTime,
				rollingTimeUnlimited);
		
		return null;
	}

	/**
	 * 删除一个模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:34:52
	 * @param templateId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/style/templates/remove")
	public Object removeStyleTemplate(
			String templateId,
		    HttpServletRequest request) throws Exception{
		
		commandMessageStyleServiceImpl.removeStyleTemplate(Long.parseLong(templateId));
		return null;
	}

	/**
	 * 分页查询指挥中的通知样式模板列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月19日 下午4:35:06
	 * @param currentPage
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/style/templates")
	public Object queryStyleTemplate(
			int currentPage,
			int pageSize,
		    HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<CommandGroupMessageStylePO> styles = commandGroupMessageStyleDao.findAll(page);//这里后续改成findByUserId
		if(styles == null){
			return null;
		}
		
		JSONObject result = new JSONObject();
		result.put("total", styles.getTotalElements());
		JSONArray rows = new JSONArray();
		for(CommandGroupMessageStylePO style : styles){
			JSONObject styleJson = (JSONObject) JSON.toJSON(style);
			styleJson.put("id", style.getId().toString());
			rows.add(styleJson);
		}
		result.put("rows", rows);
		
		return result;
	}	
	
	/**
	 * 发送实时消息，给会议内的成员<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 下午7:09:55
	 * @param id
	 * @param message
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/broadcast/instant/message")
	public Object broadcastInstantMessage(
			String id,
		    String message,
		    HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);		
		commandMessageServiceImpl.broadcastInstantMessage(user.getId(), user.getName(), Long.parseLong(id), message);
		return null;		
		
	}

	//发送文件
	public Object sendFile(HttpServletRequest request) throws Exception{
		return null;
	}
	
	//查询当前用户与目标用户的历史消息
	public Object queryHistoryMessage(HttpServletRequest request) throws Exception{
		return null;
	}
	
}
