package com.sumavision.tetris.subordinate.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 隶属角色对应用户操作<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月30日 上午11:34:06
 */
@Component
public class UserSubordinateRolePermissionQuery {
	@Autowired
	private UserSubordinateRolePermissionDAO userSubordinateRolePermissionDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	public Map<String, Object> getUserFromRole(Long roleId, int currentPage, int pageSize) throws Exception{
		int total = userSubordinateRolePermissionDAO.countByRoleId(roleId);
		List<UserPO> users = findByRoleId(roleId, currentPage, pageSize);
		List<UserVO> view_users = new ArrayList<UserVO>();
		if(users!=null && users.size()>0){
			for(UserPO user:users){
				view_users.add(new UserVO().set(user));
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", view_users)
												   .getMap();
	}
	
	public List<UserPO> findByRoleId(Long roleId, int currentPage, int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<UserPO> users = userDAO.findByRoleId(roleId, page);
		return users.getContent();
	}
	
	public Map<String, Object> getUserByCompanyWithExcept(Long userId, List<Long> exceptIds, Long companyId, int currentPage, int pageSize) throws Exception {
		exceptIds.add(userId);
		return userQuery.listByCompanyIdWithExcept(companyId, exceptIds, currentPage, pageSize);
	}
}
