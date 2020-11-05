package com.suma.venus.resource.externalinterface;

import java.util.Map.Entry;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessLayerParam;
import com.suma.venus.resource.base.bo.BatchLockBundleParam;
import com.suma.venus.resource.base.bo.BatchLockBundleRespBody;
import com.suma.venus.resource.base.bo.BatchLockBundleRespParam;
import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.base.bo.ChannelStatusParam;
import com.suma.venus.resource.base.bo.CreateResourceParam;
import com.suma.venus.resource.base.bo.CreateResourceRespParam;
import com.suma.venus.resource.base.bo.DeleteResourceParam;
import com.suma.venus.resource.base.bo.DeleteResourceRespParam;
import com.suma.venus.resource.base.bo.LayerBody;
import com.suma.venus.resource.base.bo.LockBundleParam;
import com.suma.venus.resource.base.bo.LockBundleRespParam;
import com.suma.venus.resource.base.bo.LockChannelParam;
import com.suma.venus.resource.base.bo.LockChannelRespParam;
import com.suma.venus.resource.base.bo.ReleaseChannelRespParam;
import com.suma.venus.resource.base.bo.ReleaseChannelResponseBody;
import com.suma.venus.resource.base.bo.ResponseBody;
import com.suma.venus.resource.base.bo.ResultBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.RoleBO;
import com.suma.venus.resource.base.bo.RoleResultBO;
import com.suma.venus.resource.base.bo.UpdateResourceParam;
import com.suma.venus.resource.base.bo.UpdateResourceRespParam;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserResultBO;
import com.suma.venus.resource.bo.CreateBundleRequest;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.dao.VirtualResourceDao;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.VirtualResourcePO;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.WorkNodeService;

