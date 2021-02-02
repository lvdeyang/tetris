package com.sumavision.tetris.omms.hardware.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.omms.hardware.database.DatabaseDAO;
import com.sumavision.tetris.omms.hardware.database.DatabasePO;
import com.sumavision.tetris.omms.hardware.database.DatabaseVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerAlarmDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerAlarmPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerAlarmVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataVO;
import com.sumavision.tetris.omms.hardware.server.data.process.ServerProcessUsageDAO;
import com.sumavision.tetris.omms.hardware.server.data.process.ServerProcessUsagePO;
import com.sumavision.tetris.omms.hardware.server.data.process.ServerProcessUsageVO;

@Component
public class ServerQuery {

	@Autowired
	private ServerDAO serverDao;
	
	@Autowired
	private ServerOneDimensionalDataDAO serverOneDimensionalDataDao;
	
	@Autowired
	private ServerHardDiskDataDAO serverHardDiskDataDao;
	
	@Autowired
	private ServerNetworkCardTrafficDataDAO serverNetworkCardTrafficDataDao;
	
	@Autowired
	private DatabaseDAO databaseDAO;
	
	@Autowired
	private ServerAlarmDAO serverAlarmDAO;
	
	@Autowired
	private ServerOneDimensionalDataDAO serverOneDimensionalDataDAO;
	
	@Autowired
	private ServerProcessUsageDAO serverProcessUsageDAO;
	
	/**
	 * 分页查询服务器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月14日 下午1:48:03
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<ServerVO> 服务器列表
	 */
	public List<ServerVO> load(
			int currentPage, 
			int pageSize) throws Exception{
		
		Pageable page = PageRequest.of(currentPage-1, pageSize);
		Page<ServerPO> pagedEntities = serverDao.findAll(page);
		List<ServerPO> entities = pagedEntities.getContent();
		if(entities!=null && entities.size()>0){
			return ServerVO.getConverter(ServerVO.class).convert(entities, ServerVO.class);
		}
		return null;
	}
	
	/**
	 * 查询服务器状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月25日 上午10:58:39
	 * @param Long id 服务器id
	 * @return server ServerVO 服务器
	 * @return oneDimensionalData ServerOneDimensionalDataVO 服务器以为数据信息
	 * @return disks List<ServerHardDiskDataVO> 硬盘信息列表
	 * @return networks List<ServerNetworkCardTrafficDataVO> 网卡信息列表
	 */
	public Map<String, Object> queryStatus(Long id) throws Exception{
		Map<String, Object> serverStatus = new HashMap<String, Object>();
		ServerPO serverEntity = serverDao.findById(id);
		serverStatus.put("server", new ServerVO().set(serverEntity));
		List<ServerOneDimensionalDataPO> oneDimensionalDataEntities = serverOneDimensionalDataDao.findLastDataByServerId(id);
		if(oneDimensionalDataEntities!=null && oneDimensionalDataEntities.size()>0){
			ServerOneDimensionalDataPO oneDimensionalDataEntity = oneDimensionalDataEntities.get(0);
			serverStatus.put("oneDimensionalData", new ServerOneDimensionalDataVO().set(oneDimensionalDataEntity));
		}
		List<ServerHardDiskDataPO> diskEntities = serverHardDiskDataDao.findLastDataByServerId(id);
		serverStatus.put("disks", ServerHardDiskDataVO.getConverter(ServerHardDiskDataVO.class).convert(diskEntities, ServerHardDiskDataVO.class));
		List<ServerNetworkCardTrafficDataPO> networkEntities = serverNetworkCardTrafficDataDao.findLastDataByServerId(id);
		serverStatus.put("networks", ServerNetworkCardTrafficDataVO.getConverter(ServerNetworkCardTrafficDataVO.class).convert(networkEntities, ServerNetworkCardTrafficDataVO.class));
		return serverStatus;
	}
	
