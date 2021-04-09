package com.sumavision.bvc.common.group.po;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sumavision.bvc.device.group.enumeration.ForwardMode;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.enumeration.TransmissionMode;
import com.sumavision.bvc.device.jv230.po.CombineJv230PO;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 设备组<br/>
 * @Description: 业务中把监控室和会议室归一<br/>
 * @author lvdeyang 
 * @date 2018年7月31日 下午1:26:13 
 */
@Entity
@Table(name="BVC_COMMON_GROUP")
public class CommonGroupPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/*****************
	 * 以下为会议业务数据
	 *****************/
	
	/** 组名称 */
	private String name;
	
	/** 创建用户id */
	private Long userId;
	
	/** 创建用户名 */
	private String userName;
	
	/** 创建时间 */
	private Date createtime;
	
	/** 是否正在录制 */
	private boolean record;
	
	/** 设备组全局音频 */
	private int volume = 100;
	
	/** 发流方式：单播或组播 */
	private TransmissionMode transmissionMode;
	
	/** 会议中角色转发模式 */
	private ForwardMode forwardMode;
	
	/** 组类型 */
	private GroupType type;
	
	/** 组状态 */
	private GroupStatus status;
	
	/** 当前参数档位 */
	private GearsLevel currentGearLevel;
	
	/** 会议成员 */
	private Set<CommonMemberPO> members;
	
	/** 会议转发 */
	private Set<CommonChannelForwardPO> forwards;
	
	/** 会议中的混音 */
	private Set<CommonCombineAudioPO> combineAudios;
	
	/** 会议中的合屏 */
	private Set<CommonCombineVideoPO> combineVideos;
	
	/** 会议中的配置 */
	private Set<CommonConfigPO> configs;
	
//	/** 会议中的录制 */
//	private Set<RecordPO> records;
	
	/** 会议中的拼接屏 */
	private Set<CombineJv230PO> combineJv230s;
	
	/*****************
	 * 以下为会议资源数据
	 *****************/
	
	/** 参数方案 */
	private CommonAvtplPO avtpl;
	
	/** 关联的业务角色 */
	private Set<CommonBusinessRolePO> roles;
	
	/** 关联的屏幕布局 */
//	private Set<DeviceGroupScreenLayoutPO> layouts;
	
	/** 关联的录制方案 */
