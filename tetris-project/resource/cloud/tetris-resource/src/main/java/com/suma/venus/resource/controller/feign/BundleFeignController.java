package com.suma.venus.resource.controller.feign;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.vo.BundleFeignVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping("/feign/bundle")
public class BundleFeignController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleFeignController.class);

	@Autowired
	private BundleService bundleService;

	@Autowired
	private BundleDao bundleDao;

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
