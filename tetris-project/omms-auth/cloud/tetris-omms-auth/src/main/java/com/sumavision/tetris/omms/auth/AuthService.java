package com.sumavision.tetris.omms.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.omms.auth.exception.HttpGadgetEncryptionException;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthService {

	@Autowired
	private AuthDAO authDAO;
	
	@Autowired
	private Configure configure;
	
    /**
     * 添加授权记录
     * 添加授权<br/>
     * <p>详细描述</p>
     * <b>作者:</b>Mr.h<br/>
     * <b>版本：</b>1.0<br/>
     * <b>日期：</b>2020年11月24日 下午4:11:44
     * @param name
     * @param content
     * @param deviceId
     * @return
     * @throws Exception
     */
	public AuthPO add(
			String name,
			String content,
			String deviceId) throws Exception{
		AuthPO entity=new AuthPO();
		entity.setName(name);
		entity.setContent(content);
		entity.setDeviceId(deviceId);
		authDAO.save(entity);
		return entity;
	}
	
	/**
	 * 导入设备唯一标识
	 * 导入设备<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 上午9:14:00
	 * @param id
	 * @param deviceFile
	 * @return
	 * @throws Exception
	 */
	public AuthPO importDevice(
			Long id,
			FileItem deviceFile) throws Exception{
		AuthPO entity = authDAO.findOne(id);
		BufferedReader reader = new BufferedReader(new InputStreamReader(deviceFile.getInputStream()));
		entity.setDeviceId(reader.readLine());
		authDAO.save(entity);
		return entity;
	}
	
	
	/**
	 * 修改授权记录
	 * 修改授权<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午4:12:10
	 * @param id
	 * @param name
	 * @param content
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public AuthPO edit(
			Long id,
			String name,
			String content,
			String deviceId) throws Exception{
		AuthPO entity = authDAO.findOne(id);
		if(entity != null){
			entity.setName(name);
			entity.setContent(content);
			entity.setDeviceId(deviceId);
			authDAO.save(entity);
		}
		return entity;
	}
	public AuthPO set(
			Long id,
			String content) throws Exception{
		AuthPO entity = authDAO.findOne(id);
		if(entity != null){
			entity.setContent(content);
			authDAO.save(entity);
		}
		return entity;
	}
	
	/**
	 * 删除授权记录
	 * 删除授权<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午4:12:29
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AuthPO delete(Long id) throws Exception{
		AuthPO entity = authDAO.findOne(id);
		if(entity != null){
			authDAO.delete(entity);
		}
		return entity;
	}
	/**
	 * 分页查询授权记录
	 * 分页查询<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Mr.h<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月24日 下午4:20:22
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<AuthVO> load(
			int currentPage, 
			int pageSize) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<AuthPO> pagedEntities = authDAO.findAll(page);
		List<AuthPO> entities = pagedEntities.getContent();
		if(entities!=null && entities.size()>0){
			return AuthVO.getConverter(AuthVO.class).convert(entities, AuthVO.class);
		}
		return null;
	}
	
	/**
	 * 授权信息加密<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月16日 下午7:12:10
	 * @param entity 授权内容
	 * @return encryption 加密文件内容及长度
	 */
	public Map<String, String> encryption(AuthPO entity) throws Exception{
		Map<String, String> encryption = new HashMap<String, String>();
		CloseableHttpClient client = null;

		try {
			
			String profile = configure.readGadgetConfig();
			JSONObject gadgetfile = JSONObject.parseObject(profile);
			
			String serverip = gadgetfile.getString("ip") ;
			Integer port = gadgetfile.getInteger("port") ;
			String user =  gadgetfile.getString("username");
			String password =  gadgetfile.getString("password");
//			String serverip = "127.0.0.1" ;
//			Integer port = 8910 ;
//			String user = "Admin";
//			String password = "sumavisionrd";
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(serverip, port, "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(user, password));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(serverip).append(":").append(port).append("/action/encrypt_authcode").toString();
	        
	        System.out.println(url);
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        formparams.add(new BasicNameValuePair("type", "run"));
	        formparams.add(new BasicNameValuePair("sn", entity.getDeviceId()));
	        formparams.add(new BasicNameValuePair("data", entity.getContent()));
	        
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			String authcode = jsonObject.getString("authcode");
			String authcode_len = jsonObject.getString("authcode_len");
			if(!"0".equals(result)){
				throw new HttpGadgetEncryptionException(serverip, port.toString(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetEncryptionException(serverip, port.toString(), String.valueOf(code));
			}
			encryption.put("authcode", authcode);
			encryption.put("authcode_len", authcode_len);
			return encryption;
			
		} finally {
			if(client != null) client.close();
		}
	}
	
}
