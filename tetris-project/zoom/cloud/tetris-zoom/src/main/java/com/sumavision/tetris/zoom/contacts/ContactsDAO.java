package com.sumavision.tetris.zoom.contacts;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ContactsPO.class, idClass = Long.class)
public interface ContactsDAO extends BaseDAO<ContactsPO>{

	/**
	 * 查询联系人组下的联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午3:50:04
	 * @param Long sourceGroupId 组id
	 * @return List<ContactsPO> 联系人列表
	 */
	public List<ContactsPO> findBySourceGroupId(Long sourceGroupId);
	
	/**
	 * 查询用户的所有联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午9:38:35
	 * @param Long userId 用户id
	 * @return List<ContactsPO> 联系人列表
	 */
	public List<ContactsPO> findByUserIdOrderByRenameAsc(String userId);
	
	/**
	 * 查询用户联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 上午11:44:40
	 * @param String userId 用户id
	 * @param String contactsUserId 待查询用户id
	 * @return ContactsPO 联系人信息
	 */
	public ContactsPO findByUserIdAndContactsUserId(String userId, String contactsUserId);
	
	/**
	 * 查询用户没有分组的联系人<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月8日 下午3:33:11
	 * @param String userId 用户id
	 * @return List<ContactsPO> 联系人列表
	 */
	public List<ContactsPO> findByUserIdAndSourceGroupIdIsNull(String userId);
	
}
