package com.sumavision.tetris.omms.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.omms.hardware.database.DatabaseDAO;
import com.sumavision.tetris.omms.hardware.database.DatabasePO;
import com.sumavision.tetris.omms.hardware.database.DatabaseVO;
import com.sumavision.tetris.omms.hardware.server.ServerPO;
import com.sumavision.tetris.omms.hardware.server.ServerVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataPO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentDAO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentPO;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetModifyIniException;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthService {

	@Autowired
	private AuthDAO authDAO;
	
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
	
	
}
