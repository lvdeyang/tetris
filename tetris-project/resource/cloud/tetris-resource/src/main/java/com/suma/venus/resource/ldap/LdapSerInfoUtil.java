package com.suma.venus.resource.ldap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.application.ldap.contants.LdapContants;
import com.suma.application.ldap.ser.LdapSerInfoPo;
import com.suma.application.ldap.util.Base64Util;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerNodePO;

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

	public LdapSerInfoPo pojoToLdap(SerInfoPO pojo, SerNodePO self) {
		LdapSerInfoPo ldapSer = new LdapSerInfoPo();
		BeanUtils.copyProperties(pojo, ldapSer);
		ldapSer.setSerPwd(Base64Util.encode(pojo.getSerPwd()));
		ldapSer.setSerNode(self.getNodeUuid());
		ldapSer.setSerFactInfo(self.getNodeFactInfo());

		return ldapSer;
	}

	public LdapSerInfoPo pojoModifyToLdap(SerInfoPO pojo, LdapSerInfoPo ldapSerInfoPo) {

		BeanUtils.copyProperties(pojo, ldapSerInfoPo, "serUuid", "syncStatus", "sourceType");
		ldapSerInfoPo.setSerPwd(Base64Util.encode(pojo.getSerPwd()));

		return ldapSerInfoPo;
	}

}
