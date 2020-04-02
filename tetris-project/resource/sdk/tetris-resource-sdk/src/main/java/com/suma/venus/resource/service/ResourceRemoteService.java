package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.bo.DeviceInfoBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.lianwang.status.DeviceStatusXML;
import com.suma.venus.resource.lianwang.status.NotifyRouteLinkXml;
import com.suma.venus.resource.lianwang.status.NotifyUserDeviceXML;
import com.suma.venus.resource.lianwang.status.StatusXMLUtil;
import com.suma.venus.resource.lianwang.status.UserStatusXML;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.util.XMLBeanUtils;

/**
 * 联网新功能相关处理Service<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月31日 下午3:37:57
 */
@Service
public class ResourceRemoteService {
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private StatusXMLUtil statusXMLUtil;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private SerInfoAndNodeService serInfoAndNodeService;

	public String queryLocalLayerId() throws Exception{
		
		return "lvdeyang";
	}
	
	public List<String> querySerNodeList(List<DeviceInfoBO> devices) throws Exception{
		
		return null;
	}	
	
	/**
	 * 联网发送信息同步命令信息处理<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月1日 上午10:56:13
	 * @param requestJson
	 */
	public void notifyXml(String cmd, String xml) throws Exception{

		// 获取联网发来的notify消息
		if ("syncinfo".equals(cmd)) {
			NotifyUserDeviceXML xmlBean = XMLBeanUtils.xmlToBean(xml, NotifyUserDeviceXML.class);
			List<DeviceStatusXML> devlist = xmlBean.getDevlist();
			List<BundlePO> toUpdateBundles = new ArrayList<BundlePO>();
			// 更新设备在线状态
			if (null != devlist) {
				for (DeviceStatusXML dev : devlist) {
					BundlePO bundle = bundleDao.findByUsername(dev.getDevid());
					if (null != bundle) {
						ONLINE_STATUS status = dev.getStatus() == 1 ? ONLINE_STATUS.ONLINE
								: ONLINE_STATUS.OFFLINE;
						if (status != bundle.getOnlineStatus()) {
							bundle.setOnlineStatus(status);
							toUpdateBundles.add(bundle);
						}
					}
				}
			}
			if (!toUpdateBundles.isEmpty()) {
				bundleDao.save(toUpdateBundles);
			}

			// 更新对应用户的在线状态
			List<UserStatusXML> userlist = xmlBean.getUserlist();
			List<UserBO> userBOs = new ArrayList<UserBO>();
			if (null != userlist) {
				for (UserStatusXML userXml : userlist) {
					userBOs.add(statusXMLUtil.toUserBO(userXml));
				}
			}
			if (!userBOs.isEmpty()) {
				//TODO:更新用户状态--现在先放在folderUserMap里面
				List<String> userNos = new ArrayList<String>();
				for(UserBO userBO: userBOs){
					userNos.add(userBO.getUserNo());
				}
				List<FolderUserMap> maps = folderUserMapDao.findByUserNoIn(userNos);
				for(UserBO userBO: userBOs){
					for(FolderUserMap map: maps){
						if(map.getUserNo().equals(userBO.getUserNo())){
							map.setUserStatus(userBO.isLogined());
							break;
						}
					}
				}
				folderUserMapDao.save(maps);
			}
		}

		if ("syncroutelink".equals(cmd)) {
			NotifyRouteLinkXml xmlBean = XMLBeanUtils.xmlToBean(xml, NotifyRouteLinkXml.class);
			//更新路由
			serInfoAndNodeService.updateSerNode(xmlBean);
		}

	}
	
}
