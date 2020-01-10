package com.suma.venus.resource.ldap;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.suma.application.ldap.contants.LdapContants;
import com.suma.application.ldap.ser.LdapSerInfoPo;
import com.suma.application.ldap.util.Base64Util;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.SerInfoPO;

@Component
public class LdapSerInfoUtil {

	public SerInfoPO ldapToPojo(LdapSerInfoPo ldap) {
		SerInfoPO pojo = new SerInfoPO();
		BeanUtils.copyProperties(ldap, pojo);
		pojo.setSerPwd(Base64Util.decode(ldap.getSerPwd()));

		pojo.setSourceType(SOURCE_TYPE.EXTERNAL);
		pojo.setSyncStatus(SYNC_STATUS.SYNC);
		return pojo;
	}

	public LdapSerInfoPo pojoToLdap(SerInfoPO pojo) {
		LdapSerInfoPo ldapSer = new LdapSerInfoPo();
		BeanUtils.copyProperties(pojo, ldapSer);
		ldapSer.setSerPwd(Base64Util.encode(pojo.getSerPwd()));
		ldapSer.setSerNode(LdapContants.DEFAULT_NODE_UUID);
		ldapSer.setSerFactInfo(LdapContants.DEFAULT_FACT_UUID);

		return ldapSer;
	}

	public LdapSerInfoPo pojoModifyToLdap(SerInfoPO pojo, LdapSerInfoPo ldapSerInfoPo) {

		BeanUtils.copyProperties(pojo, ldapSerInfoPo, "serUuid", "syncStatus", "sourceType");

		return ldapSerInfoPo;
	}

}
