package com.sumavision.tetris.bvc.business.group.api;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.swing.GroupLayout.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.internal.function.json.Append;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;
import com.sumavision.bvc.control.utils.TreeUtils;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
import com.sumavision.tetris.bvc.business.OriginType;
import com.sumavision.tetris.bvc.business.group.BusinessType;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.group.GroupQuery;
import com.sumavision.tetris.bvc.business.group.GroupService;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.bvc.control.welcome.UserVO;
import io.swagger.annotations.Info;

@Controller
@RequestMapping(value = "/api/tvos/group")
public class ApiTvosGroupController {
	
	@Autowired
	private GroupQuery groupQuery;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired	
	private TreeUtils treeUtils;
	
	
	/**
	 * 
	 * 获取会议列表<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月14日 上午11:50:51
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/tvos/group/list")
	public Object queryTvosGroupList(HttpServletRequest request) throws Exception{
		return groupQuery.queryTvosGroupList();
		
	}

	/**
	 * 
	 * 开始会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月14日 上午11:53:37
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
			HttpServletRequest request)throws Exception{
		Object result = groupService.start(Long.parseLong(id), -1);
		return result;	
	}
	
	/**
	 * 
	 * 停止会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月14日 上午11:52:39
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop")
	public Object stop(String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		Object chairSplits = groupService.stop(userId, Long.parseLong(id), 0);
		return chairSplits;
	}
	
	/**
	 * 
	 * 删除会议<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月14日 上午11:51:38
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove")
	public Object remove(String ids,
			HttpServletRequest request) throws Exception{
		Long userId = userUtils.getUserIdFromSession(request);
				
		JSONArray idsArray = JSON.parseArray(ids);
		List<Long> groupIds = new ArrayList<Long>();
		for(int i=0; i<idsArray.size(); i++){
			Long id = Long.parseLong(idsArray.getString(i));
			groupIds.add(id);
		}		
		groupService.remove(userId, groupIds);
		
		return null;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String members,
			String hallIds,
			String name,
			HttpServletRequest request
			)throws Exception{
		UserVO user = userUtils.getUserFromSession(request);
		
		Date date = new Date();
		String createTime = DateUtil.format(date,DateUtil.dateTimePattern);
		if(name == null ||name.equals("")){
			name = new StringBuilder().append(user.getName())
					.append(" ")
					.append(createTime)
					.append(" ")
					.append("会议")
					.toString();
		}
		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
		List<Long> hallIdArray = JSONArray.parseArray(hallIds, Long.class);
		List<String> bundleIdArray = new ArrayList<String>();
		
		GroupPO group = null;
		try {
			groupService.saveCommand(user.getId(), user.getId(), user.getName(),name,name,BusinessType.MEETING_QT, OriginType.INNER, userIdArray,hallIdArray,bundleIdArray,null);
		} catch (CommandGroupNameAlreadyExistedException e) {
			// TODO: handle exception
			JSONObject info = new JSONObject();
			info.put("status", "error");
			JSONObject errorInfo = new JSONObject();
			errorInfo.put("type", "");
			errorInfo.put("msg", "名称已被使用");
			errorInfo.put("recommendedName", e.getRecommendedName());
			info.put("errorInfo", errorInfo);
			return info;
			
		}

		JSONObject info = new JSONObject();
		info.put("id",group.getId().toString());
		info.put("name", group.getName());
		info.put("status", group.getStatus().getCode());
		info.put("commander", group.getUserId());
		info.put("creator", group.getUserId());
		Object membersArray = treeUtils.queryGroupTree(group.getId());
		info.put("members", membersArray);
		
		return info;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/organization")
	public Object queryOrganization(Long id) throws Exception{
		return groupQuery.queryOrganization(id);
		
	}
}
