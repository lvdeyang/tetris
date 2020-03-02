package com.sumavision.bvc.control.device.group.vo.tree;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.pojo.ChannelSchemePO.LockStatus;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.FolderPO;
import com.sumavision.bvc.command.group.basic.CommandGroupMemberPO;
import com.sumavision.bvc.command.group.basic.CommandGroupPO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeIcon;
import com.sumavision.bvc.control.device.group.vo.tree.enumeration.TreeNodeType;
import com.sumavision.bvc.control.device.monitor.vod.VodResourceVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.BundlePreviewBO;
import com.sumavision.bvc.device.group.bo.BundleScreenBO;
import com.sumavision.bvc.device.group.bo.ChannelBO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupBusinessRolePO;
import com.sumavision.bvc.device.group.po.DeviceGroupConfigVideoPO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.po.AuthorizationMemberPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
/**
 * 
* @ClassName: TreeNodeVO 
* @Description: 生成树结构
* @author xpz
* @date 2018年8月10日 上午11:24:41 
*
 */
public class TreeNodeVO {
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	/** 根目录节点id */
	public static final long FOLDERID_ROOT = -1l;
	
	/** 扩展节点id：拼接屏 */
	public static final long FOLDERID_COMBINEJV230 = -2l;
	
	/** 扩展节点id：发言人 */
	public static final long FOLDERID_SPOKESMAN = -3l;
	
	/** 扩展节点id：虚拟源 */
	public static final long FOLDERID_VIRTUALSOURCE = -4l;
	
	/** 扩展节点id：指挥*/
	public static final long FOLDERID_COMMAND= -5l;

	private String id;
	
	private String uuid;
	
	private String name;
	
	/** type@@id */
	private String key;
	
	private TreeNodeType type;
	
	private List<TreeNodeVO> children;
	
	/** TreeNodeType为BUNDLE时往里面加屏幕 */
	private List<TreeNodeVO> screens;
	
	/** jsonString 附加参数 */
	private String param;
	
	private String icon;
	
	//设备状态：bundle-online bundle-offline 用于作为前端的class
	private String bundleStatus;
	
	private String style;
	
	private String webUrl;
	
	private String layerWebUrl;

	public String getId() {
		return id;
	}

	public TreeNodeVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public TreeNodeVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getName() {
		return name;
	}

	public TreeNodeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getKey() {
		return key;
	}

	public TreeNodeVO setKey(){
		this.setKey(this.generateKey());
		return this;
	}
	public TreeNodeVO setKey(String key) {
		this.key = key;
		return this;
	}

	public TreeNodeType getType() {
		return type;
	}

	public TreeNodeVO setType(TreeNodeType type) {
		this.type = type;
		return this;
	}

	public List<TreeNodeVO> getChildren() {
		return children;
	}

	public TreeNodeVO setChildren(List<TreeNodeVO> children) {
		this.children = children;
		return this;
	}

	public List<TreeNodeVO> getScreens() {
		return screens;
	}

	public TreeNodeVO setScreens(List<TreeNodeVO> screens) {
		this.screens = screens;
		return this;
	}

	public String getParam() {
		return param;
	}

