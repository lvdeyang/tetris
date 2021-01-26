package com.suma.venus.resource.ldap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.SerNodePO;

/**
 * ldap组织信息处理工具类
 * 
 * @author lxw
 */

@Component
public class LdapDepartInfoUtil {

	@Autowired
	private FolderDao folderDao;

	public FolderPO ldapToPojo(LdapDepartmentPo ldapDepart) {
		FolderPO folderPO = new FolderPO();
		folderPO.setUuid(ldapDepart.getOrgUuid());
//		folderPO.setFolderType(FolderType.TERMINAL);
		folderPO.setName(ldapDepart.getOrgName());
		folderPO.setParentId(-1l);
		folderPO.setFolderIndex(1);
		folderPO.setToLdap(true);
		String parentUuid = ldapDepart.getOrgRelation();
		if (null != parentUuid) {
			FolderPO parentFolder = folderDao.findTopByUuid(parentUuid);
			if (null != parentFolder) {
				folderPO.setParentId(parentFolder.getId());
				if (null != parentFolder.getParentPath()) {
					folderPO.setParentPath(parentFolder.getParentPath() + "/" + parentFolder.getId());
				} else {
					folderPO.setParentPath("/" + parentFolder.getId());
				}

				int maxIndex = caculateMaxfolderIndex(parentFolder, folderPO);
				folderPO.setFolderIndex(maxIndex + 1);
			}

		}
		folderPO.setBeBvcRoot(false);
		folderPO.setSourceType(SOURCE_TYPE.EXTERNAL);
		folderPO.setSyncStatus(SYNC_STATUS.SYNC);
		return folderPO;
	}

	public LdapDepartmentPo pojoToLdap(FolderPO folderPO, SerNodePO self) {
		LdapDepartmentPo ldapDepart = new LdapDepartmentPo();
		ldapDepart.setOrgUuid(folderPO.getUuid());
		ldapDepart.setOrgName(folderPO.getName());
		ldapDepart.setOrgRelation("NULL");
		ldapDepart.setOrgCmdRelation("NULL");
		if (null != folderPO.getParentId()) {
			FolderPO parentFolder = folderDao.findById(folderPO.getParentId());
			if (null != parentFolder) {
				ldapDepart.setOrgRelation(parentFolder.getUuid());
//				ldapDepart.setOrgCmdRelation(parentFolder.getUuid());
			}
		}
		ldapDepart.setOrgFactInfo(self.getNodeFactInfo());

		return ldapDepart;
	}

	public FolderPO ldapMofidyPojo(LdapDepartmentPo ldapDepart, FolderPO folder) {
		folder.setName(ldapDepart.getOrgName());
		FolderPO parentFolder = folderDao.findTopByUuid(ldapDepart.getOrgRelation());
		// 父级节点是否变化
		if (null != parentFolder && parentFolder.getId() != folder.getParentId()) {
			folder.setParentId(parentFolder.getId());
			if (null != parentFolder.getParentPath()) {
				folder.setParentPath(parentFolder.getParentPath() + "/" + parentFolder.getId());
			} else {
				folder.setParentPath("/" + parentFolder.getId());
			}
		}
		return folder;
	}

	public Integer caculateMaxfolderIndex(FolderPO folderPO, FolderPO changechildFolderPO) {

		// 查询所有同级folders
		List<Integer> brotherFolderIndexs = new ArrayList<Integer>();
		List<FolderPO> brotherFolders = folderDao.findByParentId(folderPO.getId());

		for (FolderPO folderTemp : brotherFolders) {
			if (!folderTemp.getUuid().equals(changechildFolderPO.getUuid())) {
				brotherFolderIndexs.add(folderTemp.getFolderIndex());
			}
		}

		if (!CollectionUtils.isEmpty(brotherFolderIndexs)) {
			return brotherFolderIndexs.stream().max(Integer::compare).get();
		}

		return 0;

	}
}
