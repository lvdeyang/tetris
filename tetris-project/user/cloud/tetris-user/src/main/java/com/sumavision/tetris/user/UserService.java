package com.sumavision.tetris.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenDAO;
import com.sumavision.tetris.auth.token.TokenPO;
import com.sumavision.tetris.business.role.BusinessRoleService;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Sha256Encoder;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.organization.CompanyDAO;
import com.sumavision.tetris.organization.CompanyPO;
import com.sumavision.tetris.organization.CompanyService;
import com.sumavision.tetris.organization.CompanyUserPermissionDAO;
import com.sumavision.tetris.organization.CompanyUserPermissionPO;
import com.sumavision.tetris.organization.CompanyUserPermissionService;
import com.sumavision.tetris.organization.CompanyVO;
import com.sumavision.tetris.organization.OrganizationDAO;
import com.sumavision.tetris.organization.OrganizationPO;
import com.sumavision.tetris.organization.OrganizationUserPermissionDAO;
import com.sumavision.tetris.organization.OrganizationUserPermissionPO;
import com.sumavision.tetris.organization.exception.CompanyNotExistException;
import com.sumavision.tetris.system.role.SystemRoleDAO;
import com.sumavision.tetris.system.role.SystemRolePO;
import com.sumavision.tetris.system.role.SystemRoleType;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionDAO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionService;
import com.sumavision.tetris.user.event.TouristCreateEvent;
import com.sumavision.tetris.user.event.TouristDeleteBatchEvent;
import com.sumavision.tetris.user.event.TouristDeleteEvent;
import com.sumavision.tetris.user.event.UserDeletedEvent;
import com.sumavision.tetris.user.event.UserImportEventPublisher;
import com.sumavision.tetris.user.event.UserRegisteredEvent;
import com.sumavision.tetris.user.exception.DeletedUserIsNotATouristException;
import com.sumavision.tetris.user.exception.DuplicateUserNumberImportedException;
import com.sumavision.tetris.user.exception.DuplicateUsernameImportedException;
import com.sumavision.tetris.user.exception.MailAlreadyExistException;
import com.sumavision.tetris.user.exception.MobileAlreadyExistException;
import com.sumavision.tetris.user.exception.MobileNotExistException;
import com.sumavision.tetris.user.exception.PasswordCannotBeNullException;
import com.sumavision.tetris.user.exception.PasswordErrorException;
import com.sumavision.tetris.user.exception.RepeatNotMatchPasswordException;
import com.sumavision.tetris.user.exception.UserCannotDeleteBecauseOnlineStatusException;
import com.sumavision.tetris.user.exception.UserNotExistException;
import com.sumavision.tetris.user.exception.UsernameAlreadyExistException;
import com.sumavision.tetris.user.exception.UsernameCannotBeNullException;
import com.sumavision.tetris.user.exception.UsernoCannotBeNullException;

