package com.sumavision.tetris.business.yjgb.controller.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.yjgb.service.TransformService;
import com.sumavision.tetris.business.yjgb.vo.OutParamVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/capacity/transform/feign")
public class TransformFeignController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransformFeignController.class);

	@Autowired
	private TransformService transformService;

	/**
	 * 文件刷表，注：接口从媒资服务调的（应急广播业务需求）
	 * @param deviceIp  刷源设备IP
	 * @param url  刷源地址
	 * @param request
	 * @return 返回刷表信息
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/analysis/input")
	public Object analysis(String deviceIp, String url, HttpServletRequest request) throws Exception{
		LOGGER.info("[yjgb]<analysis-input> syn from {}, ip: {}, url:{}",request.getRemoteAddr()+":"+request.getRemotePort(),deviceIp,url);
		String result = transformService.analysisInput(deviceIp, url);
		LOGGER.info("[yjgb]<analysis-input> ack, result: {}",result);
		return result;
	}

	/**
	 * 添加输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午5:54:17
	 * @param String id 任务标识
	 * @param String outputParam 添加输出信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/output")
	public Object addOutput(String id, String outputParam, HttpServletRequest request) throws Exception{
		LOGGER.info("[yjgb]<add-output> syn from {}, id: {}, param:{}",request.getRemoteAddr()+":"+request.getRemotePort(),id,outputParam);
		List<OutParamVO> outputParams = JSONObject.parseArray(outputParam, OutParamVO.class);
		//添加输出
		transformService.addStreamOutput(id, outputParams);
		LOGGER.info("[yjgb]<add-output> ack, id: {}",request.getRemoteAddr()+":"+request.getRemotePort(),id);
		return null;
	}
	
	/**
	 * 删除输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午5:53:47
	 * @param String id 任务标识
	 * @param String outputParam 删除输出信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/output")
	public Object deleteOutput(String id, String outputParam, HttpServletRequest request) throws Exception {
		LOGGER.info("[yjgb]<delete-output> syn from {}, id: {}, param:{}",request.getRemoteAddr()+":"+request.getRemotePort(),id,outputParam);
		List<OutParamVO> outputParams = JSONObject.parseArray(outputParam, OutParamVO.class);
		//删除输出
		transformService.deleteStreamOutput(id, outputParams);
		LOGGER.info("[yjgb]<delete-output> ack, id: {}",id);
		return null;
	}
	
	/**
	 * 删除一个任务的全部输出<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月3日 下午5:55:44
	 * @param String id 任务标识
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/all")
	public Object deleteOutput(String id, HttpServletRequest request) throws Exception {
		LOGGER.info("[yjgb]<delete-all> syn from {}, id: {}",request.getRemoteAddr()+":"+request.getRemotePort(),id);
		//删除任务全部输出
		System.out.println("删除任务全部输出" + id);
		transformService.deleteAllOutput(id);
		LOGGER.info("[yjgb]<delete-all> ack, id: {}",id);

		return null;
	}
	
}
