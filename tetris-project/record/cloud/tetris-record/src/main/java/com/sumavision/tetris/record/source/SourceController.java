package com.sumavision.tetris.record.source;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class SourceController {
	
	// @Autowired
	// private SourceDAO sourceDAO;
	
	/**
	 * 
	 * 从媒资系统查询直播源信息
	 * 
	 * @param keyword
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/querySource")
	public Object querySourceInfo(String keyword, Integer currentPage, Integer pageSize) {

		// TODO
		return null;
	}
	
	@RequestMapping(value = "/addSource")
	@ResponseBody
	public Map<String, Object> addSource(@RequestParam(value = "recordName") String name,
			@RequestParam(value = "recordURL") String url, @RequestParam(value = "localIp") String localIp) {

		Map<String, Object> data = new HashMap<String, Object>();
		SourcePO sourcePO = new SourcePO();
		// TaskPO taskPO = new TaskPO();

		try {
			sourcePO.setName(name);
			sourcePO.setUrl(url);
			sourcePO.setLocalIp(localIp);
			// sourcePO.setvPort(vPort);
			// sourcePO.setaPort(aPort);
			// sourcePO.setRecordStatus(0);
			// sourcePO.setIsAnalyze(analyze);
			// sourceDAO.save(sourcePO);
			data.put("name", sourcePO.getName());
			data.put("errMsg", "");
			return data;
			

			// logger.info("addRecord save sourcePO ==" + JSONObject.toJSONString(sourcePO));
			
			/*
			 * 刷表
			boolean flag = channelRecordService.refreshSource(sourcePO);
			if (!flag) {
				data.put(ERRMSG, "刷表失败");
				sourceDAO.delete(sourcePO);// 回滚
				return data;
			}
			*/

			// boolean ftpFlag = false;
			
			/*
			List<StoragePO> storagePOs = storageDao.findAll();
			for (StoragePO storagePO : storagePOs) {
				if (storagePO.getAddress().contains(localIp)) {
					sourcePO.setFtpUrl(storagePO.getAddress());
					sourceDAO.save(sourcePO);
					ftpFlag = true;
				}
			}

			if (!ftpFlag && !storagePOs.isEmpty()) {
				sourcePO.setFtpUrl(storagePOs.get(0).getAddress());
				sourceDAO.save(sourcePO);
			}

			if (null == sourcePO.getFtpUrl() || sourcePO.getFtpUrl().isEmpty()) {
				data.put(ERRMSG, "无可用存储仓库，请先配置仓库");
				sourceDAO.delete(sourcePO);// 回滚
				return data;
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}
			
		return data;
	}


}
