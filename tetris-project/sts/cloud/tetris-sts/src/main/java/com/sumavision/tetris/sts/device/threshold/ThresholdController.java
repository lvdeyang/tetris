package com.sumavision.tetris.sts.device.threshold;

import com.sumavision.tetris.sts.common.CommonController;
import com.sumavision.tetris.sts.device.monitor.DeviceMonitorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/threshold")
public class ThresholdController extends CommonController {

	@Autowired
	ThresholdDao thresholdDao;
	
	@Autowired
	DeviceMonitorService deviceMonitorService;
	
	static Logger logger = LogManager.getLogger(ThresholdController.class);
	
	@RequestMapping("/mainView")
    @ResponseBody
    public ModelAndView thresholdView() {
        ModelAndView modelAndView = new ModelAndView();
        //加载threshold.jsp页面
        ThresholdPO thresholdPO = thresholdDao.findTopByIdIsNotNull();
        modelAndView.addObject("cpuOccupyTh", thresholdPO.getCpuOccupyTh());
	   	modelAndView.addObject("gpuOccupyTh", thresholdPO.getGpuOccupyTh());
	   	modelAndView.addObject("memOccupyTh", thresholdPO.getMemOccupyTh());
	   	modelAndView.addObject("cpuTemperatureTh", thresholdPO.getCpuTemperatureTh());
	   	modelAndView.addObject("diskOccupyTh",thresholdPO.getDiskOccupyTh());
	   	modelAndView.addObject("netCardFlowMax", thresholdPO.getNetCardFlowMax());
        modelAndView.setViewName("threshold");
        return modelAndView;
    }

	@RequestMapping("/updateThreshold")
	@ResponseBody
	public Map<String, Object> updateThreshold(@RequestParam("cpuOccupyTh") Integer cpuOccupyTh,
			@RequestParam("gpuOccupyTh") Integer gpuOccupyTh,
			@RequestParam("memOccupyTh") Integer memOccupyTh,
			@RequestParam("cpuTemperatureTh") Integer cpuTemperatureTh,
			@RequestParam("diskOccupyTh") Integer diskOccupyTh,
			@RequestParam("netCardFlowMax") Long netCardFlowMax){
		Map<String, Object> data = makeAjaxData();
		try {
			deviceMonitorService.setDeviceThresholdData(cpuOccupyTh, gpuOccupyTh, memOccupyTh, cpuTemperatureTh,diskOccupyTh,netCardFlowMax*1000000);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateThreshold err",e);
			data.put("errMsg", "修改阈值失败");
		}
		return data;
	}


	@RequestMapping("/getThreshold")
	@ResponseBody
	public Map<String, Object> getThreshold(){
		Map<String, Object> data = makeAjaxData();
		try {
			ThresholdPO thresholdPO = thresholdDao.findTopByIdIsNotNull();
			thresholdPO.setNetCardFlowMax(thresholdPO.getNetCardFlowMax()/1000000);
			data.put("threshold",thresholdPO);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getThreshold err",e);
			data.put("errMsg", "获取阈值失败");
		}
		return data;
	}




}