	public TreeNodeVO setParam(String param) {
		this.param = param;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public TreeNodeVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getBundleStatus() {
		return bundleStatus;
	}

	public void setBundleStatus(String bundleStatus) {
		this.bundleStatus = bundleStatus;
	}

	public String getStyle() {
		return style;
	}

	public TreeNodeVO setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public TreeNodeVO setWebUrl(String webUrl) {
		this.webUrl = webUrl;
		return this;
	}

	public String getLayerWebUrl() {
		return layerWebUrl;
	}

	public TreeNodeVO setLayerWebUrl(String layerWebUrl) {
		this.layerWebUrl = layerWebUrl;
		return this;
	}
	
	public String generateKey(){
		return new StringBufferWrapper().append(this.getType().toString())
										.append("@@")
										.append(this.getId())
										.toString();
	}
	
	/**
	 * 创建文件夹节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月14日 下午2:28:14
	 * @param folder 文件夹
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(FolderBO folder){
		this.setId(folder.getId().toString())
			.setName(folder.getName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("parentId", folder.getParentId())
														  .put("parentPath", folder.getParentPath())
														  .getMap()))
			.setType(TreeNodeType.FOLDER)
			.setIcon(TreeNodeIcon.FOLDER.getName())
			.setKey(this.generateKey());
			
		return this;
	}
	
	/**
	 * 创建点播资源节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午11:51:36
	 * @param VodResourceVO resource 点播资源
	 * @return TreeNodeVO 点播资源节点
	 */
	public TreeNodeVO set(VodResourceVO resource){
		this.setId(resource.getId().toString())
			.setName(resource.getName())
			.setParam(resource.getPreviewUrl())
			.setType(TreeNodeType.VOD_RESOURCE)
			.setIcon(TreeNodeIcon.VOD_RESOURCE.getName())
			.setKey(this.generateKey());
		return this;
	}
	
	/**
	 * 创建用户节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月3日 上午10:45:34
	 * @param UserBO user 用户数据
	 * @return TreeNodeVO 树节点
	 */
	@Deprecated
	public TreeNodeVO set(UserBO user){
		EncoderDecoderUserMap userMap = encoderDecoderUserMapDao.findByUserId(user.getId());
		return this.set(user, userMap);
//		this.setId(user.getId().toString())
//			.setName(user.getName())
//			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("userId", user.getId())
//																			.put("username", user.getName())
//																			.put("userno", user.getUserNo())
//																			.put("creater", user.getCreater())
//																			.put("encoderId", userMap==null?null:userMap.getEncodeBundleId())
//																			.put("decoderId", userMap==null?null:userMap.getDecodeBundleId())
//																		    .getMap()))
//			.setType(TreeNodeType.USER)
//			.setIcon(TreeNodeIcon.SPOKESMAN.getName())
//			.setKey(new StringBufferWrapper().append(this.generateKey()).append("@@").append(user.getCreater()).toString());
//		
//		if(user.isLogined()){
//			this.setStyle("color:#0dcc19;");
//			this.setBundleStatus("bundle-online");
//		}else{
//			this.setBundleStatus("bundle-offline");
//		}
//			
//		return this;
	}
	
	/**
	 * 创建用户节点<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月16日 下午5:19:33
	 * @param user 用户数据
	 * @param userMap 编解码器信息，有判空
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(UserBO user, EncoderDecoderUserMap userMap){
		this.setId(user.getId().toString())
			.setName(user.getName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("userId", user.getId())
																			.put("username", user.getName())
																			.put("userno", user.getUserNo())
																			.put("creater", user.getCreater())
																			.put("encoderId", userMap==null?null:userMap.getEncodeBundleId())
																			.put("decoderId", userMap==null?null:userMap.getDecodeBundleId())
																		    .getMap()))
			.setType(TreeNodeType.USER)
			.setIcon(TreeNodeIcon.SPOKESMAN.getName())
			.setKey(new StringBufferWrapper().append(this.generateKey()).append("@@").append(user.getCreater()).toString());
		
		if(user.isLogined()){
			this.setStyle("color:#0dcc19;");
			this.setBundleStatus("bundle-online");
		}else{
			this.setBundleStatus("bundle-offline");
		}
			
		return this;
	}
	
	/**
	 * 创建指挥成员节点<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月23日 上午10:45:34
	 * @param CommandGroupMemberPO member 指挥成员
	 * @param UserBO userBO 成员对应的用户，用于获得在线状态；null则按照离线处理
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(CommandGroupMemberPO member, UserBO userBO){
		this.setId(member.getUserId().toString())
			.setUuid(member.getUuid())
			.setName(member.getUserName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("id", member.getUserId())
																			.put("username", member.getUserName())
																			.put("status", "")
																		    .getMap()))
			.setType(TreeNodeType.USER)
			.setIcon(TreeNodeIcon.SPOKESMAN.getName());
//			.setKey(new StringBufferWrapper().append(this.generateKey()).append("@@").append(user.getCreater()).toString());
		
		//查询成员状态，添加样式等
		if(userBO!=null && userBO.isLogined()){
			this.setStyle("color:#0dcc19;");
			this.setBundleStatus("bundle-online");
		}else{
			this.setBundleStatus("bundle-offline");
		}
			
		return this;
	}
	
	/**
	 * 创建指挥成员节点<br/>
	 * <p>名称中添加信息：小明[xxx机构]，如果是主席，则：小明[主席][xxx机构]</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月21日 上午11:17:27
	 * @param member
	 * @param userBO
	 * @param folder
	 * @return
	 */
	public TreeNodeVO setWithInfo(CommandGroupMemberPO member, UserBO userBO, FolderPO folder){
		
		StringBufferWrapper name = new StringBufferWrapper().append(member.getUserName());
		if(member.isAdministrator()) name.append(" [主席]");
		if(folder != null) name.append(" [").append(folder.getName()).append("]");
		
		this.setId(member.getUserId().toString())
			.setUuid(member.getUuid())
			.setName(name.toString())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("id", member.getUserId())
																			.put("username", member.getUserName())
																			.put("status", "")
																		    .getMap()))
			.setType(TreeNodeType.USER)
			.setIcon(TreeNodeIcon.SPOKESMAN.getName());
//			.setKey(new StringBufferWrapper().append(this.generateKey()).append("@@").append(user.getCreater()).toString());
		
