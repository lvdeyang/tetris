
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-ui 上午10:59:33  
* All right reserved.  
*  
*/

package com.sumavision.bvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.aop.annotation.WebLog;
import com.sumavision.bvc.DTO.PreviewDTO;
import com.sumavision.bvc.DTO.ResultMap;
import com.sumavision.bvc.monitor.logic.bussiness.PreviewBussinessLogic;
import com.sumavision.bvc.repository.PreviewRespository;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * @desc: bvc-monitor-ui
 * @author: cll 逻辑层业务json
 * @createTime: 2018年6月5日 上午10:59:33
 * @history:
 * @version: v1.0
 */
@Controller
@RequestMapping("/demo/preview")
@Slf4j
public class PreviewController {

	@Autowired
	PreviewRespository previewDao;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private PreviewBussinessLogic previewBussinessLogic;

	@WebLog
	@GetMapping(value = "")
	public String monitorMain(Model model) {
		String condition = "{\"userId\": 1, \"device_model\": \"ipc\"}";
		List<BundleBody> res = resourceService.queryBundles(condition);
		model.addAttribute("camerasAvailable", res);
//		log.info(res.toString());
		return "monitorMain";
	}

	@WebLog
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public ResultMap addPreview(@RequestParam("previewJson") String previewJson) {
		log.info("add preview");
		JSONObject jsonRecordData = JSONObject.fromObject(previewJson);
		PreviewDTO previewDTO = (PreviewDTO) JSONObject.toBean(jsonRecordData, PreviewDTO.class);
		previewBussinessLogic.executeBussiness(previewDTO);
		return ResultMap.ok();
	}

	@WebLog
	@RequestMapping(value = "/preview/getAll", method = RequestMethod.GET)
	@ResponseBody
	public ResultMap getAllPreview() {
		String previewJson = "";
		// TaskLogicManager taskLogicManager = new
		// TaskLogicManager(previewJson);
		return ResultMap.ok();

	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/test/rest")
	public Object testRest(HttpServletRequest request){
		TestServiceImpl test = SpringContext.getBean(TestServiceImpl.class);
		test.test();
		return 200;
	}
	
}
