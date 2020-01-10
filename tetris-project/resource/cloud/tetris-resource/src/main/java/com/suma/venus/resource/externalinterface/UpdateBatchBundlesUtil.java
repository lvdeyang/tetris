package com.suma.venus.resource.externalinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.BatchLockBundleRespBody;
import com.suma.venus.resource.base.bo.BatchLockBundleRespParam;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.base.bo.LockBundleParam;
import com.suma.venus.resource.base.bo.ScreenBody;
import com.suma.venus.resource.constant.ErrorCode;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.dao.LockScreenParamDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.LockBundleParamPO;
import com.suma.venus.resource.pojo.LockChannelParamPO;
import com.suma.venus.resource.pojo.LockScreenParamPO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;

/**
 * 
 * @author chenmo
 *
 */
@Service
public class UpdateBatchBundlesUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateBatchBundlesUtil.class);

	@Autowired
	private BundleDao bundleDao;

	// @Autowired
	// private ChannelSchemeService channelSchemeService;

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Autowired
	private LockBundleParamDao lockBundleParamDao;

	// @Autowired
	// private LockChannelParamService lockChannelParamService;

	@Autowired
	private LockChannelParamDao lockChannelParamDao;

	@Autowired
	private LockScreenParamDao lockScreenParamDao;

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public BatchLockBundleRespParam lockAndUpdateBatchBundles(Map<String, LockBundleParam> lockBundleParamsMap, Set<String> bundleIdSet, Long userId, boolean mustLockAll)
			throws Exception {

		long startTime = System.currentTimeMillis();

		BatchLockBundleRespParam resp = new BatchLockBundleRespParam();
		List<BatchLockBundleRespBody> batchLockBundleRespBodyList = new ArrayList<BatchLockBundleRespBody>();
		int successCnt = 0;

		List<BundlePO> originBundlePOList = bundleDao.findByBundleIdIn(bundleIdSet);
		LOGGER.info("lockAndUpdateBatchBundles, query all BundlePO time=" + (System.currentTimeMillis() - startTime) + ", originBundlePOList size=" + originBundlePOList.size());

		// 先将涉及的bundlePO统一查出，在内存中处理
		// lockBundlePOList是原始查出的数据List
		// bundlePOList 是待提交到数据库修改的数据List
		List<LockBundleParamPO> busyLockBundleParamPOList = lockBundleParamDao.findByBundleIdIn(bundleIdSet);
		Map<String, LockBundleParamPO> lockBundleParamPOMap = new HashMap<String, LockBundleParamPO>();
		if (!CollectionUtils.isEmpty(busyLockBundleParamPOList)) {
			for (LockBundleParamPO lockBundleParamPO : busyLockBundleParamPOList) {
				lockBundleParamPOMap.put(lockBundleParamPO.getBundleId(), lockBundleParamPO);
			}
		}

		LOGGER.info(
				"lockAndUpdateBatchBundles, query and reassemble all LockBundleParamPO time=" + (System.currentTimeMillis() - startTime) + " ,size=" + lockBundleParamPOMap.size());

		// 将涉及的 LockChannelParamPO 统一查出 在内存中处理
		// originlockChannelParamPOList 是原始查出的 数据list
		// lockChannelParamPOList 是待提交到数据库修改的数据list
		List<LockChannelParamPO> originlockChannelParamPOList = lockChannelParamDao.findByBundleIdIn(bundleIdSet);
		Map<String, LockChannelParamPO> lockChannelParamPOMap = new HashMap<String, LockChannelParamPO>();
		if (!CollectionUtils.isEmpty(originlockChannelParamPOList)) {

			for (LockChannelParamPO lockChannelParamPO : originlockChannelParamPOList) {
				lockChannelParamPOMap.put(lockChannelParamPO.getBundleId() + "-" + lockChannelParamPO.getChannelId(), lockChannelParamPO);
			}
		}

		LOGGER.info("lockAndUpdateBatchBundles, query and reassemble all LockChannelParamPO time=" + (System.currentTimeMillis() - startTime) + " ,size="
				+ lockChannelParamPOMap.size());

		// 将涉及的 ChannelSchemePO 统一查出 在内存中处理
		// originChannelSchemePOList 是原始查出的 数据list
		// channelSchemePOList 是待提交到数据库修改的数据list
		List<ChannelSchemePO> originChannelSchemePOList = channelSchemeDao.findByBundleIdIn(bundleIdSet);
		Map<String, ChannelSchemePO> channelSchemePOMap = new HashMap<String, ChannelSchemePO>();

		if (!CollectionUtils.isEmpty(originChannelSchemePOList)) {
			for (ChannelSchemePO channelSchemePO : originChannelSchemePOList) {
				channelSchemePOMap.put(channelSchemePO.getBundleId() + "-" + channelSchemePO.getChannelId(), channelSchemePO);
			}
		}

		LOGGER.info("lockAndUpdateBatchBundles, query and reassemble all ChannelSchemePO time=" + (System.currentTimeMillis() - startTime) + " ,size=" + channelSchemePOMap.size());

		// 将涉及的 ScreenSchemePO 统一查出 在内存中处理
		// originSceenSchemePOList 是原始查出的 数据list
		// screenSchemePOList 是待提交到数据库修改的数据list
		List<ScreenSchemePO> originSceenSchemePOList = screenSchemeDao.findByBundleIdIn(bundleIdSet);
		Map<String, ScreenSchemePO> screenSchemePOMap = new HashMap<String, ScreenSchemePO>();
		if (!CollectionUtils.isEmpty(originSceenSchemePOList)) {
			for (ScreenSchemePO screenSchemePO : originSceenSchemePOList) {
				screenSchemePOMap.put(screenSchemePO.getBundleId() + "-" + screenSchemePO.getScreenId(), screenSchemePO);
			}
		}

		// LOGGER.info("lockAndUpdateBatchBundles, query and reassemble all ScreenSchemePO time=" + (System.currentTimeMillis() - startTime) + " ,size=" + screenSchemePOMap.size());

		// 将涉及的 LockScreenParamPO 统一查出 在内存中处理
		// originLockScreenParamPOList 是原始查出的 数据list
		// lockScreenParamPOList 是待提交到数据库修改的数据list
		List<LockScreenParamPO> originLockScreenParamPOList = lockScreenParamDao.findByBundleIdIn(bundleIdSet);
		Map<String, LockScreenParamPO> lockScreenParamPOMap = new HashMap<String, LockScreenParamPO>();

		if (!CollectionUtils.isEmpty(originLockScreenParamPOList)) {
			for (LockScreenParamPO lockScreenParamPO : originLockScreenParamPOList) {
				lockScreenParamPOMap.put(lockScreenParamPO.getBundleId() + "-" + lockScreenParamPO.getScreenId(), lockScreenParamPO);
			}
		}

		//LOGGER.info(
		//		"lockAndUpdateBatchBundles, query and reassemble all LockScreenParamPO time=" + (System.currentTimeMillis() - startTime) + " ,size=" + lockScreenParamPOMap.size());

		// 待批量提交到数据库操作的 所有 List
		List<BundlePO> bundlePOList = new ArrayList<BundlePO>();
		List<ChannelSchemePO> channelSchemePOList = new ArrayList<ChannelSchemePO>();
		List<LockChannelParamPO> lockChannelParamPOList = new ArrayList<LockChannelParamPO>();
		List<ScreenSchemePO> screenSchemePOList = new ArrayList<ScreenSchemePO>();
		List<LockScreenParamPO> lockScreenParamPOList = new ArrayList<LockScreenParamPO>();
		List<LockBundleParamPO> lockBundleParamPOList = new ArrayList<LockBundleParamPO>();

		for (BundlePO bundlePO : originBundlePOList) {

			BatchLockBundleRespBody lockBundleRespBody = new BatchLockBundleRespBody();

			LockBundleParam lockParam = lockBundleParamsMap.get(bundlePO.getBundleId());

			if (LockStatus.IDLE == bundlePO.getLockStatus()) {
				// bundle当前未被锁定
				bundlePO.setLockStatus(LockStatus.BUSY);
				bundlePO.setOperateIndex(bundlePO.getOperateIndex() + 1);

				// 是否需要计数
				if (lockParam.isOperateCountSwitch()) {
					if (null == bundlePO.getOperateCount() || bundlePO.getOperateCount() < 0) {
						bundlePO.setOperateCount(0);
					}
					bundlePO.setOperateCount(bundlePO.getOperateCount() + 1);
				}

				bundlePOList.add(bundlePO);

				operateLockChannelParam(lockParam, channelSchemePOList, lockChannelParamPOList, channelSchemePOMap);

				operateLockScreenParam(lockParam, screenSchemePOList, lockScreenParamPOList, screenSchemePOMap);

				LockBundleParamPO lockBundleParamPO = new LockBundleParamPO();
				lockBundleParamPO.setBundleId(lockParam.getBundleId());
				lockBundleParamPO.setUserId(lockParam.getUserId());
				String pass_by_str = lockParam.getPassByStr();
				if (null != pass_by_str && !pass_by_str.isEmpty()) {
					lockBundleParamPO.setPassByStr(pass_by_str);
				}
				lockBundleParamPOList.add(lockBundleParamPO);

			} else {
				// bundle之前已经被锁定
				LockBundleParamPO lockBundleParamPO = lockBundleParamPOMap.get(bundlePO.getBundleId());
				if (null != lockBundleParamPO && !lockBundleParamPO.getUserId().equals(lockParam.getUserId())) {

					LOGGER.info("one bundle lock failed, lockBundleParamPO=" + JSONObject.toJSONString(lockBundleParamPO));

					if (mustLockAll) {
						throw new Exception(ErrorCode.BUNDLE_BUSY.toString());
					} else {
						lockBundleRespBody.setBundleId(bundlePO.getBundleId());
						lockBundleRespBody.setOperateResult(false);
						batchLockBundleRespBodyList.add(lockBundleRespBody);
						continue;
					}
				}

				if (null == lockBundleParamPO) {
					lockBundleParamPO = new LockBundleParamPO();
					lockBundleParamPO.setBundleId(lockParam.getBundleId());
					lockBundleParamPO.setUserId(lockParam.getUserId());
				}

				// 设置新的passbystr参数
				String pass_by_str = lockParam.getPassByStr();
				if (null != pass_by_str && !pass_by_str.isEmpty()) {
					lockBundleParamPO.setPassByStr(pass_by_str);
				}

				// lockBundleParamDao.save(lockBundleParamPO);
				lockBundleParamPOList.add(lockBundleParamPO);

				if (null != lockParam.getChannels()) {
					for (ChannelBody channelBody : lockParam.getChannels()) {

						// TODO 这个查询暂时不优化,彻底优化的话需要修改数据结构
						// LockChannelParamPO lockChannelParamPO =
						// lockChannelParamService.findByBundleIdAndChannelId(lockParam.getBundleId(),
						// channelBody.getChannel_id());

						LockChannelParamPO lockChannelParamPO = lockChannelParamPOMap.get(lockParam.getBundleId() + "-" + channelBody.getChannel_id());
						if (null != lockChannelParamPO) {
							// 该通道上存在任务参数
							lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channelBody.getChannel_param()));
							// lockChannelParamService.save(lockChannelParamPO);
							lockChannelParamPOList.add(lockChannelParamPO);

						} else {

							// 该通道上不存在任务参数
							lockChannelParamPO = new LockChannelParamPO();
							lockChannelParamPO.setBundleId(lockParam.getBundleId());
							lockChannelParamPO.setChannelId(channelBody.getChannel_id());
							lockChannelParamPO.setUserId(lockParam.getUserId());
							lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channelBody.getChannel_param()));
							// lockChannelParamService.save(lockChannelParamPO);
							lockChannelParamPOList.add(lockChannelParamPO);

							// ChannelSchemePO channelSchemePO =
							// channelSchemeService.findByBundleIdAndChannelId(lockParam.getBundleId(),
							// channelBody.getChannel_id());
							ChannelSchemePO channelSchemePO = channelSchemePOMap.get(lockParam.getBundleId() + "-" + channelBody.getChannel_id());

							channelSchemePO.setChannelStatus(LockStatus.BUSY);
							channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
							// channelSchemeService.save(channelSchemePO);
							channelSchemePOList.add(channelSchemePO);

						}
					}
				}

				if (null != lockParam.getScreens()) {
					for (ScreenBody screenBody : lockParam.getScreens()) {
						// ScreenSchemePO screenSchemePO =
						// screenSchemeDao.findByBundleIdAndScreenId(lockParam.getBundleId(),
						// screenBody.getScreen_id());
						ScreenSchemePO screenSchemePO = screenSchemePOMap.get(lockParam.getBundleId() + "-" + screenBody.getScreen_id());

						if (null != screenSchemePO) {
							if (LockStatus.IDLE.equals(screenSchemePO.getStatus())) {
								// 屏之前未被锁定
								screenSchemePO.setStatus(LockStatus.BUSY);
								// screenSchemeDao.save(screenSchemePO);
								screenSchemePOList.add(screenSchemePO);

								LockScreenParamPO lockScreenParamPO = new LockScreenParamPO();
								lockScreenParamPO.setBundleId(lockParam.getBundleId());
								lockScreenParamPO.setScreenId(screenBody.getScreen_id());
								lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
								// lockScreenParamDao.save(lockScreenParamPO);
								lockScreenParamPOList.add(lockScreenParamPO);
							} else {
								// 屏之前已被锁定
								// LockScreenParamPO lockScreenParamPO =
								// lockScreenParamDao.findByBundleIdAndScreenId(lockParam.getBundleId(),
								// screenBody.getScreen_id());
								LockScreenParamPO lockScreenParamPO = lockScreenParamPOMap.get(lockParam.getBundleId() + "-" + screenBody.getScreen_id());

								if (lockScreenParamPO == null) {
									
									//兜底 走到这里其实是数据库之前的操作有异常了
									lockScreenParamPO = new LockScreenParamPO();
									lockScreenParamPO.setBundleId(lockParam.getBundleId());
									lockScreenParamPO.setScreenId(screenBody.getScreen_id());
									lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
									// lockScreenParamDao.save(lockScreenParamPO);
									lockScreenParamPOList.add(lockScreenParamPO);
								} else {
									lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
									// lockScreenParamDao.save(lockScreenParamPO);
									lockScreenParamPOList.add(lockScreenParamPO);
								}
							}
						}
					}
				}

				bundlePO.setOperateIndex(bundlePO.getOperateIndex() + 1);

				// 是否需要计数
				if (lockParam.isOperateCountSwitch()) {
					if (null == bundlePO.getOperateCount() || bundlePO.getOperateCount() < 0) {
						bundlePO.setOperateCount(0);
					}
					bundlePO.setOperateCount(bundlePO.getOperateCount() + 1);
				}

				// bundleService.save(bundle);
				bundlePOList.add(bundlePO);
			}

			lockBundleRespBody.setBundleId(bundlePO.getBundleId());
			lockBundleRespBody.setOperateResult(true);
			lockBundleRespBody.setOperate_count(bundlePO.getOperateCount());
			lockBundleRespBody.setOperate_index(bundlePO.getOperateIndex());
			batchLockBundleRespBodyList.add(lockBundleRespBody);
			successCnt++;
		}

		//LOGGER.info("lockAndUpdateBatchBundles,logical operate time=" + (System.currentTimeMillis() - startTime));

		// 这种save的效率 ?
		// 经测试，效率还可
		bundleDao.save(bundlePOList);
		channelSchemeDao.save(channelSchemePOList);
		lockChannelParamDao.save(lockChannelParamPOList);
		screenSchemeDao.save(screenSchemePOList);
		lockScreenParamDao.save(lockScreenParamPOList);
		lockBundleParamDao.save(lockBundleParamPOList);

		LOGGER.info("lockAndUpdateBatchBundles after time=" + (System.currentTimeMillis() - startTime) + " , bundlePOList size=" + bundlePOList.size()
				+ " , channelSchemePOList size=" + channelSchemePOList.size() + " , lockChannelParamPOList size=" + lockChannelParamPOList.size() + " , screenSchemePOList size="
				+ screenSchemePOList.size() + " , lockScreenParamPOList size=" + lockScreenParamPOList.size() + " , lockBundleParamPOList size=" + lockBundleParamPOList.size());

		resp.setSuccessCnt(successCnt);
		resp.setOperateBundlesResult(batchLockBundleRespBodyList);

		if (successCnt == 0) {
			resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.FAIL);
		} else {
			resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		}

		return resp;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public BatchLockBundleRespParam unlockAndUpdateBatchBundles(Map<String, LockBundleParam> releaseBundleParamMap, Set<String> bundleIdSet, String businessMode) {
		long startTime = System.currentTimeMillis();

		BatchLockBundleRespParam resp = new BatchLockBundleRespParam();
		List<BatchLockBundleRespBody> batchReleaseBundleRespBodyList = new ArrayList<BatchLockBundleRespBody>();
		int successCnt = 0;

		List<BundlePO> releaseBundlePOList = bundleDao.findByBundleIdIn(bundleIdSet);
		LOGGER.info(
				"unlockAndUpdateBatchBundles, query all BundlePO time=" + (System.currentTimeMillis() - startTime) + ", releaseBundlePOList size=" + releaseBundlePOList.size());

		// 将涉及的 ChannelSchemePO 统一查出 在内存中处理
		// originChannelSchemePOList 是原始查出的 数据list
		// channelSchemePOList 是待提交到数据库修改的数据list
		List<ChannelSchemePO> originChannelSchemePOList = channelSchemeDao.findByBundleIdIn(bundleIdSet);

		// TODO lamda 过滤
		List<ChannelSchemePO> originBusyChannelSchemePOList = originChannelSchemePOList.stream().filter(a -> a.getChannelStatus().equals(LockStatus.BUSY))
				.collect(Collectors.toList());

		// TODO lamda分组
		Map<String, List<ChannelSchemePO>> channelSchemePOMap = originBusyChannelSchemePOList.stream().collect(Collectors.groupingBy(ChannelSchemePO::getBundleId));

		LOGGER.info("unlockAndUpdateBatchBundles, query and reassemble all channelSchemePOMap time=" + (System.currentTimeMillis() - startTime) + " ,size="
				+ channelSchemePOMap.size());

		// 将涉及的 ScreenSchemePO 统一查出 在内存中处理
		// originSceenSchemePOList 是原始查出的 数据list
		// screenSchemePOList 是待提交到数据库修改的数据list
		List<ScreenSchemePO> originSceenSchemePOList = screenSchemeDao.findByBundleIdIn(bundleIdSet);

		// TODO lamda 过滤
		List<ScreenSchemePO> originBusySceenSchemePOList = originSceenSchemePOList.stream().filter(a -> a.getStatus().equals(LockStatus.BUSY)).collect(Collectors.toList());

		// TODO lamda 分组
		Map<String, List<ScreenSchemePO>> screenSchemePOMap = originBusySceenSchemePOList.stream().collect(Collectors.groupingBy(ScreenSchemePO::getBundleId));

		LOGGER.info(
				"unlockAndUpdateBatchBundles, query and reassemble all screenSchemePOMap time=" + (System.currentTimeMillis() - startTime) + " ,size=" + screenSchemePOMap.size());

		List<BundlePO> bundlePOList = new ArrayList<>();
		List<ChannelSchemePO> channelSchemePOList = new ArrayList<>();
		List<ScreenSchemePO> screenSchemePOList = new ArrayList<>();

		for (BundlePO bundlePO : releaseBundlePOList) {

			BatchLockBundleRespBody releaseBundleRespBody = new BatchLockBundleRespBody();
			LockBundleParam releaseBundleParam = releaseBundleParamMap.get(bundlePO.getBundleId());

			if (releaseBundleParam.isOperateCountSwitch()) {
				if (null == bundlePO.getOperateCount()) {
					bundlePO.setOperateCount(0);
				} else {
					bundlePO.setOperateCount(bundlePO.getOperateCount() - 1);

				}
			}

			if (!releaseBundleParam.isOperateCountSwitch() || (releaseBundleParam.isOperateCountSwitch() && bundlePO.getOperateCount() == 0)) {

				// 还可以优化
				// List<ChannelSchemePO> busyChannels =
				// channelSchemeService.findByBundleIdAndChannelStatus(bundlePO.getBundleId(),
				// LockStatus.BUSY);
				List<ChannelSchemePO> busyChannels = channelSchemePOMap.get(bundlePO.getBundleId());

				if (!CollectionUtils.isEmpty(busyChannels)) {
					for (ChannelSchemePO busyChannel : busyChannels) {
						busyChannel.setChannelStatus(LockStatus.IDLE);
						busyChannel.setOperateIndex(busyChannel.getOperateIndex() + 1);
						// channelSchemeService.save(busyChannel);
						channelSchemePOList.add(busyChannel);
					}
				}

				// 还可以优化
				// List<ScreenSchemePO> busyScreens =
				// screenSchemeDao.findByBundleIdAndStatus(bundlePO.getBundleId(),
				// LockStatus.BUSY);

				List<ScreenSchemePO> busyScreens = screenSchemePOMap.get(bundlePO.getBundleId());

				if (!CollectionUtils.isEmpty(busyScreens)) {
					for (ScreenSchemePO busyScreen : busyScreens) {
						busyScreen.setStatus(LockStatus.IDLE);
						// screenSchemeDao.save(busyScreen);
						screenSchemePOList.add(busyScreen);
					}
				}

				bundlePO.setLockStatus(LockStatus.IDLE);
			}

			bundlePO.setOperateIndex(bundlePO.getOperateIndex() + 1);

			bundlePOList.add(bundlePO);

			releaseBundleRespBody.setBundleId(bundlePO.getBundleId());
			releaseBundleRespBody.setOperateResult(true);
			releaseBundleRespBody.setOperate_count(bundlePO.getOperateCount());
			releaseBundleRespBody.setOperate_index(bundlePO.getOperateIndex());
			batchReleaseBundleRespBodyList.add(releaseBundleRespBody);
			successCnt++;

		}

		LOGGER.info("unlockAndUpdateBatchBundles, logical operate time=" + (System.currentTimeMillis() - startTime));

		lockChannelParamDao.deleteBatchByBundleIds(bundleIdSet);
		lockScreenParamDao.deleteBatchByBundleIds(bundleIdSet);
		lockBundleParamDao.deleteBatchByBundleIds(bundleIdSet);

		LOGGER.info("unlockAndUpdateBatchBundles del all time=" + (System.currentTimeMillis() - startTime));

		channelSchemeDao.save(channelSchemePOList);
		screenSchemeDao.save(screenSchemePOList);
		bundleDao.save(bundlePOList);

		LOGGER.info("unlockAndUpdateBatchBundles saveAll cost time=" + (System.currentTimeMillis() - startTime) + " , channelSchemePOList size=" + channelSchemePOList.size()
				+ " , screenSchemePOList size=" + screenSchemePOList.size() + " , bundlePOList size=" + bundlePOList.size());

		resp.setSuccessCnt(successCnt);
		resp.setOperateBundlesResult(batchReleaseBundleRespBodyList);
		resp.setResult(com.suma.venus.resource.base.bo.ResponseBody.SUCCESS);
		return resp;

	}

	private void operateLockChannelParam(LockBundleParam lockParam, List<ChannelSchemePO> channelSchemePOList, List<LockChannelParamPO> lockChannelParamPOList,
			Map<String, ChannelSchemePO> channelSchemePOMap) {
		if (null != lockParam.getChannels()) {
			for (ChannelBody channelBody : lockParam.getChannels()) {
				// ChannelSchemePO channelSchemePO =
				// channelSchemeService.findByBundleIdAndChannelId(lockParam.getBundleId(),
				// channelBody.getChannel_id());
				ChannelSchemePO channelSchemePO = channelSchemePOMap.get(lockParam.getBundleId() + "-" + channelBody.getChannel_id());
				
				channelSchemePO.setChannelStatus(LockStatus.BUSY);
				channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
				// channelSchemeService.save(channelSchemePO);
				channelSchemePOList.add(channelSchemePO);

				LockChannelParamPO lockChannelParamPO = new LockChannelParamPO();
				lockChannelParamPO.setBundleId(lockParam.getBundleId());
				lockChannelParamPO.setChannelId(channelBody.getChannel_id());
				lockChannelParamPO.setUserId(lockParam.getUserId());
				lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channelBody.getChannel_param()));
				// lockChannelParamService.save(lockChannelParamPO);
				lockChannelParamPOList.add(lockChannelParamPO);
			}
		}
	}

	private void operateLockScreenParam(LockBundleParam lockParam, List<ScreenSchemePO> screenSchemePOList, List<LockScreenParamPO> lockScreenParamPOList,
			Map<String, ScreenSchemePO> screenSchemePOMap) {
		if (null != lockParam.getScreens()) {
			for (ScreenBody screenBody : lockParam.getScreens()) {
				// ScreenSchemePO screenSchemePO =
				// screenSchemeDao.findByBundleIdAndScreenId(lockParam.getBundleId(),
				// screenBody.getScreen_id());
				ScreenSchemePO screenSchemePO = screenSchemePOMap.get(lockParam.getBundleId() + "-" + screenBody.getScreen_id());

				if (null != screenSchemePO) {
					screenSchemePO.setStatus(LockStatus.BUSY);
					// screenSchemeDao.save(screenSchemePO);
					screenSchemePOList.add(screenSchemePO);

					LockScreenParamPO lockScreenParamPO = new LockScreenParamPO();
					lockScreenParamPO.setBundleId(lockParam.getBundleId());
					lockScreenParamPO.setScreenId(screenBody.getScreen_id());
					lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
					// lockScreenParamDao.save(lockScreenParamPO);
					lockScreenParamPOList.add(lockScreenParamPO);

				}
			}
		}
	}

}