//	private Set<DeviceGroupRecordSchemePO> recordSchemes;
	
	/** 关联系统级模板id */
	private Long systemTplId;
	
	/** 关联系统级模板名称 */
	private String systemTplName;

	/** 关联字典模板地区的boId，逗号分隔的多个*/
	private String dicRegionId;
	
	/** 关联字典模板地区名称，逗号分隔的多个 */
	private String dicRegionContent;

	/** 关联字典模板点播二级栏目的boId */
	private String dicProgramId;
	
	/** 关联字典模板点播二级栏目名称 */
	private String dicProgramContent;

	/** 关联字典模板直播栏目的boId */
	private String dicCategoryLiveId;
	
	/** 关联字典模板直播栏目名称 */
	private String dicCategoryLiveContent;

	/** 关联字典模板存储位置的code */
	private String dicStorageLocationCode;
	
	/** 关联字典模板存储位置的名称 */
	private String dicStorageLocationContent;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "RECORD")
	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	@Column(name = "VOLUME")
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TRANSMISSION_MODE")
	public TransmissionMode getTransmissionMode() {
		return transmissionMode;
	}

	public void setTransmissionMode(TransmissionMode transmissionMode) {
		this.transmissionMode = transmissionMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "FORWARD_MODE")
	public ForwardMode getForwardMode() {
		return forwardMode;
	}

	public void setForwardMode(ForwardMode forwardMode) {
		this.forwardMode = forwardMode;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public GroupStatus getStatus() {
		return status;
	}

	public void setStatus(GroupStatus status) {
		this.status = status;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "CURRENT_GEAR_LEVEL")
	public GearsLevel getCurrentGearLevel() {
		return currentGearLevel;
	}

	public void setCurrentGearLevel(GearsLevel currentGearLevel) {
		this.currentGearLevel = currentGearLevel;
	}

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonMemberPO> getMembers() {
		return members;
	}

	public void setMembers(Set<CommonMemberPO> members) {
		this.members = members;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonChannelForwardPO> getForwards() {
		return forwards;
	}

	public void setForwards(Set<CommonChannelForwardPO> forwards) {
		this.forwards = forwards;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonCombineAudioPO> getCombineAudios() {
		return combineAudios;
	}

	public void setCombineAudios(Set<CommonCombineAudioPO> combineAudios) {
		this.combineAudios = combineAudios;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonCombineVideoPO> getCombineVideos() {
		return combineVideos;
	}

	public void setCombineVideos(Set<CommonCombineVideoPO> combineVideos) {
		this.combineVideos = combineVideos;
	}
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonConfigPO> getConfigs() {
		return configs;
	}

	public void setConfigs(Set<CommonConfigPO> configs) {
		this.configs = configs;
	}
	
//	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	public Set<RecordPO> getRecords() {
//		return records;
//	}
//
//	public void setRecords(Set<RecordPO> records) {
//		this.records = records;
//	}
	
	@Transient
	public Set<CombineJv230PO> getCombineJv230s() {
		return combineJv230s;
	}

	public void setCombineJv230s(Set<CombineJv230PO> combineJv230s) {
		this.combineJv230s = combineJv230s;
	}

	@OneToOne(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	public CommonAvtplPO getAvtpl() {
		return avtpl;
	}

	public void setAvtpl(CommonAvtplPO avtpl) {
		this.avtpl = avtpl;
	}

	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<CommonBusinessRolePO> getRoles() {
		return roles;
	}

	public void setRoles(Set<CommonBusinessRolePO> roles) {
		this.roles = roles;
	}

//	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	public Set<DeviceGroupScreenLayoutPO> getLayouts() {
//		return layouts;
//	}
//
//	public void setLayouts(Set<DeviceGroupScreenLayoutPO> layouts) {
//		this.layouts = layouts;
//	}

//	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	public Set<DeviceGroupRecordSchemePO> getRecordSchemes() {
//		return recordSchemes;
//	}
//
//	public void setRecordSchemes(Set<DeviceGroupRecordSchemePO> recordSchemes) {
//		this.recordSchemes = recordSchemes;
//	}

	@Column(name = "SYSTEM_TPL_ID")
	public Long getSystemTplId() {
		return systemTplId;
	}

	public void setSystemTplId(Long systemTplId) {
		this.systemTplId = systemTplId;
	}

	@Column(name = "SYSTEM_TPL_NAME")
	public String getSystemTplName() {
		return systemTplName;
	}

	public void setSystemTplName(String systemTplName) {
		this.systemTplName = systemTplName;
	}

	@Column(name = "DIC_REGION_ID", length = 2048)
	public String getDicRegionId() {
		return dicRegionId;
	}

	public void setDicRegionId(String dicRegionId) {
		this.dicRegionId = dicRegionId;
	}

	@Column(name = "DIC_REGION_CONTENT", length = 1024)
	public String getDicRegionContent() {
		return dicRegionContent;
	}

	public void setDicRegionContent(String dicRegionContent) {
		this.dicRegionContent = dicRegionContent;
	}

	@Column(name = "DIC_PROGRAM_ID")
	public String getDicProgramId() {
		return dicProgramId;
	}

	public void setDicProgramId(String dicProgramId) {
		this.dicProgramId = dicProgramId;
	}

	@Column(name = "DIC_PROGRAM_CONTENT")
	public String getDicProgramContent() {
		return dicProgramContent;
	}

	public void setDicProgramContent(String dicProgramContent) {
		this.dicProgramContent = dicProgramContent;
	}

	@Column(name = "DIC_CATEGORY_LIVE_ID")
	public String getDicCategoryLiveId() {
		return dicCategoryLiveId;
	}

	public void setDicCategoryLiveId(String dicCategoryLiveId) {
		this.dicCategoryLiveId = dicCategoryLiveId;
	}

	@Column(name = "DIC_CATEGORY_LIVE_CONTENT")
	public String getDicCategoryLiveContent() {
		return dicCategoryLiveContent;
	}

	public void setDicCategoryLiveContent(String dicCategoryLiveContent) {
		this.dicCategoryLiveContent = dicCategoryLiveContent;
	}

	@Column(name = "DIC_STORAGE_LOCATION_CODE")
	public String getDicStorageLocationCode() {
		return dicStorageLocationCode;
	}

	public void setDicStorageLocationCode(String dicStorageLocationCode) {
		this.dicStorageLocationCode = dicStorageLocationCode;
	}

	@Column(name = "DIC_STORAGE_LOCATION_CONTENT")
	public String getDicStorageLocationContent() {
		return dicStorageLocationContent;
	}

	public void setDicStorageLocationContent(String dicStorageLocationContent) {
		this.dicStorageLocationContent = dicStorageLocationContent;
	}
	
	/** 是否有正在进行的直播发布(rtmp) **/
//	public boolean hasRunningPublishStream() {
//		Set<RecordPO> recordPOs = this.getRecords();
//		if(null == recordPOs) return false;
//		for(RecordPO recordPO : recordPOs){
//			if(RecordType.PUBLISH.equals(recordPO.getType()) && recordPO.isRun()){
//				return true;
//			}
//		}
//		return false;
//	}
	
}
