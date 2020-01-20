package com.suma.venus.alarmoprlog.controller.alarm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.dao.IAlarmInfoDAO;
import com.suma.venus.alarmoprlog.orm.dao.IRawAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EBlockStatus;
import com.suma.venus.alarmoprlog.service.alarm.AlarmInfoService;
import com.suma.venus.alarmoprlog.service.alarm.vo.AlarmInfoVO;

/**
 * 告警基本信息 AlarmInfo 的 controller
 * 
 * @author chenmo
 *
 */
@Controller
@RequestMapping("/alarmInfo")
public class AlarmInfoController {

	@Autowired
	private IAlarmInfoDAO alarmInfoDAO;

	@Autowired
	private IRawAlarmDAO rawAlarmDAO;

	@Autowired
	private IAlarmDAO alarmDAO;

	@Autowired
	private AlarmInfoService alarmInfoService;

	/**
	 * 按条件查询告警基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryPage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryAlarmInfo(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {

		Map<String, Object> data = new HashMap<String, Object>();

		try {

			//Pageable pageable = PageRequest.of(pageIndex, pageSize);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			

			Page<AlarmInfoPO> alarmInfoPage = alarmInfoDAO.findByKeywordContaining(pageable,
					keyword == null ? "" : keyword);

			List<AlarmInfoVO> alarmInfoVOs = AlarmInfoVO.transFromPOs(alarmInfoPage.getContent());

			data.put("total", alarmInfoPage.getTotalElements());
			data.put("alarmInfoVOs", JSONObject.toJSON(alarmInfoVOs));
			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 检查alarmCode是否重复
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkCode(@RequestParam(value = "alarmCode") String alarmCode) {

		Map<String, Object> data = new HashMap<String, Object>();

		try {

			AlarmInfoPO alarmInfoPO = alarmInfoDAO.findByAlarmCode(alarmCode);

			if (alarmInfoPO != null) {
				data.put("check", "exist");
				data.put("errMsg", "");
			}

			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 添加 新建告警基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@ModelAttribute AlarmInfoVO alarmInfoVO) {

		Map<String, Object> data = new HashMap<String, Object>();

		if (alarmInfoVO == null || StringUtils.isEmpty(alarmInfoVO.getAlarmCode())
				|| StringUtils.isEmpty(alarmInfoVO.getAlarmName()) || alarmInfoVO.getAlarmLevel() == null) {
			data.put("errMsg", "参数错误");
			return data;
		}

		try {
			AlarmInfoPO po = alarmInfoDAO.findByAlarmCode(alarmInfoVO.getAlarmCode());
			if (po != null) {
				data.put("errMsg", "alarmCode重复");
				return data;
			}

			AlarmInfoPO alarmInfoPO = new AlarmInfoPO();
			BeanUtils.copyProperties(alarmInfoVO, alarmInfoPO, "blockStatus");

			alarmInfoDAO.save(alarmInfoPO);
			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 编辑一个告警基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> edit(@ModelAttribute AlarmInfoVO alarmInfoVO) {

		Map<String, Object> data = new HashMap<String, Object>();

		if (alarmInfoVO == null || alarmInfoVO.getId() == null || StringUtils.isEmpty(alarmInfoVO.getAlarmCode())
				|| StringUtils.isEmpty(alarmInfoVO.getAlarmName()) || alarmInfoVO.getAlarmLevel() == null) {
			data.put("errMsg", "参数错误");
			return data;
		}

		try {
			Optional<AlarmInfoPO> alarmInfoPOOptional = alarmInfoDAO.findById(alarmInfoVO.getId());

			if (!alarmInfoPOOptional.isPresent()) {
				data.put("errMsg", "参数错误");
				return data;
			}

			AlarmInfoPO alarmInfoPO = alarmInfoPOOptional.get();
			BeanUtils.copyProperties(alarmInfoVO, alarmInfoPO, "id", "alarmCode", "blockStatus", "emailNotify",
					"SMSNotify", "editable");
			alarmInfoDAO.save(alarmInfoPO);
			data.put("errMsg", "");

		} catch (Exception e) {
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 删除一个告警基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> del(@RequestParam(value = "id", required = true) Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Optional<AlarmInfoPO> alarmInfoPOOptional = alarmInfoDAO.findById(id);

			if (!alarmInfoPOOptional.isPresent()) {
				data.put("errMsg", "参数错误");
				return data;
			}

			// TODO 删除关联的所有告警,这种做法不好，应该封装成一个事务。。。
			alarmDAO.deleteInBatch(
					alarmDAO.findByLastAlarm_AlarmInfo_AlarmCode(alarmInfoPOOptional.get().getAlarmCode()));

			rawAlarmDAO.deleteInBatch(rawAlarmDAO.findByAlarmInfo_AlarmCode(alarmInfoPOOptional.get().getAlarmCode()));

			alarmInfoDAO.delete(alarmInfoPOOptional.get());

			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 屏蔽一个告警基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shield", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> shieldAlarmInfo(@RequestParam(value = "id", required = true) Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {

			Optional<AlarmInfoPO> alarmInfoPOOptional = alarmInfoDAO.findById(id);

			if (!alarmInfoPOOptional.isPresent()) {
				data.put("errMsg", "参数错误");
				return data;
			}

			AlarmInfoPO alarmInfoPO = alarmInfoPOOptional.get();
			alarmInfoPO.setBlockStatus(EBlockStatus.BLOCKED);
			alarmInfoDAO.save(alarmInfoPO);
			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 恢复一个告警基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/unshield", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unShieldAlarmInfo(@RequestParam(value = "id", required = true) Long id) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {

			Optional<AlarmInfoPO> alarmInfoPOOptional = alarmInfoDAO.findById(id);

			if (!alarmInfoPOOptional.isPresent()) {
				data.put("errMsg", "参数错误");
				return data;
			}

			AlarmInfoPO alarmInfoPO = alarmInfoPOOptional.get();
			alarmInfoPO.setBlockStatus(EBlockStatus.NORMAL);
			alarmInfoDAO.save(alarmInfoPO);
			data.put("errMsg", "");

		} catch (Exception e) {
			// LOGGER.error(e.toString());
			data.put("errMsg", "内部错误");
		}
		return data;
	}

	/**
	 * 导入excel格式的告警信息列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> importAlarmInfo(@RequestParam("filePoster") MultipartFile uploadFile) {

		try {
			return alarmInfoService.importAlarmInfoExcel(uploadFile.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("errMsg", "内部错误");
			return data;
		}
	}

}
