package com.suma.venus.resource.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.suma.application.ldap.equip.dao.LdapEquipDao;
import com.suma.application.ldap.equip.po.LdapEquipPo;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.dao.ExtraInfoDao;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.dao.SerNodeDao;
import com.suma.venus.resource.ldap.LdapEquipInfoUtil;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.SerNodePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;

@Component
public class EquipSyncLdapUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipSyncLdapUtils.class);

	@Autowired
	private LdapEquipDao ldapEquipDao;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private ExtraInfoDao extraInfoDao;

	@Autowired
	private LdapEquipInfoUtil ldapEquipInfoUtil;

	@Autowired
	private ChannelTemplateDao channelTemplateDao;

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private BundleService bundleService;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Autowired
	private LockChannelParamDao lockChannelParamDao;

	@Autowired
	private LockBundleParamDao lockBundleParamDao;
	
	@Autowired
	private SerNodeDao serNodeDao;

	public int handleSyncFromLdap() {
		List<LdapEquipPo> ldapEquips = ldapEquipDao.queryAllEquips();
		List<BundlePO> successBundles = new ArrayList<BundlePO>();
		List<ExtraInfoPO> successExtraInfos = new ArrayList<ExtraInfoPO>();
		if (!CollectionUtils.isEmpty(ldapEquips)) {

			List<String> allBundleIdsInLdap = new ArrayList<String>();
			for (LdapEquipPo ldapEquip : ldapEquips) {
				allBundleIdsInLdap.add(ldapEquip.getEquipUuid());
			}
			// 处理ldap上删除了的外部设备（外部系统删除了它上传到ldap的设备，bvc如果下载过，也要对应删除）
			List<String> ldapBundleIdsInBvc = bundleDao.findBundleIdsFromLdap();
			ldapBundleIdsInBvc.removeAll(allBundleIdsInLdap);
			if (!CollectionUtils.isEmpty(ldapBundleIdsInBvc)) {
				List<BundlePO> ldapBundlePOList = bundleDao.findInBundleIds(ldapBundleIdsInBvc);
				// TODO
				deleteLdapBundles(ldapBundlePOList);
			}

			// 处理新增或修改的ldap设备
			for (LdapEquipPo ldapEquip : ldapEquips) {
				try {
					BundlePO bundlePO = bundleDao.findByBundleId(ldapEquip.getEquipUuid());
					if (null != bundlePO) {
						// 已存在，是否要更新
						if (SOURCE_TYPE.EXTERNAL.equals(bundlePO.getSourceType())) {
							// 更新局部信息
							ldapEquipInfoUtil.ldapModifyPojo(ldapEquip, bundlePO);
							successBundles.add(bundlePO);
						}
					} else {
						// 判断设备是否冲突账号
						if (null != bundleDao.findByUsername(ldapEquip.getEquipNo())) {
							continue;
						}

						// 不存在，直接保存
						BundlePO bundle = ldapEquipInfoUtil.ldapToPojo(ldapEquip);
						if (null == bundle) {
							continue;
						}

						// 配置能力通道
						// 配置前先根据bundleId清下通道数据，以防之前有残留
						channelSchemeService.deleteByBundleId(bundle.getBundleId());
						if (ldapEquip.getEquipType() == 2) {
							// 编码设备
							// 配置两路编码通道(音频编码和视频编码各一路)
							channelSchemeDao.save(channelSchemeService.createAudioAndVideoEncodeChannel(bundle));
						} else if (ldapEquip.getEquipType() == 3) {
							// 解码设备
							// 配置两路解码通道(音频解码和视频解码各一路)
							channelSchemeDao.save(channelSchemeService.createAudioAndVideoDecodeChannel(bundle));
						} else {
							// 按照模板最大通道数自动生成能力配置
							bundleService.configDefaultAbility(bundle);
						}
						successBundles.add(bundle);
						ExtraInfoPO extraInfo = new ExtraInfoPO();
						extraInfo.setName("extend_param");
						JSONObject params = new JSONObject();
						params.put("region", bundle.getEquipNode());
						extraInfo.setValue(params.toJSONString());
						extraInfo.setBundleId(bundle.getBundleId());
						successExtraInfos.add(extraInfo);
					}

				} catch (Exception e) {
					LOGGER.error("", e);
				}
			}

		}
		if (!successBundles.isEmpty()) {
			bundleDao.save(successBundles);
		}
		if(!successExtraInfos.isEmpty()){
			extraInfoDao.save(successExtraInfos);
		}
		return successBundles.size();
	}

	public int handleSyncToLdap(String syncAll) {
		
		List<BundlePO> bundlesToLdap = null;
		
		if (syncAll.equals("true")) {
			bundlesToLdap = bundleDao.findAllBundlesSyncToLdap();
			System.out.println("handleSyncToLdap syncAll==true, bundlesToLdap.size=" + bundlesToLdap.size());
		} else {
			bundlesToLdap = bundleDao.findBundlesSyncToLdap();
			System.out.println("handleSyncToLdap syncAll==false, bundlesToLdap.size=" + bundlesToLdap.size());

		}
		
		SerNodePO self = serNodeDao.findTopBySourceType(SOURCE_TYPE.SYSTEM);
		
		//本系统在ldap上的设备信息
		List<LdapEquipPo> allLdapEquips = ldapEquipDao.getEquipByNode(self.getNodeUuid());
		
		List<BundlePO> successBundles = new ArrayList<BundlePO>();
		for (BundlePO bundleToLdap : bundlesToLdap) {
			try {
				List<LdapEquipPo> oldLdapEquips = ldapEquipDao.getEquipByIUuid(bundleToLdap.getBundleId());
				if (!CollectionUtils.isEmpty(oldLdapEquips)) {
					LdapEquipPo ldapEquip = ldapEquipInfoUtil.pojoToLdap(bundleToLdap, self);
					if (null == ldapEquip) {
						continue;
					}
					ldapEquipDao.update(ldapEquip);
					
					if(syncAll.equals("true")){
						allLdapEquips.removeAll(oldLdapEquips);
					}
					
				} else {
					// 新建
					LdapEquipPo ldapEquip = ldapEquipInfoUtil.pojoToLdap(bundleToLdap, self);
					if (null == ldapEquip) {
						continue;
					}
					ldapEquipDao.save(ldapEquip);
				}

				bundleToLdap.setSyncStatus(SYNC_STATUS.SYNC);
				successBundles.add(bundleToLdap);
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}
		
		if(syncAll.equals("true") && allLdapEquips.size() > 0){
			ldapEquipDao.removeAll(allLdapEquips);
		}
		
		if (!successBundles.isEmpty()) {
			bundleDao.save(successBundles);
		}
		return successBundles.size();
	}

	public String handleCleanUpLdap() {

		// 删除本地上传到ldap的数据
		List<BundlePO> bundlePOList = bundleDao.findLocalDevsForCleanUpLdap();

		if (!CollectionUtils.isEmpty(bundlePOList)) {

			for (BundlePO bundlePO : bundlePOList) {

				try {

					List<LdapEquipPo> ldapEquipPoList = ldapEquipDao.getEquipByIUuid(bundlePO.getBundleId());

					if (!CollectionUtils.isEmpty(ldapEquipPoList)) {
						ldapEquipDao.remove(ldapEquipPoList.get(0));
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				bundlePO.setSyncStatus(SYNC_STATUS.ASYNC);
			}
			bundleDao.save(bundlePOList);
		}

		// 再删除从LDAP服务端下载的数据
		List<BundlePO> ldapBundlePOList = bundleDao.findBySourceType(SOURCE_TYPE.EXTERNAL);
		deleteLdapBundles(ldapBundlePOList);

		return "";
	}

	private void deleteLdapBundles(List<BundlePO> ldapBundlePOList) {
		if (!CollectionUtils.isEmpty(ldapBundlePOList)) {
			for (BundlePO ldapBundlePO : ldapBundlePOList) {
				if (ldapBundlePO.getUsername().endsWith("_encoder") || ldapBundlePO.getUsername().endsWith("_decoder")) {
					// ldap用户自动生成的编码器和解码器不是从ldap服务器上同步下来的，不删
					continue;
				}
				// 删除配置能力
				channelSchemeService.deleteByBundleId(ldapBundlePO.getBundleId());
				lockChannelParamDao.deleteByBundleId(ldapBundlePO.getBundleId());
				lockBundleParamDao.deleteByBundleId(ldapBundlePO.getBundleId());

				bundleDao.delete(ldapBundlePO);
			}
		}
	}

}