@Service
public class InterfaceToResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceToResource.class);

	@Autowired
	private BundleService bundleService;

	@Autowired
	private VirtualResourceDao virtualResourceDao;

	@Autowired
	private WorkNodeService workNodeService;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private ExtraInfoService extraInfoService;

	@Autowired
	private UpdateSingleChannelUtil updateSingleChannelUtil;

	@Autowired
	private UpdateSingleBundleUtil updateSingleBundleUtil;

	@Autowired
	private UpdateBatchBundlesUtil updateBatchBundlesUtil;

	@Autowired
	private FolderDao folderDao;

	/** 锁定bundle */
	@Transactional(rollbackFor = Exception.class)
	public LockBundleRespParam lockBundle(LockBundleParam lockParam) throws Exception {
		LockBundleRespParam resp = new LockBundleRespParam();

		// 并发操作统一bundle时需加锁，锁定完成后提交事务
		synchronized (lockParam.getBundleId().intern()) {
			BundlePO bundle = updateSingleBundleUtil.lockAndUpdateSingleBundle(lockParam);
			resp.setOperate_index(bundle.getOperateIndex());
			resp.setOperate_count(bundle.getOperateCount());
		}

		resp.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/** 批量锁定bundle */
	@Deprecated
	@Transactional(rollbackFor = Exception.class)
	public BatchLockBundleRespParam batchlockBundle(BatchLockBundleParam batchLockBundleParam) throws Exception {

		BatchLockBundleRespParam resp = new BatchLockBundleRespParam();
		List<BatchLockBundleRespBody> batchLockBundleRespBodyList = new ArrayList<BatchLockBundleRespBody>();
		int successCnt = 0;

		for (LockBundleParam lockBundleParam : batchLockBundleParam.getBundles()) {

			BatchLockBundleRespBody lockBundleRespBody = new BatchLockBundleRespBody();

			synchronized (lockBundleParam.getBundleId().intern()) {
				BundlePO bundle = updateSingleBundleUtil.lockAndUpdateSingleBundle(lockBundleParam);

				lockBundleRespBody.setBundleId(lockBundleParam.getBundleId());
				lockBundleRespBody.setOperateResult(true);
				lockBundleRespBody.setOperate_count(bundle.getOperateCount());
				lockBundleRespBody.setOperate_index(bundle.getOperateIndex());
				batchLockBundleRespBodyList.add(lockBundleRespBody);
				successCnt++;
			}
		}

		resp.setSuccessCnt(successCnt);
		resp.setOperateBundlesResult(batchLockBundleRespBodyList);
		resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);

		return resp;
	}

	/** 批量锁定bundle */
	// TODO
	public BatchLockBundleRespParam batchlockBundleNew(BatchLockBundleParam batchLockBundleParam) throws Exception {

		// 使用map建索引
		Map<String, LockBundleParam> lockBundleParamsMap = new HashMap<>();
		Map<String, Integer> lockBundleCountMap = new HashMap<>();

		Set<String> bundleIdSet = new HashSet<>();

		for (LockBundleParam lockBundleParam : batchLockBundleParam.getBundles()) {

			if (lockBundleCountMap.get(lockBundleParam.getBundleId()) == null) {
				lockBundleCountMap.put(lockBundleParam.getBundleId(), 1);
			} else {
				Integer tempCount = lockBundleCountMap.get(lockBundleParam.getBundleId());
				lockBundleCountMap.put(lockBundleParam.getBundleId(), tempCount + 1);
			}

			lockBundleParamsMap.put(lockBundleParam.getBundleId(), lockBundleParam);
			bundleIdSet.add(lockBundleParam.getBundleId());
		}
		
		LOGGER.info("lockBundleCountMap= " + JSONObject.toJSONString(lockBundleCountMap));
		
		synchronized (this) {
			return updateBatchBundlesUtil.lockAndUpdateBatchBundles(lockBundleParamsMap, lockBundleCountMap,
					bundleIdSet, batchLockBundleParam.getUserId(), batchLockBundleParam.isMustLockAll());
		}

	}

	/** 释放bundle */
	@Transactional(rollbackFor = Exception.class)
	public LockBundleRespParam releaseBundle(LockBundleParam releaseParam, String businessMode) throws Exception {
		String bundleId = releaseParam.getBundleId();
		LockBundleRespParam resp = new LockBundleRespParam();

		// 并发操作统一bundle时需加锁，释放完成后提交事务
		synchronized (bundleId.intern()) {
			BundlePO bundle = updateSingleBundleUtil.unlockAndUpdateSingleBundle(businessMode, bundleId,
					releaseParam.isOperateCountSwitch());
			resp.setOperate_index(bundle.getOperateIndex());
			resp.setOperate_count(bundle.getOperateCount());
		}

		resp.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/** 批量释放bundle */
	@Deprecated
	@Transactional(rollbackFor = Exception.class)
	public BatchLockBundleRespParam batchReleaseBundle(BatchLockBundleParam batchReleaseParam, String businessMode)
			throws Exception {

		BatchLockBundleRespParam resp = new BatchLockBundleRespParam();
		List<BatchLockBundleRespBody> batchReleaseBundleRespBodyList = new ArrayList<BatchLockBundleRespBody>();
		int successCnt = 0;

		String bundleId = null;

		for (LockBundleParam releaseBundleParam : batchReleaseParam.getBundles()) {

			bundleId = releaseBundleParam.getBundleId();
			BatchLockBundleRespBody releaseBundleRespBody = new BatchLockBundleRespBody();

			synchronized (bundleId.intern()) {
				BundlePO bundle = updateSingleBundleUtil.unlockAndUpdateSingleBundle(businessMode, bundleId,
						releaseBundleParam.isOperateCountSwitch());

				releaseBundleRespBody.setBundleId(releaseBundleParam.getBundleId());
				releaseBundleRespBody.setOperateResult(true);
				releaseBundleRespBody.setOperate_count(bundle.getOperateCount());
				releaseBundleRespBody.setOperate_index(bundle.getOperateIndex());
				batchReleaseBundleRespBodyList.add(releaseBundleRespBody);
				successCnt++;
			}

		}

		resp.setSuccessCnt(successCnt);
		resp.setOperateBundlesResult(batchReleaseBundleRespBodyList);
		resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		return resp;

	}

	/** 批量释放bundle */
	@Transactional(rollbackFor = Exception.class)
	public BatchLockBundleRespParam batchReleaseBundleNew(BatchLockBundleParam batchReleaseParam, String businessMode)
			throws Exception {

		// 使用map建索引
		Map<String, LockBundleParam> releaseBundleParamsMap = new HashMap<>();
		Map<String, Integer> releaseCountMap = new HashMap<>();
		
		Set<String> bundleIdSet = new HashSet<>();
		
		

		for (LockBundleParam releaseBundleParam : batchReleaseParam.getBundles()) {
			
			if (releaseCountMap.get(releaseBundleParam.getBundleId()) == null) {
				releaseCountMap.put(releaseBundleParam.getBundleId(), 1);
			} else {
				Integer tempCount = releaseCountMap.get(releaseBundleParam.getBundleId());
				releaseCountMap.put(releaseBundleParam.getBundleId(), tempCount + 1);
			}
			
			releaseBundleParamsMap.put(releaseBundleParam.getBundleId(), releaseBundleParam);
			bundleIdSet.add(releaseBundleParam.getBundleId());
		}
		
		LOGGER.info("releaseCountMap= " + JSONObject.toJSONString(releaseCountMap));

		synchronized (this) {
			return updateBatchBundlesUtil.unlockAndUpdateBatchBundles(releaseBundleParamsMap, releaseCountMap, bundleIdSet,
					businessMode);
		}

	}

	/**
	 * 锁定通道
	 * 
	 * @param lockChannelParam
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public LockChannelRespParam lockChannel(LockChannelParam lockChannelParam) throws Exception {
		LockChannelRespParam resp = new LockChannelRespParam();
		ReleaseChannelResponseBody respBody = new ReleaseChannelResponseBody();
		resp.setLock_channel_response(respBody);
		List<ChannelStatusParam> channelStatusParams = new ArrayList<ChannelStatusParam>();
		respBody.setChannels(channelStatusParams);

		for (ChannelBody channel : lockChannelParam.getChannels()) {
			String channel_identifier = channel.getBundle_id() + channel.getChannel_id();
			// 并发操作同一通道时加锁,完成单个通道锁定后提交事务
			synchronized (channel_identifier.intern()) {
				ChannelSchemePO channelSchemePO = updateSingleChannelUtil.lockAndUpdateSingleChannel(lockChannelParam,
						channel);
				channelStatusParams.add(new ChannelStatusParam(channel.getBundle_id(), channel.getChannel_id(),
						channelSchemePO.getOperateIndex()));
			}
		}
		respBody.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/**
	 * 释放通道
	 * 
	 * @param releaseChannelParam
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public ReleaseChannelRespParam releaseChannel(LockChannelParam releaseChannelParam) throws Exception {
		ReleaseChannelRespParam resp = new ReleaseChannelRespParam();
		ReleaseChannelResponseBody respBody = new ReleaseChannelResponseBody();
		resp.setRelease_channel_response(respBody);
		List<ChannelStatusParam> channelStatusParams = new ArrayList<ChannelStatusParam>();
		respBody.setChannels(channelStatusParams);

		for (ChannelBody channel : releaseChannelParam.getChannels()) {
			String channel_identifier = channel.getBundle_id() + channel.getChannel_id();
			// 并发操作同一通道时需加锁,完成单个通道锁定后提交事务
			synchronized (channel_identifier.intern()) {
				ChannelSchemePO channelSchemePO = updateSingleChannelUtil.unlockAndUpdateSinggleChannel(channel);
				channelStatusParams.add(new ChannelStatusParam(channel.getBundle_id(), channel.getChannel_id(),
						channelSchemePO.getOperateIndex()));
			}
		}

		respBody.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/** 对外接口:创建bundle */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> createBundle(CreateBundleRequest createBundleRequest) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
//		CreateBundleResponse response = new CreateBundleResponse();
		BundleBody bundle = createBundleRequest.getCreate_bundle_request().getBundle();
		BundlePO bundlePO = new BundlePO();
		bundlePO.setDeviceModel(bundle.getDevice_model());
		bundlePO.setBundleType(bundle.getBundle_type());
		bundlePO.setBundleName(bundle.getBundle_name());
		bundlePO.setUsername(bundle.getUsername());
		bundlePO.setOnlinePassword(bundle.getPassword());
//			bundlePO.setAccessNodeUid(bundle.getAccess_node_uid());
		bundlePO.setBundleId(BundlePO.createBundleId());
		/** 针对合屏混音设备，需设置音频/视频编码最大路数 */
		if ("mixer".equalsIgnoreCase(bundlePO.getDeviceModel())) {
			bundlePO.setMaxAudioSrcCnt(VenusParamConstant.MIXER_MAX_AUDIO_SRC_CNT);
			bundlePO.setMaxVideoSrcCnt(VenusParamConstant.MIXER_MAX_VIDEO_SRC_CNT);
			bundlePO.setFreeAudioSrcCnt(bundlePO.getMaxAudioSrcCnt());
			bundlePO.setFreeVideoSrcCnt(bundlePO.getMaxVideoSrcCnt());
		}

		if (null != bundlePO.getUsername() && !bundlePO.getUsername().isEmpty()) {
			if (null != bundleService.findByUsername(bundlePO.getUsername())) {
				LOGGER.error("Fail to create bundle : 设备账号已存在");
				result.put("result", com.suma.venus.resource.base.bo.ResponseBody.FAIL);
				return result;
			}

			// 通知用户权限服务创建虚拟用户
			/*
			 * Boolean beDevice = BundlePO.beDeviceBundle(bundlePO.getDeviceModel());
			 * UserResultBO userResult = userFeign.createVirtualUser(bundlePO.getUsername(),
			 * bundlePO.getOnlinePassword(), beDevice); if (null == userResult || null ==
			 * userResult.getUserId()) { LOGGER.error("Fail to create bundle : 创建设备账号失败");
			 * result.put("result", com.suma.venus.resource.bo.ResponseBody.FAIL); return
			 * result; }
			 */

			// 通知用户权限服务创建虚拟角色
			/*
			 * RoleResultBO roleResult =
			 * userFeign.createVirtualRole(bundlePO.getUsername()); if (null == roleResult
			 * || null == roleResult.getRoleId()) {
			 * LOGGER.error("Fail to create bundle : 创建用户角色失败"); result.put("result",
			 * com.suma.venus.resource.bo.ResponseBody.FAIL); return result; }
			 */

			// 绑定虚拟角色和bundle权限
			/*
			 * RoleAndResourceIdBO roleAndResourceIdBO = new RoleAndResourceIdBO();
			 * roleAndResourceIdBO.setRoleId(roleResult.getRoleId());
			 * roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
			 * roleAndResourceIdBO.getResourceCodes().add(bundlePO.getBundleId()); ResultBO
			 * bindResult = userFeign.bindRolePrivilege(roleAndResourceIdBO); if (null ==
			 * bindResult || !bindResult.isResult()) {
			 * LOGGER.error("Fail to create bundle : 用户绑定设备失败"); result.put("result",
			 * com.suma.venus.resource.bo.ResponseBody.FAIL); return result; }
			 */

			// 给管理员默认角色绑定该设备权限
			/*
			 * Map<String, Object> adminRoleResult = userFeign.queryRoleByName("管理员默认角色");
			 * RoleBO defaultAdminRole =
			 * JSONObject.parseObject(JSONObject.toJSONString(adminRoleResult.get("role")),
			 * RoleBO.class); roleAndResourceIdBO.setRoleId(defaultAdminRole.getId());
			 * roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
			 * roleAndResourceIdBO.getResourceCodes().add(bundlePO.getBundleId() + "-r");
			 * roleAndResourceIdBO.getResourceCodes().add(bundlePO.getBundleId() + "-w");
			 * bindResult = userFeign.bindRolePrivilege(roleAndResourceIdBO); if (null ==
			 * bindResult || !bindResult.isResult()) {
			 * LOGGER.error("Fail to create bundle : 管理员绑定设备失败"); result.put("result",
			 * com.suma.venus.resource.bo.ResponseBody.FAIL); return result; }
			 */
		}
		// 设备默认分组
		bundlePO.setFolderId(folderDao.findByParentId(-1l).get(0).getId());

		bundleService.save(bundlePO);
		if (null != bundle.getExtra_info()) {
			for (Entry<String, Object> entry : bundle.getExtra_info().entrySet()) {
				ExtraInfoPO extraInfoPO = new ExtraInfoPO();
				extraInfoPO.setName(entry.getKey());
				extraInfoPO.setValue(String.valueOf(entry.getValue()));
				extraInfoPO.setBundleId(bundlePO.getBundleId());
				extraInfoService.save(extraInfoPO);
			}
		}

		// 生成默认通道配置
		bundleService.configDefaultAbility(bundlePO);

		result.put("result", com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		result.put("bundle_id", bundlePO.getBundleId());
		return result;
	}

	/**
	 * 创建资源
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public CreateResourceRespParam createResource(CreateResourceParam param) throws Exception {
		CreateResourceRespParam resp = new CreateResourceRespParam();
		ResponseBody respBody = new ResponseBody();
		resp.setCreate_resource_response(respBody);

		JSONObject resource = param.getResource();
		String resourceId = BundlePO.createBundleId();
		List<VirtualResourcePO> virtualResourcePOs = new ArrayList<VirtualResourcePO>();
		for (Entry<String, Object> entry : resource.entrySet()) {
			virtualResourcePOs.add(new VirtualResourcePO(entry.getKey(), String.valueOf(entry.getValue()), resourceId));
		}
		// 添加创建用户ID(userId)字段
		virtualResourcePOs.add(new VirtualResourcePO("userId", String.valueOf(param.getUserId()), resourceId));

		virtualResourceDao.save(virtualResourcePOs);

		// user和资源之间的权限关系待补充
		Long userId = param.getUserId();
		if (null != userId) {
//			VirtualResourcePO po = new VirtualResourcePO("userId", String.valueOf(userId),resourceId);
//			virtualResourceService.save(po);
			// 绑定资源权限
			UserAndResourceIdBO userAndResourceIds = new UserAndResourceIdBO();
			userAndResourceIds.setUserId(userId);
			List<String> resourceCodes = new ArrayList<String>();
			resourceCodes.add(resourceId);
			userAndResourceIds.setResourceCodes(resourceCodes);
			ResultBO result = userFeign.bindUserPrivilege(userAndResourceIds);
			if (null == result || !result.isResult()) {
				LOGGER.error("Fail to bind virtual resourceId : " + resourceId + " on user which id=" + userId);
			}
		}

		respBody.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/**
	 * 更新资源
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public UpdateResourceRespParam updateResource(UpdateResourceParam param) throws Exception {
		UpdateResourceRespParam resp = new UpdateResourceRespParam();
		ResponseBody respBody = new ResponseBody();
		resp.setUpdate_resource_response(respBody);

		// TODO user和资源之间的权限关系待补充
		// Long userId = param.getUserId();
		String resourceId = param.getResourceId();
		JSONObject attrs = param.getAttrs();
		List<VirtualResourcePO> virtualResourcePOs = new ArrayList<VirtualResourcePO>();
		for (Entry<String, Object> entry : attrs.entrySet()) {
			String attrName = entry.getKey();
			VirtualResourcePO po = virtualResourceDao.findByResourceIdAndAttrName(resourceId, attrName);
			if (null == po) {
				po = new VirtualResourcePO(attrName, String.valueOf(entry.getValue()), resourceId);
			} else {
				po.setAttrValue(String.valueOf(entry.getValue()));
			}
			virtualResourcePOs.add(po);
		}

		virtualResourceDao.save(virtualResourcePOs);
		respBody.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/**
	 * 接入层节点上线认证
	 * 
	 * @param accessLayerParam
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public ResponseBody layerOnlineCertify(AccessLayerParam accessLayerParam) throws Exception {
		ResponseBody resp = new ResponseBody();
		LayerBody layerBody = accessLayerParam.getAccess_layer();
		WorkNodePO node = workNodeService.findByNodeUid(layerBody.getLayer_id());
		// TODO 校验layer密码key?
		node.setOnlineStatus(ONLINE_STATUS.ONLINE);
		workNodeService.save(node);

		resp.setResult(ResponseBody.SUCCESS);
		return resp;
	}

	/**
	 * 删除资源
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public DeleteResourceRespParam deleteResource(DeleteResourceParam param) throws Exception {
		DeleteResourceRespParam resp = new DeleteResourceRespParam();
		ResponseBody respBody = new ResponseBody();
		resp.setDelete_resource_response(respBody);

		// 判断用户和资源权限关系
		// Long userId = param.getUserId();
		String resourceId = param.getResourceId();
//		if(!resourceService.hasPrivilege(userId, resourceId)){
//			throw new Exception(ErrorCode.WITHOUT_PERMISSION.toString());
//		}

		List<VirtualResourcePO> pos = virtualResourceDao.findByResourceId(resourceId);
		if (!pos.isEmpty()) {
			virtualResourceDao.delete(pos);
		}

		respBody.setResult(ResponseBody.SUCCESS);
		return resp;
	}

}
