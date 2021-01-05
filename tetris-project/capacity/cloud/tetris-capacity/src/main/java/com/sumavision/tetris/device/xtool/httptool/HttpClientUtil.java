package com.sumavision.tetris.device.xtool.httptool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用httpclient 进行身份验证
 */

@Component
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);


    /**
     * 小工具https加密方法
     * @param url
     * @param obj
     * @return
     */
    public JSONObject httpsPostWithDigestAuth(String url, JSONObject obj) throws BaseException {

        JSONObject resultObject = null;

        try {
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder().loadTrustMaterial(null,new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(100);

            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(getIpFromUrl(url), getPortFromUrl(url), "example.com", AuthScope.ANY_SCHEME),
                    new UsernamePasswordCredentials("Admin", "sumavisionrd"));

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setConnectionManager(cm)
                    .setDefaultCredentialsProvider(credsProvider)
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(3,true))
                    .build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(format(obj), StandardCharsets.UTF_8));
//            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
//            httpPost.setConfig(requestConfig);

            logger.info("send message to {}.\n{}" , url , obj);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            resultObject = JSON.parseObject(result);
            logger.info("ack from {}.\n{}" , url , result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }catch(HttpHostConnectException e){
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"connect error, http host exception");
        } catch (UnsupportedEncodingException e){
            throw new BaseException(StatusCode.ERROR,"encode error");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"io error");
        } catch (Exception e){
            logger.error("msg ack err",e);
            throw new BaseException(StatusCode.ERROR,"msg ack err");
        }

        return  resultObject;
    }

    
    public List<NameValuePair> format(JSONObject jsonObject) {
        List<NameValuePair> params = new ArrayList();
        if (jsonObject != null && !jsonObject.isEmpty()) {
            jsonObject.entrySet().forEach(e -> {
                formatObj(params , e.getKey() , e.getValue());
            });
        }
        return params;
    }

    private void format(List params,String pre , JSONObject jsonObject) {
        jsonObject.entrySet().stream().forEach(objectEntry -> {
            String key = null == pre || pre.isEmpty() ? objectEntry.getKey() : pre + "[" + objectEntry.getKey() + "]";
            Object obj = objectEntry.getValue();
            formatObj(params, key , obj);
        });
    }

    private void format(List params, String pre, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            String key = pre + "[" + i + "]";
            Object obj = jsonArray.get(i);
            formatObj(params, key , obj);
        }
    }

    private void formatObj(List params,String key , Object obj) {
        if (obj instanceof JSONArray)
            format (params, key, (JSONArray)obj);
        else if (obj instanceof JSONObject)
            format (params, key, (JSONObject)obj);
        else {
            BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, obj.toString());
            params.add(basicNameValuePair);
//            System.out.println(key + "=" + obj);
        }
    }

    public static String getSocketFromUrl(String url) {
        String host="";//格式为ip:port
        // 1.判断是否为空
        if (url == null || url.trim().equals("")) {
            return "";
        }
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group() ;
        }
        return host;
    }

    public static String getIpFromUrl(String url) {
        String ip="";//格式为ip:port
        // 1.判断是否为空
        if (url == null || url.trim().equals("")) {
            return "";
        }
        Pattern p = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            ip = matcher.group() ;
        }
        return ip;
    }

    public static Integer getPortFromUrl(String url) {
        String socketAddr = getSocketFromUrl(url);
        return getPort(socketAddr);
    }

    public static Integer getPort(String socketAddress){
        return Integer.parseInt(socketAddress.trim().split(":")[1]);
    }
    
}
