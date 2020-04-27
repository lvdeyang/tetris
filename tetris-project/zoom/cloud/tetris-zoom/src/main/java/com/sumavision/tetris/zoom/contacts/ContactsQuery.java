package com.sumavision.tetris.zoom.contacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.auth.token.TokenVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserStatus;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.zoom.SourceGroupDAO;
import com.sumavision.tetris.zoom.SourceGroupPO;
import com.sumavision.tetris.zoom.SourceGroupType;

@Component
public class ContactsQuery {

	@Autowired
	private ContactsDAO contactsDao;
	
	@Autowired
	private SourceGroupDAO sourceGroupDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TokenQuery tokenQuery;
	
	/**
	 * 查询用户联系人列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午10:00:35
	 * @return List<ContactsGroupVO> 联系人组列表
	 */
	public List<ContactsGroupVO> list() throws Exception{
		
		UserVO user = userQuery.current();
		
		List<ContactsGroupVO> groups = new ArrayList<ContactsGroupVO>();
		ContactsGroupVO defaultGroup = ContactsGroupVO.defaultGroup();
		groups.add(defaultGroup);
		List<ContactsPO> entities = contactsDao.findByUserIdOrderByRenameAsc(user.getId().toString());
		if(entities==null || entities.size()<=0) return groups;
		List<Long> userIds = new ArrayList<Long>();
		for(ContactsPO entity:entities){
			userIds.add(Long.valueOf(entity.getUserId()));
		}
		
		List<TokenVO> tokens = new ArrayList<TokenVO>();
		List<TokenVO> androidTokens = tokenQuery.findByUserIdInAndType(userIds, TerminalType.ZOOM_ANDROID);
		List<TokenVO> qtTokens = tokenQuery.findByUserIdInAndType(userIds, TerminalType.ZOOM_QT);
		if(androidTokens!=null && androidTokens.size()>0) tokens.addAll(androidTokens);
		if(qtTokens!=null && qtTokens.size()>0) tokens.addAll(qtTokens);
		
		Set<Long> groupIds = new HashSet<Long>();
		for(int i=0; i<entities.size(); i++){
			ContactsPO entity = entities.get(i);
			if(entity.getSourceGroupId() != null){
				groupIds.add(entity.getSourceGroupId());
			}else{
				ContactsVO contacts = new ContactsVO().set(entity);
				defaultGroup.getContacts().add(contacts);
				String status = UserStatus.OFFLINE.toString();
				List<String> onlineTerminalTypes = new ArrayList<String>();
				if(tokens!=null && tokens.size()>0){
					for(TokenVO token:tokens){
						if(contacts.getUserId().equals(token.getId().toString())){
							if(UserStatus.ONLINE.toString().equals(token.getStatus())){
								status = UserStatus.ONLINE.toString();
								onlineTerminalTypes.add(token.getType());
							}
						}
					}
				}
				contacts.setStatus(status);
				contacts.setOnlineTerminalTypes(onlineTerminalTypes);
			}
		}
		
		if(groupIds.size() > 0){
			List<SourceGroupPO> groupEntities = sourceGroupDao.findByIdInOrderByNameAsc(groupIds);
			for(int i=0; i<groupEntities.size(); i++){
				ContactsGroupVO contactsGroup = new ContactsGroupVO().set(groupEntities.get(i));
				groups.add(contactsGroup);
				for(int j=0; j<entities.size(); j++){
					if(contactsGroup.getId().equals(entities.get(j).getSourceGroupId())){
						ContactsVO contacts = new ContactsVO().set(entities.get(j));
						contactsGroup.getContacts().add(contacts);
						String status = UserStatus.OFFLINE.toString();
						List<String> onlineTerminalTypes = new ArrayList<String>();
						if(tokens!=null && tokens.size()>0){
							for(TokenVO token:tokens){
								if(contacts.getUserId().equals(token.getId().toString())){
									if(UserStatus.ONLINE.toString().equals(token.getStatus())){
										status = UserStatus.ONLINE.toString();
										onlineTerminalTypes.add(token.getType());
									}
								}
							}
						}
						contacts.setStatus(status);
						contacts.setOnlineTerminalTypes(onlineTerminalTypes);
					}
				}
			}
		}
		
		return groups;
	}
	
