package com.sumavision.bvc.device.monitor.live.device;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.bvc.page.PageTaskDAO;

@Component
public class MonitorLiveDeviceQuery {
	
	@Autowired
	private MonitorLiveDeviceDAO monitorLiveDeviceDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	/**
	 * 分页查询用户点播设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月24日 上午11:55:44
	 * @param Long userId 业务用户id
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorLiveDevicePO> 点播设备任务列表
	 */
	public List<MonitorLiveDevicePO> findByUserId(
			Long userId, 
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorLiveDevicePO> pagedEntities = monitorLiveDeviceDao.findByUserId(userId, page);
		if(pagedEntities == null){
			return null;
		}
		return pagedEntities.getContent();
	}
	
	/**
	 * 分页查询用户点播设备任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月27日 上午10:24:50
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<MonitorLiveDevicePO> 点播设备任务列表
	 */
	public List<MonitorLiveDevicePO> findAll(
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<MonitorLiveDevicePO> pagedEntities = monitorLiveDeviceDao.findAll(page);
		if(pagedEntities == null){
			return null;
		}
		return pagedEntities.getContent();
	}
	
	/**
	 * 查询转发路数<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月13日 下午4:32:43
	 * @return
	 */
	public Long queryCountOfTransmit(){
		
		 return monitorLiveDeviceDao.countByStatus(MonitorRecordStatus.RUN);
	}
	
}
