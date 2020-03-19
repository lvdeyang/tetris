package com.sumavision.tetris.zoom;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;
import com.sumavision.tetris.zoom.webrtc.WebRtcRoomInfoVO;
import com.sumavision.tetris.zoom.webrtc.WebRtcVO;

public class ZoomVO extends AbstractBaseVO<ZoomVO, ZoomPO>{

	/** 会议名称 */
	private String name;
	
	/** 会议号码 */
	private String code;
	
	/** 会议状态 */
	private String status;
	
	/** 保密等级 */
	private String secretLevel;

	/** 会议模式 */
	private String mode;
	
	/** 创建者用户id */
	private Long creatorUserId;
	
	/** 创建者用户昵称 */
	private String creatorUserNickname;
	
	/** webrtc信息 */
	private WebRtcVO webRtc;
	
	/** 自己 */
	private ZoomMemberVO me;
	
	/** 会议主席 */
	private ZoomMemberVO chairman;
	
	/** 发言人列表 */
	private List<ZoomMemberVO> spokesmem;
	
	/** 成员总数 */
	private Long totalMembers;
	
	/** 成员列表 */
	private List<ZoomMemberVO> members;
	
	public String getName() {
		return name;
	}

	public ZoomVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getCode() {
		return code;
	}

	public ZoomVO setCode(String code) {
		this.code = code;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ZoomVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getSecretLevel() {
		return secretLevel;
	}

	public ZoomVO setSecretLevel(String secretLevel) {
		this.secretLevel = secretLevel;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public ZoomVO setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public Long getCreatorUserId() {
		return creatorUserId;
	}

	public ZoomVO setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
		return this;
	}

	public String getCreatorUserNickname() {
		return creatorUserNickname;
	}

	public ZoomVO setCreatorUserNickname(String creatorUserNickname) {
		this.creatorUserNickname = creatorUserNickname;
		return this;
	}
	
	public WebRtcVO getWebRtc() {
		return webRtc;
	}

	public ZoomVO setWebRtc(WebRtcVO webRtc) {
		this.webRtc = webRtc;
		return this;
	}
	
	public ZoomVO setWebRtc(WebRtcRoomInfoVO webRtcRoomInfo) {
		this.webRtc = webRtcRoomInfo.transform();
		return this;
	}

	public ZoomMemberVO getMe() {
		return me;
	}

	public ZoomVO setMe(ZoomMemberVO me) {
		this.me = me;
		return this;
	}

	public ZoomMemberVO getChairman() {
		return chairman;
	}

	public ZoomVO setChairman(ZoomMemberVO chairman) {
		this.chairman = chairman;
		return this;
	}

	public List<ZoomMemberVO> getSpokesmem() {
		return spokesmem;
	}

	public ZoomVO setSpokesmem(List<ZoomMemberVO> spokesmem) {
		this.spokesmem = spokesmem;
		return this;
	}
	
	public Long getTotalMembers() {
		return totalMembers;
	}

	public ZoomVO setTotalMembers(Long totalMembers) {
		this.totalMembers = totalMembers;
		return this;
	}

	public ZoomVO addSpokesman(ZoomMemberVO spokesmem) {
		if(this.getSpokesmem() == null) this.setSpokesmem(new ArrayList<ZoomMemberVO>());
		this.getSpokesmem().add(spokesmem);
		return this;
	}

	public List<ZoomMemberVO> getMembers() {
		return members;
	}

	public ZoomVO setMembers(List<ZoomMemberVO> members) {
		this.members = members;
		return this;
	}

	public ZoomVO addMember(ZoomMemberVO member) {
		if(this.getMembers() == null) this.setMembers(new ArrayList<ZoomMemberVO>());
		this.getMembers().add(member);
		return this;
	}
	
	@Override
	public ZoomVO set(ZoomPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setCode(entity.getCode())
			.setStatus(entity.getStatus().toString())
			.setSecretLevel(entity.getSecretLevel().toString())
			.setMode(entity.getMode().toString())
			.setCreatorUserId(entity.getCreatorUserId())
			.setCreatorUserNickname(entity.getCreatorUserNickname());
		return this;
	}
	
}
