package com.sumavision.bvc.device.monitor.point;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PtzctrlPassByContent;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.point.exception.MonitorPointNotExistException;
import com.sumavision.bvc.device.monitor.point.exception.NoUseableIndexException;
import com.sumavision.bvc.device.monitor.point.exception.UserHasNoPermissionForPointException;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorPointService {
	
	private static final String PASSBY_TYPE = "pointindexoperation";
	
	@Autowired
	private MonitorPointDAO monitorPointDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusinessProxy;

	/**
	 * xt通过联网添加预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 下午2:16:54
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @param String layerId 设备接入层id
	 * @param String name 预置点
	 * @param Long userId 用户id
	 * @param String username 用户名称
	 * @param String xml 透传协议
	 * @return MonitorPointPO 预置点
	 */
	public MonitorPointPO passbyAdd(
			String bundleId,
			String bundleName,
			String layerId,
			String name,
			Long userId,
			String username,
			String xml) throws Exception{
		
		String index = findUseableIndex(bundleId);
		if(index == null){
			throw new NoUseableIndexException();
		}
		
		MonitorPointPO point = new MonitorPointPO();
		point.setBundleId(bundleId);
		point.setBundleName(bundleName);
		point.setLayerId(layerId);
		point.setName(name);
		point.setIndex(index);
		point.setUserId(userId);
		point.setUsername(username);
		monitorPointDao.save(point);
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setBundle_id(bundleId);
		passBy.setLayer_id(layerId);
		passBy.setType(PASSBY_TYPE);
		PtzctrlPassByContent content = new PtzctrlPassByContent();
		content.setXml(xml);
		passBy.setPass_by_content(content);
		logic.getPass_by().add(passBy);
		
		executeBusinessProxy.execute(logic, "点播系统：xt通过联网添加预置点");
		
		return point;
	}
	
	/**
	 * 添加预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:15:35
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @param String layerId 接入层id
	 * @param String name 预置点名称
	 * @param Long userId 用户id
	 * @param String username 用户名称
	 * @return MonitorPointPO 预置点
	 */
	public MonitorPointPO add(
			String bundleId,
			String bundleName,
			String layerId,
			String name,
			Long userId,
			String username) throws Exception{
		
		String index = findUseableIndex(bundleId);
		if(index == null){
			throw new NoUseableIndexException();
		}
		
		MonitorPointPO point = new MonitorPointPO();
		point.setBundleId(bundleId);
		point.setBundleName(bundleName);
		point.setLayerId(layerId);
		point.setName(name);
		point.setIndex(index);
		point.setUserId(userId);
		point.setUsername(username);
		monitorPointDao.save(point);
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setBundle_id(bundleId);
		passBy.setLayer_id(layerId);
		passBy.setType(PASSBY_TYPE);
		String xml = generateProtocal("add", name, index);
		PtzctrlPassByContent content = new PtzctrlPassByContent();
		content.setXml(xml);
		passBy.setPass_by_content(content);
		logic.getPass_by().add(passBy);
		
		executeBusinessProxy.execute(logic, "点播系统：添加预置点");
		
		return point;
	}
	
	/**
	 * xt通过联网设置预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 下午3:40:04
	 * @param String userId 用户id
	 * @param String bundleId 设备id
	 * @param String layerId 设备接入层id
	 * @param String xml 透传预置点协议
	 */
	public void passByInvoke(
			Long userId,
			String bundleId,
			String layerId,
			String xml) throws Exception{
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setType(PASSBY_TYPE);
		PtzctrlPassByContent content = new PtzctrlPassByContent();
		content.setXml(xml);
		passBy.setPass_by_content(content);
		logic.getPass_by().add(passBy);
		
		executeBusinessProxy.execute(logic, "点播系统：xt通过联网设置预置点");
	}
	
	/**
	 * 移动到预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:45:12
	 * @param Long id 预置点id
	 * @param Long userId 业务用户id
	 */
	public void invoke(Long id, Long userId) throws Exception{
		MonitorPointPO point = monitorPointDao.findOne(id);
		if(point == null){
			throw new MonitorPointNotExistException();
		}
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setBundle_id(point.getBundleId());
		passBy.setLayer_id(point.getLayerId());
		passBy.setType(PASSBY_TYPE);
		String xml = generateProtocal("invoke", point.getName(), point.getIndex());
		PtzctrlPassByContent content = new PtzctrlPassByContent();
		content.setXml(xml);
		passBy.setPass_by_content(content);
		logic.getPass_by().add(passBy);
		
		executeBusinessProxy.execute(logic, "点播系统：移动到预置点");
	}
	
	/**
	 * xt通过联网删除预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 下午5:26:28
	 * @param Long userId 用户id
	 * @param String bundleId 设备id
	 * @param String layerId 设备接入层id
	 * @param String xml 透传协议
	 */
	public void passByRemove(
			Long userId, 
			String bundleId, 
			String layerId, 
			String xml) throws Exception{
		
		String index = xml.split("<pointindexvalue>")[1].split("</pointindexvalue>")[0];
		
		List<MonitorPointPO> points = monitorPointDao.findByBundleIdAndIndex(bundleId, index);
		if(points!=null && points.size()>0){
			monitorPointDao.deleteInBatch(points);
		}
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setBundle_id(bundleId);
		passBy.setLayer_id(layerId);
		passBy.setType(PASSBY_TYPE);
		PtzctrlPassByContent content = new PtzctrlPassByContent();
		content.setXml(xml);
		passBy.setPass_by_content(content);
		logic.getPass_by().add(passBy);
		
		executeBusinessProxy.execute(logic, "点播系统：xt通过联网删除预置点");
	}
	
	/**
	 * 删除预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:51:05
	 * @param Long id 预置点id
	 * @param Long userId 业务用户id
	 */
	public void remove(Long id, Long userId) throws Exception{
		MonitorPointPO point = monitorPointDao.findOne(id);
		if(point == null){
			return;
		}
		if(!point.getUserId().equals(userId)){
			throw new UserHasNoPermissionForPointException();
		}
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		
		PassByBO passBy = new PassByBO();
		passBy.setBundle_id(point.getBundleId());
		passBy.setLayer_id(point.getLayerId());
		passBy.setType(PASSBY_TYPE);
		String xml = generateProtocal("remove", point.getName(), point.getIndex());
		PtzctrlPassByContent content = new PtzctrlPassByContent();
		content.setXml(xml);
		passBy.setPass_by_content(content);
		logic.getPass_by().add(passBy);
		
		executeBusinessProxy.execute(logic, "点播系统：删除预置点");
		
		monitorPointDao.delete(point);
	}
	
	/**
	 * 获取可用的预置点索引<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:14:53
	 * @param String bundleId 设备id
	 * @return String 索引
	 */
	private String findUseableIndex(String bundleId){
		List<MonitorPointPO> existPoints = monitorPointDao.findByBundleId(bundleId);
		if(existPoints==null || existPoints.size()<=0) return "0";
		Set<String> existIndexes = new HashSet<String>();
		for(MonitorPointPO point:existPoints){
			existIndexes.add(point.getIndex());
		}
		for(int i=0; i<256; i++){
			String testIndex = String.valueOf(i);
			if(!existIndexes.contains(testIndex)){
				return testIndex;
			}
		}
		return null;
	}
	
	/**
	 * 生成sip协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:29:56
	 * @param String action 操作
	 * @param String name 预置点名称
	 * @param String index 预置点索引
	 * @return String sip协议
	 */
	private String generateProtocal(String action, String name, String index){
		StringBufferWrapper protocol = new StringBufferWrapper();
		protocol.append("<control>")
				.append("<commandname>pointindexoperation</commandname>")
				.append(new StringBufferWrapper().append("<operation>").append(action).append("</operation>").toString())
				.append(new StringBufferWrapper().append("<seq>").append(UUID.randomUUID().toString().replaceAll("-", "")).append("</seq>").toString())
				.append(new StringBufferWrapper().append("<ts>").append(new Date().getTime()).append("</ts>").toString())
				.append("<devname></devname>")
				.append("<devid></devid>")
				.append(new StringBufferWrapper().append("<pointindexname>").append(name).append("</pointindexname>").toString())
				.append(new StringBufferWrapper().append("<pointindexvalue>").append(index).append("</pointindexvalue>").toString())
				.append("</control>");
		return protocol.toString();
	}
	
}
