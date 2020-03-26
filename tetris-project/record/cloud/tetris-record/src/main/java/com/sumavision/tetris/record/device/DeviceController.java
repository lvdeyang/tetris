package com.sumavision.tetris.record.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.record.device.DevicePO.EDeviceOnlineStatus;
import com.sumavision.tetris.resouce.feign.bundle.BundleFeignVO;
import com.sumavision.tetris.resouce.feign.bundle.BundleService;

@Controller
@RequestMapping(value = "/record/device")
public class DeviceController {

	@Autowired
	DeviceDAO deviceDAO;

	@Autowired
	private BundleService bundleService;

	@RequestMapping("/query")
	@ResponseBody
	public Object queryRecordDevice(@RequestParam(value = "keyword", required = false) String keyword,
			Integer currentPage, Integer pageSize) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			// TODO
			// List<DevicePO> devicePOs = deviceDAO.findByKeywordLike(keyword);

			List<DevicePO> devicePOs = deviceDAO.findAll();

			List<DeviceVO> deviceVOs = DeviceVO.transFromPOs(devicePOs);

			data.put("errMsg", "");
			data.put("recordDeviceVOs", deviceVOs);
			data.put("totalNum", deviceVOs.size());

		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	@RequestMapping("/queryFromFeign")
	@ResponseBody
	public Object queryDeviceFromFeign() {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<BundleFeignVO> bundleFeignVOS = bundleService.queryTranscodeDevice();

			if (CollectionUtils.isEmpty(bundleFeignVOS)) {
				data.put("errMsg", "找不到设备");
				return data;
			}

			data.put("deviceFeignVOs", bundleFeignVOS);
			data.put("errMsg", "");

		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	@RequestMapping("/add")
	@ResponseBody
	public Object addRecordDevice(String deviceName, String deviceUuid, String deviceIp, Integer devicePort,
			String status) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			DevicePO devicePO = new DevicePO();
			devicePO.setDeviceName(deviceName);
			devicePO.setDeviceUuid(deviceUuid);
			devicePO.setDeviceIP(deviceIp);
			devicePO.setDevicePort(devicePort);
			try {
				devicePO.setOnlineStatus(EDeviceOnlineStatus.fromString(status));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				devicePO.setOnlineStatus(EDeviceOnlineStatus.OFFLINE);
			}

			deviceDAO.save(devicePO);

			data.put("errMsg", "");

		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;
	}

	@RequestMapping("/del")
	@ResponseBody
	public Object delRecordDevice(Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {

			deviceDAO.delete(id);
			data.put("errMsg", "");

		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;
	}

}
