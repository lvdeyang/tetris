package com.suma.venus.resource.ldap;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.application.ldap.contants.LdapContants;
import com.suma.application.ldap.node.LdapNodePo;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.SerNodePO;

@Component
public class LdapNodeInfoUtil implements InitializingBean{
	
	@Autowired
	SerNodeDao serNodeDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//手动创建
		/*List<SerNodePO> serNodePOs = serNodeDao.findAll();
		if(serNodePOs.isEmpty()){
			SerNodePO serNodePO = new SerNodePO(LdapContants.DEFAULT_NODE_UUID, "SUMA服务节点",
					"NULL", "NULL", LdapContants.DEFAULT_FACT_UUID);
			serNodeDao.save(serNodePO);
		}*/
		
	}

	public SerNodePO ldapToPojo(LdapNodePo ldapNode){
		SerNodePO serNodePO = new SerNodePO(ldapNode.getNodeUuid(), ldapNode.getNodeName(), ldapNode.getNodeFather(),
				ldapNode.getNodeRelations(), ldapNode.getNodeFactInfo());
		
		serNodePO.setSourceType(SOURCE_TYPE.EXTERNAL);
		serNodePO.setSyncStatus(SYNC_STATUS.SYNC);
		return serNodePO;
	}
	
	public LdapNodePo pojoToLdap(SerNodePO serNodePO){
		LdapNodePo ldapNodePo = new LdapNodePo();
		ldapNodePo.setNodeUuid(serNodePO.getNodeUuid());
		ldapNodePo.setNodeName(serNodePO.getNodeName());
		ldapNodePo.setNodeFather(serNodePO.getNodeFather());
		ldapNodePo.setNodeRelations(serNodePO.getNodeRelations());
		ldapNodePo.setNodeFactInfo(serNodePO.getNodeFactInfo());
		
		return ldapNodePo;
	}

}