	/**
	 * 
	 * 根据服务器id查询数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月2日 下午5:40:08
	 * @param serverId 服务器id
	 * @return
	 * @throws Exception
	 */
	public List<DatabaseVO> findDatabase(Long serverId) throws Exception{
		List<DatabasePO> list = databaseDAO.findByServerId(serverId);
		return DatabaseVO.getConverter(DatabaseVO.class).convert(list, DatabaseVO.class);
	}
	
	/**
	 * 
	 * 查询所有数据库<br/>
	 * <b>作者:</b>jiajun<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 下午2:07:47
	 * @return
	 * @throws Exception
	 */
	public List<DatabaseVO> findAllDatabase() throws Exception{
		List<DatabasePO> list = databaseDAO.findAll();
		return DatabaseVO.getConverter(DatabaseVO.class).convert(list, DatabaseVO.class);
	}

	/**
	 * 查询和开启告警<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月26日 下午1:47:58
	 * @return ServerAlarmVO cpu、内存、硬盘使用告警限制，默认80、80、80
	 */
	public ServerAlarmVO queryLimitRate() throws Exception{
		List<ServerAlarmPO> serverAlarmPOs = serverAlarmDAO.findAll();
		if (serverAlarmPOs != null && serverAlarmPOs.size() > 0) {
			ServerAlarmVO serverAlarmVO = new ServerAlarmVO().set(serverAlarmPOs.get(0));
			return serverAlarmVO;
		}else {
			ServerAlarmPO serverAlarmPO = new ServerAlarmPO();
			serverAlarmPO.setCpuRate(80l);
			serverAlarmPO.setDiskRate(80l);
			serverAlarmPO.setMemoryRate(80l);
			serverAlarmPO.setProcessCpu(50l);
			serverAlarmDAO.save(serverAlarmPO);
			ServerAlarmVO serverAlarmVO = new ServerAlarmVO().set(serverAlarmPO);
			return serverAlarmVO;
		}
	}

	/**
	 * 查询存在告警信息的服务器数据<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 上午10:00:18
	 * @param serverId 服务器id
	 * @return List<ServerOneDimensionalDataVO>
	 */
	public List<ServerOneDimensionalDataVO> showAlarmMessage(Long serverId) throws Exception{
		List<ServerOneDimensionalDataPO> serverOneDimensionalDataPOs = serverOneDimensionalDataDAO.findByServerIdAndAlarmMessageNotNULL(serverId);
		List<ServerOneDimensionalDataVO> serverOneDimensionalDataVOs = new ArrayList<ServerOneDimensionalDataVO>();
		if(serverOneDimensionalDataPOs != null && serverOneDimensionalDataPOs.size() > 0){
			for (ServerOneDimensionalDataPO serverOneDimensionalDataPO : serverOneDimensionalDataPOs) {
				ServerOneDimensionalDataVO serverOneDimensionalDataVO = new ServerOneDimensionalDataVO().set(serverOneDimensionalDataPO);
				serverOneDimensionalDataVOs.add(serverOneDimensionalDataVO);
			}
		}
		return serverOneDimensionalDataVOs;
	}

	/**
	 * 查询具体的进程CPU占用率<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午6:20:27
	 * @param dataId 服务器数据id
	 * @return List<ServerProcessUsageVO> 进程的详细数据
	 */
	public List<ServerProcessUsageVO> showAlarmDetails(Long dataId) throws Exception{
		List<ServerProcessUsagePO> serverProcessUsagePOs =serverProcessUsageDAO.findByDataId(dataId);
		List<ServerProcessUsageVO> serverProcessUsageVOs = new ArrayList<ServerProcessUsageVO>();
		if(serverProcessUsagePOs != null && serverProcessUsagePOs.size() >0){
			for (ServerProcessUsagePO serverProcessUsagePO : serverProcessUsagePOs) {
				ServerProcessUsageVO serverProcessUsageVO = new ServerProcessUsageVO().set(serverProcessUsagePO);
				serverProcessUsageVOs.add(serverProcessUsageVO);
			}
		}
		return serverProcessUsageVOs;
	}
}
