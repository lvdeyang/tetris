package com.sumavision.tetris.auth.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.auth.login.exception.AppIdCannotBeNullException;
import com.sumavision.tetris.auth.login.exception.DonotSupportRoamLoginException;
import com.sumavision.tetris.auth.login.exception.SignCannotBeNullException;
import com.sumavision.tetris.auth.login.exception.SignVerifyFailException;
import com.sumavision.tetris.auth.login.exception.TimestampCannotBeNullException;
import com.sumavision.tetris.auth.login.exception.TooManyAbnormalLoginTimesException;
import com.sumavision.tetris.auth.login.exception.UnknownAppIdException;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenDAO;
import com.sumavision.tetris.auth.token.TokenPO;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Base64;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Sha256Encoder;
import com.sumavision.tetris.user.BasicDevelopmentDAO;
import com.sumavision.tetris.user.BasicDevelopmentPO;
import com.sumavision.tetris.user.BasicDevelopmentQuery;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserDAO;
import com.sumavision.tetris.user.UserPO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserStatus;
import com.sumavision.tetris.user.UserVO;
import com.sumavision.tetris.user.exception.PasswordErrorException;
import com.sumavision.tetris.user.exception.TokenTimeoutException;
import com.sumavision.tetris.user.exception.UserIpNotAllowLoginException;
import com.sumavision.tetris.user.exception.UsernameCannotBeNullException;
import com.sumavision.tetris.user.exception.UsernameNotExistException;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

@Service
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
	private Base64 base64;
	
	@Autowired
	private BasicDevelopmentQuery basicDevelopmentQuery;
	
	@Autowired
	private BasicDevelopmentDAO basicDevelopmentDao;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	/**
	 * 强制用户id登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:08
	 * @param Long userId 用户名id
	 * @return String token
	 */
	@Transactional(rollbackFor = Exception.class)
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
		
		if(UserClassify.LDAP.equals(user.getClassify())){
			throw new DonotSupportRoamLoginException();
		}
		
		if(user.getErrorLoginTimes()!=null && user.getErrorLoginTimes().intValue()>=10){
			throw new TooManyAbnormalLoginTimesException();
		} 
		if(!TerminalType.ANDROID_TVOS.equals(terminalType)){
			for(int i=0; i<5; i++){
				password = base64.decode(password);
			}
		}
		password = sha256Encoder.encode(password);
		if(!user.getPassword().equals(password)){
			user.setErrorLoginTimes((user.getErrorLoginTimes()==null?1:(user.getErrorLoginTimes()+1)));
			userDao.saveAndFlush(user);
			throw new PasswordErrorException(username, password);
		} 
		if(TerminalType.QT_ZK.equals(terminalType)){
			//指控终端重复登录校验
			TokenPO token = tokenDao.findByUserIdAndType(user.getId(), terminalType);
			if(token!=null && UserStatus.ONLINE.equals(token.getStatus())){
				//重复登录踢人下线
				websocketMessageService.push(user.getId().toString(), "forceOffLine", null, user.getId().toString(), user.getNickname());
			}
		}
		return doPasswordLoginTransactional(user, terminalType, ip);
	}
	
	/**
	 * 用户名密码登录-支持多终端登录<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月15日 下午1:44:24
	 *@param String username 用户名
	 * @param String password 密码
	 * @param String ip 登录ip
	 * @param TerminalType terminalType 终端类型
	 * @param String verifyCode 验证码
	 * @param String loginIp 登录地址ip
	 * @return String token
	 */
	public String doPasswordLogin(
			String username,
			String password,
			String ip,
			TerminalType terminalType,
			String verifyCode,
			String loginIp) throws Exception{
		
		if(username==null || "".equals(username)) throw new UsernameCannotBeNullException();
		
		UserPO user = userDao.findByUsername(username);
		if(user == null) throw new UsernameNotExistException(username);
		if(user.getIp() != null && user.getIp().equals("")){
			if(! loginIp.equals(user.getIp())) throw new UserIpNotAllowLoginException(loginIp);
		}
		if(UserClassify.LDAP.equals(user.getClassify())){
			throw new DonotSupportRoamLoginException();
		}
		
		if(user.getErrorLoginTimes()!=null && user.getErrorLoginTimes().intValue()>=10){
			throw new TooManyAbnormalLoginTimesException();
		} 
		if(!TerminalType.ANDROID_TVOS.equals(terminalType)){
			for(int i=0; i<5; i++){
				password = base64.decode(password);
			}
		}
		password = sha256Encoder.encode(password);
		if(!user.getPassword().equals(password)){
			user.setErrorLoginTimes((user.getErrorLoginTimes()==null?1:(user.getErrorLoginTimes()+1)));
			userDao.saveAndFlush(user);
			throw new PasswordErrorException(username, password);
		} 
		if(TerminalType.QT_ZK.equals(terminalType)){
			//指控终端重复登录校验
			TokenPO token = tokenDao.findByUserIdAndType(user.getId(), terminalType);
			if(token!=null && UserStatus.ONLINE.equals(token.getStatus())){
				//重复登录踢人下线
				websocketMessageService.push(user.getId().toString(), "forceOffLine", null, user.getId().toString(), user.getNickname());
			}
		}
		return doPasswordLoginTransactional(user, terminalType, ip);
	}
	
	/**
	 * 处理用户名密码登录事务操作-支持多终端登录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:13:08
	 * @param UserPO user 用户
	 * @param TerminalType terminalType 终端类型
	 * @param String ip 登录ip
	 * @return String token
	 */
	@Transactional(rollbackFor = Exception.class)
	private String doPasswordLoginTransactional(UserPO user, TerminalType terminalType, String ip) throws Exception{
		user.setErrorLoginTimes(0);
		userDao.save(user);
		
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
	@Transactional(rollbackFor = Exception.class)
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
		String serverSign = basicDevelopmentQuery.sign(appId, timestamp, basicDevelopment.decodeAppSecret());
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
	@Transactional(rollbackFor = Exception.class)
	public void doLogout() throws Exception{
		UserVO user = userQuery.current();
		TokenPO token = tokenDao.findByToken(user.getToken());
		token.setToken(null);
		token.setLastModifyTime(null);
		token.setStatus(UserStatus.OFFLINE);
		tokenDao.save(token);
	}
	
	public String loginIp(HttpServletRequest request) throws Exception{
		String ip = request.getHeader("x-forwarded-for");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    
	    return ip;
	}
	
}
