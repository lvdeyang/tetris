package com.sumavision.bvc.device.group.bo;

import java.util.Comparator;
import java.util.List;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.MemberStatus;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;

public class BundleBO {

	private String id;
	
	//资源名称
	private String name;
	
	//资源类型
	private String model;
	
	//venus类型，对应BundlePO的bundleType，如VenusTerminal
	private String type;
	
	//资源id
	private String bundleId;
	
	//所属接入层Uid
	private String nodeUid;
	
	//资源在线状态
	private String onlineStatus;
	
	//资源锁定状态
	private LockStatus lockStatus;
	
	//资源连接状态
	private MemberStatus memberStatus;
	
	//资源所在文件夹
	private Long folderId;
	
	private boolean openAudio;
	
	private Long memberId;
	
	//标识设备真实类型
	private String realType;
	
	private String deviceIp;
	
	private Integer devicePort;
	
	/**如jv210等*/
	private String deviceModel;
	
	private String layerIp;
	
	//注意判空
	private NodeType layerType;
	
	//------以下属性根据上述属性生成，通过generateExtraInfo()方法
	
	private String webUrl;
	
	private String layerWebUrl;
	
	/** 外域名称*/
	private String equipFactInfo;
	
	public static enum BundleRealType{
		XT("系统设备"),
		BVC("bvc设备");
		private String name;
		private BundleRealType(String name){
			this.name = name;
		}
	}
	
	
	public String getEquipFactInfo() {
		return equipFactInfo;
	}

	public BundleBO setEquipFactInfo(String equipFactInfo) {
		this.equipFactInfo = equipFactInfo;
		return this;
	}

	public Long getMemberId() {
		return memberId;
	}

	public BundleBO setMemberId(Long memberId) {
		this.memberId = memberId;
		return this;
	}

	public boolean isOpenAudio() {
		return openAudio;
	}

