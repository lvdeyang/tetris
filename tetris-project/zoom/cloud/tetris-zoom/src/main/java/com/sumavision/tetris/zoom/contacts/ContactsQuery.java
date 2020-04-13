package com.sumavision.tetris.zoom.contacts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserQuery;
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
		Set<Long> groupIds = new HashSet<Long>();
		for(int i=0; i<entities.size(); i++){
			ContactsPO entity = entities.get(i);
			if(entity.getSourceGroupId() != null){
				groupIds.add(entity.getSourceGroupId());
			}else{
				defaultGroup.getContacts().add(new ContactsVO().set(entity));
			}
		}
		
		if(groupIds.size() > 0){
			List<SourceGroupPO> groupEntities = sourceGroupDao.findByIdInOrderByNameAsc(groupIds);
			for(int i=0; i<groupEntities.size(); i++){
				ContactsGroupVO contactsGroup = new ContactsGroupVO().set(groupEntities.get(i));
				groups.add(contactsGroup);
				for(int j=0; j<entities.size(); j++){
					if(contactsGroup.getId().equals(entities.get(j).getSourceGroupId())){
						contactsGroup.getContacts().add(new ContactsVO().set(entities.get(j)));
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
		
		List<ContactsVO> contacts = new ArrayList<ContactsVO>();
		List<ContactsPO> contactsEntities = contactsDao.findByUserIdAndSourceGroupIdIsNull(user.getId().toString());
		if(contactsEntities!=null && contactsEntities.size()>0){
			for(int i=0; i<contactsEntities.size(); i++){
				contacts.add(new ContactsVO().set(contactsEntities.get(i)));
			}
		}
		
		List<ContactsGroupVO> groups = new ArrayList<ContactsGroupVO>();
		List<SourceGroupPO> groupEntities = sourceGroupDao.findByUserIdAndTypeOrderByNameAsc(user.getId().toString(), SourceGroupType.CONTACTS);
		if(groupEntities!=null && groupEntities.size()>0){
			for(int i=0; i<groupEntities.size(); i++){
				groups.add(new ContactsGroupVO().set(groupEntities.get(i)));
			}
		}
		
		return new HashMapWrapper<String, Object>().put("groups", groups)
												   .put("contacts", contacts)
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
		return ContactsVO.getConverter(ContactsVO.class).convert(entities, ContactsVO.class);
	}
	
}
