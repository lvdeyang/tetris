package com.sumavision.tetris.omms.hardware.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.sumavision.tetris.omms.hardware.database.DatabaseVO;
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
public class ServerService {

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
	public void setStatus(Long serverId, JSONObject serverInfo){
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
	public DatabaseVO addDatabase(Long serverId, String databasePort, String username, String password) throws Exception{
		ServerPO server = serverDao.findOne(serverId);
		DatabasePO database = new DatabasePO();
		database.setServerId(serverId);
		database.setDatabaseIP(server.getIp());
		database.setDatabasePort(databasePort);
		database.setUsername(username);
		database.setPassword(password);
		databaseDAO.save(database);
		return new DatabaseVO().set(database);
	}
}
