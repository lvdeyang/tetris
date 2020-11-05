package com.sumavision.tetris.record.file;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.storage.StorageDAO;
import com.sumavision.tetris.record.storage.StoragePO;

@Controller
@RequestMapping(value = "/record/file")
public class RecordFileController {

	@Autowired
	RecordFileService recordFileService;

	@Autowired
	RecordFileDAO recordFileDAO;

	@Autowired
	StorageDAO storageDAO;

	@Autowired
	RestTemplate restTemplate;

	@ResponseBody
	@RequestMapping(value = "/query")
	public Object query(@RequestParam(value = "recordStrategyId") Long recordStrategyId, Integer pageIndex,
			Integer pageSize) {

		Map<String, Object> data = new HashMap<String, Object>();

		Pageable pageable = new PageRequest(pageIndex, pageSize, Sort.Direction.DESC, "id");
		try {
			Page<RecordFilePO> recordFilePOPage = recordFileService.queryRecordFileByMultiParamsPage(recordStrategyId,
					pageable);
			data.put("errMsg", "");
			data.put("totalNum", recordFilePOPage.getTotalElements());
			data.put("recordFileVOs", RecordFileVO.fromPOList(recordFilePOPage.getContent()));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;

	}

	@ResponseBody
	@RequestMapping(value = "/del")
	public Object delete(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();

		// TODO

		return data;
	}

	@ResponseBody
	@RequestMapping(value = "/preview")
	public Object preview(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();

		// TODO
		try {
			RecordFilePO recordFilePO = recordFileDAO.findOne(id);

			if (recordFilePO == null) {
				data.put("errMsg", "内部错误");
				return data;
			}

			StoragePO storagePO = storageDAO.findOne(recordFilePO.getStorageId());
			if (storagePO == null) {
				data.put("errMsg", "内部错误");
				return data;
			}

			String previewUrl = null;

			Map<String, String> fileUrlMap = recordFileService.getFileUrl(recordFilePO, storagePO);

			if (recordFilePO.getStatus().equals(ERecordFileStatus.RECORD_SUC)) {
				previewUrl = fileUrlMap.get("httpVodUrl");
			} else {
				previewUrl = fileUrlMap.get("httpLiveUrl");
			}

			data.put("previewUrl", previewUrl);
			data.put("errMsg", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.put("errMsg", "内部错误");
			return data;
		}

		return data;
	}

	@ResponseBody
	@RequestMapping(value = "/uploadToMims")
	public Object uploadToMims(@RequestParam(value = "id") Long id) {

		Map<String, Object> data = new HashMap<String, Object>();

		try {
			RecordFilePO recordFilePO = recordFileDAO.findOne(id);

			if (recordFilePO == null) {
				data.put("errMsg", "内部错误");
				return data;
			}

			String resp = recordFileService.uploadMims(recordFilePO);
			data.put("resp", resp);

		} catch (Exception e) {
			e.printStackTrace();
			data.put("errMsg", "内部错误");
		}

		return data;
	}

}
