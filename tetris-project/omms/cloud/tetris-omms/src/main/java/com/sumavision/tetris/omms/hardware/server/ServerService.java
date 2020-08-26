package com.sumavision.tetris.omms.hardware.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
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
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataPO;

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
			Date createTime) throws Exception{
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
	 * @param String ip ip地址
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
			String ip,
			String gadgetPort,
			String gadgetUsername,
			String gadgetPassword,
			String remark,
			String creator) throws Exception{
		ServerPO entity = serverDao.findOne(id);
		if(entity != null){
			entity.setName(name);
			entity.setIp(ip);
			entity.setGadgetPort(gadgetPort);
			entity.setGadgetUsername(gadgetUsername);
			entity.setGadgetPassword(gadgetPassword);
			entity.setRemark(remark);
			entity.setCreator(creator);
			entity.setUpdateTime(new Date());
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
		        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials("Admin", "sumavisionrd"));
				CloseableHttpAsyncClient client = HttpAsyncClients.custom()
																  .setDefaultCredentialsProvider(credsProvider)
																  .build();
				String url = new StringBufferWrapper().append("http://").append(server.getIp()).append(":").append(server.getGadgetPort()).append("/action/get_capability_info").toString();
				HttpPost httpPost = new HttpPost(url);
				client.start();
				client.execute(httpPost, new QueryStatusListener(server.getId()));
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
		
		QueryStatusListener(
				Long serverId){
			this.serverId = serverId;
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
			}
		}
		
		@Override
		public void failed(Exception ex) {
			ex.printStackTrace();
			ServerService serverService = SpringContext.getBean(ServerService.class);
			serverService.offlineStatus(this.serverId);
		}
		
		@Override
		public void cancelled() {
			ServerService serverService = SpringContext.getBean(ServerService.class);
			serverService.offlineStatus(this.serverId);
		}
		
	}
	
}
