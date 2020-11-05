package com.sumavision.tetris.cs.upgrade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cs.area.AreaVO;
import com.sumavision.tetris.mims.app.media.compress.MediaCompressVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/upgrade")
public class CsUpgradeController {
	@Autowired
	private CsUpgradeService upgradeService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/start")
	public Object startUpgrade(
			String version,
			String way,
			String compress,
			String areaList,
			HttpServletRequest request) throws Exception {
		CsUpgradeBroadWay broadWay = CsUpgradeBroadWay.fromName(way);
		
		MediaCompressVO compressVO = compress != null && !compress.isEmpty()
				? JSONObject.parseObject(compress, MediaCompressVO.class) : null;
		
		List<AreaVO> areaVOs = areaList != null && !areaList.isEmpty()
				? JSONArray.parseArray(areaList, AreaVO.class) : null;
		
		upgradeService.start(version, broadWay, compressVO, areaVOs);
		
		return null;
	}
}
