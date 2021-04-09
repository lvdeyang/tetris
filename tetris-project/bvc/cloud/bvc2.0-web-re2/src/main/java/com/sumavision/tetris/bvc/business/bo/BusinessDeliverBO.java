package com.sumavision.tetris.bvc.business.bo;

import java.util.HashSet;
import java.util.Set;

import com.sumavision.bvc.device.group.bo.RecordSetBO;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.combine.audio.BusinessCombineAudioPO;
import com.sumavision.tetris.bvc.business.po.combine.video.BusinessCombineVideoPO;
import com.sumavision.tetris.bvc.business.po.combine.video.CombineTemplateGroupAgendeForwardPermissionPO;
import com.sumavision.tetris.bvc.page.PageTaskPO;

public class BusinessDeliverBO {
	
	private String userId = "-1";
	
	/** group用于获取参数模板等信息 */
	private GroupPO group;
	
	/** 暂时没有用 */
	private Set<CombineTemplateGroupAgendeForwardPermissionPO> usefulVideoPermissions = new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>();

	/** 标记不再有用的合屏关联关系，最后统一删除，并删除不再用的合屏。注：所有的关联关系都先放进来，后边再把有用的剔除 */
	private Set<CombineTemplateGroupAgendeForwardPermissionPO> unusefulVideoPermissions = new HashSet<CombineTemplateGroupAgendeForwardPermissionPO>();
	
	private Set<BusinessCombineVideoPO> startCombineVideos = new HashSet<BusinessCombineVideoPO>();
	
	private Set<BusinessCombineVideoPO> updateCombineVideos = new HashSet<BusinessCombineVideoPO>();
	
	private Set<BusinessCombineVideoPO> stopCombineVideos = new HashSet<BusinessCombineVideoPO>();
	
	private Set<BusinessCombineAudioPO> startCombineAudios = new HashSet<BusinessCombineAudioPO>();
	
	private Set<BusinessCombineAudioPO> updateCombineAudios = new HashSet<BusinessCombineAudioPO>();
	
	private Set<BusinessCombineAudioPO> stopCombineAudios = new HashSet<BusinessCombineAudioPO>();
	
	private Set<PageTaskPO> startPageTasks = new HashSet<PageTaskPO>();
	
	private Set<PageTaskPO> updatePageTasks = new HashSet<PageTaskPO>();
	
	private Set<PageTaskPO> stopPageTasks = new HashSet<PageTaskPO>();
	
	private Set<RecordSetBO> startRecordSets = new HashSet<RecordSetBO>();
	
	private Set<RecordSetBO> updateRecordSet = new HashSet<RecordSetBO>();
	
	private Set<RecordSetBO> stopRecordSets = new HashSet<RecordSetBO>();

	public String getUserId() {
		return userId;
	}

	public BusinessDeliverBO setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public GroupPO getGroup() {
		return group;
	}

	public BusinessDeliverBO setGroup(GroupPO group) {
		this.group = group;
		return this;
	}

	public Set<CombineTemplateGroupAgendeForwardPermissionPO> getUsefulVideoPermissions() {
		return usefulVideoPermissions;
	}

	public BusinessDeliverBO setUsefulVideoPermissions(Set<CombineTemplateGroupAgendeForwardPermissionPO> usefulVideoPermissions) {
		this.usefulVideoPermissions = usefulVideoPermissions;
		return this;
	}

	public Set<CombineTemplateGroupAgendeForwardPermissionPO> getUnusefulVideoPermissions() {
		return unusefulVideoPermissions;
	}

	public BusinessDeliverBO setUnusefulVideoPermissions(Set<CombineTemplateGroupAgendeForwardPermissionPO> unusefulVideoPermissions) {
		this.unusefulVideoPermissions = unusefulVideoPermissions;
		return this;
	}

	public Set<BusinessCombineVideoPO> getStartCombineVideos() {
		return startCombineVideos;
	}

	public BusinessDeliverBO setStartCombineVideos(Set<BusinessCombineVideoPO> startCombineVideos) {
		this.startCombineVideos = startCombineVideos;
		return this;
	}

	public Set<BusinessCombineVideoPO> getUpdateCombineVideos() {
		return updateCombineVideos;
	}

	public BusinessDeliverBO setUpdateCombineVideos(Set<BusinessCombineVideoPO> updateCombineVideos) {
		this.updateCombineVideos = updateCombineVideos;
		return this;
	}

	public Set<BusinessCombineVideoPO> getStopCombineVideos() {
		return stopCombineVideos;
	}

	public BusinessDeliverBO setStopCombineVideos(Set<BusinessCombineVideoPO> stopCombineVideos) {
		this.stopCombineVideos = stopCombineVideos;
		return this;
	}

	public Set<BusinessCombineAudioPO> getStartCombineAudios() {
		return startCombineAudios;
	}

	public BusinessDeliverBO setStartCombineAudios(Set<BusinessCombineAudioPO> startCombineAudios) {
		this.startCombineAudios = startCombineAudios;
		return this;
	}

	public Set<BusinessCombineAudioPO> getUpdateCombineAudios() {
		return updateCombineAudios;
	}

	public BusinessDeliverBO setUpdateCombineAudios(Set<BusinessCombineAudioPO> updateCombineAudios) {
		this.updateCombineAudios = updateCombineAudios;
		return this;
	}

	public Set<BusinessCombineAudioPO> getStopCombineAudios() {
		return stopCombineAudios;
	}

	public BusinessDeliverBO setStopCombineAudios(Set<BusinessCombineAudioPO> stopCombineAudios) {
		this.stopCombineAudios = stopCombineAudios;
		return this;
	}

	public Set<PageTaskPO> getStartPageTasks() {
		return startPageTasks;
	}

	public BusinessDeliverBO setStartPageTasks(Set<PageTaskPO> startPageTasks) {
		this.startPageTasks = startPageTasks;
		return this;
	}

	public Set<PageTaskPO> getUpdatePageTasks() {
		return updatePageTasks;
	}

	public BusinessDeliverBO setUpdatePageTasks(Set<PageTaskPO> updatePageTasks) {
		this.updatePageTasks = updatePageTasks;
		return this;
	}

	public Set<PageTaskPO> getStopPageTasks() {
		return stopPageTasks;
	}

	public BusinessDeliverBO setStopPageTasks(Set<PageTaskPO> stopPageTasks) {
		this.stopPageTasks = stopPageTasks;
		return this;
	}

	public Set<RecordSetBO> getStartRecordSets() {
		return startRecordSets;
	}

	public BusinessDeliverBO setStartRecordSets(Set<RecordSetBO> startRecordSets) {
		this.startRecordSets = startRecordSets;
		return this;
	}

	public Set<RecordSetBO> getUpdateRecordSet() {
		return updateRecordSet;
	}

	public BusinessDeliverBO setUpdateRecordSet(Set<RecordSetBO> updateRecordSet) {
		this.updateRecordSet = updateRecordSet;
		return this;
	}

	public Set<RecordSetBO> getStopRecordSets() {
		return stopRecordSets;
	}

	public BusinessDeliverBO setStopRecordSets(Set<RecordSetBO> stopRecordSets) {
		this.stopRecordSets = stopRecordSets;
		return this;
	}
	
}
