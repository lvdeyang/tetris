package com.sumavision.tetris.auth.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.auth.login.exception.AppIdCannotBeNullException;
import com.sumavision.tetris.auth.login.exception.SignCannotBeNullException;
import com.sumavision.tetris.auth.login.exception.SignVerifyFailException;
import com.sumavision.tetris.auth.login.exception.TimestampCannotBeNullException;
import com.sumavision.tetris.auth.login.exception.UnknownAppIdException;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenDAO;
import com.sumavision.tetris.auth.token.TokenPO;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Sha256Encoder;
import com.sumavision.tetris.user.BasicDevelopmentDAO;
import com.sumavision.tetris.user.BasicDevelopmentPO;
import com.sumavision.tetris.user.BasicDevelopmentQuery;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserStatus;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.exception.PasswordErrorException;
import com.sumavision.tetris.user.exception.TokenTimeoutException;
import com.sumavision.tetris.user.exception.UsernameCannotBeNullException;
import com.sumavision.tetris.user.exception.UsernameNotExistException;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginService {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private TokenDAO tokenDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TokenQuery tokenQuery;
	
	@Autowired
	private Sha256Encoder sha256Encoder;
	
	@Autowired
	private BasicDevelopmentQuery basicDevelopmentQuery;
	
	@Autowired
	private BasicDevelopmentDAO basicDevelopmentDao;
	
	/**
	 * 强制用户id登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:08
	 * @param Long userId 用户名id
	 * @return String token
	 */
	public String doUserIdLogin(Long userId) throws Exception{
		TokenPO token = tokenDao.findByUserIdAndType(userId, TerminalType.API);
		if(token == null){
			token = new TokenPO();
			token.newToken();
			token.setUserId(userId);
			token.setType(TerminalType.API);
			tokenDao.save(token);
		}
		return token.getToken();
	}
	
	/**
	 * 用户名密码登录-支持多终端登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:08
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String ip 登录ip
	 * @param TerminalType terminalType 终端类型
	 * @param String verifyCode 验证码
	 * @return String token
	 */
	public String doPasswordLogin(
			String username,
			String password,
			String ip,
			TerminalType terminalType,
			String verifyCode) throws Exception{
		
		if(username==null || "".equals(username)) throw new UsernameCannotBeNullException();
		
		UserPO user = userDao.findByUsername(username);
		if(user == null) throw new UsernameNotExistException(username);
		password = sha256Encoder.encode(password);
		if(!user.getPassword().equals(password)) throw new PasswordErrorException(username, password);
		
		TokenPO token = tokenDao.findByUserIdAndType(user.getId(), terminalType);
		boolean result = false;
		if(token != null){
			try{
				result = tokenQuery.checkToken(token);
			}catch(TokenTimeoutException e){
				result = false;
			}
		}
		if(!result && token!=null){
			token.newToken();
			token.setStatus(UserStatus.ONLINE);
		}else if(!result && token == null){
			token = new TokenPO();
			token.setUserId(user.getId());
			token.setType(terminalType);
			token.newToken();
			token.setStatus(UserStatus.ONLINE);
		}
		token.setIp(ip);
		tokenDao.save(token);
		return token.getToken();
	}
	
	/**
	 * 开发者登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月22日 下午4:30:59
	 * @param String appId 开发者id
	 * @param String timestamp 时间戳
	 * @param String sign 签名
	 * @return String token
	 */
	public String doAppSecretLogin(
			String appId, 
			String timestamp, 
			String sign) throws Exception{
		
		//参数校验
		if(appId == null) throw new AppIdCannotBeNullException();
		if(timestamp == null) throw new TimestampCannotBeNullException();
		if(sign == null) throw new SignCannotBeNullException();
		
		//签名校验
		BasicDevelopmentPO basicDevelopment = basicDevelopmentDao.findByAppId(appId);
		if(basicDevelopment==null) throw new UnknownAppIdException(appId);
		String serverSign = basicDevelopmentQuery.sign(appId, timestamp, basicDevelopment.getAppSecret());
		if(!serverSign.equals(sign)) throw new SignVerifyFailException(appId, timestamp, sign);
		
		TokenPO token = tokenDao.findByUserIdAndType(basicDevelopment.getUserId(), TerminalType.API);
		if(token == null){
			token = new TokenPO();
			token.setUserId(basicDevelopment.getUserId());
			token.setType(TerminalType.API);
			token.newToken();
			tokenDao.save(token);
		}
		
		return token.getToken();
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
		TokenPO token = tokenDao.findByToken(user.getToken());
		token.setToken(null);
		token.setLastModifyTime(null);
		token.setStatus(UserStatus.OFFLINE);
		tokenDao.save(token);
	}
	
}
