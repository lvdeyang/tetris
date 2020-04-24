package com.suma.venus.resource.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.application.ldap.equip.dao.LdapEquipDao;
import com.suma.application.ldap.equip.po.LdapEquipPo;
import com.suma.venus.resource.constant.VenusParamConstant;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.BundleLoginBlackListDao;
import com.suma.venus.resource.dao.ChannelSchemeDao;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.dao.LockBundleParamDao;
import com.suma.venus.resource.dao.LockChannelParamDao;
import com.suma.venus.resource.dao.LockScreenParamDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.externalinterface.InterfaceFromResource;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.pojo.BundleLoginBlackListPO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.BundlePO.SYNC_STATUS;
import com.suma.venus.resource.pojo.ChannelSchemePO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.ExtraInfoPO;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.ScreenSchemePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.BundleSpecificationBuilder;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ChannelTemplateService;
import com.suma.venus.resource.service.ExtraInfoService;
import com.suma.venus.resource.service.FolderService;
import com.suma.venus.resource.task.BundleHeartBeatService;
import com.suma.venus.resource.util.EquipSyncLdapUtils;
import com.suma.venus.resource.vo.BundleVO;
import com.suma.venus.resource.vo.BundleVO.CoderType;
import com.suma.venus.resource.vo.ChannelSchemeVO;
import com.sumavision.tetris.capacity.server.CapacityService;

