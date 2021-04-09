package com.sumavision.bvc.device.monitor.live;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.enumeration.CodecParamType;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.MeetingUtil;
import com.sumavision.bvc.device.monitor.exception.AvtplNotFoundException;
import com.sumavision.bvc.device.system.AvtplService;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;

@Component
public class MonitorLiveCommons {

	@Autowired
	private AvtplDAO avtplDao;
	
	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private AvtplService avtplService;
	
	@Autowired
	private MeetingUtil meetingUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * 获取系统默认参数模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午9:27:43
	 * @return avtpl AvtplPO 参数模板
	 * @return gear AvtplGearsPO 参数档位
	 */
	public Map<String, Object> queryDefaultAvCodec() throws Exception{
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
		if(avTpls==null || avTpls.size()<=0){
			avtplService.generateDefaultAvtpls();
			avTpls = avtplDao.findByUsageType(AvtplUsageType.VOD);
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "VOD1");
			avTpls.add(sys_avtpl);
			throw new AvtplNotFoundException("系统缺少点播系统参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		List<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		if(targetGear == null){
			throw new AvtplNotFoundException("点播系统参数模板没有创建档位！");
		}
		
		return new HashMapWrapper<String, Object>().put("avtpl", targetAvtpl)
												   .put("gear", targetGear)
												   .getMap();
	}

	/**
	 * 获取系统默认播放器预览参数模板<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月18日 上午9:11:04
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryDefaultPlayerAvCodec() throws Exception{
		
		AvtplPO targetAvtpl = null;
		AvtplGearsPO targetGear = null;
		
		//查询codec参数
		List<AvtplPO> avTpls = avtplDao.findByUsageType(AvtplUsageType.WEB_PLAYER);
		if(avTpls==null || avTpls.size()<=0){
			avtplService.generateDefaultAvtpls();
			avTpls = avtplDao.findByUsageType(AvtplUsageType.WEB_PLAYER);
			AvtplPO sys_avtpl = meetingUtil.generateAvtpl(CodecParamType.DEFAULT.getName(), "PLAYER1");
			avTpls.add(sys_avtpl);
//			throw new AvtplNotFoundException("系统缺少播放器预览参数模板！");
		}
		targetAvtpl = avTpls.get(0);
		//查询codec模板档位
		List<AvtplGearsPO> gears = targetAvtpl.getGears();
		for(AvtplGearsPO gear:gears){
			targetGear = gear;
			break;
		}
		
		if(targetGear == null){
			throw new AvtplNotFoundException("播放器预览模板没有创建档位！");
		}
		
		return new HashMapWrapper<String, Object>().put("avtpl", targetAvtpl)
												   .put("gear", targetGear)
												   .getMap();
	}
	
	/**
	 * 获取系统中的联网，当前规定联网不做扩容，系统中只有一个<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月21日 上午10:59:47
	 * @return String 联网的接入层id
	 */
	public String queryNetworkLayerId() throws Exception{
		//List<WorkNodePO> layers = workNodeDao.findByType(NodeType.ACCESS_NETWORK);
		List<WorkNodePO> layers = workNodeDao.findByType(NodeType.ACCESS_LIANWANG);
		if(layers==null||layers.size()<=0){
			return null;
		}else{
			return layers.get(0).getNodeUid();
		}
	}
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月26日 下午4:28:17
	 * @param String uuid 业务uuid
	 * @param String code 错误码
	 * @param String message 错误信息
	 * @param Long userId 当前业务用户id
	 */
	public void sendFailPassby(String uuid, String code, String message, Long userId) throws Exception{
		String networkLayerId = queryNetworkLayerId();
		LogicBO logic = new LogicBO().setUserId(userId.toString())
									 .setPass_by(new ArrayList<PassByBO>());
		PassByBO passBy = new PassByBO().setLayer_id(networkLayerId)
										.setType("xt_business")
										.setPass_by_content(new HashMapWrapper<String, String>().put("uuid", uuid)
																								.put("cmd", "xt_business")
																								.put("operate", "stop")
																								.put("code", code)
																								.put("message", message)
																								.getMap());
		logic.getPass_by().add(passBy);
		executeBusiness.execute(logic, "点播系统：xt开启业务失败");
	}
	
}
