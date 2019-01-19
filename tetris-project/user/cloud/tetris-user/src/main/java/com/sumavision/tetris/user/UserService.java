package com.sumavision.tetris.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.system.role.UserSystemRolePermissionDAO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.user.exception.PasswordCannotBeNullException;
import com.sumavision.tetris.user.exception.PasswordErrorException;
import com.sumavision.tetris.user.exception.RepeatNotMatchPasswordException;
import com.sumavision.tetris.user.exception.UserNotExistException;
import com.sumavision.tetris.user.exception.UsernameCannotBeNullException;

/**
 * 用户操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月19日 下午3:51:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private UserSystemRolePermissionDAO userSystemRolePermissionDao;
	
	/**
	 * 添加一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午3:51:08
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 确认密码
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @return UserVO 新建的用户
	 */
	public UserVO add(
			String nickname,
            String username,
            String password,
            String repeat,
            String mobile,
            String mail) throws Exception{
		
		if(username == null) throw new UsernameCannotBeNullException();
		
		if(nickname == null) nickname = username;
		
		if(password == null) throw new PasswordCannotBeNullException();
		
		if(!password.equals(repeat)) throw new RepeatNotMatchPasswordException();
		
		UserPO user = new UserPO();
		user.setNickname(nickname);
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setMail(mail);
		user.setStatus(UserStatus.OFFLINE);
		user.setAutoGeneration(false);
		user.setUpdateTime(new Date());
		userDao.save(user);
		
		return new UserVO().set(user);
	}
	
	/**
	 * 删除一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:37:10
	 * @param Long id 用户id
	 */
	public void delete(Long id) throws Exception{
		
		UserPO user = userDao.findOne(id);
		
		if(user != null) userDao.delete(user);
		
		List<UserSystemRolePermissionPO> permissions = userSystemRolePermissionDao.findByUserId(id);
		
		userSystemRolePermissionDao.deleteInBatch(permissions);
		
	}
	
	/**
	 * 修改一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:51:17
	 * @param Long id 用户id
	 * @param String nickname 用户昵称
	 * @param String mobile 用户手机号
	 * @param String mail 用户邮箱
	 * @param boolean editPassword 是否修改密码
	 * @param String oldPassword 旧密码
	 * @param String newPassword 新密码
	 * @param String repeat 新密码确认
	 * @return UserVO 修改后的用户
	 */
	public UserVO edit(
			Long id, 
			String nickname,
            String mobile,
            String mail,
            boolean editPassword,
            String oldPassword,
            String newPassword,
            String repeat) throws Exception{
		
		UserPO user = userDao.findOne(id);
		
		if(user == null) throw new UserNotExistException(id);
		
		if(editPassword){
			if(!user.getPassword().equals(oldPassword)) throw new PasswordErrorException();
			
			if(newPassword == null) throw new PasswordCannotBeNullException();
			
			if(!newPassword.equals(repeat)) throw new RepeatNotMatchPasswordException();
			
			user.setPassword(newPassword);
		}
		
		user.setNickname(nickname);
		user.setMobile(mobile);
		user.setMail(mail);
		user.setUpdateTime(new Date());
		userDao.save(user);
		
		return new UserVO().set(user);
	}
	
}
