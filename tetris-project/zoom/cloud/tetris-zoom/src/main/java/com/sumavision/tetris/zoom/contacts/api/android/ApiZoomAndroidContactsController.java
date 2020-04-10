package com.sumavision.tetris.zoom.contacts.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.contacts.ContactsQuery;
import com.sumavision.tetris.zoom.contacts.ContactsService;

@Controller
@RequestMapping(value = "/api/zoom/android/contacts")
public class ApiZoomAndroidContactsController {

	@Autowired
	private ContactsService contactsService;
	
	@Autowired
	private ContactsQuery contactsQuery;
	
	/**
	 * 分组查询用户的联系人列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午10:02:45
	 * @return List<ContactsGroupVO> 联系人组列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list() throws Exception{
		
		return contactsQuery.list();
	}
	
	/**
	 * 查询联系人分组以及无分组的联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:47:05
	 * @return groups List<ContactsGroupVO> 联系人分组列表
	 * @return contacts List<ContactsVO> 联系人列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/groups/and/free/contacts")
	public Object findGroupsAndFreeContacts() throws Exception{
		
		return contactsQuery.findGroupsAndFreeContacts();
	}
	
	/**
	 * 查询分组下的联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:57:29
	 * @param Long sourceGroupId 分组id
	 * @return List<ContactsVO> 联系人列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/source/group/id")
	public Object findBySourceGroupId(Long sourceGroupId) throws Exception{
		
		return contactsQuery.findBySourceGroupId(sourceGroupId);
	}
	
	/**
	 * 添加联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午4:06:35
	 * @param Long userId 用户id
	 * @param String rename 重命名
	 * @param Long sourceGroupId 联系人分组
	 * @return ContactsVO 联系人信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long userId,
			String rename,
			Long sourceGroupId,
			HttpServletRequest request) throws Exception{
		
		return contactsService.add(userId, rename, sourceGroupId);
	}
	
	/**
	 * 联系人重命名<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午4:18:42
	 * @param Long id 联系人id
	 * @param String rename 重命名
	 * @return ContactsVO 联系人信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/rename")
	public Object rename(
			Long id,
			String rename,
			HttpServletRequest request) throws Exception{
		
		return contactsService.rename(id, rename);
	}
	
	/**
	 * 删除联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午4:41:40
	 * @param Long id 联系人id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			HttpServletRequest request) throws Exception{
		
		contactsService.remove(id);
		return null;
	}
	
}
