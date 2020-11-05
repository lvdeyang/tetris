package com.sumavision.tetris.mims.app.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;



public class BoUtil {

	public static boolean uploadFile(String filePath, String dst, String ftpServer, int ftpPort, String userName,
			String password) {
		FtpUtil ftpUtil = new FtpUtil(ftpServer, ftpPort, userName, password);
		filePath = filePath.replace("\\", "/");
		String[] filePaths = filePath.split("/");
		ftpUtil.uploadFile(dst, filePaths[filePaths.length - 1], filePath);
		return true;
	}

	public static String injectBo(AdiBo bo) throws Exception {
		// TODO Auto-generated method stub
		// 1.创建SAXReader对象用于读取xml文件
		SAXReader reader = new SAXReader();
		// 2.读取xml文件，获得Document对象
		Document doc = reader.read(ResourceUtils.getFile("classpath:adi.xml"));
		// 3.获取根元素
		Element root = doc.getRootElement();
		// 4.获取根元素下的所有子元素（通过迭代器）
		Iterator<Element> it = root.elementIterator();
		while (it.hasNext()) {

			Element e = it.next();

			if (e.getName().equals("OpenGroupAsset")) {
				Element openElement = e.element("VODRelease");
				openElement.setAttributeValue("assetID", bo.getMediaId());
			} else if (!e.getName().equals("AcceptContentAsset")) {
				e.setAttributeValue("groupAssetID", bo.getMediaId());
				List<Element> innerElements = e.elements();
				for (Element innerElement : innerElements) {
					innerElement.setAttributeValue("assetID", bo.getMediaId());
					if (innerElement.getName().equals("Title")) {
						Element subinele = innerElement.element("TitleFull");
						subinele.setText(bo.getFileName());
						innerElement.element("programType").setText(bo.getFormat());
					}
				}

			}

		}
		System.out.println(doc.asXML());
		return doc.asXML();
	}

	// post
	public static String post(String url, String params) throws Exception {
		return post(url, params, Locale.ENGLISH);
	}

	// post
	public static String post(String url, String params, Locale locale) throws Exception {
		return post(url, params, locale, null);
	}

	public static String post(String url, String params, Locale locale, RequestConfig config) throws Exception {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		StringBuilder sBuilder = null;
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			if (config != null)
				httpPost.setConfig(config);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept-Charset", "utf-8");
			httpPost.setHeader("Accept-Language", locale.toLanguageTag());

			httpPost.setEntity(new StringEntity(params, "utf-8"));
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				InputStream in = entity.getContent();
				sBuilder = new StringBuilder();
				String line = "";
				bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				while ((line = bReader.readLine()) != null) {
					sBuilder.append(line);
				}
				EntityUtils.consume(entity);
			} else {
				return response.getStatusLine().getStatusCode() + "@$$@请求失败";
			}
		} finally {
			if (bReader != null)
				bReader.close();
			if (response != null)
				response.close();
			if (httpclient != null)
				httpclient.close();
		}
		return sBuilder == null ? null : sBuilder.toString();
	}

}
