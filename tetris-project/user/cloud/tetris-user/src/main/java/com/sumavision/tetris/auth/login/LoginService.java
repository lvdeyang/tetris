package com.sumavision.tetris.auth.login;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Sha256Encoder;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserStatus;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.exception.PasswordCannotBeNullException;
import com.sumavision.tetris.user.exception.PasswordErrorException;
import com.sumavision.tetris.user.exception.UsernameCannotBeNullException;
import com.sumavision.tetris.user.exception.UsernameNotExistException;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginService {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private Sha256Encoder sha256Encoder;
	
	/**
	 * 用户名密码登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:08
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String verifyCode 验证码
	 * @return String token
	 */
	public String doPasswordLogin(
			String username,
			String password,
			String verifyCode) throws Exception{
		
		if(username == null) throw new UsernameCannotBeNullException();
		
		if(password == null) throw new PasswordCannotBeNullException();
		
		UserPO user = userDao.findByUsername(username);
		
		if(user == null) throw new UsernameNotExistException(username);
		
		password = sha256Encoder.encode(password);
		
		if(!user.getPassword().equals(password)) throw new PasswordErrorException(username, password);
		
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		
		user.setStatus(UserStatus.ONLINE);
		user.setLastModifyTime(new Date());
		user.setToken(token);
		userDao.save(user);
		
		return token;
	}
	
	/**
	 * 用户注销登录<br/>
	 * <p>这个接口暂时不清理缓存</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月8日 上午10:38:16
	 */
	public void doLogout() throws Exception{
		UserVO user = userQuery.current();
		UserPO userEntity = userDao.findOne(Long.valueOf(user.getUuid()));
		userEntity.setToken(null);
		userEntity.setLastModifyTime(null);
		userEntity.setStatus(UserStatus.OFFLINE);
		userDao.save(userEntity);
	}
	
}
