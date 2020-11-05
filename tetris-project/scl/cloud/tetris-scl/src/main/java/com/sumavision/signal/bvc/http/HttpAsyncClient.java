package com.sumavision.signal.bvc.http;

import java.net.URI;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 异步的HTTP请求对象，可设置代理 
 */  

public class HttpAsyncClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAsyncClient.class);


    private static int socketTimeout = 15000;// 设置等待数据超时时间5秒钟 根据业务调整  
  
    private static int connectTimeout = 2000;// 连接超时  
  
    private static int poolSize = 3000;// 连接池最大连接数  
  
    private static int maxPerRoute = 1500;// 每个主机的并发最多只有1500  
  
    // http代理相关参数  
    private String host = "";  
    private int port = 0;  
    private String username = "";  
    private String password = "";  
  
    // 异步httpclient  
    private CloseableHttpAsyncClient asyncHttpClient;  
  
    // 异步加代理的httpclient  
    private CloseableHttpAsyncClient proxyAsyncHttpClient;  
    
    private HttpAsyncClient() {  
        try {  
            this.asyncHttpClient = createAsyncClient(); 
            this.proxyAsyncHttpClient = createAsyncClient(true);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    private static HttpAsyncClient instance;
    
    public static HttpAsyncClient getInstance(){ 
    	synchronized (HttpAsyncClient.class) {
    		if(instance == null){
        		instance = new HttpAsyncClient();
        	}
        	return instance;
    	}
    }
    
    public CloseableHttpAsyncClient createAsyncClient() throws IOReactorException{
    	
    	ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setMaxTotal(50);
		cm.setDefaultMaxPerRoute(50);
		
		ConnectionKeepAliveStrategy kaStrategy = new DefaultConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (keepAlive == -1) {
					keepAlive = 60000;
				}
				return keepAlive;
			}

		};
		
		RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(connectTimeout)  
                .setSocketTimeout(socketTimeout).build(); 
		
		CloseableHttpAsyncClient client = HttpAsyncClients.custom()
														  .setConnectionManager(cm)
														  .setKeepAliveStrategy(kaStrategy)
														  .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
														  .setDefaultRequestConfig(requestConfig)
														  .build();
		
		return client;
    }
  
    public CloseableHttpAsyncClient createAsyncClient(boolean proxy)  
            throws KeyManagementException, UnrecoverableKeyException,  
            NoSuchAlgorithmException, KeyStoreException,  
            MalformedChallengeException, IOReactorException {  
  
        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(connectTimeout)  
                .setSocketTimeout(socketTimeout).build();  
  
        SSLContext sslcontext = SSLContexts.createDefault();  
  
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(  
                username, password);  
  
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();  
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);  
  
        // 设置协议http和https对应的处理socket链接工厂的对象  
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder  
                .<SchemeIOSessionStrategy> create()  
                .register("http", NoopIOSessionStrategy.INSTANCE)  
                .register("https", new SSLIOSessionStrategy(sslcontext))  
                .build();  
  
        // 配置io线程  
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()  
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())  
                .build();  
        // 设置连接池大小  
        ConnectingIOReactor ioReactor;  
        ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);  
        PoolingNHttpClientConnectionManager conMgr = new PoolingNHttpClientConnectionManager(  
                ioReactor, null, sessionStrategyRegistry, null);  
  
        if (poolSize > 0) {  
            conMgr.setMaxTotal(poolSize);  
        }  
  
        if (maxPerRoute > 0) {  
            conMgr.setDefaultMaxPerRoute(maxPerRoute);  
        } else {  
            conMgr.setDefaultMaxPerRoute(10);  
        }  
  
        ConnectionConfig connectionConfig = ConnectionConfig.custom()  
                .setMalformedInputAction(CodingErrorAction.IGNORE)  
                .setUnmappableInputAction(CodingErrorAction.IGNORE)  
                .setCharset(Consts.UTF_8).build();  
  
        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder  
                .<AuthSchemeProvider> create()  
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())  
                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())  
                .register(AuthSchemes.NTLM, new NTLMSchemeFactory())  
                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())  
                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())  
                .build();  
        conMgr.setDefaultConnectionConfig(connectionConfig);  
  
        if (proxy) {  
            return HttpAsyncClients.custom().setConnectionManager(conMgr)  
                    .setDefaultCredentialsProvider(credentialsProvider)  
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)  
                    .setProxy(new HttpHost(host, port))  
                    .setDefaultCookieStore(new BasicCookieStore())  
                    .setDefaultRequestConfig(requestConfig).build();  
        } else {  
            return HttpAsyncClients.custom().setConnectionManager(conMgr)  
                    .setDefaultCredentialsProvider(credentialsProvider)  
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)  
                    .setDefaultCookieStore(new BasicCookieStore()).build();  
        }  
  
    }  
  
    public CloseableHttpAsyncClient getAsyncHttpClient() {  
        return asyncHttpClient;  
    }  
  
    public CloseableHttpAsyncClient getProxyAsyncHttpClient() {  
        return proxyAsyncHttpClient;  
    }
    
    public void xmlPost(
    		String baseUrl, 
    		List<BasicNameValuePair> urlParams, 
    		String xml,
    		FutureCallback callback) throws Exception{
    	
    	if (baseUrl == null) {  
            throw new Exception("url 不能为空！");  
        }  
    	
    	HttpRequestBase httpMethod;  
        CloseableHttpAsyncClient hc = null;
        
        hc = this.getAsyncHttpClient();  
        hc.start();

        HttpClientContext localContext = HttpClientContext.create();  
        BasicCookieStore cookieStore = new BasicCookieStore();  

        httpMethod = new HttpPost(baseUrl);  
        httpMethod.setHeader("Content-Type", "text/xml");
        httpMethod.setHeader("Accept-Charset", "UTF-8");
        
        if (null != xml) {  
        	StringEntity entity = new StringEntity(xml);
            ((HttpPost)httpMethod).setEntity(entity);  
        }  

        if(null != urlParams) {  
            String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));  
            httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));  
        }   

        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);  
        hc.execute(httpMethod, localContext, callback);  
    }
    
    public void formPost(
    		String baseUrl, 
    		List<BasicNameValuePair> urlParams, 
    		List<BasicNameValuePair> postBody, 
    		FutureCallback callback) throws Exception{
    	
    	if (baseUrl == null) {  
            throw new Exception("url 不能为空！");  
        }  
    	
    	HttpRequestBase httpMethod;  
        CloseableHttpAsyncClient hc = null;
        
        hc = this.getAsyncHttpClient();  
        hc.start();

        HttpClientContext localContext = HttpClientContext.create();  
        BasicCookieStore cookieStore = new BasicCookieStore();  

        httpMethod = new HttpPost(baseUrl);  
        
        if (null != postBody) {  
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postBody, "UTF-8");  
            ((HttpPost) httpMethod).setEntity(entity);  
        }  

        if(null != urlParams) {  
            String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));  
            httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));  
        }   

        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);  
        hc.execute(httpMethod, localContext, callback);  
    }
    
    public void formGet(
    		String baseUrl, 
    		List<BasicNameValuePair> urlParams, 
    		FutureCallback callback) throws Exception{
    	
    	if (baseUrl == null) {  
            throw new Exception("url 不能为空！");  
        }  
    	
    	HttpRequestBase httpMethod;  
        CloseableHttpAsyncClient hc = null;
        
        hc = this.getAsyncHttpClient(); 
        hc.start();

        HttpClientContext localContext = HttpClientContext.create();  
        BasicCookieStore cookieStore = new BasicCookieStore();
        httpMethod = new HttpGet(baseUrl);  
        
        if(null != urlParams){  
            String getUrl = EntityUtils .toString(new UrlEncodedFormEntity(urlParams));  
            httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));  
        }  
        
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);  
        hc.execute(httpMethod, localContext, callback);
    }
    
    /** 
     * 向指定的url发送一次异步post请求,参数是字符串 
     * @param baseUrl 请求地址 
     * @param postString 请求参数,格式是json.toString() 
     * @param urlParams 请求参数,格式是String 
     * @param callback 回调方法,格式是FutureCallback 
     * @return 返回结果,请求失败时返回null 
     * @apiNote http接口处用 @RequestParam接收参数 
     */  
    public void httpAsyncPost(String baseUrl,String postString,  
                              String urlParams,FutureCallback callback) throws Exception {  

        CloseableHttpAsyncClient hc = this.getAsyncHttpClient();  
        hc.start();
        
        try {  
            HttpPost httpPost = new HttpPost(baseUrl);  
  
            httpPost.setHeader("Connection","Keep-Alive");  
  
            if (null != postString) {  
                StringEntity entity = new StringEntity(postString, "UTF-8");  
                entity.setContentType("application/json");  
                httpPost.setEntity(entity);  
            }  
  
            if (null != urlParams) {  
  
                httpPost.setURI(new URI(httpPost.getURI().toString()  
                        + "?" + urlParams));  
            }  
            LOGGER.info("http async request, url: {}, postString: {}, urlParams :{} ",baseUrl, postString, urlParams);
            hc.execute(httpPost, callback);
            LOGGER.info("http async response, url: {},",baseUrl);

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
    
    public void httpAsyncPost(HttpPost httpPost,String postString,  
            String urlParams,FutureCallback callback) throws Exception {  
		
		CloseableHttpAsyncClient hc = this.getAsyncHttpClient();  
		hc.start();
		
		try {  
			
			httpPost.setHeader("Connection","Keep-Alive");  
		
			if (null != postString) {  
				StringEntity entity = new StringEntity(postString, "UTF-8");  
				entity.setContentType("application/json");  
				httpPost.setEntity(entity);  
			}  
		
			if (null != urlParams) {  
		
				httpPost.setURI(new URI(httpPost.getURI().toString()  
						+ "?" + urlParams));  
			}  
		
			hc.execute(httpPost, callback);  
		
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
    }  
    
}