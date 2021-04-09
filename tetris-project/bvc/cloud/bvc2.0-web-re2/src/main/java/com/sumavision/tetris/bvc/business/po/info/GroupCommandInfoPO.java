package com.sumavision.tetris.bvc.business.po.info;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 业务组的额外信息<br/>
 * <p>专向指挥、越级指挥等</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月19日 下午3:47:20
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_COMMAND_INFO")
public class GroupCommandInfoPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private Long groupId;
	
	//-------专向指挥信息--------
	
	private boolean hasSecret = false;
	
	/** 专向指挥转发id*/
	private String secretAgendaForwardIds;
	
	private Long secretHighMemberId;
	
	private Long secretLowMemberId;
	
	//-------专向指挥信息 结束--------
	
	//-------越级指挥--------
	
	private boolean hasCross = false;
	
	/** 越级指挥转发id*/
	private String crossAgendaForwardIds;
	
	private List<GroupCommandCrossPO> crosses;
	
	//-------协同指挥--------
	
	private boolean hasCooperate = false;
	
	/** 协同指挥转发id*/
	private String cooperateAgendaForwardIds;
	
	private List<GroupCommandCooperatePO> cooperates;
	
	//-------接替指挥--------
	
	private boolean hasReplace = false;
	
	private List<GroupCommandReplacePO> replaces;
	
	//-------授权指挥--------
	
	private boolean hasAuthorize = false;
	
	private List<GroupCommandAuthorizePO> authorizes;


	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getCooperateAgendaForwardIds() {
		return cooperateAgendaForwardIds;
	}

	public void setCooperateAgendaForwardIds(String cooperateAgendaForwardIds) {
		this.cooperateAgendaForwardIds = cooperateAgendaForwardIds;
	}

	public boolean isHasSecret() {
		return hasSecret;
	}

	public void setHasSecret(boolean hasSecret) {
		this.hasSecret = hasSecret;
	}

	public String getSecretAgendaForwardIds() {
		return secretAgendaForwardIds;
	}

	public void setSecretAgendaForwardIds(String secretAgendaForwardIds) {
		this.secretAgendaForwardIds = secretAgendaForwardIds;
	}

	public Long getSecretHighMemberId() {
		return secretHighMemberId;
	}

	public void setSecretHighMemberId(Long secretHighMemberId) {
		this.secretHighMemberId = secretHighMemberId;
	}
	
	public String getCrossAgendaForwardIds() {
		return crossAgendaForwardIds;
	}

	public void setCrossAgendaForwardIds(String crossAgendaForwardIds) {
		this.crossAgendaForwardIds = crossAgendaForwardIds;
	}

	public Long getSecretLowMemberId() {
		return secretLowMemberId;
	}

	public void setSecretLowMemberId(Long secretLowMemberId) {
		this.secretLowMemberId = secretLowMemberId;
	}
	
	public boolean isHasCross() {
		return hasCross;
	}

	public void setHasCross(boolean hasCross) {
		this.hasCross = hasCross;
	}

	@OneToMany(mappedBy = "commandInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GroupCommandCrossPO> getCrosses() {
		return crosses;
	}

	public void setCrosses(List<GroupCommandCrossPO> crosses) {
		this.crosses = crosses;
	}

	public boolean isHasCooperate() {
		return hasCooperate;
	}

	public void setHasCooperate(boolean hasCooperate) {
		this.hasCooperate = hasCooperate;
	}

	@OneToMany(mappedBy = "commandInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GroupCommandCooperatePO> getCooperates() {
		return cooperates;
	}

	public void setCooperates(List<GroupCommandCooperatePO> cooperates) {
		this.cooperates = cooperates;
	}

	public boolean isHasReplace() {
		return hasReplace;
	}

	public void setHasReplace(boolean hasReplace) {
		this.hasReplace = hasReplace;
	}

	@OneToMany(mappedBy = "commandInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GroupCommandReplacePO> getReplaces() {
		return replaces;
	}

	public void setReplaces(List<GroupCommandReplacePO> replaces) {
		this.replaces = replaces;
	}

	public boolean isHasAuthorize() {
		return hasAuthorize;
	}

	public void setHasAuthorize(boolean hasAuthorize) {
		this.hasAuthorize = hasAuthorize;
	}

	@OneToMany(mappedBy = "commandInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<GroupCommandAuthorizePO> getAuthorizes() {
		return authorizes;
	}

	public void setAuthorizes(List<GroupCommandAuthorizePO> authorizes) {
		this.authorizes = authorizes;
	}

	/** 清除全部信息。停会时调用 */
	public void clearAll(){
		this.clearCooperate();
		this.clearSecret();
		this.clearCross();
		this.clearReplace();
		this.clearAuthorizes();
	}
	
	/** 清除专向指挥信息 */
	public void clearSecret(){
		this.hasSecret = false;
		this.secretAgendaForwardIds = "";
		this.secretHighMemberId = null;
		this.secretLowMemberId = null;
	}
	
	/** 清除越级指挥信息 */
	public void clearCross(){
		this.hasCross = false;
		this.crossAgendaForwardIds = "";
		if(this.crosses != null) this.crosses.clear();
	}
	
	/** 清除协同指挥信息 */
	public void clearCooperate(){
		this.hasCooperate = false;
		this.cooperateAgendaForwardIds = "";
		if(this.cooperates != null) this.cooperates.clear();
	}
	
	/** 清除接替指挥信息 */
	public void clearReplace(){
		this.hasReplace = false;
		if(this.replaces != null) this.replaces.clear();
	}
	
	/** 清除授权指挥信息 */
	public void clearAuthorizes(){
		this.hasAuthorize = false;
		if(this.authorizes != null) this.authorizes.clear();
	}

	public GroupCommandInfoPO(){
		
	}
	
	public GroupCommandInfoPO(Long groupId){
		this.groupId = groupId;
	}
	
	public void overWriteCooperates(List<GroupCommandCooperatePO> groupCommandCooperates){
		if(this.getCooperates() == null){
			this.setCooperates(groupCommandCooperates);
		}else{
			this.getCooperates().addAll(groupCommandCooperates);
		}
	}
	
	public void overWriteCross(List<GroupCommandCrossPO> groupCommandCrossPos){
		if(this.getCooperates() == null){
			this.setCrosses(groupCommandCrossPos);
		}else{
			this.getCrosses().addAll(groupCommandCrossPos);
		}
	}
	
}
