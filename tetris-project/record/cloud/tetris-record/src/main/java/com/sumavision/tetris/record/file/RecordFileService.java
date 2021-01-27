package com.sumavision.tetris.record.file;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sumavision.tetris.record.external.ffmpeg.FFMpegTransThread;
import com.sumavision.tetris.record.file.RecordFilePO.ERecordFileStatus;
import com.sumavision.tetris.record.storage.StorageDAO;
import com.sumavision.tetris.record.storage.StoragePO;
import com.sumavision.tetris.record.strategy.RecordStrategyDAO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;

import ch.qos.logback.classic.Logger;

@Service
public class RecordFileService {

	@Autowired
	private RecordFileDAO recordFileDAO;

	@Autowired
	private StorageDAO storageDAO;

	@Autowired
	private RecordStrategyDAO recordStrategyDAO;

	@Value("${spring.cloud.client.ipAddress}")
	private String centerIP;

	/**
	 * @return
	 */
	public Page<RecordFilePO> queryRecordFileByMultiParamsPage(Long recordStrategyId, Pageable pageable) {

		Page<RecordFilePO> recordFilePOPage = recordFileDAO.findAll(new Specification<RecordFilePO>() {

			@Override
			public Predicate toPredicate(Root<RecordFilePO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicateList = new ArrayList<>();

				if (recordStrategyId != null && recordStrategyId != 0) {
					predicateList.add(cb.equal(root.get("recordStrategyId").as(Long.class), recordStrategyId));
				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		}, pageable);

		return recordFilePOPage;

	}

	public String uploadMims(RecordFilePO recordFilePO) throws Exception {

		StoragePO storagePO = storageDAO.findOne(recordFilePO.getStorageId());
		Map<String, String> recordFileUrlMap = getFileUrl(recordFilePO, storagePO);

		RecordStrategyPO recordStrategyPO = recordStrategyDAO.findOne(recordFilePO.getRecordStrategyId());
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyMMdd-HHmmss");

		String startTime = myFmt.format(recordFilePO.getStartTime());
		String uploadName = recordStrategyPO.getName() + "_" + startTime;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// String timestamp = String.valueOf(System.currentTimeMillis());
		NameValuePair appIdPair = new BasicNameValuePair("appId", "2a36d94c0c3c5045310a3db0bec9eeb97f74");
		NameValuePair timestampPair = new BasicNameValuePair("timestamp", "1555925360574");
		NameValuePair appSecretPair = new BasicNameValuePair("appSecret", "123456");
		NameValuePair signPair = new BasicNameValuePair("sign",
				"bb30877d04ce9abf4d02b3a14c06aa286540fd2bd39b221ed6801e95319e3a3e");
		NameValuePair namePair = new BasicNameValuePair("name", uploadName);
		NameValuePair httpUrlPair = new BasicNameValuePair("httpUrl", recordFileUrlMap.get("httpVodUrl"));
		NameValuePair ftpUrlPair = new BasicNameValuePair("ftpUrl", recordFileUrlMap.get("ftpUrl"));

		params.add(appIdPair);
		params.add(timestampPair);
		params.add(appSecretPair);
		params.add(signPair);
		params.add(namePair);
		params.add(httpUrlPair);
		params.add(ftpUrlPair);

		String mimsUrl = "http://" + centerIP + ":8082/tetris-mims/api/server/media/video/task/add/remote";

		System.out.println("upload to mims, url=" + mimsUrl + ", params=" + params.toString());

		String postForString = postWithParamsForString(mimsUrl, params);

		System.out.println("upload to mims, resp=" + postForString);
		
		return postForString;

	}

	public void startffMpegTrans(RecordFilePO recordFilePO, RecordStrategyPO recordStrategyPO) {

		// TODO
		StoragePO storagePO = storageDAO.findOne(recordFilePO.getStorageId());

		FFMpegTransThread ffMpegTransThread = new FFMpegTransThread(recordFilePO, storagePO, recordStrategyPO, this,
				recordFileDAO);

		ffMpegTransThread.run();

	}

	public Map<String, String> getFileUrl(RecordFilePO recordFilePO, StoragePO storagePO) {

		Map<String, String> urlMap = new HashMap<String, String>(4);

		String httpVodUrl = null;
		String httpLiveUrl = null;
		String ftpUrl = null;

		if (recordFilePO.getStatus().equals(ERecordFileStatus.RECORD_SUC)) {

			// TODO
			String vodPath = null;
			try {
				String recordXmlUrl = storagePO.getHttpBasePath() + recordFilePO.getFilePath() + "/record.xml";
				
				
				
				System.out.println("recordXmlUrl" + recordXmlUrl);

				// String t = restTemplate.getForObject(recordXmlUrl, String.class);

				String t = getWithOutParams(recordXmlUrl);

				System.out.println("record.xml=" + t);

				Document doc = DocumentHelper.parseText(t);
				Element root = doc.getRootElement();

				@SuppressWarnings("unchecked")
				List<Element> folderElements = root.elements("folder");
				if (CollectionUtils.isEmpty(folderElements)) {
					return null;
				}

				vodPath = "/" + folderElements.get(folderElements.size() - 1).getText();

			} catch (Exception e) {
				// data.put("errMsg", "http服务错误");
				e.printStackTrace();
				return null;
			}

			httpVodUrl = storagePO.getHttpBasePath() + recordFilePO.getFilePath() + vodPath + "/vod.m3u8";
			ftpUrl = storagePO.getFtpBasePath() + recordFilePO.getFilePath() + vodPath + "/vod.m3u8";

		} else {

			httpLiveUrl = storagePO.getHttpBasePath() + recordFilePO.getFilePath() + "/stream.m3u8";
		}

		urlMap.put("httpVodUrl", httpVodUrl);
		urlMap.put("httpLiveUrl", httpLiveUrl);
		urlMap.put("ftpUrl", ftpUrl);

		return urlMap;
	}

	public void delRecordFile(RecordFilePO recordFilePO) {
		
		//TODO
		

	}
	
	
	
	public void delRecordFile(List<RecordFilePO> recordFilePOList) {

		for (RecordFilePO recordFilePO : recordFilePOList) {
			delRecordFile(recordFilePO);
		}

	}

	public String getWithOutParams(String url) {
		HttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		String s = "";
		try {
			httpGet.setHeader("Content-type", "text/html");
			HttpResponse response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				s = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public String postWithParamsForString(String url, List<NameValuePair> params) {
		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		String s = "";
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
			HttpResponse response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				s = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

}
