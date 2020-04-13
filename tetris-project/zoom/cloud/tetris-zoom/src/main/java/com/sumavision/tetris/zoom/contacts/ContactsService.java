package com.sumavision.tetris.zoom.contacts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.zoom.SourceGroupDAO;
import com.sumavision.tetris.zoom.SourceGroupPO;
import com.sumavision.tetris.zoom.contacts.exception.ContactsNotFoundException;
import com.sumavision.tetris.zoom.exception.SourceGroupNotFoundException;

@Service
public class ContactsService {

	@Autowired
	private ContactsDAO contactsDao;
	
	@Autowired
	private SourceGroupDAO sourceGroupDao;
	
	@Autowired
	private UserQuery userQuery;
	
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
	public ContactsVO add(
			Long userId,
			String rename,
			Long sourceGroupId) throws Exception{
		
		UserVO user = userQuery.current();
		
		SourceGroupPO sourceGroup = null;
		if(sourceGroupId != null){
			sourceGroup = sourceGroupDao.findOne(sourceGroupId);
			if(sourceGroup == null){
				throw new SourceGroupNotFoundException(sourceGroupId);
			}
		}
		
		List<UserVO> contactsUsers = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(userId).getList());
		
		if(contactsUsers!=null && contactsUsers.size()>0){
			UserVO contactsUser = contactsUsers.get(0);
			ContactsPO contacts = new ContactsPO();
			contacts.setUserId(user.getId().toString());
			contacts.setContactsUserId(contactsUser.getId().toString());
			contacts.setContactsUserNickname(contactsUser.getNickname());
			contacts.setRename(rename);
			contacts.setSourceGroupId(sourceGroupId);
			contactsDao.save(contacts);
			return new ContactsVO().set(contacts);
		}
		
		return null;
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
	public ContactsVO rename(
			Long id,
			String rename) throws Exception{
		
		ContactsPO entity = contactsDao.findOne(id);
		if(entity == null){
			throw new ContactsNotFoundException(id);
		}
		
		entity.setRename(rename);
		contactsDao.save(entity);
		
		return new ContactsVO().set(entity);
	}
	
	/**
	 * 删除联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午4:41:40
	 * @param Long id 联系人id
	 */
	public void remove(Long id) throws Exception{
		ContactsPO entity = contactsDao.findOne(id);
		if(entity != null){
			contactsDao.save(entity);
		}
	}
	
}
