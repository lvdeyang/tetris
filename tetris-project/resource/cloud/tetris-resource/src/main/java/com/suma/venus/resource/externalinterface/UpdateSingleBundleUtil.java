package com.suma.venus.resource.externalinterface;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ChannelBody;
import com.suma.venus.resource.base.bo.LockBundleParam;
import com.suma.venus.resource.base.bo.ScreenBody;
import com.suma.venus.resource.constant.ErrorCode;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.dao.LockScreenParamDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.LockBundleParamPO;
import com.suma.venus.resource.pojo.LockChannelParamPO;
import com.suma.venus.resource.pojo.LockScreenParamPO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.LockChannelParamService;

@Service
public class UpdateSingleBundleUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSingleBundleUtil.class);

	@Autowired
	private BundleService bundleService;

	@Autowired
	private LockBundleParamDao lockBundleParamDao;

	@Autowired
	private LockChannelParamService lockChannelParamService;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Autowired
	private LockScreenParamDao lockScreenParamDao;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public BundlePO lockAndUpdateSingleBundle(LockBundleParam lockParam) throws Exception {
		BundlePO bundle = bundleService.findByBundleId(lockParam.getBundleId());
		if (LockStatus.IDLE == bundle.getLockStatus()) {// bundle当前未被锁定
			bundle.setLockStatus(LockStatus.BUSY);
			bundle.setOperateIndex(bundle.getOperateIndex() + 1);
			// 是否需要计数
			if (lockParam.isOperateCountSwitch()) {
				if (null == bundle.getOperateCount() || bundle.getOperateCount() < 0) {
					bundle.setOperateCount(0);
				}
				bundle.setOperateCount(bundle.getOperateCount() + 1);
			}

			bundleService.save(bundle);

			saveLockChannelParam(lockParam);

			saveLockScreenParam(lockParam);

			LockBundleParamPO lockBundleParamPO = new LockBundleParamPO();
			lockBundleParamPO.setBundleId(lockParam.getBundleId());
			lockBundleParamPO.setUserId(lockParam.getUserId());
			String pass_by_str = lockParam.getPassByStr();
			if (null != pass_by_str && !pass_by_str.isEmpty()) {
				lockBundleParamPO.setPassByStr(pass_by_str);
			}
			lockBundleParamDao.save(lockBundleParamPO);
		} else {// bundle之前已被锁定
			LockBundleParamPO lockBundleParamPO = lockBundleParamDao.findByBundleId(lockParam.getBundleId());
			if (null != lockBundleParamPO && !lockBundleParamPO.getUserId().equals(lockParam.getUserId())) {
				throw new Exception(ErrorCode.BUNDLE_BUSY.toString());
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
			
			lockBundleParamDao.save(lockBundleParamPO);

			if (null != lockParam.getChannels()) {
				for (ChannelBody channelBody : lockParam.getChannels()) {
					LockChannelParamPO lockChannelParamPO = lockChannelParamService
							.findByBundleIdAndChannelId(lockParam.getBundleId(), channelBody.getChannel_id());
					if (null != lockChannelParamPO) {
						// 该通道上存在任务参数
						lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channelBody.getChannel_param()));
						lockChannelParamService.save(lockChannelParamPO);
					} else {
						
						// 该通道上不存在任务参数
						lockChannelParamPO = new LockChannelParamPO();
						lockChannelParamPO.setBundleId(lockParam.getBundleId());
						lockChannelParamPO.setChannelId(channelBody.getChannel_id());
						lockChannelParamPO.setUserId(lockParam.getUserId());
						lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channelBody.getChannel_param()));
						lockChannelParamService.save(lockChannelParamPO);
						
						ChannelSchemePO channelSchemePO = channelSchemeService
								.findByBundleIdAndChannelId(lockParam.getBundleId(), channelBody.getChannel_id());
						channelSchemePO.setChannelStatus(LockStatus.BUSY);
						channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
						channelSchemeService.save(channelSchemePO);
					}
				}
			}

			if (null != lockParam.getScreens()) {
				for (ScreenBody screenBody : lockParam.getScreens()) {
					ScreenSchemePO screenSchemePO = screenSchemeDao.findByBundleIdAndScreenId(lockParam.getBundleId(),
							screenBody.getScreen_id());
					if (null != screenSchemePO) {
						if (LockStatus.IDLE.equals(screenSchemePO.getStatus())) {// 屏之前未被锁定
							screenSchemePO.setStatus(LockStatus.BUSY);
							screenSchemeDao.save(screenSchemePO);

							LockScreenParamPO lockScreenParamPO = new LockScreenParamPO();
							lockScreenParamPO.setBundleId(lockParam.getBundleId());
							lockScreenParamPO.setScreenId(screenBody.getScreen_id());
							lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
							lockScreenParamDao.save(lockScreenParamPO);
						} else {// 屏之前已被锁定
							LockScreenParamPO lockScreenParamPO = lockScreenParamDao
									.findByBundleIdAndScreenId(lockParam.getBundleId(), screenBody.getScreen_id());
							lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
							lockScreenParamDao.save(lockScreenParamPO);
						}
					}
				}
			}

			bundle.setOperateIndex(bundle.getOperateIndex() + 1);

			// 是否需要计数
			if (lockParam.isOperateCountSwitch()) {
				if (null == bundle.getOperateCount() || bundle.getOperateCount() < 0) {
					bundle.setOperateCount(0);
				}
				bundle.setOperateCount(bundle.getOperateCount() + 1);
			}

			bundleService.save(bundle);
		}
		return bundle;
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public BundlePO unlockAndUpdateSingleBundle(String businessMode, String bundleId, boolean operateCountSwitch) {
		BundlePO bundle = bundleService.findByBundleId(bundleId);

		if (operateCountSwitch) {
			if (null == bundle.getOperateCount()) {
				bundle.setOperateCount(0);
			} else {
				bundle.setOperateCount(bundle.getOperateCount() - 1);

			}
		}

		
		//TODO bundle中的channel状态只在bundle计数为0或者不使用计数的时候才全部释放，其他情况不处理
		if (!operateCountSwitch || (operateCountSwitch && bundle.getOperateCount() == 0)) {
			List<ChannelSchemePO> busyChannels = channelSchemeService.findByBundleIdAndChannelStatus(bundleId,
					LockStatus.BUSY);
			for (ChannelSchemePO busyChannel : busyChannels) {
				busyChannel.setChannelStatus(LockStatus.IDLE);
				busyChannel.setOperateIndex(busyChannel.getOperateIndex() + 1);
				channelSchemeService.save(busyChannel);
			}

			List<ScreenSchemePO> busyScreens = screenSchemeDao.findByBundleIdAndStatus(bundleId, LockStatus.BUSY);
			for (ScreenSchemePO busyScreen : busyScreens) {
				busyScreen.setStatus(LockStatus.IDLE);
				screenSchemeDao.save(busyScreen);
			}

			lockChannelParamService.deleteByBundleId(bundleId);

			lockScreenParamDao.deleteByBundleId(bundleId);

			lockBundleParamDao.deleteByBundleId(bundleId);
			
			bundle.setLockStatus(LockStatus.IDLE);
		}
		
		
		bundle.setOperateIndex(bundle.getOperateIndex() + 1);

		bundleService.save(bundle);

		return bundle;
	}

	private void saveLockChannelParam(LockBundleParam lockParam) {
		if (null != lockParam.getChannels()) {
			for (ChannelBody channelBody : lockParam.getChannels()) {
				ChannelSchemePO channelSchemePO = channelSchemeService
						.findByBundleIdAndChannelId(lockParam.getBundleId(), channelBody.getChannel_id());
				channelSchemePO.setChannelStatus(LockStatus.BUSY);
				channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
				channelSchemeService.save(channelSchemePO);

				LockChannelParamPO lockChannelParamPO = new LockChannelParamPO();
				lockChannelParamPO.setBundleId(lockParam.getBundleId());
				lockChannelParamPO.setChannelId(channelBody.getChannel_id());
				lockChannelParamPO.setUserId(lockParam.getUserId());
				lockChannelParamPO.setChannelParam(JSONObject.toJSONString(channelBody.getChannel_param()));
				lockChannelParamService.save(lockChannelParamPO);
			}
		}
	}

	private void saveLockScreenParam(LockBundleParam lockParam) {
		if (null != lockParam.getScreens()) {
			for (ScreenBody screenBody : lockParam.getScreens()) {
				ScreenSchemePO screenSchemePO = screenSchemeDao.findByBundleIdAndScreenId(lockParam.getBundleId(),
						screenBody.getScreen_id());
				if (null != screenSchemePO) {
					screenSchemePO.setStatus(LockStatus.BUSY);
					screenSchemeDao.save(screenSchemePO);

					LockScreenParamPO lockScreenParamPO = new LockScreenParamPO();
					lockScreenParamPO.setBundleId(lockParam.getBundleId());
					lockScreenParamPO.setScreenId(screenBody.getScreen_id());
					lockScreenParamPO.setScreenParam(JSONArray.toJSONString(screenBody.getRects()));
					lockScreenParamDao.save(lockScreenParamPO);
				}
			}
		}
	}

}
