package com.suma.venus.resource.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.application.ldap.department.dao.LdapDepartmentDao;
import com.suma.application.ldap.department.po.LdapDepartmentPo;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.ldap.LdapDepartInfoUtil;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

@Component
public class DepartSyncLdapUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartSyncLdapUtils.class);

	@Autowired
	LdapDepartmentDao ldapDepartmentDao;

	@Autowired
	LdapDepartInfoUtil ldapDepartInfoUtil;

	@Autowired
	FolderDao folderDao;

	public int handleSyncFromLdap() {
		List<LdapDepartmentPo> ldapDeparts = ldapDepartmentDao.queryAllDepartment();
		int successCnt = 0;
		if (!CollectionUtils.isEmpty(ldapDeparts)) {
			for (LdapDepartmentPo ldapDepart : ldapDeparts) {
				try {
					FolderPO folder = folderDao.findTopByUuid(ldapDepart.getOrgUuid());
					if (null == folder) {
						// 系统内不存在这条数据，直接存
						FolderPO folderPO = ldapDepartInfoUtil.ldapToPojo(ldapDepart);
						folderDao.save(folderPO);
						successCnt++;
					} else {
						// 系统内已存在这条数据,判断是否是LDAP数据，是的话更新局部属性
						if (SOURCE_TYPE.EXTERNAL.equals(folder.getSourceType())) {
							ldapDepartInfoUtil.ldapMofidyPojo(ldapDepart, folder);
							folderDao.save(folder);
							successCnt++;
						}
					}
				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		return successCnt;
	}

	public int handleSyncToLdap() {
		List<FolderPO> folderPOs = folderDao.findFoldersSyncToLdap();
		List<FolderPO> successFolders = new ArrayList<FolderPO>();
		for (FolderPO folderPO : folderPOs) {
			try {
				List<LdapDepartmentPo> ldapDepartmentPos = ldapDepartmentDao.getDepartByUuid(folderPO.getUuid());
				if (!CollectionUtils.isEmpty(ldapDepartmentPos)) {
					LdapDepartmentPo ldapDepart = ldapDepartInfoUtil.pojoToLdap(folderPO);
					ldapDepartmentDao.update(ldapDepart);
				} else {
					// 新建
					LdapDepartmentPo ldapDepart = ldapDepartInfoUtil.pojoToLdap(folderPO);
					ldapDepartmentDao.save(ldapDepart);
				}

				folderPO.setSyncStatus(SYNC_STATUS.SYNC);
				successFolders.add(folderPO);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}

		if (!successFolders.isEmpty()) {
			folderDao.save(successFolders);
		}
		return successFolders.size();
	}

	public String handleCleanUpLdap() {

		// 先从Ldap删除本地上传上去的数据
		List<FolderPO> folderPOs = folderDao.findBySourceType(SOURCE_TYPE.SYSTEM);
		if (!CollectionUtils.isEmpty(folderPOs)) {
			for (FolderPO folderPO : folderPOs) {
				try {

					List<LdapDepartmentPo> ldapDeparts = ldapDepartmentDao.getDepartByUuid(folderPO.getUuid());
					if (!CollectionUtils.isEmpty(ldapDeparts)) {
						ldapDepartmentDao.remove(ldapDeparts.get(0));
					}

					folderPO.setSyncStatus(SYNC_STATUS.ASYNC);
					folderDao.save(folderPO);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

		// 再从本地删除从ldap下载下来的数据
		List<FolderPO> externalFolderPOs = folderDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		if (!CollectionUtils.isEmpty(externalFolderPOs)) {
			folderDao.delete(externalFolderPOs);
		}

		return "";
	}

}
