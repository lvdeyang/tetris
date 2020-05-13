package com.sumavision.tetris.record.file;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.storage.StorageDAO;
import com.sumavision.tetris.record.storage.StoragePO;
import com.sumavision.tetris.record.strategy.RecordStrategyDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;

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
			String previewUrl = null;

			if (recordFilePO.getStatus().equals(ERecordFileStatus.RECORD_SUC)) {

				// TODO
				String vodPath = null;
				try {
					String recordXmlUrl = storagePO.getHttpBasePath() + recordFilePO.getFilePath() + "/record.xml";

					String t = restTemplate.getForObject(recordXmlUrl, String.class);

					Document doc = DocumentHelper.parseText(t);
					Element root = doc.getRootElement();

					@SuppressWarnings("unchecked")
					List<Element> folderElements = root.elements("folder");
					if (CollectionUtils.isEmpty(folderElements)) {
						data.put("errMsg", "http服务错误");
						return data;
					}

					vodPath = "/" + folderElements.get(folderElements.size() - 1).getText();

				} catch (Exception e) {
					data.put("errMsg", "http服务错误");
					e.printStackTrace();
				}

				previewUrl = storagePO.getHttpBasePath() + recordFilePO.getFilePath() + vodPath + "/vod.m3u8";

			} else {

				previewUrl = storagePO.getHttpBasePath() + recordFilePO.getFilePath() + "/stream.m3u8";
			}

			data.put("previewUrl", previewUrl);
			data.put("errMsg", "");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return data;
	}

}
