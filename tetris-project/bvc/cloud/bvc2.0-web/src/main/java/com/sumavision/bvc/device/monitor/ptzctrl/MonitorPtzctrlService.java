package com.sumavision.bvc.device.monitor.ptzctrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PtzctrlPassByContent;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MonitorPtzctrlService {
	
	private static final String PASSBY_TYPE = "ptzctrl";
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness = new ExecuteBusinessProxy();
	
	/**
	 * 透传云镜控制协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 下午1:22:17
	 * @param String userId 用户id
	 * @param String bundleId 设备id
	 * @param String layerId 设备接入层id
	 * @param String xml xml协议
	 */
	public void passBy(
			String userId, 
			String bundleId,
			String layerId,
			String xml) throws Exception{
		
		LogicBO logic = new LogicBO();
		logic.setUserId(userId);
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：xt通过联网云台控制");
		
	}
	
	/**
	 * 竖直方向移动镜头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param Direction direction 方向描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void vertical(
			String bundleId, 
			String layerId, 
			Direction direction, 
			String speed, 
			Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		StringBufferWrapper control = new StringBufferWrapper();
		control.append("<tiltservo>")
			   .append(new StringBufferWrapper().append("<direction>").append(direction.getProtocol()).append("</direction>").toString())
			   .append(new StringBufferWrapper().append("<speed>").append(speed).append("</speed>").toString())
			   .append("</tiltservo>");
		String xml = generateProtocal("start", control.toString());
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：云台控制");
	}
	
	/**
	 * 水平方向移动镜头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param Direction direction 方向描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void horizontal(
			String bundleId, 
			String layerId, 
			Direction direction, 
			String speed, 
			Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		StringBufferWrapper control = new StringBufferWrapper();
		control.append("<panservo>")
			   .append(new StringBufferWrapper().append("<direction>").append(direction.getProtocol()).append("</direction>").toString())
			   .append(new StringBufferWrapper().append("<speed>").append(speed).append("</speed>").toString())
			   .append("</panservo>");
		String xml = generateProtocal("start", control.toString());
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：云台控制");
	}
	
	/**
	 * 镜头变倍控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param ZoomControl direction 缩放描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void zoom(
			String bundleId, 
			String layerId, 
			ZoomControl direction, 
			String speed, 
			Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		StringBufferWrapper control = new StringBufferWrapper();
		control.append("<zoom>")
			   .append(new StringBufferWrapper().append("<zoomctrl>").append(direction.getProtocol()).append("</zoomctrl>").toString())
			   .append(new StringBufferWrapper().append("<speed>").append(speed).append("</speed>").toString())
			   .append("</zoom>");
		String xml = generateProtocal("start", control.toString());
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：云台控制");
	}
	
	/**
	 * 焦距控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param FocusControl direction 缩放描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void focus(
			String bundleId, 
			String layerId, 
			FocusControl direction, 
			String speed, 
			Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		StringBufferWrapper control = new StringBufferWrapper();
		control.append("<focus>")
			   .append(new StringBufferWrapper().append("<focusctrl>").append(direction.getProtocol()).append("</focusctrl>").toString())
			   .append(new StringBufferWrapper().append("<speed>").append(speed).append("</speed>").toString())
			   .append("</focus>");
		String xml = generateProtocal("start", control.toString());
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：云台控制");
	}
	
	/**
	 * 光圈控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param ApertureControl direction 缩放描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void aperture(
			String bundleId, 
			String layerId, 
			ApertureControl direction, 
			String speed, 
			Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		StringBufferWrapper control = new StringBufferWrapper();
		control.append("<aperture>")
			   .append(new StringBufferWrapper().append("<aperturectrl>").append(direction.getProtocol()).append("</aperturectrl>").toString())
			   .append(new StringBufferWrapper().append("<speed>").append(speed).append("</speed>").toString())
			   .append("</aperture>");
		String xml = generateProtocal("start", control.toString());
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：云台控制");
	}
	
	/**
	 * 停止控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param Long userId 业务用户
	 */
	public void stop(
			String bundleId, 
			String layerId, 
			Long userId) throws Exception{
		LogicBO logic = new LogicBO();
		logic.setUserId(userId.toString());
		logic.setPass_by(new ArrayList<PassByBO>());
		PassByBO ptzctrl = new PassByBO();
		ptzctrl.setBundle_id(bundleId);
		ptzctrl.setLayer_id(layerId);
		ptzctrl.setType(PASSBY_TYPE);
		String xml = generateProtocal("stop", null);
		ptzctrl.setPass_by_content(new PtzctrlPassByContent().setXml(xml));
		logic.getPass_by().add(ptzctrl);
		executeBusiness.execute(logic, "点播系统：停止云台控制");
	}
	
	/**
	 * 生成云台控制协议<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午6:35:09
	 * @param String operation start or stop
	 * @param String content 控制内容
	 * @return String xml协议
	 */
	private String generateProtocal(String operation, String content){
		StringBufferWrapper protocol = new StringBufferWrapper();
		protocol.append("<control>")
				.append("<commandname>ptzctrl</commandname>")
				.append(new StringBufferWrapper().append("<operation>").append(operation).append("</operation>").toString())
				.append(new StringBufferWrapper().append("<seq>").append(UUID.randomUUID().toString().replace("-", "")).append("</seq>").toString())
				.append(new StringBufferWrapper().append("<ts>").append(new Date().getTime()).append("</ts>").toString())
				.append("<devname></devname>")
				.append("<devid></devid>")
				.append(content==null?"":content)
				.append("</control>");
		return protocol.toString();
	}
	
	public static void main(String[] args) throws Exception{
		String bundleId = "bundleId";
		String layerId = "layerId";
		Long userId = 1l;
		String speed = "10";
		
		MonitorPtzctrlService service = new MonitorPtzctrlService();
		System.out.println("----------------------------------------");
		service.vertical(bundleId, layerId, Direction.UP, speed, userId);
		service.vertical(bundleId, layerId, Direction.DOWN, speed, userId);
		System.out.println("----------------------------------------");
		service.horizontal(bundleId, layerId, Direction.LEFT, speed, userId);
		service.horizontal(bundleId, layerId, Direction.RIGHT, speed, userId);
		System.out.println("----------------------------------------");
		service.zoom(bundleId, layerId, ZoomControl.IN, speed, userId);
		service.zoom(bundleId, layerId, ZoomControl.OUT, speed, userId);
		System.out.println("----------------------------------------");
		service.focus(bundleId, layerId, FocusControl.NEAR, speed, userId);
		service.focus(bundleId, layerId, FocusControl.FAR, speed, userId);
		System.out.println("----------------------------------------");
		service.aperture(bundleId, layerId, ApertureControl.PLUS, speed, userId);
		service.aperture(bundleId, layerId, ApertureControl.MINUS, speed, userId);
		System.out.println("----------------------------------------");
		service.stop(bundleId, layerId, userId);
	}
	
}
