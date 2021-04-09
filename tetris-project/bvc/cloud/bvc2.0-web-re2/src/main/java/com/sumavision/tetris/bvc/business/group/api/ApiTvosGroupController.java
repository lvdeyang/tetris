//package com.sumavision.tetris.bvc.business.group.api;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.sumavision.bvc.control.utils.TreeUtils;
//import com.sumavision.bvc.control.utils.UserUtils;
//import com.sumavision.bvc.control.welcome.UserVO;
//import com.sumavision.bvc.device.command.exception.CommandGroupNameAlreadyExistedException;
//import com.sumavision.tetris.bvc.business.OriginType;
//import com.sumavision.tetris.bvc.business.group.BusinessType;
//import com.sumavision.tetris.bvc.business.group.GroupPO;
//import com.sumavision.tetris.bvc.business.group.GroupQuery;
//import com.sumavision.tetris.bvc.business.group.GroupService;
//import com.sumavision.tetris.commons.util.date.DateUtil;
//import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
//import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;
//
//@Controller
//@RequestMapping(value = "/api/tvos/group")
//public class ApiTvosGroupController {
//	
//	@Autowired
//	private GroupQuery groupQuery;
//	
//	@Autowired
//	private GroupService groupService;
//	
//	@Autowired
//	private UserUtils userUtils;
//	
//	@Autowired	
//	private TreeUtils treeUtils;
//	
//	/**
//	 * 获取会议列表<br/>
//	 * <b>作者:</b>lqxuhv<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月14日 上午11:50:51
//	 * @return
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/query/tvos/group/list")
//	public Object queryTvosGroupList(HttpServletRequest request) throws Exception{
//		return groupQuery.queryTvosGroupList();
//	}
//
//	/**
//	 * 开始会议<br/>
//	 * <b>作者:</b>lqxuhv<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月14日 上午11:53:37
//	 * @param String id 会议id
//	 * @return
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/start")
//	public Object start(HttpServletRequest request)throws Exception{
//		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
//		String id = requestWrapper.getString("id");
//		Object result = groupService.start(Long.parseLong(id), -1);
//		return result;	
//	}
//	
//	/**
//	 * 
//	 * 停止会议<br/>
//	 * <b>作者:</b>lqxuhv<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月14日 上午11:52:39
//	 * @param String id 会议id
//	 * @return
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/stop")
//	public Object stop(HttpServletRequest request) throws Exception{
//		
//		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
//		String id = requestWrapper.getString("id");
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//		Object chairSplits = groupService.stop(userId, Long.parseLong(id), 0);
//		return chairSplits;
//	}
//	
//	/**
//	 * 删除会议<br/>
//	 * <b>作者:</b>lqxuhv<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月14日 上午11:51:38
//	 * @param JSONString ids 会议id列表
//	 * @return
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/remove")
//	public Object remove(HttpServletRequest request) throws Exception{
//		
//		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
//		String ids = requestWrapper.getString("ids");
//		
//		Long userId = userUtils.getUserIdFromSession(request);
//				
//		JSONArray idsArray = JSON.parseArray(ids);
//		List<Long> groupIds = new ArrayList<Long>();
//		for(int i=0; i<idsArray.size(); i++){
//			Long id = Long.parseLong(idsArray.getString(i));
//			groupIds.add(id);
//		}		
//		groupService.remove(userId, groupIds);
//		
//		return null;
//	}
//	
//	/**
//	 * 创建会议<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月19日 上午11:52:30
//	 * @param JSONString members 用户成员列表
//	 * @param JSONString hallIds 会场成员列表
//	 * @param String name 会议名称
//	 * @return
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/save")
//	public Object save(HttpServletRequest request)throws Exception{
//		
//		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
//		String name = requestWrapper.getString("name");
//		String members = requestWrapper.getString("members");
//		String hallIds = requestWrapper.getString("hallIds");
//		
//		UserVO user = userUtils.getUserFromSession(request);
//		
//		Date date = new Date();
//		String createTime = DateUtil.format(date,DateUtil.dateTimePattern);
//		if(name == null ||name.equals("")){
//			name = new StringBuilder().append(user.getName())
//					.append(" ")
//					.append(createTime)
//					.append(" ")
//					.append("会议")
//					.toString();
//		}
//		List<Long> userIdArray = JSONArray.parseArray(members, Long.class);
//		List<Long> hallIdArray = JSONArray.parseArray(hallIds, Long.class);
//		List<String> bundleIdArray = new ArrayList<String>();
//		
//		GroupPO group = null;
//		try {
//			group = groupService.saveCommand(user.getId(), user.getName(), "tvosOfUser", null, name, name, BusinessType.MEETING_QT, OriginType.INNER, userIdArray, hallIdArray, bundleIdArray, null);
//		} catch (CommandGroupNameAlreadyExistedException e) {
//			// TODO: handle exception
//			JSONObject info = new JSONObject();
//			info.put("status", "error");
//			JSONObject errorInfo = new JSONObject();
//			errorInfo.put("type", "");
//			errorInfo.put("msg", "名称已被使用");
//			errorInfo.put("recommendedName", e.getRecommendedName());
//			info.put("errorInfo", errorInfo);
//			return info;
//			
//		}
//
//		JSONObject info = new JSONObject();
//		info.put("id",group.getId().toString());
//		info.put("name", group.getName());
//		info.put("status", group.getStatus().getCode());
//		info.put("commander", group.getUserId());
//		info.put("creator", group.getUserId());
//		Object membersArray = treeUtils.queryGroupTree(group.getId());
//		info.put("members", membersArray);
//		
//		return info;
//	}
//	
//	/**
//	 * 机顶盒查询组织机构<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年8月19日 上午11:53:37
//	 * @param Long folderId 组织机构id
//	 * @return List<TreeNodeVO> 成员列表
//	 */
//	@JsonBody
//	@ResponseBody
//	@RequestMapping(value = "/query/organization")
//	public Object queryOrganization(HttpServletRequest request) throws Exception{
//		JSONHttpServletRequestWrapper requestWrapper = new JSONHttpServletRequestWrapper(request);
//		return groupQuery.queryOrganization(requestWrapper.getLong("folderId"));
//		
//	}
//}
