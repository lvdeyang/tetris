package com.sumavision.tetris.omms.hardware.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerHardDiskDataVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerNetworkCardTrafficDataVO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataDAO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataPO;
import com.sumavision.tetris.omms.hardware.server.data.ServerOneDimensionalDataVO;

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
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
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
		ServerPO serverEntity = serverDao.findOne(id);
		serverStatus.put("server", new ServerVO().set(serverEntity));
		ServerOneDimensionalDataPO oneDimensionalDataEntity = serverOneDimensionalDataDao.findLastDataByServerId(id);
		if(oneDimensionalDataEntity != null){
			serverStatus.put("oneDimensionalData", new ServerOneDimensionalDataVO().set(oneDimensionalDataEntity));
		}
		List<ServerHardDiskDataPO> diskEntities = serverHardDiskDataDao.findLastDataByServerId(id);
		serverStatus.put("disks", ServerHardDiskDataVO.getConverter(ServerHardDiskDataVO.class).convert(diskEntities, ServerHardDiskDataVO.class));
		List<ServerNetworkCardTrafficDataPO> networkEntities = serverNetworkCardTrafficDataDao.findLastDataByServerId(id);
		serverStatus.put("networks", ServerNetworkCardTrafficDataVO.getConverter(ServerNetworkCardTrafficDataVO.class).convert(networkEntities, ServerNetworkCardTrafficDataVO.class));
		return serverStatus;
	}
	
}