	/**
	 * 查询联系人分组以及无分组的联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:47:05
	 * @return groups List<ContactsGroupVO> 联系人分组列表
	 * @return contacts List<ContactsVO> 联系人列表
	 */
	public Map<String, Object> findGroupsAndFreeContacts() throws Exception{
		UserVO user = userQuery.current();
		
		List<ContactsPO> totalEntities = contactsDao.findByUserIdOrderByRenameAsc(user.getId().toString());
		List<ContactsPO> groupedEntities = new ArrayList<ContactsPO>();
		List<ContactsVO> freeContacts = new ArrayList<ContactsVO>();
		Set<Long> userIds = new HashSet<Long>();
		if(totalEntities!=null && totalEntities.size()>0){
			for(int i=0; i<totalEntities.size(); i++){
				ContactsPO entity = totalEntities.get(i);
				if(entity.getSourceGroupId() != null){
					groupedEntities.add(entity);
				}else{
					freeContacts.add(new ContactsVO().set(entity));
				}
				userIds.add(Long.valueOf(entity.getContactsUserId()));
			}
		}
		
		List<TokenVO> tokens = new ArrayList<TokenVO>();
		List<TokenVO> androidTokens = tokenQuery.findByUserIdInAndType(userIds, TerminalType.ZOOM_ANDROID);
		List<TokenVO> qtTokens = tokenQuery.findByUserIdInAndType(userIds, TerminalType.ZOOM_QT);
		if(androidTokens!=null && androidTokens.size()>0) tokens.addAll(androidTokens);
		if(qtTokens!=null && qtTokens.size()>0) tokens.addAll(qtTokens);
		
		if(freeContacts.size() > 0){
			for(ContactsVO contacts:freeContacts){
				String status = UserStatus.OFFLINE.toString();
				List<String> onlineTerminalTypes = new ArrayList<String>();
				if(tokens!=null && tokens.size()>0){
					for(TokenVO token:tokens){
						if(contacts.getUserId().equals(token.getId().toString())){
							if(UserStatus.ONLINE.toString().equals(token.getStatus())){
								status = UserStatus.ONLINE.toString();
								onlineTerminalTypes.add(token.getType());
							}
						}
					}
				}
				contacts.setStatus(status);
				contacts.setOnlineTerminalTypes(onlineTerminalTypes);
			}
		}
		
		List<ContactsGroupVO> groups = new ArrayList<ContactsGroupVO>();
		List<SourceGroupPO> groupEntities = sourceGroupDao.findByUserIdAndTypeOrderByNameAsc(user.getId().toString(), SourceGroupType.CONTACTS);
		if(groupEntities!=null && groupEntities.size()>0){
			for(int i=0; i<groupEntities.size(); i++){
				ContactsGroupVO group = new ContactsGroupVO().set(groupEntities.get(i));
				groups.add(group);
				int countOnline = 0;
				int countOffline = 0;
				int countTotal = 0;
				if(groupedEntities.size() > 0){
					for(ContactsPO groupedEntity:groupedEntities){
						if(group.getId().equals(groupedEntity.getSourceGroupId())){
							countTotal += 1;
							TokenVO theToken = null;
							if(tokens!=null && tokens.size()>0){
								for(TokenVO token:tokens){
									if(groupedEntity.getContactsUserId().equals(token.getId().toString())){
										theToken = token;
										break;
									}
								}
							}
							if(theToken == null){
								countOffline += 1;
							}else if(UserStatus.ONLINE.toString().equals(theToken.getStatus())){
								countOnline += 1;
							}else{
								countOffline += 1;
							}
						}
					}
				}
				group.setCountOffline(countOffline);
				group.setCountOnline(countOnline);
				group.setCountTotal(countTotal);
			}
		}
		
		return new HashMapWrapper<String, Object>().put("groups", groups)
												   .put("contacts", freeContacts)
												   .getMap();
	}
	
	/**
	 * 查询分组下的联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:57:29
	 * @param Long sourceGroupId 分组id
	 * @return List<ContactsVO> 联系人列表
	 */
	public List<ContactsVO> findBySourceGroupId(Long sourceGroupId) throws Exception{
		List<ContactsPO> entities = contactsDao.findBySourceGroupId(sourceGroupId);
		List<ContactsVO> contacts = ContactsVO.getConverter(ContactsVO.class).convert(entities, ContactsVO.class);
		List<Long> userIds = new ArrayList<Long>();
		if(contacts!=null && contacts.size()>0){
			for(ContactsVO contact:contacts){
				userIds.add(Long.valueOf(contact.getUserId()));
			}
		}
		
		List<TokenVO> tokens = new ArrayList<TokenVO>();
		List<TokenVO> androidTokens = tokenQuery.findByUserIdInAndType(userIds, TerminalType.ZOOM_ANDROID);
		List<TokenVO> qtTokens = tokenQuery.findByUserIdInAndType(userIds, TerminalType.ZOOM_QT);
		if(androidTokens!=null && androidTokens.size()>0) tokens.addAll(androidTokens);
		if(qtTokens!=null && qtTokens.size()>0) tokens.addAll(qtTokens);
		
		if(contacts!=null && contacts.size()>0){
			for(ContactsVO contact:contacts){
				String status = UserStatus.OFFLINE.toString();
				List<String> onlineTerminalTypes = new ArrayList<String>();
				if(tokens!=null && tokens.size()>0){
					for(TokenVO token:tokens){
						if(contact.getUserId().equals(token.getId().toString())){
							if(UserStatus.ONLINE.toString().equals(token.getStatus())){
								status = UserStatus.ONLINE.toString();
								onlineTerminalTypes.add(token.getType());
							}
						}
					}
				}
				contact.setStatus(status);
				contact.setOnlineTerminalTypes(onlineTerminalTypes);
			}
		}
		
		return contacts;
	}
	
}
