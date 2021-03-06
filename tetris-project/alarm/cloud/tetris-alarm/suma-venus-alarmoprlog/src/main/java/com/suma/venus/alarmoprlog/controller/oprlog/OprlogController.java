package com.suma.venus.alarmoprlog.controller.oprlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.alarmoprlog.orm.dao.IOprlogDAO;
import com.suma.venus.alarmoprlog.orm.entity.OprlogPO;
import com.suma.venus.alarmoprlog.service.oprlog.HandleReceiveOprlogThread;
import com.suma.venus.alarmoprlog.service.oprlog.OprlogService;
import com.suma.venus.alarmoprlog.service.oprlog.vo.OprlogVO;
import com.suma.venus.alarmoprlog.service.oprlog.vo.QueryOprlogVO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO;

@Controller
@RequestMapping("/oprlog")
public class OprlogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OprlogController.class);

	@Autowired
	private IOprlogDAO oprlogDAO;

	@Autowired
	private OprlogService oprlogService;

	@RequestMapping(value = "/triggerOprlog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> triggerOprlog(@RequestBody OprlogParamBO oprlogParamBO) {

		// ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();
		HandleReceiveOprlogThread.push(oprlogParamBO);
		data.put("errMsg", "");
		return data;

	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delOprlog(@RequestParam(value = "ids") String ids) {

		// ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		if (StringUtils.isEmpty(ids)) {
			data.put("errMsg", "参数错误");
		}

		try {

			String[] idArr = ids.split(",");

			for (String id : idArr) {
				oprlogDAO.delete(Long.valueOf(id));
			}

		} catch (Exception e) {
			// TODO: handle exception
			data.put("errMsg", "内部错误");
			return data;
		}

		data.put("errMsg", "");
		return data;

	}

	@RequestMapping(value = "/queryPage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryOprlog(@ModelAttribute QueryOprlogVO queryOprlogVO) {

		// TODO ajax请求遇到跨域问题 未解决,现在为post表单方式
		Map<String, Object> data = new HashMap<String, Object>();

		// Pageable pageable = PageRequest.of(queryOprlogVO.getPageIndex(),
		// queryOprlogVO.getPageSize(),
		// Sort.Direction.DESC, "id");

		Pageable pageable = new PageRequest(queryOprlogVO.getPageIndex(), queryOprlogVO.getPageSize(),
				Sort.Direction.DESC, "id");

		try {

			Page<OprlogPO> oprlogPOPage = oprlogService.queryOprlogByQueryPage(queryOprlogVO, pageable);

			List<OprlogVO> oprlogVOs = new ArrayList<>();

			if (!oprlogPOPage.getContent().isEmpty()) {
				oprlogVOs = OprlogVO.transFromPOs(oprlogPOPage.getContent());
			}

			data.put("errMsg", "");
			data.put("total", oprlogPOPage.getTotalElements());
			data.put("oprlogVOs", JSONObject.toJSON(oprlogVOs));

		} catch (Exception e) {
			LOGGER.error("OprlogController queryOprlog() exception: " + e.toString());
			data.put("errMsg", "内部错误");
		}

		return data;
	}
}
