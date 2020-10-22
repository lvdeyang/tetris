package com.suma.venus.resource.controller.feign;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.ScreenSchemeDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ChannelSchemeService;
import com.suma.venus.resource.service.ChannelTemplateService;
import com.suma.venus.resource.task.BundleHeartBeatService;
import com.suma.venus.resource.vo.BundleFeignVO;
import com.sumavision.tetris.capacity.server.CapacityService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping("/feign/bundle")
public class BundleFeignController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleFeignController.class);

	@Autowired
	private BundleService bundleService;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private ChannelTemplateService channelTemplateService;

	@Autowired
	private ChannelSchemeService channelSchemeService;

	@Autowired
	private CapacityService capacityService;

	@Autowired
	private BundleHeartBeatService bundleHeartBeatService;

	@Autowired
	private ScreenSchemeDao screenSchemeDao;

	@Value("${realIP}")
	private String clientIP;

	@Value("${zuulPort}")
	private String zuulPort;

	/**
	 * 添加转码设备
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/addTranscodeDevice")
	public Object addTranscodeDevice(String name, String ip, Integer port) throws Exception {

		LOGGER.info("addTranscodeDevice feign api, params= " + name + "," + ip + ":" + port);
		Map<String, String> data = new HashMap<>();

		BundlePO bundlePO;

		try {

			List<BundlePO> bunldePOList = bundleDao.findByDeviceIpAndDevicePortAndDeviceModel(ip, port, "transcode");

			if (bunldePOList == null || bunldePOList.isEmpty()) {
				bundlePO = new BundlePO();

				bundlePO.setBundleName(name);
				bundlePO.setBundleAlias(ip);
				bundlePO.setUsername(ip);
				bundlePO.setOnlinePassword(ip);

				bundlePO.setDeviceIp(ip);
				bundlePO.setDevicePort(port);
				bundlePO.setDeviceModel("transcode");
				bundlePO.setBundleType(
						channelTemplateService.findByDeviceModel(bundlePO.getDeviceModel()).get(0).getBundleType());
				bundlePO.setBundleId(BundlePO.createBundleId());
				bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);
				bundleService.save(bundlePO);

				bundleService.configDefaultAbility(bundlePO);

			} else {
				LOGGER.info("addTranscodeDevice feign, device already exist");
				bundlePO = bunldePOList.get(0);
				bundlePO.setBundleName(name);
				bundlePO.setBundleAlias(ip);
				bundlePO.setUsername(ip);
				bundlePO.setOnlinePassword(ip);
				bundleService.save(bundlePO);
			}

		} catch (Exception e) {
			e.printStackTrace();

			data.put("bundle_id", "");
			return data;
		}

		data.put("bundle_id", bundlePO.getBundleId());

		try {
			LOGGER.info("add new transcode device, set heartbeaturl=" + "http://" + clientIP + ":" + zuulPort
					+ "/tetris-resource/api/thirdpart/bundleHeartBeat?bundle_ip=" + bundlePO.getDeviceIp());

			capacityService.setHeartbeatUrl(bundlePO.getDeviceIp(), "http://" + clientIP + ":" + zuulPort
					+ "/tetris-resource/api/thirdpart/bundleHeartBeat?bundle_ip=" + bundlePO.getDeviceIp());

			capacityService.setAlarmUrl(bundlePO.getDeviceIp());

		} catch (Exception e) {
			e.printStackTrace();
			data.put("bundle_set", "");
			return data;
		}

		data.put("bundle_set", "success");
		bundleHeartBeatService.addBundleStatus(ip, System.currentTimeMillis());
		LOGGER.info("addTranscodeDevice feign api, return = " + JSON.toJSONString(data));

		return data;

	}

	/**
	 * 重新设置能力发送心跳和告警的url
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resetHeartBeatAndAlarm")
	public Object resetHeartBeatAndAlarm(String bundle_id) throws Exception {

		LOGGER.info("resetHeartBeatAndAlarm feign api, bundle_id= " + bundle_id);

		try {

			BundlePO bundlePO = bundleService.findByBundleId(bundle_id);

			if (bundlePO == null) {
				return "";
			}

			LOGGER.info("add new transcode device, set heartbeaturl=" + "http://" + clientIP + ":" + zuulPort
					+ "/tetris-resource/api/thirdpart/bundleHeartBeat?bundle_ip=" + bundlePO.getDeviceIp());

			capacityService.setHeartbeatUrl(bundlePO.getDeviceIp(), "http://" + clientIP + ":" + zuulPort
					+ "/tetris-resource/api/thirdpart/bundleHeartBeat?bundle_ip=" + bundlePO.getDeviceIp());

			capacityService.setAlarmUrl(bundlePO.getDeviceIp());

			return "success";

		} catch (Exception e) {

			return "";
		}

	}

	/**
	 * 添加转码设备
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delTranscodeDevice")
	public Object delTranscodeDevice(String bundle_id) throws Exception {

		LOGGER.info("delTranscodeDevice feign api, bundle_id= " + bundle_id);

		try {

			BundlePO bundle = bundleService.findByBundleId(bundle_id);

			if (bundle == null) {
				return "";
			}

			if (bundle.getDeviceIp() != null) {
				bundleHeartBeatService.removeBundleStatus(bundle.getDeviceIp());
			}

			bundleService.delete(bundle);

			// 删除配置能力
			channelSchemeService.deleteByBundleId(bundle_id);
			// lockChannelParamDao.deleteByBundleId(bundleId);

			// 删除屏配置信息
			screenSchemeDao.deleteByBundleId(bundle_id);
			// lockScreenParamDao.deleteByBundleId(bundleId);

			// 删除设备上的锁定参数（如果有）
			// lockBundleParamDao.deleteByBundleId(bundleId);

			bundleHeartBeatService.removeBundleStatus(bundle.getDeviceIp());

			return "success";

		} catch (Exception e) {

			return "";

		}

	}

	/**
	 * 查询经纬度范围内的ipc设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午2:21:21
	 * 
	 * @param Long longitude 经度°
	 * @param Long latitude 纬度°
	 * @param Long raidus 半径范m
	 * @return List<BundlePO>
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/visible/bundle")
	public Object queryVisibleBundle(String longitude, String latitude, Long raidus, HttpServletRequest request)
			throws Exception {

		List<String> deviceModels = new ArrayList<String>();
		deviceModels.add("ipc");
		deviceModels.add("speaker");
		List<BundlePO> bundles = bundleDao.findByRaidus(longitude, latitude, raidus, deviceModels);

		List<YjgbVO> vos = new YjgbVO().getConverter(YjgbVO.class).convert(bundles, YjgbVO.class);

		return vos;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/queryTranscodeDevice", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object queryTranscodeDevice() {
		List<BundleFeignVO> bundleFeignVOs = new ArrayList<>();

		try {
			List<BundlePO> bundlePOList = bundleService.findByDeviceModel("transcode");

			if (!CollectionUtils.isEmpty(bundlePOList)) {

				for (BundlePO po : bundlePOList) {
					bundleFeignVOs.add(getBundleFeignVoFromPO(po));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Fail to query queryTranscodeDevice, ", e);
		}

		return bundleFeignVOs;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/queryDeviceByBundleId", produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object queryDeviceByBundleId(@RequestParam(value = "bundle_id") String bundle_id) {

		try {
			BundlePO bundlePO = bundleService.findByBundleId(bundle_id);

			if (bundlePO != null) {

				return getBundleFeignVoFromPO(bundlePO);

			}
		} catch (Exception e) {
			LOGGER.error("Fail to query queryTranscodeDevice, ", e);
		}

		return null;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/queryAuth", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object queryAuth(@RequestParam(value = "bundle_id") String bundle_id) {

		LOGGER.info("queryAuth in, bundle_id=" + bundle_id);

		BundlePO bundlePO = bundleService.findByBundleId(bundle_id);

		if (bundlePO == null) {
			LOGGER.error("cannot find bundlepo, bundle_id=" + bundle_id);
			return null;
		}

		String url = "http://" + bundlePO.getDeviceIp() + ":" + bundlePO.getDevicePort()
				+ "/v0.0/authorization?msg_id=test202";

		return restTemplateGet(url);

	}

	private BundleFeignVO getBundleFeignVoFromPO(BundlePO po) {
		BundleFeignVO bundle = new BundleFeignVO();
		bundle.setBundle_id(po.getBundleId());
		bundle.setDevice_model(po.getDeviceModel());
		bundle.setBundle_type(po.getBundleType());
		bundle.setDevice_version(po.getDeviceVersion());
		bundle.setDevice_ip(po.getDeviceIp());
		bundle.setDevice_port(po.getDevicePort());
		bundle.setBundle_name(po.getBundleName());
		bundle.setUsername(po.getUsername());
		bundle.setPassword(po.getOnlinePassword());
		bundle.setAccess_node_uid(po.getAccessNodeUid());
		bundle.setBundle_status(po.getOnlineStatus().toString());
		return bundle;
	}

	public Object restTemplateGet(String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
		String responseObj = restTemplate.getForObject(url, String.class);
		return responseObj;
	}

}
