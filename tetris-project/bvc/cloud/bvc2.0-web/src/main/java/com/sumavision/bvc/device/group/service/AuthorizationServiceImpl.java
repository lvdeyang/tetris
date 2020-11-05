package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PassByContentBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationMemberDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;

@Transactional(rollbackFor = Exception.class)
@Service("com.sumavision.bvc.device.group.AuthorizationServiceImpl")
public class AuthorizationServiceImpl {

	@Autowired
	DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	DeviceGroupAuthorizationMemberDAO deviceGroupAuthorizationMemberDao;
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;

	
	/**
	 * @Title: 添加一个设备组数据 <br/>
	 * @Description: 多次数据库操作需要事务<br/>
	 * @param name 设备组名称
	 * @param type 设备组类型
	 * @param transmissionMode 设备组发流类型
	 * @param avtplId 选择的参数模板id
	 * @param systemTplId 选择的会议模板id
	 * @param region 会议地区
	 * @param classify 会议分类
	 * @throws Exception 
	 * @return DeviceGroupPO 设备组
	 */
	public DeviceGroupAuthorizationPO save(
			Long groupId,
			List<String> bundleIds
			) throws Exception{
		DeviceGroupPO group = deviceGroupDao.findOne(groupId);
		return save(group.getUuid(), bundleIds);
	}
	
	public DeviceGroupAuthorizationPO save(
			String groupUuid,
			List<String> bundleIds
			) throws Exception{
		
		List<BundlePO> bundles = resourceQueryUtil.queryAllBundlesByBundleIds(bundleIds);
		if(bundles == null){
			bundles = new ArrayList<BundlePO>();
		}
		
		LogicBO logic = new LogicBO();
		logic.setPass_by(new ArrayList<PassByBO>());
		
		DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupUuid);
		if(authorizationPO == null){
			//新建授权
			authorizationPO = new DeviceGroupAuthorizationPO();
			authorizationPO.setGroupUuid(groupUuid);			
			for(BundlePO bundle : bundles){
				//BundlePO转换成DeviceGroupAuthorizationMemberPO
				DeviceGroupAuthorizationMemberPO deviceGroupAuthorizationMemberPO = new DeviceGroupAuthorizationMemberPO(bundle);
				deviceGroupAuthorizationMemberPO.setAuthorization(authorizationPO);
				authorizationPO.getAuthorizationMembers().add(deviceGroupAuthorizationMemberPO);
			}			
			deviceGroupAuthorizationDao.save(authorizationPO);
			
			for(BundlePO bundle : bundles){
				PassByBO passBy = new PassByBO().setType("playlist_change_notify")
												.setLayer_id(bundle.getAccessNodeUid())
												.setBundle_id(bundle.getBundleId())
												.setPass_by_content(new PassByContentBO());
				logic.getPass_by().add(passBy);
			}
		}else{
			//修改授权，判断变化的成员
			Set<DeviceGroupAuthorizationMemberPO> existedMembers = authorizationPO.getAuthorizationMembers();
			
			//增量对比
			List<BundlePO> addMemberBundles = new ArrayList<BundlePO>();
			for(BundlePO bundle : bundles){
				String bundleId = bundle.getBundleId();
				boolean finded = false;
				for(DeviceGroupAuthorizationMemberPO existedMember: existedMembers){
					if(existedMember.getBundleId().equals(bundleId)){
						finded = true;
						break;
					}
				}
				if(!finded){
					addMemberBundles.add(bundle);				

					DeviceGroupAuthorizationMemberPO newMemberPO = new DeviceGroupAuthorizationMemberPO(bundle);
					newMemberPO.setAuthorization(authorizationPO);
					authorizationPO.getAuthorizationMembers().add(newMemberPO);
					
					PassByBO passBy = new PassByBO().setType("playlist_change_notify")
							.setLayer_id(bundle.getAccessNodeUid())
							.setBundle_id(bundle.getBundleId())
							.setPass_by_content(new PassByContentBO());
					logic.getPass_by().add(passBy);
				}
			}
			//删除对比
			List<DeviceGroupAuthorizationMemberPO> needRemoveMembers = new ArrayList<DeviceGroupAuthorizationMemberPO>();
			for(DeviceGroupAuthorizationMemberPO member : existedMembers){
				boolean finded = false;
				for(BundlePO bundle : bundles){
					String bundleId = bundle.getBundleId();
					if(bundleId.equals(member.getBundleId())){
						finded = true;
						break;
					}
				}
				if(!finded){
					needRemoveMembers.add(member);
					member.setAuthorization(null);
					
					PassByBO passBy = new PassByBO().setType("playlist_change_notify")
							.setLayer_id(member.getLayerId())
							.setBundle_id(member.getBundleId())
							.setPass_by_content(new PassByContentBO());
					logic.getPass_by().add(passBy);
				}
			}
			deviceGroupAuthorizationMemberDao.deleteInBatch(needRemoveMembers);
			Set<DeviceGroupAuthorizationMemberPO> members = authorizationPO.getAuthorizationMembers();
			for(DeviceGroupAuthorizationMemberPO member : members){
				member.setAuthorization(null);
//				deviceGroupAuthorizationMemberDao.delete(member);
			}
			deviceGroupAuthorizationMemberDao.deleteInBatch(members);		
			
			authorizationPO.setGroupUuid(groupUuid);			
			for(BundlePO bundle : bundles){
				//BundlePO转换成DeviceGroupAuthorizationMemberPO
				DeviceGroupAuthorizationMemberPO deviceGroupAuthorizationMemberPO = new DeviceGroupAuthorizationMemberPO(bundle);
				deviceGroupAuthorizationMemberPO.setAuthorization(authorizationPO);
				authorizationPO.getAuthorizationMembers().add(deviceGroupAuthorizationMemberPO);
			}			
			deviceGroupAuthorizationDao.save(authorizationPO);
		}
		
		//推送
		if(authorizationPO.getLiveChannels().size()>0 || authorizationPO.getAssets().size()>0){
			executeBusiness.execute(logic, "看会权限设置后推送：");
		}
		
		return authorizationPO;
	}
	
	/** @Title: 删除会议关联的无用的看会权限 <br/>
	 * @Description: 即没有关联任何直播、点播的权限<br/>
	 * @param groupUuid 设备组uuid
	 * 
	 */
	public void removeUnusefulAuthorizationByGroupUuid(String groupUuid){
		DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupUuid);
		if(authorizationPO != null){
			if(authorizationPO.getLiveChannels().size()==0 && authorizationPO.getAssets().size()==0){
				deviceGroupAuthorizationDao.delete(authorizationPO);
			}
		}
	}
	
}
