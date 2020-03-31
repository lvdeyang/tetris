package com.sumavision.tetris.bvc.cascade.bo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 指挥组|会议组<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月30日 下午1:50:01
 */
public class GroupBO {

	/** uuid */
	private String seq;
	
	/** 时间戳 */
	private String ts;
	
	/** 组id */
	private String gid;
	
	/** 操作用户id */
	private String op;
	
	/** 主题 */
	private String subject;
	
	/** 开始时间 格式 yyyy-MM-dd hh:mm:ss */
	private String stime;
	
	/** cnf会议  cmd指挥 */
	private String biztype;
	
	/** 会议、指挥名称 */
	private String bizname;
	
	/** 创建者id */
	private String creatorid;
	
	/** 指挥：最上级id，会议：主席id */
	private String topid;
	
	/** 会议模式，0表示主席模式、1表示讨论模式 */
	private String mode;
	
	/** ZH状态，0表示正常业务、1表示暂停业务 */
	/** 会议状态，0表示正常业务、1表示暂停 */
	private String status;
	
	/** 发言者ID */
	private String spkid;
	
	/** 成员id */
	private String mid;
	
	/** 响应，0表示不同意、1表示同意 */
	private String code;
	
	/** 入会成员列表 */
	private List<MinfoBO> mAddList;
	
	/** 成员列表 */
	private List<MinfoBO> mlist;
	
	/** 授权指挥 */
	private AuthCommandBO authitem;
	
	/** 接替指挥 */
	private ReplaceCommandBO replaceitem;
	
	/** 专向指挥列表 */
	private List<SecretCommandBO> secretlist;
	
	/** 协同指挥列表 */
	private List<String> croplist;
	
	/** 越级指挥列表 */
	private List<CrossCommandBO> croslist;
	
	/** 媒体转发对象列表 */
	private List<String> mediaForwardMlist;
	
	/** 转发媒体id列表 */
	private List<String> medialist;
	
	public GroupBO(){
		this.seq = UUID.randomUUID().toString().replace("-", "");
		this.ts = String.valueOf(new Date().getTime()).replace(",", "");
	}
	
	public String getSeq() {
		return seq;
	}

	public GroupBO setSeq(String seq) {
		this.seq = seq;
		return this;
	}

	public String getTs() {
		return ts;
	}

	public GroupBO setTs(String ts) {
		this.ts = ts;
		return this;
	}

	public String getGid() {
		return gid;
	}

	public GroupBO setGid(String gid) {
		this.gid = gid;
		return this;
	}

	public String getOp() {
		return op;
	}

	public GroupBO setOp(String op) {
		this.op = op;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public GroupBO setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getStime() {
		return stime;
	}

	public GroupBO setStime(String stime) {
		this.stime = stime;
		return this;
	}

	public String getBiztype() {
		return biztype;
	}

	public GroupBO setBiztype(String biztype) {
		this.biztype = biztype;
		return this;
	}

	public String getBizname() {
		return bizname;
	}

	public GroupBO setBizname(String bizname) {
		this.bizname = bizname;
		return this;
	}

	public String getCreatorid() {
		return creatorid;
	}

	public GroupBO setCreatorid(String creatorid) {
		this.creatorid = creatorid;
		return this;
	}

	public String getTopid() {
		return topid;
	}

	public GroupBO setTopid(String topid) {
		this.topid = topid;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public GroupBO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public GroupBO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getSpkid() {
		return spkid;
	}

	public GroupBO setSpkid(String spkid) {
		this.spkid = spkid;
		return this;
	}

	public String getMid() {
		return mid;
	}

	public GroupBO setMid(String mid) {
		this.mid = mid;
		return this;
	}

	public String getCode() {
		return code;
	}

	public GroupBO setCode(String code) {
		this.code = code;
		return this;
	}

	public List<MinfoBO> getmAddList() {
		return mAddList;
	}

	public GroupBO setmAddList(List<MinfoBO> mAddList) {
		this.mAddList = mAddList;
		return this;
	}

	public List<MinfoBO> getMlist() {
		return mlist;
	}

	public GroupBO setMlist(List<MinfoBO> mlist) {
		this.mlist = mlist;
		return this;
	}

	public AuthCommandBO getAuthitem() {
		return authitem;
	}

	public GroupBO setAuthitem(AuthCommandBO authitem) {
		this.authitem = authitem;
		return this;
	}

	public ReplaceCommandBO getReplaceitem() {
		return replaceitem;
	}

	public GroupBO setReplaceitem(ReplaceCommandBO replaceitem) {
		this.replaceitem = replaceitem;
		return this;
	}

	public List<SecretCommandBO> getSecretlist() {
		return secretlist;
	}

	public GroupBO setSecretlist(List<SecretCommandBO> secretlist) {
		this.secretlist = secretlist;
		return this;
	}

	public List<String> getCroplist() {
		return croplist;
	}

	public GroupBO setCroplist(List<String> croplist) {
		this.croplist = croplist;
		return this;
	}

	public List<CrossCommandBO> getCroslist() {
		return croslist;
	}

	public GroupBO setCroslist(List<CrossCommandBO> croslist) {
		this.croslist = croslist;
		return this;
	}

	public List<String> getMediaForwardMlist() {
		return mediaForwardMlist;
	}

	public GroupBO setMediaForwardMlist(List<String> mediaForwardMlist) {
		this.mediaForwardMlist = mediaForwardMlist;
		return this;
	}

	public List<String> getMedialist() {
		return medialist;
	}

	public GroupBO setMedialist(List<String> medialist) {
		this.medialist = medialist;
		return this;
	}
	
}
