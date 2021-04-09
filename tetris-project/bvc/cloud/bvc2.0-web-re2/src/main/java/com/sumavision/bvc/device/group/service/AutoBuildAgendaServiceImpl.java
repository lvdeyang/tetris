package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.device.group.dao.DeviceGroupBusinessRoleDAO;
import com.sumavision.bvc.device.group.enumeration.AutoBuildAgenda;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigPO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;

/**
 * 自动建立议程，通常在建会时使用
 * @author zsy
 *
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AutoBuildAgendaServiceImpl {

	@Autowired
	private AgendaServiceImpl agendaServiceImpl;
	
	@Autowired
	private DeviceGroupBusinessRoleDAO deviceGroupBusinessRoleDao;
	
	public void buildAgendas(DeviceGroupPO group, String autoBuildAgendaIds) throws Exception{
		if(autoBuildAgendaIds == null){
			return;
		}
		String[] idArray = autoBuildAgendaIds.split(",");
		
		for(String id : idArray){
			if(AutoBuildAgenda.CHAIRMAN_AUDIENCE.getId().toString().equals(id)){
				buildAgenda1_CHAIRMAN_AUDIENCE(id, group);
			}else if(AutoBuildAgenda.ONE_VIDEO.getId().toString().equals(id)){
				buildAgenda2_ONE_VIDEO(id, group);
			}
		}
	}
	
	/**
	 * 1个视频给主席，1个视频给观众及发言人
	 * @param group
	 * @param agendaId: AUDIENCE/CHAIRMAN 等
	 * @throws Exception
	 */
	private void buildAgenda1_CHAIRMAN_AUDIENCE(String id, DeviceGroupPO group) throws Exception{
		String agendaName = AutoBuildAgenda.fromAgendaId(id).getName();
		DeviceGroupConfigPO agenda = agendaServiceImpl.save(group.getId(), agendaName, "", "保持状态");
		
		DeviceGroupConfigVideoPO video1 = agendaServiceImpl.addDefaultVideo(agenda.getId(), "主席观看");		
		JSONArray roleDst1 = new JSONArray();
		roleDst1.addAll(makeRoleDst(group.getId(), BusinessRoleSpecial.CHAIRMAN));
		agendaServiceImpl.updateVideo(video1.getId(), video1.getWebsiteDraw(), "[]", "[]", roleDst1.toJSONString(), video1.getLayout().getName(), null);

		DeviceGroupConfigVideoPO video2 = agendaServiceImpl.addDefaultVideo(agenda.getId(), "观众观看");
		JSONArray roleDst2 = new JSONArray();
		roleDst2.addAll(makeRoleDst(group.getId(), BusinessRoleSpecial.AUDIENCE));
		roleDst2.addAll(makeRoleDst(group.getId(), BusinessRoleSpecial.SPOKESMAN));
		agendaServiceImpl.updateVideo(video2.getId(), video1.getWebsiteDraw(), "[]", "[]", roleDst2.toJSONString(), video2.getLayout().getName(), null);
	}
	
	/**
	 * 1个视频给所有人
	 * @param group
	 * @throws Exception
	 */
	private void buildAgenda2_ONE_VIDEO(String id, DeviceGroupPO group) throws Exception{
		String agendaName = AutoBuildAgenda.fromAgendaId(id).getName();
		DeviceGroupConfigPO agenda = agendaServiceImpl.save(group.getId(), agendaName, "", "保持状态");
		
		DeviceGroupConfigVideoPO video1 = agendaServiceImpl.addDefaultVideo(agenda.getId(), "全体观看");		
		JSONArray roleDst1 = new JSONArray();
		roleDst1.addAll(makeRoleDst(group.getId(), BusinessRoleSpecial.CHAIRMAN));
		roleDst1.addAll(makeRoleDst(group.getId(), BusinessRoleSpecial.SPOKESMAN));
		roleDst1.addAll(makeRoleDst(group.getId(), BusinessRoleSpecial.AUDIENCE));
		agendaServiceImpl.updateVideo(video1.getId(), video1.getWebsiteDraw(), "[]", "[]", roleDst1.toJSONString(), video1.getLayout().getName(), null);
	}
	
	/**
	 * 按角色生成一个roleDst数组，给agendaServiceImpl.updateVideo()方法使用
	 * @param groupId
	 * @param role 
	 * @return 
	 */
	private JSONArray makeRoleDst(Long groupId, BusinessRoleSpecial role){
		JSONArray roleDst = new JSONArray();
		List<DeviceGroupBusinessRolePO> rolePos = deviceGroupBusinessRoleDao.findByGroupIdAndSpecial(groupId, role);
		if(rolePos!=null && rolePos.size()>0){
			for(DeviceGroupBusinessRolePO rolePo : rolePos){
				JSONObject aRoleDst = new JSONObject();
				aRoleDst.put("roleScreenId", "screen_1");
				aRoleDst.put("roleId", rolePo.getId());
				aRoleDst.put("roleName", role.getName());
				roleDst.add(aRoleDst);
			}
		}
		return roleDst;
	}
	
	//生成音频JSON
	public JSONObject generateAudio(){
		
		JSONObject agenda1audio = new JSONObject();
		agenda1audio.put("audioOperation", "自定义");
		agenda1audio.put("volume", 100);
		agenda1audio.put("audioSrcs", new ArrayList<JSONObject>());
		
		return agenda1audio;
	}
	
	//生成主席观看所有观众轮询
	public JSONObject generatePollingChairman(List<DeviceGroupMemberPO> audienceMembers, DeviceGroupBusinessRolePO roleChairman){
		
		JSONObject agendaVideo1 = new JSONObject();
		agendaVideo1.put("videoName", "主席观看");
		agendaVideo1.put("videoMode", "合屏");
		
		JSONObject layout = new JSONObject();
		JSONObject basic = new JSONObject();
		basic.put("column", 1);
		basic.put("row", 1);
		layout.put("basic", basic);
		List<JSONObject> cellspan = new ArrayList<JSONObject>();
		layout.put("cellspan", cellspan);
		agendaVideo1.put("layout", layout);
		
		List<JSONObject> agendaVideo1Positions = new ArrayList<JSONObject>();
		JSONObject agendaVideo1Position1 = new JSONObject();
		agendaVideo1Position1.put("serialNum", 1);
		agendaVideo1Position1.put("x", "0/1");
		agendaVideo1Position1.put("y", "0/1");
		agendaVideo1Position1.put("w", "1/1");
		agendaVideo1Position1.put("h", "1/1");
		agendaVideo1Position1.put("srcType", "BUNDLE");
		agendaVideo1Position1.put("pollingTime", 15);
		List<String> agendaVideo1Position1Srcs = new ArrayList<String>();
		for(DeviceGroupMemberPO memberPO: audienceMembers){
			String agendaVideo1Position1Src = memberPO.getBundleId();
			agendaVideo1Position1Srcs.add(agendaVideo1Position1Src);
		}
		agendaVideo1Position1.put("src", agendaVideo1Position1Srcs);
		agendaVideo1Positions.add(agendaVideo1Position1);
		
		agendaVideo1.put("positions", agendaVideo1Positions);
		
		List<JSONObject> agendaVideo1Dsts = new ArrayList<JSONObject>();
		JSONObject agendaVideo1Dst = new JSONObject();
		agendaVideo1Dst.put("dstType", "ROLE");
		agendaVideo1Dst.put("dst", roleChairman.getId() + "@@screen_1");
		agendaVideo1Dsts.add(agendaVideo1Dst);
		agendaVideo1.put("dsts", agendaVideo1Dsts);
		
		return agendaVideo1;
	}
	
	//生成观众和发言人观看主席
	public JSONObject generateChairmanAudience(DeviceGroupBusinessRolePO roleChairman, DeviceGroupBusinessRolePO roleAudience, List<DeviceGroupBusinessRolePO> spokenmans){
		
		JSONObject agendaVideo1 = new JSONObject();
		agendaVideo1.put("videoName", "观众观看");
		agendaVideo1.put("videoMode", "转发");
		
		JSONObject layout = new JSONObject();
		JSONObject basic = new JSONObject();
		basic.put("column", 1);
		basic.put("row", 1);
		layout.put("basic", basic);
		List<JSONObject> cellspan = new ArrayList<JSONObject>();
		layout.put("cellspan", cellspan);
		agendaVideo1.put("layout", layout);
		
		List<JSONObject> agendaVideo1Positions = new ArrayList<JSONObject>();
		JSONObject agendaVideo1Position1 = new JSONObject();
		agendaVideo1Position1.put("serialNum", 1);
		agendaVideo1Position1.put("x", "0/1");
		agendaVideo1Position1.put("y", "0/1");
		agendaVideo1Position1.put("w", "1/1");
		agendaVideo1Position1.put("h", "1/1");
		agendaVideo1Position1.put("srcType", "ROLE");
		agendaVideo1Position1.put("pollingTime", 15);
		List<String> agendaVideo1Position1Srcs = new ArrayList<String>();
		String agendaVideo1Position1Src = roleChairman.getId() +"@@VIDEOENCODE1"; 
		agendaVideo1Position1Srcs.add(agendaVideo1Position1Src);
		agendaVideo1Position1.put("src", agendaVideo1Position1Srcs);
		agendaVideo1Positions.add(agendaVideo1Position1);
		
		agendaVideo1.put("positions", agendaVideo1Positions);
		
		List<JSONObject> agendaVideo1Dsts = new ArrayList<JSONObject>();
		JSONObject agendaVideo1Dst = new JSONObject();
		agendaVideo1Dst.put("dstType", "ROLE");
		agendaVideo1Dst.put("dst", roleAudience.getId() + "@@screen_1");
		agendaVideo1Dsts.add(agendaVideo1Dst);
		
		for(DeviceGroupBusinessRolePO businessRole: spokenmans){
			JSONObject agendaVideo2Dst = new JSONObject();
			agendaVideo2Dst.put("dstType", "ROLE");
			agendaVideo2Dst.put("dst", businessRole.getId() + "@@screen_1");
			agendaVideo1Dsts.add(agendaVideo2Dst);
		}
		
		agendaVideo1.put("dsts", agendaVideo1Dsts);
		
		return agendaVideo1;
	}
	
	//所有人看四合屏
	public JSONObject generateSpokenmanAll(DeviceGroupBusinessRolePO roleChairman, List<DeviceGroupBusinessRolePO> spokenmans, DeviceGroupBusinessRolePO roleAudience){
		
		JSONObject agendaVideo1 = new JSONObject();
		agendaVideo1.put("videoName", "所有人观看");
		agendaVideo1.put("videoMode", "合屏");
		
		JSONObject layout = new JSONObject();
		JSONObject basic = new JSONObject();
		basic.put("column", 2);
		basic.put("row", 2);
		layout.put("basic", basic);
		List<JSONObject> cellspan = new ArrayList<JSONObject>();
		layout.put("cellspan", cellspan);
		agendaVideo1.put("layout", layout);
		
		List<JSONObject> agendaVideo1Positions = new ArrayList<JSONObject>();
		JSONObject agendaVideo1Position1 = new JSONObject();
		agendaVideo1Position1.put("serialNum", 1);
		agendaVideo1Position1.put("x", "0/2");
		agendaVideo1Position1.put("y", "0/2");
		agendaVideo1Position1.put("w", "1/2");
		agendaVideo1Position1.put("h", "1/2");
		agendaVideo1Position1.put("srcType", "ROLE");
		agendaVideo1Position1.put("pollingTime", 15);
		List<String> agendaVideo1Position1Srcs = new ArrayList<String>();
		String agendaVideo1Position1Src = roleChairman.getId() +"@@VIDEOENCODE1"; 
		agendaVideo1Position1Srcs.add(agendaVideo1Position1Src);
		agendaVideo1Position1.put("src", agendaVideo1Position1Srcs);
		agendaVideo1Positions.add(agendaVideo1Position1);
		
		JSONObject agendaVideo1Position2 = new JSONObject();
		agendaVideo1Position2.put("serialNum", 2);
		agendaVideo1Position2.put("x", "1/2");
		agendaVideo1Position2.put("y", "0/2");
		agendaVideo1Position2.put("w", "1/2");
		agendaVideo1Position2.put("h", "1/2");
		agendaVideo1Position2.put("srcType", "ROLE");
		agendaVideo1Position2.put("pollingTime", 15);
		List<String> agendaVideo1Position2Srcs = new ArrayList<String>();
		String agendaVideo1Position2Src = spokenmans.get(0).getId()+"@@VIDEOENCODE1"; 
		agendaVideo1Position2Srcs.add(agendaVideo1Position2Src);
		agendaVideo1Position2.put("src", agendaVideo1Position2Srcs);
		agendaVideo1Positions.add(agendaVideo1Position2);
		
		JSONObject agendaVideo1Position3 = new JSONObject();
		agendaVideo1Position3.put("serialNum",3);
		agendaVideo1Position3.put("x", "0/2");
		agendaVideo1Position3.put("y", "1/2");
		agendaVideo1Position3.put("w", "1/2");
		agendaVideo1Position3.put("h", "1/2");
		agendaVideo1Position3.put("srcType", "ROLE");
		agendaVideo1Position3.put("pollingTime", 15);
		List<String> agendaVideo1Position3Srcs = new ArrayList<String>();
		String agendaVideo1Position3Src = spokenmans.get(1).getId() +"@@VIDEOENCODE1"; 
		agendaVideo1Position3Srcs.add(agendaVideo1Position3Src);
		agendaVideo1Position3.put("src", agendaVideo1Position3Srcs);
		agendaVideo1Positions.add(agendaVideo1Position3);
		
		JSONObject agendaVideo1Position4 = new JSONObject();
		agendaVideo1Position4.put("serialNum", 4);
		agendaVideo1Position4.put("x", "1/2");
		agendaVideo1Position4.put("y", "1/2");
		agendaVideo1Position4.put("w", "1/2");
		agendaVideo1Position4.put("h", "1/2");
		agendaVideo1Position4.put("srcType", "ROLE");
		agendaVideo1Position4.put("pollingTime", 15);
		List<String> agendaVideo1Position4Srcs = new ArrayList<String>();
		String agendaVideo1Position4Src = spokenmans.get(2).getId() +"@@VIDEOENCODE1"; 
		agendaVideo1Position4Srcs.add(agendaVideo1Position4Src);
		agendaVideo1Position4.put("src", agendaVideo1Position4Srcs);
		agendaVideo1Positions.add(agendaVideo1Position4);
		
		agendaVideo1.put("positions", agendaVideo1Positions);
		
		List<JSONObject> agendaVideo1Dsts = new ArrayList<JSONObject>();
		JSONObject agendaVideo1Dst = new JSONObject();
		agendaVideo1Dst.put("dstType", "ROLE");
		agendaVideo1Dst.put("dst", roleChairman.getId() + "@@screen_1");
		agendaVideo1Dsts.add(agendaVideo1Dst);
		
		JSONObject agendaVideo2Dst = new JSONObject();
		agendaVideo2Dst.put("dstType", "ROLE");
		agendaVideo2Dst.put("dst", roleAudience.getId() + "@@screen_1");
		agendaVideo1Dsts.add(agendaVideo2Dst);
		
		for(DeviceGroupBusinessRolePO role: spokenmans){
			JSONObject agendaVideo3Dst = new JSONObject();
			agendaVideo3Dst.put("dstType", "ROLE");
			agendaVideo3Dst.put("dst", role.getId() + "@@screen_1");
			agendaVideo1Dsts.add(agendaVideo3Dst);
		}
		
		agendaVideo1.put("dsts", agendaVideo1Dsts);
		
		return agendaVideo1;
	}
	
}
