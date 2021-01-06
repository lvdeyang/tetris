package com.sumavision.tetris.record.storage;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/record/storage")
public class StorageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

	@Autowired
	private StorageService storageService;

	@Autowired
	private StorageDAO storageDAO;

	@ResponseBody
	@RequestMapping("/addStorage")
	public Map<String, Object> addStorage(@RequestParam StorageVO storageVO) {

		Map<String, Object> data = new HashMap<String, Object>();

		if (storageVO == null) {
			data.put("errMsg", "参数错误");
			return data;
		}

		StoragePO storagePO = null;

		try {

			if (storageVO.getId() == null || storageVO.getId() == 0) {
				storagePO = StorageVO.fromVO2PO(storageVO);
			} else {
				storagePO = storageDAO.findOne(storageVO.getId());
				BeanUtils.copyProperties(storageVO, storagePO, "id", "isMounted");
			}

			storageDAO.save(storagePO);
			data.put("errMsg", "");

			return data;

		} catch (Exception e) {
			LOGGER.error("addStorage exception, e=" + e);
			data.put("errMsg", "内部错误");
			return data;
		}
	}
	

	@ResponseBody
	@RequestMapping(value = "/queryStorage")
	public Map<String, Object> queryStorage(Integer pageIndex, Integer pageSize) {

		Map<String, Object> data = new HashMap<String, Object>();

		Pageable pageable = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id");
		try {
			Page<StoragePO> storagePOPage = storageDAO.findAll(pageable);
			data.put("errMsg", "");
			data.put("totalNum", storagePOPage.getTotalElements());
			data.put("StorageVOs", StorageVO.fromPOList(storagePOPage.getContent()));

		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;

	}
	
	public Map<String, Object> checkIsExistRecordTask(@RequestParam Long storageId){
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		
		
		
		return data;
	}

}