	public BundleBO setOpenAudio(boolean openAudio) {
		this.openAudio = openAudio;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public BundleBO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getId() {
		return id;
	}

	public BundleBO setId(String id) {
		this.id = id;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public BundleBO setName(String name) {
		this.name = name;
		return this;
	}

	public String getModel() {
		return model;
	}

	public BundleBO setModel(String model) {
		this.model = model;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public BundleBO setType(String type) {
		this.type = type;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public BundleBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getNodeUid() {
		return nodeUid;
	}

	public BundleBO setNodeUid(String nodeUid) {
		this.nodeUid = nodeUid;
		return this;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public BundleBO setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
		return this;
	}
	
	public LockStatus getLockStatus() {
		return lockStatus;
	}

	public BundleBO setLockStatus(LockStatus lockStatus) {
		this.lockStatus = lockStatus;
		return this;
	}

	public MemberStatus getMemberStatus() {
		return memberStatus;
	}

	public BundleBO setMemberStatus(MemberStatus memberStatus) {
		this.memberStatus = memberStatus;
		return this;
	}
	
	public String getRealType() {
		return realType;
	}

	public BundleBO setRealType(String realType) {
		this.realType = realType;
		return this;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public BundleBO setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
		return this;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public BundleBO setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
		return this;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public BundleBO setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
		return this;
	}

	public String getLayerIp() {
		return layerIp;
	}

	public BundleBO setLayerIp(String layerIp) {
		this.layerIp = layerIp;
		return this;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public BundleBO setWebUrl(String webUrl) {
		this.webUrl = webUrl;
		return this;
	}

	public String getLayerWebUrl() {
		return layerWebUrl;
	}

	public BundleBO setLayerWebUrl(String layerWebUrl) {
		this.layerWebUrl = layerWebUrl;
		return this;
	}

	public NodeType getLayerType() {
		return layerType;
	}

	public void setLayerType(NodeType layerType) {
		this.layerType = layerType;
	}

	/**
	 * @Title: 从设备组成员中复制数据<br/> 
	 * @param member 设备组成员
	 * @return BundleBO 业务数据 
	 */
	public BundleBO set(DeviceGroupMemberPO member){
		this.setId(member.getBundleId())
			.setBundleId(member.getBundleId())
			.setName(member.getBundleName())
			.setFolderId(member.getFolderId())
			.setNodeUid(member.getLayerId())
			.setOpenAudio(member.isOpenAudio())
			.setModel(member.getBundleType())
			.setMemberId(member.getId())
			.setMemberStatus(member.getMemberStatus())
			.setType(member.getVenusBundleType());
		return this;
	}
	
	/**
	 * @Title: 从设备组成员转换对象中复制数据<br/>
	 * @param member 设备组成员
	 * @return BundleBO 业务数据
	 */
	public BundleBO set(DeviceGroupMemberDTO member){
		this.setId(member.getBundleId())
			.setBundleId(member.getBundleId())
			.setModel(member.getBundleType())
			.setName(member.getBundleName())
			.setFolderId(member.getFolderId())
			.setNodeUid(member.getLayerId())
			.setOpenAudio(member.isOpenAudio())
			.setMemberId(member.getId())
			.setType(member.getVenusBundleType());
		
		return this;
	}
	
	/**
	 * @Title: 从持久化对象中复制数据<br/> 
	 * @param entity 持久化数据
	 * @return BundleBO 业务数据
	 */
	public BundleBO set(BundlePO entity){
		this.setBundleId(entity.getBundleId())
			.setName(entity.getBundleName())
			.setId(entity.getBundleId())
			.setNodeUid(entity.getAccessNodeUid())
			.setFolderId(entity.getFolderId())
			.setType(entity.getBundleType());
		return this;
	}
	
	/**
	 * @ClassName: 生成设备网管地址和接入层网管地址<br/> 
	 * @author zsy
	 * @date 2019年8月16日 上午9:17:10 
	 */
	public void generateExtraInfo(){
		
		String deviceIp = this.getDeviceIp();
		Integer devicePort = this.getDevicePort();
		String deviceModel = this.getDeviceModel();//如jv210等
		String layerIp = this.getLayerIp();
		NodeType layerType = this.getLayerType();
		
		String webUrl = null;
		String layerWebUrl = null;
		if(null != deviceIp && !"".equals(deviceIp)){
			if(null!=devicePort && devicePort.equals(5064)){
				webUrl = "https://" + deviceIp + ":8004/mainframe.asp";
			}else if("jv210".equals(deviceModel)){
				webUrl = "http://" + deviceIp;
			}
		}
		if(null != layerIp && !"".equals(layerIp)){
			if(NodeType.ACCESS_JV210.equals(layerType)){
				layerWebUrl = "https://" + layerIp + ":8007/action/get_monitor";
			}else if(NodeType.ACCESS_TVOS.equals(layerType)){
				layerWebUrl = "http://" + layerIp + ":8009/action/get_monitor";
			}
		}
		this.setWebUrl(webUrl).setLayerWebUrl(layerWebUrl);
		
	}
	
	public static int countOnline(List<BundleBO> bundleBOs){
		int onlineCount = 0;
		for(BundleBO bundleBO : bundleBOs){
			if(ONLINE_STATUS.ONLINE.toString().equals(bundleBO.getOnlineStatus())){
				onlineCount++;
			}			
		}
		return onlineCount;
	}

	/**
	 * @ClassName: member排序，根据bundleId，按照编号从小到大排列<br/> 
	 * @author wjw
	 * @date 2018年9月25日 上午13:36:10 
	 */
	public static final class BundleIdComparator implements Comparator<BundleBO>{

		@Override
		public int compare(BundleBO o1, BundleBO o2) {
			
			String str1 = o1.getName();
			String str2 = o2.getName();
			
			if(str1.compareTo(str2) < 0){
				return -1;
			}
			
			if(str1.compareTo(str2) > 0){
				return 1;
			}
			
			return 0;
		}		
	}
	
	/**
	 * @ClassName: member排序，根据onlineStatus，online的在前<br/> 
	 * @author zsy
	 * @date 2019年7月17日 上午9:17:10 
	 */
	public static final class BundleStatusComparator implements Comparator<BundleBO>{

		@Override
		public int compare(BundleBO o1, BundleBO o2) {
			
			String str1 = o1.getOnlineStatus();
			String str2 = o2.getOnlineStatus();
			
			if("OFFLINE".equals(str1) && "OFFLINE".equals(str2)){
				return 0;
			}else if("OFFLINE".equals(str1)){
				return 1;
			}else if("OFFLINE".equals(str2)){
				return -1;
			}
			
			MemberStatus s1 = o1.getMemberStatus();
			MemberStatus s2 = o2.getMemberStatus();
			
			if(MemberStatus.CONNECT.equals(s1) && MemberStatus.CONNECT.equals(s2)){
				return 0;
			}else if(MemberStatus.CONNECT.equals(s1)){
				return -1;
			}else if(MemberStatus.CONNECT.equals(s2)){
				return 1;
			}
			
			if(MemberStatus.CONNECTING.equals(s1) && MemberStatus.CONNECTING.equals(s2)){
				return 0;
			}else if(MemberStatus.CONNECTING.equals(s1)){
				return -1;
			}else if(MemberStatus.CONNECTING.equals(s2)){
				return 1;
			}
			
			if(MemberStatus.DISCONNECT.equals(s1) && MemberStatus.DISCONNECT.equals(s2)){
				return 0;
			}else if(MemberStatus.DISCONNECT.equals(s1)){
				return -1;
			}else if(MemberStatus.DISCONNECT.equals(s2)){
				return 1;
			}
			
			return 0;
		}
	}
}