@Controller
@RequestMapping("/bundle")
public class BundleManageController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleManageController.class);

	@Autowired
	private BundleService bundleService;

	@Autowired
	private ExtraInfoService extraInfoService;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private ChannelTemplateService channelTemplateService;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Autowired
	private InterfaceFromResource interfaceFromResource;

	@Autowired
	private LockChannelParamDao lockChannelParamDao;

	@Autowired
	private LockScreenParamDao lockScreenParamDao;

	@Autowired
	private LockBundleParamDao lockBundleParamDao;

	@Autowired
	private BundleLoginBlackListDao bundleLoginBlackListDao;

	@Autowired
	private FolderService folderService;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private ChannelSchemeDao channelSchemeDao;

	@Autowired
	private BundleHeartBeatService bundleHeartBeatService;

	/*
	 * @Autowired private ChannelTemplateDao channelTemplateDao;
	 */

	@Autowired
	private LdapEquipDao ldapEquipDao;

	@Autowired
	private EquipSyncLdapUtils equipSyncLdapUtils;

	@Autowired
	private CapacityService capacityService;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;

	@Value("${spring.cloud.client.ipAddress}")
	private String clientIP;

	@Value("${server.port}")
	private String port;

	private final int EXTRAINFO_START_COLUMN = 11;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@RequestParam("bundle") String bundle,
			@RequestParam("extraInfoVOList") String extraInfoVOList) {
		Map<String, Object> data = makeAjaxData();
		// TODO: 没有事务
		try {
			LOGGER.info("add bundle=" + JSONObject.toJSONString(bundle));

			BundleVO bundleVO = JSONObject.parseObject(bundle, BundleVO.class);
			List<ExtraInfoPO> extraInfos = JSONArray.parseArray(extraInfoVOList, ExtraInfoPO.class);
			BundlePO bundlePO = bundleVO.toPO();
			bundlePO.setBundleType(
					channelTemplateService.findByDeviceModel(bundlePO.getDeviceModel()).get(0).getBundleType());
			/** 针对合屏混音设备，需设置音频/视频编码最大路数 */
			if ("mixer".equalsIgnoreCase(bundlePO.getDeviceModel())) {
				bundlePO.setMaxAudioSrcCnt(VenusParamConstant.MIXER_MAX_AUDIO_SRC_CNT);
				bundlePO.setMaxVideoSrcCnt(VenusParamConstant.MIXER_MAX_VIDEO_SRC_CNT);
				bundlePO.setFreeAudioSrcCnt(bundlePO.getMaxAudioSrcCnt());
				bundlePO.setFreeVideoSrcCnt(bundlePO.getMaxVideoSrcCnt());
			}
			// 生成bundleId
			bundlePO.setBundleId(BundlePO.createBundleId());

			if (null != bundlePO.getUsername() && !bundlePO.getUsername().isEmpty()) {
				if (null != bundleService.findByUsername(bundlePO.getUsername())) {
					data.put(ERRMSG, "设备账号已存在");
					return data;
				}

				// 通知用户权限服务创建虚拟用户
				Boolean beDevice = BundlePO.beDeviceBundle(bundlePO.getDeviceModel());
				// 去掉虚拟用户和虚拟角色,去掉虚拟角色绑定权限
//				UserResultBO userResult = userFeign.createVirtualUser(bundleVO.getUsername(), bundleVO.getOnlinePassword(), beDevice);
//				if (null == userResult || null == userResult.getUserId()) {
//					data.put(ERRMSG, "创建设备账号失败");
//					return data;
//				}

				// 通知用户权限服务创建虚拟角色
//				RoleResultBO roleResult = userFeign.createVirtualRole(bundleVO.getUsername());
//				if (null == roleResult || null == roleResult.getRoleId()) {
//					data.put(ERRMSG, "创建设备账号失败");
//					return data;
//				}

				// 绑定虚拟角色和bundle权限
//				RoleAndResourceIdBO roleAndResourceIdBO = new RoleAndResourceIdBO();
//				roleAndResourceIdBO.setRoleId(roleResult.getRoleId());
//				roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
//				roleAndResourceIdBO.getResourceCodes().add(bundlePO.getBundleId());
//				ResultBO bindResult = userFeign.bindRolePrivilege(roleAndResourceIdBO);
//				if (null == bindResult || !bindResult.isResult()) {
//					data.put(ERRMSG, "创建设备账号失败");
//					return data;
//				}
			}

			bundleService.save(bundlePO);
			if (null != extraInfos) {
				for (ExtraInfoPO extraInfo : extraInfos) {
					extraInfo.setBundleId(bundlePO.getBundleId());
					extraInfoService.save(extraInfo);
				}
			}

			// 按照模板最大通道数自动生成能力配置
			if ("jv210".equals(bundlePO.getDeviceModel())) {
				if (CoderType.ENCODER.equals(bundleVO.getCoderType())) {
					channelSchemeDao
							.save(channelSchemeService.createAudioAndVideoEncodeChannel(bundlePO.getBundleId()));
				} else if (CoderType.DECODER.equals(bundleVO.getCoderType())) {
					channelSchemeDao
							.save(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO.getBundleId()));
				} else {
					bundleService.configDefaultAbility(bundlePO);
				}
			} else {
				bundleService.configDefaultAbility(bundlePO);
			}

			if ("transcode".equals(bundlePO.getDeviceModel())) {

				capacityService.setHeartbeatUrl(bundlePO.getDeviceIp(),
						"http://10.10.40.207:8082/tetris-resource/api/thirdpart/bundleHeartBeat?bundle_ip="
								+ bundlePO.getDeviceIp());

				capacityService.setAlarmUrl(bundlePO.getDeviceIp());

			}

			data.put("bundleId", bundlePO.getBundleId());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	// TODO 这个查询逻辑需要修改。。。
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> query(@RequestParam("deviceModel") String deviceModel,
			@RequestParam(name = "sourceType", required = false) String sourceType,
			@RequestParam(name = "userId", required = false) Long userId, @RequestParam("keyword") String keyword,
			@RequestParam(name = "pageNum") Integer pageNum,
			@RequestParam(name = "countPerPage") Integer countPerPage) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<BundlePO> bundlePOs1 = bundleService.queryByUserIdAndDevcieModelAndKeyword(userId, deviceModel,
					sourceType, keyword);
			
			List<BundlePO> bundlePOs2 = bundleDao.findAll(BundleSpecificationBuilder.getBundleSpecification(deviceModel, sourceType, keyword, userId));

			// 过滤掉devicemodel为空的数据 ??
			List<BundlePO> bundlePOs = bundlePOs1.stream().filter(b -> (null != b.getDeviceModel()))
					.collect(Collectors.toList());
			
			for(BundlePO bundle: bundlePOs2){
				if(!bundlePOs.contains(bundle)){
					bundlePOs.add(bundle);
				}
			}

			List<BundleVO> bundles = new ArrayList<BundleVO>();
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > bundlePOs.size()) ? bundlePOs.size() : pageNum * countPerPage;
			for (int i = from; i < to; i++) {
				BundlePO bundlePO = bundlePOs.get(i);
				BundleVO vo = BundleVO.fromPO(bundlePO);
				if (!"VenusTerminal".equals(bundlePO.getBundleType()) && !"VenusProxy".equals(bundlePO.getBundleType())
						&& !lockChannelParamDao.findByBundleId(bundlePO.getBundleId()).isEmpty()) {
					// 针对channel级锁定的bundle，如果它下面有channel被锁定，则认为bundle整体处于锁定状态
					vo.setLockStatus(LockStatus.BUSY.toString());
				}
				bundles.add(vo);
			}
			data.put("resources", bundles);
			data.put("total", bundlePOs.size());
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/queryExtraInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> detail(@RequestParam("bundleId") String bundleId) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<ExtraInfoPO> extraInfos = extraInfoService.findByBundleId(bundleId);
			data.put("extraInfos", extraInfos);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam(value = "bundleIds") String bundleIds) {
		Map<String, Object> data = makeAjaxData();
		try {
			String[] bundleIdArr = bundleIds.split(",");
			for (String bundleId : bundleIdArr) {
				try {
					deleteByBundleId(bundleId);
				} catch (Exception e) {
					LOGGER.warn("fail to delete bundle ; bundleId = " + bundleId, e);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Fail to delete bundle : ", e);
			data.put(ERRMSG, "内部错误");
		}
		return data;
	}

	private void deleteByBundleId(String bundleId) throws Exception {
		BundlePO bundle = bundleService.findByBundleId(bundleId);

		// 没有虚拟用户
//		if (null != bundle.getUsername() && !bundle.getUsername().isEmpty()) {
//			try {
//				// 删除设备对应的虚拟用户(如果有)
//				ResultBO deleteVirtualUserResult = userFeign.delVirtualUser(bundle.getUsername());
//				if (!deleteVirtualUserResult.isResult()) {
//					LOGGER.warn("Communication success but Fail to delete user of bundle; username = " + bundle.getUsername());
//				}
//			} catch (Exception e) {
//				LOGGER.warn("Communication Error : Fail to delete user of bundle; username = " + bundle.getUsername(), e);
////						data.put(ERRMSG, "删除设备账号失败:用户微服务通信失败");
//			}
//
//			// 如果有用户绑定了设备作为编码器或者解码器，需要通知用户侧解除绑定
//			try {
		// TODO:新做
//				userFeign.unbindEncoderAndDecoder(bundleId);
//			} catch (Exception e) {
//				LOGGER.error("Communication Error : Fail to unbind encoder and decoder ; bundleId = " + bundleId);
//			}
//		}

		if(bundle.getDeviceIp() != null){
			bundleHeartBeatService.removeBundleStatus(bundle.getDeviceIp());
		}

		bundleService.delete(bundle);

		// 删除extraInfo
		extraInfoService.deleteByBundleId(bundleId);

		// 删除配置能力
		channelSchemeService.deleteByBundleId(bundleId);
		lockChannelParamDao.deleteByBundleId(bundleId);

		// 删除屏配置信息
		screenSchemeDao.deleteByBundleId(bundleId);
		lockScreenParamDao.deleteByBundleId(bundleId);

		// 删除设备上的锁定参数（如果有）
		lockBundleParamDao.deleteByBundleId(bundleId);
		
		//删除用户绑定编解码器关系
		List<EncoderDecoderUserMap> maps = encoderDecoderUserMapDao.findByEncodeBundleIdOrDecodeBundleId(bundleId, bundleId);
		for(EncoderDecoderUserMap map: maps){
			if(map.getEncodeBundleId() != null && map.getEncodeBundleId().equals(bundleId)){
				map.setEncodeBundleId(null);
				map.setEncodeBundleName(null);
				map.setEncodeDeviceModel(null);
				map.setEncodeId(null);
			}
			if(map.getDecodeBundleId() != null && map.getDecodeBundleId().equals(bundleId)){
				map.setDecodeBundleId(null);
				map.setDecodeBundleName(null);
				map.setDecodeDeviceModel(null);
				map.setDecodeId(null);
			}
		}
		
		encoderDecoderUserMapDao.save(maps);
		
		// 删除设备账号对应的黑名单数据
//			bundleLoginBlackListDao.deleteByLoginId(bundle.getCurrentLoginId());

		// 通过消息队列通知接入层
//			interfaceFromResource.delBundleRequest(bundle);

		//为了删除ldap上面的设备
		if (!SOURCE_TYPE.EXTERNAL.equals(bundle.getSourceType())) {
			// 从ldap删除
			try {
				List<LdapEquipPo> oldLdapEquips = ldapEquipDao.getEquipByIUuid(bundleId);
				if (!CollectionUtils.isEmpty(oldLdapEquips)) {
					ldapEquipDao.remove(oldLdapEquips.get(0));
				}
			} catch (Exception e) {
				LOGGER.warn("delete bundle from ldap failed : ", e);
			}
		}
	}

	@RequestMapping("/setAccessLayer")
	@ResponseBody
	public Map<String, Object> setAccessLayer(@RequestParam(value = "bundleId") String bundleId,
			@RequestParam(value = "accessLayerId") String accessLayerId) {
		Map<String, Object> data = makeAjaxData();

		// TODO layerid可以为空么？
		if (StringUtils.isEmpty(bundleId)) {
			data.put(ERRMSG, "参数错误");
			return data;
		}

		String[] bundleIdArr = bundleId.split(",");

		int total = bundleIdArr.length;
		int failCnt = 0;

		for (String bundleIdTemp : bundleIdArr) {

			try {
				BundlePO bundle = bundleService.findByBundleId(bundleIdTemp);
				if (null == bundle.getAccessNodeUid()) {
					if (!StringUtils.isEmpty(accessLayerId)) {
						bundle.setAccessNodeUid(accessLayerId);
						bundleService.save(bundle);
					}
				} else if (!bundle.getAccessNodeUid().equals(accessLayerId)) {
					bundle.setAccessNodeUid(accessLayerId);
					bundleService.save(bundle);
				}

			} catch (Exception e) {
				LOGGER.error("set bundle accesslayer error, bundleId=" + bundleIdTemp + ", exception=" + e.toString());
				failCnt++;
			}
		}

		if (failCnt > 0) {
			data.put(ERRMSG, "成功设置了" + (total - failCnt) + "个设备有" + failCnt + "个设备设置失败");
		}

		return data;
	}

	/** 踢出设备账号 */
	@RequestMapping("/logout")
	@ResponseBody
	public Map<String, Object> logout(@RequestParam(value = "bundleId") String bundleId) {
		Map<String, Object> data = makeAjaxData();
		try {
			BundlePO bundle = bundleService.findByBundleId(bundleId);
			/** 通知接入层 */
			interfaceFromResource.logoutBundleRequest(bundle);
//			ResponseBO resp = interfaceFromResource.logoutBundleRequest(bundle);
//			if(null == resp || !com.suma.venus.resource.bo.ResponseBody.SUCCESS.equalsIgnoreCase(
//					resp.getMessage().getMessage_body().getJSONObject("logout_bundle_response").getString("result"))){
//				data.put(ERRMSG, "操作失败：和接入层通信错误");
//				return data;
//			}

			/** 加入黑名单 */
			if (null != bundle.getCurrentLoginId()) {
				BundleLoginBlackListPO bundleLoginBlackListPO = new BundleLoginBlackListPO();
				bundleLoginBlackListPO.setLoginId(bundle.getCurrentLoginId());
				bundleLoginBlackListPO.setStartTime(new Date());
				bundleLoginBlackListDao.save(bundleLoginBlackListPO);

				bundle.setOnlineStatus(ONLINE_STATUS.OFFLINE);
				bundleService.save(bundle);

				// 发送离线告警消息
				// AlarmOprlogClientService.getInstance().triggerAlarm(AlarmCodeConstant.BUNDLE_OFFLINE_CODE,
				// bundleId, null, new Date());
			}
		} catch (Exception e) {
			LOGGER.error("Fail to logout Resource : ", e);
			data.put(ERRMSG, "操作失败");
		}
		return data;
	}

	/** 手动释放bundle */
	@RequestMapping("/clear")
	@ResponseBody
	public Map<String, Object> clear(@RequestParam(value = "bundleId") String bundleId) {
		Map<String, Object> data = makeAjaxData();
		try {
			BundlePO bundle = bundleService.findByBundleId(bundleId);

//			ResponseBO resp = interfaceFromResource.clearBundleRequest(bundle);
//			if(null == resp || !com.suma.venus.resource.bo.ResponseBody.SUCCESS.equalsIgnoreCase(
//					resp.getMessage().getMessage_body().getJSONObject("clear_bundle_response").getString("result"))){
//				data.put(ERRMSG, "操作失败：和接入层通信错误");
//				return data;
//			}

			// 清除bundle上被锁定的channel
			List<ChannelSchemePO> channelSchemePOs = channelSchemeService.findByBundleIdAndChannelStatus(bundleId,
					LockStatus.BUSY);
			for (ChannelSchemePO channelSchemePO : channelSchemePOs) {
				channelSchemePO.setChannelStatus(LockStatus.IDLE);
				channelSchemePO.setOperateIndex(channelSchemePO.getOperateIndex() + 1);
				channelSchemeService.save(channelSchemePO);
			}
			lockChannelParamDao.deleteByBundleId(bundleId);

			// 清除bundle上被锁定的screen
			List<ScreenSchemePO> screenSchemePOs = screenSchemeDao.findByBundleIdAndStatus(bundleId, LockStatus.BUSY);
			for (ScreenSchemePO screenSchemePO : screenSchemePOs) {
				screenSchemePO.setStatus(LockStatus.IDLE);
			}
			screenSchemeDao.save(screenSchemePOs);
			lockScreenParamDao.deleteByBundleId(bundleId);

			// 删除bundle整体锁定参数(如果有)
			lockBundleParamDao.deleteByBundleId(bundleId);

			bundle.setLockStatus(LockStatus.IDLE);
			bundle.setOperateIndex(bundle.getOperateIndex() + 1);
			bundle.setOperateCount(0);
			bundle.setFreeAudioSrcCnt(bundle.getMaxAudioSrcCnt());
			bundle.setFreeVideoSrcCnt(bundle.getMaxVideoSrcCnt());
			bundleService.save(bundle);

//			interfaceFromResource.clearBundleRequest(bundle);
		} catch (Exception e) {
			LOGGER.error("Fail to clear Resource : ", e);
			data.put(ERRMSG, "操作失败");
		}
		return data;
	}

	@RequestMapping("/modifyExtraInfo")
	@ResponseBody
	public Map<String, Object> modifyExtraInfo(@RequestParam(value = "bundleId") String bundleId,
			@RequestParam(value = "bundleName") String bundleName, @RequestParam(value = "deviceIp") String deviceIp,
			@RequestParam(value = "devicePort") Integer devicePort,
			@RequestParam(value = "extraInfos") String extraInfos) {
		LOGGER.info("modifyExtraInfo, bundleId=" + bundleId + " ,bundleName=" + bundleName + " ,deviceIp=" + deviceIp
				+ " ,devicePort=" + devicePort + " ,extraInfos=" + extraInfos);

		Map<String, Object> data = makeAjaxData();
		try {
			BundlePO bundle = bundleService.findByBundleId(bundleId);
			if (!bundleName.equals(bundle.getBundleName())) {
				bundle.setBundleName(bundleName);
			}

			if (!deviceIp.equals(bundle.getDeviceIp())) {
				bundle.setDeviceIp(deviceIp);
			}

			if (!devicePort.equals(bundle.getDevicePort())) {
				bundle.setDevicePort(devicePort);
			}

			bundle.setSyncStatus(SYNC_STATUS.ASYNC);
			bundleService.save(bundle);

			// 删除旧数据
			extraInfoService.deleteByBundleId(bundleId);

			// 添加新数据
			List<ExtraInfoPO> newData = JSONArray.parseArray(extraInfos, ExtraInfoPO.class);
			if (null != newData) {
				for (ExtraInfoPO extraInfoPO : newData) {
					extraInfoPO.setBundleId(bundleId);
					extraInfoService.save(extraInfoPO);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/getBundleChannels", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryBundleAbility(@RequestParam(value = "bundleId") String bundleId) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<ChannelSchemePO> channelSchemePOs = channelSchemeService.findByBundleId(bundleId);
			List<ChannelSchemeVO> channelSchemes = new ArrayList<ChannelSchemeVO>();
			for (ChannelSchemePO po : channelSchemePOs) {
				ChannelSchemeVO vo = ChannelSchemeVO.fromPO(po);
				channelSchemes.add(vo);
			}
			data.put("channelSchemes", channelSchemes);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importBundles(@RequestParam("filePoster") MultipartFile uploadFile) {
		Map<String, Object> data = makeAjaxData();
		try {
			int successCnt = importExcel(uploadFile.getInputStream());
			data.put("successCnt", successCnt);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/ldapSync", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ldapSync() {
		Map<String, Object> data = makeAjaxData();
		try {
			// 从ldap服务器向资源层同步设备信息
			int fromLdapToResource = equipSyncLdapUtils.handleSyncFromLdap();

			// 从资源层向ldap服务器同步设备信息
			int fromResourceToLdap = equipSyncLdapUtils.handleSyncToLdap("false");

			data.put("fromLdapToResource", fromLdapToResource);
			data.put("fromResourceToLdap", fromResourceToLdap);
		} catch (Exception e) {
			LOGGER.error("LDAP sync error", e);
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}

	@RequestMapping(value = "/syncToLdap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> syncToLdap(@RequestParam(value = "syncAll") String syncAll) {

		System.out.println("syncToLdap syncAll=" + syncAll);

		Map<String, Object> data = makeAjaxData();

		// TODO
		data.put("successCnt", equipSyncLdapUtils.handleSyncToLdap(syncAll));

		return data;

	}

	@RequestMapping(value = "/syncFromLdap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> syncFromLdap() {

		Map<String, Object> data = makeAjaxData();
		// TODO
		data.put("successCnt", equipSyncLdapUtils.handleSyncFromLdap());
		return data;

	}

	@RequestMapping(value = "/cleanUpLdap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cleanUpLdap() {
		Map<String, Object> data = makeAjaxData();

		data.put(ERRMSG, equipSyncLdapUtils.handleCleanUpLdap());
		return data;
	}

	private int importExcel(InputStream is) throws Exception {
		int successCnt = 0;

		Workbook workbook = WorkbookFactory.create(is);
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		Row titleRow = sheet.getRow(0);
		int columnCnt = titleRow.getLastCellNum();
		// 标记首行附加属性列值
		Map<Integer, String> extraInfoMap = new HashMap<Integer, String>();
		for (int i = EXTRAINFO_START_COLUMN; i < columnCnt; i++) {
			// 附加属性列序号
			Cell cell = titleRow.getCell(i);
			extraInfoMap.put(i, cell.getStringCellValue());
		}

		// 按行读取并处理数据
		for (int i = 1; i <= lastRowNum; i++) {
			try {
				Row row = sheet.getRow(i);
				String bundleName = row.getCell(0).getStringCellValue();
				String deviceModel = row.getCell(1).getStringCellValue();
				String username = row.getCell(2).getStringCellValue();
				BundlePO bundle = bundleService.findByUsername(username);
				// 如果设备账号已存在，则只更新这条数据对应的附加属性字段
				if (null != bundle) {
					if (!bundle.getDeviceModel().equals(deviceModel)) {
						LOGGER.error("Fail to add bundle : deviceModel not match --> " + deviceModel);
						continue;
					}

					// update所属分组
					Cell folderNameCell = row.getCell(5);
					if (null != folderNameCell && !StringUtils.isEmpty(folderNameCell.getStringCellValue())) {
						bundle.setFolderId(folderService.findIdByName(folderNameCell.getStringCellValue()));
						bundleService.save(bundle);
					}
					// update设备ip和端口
					Cell ipCell = row.getCell(9);
					if (null != ipCell && !StringUtils.isEmpty(ipCell.getStringCellValue())) {
						bundle.setDeviceIp(ipCell.getStringCellValue());
						bundleService.save(bundle);
					}
					Cell portCell = row.getCell(10);
					if (null != portCell && !StringUtils.isEmpty(portCell.getStringCellValue())) {
						bundle.setDevicePort(Integer.valueOf(portCell.getStringCellValue()));
						bundleService.save(bundle);
					}

					// 附加属性
					updateExtraInfo(columnCnt, extraInfoMap, row, bundle.getBundleId());

					successCnt++;
					continue;
				}

				BundlePO bundlePO = new BundlePO();
				bundlePO.setBundleName(bundleName);
				bundlePO.setDeviceModel(deviceModel);
				bundlePO.setBundleType(channelTemplateService.findByDeviceModel(deviceModel).get(0).getBundleType());
				bundlePO.setUsername(username);
				bundlePO.setOnlinePassword(row.getCell(3).getStringCellValue());
				/** 针对合屏混音设备，需设置音频/视频编码最大路数 */
				if ("mixer".equalsIgnoreCase(deviceModel)) {
					bundlePO.setMaxAudioSrcCnt(VenusParamConstant.MIXER_MAX_AUDIO_SRC_CNT);
					bundlePO.setMaxVideoSrcCnt(VenusParamConstant.MIXER_MAX_VIDEO_SRC_CNT);
					bundlePO.setFreeAudioSrcCnt(bundlePO.getMaxAudioSrcCnt());
					bundlePO.setFreeVideoSrcCnt(bundlePO.getMaxVideoSrcCnt());
				}
				// 如果导入文件中指定bundle_id，则使用指定的bundle_id；否则生成新的bundleid
				Cell bundleIdCell = row.getCell(4);
				if (null != bundleIdCell && !StringUtils.isEmpty(bundleIdCell.getStringCellValue())) {
					bundlePO.setBundleId(bundleIdCell.getStringCellValue());
				} else {
					bundlePO.setBundleId(BundlePO.createBundleId());
				}

				// 分组名称
				Cell folderNameCell = row.getCell(5);
				if (null != folderNameCell && !StringUtils.isEmpty(folderNameCell.getStringCellValue())) {
					bundlePO.setFolderId(folderService.findIdByName(folderNameCell.getStringCellValue()));
				}

				// 接入层id
				Cell accessLayerUidCell = row.getCell(6);
				if (null != accessLayerUidCell && !StringUtils.isEmpty(accessLayerUidCell.getStringCellValue())) {
					bundlePO.setAccessNodeUid(accessLayerUidCell.getStringCellValue());
				}

				// save设备ip和端口
				Cell ipCell = row.getCell(9);
				if (null != ipCell && !StringUtils.isEmpty(ipCell.getStringCellValue())) {
					bundlePO.setDeviceIp(ipCell.getStringCellValue());
				}
				Cell portCell = row.getCell(10);
				if (null != portCell && !StringUtils.isEmpty(portCell.getStringCellValue())) {
					bundlePO.setDevicePort(Integer.valueOf(portCell.getStringCellValue()));
				}

				/*
				 * if (null != username && !username.isEmpty()) { // 通知用户权限服务创建虚拟用户 Boolean
				 * beDevice = BundlePO.beDeviceBundle(bundlePO.getDeviceModel()); UserResultBO
				 * userResult = userFeign.createVirtualUser(bundlePO.getUsername(),
				 * bundlePO.getOnlinePassword(), beDevice); if (null == userResult || null ==
				 * userResult.getUserId()) {
				 * LOGGER.error("Fail to add bundle : Create username Error --> " +
				 * bundlePO.getUsername()); continue; }
				 * 
				 * // 通知用户权限服务创建虚拟角色 RoleResultBO roleResult =
				 * userFeign.createVirtualRole(bundlePO.getUsername()); if (null == roleResult
				 * || null == roleResult.getRoleId()) {
				 * LOGGER.error("Fail to add bundle : Create user role Error --> " +
				 * bundlePO.getUsername()); continue; }
				 * 
				 * // 绑定虚拟角色和bundle权限 RoleAndResourceIdBO roleAndResourceIdBO = new
				 * RoleAndResourceIdBO(); roleAndResourceIdBO.setRoleId(roleResult.getRoleId());
				 * roleAndResourceIdBO.setResourceCodes(new ArrayList<String>());
				 * roleAndResourceIdBO.getResourceCodes().add(bundlePO.getBundleId()); ResultBO
				 * bindResult = userFeign.bindRolePrivilege(roleAndResourceIdBO); if (null ==
				 * bindResult || !bindResult.isResult()) {
				 * LOGGER.error("Fail to add bundle : Bind user role Error --> " +
				 * bundlePO.getUsername()); continue; } }
				 */

				bundleService.save(bundlePO);

				// 附加属性
				saveExtraInfo(columnCnt, extraInfoMap, row, bundlePO.getBundleId());

				// 配置能力通道
				if ("jv210".equals(bundlePO.getDeviceModel())) {
					Cell encoderCell = row.getCell(7);
					Cell decoderCell = row.getCell(8);
					if (null != encoderCell && "是".equals(encoderCell.getStringCellValue())) {
						// 编码器
						channelSchemeDao
								.save(channelSchemeService.createAudioAndVideoEncodeChannel(bundlePO.getBundleId()));
					} else if (null != decoderCell && "是".equals(decoderCell.getStringCellValue())) {
						// 解码器
						channelSchemeDao
								.save(channelSchemeService.createAudioAndVideoDecodeChannel(bundlePO.getBundleId()));
					} else {
						// 按照jv210模板自动生成能力配置
						bundleService.configDefaultAbility(bundlePO);
					}
				} else {
					// 按照模板最大通道数自动生成能力配置
					bundleService.configDefaultAbility(bundlePO);
				}

				successCnt++;
			} catch (Exception e) {
				LOGGER.error(e.toString());
			}
		}

		return successCnt;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void saveExtraInfo(int columnCnt, Map<Integer, String> extraInfoMap, Row row, String bundleId) {
		for (int j = EXTRAINFO_START_COLUMN; j < columnCnt; j++) {
			Cell cell = row.getCell(j);
			if (null != cell) {
				String extraInfoValue = cell.getStringCellValue();
				if (null != extraInfoValue && !extraInfoValue.isEmpty()) {
					ExtraInfoPO extraInfoPO = new ExtraInfoPO();
					extraInfoPO.setName(extraInfoMap.get(j));
					extraInfoPO.setValue(extraInfoValue);
					extraInfoPO.setBundleId(bundleId);
					extraInfoService.save(extraInfoPO);
				}
			}
		}
	}

	/** 导入批量bundle文件时，如果bundle已存在，更新其附加信息 */
	@SuppressWarnings("unused")
	@Deprecated
	private void updateExtraInfo(int columnCnt, Map<Integer, String> extraInfoMap, Row row, String bundleId) {
		for (int j = EXTRAINFO_START_COLUMN; j < columnCnt; j++) {
			Cell cell = row.getCell(j);
			if (null != cell) {
				String extraInfoName = extraInfoMap.get(j);
				String extraInfoValue = cell.getStringCellValue();
				ExtraInfoPO extraInfoPO = extraInfoService.findByBundleIdAndName(bundleId, extraInfoName);
				if (null == extraInfoValue || extraInfoValue.isEmpty()) {// 如果文件中某附加字段值为空
					if (null != extraInfoPO) {
						extraInfoService.delete(extraInfoPO);
					}
				} else {// 附件字段值非空
					if (null != extraInfoPO) {
						if (!extraInfoValue.equals(extraInfoPO.getValue())) {
							extraInfoPO.setValue(extraInfoValue);
							extraInfoService.save(extraInfoPO);
						}
					} else {
						extraInfoPO = new ExtraInfoPO();
						extraInfoPO.setName(extraInfoName);
						extraInfoPO.setValue(extraInfoValue);
						extraInfoPO.setBundleId(bundleId);
						extraInfoService.save(extraInfoPO);
					}
				}
			}
		}
	}

	@RequestMapping(value = "export", method = RequestMethod.POST)
	public ResponseEntity<byte[]> export(HttpServletResponse response) {
		try {
			String fileName = "bundle数据表";
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			createExcel().write(os);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment",
					new String((fileName + ".xls").getBytes("UTF-8"), "iso-8859-1"));

			return new ResponseEntity<byte[]>(os.toByteArray(), headers, HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.error("Fail to export excel of bundles:", e);
		}

		return null;
	}

	private Workbook createExcel() throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("sheet");

		// 创建第一行
		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("资源名称");
		titleRow.createCell(1).setCellValue("资源类型");
		titleRow.createCell(2).setCellValue("设备账号");
		titleRow.createCell(3).setCellValue("设备密码");
		titleRow.createCell(4).setCellValue("bundle_id");
		titleRow.createCell(5).setCellValue("分组名称");
		// 增加 所属接入层
		titleRow.createCell(6).setCellValue("所属接入层");

		// 增加列(是否编码器)
		titleRow.createCell(7).setCellValue("是否编码器");

		// 增加列(是否解码器)
		titleRow.createCell(8).setCellValue("是否解码器");
		titleRow.createCell(9).setCellValue("设备IP");
		titleRow.createCell(10).setCellValue("设备端口");

		// 附加字段
		Set<String> extraInfoKeys = extraInfoService.queryNames();
		Map<String, Integer> extraInfoMap = new HashMap<>();
		int indexTemp = EXTRAINFO_START_COLUMN;
		for (String extraInfoKey : extraInfoKeys) {
			titleRow.createCell(indexTemp).setCellValue(extraInfoKey);
			extraInfoMap.put(extraInfoKey, indexTemp);
			indexTemp++;
		}

		// 查询有效bundle数据
		List<BundlePO> bundles = bundleDao.findLocalDevs();// bundleService.findByDeviceModelIsNotNull();//
															// bundleService.findAll();
		int index = 1;
		for (BundlePO bundle : bundles) {
			// 创建每一行内容
			Row row = sheet.createRow(index);
			row.createCell(0).setCellValue(bundle.getBundleName());
			row.createCell(1).setCellValue(bundle.getDeviceModel());
			row.createCell(2).setCellValue(bundle.getUsername());
			row.createCell(3).setCellValue(bundle.getOnlinePassword());
			row.createCell(4).setCellValue(bundle.getBundleId());
			if (null != bundle.getFolderId()) {
				FolderPO folder = folderService.get(bundle.getFolderId());
				if (null != folder) {
					row.createCell(5).setCellValue(folder.getName());
				}
			}

			if (null != bundle.getAccessNodeUid()) {
				row.createCell(6).setCellValue(bundle.getAccessNodeUid());
			}

			if ("jv210".equals(bundle.getDeviceModel())) {
				Integer coderDeviceType = channelSchemeService.getCoderDeviceType(bundle.getBundleId());
				if (coderDeviceType == 2) { // 编码器
					row.createCell(7).setCellValue("是");
//					row.createCell(7).setCellValue("否");
				} else if (coderDeviceType == 3) { // 解码器
//					row.createCell(6).setCellValue("否");
					row.createCell(8).setCellValue("是");
				}
			}

			if (null != bundle.getDeviceIp()) {
				row.createCell(9).setCellValue(bundle.getDeviceIp());
			}
			if (null != bundle.getDevicePort()) {
				row.createCell(10).setCellValue(String.valueOf(bundle.getDevicePort()));
			}

			// 附加字段
			List<ExtraInfoPO> extraInfos = extraInfoService.findByBundleId(bundle.getBundleId());
			for (ExtraInfoPO extraInfo : extraInfos) {
				row.createCell(extraInfoMap.get(extraInfo.getName())).setCellValue(extraInfo.getValue());
			}

			index++;
		}

		// 设置列宽
		for (int i = 0; i < 10 /* + extraInfoMap.size() */; i++) {
			if (i != 4) {
				sheet.setColumnWidth(i, 18 * 256);
			} else {
				sheet.setColumnWidth(i, 35 * 256);
			}
		}
		return workbook;
	}

	private JSONObject restTemplatePutAndReturn(String url, String heartBeatUrl) {
		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> paramters = new HashMap<>();

		paramters.put("msg_id", "test333");
		paramters.put("heartbeat_url", heartBeatUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String str = null;

		str = JSONObject.toJSONString(paramters);

		HttpEntity<String> entity = new HttpEntity<>(str, headers);

		ResponseEntity<String> resultEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		return JSONObject.parseObject(resultEntity.getBody());

	}
	
	/**
	 * 同步用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 上午10:46:51
	 */
	@RequestMapping(value = "/syncUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> syncUser() {
		Map<String, Object> data = makeAjaxData();
		try {
			
			bundleService.syncUser();
			
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			data.put(ERRMSG, "内部错误");
		}

		return data;
	}
	
}
