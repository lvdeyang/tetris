package com.sumavision.tetris.zoom.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.zoom.contacts.exception.ContactsNotFoundException;

@Component
public class ContactsMessageQuery {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private ContactsDAO contactsDao;
	
	@Autowired
	private ContactsMessageDAO contactsMessageDao;
	
	/**
	 * 查询联系人所有消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午2:53:45
	 * @param Long contactsId 联系人id
	 * @return List<ContactsMessageVO> 消息列表
	 */
	public List<ContactsMessageVO> queryByContacts(Long contactsId) throws Exception{
		UserVO user = userQuery.current();
		ContactsPO contacts = contactsDao.findOne(contactsId);
		if(contacts == null){
			throw new ContactsNotFoundException(contactsId);
		}
		List<ContactsMessageVO> messages = new ArrayList<ContactsMessageVO>();
		List<ContactsMessagePO> entities = findBetweenUsers(user.getId(), Long.valueOf(contacts.getContactsUserId()));
		if(entities!=null && entities.size()>0){
			for(ContactsMessagePO entity:entities){
				ContactsMessageVO message = new ContactsMessageVO();
				message.setId(entity.getId())
				 	   .setUuid(entity.getUuid())
					   .setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
					   .setType(entity.getType().toString())
					   .setMessage(entity.getMessage())
					   .setFromUserId(entity.getFromUserId());
				if(user.getId().toString().equals(message.getFromUserId())){
					message.setFromUsername(user.getNickname());
				}else{
					message.setFromContactsId(contacts.getId())
						   .setFromUsername(contacts.getRename()==null?contacts.getContactsUserNickname():contacts.getRename());
				}
				messages.add(message);
			}
			Collections.sort(messages, new Comparator<ContactsMessageVO>(){
				@Override
				public int compare(ContactsMessageVO o1, ContactsMessageVO o2) {
					return o1.getUpdateTime().compareTo(o2.getUpdateTime());
				}
			});
		}
		return messages;
	}
	
	/**
	 * 查询两个用户间的消息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午1:58:12
	 * @param Long userId0  第一个用户id
	 * @param Long userId1  第二个用户id
	 * @return List<ContactsMessageVO> 消息列表
	 */
	public List<ContactsMessagePO> findBetweenUsers(
			Long userId0, 
			Long userId1) throws Exception{
		List<ContactsMessagePO> totalMessageEntities = new ArrayList<ContactsMessagePO>();
		List<ContactsMessagePO> entities = contactsMessageDao.findByFromUserIdAndToUserId(userId0.toString(), userId1.toString());
		if(entities!=null && entities.size()>0){
			totalMessageEntities.addAll(entities);
		}
		entities = contactsMessageDao.findByFromUserIdAndToUserId(userId1.toString(), userId0.toString());
		if(entities!=null && entities.size()>0){
			totalMessageEntities.addAll(entities);
		}
		return totalMessageEntities;
	}
	
}
