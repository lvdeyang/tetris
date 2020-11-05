package com.sumavision.tetris.zoom.contacts.api.android;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.zoom.contacts.ContactsMessageQuery;
import com.sumavision.tetris.zoom.contacts.ContactsMessageService;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/zoom/android/contacts/message")
public class ApiZoomAndroidContactsMessageController {

	@Autowired
	private ContactsMessageService contactsMessageService;
	
	@Autowired
	private ContactsMessageQuery contactsMessageQuery;
	
	/**
	 * 查询联系人所有消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午2:53:45
	 * @param Long contactsId 联系人id
	 * @return List<ContactsMessageVO> 消息列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/contacts")
	public Object queryByContacts(
			Long contactsId, 
			HttpServletRequest request) throws Exception{
		
		return contactsMessageQuery.queryByContacts(contactsId);
	}
	
	/**
	 * 联系人发送文本消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午12:01:11
	 * @param Long targetContactsId 目标联系人id
	 * @param String text 文本内容
	 * @return ContactsMessageVO 消息对象
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/send/text")
	public Object sendText(
			Long targetContactsId, 
			String text,
			HttpServletRequest request) throws Exception{
		
		return contactsMessageService.sendText(targetContactsId, text);
	}
	
}
