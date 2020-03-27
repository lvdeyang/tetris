package com.suma.venus.resource.ldap;

import org.springframework.stereotype.Component;

import com.suma.application.ldap.contants.LdapContants;
import com.suma.application.ldap.user.po.LdapUserPo;
import com.suma.application.ldap.util.Base64Util;
import com.suma.venus.resource.base.bo.UserBO;
import com.sumavision.tetris.user.UserVO;

@Component
public class LdapUserInfoUtil {

	/**
	 * 用户信息生成ldap用户信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午1:23:31
	 * @param UserBO user 用户信息
	 * @return LdapUserPo ldap用户信息
	 */
	public LdapUserPo pojoToLdap(UserBO user){
		
		LdapUserPo ldapUser = new LdapUserPo();
		ldapUser.setUserUuid(user.getUser().getUuid());
		ldapUser.setUserNo(user.getUserNo());
		ldapUser.setUserName(user.getName());
		ldapUser.setUserAccount(user.getName());
		//TODO：这里的密码没用
		ldapUser.setUserPwd(Base64Util.encode(user.getId().toString()));
		ldapUser.setUserLevel(0);
		ldapUser.setUserType(user.isAdmin() ? 2 : 1);
		ldapUser.setUserOrg(user.getFolderUuid());
		//TODO：这里的node写死的
		ldapUser.setUserNode(LdapContants.DEFAULT_NODE_UUID);
		ldapUser.setUserFactInfo(LdapContants.DEFAULT_FACT_UUID);

		return ldapUser;
	}
	
	/**
	 * ldap用户信息生成用户信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月26日 下午3:01:42
	 * @param LdapUserPo ldapUser ldap用户信息
	 * @return UserVO 用户信息
	 */
	public UserVO ldapToPojo(LdapUserPo ldapUser){
		
		UserVO user = new UserVO();
		user.setUuid(ldapUser.getUserUuid());
		user.setUserno(ldapUser.getUserNo());
		user.setNickname(ldapUser.getUserName());
		user.setUsername(ldapUser.getUserAccount());
		//userNode有什么用？
		
		return user;
	}
	

}
