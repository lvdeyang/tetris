package com.sumavision.tetris.commons.util.httprequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);    //日志记录
	 
    /**
     * httpPost
     * @param url  路径
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
        return httpPost(url, jsonParam, false);
    }
    
    /**
     * JSONObject请求<br/>
     * <b>作者:</b>lzp<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年10月24日 下午3:36:22
     * @param url
     * @param jsonParam
     * @param noNeedResponse
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){
		return httpPost(url, jsonParam != null ? jsonParam.toJSONString() : "", "application/json", noNeedResponse);
    }
    
    /**
     * JSONObject请求<br/>
     * <b>作者:</b>lzp<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年10月24日 下午3:36:22
     * @param url
     * @param jsonParam
     */
    public static JSONObject httpFormPost(String url, String jsonParam){
    	return httpPost(url, jsonParam, "application/x-www-form-urlencoded");
    }
    
    /**
     * JSONObject请求<br/>
     * <b>作者:</b>lzp<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年10月24日 下午3:36:22
     * @param url
     * @param jsonParam
     */
    public static JSONObject httpJsonPost(String url, String jsonParam){
    	return httpPost(url, jsonParam, "application/json");
    }
    
    /**
     * JSONObject请求<br/>
     * <b>作者:</b>lzp<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2019年10月24日 下午3:36:22
     * @param url
     * @param jsonParam
     * @param noNeedResponse
     */
    public static JSONObject httpPost(String url, String jsonParam , String contentType){
		return httpPost(url, jsonParam != null ? jsonParam : "", contentType == null || contentType.isEmpty() ? "application/json" : contentType, false);
    }
    
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url, String param, String contentType, boolean noNeedResponse){
        //post请求返回结果
//		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient = HttpClientBuilder.create().build();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        method.setConfig(requestConfig);
        try {
            if (null != param) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(param, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType(contentType == null || contentType.isEmpty() ? "application/json" : contentType);
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.parseObject(str);
                    System.out.println(jsonResult);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    public static void downloadTask(String url, String param, String filePath) throws Exception {
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpPost post = new HttpPost(url);
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        post.setConfig(requestConfig);
        StringEntity entity = new StringEntity(param, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
		
		CloseableHttpResponse response = httpClient.execute(post);
		File file = new File(filePath);
		File folder = file.getParentFile();
		if(!folder.exists()) folder.mkdirs();
		if(!file.exists()) file.createNewFile();
		InputStream in = null;
		OutputStream out = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try{
			if(response.getStatusLine().getStatusCode() == 200){
				in = response.getEntity().getContent();
				out = new FileOutputStream(file);
				byte[] buff = new byte[100];  
		        int rc = 0;  
		        while ((rc = in.read(buff, 0, 100)) > 0) {  
		        	out.write(buff, 0, rc);  
		        } 
				out.flush();
			}else{
			}
		}finally{
			if(in != null) in.close();
			if(out != null) out.close();
			EntityUtils.consume(response.getEntity());
		}
    }
    
    /**
     * post请求
     * @param String url 请求url地址
     * @param String xmlParam 参数
     * @return String 返回的xml内容
     */
    public static String httpXmlPost(String url,String xmlParam){
    	//post请求返回结果
//		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient = HttpClientBuilder.create().build();
        String xmlResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != xmlParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(xmlParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            try {
            	/**读取服务器返回过来的xml字符串数据**/
            	xmlResult = EntityUtils.toString(result.getEntity());
            } catch (Exception e) {
            	logger.error("post请求提交失败:" + url, e);
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return xmlResult;
    }
    
    public static Map<String,Object> uploadFileByHTTP(File postFile,String postUrl,Map<String,String> postParam){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //把一个普通参数和文件上传给下面这个地址    是一个servlet
            HttpPost httpPost = new HttpPost(postUrl);
            //把文件转换成流对象FileBody
            FileBody fundFileBin = new FileBody(postFile);
            //设置传输参数
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.addPart(postFile.getName(), fundFileBin);//相当于<input type="file" name="media"/>
            //设计文件以外的参数
            Set<String> keySet = postParam.keySet();
            for (String key : keySet) {
                //相当于<input type="text" name="name" value=name>
                multipartEntity.addPart(key, new StringBody(postParam.get(key), ContentType.create("text/plain", Consts.UTF_8)));
            }

            HttpEntity reqEntity =  multipartEntity.build();
            httpPost.setEntity(reqEntity);

            //发起请求   并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                //打印响应状态
                //log.info(response.getStatusLine());
                resultMap.put("statusCode", response.getStatusLine().getStatusCode());
                //获取响应对象
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    //打印响应内容
                    resultMap.put("data", EntityUtils.toString(resEntity,Charset.forName("UTF-8")));
                }
                //销毁
                EntityUtils.consume(resEntity);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally{
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }
    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static JSONObject httpGet(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
//            DefaultHttpClient client = new DefaultHttpClient();
        	HttpClient client = HttpClientBuilder.create().build();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.parseObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }
}
