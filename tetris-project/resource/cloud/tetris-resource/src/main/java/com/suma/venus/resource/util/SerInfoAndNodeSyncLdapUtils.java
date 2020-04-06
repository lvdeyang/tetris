package com.suma.venus.resource.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.application.ldap.node.LdapNodeDao;
import com.suma.application.ldap.node.LdapNodePo;
import com.suma.application.ldap.ser.LdapSerInfoDao;
import com.suma.application.ldap.ser.LdapSerInfoPo;
import com.suma.venus.resource.pojo.SerInfoPO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.dao.SerInfoDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.ldap.LdapNodeInfoUtil;
import com.suma.venus.resource.ldap.LdapSerInfoUtil;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;

@Component
public class SerInfoAndNodeSyncLdapUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerInfoAndNodeSyncLdapUtils.class);

	@Autowired
	LdapSerInfoDao ldapSerInfoDao;

	@Autowired
	SerInfoDao serInfoDao;

	@Autowired
	LdapNodeDao ldapNodeDao;

	@Autowired
	SerNodeDao serNodeDao;

	@Autowired
	LdapNodeInfoUtil ldapNodeInfoUtil;

	@Autowired
	LdapSerInfoUtil ldapSerInfoUtil;

	public int handleSyncSerInfoFromLdap() {

		List<LdapSerInfoPo> ldapSerInfos = ldapSerInfoDao.queryAll();
		int successCnt = 0;
		if (null != ldapSerInfos && !ldapSerInfos.isEmpty()) {
			for (LdapSerInfoPo ldapSer : ldapSerInfos) {
				try {
					// ID已存在的过滤掉，补充

					SerInfoPO serInfoPOSync = serInfoDao.findTopBySerUuid(ldapSer.getSerUuid());

					if (serInfoPOSync != null && serInfoPOSync.getSourceType().equals(SOURCE_TYPE.SYSTEM)) {
						continue;

					} else if (serInfoPOSync != null && serInfoPOSync.getSourceType().equals(SOURCE_TYPE.EXTERNAL)) {
						// 外部之前同步过的信息可能已经修改，直接save一波
						BeanUtils.copyProperties(ldapSer, serInfoPOSync, "serUuid", "syncStatus", "sourceType");
						// serInfoPOSync.setSyncStatus(SYNC_STATUS.SYNC);
						serInfoDao.save(serInfoPOSync);
						successCnt++;

					} else if (serInfoPOSync == null) {
						// 外部未同步过的，新建save
						SerInfoPO serInfoPO = ldapSerInfoUtil.ldapToPojo(ldapSer);
						serInfoDao.save(serInfoPO);
						successCnt++;
					}

				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		return successCnt;
	}

	public int handleSyncSerInfoToLdap() {

		List<SerInfoPO> serInfoPOs = serInfoDao.findSerInfoSyncToLdap();
		List<SerInfoPO> successSers = new ArrayList<SerInfoPO>();
		for (SerInfoPO serInfoPO : serInfoPOs) {
			try {

				try {

					List<LdapSerInfoPo> ldapSerInfoPoList = ldapSerInfoDao.getSerInfoByUuid(serInfoPO.getSerUuid());
					SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
					if (!CollectionUtils.isEmpty(ldapSerInfoPoList)) {
						LdapSerInfoPo ldapSerInfo = ldapSerInfoUtil.pojoModifyToLdap(serInfoPO,
								ldapSerInfoPoList.get(0));
						ldapSerInfoDao.update(ldapSerInfo);
					} else {
						LdapSerInfoPo ldapSerInfo = ldapSerInfoUtil.pojoToLdap(serInfoPO, self);
						ldapSerInfoDao.save(ldapSerInfo);
					}

					serInfoPO.setSyncStatus(SYNC_STATUS.SYNC);
					successSers.add(serInfoPO);
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error("", e);
				}

			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}

		if (!successSers.isEmpty()) {
			serInfoDao.save(successSers);
		}

		return successSers.size();
	}

	public String handleCleanUpSerInfo() {

		try {

			// 先删除上传到LDAP服务端的数据
			List<SerInfoPO> serInfoPOList = serInfoDao.findBySourceType(SOURCE_TYPE.SYSTEM);

			if (!CollectionUtils.isEmpty(serInfoPOList)) {

				for (SerInfoPO serInfoPO : serInfoPOList) {

					List<LdapSerInfoPo> ldapSerInfoPoList = ldapSerInfoDao.getSerInfoByUuid(serInfoPO.getSerUuid());
					if (!CollectionUtils.isEmpty(ldapSerInfoPoList)) {
						ldapSerInfoDao.remove(ldapSerInfoPoList.get(0));
					}

					serInfoPO.setSyncStatus(SYNC_STATUS.ASYNC);
				}

				serInfoDao.save(serInfoPOList);
			}

			// 在删除从LDAP服务端下载的数据
			List<SerInfoPO> externalSerInfoPOList = serInfoDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
			if (!CollectionUtils.isEmpty(externalSerInfoPOList)) {
				serInfoDao.delete(externalSerInfoPOList);
			}

			return "";

		} catch (Exception e) {
			LOGGER.error("cleanUpSerInfo error, e==" + e.getStackTrace());
			return "内部错误";
		}
	}

	public int handleSyncSerNodeFromLdap() {

		List<LdapNodePo> ldapNodes = ldapNodeDao.queryAll();
		int successCnt = 0;
		if (null != ldapNodes && !ldapNodes.isEmpty()) {
			for (LdapNodePo ldapNode : ldapNodes) {
				try {
					SerNodePO serNodePOTemp = serNodeDao.findTopByNodeUuid(ldapNode.getNodeUuid());

					if (serNodePOTemp != null && serNodePOTemp.getSourceType().equals(SOURCE_TYPE.EXTERNAL)) {
						serNodePOTemp.setNodeName(ldapNode.getNodeName());
						serNodePOTemp.setNodeRelations(ldapNode.getNodeRelations());
						serNodePOTemp.setNodeFactInfo(ldapNode.getNodeFactInfo());
						serNodePOTemp.setSyncStatus(SYNC_STATUS.SYNC);
						serNodeDao.save(serNodePOTemp);
						successCnt++;

					} else if (serNodePOTemp != null && serNodePOTemp.getSourceType().equals(SOURCE_TYPE.SYSTEM)) {

						continue;

					} else if (serNodePOTemp == null) {
						SerNodePO serNodePO = ldapNodeInfoUtil.ldapToPojo(ldapNode);
						serNodeDao.save(serNodePO);
						successCnt++;
					}

				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}
		}

		// 取一外部服务节点做父节点
		/*
		 * SerNodePO serNodePOFromLdap =
		 * serNodeDao.findTopBySourceType(SOURCE_TYPE.EXTERNAL); SerNodePO
		 * localSerNodePO = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM); if (null
		 * != localSerNodePO && null != serNodePOFromLdap) {
		 * localSerNodePO.setNodeFather(serNodePOFromLdap.getNodeUuid());
		 * serNodeDao.save(localSerNodePO); }
		 */

		return successCnt;
	}

	public int handleSyncSerNodeToLdap() {
		List<SerNodePO> serNodePOs = serNodeDao.findSerNodeSyncToLdap();
		List<SerNodePO> successSerNodePOs = new ArrayList<SerNodePO>();

		for (SerNodePO serNodePO : serNodePOs) {
			try {

				// 如果本地serNode没有设置父节点，则设置
				/*
				 * if (serNodePO.getNodeFather() == null ||
				 * serNodePO.getNodeFather().equals("null")) {
				 * 
				 * List<SerNodePO> externalSerNodePOs =
				 * serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
				 * 
				 * if (CollectionUtils.isEmpty(externalSerNodePOs)) {
				 * serNodePO.setNodeFather(externalSerNodePOs.get(0).getNodeUuid()); } }
				 */
				LdapNodePo ldapNode = ldapNodeInfoUtil.pojoToLdap(serNodePO);

				try {
					List<LdapNodePo> ldapNodePoList = ldapNodeDao.getNodeInfoByUuid(serNodePO.getNodeUuid());
					if (!CollectionUtils.isEmpty(ldapNodePoList)) {

						ldapNodeDao.update(ldapNode);
					} else {
						ldapNodeDao.save(ldapNode);
					}

					serNodePO.setSyncStatus(SYNC_STATUS.SYNC);
					successSerNodePOs.add(serNodePO);

				} catch (Exception e) {
					LOGGER.warn("update ldapNode error， e=" + e.getStackTrace().toString());
				}

			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}

		if (!successSerNodePOs.isEmpty()) {
			serNodeDao.save(successSerNodePOs);
		}

		return successSerNodePOs.size();

	}

	public String handleCleanUpSerNode() {

		try {

			// 先删除上传到LDAP服务端的数据
			List<SerNodePO> serNodeList = serNodeDao.findBySourceType(SOURCE_TYPE.SYSTEM);

			if (!CollectionUtils.isEmpty(serNodeList)) {

				for (SerNodePO serNodePO : serNodeList) {
					try {
						List<LdapNodePo> ldapNodePoList = ldapNodeDao.getNodeInfoByUuid(serNodePO.getNodeUuid());
						if (!CollectionUtils.isEmpty(ldapNodePoList)) {
							ldapNodeDao.remove(ldapNodePoList.get(0));
						}

					} catch (Exception e) {
						// TODO: handle exception
					}

					serNodePO.setSyncStatus(SYNC_STATUS.ASYNC);

				}

				serNodeDao.save(serNodeList);
			}

			// 再删除从LDAP服务端下载的数据
			List<SerNodePO> externalSerNodePOList = serNodeDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
			if (!CollectionUtils.isEmpty(externalSerNodePOList)) {
				serNodeDao.delete(externalSerNodePOList);
			}

			return "";

		} catch (Exception e) {
			LOGGER.error("cleanUpSerNode error, e==" + e.getStackTrace());
			return "内部错误";
		}
	}

}
