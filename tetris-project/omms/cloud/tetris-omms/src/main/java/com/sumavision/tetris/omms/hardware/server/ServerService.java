package com.sumavision.tetris.omms.hardware.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
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
import com.sumavision.tetris.omms.hardware.database.DatabaseService;
import com.sumavision.tetris.omms.hardware.database.DatabaseVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerAlarmDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerAlarmPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerAlarmVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataVO;
import com.sumavision.tetris.omms.hardware.server.data.process.ServerProcessUsageDAO;
import com.sumavision.tetris.omms.hardware.server.data.process.ServerProcessUsagePO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentDAO;
import com.sumavision.tetris.omms.software.service.deployment.ServiceDeploymentPO;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpChangeFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.FtpCreateFolderFailException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetEncyptAuthcodeException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetEquipmentIdentificationCodeException;
import com.sumavision.tetris.omms.software.service.deployment.exception.HttpGadgetModifyIniException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ServerService {
	
	public static final String RELATIVE_FOLDER = "auth";

	@Autowired
	private ServerDAO serverDao;
	
	@Autowired
	private ServerOneDimensionalDataDAO serverOneDimensionalDataDao;
	
	@Autowired
	private ServerHardDiskDataDAO serverHardDiskDataDao;
	
	@Autowired
	private ServerNetworkCardTrafficDataDAO serverNetworkCardTrafficDataDao;
	
	@Autowired
	private ServiceDeploymentDAO serviceDeploymentDao;
	
	@Autowired
	private DatabaseDAO databaseDAO;
	
	@Autowired
	private DatabaseService databaseService;
	
	@Autowired
	private ServerAlarmDAO serverAlarmDAO;
	
	@Autowired
	private ServerProcessUsageDAO serverProcessUsageDAO;
	
	@Autowired
	private ServerOneDimensionalDataDAO serverOneDimensionalDataDAO;
	
	/**
	 * 添加一个服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:54:40
	 * @param String name 名称
	 * @param String ip ip地址
	 * @param String gadgetPort 小工具端口
	 * @param String gadgetUsername 小工具用户名
	 * @param String gadgetPassword 小工具密码
	 * @param String remark 备注
	 * @param String creator 创建者
	 * @param String createTime 创建时间
	 * @param String ftpUsername ftp服务名
	 * @param String ftpPort ftp端口
	 * @param String ftpPassword ftp密码
	 * @return ServerPO 服务器
	 */
	public ServerPO add(
			String name,
			String ip,
			String gadgetPort,
			String gadgetUsername,
			String gadgetPassword,
			String remark,
			String creator,
			Date createTime,
			String ftpUsername,
			String ftpPort,
			String ftpPassword) throws Exception{
		ServerPO entity = new ServerPO();
		entity.setName(name);
		entity.setIp(ip);
		entity.setGadgetPort(gadgetPort);
		entity.setGadgetUsername(gadgetUsername);
		entity.setGadgetPassword(gadgetPassword);
		entity.setRemark(remark);
		entity.setCreator(creator);
		entity.setCreateTime(createTime);
		entity.setUpdateTime(new Date());
		entity.setStatus(ServerStatus.OFFLINE);
		entity.setFtpUsername(ftpUsername);
		entity.setFtpPort(ftpPort);
		entity.setFtpPassword(ftpPassword);
		serverDao.save(entity);
		return entity;
	}
	
	/**
	 * 修改服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:57:32
	 * @param Long id 服务器id
	 * @param String name 名称
	 * @param String gadgetPort 小工具端口
	 * @param String gadgetUsername 小工具用户名
	 * @param String gadgetPassword 小工具密码
	 * @param String remark 备注
	 * @param String creator 创建者
	 * @return ServerPO 服务器
	 */
	public ServerPO edit(
			Long id,
			String name,
			String gadgetPort,
			String gadgetUsername,
			String gadgetPassword,
			String remark,
			String creator,
			String ftpUsername,
			String ftpPort,
			String ftpPassword) throws Exception{
		ServerPO entity = serverDao.findOne(id);
		if(entity != null){
			entity.setName(name);
			entity.setGadgetPort(gadgetPort);
			entity.setGadgetUsername(gadgetUsername);
			entity.setGadgetPassword(gadgetPassword);
			entity.setRemark(remark);
			entity.setCreator(creator);
			entity.setUpdateTime(new Date());
			entity.setFtpUsername(ftpUsername);
			entity.setFtpPort(ftpPort);
			entity.setFtpPassword(ftpPassword);
			serverDao.save(entity);
		}
		return entity;
	}
	
	/**
	 * 删除一个服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:59:35
	 * @param Long id 服务器id
	 * @return ServerPO 删除的服务器
	 */
	public ServerPO delete(Long id) throws Exception{
		ServerPO entity = serverDao.findOne(id);
		if(entity != null){
			serverDao.delete(entity);
		}
		return entity;
	}
	
	/**
	 * 查询服务器状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午8:53:35
	 */
	@Transactional(rollbackFor = Exception.class)
	public void queryStatus() throws Exception{
		List<ServerPO> serverEntities = serverDao.findAll();
		if(serverEntities==null || serverEntities.size()<=0) return;
		List<ServerPO> needSaveServers = new ArrayList<ServerPO>();
		List<ServerPO> needLoopServers = new ArrayList<ServerPO>();
		for(ServerPO serverEntity:serverEntities){
			if(serverEntity.getIp()==null 
				|| serverEntity.getGadgetPort()==null 
				|| serverEntity.getGadgetUsername()==null 
				|| serverEntity.getGadgetPassword()==null){
				serverEntity.setStatus(ServerStatus.OFFLINE);
				needSaveServers.add(serverEntity);
			}else{
				needLoopServers.add(serverEntity);
			}
		}
		if(needSaveServers.size() > 0) serverDao.save(needSaveServers);
		if(needLoopServers.size() > 0){
			for(ServerPO server:needLoopServers){
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
		        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
				CloseableHttpAsyncClient client = HttpAsyncClients.custom()
																  .setDefaultCredentialsProvider(credsProvider)
																  .build();
				String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/get_capability_info").toString();
				HttpPost httpPost = new HttpPost(url);
				client.start();
				client.execute(httpPost, new QueryStatusListener(server.getId(), client));
			}
		}
	}
	
	/**
	 * 服务器状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午10:02:34
	 * @param Long serverId 服务器id
	 * @param JSONObject serverInfo 服务器信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void setStatus(Long serverId, JSONObject serverInfo)throws Exception{
		Date now = new Date();
		ServerPO serverEntity = serverDao.findOne(serverId);
		serverEntity.setStatus(ServerStatus.ONLINE);
		serverDao.save(serverEntity);
		
		ServerOneDimensionalDataPO basicInfo = new ServerOneDimensionalDataPO();
		basicInfo.setMemoryTotal(Long.valueOf(serverInfo.getJSONObject("memory").getString("total").replace("M", "")) * 1024);
		basicInfo.setMemoryBuff(0l);
		basicInfo.setMemoryUsed(Long.valueOf(serverInfo.getJSONObject("memory").getString("used").replace("M", "")) * 1024);
		basicInfo.setMemoryFree(basicInfo.getMemoryTotal() - basicInfo.getMemoryUsed());
		
		basicInfo.setTaskTotal(0l);
		basicInfo.setTaskRunning(0l);
		basicInfo.setTaskSleeping(0l);
		basicInfo.setTaskZombie(0l);
		
		basicInfo.setCpuName(serverInfo.getJSONObject("cpu").getString("name"));
		basicInfo.setCpuOccupy(serverInfo.getJSONObject("cpu").getFloat("usage"));
		
		basicInfo.setSystemTime("");
		basicInfo.setLastRebootTime("");
		basicInfo.setUpTime("");
		basicInfo.setTemperature(serverInfo.getJSONObject("cpu").getString("temperature"));
		basicInfo.setFanspeed(serverInfo.getJSONObject("power").getLong("fanspeed"));
		
		//小工具升级之后支持的参数
		if(serverInfo.containsKey("pid")){
			basicInfo.setMemoryBuff(Long.valueOf(serverInfo.getJSONObject("memory").getString("cached").replace("M", "")) * 1024);
			
			basicInfo.setTaskTotal(serverInfo.getJSONObject("pid").getLong("total"));
			basicInfo.setTaskRunning(serverInfo.getJSONObject("pid").getLong("run"));
			basicInfo.setTaskSleeping(serverInfo.getJSONObject("pid").getLong("sleep"));
			basicInfo.setTaskZombie(serverInfo.getJSONObject("pid").getLong("zombie"));
			
			basicInfo.setLastRebootTime(serverInfo.getJSONObject("cpu").getString("boot_time"));
			basicInfo.setUpTime(serverInfo.getJSONObject("cpu").getString("up_time"));
		}
		
		basicInfo.setServerId(serverId);
		basicInfo.setUpdateTime(now);
		serverOneDimensionalDataDao.save(basicInfo);
		processUsageTest(basicInfo, serverInfo);
		singleCpuTest(basicInfo, serverInfo);
		
		JSONArray storages = serverInfo.getJSONArray("storage");
		if(storages!=null && storages.size()>0){
			List<ServerHardDiskDataPO> disks = new ArrayList<ServerHardDiskDataPO>();
			for(int i=0; i<storages.size(); i++){
				JSONObject storage = storages.getJSONObject(i);
				ServerHardDiskDataPO disk = new ServerHardDiskDataPO();
				disk.setUpdateTime(now);
				disk.setServerId(serverId);
				disk.setName(storage.getString("mounted"));
				disk.setTotal(Long.valueOf(storage.getString("size").replace("MB", "")) * 1024);
				disk.setUsed(Long.valueOf(storage.getString("used").replace("MB", "")) * 1024);
				disk.setFree(Long.valueOf(storage.getString("avail").replace("MB", "")) * 1024);
				disks.add(disk);
			}
			serverHardDiskDataDao.save(disks);
		}
		
		JSONArray networks = serverInfo.getJSONArray("network");
		if(networks!=null && networks.size()>0){
			List<ServerNetworkCardTrafficDataPO> networkEntities = new ArrayList<ServerNetworkCardTrafficDataPO>();
			for(int i=0; i<networks.size(); i++){
				JSONObject network = networks.getJSONObject(i);
				ServerNetworkCardTrafficDataPO networkEntity = new ServerNetworkCardTrafficDataPO();
				networkEntity.setUpdateTime(now);
				networkEntity.setServerId(serverId);
				networkEntity.setName(network.getString("name"));
				networkEntity.setTxkB(network.getString("up"));
				networkEntity.setRxkB(network.getString("down"));
				networkEntities.add(networkEntity);
			}
			serverNetworkCardTrafficDataDao.save(networkEntities);
		}
		
	}
	
	/**
	 * 服务器离线<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午10:02:15
	 * @param Long serverId 服务器id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void offlineStatus(Long serverId){
		ServerPO serverEntity = serverDao.findOne(serverId);
		serverEntity.setStatus(ServerStatus.OFFLINE);
		serverDao.save(serverEntity);
	}
	
	/**
	 * 服务器状态回调<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午10:03:12
	 */
	private class QueryStatusListener implements FutureCallback<HttpResponse>{

		private Long serverId;
		
		private CloseableHttpAsyncClient client;
		
		QueryStatusListener(
				Long serverId,
				CloseableHttpAsyncClient client){
			this.serverId = serverId;
			this.client = client;
		}
		
		@Override
		public void completed(HttpResponse response) {
			ServerService serverService = SpringContext.getBean(ServerService.class);
			try{
				if(response.getStatusLine().getStatusCode() == 200){
		        	HttpEntity entity = response.getEntity();
		        	String status = FileUtil.readAsString(entity.getContent());
		 	        EntityUtils.consume(entity);
		 	        JSONObject serverInfo = JSON.parseObject(status);
		 	        serverService.setStatus(this.serverId, serverInfo);
		        }else{
		        	serverService.offlineStatus(this.serverId);
		        }
			}catch(Exception e){
				e.printStackTrace();
				serverService.offlineStatus(this.serverId);
			}finally{
				try {
					if(client!=null) client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void failed(Exception ex) {
			ex.printStackTrace();
			try{
				ServerService serverService = SpringContext.getBean(ServerService.class);
				serverService.offlineStatus(this.serverId);
			}finally{
				try {
					if(client!=null) client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void cancelled() {
			try{
				ServerService serverService = SpringContext.getBean(ServerService.class);
				serverService.offlineStatus(this.serverId);
			}finally{
				try {
					if(client!=null) client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 修改ip<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月4日 上午10:39:43
	 * @param Long id 服务器id
	 * @param String ip 修改的ip
	 * @return ServerVO 服务器
	 */
	@Transactional(rollbackFor = Exception.class)
	public ServerVO modifyIp(
			Long id,
			String ip) throws Exception{
		CloseableHttpClient client = null;
		try{
			ServerPO serverEntity = serverDao.findOne(id);
			String oldIp = serverEntity.getIp();
			serverEntity.setIp(ip);
			serverDao.save(serverEntity);
			ServerVO server = new ServerVO().set(serverEntity);
			
			List<ServiceDeploymentPO> deployments = serviceDeploymentDao.findByServerId(server.getId());
			if(deployments==null || deployments.size()<=0) return server;
			List<ServiceDeploymentPO> needModifyIpDeployments = new ArrayList<ServiceDeploymentPO>();
			for(ServiceDeploymentPO deployment:deployments){
				if(deployment.getConfig()!=null && deployment.getConfig().indexOf(oldIp)>=0){
					needModifyIpDeployments.add(deployment);
				}
			}
			if(needModifyIpDeployments.size() <= 0) return server;
			
			JSONArray params = new JSONArray();
			for(ServiceDeploymentPO deployment:needModifyIpDeployments){
				//替换部署配置中的ip
				deployment.setConfig(deployment.getConfig().replaceAll(oldIp, ip));
				JSONObject param = new JSONObject();
				param.put("relative_path", deployment.getInstallFullPath());
				//将部署中存储的config json形式转换为config.ini形式
				StringBufferWrapper configBuffer = new StringBufferWrapper();
				JSONObject configJson = JSON.parseObject(deployment.getConfig());
				Set<String> keys = configJson.keySet();
				for(String key:keys){
					String value = configJson.getString(key);
					configBuffer.append(key).append("=").append(value).append("\n");
				}
				param.put("config", configBuffer.toString());
				params.add(param);
			}
			serviceDeploymentDao.save(needModifyIpDeployments);
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
			String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/modifyini").toString();
			System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("params", params.toJSONString()));  
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
			if(!"0".equals(result)){
				throw new HttpGadgetModifyIniException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetModifyIniException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
			return server;
		}finally{
			if(client != null) client.close();
		}
	}
	
	/**
	 * 
	 * 删除数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午6:02:04
	 * @param databaseId 数据库id
	 * @throws Exception
	 */
	public void deleteDatabase(Long databaseId) throws Exception{
		databaseDAO.delete(databaseId);
	}
	
	
	/**
	 * 上传并解密授权信息<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月17日 下午1:11:48
	 * @param id 服务id
	 * @param authFile 授权文件
	 * @return
	 */
	public ServerVO importAuth(long id,FileItem authFile) throws Exception{

		CloseableHttpClient client = null;
		String name = authFile.getName();
		BufferedReader reader = new BufferedReader(new InputStreamReader(authFile.getInputStream()));
		//reader.readLine();
		//对接小工具下发授权并修改设备授权状态
		ServerPO server = serverDao.findOne(id);
		
		
		CloseableHttpClient faclient = null;
		try{
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        faclient = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
			String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/execute_cmd").toString();
			System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("cmd", "service xstreamdogd stop"));  
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httpPost.setConfig(requestConfig);
			
			CloseableHttpResponse response = faclient.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetEncyptAuthcodeException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetEncyptAuthcodeException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
		}finally{
			if(faclient != null) faclient.close();
		}
		
		databaseService.enableFtp(id);
		
		Boolean upload = ftpupload(
				server.getIp(), 
				Integer.parseInt(server.getFtpPort()), 
				server.getFtpUsername(), 
				server.getFtpPassword(),
				new StringBufferWrapper().append("/").append(RELATIVE_FOLDER).append("/").toString(), 
				name, 
				reader);
		if (upload) {
			try{
				
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
		        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
		        client = HttpClients.custom()
				        		    .setDefaultCredentialsProvider(credsProvider)
				        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
				        		    .build();
				String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/decrypt_authcode").toString();
				System.out.println(url);
				HttpPost httpPost = new HttpPost(url);
				
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
				formparams.add(new BasicNameValuePair("type", "run"));  
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
				
				if(!"0".equals(result)){
					throw new HttpGadgetEncyptAuthcodeException(server.getIp(), server.getGadgetPort(), errormsg);
				}
				
				int code = response.getStatusLine().getStatusCode();
				if(code != 200){
					throw new HttpGadgetEncyptAuthcodeException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
				}
			}finally{
				if(client != null) client.close();
			}
			CloseableHttpClient reclient = null;
			try{
				
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
		        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
		        reclient = HttpClients.custom()
				        		    .setDefaultCredentialsProvider(credsProvider)
				        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
				        		    .build();
				String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/execute_cmd").toString();
				System.out.println(url);
				HttpPost httpPost = new HttpPost(url);
				
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
				formparams.add(new BasicNameValuePair("cmd", "service xstreamdogd start"));  
				httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
				
				RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
	            httpPost.setConfig(requestConfig);
				
				CloseableHttpResponse response = reclient.execute(httpPost);
				
				// 解析小工具HTTP返回结果并提示异常信息
				HttpEntity httpEntity = response.getEntity();
				InputStream content = httpEntity.getContent();
				byte[] byteArr = new byte[content.available()];
				content.read(byteArr);
				String str = new String(byteArr);
				JSONObject jsonObject = JSON.parseObject(str);
				String result = jsonObject.getString("result");
				String errormsg = jsonObject.getString("errormsg");
				if(!"0".equals(result)){
					throw new HttpGadgetEncyptAuthcodeException(server.getIp(), server.getGadgetPort(), errormsg);
				}
				
				int code = response.getStatusLine().getStatusCode();
				if(code != 200){
					throw new HttpGadgetEncyptAuthcodeException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
				}
			}finally{
				if(reclient != null) reclient.close();
			}
		}
		return new ServerVO().set(server);
	}
	
	/**
	 * 
	 * 添加数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午7:13:37
	 * @param serverId 服务器id
	 * @param databaseIP 数据库IP
	 * @param databasePort 数据库端口
	 * @param username 用户名
	 * @param password 密码
	 * @throws Exception 
	 */
	public DatabaseVO addDatabase(Long serverId, String databaseIp, String databasePort, String databaseName, String username, String password) throws Exception{
		ServerPO server = serverDao.findOne(serverId);
		DatabasePO database = new DatabasePO();
		database.setServerId(serverId);
		database.setDatabaseIP(server.getIp());
		database.setDatabasePort(databasePort);
		database.setDatabaseName(databaseName);
		database.setUsername(username);
		database.setPassword(password);
		databaseDAO.save(database);
		return new DatabaseVO().set(database);
	}

	/**
	 * 获取设备唯一标识<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月16日 上午10:34:58
	 * @param id 服务器id
	 * @return String 设备标识
	 */
	public String exportDeviceid(Long id) throws Exception{
		CloseableHttpClient client = null;
		try{
			ServerPO server = serverDao.findOne(id);
			
			JSONArray params = new JSONArray();
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(server.getIp(), Integer.parseInt(server.getGadgetPort()), "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(server.getGadgetUsername(), server.getGadgetPassword()));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
			String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/get_device_id").toString();
			System.out.println(url);
			HttpPost httpPost = new HttpPost(url);
			
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
			formparams.add(new BasicNameValuePair("params", params.toJSONString()));  
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
			String sn = jsonObject.getString("sn");
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetEquipmentIdentificationCodeException(server.getIp(), server.getGadgetPort(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetEquipmentIdentificationCodeException(server.getIp(), server.getGadgetPort(), String.valueOf(code));
			}
			return sn;
		}finally{
			if(client != null) client.close();
		}
	}
	
	@Transactional
	public boolean uploadFile(String url,int port,String username, String password, String path, String filename, String inputPath) throws Exception{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		FileInputStream fin = null;
		BufferedInputStream in = null;
		OutputStream out = null;
		
		try {
			int reply;
			ftp.connect(url, port);//连接FTP服务器
			ftp.login(username, password);//登录
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			ftp.setControlEncoding("utf-8");
			ftp.setBufferSize(1024*1024*10);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			File file = new File(inputPath);
			long total = file.length();
			int lastBuffSize = 0;
			long buffLoopTimes = 0;
			int buffSize = ftp.getBufferSize();
			if(buffSize >= total){
				lastBuffSize = (int)total;
			}else{
				buffLoopTimes = (long)(total/buffSize);
				lastBuffSize = (int)(total - buffSize * buffLoopTimes);
			}
			byte[] buff = new byte[buffSize];
			byte[] lastBuff = new byte[lastBuffSize];
			
			fin = new FileInputStream(file);
			in = new BufferedInputStream(fin);
			String str = path + filename;
			out = ftp.storeFileStream(new String(str.getBytes("utf-8"), "iso-8859-1"));
			for(int i = 0; i < buffLoopTimes; i++){
				in.read(buff);
				out.write(buff);
			}
			if(lastBuffSize > 0){
				in.read(lastBuff);
				out.write(lastBuff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {out.close();} catch (IOException e) {e.printStackTrace();}
			try {in.close();} catch (IOException e) {e.printStackTrace();}
			try {fin.close();} catch (IOException e) {e.printStackTrace();}
			if (ftp.isConnected()) {
				try {ftp.logout();ftp.disconnect();} catch (IOException ioe) {}
			}
		}
		return success;
	}
	
	/**
	 * 上传授权信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 上午8:06:47
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param filename
	 * @param reader
	 * @return
	 * @throws Exception
	 */
	public Boolean ftpupload(String url,int port,String username, String password, String path, String filename, BufferedReader reader)throws Exception{
		
		boolean success = false;
		FTPClient ftp = new FTPClient();
		BufferedReader br = reader;
		BufferedReader in = null;
		OutputStream out = null;
		BufferedWriter bw = null;
		
		try {
			int reply;
			ftp.connect(url, port);//连接FTP服务器
			ftp.login(username, password);//登录
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			ftp.setControlEncoding("utf-8");
			ftp.setBufferSize(1024*1024*10);
			boolean changeResult = ftp.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
			if(!changeResult){
				boolean mdResult = ftp.makeDirectory(encodeFtpText(RELATIVE_FOLDER));
				if(!mdResult){
					throw new FtpCreateFolderFailException(url, String.valueOf(port), RELATIVE_FOLDER);
				}
				changeResult = ftp.changeWorkingDirectory(encodeFtpText(RELATIVE_FOLDER));
				if(!changeResult){
					throw new FtpChangeFolderFailException(url, String.valueOf(port), RELATIVE_FOLDER);
				}
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			filename = "auth_run.txt";
			String str = path + filename;
			out = ftp.storeFileStream(new String(str.getBytes("utf-8"), "iso-8859-1"));
			
			int ch = 0;
			while ((ch = br.read()) != -1) {
				out.write(ch);
			 }
			
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}


		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {out.close();} catch (IOException e) {e.printStackTrace();}
			try {br.close();} catch (IOException e) {e.printStackTrace();}
			if (ftp.isConnected()) {
				try {ftp.logout();ftp.disconnect();} catch (IOException ioe) {}
			}
		}
		return success;
	}
	
	private String encodeFtpText(String text) throws Exception{
		return new String(text.getBytes("utf-8"), "iso-8859-1");
	}

	/**
	 * 修改CPU、内存、硬盘告警限制<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月26日 下午2:02:44
	 * @param cpuRate CPU告警限度
	 * @param memoryRate 内存告警限度
	 * @param diskRate 硬盘告警限度
	 * @param processCpu 
	 * @return ServerAlarmVO
	 */
	public ServerAlarmVO editLimitRate(Long cpuRate, Long memoryRate, Long diskRate, Long processCpu) throws Exception {
		List<ServerAlarmPO> serverAlarmPOs = serverAlarmDAO.findAll();
		ServerAlarmVO serverAlarmVO = new ServerAlarmVO();
		if (serverAlarmPOs != null && serverAlarmPOs.size() > 0) {
			ServerAlarmPO serverAlarmPO = serverAlarmPOs.get(0);
			serverAlarmPO.setCpuRate(cpuRate);
			serverAlarmPO.setDiskRate(diskRate);
			serverAlarmPO.setMemoryRate(memoryRate);
			serverAlarmPO.setProcessCpu(processCpu);
			serverAlarmDAO.save(serverAlarmPO);
			serverAlarmVO.set(serverAlarmPO);
		}
		return serverAlarmVO;
	}
	
	/**
	 * 服务器CPU超过限制保存进程占用率<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月27日 下午4:43:57
	 * @param basicInfo
	 * @param serverInfo
	 */
	public void processUsageTest(ServerOneDimensionalDataPO basicInfo, JSONObject serverInfo) throws Exception{
		List<ServerAlarmPO> serverAlarmPOs = serverAlarmDAO.findAll();
		Date date = new Date();
		if (serverAlarmPOs != null && serverAlarmPOs.size()>0) {
			ServerAlarmPO serverAlarmPO = serverAlarmPOs.get(0);
			if (basicInfo.getCpuOccupy() > serverAlarmPO.getCpuRate()) {
				StringBufferWrapper message = new StringBufferWrapper().append("CPU使用率达到了 ")
																		.append(basicInfo.getCpuOccupy()).append("%");
				basicInfo.setAlarmMessage(message.toString());
				serverOneDimensionalDataDao.save(basicInfo);
				List<ServerProcessUsagePO> serverProcessUsagePOs = new ArrayList<ServerProcessUsagePO>();
				JSONArray process = serverInfo.getJSONArray("Process");
				if (process != null && process.size() > 0) {
					Integer member = 0;
					if (process.size()>10) {
						member = 10;
					}else {
						member = process.size();
					}
					for (int i = 0; i < member; i++) {
						JSONObject jsonObject = process.getJSONObject(i);
						ServerProcessUsagePO serverProcessUsagePO = new ServerProcessUsagePO();
						serverProcessUsagePO.setDataId(basicInfo.getId());
						serverProcessUsagePO.setName(jsonObject.getString("name"));
						serverProcessUsagePO.setUpdateTime(date);
						serverProcessUsagePO.setCpuUsage(jsonObject.getString("cpu_use"));
						serverProcessUsagePO.setMemoryUsage(jsonObject.getString("mem_use"));
						serverProcessUsagePOs.add(serverProcessUsagePO);
					}
					serverProcessUsageDAO.save(serverProcessUsagePOs);
				}
				ServerPO serverPO = serverDao.findOne(basicInfo.getServerId());
				serverPO.setIsAlarm(true);
				serverDao.save(serverPO);
			}
		}
	}

	/**
	 * 单个进程CPU超过限制<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月27日 下午6:15:06
	 * @param basicInfo
	 * @param serverInfo
	 * @throws Exception
	 */
	public void singleCpuTest(ServerOneDimensionalDataPO basicInfo, JSONObject serverInfo) throws Exception{
		List<ServerAlarmPO> serverAlarmPOs = serverAlarmDAO.findAll();
		Date date = new Date();
		if (serverAlarmPOs != null && serverAlarmPOs.size()>0) {
			ServerAlarmPO serverAlarmPO = serverAlarmPOs.get(0);
			List<ServerProcessUsagePO> serverProcessUsagePOs = new ArrayList<ServerProcessUsagePO>();
			StringBufferWrapper message = new StringBufferWrapper();
			ServerPO serverPO = serverDao.findOne(basicInfo.getServerId());
 			JSONArray process = serverInfo.getJSONArray("Process");
			if (process != null && process.size() > 0) {
				for (int i = 0; i < process.size(); i++) {
					JSONObject jsonObject = process.getJSONObject(i);
					if (Float.parseFloat(jsonObject.getString("cpu_use")) > serverAlarmPO.getProcessCpu()) {
						ServerProcessUsagePO serverProcessUsagePO = new ServerProcessUsagePO();
						serverProcessUsagePO.setDataId(basicInfo.getId());
						serverProcessUsagePO.setUpdateTime(date);
						serverProcessUsagePO.setName(jsonObject.getString("name"));
						serverProcessUsagePO.setCpuUsage(jsonObject.getString("cpu_use"));
						serverProcessUsagePO.setMemoryUsage(jsonObject.getString("mem_use"));
						serverProcessUsagePOs.add(serverProcessUsagePO);
						message = new StringBufferWrapper().append("进程 ")
								.append(serverProcessUsagePO.getName())
								.append(" CPU使用达到了 ")
								.append(serverProcessUsagePO.getCpuUsage())
								.append(System.getProperty("line.separator"));
						basicInfo.setAlarmMessage(message.toString());
						serverPO.setIsAlarm(true);
					}
				}
				
				serverOneDimensionalDataDao.save(basicInfo);
				serverDao.save(serverPO);
				serverProcessUsageDAO.save(serverProcessUsagePOs);
			}
		}
	}
	
	/**
	 * 删除告警信息<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午7:55:04
	 * @param serverId 服务器id
	 */
	public Object deleteAlarmMessage(Long serverId) {
		List<ServerOneDimensionalDataPO> serverOneDimensionalDataPOs = serverOneDimensionalDataDAO.findByServerIdAndAlarmMessageNotNULL(serverId);
		List<Long> dataIds = new ArrayList<Long>();
		if(serverOneDimensionalDataPOs != null && serverOneDimensionalDataPOs.size() > 0){
			for (ServerOneDimensionalDataPO serverOneDimensionalDataPO : serverOneDimensionalDataPOs) {
				serverOneDimensionalDataPO.setAlarmMessage(null);
				dataIds.add(serverOneDimensionalDataPO.getId());
			}
		}
		List<ServerProcessUsagePO> serverProcessUsagePOs = serverProcessUsageDAO.findByDataIdIn(dataIds);
		if (serverProcessUsagePOs != null && serverProcessUsagePOs.size() >0) {
			serverProcessUsageDAO.deleteInBatch(serverProcessUsagePOs);
		}
		return null;
	}
}
