package com.sumavision.tetris.business.yjgb.controller.feign;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

	@Autowired
	private TransformService transformService;
	
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
		
		List<OutParamVO> outputParams = JSONObject.parseArray(outputParam, OutParamVO.class);
		
		//添加输出
		transformService.addStreamOutput(id, outputParams);
		
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
		
		List<OutParamVO> outputParams = JSONObject.parseArray(outputParam, OutParamVO.class);

		//删除输出
		transformService.deleteStreamOutput(id, outputParams);
		
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
		
		//删除任务全部输出
		System.out.println("删除任务全部输出" + id);
		transformService.deleteAllOutput(id);
		
		return null;
	}
	
}