		//查询成员状态，添加样式等
		if(userBO!=null && userBO.isLogined()){
			this.setStyle("color:#0dcc19;");
			this.setBundleStatus("bundle-online");
		}else{
			this.setBundleStatus("bundle-offline");
		}
			
		return this;
	}
	
	/**
	 * 创建设备节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月14日 下午2:27:39
	 * @param bundle 设备
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(BundleBO bundle){
		this.setId(bundle.getBundleId())
			.setName(bundle.getName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("folderId", bundle.getFolderId())
																			.put("bundleId", bundle.getId())
																			.put("bundleName", bundle.getName())
																			.put("bundleType", bundle.getModel())
																			.put("nodeUid", bundle.getNodeUid())
																			.put("openAudio", bundle.isOpenAudio())
																			.put("memberId",bundle.getMemberId())
																			.put("venusBundleType", bundle.getType())
																			.put("realType", bundle.getRealType())
																		    .getMap()))
			.setType(TreeNodeType.BUNDLE)
			.setIcon(TreeNodeIcon.BUNDLE.getName())
			.setKey(this.generateKey())
			.setWebUrl(bundle.getWebUrl())
			.setLayerWebUrl(bundle.getLayerWebUrl());
		
		if(LockStatus.BUSY.equals(bundle.getLockStatus())){
			this.setStyle("color:#E6A23C;");
		}else{
			if("ONLINE".equals(bundle.getOnlineStatus())){
				this.setStyle("color:#0dcc19;");
			}
		}
		
		if(null != bundle.getOnlineStatus()){
			switch (bundle.getOnlineStatus()){
			case "ONLINE":
				this.setBundleStatus("bundle-online");
				
				if(null != bundle.getMemberStatus()){
					switch (bundle.getMemberStatus()){
					case CONNECT:
						this.setBundleStatus("bundle-connect");
						break;
					case DISCONNECT:
						this.setBundleStatus("bundle-disconnect");
						break;
					case CONNECTING:
						this.setBundleStatus("bundle-connecting");
						break;
					default:
						this.setBundleStatus("bundle-online");
						break;
					}
				}
				
				break;
			case "OFFLINE":
				this.setBundleStatus("bundle-offline");
				break;
			default:
				break;
			}			
		}
			
		return this;
	}
	
	/**
	 * 创建设备节点，带预览地址<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月9日 下午2:54:39
	 * @param bundle 设备带预览地址
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(BundlePreviewBO bundle){
		this.setId(bundle.getBundleId())
			.setName(bundle.getName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("folderId", bundle.getFolderId())
																			.put("bundleId", bundle.getId())
																			.put("bundleType", bundle.getModel())
																			.put("nodeUid", bundle.getNodeUid())
																			.put("openAudio", bundle.isOpenAudio())
																			.put("memberId",bundle.getMemberId())
																			.put("previewPlayUrl", bundle.getPreviewPlayUrl())
																		    .getMap()))
			.setType(TreeNodeType.BUNDLE)
			.setIcon(TreeNodeIcon.BUNDLE.getName())
			.setKey(this.generateKey());
			
		return this;
	}
	
	/**
	 * 创建设备节点<br/>
	 * @param bundle 设备
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(DeviceGroupMemberDTO bundle){
		this.setId(bundle.getBundleId())
			.setName(bundle.getBundleName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("folderId", bundle.getFolderId())
																			.put("bundleId", bundle.getBundleId())
																			.put("bundleType", bundle.getBundleType())
																			.put("nodeUid", bundle.getLayerId())
																			.put("openAudio", bundle.isOpenAudio())
																			.put("memberId", bundle.getId())
																			.put("roleId", bundle.getRoleId())
																			.put("roleName", bundle.getRoleName())
																		    .getMap()))
			.setType(TreeNodeType.BUNDLE)
			.setIcon(TreeNodeIcon.BUNDLE.getName())
			.setKey(this.generateKey());
			
		return this;
	}
	
	/**
	 * 创建设备编码通道节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月14日 下午2:26:59
	 * @param channel 编码通道
	 * @param bundle 隶属设备
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(ChannelBO channel, BundleBO bundle){
		this.setId(channel.getChannelId())
			.setName(channel.getName())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("bundleId", channel.getBundleId())
																			.put("bundleName", bundle.getName())
																			.put("bundleType", bundle.getType())
																			.put("channelId", channel.getChannelId())
																			.put("channelName", channel.getChannelName())
																			.put("channelType", channel.getChannelType())
																			.put("channelMemberId", channel.getChannelMemberId())
				    														.put("memberId", channel.getMemberId())
				    														.put("nodeUid", bundle.getNodeUid())
																			.getMap()))
			.setType(TreeNodeType.CHANNEL)
			.setIcon(TreeNodeIcon.CHANNEL.getName())
			.setKey(this.generateKey());
		
		if(LockStatus.BUSY.equals(bundle.getLockStatus())){
			this.setStyle("color:#E6A23C;");
		}else{
			if("ONLINE".equals(bundle.getOnlineStatus())){
				this.setStyle("color:#0dcc19;");
			}
		}
		
		return this;	
	}
	
	/**
	 * 创建设备屏幕节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月8日 下午2:26:59
	 * @param screen pingmu
	 * @param bundle 隶属设备
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(BundleScreenBO screen){
		this.setId(screen.getBundleId() + "_" + screen.getScreenId())
			.setName("屏幕" + (screen.getScreenId().split("_")[1] != ""?screen.getScreenId().split("_")[1]: screen.getScreenId()))
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("bundleId", screen.getBundleId())
																			.put("screenId", screen.getScreenId())
																			.put("rects", screen.getRects())
																			.getMap()))
			.setType(TreeNodeType.SCREEN)
			.setIcon(TreeNodeIcon.CHANNEL.getName())
			.setKey(this.generateKey());
		return this;	
	}
	
	/**
	 * 创建角色节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月14日 下午2:25:12
	 * @param entity 角色
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(DeviceGroupBusinessRolePO entity, String bundleName){
		String name = null;
		if(bundleName == null){
			name = entity.getName();
		}else{
			name = new StringBufferWrapper().append(entity.getName())
											.append("-")
											.append(bundleName)
											.toString();
		}
		this.setId(String.valueOf(entity.getId()))
			.setName(name)
			.setType(TreeNodeType.ROLE)
			.setIcon(BusinessRoleSpecial.CHAIRMAN.equals(entity.getSpecial())?TreeNodeIcon.CHAIRMAN.getName():TreeNodeIcon.SPOKESMAN.getName())
			.setKey(this.generateKey());
		return this;
	}
	
	/**
	 * 创建角色编码通道节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年10月14日 下午2:23:43
	 * @param entity 隶属角色
	 * @param type 编码通道类型
	 * @param name 编码通道别名
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(DeviceGroupBusinessRolePO entity, ChannelType type, String name){
		this.setId(type.toString())
			.setName(name)
			.setType(TreeNodeType.CHANNEL)
			.setIcon(TreeNodeIcon.CHANNEL.getName())
			.setKey(this.generateKey())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("roleName", entity.getName())
																			.put("roleId", entity.getId())
																			.put("type", type)
																			.put("channelType", "VenusVideoIn")
																			.getMap()));
		return this;
	}
	
	/**
	 * 创建虚拟源节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月16日 下午2:23:43
	 * @param entity 虚拟源来源
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(DeviceGroupConfigVideoPO entity){
		this.setId(entity.getId().toString())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setIcon(TreeNodeIcon.VIRTUAL.getName())
			.setType(TreeNodeType.VIRTUAL)
			.setKey(this.generateKey())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, Object>().put("videoUuid", entity.getUuid())
																			.put("videoName", entity.getName())
																			.getMap()));
		
		return this;
	}
	
	/**
	 * 将看会成员转换为树节点<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月2日 上午09:54:39
	 * @param member 看会成员PO
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(DeviceGroupAuthorizationMemberPO member){
		this.setId(member.getBundleId())
			.setType(TreeNodeType.BUNDLE)
			.setIcon(TreeNodeIcon.BUNDLE.getName())
			.setKey(this.generateKey());
			
		return this;
	}
	
	/**
	 * 将看会成员转换为树节点（系统）<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月8日 上午09:54:39
	 * @param member 看会成员PO
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(AuthorizationMemberPO member){
		this.setId(member.getBundleId())
			.setType(TreeNodeType.BUNDLE)
			.setIcon(TreeNodeIcon.BUNDLE.getName())
			.setKey(this.generateKey());
			
		return this;
	}
	
	/**
	 * 将指挥转换为树节点<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月15日 下午2:33:09
	 * @param CommandGroupPO command 指挥
	 * @return TreeNodeVO 树节点
	 */
	public TreeNodeVO set(CommandGroupPO command){
		this.setId(command.getId().toString())
			.setUuid(command.getUuid())
			.setName(command.getName())
			.setType(TreeNodeType.COMMAND)
			.setIcon(TreeNodeIcon.GROUP.getName())
			.setKey(this.generateKey())
			.setParam(JSON.toJSONString(new HashMapWrapper<String, String>().put("creator", command.getUserId().toString()).getMap()));
		if(GroupStatus.STOP.equals(command.getStatus())){
			this.setBundleStatus("bundle-offline");
		}else{
			this.setStyle("color:#0dcc19;");
			this.setBundleStatus("bundle-online");
		}
		
		return this;
	}
	
}