/**
 * 用户操作（主增删改）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月19日 下午3:51:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService{

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserSystemRolePermissionDAO userSystemRolePermissionDao;
	
	@Autowired
	private UserSystemRolePermissionService userSystemRolePermissionService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CompanyUserPermissionService companyUserPermissionService;
	
	@Autowired
	private CompanyDAO companyDao;
	
	@Autowired
	private OrganizationDAO organizationDao;
	
	@Autowired
	private OrganizationUserPermissionDAO organizationUserPermissionDao;
	
	@Autowired
	private CompanyUserPermissionDAO companyUserPermissionDao;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private Sha256Encoder sha256Encoder;
	
	@Autowired
	private BusinessRoleService businessRoleService;
	
	@Autowired
	private SystemRoleDAO systemRoleDao;
	
	@Autowired
	private UserImportInfoService userImportInfoService;
	
	@Autowired
	private TokenDAO tokenDao;
	
	/**
	 * 添加一个游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:40:58
	 * @param String userId 游客id
	 * @param String nickname 游客昵称
	 * @return UserVO 用户
	 */
	public UserVO addTourist(String nickname) throws Exception{
		UserPO tourist = new UserPO();
		userDao.save(tourist);
		tourist.setNickname(nickname);
		tourist.setUserno(new StringBufferWrapper().append("t").append(tourist.getId()).toString());
		tourist.setClassify(UserClassify.TOURIST);
		tourist.setUpdateTime(new Date());
		userDao.save(tourist);
		//发布游客创建事件
		TouristCreateEvent event = new TouristCreateEvent(applicationEventPublisher, tourist.getId().toString(), tourist.getNickname(), tourist.getUserno());
		applicationEventPublisher.publishEvent(event);
		return new UserVO().set(tourist);
	}
	
	/**
	 * 清除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月2日 下午4:44:32
	 * @param Long userId 游客id
	 */
	public void removeTourist(Long userId) throws Exception{
		UserPO tourist = userDao.findOne(userId);
		if(tourist == null) return;
		if(!UserClassify.TOURIST.equals(tourist.getClassify())){
			throw new DeletedUserIsNotATouristException(userId);
		}
		TouristDeleteEvent event = new TouristDeleteEvent(applicationEventPublisher, tourist.getId().toString(), tourist.getNickname(), tourist.getUserno());
		applicationEventPublisher.publishEvent(event);
		userDao.delete(tourist);
	}
	
	/**
	 * 批量清除游客<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午1:51:43
	 * @param Collection<Long> userIds 游客id列表
	 */
	public void removeTouristBatch(Collection<Long> userIds) throws Exception{
		if(userIds!=null && userIds.size()>0){
			List<UserPO> users = userDao.findAll(userIds);
			if(users!=null && users.size()>0){
				TouristDeleteBatchEvent event = new TouristDeleteBatchEvent(applicationEventPublisher, userIds);
				applicationEventPublisher.publishEvent(event);
				userDao.deleteInBatch(users);
			}
		}
	}
	
	/**
	 * 添加一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 下午1:23:01
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 密码确认
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param Integer level 用户级别
	 * @param String classify 用户类型
	 * @param boolean emit 是否要发射事件
	 * @return UserVO 用户
	 */
	public UserVO add(
			String nickname,
            String username,
            String userno,
            String password,
            String repeat,
            String mobile,
            String mail,
            Integer level,
            String classify,
            boolean emit) throws Exception{
		
		UserPO user = addUser(nickname, username, userno, password, repeat, mobile, mail, level, classify);
		
		if(emit){
			//发布用户注册事件
			UserRegisteredEvent event = new UserRegisteredEvent(applicationEventPublisher, user.getId().toString(), user.getNickname());
			applicationEventPublisher.publishEvent(event);
		}
		
		return new UserVO().set(user);
	}
	
	/**
	 * 添加一个用户，并创建公司<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午3:51:08
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 确认密码
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param Integer level 用户级别
	 * @param String companyName 公司名称
	 * @return UserVO 新建的用户
	 */
	public UserVO add(
			String nickname,
            String username,
            String userno,
            String password,
            String repeat,
            String mobile,
            String mail,
            Integer level,
            String classify,
            String companyName) throws Exception{
		
		UserPO user = addUser(nickname, username, userno, password, repeat, mobile, mail, level, UserClassify.COMPANY.getName());
		
		CompanyVO company = null;
		SystemRoleVO adminRole = null;
		if(user.getClassify().equals(UserClassify.COMPANY)){
			//创建公司
			company = companyService.add(companyName, user);
			
			//处理公司管理员
			adminRole = businessRoleService.add(company.getId(), "企业管理员", true);
			
			//用户授权
			//1：授权默认的系统角色
			//2：授权公司管理员业务角色
			userSystemRolePermissionService.bindSystemRole(user.getId(), new ArrayListWrapper<Long>().add(2l).add(Long.valueOf(adminRole.getId())).getList());
		}
		
		//发布用户注册事件
		UserRegisteredEvent event = null;
		if(company == null){
			event = new UserRegisteredEvent(applicationEventPublisher, user.getId().toString(), user.getNickname());
		}else{
			event = new UserRegisteredEvent(applicationEventPublisher, user.getId().toString(), user.getNickname(), company.getId().toString(), company.getName(), adminRole.getId().toString(), adminRole.getName());
		}
		applicationEventPublisher.publishEvent(event);
		
		return new UserVO().set(user);
	}
	
	/**
	 * 添加一个用户，并加入公司<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午3:51:08
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 确认密码
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param Integer level 用户级别
	 * @param String companyId 公司id
	 * @return UserVO 新建的用户
	 */
	public UserVO add(
			String nickname,
            String username,
            String userno,
            String password,
            String repeat,
            String mobile,
            String mail,
            Integer level,
            String classify,
            Long companyId) throws Exception{
		
		CompanyPO company = companyDao.findOne(companyId);
		
		if(company == null){
			throw new CompanyNotExistException(companyId);
		}
		
		UserPO user = addUser(nickname, username, userno, password, repeat, mobile, mail, level, classify);
		
		if(user.getClassify().equals(UserClassify.COMPANY)){
			//加入公司
			companyUserPermissionService.add(company, user);
			//绑定用户和系统角色
			userSystemRolePermissionService.bindSystemRole(user.getId(), new ArrayListWrapper<Long>().add(3l).getList());
		}
		
		//发布用户注册事件
		UserRegisteredEvent event = new UserRegisteredEvent(applicationEventPublisher, user.getId().toString(), user.getNickname(), company.getId().toString(), company.getName(), user.getUserno());
		applicationEventPublisher.publishEvent(event);
		
		return new UserVO().set(user);
	}
	
	/**
	 * 添加一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 下午1:23:01
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 密码确认
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param Integer level 用户级别
	 * @param String classify 用户类型
	 * @return UserPO 用户
	 */
	private UserPO addUser(
			String nickname,
            String username,
            String userno,
            String password,
            String repeat,
            String mobile,
            String mail,
            Integer level,
            String classify) throws Exception{
		
		if(username == null) throw new UsernameCannotBeNullException();
		
		if(nickname == null) nickname = username;
		
		if(password == null) throw new PasswordCannotBeNullException();
		
		if(!password.equals(repeat)) throw new RepeatNotMatchPasswordException();
		
		UserPO user = userDao.findByUsername(username);
		if(user != null){
			throw new UsernameAlreadyExistException(username);
		}
		
		if(mobile != null){
			user = userDao.findByMobile(mobile);
			if(user != null){
				throw new MobileAlreadyExistException(mobile);
			}
		}
		
		if(mail != null){
			user = userDao.findByMail(mail);
			if(user != null){
				throw new MailAlreadyExistException(mail);
			}
		}
		
		user = new UserPO();
		user.setNickname(nickname);
		user.setUsername(username);
		user.setUserno(userno);
		user.setPassword(sha256Encoder.encode(password));
		user.setMobile(mobile);
		user.setMail(mail);
		user.setLevel(level==null?1:level);
		user.setAutoGeneration(false);
		user.setClassify(UserClassify.fromName(classify));
		user.setUpdateTime(new Date());
		userDao.save(user);
		
		//创建私有角色
		SystemRolePO privateRole = new SystemRolePO();
		privateRole.setAutoGeneration(true);
		privateRole.setName(SystemRolePO.generatePrivateRoleName(user.getId()));
		privateRole.setType(SystemRoleType.PRIVATE);
		privateRole.setUpdateTime(new Date());
		systemRoleDao.save(privateRole);
		
		//用户关联角色
		UserSystemRolePermissionPO permission = new UserSystemRolePermissionPO();
		permission.setAutoGeneration(true);
		permission.setRoleId(privateRole.getId());
		permission.setRoleType(SystemRoleType.PRIVATE);
		permission.setUserId(user.getId());
		permission.setUpdateTime(new Date());
		userSystemRolePermissionDao.save(permission);
		
		return user;
	}
	
	/**
	 * 删除一个用户<br/>
	 * <p>
	 *  删除用户角色映射
	 * 	-如果用户是企业用户<br/>
	 * 	     如果用户创建了公司<br/>
	 *      删除创建的公司<br/>
	 *      删除公司下的所有部门<br/>
	 *      公司内的所有用户变为个人用户<br/>
	 *      删除公司内公司用户的映射<br/>
	 *      删除公司内部门用户映射<br/>
	 *    如果用户加入了公司<br/>
	 *    	删除公司内公司用户的映射<br/>
	 *      删除公司内部门用户映射<br/>
	 * 	-如果用户是个人用户直接删除<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:37:10
	 * @param Long id 用户id
	 */
	public void delete(Long id) throws Exception{
		
		UserPO user = userDao.findOne(id);
		
		if(user == null) return;
		
		List<TokenPO> tokens = tokenDao.findByUserId(user.getId());
		if(tokens!=null && tokens.size()>0){
			for(TokenPO token:tokens){
				if(UserStatus.ONLINE.equals(token.getStatus())){
					throw new UserCannotDeleteBecauseOnlineStatusException();
				}
			}
		}
		
		if(UserClassify.COMPANY.equals(user.getClassify())){
			
			CompanyPO company = companyDao.findByUserId(user.getId());
			
			if(company.getUserId().equals(user.getId().toString())){
				//修改公司内用户为普通用户
				List<UserPO> users = userDao.findByCompanyIdAndExcept(company.getId(), new ArrayListWrapper<Long>().add(user.getId()).getList());
				if(users!=null && users.size()>0){
					for(UserPO scope:users){
						scope.setClassify(UserClassify.NORMAL);
					}
				}
				userDao.save(users);
				
				//删除部门
				List<OrganizationPO> organizations = organizationDao.findByCompanyIdOrderBySerialAsc(company.getId());
				if(organizations!=null && organizations.size()>0){
					Set<Long> organizationIds = new HashSet<Long>();
					for(OrganizationPO organization:organizations){
						organizationIds.add(organization.getId());
					}
					List<OrganizationUserPermissionPO> permissions = organizationUserPermissionDao.findByOrganizationIdIn(organizationIds);
					organizationUserPermissionDao.deleteInBatch(permissions);
					organizationDao.deleteInBatch(organizations);
				}
				
				//删除公司
				List<CompanyUserPermissionPO> permissions = companyUserPermissionDao.findByCompanyId(company.getId());
				companyUserPermissionDao.deleteInBatch(permissions);
				companyDao.delete(company);
			}else{
				List<OrganizationUserPermissionPO> permissions0 = organizationUserPermissionDao.findByUserIdIn(new ArrayListWrapper<String>().add(user.getId().toString()).getList());
				if(permissions0!=null && permissions0.size()>0){
					organizationUserPermissionDao.deleteInBatch(permissions0);
				}
				List<CompanyUserPermissionPO> permissions1 = companyUserPermissionDao.findByUserId(user.getId().toString());
				if(permissions1!=null && permissions1.size()>0){
					companyUserPermissionDao.deleteInBatch(permissions1);
				}
			}
			
		}
		
		userDao.delete(user);
		
		List<UserSystemRolePermissionPO> permissions = userSystemRolePermissionDao.findByUserId(id);
		
		if(permissions!=null && permissions.size()>0){
			//清除用户权限
			userSystemRolePermissionDao.deleteInBatch(permissions);
			//清除私有角色
			UserSystemRolePermissionPO privatePermission = null;
			for(UserSystemRolePermissionPO permission:permissions){
				if(SystemRoleType.PRIVATE.equals(permission.getRoleType())){
					privatePermission = permission;
					break;
				}
			}
			if(privatePermission != null){
				SystemRolePO privateRole = systemRoleDao.findOne(privatePermission.getRoleId());
				if(privateRole != null){
					systemRoleDao.delete(privateRole);
				}
			}
		}
		
		UserDeletedEvent event = new UserDeletedEvent(applicationEventPublisher, new ArrayListWrapper<UserVO>().add(new UserVO().setId(user.getId())
																																.setUserno(user.getUserno()))
																											   .getList());
		applicationEventPublisher.publishEvent(event);
		
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
	 * @param Integer level 用户级别
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
            Integer level,
            String tags,
            boolean editPassword,
            String oldPassword,
            String newPassword,
            String repeat) throws Exception{
		
		UserPO user = userDao.findOne(id);
		
		if(user == null) throw new UserNotExistException(id);
		
		if(editPassword){
			oldPassword = sha256Encoder.encode(oldPassword);
			if(!user.getPassword().equals(oldPassword)) throw new PasswordErrorException();
			
			if(newPassword == null) throw new PasswordCannotBeNullException();
			
			if(!newPassword.equals(repeat)) throw new RepeatNotMatchPasswordException();
			
			user.setPassword(sha256Encoder.encode(newPassword));
		}
		
		if(mobile != null){
			UserPO userExist = userDao.findByMobileWithExcept(mobile, user.getId());
			if(userExist != null){
				throw new MobileAlreadyExistException(mobile);
			}
		}
		
		if(mail != null){
			UserPO userExist = userDao.findByMailWithExcept(mail, user.getId());
			if(userExist != null){
				throw new MailAlreadyExistException(mail);
			}
		}
		
		user.setNickname(nickname);
		user.setMobile(mobile);
		user.setMail(mail);
		user.setLevel(level);
		user.setUpdateTime(new Date());
		if(tags != null) user.setTags(tags);
		userDao.save(user);
		
		return new UserVO().set(user);
	}
	
	/**
	 * 修改密码<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月16日 上午11:01:23
	 * @param username 用户民
	 * @param mobile 手机号
	 * @param newPassword 新密码
	 * @param repeat 重复新密码
	 * @return UserVO 修改后的用户
	 */
	public UserVO modifyPassword(
			String username,
			String mobile,
            String newPassword,
            String repeat) throws Exception{
		
		UserPO user = null;
		
		if(mobile != null){
			user = userDao.findByMobile(mobile);
			if(user == null){
				throw new MobileNotExistException(mobile);
			}
			
			if(!newPassword.equals(repeat)) throw new RepeatNotMatchPasswordException();
			
			user.setPassword(sha256Encoder.encode(newPassword));
		}
		
		userDao.save(user);
		
		return new UserVO().set(user);
	}
	
	/** 用户导入事件发布管理 */
	private ConcurrentHashMap<String, UserImportEventPublisher> publishers = new ConcurrentHashMap<String, UserImportEventPublisher>();
	
	public UserImportEventPublisher getUserImportEventPublisher(String companyId){
		return this.publishers.get(companyId);
	}
	
	/**
	 * 导入csv<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午10:44:06
	 * @param String csv csv文件
	 */
	public void importCsv(String csv) throws Exception{
		UserVO self = userQuery.current();
		String[] lines = csv.split("\n");
		Set<String> roleNames = new HashSet<String>();
		List<UserPO> users = new ArrayList<UserPO>();
		List<CompanyUserPermissionPO> companyUserPermissions = new ArrayList<CompanyUserPermissionPO>();
		List<SystemRolePO> roles = new ArrayList<SystemRolePO>();
		List<UserSystemRolePermissionPO> userSystemRolePermissions = new ArrayList<UserSystemRolePermissionPO>();
		for(int i=1; i<lines.length; i++){
			String line = lines[i];
			String[] columns = line.split(",",-1);
			String[] roleNamesInColumn = columns[6].split("#");
			for(String name:roleNamesInColumn){
				if(!"".equals(name)){
					roleNames.add(name);
				}
			}
			if(!columns[0].equals(self.getUsername())){
				UserPO user = new UserPO();
				user.setUsername(columns[0]);
				user.setNickname(columns[1]);
				user.setPassword(sha256Encoder.encode("123456"));
				user.setUserno(columns[4]);
				user.setClassify(UserClassify.COMPANY);
				user.setMobile(columns[7]);
				user.setMail(columns[8]);
				user.setLevel(1);
				user.setAutoGeneration(false);
				users.add(user);
			}
		}
		
		if(roleNames.size() > 0){
			for(String roleName:roleNames){
				SystemRolePO role = new SystemRolePO();
				role.setAutoGeneration(false);
				role.setCompanyId(Long.valueOf(self.getGroupId()));
				role.setName(roleName);
				role.setType(SystemRoleType.BUSINESS);
				role.setUpdateTime(new Date());
				roles.add(role);
			}
		}
		
		if(users.size() > 0){
			//校验
			Set<String> usernos = new HashSet<String>();
			Set<String> usernames = new HashSet<String>();
			for(UserPO user:users){
				if(user.getUserno() == null) throw new UsernoCannotBeNullException(user.getNickname());
				if(user.getUsername() == null) throw new UsernameCannotBeNullException();
				for(String userno:usernos){
					if(userno.equals(user.getUserno())){
						throw new DuplicateUserNumberImportedException(userno);
					}
				}
				for(String username:usernames){
					if(username.equals(user.getUsername())){
						throw new DuplicateUsernameImportedException(username);
					}
				}
				usernos.add(user.getUserno());
				usernames.add(user.getUsername());
			}
			
			//忽略重复用户号码以及重复用户名的用户
			List<UserPO> usersCopy = new ArrayList<UserPO>();
			List<UserPO> duplicateUsers = userDao.findByCompanyIdAndUsernoIn(Long.valueOf(self.getGroupId()), usernos);
			List<UserPO> existUsers = userDao.findByUsernameIn(usernames);
			for(UserPO user:users){
				boolean repetitive = false;
				for(UserPO duplicateUser:duplicateUsers){
					if(duplicateUser.getUserno().equals(user.getUserno())){
						repetitive = true;
						break;
					}
				}
				if(!repetitive){
					for(UserPO existUser:existUsers){
						if(existUser.getUsername().equals(user.getUsername())){
							repetitive = true;
							break;
						}
					}
				}
				if(!repetitive){
					usersCopy.add(user);
				}
			}
			users = usersCopy;
			/*if(duplicateUsers!=null && duplicateUsers.size()>0){
				Set<String> duplicateUsernos = new HashSet<String>();
				for(UserPO user:duplicateUsers){
					duplicateUsernos.add(user.getUserno());
				}
				throw new UsernoAlreadyExistInSystemException(duplicateUsernos);
			}*/
			/*if(existUsers!=null && existUsers.size()>0){
				Set<String> duplicateUsernames = new HashSet<String>();
				for(UserPO user:existUsers){
					duplicateUsernames.add(user.getUsername());
				}
				throw new UsernameAlreadyExistException(duplicateUsernames);
			}*/
			
			userDao.save(users);
			List<UserSystemRolePermissionPO> systemRolePermissions = new ArrayList<UserSystemRolePermissionPO>();
			List<SystemRolePO> privateRoles = new ArrayList<SystemRolePO>();
			for(UserPO user:users){
				//绑定系统角色
				UserSystemRolePermissionPO systemRolePermission = new UserSystemRolePermissionPO();
				systemRolePermission.setRoleId(3l);
				systemRolePermission.setAutoGeneration(false);
				systemRolePermission.setRoleType(SystemRoleType.SYSTEM);
				systemRolePermission.setUserId(user.getId());
				systemRolePermission.setUpdateTime(new Date());
				systemRolePermissions.add(systemRolePermission);
				
				//绑定导入角色
				CompanyUserPermissionPO permission = new CompanyUserPermissionPO();
				permission.setUserId(user.getId().toString());
				permission.setCompanyId(Long.valueOf(self.getGroupId()));
				companyUserPermissions.add(permission);
				
				//创建私有角色
				SystemRolePO privateRole = new SystemRolePO();
				privateRole.setAutoGeneration(true);
				privateRole.setName(SystemRolePO.generatePrivateRoleName(user.getId()));
				privateRole.setType(SystemRoleType.PRIVATE);
				privateRole.setUpdateTime(new Date());
				privateRoles.add(privateRole);
			}
			userSystemRolePermissionDao.save(systemRolePermissions);
			companyUserPermissionDao.save(companyUserPermissions);
			systemRoleDao.save(privateRoles);
			
			//私有角色授权
			List<UserSystemRolePermissionPO> privatePermissions = new ArrayList<UserSystemRolePermissionPO>();
			for(UserPO user:users){
				String privateRoleName = new StringBufferWrapper().append("private_u_").append(user.getId()).toString();
				for(SystemRolePO privateRole:privateRoles){
					if(privateRoleName.equals(privateRole.getName())){
						UserSystemRolePermissionPO privatePermission = new UserSystemRolePermissionPO();
						privatePermission.setAutoGeneration(true);
						privatePermission.setRoleId(privateRole.getId());
						privatePermission.setRoleType(SystemRoleType.PRIVATE);
						privatePermission.setUserId(user.getId());
						privatePermission.setUpdateTime(new Date());
						privatePermissions.add(privatePermission);
						break;
					}
				}
			}
			userSystemRolePermissionDao.save(privatePermissions);
		}
		
		if(roles.size() > 0){
			//角色判重
			List<SystemRolePO> existRoles = systemRoleDao.findByCompanyIdAndNameIn(Long.valueOf(self.getGroupId()), roleNames);
			if(existRoles!=null && existRoles.size()>0){
				List<SystemRolePO> notExistRoles = new ArrayList<SystemRolePO>();
				for(SystemRolePO role:roles){
					boolean exist = false;
					for(SystemRolePO existRole:existRoles){
						if(role.getName().equals(existRole.getName())){
							exist = true;
							break;
						}
					}
					if(!exist) notExistRoles.add(role);
				}
				systemRoleDao.save(notExistRoles);
				roles = new ArrayList<SystemRolePO>();
				roles.addAll(existRoles);
				roles.addAll(notExistRoles);
			}else{
				systemRoleDao.save(roles);
			}
			for(SystemRolePO role:roles){
				for(int i=1; i<lines.length; i++){
					String line = lines[i];
					String[] columns = line.split(",");
					if(!columns[0].equals(self.getUsername()) && columns[6].indexOf(role.getName())>=0){
						for(UserPO user:users){
							if(user.getUsername().equals(columns[0])){
								UserSystemRolePermissionPO permission = new UserSystemRolePermissionPO();
								permission.setAutoGeneration(false);
								permission.setRoleId(role.getId());
								permission.setRoleType(SystemRoleType.BUSINESS);
								permission.setUserId(user.getId());
								permission.setUpdateTime(new Date());
								userSystemRolePermissions.add(permission);
								break;
							}
						}
					}
				}
			}
			if(userSystemRolePermissions.size() > 0){
				userSystemRolePermissionDao.save(userSystemRolePermissions);
			}
		}
		
		//发射事件
		if(users.size() > 0){
			UserImportEventPublisher userImportEventPublisher = new UserImportEventPublisher(applicationEventPublisher, users, userSystemRolePermissions, self.getGroupId(), self.getGroupName(), publishers);
			this.publishers.put(self.getGroupId(), userImportEventPublisher);
			userImportEventPublisher.publish();
		}
		
		//更新导入次数
		userImportInfoService.addTimes(self.getGroupId());
		
	}
	
	/**
	 * 终端用户离线<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月25日 下午5:55:38
	 * @param Long userId 用户id
	 * @param TerminalType type 终端类型
	 */
	public void userOffline(Long userId, TerminalType type) throws Exception{
		
		TokenPO token = tokenDao.findByUserIdAndType(userId, type);
		if(token != null){
			token.setStatus(UserStatus.OFFLINE);
			tokenDao.save(token);
		}
	}
	
	/**
	 * 添加ldap用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午3:48:05
	 * @param List<UserVO> userVOs 用户信息
	 * @return List<UserPO> 用户信息
	 */
	public List<UserPO> addLdapUser(List<UserVO> userVOs) throws Exception{
		List<String> userUuids = new ArrayList<String>();
		for(UserVO userVO: userVOs){
			userUuids.add(userVO.getUuid());
		}
		
		List<UserPO> users = userDao.findByUuidIn(userUuids);
		
		List<UserPO> userPOs = new ArrayList<UserPO>();
		for(UserVO userVO: userVOs){
			UserPO userPO = null;
			for(UserPO user: users){
				if(user.getUuid().equals(userVO.getUuid())){
					userPO = user;
					break;
				}
			}
			
			if(userPO == null){
				userPO = new UserPO();
			}
			
			userPO.setUuid(userVO.getUuid());
			userPO.setClassify(UserClassify.LDAP);
			userPO.setUsername(userVO.getUsername());
			userPO.setNickname(userVO.getNickname());
			userPO.setUserno(userVO.getUserno());
			
			userPOs.add(userPO);
		}
		
		userDao.save(userPOs);
		
		return userPOs;
	}
	
}
