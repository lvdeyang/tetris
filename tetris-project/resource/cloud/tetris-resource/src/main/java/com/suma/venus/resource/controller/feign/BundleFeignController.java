package com.suma.venus.resource.controller.feign;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.vo.BundleFeignVO;

@Controller
@RequestMapping("/feign/bundle")
public class BundleFeignController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleFeignController.class);

	@Autowired
	private BundleService bundleService;

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
