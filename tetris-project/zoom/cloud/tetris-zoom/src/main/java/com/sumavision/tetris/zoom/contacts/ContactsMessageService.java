package com.sumavision.tetris.zoom.contacts;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;
import com.sumavision.tetris.zoom.contacts.exception.ContactsNotFoundException;

@Service
public class ContactsMessageService {
	
	@Autowired
	private ContactsDAO contactsDao;
	
	@Autowired
	private ContactsMessageDAO contactsMessageDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private UserQuery userQuery;

	/**
	 * 联系人发送文本消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午12:01:11
	 * @param Long targetContactsId 目标联系人id
	 * @param String text 文本内容
	 * @return ContactsMessageVO 消息对象
	 */
	public ContactsMessageVO sendText(
			Long targetContactsId, 
			String text) throws Exception{
		
		UserVO user = userQuery.current();
		
		ContactsPO contacts = contactsDao.findOne(targetContactsId);
		if(contacts == null){
			throw new ContactsNotFoundException(targetContactsId);
		}
		
		ContactsMessagePO message = new ContactsMessagePO();
		message.setFromUserId(user.getId().toString());
		message.setFromUserNickname(user.getNickname());
		message.setMessage(text);
		message.setToUserId(contacts.getContactsUserId());
		message.setToUserNickname(contacts.getContactsUserNickname());
		message.setType(ContactsMessageType.TEXT);
		message.setUpdateTime(new Date());
		contactsMessageDao.save(message);
		
		ContactsMessageVO content = new ContactsMessageVO();
		content.setId(message.getId());
		content.setUuid(message.getUuid());
		content.setUpdateTime(message.getUpdateTime());
		content.setMessage(message.getMessage());
		content.setType(message.getType().toString());
		content.setFromUserId(message.getFromUserId());
		//消息发送者在消息目标的联系人
		ContactsPO contacts1 = contactsDao.findByUserIdAndContactsUserId(message.getToUserId(), message.getFromUserId());
		content.setFromUsername(contacts1==null?message.getFromUserNickname():(contacts1.getRename()==null?contacts1.getContactsUserNickname():contacts1.getRename()));
		content.setFromContactsId(contacts1==null?user.getId():contacts1.getId());
		content.setFromSourceGroupId(contacts1!=null?contacts1.getSourceGroupId():null);
		
		websocketMessageService.push(message.getToUserId(), "contactsTextMessage", JSON.parseObject(JSON.toJSONString(content)), message.getFromUserId(), message.getFromUserNickname());
	
		content.setFromUserId(null);
		content.setFromUsername(null);
		content.setFromContactsId(null);
		
		return content;
	}
	
}
