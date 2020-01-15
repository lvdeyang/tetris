package com.sumavision.bvc.control.device.group.vo;

import java.util.List;

import com.sumavision.bvc.control.system.vo.DictionaryVO;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.dto.DeviceGroupDTO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class DeviceGroupVO extends AbstractBaseVO<DeviceGroupVO, DeviceGroupDTO>{

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private String name;
	
	private Long userId;
	
	private String userName;
	
	private boolean record;
	
	private String transmissionMode;
	
	private String forwardMode;
	
	private String type;
	
	private String status;
	
	private String statusContent;
	
	private Long avtplId;

	private String avtplName;
	
	private Long systemTplId;
	
	private String systemTplName;
	
	private static int onlineCount;//在线成员总数
	
	private static int totalCount;//参会成员总数
	
	private String dicRegionId;
	
	private String dicRegionContent;
	
	private String dicProgramId;
	
	private String dicProgramContent;
	
	private String dicCategoryLiveId;
	
	private String dicCategoryLiveContent;
	
	private String dicStorageLocationCode;
	
	private String dicStorageLocationContent;

	private List<DictionaryVO> regions;
	
	public Long getId() {
		return id;
	}

	public DeviceGroupVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public DeviceGroupVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public DeviceGroupVO setUserName(String userName) {
		this.userName = userName;
		return this;
	}
	
	public boolean isRecord() {
		return record;
	}

	public DeviceGroupVO setRecord(boolean record) {
		this.record = record;
		return this;
	}

	public String getTransmissionMode() {
		return transmissionMode;
	}

	public DeviceGroupVO setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
		return this;
	}

	public String getForwardMode() {
		return forwardMode;
	}

	public DeviceGroupVO setForwardMode(String forwardMode) {
		this.forwardMode = forwardMode;
		return this;
	}

	public String getType() {
		return type;
	}

	public DeviceGroupVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public DeviceGroupVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public Long getAvtplId() {
		return avtplId;
	}

	public DeviceGroupVO setAvtplId(Long avtplId) {
		this.avtplId = avtplId;
		return this;
	}

	public String getAvtplName() {
		return avtplName;
	}

	public DeviceGroupVO setAvtplName(String avtplName) {
		this.avtplName = avtplName;
		return this;
	}

	public Long getSystemTplId() {
		return systemTplId;
	}

	public DeviceGroupVO setSystemTplId(Long systemTplId) {
		this.systemTplId = systemTplId;
		return this;
	}

	public String getSystemTplName() {
		return systemTplName;
	}

	public DeviceGroupVO setSystemTplName(String systemTplName) {
		this.systemTplName = systemTplName;
		return this;
	}

	public String getDicRegionId() {
		return dicRegionId;
	}

	public DeviceGroupVO setDicRegionId(String dicRegionId) {
		this.dicRegionId = dicRegionId;
		return this;
	}

	public String getDicRegionContent() {
		return dicRegionContent;
	}

	public DeviceGroupVO setDicRegionContent(String dicRegionContent) {
		this.dicRegionContent = dicRegionContent;
		return this;
	}

	public String getDicProgramId() {
		return dicProgramId;
	}

	public DeviceGroupVO setDicProgramId(String DicProgramId) {
		this.dicProgramId = DicProgramId;
		return this;
	}

	public String getDicProgramContent() {
		return dicProgramContent;
	}

	public DeviceGroupVO setDicProgramContent(String dicDicProgramContent) {
		this.dicProgramContent = dicDicProgramContent;
		return this;
	}

	public List<DictionaryVO> getRegions() {
		return regions;
	}

	public DeviceGroupVO setRegions(List<DictionaryVO> regions) {
		this.regions = regions;
		return this;
	}

	public String getDicCategoryLiveId() {
		return dicCategoryLiveId;
	}

	public DeviceGroupVO setDicCategoryLiveId(String dicCategoryLiveId) {
		this.dicCategoryLiveId = dicCategoryLiveId;
		return this;
	}

	public String getDicCategoryLiveContent() {
		return dicCategoryLiveContent;
	}

	public DeviceGroupVO setDicCategoryLiveContent(String dicCategoryLiveContent) {
		this.dicCategoryLiveContent = dicCategoryLiveContent;
		return this;
	}

	public String getStatusContent() {
		return statusContent;
	}

	public DeviceGroupVO setStatusContent(String statusContent) {
		this.statusContent = statusContent;
		return this;
	}

	public String getDicStorageLocationCode() {
		return dicStorageLocationCode;
	}

	public DeviceGroupVO setDicStorageLocationCode(String dicStorageLocationCode) {
		this.dicStorageLocationCode = dicStorageLocationCode;
		return this;
	}

	public String getDicStorageLocationContent() {
		return dicStorageLocationContent;
	}

	public DeviceGroupVO setDicStorageLocationContent(String dicStorageLocationContent) {
		this.dicStorageLocationContent = dicStorageLocationContent;
		return this;
	}

	public int getOnlineCount() {
		return onlineCount;
	}

	public DeviceGroupVO setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
		return this;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public DeviceGroupVO setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	@Override
	public DeviceGroupVO set(DeviceGroupDTO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setUserId(entity.getUserId())
			.setUserName(entity.getUserName())
			.setTransmissionMode(entity.getTransmissionMode().getName())
			.setForwardMode(entity.getForwardMode().getName())
			.setType(entity.getType().getName())
			.setStatus(entity.getStatus().toString())
			.setStatusContent(entity.getStatus().getName())
			.setAvtplId(entity.getAvtplId())
			.setAvtplName(entity.getAvtplName())
			.setSystemTplId(entity.getSystemTplId())
			.setSystemTplName(entity.getSystemTplName())
			.setDicRegionId(entity.getDicRegionId())
			.setDicRegionContent(entity.getDicRegionContent())
			.setDicProgramId(entity.getDicProgramId())
			.setDicProgramContent(entity.getDicProgramContent())
			.setDicCategoryLiveId(entity.getDicCategoryLiveId())
			.setDicCategoryLiveContent(entity.getDicCategoryLiveContent())
			.setDicStorageLocationCode(entity.getDicStorageLocationCode())
			.setDicStorageLocationContent(entity.getDicStorageLocationContent());
		return this;
	}
	
	/**
	 * @Title: PO转换VO方法，该方法不建议循环调用！ 
	 * @param entity 持久化数据
	 * @throws Exception
	 * @return DeviceGroupVO 视图数据
	 */
	public DeviceGroupVO set(DeviceGroupPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setUserId(entity.getUserId())
			.setUserName(entity.getUserName())
			.setRecord(entity.isRecord())
			.setTransmissionMode(entity.getTransmissionMode().getName())
			.setForwardMode(entity.getForwardMode().getName())
			.setType(entity.getType().getName())
			.setStatusContent(entity.getStatus().getName())
			.setStatus(entity.getStatus().toString())
			.setAvtplId(entity.getAvtpl().getId())
			.setAvtplName(entity.getAvtpl().getName())
			.setSystemTplId(entity.getSystemTplId())
			.setSystemTplName(entity.getSystemTplName())
			.setDicRegionId(entity.getDicRegionId())
			.setDicRegionContent(entity.getDicRegionContent())
			.setDicProgramId(entity.getDicProgramId())
			.setDicProgramContent(entity.getDicProgramContent())
			.setDicCategoryLiveId(entity.getDicCategoryLiveId())
			.setDicCategoryLiveContent(entity.getDicCategoryLiveContent())
			.setDicStorageLocationCode(entity.getDicStorageLocationCode())
			.setDicStorageLocationContent(entity.getDicStorageLocationContent());
		return this;
	}
	
	/**
	 * @Title: 只查会议本身信息，不要关联的 
	 * @param entity 持久化数据
	 * @throws Exception 
	 * @return DeviceGroupVO 视图数据
	 */
	public DeviceGroupVO setBasicInfo(DeviceGroupPO entity) throws Exception{
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setName(entity.getName())
			.setRecord(entity.isRecord())
			.setTransmissionMode(entity.getTransmissionMode().getName())
			.setType(entity.getType().getName())
			.setStatus(entity.getStatus().toString());
		return this;
	}
	
	public DeviceGroupVO setCount(List<BundleBO> bundles) {
		int onlineCount = BundleBO.countOnline(bundles);
		this.setOnlineCount(onlineCount)
			.setTotalCount(bundles.size());
		return this;
	}
	
}
