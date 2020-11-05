package com.suma.venus.resource.ldap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suma.application.ldap.contants.LdapContants;
import com.suma.application.ldap.equip.po.LdapEquipPo;
import com.suma.application.ldap.util.Base64Util;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

/**
 * ldap设备信息处理类
 * @author lxw
 *
 */
@Component
public class LdapEquipInfoUtil {

	@Autowired
	ChannelSchemeService channelSchemeService;
	
	@Autowired
	FolderDao folderDao;
	
	public BundlePO ldapToPojo(LdapEquipPo ldapEquip){
		BundlePO bundle = new BundlePO();
		switch (ldapEquip.getEquipType()) {
		case 2:
			bundle.setBundleAlias("编码器");
			bundle.setDeviceModel("jv210");
			bundle.setBundleType("VenusTerminal");
			break;
		case 3:
			bundle.setBundleAlias("解码器");
			bundle.setDeviceModel("jv210");
			bundle.setBundleType("VenusTerminal");
			break;
		case 11:
			bundle.setBundleAlias("编解码一体设备");
			bundle.setDeviceModel("jv210");
			bundle.setBundleType("VenusTerminal");
			break;
		default:
			return null;
		}
		bundle.setBundleId(ldapEquip.getEquipUuid());
		bundle.setUsername(ldapEquip.getEquipNo());
		bundle.setBundleName(ldapEquip.getEquipName());
		bundle.setDeviceIp(ldapEquip.getEquipAddr());
		bundle.setDevicePort(ldapEquip.getEquipPort());
		bundle.setOnlinePassword(Base64Util.decode(ldapEquip.getEquipPwd()));
		bundle.setEquipOrg(ldapEquip.getEquipOrg());
		bundle.setEquipNode(ldapEquip.getEquipNode());
		bundle.setEquipFactInfo(ldapEquip.getEquipFactInfo());
		
		FolderPO folderPO = folderDao.findTopByUuid(ldapEquip.getEquipOrg());
		if(null != folderPO){
			bundle.setFolderId(folderPO.getId());
		}
		
		bundle.setSourceType(SOURCE_TYPE.EXTERNAL);
		bundle.setSyncStatus(SYNC_STATUS.SYNC);
		return bundle;
	}
	
	public LdapEquipPo pojoToLdap(BundlePO bundle, SerNodePO self){
		
		LdapEquipPo ldapEquip = new LdapEquipPo();
		ldapEquip.setEquipUuid(bundle.getBundleId());
		ldapEquip.setEquipNo(bundle.getUsername());
		ldapEquip.setEquipName(bundle.getBundleName());
		ldapEquip.setEquipAddr(bundle.getDeviceIp());
		ldapEquip.setEquipPort(bundle.getDevicePort());
		ldapEquip.setEquipPwd(Base64Util.encode(bundle.getOnlinePassword()));
		
		//确定所属组织机构
		if(null == bundle.getFolderId()){
			return null;
		}
		
		FolderPO folder = folderDao.findOne(bundle.getFolderId());
		if(null == folder || null == folder.getToLdap() || !folder.getToLdap()){
			return null;//如果所属分组无效，则不上传
		}
		ldapEquip.setEquipOrg(folder.getUuid());
		
		ldapEquip.setEquipNode(null==bundle.getEquipNode() ? self.getNodeUuid(): bundle.getEquipNode());
		ldapEquip.setEquipFactInfo(self.getNodeFactInfo());
		
		//获取bundle的编码器类型
		Integer codecType = channelSchemeService.getCoderDeviceType(bundle.getBundleId());
		if(codecType != 2){//只上传编码器
			return null;
		}
		ldapEquip.setEquipType(codecType);
		
		return ldapEquip;
	}
	
	public BundlePO ldapModifyPojo(LdapEquipPo ldapEquip, BundlePO bundlePO) {
		bundlePO.setUsername(ldapEquip.getEquipNo());
		bundlePO.setBundleName(ldapEquip.getEquipName());
		bundlePO.setDeviceIp(ldapEquip.getEquipAddr());
		bundlePO.setDevicePort(ldapEquip.getEquipPort());
		bundlePO.setOnlinePassword(Base64Util.decode(ldapEquip.getEquipPwd()));
		bundlePO.setEquipOrg(ldapEquip.getEquipOrg());
		bundlePO.setEquipNode(ldapEquip.getEquipNode());
		bundlePO.setEquipFactInfo(ldapEquip.getEquipFactInfo());
		
		FolderPO folderPO = folderDao.findTopByUuid(ldapEquip.getEquipOrg());
		if(null != folderPO){
			bundlePO.setFolderId(folderPO.getId());
		}
		
		return bundlePO;
	}
}
